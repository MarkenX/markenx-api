SHELL := sh

.PHONY: dev dev-up dev-bootstrap dev-down

dev: dev-up dev-bootstrap
	@echo "DEV listo: Keycloak + MySQL arriba y Keycloak configurado."

dev-up:
	docker compose --env-file .env up -d

dev-bootstrap:
	docker compose --env-file .env --profile bootstrap run --rm keycloak-ansible

dev-down:
	docker compose --env-file .env down -v
