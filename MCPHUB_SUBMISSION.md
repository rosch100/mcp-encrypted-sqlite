# MCPHub Submission Information

Alle Informationen für die Submission bei MCPHub (mcphub.com)

## Quick Submission Guide

1. **Besuche**: https://mcphub.com/
2. **Suche nach**: "Submit", "Add Server", oder "Contribute" Button/Link
3. **Fülle das Formular aus** mit den folgenden Informationen:

---

## Server Details

### Server Name
```
encrypted-sqlite-mcp
```

### Display Name
```
Encrypted SQLite MCP Server
```

### Description
```
MCP server for SQLCipher 4 encrypted SQLite databases with full CRUD operations and query support
```

### GitHub
```
rosch100/mcp-sqlite
```

### Full GitHub URL
```
https://github.com/rosch100/mcp-sqlite
```

### Version
```
0.2.4
```

### Language
```
Java
```

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

- SQLCipher 4 support
- Database exploration
- Query support
- CRUD operations
- Configurable cipher profiles
- Encrypted passphrase support

---

## Tools

1. **list_tables** - List all tables in the database
2. **get_table_data** - Read data from a table with filtering
3. **exec_query** - Execute arbitrary SQL statements
4. **insert_or_update** - Perform UPSERT operations
5. **delete_rows** - Delete rows from a table
6. **get_table_schema** - Retrieve detailed schema information
7. **list_indexes** - List all indexes for a table

---

## Documentation

- **Main README**: https://github.com/rosch100/mcp-sqlite/blob/main/README.md
- **Docker Quickstart**: https://github.com/rosch100/mcp-sqlite/blob/main/DOCKER_QUICKSTART.md

---

## License
```
Apache-2.0
```

---

## Submission Checklist

- [ ] Alle Informationen vorbereitet
- [ ] GitHub Repository ist öffentlich
- [ ] Formular ausgefüllt
- [ ] Submission abgeschickt
- [ ] Bestätigung erhalten

