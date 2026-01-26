# Integracion Unity WebGL + React con Token Temporal

Este documento describe la arquitectura de autenticacion y los endpoints necesarios para la comunicacion entre el videojuego Unity WebGL y el BFF (Backend For Frontend).

---

## Arquitectura General

```
+------------------+                      +------------------+
|   React App      |  1. POST /auth/game-token              |
|   (Navegador)    | -------------------> |      BFF         |
+------------------+                      |  (Spring Boot)   |
        |                                 |  Puerto: 8082    |
        | 2. Pasa gameToken via URL       +------------------+
        v                                          ^
+------------------+                               |
|   Unity WebGL    |  3. Authorization: Bearer    |
|   (iframe)       | ----------------------------->
+------------------+
```

---

## Endpoints Requeridos por el Videojuego

### 1. Cargar Escenario

| Campo | Valor |
|-------|-------|
| **Endpoint** | `GET /api/v1/scenarios/{scenarioId}` |
| **Autenticacion** | `Authorization: Bearer <gameToken>` |
| **Respuesta** | `ScenarioDetailResponse` |

**Response Body:**
```json
{
  "id": "uuid",
  "title": "Nombre del escenario",
  "description": "Descripcion del escenario",
  "consumer": {
    "id": "uuid",
    "name": "Juan Perez",
    "age": 35,
    "budget": 5000.00,
    "targetAcceptanceScore": 75.0
  },
  "dimensions": [
    {
      "id": "uuid",
      "name": "price",
      "displayName": "Precio",
      "description": "Sensibilidad al precio",
      "consumerExpectation": 80.0,
      "productInitialOffer": 50.0
    }
  ],
  "actions": [
    {
      "id": "uuid",
      "name": "discount_10",
      "description": "Aplicar 10% de descuento",
      "cost": 100.00,
      "category": "PRICING",
      "isInitiallyLocked": false,
      "prerequisiteActionId": null,
      "effects": [
        {
          "dimensionId": "uuid",
          "impactValue": 15.0
        }
      ]
    }
  ],
  "events": [
    {
      "id": "uuid",
      "title": "Crisis economica",
      "description": "El mercado sufre una recesion",
      "effects": [
        {
          "dimensionId": "uuid",
          "impactValue": -10.0
        }
      ]
    }
  ]
}
```

---

### 2. Registrar Intento (Resultado de Partida)

| Campo | Valor |
|-------|-------|
| **Endpoint** | `POST /api/v1/attempts` |
| **Autenticacion** | `Authorization: Bearer <gameToken>` |
| **Request Body** | `RegisterGameSessionRequestDTO` |
| **Respuesta** | `GameSessionResponseDTO` (201 Created) |

**Request Body:**
```json
{
  "taskId": "uuid-de-la-tarea",
  "studentId": "uuid-del-estudiante",
  "sessionDate": "2026-01-22T15:30:00",
  "finalAcceptance": 85.5,
  "remainingBudget": 2500.00,
  "totalTurnsUsed": 12,
  "profileDiscoveryPercentage": 67.5,
  "history": [
    {
      "turnNumber": 1,
      "acceptanceAtEnd": 55.0,
      "budgetAtEnd": 4800.00,
      "eventOccurredTitle": null,
      "actionsTakenIds": ["action-uuid-1"]
    },
    {
      "turnNumber": 2,
      "acceptanceAtEnd": 62.0,
      "budgetAtEnd": 4500.00,
      "eventOccurredTitle": "Crisis economica",
      "actionsTakenIds": ["action-uuid-2", "action-uuid-3"]
    }
  ]
}
```

**Response Body:**
```json
{
  "id": "uuid-del-intento",
  "taskId": "uuid-de-la-tarea",
  "studentId": "uuid-del-estudiante",
  "sessionDate": "2026-01-22T15:30:00",
  "finalAcceptance": 85.5,
  "remainingBudget": 2500.00,
  "totalTurnsUsed": 12,
  "profileDiscoveryPercentage": 67.5,
  "finalOutcome": "WIN",
  "history": [...]
}
```

