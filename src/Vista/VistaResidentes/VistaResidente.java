package Vista.VistaResidentes;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import Modelo.ModeloResidente;
import Controlador.ControladorResidente;

public class VistaResidente {
    // Panel principal que será usado por VistaRegistros
    private JPanel panelPrincipal;

    private DefaultTableModel modeloTabla;
    private JTable tablaResidentes;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private List<ModeloResidente> listaResidentes = new ArrayList<>();
    private ControladorResidente controlador;

    public VistaResidente() {
        // Inicializar controlador
        controlador = new ControladorResidente(this);

        // Crear el panel principal en lugar de configurar JFrame
        crearPanelPrincipal();
    }

    private void crearPanelPrincipal() {
        // Panel principal con fondo degradado
        panelPrincipal = new JPanel(new BorderLayout()) {
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

                // Líneas curvas decorativas en la esquina superior izquierda
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(new Color(180, 170, 255, 80));
                g2.drawArc(-120, -120, 300, 300, 0, 360);
                g2.setColor(new Color(140, 130, 220, 60));
                g2.drawArc(-80, -80, 200, 200, 0, 360);

                // Líneas curvas decorativas en la esquina inferior derecha
                g2.setColor(new Color(180, 170, 255, 60));
                g2.drawArc(getWidth() - 180, getHeight() - 180, 160, 160, 0, 360);
                g2.setColor(new Color(140, 130, 220, 40));
                g2.drawArc(getWidth() - 120, getHeight() - 120, 100, 100, 0, 360);
            }
        };
        panelPrincipal.setBackground(colorFondo);

