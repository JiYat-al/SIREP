package Vista;

import Controlador.ControladorDocente;
import Modelo.Docente;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

public class DocentesUI extends JFrame {
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private ArrayList<Docente> listaDocentes = new ArrayList<>();
    private ControladorDocente controlador = new ControladorDocente();
    private JButton btnEditar;
    private JButton btnDarBaja;


    // Componentes principales
    private JTextField textField1;
    private JScrollPane scrollPane;
    private JLabel lblBuscar;
    private JLabel lblResultados;
    private JButton btnActualizar;
    private JButton btnLimpiarBusqueda;
    private JButton btnNuevoDocente;

    // Modelo de la tabla y filtros
    private TableRowSorter<DefaultTableModel> sorter;

    public DocentesUI() {
        setTitle("Sistema SIREP - Gestión de Docentes");
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
        JLabel icono = new JLabel("👨‍🎓", SwingConstants.CENTER);
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icono.setForeground(Color.WHITE);
        barraLateral.add(icono, gbcL);

        gbcL.gridy++;
        gbcL.insets = new Insets(0, 0, 0, 0);
        JLabel verticalTitle = new JLabel("<html>D<br>O<br>C<br>E<br>N<br>T<br>E<br>S</html>");
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
                    "Aquí puedes registrar, editar y eliminar docentes.\n" +
                            "Usa el botón 'Registrar nuevo docente' para agregar uno nuevo.\n" +
                            "Haz clic en 'Editar' para modificar un docente existente.",
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

        JLabel lblTitulo = new JLabel("Banco de Docentes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 0));
        header.add(lblTitulo, BorderLayout.WEST);

        // Botón Registrar nuevo
        btnNuevoDocente = new JButton("Registrar nuevo docente") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 17));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(13, 34, 13, 34));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
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
        btnPanel.add(btnNuevoDocente);
        header.add(btnPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new BoxLayout(panelBusqueda, BoxLayout.Y_AXIS));
        panelBusqueda.setOpaque(false);
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        lblBuscar = new JLabel("Buscar docente:");
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
        lblResultados = new JLabel("Mostrando todos los docentes");
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
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
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
        String[] columnas = {"No. Tarjeta", "Nombre", "Correo"};
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
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

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // Botones con los colores exactos de empresas
        btnEditar = crearBotonAccion("Editar", new Color(108, 117, 202)); // Morado empresas
        btnDarBaja = crearBotonAccion("Dar de baja", new Color(220, 53, 69)); // Rojo empresas

        btnEditar.setEnabled(false);
        btnDarBaja.setEnabled(false);

        // Agregar listener para la selección de la tabla
        btnDarBaja.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                int modeloFila = tabla.convertRowIndexToModel(filaSeleccionada);
                int numeroTarjeta = (int) modelo.getValueAt(modeloFila, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de que deseas dar de baja a este docente?",
                        "Confirmar baja",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean exito = controlador.eliminarDocente(numeroTarjeta);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Docente dado de baja exitosamente.");
                        cargarDesdeBD();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo dar de baja al docente.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        // Agregar acción al botón editar
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                editarDocente(filaSeleccionada);
            }
        });

        // Agregar botones al panel
        panelBotones.add(btnEditar);
        panelBotones.add(btnDarBaja);

        return panelBotones;
    }

    private JButton crearBotonAccion(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                // Crear gradiente suave
                GradientPaint gp;
                if (isEnabled()) {
                    gp = new GradientPaint(
                            0, 0,
                            color,
                            0, height,
                            color.brighter()
                    );
                } else {
                    Color colorDeshabilitado = color;
                    gp = new GradientPaint(
                            0, 0,
                            colorDeshabilitado,
                            0, height,
                            colorDeshabilitado
                    );
                }

                // Pintar fondo con bordes redondeados
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, width, height, 8, 8);

                // Efecto de brillo superior sutil
                if (isEnabled()) {
                    g2d.setColor(new Color(255, 255, 255, 50));
                    g2d.fillRoundRect(0, 0, width, height/2, 8, 8);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(120, 35));

        // Efectos hover más sutiles
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setFont(boton.getFont().deriveFont(Font.BOLD, 14.0f));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setFont(boton.getFont().deriveFont(Font.BOLD, 13.5f));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
                }
            }
        });

        return boton;
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
        // Evento del botón Registrar nuevo docente
        btnNuevoDocente.addActionListener(e -> {
            RegistrarDocenteDialog dialog = new RegistrarDocenteDialog(this, colorPrincipal, null);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                boolean exito = controlador.agregarDocente(dialog.docenteEditado);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Docente registrado exitosamente.");
                    cargarDesdeBD();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar el docente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            boolean filaSeleccionada = tabla.getSelectedRow() != -1;
            btnEditar.setEnabled(filaSeleccionada);
            btnDarBaja.setEnabled(filaSeleccionada);
        });


        // Evento del botón buscar
        btnActualizar.addActionListener(e -> buscarDocente());

        // Evento del botón limpiar búsqueda
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());

        // Evento de búsqueda en tiempo real
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarDocente();
            }
        });
    }

    private void buscarDocente() {
        String texto = textField1.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
            lblResultados.setText("Mostrando todos los docentes");
        } else {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + texto);
            sorter.setRowFilter(filter);
            int resultados = tabla.getRowCount();
            lblResultados.setText("Mostrando " + resultados + " resultado(s) para: \"" + texto + "\"");
        }
    }

    private void limpiarBusqueda() {
        textField1.setText("");
        sorter.setRowFilter(null);
        lblResultados.setText("Mostrando todos los docentes");
    }

    private void cargarDatosIniciales() {
        cargarDesdeBD();
    }

    private void cargarDesdeBD() {
        listaDocentes = new ArrayList<>(controlador.obtenerDocentes());
        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Docente d : listaDocentes) {
            String nombreCompleto =
                    (d.getNombre() != null ? d.getNombre() : "") + " " +
                            (d.getApellidoPaterno() != null ? d.getApellidoPaterno() : "") + " " +
                            (d.getApellidoMaterno() != null ? d.getApellidoMaterno() : "");
            modelo.addRow(new Object[]{
                    d.getNumeroTarjeta(),
                    nombreCompleto.trim(),
                    d.getCorreo() != null ? d.getCorreo() : ""
            });
        }
    }

    // Método para editar un docente
    private void editarDocente(int row) {
        if (row >= 0 && row < listaDocentes.size()) {
            Docente docente = listaDocentes.get(row);
            RegistrarDocenteDialog dialog = new RegistrarDocenteDialog(this, colorPrincipal, docente);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                boolean actualizado = controlador.actualizarDatos(dialog.docenteEditado);
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Docente actualizado exitosamente.");
                    cargarDesdeBD();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el docente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }



    public static class RegistrarDocenteDialog extends JDialog {
        public boolean seRegistro = false;
        public Docente docenteEditado;

        public RegistrarDocenteDialog(JFrame parent, Color colorPrincipal, Docente docente) {
            super(parent, (docente == null ? "Registrar" : "Editar") + " docente", true);
            setSize(500, 380);
            setResizable(false);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(18, 10, 0, 10);
            gbc.anchor = GridBagConstraints.WEST;

            // Etiquetas
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            panel.add(labelField("No. Tarjeta", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Nombre(s)", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Apellido paterno", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Apellido materno", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Correo", colorPrincipal, SwingConstants.RIGHT), gbc);

            // Campos
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField txtNumTarjeta = new JTextField(18);
            estilizaCampo(txtNumTarjeta, colorPrincipal);
            panel.add(txtNumTarjeta, gbc);

            gbc.gridy++;
            JTextField txtNombre = new JTextField(18);
            estilizaCampo(txtNombre, colorPrincipal);
            panel.add(txtNombre, gbc);

            gbc.gridy++;
            JTextField txtApellidoPaterno = new JTextField(18);
            estilizaCampo(txtApellidoPaterno, colorPrincipal);
            panel.add(txtApellidoPaterno, gbc);

            gbc.gridy++;
            JTextField txtApellidoMaterno = new JTextField(18);
            estilizaCampo(txtApellidoMaterno, colorPrincipal);
            panel.add(txtApellidoMaterno, gbc);

            gbc.gridy++;
            JTextField txtCorreo = new JTextField(18);
            estilizaCampo(txtCorreo, colorPrincipal);
            panel.add(txtCorreo, gbc);

            // Botón guardar
            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(28, 4, 4, 4);
            gbc.fill = GridBagConstraints.NONE;
            JButton btnGuardar = new JButton(docente == null ? "Guardar" : "Actualizar");
            btnGuardar.setBackground(colorPrincipal);
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 36, 10, 36));
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setOpaque(true);
            panel.add(btnGuardar, gbc);

            add(panel, BorderLayout.CENTER);

            // Si es edición, llena los campos
            if (docente != null) {
                txtNumTarjeta.setText(String.valueOf(docente.getNumeroTarjeta()));
                txtNumTarjeta.setEditable(false);
                txtNumTarjeta.setBackground(new Color(240, 240, 240)); // color gris claro para indicar campo bloqueado
                txtNombre.setText(docente.getNombre() != null ? docente.getNombre() : "");
                txtApellidoPaterno.setText(docente.getApellidoPaterno() != null ? docente.getApellidoPaterno() : "");
                txtApellidoMaterno.setText(docente.getApellidoMaterno() != null ? docente.getApellidoMaterno() : "");
                txtCorreo.setText(docente.getCorreo() != null ? docente.getCorreo() : "");
            }

            btnGuardar.addActionListener(ev -> {
                String numTarjetaString = txtNumTarjeta.getText().trim();
                String nombre = txtNombre.getText().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().trim();
                String correo = txtCorreo.getText().trim();

                if (numTarjetaString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El número de tarjeta no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNumTarjeta.requestFocus(); return;
                }
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNombre.requestFocus(); return;
                }
                if (apellidoPaterno.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El apellido paterno no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtApellidoPaterno.requestFocus(); return;
                }
                if (apellidoMaterno.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El apellido materno no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtApellidoMaterno.requestFocus(); return;
                }
                if (correo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El correo no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtCorreo.requestFocus(); return;
                }
                if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(this, "Ingresa un correo electrónico válido.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtCorreo.requestFocus(); return;
                }


                try{
                    int numTarjetaInt = Integer.parseInt(numTarjetaString);
                    docenteEditado = new Docente(numTarjetaInt, nombre, apellidoPaterno, apellidoMaterno, correo);
                    seRegistro = true;
                    dispose();
                }catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(this, "El numero de tarjeta debe ser numerico.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNumTarjeta.requestFocus(); return;
                }
            });
        }

        private static JLabel labelField(String texto, Color colorPrincipal, int align) {
            JLabel l = new JLabel(texto);
            l.setFont(new Font("Segoe UI", Font.BOLD, 15));
            l.setForeground(colorPrincipal);
            l.setHorizontalAlignment(align);
            return l;
        }

        private static void estilizaCampo(JTextField campo, Color colorPrincipal) {
            campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorPrincipal, 1, true),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DocentesUI().setVisible(true);
        });
    }
}