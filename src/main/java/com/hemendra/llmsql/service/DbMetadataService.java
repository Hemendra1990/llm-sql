package com.hemendra.llmsql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbMetadataService {
    private final DataSource dataSource;

    public String readDatabaseSchema() {
        StringBuilder schemaBuilder = new StringBuilder();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            schemaBuilder.append("Database Schema:\n");
            schemaBuilder.append("=================\n");

            // Get tables
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    schemaBuilder.append("Table: ").append(tableName).append("\n");
                    schemaBuilder.append("Columns:\n");

                    // Get columns for each table
                    try (ResultSet columns = metaData.getColumns(null, null, tableName, "%")) {
                        while (columns.next()) {
                            String columnName = columns.getString("COLUMN_NAME");
                            String dataType = columns.getString("TYPE_NAME");
                            int columnSize = columns.getInt("COLUMN_SIZE");
                            boolean isNullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable;

                            schemaBuilder
                                    .append("  - ")
                                    .append(columnName)
                                    .append(" (")
                                    .append(dataType)
                                    .append(", Size: ")
                                    .append(columnSize)
                                    .append(", Nullable: ")
                                    .append(isNullable)
                                    .append(")\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            schemaBuilder.append("Error fetching database schema: ").append(e.getMessage()).append("\n");
        }

        return schemaBuilder.toString();
    }

    public String generateDatabaseSchemaJson() {
        JSONObject schemaJson = new JSONObject();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            JSONArray tablesJson = new JSONArray();

            // Retrieve all tables
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    JSONObject tableJson = new JSONObject();
                    String tableName = tables.getString("TABLE_NAME");
                    tableJson.put("tableName", tableName);

                    // Retrieve columns for the table
                    JSONArray columnsJson = new JSONArray();
                    try (ResultSet columns = metaData.getColumns(null, null, tableName, "%")) {
                        while (columns.next()) {
                            JSONObject columnJson = new JSONObject();
                            columnJson.put("name", columns.getString("COLUMN_NAME"));
                            columnJson.put("type", columns.getString("TYPE_NAME"));
                            columnJson.put("size", columns.getInt("COLUMN_SIZE"));
                            columnJson.put("nullable", columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                            columnsJson.put(columnJson);
                        }
                    }

                    tableJson.put("columns", columnsJson);
                    tablesJson.put(tableJson);
                }
            }

            schemaJson.put("tables", tablesJson);

        } catch (Exception e) {
            schemaJson.put("error", "Error fetching database schema: " + e.getMessage());
        }

        return schemaJson.toString(2); // Pretty-print JSON with 2 spaces indentation
    }

    public String generate14DatabaseSchemaJson() throws SQLException {
        JSONObject schemaJson = new JSONObject();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://14.141.154.146:5433/44_crm", "postgres", "B6afIH?C+#W6Wrl@ED1@")) {
            DatabaseMetaData metaData = connection.getMetaData();

            JSONArray tablesJson = new JSONArray();

            // Retrieve all tables
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    JSONObject tableJson = new JSONObject();
                    String tableName = tables.getString("TABLE_NAME");
                    tableJson.put("tableName", tableName);

                    // Retrieve columns for the table
                    JSONArray columnsJson = new JSONArray();
                    try (ResultSet columns = metaData.getColumns(null, null, tableName, "%")) {
                        while (columns.next()) {
                            JSONObject columnJson = new JSONObject();
                            columnJson.put("name", columns.getString("COLUMN_NAME"));
                            columnJson.put("type", columns.getString("TYPE_NAME"));
                            columnJson.put("size", columns.getInt("COLUMN_SIZE"));
                            columnJson.put("nullable", columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                            columnsJson.put(columnJson);
                        }
                    }

                    tableJson.put("columns", columnsJson);
                    tablesJson.put(tableJson);
                }
            }

            schemaJson.put("tables", tablesJson);

        } catch (Exception e) {
            schemaJson.put("error", "Error fetching database schema: " + e.getMessage());
        }

        return schemaJson.toString(2); // Pretty-print JSON with 2 spaces indentation
    }
}
