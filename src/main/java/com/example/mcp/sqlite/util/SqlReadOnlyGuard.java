package com.example.mcp.sqlite.util;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Detects whether an SQL statement is read-only (allowed under {@code immutable=true}).
 */
public final class SqlReadOnlyGuard {
    private static final Pattern LEADING_COMMENTS = Pattern.compile(
            "(?s)^(?:\\s|--[^\\n]*\\n|/\\*.*?\\*/)*"
    );
    private static final Pattern WRITE_KEYWORD = Pattern.compile(
            "(?i)\\b(?:INSERT|UPDATE|DELETE|REPLACE|DROP|CREATE|ALTER|TRUNCATE|ATTACH|DETACH|"
                    + "REINDEX|VACUUM|ANALYZE|GRANT|REVOKE)\\b"
    );

    private SqlReadOnlyGuard() {}

    /**
     * @return true if the statement is considered read-only for immutable mode
     */
    public static boolean isReadOnly(String sql) {
        Objects.requireNonNull(sql, "sql");
        String trimmed = LEADING_COMMENTS.matcher(sql).replaceFirst("").trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        // Reject multi-statement batches in immutable mode (second stmt could write).
        if (containsUnquotedSemicolonBeforeEnd(trimmed)) {
            return false;
        }

        String upper = stripQuotedLiterals(trimmed).toUpperCase(Locale.ROOT);
        if (upper.startsWith("SELECT") || upper.startsWith("EXPLAIN")) {
            return !WRITE_KEYWORD.matcher(upper).find();
        }
        if (upper.startsWith("WITH")) {
            // CTE may feed a write: WITH ... INSERT/UPDATE/DELETE/...
            return !WRITE_KEYWORD.matcher(upper).find();
        }
        if (upper.startsWith("PRAGMA")) {
            // Assignment form (PRAGMA name = value) mutates connection/DB state.
            return !upper.contains("=");
        }
        return false;
    }

    public static void requireReadOnly(String sql) {
        if (!isReadOnly(sql)) {
            throw new IllegalStateException(
                    "Write SQL is disabled because immutable=true "
                            + "(SQLite lock-bypass / snapshot mode). Only read-only statements are allowed.");
        }
    }

    private static String stripQuotedLiterals(String sql) {
        StringBuilder out = new StringBuilder(sql.length());
        boolean inSingle = false;
        boolean inDouble = false;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '\'' && !inDouble) {
                inSingle = !inSingle;
                out.append(' ');
                continue;
            }
            if (c == '"' && !inSingle) {
                inDouble = !inDouble;
                out.append(' ');
                continue;
            }
            out.append(inSingle || inDouble ? ' ' : c);
        }
        return out.toString();
    }

    private static boolean containsUnquotedSemicolonBeforeEnd(String sql) {
        boolean inSingle = false;
        boolean inDouble = false;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '\'' && !inDouble) {
                inSingle = !inSingle;
            } else if (c == '"' && !inSingle) {
                inDouble = !inDouble;
            } else if (c == ';' && !inSingle && !inDouble) {
                String rest = sql.substring(i + 1).trim();
                return !rest.isEmpty();
            }
        }
        return false;
    }
}