        // Panel contenido (sin barra lateral, ya que la maneja VistaRegistros)
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);

        // Encabezado con fondo blanco y borde inferior
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

        JLabel lblTitulo = new JLabel("Gestión de Residentes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        header.add(lblTitulo, BorderLayout.WEST);

        // Panel de botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelBotones.setOpaque(false);

        // Botón Cargar Excel
        JButton btnCargarExcel = crearBotonAccion(" Cargar Excel", colorPrincipal);
        btnCargarExcel.addActionListener(e -> controlador.cargarExcelEnTabla());
        panelBotones.add(btnCargarExcel);

        // Botón Importar - CAMBIADO de "Importar Excel" a solo "Importar"
        JButton btnImportar = crearBotonAccion(" Importar", new Color(34, 139, 34));
        btnImportar.addActionListener(e -> controlador.importarABaseDatos());
        panelBotones.add(btnImportar);

        // Botón Agregar Manual
        JButton btnAgregarManual = crearBotonAccion(" Agregar Manual", new Color(255, 140, 0));
        btnAgregarManual.addActionListener(e -> abrirAgregarManual());
        panelBotones.add(btnAgregarManual);

        // Botón Limpiar Tabla
        JButton btnLimpiarTabla = crearBotonAccion(" Limpiar Tabla", new Color(220, 53, 69));
        btnLimpiarTabla.addActionListener(e -> limpiarTablaConConfirmacion());
        panelBotones.add(btnLimpiarTabla);

        header.add(panelBotones, BorderLayout.EAST);
        panelContenido.add(header, BorderLayout.NORTH);

        // Configurar tabla
        configurarTabla();

        // Scroll de la tabla
        JScrollPane scrollTabla = new JScrollPane(tablaResidentes);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 38, 38, 38));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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
        panelContenido.add(scrollTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
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
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(60,60,100,60));
                g2.fillRoundRect(4, 6, getWidth()-8, getHeight()-4, 30, 30);
                // Gradiente
                GradientPaint grad = new GradientPaint(0, 0, hover ? color.darker() : color,
                        getWidth(), getHeight(), color.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
    }

    private void configurarTabla() {
        String[] columnas = {
                "No. Control", "Nombre", "Apellido P.", "Apellido M.",
                "Carrera", "Semestre", "Correo", "Telefono"
        };

        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Toda la tabla de solo lectura
            }
        };

        tablaResidentes = new JTable(modeloTabla) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
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

        tablaResidentes.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tablaResidentes.setRowHeight(40);
        tablaResidentes.setShowVerticalLines(false);
        tablaResidentes.setShowHorizontalLines(true);
        tablaResidentes.setGridColor(new Color(230, 230, 250));
        tablaResidentes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tablaResidentes.getTableHeader().setBackground(new Color(220, 219, 245));
        tablaResidentes.getTableHeader().setForeground(colorPrincipal);
        tablaResidentes.getTableHeader().setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) tablaResidentes.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Configurar anchos de columna
        tablaResidentes.getColumnModel().getColumn(0).setPreferredWidth(100); // No. Control
        tablaResidentes.getColumnModel().getColumn(1).setPreferredWidth(120); // Nombre
        tablaResidentes.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellido P.
        tablaResidentes.getColumnModel().getColumn(3).setPreferredWidth(120); // Apellido M.
        tablaResidentes.getColumnModel().getColumn(4).setPreferredWidth(180); // Carrera
        tablaResidentes.getColumnModel().getColumn(5).setPreferredWidth(80);  // Semestre
        tablaResidentes.getColumnModel().getColumn(6).setPreferredWidth(200); // Correo
        tablaResidentes.getColumnModel().getColumn(7).setPreferredWidth(120); // Telefono
    }

    // ==================== MÉTODOS DE FORMATEO DE TEXTO ====================

    /**
     * Formatea texto con primera letra mayúscula y demás en minúscula
     */
    private String formatearTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        String textoLimpio = texto.trim();

        // Si es una sola palabra
        if (!textoLimpio.contains(" ")) {
            return textoLimpio.substring(0, 1).toUpperCase() + textoLimpio.substring(1).toLowerCase();
        }

        // Si son múltiples palabras, formatear cada una
        String[] palabras = textoLimpio.split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                String palabra = palabras[i].substring(0, 1).toUpperCase() + palabras[i].substring(1).toLowerCase();
                resultado.append(palabra);
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }

        return resultado.toString();
    }

    // ==================== MÉTODOS DE MANIPULACIÓN DE DATOS ====================

    public void agregarResidente(ModeloResidente residente) {
        listaResidentes.add(residente);
        Object[] fila = {
                residente.getNumeroControl(),
                formatearTexto(residente.getNombre()),
                formatearTexto(residente.getApellidoPaterno()),
                formatearTexto(residente.getApellidoMaterno()),
                formatearTexto(residente.getCarrera()),
                residente.getSemestre(),
                residente.getCorreo(), // El correo no se formatea
                residente.getTelefono()
        };
        modeloTabla.addRow(fila);
    }

    public void cargarResidentes(List<ModeloResidente> residentes) {
        limpiarTabla();
        for (ModeloResidente residente : residentes) {
            agregarResidente(residente);
        }
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
        listaResidentes.clear();
    }

    /**
     * Limpiar tabla con confirmación del usuario
     */
    private void limpiarTablaConConfirmacion() {
        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "La tabla ya está vacía",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Está seguro de limpiar toda la tabla?\n" +
                        "Se eliminarán " + listaResidentes.size() + " registro(s) de la tabla temporal.\n\n" +
                        "Esta acción no afecta la base de datos.",
                "Confirmar limpieza",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            limpiarTabla();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Tabla limpiada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public List<ModeloResidente> getListaResidentes() {
        return new ArrayList<>(listaResidentes);
    }

    public ModeloResidente getResidenteSeleccionado() {
        int filaSeleccionada = tablaResidentes.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < listaResidentes.size()) {
            return listaResidentes.get(filaSeleccionada);
        }
        return null;
    }

    // MÉTODO MEJORADO: Menos mensajes de aviso, más intuitivo
    private void abrirAgregarManual() {
        Window parentWindow = SwingUtilities.getWindowAncestor(panelPrincipal);
        Frame parentFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;

        AgregarManual dialogo = new AgregarManual(parentFrame);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            // Crear ModeloResidente con los datos del diálogo - FORMATEANDO TEXTOS
            ModeloResidente residente = new ModeloResidente();
            residente.setNumeroControl(Integer.parseInt(dialogo.getNumeroControl().replaceAll("[\\s-]", "")));
            residente.setNombre(formatearTexto(dialogo.getNombre()));
            residente.setApellidoPaterno(formatearTexto(dialogo.getApellidoPaterno()));
            residente.setApellidoMaterno(dialogo.getApellidoMaterno().isEmpty() ? null : formatearTexto(dialogo.getApellidoMaterno()));
            residente.setCarrera(formatearTexto(dialogo.getCarrera()));
            residente.setSemestre(Integer.parseInt(dialogo.getSemestre()));
            residente.setCorreo(dialogo.getCorreo().toLowerCase()); // Correo en minúsculas
            residente.setTelefono(dialogo.getTelefono().isEmpty() ? null : dialogo.getTelefono());
            residente.setIdProyecto(1);

            // Usar el controlador para agregar y validar
            controlador.agregarResidenteManual(residente);

            // MENSAJE REDUCIDO Y MÁS INTUITIVO
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Residente agregado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarResidente(int row) {
        ModeloResidente residente = listaResidentes.get(row);
        Window parentWindow = SwingUtilities.getWindowAncestor(panelPrincipal);
        JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;

        DialogoResidente dialogo = new DialogoResidente(parentFrame, colorPrincipal, residente);
        dialogo.setVisible(true);
        if (dialogo.isConfirmado()) {
            listaResidentes.set(row, dialogo.getResidente());
            cargarResidentes(listaResidentes);

            // Revalidar todos después de la edición
            controlador.revalidarTodosLosResidentes();
        }
    }

    // ==================== MÉTODOS DE UTILIDAD ====================

    public void setCursor(Cursor cursor) {
        if (panelPrincipal != null) {
            panelPrincipal.setCursor(cursor);
        }
    }

    // Método principal que devuelve el panel para VistaRegistros
    public JPanel getPanelResidente() {
        return panelPrincipal;
    }

    public JTable getTablaResidentes() {
        return tablaResidentes;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // ==================== MÉTODOS PARA SINCRONIZACIÓN ====================

    /**
     * Obtener el controlador para acceso directo
     */
    public ControladorResidente getControlador() {
        return controlador;
    }

    /**
     * Método para revalidar todos los residentes (útil después de cambios)
     */
    public void revalidarResidentes() {
        if (controlador != null) {
            controlador.revalidarTodosLosResidentes();
        }
    }

    // Método main para pruebas independientes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test VistaResidente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            VistaResidente vista = new VistaResidente();
            frame.add(vista.getPanelResidente());

            frame.setVisible(true);
        });
    }
}