package com.semitruck;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class AddEditLoadDialogController {
    @FXML private TextField loadIdField;
    @FXML private ComboBox<String> driverComboBox;
    @FXML private DatePicker pickupDatePicker;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private TextField originField;
    @FXML private TextField destinationField;
    @FXML private TextField rateField;
    @FXML private TextField milesField;
    @FXML private TextField customerNameField;
    @FXML private TextField customerContactField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField brokerNameField;
    @FXML private TextField poNumberField;
    @FXML private TextField ratePerMileField;
    @FXML private TextField weightField;
    @FXML private TextField trailerTypeField;
    @FXML private TextField commodityField;
    @FXML private TextField invoiceNumberField;
    @FXML private CheckBox paidCheckBox;
    @FXML private TextField documentPathField;
    @FXML private Button browseDocumentButton;
    @FXML private TextArea notesField;

    private Stage dialogStage;
    private Load load;
    private boolean okClicked = false;
    private List<Employee> employeeList;

    @FXML
    private void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList(
            "Assigned", "Picked Up", "In Transit", "Delivered", "Canceled"
        ));
        if (browseDocumentButton != null) {
            browseDocumentButton.setOnAction(e -> handleBrowseDocument());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEmployeeList(List<Employee> employees) {
        this.employeeList = employees;
        if (driverComboBox != null && employees != null) {
            driverComboBox.setItems(FXCollections.observableArrayList(
                employees.stream().map(Employee::getName).toList()
            ));
        }
    }

    public void setLoad(Load load) {
        this.load = load;
        if (load != null) {
            loadIdField.setText(load.getLoadId());
            driverComboBox.setValue(load.getDriverName());
            pickupDatePicker.setValue(load.getPickupDate());
            deliveryDatePicker.setValue(load.getDeliveryDate());
            originField.setText(load.getOrigin());
            destinationField.setText(load.getDestination());
            rateField.setText(String.valueOf(load.getRate()));
            milesField.setText(String.valueOf(load.getMiles()));
            customerNameField.setText(load.getCustomerName());
            customerContactField.setText(load.getCustomerContact());
            statusComboBox.setValue(load.getStatus());
            brokerNameField.setText(load.getBrokerName());
            poNumberField.setText(load.getPoNumber());
            ratePerMileField.setText(String.valueOf(load.getRatePerMile()));
            weightField.setText(String.valueOf(load.getWeight()));
            trailerTypeField.setText(load.getTrailerType());
            commodityField.setText(load.getCommodity());
            invoiceNumberField.setText(load.getInvoiceNumber());
            paidCheckBox.setSelected(load.isPaid());
            documentPathField.setText(load.getDocumentPath());
            notesField.setText(load.getNotes());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Load getLoadFromFields() {
        String selectedDriverName = driverComboBox.getValue();
        String driverId = null;
        if (employeeList != null && selectedDriverName != null) {
            for (Employee e : employeeList) {
                if (selectedDriverName.equals(e.getName())) {
                    driverId = e.getDriverId();
                    break;
                }
            }
        }
        System.out.println("DEBUG: Saving load with driverId=" + driverId + ", driverName=" + selectedDriverName);

        // Validation: Must have valid driverId
        if (driverId == null || driverId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid driver.", ButtonType.OK);
            alert.showAndWait();
            return null;
        }

        return new Load(
            loadIdField.getText(),
            driverId, // must match employees
            selectedDriverName,
            pickupDatePicker.getValue(),
            deliveryDatePicker.getValue(),
            null, // pickup address (expand if you want)
            null, // delivery address
            originField.getText(),
            destinationField.getText(),
            parseDouble(rateField.getText()),
            notesField.getText(),
            customerNameField.getText(),
            customerContactField.getText(),
            statusComboBox.getValue(),
            parseInt(milesField.getText()),
            commodityField.getText(),
            parseDouble(weightField.getText()),
            trailerTypeField.getText(),
            poNumberField.getText(),
            brokerNameField.getText(),
            parseDouble(ratePerMileField.getText()),
            invoiceNumberField.getText(),
            paidCheckBox.isSelected(),
            documentPathField.getText()
        );
    }

    @FXML
    public void handleOk() {
        if (isInputValid()) {
            if (load != null) {
                Load fromFields = getLoadFromFields();
                if (fromFields == null) {
                    return; // e.g. driverId was invalid
                }
                load.setLoadId(fromFields.getLoadId());
                load.setDriverId(fromFields.getDriverId());
                load.setDriverName(fromFields.getDriverName());
                load.setPickupDate(fromFields.getPickupDate());
                load.setDeliveryDate(fromFields.getDeliveryDate());
                load.setOrigin(fromFields.getOrigin());
                load.setDestination(fromFields.getDestination());
                load.setRate(fromFields.getRate());
                load.setNotes(fromFields.getNotes());
                load.setCustomerName(fromFields.getCustomerName());
                load.setCustomerContact(fromFields.getCustomerContact());
                load.setStatus(fromFields.getStatus());
                load.setMiles(fromFields.getMiles());
                load.setCommodity(fromFields.getCommodity());
                load.setWeight(fromFields.getWeight());
                load.setTrailerType(fromFields.getTrailerType());
                load.setPoNumber(fromFields.getPoNumber());
                load.setBrokerName(fromFields.getBrokerName());
                load.setRatePerMile(fromFields.getRatePerMile());
                load.setInvoiceNumber(fromFields.getInvoiceNumber());
                load.setPaid(fromFields.isPaid());
                load.setDocumentPath(fromFields.getDocumentPath());
            }
            okClicked = true;
            if (dialogStage != null) dialogStage.close();
        }
    }

    @FXML
    public void handleCancel() {
        if (dialogStage != null) dialogStage.close();
    }

    private void handleBrowseDocument() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Document");
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            documentPathField.setText(file.getAbsolutePath());
        }
    }

    private boolean isInputValid() {
        return loadIdField.getText() != null && !loadIdField.getText().trim().isEmpty()
            && driverComboBox.getValue() != null && !driverComboBox.getValue().trim().isEmpty();
    }

    private double parseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    // --- For auto-mileage access ---
    public TextField getOriginField() { return originField; }
    public TextField getDestinationField() { return destinationField; }
    public TextField getMilesField() { return milesField; }
}