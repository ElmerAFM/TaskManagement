package com.example.taskmanagement.models;

import javafx.beans.property.SimpleBooleanProperty;

import java.sql.Date;
import java.time.LocalDate;

public class Task {

    int taskID;
    String title;
    SimpleBooleanProperty status;
    String description;
    String priority;
    LocalDate duedate;

    public Task(int taskID, String name, boolean status, String description, String priority, Date duedate) {
        this.taskID = taskID;
        this.title = name;
        this.status = new SimpleBooleanProperty(status);
        this.description = description;
        this.priority = priority;
        if (duedate != null) {
            this.duedate = duedate.toLocalDate();
        }
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public boolean getStatus() {
        return status.get();
    }

    public void setStatus(boolean status) {
        this.status.set(status);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDuedate() {
        return duedate;
    }

    public void setDuedate(LocalDate duedate) {
        this.duedate = duedate;
    }
}