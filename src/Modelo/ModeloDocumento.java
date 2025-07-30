package Modelo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModeloDocumento {
    private int idDocumento;
    private int idResidente;
    private String nombreDocumento;
    private String nombreArchivoOriginal;
    private String nombreArchivoSistema;
    private String rutaArchivo;
    private LocalDate fechaSubida;
    private LocalDate fechaRecibido;
    private boolean recibido;
    private String estado;
    private long tamañoArchivo;
    private String tipoArchivo;
    private String observaciones;

    // Constructores
    public ModeloDocumento() {
        this.recibido = false;
        this.estado = "No entregado";
        this.tipoArchivo = "PDF";
        this.fechaSubida = LocalDate.now();
    }

    public ModeloDocumento(int idResidente, String nombreDocumento) {
        this.idResidente = idResidente;
        this.nombreDocumento = nombreDocumento;
        this.estado = "No entregado";
        this.recibido = false;
    }

    // Métodos de conexión
    private static Connection getConnection() {
        return Conexion_bd.getInstancia().getConexion();
    }

    // ==================== MÉTODOS CRUD CORREGIDOS PARA POSTGRESQL ====================

    /**
     * Guardar documento en la base de datos - CORREGIDO PARA POSTGRESQL
     */
    public boolean guardar() {
        String sql = "INSERT INTO expediente_documento " +
                "(id_residente, nombre_documento, estado, recibido, " +
                "fecha_creacion, fecha_modificacion) " +
                "VALUES (?, ?, 'No entregado', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try {
            Connection conn = Conexion_bd.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            System.out.println("DEBUG: Ejecutando inserción de documento...");
            System.out.println("- idResidente: " + this.idResidente);
            System.out.println("- nombreDocumento: " + this.nombreDocumento);
            System.out.println("- rutaArchivo: " + this.rutaArchivo);

            stmt.setInt(1, this.idResidente);
            stmt.setString(2, this.nombreDocumento);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    this.idDocumento = keys.getInt(1);
                    System.out.println("DEBUG: Documento guardado con ID: " + this.idDocumento);
                    return true;
                }
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Error al guardar documento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualizar estado del documento - CORREGIDO
     */
    public boolean actualizar() {
        String sql = "UPDATE expediente_documento SET recibido = ?, estado = ?, fecha_recibido = ?, " +
                "observaciones = ? WHERE id_documento = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setBoolean(1, this.recibido);
            pstmt.setString(2, this.estado);

            if (this.recibido && this.fechaRecibido == null) {
                this.fechaRecibido = LocalDate.now();
            }

            if (this.fechaRecibido != null) {
                pstmt.setDate(3, Date.valueOf(this.fechaRecibido));
            } else {
                pstmt.setNull(3, Types.DATE);
            }

            pstmt.setString(4, this.observaciones);
            pstmt.setInt(5, this.idDocumento);

            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("DEBUG: Documento actualizado, filas afectadas: " + filasAfectadas);
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar documento: " + e.getMessage());
        }

        return false;
    }

    /**
     * Eliminar documento físico y de base de datos
     */
    public boolean eliminar() {
        String sql = "DELETE FROM expediente_documento WHERE id_documento = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.idDocumento);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar documento: " + e.getMessage());
        }

        return false;
    }

    // ==================== MÉTODOS DE CONSULTA CORREGIDOS ====================

    /**
     * Obtener documentos de un residente - CORREGIDO PARA POSTGRESQL
     */
    public static List<ModeloDocumento> obtenerPorResidente(int idResidente) {
        List<ModeloDocumento> documentos = new ArrayList<>();

        String sql = "SELECT id_documento, id_residente, nombre_documento, " +
                "nombre_archivo_original, nombre_archivo_sistema, " +
                "ruta_archivo, fecha_subida, fecha_recibido, " +
                "recibido, estado, observaciones " +
                "FROM expediente_documento " +
                "WHERE id_residente = ? " +
                "ORDER BY nombre_documento";

        try {
            Connection conn = Conexion_bd.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idResidente);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModeloDocumento doc = new ModeloDocumento();

                // Mapear campos de la base de datos
                doc.setIdDocumento(rs.getInt("id_documento"));
                doc.setIdResidente(rs.getInt("id_residente"));
                doc.setNombreDocumento(rs.getString("nombre_documento"));
                doc.setNombreArchivoOriginal(rs.getString("nombre_archivo_original"));
                doc.setNombreArchivoSistema(rs.getString("nombre_archivo_sistema"));
                doc.setRutaArchivo(rs.getString("ruta_archivo"));
                doc.setFechaSubida(rs.getDate("fecha_subida").toLocalDate());
                doc.setFechaRecibido(rs.getDate("fecha_recibido").toLocalDate());
                doc.setRecibido(rs.getBoolean("recibido"));
                doc.setEstado(rs.getString("estado"));
                doc.setObservaciones(rs.getString("observaciones"));

                documentos.add(doc);
            }

            System.out.println("DEBUG: Encontrados " + documentos.size() + " documentos para residente " + idResidente);

        } catch (SQLException e) {
            System.err.println("Error al obtener documentos del residente " + idResidente + ": " + e.getMessage());
            e.printStackTrace();
        }

        return documentos;
    }

    /**
     * Buscar documento específico - CORREGIDO
     */
    public static ModeloDocumento buscarPorResidenteYNombre(int idResidente, String nombreDocumento) {
        String sql = "SELECT * FROM expediente_documento WHERE id_residente = ? AND nombre_documento = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idResidente);
            pstmt.setString(2, nombreDocumento);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ModeloDocumento doc = new ModeloDocumento();
                doc.setIdDocumento(rs.getInt("id_documento"));
                doc.setIdResidente(rs.getInt("id_residente"));
                doc.setNombreDocumento(rs.getString("nombre_documento"));
                doc.setNombreArchivoOriginal(rs.getString("nombre_archivo_original"));
                doc.setNombreArchivoSistema(rs.getString("nombre_archivo_sistema"));
                doc.setRutaArchivo(rs.getString("ruta_archivo"));

                Date fechaSubida = rs.getDate("fecha_subida");
                if (fechaSubida != null) {
                    doc.setFechaSubida(fechaSubida.toLocalDate());
                }

                Date fechaRecibido = rs.getDate("fecha_recibido");
                if (fechaRecibido != null) {
                    doc.setFechaRecibido(fechaRecibido.toLocalDate());
                }

                doc.setRecibido(rs.getBoolean("recibido"));
                doc.setEstado(rs.getString("estado"));
                doc.setTamañoArchivo(rs.getLong("tamaño_archivo"));
                doc.setTipoArchivo(rs.getString("tipo_archivo"));
                doc.setObservaciones(rs.getString("observaciones"));

                return doc;
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar documento: " + e.getMessage());
        }

        return null;
    }

    /**
     * Marcar documento como recibido - MEJORADO
     */
    public static boolean marcarComoRecibido(int idResidente, String nombreDocumento, boolean recibido) {
        String sql = "UPDATE expediente_documento SET " +
                "recibido = ?, " +
                "estado = ?, " +
                "fecha_recibido = CASE WHEN ? = true THEN CURRENT_DATE ELSE NULL END, " +
                "fecha_modificacion = CURRENT_TIMESTAMP " +
                "WHERE id_residente = ? AND nombre_documento = ?";

        try {
            Connection conn = Conexion_bd.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setBoolean(1, recibido);
            stmt.setString(2, recibido ? "Entregado" : "No entregado");
            stmt.setBoolean(3, recibido);
            stmt.setInt(4, idResidente);
            stmt.setString(5, nombreDocumento);

            int filasAfectadas = stmt.executeUpdate();

            System.out.println("DEBUG: Documento actualizado, filas afectadas: " + filasAfectadas);
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al marcar documento como recibido: " + e.getMessage());
            return false;
        }
    }

    /**
     * NUEVO: Verificar si existe un documento
     */
    public static boolean existeDocumento(int idResidente, String nombreDocumento) {
        String sql = "SELECT COUNT(*) FROM expediente_documento WHERE id_residente = ? AND nombre_documento = ?";

        try {
            Connection conn = Conexion_bd.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idResidente);
            stmt.setString(2, nombreDocumento);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("DEBUG: Documento '" + nombreDocumento + "' para residente " + idResidente +
                        " - Existe: " + (count > 0) + " (registros encontrados: " + count + ")");
                return count > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de documento: " + e.getMessage());
        }

        return false;
    }

    /**
     * NUEVO: Obtener estadísticas del expediente
     */
    public static EstadisticasExpediente obtenerEstadisticas(int idResidente) {
        String sql = "SELECT " +
                "COUNT(*) as total, " +
                "COUNT(CASE WHEN recibido = true THEN 1 END) as entregados, " +
                "COUNT(CASE WHEN recibido = false THEN 1 END) as pendientes " +
                "FROM expediente_documento WHERE id_residente = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idResidente);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                int entregados = rs.getInt("entregados");
                int pendientes = rs.getInt("pendientes");

                return new EstadisticasExpediente(total, entregados, pendientes);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo estadísticas: " + e.getMessage());
        }

        return new EstadisticasExpediente(0, 0, 0);
    }

    // ==================== CLASES AUXILIARES ====================

    public static class EstadisticasExpediente {
        private final int total;
        private final int entregados;
        private final int pendientes;

        public EstadisticasExpediente(int total, int entregados, int pendientes) {
            this.total = total;
            this.entregados = entregados;
            this.pendientes = pendientes;
        }

        public int getTotal() { return total; }
        public int getEntregados() { return entregados; }
        public int getPendientes() { return pendientes; }
        public double getPorcentajeCompletado() {
            return total > 0 ? (double) entregados / total * 100 : 0;
        }
    }
    public static boolean actualizarArchivo(int idResidente, String nombreDocumento, String rutaArchivo, String nombreArchivo) {
        String sql = "UPDATE documento SET " +
                "ruta_archivo = ?, " +
                "nombre_archivo_sistema = ?, " +
                "fecha_subida = CURRENT_TIMESTAMP, " +
                "recibido = true, " +
                "estado = 'Entregado' " +
                "WHERE id_residente = ? AND nombre_documento = ?";

        try {
            Connection conn = Conexion_bd.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, rutaArchivo);
            stmt.setString(2, nombreArchivo);
            stmt.setInt(3, idResidente);
            stmt.setString(4, nombreDocumento);

            int filasAfectadas = stmt.executeUpdate();

            System.out.println("DEBUG: Archivo actualizado en BD, filas afectadas: " + filasAfectadas);
            System.out.println("- ID Residente: " + idResidente);
            System.out.println("- Documento: " + nombreDocumento);
            System.out.println("- Ruta: " + rutaArchivo);
            System.out.println("- Nombre archivo: " + nombreArchivo);

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar archivo en BD: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ==================== GETTERS Y SETTERS ====================

    public int getIdDocumento() { return idDocumento; }
    public void setIdDocumento(int idDocumento) { this.idDocumento = idDocumento; }

    public int getIdResidente() { return idResidente; }
    public void setIdResidente(int idResidente) { this.idResidente = idResidente; }

    public String getNombreDocumento() { return nombreDocumento; }
    public void setNombreDocumento(String nombreDocumento) { this.nombreDocumento = nombreDocumento; }

    public String getNombreArchivoOriginal() { return nombreArchivoOriginal; }
    public void setNombreArchivoOriginal(String nombreArchivoOriginal) { this.nombreArchivoOriginal = nombreArchivoOriginal; }

    public String getNombreArchivoSistema() { return nombreArchivoSistema; }
    public void setNombreArchivoSistema(String nombreArchivoSistema) { this.nombreArchivoSistema = nombreArchivoSistema; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public LocalDate getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDate fechaSubida) { this.fechaSubida = fechaSubida; }

    public LocalDate getFechaRecibido() { return fechaRecibido; }
    public void setFechaRecibido(LocalDate fechaRecibido) { this.fechaRecibido = fechaRecibido; }

    public boolean isRecibido() { return recibido; }
    public void setRecibido(boolean recibido) { this.recibido = recibido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public long getTamañoArchivo() { return tamañoArchivo; }
    public void setTamañoArchivo(long tamañoArchivo) { this.tamañoArchivo = tamañoArchivo; }

    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "ModeloDocumento{" +
                "idDocumento=" + idDocumento +
                ", idResidente=" + idResidente +
                ", nombreDocumento='" + nombreDocumento + '\'' +
                ", rutaArchivo='" + rutaArchivo + '\'' +
                ", recibido=" + recibido +
                ", estado='" + estado + '\'' +
                '}';
    }
}