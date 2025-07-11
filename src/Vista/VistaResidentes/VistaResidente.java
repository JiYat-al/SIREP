package Vista.VistaResidentes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import Modelo.ModeloResidente;
import Utilidades.ExcelHandler;

public class VistaResidente {
    private JPanel panelResidente;
    private JButton importarExelButton;
    private JTable tablaResidentes;
    private JButton cargarExelButton;
    private JButton cancelarButton;
    private JTextField textField1;
    private JButton agregarManualButton;

    // Modelo de la tabla para poder manipularlo después
    private DefaultTableModel modeloTabla;

    // Lista para almacenar los residentes (útil para Excel)
    private List<ModeloResidente> listaResidentes;

    public VistaResidente() {
        // Inicializar la lista si no fue inicializada en createUIComponents
        if (listaResidentes == null) {
            listaResidentes = new ArrayList<>();
        }
        // Configurar eventos después de que los componentes estén listos
        configurarEventos();
    }

    private void configurarEventos() {
        // Configurar eventos de los botones
        if (importarExelButton != null) {
            importarExelButton.addActionListener(e -> importarABaseDatos());
        }

        if (cargarExelButton != null) {
            cargarExelButton.addActionListener(e -> cargarExcelEnTabla());
        }

        if (agregarManualButton != null) {
            agregarManualButton.addActionListener(e -> agregarResidenteManual());
        }

        if (cancelarButton != null) {
            cancelarButton.addActionListener(e -> cancelar());
        }
    }

