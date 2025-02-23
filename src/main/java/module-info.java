module com.example.taskmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.taskmanagement to javafx.fxml;
    exports com.example.taskmanagement;
    exports com.example.taskmanagement.models;
    opens com.example.taskmanagement.models to javafx.fxml;
}