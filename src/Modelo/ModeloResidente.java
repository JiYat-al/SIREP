package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloResidente {
    private int numeroControl;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String carrera;
    private int semestre;
    private String correo;
    private String telefono;
    private int idProyecto;

    // Constantes para la conexión
    private static final String URL = "jdbc:postgresql://localhost:5432/SIREP";
    private static final String USER = "postgres";
    private static final String PASSWORD = "hola";

    public ModeloResidente() {
        // Constructor vacío
    }

    public ModeloResidente(int numeroControl, String nombre, String apellidoPaterno, String apellidoMaterno,
                           String carrera, int semestre, String correo, String telefono, int idProyecto) {
        this.numeroControl = numeroControl;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.carrera = carrera;
        this.semestre = semestre;
        this.correo = correo;
        this.telefono = telefono;
        this.idProyecto = idProyecto;
    }

    // Método para obtener conexión
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Método para probar conexión
    private static boolean probarConexion() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            return false;
        }
    }

    // ==================== MÉTODOS DE BASE DE DATOS ====================

    /**
     * Insertar un residente en la base de datos
     */
    public boolean insertar() {
        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "carrera, semestre, correo, telefono, id_proyecto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.numeroControl);
            stmt.setString(2, this.nombre);
            stmt.setString(3, this.apellidoPaterno);
            stmt.setString(4, this.apellidoMaterno);
            stmt.setString(5, this.carrera);
            stmt.setInt(6, this.semestre);
            stmt.setString(7, this.correo);
            stmt.setString(8, this.telefono);
            stmt.setInt(9, this.idProyecto);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar residente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualizar un residente en la base de datos
     */
    public boolean actualizar() {
        String sql = "UPDATE residente SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                "carrera = ?, semestre = ?, correo = ?, telefono = ?, id_proyecto = ? " +
                "WHERE numero_control = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.nombre);
            stmt.setString(2, this.apellidoPaterno);
            stmt.setString(3, this.apellidoMaterno);
            stmt.setString(4, this.carrera);
            stmt.setInt(5, this.semestre);
            stmt.setString(6, this.correo);
            stmt.setString(7, this.telefono);
            stmt.setInt(8, this.idProyecto);
            stmt.setInt(9, this.numeroControl);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar residente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar un residente de la base de datos
     */
    public boolean eliminar() {
        String sql = "DELETE FROM residente WHERE numero_control = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.numeroControl);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar residente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Buscar un residente por número de control
     */
    public static ModeloResidente buscarPorNumeroControl(int numeroControl) {
        String sql = "SELECT * FROM residente WHERE numero_control = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroControl);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ModeloResidente(
                        rs.getInt("numero_control"),
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("carrera"),
                        rs.getInt("semestre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("id_proyecto")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar residente: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtener todos los residentes
     */
    public static List<ModeloResidente> obtenerTodos() {
        List<ModeloResidente> residentes = new ArrayList<>();
        String sql = "SELECT * FROM residente ORDER BY numero_control";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ModeloResidente residente = new ModeloResidente(
                        rs.getInt("numero_control"),
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("carrera"),
                        rs.getInt("semestre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("id_proyecto")
                );
                residentes.add(residente);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener residentes: " + e.getMessage());
        }

        return residentes;
    }

    /**
     * Verificar si un residente existe por número de control
     */
    public static boolean existe(int numeroControl) {
        String sql = "SELECT COUNT(*) FROM residente WHERE numero_control = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroControl);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar existencia: " + e.getMessage());
        }

        return false;
    }

    public static void asegurarProyectosPorDefecto() {
        String sqlCheck = "SELECT COUNT(*) FROM proyecto WHERE id_proyecto IN (0, 1)";
        String sqlInsert0 = "INSERT INTO proyecto (id_proyecto, nombre, descripcion, duracion, n_alumnos, estatus, origen) " +
                "VALUES (0, 'Sin Proyecto Asignado', 'Proyecto temporal para residentes sin asignar', " +
                "'Indefinido', 0, 'Activo', 'Sistema')";
        String sqlInsert1 = "INSERT INTO proyecto (id_proyecto, nombre, descripcion, duracion, n_alumnos, estatus, origen) " +
                "VALUES (1, 'Proyecto Por Defecto', 'Proyecto general para residentes', " +
                "'Indefinido', 0, 'Activo', 'Sistema')";

        try (Connection conn = getConnection()) {

            // Verificar cuántos proyectos por defecto existen
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Proyectos por defecto existentes: " + count);

                    if (count < 2) {
                        // Crear proyecto con id = 0
                        try (PreparedStatement stmt0 = conn.prepareStatement(sqlInsert0)) {
                            stmt0.executeUpdate();
                            System.out.println("✅ Proyecto con ID=0 creado");
                        } catch (SQLException e) {
                            if (!e.getMessage().contains("duplicate key")) {
                                System.err.println("Error creando proyecto ID=0: " + e.getMessage());
                            }
                        }

                        // Crear proyecto con id = 1
                        try (PreparedStatement stmt1 = conn.prepareStatement(sqlInsert1)) {
                            stmt1.executeUpdate();
                            System.out.println("✅ Proyecto con ID=1 creado");
                        } catch (SQLException e) {
                            if (!e.getMessage().contains("duplicate key")) {
                                System.err.println("Error creando proyecto ID=1: " + e.getMessage());
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al asegurar proyectos por defecto: " + e.getMessage());
        }
    }

    /**
     * Importar múltiples residentes a la base de datos (MEJORADO)
     */
    public static ResultadoImportacion importarResidentes(List<ModeloResidente> residentes) {
        final int TAMAÑO_LOTE = 50;

        int exitosos = 0;
        int fallidos = 0;
        int duplicados = 0;
        List<String> errores = new ArrayList<>();

        // Verificar conexión
        if (!probarConexion()) {
            errores.add("No se puede conectar a la base de datos");
            return new ResultadoImportacion(0, residentes.size(), 0, errores);
        }

        // IMPORTANTE: Asegurar que existen proyectos por defecto
        asegurarProyectosPorDefecto();

        // Validar y corregir id_proyecto en los residentes
        for (ModeloResidente residente : residentes) {
            if (residente.getIdProyecto() <= 0) {
                residente.setIdProyecto(1); // Asignar proyecto por defecto
            }
        }

        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "carrera, semestre, correo, telefono, id_proyecto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (numero_control) DO UPDATE SET " +
                "nombre = EXCLUDED.nombre, " +
                "apellido_paterno = EXCLUDED.apellido_paterno, " +
                "apellido_materno = EXCLUDED.apellido_materno, " +
                "carrera = EXCLUDED.carrera, " +
                "semestre = EXCLUDED.semestre, " +
                "correo = EXCLUDED.correo, " +
                "telefono = EXCLUDED.telefono, " +
                "id_proyecto = EXCLUDED.id_proyecto";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                for (int i = 0; i < residentes.size(); i += TAMAÑO_LOTE) {
                    int finLote = Math.min(i + TAMAÑO_LOTE, residentes.size());
                    List<ModeloResidente> loteActual = residentes.subList(i, finLote);

                    try {
                        for (ModeloResidente residente : loteActual) {
                            try {
                                // Validar datos antes de insertar
                                if (residente.getNumeroControl() <= 0) {
                                    errores.add("Número de control inválido: " + residente.getNumeroControl());
                                    fallidos++;
                                    continue;
                                }

                                if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) {
                                    errores.add("Nombre vacío para residente: " + residente.getNumeroControl());
                                    fallidos++;
                                    continue;
                                }

                                stmt.setInt(1, residente.getNumeroControl());
                                stmt.setString(2, residente.getNombre());
                                stmt.setString(3, residente.getApellidoPaterno());
                                stmt.setString(4, residente.getApellidoMaterno());
                                stmt.setString(5, residente.getCarrera());
                                stmt.setInt(6, residente.getSemestre());
                                stmt.setString(7, residente.getCorreo());
                                stmt.setString(8, residente.getTelefono());
                                stmt.setInt(9, residente.getIdProyecto()); // Ahora debería ser válido

                                stmt.addBatch();
                            } catch (SQLException e) {
                                errores.add("Error preparando residente " + residente.getNumeroControl() + ": " + e.getMessage());
                                fallidos++;
                            }
                        }

                        // Ejecutar lote
                        int[] resultados = stmt.executeBatch();
                        conn.commit();

                        // Contar resultados
                        for (int resultado : resultados) {
                            if (resultado > 0) {
                                exitosos++;
                            } else if (resultado == 0) {
                                duplicados++;
                            }
                        }

                        stmt.clearBatch();
                        System.out.println("Lote " + (i/TAMAÑO_LOTE + 1) + " procesado: " + loteActual.size() + " registros");

                    } catch (SQLException e) {
                        try {
                            conn.rollback();
                        } catch (SQLException rollbackEx) {
                            errores.add("Error en rollback: " + rollbackEx.getMessage());
                        }

                        fallidos += loteActual.size();
                        errores.add("Error en lote " + (i/TAMAÑO_LOTE + 1) + ": " + e.getMessage());
                    }
                }

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    errores.add("Error en rollback final: " + rollbackEx.getMessage());
                }
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Error en importación masiva: " + e.getMessage());
            errores.add("Error de conexión: " + e.getMessage());
            return new ResultadoImportacion(0, residentes.size(), 0, errores);
        }

        return new ResultadoImportacion(exitosos, fallidos, duplicados, errores);
    }

    /**
     * Crear un proyecto por defecto si no existe
     */
    public static void crearProyectoDefecto() {
        String sqlCheck = "SELECT COUNT(*) FROM proyecto WHERE id_proyecto = 1";
        String sqlInsert = "INSERT INTO proyecto (id_proyecto, nombre, descripcion, duracion, n_alumnos, estatus, origen) " +
                "VALUES (1, 'Proyecto Por Defecto', 'Proyecto temporal para residentes sin asignar', " +
                "'Indefinido', 0, 'Activo', 'Sistema')";

        try (Connection conn = getConnection()) {
            // Verificar si existe
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    // No existe, crear
                    try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                        insertStmt.executeUpdate();
                        System.out.println("✅ Proyecto por defecto creado");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear proyecto por defecto: " + e.getMessage());
        }
    }

    /**
     * Clase para representar el resultado de una importación
     */
    public static class ResultadoImportacion {
        private final int exitosos;
        private final int fallidos;
        private final int duplicados;
        private final List<String> errores;

        public ResultadoImportacion(int exitosos, int fallidos, int duplicados, List<String> errores) {
            this.exitosos = exitosos;
            this.fallidos = fallidos;
            this.duplicados = duplicados;
            this.errores = errores;
        }

        public int getExitosos() { return exitosos; }
        public int getFallidos() { return fallidos; }
        public int getDuplicados() { return duplicados; }
        public List<String> getErrores() { return errores; }

        public int getTotal() { return exitosos + fallidos + duplicados; }
    }

    // ==================== GETTERS Y SETTERS ====================

    public int getNumeroControl() { return numeroControl; }
    public void setNumeroControl(int numeroControl) { this.numeroControl = numeroControl; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    @Override
    public String toString() {
        return "ModeloResidente{" +
                "numeroControl=" + numeroControl +
                ", nombre='" + nombre + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", carrera='" + carrera + '\'' +
                ", semestre=" + semestre +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", idProyecto=" + idProyecto +
                '}';
    }
}