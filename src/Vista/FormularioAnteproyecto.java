package Vista;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

// Clases de ejemplo para demostración visual
class Empresa {
    private String nombre;
    private String direccion;
    private String responsable;
    private String telefono;
    private String correo;
    private String rfc;

    public Empresa(String nombre, String direccion, String responsable,
                   String telefono, String correo, String rfc) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.responsable = responsable;
        this.telefono = telefono;
        this.correo = correo;
        this.rfc = rfc;
    }

    public String getCorreo() { return correo; }
    @Override
    public String toString() { return nombre; }
}

class Docente {
    private String nombre;
    public void setNombre(String nombre) { this.nombre = nombre; }
    @Override
    public String toString() { return nombre; }
}

public class FormularioAnteproyecto extends JFrame {
    private JTextField txtNombreProyecto;
    private JTextArea txtDescripcion;
    private JComboBox<Empresa> comboEmpresa;
    private JTextField txtCorreoEmpresa;
    private JComboBox<String> comboOrigen;
    private JSpinner fechaEntrega;
    private JSpinner fechaInicio;
    private JSpinner fechaFinal;
    private JButton btnArchivo;
    private JLabel lblArchivo;
    private JList<String> listaAlumnos;
    private JList<Docente> listaAsesores;
    private JList<Docente> listaRevisores;
    private JComboBox<String> comboPeriodo;
    private JCheckBox checkAceptado;
    private File archivoSeleccionado;
    private DefaultListModel<String> modeloAlumnos;
    private DefaultListModel<Docente> modeloAsesores;
    private DefaultListModel<Docente> modeloRevisores;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorSecundario = new Color(103, 104, 189);
    private final Color colorFondo = new Color(245, 243, 255);
    private final Color colorTexto = new Color(50, 50, 93);
    private final Color colorBordes = new Color(180, 180, 220);
    private JPanel mainPanel;

    public FormularioAnteproyecto() {
        configurarVentana();
        inicializarComponentes();
        cargarDatos();
        configurarEventos();
    }

    private void configurarVentana() {
        setTitle("Registro de Anteproyecto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 650));

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);
    }

    private void inicializarComponentes() {
        // Banner compacto
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(colorPrincipal);
        banner.setPreferredSize(new Dimension(0, 70));

        JLabel lblTitulo = new JLabel("Registro de Anteproyecto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 0));
        banner.add(lblTitulo, BorderLayout.WEST);
        mainPanel.add(banner, BorderLayout.NORTH);

        // Panel central organizado
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 4, 4, 12);

        // Inicialización de componentes
        inicializarCampos();

        // Agregar componentes organizados
        agregarComponentes(panelCentral, gbc);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(panelCentral);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        crearPanelBotones();
    }

