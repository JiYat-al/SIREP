package vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RecuperacionPasswordUI extends JDialog {
    private JTextField txtUsuario;
    private JTextField txtCodigo;
    private JPasswordField txtNuevaPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnSiguiente;
    private JButton btnVerificar;
    private JButton btnCambiar;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private String codigoVerificacion;

    public RecuperacionPasswordUI(Frame parent) {
        super(parent, "Recuperación de Contraseña", true);
        setSize(500, 450); // Ventana más amplia y alta
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
        gbc.insets = new Insets(20, 30, 20, 30); // Más espaciado entre elementos
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Panel de contenido con margen
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Recuperación de Contraseña");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Título más grande
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblInstrucciones = new JLabel("<html><center>Por favor, introduce tu nombre de usuario para enviar un código de verificación a tu correo electrónico.</center></html>");
        lblInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(30, 30, 30, 30); // Más espacio después del título
        gbc.gridy = 1;
        panel.add(lblInstrucciones, gbc);

        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal),
                BorderFactory.createEmptyBorder(8, 12, 8, 12))); // Campo de texto más alto
        gbc.insets = new Insets(10, 30, 20, 30);
        gbc.gridy = 2;
        panel.add(txtUsuario, gbc);

        btnSiguiente = new JButton("Enviar Código");
        estilizarBoton(btnSiguiente);
        gbc.gridy = 3;
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

        JLabel lblTitulo = new JLabel("Verificación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblInstrucciones = new JLabel("<html><center>Se ha enviado un código de verificación a tu correo electrónico.<br>Por favor, revisa tu bandeja de entrada e introduce el código.</center></html>");
        lblInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridy = 1;
        panel.add(lblInstrucciones, gbc);

        txtCodigo = new JTextField(20);
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCodigo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtCodigo.setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto del código
        gbc.insets = new Insets(10, 30, 20, 30);
        gbc.gridy = 2;
        panel.add(txtCodigo, gbc);

        btnVerificar = new JButton("Verificar Código");
        estilizarBoton(btnVerificar);
        gbc.gridy = 3;
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

        JLabel lblTitulo = new JLabel("Nueva Contraseña");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        JLabel lblInstrucciones = new JLabel("<html><center>Por favor, introduce y confirma tu nueva contraseña.<br>La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas y números.</center></html>");
        lblInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridy = 1;
        panel.add(lblInstrucciones, gbc);

        // Panel para la nueva contraseña
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

        // Panel para confirmar contraseña
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
            if (usuario.isEmpty()) {
                mostrarError("Por favor, introduce tu nombre de usuario.");
                return;
            }

            // Aquí iría la lógica para verificar el usuario y enviar el código
            // Por ahora, simulamos el envío
            codigoVerificacion = generarCodigoVerificacion();
            if (enviarCodigoVerificacion(usuario, codigoVerificacion)) {
                mostrarMensaje("Se ha enviado un código de verificación a tu correo electrónico.");
                mostrarPanelCodigo();
            } else {
                mostrarError("No se encontró el usuario especificado.");
            }
        });
    }

    private void configurarEventosCodigo() {
        btnVerificar.addActionListener(e -> {
            String codigo = txtCodigo.getText().trim();
            if (codigo.isEmpty()) {
                mostrarError("Por favor, introduce el código de verificación.");
                return;
            }

            if (codigo.equals(codigoVerificacion)) {
                mostrarPanelPassword();
            } else {
                mostrarError("El código de verificación no es correcto.");
            }
        });
    }

    private void configurarEventosNuevaPassword() {
        btnCambiar.addActionListener(e -> {
            char[] passwordChars = txtNuevaPassword.getPassword();
            char[] confirmPasswordChars = txtConfirmarPassword.getPassword();
            String password = new String(passwordChars);
            String confirmPassword = new String(confirmPasswordChars);

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!validarPassword(password)) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas y números",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aquí iría la lógica para cambiar la contraseña
            cambiarPassword(txtUsuario.getText(), password);
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

    private String generarCodigoVerificacion() {
        // Genera un código aleatorio de 6 dígitos
        return String.format("%06d", (int)(Math.random() * 1000000));
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

    // Estos métodos deberían conectarse con el controlador y el modelo
    private boolean enviarCodigoVerificacion(String usuario, String codigo) {
        // TODO: Implementar la lógica real para enviar el código por correo
        // Por ahora retornamos true para simular que funcionó
        return true;
    }

    private boolean cambiarPassword(String usuario, String nuevaPassword) {
        // TODO: Implementar la lógica real para cambiar la contraseña
        // Por ahora retornamos true para simular que funcionó
        return true;
    }
}
