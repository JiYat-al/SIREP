package Vista;

import Modelo.ModeloResidente;
import Controlador.ControladorRegistros;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Agregar estos imports al inicio de VistaRegistros
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
public class VistaRegistros {
    private JPanel panelPrincipal;
    private JTable candidatos;
    private JButton eliminar;
    private JButton editar;
    private JTextField textField1; // Campo de búsqueda
    private JScrollPane scrollPane;
    private JLabel lblBuscar;
    private JLabel lblResultados;
    private JButton btnActualizar;
    private JButton btnLimpiarBusqueda;

    // Modelo de la tabla
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    // Controlador
    private ControladorRegistros controlador;

    // Lista original de residentes
    private List<ModeloResidente> listaOriginal;

    public VistaRegistros() {
        // IMPORTANTE: Inicializar componentes primero
        inicializarComponentes();

        // Inicializar controlador
        controlador = new ControladorRegistros(this);

        // Configurar eventos
        configurarEventos();

        // Cargar datos iniciales
        cargarDatosIniciales();
    }

    // ==================== CONFIGURACIÓN DE COMPONENTES ====================

    /**
     * Inicializar todos los componentes manualmente
     */
    private void inicializarComponentes() {
        // Configurar tabla
        configurarTabla();

        // Configurar campo de búsqueda
        configurarCampoBusqueda();

        // Configurar botones
        configurarBotones();

        // Configurar labels
        configurarLabels();

        // Configurar panel principal
        configurarPanelPrincipal();
    }

    private void createUIComponents() {
        // Este método es para GUI Designer - no se usa en este caso
        inicializarComponentes();
    }

