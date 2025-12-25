#!/bin/bash
set -e

echo "Logging into Keycloak (master realm)..."
/opt/keycloak/bin/kc.sh config credentials \
  --server "${KEYCLOAK_BASE_URL}" \
  --realm master \
  --user "${KEYCLOAK_ADMIN}" \
  --password "${KEYCLOAK_ADMIN_PASSWORD}"

echo "Creating realm admin user..."

USER_EXISTS=$(
  /opt/keycloak/bin/kc.sh get users \
    -r "${KEYCLOAK_REALM}" \
    -q username="${KEYCLOAK_REALM_ADMIN_USERNAME}" \
    --fields id \
    --output tsv
)

if [ -z "$USER_EXISTS" ]; then
  /opt/keycloak/bin/kc.sh create users \
    -r "${KEYCLOAK_REALM}" \
    -s username="${KEYCLOAK_REALM_ADMIN_USERNAME}" \
    -s email="${KEYCLOAK_REALM_ADMIN_EMAIL}" \
    -s enabled=true \
    -s emailVerified=true \
    -s firstName=John \
    -s lastName=Doe \
    -s credentials='[{"type":"password","value":"'"${KEYCLOAK_REALM_ADMIN_PASSWORD}"'","temporary":false}]'
else
  echo "ℹAdmin user already exists"
fi

echo "Assigning ADMIN role..."
/opt/keycloak/bin/kc.sh add-roles \
  -r "${KEYCLOAK_REALM}" \
  --uusername "${KEYCLOAK_REALM_ADMIN_USERNAME}" \
  --rolename ADMIN
