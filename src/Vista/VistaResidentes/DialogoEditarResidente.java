package Vista.VistaResidentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Modelo.ModeloResidente;

public class DialogoEditarResidente extends JDialog {
    private JTextField txtNumeroControl;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JSpinner spnSemestre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean confirmado = false;
    private ModeloResidente residente;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorExito = new Color(76, 175, 80);
    private final Color colorError = new Color(244, 67, 54);

    // CARRERA FIJA
    private final String CARRERA_FIJA = "Ingeniería en Sistemas Computacionales";

    public DialogoEditarResidente(Frame parent, ModeloResidente residente) {
        super(parent, "Editar Residente", true);
        this.residente = new ModeloResidente(residente); // Copia para evitar modificar el original

        setSize(600, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        configurarInterfaz();
        cargarDatos();
        configurarEventos();
    }

    private void configurarInterfaz() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
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
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitulo = new JLabel("✏️ Editar Datos del Residente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        crearCamposFormulario(panelFormulario, gbc);
        mainPanel.add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotones.setBackground(Color.WHITE);

        crearBotones(panelBotones);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        // Nota sobre carrera
        JPanel panelNota = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNota.setBackground(Color.WHITE);
        JLabel lblNota = new JLabel("🎓 Carrera: " + CARRERA_FIJA + " (Automática)");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNota.setForeground(new Color(120, 120, 120));
        panelNota.add(lblNota);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(Color.WHITE);
        panelSur.add(panelNota, BorderLayout.NORTH);
        panelSur.add(panelBotones, BorderLayout.CENTER);
        mainPanel.add(panelSur, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void crearCamposFormulario(JPanel panel, GridBagConstraints gbc) {
        // Número de Control - AHORA SÍ EDITABLE
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Número de Control"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNumeroControl = crearCampoTexto();
        txtNumeroControl.setEditable(true); // CORREGIDO: SÍ se puede editar
        txtNumeroControl.setToolTipText("Formato: 8 dígitos, no años futuros, no duplicados");
        panel.add(txtNumeroControl, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Nombre"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNombre = crearCampoTexto();
        panel.add(txtNombre, gbc);

        // Apellido Paterno
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Apellido Paterno"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApellidoPaterno = crearCampoTexto();
        panel.add(txtApellidoPaterno, gbc);

        // Apellido Materno
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Apellido Materno"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApellidoMaterno = crearCampoTexto();
        panel.add(txtApellidoMaterno, gbc);

        // CORREGIDO: Semestre con configuración mejorada
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Semestre"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;

        // IMPORTANTE: Crear SpinnerNumberModel con valores correctos
        SpinnerNumberModel semestreModel = new SpinnerNumberModel(9, 9, 15, 1);
        spnSemestre = new JSpinner(semestreModel);
        spnSemestre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spnSemestre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // CORREGIDO: Permitir edición del campo de texto del spinner
        JFormattedTextField semestreTextField = ((JSpinner.DefaultEditor) spnSemestre.getEditor()).getTextField();
        semestreTextField.setEditable(true); // Permitir edición
        semestreTextField.setHorizontalAlignment(JTextField.CENTER);

        // Agregar validación en tiempo real
        semestreTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = semestreTextField.getText();
                try {
                    int valor = Integer.parseInt(texto);
                    if (valor >= 9 && valor <= 15) {
                        semestreTextField.setBackground(Color.WHITE);
                    } else {
                        semestreTextField.setBackground(new Color(255, 235, 235));
                    }
                } catch (NumberFormatException ex) {
                    semestreTextField.setBackground(new Color(255, 235, 235));
                }
            }
        });

        panel.add(spnSemestre, gbc);

        // Correo
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Correo Electrónico"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCorreo = crearCampoTexto();
        panel.add(txtCorreo, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Teléfono"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTelefono = crearCampoTexto();
        panel.add(txtTelefono, gbc);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));

        if (texto.startsWith("*")) {
            label.setForeground(new Color(200, 60, 60));
        } else {
            label.setForeground(colorPrincipal);
        }

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // Efectos de foco
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(33, 150, 243), 3, true),
                        BorderFactory.createEmptyBorder(5, 9, 5, 9)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorPrincipal, 2, true),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
        });

        return campo;
    }

    private void crearBotones(JPanel panelBotones) {
        // Botón Guardar
        btnGuardar = new JButton("💾 Guardar Cambios") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 5, getWidth()-6, getHeight()-3, 20, 20);
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorExito.darker() : colorExito,
                        0, getHeight(), colorExito.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
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
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 5, getWidth()-6, getHeight()-3, 20, 20);
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorError.darker() : colorError,
                        0, getHeight(), colorError.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
    }

    private void cargarDatos() {
        txtNumeroControl.setText(String.valueOf(residente.getNumeroControl()));
        txtNombre.setText(residente.getNombre());
        txtApellidoPaterno.setText(residente.getApellidoPaterno());
        txtApellidoMaterno.setText(residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : "");

        // CORREGIDO: Cargar semestre correctamente
        try {
            int semestre = residente.getSemestre();
            if (semestre >= 9 && semestre <= 15) {
                spnSemestre.setValue(semestre);
            } else {
                spnSemestre.setValue(9); // Valor por defecto si está fuera de rango
            }
            System.out.println("DEBUG: Semestre cargado: " + semestre + " -> Spinner: " + spnSemestre.getValue());
        } catch (Exception e) {
            spnSemestre.setValue(9); // Valor por defecto en caso de error
            System.err.println("ERROR cargando semestre: " + e.getMessage());
        }

        txtCorreo.setText(residente.getCorreo());
        txtTelefono.setText(residente.getTelefono() != null ? residente.getTelefono() : "");
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> cancelar());

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

        // NUEVO: Focus inicial en el nombre (ya que número de control no se puede editar)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                txtNombre.requestFocusInWindow();
            }
        });
    }

    private void guardarCambios() {
        try {
            if (!validarCampos()) {
                return;
            }

            // CORREGIDO: Obtener semestre del spinner correctamente
            int semestreNuevo;
            try {
                semestreNuevo = (Integer) spnSemestre.getValue();
                System.out.println("DEBUG: Semestre a guardar: " + semestreNuevo);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al obtener el valor del semestre",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CORREGIDO: Ahora sí se puede cambiar el número de control
            int numeroControlNuevo = Integer.parseInt(txtNumeroControl.getText().trim().replaceAll("[\\s-]", ""));

            // Actualizar el objeto residente con TODOS los campos
            residente.setNumeroControl(numeroControlNuevo); // CORREGIDO: Ahora se puede cambiar
            residente.setNombre(formatearTexto(txtNombre.getText().trim()));
            residente.setApellidoPaterno(formatearTexto(txtApellidoPaterno.getText().trim()));
            residente.setApellidoMaterno(txtApellidoMaterno.getText().trim().isEmpty() ?
                    null : formatearTexto(txtApellidoMaterno.getText().trim()));
            residente.setCarrera(CARRERA_FIJA);
            residente.setSemestre(semestreNuevo);
            residente.setCorreo(txtCorreo.getText().trim().toLowerCase());
            residente.setTelefono(txtTelefono.getText().trim().isEmpty() ?
                    null : txtTelefono.getText().trim());

            confirmado = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al procesar los datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        // NUEVO: Validar número de control (ahora editable)
        String numeroControlStr = txtNumeroControl.getText().trim();
        if (numeroControlStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El número de control es obligatorio",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtNumeroControl.requestFocus();
            return false;
        }

        try {
            int numeroControlNuevo = Integer.parseInt(numeroControlStr.replaceAll("[\\s-]", ""));

            // Validar formato
            String numStr = String.valueOf(numeroControlNuevo);
            if (numStr.length() != 8) {
                JOptionPane.showMessageDialog(this,
                        "El número de control debe tener exactamente 8 dígitos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                txtNumeroControl.requestFocus();
                return false;
            }

            // Validar años futuros
            int anio = Integer.parseInt(numStr.substring(0, 2));
            int anioActual = java.time.Year.now().getValue() % 100;
            if (anio > anioActual) {
                JOptionPane.showMessageDialog(this,
                        "No se permiten años futuros en el número de control (año " + (2000 + anio) + ")",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                txtNumeroControl.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El número de control debe contener solo números",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtNumeroControl.requestFocus();
            return false;
        }

        // Validar nombre
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty() || nombre.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio y debe tener al menos 2 caracteres",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }

        // Validar apellido paterno
        String apellidoP = txtApellidoPaterno.getText().trim();
        if (apellidoP.isEmpty() || apellidoP.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "El apellido paterno es obligatorio y debe tener al menos 2 caracteres",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtApellidoPaterno.requestFocus();
            return false;
        }

        // Validar semestre
        try {
            int semestre = (Integer) spnSemestre.getValue();
            if (semestre < 9 || semestre > 15) {
                JOptionPane.showMessageDialog(this,
                        "El semestre debe estar entre 9 y 15",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                spnSemestre.requestFocus();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error en el valor del semestre",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            spnSemestre.requestFocus();
            return false;
        }

        // Validar correo
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty() || !correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this,
                    "El correo electrónico no tiene un formato válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtCorreo.requestFocus();
            return false;
        }

        // Validar teléfono (opcional)
        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty()) {
            String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
            if (telefonoLimpio.length() < 8 || telefonoLimpio.length() > 15) {
                JOptionPane.showMessageDialog(this,
                        "El teléfono debe tener entre 8 y 15 dígitos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                txtTelefono.requestFocus();
                return false;
            }
        }

        return true;
    }

    private String formatearTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        String textoLimpio = texto.trim();

        if (!textoLimpio.contains(" ")) {
            return textoLimpio.substring(0, 1).toUpperCase() + textoLimpio.substring(1).toLowerCase();
        }

        String[] palabras = textoLimpio.split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                String palabra = palabras[i].substring(0, 1).toUpperCase() + palabras[i].substring(1).toLowerCase();
                resultado.append(palabra);
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }

        return resultado.toString();
    }

    private void cancelar() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Cancelar la edición? Se perderán los cambios",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            confirmado = false;
            dispose();
        }
    }

    // ==================== GETTERS ====================

    public boolean isConfirmado() {
        return confirmado;
    }

    public ModeloResidente getResidente() {
        return residente;
    }
}