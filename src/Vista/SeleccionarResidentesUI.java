package Vista;

import Modelo.ModeloResidente;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import Modelo.Conexion_bd;

/**
 * Ventana para seleccionar SOLO RESIDENTES para gestión de expedientes
 * Filtrada para mostrar únicamente residentes con id_estatus_residente = 2
 * Los candidatos (id_estatus_residente = 1) NO aparecen en esta lista
 */
public class SeleccionarResidentesUI extends JDialog {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private final Color colorAdvertencia = new Color(255, 87, 34);

    private JTable tablaResidentes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private ExpedientesUI ventanaPadre;
    private JLabel lblContadorResidentes;

    public SeleccionarResidentesUI(ExpedientesUI padre) {
        super(padre, "Seleccionar Residente - Gestión de Expedientes", true);
        this.ventanaPadre = padre;
        inicializarVentana();
        crearInterfaz();
        cargarResidentesActivos();
        setVisible(true);
    }

    private void inicializarVentana() {
        setSize(950, 650);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void crearInterfaz() {
        // Panel principal con fondo característico del sistema
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(colorFondo);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel superior con advertencia y búsqueda
        JPanel panelSuperior = crearPanelSuperior();
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelCentral = crearPanelTabla();
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = crearPanelBotones();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelSuperior() {
        JPanel panelCompleto = new JPanel(new BorderLayout());
        panelCompleto.setOpaque(false);

        // Panel de advertencia importante
        JPanel panelAdvertencia = crearPanelAdvertencia();
        panelCompleto.add(panelAdvertencia, BorderLayout.NORTH);

        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        panelCompleto.add(panelBusqueda, BorderLayout.CENTER);

        return panelCompleto;
    }

    private JPanel crearPanelAdvertencia() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 245, 245));
        panel.setBorder(new CompoundBorder(
                new LineBorder(colorAdvertencia, 2, true),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        // Icono de advertencia
        JLabel iconoAdvertencia = new JLabel("⚠️");
        iconoAdvertencia.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconoAdvertencia.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // Texto de advertencia
        JLabel lblAdvertencia = new JLabel("<html><div style='line-height: 1.4;'>" +
                "<b style='color: #d32f2f; font-size: 14px;'>IMPORTANTE - GESTIÓN DE EXPEDIENTES</b><br>" +
                "<span style='font-size: 12px; color: #333;'>" +
                "• Solo se muestran <b>residentes activos</b> (con anteproyecto registrado)<br>" +
                "• Los <b>candidatos</b> no pueden gestionar expedientes hasta convertirse en residentes <br>" +
                "• Si no ve un alumno, verifique que tenga un anteproyecto asignado" +
                "</span></div></html>");
        lblAdvertencia.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(iconoAdvertencia, BorderLayout.WEST);
        panel.add(lblAdvertencia, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        panel.setOpaque(false);

        // Icono de búsqueda
        JLabel lblIconoBuscar = new JLabel("🔍");
        lblIconoBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblIconoBuscar.setForeground(colorPrincipal);

        // Label "Buscar Residente Activo:"
        JLabel lblBuscar = new JLabel("Buscar Residente:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBuscar.setForeground(colorPrincipal);

        // Campo de texto para búsqueda
        txtBuscar = new JTextField(35);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 170, 255), 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBuscar.setPreferredSize(new Dimension(350, 35));
        txtBuscar.setToolTipText("Buscar por número de control, nombre o correo");

        // Listener para búsqueda en tiempo real
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarTabla(txtBuscar.getText());
            }
        });

        // Contador de residentes
        lblContadorResidentes = new JLabel("Cargando...");
        lblContadorResidentes.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblContadorResidentes.setForeground(new Color(100, 100, 100));

