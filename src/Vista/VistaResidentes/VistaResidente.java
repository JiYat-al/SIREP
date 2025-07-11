package Vista.VistaResidentes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import Modelo.ModeloResidente;
import Controlador.ControladorResidente;

public class VistaResidente {
    private JPanel panelResidente;
    private JButton importarExelButton;
    private JTable tablaResidentes;
    private JButton cargarExelButton;
    private JButton cancelarButton;
    private JTextField textField1;
    private JButton agregarManualButton;
    private JButton cargarBDButton; // Nuevo botón para cargar desde BD

    // Modelo de la tabla para poder manipularlo después
    private DefaultTableModel modeloTabla;

    // Lista para almacenar los residentes (útil para Excel)
    private List<ModeloResidente> listaResidentes;

    // Controlador
    private ControladorResidente controlador;

    public VistaResidente() {
        // Inicializar la lista si no fue inicializada en createUIComponents
        if (listaResidentes == null) {
            listaResidentes = new ArrayList<>();
        }

        // Inicializar controlador
        controlador = new ControladorResidente(this);

        // Configurar eventos después de que los componentes estén listos
        configurarEventos();
    }

    // ==================== CONFIGURACIÓN DE COMPONENTES ====================

    private void configurarEventos() {
        // Configurar eventos de los botones - SOLO DELEGACIÓN AL CONTROLADOR
        if (importarExelButton != null) {
            importarExelButton.addActionListener(e -> controlador.importarABaseDatos());
        }

        if (cargarExelButton != null) {
            cargarExelButton.addActionListener(e -> controlador.cargarExcelEnTabla());
        }

        if (agregarManualButton != null) {
            agregarManualButton.addActionListener(e -> abrirDialogoAgregarManual());
        }

        if (cancelarButton != null) {
            cancelarButton.addActionListener(e -> cancelar());
        }

        if (cargarBDButton != null) {
            cargarBDButton.addActionListener(e -> controlador.cargarResidentesDesdeBaseDatos());
        }
    }

    // Método llamado automáticamente por IntelliJ GUI Designer
    private void createUIComponents() {
        // Inicializar la lista si no existe (se llama antes del constructor)
        if (listaResidentes == null) {
            listaResidentes = new ArrayList<>();
        }

        // Definir las columnas de la tabla
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

        // Configurar para que se muestren los headers
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

        // Configurar el ancho de las columnas
        tablaResidentes.getColumnModel().getColumn(0).setPreferredWidth(120); // Número de Control
        tablaResidentes.getColumnModel().getColumn(1).setPreferredWidth(100); // Nombre
        tablaResidentes.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellido Paterno
        tablaResidentes.getColumnModel().getColumn(3).setPreferredWidth(120); // Apellido Materno
        tablaResidentes.getColumnModel().getColumn(4).setPreferredWidth(150); // Carrera
        tablaResidentes.getColumnModel().getColumn(5).setPreferredWidth(80);  // Semestre
        tablaResidentes.getColumnModel().getColumn(6).setPreferredWidth(200); // Correo
        tablaResidentes.getColumnModel().getColumn(7).setPreferredWidth(120); // Teléfono
    }

    // ==================== MÉTODOS DE MANIPULACIÓN DE DATOS EN LA TABLA ====================

    /**
     * Agregar un residente a la tabla (SOLO PRESENTACIÓN)
     */
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

    /**
     * Cargar múltiples residentes en la tabla (SOLO PRESENTACIÓN)
     */
    public void cargarResidentes(List<ModeloResidente> residentes) {
        // Limpiar la tabla actual
        limpiarTabla();

        // Agregar todos los residentes
        for (ModeloResidente residente : residentes) {
            agregarResidente(residente);
        }
    }

    /**
     * Limpiar la tabla (SOLO PRESENTACIÓN)
     */
    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
        listaResidentes.clear();
    }

    // ==================== MÉTODOS DE ACCESO A DATOS ====================

    /**
     * Obtener la lista de residentes actual
     */
    public List<ModeloResidente> getListaResidentes() {
        return new ArrayList<>(listaResidentes);
    }

    /**
     * Obtener el residente seleccionado en la tabla
     */
    public ModeloResidente getResidenteSeleccionado() {
        int filaSeleccionada = tablaResidentes.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < listaResidentes.size()) {
            return listaResidentes.get(filaSeleccionada);
        }
        return null;
    }

    // ==================== MÉTODOS DE INTERACCIÓN CON EL USUARIO ====================

    /**
     * Abrir diálogo para agregar residente manual
     */
    private void abrirDialogoAgregarManual() {
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

    /**
     * Cancelar operación o cerrar ventana
     */
    private void cancelar() {
        // Cerrar la ventana o realizar acción de cancelación
        Window window = SwingUtilities.getWindowAncestor(panelResidente);
        if (window != null) {
            window.dispose();
        }
    }

    // ==================== MÉTODOS DE UTILIDAD PARA EL CONTROLADOR ====================

    /**
     * Cambiar cursor (para indicar operaciones en progreso)
     */
    public void setCursor(Cursor cursor) {
        if (panelResidente != null) {
            panelResidente.setCursor(cursor);
        }

        // También aplicar al frame padre si existe
        Window window = SwingUtilities.getWindowAncestor(panelResidente);
        if (window != null) {
            window.setCursor(cursor);
        }
    }

    /**
     * Obtener el panel principal
     */
    public JPanel getPanelResidente() {
        return panelResidente;
    }

    /**
     * Obtener la tabla de residentes
     */
    public JTable getTablaResidentes() {
        return tablaResidentes;
    }

    /**
     * Obtener el modelo de la tabla
     */
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // ==================== MÉTODO MAIN PARA PRUEBAS ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Vista Residentes - Sistema SIREP");
            VistaResidente vista = new VistaResidente();
            frame.setContentPane(vista.getPanelResidente());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

// ==================== CLASE DIÁLOGO PARA AGREGAR RESIDENTE ====================

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

    // ==================== CONFIGURACIÓN DE COMPONENTES ====================

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
            // SOLO VALIDACIÓN BÁSICA DE UI - LA LÓGICA DE NEGOCIO VA AL CONTROLADOR
            if (validarCamposBasicos()) {
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

    // ==================== VALIDACIÓN BÁSICA DE UI ====================

    private boolean validarCamposBasicos() {
        // SOLO VALIDACIONES BÁSICAS DE UI - NO LÓGICA DE NEGOCIO
        if (txtNumeroControl.getText().trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            txtNumeroControl.requestFocus();
            return false;
        }

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

    // ==================== GETTERS ====================

    public boolean isConfirmado() {
        return confirmado;
    }

    public ModeloResidente getResidente() {
        return residente;
    }
}