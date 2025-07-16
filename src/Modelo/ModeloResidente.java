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

    // NUEVO: Lista para almacenar errores de validación para tooltips
    private List<String> erroresValidacion = new ArrayList<>();

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

    public ModeloResidente(ModeloResidente otro) {
        this.numeroControl = otro.numeroControl;
        this.nombre = otro.nombre;
        this.apellidoPaterno = otro.apellidoPaterno;
        this.apellidoMaterno = otro.apellidoMaterno;
        this.carrera = otro.carrera;
        this.semestre = otro.semestre;
        this.correo = otro.correo;
        this.telefono = otro.telefono;
        this.idProyecto = otro.idProyecto;
        this.erroresValidacion = new ArrayList<>(otro.erroresValidacion);
    }

    // ==================== VALIDACIÓN COMPLETA ====================

    /**
     * NUEVO: Valida todos los campos y almacena errores para tooltips
     * IMPORTANTE: Siempre retorna un resultado, nunca falla
     */
    public boolean validarDatos() {
        erroresValidacion.clear();
        boolean esValido = true;

        // Validar número de control
        if (!validarNumeroControl()) esValido = false;

        // Validar nombre
        if (!validarNombre()) esValido = false;

        // Validar apellidos
        if (!validarApellidos()) esValido = false;

        // Validar semestre
        if (!validarSemestre()) esValido = false;

        // Validar correo
        if (!validarCorreo()) esValido = false;

        // Validar teléfono
        if (!validarTelefono()) esValido = false;

        // Validar que no exista en BD (solo para nuevos registros)
        if (esValido && existe(numeroControl)) {
            erroresValidacion.add("❌ Ya existe un residente con este número de control");
            esValido = false;
        }

        return esValido;
    }

    private boolean validarNumeroControl() {
        if (numeroControl <= 0) {
            erroresValidacion.add("❌ Número de control inválido o vacío");
            return false;
        }

        String numStr = String.valueOf(numeroControl);
        if (numStr.length() != 8) {
            erroresValidacion.add("❌ Debe tener exactamente 8 dígitos (tiene " + numStr.length() + ")");
            return false;
        }

        // Validar año (primeros 2 dígitos) - NO PERMITIR AÑOS FUTUROS
        try {
            int anio = Integer.parseInt(numStr.substring(0, 2));
            int anioActual = java.time.Year.now().getValue() % 100; // Ejemplo: 2025 → 25

            // NUEVA VALIDACIÓN: No permitir años futuros
            if (anio > anioActual) {
                int anioCompleto = 2000 + anio;
                erroresValidacion.add("❌ Año futuro no permitido (" + anioCompleto + ")");
                return false;
            }

            // Validar rango de años válidos (20 años hacia atrás desde año actual)
            int anioMinimo = (anioActual - 20 + 100) % 100;

            boolean anioValido = false;
            if (anioMinimo <= anioActual) {
                anioValido = (anio >= anioMinimo && anio <= anioActual);
            } else {
                // Manejar cambio de siglo
                anioValido = (anio >= anioMinimo || anio <= anioActual);
            }

            if (!anioValido) {
                int anioCompleto = anio <= anioActual ? 2000 + anio : 1900 + anio;
                erroresValidacion.add("⚠️ Año del número de control fuera de rango válido (" + anioCompleto + ")");
                return false;
            }
        } catch (Exception e) {
            erroresValidacion.add("❌ Error al validar número de control");
            return false;
        }

        return true;
    }

    private boolean validarNombre() {
        if (nombre == null || nombre.trim().isEmpty()) {
            erroresValidacion.add("❌ Nombre es obligatorio");
            return false;
        }

        if (nombre.trim().length() < 2) {
            erroresValidacion.add("❌ Nombre debe tener al menos 2 caracteres");
            return false;
        }

        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            erroresValidacion.add("❌ Nombre contiene caracteres inválidos");
            return false;
        }

        return true;
    }

    private boolean validarApellidos() {
        boolean valido = true;

        if (apellidoPaterno == null || apellidoPaterno.trim().isEmpty()) {
            erroresValidacion.add("❌ Apellido paterno es obligatorio");
            valido = false;
        } else if (apellidoPaterno.trim().length() < 2) {
            erroresValidacion.add("❌ Apellido paterno muy corto");
            valido = false;
        } else if (!apellidoPaterno.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            erroresValidacion.add("❌ Apellido paterno contiene caracteres inválidos");
            valido = false;
        }

        // Apellido materno es opcional
        if (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()) {
            if (apellidoMaterno.trim().length() < 2) {
                erroresValidacion.add("⚠️ Apellido materno muy corto");
                valido = false;
            } else if (!apellidoMaterno.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                erroresValidacion.add("❌ Apellido materno contiene caracteres inválidos");
                valido = false;
            }
        }

        return valido;
    }

    private boolean validarSemestre() {
        if (semestre < 9 || semestre > 15) {
            erroresValidacion.add("❌ Semestre debe estar entre 9 y 15");
            return false;
        }
        return true;
    }

    private boolean validarCorreo() {
        if (correo == null || correo.trim().isEmpty()) {
            erroresValidacion.add("❌ Correo es obligatorio");
            return false;
        }

        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            erroresValidacion.add("❌ Formato de correo inválido");
            return false;
        }

        if (correo.length() > 254) {
            erroresValidacion.add("❌ Correo demasiado largo");
            return false;
        }

        return true;
    }

    private boolean validarTelefono() {
        // Teléfono es opcional
        if (telefono == null || telefono.trim().isEmpty()) {
            return true;
        }

        // Limpiar teléfono de caracteres especiales
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");

        if (telefonoLimpio.length() < 8) {
            erroresValidacion.add("❌ Teléfono debe tener al menos 8 dígitos");
            return false;
        }

        if (telefonoLimpio.length() > 15) {
            erroresValidacion.add("❌ Teléfono demasiado largo");
            return false;
        }

        // Detectar patrones sospechosos
        if (telefonoLimpio.matches("(\\d)\\1{7,}")) {
            erroresValidacion.add("⚠️ Teléfono parece tener dígitos repetidos");
            return false;
        }

        if (telefonoLimpio.matches("12345678.*") || telefonoLimpio.matches("87654321.*")) {
            erroresValidacion.add("⚠️ Teléfono parece ser secuencial");
            return false;
        }

        return true;
    }

    // ==================== MÉTODOS DE TOOLTIPS ====================

    /**
     * NUEVO: Obtener errores de validación para mostrar en tooltips
     */
    public String getErroresTooltip() {
        if (erroresValidacion.isEmpty()) {
            return null;
        }

        StringBuilder tooltip = new StringBuilder("<html>");
        tooltip.append("<div style='padding: 5px; max-width: 300px;'>");
        tooltip.append("<b style='color: #d32f2f;'>⚠️ Errores encontrados:</b><br><br>");

        for (String error : erroresValidacion) {
            tooltip.append("• ").append(error).append("<br>");
        }

        tooltip.append("<br><i style='color: #666;'>💡 Haga doble click para editar</i>");
        tooltip.append("</div></html>");

        return tooltip.toString();
    }

    /**
     * NUEVO: Verificar si tiene errores de validación
     */
    public boolean tieneErrores() {
        return !erroresValidacion.isEmpty();
    }

    /**
     * NUEVO: Obtener lista de errores
     */
    public List<String> getErroresValidacion() {
        return new ArrayList<>(erroresValidacion);
    }

    /**
     * NUEVO: Establecer errores de validación (usado por ExcelHandler)
     */
    public void setErroresValidacion(List<String> errores) {
        this.erroresValidacion = new ArrayList<>(errores);
    }

    // Método para obtener conexión usando el Singleton
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

    // ==================== MÉTODOS DE BASE DE DATOS ====================

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

    public boolean insertar() {
        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "carrera, semestre, correo, telefono, id_proyecto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

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

    public boolean actualizar() {
        String sql = "UPDATE residente SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                "carrera = ?, semestre = ?, correo = ?, telefono = ?, id_proyecto = ? " +
                "WHERE numero_control = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

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

    public boolean eliminar() {
        String sql = "DELETE FROM residente WHERE numero_control = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, this.numeroControl);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar residente: " + e.getMessage());
            return false;
        }
    }

    public static ModeloResidente buscarPorNumeroControl(int numeroControl) {
        String sql = "SELECT * FROM residente WHERE numero_control = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

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

    public static List<ModeloResidente> obtenerTodos() {
        List<ModeloResidente> residentes = new ArrayList<>();
        String sql = "SELECT * FROM residente ORDER BY numero_control";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

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

    public static boolean existe(int numeroControl) {
        String sql = "SELECT COUNT(*) FROM residente WHERE numero_control = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

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

    /**
     * MEJORADO: Importar con mensajes más concisos
     */
    public static ResultadoImportacion importarResidentes(List<ModeloResidente> residentes) {
        final int TAMAÑO_LOTE = 50;

        int exitosos = 0;
        int fallidos = 0;
        int duplicados = 0;
        List<String> errores = new ArrayList<>();

        if (!probarConexion()) {
            errores.add("No se puede conectar a la base de datos");
            return new ResultadoImportacion(0, residentes.size(), 0, errores);
        }

        asegurarProyectosPorDefecto();

        for (ModeloResidente residente : residentes) {
            if (residente.getIdProyecto() <= 0) {
                residente.setIdProyecto(1);
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

        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < residentes.size(); i += TAMAÑO_LOTE) {
                int finLote = Math.min(i + TAMAÑO_LOTE, residentes.size());
                List<ModeloResidente> loteActual = residentes.subList(i, finLote);

                try {
                    for (ModeloResidente residente : loteActual) {
                        try {
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
                            stmt.setInt(9, residente.getIdProyecto());

                            stmt.addBatch();
                        } catch (SQLException e) {
                            errores.add("Error preparando residente " + residente.getNumeroControl() + ": " + e.getMessage());
                            fallidos++;
                        }
                    }

                    int[] resultados = stmt.executeBatch();
                    conn.commit();

                    for (int resultado : resultados) {
                        if (resultado > 0) {
                            exitosos++;
                        } else if (resultado == 0) {
                            duplicados++;
                        }
                    }

                    stmt.clearBatch();

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

            conn.setAutoCommit(true);

        } catch (SQLException e) {
            System.err.println("Error en importación masiva: " + e.getMessage());
            errores.add("Error de conexión: " + e.getMessage());
            return new ResultadoImportacion(0, residentes.size(), 0, errores);
        }

        return new ResultadoImportacion(exitosos, fallidos, duplicados, errores);
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
    // Agregar estos métodos a tu clase ModeloResidente

    // Campos para manejar validación
    private boolean esValido = true;
    private String motivoInvalido = "";

    // Getters y setters para validación
    public boolean isEsValido() {
        return esValido;
    }

    public void setEsValido(boolean esValido) {
        this.esValido = esValido;
    }

    public String getMotivoInvalido() {
        return motivoInvalido;
    }

    public void setMotivoInvalido(String motivoInvalido) {
        this.motivoInvalido = motivoInvalido;
    }

    // Método para marcar como inválido
    public void marcarComoInvalido(String motivo) {
        this.esValido = false;
        this.motivoInvalido = motivo;
    }

    // Método para revalidar (limpiar estado de invalidez)
    public void revalidar() {
        this.esValido = true;
        this.motivoInvalido = "";
    }

    // Método para validar campos básicos
    public boolean validarCamposBasicos() {
        if (this.numeroControl <= 0) {
            marcarComoInvalido("Número de control inválido");
            return false;
        }

        if (this.nombre == null || this.nombre.trim().isEmpty()) {
            marcarComoInvalido("Nombre es obligatorio");
            return false;
        }

        if (this.apellidoPaterno == null || this.apellidoPaterno.trim().isEmpty()) {
            marcarComoInvalido("Apellido paterno es obligatorio");
            return false;
        }

        if (this.correo == null || this.correo.trim().isEmpty()) {
            marcarComoInvalido("Correo es obligatorio");
            return false;
        }

        // Si llegamos aquí, todo está bien
        revalidar();
        return true;
    }
}