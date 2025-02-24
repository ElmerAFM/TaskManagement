package com.example.taskmanagement.models;

import javafx.beans.property.SimpleBooleanProperty;

import java.sql.Date;
import java.time.LocalDate;

public class Task {

    int taskID; // Unique identifier for the task
    String title; // Title of the task
    SimpleBooleanProperty status; // Status of the task (completed or not)
    String description; // Description of the task
    String priority; // Priority level of the task (e.g., low, medium, high)
    LocalDate duedate; // Due date of the task

    // Constructor to initialize a task with the provided values
    public Task(int taskID, String name, boolean status, String description, String priority, Date duedate) {
        this.taskID = taskID; // Set the task ID
        this.title = name; // Set the task title
        this.status = new SimpleBooleanProperty(status); // Set the task status
        this.description = description; // Set the task description
        this.priority = priority; // Set the task priority
        if (duedate != null) {
            this.duedate = duedate.toLocalDate(); // Convert SQL Date to LocalDate if not null
        }
    }

    // Getter for task ID
    public int getTaskID() {
        return taskID;
    }

    // Setter for task ID
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    // Getter for task title
    public String getTitle() {
        return title;
    }

    // Setter for task title
    public void setTitle(String name) {
        this.title = name;
    }

    // Getter for task status
    public boolean getStatus() {
        return status.get();
    }

    // Setter for task status
    public void setStatus(boolean status) {
        this.status.set(status);
    }

    // Getter for task description
    public String getDescription() {
        return description;
    }

    // Setter for task description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for task priority
    public String getPriority() {
        return priority;
    }

    // Setter for task priority
    public void setPriority(String priority) {
        this.priority = priority;
    }

    // Getter for task due date
    public LocalDate getDuedate() {
        return duedate;
    }

    // Setter for task due date
    public void setDuedate(LocalDate duedate) {
        this.duedate = duedate;
    }
}
