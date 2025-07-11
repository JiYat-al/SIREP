package Vista.Resource;

import javax.swing.*;
import java.awt.*;

public class RegistroEmpresaUI extends JFrame {
    public RegistroEmpresaUI() {
        setTitle("Registro de Empresas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 420);
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        setLayout(new BorderLayout());

        // === Panel de Formulario ===
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Registro de Empresas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(65, 65, 65));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelForm.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Campos
        gbc.gridy++;
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(18);
        panelForm.add(lblNombre, gbc);
        gbc.gridx = 1; panelForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblRFC = new JLabel("Direccion:");
        JTextField txtDireccion = new JTextField(18);
        panelForm.add(lblRFC, gbc);
        gbc.gridx = 1; panelForm.add(txtDireccion, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblResponsable = new JLabel("Responsable:");
        JTextField txtResponsable = new JTextField(18);
        panelForm.add(lblResponsable, gbc);
        gbc.gridx = 1; panelForm.add(txtResponsable, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField(18);
        panelForm.add(lblTelefono, gbc);
        gbc.gridx = 1; panelForm.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblCorreo = new JLabel("Correo electrónico:");
        JTextField txtCorreo = new JTextField(18);
        panelForm.add(lblCorreo, gbc);
        gbc.gridx = 1; panelForm.add(txtCorreo, gbc);

        // Botón registrar estilizado
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnRegistrar = new JButton("Registrar empresa");
        btnRegistrar.setBackground(new Color(92, 93, 169));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 15));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 30, 8, 30),
                BorderFactory.createMatteBorder(0,0,5,0,new Color(180,180,255,80))
        ));
        panelForm.add(btnRegistrar, gbc);

        add(panelForm, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroEmpresaUI().setVisible(true);
        });
    }
}
