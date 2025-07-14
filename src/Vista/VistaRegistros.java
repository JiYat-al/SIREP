package Vista;

import Modelo.ModeloResidente;
import Controlador.ControladorRegistros;
import Vista.VistaResidentes.VistaResidente;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VistaRegistros extends JFrame {
    // Paneles principales para intercambio
    private JPanel panelContenedor;
    private JPanel panelRegistros;
    private VistaResidente panelResidentes;
    private CardLayout cardLayout;

    // Vista actual
    private String vistaActual = "REGISTROS";

    // Componentes principales
    private JTable candidatos;
    private JButton eliminar;
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
    private final Color colorFondo = new Color(245, 243, 255);

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
        iconoBarra = new JLabel("R", SwingConstants.CENTER);
        iconoBarra.setFont(new Font("Segoe UI", Font.BOLD, 48));
        iconoBarra.setForeground(Color.WHITE);
        barraLateral.add(iconoBarra, gbcL);

        // Título vertical dinámico
        gbcL.gridy++;
        gbcL.insets = new Insets(0, 0, 0, 0);
        tituloBarra = new JLabel("<html>R<br>E<br>G<br>I<br>S<br>T<br>R<br>O<br>S</html>");
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
        // Crear una instancia de VistaResidente pero sin usar su JFrame
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
            vistaActual = "REGISTROS";
            cardLayout.show(panelContenedor, "REGISTROS");
            actualizarBarraLateral();

            // Recargar datos de registros y enfocar
            controlador.cargarTodosLosRegistros();
            panelRegistros.requestFocusInWindow();
        } else {
            Menu menu = new Menu();
            menu.setVisible(true);
            this.dispose();
        }
    }

    private void actualizarBarraLateral() {
        if (vistaActual.equals("REGISTROS")) {
            iconoBarra.setText("R"); // Cambiado para mejor distinción visual
            tituloBarra.setText("<html>E<br>G<br>I<br>S<br>T<br>R<br>O<br>S</html>");
        } else {
            iconoBarra.setText("R"); // Icono de casa para residentes
            tituloBarra.setText("<html>E<br>S<br>I<br>D<br>E<br>N<br>T<br>E<br>S</html>");
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

        JLabel lblTitulo = new JLabel("Registro");
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

        // Panel izquierdo con búsqueda
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);

        lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBuscar.setForeground(colorPrincipal);
        lblBuscar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // Campo de búsqueda estilizado
        textField1 = new JTextField(25);
        textField1.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textField1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        configurarCampoBusqueda();

        // Botón limpiar búsqueda
        btnLimpiarBusqueda = crearBotonAccion("Limpiar", new Color(158, 158, 158));

        panelIzquierdo.add(lblBuscar);
        panelIzquierdo.add(textField1);
        panelIzquierdo.add(Box.createHorizontalStrut(15));
        panelIzquierdo.add(btnLimpiarBusqueda);

        // Panel derecho con contador y actualizar
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelDerecho.setOpaque(false);

        lblResultados = new JLabel("Total de registros: 0");
        lblResultados.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblResultados.setForeground(new Color(100, 100, 100));

        btnActualizar = crearBotonAccion("Actualizar", new Color(46, 125, 50));

        panelDerecho.add(lblResultados);
        panelDerecho.add(Box.createHorizontalStrut(20));
        panelDerecho.add(btnActualizar);

        panelBusqueda.add(panelIzquierdo, BorderLayout.WEST);
        panelBusqueda.add(panelDerecho, BorderLayout.EAST);

        return panelBusqueda;
    }

    private void configurarTabla() {
        // Definir columnas (SIN columna de acciones)
        String[] columnas = {
                "No. Control", "Nombre", "Apellido P.", "Apellido M.",
                "Carrera", "Semestre", "Correo", "Telefono", "ID Proyecto"
        };

        // Crear modelo de tabla
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5 || columnIndex == 8) {
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
        ((DefaultTableCellRenderer) candidatos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

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
        candidatos.getColumnModel().getColumn(0).setPreferredWidth(100); // No. Control
        candidatos.getColumnModel().getColumn(1).setPreferredWidth(120); // Nombre
        candidatos.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellido P.
        candidatos.getColumnModel().getColumn(3).setPreferredWidth(120); // Apellido M.
        candidatos.getColumnModel().getColumn(4).setPreferredWidth(180); // Carrera
        candidatos.getColumnModel().getColumn(5).setPreferredWidth(80);  // Semestre
        candidatos.getColumnModel().getColumn(6).setPreferredWidth(200); // Correo
        candidatos.getColumnModel().getColumn(7).setPreferredWidth(120); // Telefono
        candidatos.getColumnModel().getColumn(8).setPreferredWidth(100); // ID Proyecto
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel panelBotonesIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBotonesIzq.setOpaque(false);

        // Botones de acción
        editar = crearBotonAccion("Editar", new Color(25, 118, 210));
        eliminar = crearBotonAccion("Eliminar", new Color(211, 47, 47));

        // Deshabilitar hasta que se seleccione
        editar.setEnabled(false);
        eliminar.setEnabled(false);

        panelBotonesIzq.add(editar);
        panelBotonesIzq.add(Box.createHorizontalStrut(15));
        panelBotonesIzq.add(eliminar);

        panelBotones.add(panelBotonesIzq, BorderLayout.WEST);

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

        // Eventos de botones
        eliminar.addActionListener(e -> controlador.eliminarRegistroSeleccionado());
        editar.addActionListener(e -> controlador.editarRegistroSeleccionado());
        btnActualizar.addActionListener(e -> controlador.cargarTodosLosRegistros());
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());

        // Selección en tabla
        candidatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = candidatos.getSelectedRow() != -1;
                eliminar.setEnabled(haySeleccion);
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
            Object[] fila = {
                    residente.getNumeroControl(),
                    residente.getNombre(),
                    residente.getApellidoPaterno(),
                    residente.getApellidoMaterno(),
                    residente.getCarrera(),
                    residente.getSemestre(),
                    residente.getCorreo(),
                    residente.getTelefono(),
                    residente.getIdProyecto()
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
                        "• Eliminar: Seleccione un registro y haga clic en Eliminar\n" +
                        "• Actualizar: Recarga todos los registros desde la base de datos\n" +
                        "• Limpiar: Borra el filtro de busqueda\n" +
                        "• Nuevo Alumno: Cambia a la vista para registrar nuevos alumnos\n" +
                        "• ESC: Atajo para limpiar la busqueda" :

                "Sistema de Gestion de Residentes SIREP\n\n" +
                        "• Cargar Excel: Carga residentes desde archivo Excel\n" +
                        "• Importar Excel: Guarda los datos cargados en la base de datos\n" +
                        "• Agregar Manual: Agrega un residente manualmente\n" +
                        "• Flecha izquierda: Regresa a la vista de registros\n" +
                        "• La tabla muestra todos los residentes activos";

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaRegistros().setVisible(true));
    }
}