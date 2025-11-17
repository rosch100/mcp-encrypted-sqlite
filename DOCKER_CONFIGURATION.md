# Docker Configuration for MCP SQLite Server

This guide explains how to configure the MCP SQLite Server using Docker Desktop in various MCP clients.

## Prerequisites

1. **Docker Desktop** installed and running
2. **Docker image** pulled: `ghcr.io/rosch100/mcp-sqlite:0.2.2`
3. **Encrypted SQLite database** file ready

## Pull the Docker Image

```bash
docker pull ghcr.io/rosch100/mcp-sqlite:0.2.2
```

Or use the latest version:

```bash
docker pull ghcr.io/rosch100/mcp-sqlite:latest
```

## Configuration Examples

### Cursor (macOS/Linux/Windows)

Edit `~/.cursor/mcp.json` (or `%APPDATA%\Cursor\mcp.json` on Windows):

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
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

**Important Notes:**
- Replace `/path/to/your/database.sqlite` with the actual path to your database file
- Replace `your-passphrase` with your actual database passphrase
- The `:ro` flag mounts the database as read-only. Remove it if you need write access
- On Windows, use Windows-style paths: `C:/path/to/database.sqlite:/data/database.sqlite:ro`

### Claude Desktop (macOS)

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

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
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

### Claude Desktop (Windows)

Edit `%APPDATA%\Claude\claude_desktop_config.json`:

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
        "C:/path/to/your/database.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

### Claude Desktop (Linux)

Edit `~/.config/Claude/claude_desktop_config.json`:

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
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

## Advanced Configuration

### Using Encrypted Passphrases

When using encrypted passphrases (with `encrypted:` prefix), you **must** pass the encryption key as an environment variable to the Docker container using the `-e` flag.

#### Step 1: Get Your Encryption Key

**On macOS (if stored in Keychain):**
```bash
security find-generic-password -s "mcp-sqlite" -a "encryption-key" -w
```

**Or if you have it stored elsewhere:**
Use your encryption key directly.

#### Step 2: Configure Docker with Encryption Key

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "docker",
      "args": [
        "run",
        "--rm",
        "-i",
        "-e",
        "MCP_SQLITE_ENCRYPTION_KEY=your-encryption-key-here",
        "-v",
        "/path/to/your/database.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"encrypted:your-encrypted-passphrase\"}"
      ]
    }
  }
}
```

**Important Notes:**
- The `-e` flag **must** come before the `-v` flag in the Docker command
- The encryption key is passed as an environment variable to the container
- **macOS Keychain is NOT accessible from inside Docker containers**, so you must pass the key explicitly
- Replace `your-encryption-key-here` with your actual encryption key
- Replace `your-encrypted-passphrase` with your encrypted passphrase (starting with `encrypted:`)

#### Example: Complete Configuration with Encrypted Passphrase

```json
{
  "mcpServers": {
    "encrypted-sqlite": {
      "command": "docker",
      "args": [
        "run",
        "--rm",
        "-i",
        "-e",
        "MCP_SQLITE_ENCRYPTION_KEY=REMOVED_ENCRYPTION_KEY=",
        "-v",
        "/Users/username/Library/Containers/com.moneymoney-app.retail/Data/Library/Application Support/MoneyMoney/Database/MoneyMoney.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"encrypted:REMOVED_ENCRYPTED_PASSPHRASE\"}"
      ]
    }
  }
}
```

#### Security Considerations

- **Never commit the encryption key** to version control
- Store the encryption key securely (e.g., in a password manager)
- Consider using environment variables in your shell instead of hardcoding in the config:
  ```bash
  export MCP_SQLITE_ENCRYPTION_KEY="your-key"
  ```
  Then reference it in your config (though Cursor may not expand shell variables)
- The encryption key is visible in process lists, so be cautious in shared environments

### Custom Cipher Profile

To use a custom cipher profile:

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
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\",\"cipherProfile\":{\"name\":\"Custom\",\"pageSize\":4096,\"kdfIterations\":256000,\"hmacAlgorithm\":\"HMAC_SHA512\",\"kdfAlgorithm\":\"PBKDF2_HMAC_SHA512\"}}"
      ]
    }
  }
}
```

