package Controlador;



import Modelo.ModeloResidente;
import Vista.VistaResidentes.VistaResidente;
import Utilidades.ExcelHandler;
import javax.swing.*;
import java.awt.*;
import java.util.List;

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
     * Cargar Excel en la tabla para visualización (sin guardar en BD)
     */
    public void cargarExcelEnTabla() {
        try {
            // Cambiar cursor a espera
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Seleccionar archivo Excel
            java.io.File archivo = ExcelHandler.seleccionarArchivoExcel();
            if (archivo == null) {
                return; // Usuario canceló
            }

            // Cargar datos desde Excel
            List<ModeloResidente> residentes = ExcelHandler.importarDesdeExcel(archivo);

            if (residentes != null && !residentes.isEmpty()) {
                // Cargar los datos en la tabla a través de la vista
                vista.cargarResidentes(residentes);

                // Mostrar mensaje de confirmación
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "✅ Archivo Excel cargado exitosamente\n" +
                                "📊 Registros cargados: " + residentes.size() + "\n" +
                                "💡 Use 'Importar Excel' para guardar en la base de datos",
                        "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "⚠️ No se encontraron datos válidos en el archivo Excel",
                        "Sin datos", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "❌ Error al cargar el archivo Excel:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Restaurar cursor normal
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Importar datos actuales de la tabla a la base de datos
     */
    public void importarABaseDatos() {
        List<ModeloResidente> listaResidentes = vista.getListaResidentes();

        // Verificar que hay datos en la tabla
        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "⚠️ No hay datos para importar\n" +
                            "💡 Primero use 'Cargar Excel' para cargar datos en la tabla",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar la importación
        int opcion = JOptionPane.showConfirmDialog(vista.getPanelResidente(),
                "¿Confirma importar " + listaResidentes.size() + " registros a la base de datos?\n\n" +
                        "⚡ El proceso se realizará en lotes optimizados",
                "Confirmar importación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                // Cambiar cursor a espera
                vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Crear diálogo de procesamiento
                JDialog processingDialog = crearDialogoProcesamiento(listaResidentes.size());

                // Realizar importación en hilo separado
                SwingWorker<ModeloResidente.ResultadoImportacion, Void> worker =
                        new SwingWorker<ModeloResidente.ResultadoImportacion, Void>() {

                            @Override
                            protected ModeloResidente.ResultadoImportacion doInBackground() throws Exception {
                                // Usar el modelo para realizar la importación
                                return ModeloResidente.importarResidentes(listaResidentes);
                            }

                            @Override
                            protected void done() {
                                try {
                                    // Cerrar diálogo de procesamiento
                                    processingDialog.dispose();

                                    // Obtener resultado
                                    ModeloResidente.ResultadoImportacion resultado = get();

                                    // Mostrar resultado
                                    mostrarResultadoImportacion(resultado);

                                    // Si fue exitoso, limpiar tabla
                                    if (resultado.getExitosos() > 0) {
                                        vista.limpiarTabla();
                                    }

                                } catch (Exception e) {
                                    processingDialog.dispose();
                                    JOptionPane.showMessageDialog(vista.getPanelResidente(),
                                            "❌ Error durante la importación:\n" + e.getMessage(),
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    vista.setCursor(Cursor.getDefaultCursor());
                                }
                            }
                        };

                // Mostrar diálogo y ejecutar worker
                SwingUtilities.invokeLater(() -> processingDialog.setVisible(true));
                worker.execute();

            } catch (Exception e) {
                vista.setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "❌ Error al iniciar la importación:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Crear diálogo de procesamiento
     */
    private JDialog crearDialogoProcesamiento(int totalRegistros) {
        JOptionPane processingPane = new JOptionPane(
                "🔄 Procesando importación...\n" +
                        "📊 Registros: " + totalRegistros + "\n" +
                        "⏳ Por favor espere...",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{},
                null
        );

        return processingPane.createDialog(vista.getPanelResidente(), "Importando");
    }

    /**
     * Mostrar resultado de la importación
     */
    private void mostrarResultadoImportacion(ModeloResidente.ResultadoImportacion resultado) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("🚀 Importación completada:\n\n");
        mensaje.append("✅ Registros exitosos: ").append(resultado.getExitosos()).append("\n");
        mensaje.append("❌ Registros fallidos: ").append(resultado.getFallidos()).append("\n");
        mensaje.append("🔄 Registros duplicados: ").append(resultado.getDuplicados()).append("\n");
        mensaje.append("📊 Total procesados: ").append(resultado.getTotal()).append("\n");

        // Calcular tasa de éxito
        double tasaExito = resultado.getTotal() > 0 ?
                (double) resultado.getExitosos() / resultado.getTotal() * 100 : 0;
        mensaje.append("📈 Tasa de éxito: ").append(String.format("%.1f%%", tasaExito));

        int tipoMensaje;
        String titulo;

        if (resultado.getExitosos() == resultado.getTotal()) {
            tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
            titulo = "✅ Importación completamente exitosa";
        } else if (resultado.getExitosos() > 0) {
            tipoMensaje = JOptionPane.WARNING_MESSAGE;
            titulo = "⚠️ Importación parcialmente exitosa";
            agregarErroresAlMensaje(mensaje, resultado.getErrores());
        } else {
            tipoMensaje = JOptionPane.ERROR_MESSAGE;
            titulo = "❌ Importación fallida";
            agregarErroresAlMensaje(mensaje, resultado.getErrores());
        }

        JOptionPane.showMessageDialog(vista.getPanelResidente(), mensaje.toString(), titulo, tipoMensaje);

        // Ofrecer ver log completo si hay muchos errores
        if (resultado.getErrores().size() > 3) {
            ofrecerLogCompleto(resultado.getErrores());
        }
    }

    /**
     * Agregar errores al mensaje de resultado
     */
    private void agregarErroresAlMensaje(StringBuilder mensaje, List<String> errores) {
        if (!errores.isEmpty()) {
            mensaje.append("\n\n📋 Primeros errores encontrados:\n");
            for (int i = 0; i < Math.min(3, errores.size()); i++) {
                mensaje.append("• ").append(errores.get(i)).append("\n");
            }

            if (errores.size() > 3) {
                mensaje.append("... y ").append(errores.size() - 3).append(" errores más");
            }
        }
    }

    /**
     * Ofrecer mostrar log completo de errores
     */
    private void ofrecerLogCompleto(List<String> errores) {
        int opcionErrores = JOptionPane.showConfirmDialog(vista.getPanelResidente(),
                "¿Desea ver el log completo de errores?\n" +
                        "Se encontraron " + errores.size() + " errores en total.",
                "Ver errores completos",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcionErrores == JOptionPane.YES_OPTION) {
            mostrarLogCompleto(errores);
        }
    }

    /**
     * Mostrar log completo de errores
     */
    private void mostrarLogCompleto(List<String> errores) {
        JDialog dialogoLog = new JDialog((Frame) SwingUtilities.getWindowAncestor(vista.getPanelResidente()),
                "Log de errores", true);
        dialogoLog.setSize(700, 500);
        dialogoLog.setLocationRelativeTo(vista.getPanelResidente());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        StringBuilder logCompleto = new StringBuilder();
        logCompleto.append("=== LOG DE ERRORES DE IMPORTACIÓN ===\n\n");
        for (int i = 0; i < errores.size(); i++) {
            logCompleto.append(String.format("%3d. %s\n", (i + 1), errores.get(i)));
        }

        textArea.setText(logCompleto.toString());
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton cerrarButton = new JButton("Cerrar");
        cerrarButton.addActionListener(e -> dialogoLog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(cerrarButton);

        dialogoLog.setLayout(new BorderLayout());
        dialogoLog.add(scrollPane, BorderLayout.CENTER);
        dialogoLog.add(buttonPanel, BorderLayout.SOUTH);

        dialogoLog.setVisible(true);
    }

    /**
     * Exportar datos actuales a Excel
     */
    public void exportarAExcel() {
        List<ModeloResidente> listaResidentes = vista.getListaResidentes();

        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "⚠️ No hay datos para exportar",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            java.io.File archivo = ExcelHandler.seleccionarUbicacionGuardar();
            if (archivo != null) {
                ExcelHandler.exportarAExcel(listaResidentes, archivo);

                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "✅ Archivo exportado exitosamente\n" +
                                "📁 Ubicación: " + archivo.getAbsolutePath(),
                        "Exportación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "❌ Error al exportar:\n" + e.getMessage(),
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
                        "✅ Residentes cargados desde la base de datos\n" +
                                "📊 Total de registros: " + residentes.size(),
                        "Carga exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "⚠️ No se encontraron residentes en la base de datos",
                        "Sin datos",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "❌ Error al cargar residentes desde la base de datos:\n" + e.getMessage(),
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
    }

    /**
     * Obtener el residente seleccionado en la tabla
     */
    public ModeloResidente obtenerResidenteSeleccionado() {
        return vista.getResidenteSeleccionado();
    }
}
