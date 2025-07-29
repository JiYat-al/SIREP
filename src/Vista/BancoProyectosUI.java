package Vista;

import Controlador.ControladorProyectos;
import Modelo.ConsultasEmpresa;
import Modelo.Empresa;
import Modelo.ProyectoDAO;
import Modelo.Proyecto;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import Modelo.EmpresaDAO;

public class BancoProyectosUI extends JFrame {
    private ControladorProyectos controladorProyectos = new ControladorProyectos(new ProyectoDAO());
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private ArrayList<Proyecto> listaProyectos = new ArrayList<>();

    public BancoProyectosUI() {
        setTitle("Banco de Proyectos - SIREP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        // Panel principal con fondo degradado EXACTO como VistaRegistros
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

        // Crear barra lateral EXACTA como VistaRegistros
        crearBarraLateral(mainPanel);

        // Panel de contenido principal
        JPanel panelContenido = crearPanelContenido();

        // Crear botón regresar EXACTO como VistaRegistros
        crearBotonRegresar(mainPanel);

        mainPanel.add(panelContenido, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Cargar datos
        cargarTablaBancoProyectos();
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
                "B<br>A<br>N<br>C<br>O<br><br>" +
                "D<br>E<br><br>" +
                "P<br>R<br>O<br>Y<br>E<br>C<br>T<br>O<br>S</div></html>");
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
        panelCentral.add(new JScrollPane(tabla), BorderLayout.CENTER);

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

        JLabel lblTitulo = new JLabel("Banco de Proyectos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        header.add(lblTitulo, BorderLayout.WEST);

        // Panel de botones en el header
        JPanel panelBotonesHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelBotonesHeader.setOpaque(false);

        JButton btnNuevo = crearBotonAccion("Nuevo Proyecto", new Color(63, 81, 181));
        btnNuevo.addActionListener(e -> mostrarDialogoNuevo());

        panelBotonesHeader.add(btnNuevo);
        header.add(panelBotonesHeader, BorderLayout.EAST);
        return header;
    }

    private void configurarTabla() {
        String[] columnas = {"ID", "Nombre", "Descripción", "Origen"};
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);

        // Ocultar columna ID
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(35);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(new Color(230, 230, 250));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar header
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(new Color(220, 219, 245));
        tabla.getTableHeader().setForeground(colorPrincipal);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));
        tabla.getTableHeader().setReorderingAllowed(false); // Evitar que se muevan las columnas
        ((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Efectos de selección
        tabla.setSelectionBackground(new Color(220, 219, 245));
        tabla.setSelectionForeground(new Color(60, 60, 100));
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
        btnEditar.addActionListener(e -> editarProyecto());
        btnEliminar.addActionListener(e -> eliminarProyecto());
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
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        if (isEnabled()) { hover = true; repaint(); }
                    }
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
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hover = true;
                        repaint();
                    }
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
        String ayuda = "Sistema de Banco de Proyectos SIREP\n\n" +
                "• Nuevo Proyecto: Registra un proyecto nuevo\n" +
                "• Editar: Modifica el proyecto seleccionado\n" +
                "• Eliminar: Elimina el proyecto seleccionado\n" +
                "• Ver Información: Muestra detalles del proyecto\n" +
                "• Flecha izquierda: Regresa al menú principal\n";

        JOptionPane.showMessageDialog(this, ayuda, "Ayuda - Banco de Proyectos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarTablaBancoProyectos() {
        modelo.setRowCount(0);
        listaProyectos = new ArrayList<>(controladorProyectos.ObProyectosBanco());
        for (Proyecto p : listaProyectos) {
            modelo.addRow(new Object[]{p.getId_proyecto(), p.getNombre(), p.getDescripcion(), p.getNombreOrigen()});
        }
    }

    private void mostrarDialogoNuevo() {
        JDialog dialogo = new JDialog(this, "Nuevo Proyecto", true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel principal con mejor diseño
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel titulo = new JLabel("Registrar Nuevo Proyecto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorPrincipal);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titulo);

        // Campos organizados
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Crear campos
        JTextField txtNombre = new JTextField();
        JTextArea txtDescripcion = new JTextArea(3, 20); // líneas y columnas
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(200, 60));

        JTextField txtDuracion = new JTextField();
        JSpinner spinnerAlumnos = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));

        JComboBox<Empresa> comboEmpresa = new JComboBox<>();
        ArrayList<Empresa> empresas = EmpresaDAO.recuperarDatos();
        for (Empresa empresa : empresas) {
            comboEmpresa.addItem(empresa);
        }

