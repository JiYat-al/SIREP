package Vista;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BancoProyectosUI extends JFrame {
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private ArrayList<Proyecto> listaProyectos = new ArrayList<>();

    public BancoProyectosUI() {
        setTitle("Banco de Proyectos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(colorFondo);

        // Barra lateral
        JPanel barraLateral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(colorPrincipal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        barraLateral.setPreferredSize(new Dimension(90, 0));
        barraLateral.setLayout(new GridBagLayout());
        JLabel icono = new JLabel("\uD83D\uDCCB", SwingConstants.CENTER);
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));
        icono.setForeground(Color.WHITE);
        JLabel verticalTitle = new JLabel("<html>B<br>A<br>N<br>C<br>O<br><br>D<br>E<br><br>P<br>R<br>O<br>Y<br>E<br>C<br>T<br>O<br>S</html>");
        verticalTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        verticalTitle.setForeground(new Color(245, 243, 255, 180));
        verticalTitle.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0; gbcL.gridy = 0; gbcL.insets = new Insets(0, 0, 14, 0);
        barraLateral.add(icono, gbcL);
        gbcL.gridy++;
        barraLateral.add(verticalTitle, gbcL);
        mainPanel.add(barraLateral, BorderLayout.WEST);

        // Panel contenido
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(colorFondo);

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 250)));
        JLabel lblTitulo = new JLabel("Banco de Proyectos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 0));
        header.add(lblTitulo, BorderLayout.WEST);

        // Botón Registrar
        JButton btnNuevo = new JButton("Registrar nuevo proyecto");
        btnNuevo.setBackground(colorPrincipal);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnNuevo.setBorder(BorderFactory.createEmptyBorder(13, 34, 13, 34));
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevo.setFocusPainted(false);
        btnNuevo.setOpaque(true);
        btnNuevo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 6, 0, new Color(120, 120, 170, 60)),
                BorderFactory.createEmptyBorder(13, 34, 13, 34)
        ));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 38, 20));
        btnPanel.setOpaque(false);
        btnPanel.add(btnNuevo);
        header.add(btnPanel, BorderLayout.EAST);
        panelContenido.add(header, BorderLayout.NORTH);

        // Tabla de proyectos
        String[] columnas = {
                "ID", "Empresa", "Descripción", "Alumnos requeridos",
                "Estatus elaboración", "Origen", "Asesor", "Revisor", "" // Sin título para el botón
        };
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la última columna (botón) es editable
                return column == 8;
            }
        };
        tabla = new JTable(modelo) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo el botón puede "editarse"
                return column == 8;
            }
            private int hoveredRow = -1;
            {
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setRowSelectionAllowed(false);
                setColumnSelectionAllowed(false);
                getTableHeader().setReorderingAllowed(false); // No permite mover columnas
                setFocusable(false);
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
                c.setBackground(Color.WHITE);
                c.setForeground(new Color(60, 60, 100));
                return c;
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

        // Botón redondo morado en la tabla
        tabla.getColumnModel().getColumn(8).setCellRenderer(new BotonRedondoRenderer());
        tabla.getColumnModel().getColumn(8).setCellEditor(new BotonRedondoEditor(new JCheckBox()));

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(32, 38, 38, 38));
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

        mainPanel.add(panelContenido, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Proyectos de ejemplo:
        agregarProyectosDeEjemplo();
        cargarTabla();

        // ---- Evento del botón Registrar (Abre formulario) ----
        btnNuevo.addActionListener(e -> {
            RegistrarProyectoDialog dialog = new RegistrarProyectoDialog(this, colorPrincipal, null);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                listaProyectos.add(dialog.proyectoEditado);
                cargarTabla();
            }
        });
    }

    private void agregarProyectosDeEjemplo() {
        listaProyectos.add(new Proyecto(1, "Empresa A", "Proyecto de software para gestión escolar", 3, "Disponible", "Banco de Proyectos", "Docente X", "Docente Y"));
        listaProyectos.add(new Proyecto(2, "Empresa B", "Sistema de inventarios", 2, "No disponible", "Externo", "Docente Y", "Docente Z"));
        listaProyectos.add(new Proyecto(3, "Empresa C", "App móvil para ventas", 4, "Terminado", "Banco de Proyectos", "Docente Z", "Docente X"));
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Proyecto p : listaProyectos) {
            modelo.addRow(new Object[]{
                    p.id, p.empresa, p.descripcion, p.alumnos,
                    p.estatus, p.origen, p.asesor, p.revisor, ""
            });
        }
    }

    // Botón Editar bonito en la tabla
    class BotonRedondoRenderer extends JButton implements TableCellRenderer {
        public BotonRedondoRenderer() {
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText("Editar");
            setForeground(Color.WHITE);
            setBackground(colorPrincipal);
            setBorder(BorderFactory.createEmptyBorder());
            return this;
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorPrincipal);
            g2.fillRoundRect(0, 2, getWidth()-1, getHeight()-5, 24, 24); // Circular/Pill
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class BotonRedondoEditor extends DefaultCellEditor {
        private JButton button;
        private int selectedRow;

        public BotonRedondoEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Editar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(colorPrincipal);
                    g2.fillRoundRect(0, 2, getWidth()-1, getHeight()-5, 24, 24);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.addActionListener(e -> {
                editarProyecto(selectedRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }
    }

    private void editarProyecto(int row) {
        Proyecto proyecto = listaProyectos.get(row);
        RegistrarProyectoDialog dialog = new RegistrarProyectoDialog(this, colorPrincipal, proyecto);
        dialog.setVisible(true);
        if (dialog.seRegistro) {
            listaProyectos.set(row, dialog.proyectoEditado);
            cargarTabla();
        }
    }

    static class Proyecto {
        int id;
        String empresa, descripcion, estatus, origen, asesor, revisor;
        int alumnos;
        Proyecto(int id, String empresa, String descripcion, int alumnos, String estatus, String origen, String asesor, String revisor) {
            this.id = id; this.empresa = empresa; this.descripcion = descripcion;
            this.alumnos = alumnos; this.estatus = estatus; this.origen = origen; this.asesor = asesor; this.revisor = revisor;
        }
    }

    public static class RegistrarProyectoDialog extends JDialog {
        public boolean seRegistro = false;
        public Proyecto proyectoEditado;
        public RegistrarProyectoDialog(JFrame parent, Color colorPrincipal, Proyecto proyecto) {
            super(parent, (proyecto == null ? "Registrar" : "Editar") + " proyecto", true);
            setSize(480, 560);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(10, 2, 2, 2);

            panel.add(labelField("Empresa", colorPrincipal), gbc);
            gbc.gridy++;
            JComboBox<String> cbEmpresa = new JComboBox<>(new String[] {"Empresa A", "Empresa B", "Empresa C"});
            personalizaCombo(cbEmpresa, colorPrincipal);
            panel.add(cbEmpresa, gbc);

            gbc.gridy++;
            panel.add(labelField("Descripción", colorPrincipal), gbc);
            gbc.gridy++;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            JTextArea txtDescripcion = new JTextArea(3, 22);
            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);
            txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
            scrollDesc.setPreferredSize(new Dimension(240, 60));
            scrollDesc.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
            panel.add(scrollDesc, gbc);
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;

            gbc.gridy++; panel.add(labelField("Alumnos requeridos", colorPrincipal), gbc);
            gbc.gridy++; JSpinner spAlumnos = new JSpinner(new SpinnerNumberModel(1,1,20,1)); panel.add(spAlumnos, gbc);

            gbc.gridy++; panel.add(labelField("Estatus elaboración", colorPrincipal), gbc);
            gbc.gridy++;
            JComboBox<String> cbEstatus = new JComboBox<>(new String[]{"Disponible", "No disponible", "Terminado"});
            personalizaCombo(cbEstatus, colorPrincipal);
            panel.add(cbEstatus, gbc);

            gbc.gridy++; panel.add(labelField("Origen", colorPrincipal), gbc);
            gbc.gridy++;
            JComboBox<String> cbOrigen = new JComboBox<>(new String[]{"Banco de Proyectos", "Externo"});
            personalizaCombo(cbOrigen, colorPrincipal);
            panel.add(cbOrigen, gbc);

            gbc.gridy++; panel.add(labelField("Asesor", colorPrincipal), gbc);
            gbc.gridy++;
            JComboBox<String> cbAsesor = new JComboBox<>(new String[] {"Docente X", "Docente Y", "Docente Z"});
            personalizaCombo(cbAsesor, colorPrincipal);
            panel.add(cbAsesor, gbc);

            gbc.gridy++; panel.add(labelField("Revisor", colorPrincipal), gbc);
            gbc.gridy++;
            JComboBox<String> cbRevisor = new JComboBox<>(new String[] {"Docente X", "Docente Y", "Docente Z"});
            personalizaCombo(cbRevisor, colorPrincipal);
            panel.add(cbRevisor, gbc);

            gbc.gridy++; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(22, 4, 4, 4); gbc.gridwidth = 2;
            JButton btnGuardar = new JButton(proyecto == null ? "Guardar" : "Actualizar");
            btnGuardar.setBackground(colorPrincipal);
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setOpaque(true);
            panel.add(btnGuardar, gbc);

            add(panel, BorderLayout.CENTER);

            // Si es edición, llena los campos
            if (proyecto != null) {
                cbEmpresa.setSelectedItem(proyecto.empresa);
                txtDescripcion.setText(proyecto.descripcion);
                spAlumnos.setValue(proyecto.alumnos);
                cbEstatus.setSelectedItem(proyecto.estatus);
                cbOrigen.setSelectedItem(proyecto.origen);
                cbAsesor.setSelectedItem(proyecto.asesor);
                cbRevisor.setSelectedItem(proyecto.revisor);
            }

            btnGuardar.addActionListener(ev -> {
                String empresa = cbEmpresa.getSelectedItem() != null ? cbEmpresa.getSelectedItem().toString().trim() : "";
                String descripcion = txtDescripcion.getText().trim();
                int alumnos = (Integer) spAlumnos.getValue();
                String estatus = cbEstatus.getSelectedItem() != null ? cbEstatus.getSelectedItem().toString() : "";
                String origen = cbOrigen.getSelectedItem() != null ? cbOrigen.getSelectedItem().toString() : "";
                String asesor = cbAsesor.getSelectedItem() != null ? cbAsesor.getSelectedItem().toString() : "";
                String revisor = cbRevisor.getSelectedItem() != null ? cbRevisor.getSelectedItem().toString() : "";

                if (empresa.isEmpty() || empresa.equals("Selecciona")) {
                    JOptionPane.showMessageDialog(this, "Selecciona la empresa.", "Validación", JOptionPane.WARNING_MESSAGE);
                    cbEmpresa.requestFocus(); return;
                }
                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtDescripcion.requestFocus(); return;
                }
                if (alumnos < 1) {
                    JOptionPane.showMessageDialog(this, "El número de alumnos requeridos debe ser mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
                    spAlumnos.requestFocus(); return;
                }
                if (estatus.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona el estatus de elaboración.", "Validación", JOptionPane.WARNING_MESSAGE);
                    cbEstatus.requestFocus(); return;
                }
                if (origen.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona el origen.", "Validación", JOptionPane.WARNING_MESSAGE);
                    cbOrigen.requestFocus(); return;
                }
                if (asesor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona un asesor.", "Validación", JOptionPane.WARNING_MESSAGE);
                    cbAsesor.requestFocus(); return;
                }
                if (revisor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona un revisor.", "Validación", JOptionPane.WARNING_MESSAGE);
                    cbRevisor.requestFocus(); return;
                }
                if (asesor.equals(revisor)) {
                    JOptionPane.showMessageDialog(this, "El asesor y el revisor deben ser diferentes.", "Validación", JOptionPane.WARNING_MESSAGE);
                    cbRevisor.requestFocus(); return;
                }

                int id = (proyecto == null) ? generarId() : proyecto.id;
                proyectoEditado = new Proyecto(id, empresa, descripcion, alumnos, estatus, origen, asesor, revisor);
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
        private static void personalizaCombo(JComboBox<String> combo, Color colorPrincipal) {
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            combo.setBackground(new Color(248,248,255));
            combo.setForeground(colorPrincipal.darker());
            combo.setUI(new BasicComboBoxUI());
        }

        private int generarId() {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof BancoProyectosUI) {
                BancoProyectosUI ui = (BancoProyectosUI) w;
                int max = 0;
                for (Proyecto p : ui.listaProyectos) {
                    if (p.id > max) max = p.id;
                }
                return max + 1;
            }
            return 1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BancoProyectosUI().setVisible(true);
        });
    }
}
