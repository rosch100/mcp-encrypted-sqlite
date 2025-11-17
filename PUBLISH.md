# Publishing to MCP Registry

## Prerequisites

✅ All files are prepared:
- `server.json` with correct format (version 0.2.2)
- GitHub repository configured
- Release v0.2.2 created (or ready to create)

## Steps to Publish

### 1. Authenticate with MCP Registry

Run the following command and follow the browser authentication:

```bash
mcp-publisher login github
```

This will:
- Open a browser window
- Ask you to authorize the MCP Publisher application
- Complete the authentication

### 2. Publish to Registry

Once authenticated, publish your server:

```bash
mcp-publisher publish
```

This will:
- Validate your `server.json` file
- Submit your server to the MCP registry
- Make it publicly available

### 3. Verify Publication

After publishing, verify your server is listed:

```bash
curl "https://registry.modelcontextprotocol.io/v0/servers?search=io.github.rosch100/mcp-sqlite"
```

Or visit the registry website to search for your server.

## Troubleshooting

If you encounter issues:

1. **Validation errors**: Check that `server.json` follows the schema
2. **Authentication errors**: Make sure you're logged in with `mcp-publisher login github`
3. **Publish errors**: Verify your GitHub repository is public and accessible

## Additional Directories

After publishing to the official MCP registry, you can also submit to:

- **MCPList.ai**: Visit https://www.mcplist.ai/ and use their submission form
- **MCP Index**: Visit https://mcpindex.net/ and follow their submission process
- **MCPServ.club**: Visit https://www.mcpserv.club/docs for submission guidelines
- **Anthropic Connectors Directory**: Visit https://support.anthropic.com/en/articles/11596036-anthropic-connectors-directory-faq

