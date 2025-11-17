# Docker Quick Start Guide

Quick setup guide for using the MCP SQLite Server with Docker Desktop.

## Step 1: Pull the Image

```bash
docker pull ghcr.io/rosch100/mcp-sqlite:0.2.2
```

## Step 2: Configure Your MCP Client

### For Cursor (Plain Passphrase)

1. Open or create `~/.cursor/mcp.json`
2. Add this configuration:

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

3. Replace:
   - `/path/to/your/database.sqlite` → Your actual database path
   - `your-passphrase` → Your database passphrase

4. Restart Cursor

### For Cursor (Encrypted Passphrase)

If you're using encrypted passphrases (recommended for security):

1. Get your encryption key:
   ```bash
   security find-generic-password -s "mcp-sqlite" -a "encryption-key" -w
   ```

2. Open or create `~/.cursor/mcp.json`
3. Add this configuration:

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

4. Replace:
   - `your-encryption-key-here` → Your encryption key from step 1
   - `/path/to/your/database.sqlite` → Your actual database path
   - `your-encrypted-passphrase` → Your encrypted passphrase (starts with `encrypted:`)

5. Restart Cursor

**Important:** The `-e` flag must come before the `-v` flag in the Docker command.

### For Claude Desktop (macOS)

1. Open or create `~/Library/Application Support/Claude/claude_desktop_config.json`
2. Use the same configuration as above
3. Restart Claude Desktop

### For Claude Desktop (Windows)

1. Open or create `%APPDATA%\Claude\claude_desktop_config.json`
2. Use Windows paths:
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
3. Restart Claude Desktop

## Step 3: Verify

1. Check Docker Desktop is running
2. Verify the image is available: `docker images | grep mcp-sqlite`
3. Check MCP client logs for connection status

## Common Issues

### Docker not running
- Start Docker Desktop
- Wait for it to fully start (whale icon in system tray)

### Path not found
- **macOS/Linux:** Always use absolute paths starting with `/` (e.g., `/Users/username/...`)
- **Windows:** Use absolute paths with forward slashes: `C:/path/to/file.sqlite`
- Common mistake: `Users/...` instead of `/Users/...`

### "mounts denied" error
- Open Docker Desktop → Settings → Resources → File Sharing
- Add the parent directory (e.g., `/Users` on macOS)
- Click "Apply & Restart"
- Ensure path starts with `/` (absolute path)

### Permission denied
- Check file permissions
- Ensure Docker Desktop has access to the directory
- macOS: Docker Desktop → Settings → Resources → File Sharing

## Need More Help?

See [DOCKER_CONFIGURATION.md](DOCKER_CONFIGURATION.md) for detailed configuration options.

