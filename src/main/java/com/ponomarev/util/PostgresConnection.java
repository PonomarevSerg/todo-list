package com.ponomarev.util;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@RequiredArgsConstructor
public class PostgresConnection {
    private final String url;
    private final String user;
    private final String password;

    public Connection connection() {
        try {
            var connection = DriverManager.getConnection(url, getProperties());
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties getProperties() {
        var properties = new Properties();
        properties.put("user", user);
        properties.put("password", password);
        return properties;
    }
}
