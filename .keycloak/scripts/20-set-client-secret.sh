#!/bin/bash
set -e

echo "Resolving client ID for ${KEYCLOAK_CLIENT}..."
CLIENT_UUID=$(
  /opt/keycloak/bin/kc.sh get clients \
    -r "${KEYCLOAK_REALM}" \
    --query "[?clientId=='${KEYCLOAK_CLIENT}'].id" \
    --output tsv
)

if [ -z "$CLIENT_UUID" ]; then
  echo "Client '${KEYCLOAK_CLIENT}' not found"
  exit 1
fi

echo "Setting client secret..."
/opt/keycloak/bin/kc.sh update "clients/${CLIENT_UUID}/client-secret" \
  -r "${KEYCLOAK_REALM}" \
  -s value="${KEYCLOAK_ADMIN_CLIENT_SECRET}"
