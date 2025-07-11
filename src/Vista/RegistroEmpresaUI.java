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
        setMinimumSize(new Dimension(1100, 700));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(colorFondo);

        // Banner morado
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(colorPrincipal);
        banner.setPreferredSize(new Dimension(0, 115));
        JLabel lblTitulo = new JLabel("Registro de Empresa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 48, 22, 0));
        banner.add(lblTitulo, BorderLayout.WEST);
        mainPanel.add(banner, BorderLayout.NORTH);

        // Panel central, blanco y amplio
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(38, 120, 38, 120));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(16, 4, 4, 18);

        // Nombre de la empresa
        gbc.gridy = 0;
        panelCentral.add(labelField("Nombre de la empresa", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtNombre = new JTextField(34);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setPreferredSize(new Dimension(530, 38));
        txtNombre.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtNombre, gbc);

        // RFC
        gbc.gridy++;
        panelCentral.add(labelField("RFC", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtRfc = new JTextField(16);
        txtRfc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtRfc.setPreferredSize(new Dimension(300, 36));
        txtRfc.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtRfc, gbc);

        // Giro o actividad económica
        gbc.gridy++;
        panelCentral.add(labelField("Giro", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtGiro = new JTextField(30);
        txtGiro.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtGiro.setPreferredSize(new Dimension(350, 36));
        txtGiro.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtGiro, gbc);

        // Domicilio
        gbc.gridy++;
        panelCentral.add(labelField("Domicilio", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtDomicilio = new JTextField(35);
        txtDomicilio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDomicilio.setPreferredSize(new Dimension(530, 36));
        txtDomicilio.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtDomicilio, gbc);

        // Teléfono
        gbc.gridy++;
        panelCentral.add(labelField("Teléfono", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtTelefono = new JTextField(15);
        txtTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtTelefono.setPreferredSize(new Dimension(220, 36));
        txtTelefono.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtTelefono, gbc);

        // Correo electrónico
        gbc.gridy++;
        panelCentral.add(labelField("Correo electrónico", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtCorreo = new JTextField(30);
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtCorreo.setPreferredSize(new Dimension(350, 36));
        txtCorreo.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtCorreo, gbc);

        // Representante legal
        gbc.gridy++;
        panelCentral.add(labelField("Representante legal", colorPrincipal), gbc);
        gbc.gridy++;
        JTextField txtRepresentante = new JTextField(30);
        txtRepresentante.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtRepresentante.setPreferredSize(new Dimension(350, 36));
        txtRepresentante.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(txtRepresentante, gbc);

        // Estatus
        gbc.gridy++;
        panelCentral.add(labelField("Estatus", colorPrincipal), gbc);
        gbc.gridy++;
        JComboBox<String> cbEstatus = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        personalizaCombo(cbEstatus, colorPrincipal);
        cbEstatus.setPreferredSize(new Dimension(180, 36));
        panelCentral.add(cbEstatus, gbc);

        // Botón guardar
        gbc.gridy++;
        gbc.insets = new Insets(36, 4, 4, 4); gbc.anchor = GridBagConstraints.CENTER; gbc.gridwidth = 2;
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(colorPrincipal);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(14, 44, 14, 44));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(true);
        panelCentral.add(btnGuardar, gbc);

        mainPanel.add(panelCentral, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Validaciones mínimas al guardar
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el nombre de la empresa.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtNombre.requestFocus(); return;
            }
            if (txtRfc.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el RFC de la empresa.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtRfc.requestFocus(); return;
            }
            if (txtRepresentante.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el representante legal.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtRepresentante.requestFocus(); return;
            }
            JOptionPane.showMessageDialog(this, "Empresa guardada (demo).");
            dispose();
        });
    }

    private static JLabel labelField(String texto, Color colorPrincipal) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(colorPrincipal);
        return l;
    }

    private static void personalizaCombo(JComboBox<String> combo, Color colorPrincipal) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        combo.setBackground(new Color(248,248,255));
        combo.setForeground(colorPrincipal.darker());
        combo.setUI(new BasicComboBoxUI());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroEmpresaUI().setVisible(true);
        });
    }
}
