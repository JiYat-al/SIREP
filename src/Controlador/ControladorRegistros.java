package Controlador;

import Modelo.ModeloResidente;
import Vista.VistaRegistros;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
// *** FIX: Agregar imports de SQL ***
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// *** FIX: Agregar import de Conexion_bd ***
import Modelo.Conexion_bd;

public class ControladorRegistros {
    private VistaRegistros vista;

    public ControladorRegistros(VistaRegistros vista) {
        this.vista = vista;

        // *** FIX: Debug simple al inicio ***
        System.out.println("=== INICIANDO CONTROLADOR REGISTROS ===");
        System.out.println("Probando conexión a BD...");

        try {
            // Probar conexión simple
            Connection conn = Conexion_bd.getInstancia().getConexion();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión a BD exitosa");

                // Contar registros totales
                String sqlCount = "SELECT COUNT(*) as total FROM residente";
                PreparedStatement stmt = conn.prepareStatement(sqlCount);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int total = rs.getInt("total");
                    System.out.println("📊 Total registros en BD: " + total);
                }

                // Contar registros activos
                String sqlActivos = "SELECT COUNT(*) as activos FROM residente WHERE estatus = TRUE";
                PreparedStatement stmtActivos = conn.prepareStatement(sqlActivos);
                ResultSet rsActivos = stmtActivos.executeQuery();
                if (rsActivos.next()) {
                    int activos = rsActivos.getInt("activos");
                    System.out.println("✅ Registros activos: " + activos);
                }

            } else {
                System.out.println("❌ Error: Conexión a BD falló");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }
    }

    // ==================== MÉTODOS PRINCIPALES ====================

    /**
     * Cargar todos los registros desde la base de datos
     */
    public void cargarTodosLosRegistros() {
        try {
            // Cambiar cursor a espera
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Obtener residentes desde el modelo
            List<ModeloResidente> residentes = ModeloResidente.obtenerTodos();

            if (residentes != null) {
                // Cargar en la vista
                vista.cargarResidentes(residentes);

                if (residentes.isEmpty()) {
                    vista.mostrarMensaje(
                            "⚠️ No se encontraron registros en la base de datos",
                            "Sin registros",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } else {
                vista.mostrarMensaje(
                        "❌ Error al cargar los registros desde la base de datos",
                        "Error de carga",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error inesperado al cargar registros:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.err.println("Error al cargar registros: " + e.getMessage());
        } finally {
            // Restaurar cursor normal
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * *** CAMBIO: Dar de baja en lugar de eliminar ***
     */
    public void darDeBajaRegistroSeleccionado() {
        ModeloResidente residenteSeleccionado = vista.getResidenteSeleccionado();

        if (residenteSeleccionado == null) {
            vista.mostrarMensaje(
                    "⚠️ Por favor seleccione un registro para dar de baja",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Confirmar dar de baja
        String mensaje = String.format(
                "¿Está seguro de dar de baja el siguiente registro?\n\n" +
                        "📝 Número de Control: %d\n" +
                        "👤 Nombre: %s %s %s\n" +
                        "🎓 Carrera: %s\n\n",
                residenteSeleccionado.getNumeroControl(),
                residenteSeleccionado.getNombre(),
                residenteSeleccionado.getApellidoPaterno(),
                residenteSeleccionado.getApellidoMaterno() != null ? residenteSeleccionado.getApellidoMaterno() : "",
                residenteSeleccionado.getCarrera()
        );

        boolean confirmado = vista.mostrarConfirmacion(mensaje, "Confirmar dar de baja");

        if (confirmado) {
            try {
                // Cambiar cursor a espera
                vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Dar de baja usando el modelo (método darDeBaja)
                boolean dadoDeBaja = residenteSeleccionado.darDeBaja();

                if (dadoDeBaja) {
                    vista.mostrarMensaje(
                            " Registro dado de baja exitosamente\n" +
                                    " Número de Control: " + residenteSeleccionado.getNumeroControl() + "\n" +
                                    "⚠️ El registro ya no aparecerá en las consultas normales",
                            "Baja exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Actualizar la tabla
                    vista.actualizarTabla();
                } else {
                    vista.mostrarMensaje(
                            "❌ No se pudo dar de baja el registro\n",
                            "Error al dar de baja",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (Exception e) {
                vista.mostrarMensaje(
                        "❌ Error al dar de baja el registro:\n" + e.getMessage(),
                        "Error al dar de baja",
                        JOptionPane.ERROR_MESSAGE
                );
                System.err.println("Error al dar de baja registro: " + e.getMessage());
            } finally {
                // Restaurar cursor normal
                vista.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    /**
     * *** MANTENIDO: Método eliminar para compatibilidad (ahora llama a darDeBaja) ***
     */
    @Deprecated
    public void eliminarRegistroSeleccionado() {
        // Redirigir al nuevo método darDeBajaRegistroSeleccionado
        darDeBajaRegistroSeleccionado();
    }

    /**
     * Editar el registro seleccionado
     */
    public void editarRegistroSeleccionado() {
        ModeloResidente residenteSeleccionado = vista.getResidenteSeleccionado();

        if (residenteSeleccionado == null) {
            vista.mostrarMensaje(
                    "⚠️ Por favor seleccione un registro para editar",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // Delegar la edición a la vista (que tiene acceso a DialogoEdicion)
            vista.abrirDialogoEdicion(residenteSeleccionado);

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error al abrir el editor:\n" + e.getMessage(),
                    "Error de edición",
                    JOptionPane.ERROR_MESSAGE
            );
            System.err.println("Error al editar registro: " + e.getMessage());
        }
    }

    /**
     * Buscar registros por criterio
     */
    public void buscarRegistros(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            cargarTodosLosRegistros();
            return;
        }

        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Intentar buscar por número de control primero
            try {
                int numeroControl = Integer.parseInt(criterio.trim());
                ModeloResidente residente = ModeloResidente.buscarPorNumeroControl(numeroControl);

                if (residente != null) {
                    // Mostrar solo este registro
                    java.util.List<ModeloResidente> resultado = java.util.Arrays.asList(residente);
                    vista.cargarResidentes(resultado);
                } else {
                    vista.cargarResidentes(new java.util.ArrayList<>());
                    vista.mostrarMensaje(
                            "⚠️ No se encontró ningún residente con el número de control: " + numeroControl,
                            "Sin resultados",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                // Si no es un número, buscar por nombre
                buscarPorNombre(criterio);
            }

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error durante la búsqueda:\n" + e.getMessage(),
                    "Error de búsqueda",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Buscar por nombre (método auxiliar)
     */
    private void buscarPorNombre(String nombre) {
        // Obtener todos los registros y filtrar por nombre
        List<ModeloResidente> todosLosResidentes = ModeloResidente.obtenerTodos();
        java.util.List<ModeloResidente> resultados = new java.util.ArrayList<>();

        String nombreBusqueda = nombre.toLowerCase().trim();

        for (ModeloResidente residente : todosLosResidentes) {
            String nombreCompleto = (residente.getNombre() + " " +
                    residente.getApellidoPaterno() + " " +
                    (residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : ""))
                    .toLowerCase();

            if (nombreCompleto.contains(nombreBusqueda) ||
                    residente.getNombre().toLowerCase().contains(nombreBusqueda) ||
                    residente.getApellidoPaterno().toLowerCase().contains(nombreBusqueda) ||
                    (residente.getApellidoMaterno() != null &&
                            residente.getApellidoMaterno().toLowerCase().contains(nombreBusqueda))) {
                resultados.add(residente);
            }
        }

        vista.cargarResidentes(resultados);

        if (resultados.isEmpty()) {
            vista.mostrarMensaje(
                    "⚠️ No se encontraron registros que coincidan con: " + nombre,
                    "Sin resultados",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * *** NUEVO: Reactivar un residente que fue dado de baja ***
     */
    public void reactivarResidente(int idResidente) {
        try {
            // Buscar el residente por ID (incluyendo inactivos)
            ModeloResidente residente = buscarResidentePorId(idResidente);

            if (residente != null && !residente.isEstatus()) {
                boolean reactivado = residente.reactivar();

                if (reactivado) {
                    vista.mostrarMensaje(
                            " Residente reactivado exitosamente\n" +
                                    "Número de Control: " + residente.getNumeroControl(),
                            "Reactivación exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Actualizar la tabla
                    vista.actualizarTabla();
                } else {
                    vista.mostrarMensaje(
                            "❌ No se pudo reactivar el residente",
                            "Error de reactivación",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error al reactivar residente:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * *** NUEVO: Buscar residente por ID (incluyendo inactivos) ***
     */
    private ModeloResidente buscarResidentePorId(int idResidente) {
        // Este método necesitaría ser implementado en ModeloResidente
        // Por ahora, retornamos null
        return null;
    }

    /**
     * Verificar si existe un residente (solo activos)
     */
    public boolean existeResidente(int numeroControl) {
        return ModeloResidente.existe(numeroControl);
    }

    /**
     * Obtener residente por número de control (solo activos)
     */
    public ModeloResidente obtenerResidentePorNumeroControl(int numeroControl) {
        return ModeloResidente.buscarPorNumeroControl(numeroControl);
    }

    /**
     * *** NUEVO: Obtener estadísticas de registros ***
     */
    public void mostrarEstadisticas() {
        try {
            List<ModeloResidente> residentes = ModeloResidente.obtenerTodos();

            if (residentes.isEmpty()) {
                vista.mostrarMensaje(
                        "⚠️ No hay registros para mostrar estadísticas",
                        "Sin datos",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Calcular estadísticas básicas
            int totalRegistros = residentes.size();

            // Contar por semestre
            java.util.Map<Integer, Integer> porSemestre = new java.util.HashMap<>();
            for (ModeloResidente residente : residentes) {
                porSemestre.put(residente.getSemestre(),
                        porSemestre.getOrDefault(residente.getSemestre(), 0) + 1);
            }

            // Crear mensaje de estadísticas
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("📊 Estadísticas de Registros Activos\n\n");
            mensaje.append("📋 Total de registros activos: ").append(totalRegistros).append("\n\n");
            mensaje.append("📚 Distribución por semestre:\n");

            for (java.util.Map.Entry<Integer, Integer> entrada : porSemestre.entrySet()) {
                mensaje.append("  • Semestre ").append(entrada.getKey())
                        .append(": ").append(entrada.getValue()).append(" estudiantes\n");
            }

            vista.mostrarMensaje(
                    mensaje.toString(),
                    "Estadísticas del Sistema",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error al generar estadísticas:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * *** NUEVO: Verificar integridad de datos ***
     */
    public void verificarIntegridadDatos() {
        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            List<ModeloResidente> residentes = ModeloResidente.obtenerTodos();
            java.util.List<String> problemasEncontrados = new java.util.ArrayList<>();

            // Verificar duplicados por número de control
            java.util.Set<Integer> numerosControl = new java.util.HashSet<>();
            for (ModeloResidente residente : residentes) {
                if (!numerosControl.add(residente.getNumeroControl())) {
                    problemasEncontrados.add("Número de control duplicado: " + residente.getNumeroControl());
                }
            }

            // Verificar correos duplicados
            java.util.Set<String> correos = new java.util.HashSet<>();
            for (ModeloResidente residente : residentes) {
                if (residente.getCorreo() != null && !residente.getCorreo().trim().isEmpty()) {
                    if (!correos.add(residente.getCorreo().toLowerCase())) {
                        problemasEncontrados.add("Correo duplicado: " + residente.getCorreo());
                    }
                }
            }

            // Verificar campos obligatorios vacíos
            for (ModeloResidente residente : residentes) {
                if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) {
                    problemasEncontrados.add("Nombre vacío: No. Control " + residente.getNumeroControl());
                }
                if (residente.getApellidoPaterno() == null || residente.getApellidoPaterno().trim().isEmpty()) {
                    problemasEncontrados.add("Apellido paterno vacío: No. Control " + residente.getNumeroControl());
                }
                if (residente.getCorreo() == null || residente.getCorreo().trim().isEmpty()) {
                    problemasEncontrados.add("Correo vacío: No. Control " + residente.getNumeroControl());
                }
            }

            // Mostrar resultados
            if (problemasEncontrados.isEmpty()) {
                vista.mostrarMensaje(
                        "✅ Verificación completada exitosamente\n\n" +
                                "No se encontraron problemas de integridad en los datos.\n" +
                                "Total de registros verificados: " + residentes.size(),
                        "Integridad de Datos - OK",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("⚠️ Se encontraron ").append(problemasEncontrados.size())
                        .append(" problemas de integridad:\n\n");

                int maxProblemas = Math.min(problemasEncontrados.size(), 10);
                for (int i = 0; i < maxProblemas; i++) {
                    mensaje.append("• ").append(problemasEncontrados.get(i)).append("\n");
                }

                if (problemasEncontrados.size() > 10) {
                    mensaje.append("\n... y ").append(problemasEncontrados.size() - 10)
                            .append(" problemas adicionales.");
                }

                vista.mostrarMensaje(
                        mensaje.toString(),
                        "Problemas de Integridad Encontrados",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error durante la verificación de integridad:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }
}