---

### 3. Obtener Token de Juego (NUEVO - Por Implementar)

| Campo | Valor |
|-------|-------|
| **Endpoint** | `POST /api/v1/auth/game-token` |
| **Autenticacion** | Cookie `JSESSIONID` (sesion de React) |
| **Respuesta** | `GameTokenResponse` |

**Response Body:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 600
}
```

---

### 4. Refrescar Token de Juego (NUEVO - Por Implementar)

| Campo | Valor |
|-------|-------|
| **Endpoint** | `POST /api/v1/auth/game-token/refresh` |
| **Autenticacion** | `Authorization: Bearer <gameToken>` (token actual, aunque este por expirar) |
| **Respuesta** | `GameTokenResponse` |

**Response Body:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...(nuevo)",
  "expiresIn": 600
}
```

**Nota:** Este endpoint permite extender la sesion si el usuario todavia no ha terminado la partida. El token actual puede estar proximo a expirar pero aun ser valido.

---

## Implementacion en React

### 1. Servicio de Game Token

**Archivo:** `src/services/gameTokenService.ts`

```typescript
import { apiClient } from './apiClient';

interface GameTokenResponse {
  token: string;
  expiresIn: number; // segundos
}

export const gameTokenService = {
  /**
   * Obtiene un token temporal para el juego Unity.
   * Requiere sesion activa (cookie JSESSIONID).
   */
  getGameToken: async (): Promise<GameTokenResponse> => {
    return apiClient.request<GameTokenResponse>(
      '/auth/game-token',
      { method: 'POST' }
    );
  },

  /**
   * Refresca el token del juego antes de que expire.
   * Usado por Unity via postMessage.
   */
  refreshGameToken: async (currentToken: string): Promise<GameTokenResponse> => {
    return apiClient.request<GameTokenResponse>(
      '/auth/game-token/refresh',
      {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${currentToken}`
        }
      }
    );
  },
};
```

### 2. Modificacion de GamePage.tsx

```typescript
import { useEffect, useState, useMemo, useCallback, useRef } from 'react';
import { gameTokenService } from '../services/gameTokenService';

const GamePage: React.FC = () => {
  const { user } = useSession();
  const { taskId } = useParams();
  const [gameToken, setGameToken] = useState<string | null>(null);
  const [tokenExpiresAt, setTokenExpiresAt] = useState<number>(0);
  const [error, setError] = useState<string | null>(null);
  const iframeRef = useRef<HTMLIFrameElement>(null);

  // Obtener token inicial
  useEffect(() => {
    const fetchToken = async () => {
      try {
        const response = await gameTokenService.getGameToken();
        setGameToken(response.token);
        setTokenExpiresAt(Date.now() + response.expiresIn * 1000);
      } catch (err) {
        console.error('Error obteniendo game token:', err);
        setError('No se pudo obtener autorizacion para el juego');
      }
    };
    fetchToken();
  }, []);

  // Escuchar mensajes de Unity para refrescar token
  useEffect(() => {
    const handleMessage = async (event: MessageEvent) => {
      if (event.data.type === 'REFRESH_TOKEN_REQUEST') {
        try {
          const response = await gameTokenService.refreshGameToken(gameToken!);
          setGameToken(response.token);
          setTokenExpiresAt(Date.now() + response.expiresIn * 1000);

          // Enviar nuevo token a Unity
          iframeRef.current?.contentWindow?.postMessage({
            type: 'REFRESH_TOKEN_RESPONSE',
            token: response.token,
            expiresIn: response.expiresIn
          }, '*');
        } catch (err) {
          console.error('Error refrescando token:', err);
          iframeRef.current?.contentWindow?.postMessage({
            type: 'REFRESH_TOKEN_ERROR',
            error: 'No se pudo refrescar el token'
          }, '*');
        }
      }
    };

    window.addEventListener('message', handleMessage);
    return () => window.removeEventListener('message', handleMessage);
  }, [gameToken]);

  // Construir URL del juego
  const gameUrl = useMemo(() => {
    if (!taskId || !user?.id || !gameToken) return null;

    const params = new URLSearchParams();
    params.set('scenarioId', task?.scenarioId || taskId);
    params.set('studentId', user.id);
    params.set('taskId', taskId);
    params.set('apiUrl', import.meta.env.VITE_API_BASE_URL.replace('/api/v1', ''));
    params.set('gameToken', gameToken);
    params.set('tokenExpiresIn', String(Math.floor((tokenExpiresAt - Date.now()) / 1000)));

    return `${import.meta.env.VITE_GAME_URL}?${params.toString()}`;
  }, [taskId, user?.id, task?.scenarioId, gameToken, tokenExpiresAt]);

  if (error) {
    return <div className="error">{error}</div>;
  }

  if (!gameUrl) {
    return <div className="loading">Cargando juego...</div>;
  }

  return (
    <iframe
      ref={iframeRef}
      src={gameUrl}
      title="MarkenX Game"
      className="game-iframe"
    />
  );
};
```

---

## Implementacion en Unity (C#)

### 1. Configuracion API

**Archivo:** `ApiConfig.cs`

```csharp
using UnityEngine;
using System;
using System.Runtime.InteropServices;

