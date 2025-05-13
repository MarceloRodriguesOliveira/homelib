package com.homelib.connection;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Log4j2
public class ConnectionFactory {
    private static final String URL = "jdbc:sqlite:C:\\Users\\Marcelo\\Documents\\Java\\homelib\\src\\main\\java\\com\\homelib\\db\\dbfortesting.db";
    public static Connection getConnection() throws SQLException {
        try{
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.\"");
            return conn;
        }catch (SQLException e){
            System.err.println("SQL connection could not be established. Check stack trace.");
            e.printStackTrace();
            log.error("SQL connection could not be established check stack strace");
            throw e;
        }
    }
}
