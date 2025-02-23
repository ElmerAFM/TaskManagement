package com.example.taskmanagement;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection conn;

    public Connection getConnection() {
        String url = "jdbc:postgresql:taskmanagement";
        String user = "postgres";
        String password = "password";

        try {
            conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
