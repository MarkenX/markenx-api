#!/bin/bash
set -e

REALM="${KEYCLOAK_REALM}"
CLIENT_ID="${KEYCLOAK_CLIENT}"
OUTPUT_DIR="/opt/keycloak/data/secrets"

mkdir -p "$OUTPUT_DIR"

# Create confidential client if not exists
CLIENT_UUID=$(/opt/keycloak/bin/kcadm.sh get clients -r "$REALM" -q clientId="$CLIENT_ID" \
    | grep '"id"' | head -n1 | sed 's/.*"id" : "\(.*\)".*/\1/' 2>/dev/null || true)

if [ -z "$CLIENT_UUID" ]; then
    echo "[INFO] Creating client: $CLIENT_ID"
    /opt/keycloak/bin/kcadm.sh create clients -r "$REALM" \
        -s clientId="$CLIENT_ID" \
        -s enabled=true \
        -s serviceAccountsEnabled=true \
        -s publicClient=false \
        -s clientAuthenticatorType="client-secret"

    CLIENT_UUID=$(/opt/keycloak/bin/kcadm.sh get clients -r "$REALM" -q clientId="$CLIENT_ID" \
        | grep '"id"' | head -n1 | sed 's/.*"id" : "\(.*\)".*/\1/')
    echo "[SUCCESS] Client created with UUID: $CLIENT_UUID"
else
    echo "[INFO] Client already exists: $CLIENT_ID"
fi

# Export client secret for API use
CLIENT_SECRET=$(/opt/keycloak/bin/kcadm.sh get "clients/$CLIENT_UUID/client-secret" -r "$REALM" \
    | grep '"value"' | sed 's/.*"value" : "\(.*\)".*/\1/')
echo "$CLIENT_SECRET" > "$OUTPUT_DIR/client-secret.txt"
echo "[SUCCESS] Client secret saved to $OUTPUT_DIR/client-secret.txt"