    private void inicializarCampos() {
        txtNombreProyecto = new JTextField(30);
        txtDescripcion = new JTextArea(3, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        comboEmpresa = new JComboBox<>();
        txtCorreoEmpresa = new JTextField(30);
        txtCorreoEmpresa.setEditable(false);
        comboOrigen = new JComboBox<>(new String[]{"Externo", "Banco de Proyectos"});

        fechaEntrega = new JSpinner(new SpinnerDateModel());
        fechaInicio = new JSpinner(new SpinnerDateModel());
        fechaFinal = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor dateEditorEntrega = new JSpinner.DateEditor(fechaEntrega, "dd/MM/yyyy");
        JSpinner.DateEditor dateEditorInicio = new JSpinner.DateEditor(fechaInicio, "dd/MM/yyyy");
        JSpinner.DateEditor dateEditorFinal = new JSpinner.DateEditor(fechaFinal, "dd/MM/yyyy");

        fechaEntrega.setEditor(dateEditorEntrega);
        fechaInicio.setEditor(dateEditorInicio);
        fechaFinal.setEditor(dateEditorFinal);

        btnArchivo = new JButton("Seleccionar Archivo");
        lblArchivo = new JLabel("No se ha seleccionado archivo");

        modeloAlumnos = new DefaultListModel<>();
        modeloAsesores = new DefaultListModel<>();
        modeloRevisores = new DefaultListModel<>();

        listaAlumnos = new JList<>(modeloAlumnos);
        listaAsesores = new JList<>(modeloAsesores);
        listaRevisores = new JList<>(modeloRevisores);

        listaAlumnos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaAsesores.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaRevisores.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        comboPeriodo = new JComboBox<>(new String[]{
                "ENERO-JUNIO", "VERANO", "AGOSTO-DICIEMBRE"
        });

        checkAceptado = new JCheckBox("Anteproyecto Aceptado");
    }

    private void agregarComponentes(JPanel panel, GridBagConstraints gbc) {
        int y = 0;

        // INFORMACIÓN DEL PROYECTO
        agregarTitulo(panel, "INFORMACIÓN DEL PROYECTO", gbc, y++);
        agregarCampo(panel, "Nombre del Proyecto:", txtNombreProyecto, gbc, y++);
        agregarCampoDescripcion(panel, "Descripción:", txtDescripcion, gbc, y++);

        // EMPRESA
        agregarTitulo(panel, "INFORMACIÓN DE LA EMPRESA", gbc, y++);
        agregarCampo(panel, "Empresa:", comboEmpresa, gbc, y++);
        agregarCampo(panel, "Correo de la Empresa:", txtCorreoEmpresa, gbc, y++);

        // CONFIGURACIÓN
        agregarTitulo(panel, "CONFIGURACIÓN DEL PROYECTO", gbc, y++);
        agregarCampo(panel, "Origen del Proyecto:", comboOrigen, gbc, y++);
        agregarCampo(panel, "Periodo:", comboPeriodo, gbc, y++);

        // FECHAS
        agregarCampo(panel, "Fecha de Entrega:", fechaEntrega, gbc, y++);
        agregarCampo(panel, "Fecha de Inicio:", fechaInicio, gbc, y++);
        agregarCampo(panel, "Fecha Final:", fechaFinal, gbc, y++);

        // DOCUMENTACIÓN
        agregarTitulo(panel, "DOCUMENTACIÓN", gbc, y++);
        agregarCampoArchivo(panel, "Archivo del Proyecto:", gbc, y++);

        // PARTICIPANTES
        agregarTitulo(panel, "PARTICIPANTES", gbc, y++);
        agregarCampoAlumnos(panel, "Alumnos:", gbc, y++);
        agregarListaSeleccion(panel, "Asesores:", listaAsesores, gbc, y++);
        agregarListaSeleccion(panel, "Revisores:", listaRevisores, gbc, y++);

        // ESTADO
        agregarTitulo(panel, "ESTADO", gbc, y++);
        agregarCheckbox(panel, checkAceptado, gbc, y++);
    }

    private void agregarTitulo(JPanel panel, String titulo, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 4, 10, 12);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, colorPrincipal),
                BorderFactory.createEmptyBorder(5, 0, 8, 0)
        ));

        panel.add(lblTitulo, gbc);

        // Restaurar configuración
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 4, 4, 12);
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo,
                              GridBagConstraints gbc, int y) {
        // Etiqueta
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEtiqueta.setForeground(colorPrincipal);
        panel.add(lblEtiqueta, gbc);

        // Campo
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        campo.setPreferredSize(new Dimension(400, 28));

        if (campo instanceof JTextField) {
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorPrincipal, 2),
                    BorderFactory.createEmptyBorder(3, 6, 3, 6)
            ));
        } else if (campo instanceof JComboBox) {
            campo.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
            ((JComboBox<?>) campo).setBackground(new Color(248, 248, 255));
            ((JComboBox<?>) campo).setForeground(colorPrincipal.darker());
        } else if (campo instanceof JSpinner) {
            campo.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        }

        panel.add(campo, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }

    private void agregarCampoDescripcion(JPanel panel, String etiqueta, JTextArea campo,
                                         GridBagConstraints gbc, int y) {
        // Etiqueta
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEtiqueta.setForeground(colorPrincipal);
        panel.add(lblEtiqueta, gbc);

        // Campo
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JScrollPane scrollCampo = new JScrollPane(campo);
        scrollCampo.setPreferredSize(new Dimension(400, 60));
        scrollCampo.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panel.add(scrollCampo, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }

    private void agregarCampoArchivo(JPanel panel, String etiqueta, GridBagConstraints gbc, int y) {
        // Etiqueta
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEtiqueta.setForeground(colorPrincipal);
        panel.add(lblEtiqueta, gbc);

        // Panel para archivo
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel panelArchivo = new JPanel(new BorderLayout(8, 0));
        panelArchivo.setOpaque(false);

        btnArchivo.setBackground(colorPrincipal);
        btnArchivo.setForeground(Color.WHITE);
        btnArchivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnArchivo.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnArchivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnArchivo.setFocusPainted(false);

        lblArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblArchivo.setForeground(new Color(100, 100, 100));

        panelArchivo.add(btnArchivo, BorderLayout.WEST);
        panelArchivo.add(lblArchivo, BorderLayout.CENTER);

        panel.add(panelArchivo, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }

    private void agregarCampoAlumnos(JPanel panel, String etiqueta, GridBagConstraints gbc, int y) {
        // Etiqueta
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEtiqueta.setForeground(colorPrincipal);
        panel.add(lblEtiqueta, gbc);

        // Panel para alumnos con buscador
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel panelAlumnos = new JPanel(new BorderLayout(5, 5));
        panelAlumnos.setOpaque(false);

        // Panel superior con buscador y botón +
        JPanel panelBuscador = new JPanel(new BorderLayout(5, 0));
        panelBuscador.setOpaque(false);

        JTextField txtBuscadorAlumnos = new JTextField();
        txtBuscadorAlumnos.setPreferredSize(new Dimension(250, 28));
        txtBuscadorAlumnos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        txtBuscadorAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscadorAlumnos.setText("Buscar alumno por número de control o nombre...");
        txtBuscadorAlumnos.setForeground(Color.GRAY);

        // Placeholder behavior
        txtBuscadorAlumnos.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscadorAlumnos.getText().equals("Buscar alumno por número de control o nombre...")) {
                    txtBuscadorAlumnos.setText("");
                    txtBuscadorAlumnos.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscadorAlumnos.getText().isEmpty()) {
                    txtBuscadorAlumnos.setText("Buscar alumno por número de control o nombre...");
                    txtBuscadorAlumnos.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnAgregarAlumno = new JButton("+");
        btnAgregarAlumno.setFont(new Font("Arial", Font.BOLD, 14));
        btnAgregarAlumno.setForeground(Color.WHITE);
        btnAgregarAlumno.setBackground(colorPrincipal);
        btnAgregarAlumno.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        btnAgregarAlumno.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarAlumno.setToolTipText("Agregar alumno seleccionado");
        btnAgregarAlumno.setFocusPainted(false);
        btnAgregarAlumno.addActionListener(e -> mostrarBuscadorAlumnos());

        panelBuscador.add(txtBuscadorAlumnos, BorderLayout.CENTER);
        panelBuscador.add(btnAgregarAlumno, BorderLayout.EAST);

        // Panel inferior con botón eliminar
        JPanel panelEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));
        panelEliminar.setOpaque(false);

        JButton btnEliminarAlumno = new JButton("Eliminar Seleccionado");
        btnEliminarAlumno.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnEliminarAlumno.setForeground(Color.WHITE);
        btnEliminarAlumno.setBackground(new Color(220, 53, 69)); // Color rojo
        btnEliminarAlumno.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        btnEliminarAlumno.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminarAlumno.setToolTipText("Eliminar alumno seleccionado de la lista");
        btnEliminarAlumno.setFocusPainted(false);
        btnEliminarAlumno.addActionListener(e -> eliminarAlumnoSeleccionado());

        panelEliminar.add(btnEliminarAlumno);

        // Lista de alumnos seleccionados
        JScrollPane scrollAlumnos = new JScrollPane(listaAlumnos);
        scrollAlumnos.setPreferredSize(new Dimension(320, 70));
        scrollAlumnos.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));

        panelAlumnos.add(panelBuscador, BorderLayout.NORTH);
        panelAlumnos.add(scrollAlumnos, BorderLayout.CENTER);
        panelAlumnos.add(panelEliminar, BorderLayout.SOUTH);

        panel.add(panelAlumnos, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }

    private void agregarListaSeleccion(JPanel panel, String etiqueta, JList<?> lista,
                                       GridBagConstraints gbc, int y) {
        // Etiqueta
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEtiqueta.setForeground(colorPrincipal);
        panel.add(lblEtiqueta, gbc);

        // Panel con buscador para docentes
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel panelDocentes = new JPanel(new BorderLayout(5, 5));
        panelDocentes.setOpaque(false);

        // Panel superior con buscador y botón +
        JPanel panelBuscador = new JPanel(new BorderLayout(5, 0));
        panelBuscador.setOpaque(false);

        JTextField txtBuscadorDocentes = new JTextField();
        txtBuscadorDocentes.setPreferredSize(new Dimension(250, 28));
        txtBuscadorDocentes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        txtBuscadorDocentes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscadorDocentes.setText("Buscar docente por nombre...");
        txtBuscadorDocentes.setForeground(Color.GRAY);

        // Placeholder behavior
        txtBuscadorDocentes.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscadorDocentes.getText().equals("Buscar docente por nombre...")) {
                    txtBuscadorDocentes.setText("");
                    txtBuscadorDocentes.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscadorDocentes.getText().isEmpty()) {
                    txtBuscadorDocentes.setText("Buscar docente por nombre...");
                    txtBuscadorDocentes.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnAgregarDocente = new JButton("+");
        btnAgregarDocente.setFont(new Font("Arial", Font.BOLD, 14));
        btnAgregarDocente.setForeground(Color.WHITE);
        btnAgregarDocente.setBackground(colorPrincipal);
        btnAgregarDocente.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        btnAgregarDocente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarDocente.setToolTipText("Agregar " + etiqueta.toLowerCase().replace(":", ""));
        btnAgregarDocente.setFocusPainted(false);

        // Determinar qué método llamar según el tipo
        if (etiqueta.contains("Asesores")) {
            btnAgregarDocente.addActionListener(e -> mostrarBuscadorAsesores());
        } else if (etiqueta.contains("Revisores")) {
            btnAgregarDocente.addActionListener(e -> mostrarBuscadorRevisores());
        }

        panelBuscador.add(txtBuscadorDocentes, BorderLayout.CENTER);
        panelBuscador.add(btnAgregarDocente, BorderLayout.EAST);

        // Panel inferior con botón eliminar
        JPanel panelEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));
        panelEliminar.setOpaque(false);

        JButton btnEliminarDocente = new JButton("Eliminar Seleccionado");
        btnEliminarDocente.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnEliminarDocente.setForeground(Color.WHITE);
        btnEliminarDocente.setBackground(new Color(220, 53, 69)); // Color rojo
        btnEliminarDocente.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        btnEliminarDocente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminarDocente.setToolTipText("Eliminar " + etiqueta.toLowerCase().replace(":", "") + " seleccionado de la lista");
        btnEliminarDocente.setFocusPainted(false);

        // Determinar qué método llamar según el tipo
        if (etiqueta.contains("Asesores")) {
            btnEliminarDocente.addActionListener(e -> eliminarAsesorSeleccionado());
        } else if (etiqueta.contains("Revisores")) {
            btnEliminarDocente.addActionListener(e -> eliminarRevisorSeleccionado());
        }

        panelEliminar.add(btnEliminarDocente);

        // Lista de docentes seleccionados
        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setPreferredSize(new Dimension(320, 70));
        scrollLista.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));

        panelDocentes.add(panelBuscador, BorderLayout.NORTH);
        panelDocentes.add(scrollLista, BorderLayout.CENTER);
        panelDocentes.add(panelEliminar, BorderLayout.SOUTH);

        panel.add(panelDocentes, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }

    private void agregarCheckbox(JPanel panel, JCheckBox checkbox, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;

        checkbox.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checkbox.setForeground(colorPrincipal);
        checkbox.setOpaque(false);

        panel.add(checkbox, gbc);

        gbc.gridwidth = 1;
    }

    private void crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JButton btnGuardar = crearBotonCompacto("Guardar", colorPrincipal);
        JButton btnCancelar = crearBotonCompacto("Cancelar", new Color(120, 120, 130));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBotonCompacto(String texto, Color colorBoton) {
        JButton boton = new JButton(texto) {
            private boolean isMouseOver = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isMouseOver = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isMouseOver = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color colorBase = isMouseOver ? colorBoton.brighter() : colorBoton;
                Color colorFinal = colorBoton.darker();

                // Sombra
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(1, 2, getWidth()-1, getHeight()-1, 15, 15);

                // Gradiente
                GradientPaint gradiente = new GradientPaint(
                        0, 0, colorBase, 0, getHeight(), colorFinal
                );
                g2d.setPaint(gradiente);
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-2, 15, 15);

                // Borde
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(colorBoton.darker());
                g2d.drawRoundRect(0, 0, getWidth()-2, getHeight()-3, 15, 15);

                // Texto
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D textBounds = fm.getStringBounds(texto, g2d);
                int textX = (getWidth() - (int) textBounds.getWidth()) / 2;
                int textY = (getHeight() - (int) textBounds.getHeight()) / 2 + fm.getAscent();

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2d.drawString(texto, textX, textY);
            }
        };

        boton.setPreferredSize(new Dimension(110, 35));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(false);

        return boton;
    }

    private void cargarDatos() {
        // Empresas de ejemplo
        Empresa empresa1 = new Empresa("Empresa 1", "Dir 1", "Resp 1", "123", "emp1@mail.com", "RFC1");
        Empresa empresa2 = new Empresa("Empresa 2", "Dir 2", "Resp 2", "456", "emp2@mail.com", "RFC2");
        comboEmpresa.addItem(empresa1);
        comboEmpresa.addItem(empresa2);

        // Docentes de ejemplo
        Docente docente1 = new Docente();
        docente1.setNombre("Dr. Juan Pérez");
        Docente docente2 = new Docente();
        docente2.setNombre("Dra. María García");

        modeloAsesores.addElement(docente1);
        modeloAsesores.addElement(docente2);
        modeloRevisores.addElement(docente1);
        modeloRevisores.addElement(docente2);

        // Alumnos de ejemplo
        String[] alumnosEjemplo = {"19680001 - Ana López", "19680002 - Carlos Ramírez"};
        for (String alumno : alumnosEjemplo) {
            modeloAlumnos.addElement(alumno);
        }
    }

    private void configurarEventos() {
        comboEmpresa.addActionListener(e -> {
            Empresa empresaSeleccionada = (Empresa) comboEmpresa.getSelectedItem();
            if (empresaSeleccionada != null) {
                txtCorreoEmpresa.setText(empresaSeleccionada.getCorreo());
            }
        });

        btnArchivo.addActionListener(e -> seleccionarArchivo());
    }

    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Documentos (*.pdf, *.doc, *.docx)", "pdf", "doc", "docx"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            lblArchivo.setText(archivoSeleccionado.getName());
        }
    }

    private void agregarAlumnoManualmente() {
        JDialog dialogo = new JDialog(this, "Agregar Alumno", true);
        dialogo.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField txtNumControl = new JTextField(15);
        JTextField txtNombre = new JTextField(25);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Número de Control:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNumControl, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAceptar = new JButton("Agregar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.setBackground(colorPrincipal);
        btnAceptar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(200, 200, 200));

        btnAceptar.addActionListener(e -> {
            String numControl = txtNumControl.getText().trim();
            String nombre = txtNombre.getText().trim();

            if (numControl.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Complete todos los campos");
                return;
            }

            modeloAlumnos.addElement(numControl + " - " + nombre);
            dialogo.dispose();
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void mostrarBuscadorAlumnos() {
        JDialog dialogo = new JDialog(this, "Seleccionar Residentes", true);
        dialogo.setSize(700, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Buscador en la parte superior
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        JTextField txtBusqueda = new JTextField();
        txtBusqueda.setPreferredSize(new Dimension(0, 35));
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBusqueda.setForeground(Color.GRAY);
        txtBusqueda.setText("Escriba para buscar por número de control o nombre...");

        JLabel lblBuscar = new JLabel("🔍 Buscar Residente:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBuscar.setForeground(colorPrincipal);

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBusqueda, BorderLayout.CENTER);

        // Datos completos de residentes (simulando base de datos)
        String[][] datosResidentes = {
                {"19680001", "Ana López Martínez", "Ingeniería en Sistemas", "ana.lopez@estudiantes.itm.edu.mx", "9° Semestre"},
                {"19680002", "Carlos Ramírez Sánchez", "Ingeniería Industrial", "carlos.ramirez@estudiantes.itm.edu.mx", "8° Semestre"},
                {"19680003", "María González Hernández", "Ingeniería Química", "maria.gonzalez@estudiantes.itm.edu.mx", "9° Semestre"},
                {"19680004", "José Pérez García", "Ingeniería en Sistemas", "jose.perez@estudiantes.itm.edu.mx", "8° Semestre"},
                {"19680005", "Laura Rodríguez López", "Ingeniería Mecánica", "laura.rodriguez@estudiantes.itm.edu.mx", "9° Semestre"},
                {"19680006", "David Martínez Ruiz", "Ingeniería Electrónica", "david.martinez@estudiantes.itm.edu.mx", "8° Semestre"},
                {"19680007", "Carmen Sánchez Torres", "Ingeniería Industrial", "carmen.sanchez@estudiantes.itm.edu.mx", "9° Semestre"},
                {"19680008", "Miguel Hernández Vega", "Ingeniería en Sistemas", "miguel.hernandez@estudiantes.itm.edu.mx", "8° Semestre"},
                {"19680009", "Patricia Morales Castro", "Ingeniería Química", "patricia.morales@estudiantes.itm.edu.mx", "9° Semestre"},
                {"19680010", "Roberto Silva Mendoza", "Ingeniería Mecánica", "roberto.silva@estudiantes.itm.edu.mx", "8° Semestre"}
        };

        // Tabla para mostrar los datos
        String[] columnas = {"No. Control", "Nombre Completo", "Carrera", "Correo", "Semestre"};
        javax.swing.table.DefaultTableModel modeloTabla = new javax.swing.table.DefaultTableModel(datosResidentes, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(colorPrincipal);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setGridColor(new Color(220, 220, 220));

        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);  // No. Control
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150); // Carrera
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200); // Correo
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80);  // Semestre

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                "Residentes Disponibles - Seleccione uno o varios",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), colorPrincipal
        ));

        // Funcionalidad de búsqueda en tiempo real
        txtBusqueda.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBusqueda.getText().equals("Escriba para buscar por número de control o nombre...")) {
                    txtBusqueda.setText("");
                    txtBusqueda.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtBusqueda.getText().trim().isEmpty()) {
                    txtBusqueda.setText("Escriba para buscar por número de control o nombre...");
                    txtBusqueda.setForeground(Color.GRAY);
                }
            }
        });

        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter =
                new javax.swing.table.TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        txtBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBusqueda.getText().trim();
                if (texto.equals("Escriba para buscar por número de control o nombre...") || texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        JButton btnAgregar = new JButton("Agregar Seleccionados");
        JButton btnCancelar = new JButton("Cancelar");

        btnAgregar.setBackground(colorPrincipal);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(200, 200, 200));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);

        btnAgregar.addActionListener(e -> {
            int[] filasSeleccionadas = tabla.getSelectedRows();
            for (int fila : filasSeleccionadas) {
                int filaModelo = tabla.convertRowIndexToModel(fila);
                String noControl = (String) modeloTabla.getValueAt(filaModelo, 0);
                String nombre = (String) modeloTabla.getValueAt(filaModelo, 1);
                String residente = noControl + " - " + nombre;

                if (!modeloAlumnos.contains(residente)) {
                    modeloAlumnos.addElement(residente);
                }
            }
            dialogo.dispose();
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        dialogo.add(panelPrincipal);
        dialogo.setVisible(true);
    }

    private void mostrarBuscadorAsesores() {
        JDialog dialogo = new JDialog(this, "Seleccionar Asesores", true);
        dialogo.setSize(750, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Buscador en la parte superior
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        JTextField txtBusqueda = new JTextField();
        txtBusqueda.setPreferredSize(new Dimension(0, 35));
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBusqueda.setForeground(Color.GRAY);
        txtBusqueda.setText("Escriba para buscar por nombre o especialidad...");

        JLabel lblBuscar = new JLabel("🔍 Buscar Asesor:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBuscar.setForeground(colorPrincipal);

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBusqueda, BorderLayout.CENTER);

        // Datos completos de docentes asesores
        String[][] datosAsesores = {
                {"Dr.", "Juan Pérez García", "Ingeniería en Sistemas", "juan.perez@docentes.itm.edu.mx", "Sistemas Distribuidos"},
                {"Dra.", "María González López", "Ingeniería Industrial", "maria.gonzalez@docentes.itm.edu.mx", "Optimización de Procesos"},
                {"M.C.", "Carlos Rodríguez Sánchez", "Ingeniería Química", "carlos.rodriguez@docentes.itm.edu.mx", "Procesos Químicos"},
                {"Dra.", "Ana Martínez Hernández", "Ingeniería en Sistemas", "ana.martinez@docentes.itm.edu.mx", "Inteligencia Artificial"},
                {"Dr.", "José Luis Torres Vega", "Ingeniería Mecánica", "jose.torres@docentes.itm.edu.mx", "Diseño Mecánico"},
                {"M.C.", "Laura Sánchez Ruiz", "Ingeniería Electrónica", "laura.sanchez@docentes.itm.edu.mx", "Microelectrónica"},
                {"Dr.", "Miguel Hernández Castro", "Ingeniería Industrial", "miguel.hernandez@docentes.itm.edu.mx", "Calidad y Productividad"},
                {"Dra.", "Carmen López Martín", "Ingeniería Química", "carmen.lopez@docentes.itm.edu.mx", "Biotecnología"},
                {"M.C.", "Ricardo Morales Vega", "Ingeniería en Sistemas", "ricardo.morales@docentes.itm.edu.mx", "Redes y Seguridad"},
                {"Dr.", "Patricia Silva Mendoza", "Ingeniería Mecánica", "patricia.silva@docentes.itm.edu.mx", "Automatización"}
        };

        // Tabla para mostrar los datos
        String[] columnas = {"Grado", "Nombre Completo", "Departamento", "Correo", "Especialidad"};
        javax.swing.table.DefaultTableModel modeloTabla = new javax.swing.table.DefaultTableModel(datosAsesores, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(colorPrincipal);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setGridColor(new Color(220, 220, 220));

        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);  // Grado
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(140); // Departamento
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200); // Correo
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150); // Especialidad

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                "Docentes Disponibles para Asesoría - Seleccione uno o varios",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), colorPrincipal
        ));

        // Funcionalidad de búsqueda
        txtBusqueda.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBusqueda.getText().equals("Escriba para buscar por nombre o especialidad...")) {
                    txtBusqueda.setText("");
                    txtBusqueda.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtBusqueda.getText().trim().isEmpty()) {
                    txtBusqueda.setText("Escriba para buscar por nombre o especialidad...");
                    txtBusqueda.setForeground(Color.GRAY);
                }
            }
        });

        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter =
                new javax.swing.table.TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        txtBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBusqueda.getText().trim();
                if (texto.equals("Escriba para buscar por nombre o especialidad...") || texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        JButton btnAgregar = new JButton("Agregar Seleccionados");
        JButton btnCancelar = new JButton("Cancelar");

        btnAgregar.setBackground(colorPrincipal);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(200, 200, 200));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);

        btnAgregar.addActionListener(e -> {
            int[] filasSeleccionadas = tabla.getSelectedRows();
            for (int fila : filasSeleccionadas) {
                int filaModelo = tabla.convertRowIndexToModel(fila);
                String grado = (String) modeloTabla.getValueAt(filaModelo, 0);
                String nombre = (String) modeloTabla.getValueAt(filaModelo, 1);

                Docente docente = new Docente();
                docente.setNombre(grado + " " + nombre);

                if (!modeloAsesores.contains(docente)) {
                    modeloAsesores.addElement(docente);
                }
            }
            dialogo.dispose();
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        dialogo.add(panelPrincipal);
        dialogo.setVisible(true);
    }

    private void mostrarBuscadorRevisores() {
        JDialog dialogo = new JDialog(this, "Seleccionar Revisores", true);
        dialogo.setSize(750, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Buscador en la parte superior
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        JTextField txtBusqueda = new JTextField();
        txtBusqueda.setPreferredSize(new Dimension(0, 35));
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBusqueda.setForeground(Color.GRAY);
        txtBusqueda.setText("Escriba para buscar por nombre o especialidad...");

        JLabel lblBuscar = new JLabel("🔍 Buscar Revisor:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBuscar.setForeground(colorPrincipal);

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBusqueda, BorderLayout.CENTER);

        // Datos completos de docentes revisores (diferentes a asesores)
        String[][] datosRevisores = {
                {"Dr.", "Roberto Vázquez Morales", "Ingeniería en Sistemas", "roberto.vazquez@docentes.itm.edu.mx", "Base de Datos"},
                {"Dra.", "Patricia Ruiz González", "Ingeniería Industrial", "patricia.ruiz@docentes.itm.edu.mx", "Gestión de Proyectos"},
                {"M.C.", "Fernando Castro López", "Ingeniería Química", "fernando.castro@docentes.itm.edu.mx", "Control de Procesos"},
                {"Dra.", "Silvia Moreno Sánchez", "Ingeniería en Sistemas", "silvia.moreno@docentes.itm.edu.mx", "Desarrollo de Software"},
                {"Dr.", "Alejandro Jiménez Torres", "Ingeniería Mecánica", "alejandro.jimenez@docentes.itm.edu.mx", "Manufactura"},
                {"M.C.", "Mónica Herrera Vega", "Ingeniería Electrónica", "monica.herrera@docentes.itm.edu.mx", "Telecomunicaciones"},
                {"Dr.", "Ricardo Mendoza Castro", "Ingeniería Industrial", "ricardo.mendoza@docentes.itm.edu.mx", "Logística"},
                {"Dra.", "Elena Vargas Martín", "Ingeniería Química", "elena.vargas@docentes.itm.edu.mx", "Medio Ambiente"},
                {"M.C.", "Gabriel Ortega Ruiz", "Ingeniería en Sistemas", "gabriel.ortega@docentes.itm.edu.mx", "Ciberseguridad"},
                {"Dr.", "Claudia Ramírez Torres", "Ingeniería Mecánica", "claudia.ramirez@docentes.itm.edu.mx", "Energías Renovables"}
        };

        // Tabla para mostrar los datos
        String[] columnas = {"Grado", "Nombre Completo", "Departamento", "Correo", "Especialidad"};
        javax.swing.table.DefaultTableModel modeloTabla = new javax.swing.table.DefaultTableModel(datosRevisores, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(colorPrincipal);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setGridColor(new Color(220, 220, 220));

        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);  // Grado
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(140); // Departamento
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200); // Correo
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150); // Especialidad

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorPrincipal, 2),
                "Docentes Disponibles para Revisión - Seleccione uno o varios",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), colorPrincipal
        ));

        // Funcionalidad de búsqueda
        txtBusqueda.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBusqueda.getText().equals("Escriba para buscar por nombre o especialidad...")) {
                    txtBusqueda.setText("");
                    txtBusqueda.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtBusqueda.getText().trim().isEmpty()) {
                    txtBusqueda.setText("Escriba para buscar por nombre o especialidad...");
                    txtBusqueda.setForeground(Color.GRAY);
                }
            }
        });

        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter =
                new javax.swing.table.TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        txtBusqueda.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBusqueda.getText().trim();
                if (texto.equals("Escriba para buscar por nombre o especialidad...") || texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        JButton btnAgregar = new JButton("Agregar Seleccionados");
        JButton btnCancelar = new JButton("Cancelar");

        btnAgregar.setBackground(colorPrincipal);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(200, 200, 200));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);

        btnAgregar.addActionListener(e -> {
            int[] filasSeleccionadas = tabla.getSelectedRows();
            for (int fila : filasSeleccionadas) {
                int filaModelo = tabla.convertRowIndexToModel(fila);
                String grado = (String) modeloTabla.getValueAt(filaModelo, 0);
                String nombre = (String) modeloTabla.getValueAt(filaModelo, 1);

                Docente docente = new Docente();
                docente.setNombre(grado + " " + nombre);

                if (!modeloRevisores.contains(docente)) {
                    modeloRevisores.addElement(docente);
                }
            }
            dialogo.dispose();
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        dialogo.add(panelPrincipal);
        dialogo.setVisible(true);
    }

    // Métodos para eliminar elementos seleccionados de las listas
    private void eliminarAlumnoSeleccionado() {
        int[] indicesSeleccionados = listaAlumnos.getSelectedIndices();
        if (indicesSeleccionados.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione al menos un alumno para eliminar.",
                    "Selección requerida",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar " + indicesSeleccionados.length + " alumno(s) seleccionado(s)?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar en orden inverso para evitar problemas de índices
            for (int i = indicesSeleccionados.length - 1; i >= 0; i--) {
                modeloAlumnos.removeElementAt(indicesSeleccionados[i]);
            }
        }
    }

    private void eliminarAsesorSeleccionado() {
        int[] indicesSeleccionados = listaAsesores.getSelectedIndices();
        if (indicesSeleccionados.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione al menos un asesor para eliminar.",
                    "Selección requerida",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar " + indicesSeleccionados.length + " asesor(es) seleccionado(s)?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar en orden inverso para evitar problemas de índices
            for (int i = indicesSeleccionados.length - 1; i >= 0; i--) {
                modeloAsesores.removeElementAt(indicesSeleccionados[i]);
            }
        }
    }

    private void eliminarRevisorSeleccionado() {
        int[] indicesSeleccionados = listaRevisores.getSelectedIndices();
        if (indicesSeleccionados.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione al menos un revisor para eliminar.",
                    "Selección requerida",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar " + indicesSeleccionados.length + " revisor(es) seleccionado(s)?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar en orden inverso para evitar problemas de índices
            for (int i = indicesSeleccionados.length - 1; i >= 0; i--) {
                modeloRevisores.removeElementAt(indicesSeleccionados[i]);
            }
        }
    }

    // Métodos públicos para prellenar campos en modo edición
    public void setNombreProyecto(String nombre) {
        if (txtNombreProyecto != null) {
            txtNombreProyecto.setText(nombre);
        }
    }

    public void setDescripcionProyecto(String descripcion) {
        if (txtDescripcion != null) {
            txtDescripcion.setText(descripcion);
        }
    }

    public void setAlumnosTexto(String alumnos) {
        if (modeloAlumnos != null && alumnos != null && !alumnos.trim().isEmpty()) {
            String[] alumnosArray = alumnos.split(",");
            modeloAlumnos.clear();
            for (String alumno : alumnosArray) {
                modeloAlumnos.addElement(alumno.trim());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FormularioAnteproyecto().setVisible(true);
        });
    }
}
