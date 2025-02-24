package com.example.taskmanagement;

import com.example.taskmanagement.models.DBConnection;
import com.example.taskmanagement.models.Task;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<Task> tasksTable; // Table for displaying tasks
    @FXML
    private TableColumn<Task, String> idCol; // Column for task ID
    @FXML
    private TableColumn<Task, Boolean> checkCol; // Column for task status checkbox
    @FXML
    private TableColumn<Task, String> titleCol; // Column for task title
    @FXML
    private TableColumn<Task, String> priorityCol; // Column for task priority
    @FXML
    private TableColumn<Task, String> actionCol; // Column for action buttons (edit/delete)

    private Connection connection = null; // Database connection
    private ObservableList<Task> taskList = FXCollections.observableArrayList(); // List of tasks

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadData(); // Load tasks from the database
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void addTask(ActionEvent event) throws IOException {
        // Load the add task screen
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("addtask.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        // Refresh task list when the add task window is closed
        stage.setOnHiding(hideEvent -> {
            try {
                loadData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void signOut(ActionEvent event) throws IOException {
        // Load the login screen
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close(); // Close current window
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    private void loadData() throws SQLException {
        this.connection = DBConnection.getConnection(); // Connect to the database
        refreshTaskList(); // Refresh the task list

        // Set up the table columns
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        this.titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        this.checkCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add Edit and Delete buttons to each row
        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory = (TableColumn<Task, String> param) -> new TableCell<Task, String>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                // Style buttons
                editButton.getStyleClass().add("btn-edit");
                deleteButton.getStyleClass().add("btn-delete");

                // Handle Delete Button Click
                deleteButton.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    deleteTask(task); // Call delete method
                });

                // Handle Edit Button Click
                editButton.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    try {
                        editTask(task); // Call edit method
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox buttonsBox = new HBox(10, editButton, deleteButton); // Box for buttons
                    buttonsBox.setAlignment(Pos.CENTER);
                    setGraphic(buttonsBox);
                    setText(null);
                }
            }
        };

        // Checkbox for updating task status
        checkCol.setCellFactory(column -> new TableCell<Task, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setStyle("-fx-cursor: hand;");

                // Update database when checkbox is clicked
                checkBox.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    boolean newStatus = checkBox.isSelected();
                    task.setStatus(newStatus); // Update task status

                    // Update the database
                    String query = "UPDATE Tasks SET status = ? WHERE taskID = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setBoolean(1, newStatus);
                        preparedStatement.setInt(2, task.getTaskID());
                        preparedStatement.executeUpdate(); // Execute update
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item != null && item);
                    setGraphic(checkBox); // Set checkbox graphic
                }
            }
        });

        actionCol.setCellFactory(cellFactory); // Set custom cell factory for action buttons
        this.tasksTable.setItems(taskList); // Populate the table with tasks
    }

    @FXML
    private void editTask(Task task) throws IOException {
        // Load the add task screen for editing
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("addtask.fxml"));
        Parent root = fxmlLoader.load();
        AddTaskController controller = fxmlLoader.getController();
        controller.setUpdate(task); // Pass the task to update
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        // Refresh task list when the edit task window is closed
        stage.setOnHiding(hideEvent -> {
            try {
                loadData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void deleteTask(Task task) {
        // Confirm deletion of task
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this task?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                deleteTaskFromDatabase(task); // Delete from database
                tasksTable.getItems().remove(task); // Remove from table
            }
        });
    }

    private void deleteTaskFromDatabase(Task task) {
        // Delete task from the database
        String query = "DELETE FROM tasks WHERE taskID = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, task.getTaskID());
            stmt.executeUpdate(); // Execute delete
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTaskList() throws SQLException {
        try {
            taskList.clear(); // Clear existing tasks
            String query = "SELECT * FROM TASKS"; // Query to get tasks
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                // Add tasks to the list
                taskList.add(new Task(
                        result.getInt("taskID"),
                        result.getString("title"),
                        result.getBoolean("status"),
                        result.getString("description"),
                        result.getString("priority"),
                        result.getDate("duedate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
