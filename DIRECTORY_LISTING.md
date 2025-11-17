# Listing in Public MCP Server Directories

This document explains how to list the Encrypted SQLite MCP Server in public MCP server directories.

## Prerequisites

✅ **All requirements are met:**
- ✅ Open source repository on GitHub: https://github.com/rosch100/mcp-sqlite
- ✅ Comprehensive README with installation and usage instructions
- ✅ Working MCP server implementation following MCP specification
- ✅ Open source license (Apache License 2.0)
- ✅ Clear documentation and examples

## Public MCP Server Directories

### 1. MCPList.ai
**URL:** https://www.mcplist.ai/

**Submission Process:**
1. Visit https://www.mcplist.ai/
2. Look for "Submit Server" or "Add Server" option
3. Fill out the submission form with:
   - **Name:** Encrypted SQLite MCP Server
   - **Description:** MCP server for working with SQLCipher 4 encrypted SQLite databases. Provides tools to read database structures, query tables, and perform CRUD operations.
   - **Repository URL:** https://github.com/rosch100/mcp-sqlite
   - **Tags/Categories:** database, sqlite, encryption, sqlcipher, java
   - **License:** Apache-2.0
   - **Language:** Java
   - **Transport:** STDIO

### 2. MCP Index
**URL:** https://mcpindex.net/

**Submission Process:**
1. Visit https://mcpindex.net/
2. Check for submission guidelines or contact form
3. Provide:
   - Repository URL: https://github.com/rosch100/mcp-sqlite
   - Server description and features
   - Installation instructions

### 3. MCPServ.club
**URL:** https://www.mcpserv.club/

**Requirements:**
- ✅ Open source repository on GitHub
- ✅ Clear documentation with README
- ✅ Working implementation following MCP specification
- ✅ Appropriate open source license

**Submission Process:**
1. Visit https://www.mcpserv.club/docs
2. Review submission guidelines
3. Submit via their submission form or GitHub issue

### 4. MCP Server Finder
**URL:** https://www.mcpserverfinder.com/

**Submission Process:**
1. Visit https://www.mcpserverfinder.com/
2. Look for submission or "Add Server" option
3. Provide server details

## Submission Information Template

Use this information when submitting to directories:

### Basic Information
- **Name:** Encrypted SQLite MCP Server
- **Short Description:** MCP server for SQLCipher 4 encrypted SQLite databases
- **Long Description:** A Model Context Protocol (MCP) server for working with encrypted SQLite databases using SQLCipher. This server provides tools to read database structures, query tables, and perform CRUD operations on encrypted SQLite databases.
- **Repository:** https://github.com/rosch100/mcp-sqlite
- **License:** Apache License 2.0
- **Language:** Java
- **Version:** 0.1.0
- **Transport:** STDIO

### Features
- SQLCipher 4 support
- Database exploration (list tables and columns)
- Query support (SELECT, INSERT, UPDATE, DELETE, DDL)
- CRUD operations (insert, update, delete with filtering)
- Configurable cipher profiles
- Full MCP protocol implementation

### Tools Provided
- `listTables` - List all tables with columns and metadata
- `getTableData` - Read table data with filtering and pagination
- `execQuery` - Execute arbitrary SQL statements
- `insertOrUpdate` - Perform UPSERT operations
- `deleteRows` - Delete rows with filtering

### Requirements
- Java 17 or higher
- Gradle (wrapper included)
- SQLite JDBC driver with encryption support (automatically downloaded)

### Installation
```bash
git clone https://github.com/rosch100/mcp-sqlite.git
cd mcp-sqlite
./gradlew build
./gradlew installDist
```

### Configuration Example
```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-sqlite/build/install/mcp-sqlite/bin/mcp-sqlite",
      "args": [
        "--args",
        "{\"dbPath\":\"/path/to/your/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

### Tags/Keywords
- database
- sqlite
- encryption
- sqlcipher
- sqlcipher-4
- java
- mcp-server
- crud
- database-tools

## After Submission

1. **Monitor submissions:** Check your email or the directory's review process
2. **Respond to feedback:** Address any questions or requests for clarification
3. **Update listings:** Keep your directory listings updated when you release new versions
4. **Engage with community:** Respond to questions and feedback from users

## Additional Resources

- **MCP Specification:** https://modelcontextprotocol.io/
- **GitHub Repository:** https://github.com/rosch100/mcp-sqlite
- **Issues:** https://github.com/rosch100/mcp-sqlite/issues
- **Releases:** https://github.com/rosch100/mcp-sqlite/releases

