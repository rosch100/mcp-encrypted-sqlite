package com.example.mcp.sqlite.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqliteUtilTest {

    @Test
    void quoteLiteralEscapesSingleQuotes() {
        assertEquals("'O''Brien'", SqliteUtil.quoteLiteral("O'Brien"));
    }

    @Test
    void quoteLiteralHandlesNull() {
        assertEquals("NULL", SqliteUtil.quoteLiteral(null));
    }

    @Test
    void jdbcUrlWithoutImmutableUsesPlainPath() {
        Path path = Path.of("/tmp/demo.sqlite");
        assertEquals("jdbc:sqlite:" + path.toAbsolutePath().normalize(), SqliteUtil.jdbcUrl(path, false));
    }

    @Test
    void jdbcUrlWithImmutableUsesSqliteUri() {
        Path path = Path.of("/tmp/demo db.sqlite");
        String url = SqliteUtil.jdbcUrl(path, true);
        assertTrue(url.startsWith("jdbc:sqlite:file:"), url);
        assertTrue(url.endsWith("?immutable=1"), url);
        assertTrue(url.contains("demo%20db.sqlite") || url.contains("demo db.sqlite"), url);
    }
}
