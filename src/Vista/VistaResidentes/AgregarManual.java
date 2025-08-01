package Vista.VistaResidentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Controlador.ControladorAgrManual;

public class AgregarManual extends JDialog {
    private JTextField txtNumeroControl;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JSpinner spnSemestre; // MODIFICADO: Rango 9-15
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnAyuda;
    private JButton btnLimpiar;

    private boolean guardado = false;
    private ControladorAgrManual controlador;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorExito = new Color(76, 175, 80);
    private final Color colorError = new Color(244, 67, 54);
    private final Color colorAdvertencia = new Color(255, 152, 0);

    // CARRERA FIJA PARA TODOS LOS RESIDENTES
    private final String CARRERA_FIJA = "Ingeniería en Sistemas Computacionales";

    public AgregarManual(Frame parent) {
        super(parent, "Agregar Residente Manual", true);

        // Inicializar controlador
        controlador = new ControladorAgrManual(this);

        setSize(650, 650);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        configurarInterfaz();
        configurarEventos();
        configurarValidacionTiempoReal();
    }

    private void configurarInterfaz() {
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

        JLabel lblTituloDialog = new JLabel(" Registro de Nuevo Residente");
        lblTituloDialog.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloDialog.setForeground(Color.WHITE);
        headerPanel.add(lblTituloDialog, BorderLayout.CENTER);

        // Botón ayuda en el header
        btnAyuda = new JButton("❓");
        btnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnAyuda.setBackground(Color.WHITE);
        btnAyuda.setForeground(colorPrincipal);
        btnAyuda.setFocusPainted(false);
        btnAyuda.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btnAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAyuda.setToolTipText("Ayuda sobre el formato del número de control");
        headerPanel.add(btnAyuda, BorderLayout.EAST);

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
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        crearBotones(panelBotones);

        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        // Nota sobre campos obligatorios y carrera
        JPanel panelNota = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNota.setBackground(Color.WHITE);
        JLabel lblNota = new JLabel(" * Campos obligatorios - 🎓 Carrera: " + CARRERA_FIJA + " -  Semestre: 9-15");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNota.setForeground(new Color(150, 150, 150));
        panelNota.add(lblNota);
        panelNota.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // Panel inferior que contiene nota y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);
        panelInferior.add(panelNota, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);

        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void crearCamposFormulario(JPanel panel, GridBagConstraints gbc) {
        // *** FIX: Número de Control - AHORA ACEPTA VARCHAR(9) ***
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Número de Control"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNumeroControl = crearCampoTexto();
        txtNumeroControl.setToolTipText("Formato: máximo 9 caracteres - Ej: 22161063");
        panel.add(txtNumeroControl, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Nombre"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNombre = crearCampoTexto();
        txtNombre.setToolTipText("Solo letras, espacios y acentos. Mínimo 2 caracteres");
        panel.add(txtNombre, gbc);

        // Apellido Paterno
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Apellido Paterno"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApellidoPaterno = crearCampoTexto();
        txtApellidoPaterno.setToolTipText("Solo letras, espacios y acentos. Mínimo 2 caracteres");
        panel.add(txtApellidoPaterno, gbc);

        // Apellido Materno
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Apellido Materno"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtApellidoMaterno = crearCampoTexto();
        txtApellidoMaterno.setToolTipText("Opcional. Solo letras, espacios y acentos");
        panel.add(txtApellidoMaterno, gbc);

        // Semestre - MODIFICADO: Rango 9-15
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Semestre"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        spnSemestre = new JSpinner(new SpinnerNumberModel(9, 9, 15, 1)); // Rango 9-15
        spnSemestre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        spnSemestre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        spnSemestre.setToolTipText("Semestre actual del estudiante (9-15)");
        ((JSpinner.DefaultEditor) spnSemestre.getEditor()).getTextField().setEditable(false);
        panel.add(spnSemestre, gbc);

        // Correo
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("* Correo Electrónico"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCorreo = crearCampoTexto();
        txtCorreo.setToolTipText("Formato: usuario@dominio.com - Ej: juan.perez@tecnm.mx");
        panel.add(txtCorreo, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("Teléfono"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTelefono = crearCampoTexto();
        txtTelefono.setToolTipText("Opcional. 8-15 dígitos - Ej: 4421234567 o (442) 123-4567");
        panel.add(txtTelefono, gbc);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));

        if (texto.startsWith("*")) {
            label.setForeground(new Color(200, 60, 60)); // Rojo para campos obligatorios
        } else {
            label.setForeground(colorPrincipal);
        }

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

        // Efectos de foco mejorados
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(33, 150, 243), 3, true),
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

    private void crearBotones(JPanel panelBotones) {
        // Botón Limpiar
        btnLimpiar = new JButton(" Limpiar") {
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
                Color colorLimpiar = new Color(158, 158, 158);
                // Sombra
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 5, getWidth()-6, getHeight()-3, 25, 25);
                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorLimpiar.darker() : colorLimpiar,
                        0, getHeight(), colorLimpiar.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        // Botón Guardar - MEJORADO
        btnGuardar = new JButton(" Guardar") {
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
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 5, getWidth()-6, getHeight()-3, 25, 25);
                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorExito.darker() : colorExito,
                        0, getHeight(), colorExito.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        // Botón Cancelar
        btnCancelar = new JButton(" Cancelar") {
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
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 5, getWidth()-6, getHeight()-3, 25, 25);
                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorError.darker() : colorError,
                        0, getHeight(), colorError.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(e -> guardarResidenteDirectoABD());
        btnCancelar.addActionListener(e -> cancelar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnAyuda.addActionListener(e -> mostrarAyuda()); // *** FIX: Agregar evento de ayuda ***

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

        // Establecer foco inicial
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                txtNumeroControl.requestFocusInWindow();
            }
        });
    }

    private void configurarValidacionTiempoReal() {
        // *** FIX: Validación del número de control para VARCHAR(9) ***
        txtNumeroControl.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtNumeroControl.getText().trim();
                if (!texto.isEmpty()) {
                    validarNumeroControlVisual(texto);
                } else {
                    restaurarBordeNormal(txtNumeroControl);
                }
            }
        });

        // Validación de nombres en tiempo real
        KeyAdapter validadorNombres = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField campo = (JTextField) e.getSource();
                String texto = campo.getText().trim();
                if (!texto.isEmpty()) {
                    validarNombreVisual(campo, texto);
                } else {
                    restaurarBordeNormal(campo);
                }
            }
        };

        txtNombre.addKeyListener(validadorNombres);
        txtApellidoPaterno.addKeyListener(validadorNombres);
        txtApellidoMaterno.addKeyListener(validadorNombres);

        // Validación de correo en tiempo real
        txtCorreo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtCorreo.getText().trim();
                if (!texto.isEmpty()) {
                    validarCorreoVisual(texto);
                } else {
                    restaurarBordeNormal(txtCorreo);
                }
            }
        });

        // Validación de teléfono en tiempo real
        txtTelefono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtTelefono.getText().trim();
                if (!texto.isEmpty()) {
                    validarTelefonoVisual(texto);
                } else {
                    restaurarBordeNormal(txtTelefono);
                }
            }
        });
    }

    // ==================== MÉTODOS DE VALIDACIÓN VISUAL ====================

    // *** FIX: Validación actualizada para VARCHAR(9) ***
    private void validarNumeroControlVisual(String numeroControl) {
        String numLimpio = numeroControl.replaceAll("[\\s-]", "");

        // Validar longitud máxima de 9 caracteres
        if (numLimpio.length() > 9) {
            mostrarBordeError(txtNumeroControl);
            return;
        }

        // Validar que solo contenga números
        if (!numLimpio.matches("\\d+")) {
            mostrarBordeError(txtNumeroControl);
            return;
        }

        // Si tiene 8 dígitos, aplicar validación tradicional
        if (numLimpio.length() == 8) {
            // Validar año
            int anio = Integer.parseInt(numLimpio.substring(0, 2));
            int anioActual = java.time.Year.now().getValue() % 100;
            int anioMinimo = (anioActual - 20 + 100) % 100;
            int anioMaximo = (anioActual + 2) % 100;

            boolean anioValido = false;
            if (anioMinimo <= anioMaximo) {
                anioValido = (anio >= anioMinimo && anio <= anioMaximo);
            } else {
                anioValido = (anio >= anioMinimo || anio <= anioMaximo);
            }

            if (anioValido && !numLimpio.substring(2).startsWith("00")) {
                mostrarBordeExito(txtNumeroControl);
            } else {
                mostrarBordeAdvertencia(txtNumeroControl);
            }
        } else if (numLimpio.length() >= 6 && numLimpio.length() <= 9) {
            // Para números de 6-9 dígitos, mostrar como válido
            mostrarBordeExito(txtNumeroControl);
        } else {
            // Muy corto o muy largo
            mostrarBordeError(txtNumeroControl);
        }
    }

    private void validarNombreVisual(JTextField campo, String texto) {
        if (texto.length() >= 2 && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarBordeExito(campo);
        } else {
            mostrarBordeError(campo);
        }
    }

    private void validarCorreoVisual(String correo) {
        if (correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") &&
                !correo.contains("..") && correo.length() <= 254) {
            mostrarBordeExito(txtCorreo);
        } else {
            mostrarBordeError(txtCorreo);
        }
    }

    private void validarTelefonoVisual(String telefono) {
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
        if (telefonoLimpio.length() >= 8 && telefonoLimpio.length() <= 15 &&
                !telefonoLimpio.matches("(\\d)\\1{7,}")) {
            mostrarBordeExito(txtTelefono);
        } else {
            mostrarBordeError(txtTelefono);
        }
    }

    // ==================== MÉTODOS DE RETROALIMENTACIÓN VISUAL ====================

    private void mostrarBordeExito(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorExito, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    private void mostrarBordeError(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorError, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    private void mostrarBordeAdvertencia(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAdvertencia, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    private void restaurarBordeNormal(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    // ==================== MÉTODOS DE INTERFAZ - GUARDADO DIRECTO A BD ====================

    private void guardarResidenteDirectoABD() {
        // Restaurar bordes normales antes de validar
        restaurarTodosLosBordes();

        // Obtener valores de los campos
        String numeroControl = txtNumeroControl.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidoPaterno = txtApellidoPaterno.getText().trim();
        String apellidoMaterno = txtApellidoMaterno.getText().trim();
        String carrera = CARRERA_FIJA; // CARRERA AUTOMÁTICA
        String semestre = String.valueOf(spnSemestre.getValue());
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();

        // LLAMAR AL CONTROLADOR PARA GUARDAR DIRECTAMENTE EN BD
        boolean resultado = controlador.guardarResidenteDirectoEnBD(
                numeroControl, nombre, apellidoPaterno, apellidoMaterno,
                carrera, semestre, correo, telefono
        );

        if (resultado) {
            guardado = true;
            dispose();
        }
    }

    private void cancelar() {
        boolean hayCambios = hayCambios();

        // DELEGAR AL CONTROLADOR
        if (controlador.confirmarCancelacion(hayCambios)) {
            guardado = false;
            dispose();
        }
    }

    private void limpiarFormulario() {
        if (hayCambios()) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de limpiar todos los campos?",
                    "Confirmar Limpieza",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                limpiarCampos();
                restaurarTodosLosBordes();
                txtNumeroControl.requestFocus();
                JOptionPane.showMessageDialog(this,
                        "Formulario limpiado correctamente",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "El formulario ya está vacío",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // *** FIX: Agregar método mostrarAyuda ***
    private void mostrarAyuda() {
        String ayuda = "📋 Formato del Número de Control\n\n" +
                "🔢 Estructura actualizada:\n" +
                "• Máximo 9 caracteres\n" +
                "• Solo números\n" +
                "• Ejemplos válidos:\n" +
                "  - 22161063 (8 dígitos tradicional)\n" +
                "  - 221610631 (9 dígitos)\n" +
                "  - 2216106 (7 dígitos)\n\n" +
                "📝 Otros campos:\n" +
                "• Semestre: 9-15 (residencia)\n" +
                "• Carrera: Automática\n" +
                "• Teléfono: Opcional, 8-15 dígitos\n" +
                "• Correo: Formato estándar\n\n" +
                "⚠️ El sistema valida duplicados automáticamente";

        JOptionPane.showMessageDialog(this, ayuda,
                "Ayuda - Formato de Datos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restaurarTodosLosBordes() {
        restaurarBordeNormal(txtNumeroControl);
        restaurarBordeNormal(txtNombre);
        restaurarBordeNormal(txtApellidoPaterno);
        restaurarBordeNormal(txtApellidoMaterno);
        restaurarBordeNormal(txtCorreo);
        restaurarBordeNormal(txtTelefono);
    }

    // ==================== MÉTODOS DE UTILIDAD DE UI ====================

    private boolean hayCambios() {
        return !txtNumeroControl.getText().trim().isEmpty() ||
                !txtNombre.getText().trim().isEmpty() ||
                !txtApellidoPaterno.getText().trim().isEmpty() ||
                !txtApellidoMaterno.getText().trim().isEmpty() ||
                !((Integer) spnSemestre.getValue()).equals(9) ||
                !txtCorreo.getText().trim().isEmpty() ||
                !txtTelefono.getText().trim().isEmpty();
    }

    public void limpiarCampos() {
        txtNumeroControl.setText("");
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        spnSemestre.setValue(9); // Valor mínimo
        txtCorreo.setText("");
        txtTelefono.setText("");
    }

    // ==================== GETTERS PARA EL CONTROLADOR ====================

    public String getNumeroControl() {
        return txtNumeroControl.getText().trim();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getApellidoPaterno() {
        return txtApellidoPaterno.getText().trim();
    }

    public String getApellidoMaterno() {
        return txtApellidoMaterno.getText().trim();
    }

    public String getCarrera() {
        return CARRERA_FIJA; // RETORNA CARRERA FIJA
    }

    public String getSemestre() {
        return String.valueOf(spnSemestre.getValue());
    }

    public String getCorreo() {
        return txtCorreo.getText().trim();
    }

    public String getTelefono() {
        return txtTelefono.getText().trim();
    }

    // ==================== MÉTODOS DE ESTADO ====================

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }
}