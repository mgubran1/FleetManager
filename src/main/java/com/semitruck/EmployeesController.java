package com.semitruck;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EmployeesController {

    @FXML private TableView<Employee> table;
    @FXML private TableColumn<Employee, String> driverIdColumn;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> roleColumn;
    @FXML private TableColumn<Employee, String> companyColumn;
    @FXML private TableColumn<Employee, String> einColumn;
    @FXML private TableColumn<Employee, String> licenseNumberColumn;

    @FXML private TableColumn<Employee, LocalDate> dobColumn;
    @FXML private TableColumn<Employee, LocalDate> dateOfHireColumn;
    @FXML private TableColumn<Employee, LocalDate> medicalCardExpiryColumn;
    @FXML private TableColumn<Employee, LocalDate> cdlExpiryColumn;

    @FXML private TableColumn<Employee, String> licenseStateColumn;
    @FXML private TableColumn<Employee, String> cdlClassColumn;
    @FXML private TableColumn<Employee, String> cdlEndorsementsColumn;
    @FXML private TableColumn<Employee, String> cdlRestrictionsColumn;
    @FXML private TableColumn<Employee, String> phoneColumn;
    @FXML private TableColumn<Employee, String> emailColumn;
    @FXML private TableColumn<Employee, String> addressColumn;
    @FXML private TableColumn<Employee, String> emergencyContactNameColumn;
    @FXML private TableColumn<Employee, String> emergencyContactPhoneColumn;
    @FXML private TableColumn<Employee, String> statusColumn;
    @FXML private TableColumn<Employee, String> notesColumn;

    @FXML private TextField searchField;

    private final ObservableList<Employee> masterData = FXCollections.observableArrayList();
    private final FilteredList<Employee> filteredData = new FilteredList<>(masterData, p -> true);

    // For propagating updates to other controllers (like Loads tab)
    private Consumer<List<Employee>> onEmployeeListUpdated;

    // Optionally store the connection if you need it for DAOs, etc.
    private Connection conn;

    public void setConnection(Connection conn) {
        this.conn = conn;
        // If you want to reinitialize DAOs with this connection, do it here.
    }

    public void setOnEmployeeListUpdated(Consumer<List<Employee>> callback) {
        this.onEmployeeListUpdated = callback;
    }

    public List<Employee> getEmployeeList() {
        return FXCollections.unmodifiableObservableList(masterData);
    }

    @FXML
    public void initialize() {
        try {
            DatabaseManager.initializeSchema();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Database initialization failed: " + e.getMessage());
        }

        driverIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDriverId()));
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        roleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRole()));
        companyColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCompany()));
        einColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEin()));
        licenseNumberColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLicenseNumber()));

        dobColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dob"));
        dateOfHireColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateOfHire"));
        medicalCardExpiryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("medicalCardExpiry"));
        cdlExpiryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cdlExpiry"));

        licenseStateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLicenseState()));
        cdlClassColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCdlClass()));
        cdlEndorsementsColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCdlEndorsements()));
        cdlRestrictionsColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCdlRestrictions()));
        phoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
        emailColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        addressColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
        emergencyContactNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmergencyContactName()));
        emergencyContactPhoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmergencyContactPhone()));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));
        notesColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNotes()));

        cdlExpiryColumn.setCellFactory(getExpiryCellFactory());
        medicalCardExpiryColumn.setCellFactory(getExpiryCellFactory());

        searchField.textProperty().addListener((obs, old, val) -> {
            String filter = val == null ? "" : val.trim().toLowerCase();
            filteredData.setPredicate(emp -> filter.isEmpty() ||
                    emp.getName().toLowerCase().contains(filter) ||
                    emp.getCompany().toLowerCase().contains(filter) ||
                    emp.getLicenseNumber().toLowerCase().contains(filter));
        });
        table.setItems(filteredData);

        table.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleEditPopup(row.getItem());
                }
            });
            return row;
        });

        refreshFromDatabase();
    }

    private Callback<TableColumn<Employee, LocalDate>, TableCell<Employee, LocalDate>> getExpiryCellFactory() {
        return col -> new TableCell<Employee, LocalDate>() {
            @Override
            protected void updateItem(LocalDate expiry, boolean empty) {
                super.updateItem(expiry, empty);
                if (empty || expiry == null) {
                    setText(null); setStyle("");
                } else {
                    setText(expiry.toString());
                    LocalDate today = LocalDate.now();
                    Period period = Period.between(today, expiry);
                    if (expiry.isBefore(today)) {
                        setTextFill(Color.RED);
                        setStyle("-fx-font-weight: bold;");
                    } else if (period.getMonths() < 3 && period.getYears() == 0) {
                        setTextFill(Color.ORANGE);
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setTextFill(Color.BLACK);
                        setStyle("");
                    }
                }
            }
        };
    }

    @FXML
    private void handleAddPopup() {
        handleAddOrEditPopup(null);
    }

    @FXML
    private void handleEditPopup() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleAddOrEditPopup(selected);
        }
    }

    private void handleEditPopup(Employee employee) {
        handleAddOrEditPopup(employee);
    }

    private void handleAddOrEditPopup(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEditEmployeeDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AddEditEmployeeDialogController controller = loader.getController();
            controller.setComboBoxOptions();

            if (employee == null) { // Add
                controller.setEmployee(null); // initialize empty
            } else { // Edit
                controller.setEmployee(employee);
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(employee == null ? "Add Driver" : "Edit Driver");
            dialog.setDialogPane(dialogPane);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Employee newEmp = controller.getEmployeeFromFields();

                // Check for duplicates (excluding itself)
                if (isDuplicate(newEmp, employee)) {
                    showError("Duplicate driver detected (same Name, DOB, License Number, or EIN).");
                    return;
                }

                EmployeeDAO.insertOrUpdateEmployee(newEmp);
                refreshFromDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected employee?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            EmployeeDAO.deleteEmployee(selected.getDriverId());
            refreshFromDatabase();
        }
    }

    private boolean isDuplicate(Employee newEmp, Employee ignore) {
        List<Employee> employees = EmployeeDAO.getAllEmployees();
        for (Employee e : employees) {
            if (ignore != null && e.getDriverId().equals(ignore.getDriverId())) continue;
            if (e.getName().equalsIgnoreCase(newEmp.getName())
                    && e.getDob() != null && newEmp.getDob() != null && e.getDob().equals(newEmp.getDob())
                    && e.getLicenseNumber().equalsIgnoreCase(newEmp.getLicenseNumber())) {
                return true;
            }
            if (e.getEin() != null && newEmp.getEin() != null
                    && !e.getEin().isEmpty() && !newEmp.getEin().isEmpty()
                    && e.getEin().equalsIgnoreCase(newEmp.getEin())) {
                return true;
            }
        }
        return false;
    }

    private void refreshFromDatabase() {
        List<Employee> employees = EmployeeDAO.getAllEmployees();
        masterData.setAll(employees);
        if (onEmployeeListUpdated != null) {
            onEmployeeListUpdated.accept(employees);
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText("Error");
        alert.showAndWait();
    }
}