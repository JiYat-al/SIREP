package Controlador;

import Modelo.ModeloResidente;
import Vista.VistaResidentes.VistaResidente;
import Util.ExcelHandler;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ControladorResidente {
    private VistaResidente vista;
    private ModeloResidente modelo;

    public ControladorResidente(VistaResidente vista) {
        this.vista = vista;
        this.modelo = new ModeloResidente();
        configurarEventos();
    }

    /**
     * Configurar eventos de la vista
     */
    private void configurarEventos() {
        // Los eventos se configuran directamente en la vista, pero pueden ser manejados aquí
        // Este controlador actúa como intermediario entre la vista y el modelo
    }

    /**
     * Cargar Excel en la tabla para visualización - SIMPLIFICADO
     */
    public void cargarExcelEnTabla() {
        try {
            // Cambiar cursor a espera
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // La vista ahora maneja la carga directamente
            // Este método se mantiene por compatibilidad pero ya no es necesario
            System.out.println("Carga de Excel manejada por la vista");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "Error al cargar el archivo Excel:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Restaurar cursor normal
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Método para agregar un residente manual
     */
    public void agregarResidenteManual(ModeloResidente residente) {
        // Agregar a la vista
        vista.agregarResidente(residente);
        System.out.println("Residente agregado manualmente: " + residente.getNumeroControl());
    }

    /**
     * Revalidar todos los residentes en la tabla
     */
    public void revalidarTodosLosResidentes() {
        vista.revalidarResidentes();
        System.out.println("Residentes revalidados");
    }

    /**
     * NUEVO: Método para actualizar el estado de validación de un residente específico
     */
    public void actualizarEstadoValidacion(int numeroControl) {
        try {
            // Obtener la lista actual de residentes de la vista
            List<ModeloResidente> residentes = vista.getListaResidentes();

            // Buscar el residente específico y marcarlo como válido
            for (ModeloResidente residente : residentes) {
                if (residente.getNumeroControl() == numeroControl) {
                    // Marcar como válido ya que fue editado
                    residente.setEsValido(true);
                    residente.setMotivoInvalido("");
                    System.out.println("Residente " + numeroControl + " marcado como válido tras edición");
                    break;
                }
            }

            // Actualizar la vista
            vista.actualizarVisualizacion();

        } catch (Exception e) {
            System.err.println("Error al actualizar estado de validación: " + e.getMessage());
        }
    }

    /**
     * NUEVO: Método para validar un residente específico antes de guardarlo
     */
    public boolean validarResidenteParaGuardar(ModeloResidente residente) {
        // Validaciones básicas
        if (residente.getNumeroControl() <= 0) {
            return false;
        }

        if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) {
            return false;
        }

        if (residente.getApellidoPaterno() == null || residente.getApellidoPaterno().trim().isEmpty()) {
            return false;
        }

        if (residente.getCorreo() == null || residente.getCorreo().trim().isEmpty()) {
            return false;
        }

        // Si pasa todas las validaciones, marcarlo como válido
        residente.setEsValido(true);
        residente.setMotivoInvalido("");

        return true;
    }

    /**
     * Limpiar completamente el estado de validación
     */
    public void limpiarEstadoValidacion() {
        // Ahora la vista maneja su propio estado
        System.out.println("Estado de validación limpiado completamente");
    }

    /**
     * Importar datos a la base de datos - MEJORADO para manejar registros editados
     */
    public void importarABaseDatos() {
        try {
            List<ModeloResidente> residentes = vista.getListaResidentes();

            if (residentes.isEmpty()) {
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "No hay residentes para importar",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // NUEVO: Filtrar y revalidar residentes antes de importar
            List<ModeloResidente> residentesParaImportar = new ArrayList<>();
            List<String> residentesRechazados = new ArrayList<>();

            for (ModeloResidente residente : residentes) {
                // Revalidar cada residente antes de importar
                if (validarResidenteParaGuardar(residente)) {
                    residentesParaImportar.add(residente);
                } else {
                    residentesRechazados.add("No. Control: " + residente.getNumeroControl());
                }
            }

            // Mostrar resumen de validación
            if (!residentesRechazados.isEmpty()) {
                String mensaje = "Los siguientes registros no pasaron la validación:\n\n";
                for (String rechazado : residentesRechazados) {
                    mensaje += "• " + rechazado + "\n";
                }
                mensaje += "\n¿Desea continuar con los " + residentesParaImportar.size() + " registros válidos?";

                int opcion = JOptionPane.showConfirmDialog(vista.getPanelResidente(),
                        mensaje,
                        "Registros con problemas",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (opcion != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Proceder con la importación usando la vista
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Actualizar la lista en la vista solo con los residentes válidos
            vista.setListaResidentes(residentesParaImportar);

            // Proceder con la importación
            System.out.println("Importando " + residentesParaImportar.size() + " residentes válidos");

            // La vista maneja la importación real
            // pero ahora solo con registros revalidados

        } catch (Exception e) {
            System.err.println("Error en importación: " + e.getMessage());
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "Error durante la importación: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * NUEVO: Mtodo para procesar un residente editado
     */
    public void procesarResidenteEditado(ModeloResidente residenteEditado) {
        try {
            // Validar el residente editado
            if (validarResidenteParaGuardar(residenteEditado)) {
                // Actualizar en la vista
                vista.actualizarResidente(residenteEditado);

                // Actualizar estado de validación
                actualizarEstadoValidacion(residenteEditado.getNumeroControl());

                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "Residente editado correctamente\n" +
                                "Número de Control: " + residenteEditado.getNumeroControl(),
                        "Edición exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "El residente editado aún contiene errores\n" +
                                "Verifique todos los campos obligatorios",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "Error al procesar residente editado:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exportar datos actuales a Excel
     */
    public void exportarAExcel() {
        List<ModeloResidente> listaResidentes = vista.getListaResidentes();

        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "MNo hay datos para exportar",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            java.io.File archivo = ExcelHandler.seleccionarUbicacionGuardar();
            if (archivo != null) {
                ExcelHandler.exportarAExcel(listaResidentes, archivo);

                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "Archivo exportado exitosamente\n" +
                                "Ubicación: " + archivo.getAbsolutePath(),
                        "Exportación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "Error al exportar:\n" + e.getMessage(),
                    "Error de exportación",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Buscar un residente por número de control
     */
    public ModeloResidente buscarResidentePorNumeroControl(int numeroControl) {
        return ModeloResidente.buscarPorNumeroControl(numeroControl);
    }

    /**
     * Obtener todos los residentes de la base de datos
     */
    public List<ModeloResidente> obtenerTodosLosResidentes() {
        return ModeloResidente.obtenerTodos();
    }

    /**
     * Verificar si un residente existe en la base de datos
     */
    public boolean existeResidente(int numeroControl) {
        return ModeloResidente.existe(numeroControl);
    }

    /**
     * Cargar residentes desde la base de datos a la tabla
     */
    public void cargarResidentesDesdeBaseDatos() {
        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            List<ModeloResidente> residentes = ModeloResidente.obtenerTodos();

            if (residentes != null && !residentes.isEmpty()) {
                vista.cargarResidentes(residentes);

                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "Residentes cargados desde la base de datos\n" +
                                "Total de registros: " + residentes.size(),
                        "Carga exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "No se encontraron residentes en la base de datos",
                        "Sin datos",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "Error al cargar residentes desde la base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Limpiar la tabla de residentes
     */
    public void limpiarTabla() {
        vista.limpiarTabla();
        System.out.println("Tabla limpiada por controlador");
    }

    /**
     * Obtener el residente seleccionado en la tabla
     */
    public ModeloResidente obtenerResidenteSeleccionado() {
        return vista.getResidenteSeleccionado();
    }
}