package Vista;

import Modelo.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.*;

public class AnteproyectoInterfaz extends JFrame {
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private ArrayList<Anteproyecto> listaAnteproyectos = new ArrayList<>();

    public AnteproyectoInterfaz() {
        setTitle("Banco de Anteproyectos - SIREP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        // Panel principal con fondo degradado EXACTO como BancoProyectos
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

                // Líneas curvas decorativas EXACTAS
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

        // Crear barra lateral EXACTA como BancoProyectos
        crearBarraLateral(mainPanel);

        // Panel de contenido principal
        JPanel panelContenido = crearPanelContenido();

        // Crear botón regresar EXACTO como BancoProyectos
        crearBotonRegresar(mainPanel);

        mainPanel.add(panelContenido, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Cargar datos de ejemplo
        cargarTablaAnteproyectos();
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

        // Icono
        JLabel iconoBarra = new JLabel("📋", SwingConstants.CENTER);
        iconoBarra.setFont(new Font("Segoe UI", Font.BOLD, 48));
        iconoBarra.setForeground(Color.WHITE);
        barraLateral.add(iconoBarra, gbcL);

        // Título vertical
        gbcL.gridy++;
        gbcL.insets = new Insets(0, 0, 0, 0);
        JLabel tituloBarra = new JLabel("<html><div style='text-align: center; line-height: 1.5;'>" +
                "A<br>N<br>T<br>E<br>P<br>R<br>O<br>Y<br>E<br>C<br>T<br>O<br>S</div></html>");
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

    private void crearBotonRegresar(JPanel mainPanel) {
        JButton btnRegresar = new JButton("\u2190");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRegresar.setForeground(colorPrincipal);
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> {
            dispose();
            new Menu().setVisible(true);
        });

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTop.setOpaque(false);
        panelTop.add(btnRegresar);
        mainPanel.add(panelTop, BorderLayout.NORTH);
    }

    private JPanel crearPanelContenido() {
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);

        // Header
        JPanel header = crearHeader();
        panelContenido.add(header, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 38, 20, 38));

        // Configurar tabla
        configurarTabla();
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
        return panelContenido;
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

        JLabel lblTitulo = new JLabel("Banco de Anteproyectos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        header.add(lblTitulo, BorderLayout.WEST);

        // Panel de botones en el header
        JPanel panelBotonesHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelBotonesHeader.setOpaque(false);

        JButton btnNuevo = crearBotonAccion("Nuevo Anteproyecto", new Color(63, 81, 181));
        btnNuevo.addActionListener(e -> {
            mostrarDialogoNuevo();
        });

        JButton btnProrroga = crearBotonAccion("Prórroga", new Color(255, 152, 0));
        btnProrroga.addActionListener(e -> {
            mostrarSeleccionProrrogaEnMismaVentana();
        });

        panelBotonesHeader.add(btnNuevo);
        panelBotonesHeader.add(btnProrroga);
        header.add(panelBotonesHeader, BorderLayout.EAST);
        return header;
    }

    private void configurarTabla() {
        String[] columnas = {"NOMBRE", "DESCRIPCIÓN", "ALUMNOS", "ASESOR", "REVISORES"};
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(45);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(new Color(230, 230, 250));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(200); // NOMBRE
        tabla.getColumnModel().getColumn(1).setPreferredWidth(300); // DESCRIPCIÓN
        tabla.getColumnModel().getColumn(2).setPreferredWidth(250); // ALUMNOS
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150); // ASESOR
        tabla.getColumnModel().getColumn(4).setPreferredWidth(200); // REVISORES

        // Configurar header
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(new Color(220, 219, 245));
        tabla.getTableHeader().setForeground(colorPrincipal);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 45));
        tabla.getTableHeader().setReorderingAllowed(false);
        ((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Efectos de selección
        tabla.setSelectionBackground(new Color(220, 219, 245));
        tabla.setSelectionForeground(new Color(60, 60, 100));

        // Configurar renderer para wrap text y colorear prórroga
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Colorear filas con prórroga de amarillo (color huevo)
                if (row < listaAnteproyectos.size() && listaAnteproyectos.get(row).isTieneProrroga()) {
                    if (!isSelected) {
                        c.setBackground(new Color(255, 248, 180)); // Color amarillo huevo
                    }
                } else {
                    if (!isSelected) {
                        c.setBackground(Color.WHITE);
                    }
                }

                if (column == 1) { // Columna DESCRIPCIÓN
                    setToolTipText(value != null ? value.toString() : "");
                }
                return c;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelBotones.setOpaque(false);

        JButton btnEditar = crearBotonEstilo("Editar");
        JButton btnEliminar = crearBotonEstilo("Eliminar");
        JButton btnVerInfo = crearBotonEstilo("Ver Información");

        // Inicialmente deshabilitados
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnVerInfo.setEnabled(false);

        // Eventos
        btnEditar.addActionListener(e -> editarAnteproyecto());
        btnEliminar.addActionListener(e -> eliminarAnteproyecto());
        btnVerInfo.addActionListener(e -> verInformacion());

        // Habilitar/deshabilitar según selección
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = tabla.getSelectedRow() != -1;
                btnEditar.setEnabled(haySeleccion);
                btnEliminar.setEnabled(haySeleccion);
                btnVerInfo.setEnabled(haySeleccion);
            }
        });

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVerInfo);

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
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        if (isEnabled()) { hover = true; repaint(); }
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
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


    private JButton crearBotonEstilo(String texto) {
        return new JButton(texto) {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
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
    }

    private void mostrarAyuda() {
        String ayuda = "Sistema de Banco de Anteproyectos SIREP\n\n" +
                "• Nuevo Anteproyecto: Registra un anteproyecto nuevo\n" +
                "• Prórroga: Gestiona prórrogas para anteproyectos existentes\n" +
                "• Editar: Modifica el anteproyecto seleccionado\n" +
                "• Eliminar: Elimina el anteproyecto seleccionado\n" +
                "• Ver Información: Muestra detalles del anteproyecto\n" +
                "• Flecha izquierda: Regresa al menú principal\n\n" +
                "NOTA: Los anteproyectos con prórroga aparecen resaltados en amarillo.";

        JOptionPane.showMessageDialog(this, ayuda, "Ayuda - Banco de Anteproyectos", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cargarTablaAnteproyectos() {
        modelo.setRowCount(0);

        // Obtener lista desde DAO
        listaAnteproyectos = AnteproyectoDAO.listaAnteproyectos();
        System.out.println(listaAnteproyectos.toString());

        for (Anteproyecto a : listaAnteproyectos) {
            String nombre = a.getProyecto().getNombre();
            String descripcion = a.getProyecto().getDescripcion();

            String alumnos = a.getResidentes().stream()
                    .map(ModeloResidente::getNombre)  // asumiendo que ModeloResidente tiene getNombre()
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("Sin alumnos");

            String asesor = a.getAsesor().getNombre(); // asumiendo que Docente tiene getNombre()

            String revisores = a.getRevisores().stream()
                    .map(Docente::getNombre)
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("Sin revisores");

            modelo.addRow(new Object[]{
                    nombre, descripcion, alumnos, asesor, revisores
            });
        }
    }

    private void mostrarDialogoNuevo() {
        // Abrir el formulario de registro de anteproyectos
        try {
            new FormularioAnteproyecto(this).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir el formulario de registro:\n" + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarAnteproyecto() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {

            Anteproyecto ap = listaAnteproyectos.get(filaSeleccionada);

            // Abrir el formulario de registro con los datos del anteproyecto seleccionado
            try {
                FormularioAnteproyecto formulario = new FormularioAnteproyecto(this);

                // Cambiar el título para indicar que es edición
                formulario.setTitle("Editar Anteproyecto - " + ap.getProyecto().getNombre());

                // Prellenar los campos con los datos del anteproyecto seleccionado
                formulario.setNombreProyecto(ap.getProyecto().getNombre());

                Empresa emps = EmpresaDAO.buscarPorID(ap.getProyecto().getId_empresa());
                formulario.setEmpresa(emps);

                formulario.setTodo(ap);

                //formulario.setAlumnosTexto(ap.getResidentes());

                formulario.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir el formulario de edición:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarAnteproyecto() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {

            Anteproyecto ap = listaAnteproyectos.get(filaSeleccionada);
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el anteproyecto:\n" + ap.getProyecto().getNombre() + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirmacion == JOptionPane.YES_OPTION) {
                listaAnteproyectos.remove(filaSeleccionada);
                modelo.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(this, "Anteproyecto eliminado correctamente.");
            }
        }
    }

    private void verInformacion() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {

            Anteproyecto ap = listaAnteproyectos.get(filaSeleccionada);

            // Crear diálogo moderno y elegante
            JDialog dialogo = new JDialog(this, "Información del Anteproyecto", true);
            dialogo.setSize(900, 700);
            dialogo.setLocationRelativeTo(this);
            dialogo.setResizable(false);

            // Panel principal con fondo degradado
            JPanel panelPrincipal = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Fondo degradado elegante
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(250, 248, 255),
                            0, getHeight(), new Color(240, 235, 255)
                    );
                    g2.setPaint(gradient);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Decoraciones sutiles
                    g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 30));
                    g2.fillOval(-50, -50, 150, 150);
                    g2.fillOval(getWidth() - 100, getHeight() - 100, 120, 120);
                }
            };

            // Header con título elegante
            JPanel headerPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Fondo del header con gradiente
                    GradientPaint grad = new GradientPaint(
                            0, 0, colorPrincipal,
                            0, getHeight(), colorPrincipal.darker()
                    );
                    g2.setPaint(grad);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                    // Sombra interna sutil
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.drawLine(0, 1, getWidth(), 1);
                }
            };
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            headerPanel.setPreferredSize(new Dimension(0, 80));
            JLabel tituloHeader = new JLabel( ap.getProyecto().getNombre());
            tituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
            tituloHeader.setForeground(Color.WHITE);
            headerPanel.add(tituloHeader, BorderLayout.WEST);

            // Indicador de estado elegante
            JLabel estadoLabel = new JLabel();//ap.isAceptado() ? "✅ APROBADO" : "⏳ PENDIENTE");
            estadoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            estadoLabel.setForeground(new Color(46, 204, 113));//ap.isAceptado() ? new Color(46, 204, 113) : new Color(255, 193, 7));
            estadoLabel.setOpaque(true);
            estadoLabel.setBackground(Color.WHITE);
            estadoLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            headerPanel.add(estadoLabel, BorderLayout.EAST);
            // Contenido principal con scroll
            JPanel contenidoPanel = new JPanel();
            contenidoPanel.setLayout(new BoxLayout(contenidoPanel, BoxLayout.Y_AXIS));
            contenidoPanel.setOpaque(false);
            contenidoPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

            // Crear secciones elegantes
            contenidoPanel.add(crearSeccionInfo("DESCRIPCIÓN DEL PROYECTO", ap.getProyecto().getDescripcion()));
            contenidoPanel.add(Box.createVerticalStrut(20));

            Empresa emps = EmpresaDAO.buscarPorID(ap.getProyecto().getId_empresa());

            contenidoPanel.add(crearSeccionInfo("INFORMACIÓN EMPRESARIAL",
                    "Empresa: " + emps.getNombre() + "\n" +
                            "Contacto: " + emps.getTelefono() + "\n" +
                            "Origen: " + ap.getProyecto().getNombreOrigen()));
            contenidoPanel.add(Box.createVerticalStrut(20));

            SimpleDateFormat formato = new SimpleDateFormat("d MMMM yyyy", Locale.of("es", "ES"));

            contenidoPanel.add(crearSeccionInfo("CRONOGRAMA",
                    "Periodo: " + ap.getPeriodo() + "\n" +
                            "Inicio: " + formato.format(ap.getFechaInicio()) + "\n" +
                            "Finalización: " + formato.format(ap.getFechaFin())
            ));
            contenidoPanel.add(Box.createVerticalStrut(20));

            String alumnos = "";
            for (ModeloResidente r : ap.getResidentes()) {
                alumnos += r.getNumeroControl() + " - " +
                        r.getNombre() + " " + r.getApellidoPaterno() + " " + r.getApellidoMaterno() + " - " +
                        r.getCorreo() + "\n";
            }
            String asesor = ap.getAsesor().getNumeroTarjeta() + " - " + ap.getAsesor().getNombre() + " " + ap.getAsesor().getApellidoPaterno()
                    + " " + ap.getAsesor().getApellidoMaterno() + " - " + ap.getAsesor().getCorreo();

            String revisores = "";
            for (Docente d : ap.getRevisores()) {
                revisores += d.getNumeroTarjeta() + " - " + d.getNombre() + " " + d.getApellidoPaterno() + " " + d.getApellidoMaterno() + " - " +
                        d.getCorreo() + "\n";
            }

            contenidoPanel.add(crearSeccionInfo("EQUIPO DE TRABAJO",
                    "Alumnos:\n" + alumnos + "\n" +
                            "Asesor:\n" + asesor + "\n\n" +
                            "Revisores:\n" + revisores
            ));
            contenidoPanel.add(Box.createVerticalStrut(20));

            // Información del documento de prórroga si existe
            String archivoAnteproyecto = (ap.getArchivoAnteproyecto() != null)
                    ? ap.getArchivoAnteproyecto().substring(ap.getArchivoAnteproyecto().lastIndexOf('/') + 1)
                    : "Ningún archivo seleccionado";

            String documentacionTexto = "Archivo Anteproyecto: " + archivoAnteproyecto;
            if (ap.isTieneProrroga() && ap.getArchivoAutorizacionProrroga() != null &&
                    !ap.getArchivoAutorizacionProrroga().trim().isEmpty()) {

                String nombreDocProrroga = ap.getArchivoAutorizacionProrroga();
                if (nombreDocProrroga.contains("\\") || nombreDocProrroga.contains("/")) {
                    nombreDocProrroga = nombreDocProrroga.substring(Math.max(
                            nombreDocProrroga.lastIndexOf("\\"),
                            nombreDocProrroga.lastIndexOf("/")
                    ) + 1);
                }
                documentacionTexto += "\nAutorización Prórroga: " + nombreDocProrroga;
            }

            contenidoPanel.add(crearSeccionInfo("DOCUMENTACIÓN", documentacionTexto));

            JScrollPane scrollPane = new JScrollPane(contenidoPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // Panel de botones elegante
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            panelBotones.setOpaque(false);

            JButton btnCerrar = new JButton("Cerrar") {
                private boolean hover = false;
                {
                    setContentAreaFilled(false);
                    setFocusPainted(false);
                    setFont(new Font("Segoe UI", Font.BOLD, 16));
                    setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));

                    addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent e) {
                            hover = true; repaint();
                        }
                        @Override
                        public void mouseExited(java.awt.event.MouseEvent e) {
                            hover = false; repaint();
                        }
                    });
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Sombra
                    g2.setColor(new Color(0, 0, 0, 20));
                    g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 4, 25, 25);
                    // Botón con gradiente
                    Color colorBase = hover ? colorPrincipal.darker() : colorPrincipal;
                    GradientPaint grad = new GradientPaint(0, 0, colorBase.brighter(), 0, getHeight(), colorBase);
                    g2.setPaint(grad);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    super.paintComponent(g);
                }
            };
            btnCerrar.addActionListener(e -> dialogo.dispose());
            panelBotones.add(btnCerrar);
            // Ensamblar el diálogo
            panelPrincipal.add(headerPanel, BorderLayout.NORTH);
            panelPrincipal.add(scrollPane, BorderLayout.CENTER);
            panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
            dialogo.setContentPane(panelPrincipal);
            dialogo.setVisible(true);
        }
    }

    private JPanel crearSeccionInfo(String titulo, String contenido) {
        JPanel seccion = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con sombra suave
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 4, 15, 15);

                // Fondo principal
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde sutil
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 40));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        seccion.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Título de la sección
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Contenido de la sección
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setForeground(new Color(60, 60, 80));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        seccion.add(lblTitulo, BorderLayout.NORTH);
        seccion.add(textArea, BorderLayout.CENTER);

        return seccion;
    }

    private void mostrarSeleccionProrrogaEnMismaVentana() {
        // Verificar que hay anteproyectos disponibles
        if (listaAnteproyectos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay anteproyectos disponibles para gestionar prórrogas.",
                    "Sin Anteproyectos",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Verificar que hay una fila seleccionada
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un anteproyecto de la tabla para gestionar su prórroga.",
                    "Seleccione un Anteproyecto",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Abrir el formulario de prórroga
        Anteproyecto anteproyectoSeleccionado = listaAnteproyectos.get(filaSeleccionada);

        // Validación: Verificar si ya tiene prórroga
        if (anteproyectoSeleccionado.isTieneProrroga()) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Este anteproyecto ya tiene una prórroga asignada.\n" +
                            "¿Desea modificar la prórroga existente?",
                    "Prórroga Existente",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Abrir el formulario de prórroga en una clase separada
        FormularioProrroga formulario = new FormularioProrroga(this, anteproyectoSeleccionado);
        formulario.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnteproyectoInterfaz().setVisible(true);
        });
    }
}
