package com.example.mcp.sqlite.util;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SqliteUtil {
    private SqliteUtil() {}

    /**
     * Builds a JDBC SQLite URL. When {@code immutable} is true, uses the SQLite URI form
     * {@code file:&lt;path&gt;?immutable=1} so the driver opens without taking file locks.
     */
    public static String jdbcUrl(Path databasePath, boolean immutable) {
        Objects.requireNonNull(databasePath, "databasePath");
        Path absolute = databasePath.toAbsolutePath().normalize();
        if (!immutable) {
            return "jdbc:sqlite:" + absolute;
        }
        String encodedPath = absolute.toUri().getRawPath();
        if (encodedPath == null || encodedPath.isEmpty()) {
            throw new IllegalArgumentException("Cannot build immutable JDBC URL for path: " + absolute);
        }
        return "jdbc:sqlite:file:" + encodedPath + "?immutable=1";
    }

    public static String quoteLiteral(String value) {
        if (value == null) {
            return "NULL";
        }
        return "'" + value.replace("'", "''") + "'";
    }

    public static List<Map<String, Object>> toRowList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                row.put(columnName, resultSet.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }
}