public class ApiConfig : MonoBehaviour
{
    public static ApiConfig Instance { get; private set; }

    private string _apiUrl;
    private string _scenarioId;
    private string _studentId;
    private string _taskId;
    private string _gameToken;
    private int _tokenExpiresIn;
    private DateTime _tokenExpiresAt;

    public string ApiUrl => _apiUrl;
    public string ScenarioId => _scenarioId;
    public string StudentId => _studentId;
    public string TaskId => _taskId;
    public string GameToken => _gameToken;
    public bool IsTokenExpiringSoon => DateTime.Now > _tokenExpiresAt.AddMinutes(-2);

    // Para comunicacion con JavaScript
    [DllImport("__Internal")]
    private static extern void RequestTokenRefresh();

    void Awake()
    {
        if (Instance == null)
        {
            Instance = this;
            DontDestroyOnLoad(gameObject);
            LoadFromQueryParams();
        }
        else
        {
            Destroy(gameObject);
        }
    }

    private void LoadFromQueryParams()
    {
        string url = Application.absoluteURL;

        _apiUrl = GetQueryParam(url, "apiUrl") ?? "http://localhost:8082";
        _scenarioId = GetQueryParam(url, "scenarioId");
        _studentId = GetQueryParam(url, "studentId");
        _taskId = GetQueryParam(url, "taskId");
        _gameToken = GetQueryParam(url, "gameToken");

        string expiresIn = GetQueryParam(url, "tokenExpiresIn");
        _tokenExpiresIn = string.IsNullOrEmpty(expiresIn) ? 600 : int.Parse(expiresIn);
        _tokenExpiresAt = DateTime.Now.AddSeconds(_tokenExpiresIn);

        Debug.Log($"[ApiConfig] Configuracion cargada:");
        Debug.Log($"  - API URL: {_apiUrl}");
        Debug.Log($"  - Scenario ID: {_scenarioId}");
        Debug.Log($"  - Student ID: {_studentId}");
        Debug.Log($"  - Task ID: {_taskId}");
        Debug.Log($"  - Token expira en: {_tokenExpiresIn}s");
    }

    private string GetQueryParam(string url, string param)
    {
        if (string.IsNullOrEmpty(url) || !url.Contains("?"))
            return null;

        string query = url.Substring(url.IndexOf('?') + 1);
        string[] pairs = query.Split('&');

        foreach (string pair in pairs)
        {
            string[] keyValue = pair.Split('=');
            if (keyValue.Length == 2 && keyValue[0] == param)
            {
                return Uri.UnescapeDataString(keyValue[1]);
            }
        }
        return null;
    }

