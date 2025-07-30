package Vista;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class Menu extends JFrame {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);

    public Menu() {
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
        ImageIcon icon = cargarPNGComoIcono("/Recursos/logo.png", 64, 64);
        JLabel logo = new JLabel();
        if (icon != null && icon.getIconWidth() > 0) {
            logo.setIcon(icon);
        } else {
            logo.setText("");
        }
        logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
        panelLogo.add(logo);
        barraSuperior.add(panelLogo, BorderLayout.WEST);

        // Menú principal con iconos PNG personalizados
        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 32, 8));
        panelMenu.setOpaque(false);

        JButton btnAlumnos = new JButton("Alumnos \u25BC");
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

        JButton btnReportes = new JButton("Reportes \u25BC");
        btnReportes.setIcon(cargarPNGComoIcono("/Recursos/excel_1.png", 32, 32));
        configurarBotonMenu(btnReportes);

        panelMenu.add(btnAlumnos);
        panelMenu.add(btnDocentes);
        panelMenu.add(btnEmpresas);
        panelMenu.add(btnProyectos);
        panelMenu.add(btnReportes);
        barraSuperior.add(panelMenu, BorderLayout.CENTER);

        // Estilo personalizado para los menús popup
        class MenuPopupEstilizado extends JPopupMenu {
            public MenuPopupEstilizado() {
                setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(180, 170, 255), 1),
                        BorderFactory.createEmptyBorder(5, 0, 5, 0)
                ));
                setBackground(Color.WHITE);
                // Sombra suave
                setBorder(BorderFactory.createCompoundBorder(
                        new Border() {
                            @Override
                            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                                Graphics2D g2 = (Graphics2D) g;
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2.setColor(new Color(0, 0, 0, 20));
                                for (int i = 0; i < 4; i++) {
                                    g2.drawRoundRect(x + i, y + i, width - 2*i - 1, height - 2*i - 1, 8, 8);
                                }
                            }
                            @Override
                            public Insets getBorderInsets(Component c) { return new Insets(4,4,4,4); }
                            @Override
                            public boolean isBorderOpaque() { return false; }
                        },
                        BorderFactory.createEmptyBorder(5, 0, 5, 0)
                ));
            }
        }

        // Configurar estilo para los items del menú
        class MenuItemEstilizado extends JMenuItem {
            public MenuItemEstilizado(String texto, Icon icono) {
                super(texto, icono);
                setFont(new Font("Segoe UI", Font.PLAIN, 16));
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
                setIconTextGap(12);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        setBackground(new Color(240, 238, 255));
                        setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 3, 0, 0, colorPrincipal),
                                BorderFactory.createEmptyBorder(8, 17, 8, 20)
                        ));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        setBackground(Color.WHITE);
                        setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
                    }
                });
            }
        }

        // Crear menús popup estilizados
        JPopupMenu menuProyectos = new MenuPopupEstilizado();
        JPopupMenu menuAlumnos = new MenuPopupEstilizado();
        JPopupMenu menuReportes = new MenuPopupEstilizado();

        // Items del menú de proyectos
        JMenuItem itemBancoProyectos = new MenuItemEstilizado("Banco de Proyectos", cargarPNGComoIcono("/Recursos/proyectos.png", 22, 22));
        JMenuItem itemAnteproyectos = new MenuItemEstilizado("Anteproyectos", cargarPNGComoIcono("/Recursos/docentes.png", 22, 22));

        // Items del menú de reportes
        JMenuItem itemReporteAsesor = new MenuItemEstilizado("Reporte de Asesores", cargarPNGComoIcono("/Recursos/docentes.png", 22, 22));
        JMenuItem itemReporteRevisor = new MenuItemEstilizado("Reporte de Revisores", cargarPNGComoIcono("/Recursos/docentes.png", 22, 22));
        JMenuItem itemReporteAlumnos = new MenuItemEstilizado("Reporte de Alumnos", cargarPNGComoIcono("/Recursos/alumnos.png", 22, 22));

        menuProyectos.add(itemBancoProyectos);
        menuProyectos.add(itemAnteproyectos);

        // Items del menú de alumnos
        JMenuItem itemCandidatos = new MenuItemEstilizado("Candidatos", cargarPNGComoIcono("/Recursos/alumnos.png", 22, 22));
        JMenuItem itemResidentes = new MenuItemEstilizado("Residentes", cargarPNGComoIcono("/Recursos/alumnos.png", 22, 22));
        JMenuItem itemExpedientes = new MenuItemEstilizado("Expedientes", cargarPNGComoIcono("/Recursos/alumnos.png", 22, 22));

        menuAlumnos.add(itemCandidatos);
        menuAlumnos.add(itemResidentes);
        menuAlumnos.add(itemExpedientes);

        // Agregar items al menú de reportes
        menuReportes.add(itemReporteAsesor);
        menuReportes.add(itemReporteRevisor);
        menuReportes.add(itemReporteAlumnos);

        // Agregar funcionalidad a los items de reportes
        itemReporteAsesor.addActionListener(e -> GenerarReportes.mostrar(this, "Reporte de Asesores"));
        itemReporteRevisor.addActionListener(e -> GenerarReportes.mostrar(this, "Reporte de Revisores"));
        itemReporteAlumnos.addActionListener(e -> GenerarReportes.mostrar(this, "Reporte de Alumnos"));

        // Alinear los menús popup con los botones
        btnProyectos.addActionListener(e -> {
            int x = (btnProyectos.getWidth() - menuProyectos.getPreferredSize().width) / 2;
            menuProyectos.show(btnProyectos, x, btnProyectos.getHeight());
        });

        btnAlumnos.addActionListener(e -> {
            int x = (btnAlumnos.getWidth() - menuAlumnos.getPreferredSize().width) / 2;
            menuAlumnos.show(btnAlumnos, x, btnAlumnos.getHeight());
        });

        btnReportes.addActionListener(e -> {
            int x = (btnReportes.getWidth() - menuReportes.getPreferredSize().width) / 2;
            menuReportes.show(btnReportes, x, btnReportes.getHeight());
        });

        // Panel para los botones de perfil y cerrar sesión
        JPanel panelBotonesUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panelBotonesUsuario.setOpaque(false);

        // Botón de perfil PNG
        JButton btnPerfil = new JButton();
        btnPerfil.setToolTipText("Perfil");
        btnPerfil.setIcon(cargarPNGComoIcono("/Recursos/perfil.png", 32, 32));
        btnPerfil.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btnPerfil.setBackground(Color.WHITE);
        btnPerfil.setFocusPainted(false);
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPerfil.setOpaque(true);

        // Efecto hover para el botón de perfil
        btnPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnPerfil.setBackground(new Color(230, 225, 255));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                btnPerfil.setBackground(Color.WHITE);
            }
        });

        // Botón de cerrar sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setToolTipText("Cerrar Sesión");
        btnCerrarSesion.setIcon(cargarPNGComoIcono("/Recursos/perfil.svg", 36, 36));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCerrarSesion.setForeground(new Color(200, 60, 60));
        btnCerrarSesion.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnCerrarSesion.setBackground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setIconTextGap(12);

        // Efecto hover para el botón de cerrar sesión
        btnCerrarSesion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnCerrarSesion.setBackground(new Color(255, 200, 200));
                btnCerrarSesion.setBorder(new CompoundBorder(
                        new LineBorder(new Color(200, 60, 60), 2, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                btnCerrarSesion.setFont(btnCerrarSesion.getFont().deriveFont(Font.BOLD, 17f));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                btnCerrarSesion.setBackground(Color.WHITE);
                btnCerrarSesion.setBorder(new CompoundBorder(
                        new LineBorder(new Color(230, 200, 200), 2, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                btnCerrarSesion.setFont(btnCerrarSesion.getFont().deriveFont(Font.BOLD, 16f));
            }
        });

        // Acción de cerrar sesión
        btnCerrarSesion.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea cerrar sesión?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == JOptionPane.YES_OPTION) {
                dispose();
                new LoginITO().setVisible(true);
            }
        });

        // Agregar botones al panel
        panelBotonesUsuario.add(btnPerfil);
        panelBotonesUsuario.add(btnCerrarSesion);

        barraSuperior.add(panelBotonesUsuario, BorderLayout.EAST);

        mainPanel.add(barraSuperior, BorderLayout.NORTH);

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
        JLabel lblBienvenida = new JLabel("Bienvenida profesora Maricarmen!");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblBienvenida.setForeground(new Color(60, 60, 100));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(lblBienvenida, gbc);

        // Frases motivacionales rotativas
        gbc.gridy++;
        JLabel lblFrase = new JLabel();
        lblFrase.setFont(new Font("Segoe UI", Font.ITALIC, 28));
        lblFrase.setForeground(new Color(92, 93, 169));
        lblFrase.setHorizontalAlignment(SwingConstants.CENTER);

        String[][] frases = {
                {"El futuro pertenece a quienes creen en la belleza de sus sueños.", "Eleanor Roosevelt"},
                {"La educación es el arma más poderosa que puedes usar para cambiar el mundo.", "Nelson Mandela"},
                {"El éxito es la suma de pequeños esfuerzos repetidos día tras día.", "Robert Collier"},
                {"Todo lo que puedas imaginar es real.", "Pablo Picasso"},
                {"La mejor forma de predecir el futuro es creándolo.", "Peter Drucker"},
                {"No dejes que tus miedos se interpongan en tus sueños.", "Walt Disney"}
        };

        final int[] indice = {0};
        Timer timerFrases = new Timer(20000, e -> {
            indice[0] = (indice[0] + 1) % frases.length;
            lblFrase.setText("<html><center>'" + frases[indice[0]][0] +
                    "'<br><span style='font-size:18px;color:#5c5da9;'>- " +
                    frases[indice[0]][1] + "</span></center></html>");
        });

        // Establecer la primera frase
        lblFrase.setText("<html><center>'" + frases[0][0] +
                "'<br><span style='font-size:18px;color:#5c5da9;'>- " +
                frases[0][1] + "</span></center></html>");

        timerFrases.start();
        panelCentral.add(lblFrase, gbc);

        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Botón de créditos y etiqueta SIREP en footer
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);

        JLabel lblSirep = new JLabel("SIREP (2025)");
        lblSirep.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSirep.setForeground(new Color(92, 93, 169));
        lblSirep.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 0));

        JButton btnCreditos = new JButton();
        btnCreditos.setToolTipText("Créditos");
        btnCreditos.setIcon(cargarPNGComoIcono("/Recursos/perfil.png", 32, 32));
        btnCreditos.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btnCreditos.setBackground(Color.WHITE);
        btnCreditos.setFocusPainted(false);
        btnCreditos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreditos.setOpaque(true);

        btnCreditos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCreditos.setBackground(new Color(230, 225, 255));
            }
            public void mouseExited(MouseEvent evt) {
                btnCreditos.setBackground(Color.WHITE);
            }
        });

        btnCreditos.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "<html><center>"
                        + "<span style='font-size:28px; color:#5c5da9; font-weight:bold;'>SIREP (2025)</span><br><br>"
                        + "<b>by:</b><br>"
                        + "Alonso Cruz Yahir Jibsam<br>"
                        + "Mendoza Duran Juan Francisco<br>"
                        + "Cruz Gomez Ramon Isaac<br>"
                        + "Giner Coache Nierika"
                        + "</center></html>",
                "Créditos del Proyecto",
                JOptionPane.INFORMATION_MESSAGE
        ));

        panelFooter.add(lblSirep, BorderLayout.WEST);
        panelFooter.add(btnCreditos, BorderLayout.EAST);
        mainPanel.add(panelFooter, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Acciones de los botones principales
        btnDocentes.addActionListener(e -> {
            new DocentesUI().setVisible(true);
            this.dispose();
        });
        btnEmpresas.addActionListener(e -> {
            new BancoEmpresasUI().setVisible(true);
            this.dispose();
        });

        // Acciones de los items del menú proyectos
        itemBancoProyectos.addActionListener(e -> {
            new BancoProyectosUI().setVisible(true);
            this.dispose();
        });

        itemAnteproyectos.addActionListener(e -> {
            new AnteproyectoInterfaz().setVisible(true);
            this.dispose();
        });

        // Acciones de los items del menú alumnos
        itemCandidatos.addActionListener(e -> {
            VistaRegistros candidatos = new VistaRegistros();
            candidatos.setVisible(true);
            this.dispose();
        });

        itemResidentes.addActionListener(e -> {
            VistaResidentesActivos residentes = new VistaResidentesActivos();
            residentes.setVisible(true);
            this.dispose();
        });

        itemExpedientes.addActionListener(e -> {
            new ExpedientesUI();
            this.dispose();
        });

        btnPerfil.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Perfil de la profesora Maricarmen\nCorreeo: maricarmen@ito.edu.mx\nRol: Administradora",
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

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
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
            public void mouseExited(MouseEvent evt) {
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
            new Menu().setVisible(true);
        });
    }
}