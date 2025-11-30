# Encrypted SQLite MCP Server

**MCP server for encrypted SQLite databases**

A Model Context Protocol (MCP) server for working with encrypted SQLite databases using SQLCipher. This server provides tools to read database structures, query tables, and perform CRUD operations on encrypted SQLite databases.

**Compatible with all MCP clients** (Cursor, Claude Desktop, and others).

**Works with encrypted databases from:** MoneyMoney, 1Password, Signal, WhatsApp, Firefox, Telegram, KeePass, and other applications using SQLCipher encryption.

## Features

- **Encrypted SQLite Support**: Works with SQLCipher 4 encrypted databases - the key differentiator of this MCP server
- **Encrypted Passphrases**: Support for AES-256-GCM encrypted passphrases with macOS Keychain integration
- **Database Exploration**: List tables, columns, indexes, and schema metadata
- **Query Support**: Execute arbitrary SQL queries (SELECT, INSERT, UPDATE, DELETE, DDL)
- **CRUD Operations**: Insert, update, and delete rows with filtering
- **Configurable Cipher Profiles**: Support for different SQLCipher configurations
- **MCP Protocol**: Full Model Context Protocol implementation via STDIO
- **Security**: SQL identifier validation to prevent SQL injection
- **Debug Mode**: Optional debug output via `MCP_DEBUG` environment variable
- **Input Validation**: Comprehensive validation for limits, offsets, and identifiers

## Why Encrypted SQLite?

Many popular applications use encrypted SQLite databases (SQLCipher) to protect sensitive data. This MCP server is specifically designed to work with these encrypted databases.

### Applications Using Encrypted SQLite Databases

- **MoneyMoney** (macOS): Financial management app with encrypted local databases
- **1Password**: Password manager using SQLCipher for local vault storage
- **Signal**: Encrypted messaging app with SQLCipher-protected message databases
- **WhatsApp**: Messaging app using encrypted SQLite for local message storage
- **Firefox**: Browser using SQLCipher for encrypted login databases
- **Telegram**: Messaging app with encrypted local database storage
- **KeePass**: Password manager supporting encrypted SQLite database files

If you need to access data from any of these applications or other SQLCipher-encrypted databases, this MCP server provides the tools you need. Note that you need the passphrase of the encrypted database.

## Requirements