    /// <summary>
    /// Solicita a React que refresque el token.
    /// Llamar cuando el token esta por expirar.
    /// </summary>
    public void RequestTokenRefreshFromReact()
    {
        #if UNITY_WEBGL && !UNITY_EDITOR
        RequestTokenRefresh();
        #else
        Debug.Log("[ApiConfig] Token refresh solicitado (modo editor)");
        #endif
    }

    /// <summary>
    /// Llamado desde JavaScript cuando React proporciona nuevo token.
    /// </summary>
    public void OnTokenRefreshed(string newToken, int expiresIn)
    {
        _gameToken = newToken;
        _tokenExpiresIn = expiresIn;
        _tokenExpiresAt = DateTime.Now.AddSeconds(expiresIn);
        Debug.Log($"[ApiConfig] Token refrescado. Nuevo expira en: {expiresIn}s");
    }
}
```

### 2. Plugin JavaScript para WebGL

**Archivo:** `Plugins/WebGL/TokenBridge.jslib`

```javascript
mergeInto(LibraryManager.library, {
    RequestTokenRefresh: function() {
        // Enviar mensaje a React para solicitar refresh
        window.parent.postMessage({ type: 'REFRESH_TOKEN_REQUEST' }, '*');
    }
});
```

### 3. Listener de Mensajes (en index.html del build WebGL)

Agregar en el `<head>` del `index.html` generado por Unity:

```html
<script>
    window.addEventListener('message', function(event) {
        if (event.data.type === 'REFRESH_TOKEN_RESPONSE') {
            // Llamar a Unity con el nuevo token
            if (window.unityInstance) {
                window.unityInstance.SendMessage(
                    'ApiConfig',
                    'OnTokenRefreshed',
                    JSON.stringify({
                        token: event.data.token,
                        expiresIn: event.data.expiresIn
                    })
                );
            }
        } else if (event.data.type === 'REFRESH_TOKEN_ERROR') {
            console.error('Error refrescando token:', event.data.error);
        }
    });
</script>
```

### 4. Servicio Base de API

**Archivo:** `BaseApiService.cs`

```csharp
using UnityEngine;
using UnityEngine.Networking;
using System;
using System.Collections;
using System.Text;

public abstract class BaseApiService : MonoBehaviour
{
    protected string BaseUrl => $"{ApiConfig.Instance.ApiUrl}/api/v1";

    protected IEnumerator Get<T>(string endpoint, Action<T> onSuccess, Action<string> onError)
    {
        // Verificar si el token esta por expirar
        if (ApiConfig.Instance.IsTokenExpiringSoon)
        {
            ApiConfig.Instance.RequestTokenRefreshFromReact();
            // Esperar un poco para que llegue el nuevo token
            yield return new WaitForSeconds(1f);
        }

        string url = $"{BaseUrl}{endpoint}";

        using (UnityWebRequest request = UnityWebRequest.Get(url))
        {
            AddAuthHeader(request);
            request.SetRequestHeader("Content-Type", "application/json");

            yield return request.SendWebRequest();

            HandleResponse(request, onSuccess, onError);
        }
    }

    protected IEnumerator Post<TRequest, TResponse>(
        string endpoint,
        TRequest body,
        Action<TResponse> onSuccess,
        Action<string> onError)
    {
        // Verificar si el token esta por expirar
        if (ApiConfig.Instance.IsTokenExpiringSoon)
        {
            ApiConfig.Instance.RequestTokenRefreshFromReact();
            yield return new WaitForSeconds(1f);
        }

        string url = $"{BaseUrl}{endpoint}";
        string jsonBody = JsonUtility.ToJson(body);

        using (UnityWebRequest request = new UnityWebRequest(url, "POST"))
        {
            byte[] bodyRaw = Encoding.UTF8.GetBytes(jsonBody);
            request.uploadHandler = new UploadHandlerRaw(bodyRaw);
            request.downloadHandler = new DownloadHandlerBuffer();

            AddAuthHeader(request);
            request.SetRequestHeader("Content-Type", "application/json");

            yield return request.SendWebRequest();

            HandleResponse(request, onSuccess, onError);
        }
    }

