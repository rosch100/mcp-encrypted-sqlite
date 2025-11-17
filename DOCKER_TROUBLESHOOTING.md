# Docker Troubleshooting Guide

Common issues and solutions when using the MCP SQLite Server with Docker.

## Error: "mounts denied: The path is not shared"

### Problem
Docker Desktop on macOS requires explicit file sharing configuration.

### Solution

1. **Open Docker Desktop**
2. **Go to Settings** (gear icon)
3. **Navigate to Resources → File Sharing**
4. **Add the parent directory** of your database:
   - For `/Users/username/...` → Add `/Users`
   - For `/home/user/...` → Add `/home`
5. **Click "Apply & Restart"**
6. **Wait for Docker to restart**

### Example Configuration

If your database is at:
```
/Users/username/Library/Containers/com.moneymoney-app.retail/Data/Library/Application Support/MoneyMoney/Database/MoneyMoney.sqlite
```

Add `/Users` to File Sharing in Docker Desktop.

## Error: "invalid volume specification" or "includes invalid characters"

### Problem
The path is missing the leading `/` or is not absolute.

### Common Mistakes

❌ **Wrong:**
```json
"-v",
"Users/username/Library/...",
```

✅ **Correct:**
```json
"-v",
"/Users/username/Library/...",
```

### Solution

**Always use absolute paths:**
- macOS/Linux: Must start with `/`
- Windows: Must start with `C:/` or `D:/` etc.

### Example for macOS

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

**Note:** Paths with spaces (like "Application Support") work fine in Docker - no escaping needed if the path is absolute.

## Error: "no matching manifest for linux/arm64"

### Problem
The Docker image was built only for `linux/amd64`, but you're on Apple Silicon (ARM64).

### Solution

The image should now support both platforms. If you still see this error:

1. Pull the latest image: `docker pull ghcr.io/rosch100/mcp-sqlite:0.2.2`
2. Check if multi-platform build completed: https://github.com/rosch100/mcp-sqlite/actions
3. Try using `latest` tag: `ghcr.io/rosch100/mcp-sqlite:latest`

## Error: "Client closed for command"

### Problem
The container starts but immediately exits, usually due to:
- Invalid database path
- Wrong passphrase
- Database file not accessible
- Missing encryption key (for encrypted passphrases)

### Solution

1. **Test manually (plain passphrase):**
   ```bash
   docker run --rm -i \
     -v /absolute/path/to/database.sqlite:/data/database.sqlite:ro \
     ghcr.io/rosch100/mcp-sqlite:0.2.2 \
     --args '{"db_path":"/data/database.sqlite","passphrase":"test"}'
   ```

2. **Test manually (encrypted passphrase):**
   ```bash
   docker run --rm -i \
     -e MCP_SQLITE_ENCRYPTION_KEY="your-encryption-key" \
     -v /absolute/path/to/database.sqlite:/data/database.sqlite:ro \
     ghcr.io/rosch100/mcp-sqlite:0.2.2 \
     --args '{"db_path":"/data/database.sqlite","passphrase":"encrypted:your-encrypted-passphrase"}'
   ```

3. **Check Docker logs:**
   - Look for error messages
   - Verify the path is correct
   - Check passphrase
   - If using encrypted passphrase, verify `MCP_SQLITE_ENCRYPTION_KEY` is set

4. **Verify file permissions:**
   ```bash
   ls -la /path/to/your/database.sqlite
   ```

## Error: "Encrypted passphrase detected, but MCP_SQLITE_ENCRYPTION_KEY is not set"

### Problem
You're using an encrypted passphrase (`encrypted:...`) but haven't passed the encryption key to the Docker container.

### Solution

1. **Get your encryption key:**
   ```bash
   # On macOS (if stored in Keychain)
   security find-generic-password -s "mcp-sqlite" -a "encryption-key" -w
   ```

2. **Add the `-e` flag to your Docker command:**
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
           "/path/to/database.sqlite:/data/database.sqlite:ro",
           "ghcr.io/rosch100/mcp-sqlite:0.2.2",
           "--args",
           "{\"db_path\":\"/data/database.sqlite\",\"passphrase\":\"encrypted:...\"}"
         ]
       }
     }
   }
   ```

3. **Important:** The `-e` flag must come **before** the `-v` flag in the Docker command.

4. **Restart your MCP client** after updating the configuration.

## Error: "No server info found"

### Problem
The MCP server didn't start successfully, so the client can't connect.

### Solution

1. Check the errors above
2. Verify Docker Desktop is running
3. Test the container manually (see above)
4. Check MCP client logs for detailed error messages

## Quick Diagnostic Steps

1. **Verify Docker is running:**
   ```bash
   docker ps
   ```

2. **Pull the image:**
   ```bash
   docker pull ghcr.io/rosch100/mcp-sqlite:0.2.2
   ```

3. **Test with a simple path:**
   ```bash
   # Create a test database path
   mkdir -p /tmp/test
   touch /tmp/test/test.db
   
   # Test the container
   docker run --rm -i \
     -v /tmp/test/test.db:/data/test.db:ro \
     ghcr.io/rosch100/mcp-sqlite:0.2.2 \
     --args '{"db_path":"/data/test.db","passphrase":""}'
   ```

4. **If test works, check your actual path:**
   - Ensure it's absolute (starts with `/`)
   - Ensure parent directory is in Docker File Sharing
   - Check file permissions

## Configuration Checklist

- [ ] Docker Desktop is running
- [ ] Image is pulled: `docker pull ghcr.io/rosch100/mcp-sqlite:0.2.2`
- [ ] Path is absolute (starts with `/` on macOS/Linux)
- [ ] Parent directory is in Docker File Sharing settings
- [ ] Database file exists and is readable
- [ ] Passphrase is correct
- [ ] MCP client configuration JSON is valid

## Still Having Issues?

1. Check the main [DOCKER_CONFIGURATION.md](DOCKER_CONFIGURATION.md)
2. Review [README.md](README.md) for general setup
3. Check GitHub Issues: https://github.com/rosch100/mcp-sqlite/issues


