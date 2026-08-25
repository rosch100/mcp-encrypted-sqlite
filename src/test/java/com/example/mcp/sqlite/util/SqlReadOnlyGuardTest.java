package com.example.mcp.sqlite.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlReadOnlyGuardTest {

    @Test
    void allowsSelectAndExplainAndReadPragma() {
        assertTrue(SqlReadOnlyGuard.isReadOnly("SELECT 1"));
        assertTrue(SqlReadOnlyGuard.isReadOnly("  /* c */ SELECT * FROM t"));
        assertTrue(SqlReadOnlyGuard.isReadOnly("EXPLAIN QUERY PLAN SELECT 1"));
        assertTrue(SqlReadOnlyGuard.isReadOnly("PRAGMA journal_mode;"));
        assertTrue(SqlReadOnlyGuard.isReadOnly("SELECT 'INSERT' AS x"));
        assertTrue(SqlReadOnlyGuard.isReadOnly("WITH x AS (SELECT 1 AS n) SELECT * FROM x"));
    }

    @Test
    void rejectsWrites() {
        assertFalse(SqlReadOnlyGuard.isReadOnly("INSERT INTO t VALUES (1)"));
        assertFalse(SqlReadOnlyGuard.isReadOnly("UPDATE t SET a=1"));
        assertFalse(SqlReadOnlyGuard.isReadOnly("DELETE FROM t"));
        assertFalse(SqlReadOnlyGuard.isReadOnly("DROP TABLE t"));
        assertFalse(SqlReadOnlyGuard.isReadOnly("PRAGMA journal_mode=WAL"));
        assertFalse(SqlReadOnlyGuard.isReadOnly("WITH x AS (SELECT 1) INSERT INTO t SELECT * FROM x"));
        assertFalse(SqlReadOnlyGuard.isReadOnly("SELECT 1; DELETE FROM t"));
    }

    @Test
    void requireReadOnlyThrowsOnWrite() {
        assertThrows(IllegalStateException.class, () -> SqlReadOnlyGuard.requireReadOnly("DELETE FROM t"));
    }
}