    private void configurarTabla() {
        // Definir columnas
        String[] columnas = {
                "Número Control", "Nombre", "Apellido Paterno", "Apellido Materno",
                "Carrera", "Semestre", "Correo", "Teléfono", "ID Proyecto"
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
                    return Integer.class; // Número Control, Semestre, ID Proyecto
                }
                return String.class;
            }
        };

        // Crear tabla
        candidatos = new JTable(modeloTabla);

        // Configurar propiedades de la tabla
        candidatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        candidatos.setRowHeight(25);
        candidatos.getTableHeader().setReorderingAllowed(false);
        candidatos.setAutoCreateRowSorter(true);

        // Configurar ancho de columnas
        configurarAnchoColumnas();

        // Crear scroll pane
        scrollPane = new JScrollPane(candidatos);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        // Configurar sorter para búsqueda
        sorter = new TableRowSorter<>(modeloTabla);
        candidatos.setRowSorter(sorter);
    }

    private void configurarAnchoColumnas() {
        if (candidatos != null) {
            candidatos.getColumnModel().getColumn(0).setPreferredWidth(100); // Número Control
            candidatos.getColumnModel().getColumn(1).setPreferredWidth(120); // Nombre
            candidatos.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellido Paterno
            candidatos.getColumnModel().getColumn(3).setPreferredWidth(120); // Apellido Materno
            candidatos.getColumnModel().getColumn(4).setPreferredWidth(180); // Carrera
            candidatos.getColumnModel().getColumn(5).setPreferredWidth(70);  // Semestre
            candidatos.getColumnModel().getColumn(6).setPreferredWidth(200); // Correo
            candidatos.getColumnModel().getColumn(7).setPreferredWidth(120); // Teléfono
            candidatos.getColumnModel().getColumn(8).setPreferredWidth(80);  // ID Proyecto
        }
    }

    private void configurarCampoBusqueda() {
        textField1 = new JTextField(20);
        textField1.setToolTipText("Buscar por nombre o número de control");

        // Placeholder effect
        textField1.setForeground(Color.GRAY);
        textField1.setText("Buscar por nombre o número de control...");

        textField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField1.getText().equals("Buscar por nombre o número de control...")) {
                    textField1.setText("");
                    textField1.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField1.getText().isEmpty()) {
                    textField1.setForeground(Color.GRAY);
                    textField1.setText("Buscar por nombre o número de control...");
                }
            }
        });
    }

    private void configurarBotones() {
        eliminar = new JButton("🗑️ Eliminar");
        editar = new JButton("✏️ Editar");
        btnActualizar = new JButton("🔄 Actualizar");
        btnLimpiarBusqueda = new JButton("🧹 Limpiar");

        // Configurar colores
        eliminar.setBackground(new Color(211, 47, 47));
        eliminar.setForeground(Color.WHITE);
        eliminar.setFocusPainted(false);

        editar.setBackground(new Color(25, 118, 210));
        editar.setForeground(Color.WHITE);
        editar.setFocusPainted(false);

        btnActualizar.setBackground(new Color(46, 125, 50));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);

        btnLimpiarBusqueda.setBackground(new Color(158, 158, 158));
        btnLimpiarBusqueda.setForeground(Color.WHITE);
        btnLimpiarBusqueda.setFocusPainted(false);

        // Deshabilitar botones hasta que se seleccione un registro
        eliminar.setEnabled(false);
        editar.setEnabled(false);
    }

    private void configurarLabels() {
        lblBuscar = new JLabel("🔍 Buscar:");
        lblBuscar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        lblResultados = new JLabel("📊 Total de registros: 0");
        lblResultados.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        lblResultados.setForeground(Color.GRAY);
    }

    private void configurarPanelPrincipal() {
        panelPrincipal = new JPanel(new BorderLayout());

        // Panel superior con búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(lblBuscar);
        panelSuperior.add(textField1);
        panelSuperior.add(btnLimpiarBusqueda);
        panelSuperior.add(Box.createHorizontalStrut(20));
        panelSuperior.add(btnActualizar);

        // Panel inferior con botones y resultados
        JPanel panelInferior = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(editar);
        panelBotones.add(eliminar);

        JPanel panelResultados = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelResultados.add(lblResultados);

        panelInferior.add(panelBotones, BorderLayout.WEST);
        panelInferior.add(panelResultados, BorderLayout.EAST);

        // Agregar todo al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // ==================== CONFIGURACIÓN DE EVENTOS ====================

    private void configurarEventos() {
        // Evento de búsqueda en tiempo real
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarDatos();
            }
        });

        // Eventos de botones - DELEGACIÓN AL CONTROLADOR
        eliminar.addActionListener(e -> controlador.eliminarRegistroSeleccionado());
        editar.addActionListener(e -> controlador.editarRegistroSeleccionado());
        btnActualizar.addActionListener(e -> controlador.cargarTodosLosRegistros());
        btnLimpiarBusqueda.addActionListener(e -> limpiarBusqueda());

        // Evento de selección en la tabla
        candidatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = candidatos.getSelectedRow() != -1;
                eliminar.setEnabled(haySeleccion);
                editar.setEnabled(haySeleccion);
            }
        });

        // Escape para limpiar búsqueda
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        panelPrincipal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        panelPrincipal.getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarBusqueda();
            }
        });
    }

    // ==================== MÉTODOS DE MANIPULACIÓN DE DATOS ====================

    /**
     * Cargar residentes en la tabla
     */
    public void cargarResidentes(List<ModeloResidente> residentes) {
        // Guardar lista original para búsquedas
        this.listaOriginal = residentes;

        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Agregar datos
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

        // Actualizar contador
        actualizarContadorResultados();
    }

    /**
     * Filtrar datos según el texto de búsqueda
     */
    private void filtrarDatos() {
        String textoBusqueda = textField1.getText().trim();

        // Si es el placeholder o está vacío, mostrar todos
        if (textoBusqueda.isEmpty() || textoBusqueda.equals("Buscar por nombre o número de control...")) {
            sorter.setRowFilter(null);
        } else {
            // Crear filtro que busque en nombre, apellidos y número de control
            RowFilter<DefaultTableModel, Object> filtro = RowFilter.regexFilter(
                    "(?i)" + textoBusqueda, // Case insensitive
                    0, 1, 2, 3 // Buscar en columnas: Número Control, Nombre, Apellido Paterno, Apellido Materno
            );
            sorter.setRowFilter(filtro);
        }

        // Actualizar contador
        actualizarContadorResultados();
    }

    /**
     * Limpiar búsqueda
     */
    private void limpiarBusqueda() {
        textField1.setText("");
        textField1.setForeground(Color.GRAY);
        textField1.setText("Buscar por nombre o número de control...");
        sorter.setRowFilter(null);
        actualizarContadorResultados();
    }

    /**
     * Actualizar contador de resultados
     */
    private void actualizarContadorResultados() {
        int totalRegistros = modeloTabla.getRowCount();
        int registrosFiltrados = candidatos.getRowCount();

        if (sorter.getRowFilter() == null) {
            lblResultados.setText("📊 Total de registros: " + totalRegistros);
        } else {
            lblResultados.setText("📊 Mostrando: " + registrosFiltrados + " de " + totalRegistros + " registros");
        }
    }

    /**
     * Cargar datos iniciales
     */
    private void cargarDatosIniciales() {
        if (controlador != null) {
            controlador.cargarTodosLosRegistros();
        }
    }

    // ==================== MÉTODOS PARA EL CONTROLADOR ====================

    /**
     * Obtener el residente seleccionado
     */
    public ModeloResidente getResidenteSeleccionado() {
        int filaSeleccionada = candidatos.getSelectedRow();
        if (filaSeleccionada != -1) {
            // Convertir índice de vista a índice de modelo (por si hay filtros activos)
            int filaModelo = candidatos.convertRowIndexToModel(filaSeleccionada);

            if (listaOriginal != null && filaModelo < listaOriginal.size()) {
                return listaOriginal.get(filaModelo);
            }
        }
        return null;
    }

    /**
     * Mostrar mensaje al usuario
     */
    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(panelPrincipal, mensaje, titulo, tipo);
    }

    /**
     * Mostrar mensaje de confirmación
     */
    public boolean mostrarConfirmacion(String mensaje, String titulo) {
        int opcion = JOptionPane.showConfirmDialog(
                panelPrincipal, mensaje, titulo,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return opcion == JOptionPane.YES_OPTION;
    }

    /**
     * Actualizar tabla después de operaciones
     */
    public void actualizarTabla() {
        controlador.cargarTodosLosRegistros();
    }

    /**
     * Cambiar cursor
     */
    public void setCursor(Cursor cursor) {
        if (panelPrincipal != null) {
            panelPrincipal.setCursor(cursor);
        }
    }

    // ==================== GETTERS ====================

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public JTable getCandidatos() {
        return candidatos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public String getTextoBusqueda() {
        String texto = textField1.getText().trim();
        return texto.equals("Buscar por nombre o número de control...") ? "" : texto;
    }

    // ==================== MÉTODO MAIN PARA PRUEBAS ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Registros de Residentes - Sistema SIREP");
                VistaRegistros vista = new VistaRegistros();

                frame.setContentPane(vista.getPanelPrincipal());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1000, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}