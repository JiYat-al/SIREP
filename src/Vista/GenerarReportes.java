package Vista;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Controlador.ControladorReporte;
import Modelo.Docente;
import Modelo.ModeloResidente;
import org.apache.poi.ss.formula.functions.Mode;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ListSelectionModel;

public class GenerarReportes extends JDialog {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private JTextField campoFiltro;
    private String tipoReporte;
    private Timer animacionTimer;
    private int animacionFrame = 0;
    private ControladorReporte controladorReporte;
    private java.util.List<Docente> docentes = new ArrayList<>();
    private List<Docente> docentesFiltrados = new ArrayList<>();
    private List<ModeloResidente> residentes = new ArrayList<>();
    private List<ModeloResidente> residentesFiltrados = new ArrayList<>();
    private JList<Object> listaResultados;

    public GenerarReportes(JFrame parent, String tipoReporte) {
        super(parent, "Generar Reporte", true);
        this.tipoReporte = tipoReporte;
        System.out.println("Inicializando GenerarReportes para: " + tipoReporte);
        setSize(650, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        setUndecorated(true);

        try {
            // Animación de entrada
            setOpacity(0.0f);
            inicializarComponentes();
            iniciarAnimacionEntrada();
            System.out.println("GenerarReportes inicializado correctamente");
        } catch (Exception e) {
            System.err.println("Error al inicializar GenerarReportes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosPorTipoReporte() {
        try {
            ControladorReporte controladorReporte = new ControladorReporte();

            switch (tipoReporte) {
                case "Reporte de Asesores":
                    docentes = controladorReporte.obtenerAsesores();
                    if (docentes == null) docentes = new ArrayList<>();
                    docentesFiltrados = new ArrayList<>(docentes);
                    System.out.println("Asesores cargados: " + docentes.size());
                    break;
                case "Reporte de Revisores":
                    docentes = controladorReporte.obtenerRevisores();
                    if (docentes == null) docentes = new ArrayList<>();
                    docentesFiltrados = new ArrayList<>(docentes);
                    System.out.println("Revisores cargados: " + docentes.size());
                    break;
                case "Reporte de Alumnos":
                    residentes = controladorReporte.obtenerResidentes();
                    if (residentes == null) residentes = new ArrayList<>();
                    residentesFiltrados = new ArrayList<>(residentes);
                    System.out.println("Residentes cargados: " + residentes.size());
                    break;
                default:
                    docentes = new ArrayList<>();
                    docentesFiltrados = new ArrayList<>();
                    residentes = new ArrayList<>();
                    residentesFiltrados = new ArrayList<>();
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
            // Inicializar con listas vacías en caso de error
            docentes = new ArrayList<>();
            docentesFiltrados = new ArrayList<>();
            residentes = new ArrayList<>();
            residentesFiltrados = new ArrayList<>();
        }
    }

    private void inicializarComponentes() {
        cargarDatosPorTipoReporte();

        // Inicializar la lista de resultados PRIMERO
        listaResultados = new JList<>();
        listaResultados.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ModeloResidente) {
                    setText(((ModeloResidente) value).getNombre());
                } else if (value instanceof Docente) {
                    setText(((Docente) value).getNombre());
                }
                return this;
            }
        });

        // Panel principal con efectos avanzados
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo degradado más complejo
                GradientPaint gradient1 = new GradientPaint(
                        0, 0, new Color(255, 252, 255),
                        getWidth()/2, getHeight()/2, new Color(245, 240, 255)
                );
                g2.setPaint(gradient1);
                g2.fillRect(0, 0, getWidth(), getHeight());

                GradientPaint gradient2 = new GradientPaint(
                        getWidth()/2, getHeight()/2, new Color(245, 240, 255, 150),
                        getWidth(), getHeight(), new Color(235, 225, 255, 180)
                );
                g2.setPaint(gradient2);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decoraciones geométricas animadas
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 25));
                for (int i = 0; i < 3; i++) {
                    int offset = (animacionFrame + i * 50) % 200;
                    g2.fillOval(-50 + offset/4, -50 + i*30, 120, 120);
                    g2.fillOval(getWidth() - 100 + offset/6, getHeight() - 120 + i*20, 100, 100);
                }

                // Líneas decorativas sutiles
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 40));
                for (int i = 0; i < 5; i++) {
                    int y = 50 + i * 80;
                    g2.drawLine(0, y, getWidth()/4, y + 20);
                    g2.drawLine(getWidth() - getWidth()/4, y + 40, getWidth(), y + 60);
                }
                // Borde exterior elegante
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 80));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
            }
        };

        // Header mejorado
        JPanel headerPanel = crearHeaderMejorado();

        // Panel de contenido expandido
        JPanel contenidoPanel = crearContenidoMejorado();

        // Footer con información adicional
        JPanel footerPanel = crearFooter();

        // Ensamblar el diálogo
        panelPrincipal.add(headerPanel, BorderLayout.NORTH);
        panelPrincipal.add(contenidoPanel, BorderLayout.CENTER);
        panelPrincipal.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

    }



    private void iniciarAnimacionEntrada() {
        animacionTimer = new Timer(15, e -> {
            animacionFrame += 5;
            float alpha = Math.min(1.0f, animacionFrame / 60.0f);
            setOpacity(alpha);
            repaint();

            if (alpha >= 1.0f) {
                animacionTimer.stop();
                // Iniciar animación de fondo continua
                Timer backgroundTimer = new Timer(50, evt -> {
                    animacionFrame++;
                    repaint();
                });
                backgroundTimer.start();
            }
        });
        animacionTimer.start();
    }

    private JPanel crearHeaderMejorado() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo del header con gradiente complejo
                GradientPaint grad1 = new GradientPaint(
                        0, 0, colorPrincipal.brighter(),
                        getWidth()/2, 0, colorPrincipal
                );
                g2.setPaint(grad1);
                g2.fillRect(0, 0, getWidth(), getHeight());

                GradientPaint grad2 = new GradientPaint(
                        getWidth()/2, 0, colorPrincipal,
                        getWidth(), getHeight(), colorPrincipal.darker()
                );
                g2.setPaint(grad2);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Efectos de brillo múltiples
                g2.setColor(new Color(255, 255, 255, 60));
                g2.drawLine(0, 1, getWidth(), 1);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.drawLine(0, 2, getWidth(), 2);

                // Patrón decorativo en el header
                g2.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < getWidth(); i += 40) {
                    g2.fillOval(i - 10, getHeight() - 20, 20, 20);
                }
            }
        };
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        // Panel para el título
        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tituloPanel.setOpaque(false);

        JLabel tituloHeader = new JLabel("Generar " + tipoReporte);
        tituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloHeader.setForeground(Color.WHITE);

        tituloPanel.add(tituloHeader);
        headerPanel.add(tituloPanel, BorderLayout.WEST);

        // Botón de cerrar personalizado
        JButton btnCerrar = new JButton("✕") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                setForeground(Color.WHITE);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(30, 30));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true; repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false; repaint();
                    }
                });

                addActionListener(e -> dispose());
            }

            @Override
            protected void paintComponent(Graphics g) {
                if (hover) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 100, 100, 100));
                    g2.fillOval(2, 2, getWidth()-4, getHeight()-4);
                }
                super.paintComponent(g);
            }
        };

        headerPanel.add(btnCerrar, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel crearContenidoMejorado() {
        JPanel contenidoPanel = new JPanel(new GridBagLayout());
        contenidoPanel.setOpaque(false);
        contenidoPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        // Descripción del reporte
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel descripcionPanel = crearPanelDescripcion();
        contenidoPanel.add(descripcionPanel, gbc);

        // Campo de búsqueda mejorado
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 10, 0);

        JPanel panelCampo = crearCampoBusquedaMejorado();
        contenidoPanel.add(panelCampo, gbc);

        // Lista de selección (arriba de los botones)
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel panelSeleccion = crearPanelSeleccion();
        contenidoPanel.add(panelSeleccion, gbc);

        // Panel de botones mejorado (ahora abajo)
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;

        JPanel panelBotones = crearPanelBotonesMejorado();
        contenidoPanel.add(panelBotones, gbc);

        return contenidoPanel;
    }

    private JPanel crearPanelDescripcion() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con gradiente sutil
                GradientPaint grad = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 200),
                        0, getHeight(), new Color(250, 248, 255, 150)
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Borde elegante
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 40));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        String descripcion = obtenerDescripcionReporte();
        JLabel lblDescripcion = new JLabel("<html><center>" + descripcion + "</center></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(80, 80, 100));
        panel.add(lblDescripcion, BorderLayout.CENTER);

        return panel;
    }

    private String obtenerDescripcionReporte() {
        switch (tipoReporte) {
            case "Reporte de Asesores":
                return "Genere un reporte detallado de todos los asesores académicos<br>incluyendo proyectos asignados y estado actual";
            case "Reporte de Revisores":
                return "Consulte información completa sobre revisores de proyectos<br>con estadísticas de evaluaciones realizadas";
            case "Reporte de Alumnos":
                return "Obtenga datos comprehensivos de estudiantes participantes<br>en proyectos de residencia profesional";
            default:
                return "Configure los parámetros para generar su reporte personalizado";
        }
    }

    private JPanel crearFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // Línea separadora elegante
                GradientPaint grad = new GradientPaint(
                        0, 0, new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 0),
                        getWidth()/2, 0, new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 60)
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth()/2, 2);

                grad = new GradientPaint(
                        getWidth()/2, 0, new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 60),
                        getWidth(), 0, new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 0)
                );
                g2.setPaint(grad);
                g2.fillRect(getWidth()/2, 0, getWidth()/2, 2);
            }
        };
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 15, 30));
        footerPanel.setPreferredSize(new Dimension(0, 40));

        JLabel lblInfo = new JLabel("Consejo: Use filtros específicos para obtener resultados más precisos");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(120, 120, 140));
        footerPanel.add(lblInfo, BorderLayout.CENTER);

        return footerPanel;
    }

    private JPanel crearCampoBusquedaMejorado() {
        campoFiltro = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con gradiente y bordes redondeados
                GradientPaint grad = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(248, 248, 255)
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Borde con efecto glow
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 80));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);

                // Borde interno sutil
                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 30));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(3, 3, getWidth()-7, getHeight()-7, 16, 16);

                super.paintComponent(g);
            }
        };
        campoFiltro.setOpaque(false);
        campoFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoFiltro.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        campoFiltro.setPreferredSize(new Dimension(450, 50));
        campoFiltro.setForeground(new Color(60, 60, 80));

        // Placeholder text
        campoFiltro.setText("Ingrese criterios de filtrado (nombre, periodo, etc.)");
        campoFiltro.setForeground(new Color(150, 150, 150));
        campoFiltro.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campoFiltro.getText().equals("Ingrese criterios de filtrado (nombre, periodo, etc.)")) {
                    campoFiltro.setText("");
                    campoFiltro.setForeground(new Color(60, 60, 80));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campoFiltro.getText().isEmpty()) {
                    campoFiltro.setText("Ingrese criterios de filtrado (nombre, periodo, etc.)");
                    campoFiltro.setForeground(new Color(150, 150, 150));
                }
            }
        });
        campoFiltro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarDatos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarDatos(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarDatos(); }
        });


        // Panel contenedor con efectos
        JPanel panelCampo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra exterior
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(3, 6, getWidth()-6, getHeight()-4, 25, 25);
            }
        };
        panelCampo.setOpaque(false);
        panelCampo.setPreferredSize(new Dimension(450, 50));

        panelCampo.add(campoFiltro, BorderLayout.CENTER);

        return panelCampo;

    }

    private JPanel crearPanelSeleccion() {
        JPanel panelSeleccion = new JPanel(new BorderLayout());
        panelSeleccion.setOpaque(false);

        // Etiqueta descriptiva
        JLabel lblSeleccion = new JLabel(obtenerTextoSeleccion());
        lblSeleccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSeleccion.setForeground(new Color(80, 80, 100));
        lblSeleccion.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lblSeleccion.setHorizontalAlignment(SwingConstants.LEFT);

        panelSeleccion.add(lblSeleccion, BorderLayout.NORTH);
        panelSeleccion.add(crearListaSeleccion(), BorderLayout.CENTER);

        return panelSeleccion;
    }

    private String obtenerTextoSeleccion() {
        switch (tipoReporte) {
            case "Reporte de Asesores":
                return "Seleccione un asesor para generar el reporte:";
            case "Reporte de Revisores":
                return "Seleccione un revisor para generar el reporte:";
            case "Reporte de Alumnos":
                return "Seleccione un alumno para generar el reporte:";
            default:
                return "Seleccione un elemento para generar el reporte:";
        }
    }

    private JScrollPane crearListaSeleccion() {
        // Configurar la lista de resultados
        listaResultados.setVisibleRowCount(5);
        listaResultados.setFixedCellWidth(400);
        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Estilo personalizado para la lista
        listaResultados.setBackground(new Color(250, 250, 255));
        listaResultados.setSelectionBackground(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 100));
        listaResultados.setSelectionForeground(Color.BLACK);
        listaResultados.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Crear el scroll pane con estilo
        JScrollPane scrollPane = new JScrollPane(listaResultados) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con gradiente sutil
                GradientPaint grad = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 230),
                        0, getHeight(), new Color(248, 248, 255, 200)
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                super.paintComponent(g);
            }
        };

        scrollPane.setPreferredSize(new Dimension(450, 130));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 80), 2, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(new Color(250, 250, 255));

        // Cargar datos iniciales en la lista
        filtrarDatos();

        return scrollPane;
    }

    private void filtrarDatos() {
        String texto = getTextoFiltro().toLowerCase();

        if (tipoReporte.equals("Reporte de Alumnos")) {
            residentesFiltrados = residentes.stream()
                    .filter(r -> r.getNombre().toLowerCase().contains(texto))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(ModeloResidente::getNombre, r -> r, (a, b) -> a),
                            m -> new ArrayList<>(m.values())
                    ));
            listaResultados.setListData(residentesFiltrados.toArray(new ModeloResidente[0]));
        } else {
            docentesFiltrados = docentes.stream()
                    .filter(d -> d.getNombre().toLowerCase().contains(texto))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(Docente::getNombre, d -> d, (a, b) -> a),
                            m -> new ArrayList<>(m.values())
                    ));
            listaResultados.setListData(docentesFiltrados.toArray(new Docente[0]));
        }
    }

    /** private void filtrarDatos() {
     String texto = getTextoFiltro().toLowerCase();
     if (tipoReporte.equals("Reporte de Alumnos")) {
     residentesFiltrados = residentes.stream()
     .filter(r -> r.getNombre().toLowerCase().contains(texto))
     .collect(Collectors.toList());  // sin toMap, sin eliminar duplicados
     listaResultados.setListData(residentesFiltrados.toArray(new ModeloResidente[0]));
     } else {
     // mantén el código de docentes igual
     docentesFiltrados = docentes.stream()
     .filter(d -> d.getNombre().toLowerCase().contains(texto))
     .collect(Collectors.collectingAndThen(
     Collectors.toMap(Docente::getNombre, d -> d, (a, b) -> a),
     m -> new ArrayList<>(m.values())
     ));
     listaResultados.setListData(docentesFiltrados.toArray(new Docente[0]));
     }
     }
     */







    private JPanel crearPanelBotonesMejorado() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);

        // Botón Generar (más prominente)
        JButton btnGenerar = crearBotonEleganteMejorado("Generar Reporte", colorPrincipal, Color.WHITE, true);
        btnGenerar.addActionListener(e -> {
            // Animación de procesamiento

            if (tipoReporte.equals("Reporte de Asesores")) {
                Docente asesorSeleccionado = (Docente) listaResultados.getSelectedValue();
                if (asesorSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Por favor selecciona un asesor.");
                    return;
                }
            }
            if (tipoReporte.equals("Reporte de Revisores")) {
                Docente asesorSeleccionado = (Docente) listaResultados.getSelectedValue();
                if (asesorSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Por favor selecciona un asesor.");
                    return;
                }
            }


            btnGenerar.setText("Generando...");
            btnGenerar.setEnabled(false);
            Timer processingTimer = new Timer(200, evt -> {
                try{

                    switch (tipoReporte) {
                        case "Reporte de Asesores":{

                            Docente asesorSeleccionado = (Docente) listaResultados.getSelectedValue();
                            /**if (asesorSeleccionado == null) {
                             JOptionPane.showMessageDialog(this, "Por favor selecciona un asesor.");

                             return;
                             /**}else{*/
                            String rutaArchivo = seleccionarRutaGuardarArchivo();
                            if (rutaArchivo == null) {
                                System.out.println("Guardado cancelado");
                                dispose();
                            }
                            ControladorReporte controlador = new ControladorReporte();
                            int tarjeta= asesorSeleccionado.getNumeroTarjeta();
                            controlador.generarReporteAsesor(tarjeta,rutaArchivo);
                            abrirArchivo(rutaArchivo);
                            break;
                        }
                        /*}*/
                        case "Reporte de Revisores":{
                            Docente revisorSeleccionado = (Docente) listaResultados.getSelectedValue();
                            String rutaArchivo2 = seleccionarRutaGuardarArchivo();
                            if (rutaArchivo2 == null) {
                                System.out.println("Guardado cancelado");
                                dispose();
                            }
                            ControladorReporte controlador2 = new ControladorReporte();
                            int tarjetarevisor= revisorSeleccionado.getNumeroTarjeta();
                            controlador2.generarReporteRevisor(tarjetarevisor,rutaArchivo2);
                            abrirArchivo(rutaArchivo2);
                            break;
                        }
                        case "Reporte de Alumnos":{
                            ModeloResidente residenteSeleccionado= (ModeloResidente) listaResultados.getSelectedValue();
                            String rutaArchivo3 = seleccionarRutaGuardarArchivo();
                            if (rutaArchivo3 == null) {
                                System.out.println("Guardado cancelado");
                                dispose();
                            }
                            ControladorReporte controlador3 = new ControladorReporte();
                            int id= residenteSeleccionado.getIdResidente();
                            System.out.println(id);
                            controlador3.generarReporteResidente(id,rutaArchivo3);
                            abrirArchivo(rutaArchivo3);
                            break;
                        }

                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
                }


                dispose();
            });
            processingTimer.setRepeats(false);
            processingTimer.start();
        });

        // Botón Cancelar (estilo secundario)
        JButton btnCancelar = crearBotonEleganteMejorado("Cancelar", colorPrincipal, Color.WHITE, false);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGenerar);
        panelBotones.add(btnCancelar);

        return panelBotones;
    }


    private JButton crearBotonEleganteMejorado(String texto, Color colorFondo, Color colorTexto, boolean esPrimario) {
        return new JButton(texto) {
            private boolean hover = false;

            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setForeground(colorTexto);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true; repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false; repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo del botón morado simple
                Color colorBase = hover ? colorPrincipal.darker() : colorPrincipal;
                g2.setColor(colorBase);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde sutil
                g2.setColor(colorBase.darker());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

                super.paintComponent(g);
            }
        };
    }

    private void abrirArchivo(String rutaArchivo) {
        try {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            java.io.File file = new java.io.File(rutaArchivo);
            if (file.exists()) {
                desktop.open(file); // Abre directamente el archivo con el programa predeterminado
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no se encontró.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo: " + e.getMessage());
        }
    }


    public String seleccionarRutaGuardarArchivo() {
        JFileChooser fileChooser = new JFileChooser();

        // Filtro para archivos Excel (.xlsx)
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filtro);

        fileChooser.setDialogTitle("Guardar reporte como");

        int userSelection = fileChooser.showSaveDialog(null); // null para que sea centrado en pantalla

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String rutaSeleccionada = fileChooser.getSelectedFile().getAbsolutePath();
            // Asegurarse que la extensión sea .xlsx
            if (!rutaSeleccionada.toLowerCase().endsWith(".xlsx")) {
                rutaSeleccionada += ".xlsx";
            }
            return rutaSeleccionada;
        }
        return null; // usuario canceló
    }

    // Método público para obtener el texto del filtro (para uso futuro del backend)
    public String getTextoFiltro() {
        String texto = campoFiltro.getText();
        return texto.equals("Ingrese criterios de filtrado (nombre, periodo, etc.)") ? "" : texto;
    }

    // Método estático de conveniencia para mostrar el diálogo
    public static void mostrar(JFrame parent, String tipoReporte) {
        try {
            System.out.println("Llamando a GenerarReportes.mostrar() con tipo: " + tipoReporte);
            GenerarReportes dialogo = new GenerarReportes(parent, tipoReporte);
            System.out.println("Mostrando el diálogo...");
            dialogo.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error en mostrar(): " + e.getMessage());
            e.printStackTrace();
        }
    }

}

