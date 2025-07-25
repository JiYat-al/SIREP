package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion_bd {

    private static Conexion_bd instancia;
    private Connection conexion;

    private final String user = "postgres";
    private final String password = "isaacadmin";
    private final String base = "sirep";
    private final String host = "localhost";
    private final String port = "5432";
    private final String url = "jdbc:postgresql://" + host + ":" + port + "/" + base;

    private Conexion_bd() {
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión a PostgreSQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized Conexion_bd  getInstancia() {
        if (instancia == null) {
            instancia = new Conexion_bd();
        } else {
            try {
                if (instancia.conexion == null || instancia.conexion.isClosed()) {
                    instancia = new Conexion_bd();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error verificando conexión: " + e.getMessage());
            }
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    /**Método adicional para compatibilidad con código anterior*/
    public static Connection getConnection() throws SQLException {
        return getInstancia().getConexion();
    }
}

