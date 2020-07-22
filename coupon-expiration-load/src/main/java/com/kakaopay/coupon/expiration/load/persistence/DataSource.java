package com.kakaopay.coupon.expiration.load.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private final String url;
    private final String username;
    private final String password;

    private Connection connection;

    public DataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException exception) {
            System.out.println("Database Connection Fail.");
            exception.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException exception) {
            System.out.println("Database Disconnection Fail.");
            exception.printStackTrace();
        }
    }
}
