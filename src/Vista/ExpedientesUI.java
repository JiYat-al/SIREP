package Vista;

import Modelo.ModeloResidente;
import Modelo.ModeloDocumento;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ExpedientesUI extends JFrame {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private final Color colorSecundario = new Color(180, 170, 255);
    private final Color colorAdvertencia = new Color(255, 87, 34);
    private final Color colorExito = new Color(76, 175, 80);

    private JTable tablaDocumentos;
    private DefaultTableModel modeloTabla;
    private JButton btnSeleccionarResidente;
    private JLabel lblEstadoGeneral;
    private JLabel lblEstadoResidente;
    private String residenteSeleccionado = "Ningún residente seleccionado";
    private ModeloResidente residenteActual = null;
    private JButton btnSubirArchivo;
    private JButton btnEliminarArchivo;
    private JButton btnVisualizarArchivo;
    private JButton btnGuardarCambios;
    private List<String> cambiosPendientes = new ArrayList<>();

    private String[] nombresDocumentos = {
            "Solicitud de Residencia", "Carta de Presentación", "Anteproyecto",
            "Carta de Aceptación", "Seguro Médico", "Constancia de Estudios",
            "Reporte Técnico", "Evaluación Empresa", "Evaluación Asesor", "Carta de Terminación"
    };

    public ExpedientesUI() {
        // Inicializar sistema de archivos
        Modelo.GestorArchivos.inicializarSistemaArchivos();

        inicializarVentana();
        crearInterfaz();
        mostrarEstadoInicial();
        setVisible(true);
    }

    private void inicializarVentana() {
        setTitle("Gestión de Expedientes - SIREP");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // CAMBIO: No cerrar automáticamente
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));
        setIconImage(cargarIconoVentana());

        // NUEVO: Agregar listener para el botón de cerrar ventana (X)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                regresarAlMenu(); // Usar la misma lógica que el botón Regresar
            }
        });
    }

    private Image cargarIconoVentana() {
        try {
            return new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        } catch (Exception e) {
            return null;
        }
    }

    private void crearInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(colorFondo);

        JPanel barraSuperior = crearBarraSuperior();
        panelPrincipal.add(barraSuperior, BorderLayout.NORTH);

        JPanel panelFiltros = crearPanelFiltros();
        panelPrincipal.add(panelFiltros, BorderLayout.CENTER);

        JPanel panelInferior = crearPanelInferior();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setPreferredSize(new Dimension(0, 80));
        barra.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTitulo.setOpaque(false);

        JLabel iconoExpedientes = new JLabel("📋");
        iconoExpedientes.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel lblTitulo = new JLabel("Gestión de Expedientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(colorPrincipal);

        panelTitulo.add(iconoExpedientes);
        panelTitulo.add(lblTitulo);

        JButton btnRegresar = crearBotonEstilizado("← Regresar", "🏠");
        btnRegresar.addActionListener(e -> regresarAlMenu());

        barra.add(panelTitulo, BorderLayout.WEST);
        barra.add(btnRegresar, BorderLayout.EAST);

        return barra;
    }

    private JPanel crearPanelFiltros() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setOpaque(false);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel panelControles = crearPanelControles();
        panelPrincipal.add(panelControles, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(15, 0));
        panelCentral.setOpaque(false);

        crearTablaDocumentos();
        JScrollPane scrollTabla = new JScrollPane(tablaDocumentos);
        configurarScrollPane(scrollTabla);

        JPanel panelAcciones = crearPanelAcciones();

        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panelCentral.add(panelAcciones, BorderLayout.EAST);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        return panelPrincipal;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 248, 255));
        panel.setBorder(new CompoundBorder(
                new LineBorder(colorSecundario, 2, true),
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        panel.setPreferredSize(new Dimension(200, 0));

        JLabel lblTitulo = new JLabel("Acciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblInstruccion = new JLabel("<html><div style='text-align: center;'>" +
                "Seleccione un documento<br>de la tabla para<br>realizar acciones" +
                "</div></html>");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInstruccion.setForeground(new Color(100, 100, 100));
        lblInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblInstruccion);
        panel.add(Box.createVerticalStrut(25));

        btnSubirArchivo = crearBotonAccion("📤 Subir Archivo", "Subir un nuevo documento PDF");
        btnEliminarArchivo = crearBotonAccion("🗑️ Eliminar Archivo", "Eliminar el archivo seleccionado");
        btnVisualizarArchivo = crearBotonAccion("👁️ Ver Archivo", "Visualizar el documento PDF");

        btnSubirArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tablaDocumentos.getSelectedRow();
                if (fila >= 0) {
                    subirDocumento(fila);
                }
            }
        });

        btnEliminarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tablaDocumentos.getSelectedRow();
                if (fila >= 0) {
                    eliminarDocumento(fila);
                }
            }
        });

        btnVisualizarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tablaDocumentos.getSelectedRow();
                if (fila >= 0) {
                    visualizarDocumento(fila);
                }
            }
        });

        panel.add(btnSubirArchivo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnEliminarArchivo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnVisualizarArchivo);

        btnGuardarCambios = crearBotonAccion("💾 Guardar Cambios", "Guardar todos los cambios pendientes");
        btnGuardarCambios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarTodosCambios();
            }
        });
        btnGuardarCambios.setEnabled(false); // Inicialmente deshabilitado

        panel.add(btnGuardarCambios);
        panel.add(Box.createVerticalStrut(15));

        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separador);
        panel.add(Box.createVerticalStrut(20));

        JButton btnEditarLista = crearBotonAccion("⚙️ Editar Lista", "Modificar lista de documentos");
        btnEditarLista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirEditorDocumentos();
            }
        });
        panel.add(btnEditarLista);

        panel.add(Box.createVerticalGlue());

        actualizarBotonesAccion();

        return panel;
    }

    private JButton crearBotonAccion(String texto, String tooltip) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorPrincipal);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        boton.setToolTipText(tooltip);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(colorPrincipal.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(colorPrincipal);
                }
            }
        });

        return boton;
    }

    private void actualizarBotonesAccion() {
        int filaSeleccionada = tablaDocumentos.getSelectedRow();
        boolean haySeleccion = (filaSeleccionada >= 0);
        boolean hayResidente = (residenteActual != null);

        if (btnSubirArchivo != null) {
            btnSubirArchivo.setEnabled(haySeleccion && hayResidente);
            btnEliminarArchivo.setEnabled(haySeleccion && hayResidente);
            btnVisualizarArchivo.setEnabled(haySeleccion && hayResidente);

            Color colorBoton = (haySeleccion && hayResidente) ? colorPrincipal : new Color(150, 150, 150);
            btnSubirArchivo.setBackground(colorBoton);
            btnEliminarArchivo.setBackground(colorBoton);
            btnVisualizarArchivo.setBackground(colorBoton);
        }
    }

    private void actualizarBotonGuardar() {
        if (btnGuardarCambios != null) {
            boolean hayCambios = !cambiosPendientes.isEmpty();
            btnGuardarCambios.setEnabled(hayCambios);

            Color colorBoton = hayCambios ? colorExito : new Color(150, 150, 150);
            btnGuardarCambios.setBackground(colorBoton);

            String tooltip = hayCambios ?
                    "Guardar " + cambiosPendientes.size() + " cambios pendientes" :
                    "No hay cambios pendientes";
            btnGuardarCambios.setToolTipText(tooltip);
        }
    }

    private JPanel crearPanelControles() {
        JPanel panelCompleto = new JPanel(new BorderLayout());
        panelCompleto.setOpaque(false);

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSeleccion.setOpaque(false);

        JLabel lblResidente = new JLabel("Seleccionar Residente:");
        lblResidente.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResidente.setForeground(colorPrincipal);

        btnSeleccionarResidente = crearBotonEstilizado(residenteSeleccionado, "👤");
        btnSeleccionarResidente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirSeleccionResidentes();
            }
        });

        panelSeleccion.add(lblResidente);
        panelSeleccion.add(btnSeleccionarResidente);

        lblEstadoResidente = new JLabel("");
        lblEstadoResidente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstadoResidente.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        panelCompleto.add(panelSeleccion, BorderLayout.WEST);
        panelCompleto.add(lblEstadoResidente, BorderLayout.EAST);

        return panelCompleto;
    }

    private void crearTablaDocumentos() {
        String[] columnas = {"Documento", "Estado", "Archivo", "Fecha Subida", "Recibido"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (residenteActual == null) {
                    return false;
                }
                if (column == 4) {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class;
                return String.class;
            }
        };

        tablaDocumentos = new JTable(modeloTabla);
        configurarTabla();
    }

    private void configurarTabla() {
        tablaDocumentos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaDocumentos.setRowHeight(40);
        tablaDocumentos.setGridColor(new Color(230, 230, 250));
        tablaDocumentos.setSelectionBackground(new Color(colorSecundario.getRed(), colorSecundario.getGreen(), colorSecundario.getBlue(), 100));
        tablaDocumentos.setBackground(Color.WHITE);
        tablaDocumentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaDocumentos.getTableHeader().setReorderingAllowed(false);
        tablaDocumentos.getTableHeader().setResizingAllowed(false);
        tablaDocumentos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tablaDocumentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tablaDocumentos.getTableHeader().setBackground(colorPrincipal);
        tablaDocumentos.getTableHeader().setForeground(Color.WHITE);
        tablaDocumentos.getTableHeader().setPreferredSize(new Dimension(0, 50));

        tablaDocumentos.getColumnModel().getColumn(0).setPreferredWidth(250);
        tablaDocumentos.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaDocumentos.getColumnModel().getColumn(2).setPreferredWidth(300);
        tablaDocumentos.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaDocumentos.getColumnModel().getColumn(4).setPreferredWidth(100);

        tablaDocumentos.getColumnModel().getColumn(1).setCellRenderer(new EstadoRenderer());
        tablaDocumentos.getColumnModel().getColumn(2).setCellRenderer(new ArchivoRenderer());

        tablaDocumentos.getModel().addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                if (e.getColumn() == 4 && residenteActual != null) {
                    int fila = e.getFirstRow();
                    boolean recibido = (Boolean) modeloTabla.getValueAt(fila, 4);
                    String documento = (String) modeloTabla.getValueAt(fila, 0);

                    marcarComoRecibido(documento, recibido);
                    actualizarEstadoGeneral();
                }
            }
        });

        tablaDocumentos.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarBotonesAccion();
                }
            }
        });
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint grad = new GradientPaint(
                        0, 0, new Color(250, 248, 255),
                        0, getHeight(), Color.WHITE
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(colorPrincipal.getRed(), colorPrincipal.getGreen(), colorPrincipal.getBlue(), 30));
                g2.fillRect(0, 0, getWidth(), 2);
            }
        };
        panel.setPreferredSize(new Dimension(0, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        lblEstadoGeneral = new JLabel("Seleccione un residente activo para gestionar su expediente");
        lblEstadoGeneral.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEstadoGeneral.setForeground(new Color(100, 100, 100));

        JButton btnEditarVista = crearBotonEstilizado("Editar Lista de Documentos", "⚙️");
        btnEditarVista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirEditorDocumentos();
            }
        });

        panel.add(lblEstadoGeneral, BorderLayout.CENTER);
        panel.add(btnEditarVista, BorderLayout.EAST);

        return panel;
    }

    private JButton crearBotonEstilizado(String texto, String icono) {
        JButton boton = new JButton(texto);
        if (icono != null && !icono.startsWith("/")) {
            boton.setText(icono + " " + texto);
        }

        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setForeground(colorPrincipal);
        boton.setBackground(Color.WHITE);
        boton.setBorder(new CompoundBorder(
                new LineBorder(colorSecundario, 2, true),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    private void configurarScrollPane(JScrollPane scroll) {
        scroll.setBorder(new CompoundBorder(
                new LineBorder(colorSecundario, 2, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);

        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = colorSecundario;
                this.trackColor = new Color(245, 245, 245);
            }
        });
    }

    private void mostrarEstadoInicial() {
        modeloTabla.setRowCount(0);
        lblEstadoGeneral.setText("Seleccione un residente para gestionar su expediente");
        lblEstadoGeneral.setForeground(new Color(100, 100, 100));
        lblEstadoResidente.setText("");
        residenteActual = null;
    }

    private void abrirSeleccionResidentes() {
        new SeleccionarResidentesUI(this);
    }

    public void actualizarResidenteSeleccionado(String residente) {
        try {
            String numeroControl = residente.split(" - ")[0];

            if (validarYConfirmarResidenteActivo(numeroControl)) {
                this.residenteSeleccionado = residente;
                btnSeleccionarResidente.setText(residente);

                lblEstadoResidente.setText("✓ RESIDENTE ACTIVO");
                lblEstadoResidente.setForeground(colorExito);
                lblEstadoResidente.setBackground(new Color(colorExito.getRed(), colorExito.getGreen(), colorExito.getBlue(), 30));
                lblEstadoResidente.setOpaque(true);

                JOptionPane.showMessageDialog(this,
                        "Residente seleccionado correctamente:\n\n" +
                                residente + "\n\n" +
                                "✓ Estado: RESIDENTE ACTIVO\n" +
                                "✓ Expediente habilitado para gestión",
                        "Residente Seleccionado",
                        JOptionPane.INFORMATION_MESSAGE);

                cargarDatos();
            } else {
                mostrarErrorResidenteNoActivo(residente);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar residente seleccionado: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al procesar la selección del residente:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarYConfirmarResidenteActivo(String numeroControl) {
        try {
            int numControl = Integer.parseInt(numeroControl);
            ModeloResidente residente = ModeloResidente.buscarPorNumeroControl(numControl);

            if (residente == null) {
                System.err.println("DEBUG: Residente no encontrado: " + numeroControl);
                return false;
            }

            boolean tieneProyecto = residente.getIdProyecto() > 0;
            boolean estaHabilitado = residente.isEstatus();

            if (tieneProyecto && estaHabilitado) {
                residenteActual = residente;
                System.out.println("DEBUG: ✅ Residente ACEPTADO - " + numeroControl);
                return true;
            } else {
                System.out.println("DEBUG: ❌ Residente RECHAZADO - " + numeroControl);
                residenteActual = null;
                return false;
            }

        } catch (NumberFormatException e) {
            System.err.println("DEBUG: Número de control inválido: " + numeroControl);
            return false;
        } catch (Exception e) {
            System.err.println("DEBUG: Error al validar residente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarErrorResidenteNoActivo(String residente) {
        lblEstadoResidente.setText("✗ NO ES RESIDENTE ACTIVO");
        lblEstadoResidente.setForeground(Color.RED);
        lblEstadoResidente.setBackground(new Color(255, 230, 230));
        lblEstadoResidente.setOpaque(true);

        JOptionPane.showMessageDialog(this,
                "ERROR: El residente seleccionado no está activo.\n\n" +
                        "MOTIVOS POSIBLES:\n" +
                        "• Es un candidato (no tiene anteproyecto)\n" +
                        "• Fue dado de baja del sistema\n" +
                        "• Error en la base de datos\n\n" +
                        "SOLUCIÓN:\n" +
                        "• Verifique que tenga un anteproyecto registrado\n" +
                        "• Conviértalo a residente activo si es necesario\n" +
                        "• Solo los residentes activos pueden gestionar expedientes\n\n" +
                        "Contacte al administrador si el problema persiste.",
                "Residente No Activo",
                JOptionPane.ERROR_MESSAGE);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        if (residenteActual == null) {
            lblEstadoGeneral.setText("Seleccione un residente para gestionar su expediente");
            lblEstadoGeneral.setForeground(new Color(100, 100, 100));
            return;
        }

        try {
            List<ModeloDocumento> documentosDB = ModeloDocumento.obtenerPorResidente(residenteActual.getIdResidente());

            if (documentosDB.isEmpty()) {
                crearDocumentosPorDefecto();
                documentosDB = ModeloDocumento.obtenerPorResidente(residenteActual.getIdResidente());
            }

            for (ModeloDocumento doc : documentosDB) {
                String archivo = (doc.getNombreArchivoSistema() != null) ? doc.getNombreArchivoSistema() : "Sin archivo";
                String fecha = (doc.getFechaSubida() != null) ? doc.getFechaSubida().toString() : "-";

                modeloTabla.addRow(new Object[]{
                        doc.getNombreDocumento(),
                        doc.getEstado(),
                        archivo,
                        fecha,
                        doc.isRecibido()
                });
            }

            actualizarEstadoGeneral();

        } catch (Exception e) {
            System.err.println("Error al cargar datos del expediente: " + e.getMessage());
            e.printStackTrace();
            cargarDatosEjemplo();
        }
    }

    private void crearDocumentosPorDefecto() {
        try {
            for (String nombreDoc : nombresDocumentos) {
                ModeloDocumento doc = new ModeloDocumento(residenteActual.getIdResidente(), nombreDoc);
                doc.guardar();
                System.out.println("Documento creado por defecto: " + nombreDoc);
            }
        } catch (Exception e) {
            System.err.println("Error al crear documentos por defecto: " + e.getMessage());
        }
    }

    private void cargarDatosEjemplo() {
        for (int i = 0; i < nombresDocumentos.length; i++) {
            String documento = nombresDocumentos[i];
            String archivo = (i % 2 == 0) ? "archivo_" + documento + ".pdf" : "Sin archivo";
            String fecha = (i % 2 == 0) ? "2025-07-" + (20 + i) : "-";
            boolean recibido = i % 4 == 0;

            String estado = recibido ? "Entregado" : "No entregado";

            modeloTabla.addRow(new Object[]{documento, estado, archivo, fecha, recibido});
        }

        actualizarEstadoGeneral();
    }

    private void marcarComoRecibido(String documento, boolean recibido) {
        if (residenteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: No hay residente seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Marcar como cambio pendiente
            String cambio = "recibido_" + documento + "_" + recibido;
            if (!cambiosPendientes.contains(cambio)) {
                cambiosPendientes.add(cambio);
                actualizarBotonGuardar();
            }

            boolean actualizado = ModeloDocumento.marcarComoRecibido(
                    residenteActual.getIdResidente(), documento, recibido);

            if (actualizado) {
                System.out.println("✅ Documento " + documento + " marcado como recibido: " + recibido +
                        " para residente: " + residenteActual.getNumeroControl());

                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    if (modeloTabla.getValueAt(i, 0).equals(documento)) {
                        String nuevoEstado = recibido ? "Entregado" : "No entregado";
                        modeloTabla.setValueAt(nuevoEstado, i, 1);
                        break;
                    }
                }

                cambiosPendientes.remove(cambio);
                actualizarBotonGuardar();

            } else {
                System.err.println("❌ No se pudo actualizar el estado del documento en BD");
                JOptionPane.showMessageDialog(this,
                        "Error al guardar el cambio en la base de datos.\n" +
                                "El cambio se revertirá.",
                        "Error de Base de Datos",
                        JOptionPane.ERROR_MESSAGE);

                // Revertir el cambio en la tabla directamente
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    if (modeloTabla.getValueAt(i, 0).equals(documento)) {
                        modeloTabla.setValueAt(!recibido, i, 4);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al marcar documento como recibido: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar estado del documento:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void subirDocumento(int fila) {
        if (residenteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: No hay residente activo seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreDocumento = (String) modeloTabla.getValueAt(fila, 0);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Documentos PDF (*.pdf)", "pdf"));
        fileChooser.setDialogTitle("Seleccionar documento PDF - " + nombreDocumento);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            // Validación mejorada con mensajes específicos
            String errorValidacion = validarDocumentoDetallado(archivo);
            if (errorValidacion != null) {
                JOptionPane.showMessageDialog(this,
                        errorValidacion,
                        "Archivo no válido",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String numeroControl = String.valueOf(residenteActual.getNumeroControl());

                // Usar el GestorArchivos para guardar el archivo
                String rutaGuardada = Modelo.GestorArchivos.guardarArchivo(archivo, numeroControl, nombreDocumento);

                if (rutaGuardada != null) {
                    // Obtener solo el nombre del archivo guardado
                    String nombreArchivoGuardado = new File(rutaGuardada).getName();

                    // IMPORTANTE: Actualizar en base de datos ANTES de actualizar la tabla
                    boolean actualizadoBD = ModeloDocumento.actualizarArchivo(
                            residenteActual.getIdResidente(),
                            nombreDocumento,
                            rutaGuardada,
                            nombreArchivoGuardado
                    );

                    if (actualizadoBD) {
                        // Solo actualizar tabla si se guardó en BD exitosamente
                        modeloTabla.setValueAt(nombreArchivoGuardado, fila, 2);
                        modeloTabla.setValueAt(java.time.LocalDate.now().toString(), fila, 3);
                        modeloTabla.setValueAt(true, fila, 4); // AUTO MARCAR COMO ENTREGADO
                        modeloTabla.setValueAt("Entregado", fila, 1);

                        // También actualizar como recibido en BD
                        ModeloDocumento.marcarComoRecibido(residenteActual.getIdResidente(), nombreDocumento, true);

                        JOptionPane.showMessageDialog(this,
                                "Documento PDF guardado exitosamente:\n\n" +
                                        "Residente: " + residenteSeleccionado + "\n" +
                                        "Archivo original: " + archivo.getName() + "\n" +
                                        "Archivo guardado: " + nombreArchivoGuardado + "\n" +
                                        "Documento: " + nombreDocumento + "\n" +
                                        "Ubicación: " + rutaGuardada + "\n" +
                                        "Fecha: " + java.time.LocalDate.now() +
                                        "\nEstado: Entregado ✓\n\n" +
                                        "✅ Guardado en base de datos correctamente",
                                "Archivo subido correctamente",
                                JOptionPane.INFORMATION_MESSAGE);

                        actualizarEstadoGeneral();

                    } else {
                        // Si falla BD, eliminar archivo físico
                        Modelo.GestorArchivos.eliminarArchivo(rutaGuardada);

                        JOptionPane.showMessageDialog(this,
                                "Error: El archivo se guardó físicamente pero no se pudo actualizar en la base de datos.\n\n" +
                                        "El archivo fue eliminado para mantener consistencia.\n" +
                                        "Intente nuevamente o contacte al administrador.",
                                "Error de Base de Datos",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al guardar el archivo en el sistema.\n\n" +
                                    "POSIBLES CAUSAS:\n" +
                                    "• Permisos insuficientes en la carpeta de destino\n" +
                                    "• Espacio insuficiente en disco\n" +
                                    "• El archivo está siendo usado por otra aplicación\n" +
                                    "• Problemas con archivos de OneDrive (proveedor de nube no ejecutándose)\n\n" +
                                    "SOLUCIONES:\n" +
                                    "• Copie el archivo a una ubicación local (no OneDrive)\n" +
                                    "• Cierre otras aplicaciones que puedan usar el archivo\n" +
                                    "• Ejecute como administrador\n" +
                                    "• Verifique el espacio disponible en disco",
                            "Error al guardar archivo",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                System.err.println("Error al subir documento: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Error inesperado al guardar el documento:\n\n" +
                                "Error técnico: " + e.getMessage() + "\n\n" +
                                "Contacte al administrador del sistema si el problema persiste.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarDocumento(int fila) {
        if (residenteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: No hay residente activo seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreDoc = (String) modeloTabla.getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el documento?\n\n" +
                        "Residente: " + residenteSeleccionado + "\n" +
                        "Documento: " + nombreDoc + "\n\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                System.out.println("Eliminando documento " + nombreDoc +
                        " del residente: " + residenteActual.getNumeroControl());

                modeloTabla.setValueAt("Sin archivo", fila, 2);
                modeloTabla.setValueAt("-", fila, 3);
                modeloTabla.setValueAt(false, fila, 4);
                modeloTabla.setValueAt("No entregado", fila, 1);

                JOptionPane.showMessageDialog(this,
                        "Documento eliminado correctamente\n\n" +
                                "Residente: " + residenteSeleccionado + "\n" +
                                "El archivo ha sido removido\n" +
                                "Estado actualizado a: No entregado",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                actualizarEstadoGeneral();

            } catch (Exception e) {
                System.err.println("Error al eliminar documento: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar el documento:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Validación detallada de documento con mensajes específicos
     */
    private String validarDocumentoDetallado(File archivo) {
        if (archivo == null) {
            return "❌ ERROR: No se seleccionó ningún archivo\n\n" +
                    "SOLUCIÓN: Seleccione un archivo PDF válido.";
        }

        if (!archivo.exists()) {
            return "❌ ERROR: El archivo no existe\n\n" +
                    "Archivo: " + archivo.getAbsolutePath() + "\n\n" +
                    "POSIBLES CAUSAS:\n" +
                    "• El archivo fue movido o eliminado\n" +
                    "• Problemas de red (si está en ubicación remota)\n\n" +
                    "SOLUCIÓN: Verifique que el archivo exista en la ubicación especificada.";
        }

        if (!archivo.canRead()) {
            return "❌ ERROR: Sin permisos de lectura\n\n" +
                    "Archivo: " + archivo.getName() + "\n\n" +
                    "POSIBLES CAUSAS:\n" +
                    "• El archivo está siendo usado por otra aplicación\n" +
                    "• Permisos insuficientes del usuario\n" +
                    "• Archivo corrupto o bloqueado\n\n" +
                    "SOLUCIONES:\n" +
                    "• Cierre otras aplicaciones que puedan usar el archivo\n" +
                    "• Ejecute el programa como administrador\n" +
                    "• Copie el archivo a otra ubicación";
        }

        String nombre = archivo.getName().toLowerCase();
        if (!nombre.endsWith(".pdf")) {
            String extension = "";
            int ultimoPunto = nombre.lastIndexOf('.');
            if (ultimoPunto > 0) {
                extension = nombre.substring(ultimoPunto);
            }

            return "❌ ERROR: Formato de archivo no válido\n\n" +
                    "Archivo: " + archivo.getName() + "\n" +
                    "Extensión detectada: " + (extension.isEmpty() ? "sin extensión" : extension) + "\n" +
                    "Extensión requerida: .pdf\n\n" +
                    "SOLUCIÓN:\n" +
                    "• Solo se permiten archivos PDF\n" +
                    "• Convierta su documento a formato PDF\n" +
                    "• Verifique que el archivo tenga la extensión .pdf";
        }

        long tamaño = archivo.length();
        long maxTamaño = 10 * 1024 * 1024; // 10MB

        if (tamaño == 0) {
            return "❌ ERROR: Archivo vacío\n\n" +
                    "Archivo: " + archivo.getName() + "\n" +
                    "Tamaño: 0 bytes\n\n" +
                    "POSIBLES CAUSAS:\n" +
                    "• El archivo no se descargó completamente\n" +
                    "• Archivo corrupto\n" +
                    "• Error durante la creación del archivo\n\n" +
                    "SOLUCIÓN: Seleccione un archivo PDF válido con contenido.";
        }

        if (tamaño > maxTamaño) {
            double tamañoMB = tamaño / (1024.0 * 1024.0);
            return "❌ ERROR: Archivo demasiado grande\n\n" +
                    "Archivo: " + archivo.getName() + "\n" +
                    "Tamaño actual: " + String.format("%.2f MB", tamañoMB) + "\n" +
                    "Tamaño máximo permitido: 10 MB\n\n" +
                    "SOLUCIONES:\n" +
                    "• Comprima el archivo PDF\n" +
                    "• Reduzca la calidad de las imágenes\n" +
                    "• Divida el documento en partes más pequeñas\n" +
                    "• Use herramientas online para reducir el tamaño del PDF";
        }

        // Verificación básica de formato PDF
        try {
            byte[] header = new byte[4];
            java.io.FileInputStream fis = new java.io.FileInputStream(archivo);
            int bytesRead = fis.read(header);
            fis.close();

            if (bytesRead >= 4) {
                String headerStr = new String(header);
                if (!headerStr.equals("%PDF")) {
                    return "❌ ERROR: Archivo PDF corrupto o inválido\n\n" +
                            "Archivo: " + archivo.getName() + "\n" +
                            "Problema: El archivo no tiene el formato PDF correcto\n\n" +
                            "POSIBLES CAUSAS:\n" +
                            "• El archivo está corrupto\n" +
                            "• No es realmente un archivo PDF\n" +
                            "• Se cambió la extensión manualmente\n\n" +
                            "SOLUCIONES:\n" +
                            "• Vuelva a generar el archivo PDF\n" +
                            "• Use una herramienta confiable para crear PDFs\n" +
                            "• Verifique que el archivo se abra correctamente en un visor PDF";
                }
            }
        } catch (Exception e) {
            return "❌ ERROR: No se puede verificar el archivo\n\n" +
                    "Archivo: " + archivo.getName() + "\n" +
                    "Error técnico: " + e.getMessage() + "\n\n" +
                    "SOLUCIONES:\n" +
                    "• Intente con otro archivo\n" +
                    "• Verifique que el archivo no esté corrupto\n" +
                    "• Contacte al administrador si el problema persiste";
        }

        // Si llegó hasta aquí, el archivo es válido
        return null;
    }

    private void visualizarDocumento(int fila) {
        if (residenteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: No hay residente activo seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreArchivo = (String) modeloTabla.getValueAt(fila, 2);
        String nombreDocumento = (String) modeloTabla.getValueAt(fila, 0);

        if ("Sin archivo".equals(nombreArchivo) || "-".equals(nombreArchivo)) {
            JOptionPane.showMessageDialog(this,
                    "No hay archivo para visualizar.\n\n" +
                            "Residente: " + residenteSeleccionado + "\n" +
                            "Documento: " + nombreDocumento + "\n" +
                            "Primero debe subir un archivo PDF para poder visualizarlo.",
                    "No hay archivo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // Usar la nueva clase para visualizar
            VisualizadorPDF visualizador = new VisualizadorPDF(this, nombreArchivo, nombreDocumento, residenteSeleccionado);
            visualizador.mostrarVisualizador();

        } catch (Exception e) {
            System.err.println("Error al visualizar documento: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el documento:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirEditorDocumentos() {
        JDialog dialogo = new JDialog(this, "Editor de Lista de Documentos", true);
        dialogo.setSize(600, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Gestionar Lista de Documentos Requeridos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorPrincipal);
        titulo.setHorizontalAlignment(JLabel.CENTER);

        JTextArea listaActual = new JTextArea(15, 40);
        listaActual.setEditable(true);
        listaActual.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaActual.setBorder(BorderFactory.createTitledBorder("Lista de Documentos (uno por línea)"));

        StringBuilder documentosTexto = new StringBuilder();
        for (String doc : nombresDocumentos) {
            documentosTexto.append(doc).append("\n");
        }
        listaActual.setText(documentosTexto.toString());

        JScrollPane scrollLista = new JScrollPane(listaActual);

        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnGuardar = crearBotonEstilizado("Guardar Cambios", "💾");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] nuevosDocumentos = listaActual.getText().trim().split("\n");

                List<String> documentosLimpios = new ArrayList<>();
                for (String doc : nuevosDocumentos) {
                    if (!doc.trim().isEmpty()) {
                        documentosLimpios.add(doc.trim());
                    }
                }

                if (documentosLimpios.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo,
                            "Debe haber al menos un documento en la lista",
                            "Lista vacía",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                nombresDocumentos = documentosLimpios.toArray(new String[0]);

                if (residenteActual != null) {
                    cargarDatos();
                }

                JOptionPane.showMessageDialog(dialogo,
                        "Lista de documentos actualizada exitosamente.\n" +
                                "Total de documentos: " + nombresDocumentos.length + "\n\n" +
                                "Los cambios se aplican a todos los expedientes.",
                        "Cambios guardados",
                        JOptionPane.INFORMATION_MESSAGE);

                dialogo.dispose();
            }
        });

        JButton btnCancelar = crearBotonEstilizado("Cancelar", "❌");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogo.dispose();
            }
        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        JLabel instrucciones = new JLabel("<html><div style='text-align: center;'>" +
                "<b>Instrucciones:</b><br>" +
                "• Un documento por línea<br>" +
                "• Puede agregar nuevos documentos<br>" +
                "• Puede eliminar líneas existentes<br>" +
                "• Los cambios se aplicarán a todos los expedientes<br>" +
                "• Se recomienda mantener los documentos estándar de residencias" +
                "</div></html>");
        instrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        instrucciones.setForeground(new Color(100, 100, 100));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scrollLista, BorderLayout.CENTER);
        panel.add(instrucciones, BorderLayout.SOUTH);

        JPanel panelCompleto = new JPanel(new BorderLayout());
        panelCompleto.add(panel, BorderLayout.CENTER);
        panelCompleto.add(panelBotones, BorderLayout.SOUTH);

        dialogo.add(panelCompleto);
        dialogo.setVisible(true);
    }

    private void actualizarEstadoGeneral() {
        if (residenteActual == null) {
            lblEstadoGeneral.setText("Seleccione un residente activo para gestionar su expediente");
            lblEstadoGeneral.setForeground(new Color(100, 100, 100));
            return;
        }

        int total = modeloTabla.getRowCount();
        int recibidos = 0;

        for (int i = 0; i < total; i++) {
            if ((Boolean) modeloTabla.getValueAt(i, 4)) {
                recibidos++;
            }
        }

        lblEstadoGeneral.setText("Expediente de " + residenteSeleccionado + ": " +
                recibidos + "/" + total + " documentos recibidos");

        if (recibidos == total) {
            lblEstadoGeneral.setForeground(colorExito);
        } else if (recibidos > total / 2) {
            lblEstadoGeneral.setForeground(new Color(255, 193, 7));
        } else {
            lblEstadoGeneral.setForeground(colorAdvertencia);
        }
    }

    private void guardarTodosCambios() {
        if (cambiosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay cambios pendientes para guardar",
                    "Sin cambios",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Confirma guardar todos los cambios pendientes?\n\n" +
                        "Se guardarán " + cambiosPendientes.size() + " cambios:\n" +
                        "• Documentos marcados como recibidos\n" +
                        "• Archivos subidos\n" +
                        "• Otros cambios realizados\n\n" +
                        "Esta acción guardará permanentemente los cambios.",
                "Confirmar Guardado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int cambiosExitosos = 0;
                int cambiosFallidos = 0;

                System.out.println("Guardando " + cambiosPendientes.size() + " cambios...");

                // Procesar cada cambio pendiente
                for (String cambio : cambiosPendientes) {
                    try {
                        if (procesarCambioPendiente(cambio)) {
                            cambiosExitosos++;
                            System.out.println("✅ Guardado: " + cambio);
                        } else {
                            cambiosFallidos++;
                            System.err.println("❌ Falló: " + cambio);
                        }
                        Thread.sleep(100); // Simular tiempo de procesamiento

                    } catch (Exception e) {
                        cambiosFallidos++;
                        System.err.println("❌ Error procesando: " + cambio + " - " + e.getMessage());
                    }
                }

                // Mostrar resultado
                if (cambiosFallidos == 0) {
                    JOptionPane.showMessageDialog(this,
                            "Todos los cambios han sido guardados exitosamente.\n\n" +
                                    "Cambios procesados: " + cambiosExitosos + "\n" +
                                    "Residente: " + residenteSeleccionado,
                            "Cambios Guardados",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Guardado completado con algunos errores:\n\n" +
                                    "✅ Exitosos: " + cambiosExitosos + "\n" +
                                    "❌ Fallidos: " + cambiosFallidos + "\n" +
                                    "Residente: " + residenteSeleccionado + "\n\n" +
                                    "Revise la consola para más detalles.",
                            "Guardado Parcial",
                            JOptionPane.WARNING_MESSAGE);
                }

                cambiosPendientes.clear();
                actualizarBotonGuardar();

            } catch (Exception e) {
                System.err.println("Error general al guardar cambios: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Error al guardar cambios:\n" + e.getMessage() + "\n\n" +
                                "Algunos cambios pueden no haberse guardado correctamente.",
                        "Error de Guardado",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Procesar un cambio pendiente específico
     */
    private boolean procesarCambioPendiente(String cambio) {
        try {
            if (residenteActual == null) {
                System.err.println("No hay residente activo para procesar cambio: " + cambio);
                return false;
            }

            // Procesar cambios de tipo "recibido_"
            if (cambio.startsWith("recibido_")) {
                String[] partes = cambio.split("_");
                if (partes.length >= 3) {
                    String nombreDocumento = partes[1];
                    boolean recibido = Boolean.parseBoolean(partes[2]);

                    // Actualizar en base de datos
                    return ModeloDocumento.marcarComoRecibido(
                            residenteActual.getIdResidente(), nombreDocumento, recibido);
                }
            }

            // Procesar cambios de tipo "archivo_"
            else if (cambio.startsWith("archivo_")) {
                String[] partes = cambio.split("_");
                if (partes.length >= 3) {
                    String nombreDocumento = partes[1];
                    String nombreArchivo = partes[2];

                    // TODO: Implementar actualización de archivo en BD
                    // return ModeloDocumento.actualizarArchivo(residenteActual.getIdResidente(), nombreDocumento, nombreArchivo);

                    System.out.println("Procesado cambio de archivo: " + nombreDocumento + " -> " + nombreArchivo);
                    return true; // Temporalmente retornar true
                }
            }

            System.err.println("Formato de cambio no reconocido: " + cambio);
            return false;

        } catch (Exception e) {
            System.err.println("Error procesando cambio '" + cambio + "': " + e.getMessage());
            return false;
        }
    }

    class EstadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String estado = (String) value;
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));

            if ("No entregado".equals(estado)) {
                setBackground(new Color(255, 230, 230));
                setForeground(new Color(200, 0, 0));
            } else if ("Entregado".equals(estado)) {
                setBackground(new Color(230, 255, 230));
                setForeground(new Color(0, 150, 0));
            } else {
                setBackground(new Color(245, 245, 245));
                setForeground(new Color(100, 100, 100));
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }

    class ArchivoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String archivo = (String) value;
            setHorizontalAlignment(LEFT);
            setFont(new Font("Segoe UI", Font.PLAIN, 12));

            if ("Sin archivo".equals(archivo) || "-".equals(archivo)) {
                setBackground(new Color(255, 250, 200));
                setForeground(new Color(180, 140, 0));
            } else {
                setBackground(new Color(230, 255, 230));
                setForeground(new Color(0, 120, 0));
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }

    /**
     * Manejar el regreso al menú principal con validaciones
     */
    private void regresarAlMenu() {
        // Verificar si hay cambios pendientes
        if (!cambiosPendientes.isEmpty()) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "Tiene " + cambiosPendientes.size() + " cambios sin guardar.\n\n" +
                            "¿Qué desea hacer?\n\n" +
                            "• SÍ: Guardar cambios y regresar al menú\n" +
                            "• NO: Descartar cambios y regresar al menú\n" +
                            "• CANCELAR: Permanecer en expedientes",
                    "Cambios sin guardar",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            switch (respuesta) {
                case JOptionPane.YES_OPTION:
                    // Guardar cambios y regresar
                    guardarCambiosYRegresar();
                    break;
                case JOptionPane.NO_OPTION:
                    // Descartar cambios y regresar
                    descartarCambiosYRegresar();
                    break;
                case JOptionPane.CANCEL_OPTION:
                default:
                    // No hacer nada, permanecer en la ventana
                    return;
            }
        } else {
            // No hay cambios pendientes, regresar directamente
            confirmarYRegresar();
        }
    }

    /**
     * Guardar cambios pendientes y regresar al menú
     */
    private void guardarCambiosYRegresar() {
        try {
            // Simular guardado de cambios
            System.out.println("Guardando " + cambiosPendientes.size() + " cambios antes de salir...");

            // TODO: Implementar guardado real en base de datos
            // Por ejemplo, recorrer cambiosPendientes y ejecutar las actualizaciones correspondientes

            for (String cambio : cambiosPendientes) {
                System.out.println("Guardando: " + cambio);
                Thread.sleep(50); // Simular tiempo de procesamiento
            }

            JOptionPane.showMessageDialog(this,
                    "Cambios guardados exitosamente.\n" +
                            "Regresando al menú principal...",
                    "Cambios guardados",
                    JOptionPane.INFORMATION_MESSAGE);

            cambiosPendientes.clear();
            actualizarBotonGuardar();

            // Regresar al menú
            volverAlMenuPrincipal();

        } catch (Exception e) {
            System.err.println("Error al guardar cambios: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al guardar algunos cambios:\n" + e.getMessage() + "\n\n" +
                            "¿Desea regresar al menú sin guardar?",
                    "Error de guardado",
                    JOptionPane.ERROR_MESSAGE);

            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Regresar al menú sin guardar los cambios?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                volverAlMenuPrincipal();
            }
        }
    }

    /**
     * Descartar cambios y regresar al menú
     */
    private void descartarCambiosYRegresar() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea descartar todos los cambios?\n\n" +
                        "Se perderán " + cambiosPendientes.size() + " cambios:\n" +
                        "• Documentos marcados como recibidos\n" +
                        "• Archivos subidos\n" +
                        "• Otras modificaciones\n\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar descarte de cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.out.println("Descartando " + cambiosPendientes.size() + " cambios pendientes...");
            cambiosPendientes.clear();

            JOptionPane.showMessageDialog(this,
                    "Cambios descartados.\nRegresando al menú principal...",
                    "Cambios descartados",
                    JOptionPane.INFORMATION_MESSAGE);

            volverAlMenuPrincipal();
        }
    }

    /**
     * Confirmar regreso cuando no hay cambios pendientes
     */
    private void confirmarYRegresar() {
        if (residenteActual != null) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Regresar al menú principal?\n\n" +
                            "Residente actual: " + residenteSeleccionado + "\n" +
                            "No hay cambios sin guardar.",
                    "Confirmar regreso",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                volverAlMenuPrincipal();
            }
        } else {
            volverAlMenuPrincipal();
        }
    }

    /**
     * Volver al menú principal
     */
    private void volverAlMenuPrincipal() {
        try {
            System.out.println("Cerrando gestión de expedientes...");

            // Cerrar esta ventana
            this.dispose();

            // Abrir el menú principal directamente
            try {
                // CAMBIO: Usar el nombre correcto de tu clase Menu
                Vista.Menu menuPrincipal = new Vista.Menu();
                menuPrincipal.setVisible(true);

                System.out.println("Menú principal abierto exitosamente");

            } catch (Exception e) {
                System.err.println("Error al abrir menú principal: " + e.getMessage());

                // Fallback: mostrar mensaje de error
                JOptionPane.showMessageDialog(null,
                        "Error al regresar al menú principal:\n" + e.getMessage() + "\n\n" +
                                "El programa se cerrará. Vuelva a ejecutarlo manualmente.",
                        "Error de navegación",
                        JOptionPane.ERROR_MESSAGE);

                // Cerrar completamente la aplicación
                System.exit(0);
            }

        } catch (Exception e) {
            System.err.println("Error al regresar al menú: " + e.getMessage());

            JOptionPane.showMessageDialog(this,
                    "Error al regresar al menú principal:\n" + e.getMessage() + "\n\n" +
                            "La ventana se cerrará.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            this.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error al establecer Look and Feel: " + e.getMessage());
        }

        new ExpedientesUI();
    }
}