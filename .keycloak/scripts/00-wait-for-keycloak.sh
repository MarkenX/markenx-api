#!/bin/bash
set -e

KEYCLOAK_BASE_URL="${KEYCLOAK_SCHEME}://${KEYCLOAK_HOST}:${KEYCLOAK_PORT}"

echo "Waiting for Keycloak at ${KEYCLOAK_BASE_URL}..."
until curl -sf "${KEYCLOAK_BASE_URL}/health/ready"; do
  sleep 2
done
