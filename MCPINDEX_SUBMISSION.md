# MCP Index Submission Information

Alle Informationen für die Submission bei MCP Index (mcpindex.net)

## Quick Submission Guide

1. **Besuche**: https://mcpindex.net/
2. **Suche nach**: "Submit", "Add Server", oder "Contribute" Button/Link
3. **Fülle das Formular aus** mit den folgenden Informationen:

---

## Server Details

### Name
```
encrypted-sqlite-mcp
```

### Display Name
```
Encrypted SQLite MCP Server
```

### Description
```
MCP server for working with SQLCipher 4 encrypted SQLite databases. Provides tools to read database structures, query tables, and perform CRUD operations.
```

### Repository
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

### Transport
```
stdio
```

---

## Tools

1. **list_tables** - List all tables in the database with their columns and metadata
2. **get_table_data** - Read data from a table with optional filtering, column selection, and pagination
3. **exec_query** - Execute arbitrary SQL statements (SELECT, INSERT, UPDATE, DELETE, DDL)
4. **insert_or_update** - Perform UPSERT operations (INSERT or UPDATE on conflict)
5. **delete_rows** - Delete rows from a table based on filters
6. **get_table_schema** - Retrieve detailed schema information (columns, indexes, foreign keys)
7. **list_indexes** - List all indexes for a given table

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

## Configuration Example

### Docker
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

## Keywords/Tags
```
mcp, mcp-server, sqlite, sqlcipher, encryption, database, java, crud, database-tools
```

## License
```
Apache-2.0
```

## Features
- SQLCipher 4 support
- Database exploration
- Query support
- CRUD operations
- Configurable cipher profiles
- Encrypted passphrase support

---

## Documentation Links

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

