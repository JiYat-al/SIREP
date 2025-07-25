package Vista;

import Controlador.ControladorResidentesActivos;
import Modelo.ModeloResidente;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

public class VistaResidentesActivos extends JFrame {
    private JTable tablaResidentes;
    private JButton btnEditar;
    private JButton btnDarDeBaja;
    private JButton btnRegresarACandidato;
    private JTextField txtBusqueda;
    private JScrollPane scrollPane;
    private JLabel lblBuscar;
    private JLabel lblResultados;
    private JButton btnActualizar;
    private JButton btnLimpiarBusqueda;
    private JButton btnRegresar;

    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private ControladorResidentesActivos controlador;
    private List<ModeloResidente> listaOriginal;
    private final Color colorPrincipal = new Color(92, 93, 169);

    public VistaResidentesActivos() {
        setTitle("Sistema SIREP - Residentes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        controlador = new ControladorResidentesActivos(this);
        configurarInterfaz();
        configurarEventos();
        cargarDatosIniciales();
    }

    class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        private final int thickness;

        public RoundBorder(Color color, int radius, int thickness) {
            this.color = color;
            this.radius = radius;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness/2, y + thickness/2,
                    width - thickness, height - thickness,
                    radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private void configurarInterfaz() {
        JPanel mainPanel = new JPanel(new BorderLayout()) {
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

        crearBarraLateral(mainPanel);

        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);

        JPanel header = crearHeader();
        panelContenido.add(header, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 38, 20, 38));

        JPanel panelBusqueda = crearPanelBusqueda();
        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        configurarTabla();
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        panelContenido.add(panelCentral, BorderLayout.CENTER);

        crearBotonRegresar(mainPanel);

        mainPanel.add(panelContenido, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void crearBarraLateral(JPanel mainPanel) {
        JPanel barraLateral = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint grad = new GradientPaint(
                        0, 0, colorPrincipal.darker(),
                        getWidth(), getHeight(), colorPrincipal.brighter()
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                g2.setColor(new Color(60, 60, 100, 40));
                g2.fillRect(getWidth() - 8, 0, 8, getHeight());
            }
        };
        barraLateral.setPreferredSize(new Dimension(100, 0));

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0; gbcL.gridy = 0;
        gbcL.insets = new Insets(24, 0, 18, 0);
        gbcL.anchor = GridBagConstraints.NORTH;

        JLabel iconoBarra = new JLabel("R", SwingConstants.CENTER);
        iconoBarra.setFont(new Font("Segoe UI", Font.BOLD, 48));
        iconoBarra.setForeground(Color.WHITE);
        barraLateral.add(iconoBarra, gbcL);

        gbcL.gridy++;
        gbcL.insets = new Insets(0, 0, 0, 0);
        JLabel tituloBarra = new JLabel("<html><div style='text-align: center; line-height: 1.5;'>" +
                "E<br>S<br>I<br>D<br>E<br>N<br>T<br>E<br>S</div></html>");
        tituloBarra.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloBarra.setForeground(new Color(245, 243, 255, 200));
        tituloBarra.setHorizontalAlignment(SwingConstants.CENTER);
        barraLateral.add(tituloBarra, gbcL);

        gbcL.gridy++;
        gbcL.weighty = 1.0;
        gbcL.anchor = GridBagConstraints.SOUTH;
        gbcL.insets = new Insets(0, 0, 24, 0);
        JButton btnAyuda = new JButton("?");
        btnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnAyuda.setForeground(colorPrincipal);
        btnAyuda.setBackground(Color.WHITE);
        btnAyuda.setFocusPainted(false);
        btnAyuda.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAyuda.addActionListener(e -> mostrarAyuda());
        barraLateral.add(btnAyuda, gbcL);

        mainPanel.add(barraLateral, BorderLayout.WEST);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                g2.setColor(new Color(230, 230, 250));
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 38));

        JLabel lblTitulo = new JLabel("Residentes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        header.add(lblTitulo, BorderLayout.WEST);

        return header;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.setOpaque(false);
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel panelBusquedaCompleto = new JPanel();
        panelBusquedaCompleto.setLayout(new BoxLayout(panelBusquedaCompleto, BoxLayout.Y_AXIS));
        panelBusquedaCompleto.setOpaque(false);

        lblBuscar = new JLabel("Buscar residente:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBuscar.setForeground(colorPrincipal);
        lblBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBuscar.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        txtBusqueda = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                super.paintComponent(g);
            }
        };
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtBusqueda.setBackground(Color.WHITE);
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        txtBusqueda.setPreferredSize(new Dimension(400, 40));
        txtBusqueda.setMaximumSize(new Dimension(400, 40));
        txtBusqueda.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelBusquedaHorizontal = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBusquedaHorizontal.setOpaque(false);
        panelBusquedaHorizontal.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnActualizar = crearBotonBusqueda("Buscar", "🔍");
        btnLimpiarBusqueda = crearBotonBusqueda("Limpiar", "✖");

        panelBusquedaHorizontal.add(txtBusqueda);
        panelBusquedaHorizontal.add(btnActualizar);
        panelBusquedaHorizontal.add(btnLimpiarBusqueda);

        JButton btnActualizarTabla = crearBotonBusqueda("Actualizar", "🔄");
        panelBusquedaHorizontal.add(Box.createHorizontalStrut(10));
        panelBusquedaHorizontal.add(btnActualizarTabla);
        btnActualizarTabla.addActionListener(e -> controlador.cargarTodosLosResidentes());

        lblResultados = new JLabel("Total de residentes: 0");
        lblResultados.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblResultados.setForeground(new Color(100, 100, 100));
        lblResultados.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        lblResultados.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelBusquedaCompleto.add(lblBuscar);
        panelBusquedaCompleto.add(panelBusquedaHorizontal);
        panelBusquedaCompleto.add(lblResultados);

        panelBusqueda.add(panelBusquedaCompleto, BorderLayout.WEST);
        return panelBusqueda;
    }

