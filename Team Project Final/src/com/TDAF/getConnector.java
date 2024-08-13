package com.TDAF;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
//This class was created by Joe, used to initialise a connection to the database when needed.
//using JDBC, and in our testing and development, a local MySQL server, we can connect to and manipulate the BAPERS database.
public class getConnector {
    private static final String URL = "jdbc:mysql://localhost/bapers";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public Connection getConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", USERNAME);
        connectionProps.put("password", PASSWORD);

        Connection connection = DriverManager.getConnection(URL, connectionProps);
        System.out.println("Connected to BAPERS Database.");
        return connection;
    }

}
