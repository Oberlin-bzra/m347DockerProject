// Basis-URL der API. Der nginx-Reverse-Proxy leitet "/api" an den
// "backend"-Container weiter (siehe frontend/nginx.conf), daher genügt
// ein relativer Pfad -- es sind keine CORS-Konfigurationen im Browser nötig.
const API_URL = "/api/fahrzeuge";

const form = document.getElementById("fahrzeug-form");
const tbody = document.getElementById("fahrzeug-tbody");
const idField = document.getElementById("fahrzeug-id");
const formTitle = document.getElementById("form-title");
const submitBtn = document.getElementById("submit-btn");
const cancelBtn = document.getElementById("cancel-btn");
const refreshBtn = document.getElementById("refresh-btn");
const statusBanner = document.getElementById("status-banner");

document.addEventListener("DOMContentLoaded", ladeFahrzeuge);
refreshBtn.addEventListener("click", ladeFahrzeuge);
form.addEventListener("submit", fahrzeugSpeichern);
cancelBtn.addEventListener("click", formZuruecksetzen);

async function ladeFahrzeuge() {
    tbody.innerHTML = `<tr><td colspan="8" class="empty-state">Lade Daten…</td></tr>`;
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Fehler beim Laden der Fahrzeuge");
        const fahrzeuge = await response.json();
        renderTabelle(fahrzeuge);
    } catch (err) {
        zeigeStatus(err.message || "Verbindung zum Server fehlgeschlagen", "error");
        tbody.innerHTML = `<tr><td colspan="8" class="empty-state">Keine Daten verfügbar</td></tr>`;
    }
}

function renderTabelle(fahrzeuge) {
    if (!fahrzeuge || fahrzeuge.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" class="empty-state">Noch keine Fahrzeuge erfasst</td></tr>`;
        return;
    }

    tbody.innerHTML = fahrzeuge.map(f => `
        <tr>
            <td>${escapeHtml(f.kennzeichen)}</td>
            <td>${escapeHtml(f.marke)}</td>
            <td>${escapeHtml(f.modell)}</td>
            <td>${f.baujahr ?? "-"}</td>
            <td>${escapeHtml(f.farbe ?? "-")}</td>
            <td>${f.kilometerstand != null ? f.kilometerstand.toLocaleString("de-CH") + " km" : "-"}</td>
            <td>${statusBadge(f.status)}</td>
            <td class="row-actions">
                <button class="edit-btn" onclick="fahrzeugBearbeiten(${f.id})">Bearbeiten</button>
                <button class="delete-btn" onclick="fahrzeugLoeschen(${f.id})">Löschen</button>
            </td>
        </tr>
    `).join("");
}

function statusBadge(status) {
    const map = {
        VERFUEGBAR: ["verfuegbar", "Verfügbar"],
        VERMIETET: ["vermietet", "Vermietet"],
        IN_WARTUNG: ["in_wartung", "In Wartung"],
    };
    const [cssClass, label] = map[status] || ["verfuegbar", status || "-"];
    return `<span class="badge ${cssClass}">${label}</span>`;
}

async function fahrzeugSpeichern(event) {
    event.preventDefault();

    const payload = {
        kennzeichen: document.getElementById("kennzeichen").value.trim(),
        marke: document.getElementById("marke").value.trim(),
        modell: document.getElementById("modell").value.trim(),
        baujahr: Number(document.getElementById("baujahr").value),
        farbe: document.getElementById("farbe").value.trim(),
        kilometerstand: document.getElementById("kilometerstand").value
            ? Number(document.getElementById("kilometerstand").value)
            : null,
        status: document.getElementById("status").value,
    };

    const id = idField.value;
    const url = id ? `${API_URL}/${id}` : API_URL;
    const method = id ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const fehlerDetails = await response.json().catch(() => ({}));
            const meldung = Object.values(fehlerDetails).join(", ") || "Speichern fehlgeschlagen";
            throw new Error(meldung);
        }

        zeigeStatus(id ? "Fahrzeug aktualisiert." : "Fahrzeug erfasst.", "success");
        formZuruecksetzen();
        ladeFahrzeuge();
    } catch (err) {
        zeigeStatus(err.message, "error");
    }
}

async function fahrzeugBearbeiten(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Fahrzeug konnte nicht geladen werden");
        const f = await response.json();

        idField.value = f.id;
        document.getElementById("kennzeichen").value = f.kennzeichen;
        document.getElementById("marke").value = f.marke;
        document.getElementById("modell").value = f.modell;
        document.getElementById("baujahr").value = f.baujahr;
        document.getElementById("farbe").value = f.farbe ?? "";
        document.getElementById("kilometerstand").value = f.kilometerstand ?? "";
        document.getElementById("status").value = f.status ?? "VERFUEGBAR";

        formTitle.textContent = `Fahrzeug bearbeiten: ${f.kennzeichen}`;
        submitBtn.textContent = "Aktualisieren";
        cancelBtn.hidden = false;
        window.scrollTo({ top: 0, behavior: "smooth" });
    } catch (err) {
        zeigeStatus(err.message, "error");
    }
}

async function fahrzeugLoeschen(id) {
    if (!confirm("Dieses Fahrzeug wirklich löschen?")) return;
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        if (!response.ok && response.status !== 204) throw new Error("Löschen fehlgeschlagen");
        zeigeStatus("Fahrzeug gelöscht.", "success");
        ladeFahrzeuge();
    } catch (err) {
        zeigeStatus(err.message, "error");
    }
}

function formZuruecksetzen() {
    form.reset();
    idField.value = "";
    formTitle.textContent = "Neues Fahrzeug erfassen";
    submitBtn.textContent = "Speichern";
    cancelBtn.hidden = true;
}

function zeigeStatus(nachricht, typ) {
    statusBanner.textContent = nachricht;
    statusBanner.className = `status-banner ${typ}`;
    statusBanner.hidden = false;
    setTimeout(() => { statusBanner.hidden = true; }, 4000);
}

function escapeHtml(value) {
    if (value == null) return "";
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;");
}
