package com.semitruck;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class LoadsController {
    @FXML private TableView<Load> loadsTable;
    @FXML private TableColumn<Load, String> loadIdColumn;
    @FXML private TableColumn<Load, String> driverNameColumn;
    @FXML private TableColumn<Load, String> pickupDateColumn;
    @FXML private TableColumn<Load, String> deliveryDateColumn;
    @FXML private TableColumn<Load, String> originColumn;
    @FXML private TableColumn<Load, String> destinationColumn;
    @FXML private TableColumn<Load, String> rateColumn;
    @FXML private TableColumn<Load, String> milesColumn;
    @FXML private TableColumn<Load, String> statusColumn;
    @FXML private TableColumn<Load, String> customerNameColumn;
    @FXML private TableColumn<Load, String> customerContactColumn;
    @FXML private TableColumn<Load, String> brokerNameColumn;
    @FXML private TableColumn<Load, String> poNumberColumn;
    @FXML private TableColumn<Load, String> ratePerMileColumn;
    @FXML private TableColumn<Load, String> weightColumn;
    @FXML private TableColumn<Load, String> trailerTypeColumn;
    @FXML private TableColumn<Load, String> commodityColumn;
    @FXML private TableColumn<Load, String> invoiceNumberColumn;
    @FXML private TableColumn<Load, String> paidColumn;
    @FXML private TableColumn<Load, String> documentPathColumn;
    @FXML private TableColumn<Load, String> notesColumn;
    @FXML private TextField searchField;
    @FXML private Button addButton, editButton, deleteButton;

    private ObservableList<Load> masterData = FXCollections.observableArrayList();
    private FilteredList<Load> filteredData = new FilteredList<>(masterData, p -> true);
    private LoadDAO loadDAO = new LoadDAO();
    private List<Employee> employeeList;

    // Small US city/state sample. Expand as needed!
    private static final Map<String, double[]> CITY_STATE_TO_LATLON = Map.ofEntries(
        Map.entry("NEW YORK,NY", new double[]{40.7128, -74.0060}),
        Map.entry("LOS ANGELES,CA", new double[]{34.0522, -118.2437}),
        Map.entry("CHICAGO,IL", new double[]{41.8781, -87.6298}),
        Map.entry("DALLAS,TX", new double[]{32.7767, -96.7970}),
        Map.entry("MIAMI,FL", new double[]{25.7617, -80.1918}),
        Map.entry("DETROIT,MI", new double[]{42.3314, -83.0458}),
        Map.entry("ATLANTA,GA", new double[]{33.7490, -84.3880})
    );

    public void setEmployeeList(List<Employee> employees) {
        this.employeeList = employees;
    }

    @FXML
    public void initialize() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        loadIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLoadId()));
        driverNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDriverName()));
        pickupDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPickupDate() != null ? cell.getValue().getPickupDate().format(dateFmt) : ""));
        deliveryDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDeliveryDate() != null ? cell.getValue().getDeliveryDate().format(dateFmt) : ""));
        originColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOrigin()));
        destinationColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDestination()));
        rateColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRate())));
        milesColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getMiles())));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));
        customerNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomerName()));
        customerContactColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomerContact()));
        brokerNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBrokerName()));
        poNumberColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPoNumber()));
        ratePerMileColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getRatePerMile())));
        weightColumn.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getWeight())));
        trailerTypeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTrailerType()));
        commodityColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCommodity()));
        invoiceNumberColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getInvoiceNumber()));
        paidColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isPaid() ? "Yes" : "No"));
        documentPathColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDocumentPath()));
        notesColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNotes()));

        loadsTable.setItems(filteredData);

        searchField.textProperty().addListener((obs, old, val) -> {
            String filter = val == null ? "" : val.trim().toLowerCase();
            filteredData.setPredicate(load -> filter.isEmpty() ||
                    (load.getDriverName() != null && load.getDriverName().toLowerCase().contains(filter)) ||
                    (load.getOrigin() != null && load.getOrigin().toLowerCase().contains(filter)) ||
                    (load.getDestination() != null && load.getDestination().toLowerCase().contains(filter)) ||
                    (load.getCustomerName() != null && load.getCustomerName().toLowerCase().contains(filter)));
        });

        loadsTable.setRowFactory(tv -> {
            TableRow<Load> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    handleEditLoad();
                }
            });
            return row;
        });

        addButton.setOnAction(e -> handleAddLoad());
        editButton.setOnAction(e -> handleEditLoad());
        deleteButton.setOnAction(e -> handleDeleteLoad());

        refreshTable();
    }

    private void refreshTable() {
        try {
            masterData.setAll(loadDAO.getAllLoads());
        } catch (Exception ex) {
            showAlert("Error", "Could not load loads: " + ex.getMessage());
        }
    }

    private boolean isDuplicateLoadId(String loadId, String ignoreLoadId) {
        try {
            return loadDAO.getAllLoads().stream().anyMatch(
                l -> l.getLoadId().equalsIgnoreCase(loadId) && (ignoreLoadId == null || !l.getLoadId().equalsIgnoreCase(ignoreLoadId))
            );
        } catch (Exception e) {
            return false;
        }
    }

    private void handleAddLoad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEditLoadDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AddEditLoadDialogController controller = loader.getController();
            controller.setEmployeeList(employeeList);
            controller.setLoad(new Load("", "", "", null, null, "", "", "", "", 0, "", "", "", "", 0, "", 0.0, "", "", "", 0.0, "", false, ""));

            // Attach listener for miles auto-calc
            controller.getOriginField().textProperty().addListener((obs, old, val) -> updateMilesEstimate(controller));
            controller.getDestinationField().textProperty().addListener((obs, old, val) -> updateMilesEstimate(controller));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Load");
            dialog.setDialogPane(dialogPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    controller.handleOk();
                    if (controller.isOkClicked()) {
                        return ButtonType.OK;
                    }
                    return null;
                }
                controller.handleCancel();
                return dialogButton;
            });

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK && controller.isOkClicked()) {
                Load newLoad = controller.getLoadFromFields();
                if (isDuplicateLoadId(newLoad.getLoadId(), null)) {
                    showAlert("Error", "Duplicate Load ID. Each Load ID must be unique.");
                    return;
                }
                loadDAO.insertOrUpdateLoad(newLoad);
                refreshTable();
            }
        } catch (Exception ex) {
            showAlert("Error", "Could not add load: " + ex.getMessage());
        }
    }

    private void handleEditLoad() {
        Load selected = loadsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a load to edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEditLoadDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AddEditLoadDialogController controller = loader.getController();
            controller.setEmployeeList(employeeList);
            controller.setLoad(selected);

            // Attach listener for miles auto-calc
            controller.getOriginField().textProperty().addListener((obs, old, val) -> updateMilesEstimate(controller));
            controller.getDestinationField().textProperty().addListener((obs, old, val) -> updateMilesEstimate(controller));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Load");
            dialog.setDialogPane(dialogPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    controller.handleOk();
                    if (controller.isOkClicked()) {
                        return ButtonType.OK;
                    }
                    return null;
                }
                controller.handleCancel();
                return dialogButton;
            });

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK && controller.isOkClicked()) {
                Load updatedLoad = controller.getLoadFromFields();
                if (isDuplicateLoadId(updatedLoad.getLoadId(), selected.getLoadId())) {
                    showAlert("Error", "Duplicate Load ID. Each Load ID must be unique.");
                    return;
                }
                loadDAO.insertOrUpdateLoad(updatedLoad);
                refreshTable();
            }
        } catch (Exception ex) {
            showAlert("Error", "Could not edit load: " + ex.getMessage());
        }
    }

    private void handleDeleteLoad() {
        Load selected = loadsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a load to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this load?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                loadDAO.deleteLoad(selected.getLoadId());
                refreshTable();
            } catch (Exception ex) {
                showAlert("Error", "Could not delete load: " + ex.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    // --- AUTO-MILEAGE LOGIC ---
    private void updateMilesEstimate(AddEditLoadDialogController controller) {
        String origin = controller.getOriginField().getText();
        String destination = controller.getDestinationField().getText();
        int miles = estimateMilesFromText(origin, destination);
        if (miles > 0) {
            controller.getMilesField().setText(String.valueOf(miles));
        }
    }

    private int estimateMilesFromText(String origin, String destination) {
        String[] orig = origin.split(",");
        String[] dest = destination.split(",");
        if (orig.length == 2 && dest.length == 2) {
            return haversineEstimate(orig[0].trim(), orig[1].trim(), dest[0].trim(), dest[1].trim());
        }
        return -1;
    }

    // Returns miles or -1 if not found
    private int haversineEstimate(String origCity, String origState, String destCity, String destState) {
        String key1 = (origCity + "," + origState).toUpperCase();
        String key2 = (destCity + "," + destState).toUpperCase();

        double[] from = CITY_STATE_TO_LATLON.get(key1);
        double[] to = CITY_STATE_TO_LATLON.get(key2);
        if (from == null || to == null) return -1;

        double lat1 = from[0], lon1 = from[1];
        double lat2 = to[0], lon2 = to[1];
        double R = 3958.8; // Earth radius miles
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return (int)Math.round(distance);
    }
}