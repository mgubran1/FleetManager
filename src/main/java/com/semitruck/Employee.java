package com.semitruck;

import java.time.LocalDate;

public class Employee {
    private String driverId;
    private String name;
    private String role;
    private String company;
    private String ein;
    private String licenseNumber;
    private String licenseState;
    private String cdlClass;
    private String cdlEndorsements;
    private String cdlRestrictions;
    private LocalDate dob;
    private LocalDate dateOfHire;
    private String phone;
    private String email;
    private String address;
    private LocalDate medicalCardExpiry;
    private LocalDate cdlExpiry;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String status;
    private String notes;

    public Employee(
        String driverId,
        String name,
        String role,
        String company,
        String ein,
        String licenseNumber,
        String licenseState,
        String cdlClass,
        String cdlEndorsements,
        String cdlRestrictions,
        LocalDate dob,
        LocalDate dateOfHire,
        String phone,
        String email,
        String address,
        LocalDate medicalCardExpiry,
        LocalDate cdlExpiry,
        String emergencyContactName,
        String emergencyContactPhone,
        String status,
        String notes
    ) {
        this.driverId = driverId;
        this.name = name;
        this.role = role;
        this.company = company;
        this.ein = ein;
        this.licenseNumber = licenseNumber;
        this.licenseState = licenseState;
        this.cdlClass = cdlClass;
        this.cdlEndorsements = cdlEndorsements;
        this.cdlRestrictions = cdlRestrictions;
        this.dob = dob;
        this.dateOfHire = dateOfHire;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.medicalCardExpiry = medicalCardExpiry;
        this.cdlExpiry = cdlExpiry;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters for all fields
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }

    public String getRole() { return role; }
    public void setRole(String r) { this.role = r; }

    public String getCompany() { return company; }
    public void setCompany(String c) { this.company = c; }

    public String getEin() { return ein; }
    public void setEin(String e) { this.ein = e; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getLicenseState() { return licenseState; }
    public void setLicenseState(String licenseState) { this.licenseState = licenseState; }

    public String getCdlClass() { return cdlClass; }
    public void setCdlClass(String cdlClass) { this.cdlClass = cdlClass; }

    public String getCdlEndorsements() { return cdlEndorsements; }
    public void setCdlEndorsements(String cdlEndorsements) { this.cdlEndorsements = cdlEndorsements; }

    public String getCdlRestrictions() { return cdlRestrictions; }
    public void setCdlRestrictions(String cdlRestrictions) { this.cdlRestrictions = cdlRestrictions; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate d) { this.dob = d; }

    public LocalDate getDateOfHire() { return dateOfHire; }
    public void setDateOfHire(LocalDate dateOfHire) { this.dateOfHire = dateOfHire; }

    public String getPhone() { return phone; }
    public void setPhone(String p) { this.phone = p; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getMedicalCardExpiry() { return medicalCardExpiry; }
    public void setMedicalCardExpiry(LocalDate medicalCardExpiry) { this.medicalCardExpiry = medicalCardExpiry; }

    public LocalDate getCdlExpiry() { return cdlExpiry; }
    public void setCdlExpiry(LocalDate d) { this.cdlExpiry = d; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String name) { this.emergencyContactName = name; }

    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String phone) { this.emergencyContactPhone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}