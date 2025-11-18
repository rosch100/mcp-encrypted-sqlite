# MCPList.ai Submission Information

Alle Informationen für die Submission bei MCPList.ai

## ⚠️ Wichtig: Kein Submission-Formular verfügbar

MCPList.ai bietet **kein öffentliches Submission-Formular** auf der Website. Die Submission erfolgt per **E-Mail**.

## Quick Submission Guide

1. **Kontaktiere MCPList.ai per E-Mail**
   - E-Mail-Adresse: [email protected] (oder über Kontakt-Seite)
   - Betreff: "MCP Server Submission: Encrypted SQLite MCP Server"
   
2. **Verwende die folgende E-Mail-Vorlage** mit allen Informationen:

---

## Server Details

### Name
```
Encrypted SQLite MCP Server
```

### GitHub Repository
```
https://github.com/rosch100/mcp-sqlite
```

### Beschreibung (Short)
```
MCP server for SQLCipher 4 encrypted SQLite databases with full CRUD operations
```

### Beschreibung (Long)
```
MCP server for SQLCipher 4 encrypted SQLite databases. Provides comprehensive tools for database exploration, querying, and CRUD operations. Features include encrypted passphrase support with macOS Keychain integration, configurable cipher profiles, and full SQL query support.
```

### Kategorien/Tags
- Database
- Encryption
- SQLite
- CRUD Operations
- Java
- Docker

### Features
- SQLCipher 4 support
- Database exploration
- Query support
- CRUD operations
- Configurable cipher profiles
- Encrypted passphrase support
- macOS Keychain integration

---

## Installation

### Docker (Empfohlen)
```bash
docker pull ghcr.io/rosch100/mcp-sqlite:latest
```

### Source Build
```bash
git clone https://github.com/rosch100/mcp-sqlite.git
cd mcp-sqlite
./gradlew build
./gradlew installDist
```

---

## Configuration Example

### Docker (Cursor/Claude Desktop)
```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "docker",
      "args": [
        "run",
        "--rm",
        "-i",
        "-v",
        "/path/to/your/database.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-sqlite:latest",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

### Source Build
```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-sqlite/build/install/mcp-sqlite/bin/mcp-sqlite",
      "args": [
        "--args",
        "{\"db_path\":\"/path/to/your/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

---

## Tools Provided

1. **list_tables** - List all tables in the database with their columns and metadata
2. **get_table_data** - Read data from a table with optional filtering, column selection, and pagination
3. **exec_query** - Execute arbitrary SQL statements (SELECT, INSERT, UPDATE, DELETE, DDL)
4. **insert_or_update** - Perform UPSERT operations (INSERT or UPDATE on conflict)
5. **delete_rows** - Delete rows from a table based on filters
6. **get_table_schema** - Retrieve detailed schema information (columns, indexes, foreign keys)
7. **list_indexes** - List all indexes for a given table

---

## Documentation Links

- **Main README**: https://github.com/rosch100/mcp-sqlite/blob/main/README.md
- **Docker Quickstart**: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_QUICKSTART.md
- **Docker Configuration**: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_CONFIGURATION.md
- **Passphrase Encryption**: https://github.com/rosch100/mcp-sqlite/blob/main/PASSPHRASE_ENCRYPTION.md

---

## Version
```
0.2.4
```

## License
```
Apache-2.0
```

## Language
```
Java
```

## Runtime
```
Java 21+
```

## Transport
```
stdio
```

---

## Contact Information

- **GitHub**: https://github.com/rosch100/mcp-sqlite
- **Issues**: https://github.com/rosch100/mcp-sqlite/issues
- **Releases**: https://github.com/rosch100/mcp-sqlite/releases

---

## E-Mail-Vorlage für Submission

```
Betreff: MCP Server Submission: Encrypted SQLite MCP Server

Hallo MCPList.ai Team,

ich möchte meinen MCP Server zur Aufnahme in Ihr Verzeichnis vorschlagen:

Server Name: Encrypted SQLite MCP Server
GitHub Repository: https://github.com/rosch100/mcp-sqlite
Version: 0.2.4

Beschreibung:
MCP server for SQLCipher 4 encrypted SQLite databases. Provides comprehensive tools for database exploration, querying, and CRUD operations. Features include encrypted passphrase support with macOS Keychain integration, configurable cipher profiles, and full SQL query support.

Kategorien:
- Database
- Encryption
- SQLite
- CRUD Operations

Features:
- SQLCipher 4 support
- Database exploration
- Query support
- CRUD operations
- Configurable cipher profiles
- Encrypted passphrase support

Installation:
docker pull ghcr.io/rosch100/mcp-sqlite:latest

Dokumentation:
- README: https://github.com/rosch100/mcp-sqlite/blob/main/README.md
- Docker Quickstart: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_QUICKSTART.md

Konfiguration:
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "docker",
      "args": [
        "run",
        "--rm",
        "-i",
        "-v",
        "/path/to/your/database.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-sqlite:latest",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}

Mit freundlichen Grüßen
[Ihr Name]
```

## Alternative Kontaktmöglichkeiten

Falls die E-Mail-Adresse nicht direkt verfügbar ist:
1. Prüfe die "About" oder "Contribute" Seite auf mcplist.ai
2. Suche nach GitHub Repository von MCPList.ai (siehe Footer-Link)
3. Nutze das Kontaktformular falls vorhanden

---

## Submission Checklist

- [ ] Alle Informationen vorbereitet
- [ ] GitHub Repository ist öffentlich
- [ ] README.md ist vollständig
- [ ] Docker Image ist verfügbar
- [ ] Release ist erstellt
- [ ] E-Mail-Vorlage angepasst
- [ ] E-Mail versendet
- [ ] Bestätigung erhalten

---

## Nach der Submission

1. Warte auf Antwort von MCPList.ai (kann einige Tage dauern)
2. Prüfe regelmäßig, ob der Server im Verzeichnis erscheint
3. Teste die Links und Informationen
4. Aktualisiere bei Bedarf per E-Mail

