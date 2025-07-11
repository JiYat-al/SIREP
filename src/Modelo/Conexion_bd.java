package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion_bd {

    // Configuración de la base de datos
    private static final String URL = "jdbc:postgresql://localhost:5432/SIREP";  // Cambié a SIREP
    private static final String USER = "postgres";
    private static final String PASSWORD = "hola";

    /**
     * Obtiene una nueva conexión a la base de datos
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver de PostgreSQL (opcional en versiones nuevas)
            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de PostgreSQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    /**
     * Prueba la conexión a la base de datos
     */
    public static boolean probarConexion() {
        try (Connection con = getConnection()) {
            System.out.println("✅ Conexión exitosa a la base de datos SIREP");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método main para probar la conexión
     */
    public static void main(String[] args) {
        System.out.println("🔄 Probando conexión a la base de datos...");

        if (probarConexion()) {
            System.out.println("🎉 La conexión funciona correctamente");
        } else {
            System.out.println("💥 La conexión falló");
        }
    }
}