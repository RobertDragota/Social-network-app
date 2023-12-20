package com.socialnetwork.map_toysocialnetwork.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDB_Utils {
    private final String URL;
    private final String username;
    private final String password;

    public JDB_Utils(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, username, password);
        } catch (SQLException ex) {
            System.out.println("Error getting connection" + ex);
        }
        return connection;
    }

}
