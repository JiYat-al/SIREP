package Vista;

import Modelo.ModeloResidente;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DialogoEdicionResidente extends JDialog {
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

    private boolean guardado = false;
    private ModeloResidente residente;

    public boolean isGuardado() {
        return guardado;
    }

    public DialogoEdicionResidente(Frame parent, ModeloResidente residente) {
        super(parent, "Editar Residente", true);
        this.residente = residente;

        initComponents();
        cargarDatos();
        setupLayout();
        setupEvents();

        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(true);
        setMinimumSize(new Dimension(450, 400));
    }

    private void initComponents() {
        txtNumeroControl = new JTextField(15);
        txtNumeroControl.setEditable(true);
        txtNumeroControl.setEnabled(true);
        txtNumeroControl.setBackground(Color.WHITE);
        txtNumeroControl.setForeground(Color.BLACK);

        txtNombre = new JTextField(15);
        txtApellidoPaterno = new JTextField(15);
        txtApellidoMaterno = new JTextField(15);
        txtCarrera = new JTextField(15);
        spnSemestre = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        txtCorreo = new JTextField(15);
        txtTelefono = new JTextField(15);

        btnGuardar = new JButton("Guardar Cambios");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(211, 47, 47));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
    }

    private void cargarDatos() {
        txtNumeroControl.setText(String.valueOf(residente.getNumeroControl()));
        txtNombre.setText(residente.getNombre());
        txtApellidoPaterno.setText(residente.getApellidoPaterno());
        txtApellidoMaterno.setText(residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : "");
        txtCarrera.setText(residente.getCarrera());
        spnSemestre.setValue(residente.getSemestre());
        txtCorreo.setText(residente.getCorreo());
        txtTelefono.setText(residente.getTelefono() != null ? residente.getTelefono() : "");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitulo = new JLabel("Editar Información del Residente");
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        agregarCampo(panelCampos, "* Número de Control:", txtNumeroControl, gbc, 0);
        agregarCampo(panelCampos, "* Nombre:", txtNombre, gbc, 1);
        agregarCampo(panelCampos, "* Apellido Paterno:", txtApellidoPaterno, gbc, 2);
        agregarCampo(panelCampos, "Apellido Materno:", txtApellidoMaterno, gbc, 3);
        agregarCampo(panelCampos, "* Carrera:", txtCarrera, gbc, 4);
        agregarCampo(panelCampos, "* Semestre:", spnSemestre, gbc, 5);
        agregarCampo(panelCampos, "* Correo:", txtCorreo, gbc, 6);
        agregarCampo(panelCampos, "Teléfono:", txtTelefono, gbc, 7);

        JScrollPane scrollCampos = new JScrollPane(panelCampos);
        scrollCampos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCampos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCampos.setBorder(BorderFactory.createEmptyBorder());

        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblNota, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollCampos, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo, GridBagConstraints gbc, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JLabel label = new JLabel(etiqueta);
        if (etiqueta.startsWith("*")) {
            label.setForeground(new Color(211, 47, 47));
        }
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void setupEvents() {
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> cancelar());

        txtNumeroControl.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                txtNumeroControl.selectAll();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
            }
        });

        getRootPane().setDefaultButton(btnGuardar);

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    private void guardarCambios() {
        if (!validarCampos()) {
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de guardar los cambios?\n" +
                        "Nombre: " + txtNombre.getText().trim() + " " + txtApellidoPaterno.getText().trim(),
                "Confirmar cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                residente.setNumeroControl(Integer.parseInt(txtNumeroControl.getText().trim()));
                residente.setNombre(txtNombre.getText().trim());
                residente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
                residente.setApellidoMaterno(txtApellidoMaterno.getText().trim().isEmpty() ? null : txtApellidoMaterno.getText().trim());
                residente.setCarrera(txtCarrera.getText().trim());
                residente.setSemestre((Integer) spnSemestre.getValue());
                residente.setCorreo(txtCorreo.getText().trim());
                residente.setTelefono(txtTelefono.getText().trim().isEmpty() ? null : txtTelefono.getText().trim());

                boolean actualizado = residente.actualizar();

                if (actualizado) {
                    guardado = true;
                    JOptionPane.showMessageDialog(this,
                            "Cambios guardados exitosamente\n" +
                                    "Número de Control: " + residente.getNumeroControl(),
                            "Actualización exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudieron guardar los cambios\n" +
                                    "Verifique que el registro aún existe en la base de datos",
                            "Error de actualización",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar los cambios:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("Error al actualizar residente: " + e.getMessage());
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    private boolean validarCampos() {
        String numeroControlTexto = txtNumeroControl.getText().trim();
        if (numeroControlTexto.isEmpty()) {
            mostrarError("El número de control es obligatorio", txtNumeroControl);
            return false;
        }

        try {
            int numeroControl = Integer.parseInt(numeroControlTexto);
            if (numeroControl <= 0) {
                mostrarError("El número de control debe ser un número positivo", txtNumeroControl);
                return false;
            }
            if (numeroControlTexto.length() != 8) {
                mostrarError("El número de control debe tener exactamente 8 dígitos", txtNumeroControl);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El número de control debe ser un número válido", txtNumeroControl);
            return false;
        }

        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio", txtNombre);
            return false;
        }

        if (txtNombre.getText().trim().length() < 2) {
            mostrarError("El nombre debe tener al menos 2 caracteres", txtNombre);
            return false;
        }

        if (txtApellidoPaterno.getText().trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio", txtApellidoPaterno);
            return false;
        }

        if (txtApellidoPaterno.getText().trim().length() < 2) {
            mostrarError("El apellido paterno debe tener al menos 2 caracteres", txtApellidoPaterno);
            return false;
        }

        if (txtCarrera.getText().trim().isEmpty()) {
            mostrarError("La carrera es obligatoria", txtCarrera);
            return false;
        }

        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            mostrarError("El correo es obligatorio", txtCorreo);
            return false;
        }

        if (!validarFormatoCorreo(correo)) {
            mostrarError("El formato del correo no es válido\nEjemplo: usuario@dominio.com", txtCorreo);
            return false;
        }

        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !validarFormatoTelefono(telefono)) {
            mostrarError("El formato del teléfono no es válido\nDebe contener solo números y tener entre 8 y 15 dígitos", txtTelefono);
            return false;
        }

        return true;
    }

    private boolean validarFormatoCorreo(String correo) {
        return correo.contains("@") &&
                correo.contains(".") &&
                correo.indexOf("@") > 0 &&
                correo.indexOf("@") < correo.lastIndexOf(".") &&
                correo.lastIndexOf(".") < correo.length() - 1;
    }

    private boolean validarFormatoTelefono(String telefono) {
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
        return telefonoLimpio.length() >= 8 && telefonoLimpio.length() <= 15;
    }

    private void mostrarError(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
        campo.requestFocus();
        campo.selectAll();
    }

    private void cancelar() {
        if (hayCambios()) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Hay cambios sin guardar. ¿Desea cancelar la edición?",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (opcion == JOptionPane.NO_OPTION) {
                return;
            }
        }
        dispose();
    }

    private boolean hayCambios() {
        if (!txtNumeroControl.getText().trim().equals(String.valueOf(residente.getNumeroControl()))) return true;
        if (!txtNombre.getText().trim().equals(residente.getNombre())) return true;
        if (!txtApellidoPaterno.getText().trim().equals(residente.getApellidoPaterno())) return true;
        String originalApellidoMaterno = residente.getApellidoMaterno() == null ? "" : residente.getApellidoMaterno();
        if (!txtApellidoMaterno.getText().trim().equals(originalApellidoMaterno)) return true;
        if (!txtCarrera.getText().trim().equals(residente.getCarrera())) return true;
        if (!spnSemestre.getValue().equals(residente.getSemestre())) return true;
        if (!txtCorreo.getText().trim().equals(residente.getCorreo())) return true;
        String originalTelefono = residente.getTelefono() == null ? "" : residente.getTelefono();
        if (!txtTelefono.getText().trim().equals(originalTelefono)) return true;
        return false;
    }
}