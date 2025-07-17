package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloResidente {
    // *** CAMBIO: Nueva estructura con id_residente y estatus ***
    private int idResidente;           // Nueva PK SERIAL
    private int numeroControl;         // Mantener INT para compatibilidad
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String carrera;
    private int semestre;
    private String correo;
    private String telefono;
    private int idProyecto;
    private boolean estatus = true;    // Campo estatus para dar de baja

    // Campos para manejar validación (usados por la Vista)
    private boolean esValido = true;
    private String motivoInvalido = "";
    private List<String> erroresValidacion = new ArrayList<>();

    // ==================== CONSTRUCTORES ====================

    public ModeloResidente() {
        this.estatus = true;
        this.esValido = true;
        this.motivoInvalido = "";
        this.erroresValidacion = new ArrayList<>();
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
        this.estatus = true;

        // Inicializar campos de validación
        this.esValido = true;
        this.motivoInvalido = "";
        this.erroresValidacion = new ArrayList<>();
    }

    public ModeloResidente(ModeloResidente otro) {
        this.idResidente = otro.idResidente;
        this.numeroControl = otro.numeroControl;
        this.nombre = otro.nombre;
        this.apellidoPaterno = otro.apellidoPaterno;
        this.apellidoMaterno = otro.apellidoMaterno;
        this.carrera = otro.carrera;
        this.semestre = otro.semestre;
        this.correo = otro.correo;
        this.telefono = otro.telefono;
        this.idProyecto = otro.idProyecto;
        this.estatus = otro.estatus;
        this.esValido = otro.esValido;
        this.motivoInvalido = otro.motivoInvalido;
        this.erroresValidacion = new ArrayList<>(otro.erroresValidacion);
    }

    // ==================== MÉTODOS DE VALIDACIÓN BÁSICA ====================

    public boolean validarCamposBasicos() {
        boolean valido = (this.numeroControl > 0 &&
                this.nombre != null && !this.nombre.trim().isEmpty() &&
                this.apellidoPaterno != null && !this.apellidoPaterno.trim().isEmpty() &&
                this.correo != null && !this.correo.trim().isEmpty());

        this.esValido = valido;
        if (!valido) {
            this.motivoInvalido = "Campos básicos incompletos";
        } else {
            this.motivoInvalido = "";
        }

        return valido;
    }

    public void marcarComoInvalido(String motivo) {
        this.esValido = false;
        this.motivoInvalido = motivo;
    }

    public void revalidar() {
        this.esValido = true;
        this.motivoInvalido = "";
        this.erroresValidacion.clear();
    }

    // ==================== MÉTODOS DE CONEXIÓN ====================

    private static Connection getConnection() {
        return Conexion_bd.getInstancia().getConexion();
    }

    private static boolean probarConexion() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            return false;
        }
    }

    // ==================== MÉTODOS DE BASE DE DATOS ACTUALIZADOS ====================

    public boolean guardarEnBaseDatos() {
        try {
            crearProyectoDefecto();

            if (this.idProyecto <= 0) {
                this.idProyecto = 1;
            }

            return this.insertar();

        } catch (Exception e) {
            System.err.println("Error en guardarEnBaseDatos: " + e.getMessage());
            return false;
        }
    }

    // *** CAMBIO: Insertar con nueva estructura ***
    public boolean insertar() {
        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "carrera, semestre, correo, telefono, id_proyecto, estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // BD usa VARCHAR(9) internamente pero objeto mantiene int
            stmt.setString(1, String.valueOf(this.numeroControl));
            stmt.setString(2, this.nombre);
            stmt.setString(3, this.apellidoPaterno);
            stmt.setString(4, this.apellidoMaterno);
            stmt.setString(5, this.carrera);
            stmt.setInt(6, this.semestre);
            stmt.setString(7, this.correo);
            stmt.setString(8, this.telefono);
            stmt.setInt(9, this.idProyecto);
            stmt.setBoolean(10, true); // estatus = true por defecto

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.idResidente = generatedKeys.getInt(1);
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar residente: " + e.getMessage());
            return false;
        }
    }

    // *** CAMBIO: Actualizar con nueva PK ***
    public boolean actualizar() {
        String sql = "UPDATE residente SET numero_control = ?, nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                "carrera = ?, semestre = ?, correo = ?, telefono = ?, id_proyecto = ? " +
                "WHERE id_residente = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, String.valueOf(this.numeroControl));
            stmt.setString(2, this.nombre);
            stmt.setString(3, this.apellidoPaterno);
            stmt.setString(4, this.apellidoMaterno);
            stmt.setString(5, this.carrera);
            stmt.setInt(6, this.semestre);
            stmt.setString(7, this.correo);
            stmt.setString(8, this.telefono);
            stmt.setInt(9, this.idProyecto);
            stmt.setInt(10, this.idResidente); // Usar nueva PK

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar residente: " + e.getMessage());
            return false;
        }
    }

    // *** CAMBIO: Eliminar cambiado por "dar de baja" ***
    public boolean eliminar() {
        return darDeBaja();
    }

    // *** NUEVO: Método darDeBaja ***
    public boolean darDeBaja() {
        String sql = "UPDATE residente SET estatus = FALSE WHERE id_residente = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, this.idResidente);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                this.estatus = false; // Actualizar el objeto
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al dar de baja residente: " + e.getMessage());
            return false;
        }
    }

    // *** NUEVO: Método reactivar ***
    public boolean reactivar() {
        String sql = "UPDATE residente SET estatus = TRUE WHERE id_residente = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, this.idResidente);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                this.estatus = true;
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al reactivar residente: " + e.getMessage());
            return false;
        }
    }

    // *** CAMBIO: Buscar solo activos ***
    public static ModeloResidente buscarPorNumeroControl(int numeroControl) {
        String sql = "SELECT * FROM residente WHERE numero_control = ? AND estatus = TRUE";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, String.valueOf(numeroControl));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ModeloResidente residente = new ModeloResidente();
                residente.setIdResidente(rs.getInt("id_residente"));
                residente.setNumeroControl(Integer.parseInt(rs.getString("numero_control")));
                residente.setNombre(rs.getString("nombre"));
                residente.setApellidoPaterno(rs.getString("apellido_paterno"));
                residente.setApellidoMaterno(rs.getString("apellido_materno"));
                residente.setCarrera(rs.getString("carrera"));
                residente.setSemestre(rs.getInt("semestre"));
                residente.setCorreo(rs.getString("correo"));
                residente.setTelefono(rs.getString("telefono"));
                residente.setIdProyecto(rs.getInt("id_proyecto"));
                residente.setEstatus(rs.getBoolean("estatus"));
                return residente;
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar residente: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir numero_control: " + e.getMessage());
        }

        return null;
    }

    // *** CAMBIO: Obtener solo activos ***
    public static List<ModeloResidente> obtenerTodos() {
        List<ModeloResidente> residentes = new ArrayList<>();
        String sql = "SELECT * FROM residente WHERE estatus = TRUE ORDER BY numero_control";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModeloResidente residente = new ModeloResidente();
                residente.setIdResidente(rs.getInt("id_residente"));
                residente.setNumeroControl(Integer.parseInt(rs.getString("numero_control")));
                residente.setNombre(rs.getString("nombre"));
                residente.setApellidoPaterno(rs.getString("apellido_paterno"));
                residente.setApellidoMaterno(rs.getString("apellido_materno"));
                residente.setCarrera(rs.getString("carrera"));
                residente.setSemestre(rs.getInt("semestre"));
                residente.setCorreo(rs.getString("correo"));
                residente.setTelefono(rs.getString("telefono"));
                residente.setIdProyecto(rs.getInt("id_proyecto"));
                residente.setEstatus(rs.getBoolean("estatus"));
                residentes.add(residente);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener residentes: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir numero_control: " + e.getMessage());
        }

        return residentes;
    }

    // *** CAMBIO: Verificar existencia solo activos ***
    public static boolean existe(int numeroControl) {
        String sql = "SELECT COUNT(*) FROM residente WHERE numero_control = ? AND estatus = TRUE";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, String.valueOf(numeroControl));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar existencia: " + e.getMessage());
        }

        return false;
    }

    // ==================== IMPORTACIÓN ACTUALIZADA ====================

    public static ResultadoImportacion importarResidentes(List<ModeloResidente> residentes) {
        int exitosos = 0;
        int fallidos = 0;
        int duplicados = 0;
        List<String> errores = new ArrayList<>();

        System.out.println("DEBUG: === INICIANDO IMPORTACIÓN ===");
        System.out.println("DEBUG: Residentes recibidos: " + residentes.size());

        if (!probarConexion()) {
            errores.add("No se puede conectar a la base de datos");
            return new ResultadoImportacion(0, residentes.size(), 0, errores);
        }

        asegurarProyectosPorDefecto();

        // *** CAMBIO: SQL actualizado para nueva estructura ***
        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "carrera, semestre, correo, telefono, id_proyecto, estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE)";

        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (ModeloResidente residente : residentes) {
                try {
                    if (residente.getIdProyecto() <= 0) {
                        residente.setIdProyecto(1);
                    }

                    System.out.println("DEBUG: Procesando residente " + residente.getNumeroControl() +
                            " - " + residente.getNombre() + " " + residente.getApellidoPaterno());

                    if (existe(residente.getNumeroControl())) {
                        System.out.println("DEBUG: ❌ Ya existe en BD: " + residente.getNumeroControl());
                        duplicados++;
                        errores.add("Ya existe: " + residente.getNumeroControl() + " - " + residente.getNombre());
                        continue;
                    }

                    if (residente.getNumeroControl() <= 0) {
                        System.out.println("DEBUG: ❌ Número control inválido: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Número control inválido: " + residente.getNumeroControl());
                        continue;
                    }

                    if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) {
                        System.out.println("DEBUG: ❌ Nombre vacío: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Nombre vacío: " + residente.getNumeroControl());
                        continue;
                    }

                    if (residente.getApellidoPaterno() == null || residente.getApellidoPaterno().trim().isEmpty()) {
                        System.out.println("DEBUG: ❌ Apellido paterno vacío: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Apellido paterno vacío: " + residente.getNumeroControl());
                        continue;
                    }

                    if (residente.getCorreo() == null || residente.getCorreo().trim().isEmpty()) {
                        System.out.println("DEBUG: ❌ Correo vacío: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Correo vacío: " + residente.getNumeroControl());
                        continue;
                    }

                    // Preparar inserción (mantener int → String para BD)
                    stmt.setString(1, String.valueOf(residente.getNumeroControl()));
                    stmt.setString(2, residente.getNombre().trim());
                    stmt.setString(3, residente.getApellidoPaterno().trim());
                    stmt.setString(4, residente.getApellidoMaterno() != null ? residente.getApellidoMaterno().trim() : null);
                    stmt.setString(5, residente.getCarrera());
                    stmt.setInt(6, residente.getSemestre());
                    stmt.setString(7, residente.getCorreo().trim());
                    stmt.setString(8, residente.getTelefono() != null ? residente.getTelefono().trim() : null);
                    stmt.setInt(9, residente.getIdProyecto());

                    int filasAfectadas = stmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        exitosos++;
                        System.out.println("DEBUG: ✅ Insertado exitosamente: " + residente.getNumeroControl());
                    } else {
                        fallidos++;
                        errores.add("No se pudo insertar: " + residente.getNumeroControl());
                    }

                } catch (SQLException e) {
                    System.out.println("DEBUG: ❌ Error SQL para " + residente.getNumeroControl() + ": " + e.getMessage());

                    if (e.getMessage().toLowerCase().contains("duplicate") ||
                            e.getMessage().toLowerCase().contains("unique constraint")) {
                        duplicados++;
                        errores.add("Duplicado: " + residente.getNumeroControl() + " - " + residente.getNombre());
                    } else {
                        fallidos++;
                        errores.add("Error: " + residente.getNumeroControl() + " - " + e.getMessage());
                    }
                }
            }

            conn.commit();
            conn.setAutoCommit(true);

            System.out.println("DEBUG: === RESUMEN IMPORTACIÓN ===");
            System.out.println("DEBUG: Exitosos: " + exitosos);
            System.out.println("DEBUG: Fallidos: " + fallidos);
            System.out.println("DEBUG: Duplicados: " + duplicados);

        } catch (SQLException e) {
            System.err.println("Error de conexión en importación: " + e.getMessage());
            errores.add("Error de conexión: " + e.getMessage());
            return new ResultadoImportacion(0, residentes.size(), 0, errores);
        }

        return new ResultadoImportacion(exitosos, fallidos, duplicados, errores);
    }

    // ==================== MÉTODOS DE PROYECTOS ====================

    public static void asegurarProyectosPorDefecto() {
        String sqlCheck = "SELECT COUNT(*) FROM proyecto WHERE id_proyecto IN (0, 1)";
        String sqlInsert0 = "INSERT INTO proyecto (id_proyecto, nombre, descripcion, duracion, n_alumnos, estatus, origen) " +
                "VALUES (0, 'Sin Proyecto Asignado', 'Proyecto temporal para residentes sin asignar', " +
                "'Indefinido', 0, 'Activo', 'Sistema')";
        String sqlInsert1 = "INSERT INTO proyecto (id_proyecto, nombre, descripcion, duracion, n_alumnos, estatus, origen) " +
                "VALUES (1, 'Proyecto Por Defecto', 'Proyecto general para residentes', " +
                "'Indefinido', 0, 'Activo', 'Sistema')";

        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count < 2) {
                    try {
                        PreparedStatement stmt0 = conn.prepareStatement(sqlInsert0);
                        stmt0.executeUpdate();
                    } catch (SQLException e) {
                        if (!e.getMessage().contains("duplicate key") && !e.getMessage().contains("already exists")) {
                            System.err.println("Error creando proyecto ID=0: " + e.getMessage());
                        }
                    }
                    try {
                        PreparedStatement stmt1 = conn.prepareStatement(sqlInsert1);
                        stmt1.executeUpdate();
                    } catch (SQLException e) {
                        if (!e.getMessage().contains("duplicate key") && !e.getMessage().contains("already exists")) {
                            System.err.println("Error creando proyecto ID=1: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al asegurar proyectos por defecto: " + e.getMessage());
        }
    }

    public static void crearProyectoDefecto() {
        String sqlCheck = "SELECT COUNT(*) FROM proyecto WHERE id_proyecto = 1";
        String sqlInsert = "INSERT INTO proyecto (id_proyecto, nombre, descripcion, duracion, n_alumnos, estatus, origen) " +
                "VALUES (1, 'Proyecto Por Defecto', 'Proyecto temporal para residentes sin asignar', " +
                "'Indefinido', 0, 'Activo', 'Sistema')";

        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                PreparedStatement insertStmt = conn.prepareStatement(sqlInsert);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            if (!e.getMessage().contains("duplicate key") && !e.getMessage().contains("already exists")) {
                System.err.println("Error al crear proyecto por defecto: " + e.getMessage());
            }
        }
    }

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

    // ==================== GETTERS Y SETTERS ACTUALIZADOS ====================

    public int getIdResidente() { return idResidente; }
    public void setIdResidente(int idResidente) { this.idResidente = idResidente; }

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

    public boolean isEstatus() { return estatus; }
    public void setEstatus(boolean estatus) { this.estatus = estatus; }

    // Getters y setters para validación
    public boolean isEsValido() { return esValido; }
    public void setEsValido(boolean esValido) { this.esValido = esValido; }

    public String getMotivoInvalido() { return motivoInvalido; }
    public void setMotivoInvalido(String motivoInvalido) { this.motivoInvalido = motivoInvalido; }

    public List<String> getErroresValidacion() { return new ArrayList<>(erroresValidacion); }
    public void setErroresValidacion(List<String> errores) { this.erroresValidacion = new ArrayList<>(errores); }

    @Override
    public String toString() {
        return "ModeloResidente{" +
                "idResidente=" + idResidente +
                ", numeroControl=" + numeroControl +
                ", nombre='" + nombre + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", carrera='" + carrera + '\'' +
                ", semestre=" + semestre +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", idProyecto=" + idProyecto +
                ", estatus=" + estatus +
                ", esValido=" + esValido +
                '}';
    }
}