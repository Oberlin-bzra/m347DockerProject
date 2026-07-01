# Fahrzeugverwaltung – Containerisierte Applikation

Einfache Fahrzeugverwaltung als Beispielapplikation für Modul 347
("Eine Applikation mit Containers virtualisieren"). Die Applikation besteht
aus drei Microservices, die über Docker Compose orchestriert werden.

| Service    | Technologie                          | Aufgabe                             |
|------------|---------------------------------------|--------------------------------------|
| `db`       | PostgreSQL 16                        | Persistiert die Fahrzeugdaten        |
| `backend`  | Spring Boot 3 (Java 21)              | REST-API (CRUD) für Fahrzeuge        |
| `frontend` | HTML/CSS/JS, ausgeliefert via nginx  | Web-Oberfläche für die Verwaltung    |

Vollständige Dokumentation (Architektur, Containertechnologien,
Bewertungskriterien): siehe [`docs/Dokumentation.docx`](docs/Dokumentation.docx).

## Voraussetzungen

- Docker Desktop bzw. Docker Engine + Docker Compose Plugin (aktuelle Version)
- Keine lokale Java-, Node- oder PostgreSQL-Installation nötig – alles läuft im Container

## Projekt starten

```bash
# 1. Ins Projektverzeichnis wechseln
cd fahrzeugverwaltung

# 2. Umgebungsvariablen bereitstellen (Vorlage kopieren und bei Bedarf anpassen)
cp .env.example .env

# 3. Applikation bauen und starten
docker compose up --build
```

Danach im Browser öffnen:

```
http://localhost:8081
```

Das Backend wird beim allerersten Start automatisch mit vier
Beispielfahrzeugen befüllt (siehe `DatenInitialisierer.java`), damit die
Oberfläche sofort nutzbar ist.

## Nützliche Befehle

```bash
docker compose ps            # Status aller Services
docker compose logs -f       # Logs aller Services live verfolgen
docker compose logs -f backend
docker compose down          # Stoppen (Datenbank-Volume bleibt erhalten)
docker compose down -v       # Stoppen und Datenbank-Volume löschen
```

## Projektstruktur

```
fahrzeugverwaltung/
├── compose.yaml              # Docker-Compose-Anwendungsmodell (Services, Netzwerke, Volumes)
├── .env                      # Umgebungsvariablen (lokal, nicht eingecheckt)
├── .env.example               # Vorlage für Umgebungsvariablen
├── backend/
│   ├── Dockerfile            # Multi-Stage-Build (Maven -> JRE)
│   ├── pom.xml
│   └── src/main/java/ch/example/fahrzeugverwaltung/
│       ├── FahrzeugverwaltungApplication.java
│       ├── model/Fahrzeug.java
│       ├── repository/FahrzeugRepository.java
│       ├── controller/FahrzeugController.java
│       ├── config/WebConfig.java
│       ├── config/DatenInitialisierer.java
│       └── exception/GlobalExceptionHandler.java
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf            # Statisches Hosting + Reverse-Proxy zu /api
│   └── public/
│       ├── index.html
│       ├── css/style.css
│       └── js/app.js
└── docs/
    └── Dokumentation.docx    # Abschlussarbeit-Dokumentation
```

## API-Endpunkte (Backend)

| Methode | Pfad                  | Beschreibung                  |
|---------|------------------------|--------------------------------|
| GET     | `/api/fahrzeuge`       | Alle Fahrzeuge auflisten       |
| GET     | `/api/fahrzeuge/{id}`  | Einzelnes Fahrzeug abrufen     |
| POST    | `/api/fahrzeuge`       | Neues Fahrzeug anlegen         |
| PUT     | `/api/fahrzeuge/{id}`  | Fahrzeug aktualisieren         |
| DELETE  | `/api/fahrzeuge/{id}`  | Fahrzeug löschen               |
| GET     | `/actuator/health`     | Health-Check                   |

## Architektur

```
Browser ──▶ frontend (nginx, Port 8081) ──▶ backend (Spring Boot, Port 8080) ──▶ db (PostgreSQL, Port 5432)
             frontend-tier Netzwerk           frontend-tier + backend-tier          backend-tier Netzwerk
```

Die Datenbank ist ausschliesslich über das interne `backend-tier`-Netzwerk
erreichbar und hat keinen direkten Kontakt zum Frontend-Netzwerk – das
Backend fungiert als einzige Schnittstelle zur Datenbank.