package Vista.VistaResidentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Controlador.ControladorAgrManual;

public class AgregarManual extends JDialog {
    private JTextField txtNumeroControl;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCarrera;
    private JTextField txtSemestre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean guardado = false;
    private ControladorAgrManual controlador;
    private JTextField Nombre;
    private JTextField Apellido_Materno;
    private JTextField Carrera;
    private JTextField textField1;
    private JLabel Apellido_Paterno;
    private JTextField Semestre;
    private JTextField textField6;
    private JTextField textField7;
    private JButton guardar;
    private JButton cancelar;

    public AgregarManual(Frame parent) {
        super(parent, "Agregar Residente Manual", true);

        // Inicializar controlador
        controlador = new ControladorAgrManual(this);

        initComponents();
        setupLayout();
        setupEvents();

        setSize(450, 400);
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
        txtSemestre = new JTextField(15);
        txtCorreo = new JTextField(15);
        txtTelefono = new JTextField(15);

        btnGuardar = new JButton("💾 Guardar");
        btnCancelar = new JButton("❌ Cancelar");

        // Establecer colores para mejor apariencia
        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(211, 47, 47));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel principal con campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Título
        JLabel lblTitulo = new JLabel("📝 Registro de Nuevo Residente");
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Agregar campos
        agregarCampo(panelCampos, "* Número de Control:", txtNumeroControl, gbc, 0);
        agregarCampo(panelCampos, "* Nombre:", txtNombre, gbc, 1);
        agregarCampo(panelCampos, "* Apellido Paterno:", txtApellidoPaterno, gbc, 2);
        agregarCampo(panelCampos, "Apellido Materno:", txtApellidoMaterno, gbc, 3);
        agregarCampo(panelCampos, "* Carrera:", txtCarrera, gbc, 4);
        agregarCampo(panelCampos, "* Semestre:", txtSemestre, gbc, 5);
        agregarCampo(panelCampos, "* Correo:", txtCorreo, gbc, 6);
        agregarCampo(panelCampos, "Teléfono:", txtTelefono, gbc, 7);

        // Nota sobre campos obligatorios
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Panel superior con título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        // Panel inferior con nota y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblNota, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar todo al diálogo
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Borde general
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo, GridBagConstraints gbc, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JLabel label = new JLabel(etiqueta);
        if (etiqueta.startsWith("*")) {
            label.setForeground(new Color(211, 47, 47)); // Rojo para obligatorios
        }
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void setupEvents() {
        // SOLO DELEGACIÓN AL CONTROLADOR - NO LÓGICA DE NEGOCIO
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarResidente();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);

        // Escape para cancelar
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    // ==================== MÉTODOS DE INTERFAZ - SOLO DELEGACIÓN ====================

    /**
     * Guardar residente - DELEGA AL CONTROLADOR
     */
    private void guardarResidente() {
        // Obtener valores de los campos
        String numeroControl = txtNumeroControl.getText();
        String nombre = txtNombre.getText();
        String apellidoPaterno = txtApellidoPaterno.getText();
        String apellidoMaterno = txtApellidoMaterno.getText();
        String carrera = txtCarrera.getText();
        String semestre = txtSemestre.getText();
        String correo = txtCorreo.getText();
        String telefono = txtTelefono.getText();

        // DELEGAR AL CONTROLADOR
        boolean resultado = controlador.guardarResidente(
                numeroControl, nombre, apellidoPaterno, apellidoMaterno,
                carrera, semestre, correo, telefono
        );

        if (resultado) {
            guardado = true;
            dispose();
        }
    }

    /**
     * Cancelar - DELEGA AL CONTROLADOR
     */
    private void cancelar() {
        boolean hayCambios = hayCambios();

        // DELEGAR AL CONTROLADOR
        if (controlador.confirmarCancelacion(hayCambios)) {
            guardado = false;
            dispose();
        }
    }

    // ==================== MÉTODOS DE UTILIDAD DE UI ====================

    /**
     * Verificar si hay cambios en los campos
     */
    private boolean hayCambios() {
        return !txtNumeroControl.getText().trim().isEmpty() ||
                !txtNombre.getText().trim().isEmpty() ||
                !txtApellidoPaterno.getText().trim().isEmpty() ||
                !txtApellidoMaterno.getText().trim().isEmpty() ||
                !txtCarrera.getText().trim().isEmpty() ||
                !txtSemestre.getText().trim().isEmpty() ||
                !txtCorreo.getText().trim().isEmpty() ||
                !txtTelefono.getText().trim().isEmpty();
    }

    /**
     * Limpiar todos los campos del formulario
     */
    public void limpiarCampos() {
        txtNumeroControl.setText("");
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtCarrera.setText("");
        txtSemestre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
    }

    /**
     * Establecer foco en el primer campo
     */
    public void establecerFocoPrimero() {
        txtNumeroControl.requestFocus();
    }

    /**
     * Habilitar/deshabilitar campos
     */
    public void habilitarCampos(boolean habilitado) {
        txtNumeroControl.setEnabled(habilitado);
        txtNombre.setEnabled(habilitado);
        txtApellidoPaterno.setEnabled(habilitado);
        txtApellidoMaterno.setEnabled(habilitado);
        txtCarrera.setEnabled(habilitado);
        txtSemestre.setEnabled(habilitado);
        txtCorreo.setEnabled(habilitado);
        txtTelefono.setEnabled(habilitado);
        btnGuardar.setEnabled(habilitado);
    }

    // ==================== GETTERS PARA EL CONTROLADOR ====================

    public String getNumeroControl() {
        return txtNumeroControl.getText();
    }

    public String getNombre() {
        return txtNombre.getText();
    }

    public String getApellidoPaterno() {
        return txtApellidoPaterno.getText();
    }

    public String getApellidoMaterno() {
        return txtApellidoMaterno.getText();
    }

    public String getCarrera() {
        return txtCarrera.getText();
    }

    public String getSemestre() {
        return txtSemestre.getText();
    }

    public String getCorreo() {
        return txtCorreo.getText();
    }

    public String getTelefono() {
        return txtTelefono.getText();
    }

    // ==================== SETTERS PARA PRE-CARGAR DATOS ====================

    public void setNumeroControl(String numeroControl) {
        txtNumeroControl.setText(numeroControl);
    }

    public void setNombre(String nombre) {
        txtNombre.setText(nombre);
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        txtApellidoPaterno.setText(apellidoPaterno);
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        txtApellidoMaterno.setText(apellidoMaterno);
    }

    public void setCarrera(String carrera) {
        txtCarrera.setText(carrera);
    }

    public void setSemestre(String semestre) {
        txtSemestre.setText(semestre);
    }

    public void setCorreo(String correo) {
        txtCorreo.setText(correo);
    }

    public void setTelefono(String telefono) {
        txtTelefono.setText(telefono);
    }

    // ==================== MÉTODOS DE ESTADO ====================

    /**
     * Verificar si el residente fue guardado
     */
    public boolean isGuardado() {
        return guardado;
    }

    /**
     * Establecer estado de guardado
     */
    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }
}