    private void AddAuthHeader(UnityWebRequest request)
    {
        string token = ApiConfig.Instance.GameToken;
        if (!string.IsNullOrEmpty(token))
        {
            request.SetRequestHeader("Authorization", $"Bearer {token}");
        }
    }

    private void HandleResponse<T>(
        UnityWebRequest request,
        Action<T> onSuccess,
        Action<string> onError)
    {
        if (request.result == UnityWebRequest.Result.Success)
        {
            try
            {
                T response = JsonUtility.FromJson<T>(request.downloadHandler.text);
                onSuccess?.Invoke(response);
            }
            catch (Exception ex)
            {
                onError?.Invoke($"Error parsing response: {ex.Message}");
            }
        }
        else
        {
            string errorMsg = $"HTTP {request.responseCode}: {request.error}";
            Debug.LogError($"[API] {request.url} - {errorMsg}");
            onError?.Invoke(errorMsg);
        }
    }
}
```

### 5. Servicio de Escenarios

**Archivo:** `ScenarioApiService.cs`

```csharp
using System;
using System.Collections;

public class ScenarioApiService : BaseApiService
{
    public static ScenarioApiService Instance { get; private set; }

    void Awake()
    {
        Instance = this;
    }

    public void LoadScenario(string scenarioId, Action<ScenarioResponse> onSuccess, Action<string> onError)
    {
        StartCoroutine(Get<ScenarioResponse>($"/scenarios/{scenarioId}", onSuccess, onError));
    }
}

// DTOs
[Serializable]
public class ScenarioResponse
{
    public string id;
    public string title;
    public string description;
    public ConsumerResponse consumer;
    public DimensionResponse[] dimensions;
    public ActionResponse[] actions;
    public EventResponse[] events;
}

[Serializable]
public class ConsumerResponse
{
    public string id;
    public string name;
    public int age;
    public float budget;
    public float targetAcceptanceScore;
}

[Serializable]
public class DimensionResponse
{
    public string id;
    public string name;
    public string displayName;
    public string description;
    public float consumerExpectation;
    public float productInitialOffer;
}

[Serializable]
public class ActionResponse
{
    public string id;
    public string name;
    public string description;
    public float cost;
    public string category;
    public bool isInitiallyLocked;
    public string prerequisiteActionId;
    public ActionEffectResponse[] effects;
}

[Serializable]
public class ActionEffectResponse
{
    public string dimensionId;
    public float impactValue;
}

[Serializable]
public class EventResponse
{
    public string id;
    public string title;
    public string description;
    public EventEffectResponse[] effects;
}

[Serializable]
public class EventEffectResponse
{
    public string dimensionId;
    public float impactValue;
}
```

### 6. Servicio de Intentos

**Archivo:** `AttemptApiService.cs`

```csharp
using System;
using System.Collections;
using System.Collections.Generic;

public class AttemptApiService : BaseApiService
{
    public static AttemptApiService Instance { get; private set; }

    void Awake()
    {
        Instance = this;
    }

    public void RegisterAttempt(AttemptRequest request, Action<AttemptResponse> onSuccess, Action<string> onError)
    {
        StartCoroutine(Post<AttemptRequest, AttemptResponse>("/attempts", request, onSuccess, onError));
    }
}

// DTOs
[Serializable]
public class AttemptRequest
{
    public string taskId;
    public string studentId;
    public string sessionDate; // ISO 8601 format
    public float finalAcceptance;
    public float remainingBudget;
    public int totalTurnsUsed;
    public float profileDiscoveryPercentage;
    public List<TurnHistoryRequest> history;
}

[Serializable]
public class TurnHistoryRequest
{
    public int turnNumber;
    public float acceptanceAtEnd;
    public float budgetAtEnd;
    public string eventOccurredTitle;
    public List<string> actionsTakenIds;
}

