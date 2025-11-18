# Automatisierungs-Setup Anleitung

Kurzanleitung zur Einrichtung der automatischen Submission-Strategie.

## Übersicht

Die Automatisierung besteht aus drei GitHub Actions Workflows:

1. **publish-mcp-registry.yml** - Automatische Veröffentlichung in MCP Registry
2. **release-notification.yml** - Erstellt Checkliste für manuelle Submissions
3. **update-server-json.yml** - Synchronisiert server.json Version automatisch

## Einrichtung

### 1. MCP Registry Auto-Publish

**Status**: ✅ Bereit (benötigt nur GitHub OIDC)

**Was passiert automatisch:**
- Bei jedem Release wird der Server automatisch zur MCP Registry publiziert
- `mcp-publisher` verwendet GitHub OIDC für Authentifizierung
- Keine manuellen Secrets nötig

**Erste Veröffentlichung:**
```bash
# Manuell testen (optional):
mcp-publisher login github-oidc
mcp-publisher publish
```

**Bei nächstem Release:**
- Workflow läuft automatisch
- Keine manuelle Intervention nötig

### 2. Release Notification

**Status**: ✅ Aktiv

**Was passiert automatisch:**
- Bei jedem Release wird ein Kommentar/Issue erstellt
- Enthält Checkliste für manuelle Submissions
- Links zu allen Submission-Templates

**Keine Einrichtung nötig** - läuft automatisch

### 3. server.json Auto-Update

**Status**: ✅ Aktiv

**Was passiert automatisch:**
- Wenn `build.gradle` Version geändert wird
- `server.json` wird automatisch aktualisiert
- Commit wird automatisch erstellt

**Keine Einrichtung nötig** - läuft automatisch

## Workflow-Trigger

### Automatische Trigger

| Workflow | Trigger | Beschreibung |
|----------|---------|--------------|
| `publish-mcp-registry` | `release: published` | Bei neuen Releases |
| `release-notification` | `release: published` | Bei neuen Releases |
| `update-server-json` | `push` (build.gradle) | Bei Version-Änderungen |

### Manuelle Trigger

Alle Workflows können auch manuell ausgelöst werden:
- GitHub UI: Actions → Workflow → "Run workflow"
- GitHub CLI: `gh workflow run <workflow-name>`

## Release-Prozess (Automatisiert)

### Schritt 1: Version bumpen

```bash
# In build.gradle ändern:
version = '0.2.5'
```

### Schritt 2: Committen & Pushen

```bash
git add build.gradle CHANGELOG.md
git commit -m "Bump version to 0.2.5"
git push origin main
```

**Automatisch:**
- ✅ `update-server-json.yml` aktualisiert `server.json`
- ✅ Commit wird automatisch erstellt

### Schritt 3: Release erstellen

```bash
git tag -a v0.2.5 -m "Release 0.2.5"
git push origin v0.2.5

# Oder über GitHub UI:
# Releases → Draft a new release → Tag: v0.2.5
```

**Automatisch:**
- ✅ Docker Image wird gebaut und gepusht (`docker.yml`)
- ✅ MCP Registry wird publiziert (`publish-mcp-registry.yml`)
- ✅ Release-Notification wird erstellt (`release-notification.yml`)

### Schritt 4: Manuelle Submissions (Community-Verzeichnisse)

**Automatisch erstellt:**
- ✅ Checkliste in Release-Kommentar/Issue
- ✅ Links zu allen Submission-Templates

**Manuell ausführen:**
- Öffne die Submission-Templates
- Fülle die Formulare in den Community-Verzeichnissen aus

## Verifizierung

### Nach Release

1. **MCP Registry:**
   ```bash
   curl "https://registry.modelcontextprotocol.io/v0/servers?search=io.github.rosch100/mcp-sqlite"
   ```

2. **Docker Image:**
   ```bash
   docker pull ghcr.io/rosch100/mcp-sqlite:0.2.5
   ```

3. **GitHub Actions:**
   - Prüfe Actions-Tab für Workflow-Status
   - Alle Workflows sollten erfolgreich sein

## Troubleshooting

### MCP Registry Publish fehlgeschlagen

**Problem**: GitHub OIDC Authentifizierung fehlgeschlagen

**Lösung**:
1. Prüfe Repository Settings → Actions → General
2. Stelle sicher, dass "Read and write permissions" aktiviert ist
3. Prüfe Workflow-Logs für Details

### server.json Update fehlgeschlagen

**Problem**: Version wurde nicht aktualisiert

**Lösung**:
1. Prüfe ob `build.gradle` geändert wurde
2. Prüfe Workflow-Logs
3. Manuell aktualisieren falls nötig

### Release Notification fehlgeschlagen

**Problem**: Kommentar/Issue wurde nicht erstellt

**Lösung**:
1. Prüfe Repository Permissions
2. Prüfe ob Release korrekt erstellt wurde
3. Workflow kann manuell erneut ausgeführt werden

## Best Practices

1. **Versionierung**: Immer SemVer verwenden (`0.2.5`)
2. **Tagging**: Immer `v` Präfix (`v0.2.5`)
3. **Changelog**: Aktualisiere CHANGELOG.md vor Release
4. **Testing**: Teste Workflows vor Production-Release
5. **Monitoring**: Prüfe Workflow-Status nach Release

## Nächste Schritte

1. ✅ Workflows sind eingerichtet
2. ⏳ Teste bei nächstem Release
3. ⏳ Prüfe MCP Registry nach Veröffentlichung
4. ⏳ Führe manuelle Submissions durch

## Weitere Automatisierung (Zukünftig)

### Webhook-basierte Updates

Falls Community-Verzeichnisse APIs bereitstellen:
- GitHub Webhook konfigurieren
- API-Calls bei Releases
- Automatische Updates

### Monitoring

- Automatische Verifizierung nach Submission
- Status-Checks für alle Verzeichnisse
- Benachrichtigungen bei Fehlern

