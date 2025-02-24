package com.example.taskmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the Task Management application.
 */
public class MainApplication extends Application {

    /**
     * Starts the application and loads the login screen.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the login screen from the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));

        // Create a new scene with the loaded layout
        Scene scene = new Scene(fxmlLoader.load());

        // Set the window title
        stage.setTitle("Task Management");

        // Set and show the scene
        stage.setScene(scene);
        stage.show();

        // Center the window on the screen
        stage.centerOnScreen();
    }

    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        launch();
    }
}
