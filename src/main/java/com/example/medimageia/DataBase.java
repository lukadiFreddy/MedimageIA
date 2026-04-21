package com.example.medimageia;

import javax.xml.crypto.MarshalException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataBase {
    public static Connection connectDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/medimageia",
                    "root",
                    ""
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
