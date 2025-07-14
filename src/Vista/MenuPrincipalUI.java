package Vista;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Menú principal ultra mejorado con efectos visuales, iconos, animaciones y detalles modernos.
 * Pensado para impresionar a la profesora Maricarmen.
 */
public class MenuPrincipalUI extends JFrame {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);

    public MenuPrincipalUI() {
        setTitle("Sistema de Banco de Proyectos - ITO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        // Fondo degradado con líneas decorativas y partículas animadas
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            int[] px = new int[24];
            int[] py = new int[24];
            int[] dx = new int[24];
            int[] dy = new int[24];
            {
                for (int i = 0; i < px.length; i++) {
                    px[i] = (int)(Math.random()*1200);
                    py[i] = (int)(Math.random()*700);
                    dx[i] = (int)(Math.random()*3+1);
                    dy[i] = (int)(Math.random()*3+1);
                }
                Timer timer = new Timer(40, e -> repaint());
                timer.start();
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(230, 225, 255),
                        0, getHeight(), colorPrincipal
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Líneas curvas decorativas
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

                // Partículas animadas
                for (int i = 0; i < px.length; i++) {
                    g2.setColor(new Color(180, 170, 255, 40));
                    g2.fillOval(px[i], py[i], 12, 12);
                    px[i] += dx[i];
                    py[i] += dy[i];
                    if (px[i] > getWidth()) px[i] = 0;
                    if (py[i] > getHeight()) py[i] = 0;
                }
            }
        };
        mainPanel.setBackground(colorFondo);

        // Barra superior con gradiente, sombra y glow
        JPanel barraSuperior = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint grad = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(230, 225, 255)
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(92, 93, 169, 30));
                g2.fillRect(0, getHeight()-8, getWidth(), 8);

                // Glow inferior
                for (int i = 0; i < 8; i++) {
                    g2.setColor(new Color(92, 93, 169, 10));
                    g2.fillRect(0, getHeight()-8-i, getWidth(), 1);
                }
            }
        };
        barraSuperior.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 250)));

        // Logo ITO con glow y animación de entrada
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 8));
        panelLogo.setOpaque(false);

        // Carga el logotipo del ITO desde la carpeta de imágenes y ajusta el tamaño
        ImageIcon icon = cargarPNGComoIcono("/Recursos/logo.png", 64, 64); // Ajusta el tamaño aquí
        JLabel logo = new JLabel();
        if (icon != null && icon.getIconWidth() > 0) {
            logo.setIcon(icon);
        } else {
            logo.setText(""); // No mostrar texto alternativo
        }
        logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
        panelLogo.add(logo);
        barraSuperior.add(panelLogo, BorderLayout.WEST);

        // Menú principal con iconos PNG personalizados
        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 32, 8));
        panelMenu.setOpaque(false);

        JButton btnAlumnos = new JButton("Alumnos");
        btnAlumnos.setIcon(cargarPNGComoIcono("/Recursos/alumnos.png", 28, 28));
        configurarBotonMenu(btnAlumnos);

        JButton btnDocentes = new JButton("Docentes");
        btnDocentes.setIcon(cargarPNGComoIcono("/Recursos/docentes.png", 28, 28));
        configurarBotonMenu(btnDocentes);

        JButton btnEmpresas = new JButton("Empresas");
        btnEmpresas.setIcon(cargarPNGComoIcono("/Recursos/empresa.png", 28, 28));
        configurarBotonMenu(btnEmpresas);

        JButton btnProyectos = new JButton("Proyectos \u25BC");
        btnProyectos.setIcon(cargarPNGComoIcono("/Recursos/proyectos.png", 28, 28));
        configurarBotonMenu(btnProyectos);

        JPopupMenu menuProyectos = new JPopupMenu();
        JMenuItem itemBancoProyectos = new JMenuItem("Banco de Proyectos", cargarPNGComoIcono("/Recursos/proyecto.png", 22, 22));
        JMenuItem itemAnteproyectos = new JMenuItem("Anteproyectos", cargarPNGComoIcono("/Recursos/proyecto.png", 22, 22));
        itemBancoProyectos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        itemAnteproyectos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        itemBancoProyectos.setBackground(Color.WHITE);
        itemAnteproyectos.setBackground(Color.WHITE);

        // Efecto hover en el menú popup
        itemBancoProyectos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { itemBancoProyectos.setBackground(new Color(230,225,255)); }
            public void mouseExited(MouseEvent e) { itemBancoProyectos.setBackground(Color.WHITE); }
        });
        itemAnteproyectos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { itemAnteproyectos.setBackground(new Color(230,225,255)); }
            public void mouseExited(MouseEvent e) { itemAnteproyectos.setBackground(Color.WHITE); }
        });

        menuProyectos.add(itemBancoProyectos);
        menuProyectos.add(itemAnteproyectos);

        btnProyectos.addActionListener(e -> {
            menuProyectos.show(btnProyectos, 0, btnProyectos.getHeight());
        });

        panelMenu.add(btnAlumnos);
        panelMenu.add(btnDocentes);
        panelMenu.add(btnEmpresas);
        panelMenu.add(btnProyectos);

        barraSuperior.add(panelMenu, BorderLayout.CENTER);

        // Botón de perfil PNG en la esquina superior derecha
        JButton btnPerfil = new JButton();
        btnPerfil.setToolTipText("Perfil");
        btnPerfil.setIcon(cargarPNGComoIcono("/Recursos/perfil.png", 32, 32)); // Usa un tamaño más grande para mejor visibilidad
        btnPerfil.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btnPerfil.setBackground(Color.WHITE);
        btnPerfil.setFocusPainted(false);
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPerfil.setOpaque(true);

        // Efecto hover para el botón de perfil
        btnPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPerfil.setBackground(new Color(230, 225, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPerfil.setBackground(Color.WHITE);
            }
        });

        barraSuperior.add(btnPerfil, BorderLayout.EAST);

        mainPanel.add(barraSuperior, BorderLayout.NORTH);

        // Botón de créditos en la esquina inferior derecha
        JButton btnCreditos = new JButton();
        btnCreditos.setToolTipText("Créditos");
        btnCreditos.setIcon(cargarPNGComoIcono("/Recursos/creditos.png", 32, 32)); // Usa tu icono, por ejemplo "creditos.png"
        btnCreditos.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btnCreditos.setBackground(Color.WHITE);
        btnCreditos.setFocusPainted(false);
        btnCreditos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreditos.setOpaque(true);

        // Efecto hover para el botón de créditos
        btnCreditos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCreditos.setBackground(new Color(230, 225, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCreditos.setBackground(Color.WHITE);
            }
        });

        btnCreditos.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "<html><center>"
                        + "<span style='font-size:28px; color:#5c5da9; font-weight:bold;'>SIREP (2025)</span><br><br>"
                        + "<b>by:</b><br>"
                        + "Yahir Jibsam Alonso Cruz<br>"
                        + "Juan Francisco Mendoza<br>"
                        + "Cruz Gomez Ramon Isaac<br>"
                        + "Giner Coache Nierika"
                        + "</center></html>",
                "Créditos del Proyecto",
                JOptionPane.INFORMATION_MESSAGE
        ));

        // Panel para colocar el botón en la esquina inferior derecha
        JPanel panelCreditos = new JPanel(new BorderLayout());
        panelCreditos.setOpaque(false);
        panelCreditos.add(btnCreditos, BorderLayout.SOUTH);
        mainPanel.add(panelCreditos, BorderLayout.SOUTH);

        // Etiqueta SIREP (2025) en la esquina inferior izquierda
        JLabel lblSirep = new JLabel("SIREP (2025)");
        lblSirep.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSirep.setForeground(new Color(92, 93, 169));
        lblSirep.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 0));

        // Panel para colocar la etiqueta en la esquina inferior izquierda
        JPanel panelSirep = new JPanel(new BorderLayout());
        panelSirep.setOpaque(false);
        panelSirep.add(lblSirep, BorderLayout.WEST);
        mainPanel.add(panelSirep, BorderLayout.SOUTH);

        // Panel footer para colocar juntos el botón de créditos y la etiqueta SIREP
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.add(lblSirep, BorderLayout.WEST);
        panelFooter.add(btnCreditos, BorderLayout.EAST);
        mainPanel.add(panelFooter, BorderLayout.SOUTH);

        // Panel central con icono antes del texto de bienvenida
        JPanel panelCentral = new JPanel(new GridBagLayout()) {
            int alpha = 0;
            Timer fadeIn = new Timer(20, e -> {
                if (alpha < 255) { alpha += 8; repaint(); }
            });
            { fadeIn.start(); }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                String titulo = "SIREP";
                int x = getWidth()/2 - g2.getFontMetrics(new Font("Segoe UI Black", Font.BOLD, 110)).stringWidth(titulo)/2;
                int y = 120;
                g2.setFont(new Font("Segoe UI Black", Font.BOLD, 110));
                for (int i = 12; i >= 2; i -= 2) {
                    g2.setColor(new Color(92, 93, 169, 12));
                    g2.drawString(titulo, x + i, y + i);
                }
                for (int i = 1; i <= 16; i++) {
                    g2.setColor(new Color(180, 170, 255, 10));
                    g2.drawString(titulo, x + i, y + i);
                }
                g2.setColor(new Color(255,255,255,120));
                g2.setStroke(new BasicStroke(4f));
                g2.drawString(titulo, x-2, y-2);
                g2.setColor(new Color(92, 93, 169, Math.min(alpha,255)));
                g2.setFont(new Font("Segoe UI Black", Font.BOLD, 110));
                g2.drawString(titulo, x, y);

                String reflejo = "SIREP";
                g2.setFont(new Font("Segoe UI Black", Font.BOLD, 110));
                Composite old = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                g2.setColor(colorPrincipal.brighter());
                g2.drawString(reflejo, x, y + 80);
                g2.setComposite(old);

                // Efecto de partículas en el centro
                for (int i = 0; i < 12; i++) {
                    int px = x + (int)(Math.random()*600) - 300;
                    int py = y + (int)(Math.random()*120) + 40;
                    g2.setColor(new Color(180, 170, 255, 40));
                    g2.fillOval(px, py, 10, 10);
                }
            }
        };
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 20, 0);

        panelCentral.add(Box.createRigidArea(new Dimension(900, 120)), gbc);

        // Icono antes del texto de bienvenida
        gbc.gridy++;
        JLabel iconoProfa = new JLabel();
        iconoProfa.setIcon(cargarPNGComoIcono("/Recursos/profa.png", 54, 54));
        iconoProfa.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(iconoProfa, gbc);

        // Texto de bienvenida
        gbc.gridy++;
        JLabel lblBienvenida = new JLabel("Bienvenida profesora Maricarmen");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblBienvenida.setForeground(new Color(60, 60, 100));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(lblBienvenida, gbc);

        // Frase motivacional
        gbc.gridy++;
        JLabel lblFrase = new JLabel("<html><center>“El futuro pertenece a quienes creen en la belleza de sus sueños.”<br><span style='font-size:18px;color:#5c5da9;'>- Eleanor Roosevelt</span></center></html>");
        lblFrase.setFont(new Font("Segoe UI", Font.ITALIC, 28));
        lblFrase.setForeground(new Color(92, 93, 169));
        lblFrase.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(lblFrase, gbc);

        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Easter egg: botón invisible en la esquina inferior izquierda
        JButton btnEasterEgg = new JButton();
        btnEasterEgg.setOpaque(false);
        btnEasterEgg.setContentAreaFilled(false);
        btnEasterEgg.setBorderPainted(false);
        btnEasterEgg.setFocusPainted(false);
        btnEasterEgg.setPreferredSize(new Dimension(40, 40));
        btnEasterEgg.setToolTipText("¿Curioso?");

        btnEasterEgg.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "<html><center>"
                        + "<span style='font-size:32px; color:#5c5da9; font-weight:bold;'>SIREP (2025)</span><br><br>"
                        + "<b>by:</b><br>"
                        + "<div style='font-size:20px; color:#444;'>"
                        + "Yahir Jibsam Alonso Cruz<br>"
                        + "Juan Francisco Mendoza<br>"
                        + "Cruz Gomez Ramon Isaac<br>"
                        + "Giner Coache Nierika"
                        + "</div>"
                        + "<br><span style='font-size:16px;color:#aaa;'>¡Gracias por descubrir el easter egg!</span>"
                        + "</center></html>",
                "Easter Egg",
                JOptionPane.INFORMATION_MESSAGE
        ));

        // Panel para colocar el botón invisible en la esquina inferior izquierda
        JPanel panelEasterEgg = new JPanel(new BorderLayout());
        panelEasterEgg.setOpaque(false);
        panelEasterEgg.add(btnEasterEgg, BorderLayout.WEST);
        mainPanel.add(panelEasterEgg, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Acciones de los botones para abrir los bancos
        /*btnAlumnos.addActionListener(e -> new BancoAlumnosUI().setVisible(true));
        btnDocentes.addActionListener(e -> new BancoDocentesUI().setVisible(true));*/
        btnEmpresas.addActionListener(e -> {
            new BancoEmpresasUI().setVisible(true);
            this.dispose();
        });
        itemBancoProyectos.addActionListener(e -> new BancoProyectosUI().setVisible(true));
       // itemAnteproyectos.addActionListener(e -> new BancoAnteproyectosUI().setVisible(true));
        btnPerfil.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Perfil de la profesora Maricarmen\nCorreo: maricarmen@ito.edu.mx\nRol: Administradora",
                "Perfil", JOptionPane.INFORMATION_MESSAGE));
    }

    // Utilidad para cargar PNG como ImageIcon
    private ImageIcon cargarPNGComoIcono(String ruta, int ancho, int alto) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(ruta));
            Image esc = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(esc);
        } catch (Exception ex) {
            return null;
        }
    }

    // Método para configurar los botones del menú con efectos
    private void configurarBotonMenu(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(colorPrincipal);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 250), 2, true),
                BorderFactory.createEmptyBorder(10, 28, 10, 28)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(14);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(230, 225, 255));
                btn.setForeground(colorPrincipal.darker());
                btn.setBorder(new CompoundBorder(
                        new LineBorder(colorPrincipal, 2, true),
                        BorderFactory.createEmptyBorder(10, 28, 10, 28)
                ));
                btn.setFont(btn.getFont().deriveFont(Font.BOLD, 20f));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(180,170,255,120), 3, true),
                        BorderFactory.createEmptyBorder(10, 28, 10, 28)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(colorPrincipal);
                btn.setBorder(new CompoundBorder(
                        new LineBorder(new Color(230, 230, 250), 2, true),
                        BorderFactory.createEmptyBorder(10, 28, 10, 28)
                ));
                btn.setFont(btn.getFont().deriveFont(Font.BOLD, 18f));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipalUI().setVisible(true);
        });
    }
}