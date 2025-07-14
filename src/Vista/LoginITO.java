package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginITO extends JFrame {
    private final Color colorPrincipal = new Color(92, 93, 169);

    // Atributos que se necesitan desde el controlador
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginITO() {
        setTitle("Iniciar sesión - Instituto Tecnológico de Oaxaca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setMinimumSize(new Dimension(420, 500));
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(230, 225, 255), 0, getHeight(), colorPrincipal);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(new Color(180, 170, 255, 80));
                g2.drawArc(-120, -120, 300, 300, 0, 360);
                g2.setColor(new Color(140, 130, 220, 60));
                g2.drawArc(-80, -80, 200, 200, 0, 360);

                g2.setColor(new Color(180, 170, 255, 60));
                g2.drawArc(getWidth() - 180, getHeight() - 180, 160, 160, 0, 360);
                g2.setColor(new Color(140, 130, 220, 40));
                g2.drawArc(getWidth() - 120, getHeight() - 120, 100, 100, 0, 360);
            }
        };
        fondo.setLayout(new BorderLayout());

        JPanel panelLogoFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(new Color(60, 60, 100, 60));
                g2.fillRoundRect(w / 2 - 85, 18, 170, 170, 90, 90);

                GradientPaint grad = new GradientPaint(
                        w / 2 - 80, 10, colorPrincipal.darker(),
                        w / 2 + 80, 170, colorPrincipal.brighter()
                );
                g2.setPaint(grad);
                g2.fillRoundRect(w / 2 - 80, 10, 160, 160, 80, 80);

                g2.setColor(new Color(255, 255, 255, 120));
                g2.setStroke(new BasicStroke(4f));
                g2.drawRoundRect(w / 2 - 80, 10, 160, 160, 80, 80);
            }
        };
        panelLogoFondo.setOpaque(false);
        panelLogoFondo.setPreferredSize(new Dimension(200, 180));
        panelLogoFondo.setLayout(new GridBagLayout());

        JLabel lblLogo = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/logo.png"));
            Image img = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            lblLogo.setText("ITO");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 42));
            lblLogo.setForeground(Color.WHITE);
        }
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setVerticalAlignment(SwingConstants.CENTER);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(30, 0, 16, 0));
        panelLogoFondo.add(lblLogo, new GridBagConstraints());
        fondo.add(panelLogoFondo, BorderLayout.NORTH);

        JPanel panelLogin = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(60, 60, 100, 40));
                g2.fillRoundRect(18, 24, getWidth() - 36, getHeight() - 36, 60, 60);
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(12, 18, getWidth() - 24, getHeight() - 24, 54, 54);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelLogin.setOpaque(false);
        panelLogin.setBackground(Color.WHITE);
        panelLogin.setBorder(BorderFactory.createEmptyBorder(18, 36, 18, 36));
        panelLogin.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(12, 0, 12, 0);
        c.gridx = 0;
        c.gridwidth = 2;

        JLabel lblInstituto = new JLabel("INSTITUTO TECNOLÓGICO DE OAXACA");
        lblInstituto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInstituto.setForeground(colorPrincipal.darker());
        lblInstituto.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 0;
        panelLogin.add(lblInstituto, c);

        JLabel lblTitulo = new JLabel("Iniciar sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy++;
        panelLogin.add(lblTitulo, c);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUsuario.setForeground(colorPrincipal.darker());
        c.gridy++;
        panelLogin.add(lblUsuario, c);

        txtUsuario = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(92, 93, 169, 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(0, 14, 0, 0)));
        c.gridy++;
        panelLogin.add(txtUsuario, c);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPassword.setForeground(colorPrincipal.darker());
        c.gridy++;
        panelLogin.add(lblPassword, c);

        txtPassword = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(92, 93, 169, 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(0, 14, 0, 0)));
        c.gridy++;
        panelLogin.add(txtPassword, c);

        btnLogin = new JButton("Iniciar sesión") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 18));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(13, 40, 13, 40));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(60, 60, 100, 60));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 4, 40, 40);
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorPrincipal.darker() : colorPrincipal,
                        getWidth(), getHeight(), colorPrincipal.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        c.gridy++;
        c.insets = new Insets(18, 0, 12, 0);
        panelLogin.add(btnLogin, c);

        JPanel espacioOlvido = new JPanel();
        espacioOlvido.setOpaque(false);
        espacioOlvido.setPreferredSize(new Dimension(10, 10));
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        panelLogin.add(espacioOlvido, c);

        JButton btnForgot = new JButton("<HTML><U>¿Olvidaste tu contraseña?</U></HTML>");
        btnForgot.setContentAreaFilled(false);
        btnForgot.setBorderPainted(false);
        btnForgot.setFocusPainted(false);
        btnForgot.setForeground(new Color(92, 93, 169));
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgot.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Solicita el restablecimiento al administrador.",
                "Recuperar contraseña", JOptionPane.INFORMATION_MESSAGE));
        btnForgot.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnForgot.setForeground(colorPrincipal.darker());
            }
            public void mouseExited(MouseEvent e) {
                btnForgot.setForeground(new Color(92, 93, 169));
            }
        });

        c.gridy++;
        c.insets = new Insets(8, 0, 0, 0);
        panelLogin.add(btnForgot, c);

        fondo.add(panelLogin, BorderLayout.CENTER);

        setContentPane(fondo);
    }

    //  Getters
    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginITO().setVisible(true);
        });
    }
}
