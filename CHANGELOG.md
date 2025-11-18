# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.3] - 2025-11-18

### Changed
- **Repository Privacy**: Removed all personal references from git history
  - Replaced personal paths (`/Users/roschmac`) with generic examples (`/Users/username`)
  - Replaced specific application paths with generic examples
  - All commit messages and file contents now use generic placeholders
- Updated documentation examples to use generic paths and usernames

### Security
- Git history has been rewritten to remove personal information
- All file contents and commit messages are now privacy-compliant

## [0.2.2] - 2024-11-17

### Changed
- Repository cleanup: removed redundant test scripts and development files
- Complete translation of all remaining German comments to English
- Improved best practices:
  - Added `gradle.properties` to `.gitignore` (local configuration)
  - Made `run-mcp-server.sh` more generic (removed hardcoded macOS paths)
  - Updated `.dockerignore` to keep `.github/` for CI/CD workflows
  - Cleaned up `gradle.properties` with comments for team use

### Removed
- Test scripts: `test-mcp-interactive.py`, `test-list-tables.sh`, `test-docker.sh`
- Redundant documentation: `TESTING.md`
- Example configurations: `mcp.json.example`, `cursor-mcp-example.json`
- Test utility classes from `src/main`: `TestDatabaseConnection`, `ComparePassphrases`, `TestKeychainAccess`, `TestDecryption`, `TestFullDecryption`
- Backup files: `server.json.backup`, `check-ghcr.md`
- Root-level test files: `TestKeychain.java`, `TestOS.java`

## [0.2.1] - 2024-11-17

### Changed
- Complete translation of all German comments and error messages to English
- Updated version references in documentation from 0.1.0 to 0.2.0

### Fixed
- Consistency between source code and documentation
- All encrypted passphrase features properly documented

## [0.2.0] - 2025-11-17

### Added
- **New Tools:**
  - `get_table_schema`: Retrieve detailed schema information (columns, indexes, foreign keys, CREATE SQL)
  - `list_indexes`: List all indexes for a given table
- **Security Enhancements:**
  - SQL identifier validation to prevent SQL injection attacks
  - Input validation for limits, offsets, and identifiers
  - Maximum limit cap (10,000 rows) to prevent excessive memory usage
  - **Encrypted Passphrases**: Support for AES-256-GCM encrypted passphrases
    - macOS Keychain integration for secure key storage
    - Environment variable support for cross-platform usage
    - Automatic detection and decryption of encrypted passphrases
- **Debug Mode:**
  - Optional debug output via `MCP_DEBUG` environment variable
  - Debug logs written to `stderr` (MCP protocol compliant)
- **Parameter Compatibility:**
  - Support for `db_path`, `dbPath`, and `db_Path` parameter variants
  - Backward compatibility with camelCase parameter names

### Changed
- **MCP Protocol Compliance:**
  - Fixed `tools/call` response format to use standard MCP `content` array structure
  - Moved all debug output from `stdout` to `stderr` (MCP requires stdout for JSON-RPC only)
  - Protocol version handling now dynamically uses client's protocol version
- **Naming Standardization:**
  - All tool names standardized to `snake_case` (e.g., `list_tables`, `get_table_data`)
  - All parameter names standardized to `snake_case` (e.g., `db_path`, `include_columns`, `primary_keys`)
  - Response fields standardized to `snake_case` (e.g., `affected_rows`, `table_name`, `not_null`)
- **Code Quality:**
  - Upgraded to Java 21
  - Replaced magic numbers with named constants
  - Translated all German comments and log messages to English
  - Improved error messages with exception types and stack traces
- **Documentation:**
  - Updated all examples to use `snake_case` naming
  - Added security warnings for `execute_sql` tool
  - Added debug mode documentation
  - Improved troubleshooting section

### Fixed
- JSON parsing errors caused by debug output on `stdout`
- Response structure issues with Cursor and other MCP clients
- Protocol version mismatch between client and server

### Technical Details
- Java 21+ required (upgraded from Java 17)
- Uses sqlite-jdbc-crypt library (version 3.50.1.0)
- Gradle build system
- JUnit 5 for testing

## [0.1.0] - 2025-11-17

### Added
- Initial release
- MCP server implementation with STDIO transport
- Support for SQLCipher 4 encrypted databases
- `listTables` tool to enumerate database tables and columns
- `getTableData` tool to read table data with filtering and pagination
- `execQuery` tool to execute arbitrary SQL statements
- `insertOrUpdate` tool for UPSERT operations
- `deleteRows` tool to delete rows with filtering
- Configurable cipher profiles
- Default SQLCipher 4 configuration
- Error handling for FTS virtual tables
- Comprehensive logging support

### Technical Details
- Java 17+ required
- Uses sqlite-jdbc-crypt library (version 3.50.1.0)
- Gradle build system
- JUnit 5 for testing

