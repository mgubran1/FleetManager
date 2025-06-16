package com.semitruck;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.UUID;

public class AddEditEmployeeDialogController {
    @FXML public TextField driverIdField;
    @FXML public TextField nameField;
    @FXML public ComboBox<String> roleField;
    @FXML public TextField companyField;
    @FXML public TextField einField;
    @FXML public TextField licenseNumberField;
    @FXML public TextField licenseStateField;
    @FXML public ComboBox<String> cdlClassField;
    @FXML public TextField cdlEndorsementsField;
    @FXML public TextField cdlRestrictionsField;
    @FXML public DatePicker dobField;
    @FXML public DatePicker dateOfHireField;
    @FXML public TextField phoneField;
    @FXML public TextField emailField;
    @FXML public TextField addressField;
    @FXML public DatePicker medicalCardExpiryField;
    @FXML public DatePicker cdlExpiryField;
    @FXML public TextField emergencyContactNameField;
    @FXML public TextField emergencyContactPhoneField;
    @FXML public ComboBox<String> statusField;
    @FXML public TextArea notesField;

    public void setComboBoxOptions() {
        roleField.setItems(javafx.collections.FXCollections.observableArrayList("Owner Operator", "Company Driver"));
        cdlClassField.setItems(javafx.collections.FXCollections.observableArrayList("A", "B", "C", "Other"));
        statusField.setItems(javafx.collections.FXCollections.observableArrayList("Active", "Suspended", "Terminated", "On Leave"));
    }

    public void setEmployee(Employee emp) {
        if (emp == null) {
            driverIdField.setText("DRV" + UUID.randomUUID().toString().substring(0, 8));
            nameField.clear();
            roleField.setValue(null);
            companyField.clear();
            einField.clear();
            licenseNumberField.clear();
            licenseStateField.clear();
            cdlClassField.setValue(null);
            cdlEndorsementsField.clear();
            cdlRestrictionsField.clear();
            dobField.setValue(null);
            dateOfHireField.setValue(null);
            phoneField.clear();
            emailField.clear();
            addressField.clear();
            medicalCardExpiryField.setValue(null);
            cdlExpiryField.setValue(null);
            emergencyContactNameField.clear();
            emergencyContactPhoneField.clear();
            statusField.setValue(null);
            notesField.clear();
        } else {
            driverIdField.setText(emp.getDriverId());
            nameField.setText(emp.getName());
            roleField.setValue(emp.getRole());
            companyField.setText(emp.getCompany());
            einField.setText(emp.getEin());
            licenseNumberField.setText(emp.getLicenseNumber());
            licenseStateField.setText(emp.getLicenseState());
            cdlClassField.setValue(emp.getCdlClass());
            cdlEndorsementsField.setText(emp.getCdlEndorsements());
            cdlRestrictionsField.setText(emp.getCdlRestrictions());
            dobField.setValue(emp.getDob());
            dateOfHireField.setValue(emp.getDateOfHire());
            phoneField.setText(emp.getPhone());
            emailField.setText(emp.getEmail());
            addressField.setText(emp.getAddress());
            medicalCardExpiryField.setValue(emp.getMedicalCardExpiry());
            cdlExpiryField.setValue(emp.getCdlExpiry());
            emergencyContactNameField.setText(emp.getEmergencyContactName());
            emergencyContactPhoneField.setText(emp.getEmergencyContactPhone());
            statusField.setValue(emp.getStatus());
            notesField.setText(emp.getNotes());
        }
    }

    public Employee getEmployeeFromFields() {
        return new Employee(
                driverIdField.getText().trim(),
                nameField.getText().trim(),
                roleField.getValue(),
                companyField.getText().trim(),
                einField.getText().trim(),
                licenseNumberField.getText().trim(),
                licenseStateField.getText().trim(),
                cdlClassField.getValue(),
                cdlEndorsementsField.getText().trim(),
                cdlRestrictionsField.getText().trim(),
                dobField.getValue(),
                dateOfHireField.getValue(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                addressField.getText().trim(),
                medicalCardExpiryField.getValue(),
                cdlExpiryField.getValue(),
                emergencyContactNameField.getText().trim(),
                emergencyContactPhoneField.getText().trim(),
                statusField.getValue(),
                notesField.getText()
        );
    }
}