# Directory MCP Submission Information

Alle Informationen für die Submission bei Directory MCP (directorymcp.com)

## Quick Submission Guide

1. **Besuche**: https://directorymcp.com/
2. **Suche nach**: "Submit", "Add Component", oder "Contribute" Button/Link
3. **Fülle das Formular aus** mit den folgenden Informationen:

---

## Component Details

### Component Name
```
Encrypted SQLite MCP Server
```

### Type
```
MCP Server
```

### Repository
```
https://github.com/rosch100/mcp-sqlite
```

### Description
```
MCP server implementation for SQLCipher 4 encrypted SQLite databases. Provides full CRUD operations, database exploration, and query support via Model Context Protocol.
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

## Installation

### Docker (Recommended)
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

## Usage

See README.md and DOCKER_QUICKSTART.md for detailed configuration instructions.

### Quick Configuration Example
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

## License
```
Apache License 2.0
```

## Version
```
0.2.4
```

## Language
```
Java
```

## Runtime
```
Java 21+
```

---

## Documentation

- **Main README**: https://github.com/rosch100/mcp-sqlite/blob/main/README.md
- **Docker Quickstart**: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_QUICKSTART.md
- **Docker Configuration**: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_CONFIGURATION.md

---

## Submission Checklist

- [ ] Alle Informationen vorbereitet
- [ ] GitHub Repository ist öffentlich
- [ ] Formular ausgefüllt
- [ ] Submission abgeschickt
- [ ] Bestätigung erhalten