- **Java 21** or higher (JDK)
- **Gradle** (wrapper included)
- SQLite JDBC driver with encryption support (`sqlite-jdbc-3.50.1.0.jar` from [sqlite-jdbc-crypt](https://github.com/Willena/sqlite-jdbc-crypt))

## Quick Start

### Cursor (One-Click Installation)

The easiest way to install this MCP server in Cursor is via the **Cursor MCP Store**:

1. Visit [cursor.store/mcp/rosch100/mcp-encrypted-sqlite](https://www.cursor.store/mcp/rosch100/mcp-encrypted-sqlite)
2. Click **"Add to Cursor"**
3. Follow the prompts to configure your database path and passphrase

### Other MCP Clients

This server works with any MCP-compatible client. See the [Configuration](#configuration) section below for setup instructions.

## Installation

### Docker (Recommended)

Use the pre-built Docker image from GitHub Container Registry:

`docker pull ghcr.io/rosch100/mcp-encrypted-sqlite:latest`

**Quick Start:** See [DOCKER_QUICKSTART.md](DOCKER_QUICKSTART.md) for Docker Desktop setup.

**Detailed Configuration:** See [DOCKER_CONFIGURATION.md](DOCKER_CONFIGURATION.md) for advanced options.

### From Source

1. Clone the repository:

   `git clone https://github.com/rosch100/mcp-encrypted-sqlite.git`
   
   `cd mcp-encrypted-sqlite`

2. Build the project:

   `./gradlew build installDist`

The build process will automatically download `sqlite-jdbc-3.50.1.0.jar` from [sqlite-jdbc-crypt releases](https://github.com/Willena/sqlite-jdbc-crypt/releases) and place it in the `libs/` directory.

The executable will be available at `build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite`.

## Configuration

This MCP server works with **any MCP-compatible client** (Cursor, Claude Desktop, etc.). The configuration format follows the [Model Context Protocol specification](https://modelcontextprotocol.io/).

The server communicates via STDIO (standard input/output). Add the following configuration to your MCP client's configuration file:

**Configuration file locations:**
- **Cursor**: `~/.cursor/mcp.json`
- **Claude Desktop** (macOS): `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Claude Desktop** (Windows): `%APPDATA%\Claude\claude_desktop_config.json`
- **Other clients**: Refer to your client's documentation

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-encrypted-sqlite/build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite",
      "args": [
        "--args",
        "{\"db_path\":\"/path/to/your/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

**Optional Parameters:**

- **`transport`**: Defaults to `"stdio"` (can be omitted)
- **`cwd`**: Not needed when using absolute paths (can be omitted)
- **`env`**: Only needed if Java is not in your system PATH or for custom Java installation:

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-encrypted-sqlite/build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite",
      "args": [
        "--args",
        "{\"db_path\":\"/path/to/your/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ],
      "env": {
        "JAVA_HOME": "/path/to/java/home"
      }
    }
  }
}
```

### Docker Configuration

#### Plain Passphrase

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "docker",
      "args": [
        "run", "--rm", "-i",
        "-v", "/path/to/your/database.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-encrypted-sqlite:latest",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

#### Encrypted Passphrase (Recommended)

When using encrypted passphrases, you **must** pass the encryption key as an environment variable:

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "docker",
      "args": [
        "run", "--rm", "-i",
        "-e", "MCP_SQLITE_ENCRYPTION_KEY=your-encryption-key",
        "-v", "/path/to/your/database.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-encrypted-sqlite:latest",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"encrypted:your-encrypted-passphrase\"}"
      ]
    }
  }
}
```

**Important Notes:**
- The `-e` flag **must** come before the `-v` flag
- **macOS Keychain is NOT accessible from Docker containers** - pass the encryption key explicitly
- Get your encryption key: `security find-generic-password -s "mcp-encrypted-sqlite" -a "encryption-key" -w`
- The database file is mounted as read-only (`:ro`) by default. Remove `:ro` if you need write access

**Security Warning:** Storing both the encryption key and encrypted passphrase as plain text in your configuration file is a security risk. See [DOCKER_CONFIGURATION.md](DOCKER_CONFIGURATION.md) for secure alternatives.

### Custom Cipher Profile

Override the default SQLCipher 4 settings by including a `cipherProfile` in the configuration JSON:

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-encrypted-sqlite/build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite",
      "args": [
        "--args",
        "{\"db_path\":\"/path/to/your/database.sqlite\",\"passphrase\":\"your-passphrase\",\"cipherProfile\":{\"name\":\"SQLCipher 4 defaults\",\"pageSize\":4096,\"kdfIterations\":256000,\"hmacAlgorithm\":\"HMAC_SHA512\",\"kdfAlgorithm\":\"PBKDF2_HMAC_SHA512\"}}"
      ]
    }
  }
}
```

**Note:** All fields in `cipherProfile` are optional - only specify the ones you want to override from the defaults. You can also specify `cipherProfile` in individual tool calls, but it's recommended to configure it once in the MCP server configuration for consistency.

### Encrypted Passphrases

For enhanced security, you can store passphrases in encrypted form. The server uses **AES-256-GCM** encryption, which provides authenticated encryption and is both secure and fast.

#### macOS Keychain (Recommended for macOS)

1. **Generate and store key in Keychain:**
   Run: `./store-key-in-keychain.sh --generate`

2. **Encrypt your passphrase:**
   Run: `./encrypt-passphrase.sh "your-plain-passphrase"`

The key is automatically loaded from the Keychain when no environment variable is set.

**Benefits:**
- Key is securely encrypted and stored by macOS
- No environment variables needed
- Automatic unlock with macOS user password
- Works system-wide for all applications

#### Environment Variable (Cross-Platform)

1. **Generate an encryption key:**
   Run: `java -cp build/libs/mcp-encrypted-sqlite-VERSION.jar com.example.mcp.sqlite.config.PassphraseEncryption`

2. **Set the encryption key:**
   Run: `export MCP_SQLITE_ENCRYPTION_KEY="<your-generated-key>"`

3. **Encrypt your passphrase:**
   Run: `java -cp build/libs/mcp-encrypted-sqlite-VERSION.jar com.example.mcp.sqlite.util.EncryptPassphrase "your-plain-passphrase"`

#### Usage

Use the encrypted passphrase (with `encrypted:` prefix) in your configuration:

**On macOS with Keychain (recommended):**
```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-encrypted-sqlite/build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite",
      "args": [
        "--args",
        "{\"db_path\":\"/path/to/your/database.sqlite\",\"passphrase\":\"encrypted:<encrypted-passphrase>\"}"
      ]
    }
  }
}
```
*Note: No `env` section needed - the key is automatically loaded from macOS Keychain.*

**With environment variable (cross-platform):**
```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-encrypted-sqlite/build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite",
      "args": [
        "--args",
        "{\"db_path\":\"/path/to/your/database.sqlite\",\"passphrase\":\"encrypted:<encrypted-passphrase>\"}"
      ],
      "env": {
        "MCP_SQLITE_ENCRYPTION_KEY": "<your-encryption-key>"
      }
    }
  }
}
```

**Important Security Notes:**
- The encryption key **MUST** be available (macOS Keychain or `MCP_SQLITE_ENCRYPTION_KEY` environment variable)
- The server automatically detects encrypted passphrases (starting with `encrypted:`) and decrypts them
- Use `PassphraseEncryption.generateKey()` to generate strong keys (256 bits / 32 bytes)
- **AES-256-GCM** provides authenticated encryption
- Weak keys are automatically rejected

## Available Tools

### `list_tables`

List all tables in the database. By default only table names, with `include_columns=true` also column details.

**Parameters:**
- `db_path` (optional if configured): Path to the database file
- `passphrase` (optional if configured): Database passphrase
- `include_columns` (optional, default: false): If true, column details are also returned

**Example:**
```json
{
  "name": "list_tables",
  "arguments": {
    "include_columns": true
  }
}
```

### `get_table_data`

Read data from a table with optional filtering, column selection, and pagination.

**Parameters:**
- `table` (required): Table name
- `columns` (optional): Array of column names to select
- `filters` (optional): Object with column-value pairs for filtering
- `limit` (optional, default: 200): Maximum number of rows
- `offset` (optional, default: 0): Offset for pagination

**Example:**
```json
{
  "name": "get_table_data",
  "arguments": {
    "table": "accounts",
    "columns": ["id", "name", "balance"],
    "filters": {"status": "active"},
    "limit": 50,
    "offset": 0
  }
}
```

### `execute_sql`

Execute arbitrary SQL statements (SELECT, INSERT, UPDATE, DELETE, DDL).

**Security Warning**: This tool executes raw SQL without parameterization. Only use with trusted SQL or ensure proper validation and sanitization is performed before calling this tool. For safer operations, use the other tools (`get_table_data`, `insert_or_update`, `delete_rows`) which use parameterized queries.

**Parameters:**
- `sql` (required): SQL statement to execute

**Example:**
```json
{
  "name": "execute_sql",
  "arguments": {
    "sql": "SELECT COUNT(*) FROM transactions WHERE amount > 1000"
  }
}
```

### `insert_or_update`

Perform UPSERT operations (INSERT or UPDATE on conflict).

**Parameters:**
- `table` (required): Table name
- `primary_keys` (required): Array of primary key column names
- `rows` (required): Array of row objects to insert/update

**Example:**
```json
{
  "name": "insert_or_update",
  "arguments": {
    "table": "accounts",
    "primary_keys": ["id"],
    "rows": [
      {"id": 1, "name": "Account 1", "balance": 1000.0},
      {"id": 2, "name": "Account 2", "balance": 2000.0}
    ]
  }
}
```

### `delete_rows`

Delete rows from a table based on filters.

**Parameters:**
- `table` (required): Table name
- `filters` (required): Object with column-value pairs for filtering

**Example:**
```json
{
  "name": "delete_rows",
  "arguments": {
    "table": "transactions",
    "filters": {"status": "cancelled"}
  }
}
```

### `get_table_schema`

Retrieves detailed schema information for a table (columns, indexes, foreign keys, constraints).

**Parameters:**
- `table` (required): Table name

**Example:**
```json
{
  "name": "get_table_schema",
  "arguments": {
    "table": "accounts"
  }
}
```

### `list_indexes`

Lists all indexes of a table.

**Parameters:**
- `table` (required): Table name

**Example:**
```json
{
  "name": "list_indexes",
  "arguments": {
    "table": "accounts"
  }
}
```

## Debug Mode

The server supports optional debug output via the `MCP_DEBUG` environment variable. When enabled, detailed debug information is written to `stderr` (not `stdout`, to comply with MCP protocol requirements).

**Enable debug mode:**

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "/path/to/mcp-encrypted-sqlite/build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite",
      "args": [
        "--args",
        "{\"db_path\":\"/path/to/your/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ],
      "env": {
        "MCP_DEBUG": "true"
      }
    }
  }
}
```

**Debug output includes:**
- Server startup information (Java version, OS, arguments)
- Configuration parsing details
- Request processing information
- Response sizes and structures
- Database connection details

**Note:** Debug output is disabled by default to keep logs clean. Only enable it when troubleshooting issues.

## Default Cipher Profile

The server uses **SQLCipher 4 defaults** by default:

- `cipher_page_size`: 4096
- `kdf_iter`: 256000
- `cipher_hmac_algorithm`: HMAC_SHA512
- `cipher_kdf_algorithm`: PBKDF2_HMAC_SHA512
- `cipher_use_hmac`: ON
- `cipher_plaintext_header_size`: 0

These settings match the defaults used by tools like "DB Browser for SQLite" with SQLCipher 4.

## Development

See [DEVELOPMENT.md](DEVELOPMENT.md) for development setup, building, testing, and project structure.

## Security Considerations

### General Security

- **Passphrases**: Passphrases are only stored in memory and never logged
- **Encrypted Passphrases**: Use encrypted passphrases with AES-256-GCM for storing passphrases in configuration files (see [Encrypted Passphrases](#encrypted-passphrases) section)
- **Memory**: Note that decrypted passphrases remain in memory as Java Strings (immutable). For maximum security, consider using `char[]` arrays, though this is not currently implemented.
- **Transport**: Use secure transport channels (e.g., encrypted sessions) when accessing the server remotely
- **File Permissions**: Ensure database files have appropriate file system permissions

### Security Best Practices

1. **Use encrypted passphrases** with AES-256-GCM encryption
2. **Generate strong keys** using `PassphraseEncryption.generateKey()` (256 bits / 32 bytes)
3. **Store encryption keys securely**: Use macOS Keychain (recommended on macOS) or secure secret storage
4. **Never store both encryption key and encrypted passphrase in the same configuration file** - Use wrapper scripts or environment variables to load the key securely (see [DOCKER_CONFIGURATION.md](DOCKER_CONFIGURATION.md) for details)
5. **Rotate keys periodically** - when rotating, re-encrypt all passphrases with the new key
6. **Use different keys** for different environments (development, staging, production)
7. **Never commit keys or encrypted passphrases** to version control
8. **Restrict file permissions** on configuration files containing secrets (`chmod 600`)

## Troubleshooting

### Debugging MCP Server Communication Issues

The MCP server includes extensive debugging features to help diagnose communication problems.

#### Viewing Logs

**In MCP clients:**
- Check your client's log output (e.g., Cursor: Output panel → "MCP Logs")
- All debug output is written to `stderr`

**Manual testing:**
Run: `./build/install/mcp-encrypted-sqlite/bin/mcp-encrypted-sqlite --args '{"db_path":"/path/to/db.sqlite","passphrase":"secret"}' 2>&1 | tee mcp-debug.log`

#### Common Communication Problems

**1. Server does not start**
- **Symptom**: No logs visible, server does not respond
- **Debugging**: Check startup logs:
  - Java version is logged
  - Arguments are logged
  - Configuration parsing is logged
- **Solution**: 
  - Verify Java is correctly installed
  - Check the MCP configuration (`mcp.json`)
  - Check paths in `command` and `args` fields

**2. JSON Parsing Errors**
- **Symptom**: "Parse error" in logs
- **Debugging**: The server logs:
  - First 500 characters of received JSON
  - Exact exception with stack trace
- **Solution**:
  - Check JSON structure in MCP configuration
  - Ensure JSON is properly escaped
  - Verify all required fields are present

**3. Missing or Incorrect Responses**
- **Symptom**: Requests are not answered or timeouts occur
- **Debugging**: The server logs:
  - Each received request with ID and method
  - Response size and status
  - Flush status after writing
- **Solution**:
  - Check if `STDOUT` is available (logged at startup)
  - Check response size (very large responses can cause problems)
  - Verify flush was successful

**4. Invalid Requests**
- **Symptom**: "Invalid Request" errors
- **Debugging**: The server logs:
  - Missing fields (e.g., `method`, `id`)
  - JSON-RPC version mismatches
  - Invalid parameters
- **Solution**:
  - Ensure all requests comply with JSON-RPC 2.0 standard
  - Verify `method` and `id` fields are present
  - Check parameter structure

**5. Database Connection Problems**
- **Symptom**: "Database error" errors
- **Debugging**: The server logs:
  - Used DB paths (Default vs. Override)
  - Passphrase status (encrypted/decrypted)
  - CipherProfile configuration
- **Solution**:
  - Check DB path in logs
  - Verify passphrase was correctly decrypted
  - Check CipherProfile settings

#### Debugging Features in Detail

The server automatically logs:

- **Startup Information**:
  - Java version and Java Home
  - Operating system information
  - Number and content of arguments
  - Configuration parsing status

- **Request Processing**:
  - Each received request with number and length
  - JSON-RPC validation
  - Method calls with parameters
  - Response size and status

- **Error Handling**:
  - Detailed exception information with stack traces
  - JSON-RPC error codes according to specification
  - Error responses with additional debug data

- **Database Operations**:
  - Used configurations (Default vs. Override)
  - SQL queries (first 100 characters)
  - Result sizes and affected rows

### Database cannot be opened

- Verify the passphrase is correct
- Check that the database uses SQLCipher 4 defaults (or configure a custom cipher profile)
- Ensure the database file path is correct and accessible
- **Check logs**: The server logs detailed information about passphrase decryption and DB path

### Connection issues

- Verify Java is installed: `java -version`
- Check that `JAVA_HOME` is set correctly in the MCP configuration
- Review your MCP client logs for detailed error messages

### FTS (Full-Text Search) tables

The server automatically handles FTS virtual tables that may not have accessible metadata. These tables will appear with empty column lists.

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for details.

### Third-Party Licenses

- **sqlite-jdbc-crypt** (Apache License 2.0) - SQLite JDBC driver with encryption support
  - Source: https://github.com/Willena/sqlite-jdbc-crypt
- **Gson** (Apache License 2.0) - JSON library for Java
  - Source: https://github.com/google/gson
- **JUnit Jupiter** (Eclipse Public License 2.0) - Testing framework
  - Source: https://junit.org/junit5/

See [NOTICE](NOTICE) for detailed attribution information.

## Acknowledgments

- [sqlite-jdbc-crypt](https://github.com/Willena/sqlite-jdbc-crypt) - SQLite JDBC driver with encryption support
- [Model Context Protocol](https://modelcontextprotocol.io/) - MCP specification

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Support

For issues, questions, or contributions, please open an issue on [GitHub](https://github.com/rosch100/mcp-encrypted-sqlite/issues).

## Buy me a coffee

Like this integration? Feel free to buy me a coffee! Your support helps me continue working on cool features.

[![Buy Me A Coffee](https://img.buymeacoffee.com/button-api/?text=Buy%20me%20a%20coffee&emoji=&slug=rosch100&button_colour=FFDD00&font_colour=000000&font_family=Cookie&outline_colour=000000&coffee_colour=ffffff)](https://buymeacoffee.com/rosch100)
