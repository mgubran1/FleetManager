package com.semitruck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:fleetapp.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        }
        return connection;
    }

    public static void initializeSchema() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Employees table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees (
                    driver_id TEXT PRIMARY KEY,
                    name TEXT,
                    role TEXT,
                    company TEXT,
                    ein TEXT,
                    license_number TEXT,
                    license_state TEXT,
                    cdl_class TEXT,
                    cdl_endorsements TEXT
                )
            """);

            // Loads table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS loads (
                    load_id TEXT PRIMARY KEY,
                    driver_id TEXT,
                    driver_name TEXT,
                    pickup_date TEXT,
                    delivery_date TEXT,
                    pickup_address TEXT,
                    delivery_address TEXT,
                    origin TEXT,
                    destination TEXT,
                    rate REAL,
                    notes TEXT,
                    customer_name TEXT,
                    customer_contact TEXT,
                    status TEXT,
                    miles INTEGER,
                    commodity TEXT,
                    weight REAL,
                    trailer_type TEXT,
                    po_number TEXT,
                    broker_name TEXT,
                    rate_per_mile REAL,
                    invoice_number TEXT,
                    paid INTEGER,
                    document_path TEXT,
                    FOREIGN KEY(driver_id) REFERENCES employees(driver_id)
                )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {}
        }
    }
}