    // Método llamado automáticamente por IntelliJ GUI Designer
    private void createUIComponents() {
        // Inicializar la lista si no existe (se llama antes del constructor)
        if (listaResidentes == null) {
            listaResidentes = new ArrayList<>();
        }

        // Definir las columnas de la tabla (sin ID Proyecto)
        String[] columnas = {
                "Número de Control", "Nombre", "Apellido Paterno", "Apellido Materno",
                "Carrera", "Semestre", "Correo", "Teléfono"
        };

        // Crear el modelo de la tabla
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla de solo lectura
            }
        };

        // Crear la tabla con el modelo
        tablaResidentes = new JTable(modeloTabla);

        // IMPORTANTE: Configurar para que se muestren los headers
        tablaResidentes.setTableHeader(tablaResidentes.getTableHeader());

        // Configurar propiedades básicas de la tabla
        tablaResidentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResidentes.setRowHeight(25);
        tablaResidentes.getTableHeader().setReorderingAllowed(false);

        // Configurar la tabla
        configurarTabla();
    }

    private void configurarTabla() {
        if (tablaResidentes == null) return;

        // Configurar el comportamiento de la tabla
        tablaResidentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResidentes.setRowHeight(25);
        tablaResidentes.getTableHeader().setReorderingAllowed(false);

        // Configurar el ancho de las columnas (sin ID Proyecto)
        tablaResidentes.getColumnModel().getColumn(0).setPreferredWidth(120); // Número de Control
        tablaResidentes.getColumnModel().getColumn(1).setPreferredWidth(100); // Nombre
        tablaResidentes.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellido Paterno
        tablaResidentes.getColumnModel().getColumn(3).setPreferredWidth(120); // Apellido Materno
        tablaResidentes.getColumnModel().getColumn(4).setPreferredWidth(150); // Carrera
        tablaResidentes.getColumnModel().getColumn(5).setPreferredWidth(80);  // Semestre
        tablaResidentes.getColumnModel().getColumn(6).setPreferredWidth(200); // Correo
        tablaResidentes.getColumnModel().getColumn(7).setPreferredWidth(120); // Teléfono
    }

    // Método para agregar un residente a la tabla (sin ID Proyecto)
    public void agregarResidente(ModeloResidente residente) {
        listaResidentes.add(residente);
        Object[] fila = {
                residente.getNumeroControl(),
                residente.getNombre(),
                residente.getApellidoPaterno(),
                residente.getApellidoMaterno(),
                residente.getCarrera(),
                residente.getSemestre(),
                residente.getCorreo(),
                residente.getTelefono()
        };
        modeloTabla.addRow(fila);
    }

    // Método para cargar múltiples residentes (útil para Excel)
    public void cargarResidentes(List<ModeloResidente> residentes) {
        // Limpiar la tabla actual
        limpiarTabla();

        // Agregar todos los residentes
        for (ModeloResidente residente : residentes) {
            agregarResidente(residente);
        }
    }

    // Método para limpiar la tabla
    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
        listaResidentes.clear();
    }

    // Método para obtener la lista de residentes
    public List<ModeloResidente> getListaResidentes() {
        return new ArrayList<>(listaResidentes);
    }

    // Método para obtener el residente seleccionado
    public ModeloResidente getResidenteSeleccionado() {
        int filaSeleccionada = tablaResidentes.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < listaResidentes.size()) {
            return listaResidentes.get(filaSeleccionada);
        }
        return null;
    }

    /**
     * Cargar Excel en la tabla para visualización (sin guardar en BD)
     */
    private void cargarExcelEnTabla() {
        try {
            // Cambiar cursor a espera
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Seleccionar archivo Excel
            java.io.File archivo = ExcelHandler.seleccionarArchivoExcel();
            if (archivo == null) {
                return; // Usuario canceló
            }

            // Cargar datos desde Excel
            List<ModeloResidente> residentes = ExcelHandler.importarDesdeExcel(archivo);

            if (residentes != null && !residentes.isEmpty()) {
                // Cargar los datos en la tabla
                cargarResidentes(residentes);

                // Mostrar mensaje de confirmación
                JOptionPane.showMessageDialog(panelResidente,
                        "✅ Archivo Excel cargado exitosamente\n" +
                                "📊 Registros cargados: " + residentes.size() + "\n" +
                                "💡 Use 'Importar Excel' para guardar en la base de datos",
                        "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelResidente,
                        "⚠️ No se encontraron datos válidos en el archivo Excel",
                        "Sin datos", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelResidente,
                    "❌ Error al cargar el archivo Excel:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Restaurar cursor normal
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Importar datos actuales de la tabla a la base de datos (SIN PROGRESS BAR)
     */
    private void importarABaseDatos() {
        // Verificar que hay datos en la tabla
        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(panelResidente,
                    "⚠️ No hay datos para importar\n" +
                            "💡 Primero use 'Cargar Excel' para cargar datos en la tabla",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar la importación
        int opcion = JOptionPane.showConfirmDialog(panelResidente,
                "¿Confirma importar " + listaResidentes.size() + " registros a la base de datos?\n\n" +
                        "⚡ El proceso se realizará en lotes optimizados",
                "Confirmar importación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                // Cambiar cursor a espera
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Mostrar mensaje de procesamiento
                JOptionPane processingPane = new JOptionPane(
                        "🔄 Procesando importación...\n" +
                                "📊 Registros: " + listaResidentes.size() + "\n" +
                                "⏳ Por favor espere...",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        null,
                        new Object[]{},
                        null
                );

                JDialog processingDialog = processingPane.createDialog(panelResidente, "Importando");

                // Mostrar diálogo sin bloquear
                SwingUtilities.invokeLater(() -> processingDialog.setVisible(true));

                // Realizar importación en hilo separado para no bloquear UI
                SwingWorker<ModeloResidente.ResultadoImportacion, Void> worker =
                        new SwingWorker<ModeloResidente.ResultadoImportacion, Void>() {

                            @Override
                            protected ModeloResidente.ResultadoImportacion doInBackground() throws Exception {
                                // Realizar la importación
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
                                        limpiarTabla();
                                    }

                                } catch (Exception e) {
                                    processingDialog.dispose();
                                    JOptionPane.showMessageDialog(panelResidente,
                                            "❌ Error durante la importación:\n" + e.getMessage(),
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    setCursor(Cursor.getDefaultCursor());
                                }
                            }
                        };

                worker.execute();

            } catch (Exception e) {
                setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(panelResidente,
                        "❌ Error al iniciar la importación:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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

            if (!resultado.getErrores().isEmpty()) {
                mensaje.append("\n\n📋 Primeros errores encontrados:\n");
                for (int i = 0; i < Math.min(3, resultado.getErrores().size()); i++) {
                    mensaje.append("• ").append(resultado.getErrores().get(i)).append("\n");
                }

                if (resultado.getErrores().size() > 3) {
                    mensaje.append("... y ").append(resultado.getErrores().size() - 3).append(" errores más");
                }
            }
        } else {
            tipoMensaje = JOptionPane.ERROR_MESSAGE;
            titulo = "❌ Importación fallida";

            if (!resultado.getErrores().isEmpty()) {
                mensaje.append("\n\n📋 Errores principales:\n");
                for (int i = 0; i < Math.min(3, resultado.getErrores().size()); i++) {
                    mensaje.append("• ").append(resultado.getErrores().get(i)).append("\n");
                }
            }
        }

        JOptionPane.showMessageDialog(panelResidente, mensaje.toString(), titulo, tipoMensaje);

        // Ofrecer ver log completo si hay muchos errores
        if (resultado.getErrores().size() > 3) {
            int opcionErrores = JOptionPane.showConfirmDialog(panelResidente,
                    "¿Desea ver el log completo de errores?\n" +
                            "Se encontraron " + resultado.getErrores().size() + " errores en total.",
                    "Ver errores completos",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcionErrores == JOptionPane.YES_OPTION) {
                mostrarLogCompleto(resultado.getErrores());
            }
        }
    }

    private void mostrarLogCompleto(List<String> errores) {
        JDialog dialogoLog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panelResidente),
                "Log de errores", true);
        dialogoLog.setSize(700, 500);
        dialogoLog.setLocationRelativeTo(panelResidente);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        StringBuilder logCompleto = new StringBuilder();
        logCompleto.append("=== LOG DE ERRORES DE IMPORTACIÓN ===\n\n");
        for (int i = 0; i < errores.size(); i++) {
            logCompleto.append(String.format("%3d. %s\n", (i + 1), errores.get(i)));
        }

        textArea.setText(logCompleto.toString());
        textArea.setCaretPosition(0); // Ir al inicio

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

    private void agregarResidenteManual() {
        DialogoResidente dialogo = new DialogoResidente((Frame) SwingUtilities.getWindowAncestor(panelResidente));
        dialogo.setVisible(true);

        if (dialogo.isConfirmado()) {
            ModeloResidente residente = dialogo.getResidente();
            agregarResidente(residente);

            JOptionPane.showMessageDialog(panelResidente,
                    "✅ Residente agregado correctamente a la tabla\n" +
                            "💡 Use 'Importar Excel' para guardarlo en la base de datos",
                    "Residente agregado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelar() {
        // Cerrar la ventana o realizar acción de cancelación
        Window window = SwingUtilities.getWindowAncestor(panelResidente);
        if (window != null) {
            window.dispose();
        }
    }

    // Método para exportar datos actuales a Excel
    public void exportarAExcel() {
        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(panelResidente,
                    "⚠️ No hay datos para exportar",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            java.io.File archivo = ExcelHandler.seleccionarUbicacionGuardar();
            if (archivo != null) {
                ExcelHandler.exportarAExcel(listaResidentes, archivo);

                JOptionPane.showMessageDialog(panelResidente,
                        "✅ Archivo exportado exitosamente\n" +
                                "📁 Ubicación: " + archivo.getAbsolutePath(),
                        "Exportación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelResidente,
                    "❌ Error al exportar:\n" + e.getMessage(),
                    "Error de exportación",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void setCursor(Cursor cursor) {
        if (panelResidente != null) {
            panelResidente.setCursor(cursor);
        }

        // También aplicar al frame padre si existe
        Window window = SwingUtilities.getWindowAncestor(panelResidente);
        if (window != null) {
            window.setCursor(cursor);
        }
    }

    // Getter para el panel principal
    public JPanel getPanelResidente() {
        return panelResidente;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Sin Look and Feel personalizado - usar el default de Java
            JFrame frame = new JFrame("Vista Residentes - Sistema SIREP");
            VistaResidente vista = new VistaResidente();
            frame.setContentPane(vista.getPanelResidente());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

// Clase DialogoResidente (debe estar en el mismo archivo o crear archivo separado)
class DialogoResidente extends JDialog {
    private JTextField txtNumeroControl;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCarrera;
    private JSpinner spnSemestre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JButton btnAceptar;
    private JButton btnCancelar;

    private boolean confirmado = false;
    private ModeloResidente residente;

    public DialogoResidente(Frame parent) {
        super(parent, "Agregar Residente Manual", true);
        initComponents();
        setupLayout();
        setupEvents();

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        txtNumeroControl = new JTextField(15);
        txtNombre = new JTextField(15);
        txtApellidoPaterno = new JTextField(15);
        txtApellidoMaterno = new JTextField(15);
        txtCarrera = new JTextField(15);
        spnSemestre = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        txtCorreo = new JTextField(15);
        txtTelefono = new JTextField(15);

        btnAceptar = new JButton("✅ Aceptar");
        btnCancelar = new JButton("❌ Cancelar");

        // Configurar el spinner
        ((JSpinner.DefaultEditor) spnSemestre.getEditor()).getTextField().setEditable(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel principal con campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Agregar campos
        agregarCampo(panelCampos, "Número de Control:", txtNumeroControl, gbc, 0);
        agregarCampo(panelCampos, "Nombre:", txtNombre, gbc, 1);
        agregarCampo(panelCampos, "Apellido Paterno:", txtApellidoPaterno, gbc, 2);
        agregarCampo(panelCampos, "Apellido Materno:", txtApellidoMaterno, gbc, 3);
        agregarCampo(panelCampos, "Carrera:", txtCarrera, gbc, 4);
        agregarCampo(panelCampos, "Semestre:", spnSemestre, gbc, 5);
        agregarCampo(panelCampos, "Correo:", txtCorreo, gbc, 6);
        agregarCampo(panelCampos, "Teléfono:", txtTelefono, gbc, 7);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        // Agregar al diálogo
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Padding
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo, GridBagConstraints gbc, int fila) {
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    private void setupEvents() {
        btnAceptar.addActionListener(e -> {
            if (validarCampos()) {
                crearResidente();
                confirmado = true;
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        // Enter para aceptar
        getRootPane().setDefaultButton(btnAceptar);

        // Escape para cancelar
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmado = false;
                dispose();
            }
        });
    }

    private boolean validarCampos() {
        // Validar número de control
        String numeroControlTexto = txtNumeroControl.getText().trim();
        if (numeroControlTexto.isEmpty()) {
            mostrarError("El número de control es obligatorio");
            txtNumeroControl.requestFocus();
            return false;
        }

        try {
            int numeroControl = Integer.parseInt(numeroControlTexto);
            if (numeroControl <= 0) {
                mostrarError("El número de control debe ser un número positivo");
                txtNumeroControl.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El número de control debe ser un número válido");
            txtNumeroControl.requestFocus();
            return false;
        }

        // Validar campos obligatorios
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        }

        if (txtApellidoPaterno.getText().trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio");
            txtApellidoPaterno.requestFocus();
            return false;
        }

        if (txtCarrera.getText().trim().isEmpty()) {
            mostrarError("La carrera es obligatoria");
            txtCarrera.requestFocus();
            return false;
        }

        // Validar correo (básico)
        String correo = txtCorreo.getText().trim();
        if (!correo.isEmpty() && !correo.contains("@")) {
            mostrarError("El formato del correo no es válido");
            txtCorreo.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    private void crearResidente() {
        residente = new ModeloResidente();
        residente.setNumeroControl(Integer.parseInt(txtNumeroControl.getText().trim()));
        residente.setNombre(txtNombre.getText().trim());
        residente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
        residente.setApellidoMaterno(txtApellidoMaterno.getText().trim());
        residente.setCarrera(txtCarrera.getText().trim());
        residente.setSemestre((Integer) spnSemestre.getValue());
        residente.setCorreo(txtCorreo.getText().trim());
        residente.setTelefono(txtTelefono.getText().trim());
        residente.setIdProyecto(1); // Valor por defecto
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public ModeloResidente getResidente() {
        return residente;
    }
}}