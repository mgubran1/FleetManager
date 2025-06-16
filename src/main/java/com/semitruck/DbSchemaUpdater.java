package com.semitruck;

import java.sql.*;

public class DbSchemaUpdater {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:fleetapp.db")) {
            addColumnIfMissing(conn, "employees", "cdl_restrictions", "TEXT");
            addColumnIfMissing(conn, "employees", "dob", "TEXT");
            addColumnIfMissing(conn, "employees", "date_of_hire", "TEXT");
            addColumnIfMissing(conn, "employees", "phone", "TEXT");
            addColumnIfMissing(conn, "employees", "email", "TEXT");
            addColumnIfMissing(conn, "employees", "address", "TEXT");
            addColumnIfMissing(conn, "employees", "medical_card_expiry", "TEXT");
            addColumnIfMissing(conn, "employees", "cdl_expiry", "TEXT");
            addColumnIfMissing(conn, "employees", "emergency_contact_name", "TEXT");
            addColumnIfMissing(conn, "employees", "emergency_contact_phone", "TEXT");
            addColumnIfMissing(conn, "employees", "status", "TEXT");
            addColumnIfMissing(conn, "employees", "notes", "TEXT");
            // Add additional columns as needed to match your Employee model
            System.out.println("Schema update completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addColumnIfMissing(Connection conn, String table, String column, String type) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + table + ")");
            boolean found = false;
            while (rs.next()) {
                if (rs.getString("name").equalsIgnoreCase(column)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                String sql = "ALTER TABLE " + table + " ADD COLUMN " + column + " " + type;
                stmt.execute(sql);
                System.out.println("Added column: " + column + " to " + table);
            }
        }
    }
}