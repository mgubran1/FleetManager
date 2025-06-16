package com.semitruck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize database schema
        try {
            DatabaseManager.initializeSchema();
        } catch (Exception ex) {
            // Show a simple error dialog, can use a helper if needed
            ex.printStackTrace();
            return;
        }

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/main_view.fxml"));
        BorderPane root = loader.load();

        Scene scene = new Scene(root, 1000, 700);
        String css = MainApp.class.getResource("/app.css") != null ? MainApp.class.getResource("/app.css").toExternalForm() : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        }
        stage.setTitle("Fleet Management System");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}