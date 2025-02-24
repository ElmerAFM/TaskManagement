package com.example.taskmanagement;

import com.example.taskmanagement.models.DBConnection;
import com.example.taskmanagement.models.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {
    @FXML
    private TextField titleField; // Field for task title

    @FXML
    private TextArea descriptionField; // Field for task description

    @FXML
    private DatePicker duedateField; // Field for due date

    @FXML
    private ComboBox priorityField; // Dropdown for task priority

    @FXML
    private Label addMessageLabel; // Label for displaying messages

    private boolean update = false; // Flag to check if updating a task
    private int taskID; // Store task ID for updates
    private int userID; // Store user ID for updating/creating tasks

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set priority options when due date is selected
        duedateField.valueProperty().addListener((observable, oldValue, newValue) -> {
            priorityField.getItems().setAll("Low", "Medium", "High");
        });
    }

    @FXML
    void ok(ActionEvent event) throws SQLException {
        // Check for errors before proceeding
        if (Error()) {
            return;
        }

        // Get values from input fields
        String title = titleField.getText();
        String description = descriptionField.getText();
        String priority = priorityField.getValue().toString();
        LocalDate duedate = duedateField.getValue();

        try {
            Connection connection = DBConnection.getConnection(); // Get database connection
            if (update) {
                // Update existing task in the database
                String query = "UPDATE Tasks SET title = ?, description = ?, priority = ?, duedate = ? WHERE taskID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, priority);
                    preparedStatement.setDate(4, java.sql.Date.valueOf(duedate));
                    preparedStatement.setInt(5, taskID);
                    preparedStatement.executeUpdate(); // Execute update
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle any SQL errors
                }
            } else {
                // Insert a new task into the database
                String query = "INSERT INTO Tasks (title, description, priority, duedate, userID) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, priority);
                    preparedStatement.setDate(4, java.sql.Date.valueOf(duedate));
                    preparedStatement.setInt(5, this.userID);
                    preparedStatement.executeUpdate(); // Execute insert
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle any SQL errors
                }
            }

            connection.close(); // Close the database connection
            Stage currentStage = (Stage) titleField.getScene().getWindow();
            currentStage.close(); // Close the current window

        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL errors
        }
    }

    // Validate input fields for errors
    public boolean Error() {
        if (duedateField.getValue() == null || titleField.getText().isEmpty() || descriptionField.getText().isEmpty() || priorityField.getValue() == null) {
            addMessageLabel.setText("Please fill all the fields"); // Show error message
            return true; // Return true if there's an error
        }
        return false; // Return false if no errors
    }

    @FXML
    protected void cancel(ActionEvent event) throws IOException {
        // Close the current window when canceling
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    // Set fields for updating an existing task
    public void setUpdate(Task task) {
        this.titleField.setText(task.getTitle());
        this.descriptionField.setText(task.getDescription());
        this.duedateField.setValue(task.getDuedate());
        this.priorityField.setValue(task.getPriority());
        this.taskID = task.getTaskID();
        this.update = true; // Set update flag to true
    }
}
