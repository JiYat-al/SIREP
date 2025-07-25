package Vista.VistaResidentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Modelo.ModeloResidente;

public class DialogoResidente extends JDialog {
    private JTextField txtNumeroControl;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCarrera;
    private JSpinner spnSemestre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean confirmado = false;
    private ModeloResidente residente;
    private final Color colorPrincipal;

    // Constructor para agregar nuevo residente
    public DialogoResidente(JFrame parent, Color colorPrincipal) {
        this(parent, colorPrincipal, null);
    }

    // Constructor para editar residente existente
    public DialogoResidente(JFrame parent, Color colorPrincipal, ModeloResidente residente) {
        super(parent, (residente == null ? "Registrar" : "Editar") + " Residente", true);
        this.colorPrincipal = colorPrincipal;

        setSize(600, 680);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        // Panel principal con fondo blanco
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header con título
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint grad = new GradientPaint(
                        0, 0, colorPrincipal,
                        0, getHeight(), colorPrincipal.brighter()
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTituloDialog = new JLabel(residente == null ? "📝 Registrar Nuevo Residente" : "✏️ Editar Residente");
        lblTituloDialog.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloDialog.setForeground(Color.WHITE);
        headerPanel.add(lblTituloDialog, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Crear campos del formulario
        crearCamposFormulario(panelFormulario, gbc);

        mainPanel.add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Botón Guardar
        btnGuardar = new JButton(residente == null ? "💾 Guardar" : "✅ Actualizar") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(60,60,100,60));
                g2.fillRoundRect(4, 6, getWidth()-8, getHeight()-4, 40, 40);
                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorPrincipal.darker() : colorPrincipal,
                        getWidth(), getHeight(), colorPrincipal.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        // Botón Cancelar
        btnCancelar = new JButton("❌ Cancelar") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color colorCancelar = new Color(150, 150, 150);
                // Sombra
                g2.setColor(new Color(60,60,100,60));
                g2.fillRoundRect(4, 6, getWidth()-8, getHeight()-4, 40, 40);
                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorCancelar.darker() : colorCancelar,
                        getWidth(), getHeight(), colorCancelar.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Si es edición, llenar los campos
        if (residente != null) {
            llenarCampos(residente);
        }

        // Configurar eventos
        configurarEventos();

        // Configurar teclas
        configurarTeclas();
    }

    private void crearCamposFormulario(JPanel panel, GridBagConstraints gbc) {
        // Número de Control
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("📋 Número de Control"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNumeroControl = crearCampoTexto();
        panel.add(txtNumeroControl, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("👤 Nombre"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNombre = crearCampoTexto();
        panel.add(txtNombre, gbc);

        // Apellido Paterno
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("👨 Apellido Paterno"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApellidoPaterno = crearCampoTexto();
        panel.add(txtApellidoPaterno, gbc);

        // Apellido Materno
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("👩 Apellido Materno"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApellidoMaterno = crearCampoTexto();
        panel.add(txtApellidoMaterno, gbc);

        // Carrera
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("🎓 Carrera"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCarrera = crearCampoTexto();
        panel.add(txtCarrera, gbc);

        // Semestre
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("📚 Semestre"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        spnSemestre = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spnSemestre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        spnSemestre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        ((JSpinner.DefaultEditor) spnSemestre.getEditor()).getTextField().setEditable(false);
        panel.add(spnSemestre, gbc);

        // Correo
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("📧 Correo Electrónico"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCorreo = crearCampoTexto();
        panel.add(txtCorreo, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("📱 Teléfono"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTelefono = crearCampoTexto();
        panel.add(txtTelefono, gbc);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(colorPrincipal);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Efectos de foco
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorPrincipal.darker(), 3, true),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorPrincipal, 2, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        return campo;
    }

    private void llenarCampos(ModeloResidente residente) {
        txtNumeroControl.setText(String.valueOf(residente.getNumeroControl()));
        txtNombre.setText(residente.getNombre());
        txtApellidoPaterno.setText(residente.getApellidoPaterno());
        txtApellidoMaterno.setText(residente.getApellidoMaterno());
        txtCarrera.setText(residente.getCarrera());
        spnSemestre.setValue(residente.getSemestre());
        txtCorreo.setText(residente.getCorreo());
        txtTelefono.setText(residente.getTelefono());
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(e -> {
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
    }

    private void configurarTeclas() {
        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);

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
        if (txtNumeroControl.getText().trim().isEmpty()) {
            mostrarError("El número de control es obligatorio", txtNumeroControl);
            return false;
        }

        try {
            Integer.parseInt(txtNumeroControl.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("El número de control debe ser numérico", txtNumeroControl);
            return false;
        }

        // Validar nombre
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio", txtNombre);
            return false;
        }

        // Validar apellido paterno
        if (txtApellidoPaterno.getText().trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio", txtApellidoPaterno);
            return false;
        }

        // Validar carrera
        if (txtCarrera.getText().trim().isEmpty()) {
            mostrarError("La carrera es obligatoria", txtCarrera);
            return false;
        }

        // Validar correo (si no está vacío, debe tener formato válido)
        String correo = txtCorreo.getText().trim();
        if (!correo.isEmpty() && !correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarError("El formato del correo electrónico no es válido", txtCorreo);
            return false;
        }

        // Validar teléfono (si no está vacío, debe tener formato válido)
        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !telefono.matches("^[0-9]{10}$")) {
            mostrarError("El teléfono debe tener 10 dígitos", txtTelefono);
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de validación", JOptionPane.ERROR_MESSAGE);
        campo.requestFocus();
        campo.selectAll();
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