package Vista;

import Controlador.ControladorRegistros;
import Modelo.ModeloResidente;
import Vista.VistaResidentes.VistaResidente;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

public class VistaRegistros extends JFrame {
    // Paneles principales para intercambio
    private JPanel panelContenedor;
    private JPanel panelRegistros;
    private VistaResidente panelResidentes; // AGREGADO: referencia directa
    private CardLayout cardLayout;

    // Vista actual
    private String vistaActual = "REGISTROS";

    // Componentes principales
    private JTable candidatos;
    private JButton darDeBaja; // *** CAMBIO: eliminar → darDeBaja ***
    private JButton editar;
    private JTextField textField1;
    private JScrollPane scrollPane;
    private JLabel lblBuscar;
    private JLabel lblResultados;
    private JButton btnActualizar;
    private JButton btnLimpiarBusqueda;
    private JButton btnNuevoAlumno;

    // Modelo de la tabla y filtros
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    // Controlador y datos
    private ControladorRegistros controlador;
    private List<ModeloResidente> listaOriginal;

    // Colores del tema
    private final Color colorPrincipal = new Color(92, 93, 169);
    // Referencias para actualizar barra lateral
    private JLabel iconoBarra;
    private JLabel tituloBarra;

    public VistaRegistros() {
        setTitle("Sistema SIREP - Gestión de Alumnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        // Inicializar controlador
        controlador = new ControladorRegistros(this);

        // Configurar interfaz con sistema de paneles
        configurarInterfazConPaneles();
        configurarEventos();
        cargarDatosIniciales();
    }
    
    // ==================== CLASE BORDE REDONDEADO ====================
    
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

    private void configurarInterfazConPaneles() {
        // Panel principal con fondo degradado
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
            }
        };

        // Crear barra lateral
        crearBarraLateral(mainPanel);

        // Configurar sistema de paneles intercambiables
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setOpaque(false);

        // Crear panel de registros (vista actual)
        crearPanelRegistros();

        // Crear panel de residentes (usando VistaResidente sin JFrame)
        crearPanelResidentes();

        // Agregar paneles al contenedor
        panelContenedor.add(panelRegistros, "REGISTROS");
        panelContenedor.add(panelResidentes.getPanelResidente(), "RESIDENTES");

        // Configurar botón regresar
        crearBotonRegresar(mainPanel);

        mainPanel.add(panelContenedor, BorderLayout.CENTER);
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

