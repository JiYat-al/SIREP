package Utilidades;

import Modelo.ModeloResidente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Clase auxiliar para importar residentes con reporte de progreso
 */
public class ImportadorConProgreso {

    /**
     * Clase para reportar progreso de importación
     */
    public static class ProgresoImportacion {
        private final int procesados;
        private final int total;
        private final int exitosos;
        private final int fallidos;
        private final int duplicados;
        private final String mensajeActual;

        public ProgresoImportacion(int procesados, int total, int exitosos, int fallidos, int duplicados, String mensajeActual) {
            this.procesados = procesados;
            this.total = total;
            this.exitosos = exitosos;
            this.fallidos = fallidos;
            this.duplicados = duplicados;
            this.mensajeActual = mensajeActual;
        }

        // Getters
        public int getProcesados() { return procesados; }
        public int getTotal() { return total; }
        public int getExitosos() { return exitosos; }
        public int getFallidos() { return fallidos; }
        public int getDuplicados() { return duplicados; }
        public String getMensajeActual() { return mensajeActual; }

        public double getPorcentaje() {
            return total > 0 ? (double) procesados / total * 100 : 0;
        }
    }

    /**
     * Importar residentes con reporte de progreso
     * @param residentes Lista de residentes a importar
     * @param callbackProgreso Función que se llama para reportar progreso
     * @return Resultado de la importación
     */
    public static ModeloResidente.ResultadoImportacion importarConProgreso(
            List<ModeloResidente> residentes,
            BiConsumer<ProgresoImportacion, Boolean> callbackProgreso) {

        final int TAMAÑO_LOTE = 50; // Lotes más pequeños para mejor reporte de progreso

        int exitosos = 0;
        int fallidos = 0;
        int duplicados = 0;
        List<String> errores = new ArrayList<>();

        // Configuración de base de datos (usa los mismos valores que ModeloResidente)
        String URL = "jdbc:postgresql://localhost:5432/tu_base_datos";
        String USER = "tu_usuario";
        String PASSWORD = "tu_password";

        String sql = "INSERT INTO residentes (numero_control, nombre, apellido_paterno, apellido_materno, " +
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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Procesar en lotes con reporte de progreso
                for (int i = 0; i < residentes.size(); i += TAMAÑO_LOTE) {
                    int finLote = Math.min(i + TAMAÑO_LOTE, residentes.size());
                    List<ModeloResidente> loteActual = residentes.subList(i, finLote);

                    // Reportar progreso antes del lote
                    ProgresoImportacion progreso = new ProgresoImportacion(
                            i, residentes.size(), exitosos, fallidos, duplicados,
                            "Procesando lote " + (i/TAMAÑO_LOTE + 1) + "..."
                    );
                    callbackProgreso.accept(progreso, false);

                    try {
                        // Agregar lote al batch
                        for (ModeloResidente residente : loteActual) {
                            try {
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

                        // Limpiar batch
                        stmt.clearBatch();

                    } catch (SQLException e) {
                        // Rollback del lote actual
                        try {
                            conn.rollback();
                        } catch (SQLException rollbackEx) {
                            errores.add("Error en rollback: " + rollbackEx.getMessage());
                        }

                        // Marcar registros del lote como fallidos
                        fallidos += loteActual.size();
                        errores.add("Error en lote " + (i/TAMAÑO_LOTE + 1) + ": " + e.getMessage());
                    }

                    // Reportar progreso después del lote
                    int procesados = Math.min(i + TAMAÑO_LOTE, residentes.size());
                    ProgresoImportacion progresoFinal = new ProgresoImportacion(
                            procesados, residentes.size(), exitosos, fallidos, duplicados,
                            "Completado lote " + (i/TAMAÑO_LOTE + 1)
                    );
                    callbackProgreso.accept(progresoFinal, false);

                    // Pausa breve para no sobrecargar
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
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
            errores.add("Error de conexión: " + e.getMessage());
            fallidos = residentes.size();
        }

        // Reportar progreso final
        ProgresoImportacion progresoFinal = new ProgresoImportacion(
                residentes.size(), residentes.size(), exitosos, fallidos, duplicados,
                "Importación completada"
        );
        callbackProgreso.accept(progresoFinal, true);

        return new ModeloResidente.ResultadoImportacion(exitosos, fallidos, duplicados, errores);
    }
}