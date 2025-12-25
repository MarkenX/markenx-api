#!/bin/bash
set -e

export KEYCLOAK_BASE_URL="${KEYCLOAK_SCHEME}://${KEYCLOAK_HOST}:${KEYCLOAK_PORT}"

./scripts/00-wait-for-keycloak.sh
./scripts/10-create-realm-admin.sh
./scripts/20-set-client-secret.sh

echo "Keycloak fully initialized"