    private void configurarTabla() {
        String[] columnas = {
                "No. Control", "Nombre", "Apellido P.", "Apellido M.",
                "Semestre", "Correo", "Telefono"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 4) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        tablaResidentes = new JTable(modeloTabla) {
            private int hoveredRow = -1;
            {
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setRowSelectionAllowed(true);
                setColumnSelectionAllowed(false);
                getTableHeader().setReorderingAllowed(false);
                setFocusable(true);
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent e) {
                        int row = rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row; repaint();
                        }
                    }
                });
                addMouseListener(new MouseAdapter() {
                    public void mouseExited(MouseEvent e) {
                        hoveredRow = -1; repaint();
                    }
                });
            }
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(220, 219, 245));
                } else {
                    c.setBackground(Color.WHITE);
                }
                c.setForeground(new Color(60, 60, 100));
                return c;
            }
        };

        tablaResidentes.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tablaResidentes.setRowHeight(35);
        tablaResidentes.setShowVerticalLines(false);
        tablaResidentes.setShowHorizontalLines(true);
        tablaResidentes.setGridColor(new Color(230, 230, 250));

        tablaResidentes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tablaResidentes.getTableHeader().setBackground(new Color(220, 219, 245));
        tablaResidentes.getTableHeader().setForeground(colorPrincipal);
        tablaResidentes.getTableHeader().setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) tablaResidentes.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tablaResidentes.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaResidentes.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tablaResidentes.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tablaResidentes.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tablaResidentes.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tablaResidentes.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        tablaResidentes.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        configurarAnchoColumnas();

        sorter = new TableRowSorter<>(modeloTabla);
        tablaResidentes.setRowSorter(sorter);

        scrollPane = new JScrollPane(tablaResidentes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = colorPrincipal;
                this.trackColor = new Color(235, 235, 250);
            }
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
    }

    private void configurarAnchoColumnas() {
        tablaResidentes.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaResidentes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaResidentes.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaResidentes.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaResidentes.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaResidentes.getColumnModel().getColumn(5).setPreferredWidth(250);
        tablaResidentes.getColumnModel().getColumn(6).setPreferredWidth(150);
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelBotones.setOpaque(false);

        btnEditar = new JButton("Editar") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hover) {
                    g2.setColor(colorPrincipal.darker());
                } else {
                    g2.setColor(colorPrincipal);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        btnDarDeBaja = new JButton("Dar de baja") {
            private boolean hover = false;
            private final Color colorBase = new Color(180, 170, 255, 80);
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hover) {
                    g2.setColor(colorBase.darker());
                } else {
                    g2.setColor(colorBase);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        btnRegresarACandidato = new JButton("Regresar a Candidato") {
            private boolean hover = false;
            private final Color colorBase = new Color(180, 170, 255, 80);
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hover) {
                    g2.setColor(colorBase.darker());
                } else {
                    g2.setColor(colorBase);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        panelBotones.add(btnEditar);
        panelBotones.add(btnDarDeBaja);
        panelBotones.add(btnRegresarACandidato);

        return panelBotones;
    }

    private JButton crearBotonBusqueda(String texto, String icono) {
        JButton boton = new JButton(icono + " " + texto) {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(colorPrincipal);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(new RoundBorder(colorPrincipal, 8, 1));
                setPreferredSize(new Dimension(95, 28));
                setMargin(new Insets(2, 8, 2, 8));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        setForeground(colorPrincipal.darker());
                        setBorder(new RoundBorder(colorPrincipal.darker(), 8, 1));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        setForeground(colorPrincipal);
                        setBorder(new RoundBorder(colorPrincipal, 8, 1));
                    }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hover) {
                    g2.setColor(new Color(245, 245, 255));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                super.paintComponent(g);
                g2.dispose();
            }
        };
        return boton;
    }

    private void crearBotonRegresar(JPanel mainPanel) {
        btnRegresar = new JButton("\u2190");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRegresar.setForeground(colorPrincipal);
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> regresarAMenu());

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTop.setOpaque(false);
        panelTop.add(btnRegresar);
        mainPanel.add(panelTop, BorderLayout.NORTH);
    }

    private void configurarEventos() {
        txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarDatos();
            }
        });

        btnDarDeBaja.addActionListener(e -> controlador.darDeBajaResidenteSeleccionado());
        btnEditar.addActionListener(e -> controlador.editarResidenteSeleccionado());
        btnRegresarACandidato.addActionListener(e -> controlador.regresarACandidato());
        btnActualizar.addActionListener(e -> controlador.cargarTodosLosResidentes());
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());

        tablaResidentes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = tablaResidentes.getSelectedRow() != -1;
                btnDarDeBaja.setEnabled(haySeleccion);
                btnEditar.setEnabled(haySeleccion);
                btnRegresarACandidato.setEnabled(haySeleccion);
            }
        });

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarBusqueda();
            }
        });
    }

    private void filtrarDatos() {
        String textoBusqueda = txtBusqueda.getText().trim();

        if (textoBusqueda.isEmpty() || textoBusqueda.equals("Buscar por nombre o numero de control...")) {
            sorter.setRowFilter(null);
        } else {
            RowFilter<DefaultTableModel, Object> filtro = RowFilter.regexFilter(
                    "(?i)" + textoBusqueda, 0, 1, 2, 3
            );
            sorter.setRowFilter(filtro);
        }

        actualizarContadorResultados();
    }

    private void limpiarBusqueda() {
        txtBusqueda.setText("");
        sorter.setRowFilter(null);
        actualizarContadorResultados();
    }

    private void actualizarContadorResultados() {
        int totalRegistros = modeloTabla.getRowCount();
        int registrosFiltrados = tablaResidentes.getRowCount();

        if (sorter.getRowFilter() == null) {
            lblResultados.setText("Total de residentes: " + totalRegistros);
        } else {
            lblResultados.setText("Mostrando: " + registrosFiltrados + " de " + totalRegistros + " residentes");
        }
    }

    private void cargarDatosIniciales() {
        if (controlador != null) {
            controlador.cargarTodosLosResidentes();
        }
    }

    private void mostrarAyuda() {
        String ayuda = "Sistema de Residentes SIREP\n\n" +
                "• Busqueda: Escriba para filtrar por nombre o numero de control\n" +
                "• Editar: Seleccione un residente y haga clic en Editar\n" +
                "• Dar de baja: Seleccione un residente y haga clic en Dar de baja\n" +
                "• Regresar a Candidato: Convierte el residente de vuelta a candidato\n" +
                "• Actualizar: Recarga todos los residentes desde la base de datos\n" +
                "• Limpiar: Borra el filtro de busqueda\n" +
                "• ESC: Atajo para limpiar la busqueda\n" +
                "• Flecha izquierda: Regresa al menu principal\n\n";

        JOptionPane.showMessageDialog(this, ayuda, "Ayuda - Sistema SIREP", JOptionPane.INFORMATION_MESSAGE);
    }

    private void regresarAMenu() {
        Menu menu = new Menu();
        menu.setVisible(true);
        this.dispose();
    }

    public void cargarResidentes(List<ModeloResidente> residentes) {
        this.listaOriginal = residentes;
        modeloTabla.setRowCount(0);

        for (ModeloResidente residente : residentes) {
            Object[] fila = {
                    residente.getNumeroControl(),
                    residente.getNombre(),
                    residente.getApellidoPaterno(),
                    residente.getApellidoMaterno(),
                    residente.getSemestre(),
                    residente.getCorreo(),
                    residente.getTelefono()
            };
            modeloTabla.addRow(fila);
        }

        actualizarContadorResultados();
    }

    public ModeloResidente getResidenteSeleccionado() {
        int filaSeleccionada = tablaResidentes.getSelectedRow();
        if (filaSeleccionada != -1) {
            int filaModelo = tablaResidentes.convertRowIndexToModel(filaSeleccionada);
            if (listaOriginal != null && filaModelo < listaOriginal.size()) {
                return listaOriginal.get(filaModelo);
            }
        }
        return null;
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    public boolean mostrarConfirmacion(String mensaje, String titulo) {
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }

    public void actualizarTabla() {
        controlador.cargarTodosLosResidentes();
    }

    public void abrirDialogoEdicion(ModeloResidente residente) {
        try {
            DialogoEdicionResidente dialogo = new DialogoEdicionResidente(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    residente
            );

            dialogo.setVisible(true);

            if (dialogo.isGuardado()) {
                actualizarTabla();
            }

        } catch (Exception e) {
            mostrarMensaje(
                    "Error al abrir el editor:\n" + e.getMessage(),
                    "Error de edición",
                    JOptionPane.ERROR_MESSAGE
            );
            System.err.println("Error al editar residente: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaResidentesActivos().setVisible(true));
    }
}