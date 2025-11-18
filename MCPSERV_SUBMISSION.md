# MCPServ.club Submission Information

Alle Informationen für die Submission bei MCPServ.club

## Quick Submission Guide

1. **Besuche**: https://www.mcpserv.club/
2. **Suche nach**: "Submit", "Add Server", oder Dokumentation unter `/docs`
3. **Fülle das Formular aus** mit den folgenden Informationen:

---

## Server Details

### Server Name
```
Encrypted SQLite MCP Server
```

### GitHub URL
```
https://github.com/rosch100/mcp-sqlite
```

### Short Description
```
MCP server for SQLCipher 4 encrypted SQLite databases with full CRUD operations
```

### Long Description
```
A Model Context Protocol server for working with SQLCipher 4 encrypted SQLite databases. Provides comprehensive tools for database exploration, querying, and CRUD operations. Features include encrypted passphrase support with macOS Keychain integration, configurable cipher profiles, and full SQL query support.
```

### Category
```
Database
```

### Tags
```
sqlite, sqlcipher, encryption, database, crud, java
```

---

## Installation Method
```
Docker
```

### Docker Image
```
ghcr.io/rosch100/mcp-sqlite:latest
```

### Installation Command
```bash
docker pull ghcr.io/rosch100/mcp-sqlite:latest
```

---

## Configuration

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
        "/path/to/database.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-sqlite:latest",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

---

## Features

- SQLCipher 4 encryption support
- Database structure exploration
- SQL query execution
- CRUD operations (Create, Read, Update, Delete)
- UPSERT operations
- Encrypted passphrase support
- macOS Keychain integration
- Configurable cipher profiles

---

## Documentation Links

- **Main README**: https://github.com/rosch100/mcp-sqlite
- **Docker Quickstart**: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_QUICKSTART.md
- **Docker Configuration**: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_CONFIGURATION.md

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

---

## Submission Checklist

- [ ] Alle Informationen vorbereitet
- [ ] GitHub Repository ist öffentlich
- [ ] Docker Image ist verfügbar
- [ ] Formular ausgefüllt
- [ ] Submission abgeschickt
- [ ] Bestätigung erhalten

