package com.semitruck;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.util.List;

public class MainViewController {

    @FXML private TabPane mainTabPane;
    @FXML private Tab employeesTab;
    @FXML private Tab loadsTab;
    @FXML private Pane employeesTabPane;
    @FXML private Pane loadsTabPane;

    // These will be set after loading their FXMLs
    private EmployeesController employeesController;
    private LoadsController loadsController;

    private Connection conn;

    @FXML
    private void initialize() throws Exception {
        conn = DatabaseManager.getConnection();

        // Load employees tab and controller
        FXMLLoader employeesLoader = new FXMLLoader(MainApp.class.getResource("/employees_view.fxml"));
        Pane employeesPane = employeesLoader.load();
        employeesTabPane.getChildren().setAll(employeesPane);
        employeesController = employeesLoader.getController();
        // employeesController.setConnection(conn); // No longer needed if EmployeesController fetches its own connection

        // Load loads tab and controller
        FXMLLoader loadsLoader = new FXMLLoader(MainApp.class.getResource("/LoadsTab.fxml"));
        Pane loadsPane = loadsLoader.load();
        loadsTabPane.getChildren().setAll(loadsPane);
        loadsController = loadsLoader.getController();
        // loadsController.setConnection(conn); // REMOVE THIS LINE! LoadsController manages DB access itself

        // Propagate employee list to Loads tab
        List<Employee> employeeList = employeesController.getEmployeeList();
        loadsController.setEmployeeList(employeeList);

        // If employees are updated, update Loads tab too
        employeesController.setOnEmployeeListUpdated(updatedList -> {
            loadsController.setEmployeeList(updatedList);
        });

        employeesTab.setClosable(false);
        loadsTab.setClosable(false);
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fleet Management System\nVersion 1.0", ButtonType.OK);
        alert.setHeaderText("About");
        alert.showAndWait();
    }
}