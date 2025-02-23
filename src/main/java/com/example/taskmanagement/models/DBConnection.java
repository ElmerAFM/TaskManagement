package com.example.taskmanagement.models;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection conn;

    public static Connection getConnection() {
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
