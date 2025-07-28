package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Modelo.UsuarioDAO;

public class RecuperacionPasswordUI extends JDialog {
    private JTextField txtUsuario;
    private JTextField txtEmail;
    private JTextField txtCodigo;
    private JPasswordField txtNuevaPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnSiguiente;
    private JButton btnVerificar;
    private JButton btnCambiar;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private UsuarioDAO usuarioDAO;

    public RecuperacionPasswordUI(Frame parent) {
        super(parent, "Recuperación de Contraseña - SIREP", true);
        this.usuarioDAO = new UsuarioDAO();

        setSize(520, 500); // Aumenté el ancho y alto
        setLocationRelativeTo(parent);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        // Panel para solicitar usuario
        JPanel panelUsuario = crearPanelUsuario();
        cardPanel.add(panelUsuario, "usuario");

        // Panel para código de verificación
        JPanel panelCodigo = crearPanelCodigo();
        cardPanel.add(panelCodigo, "codigo");

        // Panel para nueva contraseña
        JPanel panelNuevaPassword = crearPanelNuevaPassword();
        cardPanel.add(panelNuevaPassword, "password");

        add(cardPanel);
        mostrarPanelUsuario();
    }

    private JPanel crearPanelUsuario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel lblTitulo = new JLabel("SIREP - Recuperación de Contraseña");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Reducí un poco el tamaño
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Instituto Tecnológico de Oaxaca - ITO");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(colorPrincipal.darker());
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 30, 20, 30);
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);

        JLabel lblInstrucciones = new JLabel("<html><center>Por favor, introduce tu nombre de usuario y correo electrónico<br>para verificar tu identidad y enviar el código de verificación.</center></html>");
        lblInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(10, 30, 20, 30); // Reducí el espaciado
        gbc.gridy = 2;
        panel.add(lblInstrucciones, gbc);

        // Campo Usuario
        JLabel lblUsuario = new JLabel("Nombre de usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(5, 30, 5, 30);
        gbc.gridy = 3;
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        gbc.gridy = 4;
        panel.add(txtUsuario, gbc);

        // Campo Email
        JLabel lblEmail = new JLabel("Correo electrónico:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(15, 30, 5, 30);
        gbc.gridy = 5;
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        gbc.insets = new Insets(5, 30, 20, 30);
        gbc.gridy = 6;
        panel.add(txtEmail, gbc);

        btnSiguiente = new JButton("Enviar Código");
        estilizarBoton(btnSiguiente);
        gbc.gridy = 7;
        panel.add(btnSiguiente, gbc);

        configurarEventosUsuario();
        return panel;
    }

    private JPanel crearPanelCodigo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel lblTitulo = new JLabel("SIREP - Verificación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Instituto Tecnológico de Oaxaca - ITO");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(colorPrincipal.darker());
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 30, 20, 30);
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);

        JLabel lblInstrucciones = new JLabel("<html><center>Se ha enviado un código de verificación a tu correo electrónico.<br>Por favor, revisa tu bandeja de entrada e introduce el código.</center></html>");
        lblInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(10, 30, 20, 30);
        gbc.gridy = 2;
        panel.add(lblInstrucciones, gbc);

        txtCodigo = new JTextField(20);
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCodigo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtCodigo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(10, 30, 20, 30);
        gbc.gridy = 3;
        panel.add(txtCodigo, gbc);

        btnVerificar = new JButton("Verificar Código");
        estilizarBoton(btnVerificar);
        gbc.gridy = 4;
        panel.add(btnVerificar, gbc);

        configurarEventosCodigo();
        return panel;
    }

    private JPanel crearPanelNuevaPassword() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel lblTitulo = new JLabel("SIREP - Nueva Contraseña");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Instituto Tecnológico de Oaxaca - ITO");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(colorPrincipal.darker());
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 30, 20, 30);
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);

        JLabel lblInstrucciones = new JLabel("<html><center>Por favor, introduce y confirma tu nueva contraseña.<br>La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas y números.</center></html>");
        lblInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(10, 30, 20, 30);
        gbc.gridy = 2;
        panel.add(lblInstrucciones, gbc);

        JLabel lblNuevaPassword = new JLabel("Nueva contraseña:");
        lblNuevaPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(5, 30, 5, 30);
        gbc.gridy = 2;
        panel.add(lblNuevaPassword, gbc);

        txtNuevaPassword = new JPasswordField(20);
        txtNuevaPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNuevaPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        gbc.gridy = 3;
        panel.add(txtNuevaPassword, gbc);

        JLabel lblConfirmarPassword = new JLabel("Confirmar contraseña:");
        lblConfirmarPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(15, 30, 5, 30);
        gbc.gridy = 4;
        panel.add(lblConfirmarPassword, gbc);

        txtConfirmarPassword = new JPasswordField(20);
        txtConfirmarPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirmarPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        gbc.insets = new Insets(5, 30, 20, 30);
        gbc.gridy = 5;
        panel.add(txtConfirmarPassword, gbc);

        btnCambiar = new JButton("Cambiar Contraseña");
        estilizarBoton(btnCambiar);
        gbc.gridy = 6;
        panel.add(btnCambiar, gbc);

        configurarEventosNuevaPassword();
        return panel;
    }

    private void estilizarBoton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorPrincipal);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorPrincipal.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorPrincipal);
            }
        });
    }

    private void configurarEventosUsuario() {
        btnSiguiente.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String email = txtEmail.getText().trim();

            if (usuario.isEmpty()) {
                mostrarError("Por favor, introduce tu nombre de usuario.");
                return;
            }

            if (email.isEmpty()) {
                mostrarError("Por favor, introduce tu correo electrónico.");
                return;
            }

            // Validar formato de email básico
            if (!email.contains("@") || !email.contains(".")) {
                mostrarError("Por favor, introduce un correo electrónico válido.");
                return;
            }

            // Usar UsuarioDAO para verificar usuario y email, luego generar código
            String codigo = usuarioDAO.generarCodigoVerificacion(usuario, email);
            if (codigo != null) {
                mostrarMensaje("Se ha enviado un código de verificación a: " + email + "\n" +
                        "Por favor, revisa tu bandeja de entrada.");
                mostrarPanelCodigo();
            } else {
                mostrarError("El usuario y/o correo electrónico no coinciden con nuestros registros.\n" +
                        "Por favor, verifica la información ingresada.");
            }
        });
    }

    private void configurarEventosCodigo() {
        btnVerificar.addActionListener(e -> {
            String codigo = txtCodigo.getText().trim();
            String usuario = txtUsuario.getText().trim();

            if (codigo.isEmpty()) {
                mostrarError("Por favor, introduce el código de verificación.");
                return;
            }

            if (usuarioDAO.verificarCodigo(usuario, codigo)) {
                mostrarPanelPassword();
            } else {
                mostrarError("El código de verificación no es correcto o ha expirado.");
            }
        });
    }

    private void configurarEventosNuevaPassword() {
        btnCambiar.addActionListener(e -> {
            char[] passwordChars = txtNuevaPassword.getPassword();
            char[] confirmPasswordChars = txtConfirmarPassword.getPassword();
            String password = new String(passwordChars);
            String confirmPassword = new String(confirmPasswordChars);

            // Limpiar arrays por seguridad
            java.util.Arrays.fill(passwordChars, ' ');
            java.util.Arrays.fill(confirmPasswordChars, ' ');

            if (!password.equals(confirmPassword)) {
                mostrarError("Las contraseñas no coinciden");
                return;
            }

            if (!validarPassword(password)) {
                mostrarError("La contraseña debe tener al menos 8 caracteres,\n" +
                        "incluir mayúsculas, minúsculas y números");
                return;
            }

            // Usar UsuarioDAO para actualizar la contraseña
            if (usuarioDAO.actualizarPassword(txtUsuario.getText(), password)) {
                mostrarMensaje("Contraseña cambiada exitosamente.");
                dispose();
            } else {
                mostrarError("Error al cambiar la contraseña. Inténtalo nuevamente.");
            }
        });
    }

    private void mostrarPanelUsuario() {
        cardLayout.show(cardPanel, "usuario");
    }

    private void mostrarPanelCodigo() {
        cardLayout.show(cardPanel, "codigo");
    }

    private void mostrarPanelPassword() {
        cardLayout.show(cardPanel, "password");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean validarPassword(String password) {
        // Validar que la contraseña tenga al menos 8 caracteres
        if (password.length() < 8) return false;

        // Validar que contenga al menos una mayúscula
        if (!password.matches(".*[A-Z].*")) return false;

        // Validar que contenga al menos una minúscula
        if (!password.matches(".*[a-z].*")) return false;

        // Validar que contenga al menos un número
        if (!password.matches(".*\\d.*")) return false;

        return true;
    }
}