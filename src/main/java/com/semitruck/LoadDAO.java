package com.semitruck;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoadDAO {

    public LoadDAO() {}

    public void insertOrUpdateLoad(Load load) throws SQLException {
        String sql = """
            INSERT INTO loads (
                load_id, driver_id, driver_name, pickup_date, delivery_date, pickup_address, delivery_address, origin,
                destination, rate, notes, customer_name, customer_contact, status, miles, commodity, weight, trailer_type,
                po_number, broker_name, rate_per_mile, invoice_number, paid, document_path
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(load_id) DO UPDATE SET
                driver_id=excluded.driver_id,
                driver_name=excluded.driver_name,
                pickup_date=excluded.pickup_date,
                delivery_date=excluded.delivery_date,
                pickup_address=excluded.pickup_address,
                delivery_address=excluded.delivery_address,
                origin=excluded.origin,
                destination=excluded.destination,
                rate=excluded.rate,
                notes=excluded.notes,
                customer_name=excluded.customer_name,
                customer_contact=excluded.customer_contact,
                status=excluded.status,
                miles=excluded.miles,
                commodity=excluded.commodity,
                weight=excluded.weight,
                trailer_type=excluded.trailer_type,
                po_number=excluded.po_number,
                broker_name=excluded.broker_name,
                rate_per_mile=excluded.rate_per_mile,
                invoice_number=excluded.invoice_number,
                paid=excluded.paid,
                document_path=excluded.document_path
        """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, load.getLoadId());
            ps.setString(2, load.getDriverId()); // Ensure driverId is NOT null!
            ps.setString(3, load.getDriverName());
            ps.setString(4, load.getPickupDate() != null ? load.getPickupDate().toString() : null);
            ps.setString(5, load.getDeliveryDate() != null ? load.getDeliveryDate().toString() : null);
            ps.setString(6, load.getPickupAddress());
            ps.setString(7, load.getDeliveryAddress());
            ps.setString(8, load.getOrigin());
            ps.setString(9, load.getDestination());
            ps.setDouble(10, load.getRate());
            ps.setString(11, load.getNotes());
            ps.setString(12, load.getCustomerName());
            ps.setString(13, load.getCustomerContact());
            ps.setString(14, load.getStatus());
            ps.setInt(15, load.getMiles());
            ps.setString(16, load.getCommodity());
            ps.setDouble(17, load.getWeight());
            ps.setString(18, load.getTrailerType());
            ps.setString(19, load.getPoNumber());
            ps.setString(20, load.getBrokerName());
            ps.setDouble(21, load.getRatePerMile());
            ps.setString(22, load.getInvoiceNumber());
            ps.setInt(23, load.isPaid() ? 1 : 0);
            ps.setString(24, load.getDocumentPath());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting/updating load: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Load> getAllLoads() throws SQLException {
        String sql = "SELECT * FROM loads";
        List<Load> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Load load = new Load(
                    rs.getString("load_id"),
                    rs.getString("driver_id"),
                    rs.getString("driver_name"),
                    parseDate(rs.getString("pickup_date")),
                    parseDate(rs.getString("delivery_date")),
                    rs.getString("pickup_address"),
                    rs.getString("delivery_address"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getDouble("rate"),
                    rs.getString("notes"),
                    rs.getString("customer_name"),
                    rs.getString("customer_contact"),
                    rs.getString("status"),
                    rs.getInt("miles"),
                    rs.getString("commodity"),
                    rs.getDouble("weight"),
                    rs.getString("trailer_type"),
                    rs.getString("po_number"),
                    rs.getString("broker_name"),
                    rs.getDouble("rate_per_mile"),
                    rs.getString("invoice_number"),
                    rs.getInt("paid") == 1,
                    rs.getString("document_path")
                );
                list.add(load);
            }
        }
        return list;
    }

    public void deleteLoad(String loadId) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM loads WHERE load_id = ?")) {
            ps.setString(1, loadId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting load: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private LocalDate parseDate(String s) {
        try {
            return s != null ? LocalDate.parse(s) : null;
        } catch (Exception e) {
            return null;
        }
    }
}