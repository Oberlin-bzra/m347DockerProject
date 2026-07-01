# syntax=docker/dockerfile:1

FROM nginx:1.27-alpine

# Eigene nginx-Konfiguration: statisches Hosting + Reverse-Proxy fuer /api
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Statische Frontend-Dateien (HTML/CSS/JS)
COPY public /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=5 \
    CMD wget -q -O- http://localhost:80/ || exit 1
