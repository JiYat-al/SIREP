package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloResidente {
    private int idResidente;
    private int numeroControl;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String carrera;
    private int semestre;
    private String correo;
    private String telefono;
    private int idProyecto;
    private boolean estatus = true;

    private boolean esValido = true;
    private String motivoInvalido = "";
    private List<String> erroresValidacion = new ArrayList<>();
    private boolean esResidenteActivo = false;

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


    public boolean guardarEnBaseDatos() {
        try {
            System.out.println("DEBUG: === INICIANDO guardarEnBaseDatos() ===");

            // Crear proyecto por defecto si no existe
            crearProyectoDefecto();

            if (this.idProyecto <= 0) {
                this.idProyecto = 1;
                System.out.println("DEBUG: ID proyecto asignado por defecto: " + this.idProyecto);
            }

            System.out.println("DEBUG: Llamando a insertar()...");
            boolean resultado = this.insertar();

            System.out.println("DEBUG: Resultado de insertar(): " + resultado);

            if (resultado) {
                System.out.println("DEBUG: guardarEnBaseDatos() exitoso");
            } else {
                System.out.println("DEBUG: guardarEnBaseDatos() falló");
            }

            return resultado;

        } catch (Exception e) {
            System.err.println("DEBUG: Error en guardarEnBaseDatos(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // *** FIX: Insertar con debug detallado para encontrar el problema ***
    public boolean insertar() {
        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "carrera, semestre, correo, telefono, id_proyecto, estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("DEBUG: Insertando residente con datos:");
        System.out.println("  - numero_control: " + this.numeroControl);
        System.out.println("  - nombre: " + this.nombre);
        System.out.println("  - apellido_paterno: " + this.apellidoPaterno);
        System.out.println("  - apellido_materno: " + this.apellidoMaterno);
        System.out.println("  - carrera: " + this.carrera);
        System.out.println("  - semestre: " + this.semestre);
        System.out.println("  - correo: " + this.correo);
        System.out.println("  - telefono: " + this.telefono);
        System.out.println("  - id_proyecto: " + this.idProyecto);
        System.out.println("  - estatus: " + this.estatus);

        try {
            Connection conn = getConnection();
            if (conn == null) {
                System.err.println("DEBUG: Conexión es null");
                return false;
            }

            System.out.println("DEBUG: Conexión obtenida correctamente");

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("DEBUG: PreparedStatement creado");

            // *** FIX: Orden correcto de parámetros con validación ***
            stmt.setString(1, String.valueOf(this.numeroControl));  // numero_control (VARCHAR)
            System.out.println("DEBUG: Parámetro 1 (numero_control) establecido: " + String.valueOf(this.numeroControl));

            stmt.setString(2, this.nombre);                          // nombre (VARCHAR)
            System.out.println("DEBUG: Parámetro 2 (nombre) establecido: " + this.nombre);

            stmt.setString(3, this.apellidoPaterno);                // apellido_paterno (VARCHAR)
            System.out.println("DEBUG: Parámetro 3 (apellido_paterno) establecido: " + this.apellidoPaterno);

            stmt.setString(4, this.apellidoMaterno);                // apellido_materno (VARCHAR)
            System.out.println("DEBUG: Parámetro 4 (apellido_materno) establecido: " + this.apellidoMaterno);

            stmt.setString(5, this.carrera);                        // carrera (VARCHAR)
            System.out.println("DEBUG: Parámetro 5 (carrera) establecido: " + this.carrera);

            stmt.setInt(6, this.semestre);                          // semestre (INT)
            System.out.println("DEBUG: Parámetro 6 (semestre) establecido: " + this.semestre);

            stmt.setString(7, this.correo);                         // correo (VARCHAR)
            System.out.println("DEBUG: Parámetro 7 (correo) establecido: " + this.correo);

            stmt.setString(8, this.telefono);                       // telefono (VARCHAR)
            System.out.println("DEBUG: Parámetro 8 (telefono) establecido: " + this.telefono);

            stmt.setInt(9, this.idProyecto);                        // id_proyecto (INT)
            System.out.println("DEBUG: Parámetro 9 (id_proyecto) establecido: " + this.idProyecto);

            stmt.setBoolean(10, true);                              // estatus (BOOLEAN)
            System.out.println("DEBUG: Parámetro 10 (estatus) establecido: true");

            System.out.println("DEBUG: Todos los parámetros establecidos, ejecutando SQL...");
            System.out.println("DEBUG: SQL: " + sql);

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("DEBUG: SQL ejecutado, filas afectadas: " + filasAfectadas);

            if (filasAfectadas > 0) {
                System.out.println("DEBUG: Inserción exitosa, obteniendo ID generado...");

                // Obtener el ID generado
                try {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        this.idResidente = generatedKeys.getInt(1);
                        System.out.println("DEBUG: ID generado obtenido: " + this.idResidente);
                    } else {
                        System.out.println("DEBUG: ⚠️ No se generaron keys, pero la inserción fue exitosa");
                    }
                } catch (SQLException e) {
                    System.err.println("DEBUG: ⚠️ Error al obtener ID generado (pero inserción exitosa): " + e.getMessage());
                    // No retornar false aquí, la inserción fue exitosa
                }

                System.out.println("DEBUG: insertar() retornando true");
                return true;
            } else {
                System.out.println("DEBUG: No se insertaron filas (filasAfectadas = 0)");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("DEBUG: SQLException en insertar(): " + e.getMessage());
            System.err.println("DEBUG: SQL State: " + e.getSQLState());
            System.err.println("DEBUG: Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("DEBUG: Exception general en insertar(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    // Modificar el método insertar para incluir el nuevo campo
    public boolean insertarConEstatus() {
        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "carrera, semestre, correo, telefono, id_proyecto, estatus, es_residente_activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, String.valueOf(this.numeroControl));
            stmt.setString(2, this.nombre);
            stmt.setString(3, this.apellidoPaterno);
            stmt.setString(4, this.apellidoMaterno);
            stmt.setString(5, this.carrera);
            stmt.setInt(6, this.semestre);
            stmt.setString(7, this.correo);
            stmt.setString(8, this.telefono);
            stmt.setInt(9, this.idProyecto);
            stmt.setBoolean(10, true);
            stmt.setBoolean(11, this.esResidenteActivo);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        this.idResidente = generatedKeys.getInt(1);
                    }
                } catch (SQLException e) {
                    System.err.println("Error al obtener ID generado: " + e.getMessage());
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar residente con estatus: " + e.getMessage());
            return false;
        }
    }


    // *** CAMBIO: Actualizar SIN modificar id_proyecto ***
    public boolean actualizar() {
        String sql = "UPDATE residente SET numero_control = ?, nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                "carrera = ?, semestre = ?, correo = ?, telefono = ? " +
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
            stmt.setInt(9, this.idResidente); // Usar nueva PK
            // *** ELIMINADO: id_proyecto de la actualización ***

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

    // *** NUEVO: Mtodo reactivar ***
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

    public boolean convertirAResidenteActivo() {
        String sql = "UPDATE residente SET id_estatus_residente = 2 WHERE id_residente = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.idResidente);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                this.idEstatus = 2; // Actualizar el objeto
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al convertir a residente activo: " + e.getMessage());
            return false;
        }
    }
    public static List<ModeloResidente> obtenerCandidatos() {
        List<ModeloResidente> candidatos = new ArrayList<>();
        String sql = "SELECT * FROM residente WHERE estatus = TRUE AND (id_estatus_residente = 1 OR id_estatus_residente IS NULL) ORDER BY numero_control";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
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

                    // Manejar id_estatus_residente
                    try {
                        residente.setIdEstatus(rs.getInt("id_estatus_residente"));
                    } catch (SQLException e) {
                        residente.setIdEstatus(1); // Default candidato
                    }

                    candidatos.add(residente);
                } catch (Exception e) {
                    System.err.println("Error procesando candidato: " + e.getMessage());
                    continue;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener candidatos: " + e.getMessage());
        }

        return candidatos;
    }

    public static List<ModeloResidente> obtenerResidentesActivos() {
        List<ModeloResidente> residentes = new ArrayList<>();
        String sql = "SELECT * FROM residente WHERE estatus = TRUE AND id_estatus_residente = 2 ORDER BY numero_control";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
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
                    residente.setIdEstatus(rs.getInt("id_estatus_residente"));

                    residentes.add(residente);
                } catch (Exception e) {
                    System.err.println("Error procesando residente activo: " + e.getMessage());
                    continue;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener residentes activos: " + e.getMessage());
        }

        return residentes;
    }

    public static boolean regresarACandidato(int idResidente) {
        String sql = "UPDATE residente SET id_estatus_residente = 1 WHERE id_residente = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idResidente);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al regresar a candidato: " + e.getMessage());
            return false;
        }
    }
    public static void verificarEstructuraTabla() {
        String sqlCheck = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = 'residente' AND column_name = 'es_residente_activo'";

        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                String sqlAlter = "ALTER TABLE residente ADD COLUMN es_residente_activo BOOLEAN DEFAULT FALSE";
                PreparedStatement alterStmt = conn.prepareStatement(sqlAlter);
                alterStmt.executeUpdate();
                System.out.println("Columna es_residente_activo agregada a la tabla residente");
            } else {
                System.out.println("Columna es_residente_activo ya existe en la tabla residente");
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar/crear estructura de tabla: " + e.getMessage());
        }
    }

    public static void verificarEstatusResidentes() {
        String sqlCheck = "SELECT COUNT(*) FROM estatus_residente";
        String sqlInsert1 = "INSERT INTO estatus_residente (id_estatus_residente, estatus) VALUES (1, true)";
        String sqlInsert2 = "INSERT INTO estatus_residente (id_estatus_residente, estatus) VALUES (2, true)";

        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    // Insertar estatus por defecto
                    try {
                        PreparedStatement stmt1 = conn.prepareStatement(sqlInsert1);
                        stmt1.executeUpdate();
                        System.out.println("Estatus candidato (1) creado");
                    } catch (SQLException e) {
                        System.err.println("Error creando estatus candidato: " + e.getMessage());
                    }

                    try {
                        PreparedStatement stmt2 = conn.prepareStatement(sqlInsert2);
                        stmt2.executeUpdate();
                        System.out.println("Estatus residente activo (2) creado");
                    } catch (SQLException e) {
                        System.err.println("Error creando estatus residente: " + e.getMessage());
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar estatus de residentes: " + e.getMessage());
        }
    }

    private int idEstatus = 1; // 1 = candidato, 2 = residente activo
    public boolean isResidenteActivo() { return idEstatus == 2; }
    public boolean isCandidato() { return idEstatus == 1 || idEstatus == 0; }






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

    // *** CAMBIO: Obtener solo activos - SIMPLIFICADO ***
    public static List<ModeloResidente> obtenerTodos() {
        List<ModeloResidente> residentes = new ArrayList<>();
        String sql = "SELECT * FROM residente WHERE estatus = TRUE ORDER BY numero_control";

        System.out.println("DEBUG: Ejecutando consulta: " + sql);

        try {
            Connection conn = getConnection();
            if (conn == null) {
                System.err.println("ERROR: Conexión es null");
                return residentes;
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int contador = 0;
            while (rs.next()) {
                try {
                    ModeloResidente residente = new ModeloResidente();
                    residente.setIdResidente(rs.getInt("id_residente"));

                    // Convertir numero_control a int
                    String numeroControlStr = rs.getString("numero_control");
                    residente.setNumeroControl(Integer.parseInt(numeroControlStr));

                    residente.setNombre(rs.getString("nombre"));
                    residente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    residente.setApellidoMaterno(rs.getString("apellido_materno"));
                    residente.setSemestre(rs.getInt("semestre"));
                    residente.setCorreo(rs.getString("correo"));
                    residente.setTelefono(rs.getString("telefono"));
                    residente.setIdProyecto(rs.getInt("id_proyecto"));
                    residente.setEstatus(rs.getBoolean("estatus"));

                    residentes.add(residente);
                    contador++;

                    if (contador <= 3) { // Solo mostrar los primeros 3 para debug
                        System.out.println("DEBUG: Residente cargado - ID: " + residente.getIdResidente() +
                                ", No.Control: " + residente.getNumeroControl() +
                                ", Nombre: " + residente.getNombre() +
                                ", Estatus: " + residente.isEstatus());
                    }

                } catch (Exception e) {
                    System.err.println("Error procesando fila: " + e.getMessage());
                    continue; // Continuar con el siguiente registro
                }
            }

            System.out.println("DEBUG: Total residentes cargados: " + residentes.size());

        } catch (SQLException e) {
            System.err.println("Error al obtener residentes: " + e.getMessage());
            e.printStackTrace();

            // *** FALLBACK: Si falla, intentar sin filtro de estatus ***
            System.out.println("DEBUG: Intentando consulta sin filtro estatus...");
            return obtenerTodosSinFiltro();
        }

        return residentes;
    }

    // *** NUEVO: Método fallback sin filtro de estatus ***
    private static List<ModeloResidente> obtenerTodosSinFiltro() {
        List<ModeloResidente> residentes = new ArrayList<>();
        String sql = "SELECT * FROM residente ORDER BY numero_control";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
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

                    // Verificar si tiene columna estatus
                    try {
                        residente.setEstatus(rs.getBoolean("estatus"));
                    } catch (SQLException e) {
                        residente.setEstatus(true); // Default si no existe la columna
                    }

                    residentes.add(residente);
                } catch (Exception e) {
                    System.err.println("Error procesando fila en fallback: " + e.getMessage());
                }
            }

            System.out.println("DEBUG: Fallback cargó " + residentes.size() + " residentes");

        } catch (SQLException e) {
            System.err.println("Error en fallback: " + e.getMessage());
        }

        return residentes;
    }

    // *** CAMBIO: Verificar existencia solo activos ***
    public static boolean existe(int numeroControl) {
        // *** FIX: Verificar si la columna estatus existe ***
        String sqlCheck = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = 'residente' AND column_name = 'estatus'";

        String sql;
        boolean tieneColumnaEstatus = false;

        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
            ResultSet checkRs = checkStmt.executeQuery();

            tieneColumnaEstatus = checkRs.next();

            // Si tiene columna estatus, filtrar por activos. Si no, verificar existencia simple
            if (tieneColumnaEstatus) {
                sql = "SELECT COUNT(*) FROM residente WHERE numero_control = ? AND estatus = TRUE";
            } else {
                sql = "SELECT COUNT(*) FROM residente WHERE numero_control = ?";
            }

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

    // ==================== IMPORTACIÓN MEJORADA ====================

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

        // *** FIX: SQL actualizado para nueva estructura ***
        String sql = "INSERT INTO residente (numero_control, nombre, apellido_paterno, apellido_materno, " +
                "semestre, correo, telefono, id_proyecto, estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, TRUE)";

        try {
            Connection conn = getConnection();
            // *** FIX: NO usar transacciones para evitar abortos en duplicados ***
            conn.setAutoCommit(true); // Cada insert es independiente

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (ModeloResidente residente : residentes) {
                try {
                    if (residente.getIdProyecto() <= 0) {
                        residente.setIdProyecto(1);
                    }

                    System.out.println("DEBUG: Procesando residente " + residente.getNumeroControl() +
                            " - " + residente.getNombre() + " " + residente.getApellidoPaterno());

                    // *** FIX: Verificar duplicados ANTES de insertar ***
                    if (existe(residente.getNumeroControl())) {
                        System.out.println("DEBUG: Ya existe en BD: " + residente.getNumeroControl());
                        duplicados++;
                        errores.add("Ya existe: " + residente.getNumeroControl() + " - " + residente.getNombre());
                        continue;
                    }

                    // *** FIX: Verificar correo duplicado ***
                    if (existeCorreo(residente.getCorreo())) {
                        System.out.println("DEBUG: Correo duplicado: " + residente.getCorreo());
                        duplicados++;
                        errores.add("Correo duplicado: " + residente.getCorreo() + " - " + residente.getNombre());
                        continue;
                    }

                    if (residente.getNumeroControl() <= 0) {
                        System.out.println("DEBUG: Número control inválido: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Número control inválido: " + residente.getNumeroControl());
                        continue;
                    }

                    if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) {
                        System.out.println("DEBUG: Nombre vacío: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Nombre vacío: " + residente.getNumeroControl());
                        continue;
                    }

                    if (residente.getApellidoPaterno() == null || residente.getApellidoPaterno().trim().isEmpty()) {
                        System.out.println("DEBUG: Apellido paterno vacío: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Apellido paterno vacío: " + residente.getNumeroControl());
                        continue;
                    }

                    if (residente.getCorreo() == null || residente.getCorreo().trim().isEmpty()) {
                        System.out.println("DEBUG: Correo vacío: " + residente.getNumeroControl());
                        fallidos++;
                        errores.add("Correo vacío: " + residente.getNumeroControl());
                        continue;
                    }

                    // Preparar inserción (mantener int → String para BD)
                    stmt.setString(1, String.valueOf(residente.getNumeroControl()));
                    stmt.setString(2, residente.getNombre().trim());
                    stmt.setString(3, residente.getApellidoPaterno().trim());
                    stmt.setString(4, residente.getApellidoMaterno() != null ? residente.getApellidoMaterno().trim() : null);
                    stmt.setInt(5, residente.getSemestre());
                    stmt.setString(6, residente.getCorreo().trim());
                    stmt.setString(7, residente.getTelefono() != null ? residente.getTelefono().trim() : null);
                    stmt.setInt(8, residente.getIdProyecto());

                    int filasAfectadas = stmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        exitosos++;
                        System.out.println("DEBUG: Insertado exitosamente: " + residente.getNumeroControl());
                    } else {
                        fallidos++;
                        errores.add("No se pudo insertar: " + residente.getNumeroControl());
                    }

                } catch (SQLException e) {
                    System.out.println("DEBUG: Error SQL para " + residente.getNumeroControl() + ": " + e.getMessage());

                    if (e.getMessage().toLowerCase().contains("duplicate") ||
                            e.getMessage().toLowerCase().contains("unique constraint") ||
                            e.getMessage().toLowerCase().contains("unicidad")) {
                        duplicados++;
                        errores.add("Duplicado: " + residente.getNumeroControl() + " - " + residente.getNombre());
                    } else {
                        fallidos++;
                        errores.add("Error: " + residente.getNumeroControl() + " - " + e.getMessage());
                    }
                }
            }

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

    // *** NUEVO: Verificar si existe un correo ***
    private static boolean existeCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM residente WHERE correo = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }

        return false;
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
        System.out.println("DEBUG: === VERIFICANDO PROYECTO POR DEFECTO ===");

        String sqlCheck = "SELECT COUNT(*) FROM proyecto WHERE id_proyecto = 1";
        String sqlInsert = "INSERT INTO proyecto (id_proyecto, nombre, descripcion, duracion, n_alumnos, estatus, origen) " +
                "VALUES (1, 'Proyecto Por Defecto', 'Proyecto temporal para residentes sin asignar', " +
                "'Indefinido', 0, 'Activo', 'Sistema')";

        try {
            Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("DEBUG: Proyectos con ID=1 encontrados: " + count);

                if (count == 0) {
                    System.out.println("DEBUG: Creando proyecto por defecto...");
                    PreparedStatement insertStmt = conn.prepareStatement(sqlInsert);
                    int filasInsertadas = insertStmt.executeUpdate();
                    System.out.println("DEBUG: Proyecto creado, filas insertadas: " + filasInsertadas);
                } else {
                    System.out.println("DEBUG: Proyecto por defecto ya existe");
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key") || e.getMessage().contains("already exists")) {
                System.out.println("DEBUG: Proyecto ya existe (error de duplicado ignorado)");
            } else {
                System.err.println("DEBUG: Error al crear proyecto por defecto: " + e.getMessage());
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

    public boolean isEsResidenteActivo() { return esResidenteActivo; }
    public void setEsResidenteActivo(boolean esResidenteActivo) { this.esResidenteActivo = esResidenteActivo; }

    public int getIdEstatus() { return idEstatus; }
    public void setIdEstatus(int idEstatus) { this.idEstatus = idEstatus; }


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