### Write Access

To enable write access to the database, remove the `:ro` flag:

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
        "/path/to/your/database.sqlite:/data/database.sqlite",
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

### Debug Mode

To enable debug output:

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
        "-e",
        "MCP_DEBUG=true",
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

## Testing the Configuration

### Manual Test

Test the Docker container manually:

```bash
docker run --rm -i \
  -v /path/to/your/database.sqlite:/data/database.sqlite:ro \
  ghcr.io/rosch100/mcp-sqlite:0.2.2 \
  --args '{"db_path":"/data/database.sqlite","passphrase":"your-passphrase"}'
```

### Test MCP Protocol

Send an initialize request:

```bash
echo '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},"clientInfo":{"name":"test","version":"1.0"}}}' | \
  docker run --rm -i \
  -v /path/to/your/database.sqlite:/data/database.sqlite:ro \
  ghcr.io/rosch100/mcp-sqlite:0.2.2 \
  --args '{"db_path":"/data/database.sqlite","passphrase":"your-passphrase"}'
```

## Troubleshooting

### Docker not found

- Ensure Docker Desktop is running
- Verify Docker is in your PATH: `docker --version`
- Restart your MCP client after starting Docker

### "mounts denied" or "path not shared" error

**On macOS:**
1. Open Docker Desktop
2. Go to Settings → Resources → File Sharing
3. Add the parent directory (e.g., `/Users` or `/Users/username`)
4. Click "Apply & Restart"
5. Ensure your path starts with `/` (absolute path)

**Common mistake:** Using relative paths like `Users/...` instead of `/Users/...`

### "invalid volume specification" error

**Problem:** Path contains spaces or special characters

**Solution:** Use absolute paths with proper escaping. For paths with spaces:

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
        "/Users/username/Library/Containers/com.moneymoney-app.retail/Data/Library/Application Support/MoneyMoney/Database/MoneyMoney.sqlite:/data/database.sqlite:ro",
        "ghcr.io/rosch100/mcp-sqlite:0.2.2",
        "--args",
        "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"your-passphrase\"}"
      ]
    }
  }
}
```

**Important:** Always use absolute paths starting with `/` on macOS/Linux.

### Permission denied

- Check file permissions on your database file
- Ensure Docker has access to the mounted directory
- On macOS, check Docker Desktop → Settings → Resources → File Sharing
- Make sure the parent directory is shared (e.g., `/Users`)

### Container exits immediately

- Check Docker logs: `docker logs <container-id>`
- Verify the database path is correct (must be absolute)
- Ensure the passphrase is correct
- Verify the path is shared in Docker Desktop

### Path issues on Windows

- Use forward slashes: `C:/path/to/file.sqlite`
- Or escape backslashes: `C:\\path\\to\\file.sqlite`
- Use Windows-style paths in the volume mount

### Path issues on macOS

**Common errors:**
- ❌ `Users/...` (missing leading `/`)
- ✅ `/Users/...` (correct absolute path)

**For paths with spaces:**
- The path in the `-v` argument should be properly quoted or escaped
- Docker handles spaces in paths automatically if the path is absolute
- Example: `/Users/username/Library/Application Support/...` works fine

### Image not found

- Pull the image: `docker pull ghcr.io/rosch100/mcp-sqlite:0.2.2`
- Check if the image exists: `docker images | grep mcp-sqlite`
- Verify the tag is correct

## Security Considerations

1. **Passphrases**: Never commit passphrases to version control
2. **File Permissions**: Use read-only mounts (`:ro`) when possible
3. **Encrypted Passphrases**: Use encrypted passphrases for better security
4. **Environment Variables**: Be careful with environment variables containing secrets

## Restart MCP Client

After making configuration changes:

1. **Cursor**: Restart the application
2. **Claude Desktop**: Restart the application
3. Check MCP logs for connection status

## Verification

Once configured, you should see:

1. The server appears in your MCP client's server list
2. Tools are available (list_tables, get_table_data, etc.)
3. You can query your encrypted database

For more information, see the main [README.md](README.md).

