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
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker duedateField;

    @FXML
    private ComboBox priorityField;

    @FXML
    private Label addMessageLabel;

    private boolean update = false;
    private int taskID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        duedateField.valueProperty().addListener((observable, oldValue, newValue) -> {
            priorityField.getItems().setAll("Low", "Medium", "High");
        });
    }

    @FXML
    void ok(ActionEvent event) throws SQLException {
        if (Error()) {
            return;
        }

        String title = titleField.getText();
        String description = descriptionField.getText();
        String priority = priorityField.getValue().toString();
        LocalDate duedate = duedateField.getValue();

        try {
            Connection connection = DBConnection.getConnection();
            if (update) {
                // Update task in database
                String query = "UPDATE Tasks SET title = ?, description = ?, priority = ?, duedate = ? WHERE taskID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, priority);
                    preparedStatement.setDate(4, java.sql.Date.valueOf(duedate));
                    preparedStatement.setInt(5, taskID);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // Insert a new task
                String query = "INSERT INTO Tasks (title, description, priority, duedate) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, priority);
                    preparedStatement.setDate(4, java.sql.Date.valueOf(duedate));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            connection.close();
            Stage currentstage = (Stage) titleField.getScene().getWindow();
            currentstage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean Error() {
        if (duedateField.getValue() == null || titleField.getText().isEmpty() || descriptionField.getText().isEmpty() || priorityField.getValue() == null) {
            addMessageLabel.setText("Please fill all the fields");
            return true;
        }
        return false;
    }


    @FXML
    protected void cancel(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void setUpdate(Task task) {

        this.titleField.setText(task.getTitle());
        this.descriptionField.setText(task.getDescription());
        this.duedateField.setValue(task.getDuedate());
        this.priorityField.setValue(task.getPriority());
        this.taskID = task.getTaskID();
        this.update = true;

    }

}
