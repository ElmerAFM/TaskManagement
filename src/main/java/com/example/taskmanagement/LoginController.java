package com.example.taskmanagement;

import com.example.taskmanagement.models.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {

    @FXML
    private Label loginMessageLabel; // Label for login messages
    @FXML
    private TextField usernameField; // Field for entering username
    @FXML
    private PasswordField passwordField; // Field for entering password

    private int userID;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException, SQLException {
        // Check if username and password are provided
        if (!usernameField.getText().isBlank() && !passwordField.getText().isBlank()) {
            if (doLogin()) {
                // Login successful, switch to main screen
                switchToMain(event);
            } else {
                // Login failed, display error message
                loginMessageLabel.setText("Login failed");
            }
        } else {
            // If fields are blank, display a message
            loginMessageLabel.setText("Please enter your username and password");
        }
    }

    // Switch to the main screen after successful login
    protected void switchToMain(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setUserID(this.userID);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close(); // Close the current login screen
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); // Show the main screen
        stage.centerOnScreen(); // Center the main window
    }

    // Perform login check against the database
    private boolean doLogin() {
        Connection connection = DBConnection.getConnection(); // Get database connection
        String login = "Select count(1) as usercount, userID from users where email = '" + usernameField.getText() + "' and password = '" + passwordField.getText() + "' group by userID";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(login);
            while (result.next()) {
                if (result.getInt("usercount") == 1) {
                    this.userID = result.getInt("userID");
                    return true;// Return true if login is successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL errors
        }
        return false; // Return false if login fails
    }
}
