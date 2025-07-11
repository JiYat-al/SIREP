package Vista;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;

public class RegistroEmpresaUI extends JFrame {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);

    public RegistroEmpresaUI() {
        setTitle("Registro de Empresa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setMinimumSize(new Dimension(1000, 600));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(colorFondo);

        // Banner morado
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(colorPrincipal);
        banner.setPreferredSize(new Dimension(0, 110));
        JLabel lblTitulo = new JLabel("Registro de Empresa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(30, 48, 18, 0));
        banner.add(lblTitulo, BorderLayout.WEST);
        mainPanel.add(banner, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(38, 120, 38, 120));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(20, 6, 6, 16);

        // Nombre
        gbc.gridy = 0;
        panelCentral.add(labelField("Nombre de la empresa", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtNombre = new JTextField(32);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setPreferredSize(new Dimension(520, 38));
        txtNombre.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtNombre, gbc);

        // Dirección
        gbc.gridy++;
        panelCentral.add(labelField("Dirección", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtDireccion = new JTextField(36);
        txtDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDireccion.setPreferredSize(new Dimension(520, 38));
        txtDireccion.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtDireccion, gbc);

        // Responsable
        gbc.gridy++;
        panelCentral.add(labelField("Responsable", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtResponsable = new JTextField(32);
        txtResponsable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtResponsable.setPreferredSize(new Dimension(350, 38));
        txtResponsable.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtResponsable, gbc);

        // Teléfono
        gbc.gridy++;
        panelCentral.add(labelField("Teléfono", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtTelefono = new JTextField(15);
        txtTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtTelefono.setPreferredSize(new Dimension(220, 38));
        txtTelefono.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtTelefono, gbc);

        // Correo electrónico
        gbc.gridy++;
        panelCentral.add(labelField("Correo electrónico", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtCorreo = new JTextField(32);
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtCorreo.setPreferredSize(new Dimension(350, 38));
        txtCorreo.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtCorreo, gbc);

        // Botón registrar
        gbc.gridy++;
        gbc.insets = new Insets(38, 4, 4, 4);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        JButton btnRegistrar = new JButton("Registrar empresa");
        btnRegistrar.setBackground(colorPrincipal);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(14, 44, 14, 44));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true);
        panelCentral.add(btnRegistrar, gbc);

        mainPanel.add(panelCentral, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Validaciones mínimas al registrar
        btnRegistrar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el nombre de la empresa.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtNombre.requestFocus(); return;
            }
            if (txtDireccion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa la dirección.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtDireccion.requestFocus(); return;
            }
            if (txtResponsable.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el responsable.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtResponsable.requestFocus(); return;
            }
            if (txtTelefono.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el teléfono.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtTelefono.requestFocus(); return;
            }
            if (txtCorreo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el correo electrónico.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtCorreo.requestFocus(); return;
            }
            JOptionPane.showMessageDialog(this, "Empresa registrada (demo).");
            // Aquí puedes limpiar campos o cerrar ventana
        });
    }

    private static JLabel labelField(String texto, Color colorPrincipal) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(colorPrincipal);
        return l;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroEmpresaUI().setVisible(true);
        });
    }
}
