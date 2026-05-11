package com.example.medimageia;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBase {

    private static final String URL =
            "jdbc:mysql://localhost:3306/medimageia";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connectDB() {

        Connection connect = null;

        try {

            connect = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Connexion réussie !");

        } catch (Exception e) {

            System.out.println("Erreur connexion DB");
            e.printStackTrace();
        }

        return connect;
    }
}