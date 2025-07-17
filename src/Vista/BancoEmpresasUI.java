package Vista;

import Controlador.CtrlEmpresa;
import Modelo.Empresa;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

// Renamed to avoid duplicate class definition
public class BancoEmpresasUI extends JFrame {
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private ArrayList<Modelo.Empresa> listEmpresas = new ArrayList<>();

    // Componentes principales
    private JTextField textField1;
    private JScrollPane scrollPane;
    private JLabel lblBuscar;
    private JLabel lblResultados;
    private JButton btnActualizar;
    private JButton btnLimpiarBusqueda;
    private JButton btnNuevoEmpresa;

    // Modelo de la tabla y filtros
    private TableRowSorter<DefaultTableModel> sorter;

    public BancoEmpresasUI() {
        setTitle("Sistema SIREP - Gestión de Empresas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        configurarInterfaz();
        configurarEventos();
        cargarDatosIniciales();
    }

    private void configurarInterfaz() {
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

        // Panel contenido principal
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);

        // Header
        JPanel header = crearHeader();
        panelContenido.add(header, BorderLayout.NORTH);

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

        panelContenido.add(panelCentral, BorderLayout.CENTER);

        // Configurar botón regresar
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
        JLabel icono = new JLabel("\uD83C\uDFE2", SwingConstants.CENTER); // Edificio emoji
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icono.setForeground(Color.WHITE);
        barraLateral.add(icono, gbcL);

        gbcL.gridy++;
        gbcL.insets = new Insets(0, 0, 0, 0);
        JLabel verticalTitle = new JLabel("<html>E<br>M<br>P<br>R<br>E<br>S<br>A<br>S</html>");
        verticalTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        verticalTitle.setForeground(new Color(245, 243, 255, 200));
        verticalTitle.setHorizontalAlignment(SwingConstants.CENTER);
        barraLateral.add(verticalTitle, gbcL);

        // Botón de ayuda abajo
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
        btnAyuda.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí puedes registrar, editar y eliminar empresas.\n" +
                            "Usa el botón 'Registrar nueva empresa' para agregar una nueva.\n" +
                            "Haz clic en 'Editar' para modificar una empresa existente.",
                    "Ayuda", JOptionPane.INFORMATION_MESSAGE);
        });
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
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel lblTitulo = new JLabel("Banco de Empresas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 0));
        header.add(lblTitulo, BorderLayout.WEST);

        // Botón Registrar nuevo
        btnNuevoEmpresa = new JButton("Registrar nueva empresa") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 17));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(13, 34, 13, 34));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(60, 60, 100, 60));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 4, 40, 40);
                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover ? colorPrincipal.darker() : colorPrincipal,
                        getWidth(), getHeight(), colorPrincipal.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 38, 20));
        btnPanel.setOpaque(false);
        btnPanel.add(btnNuevoEmpresa);
        header.add(btnPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new BoxLayout(panelBusqueda, BoxLayout.Y_AXIS));
        panelBusqueda.setOpaque(false);
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        lblBuscar = new JLabel("Buscar empresa:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBuscar.setForeground(colorPrincipal);
        lblBuscar.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        textField1 = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        textField1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField1.setBackground(Color.WHITE);
        textField1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        textField1.setPreferredSize(new Dimension(300, 40));

        // Panel para el campo de búsqueda y botones
        JPanel panelCampo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelCampo.setOpaque(false);
        panelCampo.add(textField1);

        btnActualizar = crearBotonBusqueda("Buscar", "🔍");
        btnLimpiarBusqueda = crearBotonBusqueda("Limpiar", "✖");

        panelCampo.add(Box.createHorizontalStrut(10));
        panelCampo.add(btnActualizar);
        panelCampo.add(Box.createHorizontalStrut(10));
        panelCampo.add(btnLimpiarBusqueda);

        // Label de resultados
        lblResultados = new JLabel("Mostrando todas las empresas");
        lblResultados.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblResultados.setForeground(new Color(100, 100, 100));
        lblResultados.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(panelCampo);
        panelBusqueda.add(lblResultados);

        return panelBusqueda;
    }

    private JButton crearBotonBusqueda(String texto, String icono) {
        JButton boton = new JButton(icono + " " + texto) {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(colorPrincipal);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hover) {
                    g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
                g2.setColor(colorPrincipal);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        return boton;
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "RFC", "Nombre", "Dirección", "Responsable", "Teléfono", "Correo electrónico"};
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        tabla.setRowHeight(36);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(new Color(230, 230, 250));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 17));
        tabla.getTableHeader().setBackground(new Color(220, 219, 245));
        tabla.getTableHeader().setForeground(colorPrincipal);
        ((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Configurar filtro de búsqueda
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = colorPrincipal;
                this.trackColor = new Color(235, 235, 250);
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
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

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelBotones.setOpaque(false);

        // Botón Editar
        JButton btnEditar = new JButton("Editar") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }


            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(60, 60, 100, 60));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 4, 20, 20);
                // Gradiente
                GradientPaint grad = new GradientPaint(
                        0, 0,
                        hover ? colorPrincipal.darker() : colorPrincipal,
                        getWidth(), getHeight(),
                        hover ? colorPrincipal : colorPrincipal.brighter()
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        btnEditar.addActionListener(e -> {
            int selectedRow = tabla.getSelectedRow();
            if (selectedRow >= 0) {
                // Convertir el índice de vista a modelo (por el uso de sorter)
                int modelRow = tabla.convertRowIndexToModel(selectedRow);
                editarEmpresa(modelRow);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una empresa para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });


        // Botón Dar de baja
        JButton btnDarBaja = new JButton("Dar de baja") {
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
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(100, 40, 40, 60));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 4, 20, 20);
                // Gradiente
                GradientPaint grad = new GradientPaint(
                        0, 0,
                        hover ? colorBase.darker() : colorBase,
                        getWidth(), getHeight(),
                        hover ? colorBase : new Color(220, 80, 80)
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        btnDarBaja.addActionListener(e -> {
            int selectedRow = tabla.getSelectedRow();
            if (selectedRow >= 0) {
                int modelRow = tabla.convertRowIndexToModel(selectedRow);
                Empresa empresa = listEmpresas.get(modelRow);

                int opcion = JOptionPane.showConfirmDialog(
                        this,
                        "¿Estás seguro de dar de baja a la empresa: " + empresa.getNombre() + "?",
                        "Confirmar acción",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    boolean eliminado = CtrlEmpresa.cambiarEstatus(empresa.getId());
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Empresa dada de baja correctamente.");
                        cargarTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al dar de baja la empresa.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una empresa para dar de baja.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });


        panelBotones.add(btnEditar);
        panelBotones.add(btnDarBaja);

        return panelBotones;
    }

    private void crearBotonRegresar(JPanel mainPanel) {
        JButton btnRegresar = new JButton("←");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRegresar.setForeground(colorPrincipal);
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> {
            dispose();
        });

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTop.setOpaque(false);
        panelTop.add(btnRegresar);

        mainPanel.add(panelTop, BorderLayout.NORTH);
    }

    private void configurarEventos() {
        // Evento del botón Registrar nueva empresa
        btnNuevoEmpresa.addActionListener(e -> {
            RegistrarEmpresaDialog dialog = new RegistrarEmpresaDialog(this, colorPrincipal, null);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                cargarTabla();
            }
        });

        // Evento del botón buscar
        btnActualizar.addActionListener(e -> buscarEmpresa());

        // Evento del botón limpiar búsqueda
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());

        // Evento de búsqueda en tiempo real
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarEmpresa();
            }
        });
    }

    private void buscarEmpresa() {
        try {
            String texto = textField1.getText().trim();
            if (texto.isEmpty()) {
                sorter.setRowFilter(null);
                lblResultados.setText("Mostrando todas las empresas");
            } else {
                // Crear un filtro que busque en todas las columnas excepto en la última (botones)
                RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                        for (int i = 0; i < entry.getValueCount() - 1; i++) {
                            if (entry.getStringValue(i).toLowerCase().contains(texto.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    }
                };
                sorter.setRowFilter(filter);
                int resultados = tabla.getRowCount();
                lblResultados.setText("Mostrando " + resultados + " resultado(s) para: \"" + texto + "\"");
            }
        } catch (Exception ex) {
            sorter.setRowFilter(null);
            JOptionPane.showMessageDialog(this,
                    "Error en la búsqueda: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarBusqueda() {
        textField1.setText("");
        sorter.setRowFilter(null);
        lblResultados.setText("Mostrando todas las empresas");
    }

    private void cargarDatosIniciales() {
        cargarTabla();
    }

    private void cargarTabla() {
        try {
            listEmpresas = CtrlEmpresa.obtenerEmpresas();
            modelo.setRowCount(0);
            if (listEmpresas != null) {
                for (Modelo.Empresa e : listEmpresas) {
                    if (e != null) {
                        modelo.addRow(new Object[]{
                                e.getId(), e.getRfc(), e.getNombre(), e.getDireccion(), e.getResponsable(), e.getTelefono(), e.getCorreo()
                        });
                    }
                }
            }
            lblResultados.setText("Mostrando " + modelo.getRowCount() + " empresas");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para editar empresa
    private void editarEmpresa(int row) {
        if (row >= 0 && row < listEmpresas.size()) {
            Empresa empresa = listEmpresas.get(row);
            RegistrarEmpresaDialog dialog = new RegistrarEmpresaDialog(this, colorPrincipal, empresa);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                cargarTabla();
            }
        }
    }

    // Botón Editar y Eliminar en la tabla
    class BotonAccionesRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEditar;
        private final JButton btnEliminar;

        public BotonAccionesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0)); // Centrado y con espacio
            setOpaque(false);

            btnEditar = new JButton("Editar");
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEditar.setForeground(Color.WHITE); // Letras blancas
            btnEditar.setBackground(colorPrincipal);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditar.setOpaque(true);

            btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEliminar.setForeground(Color.WHITE); // Letras blancas
            btnEliminar.setBackground(new Color(200, 60, 60));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEliminar.setOpaque(true);

            add(btnEditar);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    class BotonAccionesEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton btnEditar;
        private final JButton btnEliminar;
        private int selectedRow;

        public BotonAccionesEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0)); // Centrado y con espacio
            panel.setOpaque(false);

            btnEditar = new JButton("Editar");
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setBackground(colorPrincipal);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditar.setOpaque(true);

            btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setBackground(new Color(200, 60, 60));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEliminar.setOpaque(true);

            btnEditar.addActionListener(e -> {
                BancoEmpresasUI.this.editarEmpresa(selectedRow);
            });

            btnEliminar.addActionListener(e -> {
                if (selectedRow >= 0 && selectedRow < listEmpresas.size()) {
                    Empresa empresa = listEmpresas.get(selectedRow);
                    int opcion = JOptionPane.showConfirmDialog(
                            null,
                            "¿Seguro que quieres eliminar la empresa: " + empresa.getNombre() + "?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (opcion == JOptionPane.YES_OPTION) {
                        boolean eliminado = CtrlEmpresa.cambiarEstatus(empresa.getId());
                        if (eliminado) {
                            JOptionPane.showMessageDialog(null, "Empresa eliminada correctamente.");
                            cargarTabla();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al eliminar empresa.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            panel.add(btnEditar);
            panel.add(btnEliminar);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            return panel;
        }
    }

    public static class RegistrarEmpresaDialog extends JDialog {
        public boolean seRegistro = false;
        public Empresa empresaEditada;

        public RegistrarEmpresaDialog(JFrame parent, Color colorPrincipal, Empresa empresa) {
            super(parent, (empresa == null ? "Registrar" : "Editar") + " empresa", true);
            setSize(480, 420); // Más compacto y proporcionado
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 8, 10, 8);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos
            JLabel lblRfc = labelField("RFC", colorPrincipal);
            JTextField txtRfc = new JTextField(20);

            JLabel lblNombre = labelField("Nombre", colorPrincipal);
            JTextField txtNombre = new JTextField(20);

            JLabel lblDireccion = labelField("Dirección", colorPrincipal);
            JTextField txtDireccion = new JTextField(20);

            JLabel lblResponsable = labelField("Responsable", colorPrincipal);
            JTextField txtResponsable = new JTextField(20);

            JLabel lblTelefono = labelField("Teléfono", colorPrincipal);
            JTextField txtTelefono = new JTextField(20);

            JLabel lblCorreo = labelField("Correo electrónico", colorPrincipal);
            JTextField txtCorreo = new JTextField(20);

            // Etiquetas columna 0
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(lblRfc, gbc);
            gbc.gridy++;
            panel.add(lblNombre, gbc);
            gbc.gridy++;
            panel.add(lblDireccion, gbc);
            gbc.gridy++;
            panel.add(lblResponsable, gbc);
            gbc.gridy++;
            panel.add(lblTelefono, gbc);
            gbc.gridy++;
            panel.add(lblCorreo, gbc);

// Campos columna 1
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
            panel.add(txtRfc, gbc);
            gbc.gridy++;
            panel.add(txtNombre, gbc);
            gbc.gridy++;
            panel.add(txtDireccion, gbc);
            gbc.gridy++;
            panel.add(txtResponsable, gbc);
            gbc.gridy++;
            panel.add(txtTelefono, gbc);
            gbc.gridy++;
            panel.add(txtCorreo, gbc);

            // Botón guardar centrado y ancho
            JButton btnGuardar = new JButton(empresa == null ? "Guardar" : "Actualizar");
            btnGuardar.setBackground(colorPrincipal);
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 17));
            btnGuardar.setBorder(BorderFactory.createEmptyBorder(12, 38, 12, 38));
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setOpaque(true);

            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(24, 8, 8, 8);
            panel.add(btnGuardar, gbc);

            add(panel, BorderLayout.CENTER);

            // Si es edición, llena los campos
            if (empresa != null) {
                txtRfc.setText(empresa.getRfc());
                txtNombre.setText(empresa.getNombre());
                txtDireccion.setText(empresa.getDireccion());
                txtResponsable.setText(empresa.getResponsable());
                txtTelefono.setText(empresa.getTelefono());
                txtCorreo.setText(empresa.getCorreo());
            }

            btnGuardar.addActionListener(ev -> {
                if (txtNombre.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNombre.requestFocus(); return;
                }
                if (txtDireccion.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtDireccion.requestFocus(); return;
                }
                if (txtResponsable.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El responsable no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtResponsable.requestFocus(); return;
                }
                if (txtTelefono.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El teléfono no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtTelefono.requestFocus(); return;
                }
                if (txtCorreo.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El correo no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtCorreo.requestFocus(); return;
                }

                if (!txtCorreo.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(this, "Ingresa un correo electrónico válido.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtCorreo.requestFocus(); return;
                }

                if (txtRfc.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El RFC no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtRfc.requestFocus(); return;
                }


                if (empresa == null) {
                    CtrlEmpresa.btnGuardar(txtNombre, txtDireccion, txtResponsable, txtTelefono, txtCorreo, txtRfc);
                } else {
                    CtrlEmpresa.editarEmpresa(empresa.getId(), txtNombre, txtDireccion, txtResponsable, txtTelefono, txtCorreo, txtRfc);
                }
                seRegistro = true;
                dispose();
            });
        }

        private static JLabel labelField(String texto, Color colorPrincipal) {
            JLabel l = new JLabel(texto);
            l.setFont(new Font("Segoe UI", Font.BOLD, 15));
            l.setForeground(colorPrincipal);
            return l;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BancoEmpresasUI ui = new BancoEmpresasUI();
            ui.setVisible(true);
        });
    }
}