        // Añadir campos con etiquetas usando GridBagLayout
        camposPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        camposPanel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        camposPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        camposPanel.add(scrollDesc, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        camposPanel.add(new JLabel("Duración (meses):"), gbc);
        gbc.gridx = 1;
        camposPanel.add(txtDuracion, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        camposPanel.add(new JLabel("Número de alumnos:"), gbc);
        gbc.gridx = 1;
        camposPanel.add(spinnerAlumnos, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        camposPanel.add(new JLabel("Empresa:"), gbc);
        gbc.gridx = 1;
        camposPanel.add(comboEmpresa, gbc);

        mainPanel.add(camposPanel);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(colorPrincipal);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnGuardar.setFocusPainted(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancelar.setFocusPainted(false);

        // Eventos de botones
        btnGuardar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String duracion = txtDuracion.getText().trim();
            int numAlumnos = (int) spinnerAlumnos.getValue();
            Empresa empresa = (Empresa) comboEmpresa.getSelectedItem();

            if (nombre.isEmpty() || descripcion.isEmpty() || duracion.isEmpty() || empresa == null) {
                JOptionPane.showMessageDialog(dialogo, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.length() < 2 || nombre.matches("\\d+") || descripcion.length() < 2 || descripcion.matches("\\d+")) {
                JOptionPane.showMessageDialog(dialogo, "Nombre y descripción deben tener al menos dos caracteres y no ser solo números.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Integer.parseInt(duracion);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "La duración debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Proyecto proyecto = new Proyecto(nombre, descripcion, duracion, numAlumnos, empresa.getId());

            if (controladorProyectos.NuevoProyectoBanco(proyecto)) {
                JOptionPane.showMessageDialog(dialogo, "Proyecto registrado exitosamente.");
                dialogo.dispose();
                cargarTablaBancoProyectos();
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al registrar el proyecto.");
            }
        });

        btnCancelar.addActionListener(ev -> dialogo.dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        mainPanel.add(panelBotones);

        dialogo.add(mainPanel, BorderLayout.CENTER);
        dialogo.setVisible(true);
    }


    private void editarProyecto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;


        Proyecto proyecto = listaProyectos.get(fila);
        String[] datos = controladorProyectos.InformacionProyectoBanco(proyecto.getId_proyecto());
        if (datos == null) return;

        JDialog dialogo = new JDialog(this, "Editar Proyecto", true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel principal con mejor diseño
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel titulo = new JLabel("Editar Proyecto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorPrincipal);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titulo);

        // Campos organizados
        JPanel camposPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        camposPanel.setBackground(Color.WHITE);

        // Crear campos
        JTextField txtNombre = new JTextField(datos[0]);
        JTextArea txtDescripcion = new JTextArea(datos[1], 2, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(200, 60));

        JTextField txtDuracion = new JTextField(datos[2]);
        JSpinner spinnerAlumnos = new JSpinner(new SpinnerNumberModel(Integer.parseInt(datos[3]), 1, 100, 1));

        JComboBox<Empresa> comboEmpresa = new JComboBox<>();
        ArrayList<Empresa> empresas = EmpresaDAO.recuperarDatos();
        for (Empresa empresa : empresas) {
            comboEmpresa.addItem(empresa);
        }

        JComboBox<String> comboEstatus = new JComboBox<>();
        comboEstatus.addItem("Disponible");
        comboEstatus.addItem("Cancelado");
        comboEstatus.setSelectedItem(datos[5].trim());

        // Añadir campos con etiquetas
        camposPanel.add(new JLabel("Nombre:"));
        camposPanel.add(txtNombre);
        camposPanel.add(new JLabel("Descripción:"));
        camposPanel.add(scrollDesc);
        camposPanel.add(new JLabel("Duración (meses):"));
        camposPanel.add(txtDuracion);
        camposPanel.add(new JLabel("Número de alumnos:"));
        camposPanel.add(spinnerAlumnos);
        camposPanel.add(new JLabel("Empresa:"));
        camposPanel.add(comboEmpresa);
        camposPanel.add(new JLabel("Estatus:"));
        camposPanel.add(comboEstatus);

        mainPanel.add(camposPanel);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(colorPrincipal);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnGuardar.setFocusPainted(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancelar.setFocusPainted(false);

        // Eventos de botones
        btnGuardar.addActionListener(ev -> {
            if (txtNombre.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty() ||
                    txtDuracion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Integer.parseInt(txtDuracion.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "La duración debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Proyecto proyectoActualizado = new Proyecto();
            proyectoActualizado.setId_proyecto(proyecto.getId_proyecto());
            proyectoActualizado.setNombre(txtNombre.getText().trim());
            proyectoActualizado.setDescripcion(txtDescripcion.getText().trim());
            proyectoActualizado.setDuracion(txtDuracion.getText().trim());
            proyectoActualizado.setNumero_alumnos((Integer) spinnerAlumnos.getValue());

            Empresa empresa = (Empresa) comboEmpresa.getSelectedItem();
            proyectoActualizado.setId_empresa(empresa.getId());

            String estatus = comboEstatus.getSelectedItem().toString();
            proyectoActualizado.setEstado(!estatus.equals("Cancelado"));

            if (controladorProyectos.EditarInformacionProyectoResidencia(proyectoActualizado)) {
                JOptionPane.showMessageDialog(dialogo, "Proyecto actualizado exitosamente.");
                dialogo.dispose();
                cargarTablaBancoProyectos();
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al actualizar el proyecto.");
            }
        });

        btnCancelar.addActionListener(ev -> dialogo.dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        mainPanel.add(panelBotones);

        dialogo.add(mainPanel, BorderLayout.CENTER);
        dialogo.setVisible(true);
    }

    private void eliminarProyecto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        int idProyecto = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
        String nombre = tabla.getValueAt(fila, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el proyecto '" + nombre + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controladorProyectos.Baja(idProyecto)) {
                JOptionPane.showMessageDialog(this, "Proyecto eliminado exitosamente.");
                cargarTablaBancoProyectos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el proyecto.");
            }
        }
    }

    private void verInformacion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        int idProyecto = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
        String[] datos = controladorProyectos.InformacionProyectoBanco(idProyecto);

        if (datos != null) {
            StringBuilder info = new StringBuilder();
            info.append("Información del Proyecto\n\n");
            info.append("Nombre: ").append(datos[0]).append("\n");
            info.append("Descripción: ").append(datos[1]).append("\n");
            info.append("Duración: ").append(datos[2]).append(" meses\n");
            info.append("Número de Alumnos: ").append(datos[3]).append("\n");
            info.append("Empresa: ").append(datos[4]).append("\n");
            info.append("Estatus: ").append(datos[5]).append("\n");
            info.append("Origen: ").append(datos[6]).append("\n");

            JOptionPane.showMessageDialog(this, info.toString(), "Información del Proyecto", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BancoProyectosUI().setVisible(true);
        });
    }
}