        panel.add(lblIconoBuscar);
        panel.add(lblBuscar);
        panel.add(txtBuscar);
        panel.add(lblContadorResidentes);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        // Título de la sección
        JLabel lblTitulo = new JLabel("Residentes Disponibles - Seleccione UNO para gestionar expediente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Crear tabla para residentes activos
        crearTabla();
        JScrollPane scrollPane = new JScrollPane(tablaResidentes);
        configurarScrollPane(scrollPane);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void crearTabla() {
        // Columnas sin carrera
        String[] columnas = {"No. Control", "Nombre Completo", "Semestre", "Correo", "Proyecto"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };

        tablaResidentes = new JTable(modeloTabla);
        configurarTabla();
    }

    private void configurarTabla() {
        // Configuración general
        tablaResidentes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaResidentes.setRowHeight(28);
        tablaResidentes.setGridColor(new Color(230, 230, 250));
        tablaResidentes.setSelectionBackground(colorPrincipal);
        tablaResidentes.setSelectionForeground(Color.WHITE);
        tablaResidentes.setBackground(Color.WHITE);

        // Configurar tabla fija
        tablaResidentes.getTableHeader().setReorderingAllowed(false);
        tablaResidentes.getTableHeader().setResizingAllowed(false);
        tablaResidentes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // CAMBIO IMPORTANTE: Solo selección SIMPLE para expedientes (uno a la vez)
        tablaResidentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Header personalizado
        tablaResidentes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaResidentes.getTableHeader().setBackground(colorPrincipal);
        tablaResidentes.getTableHeader().setForeground(Color.WHITE);
        tablaResidentes.getTableHeader().setPreferredSize(new Dimension(0, 38));

        // Configurar anchos de columna
        tablaResidentes.getColumnModel().getColumn(0).setPreferredWidth(100); // No. Control
        tablaResidentes.getColumnModel().getColumn(1).setPreferredWidth(300); // Nombre Completo
        tablaResidentes.getColumnModel().getColumn(2).setPreferredWidth(80);  // Semestre
        tablaResidentes.getColumnModel().getColumn(3).setPreferredWidth(200); // Correo
        tablaResidentes.getColumnModel().getColumn(4).setPreferredWidth(200); // Proyecto

        // Centrar columnas numéricas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaResidentes.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaResidentes.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // Renderer para alternar colores y mostrar estado
        tablaResidentes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 255, 248)); // Verde muy suave para residentes activos
                    }
                }

                return c;
            }
        });
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panel.setOpaque(false);

        // Botón "Seleccionar Residente" - Texto cambiado para expedientes
        JButton btnSeleccionar = new JButton("Seleccionar para Expediente");
        btnSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setBackground(colorPrincipal);
        btnSeleccionar.setBorder(new CompoundBorder(
                new LineBorder(colorPrincipal, 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionar.setOpaque(true);

        // Efecto hover
        btnSeleccionar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSeleccionar.setBackground(colorPrincipal.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSeleccionar.setBackground(colorPrincipal);
            }
        });

        // Botón "Cancelar"
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setForeground(new Color(80, 80, 80));
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setOpaque(true);

        // Efecto hover para cancelar
        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancelar.setBackground(new Color(230, 230, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCancelar.setBackground(new Color(240, 240, 240));
            }
        });

        // Acciones de los botones
        btnSeleccionar.addActionListener(e -> seleccionarResidente());
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnSeleccionar);
        panel.add(btnCancelar);

        return panel;
    }

    private void configurarScrollPane(JScrollPane scroll) {
        scroll.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 170, 255), 2, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(900, 300));

        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        // Scrollbars personalizadas
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(180, 170, 255);
                this.trackColor = new Color(245, 245, 245);
            }
        });
    }

    /**
     * MÉTODO PRINCIPAL: Cargar solo residentes activos desde la base de datos
     */
    private void cargarResidentesActivos() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);

        try {
            List<ModeloResidente> residentes = ModeloResidente.obtenerResidentesActivos();

            if (residentes.isEmpty()) {
                // Mostrar mensaje especial si no hay residentes activos
                mostrarMensajeSinResidentesActivos();
                lblContadorResidentes.setText("No hay residentes activos");
                return;
            }

            // Cargar residentes activos en la tabla
            for (ModeloResidente residente : residentes) {
                String nombreCompleto = construirNombreCompleto(residente);

                // ADAPTADO: Usar métodos helper seguros
                String proyecto = obtenerNombreProyectoSeguro(residente);

                modeloTabla.addRow(new Object[]{
                        residente.getNumeroControl(),
                        nombreCompleto,
                        residente.getSemestre(),
                        residente.getCorreo(),
                        proyecto
                });
            }

            // Actualizar contador
            lblContadorResidentes.setText(residentes.size() + " residentes activos encontrados");

        } catch (Exception e) {
            System.err.println("Error al cargar residentes activos: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "Error al cargar residentes activos:\n" + e.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);

            lblContadorResidentes.setText("Error al cargar datos");
        }
    }

    /**
     * Método helper seguro para obtener nombre de carrera
     */
    private String obtenerNombreCarreraSeguro(ModeloResidente residente) {
        try {
            // Si el ModeloResidente ya tiene carrera como string, usarla
            if (residente.getCarrera() != null && !residente.getCarrera().trim().isEmpty()) {
                return residente.getCarrera();
            }

            // Si no, hacer consulta directa usando conexión
            return consultarNombreCarreraPorResidente(residente.getIdResidente());

        } catch (Exception e) {
            System.err.println("Error obteniendo carrera para residente " + residente.getIdResidente() + ": " + e.getMessage());
            return "Sin carrera";
        }
    }

    /**
     * Método helper seguro para obtener nombre de proyecto
     */
    private String obtenerNombreProyectoSeguro(ModeloResidente residente) {
        try {
            if (residente.getIdProyecto() <= 0) {
                return "Sin proyecto";
            }

            return consultarNombreProyecto(residente.getIdProyecto());

        } catch (Exception e) {
            System.err.println("Error obteniendo proyecto para residente " + residente.getIdResidente() + ": " + e.getMessage());
            return "Proyecto ID: " + residente.getIdProyecto();
        }
    }

    /**
     * Consulta directa para obtener nombre de carrera por residente
     */
    private String consultarNombreCarreraPorResidente(int idResidente) {
        String sql = "SELECT c.nombre_carrera " +
                "FROM residente r " +
                "LEFT JOIN catalogo_carrera c ON r.id_carrera = c.id_carrera " +
                "WHERE r.id_residente = ?";

        try {
            Connection conn = Conexion_bd.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idResidente);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nombreCarrera = rs.getString("nombre_carrera");
                return nombreCarrera != null ? nombreCarrera : "Sin carrera";
            }
        } catch (SQLException e) {
            System.err.println("Error consultando carrera: " + e.getMessage());
        }

        return "Sin carrera";
    }

    /**
     *Consulta directa para obtener nombre de proyecto
     */
    private String consultarNombreProyecto(int idProyecto) {
        if (idProyecto <= 0) {
            return "Sin proyecto";
        }

        String sql = "SELECT nombre FROM proyecto WHERE id_proyecto = ?";

        try {
            Connection conn = Conexion_bd.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idProyecto);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nombreProyecto = rs.getString("nombre");
                return nombreProyecto != null ? nombreProyecto : "Sin nombre";
            }
        } catch (SQLException e) {
            System.err.println("Error consultando proyecto: " + e.getMessage());
        }

        return "Proyecto ID: " + idProyecto;
    }

    /**
     * Mostrar mensaje cuando no hay residentes activos
     */
    private void mostrarMensajeSinResidentesActivos() {
        JOptionPane.showMessageDialog(this,
                "No se encontraron residentes activos en el sistema.\n\n" +
                        "CAUSAS POSIBLES:\n" +
                        "• No hay alumnos con anteproyectos registrados\n" +
                        "• Los candidatos aún no han sido convertidos a residentes activos\n" +
                        "• Problemas con la base de datos\n\n" +
                        "SOLUCIÓN:\n" +
                        "1. Verifique que existan anteproyectos registrados\n" +
                        "2. Convierta candidatos a residentes activos\n" +
                        "3. Consulte al administrador del sistema",
                "Sin Residentes Activos",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Construir nombre completo del residente
     */
    private String construirNombreCompleto(ModeloResidente residente) {
        StringBuilder nombre = new StringBuilder();
        nombre.append(residente.getNombre());
        nombre.append(" ").append(residente.getApellidoPaterno());

        if (residente.getApellidoMaterno() != null && !residente.getApellidoMaterno().trim().isEmpty()) {
            nombre.append(" ").append(residente.getApellidoMaterno());
        }

        return nombre.toString().trim();
    }

    /**
     * Filtrar tabla según texto de búsqueda - SOLO residentes activos
     */
    private void filtrarTabla(String filtro) {
        modeloTabla.setRowCount(0);

        if (filtro == null || filtro.trim().isEmpty()) {
            cargarResidentesActivos(); // Recargar todos los residentes activos
            return;
        }

        String filtroLower = filtro.toLowerCase().trim();
        int contador = 0;

        try {
            List<ModeloResidente> residentes = ModeloResidente.obtenerResidentesActivos();

            for (ModeloResidente residente : residentes) {
                String nombreCompleto = construirNombreCompleto(residente).toLowerCase();
                String numeroControl = String.valueOf(residente.getNumeroControl()).toLowerCase();
                String correo = residente.getCorreo() != null ? residente.getCorreo().toLowerCase() : "";
                String carrera = residente.getCarrera() != null ? residente.getCarrera().toLowerCase() : "";

                // Filtrar por cualquier campo
                if (numeroControl.contains(filtroLower) ||
                        nombreCompleto.contains(filtroLower) ||
                        correo.contains(filtroLower) ||
                        carrera.contains(filtroLower)) {

                    String nombreCompletoOriginal = construirNombreCompleto(residente);
                    // ADAPTADO: Usar método helper que funciona con la estructura actual
                    String proyecto = obtenerNombreProyectoSeguro(residente);

                    modeloTabla.addRow(new Object[]{
                            residente.getNumeroControl(),
                            nombreCompletoOriginal,
                            residente.getSemestre(),
                            residente.getCorreo(),
                            proyecto
                    });

                    contador++;
                }
            }

            // Actualizar contador con filtro
            lblContadorResidentes.setText(contador + " residentes encontrados (filtrado)");

        } catch (Exception e) {
            System.err.println("Error al filtrar residentes: " + e.getMessage());
            lblContadorResidentes.setText("Error en búsqueda");
        }
    }

    /**
     * Seleccionar residente para gestión de expediente
     */
    private void seleccionarResidente() {
        int filaSeleccionada = tablaResidentes.getSelectedRow();

        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un residente activo de la lista",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos del residente seleccionado
        String numeroControl = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
        String nombreCompleto = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

        // Validar que sea residente activo (doble verificación)
        try {
            int numControl = Integer.parseInt(numeroControl);
            ModeloResidente residente = ModeloResidente.buscarPorNumeroControl(numControl);

            if (residente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el residente seleccionado en la base de datos.",
                        "Residente no encontrado",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ADAPTADO: Verificación usando método existente
            if (!residente.isResidenteActivo()) {
                JOptionPane.showMessageDialog(this,
                        "El residente seleccionado no está activo.\n" +
                                "Solo se pueden gestionar expedientes de residentes activos.",
                        "Residente no activo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Confirmar selección
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Confirma seleccionar este residente para gestión de expediente?\n\n" +
                            "Residente: " + nombreCompleto + "\n" +
                            "No. Control: " + numeroControl + "\n" +
                            "Estado: RESIDENTE ACTIVO ✓",
                    "Confirmar Selección",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Notificar a la ventana padre
                String seleccion = numeroControl + " - " + nombreCompleto;
                if (ventanaPadre != null) {
                    ventanaPadre.actualizarResidenteSeleccionado(seleccion);
                }
                dispose();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error en el número de control seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error al seleccionar residente: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al procesar la selección: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}