                // Sombra suave a la derecha
                g2.setColor(new Color(60, 60, 100, 40));
                g2.fillRect(getWidth() - 8, 0, 8, getHeight());
            }
        };
        barraLateral.setPreferredSize(new Dimension(100, 0));

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0; gbcL.gridy = 0;
        gbcL.insets = new Insets(24, 0, 18, 0);
        gbcL.anchor = GridBagConstraints.NORTH;

        // Icono dinámico
        iconoBarra = new JLabel("G", SwingConstants.CENTER);
        iconoBarra.setFont(new Font("Segoe UI", Font.BOLD, 48));
        iconoBarra.setForeground(Color.WHITE);
        barraLateral.add(iconoBarra, gbcL);

        // Título vertical dinámico
        gbcL.gridy++;
        gbcL.insets = new Insets(0, 0, 0, 0);
        tituloBarra = new JLabel("<html><div style='text-align: center; line-height: 1.5;'>" +
                "E<br>S<br>T<br>I<br>Ó<br>N<br><br>" +
                "D<br>E<br><br>" +
                "C<br>A<br>N<br>D<br>I<br>D<br>A<br>T<br>O<br>S</div></html>");
        tituloBarra.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloBarra.setForeground(new Color(245, 243, 255, 200));
        tituloBarra.setHorizontalAlignment(SwingConstants.CENTER);
        barraLateral.add(tituloBarra, gbcL);

        // Botón de ayuda
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

    private void crearPanelRegistros() {
        panelRegistros = new JPanel(new BorderLayout());
        panelRegistros.setOpaque(false);

        // Header
        JPanel header = crearHeader();
        panelRegistros.add(header, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 38, 20, 38));

        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // Configurar tabla
        configurarTabla();
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        panelRegistros.add(panelCentral, BorderLayout.CENTER);
    }

    private void crearPanelResidentes() {
        // MODIFICADO: Crear una instancia de VistaResidente y mantener referencia
        panelResidentes = new VistaResidente();
        // Ya no necesita setVisible porque ahora es solo un panel
    }

    private void crearBotonRegresar(JPanel mainPanel) {
        JButton btnRegresar = new JButton("\u2190");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRegresar.setForeground(colorPrincipal);
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> regresarAVistaRegistros());

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTop.setOpaque(false);
        panelTop.add(btnRegresar);
        mainPanel.add(panelTop, BorderLayout.NORTH);
    }

    // ==================== MÉTODOS DE NAVEGACIÓN ====================

    private void cambiarAVistaResidentes() {
        vistaActual = "RESIDENTES";
        cardLayout.show(panelContenedor, "RESIDENTES");
        actualizarBarraLateral();

        // Enfocar el panel de residentes para mejor experiencia
        panelResidentes.getPanelResidente().requestFocusInWindow();
    }

    private void regresarAVistaRegistros() {
        if (vistaActual.equals("RESIDENTES")) {
            // MODIFICADO: Limpiar la tabla de residentes al regresar
            if (panelResidentes != null) {
                panelResidentes.limpiarTabla();
            }

            vistaActual = "REGISTROS";
            cardLayout.show(panelContenedor, "REGISTROS");
            actualizarBarraLateral();

            // Recargar datos de registros y enfocar
            controlador.cargarTodosLosRegistros();
            panelRegistros.requestFocusInWindow();
        }  else {
            Menu menu =  new Menu();
            menu.setVisible(true);
            this.dispose();
        }
    }

    private void actualizarBarraLateral() {
        if (vistaActual.equals("REGISTROS")) {
            iconoBarra.setText("G");
            tituloBarra.setText("<html><div style='text-align: center; line-height: 1.5;'>" +
                    "G<br>E<br>S<br>T<br>I<br>Ó<br>N<br><br>" +
                    "D<br>E<br><br>" +
                    "C<br>A<br>N<br>D<br>I<br>D<br>A<br>T<br>O<br>S</div></html>");
        } else {
            iconoBarra.setText("G");
            tituloBarra.setText("<html><div style='text-align: center; line-height: 1.5;'>" +
                    "G<br>E<br>S<br>T<br>I<br>Ó<br>N<br><br>" +
                    "D<br>E<br><br>" +
                    "R<br>E<br>S<br>I<br>D<br>E<br>N<br>T<br>E<br>S</div></html>");
        }

        // Forzar repintado de la barra lateral
        iconoBarra.repaint();
        tituloBarra.repaint();
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
        header.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 0));

        JLabel lblTitulo = new JLabel("Registro De Candidatos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        header.add(lblTitulo, BorderLayout.WEST);

        // Botón Nuevo Alumno en el header
        JPanel panelBotonHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelBotonHeader.setOpaque(false);

        btnNuevoAlumno = crearBotonAccion("Nuevo Alumno", new Color(34, 139, 34));
        btnNuevoAlumno.addActionListener(e -> cambiarAVistaResidentes());
        panelBotonHeader.add(btnNuevoAlumno);

        header.add(panelBotonHeader, BorderLayout.EAST);
        return header;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.setOpaque(false);
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Panel izquierdo para búsqueda
        JPanel panelBusquedaCompleto = new JPanel();
        panelBusquedaCompleto.setLayout(new BoxLayout(panelBusquedaCompleto, BoxLayout.Y_AXIS));
        panelBusquedaCompleto.setOpaque(false);
        
        // Label de búsqueda
        lblBuscar = new JLabel("Buscar alumno:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBuscar.setForeground(colorPrincipal);
        lblBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBuscar.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Campo de búsqueda con esquinas redondeadas
        textField1 = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                super.paintComponent(g);
            }
        };
        textField1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField1.setBackground(Color.WHITE);
        textField1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        textField1.setPreferredSize(new Dimension(400, 40));
        textField1.setMaximumSize(new Dimension(400, 40));
        textField1.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel horizontal para campo de búsqueda y botones
        JPanel panelBusquedaHorizontal = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBusquedaHorizontal.setOpaque(false);
        panelBusquedaHorizontal.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnActualizar = crearBotonBusqueda("Buscar", "🔍");
        btnLimpiarBusqueda = crearBotonBusqueda("Limpiar", "✖");

        // Añadir campo y botones al panel horizontal
        panelBusquedaHorizontal.add(textField1);
        panelBusquedaHorizontal.add(btnActualizar);
        panelBusquedaHorizontal.add(btnLimpiarBusqueda);

        // Crear y añadir botón actualizar
        btnActualizar = crearBotonBusqueda("Actualizar", "🔄");
        panelBusquedaHorizontal.add(Box.createHorizontalStrut(10));
        panelBusquedaHorizontal.add(btnActualizar);

        // Label de resultados
        lblResultados = new JLabel("Total de registros: 0");
        lblResultados.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblResultados.setForeground(new Color(100, 100, 100));
        lblResultados.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        lblResultados.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agregar componentes al panel principal
        panelBusquedaCompleto.add(lblBuscar);
        panelBusquedaCompleto.add(panelBusquedaHorizontal);
        panelBusquedaCompleto.add(lblResultados);

        // Agregar el panel completo al contenedor principal
        panelBusqueda.add(panelBusquedaCompleto, BorderLayout.WEST);

        return panelBusqueda;
    }

    private void configurarTabla() {
        // MODIFICADO: Definir columnas SIN CARRERA E ID PROYECTO
        String[] columnas = {
                "No. Control", "Nombre", "Apellido P.", "Apellido M.",
                "Semestre", "Correo", "Telefono"
        };

        // Crear modelo de tabla
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // MODIFICADO: Ajustar índices - ahora Semestre es columna 4
                if (columnIndex == 0 || columnIndex == 4) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        // Crear tabla
        candidatos = new JTable(modeloTabla) {
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

        candidatos.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        candidatos.setRowHeight(35);
        candidatos.setShowVerticalLines(false);
        candidatos.setShowHorizontalLines(true);
        candidatos.setGridColor(new Color(230, 230, 250));

        // Configurar header
        candidatos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        candidatos.getTableHeader().setBackground(new Color(220, 219, 245));
        candidatos.getTableHeader().setForeground(colorPrincipal);
        candidatos.getTableHeader().setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) candidatos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // *** FIX: Configurar alineación de celdas hacia la derecha ***
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Aplicar renderizadores por columna
        candidatos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // No. Control - centrado
        candidatos.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);  // Nombre - derecha
        candidatos.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Apellido P. - derecha
        candidatos.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Apellido M. - derecha
        candidatos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Semestre - centrado
        candidatos.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);  // Correo - derecha
        candidatos.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Telefono - centrado

        // Configurar anchos de columna
        configurarAnchoColumnas();

        // Configurar sorter
        sorter = new TableRowSorter<>(modeloTabla);
        candidatos.setRowSorter(sorter);

        // Scroll personalizado
        scrollPane = new JScrollPane(candidatos);
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
        // MODIFICADO: Configurar anchos sin carrera e ID proyecto - más espacio para cada columna
        candidatos.getColumnModel().getColumn(0).setPreferredWidth(120); // No. Control
        candidatos.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        candidatos.getColumnModel().getColumn(2).setPreferredWidth(150); // Apellido P.
        candidatos.getColumnModel().getColumn(3).setPreferredWidth(150); // Apellido M.
        candidatos.getColumnModel().getColumn(4).setPreferredWidth(100); // Semestre
        candidatos.getColumnModel().getColumn(5).setPreferredWidth(250); // Correo
        candidatos.getColumnModel().getColumn(6).setPreferredWidth(150); // Telefono
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelBotones.setOpaque(false);

        // Botón Editar - Mantiene su funcionalidad existente
        editar = new JButton("Editar") {
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

        // Botón Dar de Baja - Mantiene su funcionalidad existente
        darDeBaja = new JButton("Dar de baja") {
            private boolean hover = false;
            private final Color colorBase = new Color(200, 60, 60);
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

        panelBotones.add(editar);
        panelBotones.add(darDeBaja);

        return panelBotones;
    }

    private JButton crearBotonAccion(String texto, Color color) {
        return new JButton(texto) {
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
                        if (isEnabled()) { hover = true; repaint(); }
                    }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color baseColor = isEnabled() ? color : new Color(180, 180, 180);

                // Sombra
                g2.setColor(new Color(60,60,100,60));
                g2.fillRoundRect(4, 6, getWidth()-8, getHeight()-4, 30, 30);

                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover && isEnabled() ? baseColor.darker() : baseColor,
                        getWidth(), getHeight(), baseColor.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
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

    private void configurarCampoBusqueda() {
        // Placeholder effect
        textField1.setForeground(Color.GRAY);
        textField1.setText("Buscar por nombre o numero de control...");

        textField1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (textField1.getText().equals("Buscar por nombre o numero de control...")) {
                    textField1.setText("");
                    textField1.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (textField1.getText().isEmpty()) {
                    textField1.setForeground(Color.GRAY);
                    textField1.setText("Buscar por nombre o numero de control...");
                }
            }
        });

        // Efectos de foco
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField1.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorPrincipal.darker(), 3, true),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField1.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorPrincipal, 2, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    private void configurarEventos() {
        // Búsqueda en tiempo real
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarDatos();
            }
        });

        // *** CAMBIO: Eventos de botones actualizados ***
        darDeBaja.addActionListener(e -> controlador.darDeBajaRegistroSeleccionado());
        editar.addActionListener(e -> controlador.editarRegistroSeleccionado());
        btnActualizar.addActionListener(e -> controlador.cargarTodosLosRegistros());
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());

        // Selección en tabla
        candidatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = candidatos.getSelectedRow() != -1;
                darDeBaja.setEnabled(haySeleccion); // *** CAMBIO: eliminar → darDeBaja ***
                editar.setEnabled(haySeleccion);
            }
        });

        // Tecla Escape para limpiar búsqueda
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        panelRegistros.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        panelRegistros.getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarBusqueda();
            }
        });
    }

    // ==================== MÉTODOS DE MANIPULACIÓN DE DATOS ====================

    public void cargarResidentes(List<ModeloResidente> residentes) {
        this.listaOriginal = residentes;
        modeloTabla.setRowCount(0);

        for (ModeloResidente residente : residentes) {
            // MODIFICADO: Fila sin carrera e ID proyecto
            Object[] fila = {
                    residente.getNumeroControl(),
                    residente.getNombre(),
                    residente.getApellidoPaterno(),
                    residente.getApellidoMaterno(),
                    // ELIMINADO: residente.getCarrera(),
                    residente.getSemestre(),
                    residente.getCorreo(),
                    residente.getTelefono()
                    // ELIMINADO: residente.getIdProyecto()
            };
            modeloTabla.addRow(fila);
        }

        actualizarContadorResultados();
    }

    private void filtrarDatos() {
        String textoBusqueda = textField1.getText().trim();

        if (textoBusqueda.isEmpty() || textoBusqueda.equals("Buscar por nombre o numero de control...")) {
            sorter.setRowFilter(null);
        } else {
            // MODIFICADO: Filtrar solo por No. Control (0), Nombre (1), Apellido P. (2), Apellido M. (3)
            RowFilter<DefaultTableModel, Object> filtro = RowFilter.regexFilter(
                    "(?i)" + textoBusqueda, 0, 1, 2, 3
            );
            sorter.setRowFilter(filtro);
        }

        actualizarContadorResultados();
    }

    private void limpiarBusqueda() {
        textField1.setText("");
        textField1.setForeground(Color.GRAY);
        textField1.setText("Buscar por nombre o numero de control...");
        sorter.setRowFilter(null);
        actualizarContadorResultados();
    }

    private void actualizarContadorResultados() {
        int totalRegistros = modeloTabla.getRowCount();
        int registrosFiltrados = candidatos.getRowCount();

        if (sorter.getRowFilter() == null) {
            lblResultados.setText("Total de registros: " + totalRegistros);
        } else {
            lblResultados.setText("Mostrando: " + registrosFiltrados + " de " + totalRegistros + " registros");
        }
    }

    private void cargarDatosIniciales() {
        if (controlador != null) {
            controlador.cargarTodosLosRegistros();
        }
    }

    private void mostrarAyuda() {
        String ayuda = vistaActual.equals("REGISTROS") ?
                "Sistema de Registros de Residentes SIREP\n\n" +
                        "• Busqueda: Escriba para filtrar por nombre o numero de control\n" +
                        "• Editar: Seleccione un registro y haga clic en Editar\n" +
                        "• Dar de baja: Seleccione un registro y haga clic en Dar de baja\n" +
                        "• Actualizar: Recarga todos los registros desde la base de datos\n" +
                        "• Limpiar: Borra el filtro de busqueda\n" +
                        "• Nuevo Alumno: Cambia a la vista para registrar nuevos alumnos\n" +
                        "• ESC: Atajo para limpiar la busqueda\n\n":

                "Sistema de Gestion de Residentes SIREP\n\n" +
                        "• Cargar Excel: Carga residentes desde archivo Excel\n" +
                        "• Importar: Guarda los datos cargados en la base de datos\n" +
                        "• Agregar Manual: Agrega un residente manualmente\n" +
                        "• Limpiar Tabla: Limpia todos los datos de la tabla temporal\n" +
                        "• Flecha izquierda: Regresa a la vista de registros\n\n";

        JOptionPane.showMessageDialog(this, ayuda, "Ayuda - Sistema SIREP", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== MÉTODOS PARA EL CONTROLADOR ====================

    public ModeloResidente getResidenteSeleccionado() {
        int filaSeleccionada = candidatos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int filaModelo = candidatos.convertRowIndexToModel(filaSeleccionada);
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
        controlador.cargarTodosLosRegistros();
    }

    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
    }

    // ==================== GETTERS ====================

    public JPanel getPanelPrincipal() {
        return panelRegistros; // Retorna el panel de registros
    }

    public JTable getCandidatos() {
        return candidatos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public String getTextoBusqueda() {
        String texto = textField1.getText().trim();
        return texto.equals("Buscar por nombre o numero de control...") ? "" : texto;
    }

    // ==================== MÉTODOS PARA CONTROLADOR RESIDENTE ====================

    // Estos métodos permiten que el ControladorResidente funcione con esta vista unificada
    public JPanel getPanelResidente() {
        return panelResidentes.getPanelResidente();
    }

    public JTable getTablaResidentes() {
        return panelResidentes.getTablaResidentes();
    }

    public DefaultTableModel getModeloTablaResidentes() {
        return panelResidentes.getModeloTabla();
    }

    public List<ModeloResidente> getListaResidentes() {
        return panelResidentes.getListaResidentes();
    }

    public ModeloResidente getResidenteSeleccionadoResidentes() {
        return panelResidentes.getResidenteSeleccionado();
    }

    public void agregarResidente(ModeloResidente residente) {
        panelResidentes.agregarResidente(residente);
    }

    public void cargarResidentesEnTablaResidentes(List<ModeloResidente> residentes) {
        panelResidentes.cargarResidentes(residentes);
    }

    public void limpiarTablaResidentes() {
        panelResidentes.limpiarTabla();
    }

    // AGREGADO: Método para acceder a la vista de residentes
    public VistaResidente getVistaResidente() {
        return panelResidentes;
    }

    // *** NUEVO: Método para abrir diálogo de edición desde el controlador ***
    public void abrirDialogoEdicion(ModeloResidente residente) {
        try {
            // Crear diálogo de edición
            DialogoEdicion dialogo = new DialogoEdicion(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    residente
            );

            dialogo.setVisible(true);

            // Si se confirmó la edición, actualizar tabla
            if (dialogo.isGuardado()) {
                actualizarTabla();
            }

        } catch (Exception e) {
            mostrarMensaje(
                    "❌ Error al abrir el editor:\n" + e.getMessage(),
                    "Error de edición",
                    JOptionPane.ERROR_MESSAGE
            );
            System.err.println("Error al editar registro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaRegistros().setVisible(true));
    }
}

// ==================== CLASE DIÁLOGO DE EDICIÓN ACTUALIZADA ====================

class DialogoEdicion extends JDialog {
    // *** CAMBIO: Quitar campo ID Proyecto ***
    private JTextField txtNumeroControl;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCarrera;
    private JSpinner spnSemestre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    // *** ELIMINADO: private JSpinner spnIdProyecto; ***
    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean guardado = false;
    private ModeloResidente residente;

    public boolean isGuardado() {
        return guardado;
    }

    public DialogoEdicion(Frame parent, ModeloResidente residente) {
        super(parent, "✏️ Editar Residente", true);
        this.residente = residente;

        initComponents();
        cargarDatos();
        setupLayout();
        setupEvents();

        // *** FIX: Tamaño original con scroll ***
        setSize(450, 400); // Tamaño original
        setLocationRelativeTo(parent);
        setResizable(true);
        setMinimumSize(new Dimension(450, 400));
    }

    private void initComponents() {
        // *** FIX: Campo numero_control igual que los demás ***
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
        // *** ELIMINADO: spnIdProyecto ***

        btnGuardar = new JButton("💾 Guardar Cambios");
        btnCancelar = new JButton("❌ Cancelar");

        // Configurar colores
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
        // *** ELIMINADO: spnIdProyecto.setValue(residente.getIdProyecto()); ***
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel principal con campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Título
        JLabel lblTitulo = new JLabel("✏️ Editar Información del Residente");
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // *** FIX: Sin etiqueta especial, igual que los demás y sin ID Proyecto ***
        agregarCampo(panelCampos, "* Número de Control:", txtNumeroControl, gbc, 0);
        agregarCampo(panelCampos, "* Nombre:", txtNombre, gbc, 1);
        agregarCampo(panelCampos, "* Apellido Paterno:", txtApellidoPaterno, gbc, 2);
        agregarCampo(panelCampos, "Apellido Materno:", txtApellidoMaterno, gbc, 3);
        agregarCampo(panelCampos, "* Carrera:", txtCarrera, gbc, 4);
        agregarCampo(panelCampos, "* Semestre:", spnSemestre, gbc, 5);
        agregarCampo(panelCampos, "* Correo:", txtCorreo, gbc, 6);
        agregarCampo(panelCampos, "Teléfono:", txtTelefono, gbc, 7);
        // *** ELIMINADO: agregarCampo(panelCampos, "ID Proyecto:", spnIdProyecto, gbc, 8); ***

        // *** FIX: Scroll para los campos ***
        JScrollPane scrollCampos = new JScrollPane(panelCampos);
        scrollCampos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCampos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCampos.setBorder(BorderFactory.createEmptyBorder());

        // Nota sobre campos obligatorios
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Panel superior con título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        // Panel inferior con nota y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblNota, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar todo al diálogo
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollCampos, BorderLayout.CENTER); // *** FIX: Scroll en el centro ***
        add(panelInferior, BorderLayout.SOUTH);

        // Borde general
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo, GridBagConstraints gbc, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JLabel label = new JLabel(etiqueta);
        if (etiqueta.startsWith("*")) {
            label.setForeground(new Color(211, 47, 47)); // Rojo para obligatorios
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

        // *** FIX: Asegurar que txtNumeroControl sea focusable ***
        txtNumeroControl.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                txtNumeroControl.selectAll(); // Seleccionar todo al enfocar
                System.out.println("DEBUG: Campo número de control enfocado");
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                System.out.println("DEBUG: Campo número de control perdió el foco. Valor: " + txtNumeroControl.getText());
            }
        });

        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);

        // Escape para cancelar
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
        // Validar campos obligatorios
        if (!validarCampos()) {
            return;
        }

        // Confirmar guardado
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de guardar los cambios?\n" +
                        "Nombre: " + txtNombre.getText().trim() + " " + txtApellidoPaterno.getText().trim(),
                "Confirmar cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                // Cambiar cursor a espera
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // *** CAMBIO: Actualizar datos del residente SIN id_proyecto ***
                residente.setNumeroControl(Integer.parseInt(txtNumeroControl.getText().trim()));
                residente.setNombre(txtNombre.getText().trim());
                residente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
                residente.setApellidoMaterno(txtApellidoMaterno.getText().trim().isEmpty() ? null : txtApellidoMaterno.getText().trim());
                residente.setCarrera(txtCarrera.getText().trim());
                residente.setSemestre((Integer) spnSemestre.getValue());
                residente.setCorreo(txtCorreo.getText().trim());
                residente.setTelefono(txtTelefono.getText().trim().isEmpty() ? null : txtTelefono.getText().trim());
                // *** ELIMINADO: residente.setIdProyecto((Integer) spnIdProyecto.getValue()); ***

                // Guardar en la base de datos
                boolean actualizado = residente.actualizar();

                if (actualizado) {
                    guardado = true;
                    JOptionPane.showMessageDialog(this,
                            "✅ Cambios guardados exitosamente\n" +
                                    "📝 Número de Control: " + residente.getNumeroControl(),
                            "Actualización exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "❌ No se pudieron guardar los cambios\n" +
                                    "Verifique que el registro aún existe en la base de datos",
                            "Error de actualización",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "❌ Error al guardar los cambios:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("Error al actualizar residente: " + e.getMessage());
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    private boolean validarCampos() {
        // *** CAMBIO: Validar número de control ***
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

        // Validar nombre
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio", txtNombre);
            return false;
        }

        if (txtNombre.getText().trim().length() < 2) {
            mostrarError("El nombre debe tener al menos 2 caracteres", txtNombre);
            return false;
        }

        // Validar apellido paterno
        if (txtApellidoPaterno.getText().trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio", txtApellidoPaterno);
            return false;
        }

        if (txtApellidoPaterno.getText().trim().length() < 2) {
            mostrarError("El apellido paterno debe tener al menos 2 caracteres", txtApellidoPaterno);
            return false;
        }

        // Validar carrera
        if (txtCarrera.getText().trim().isEmpty()) {
            mostrarError("La carrera es obligatoria", txtCarrera);
            return false;
        }

        // Validar correo
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            mostrarError("El correo es obligatorio", txtCorreo);
            return false;
        }

        if (!validarFormatoCorreo(correo)) {
            mostrarError("El formato del correo no es válido\nEjemplo: usuario@dominio.com", txtCorreo);
            return false;
        }

        // Validar teléfono (opcional, pero si se proporciona debe ser válido)
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
            // Aquí puedes agregar la lógica para confirmar la cancelación si hay cambios
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

    // Comprueba si algún campo fue modificado respecto al residente original
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

// Clase para crear bordes redondeados (mover fuera de DialogoEdicion)
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