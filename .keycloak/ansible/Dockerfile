FROM python:3.12-slim

# Paquetes mínimos
RUN apt-get update && apt-get install -y --no-install-recommends \
    sshpass \
    && rm -rf /var/lib/apt/lists/*

# Ansible
RUN pip install --no-cache-dir ansible==10.6.0

WORKDIR /work
