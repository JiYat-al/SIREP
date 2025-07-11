package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion_bd {

    public static void main(String[] args) {

        String user = "postgres";
        String password = "isaacadmin";

        try {

            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", user, password);
            System.out.println("Connected to the database");

        } catch (Exception e) {

            System.out.println("Could not connect to the database");

        }

    }

}