[Serializable]
public class AttemptResponse
{
    public string id;
    public string taskId;
    public string studentId;
    public string sessionDate;
    public float finalAcceptance;
    public float remainingBudget;
    public int totalTurnsUsed;
    public float profileDiscoveryPercentage;
    public string finalOutcome;
}
```

---

## Implementacion en Backend (Spring Boot)

### Endpoints de Game Token

| Endpoint | Metodo | Descripcion | Estado |
|----------|--------|-------------|--------|
| `POST /api/v1/auth/game-token` | POST | Genera token JWT temporal | **IMPLEMENTADO** |
| `POST /api/v1/auth/game-token/refresh` | POST | Refresca token existente | **IMPLEMENTADO** |

### 1. GameTokenController

```java
package com.udla.markenx.api.security.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class GameTokenController {

    private final GameTokenService gameTokenService;

    public GameTokenController(GameTokenService gameTokenService) {
        this.gameTokenService = gameTokenService;
    }

    /**
     * Genera un token temporal para el juego Unity.
     * Requiere sesion activa via cookie JSESSIONID.
     */
    @PostMapping("/game-token")
    public ResponseEntity<GameTokenResponse> generateGameToken(
            @AuthenticationPrincipal OAuth2User user) {

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        GameTokenResponse response = gameTokenService.generateToken(user);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresca un token existente antes de que expire.
     * Permite extender la sesion si el usuario sigue jugando.
     */
    @PostMapping("/game-token/refresh")
    public ResponseEntity<GameTokenResponse> refreshGameToken(
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String currentToken = authHeader.substring(7);

        try {
            GameTokenResponse response = gameTokenService.refreshToken(currentToken);
            return ResponseEntity.ok(response);
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
```

### 2. GameTokenService

```java
package com.udla.markenx.api.security.application;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Service
public class GameTokenService {

    private final SecretKey secretKey;
    private final long tokenDurationSeconds;

    public GameTokenService(
            @Value("${app.game-token.secret}") String secret,
            @Value("${app.game-token.duration-seconds:600}") long durationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenDurationSeconds = durationSeconds;
    }

    public GameTokenResponse generateToken(OAuth2User user) {
        String username = user.getAttribute("preferred_username");
        String email = user.getAttribute("email");
        List<String> roles = extractRoles(user);

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(tokenDurationSeconds);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return new GameTokenResponse(token, tokenDurationSeconds);
    }

    public GameTokenResponse refreshToken(String currentToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(currentToken)
                    .getBody();

            // Verificar que el token no haya expirado hace mas de 5 minutos
            // (permite refresh de tokens recien expirados)
            Date expiration = claims.getExpiration();
            long expiredAgo = System.currentTimeMillis() - expiration.getTime();
            if (expiredAgo > 5 * 60 * 1000) {
                throw new InvalidTokenException("Token expirado hace mas de 5 minutos");
            }

            // Generar nuevo token con los mismos claims
            Instant now = Instant.now();
            Instant newExpiration = now.plusSeconds(tokenDurationSeconds);

            String newToken = Jwts.builder()
                    .setSubject(claims.getSubject())
                    .claim("email", claims.get("email"))
                    .claim("roles", claims.get("roles"))
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(newExpiration))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();

            return new GameTokenResponse(newToken, tokenDurationSeconds);

        } catch (JwtException e) {
            throw new InvalidTokenException("Token invalido: " + e.getMessage());
        }
    }

    private List<String> extractRoles(OAuth2User user) {
        // Extraer roles de realm_access.roles (Keycloak)
        Map<String, Object> realmAccess = user.getAttribute("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (List<String>) realmAccess.get("roles");
        }
        return Collections.emptyList();
    }
}
```

### 3. GameTokenResponse DTO

```java
package com.udla.markenx.api.security.infrastructure.web;

public record GameTokenResponse(String token, long expiresIn) {}
```

### 4. Configuracion de Seguridad (Actualizar DevSecurityConfig)

Agregar soporte para autenticacion via Bearer token ademas de cookie:

```java
// En DevSecurityConfig.java, agregar:
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        // ... configuracion existente ...
        .addFilterBefore(
            new GameTokenAuthenticationFilter(gameTokenService),
            UsernamePasswordAuthenticationFilter.class
        )
        .build();
}
```

### 5. GameTokenAuthenticationFilter

```java
package com.udla.markenx.api.security.infrastructure.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GameTokenAuthenticationFilter extends OncePerRequestFilter {

    private final GameTokenService gameTokenService;

    public GameTokenAuthenticationFilter(GameTokenService gameTokenService) {
        this.gameTokenService = gameTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Solo procesar si hay Bearer token y no hay autenticacion previa
        if (authHeader != null &&
            authHeader.startsWith("Bearer ") &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                String token = authHeader.substring(7);
                Claims claims = gameTokenService.validateAndGetClaims(token);

                List<String> roles = (List<String>) claims.get("roles");
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                claims.getSubject(),
                                null,
                                authorities
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Token invalido, continuar sin autenticacion
                // Spring Security rechazara el request si es necesario
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

### 6. Configuracion en application.yml

```yaml
app:
  game-token:
    # IMPORTANTE: Cambiar en produccion!
    secret: "clave-secreta-de-al-menos-32-caracteres-para-HS256"
    duration-seconds: 600  # 10 minutos
```

---

## Flujo Completo

```
1. Usuario navega a /game/:taskId en React
          |
          v
2. React llama POST /auth/game-token (con cookie JSESSIONID)
          |
          v
3. BFF valida sesion y genera JWT temporal (10 min)
          |
          v
4. React construye URL del iframe:
   /game/index.html?scenarioId=X&studentId=Y&taskId=Z&apiUrl=...&gameToken=JWT
          |
          v
5. Unity WebGL se carga en iframe
          |
          v
6. Unity extrae parametros de URL y guarda gameToken
          |
          v
7. Unity llama GET /scenarios/{id} con header Authorization: Bearer JWT
          |
          v
8. BFF valida JWT y retorna escenario
          |
          v
9. Usuario juega... (si pasan ~8 minutos)
          |
          v
10. Unity detecta token por expirar, envia postMessage a React
          |
          v
11. React llama POST /auth/game-token/refresh con JWT actual
          |
          v
12. BFF genera nuevo JWT y React lo envia a Unity via postMessage
          |
          v
13. Usuario termina partida
          |
          v
14. Unity llama POST /attempts con datos de la sesion
          |
          v
15. BFF registra el intento y retorna confirmacion
```

---

## Checklist de Implementacion

### Backend (Spring Boot)
- [x] Crear `GameTokenService` con generacion y validacion JWT
- [x] Crear `GameTokenController` con endpoints `/game-token` y `/game-token/refresh`
- [x] Crear `GameTokenAuthenticationFilter`
- [x] Agregar filtro a `DevSecurityConfig`
- [x] Agregar configuracion en `application-dev.yml`
- [ ] Tests unitarios para `GameTokenService`

### Frontend (React)
- [ ] Crear `gameTokenService.ts`
- [ ] Modificar `GamePage.tsx` para obtener token
- [ ] Agregar listener de postMessage para refresh
- [ ] Manejar errores de token

### Videojuego (Unity)
- [ ] Crear/actualizar `ApiConfig.cs` con manejo de token
- [ ] Crear `TokenBridge.jslib` para comunicacion JS
- [ ] Actualizar `BaseApiService.cs` con header Authorization
- [ ] Crear `ScenarioApiService.cs`
- [ ] Crear `AttemptApiService.cs`
- [ ] Crear DTOs para request/response
- [ ] Agregar script en `index.html` para recibir tokens

### Testing
- [ ] Verificar flujo completo en desarrollo
- [ ] Probar expiracion y refresh de token
- [ ] Verificar requests en Network tab del navegador
- [ ] Probar manejo de errores (token invalido, expirado, etc.)

---

*Documento generado: Enero 2026*
*Proyecto: MarkenX - Integracion Unity + React*
