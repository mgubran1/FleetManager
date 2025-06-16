package com.semitruck;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EmployeeDAO {

    public static List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getString("driver_id"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getString("company"),
                    rs.getString("ein"),
                    rs.getString("license_number"),
                    rs.getString("license_state"),
                    rs.getString("cdl_class"),
                    rs.getString("cdl_endorsements"),
                    rs.getString("cdl_restrictions"),
                    parseDate(rs.getString("dob")),
                    parseDate(rs.getString("date_of_hire")),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    parseDate(rs.getString("medical_card_expiry")),
                    parseDate(rs.getString("cdl_expiry")),
                    rs.getString("emergency_contact_name"),
                    rs.getString("emergency_contact_phone"),
                    rs.getString("status"),
                    rs.getString("notes")
                );
                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertOrUpdateEmployee(Employee emp) {
        String sql = """
            INSERT INTO employees (
                driver_id, name, role, company, ein, license_number, license_state, cdl_class, cdl_endorsements, cdl_restrictions,
                dob, date_of_hire, phone, email, address, medical_card_expiry, cdl_expiry, emergency_contact_name, emergency_contact_phone, status, notes
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            ON CONFLICT(driver_id) DO UPDATE SET
                name=excluded.name, role=excluded.role, company=excluded.company, ein=excluded.ein,
                license_number=excluded.license_number, license_state=excluded.license_state,
                cdl_class=excluded.cdl_class, cdl_endorsements=excluded.cdl_endorsements,
                cdl_restrictions=excluded.cdl_restrictions, dob=excluded.dob, date_of_hire=excluded.date_of_hire,
                phone=excluded.phone, email=excluded.email, address=excluded.address,
                medical_card_expiry=excluded.medical_card_expiry, cdl_expiry=excluded.cdl_expiry,
                emergency_contact_name=excluded.emergency_contact_name,
                emergency_contact_phone=excluded.emergency_contact_phone,
                status=excluded.status, notes=excluded.notes
            ;
        """;
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, emp.getDriverId());
            ps.setString(2, emp.getName());
            ps.setString(3, emp.getRole());
            ps.setString(4, emp.getCompany());
            ps.setString(5, emp.getEin());
            ps.setString(6, emp.getLicenseNumber());
            ps.setString(7, emp.getLicenseState());
            ps.setString(8, emp.getCdlClass());
            ps.setString(9, emp.getCdlEndorsements());
            ps.setString(10, emp.getCdlRestrictions());
            ps.setString(11, emp.getDob() != null ? emp.getDob().toString() : null);
            ps.setString(12, emp.getDateOfHire() != null ? emp.getDateOfHire().toString() : null);
            ps.setString(13, emp.getPhone());
            ps.setString(14, emp.getEmail());
            ps.setString(15, emp.getAddress());
            ps.setString(16, emp.getMedicalCardExpiry() != null ? emp.getMedicalCardExpiry().toString() : null);
            ps.setString(17, emp.getCdlExpiry() != null ? emp.getCdlExpiry().toString() : null);
            ps.setString(18, emp.getEmergencyContactName());
            ps.setString(19, emp.getEmergencyContactPhone());
            ps.setString(20, emp.getStatus());
            ps.setString(21, emp.getNotes());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmployee(String driverId) {
        String sql = "DELETE FROM employees WHERE driver_id = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, driverId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static LocalDate parseDate(String s) {
        return (s == null || s.isEmpty()) ? null : LocalDate.parse(s);
    }
}