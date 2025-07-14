package Vista;

import Controlardor.CtrlEmpresa;
import Modelo.Empresa;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

public class BancoEmpresasUI extends JFrame {
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private ArrayList<Modelo.Empresa> listEmpresas = new ArrayList<>();
    private ArrayList<Empresa> listaEmpresas = new ArrayList<>();

    public BancoEmpresasUI() {
        setTitle("Empresas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        // Fondo degradado con líneas decorativas y detalles
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
        mainPanel.setBackground(colorFondo);

        // Barra lateral mejorada
        JPanel barraLateral = new JPanel() {
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
        barraLateral.setLayout(new GridBagLayout());

        // Icono y título vertical en la barra lateral
        JLabel icono = new JLabel("\uD83C\uDFE2", SwingConstants.CENTER); // Edificio emoji
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icono.setForeground(Color.WHITE);
        icono.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel verticalTitle = new JLabel("<html>E<br>M<br>P<br>R<br>E<br>S<br>A<br>S</html>");
        verticalTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        verticalTitle.setForeground(new Color(245, 243, 255, 200));
        verticalTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Botón de ayuda en la barra lateral
        JButton btnAyuda = new JButton("?");
        btnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnAyuda.setForeground(colorPrincipal);
        btnAyuda.setBackground(Color.WHITE);
        btnAyuda.setFocusPainted(false);
        btnAyuda.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAyuda.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí puedes registrar, editar y consultar empresas.\n" +
                            "Usa el botón 'Registrar nueva empresa' para agregar una nueva.\n" +
                            "Haz clic en 'Editar' para modificar una empresa existente.",
                    "Ayuda", JOptionPane.INFORMATION_MESSAGE);
        });

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0; gbcL.gridy = 0; gbcL.insets = new Insets(0, 0, 18, 0);
        barraLateral.add(icono, gbcL);
        gbcL.gridy++;
        barraLateral.add(verticalTitle, gbcL);
        gbcL.gridy++;
        barraLateral.add(btnAyuda, gbcL);
        mainPanel.add(barraLateral, BorderLayout.WEST);

        // Panel contenido
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
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Solo flecha como botón de regresar
        JButton btnRegresar = new JButton("\u2190");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRegresar.setForeground(colorPrincipal);
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.setOpaque(true);
        btnRegresar.addActionListener(e -> {
            dispose();
            // new MenuPrincipalUI().setVisible(true);
        });

        // Panel superior para el botón
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTop.setOpaque(false);
        panelTop.add(btnRegresar);

        // Agrega el panelTop al principio del BorderLayout.NORTH de tu ventana principal
        mainPanel.add(panelTop, BorderLayout.NORTH);

        // Título grande alineado a la izquierda
        JLabel lblTitulo = new JLabel("Banco de Empresas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 0));
        header.add(lblTitulo, BorderLayout.WEST);

        // Botón Registrar nueva empresa alineado a la derecha
        JButton btnNuevo = new JButton("Registrar nueva empresa") {
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
                g2.setColor(new Color(60,60,100,60));
                g2.fillRoundRect(4, 6, getWidth()-8, getHeight()-4, 40, 40);
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
        btnPanel.add(btnNuevo);
        header.add(btnPanel, BorderLayout.EAST);

        panelContenido.add(header, BorderLayout.NORTH);

        // Tabla de empresas
        String[] columnas = {
                "ID", "Nombre", "Dirección", "Responsable", "Teléfono", "Correo electrónico", ""
        };
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        tabla = new JTable(modelo) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
            private int hoveredRow = -1;
            {
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setRowSelectionAllowed(false);
                setColumnSelectionAllowed(false);
                getTableHeader().setReorderingAllowed(false);
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

        // Reemplaza el renderer/editor de la columna de acciones:
        tabla.getColumnModel().getColumn(6).setCellRenderer(new BotonAccionesRenderer());
        tabla.getColumnModel().getColumn(6).setCellEditor(new BotonAccionesEditor(new JCheckBox()));

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

        cargarTabla();

        // ---- Evento del botón Registrar (Abre formulario) ----
        btnNuevo.addActionListener(e -> {
            RegistrarEmpresaDialog dialog = new RegistrarEmpresaDialog(this, colorPrincipal, null);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                cargarTabla();
            }
        });
    }

    private void cargarTabla() {
        listEmpresas = CtrlEmpresa.obtenerEmpresas();
        modelo.setRowCount(0);
        for (Modelo.Empresa e : listEmpresas) {
            modelo.addRow(new Object[]{
                    e.getId(), e.getNombre(), e.getDireccion(), e.getResponsable(), e.getTelefono(), e.getCorreo(), ""
            });
        }
    }

    // Botón Editar y Eliminar en la tabla
    class BotonAccionesRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEditar;
        private final JButton btnEliminar;

        public BotonAccionesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0)); // Centrado y con espacio
            setOpaque(false);

            btnEditar = new JButton("Editar");
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEditar.setForeground(Color.WHITE); // Letras blancas
            btnEditar.setBackground(colorPrincipal);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditar.setOpaque(true);

            btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEliminar.setForeground(Color.WHITE); // Letras blancas
            btnEliminar.setBackground(new Color(200, 60, 60));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEliminar.setOpaque(true);

            add(btnEditar);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    class BotonAccionesEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnEditar;
        private JButton btnEliminar;
        private int selectedRow;

        public BotonAccionesEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0)); // Centrado y con espacio
            panel.setOpaque(false);

            btnEditar = new JButton("Editar");
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setBackground(colorPrincipal);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditar.setOpaque(true);

            btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setBackground(new Color(200, 60, 60));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
            btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEliminar.setOpaque(true);

            btnEditar.addActionListener(e -> {
                BancoEmpresasUI.this.editarEmpresa(selectedRow);
            });

            btnEliminar.addActionListener(e -> {
                // --- Aquí va la lógica para eliminar el registro de la base de datos ---
                // Por ejemplo:
                // eliminarEmpresaDeBaseDeDatos(listaEmpresas.get(selectedRow));
                listaEmpresas.remove(selectedRow);
                cargarTabla();
                // -----------------------------------------------
            });

            panel.add(btnEditar);
            panel.add(btnEliminar);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            return panel;
        }
    }

    // Método para editar empresa
    private void editarEmpresa(int row) {
        if (row >= 0 && row < listaEmpresas.size()) {
            Empresa empresa = listaEmpresas.get(row);
            RegistrarEmpresaDialog dialog = new RegistrarEmpresaDialog(this, colorPrincipal, empresa);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                listaEmpresas.set(row, dialog.empresaEditada);
                cargarTabla();
            }
        }
    }

    // Método para eliminar empresa de la base de datos (implementa según tu sistema)
    // private void eliminarEmpresaDeBaseDeDatos(Empresa empresa) {
    //     // TODO: Conectar y eliminar el registro en la base de datos
    // }

    static class Empresa {
        int id;
        String nombre, direccion, responsable, telefono, correo;
        Empresa(int id, String nombre, String direccion, String responsable, String telefono, String correo) {
            this.id = id; this.nombre = nombre; this.direccion = direccion;
            this.responsable = responsable; this.telefono = telefono; this.correo = correo;
        }
    }

    public static class RegistrarEmpresaDialog extends JDialog {
        public boolean seRegistro = false;
        public Empresa empresaEditada;
        public RegistrarEmpresaDialog(JFrame parent, Color colorPrincipal, Empresa empresa) {
            super(parent, (empresa == null ? "Registrar" : "Editar") + " empresa", true);
            setSize(480, 420); // Más compacto y proporcionado
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 8, 10, 8);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos
            JLabel lblNombre = labelField("Nombre", colorPrincipal);
            JTextField txtNombre = new JTextField(20);

            JLabel lblDireccion = labelField("Dirección", colorPrincipal);
            JTextField txtDireccion = new JTextField(20);

            JLabel lblResponsable = labelField("Responsable", colorPrincipal);
            JTextField txtResponsable = new JTextField(20);

            JLabel lblTelefono = labelField("Teléfono", colorPrincipal);
            JTextField txtTelefono = new JTextField(20);

            JLabel lblCorreo = labelField("Correo electrónico", colorPrincipal);
            JTextField txtCorreo = new JTextField(20);

            // Etiquetas columna 0
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(lblNombre, gbc);
            gbc.gridy++;
            panel.add(lblDireccion, gbc);
            gbc.gridy++;
            panel.add(lblResponsable, gbc);
            gbc.gridy++;
            panel.add(lblTelefono, gbc);
            gbc.gridy++;
            panel.add(lblCorreo, gbc);

            // Campos columna 1
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
            panel.add(txtNombre, gbc);
            gbc.gridy++;
            panel.add(txtDireccion, gbc);
            gbc.gridy++;
            panel.add(txtResponsable, gbc);
            gbc.gridy++;
            panel.add(txtTelefono, gbc);
            gbc.gridy++;
            panel.add(txtCorreo, gbc);

            // Botón guardar centrado y ancho
            JButton btnGuardar = new JButton(empresa == null ? "Guardar" : "Actualizar");
            btnGuardar.setBackground(colorPrincipal);
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 17));
            btnGuardar.setBorder(BorderFactory.createEmptyBorder(12, 38, 12, 38));
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setOpaque(true);

            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(24, 8, 8, 8);
            panel.add(btnGuardar, gbc);

            add(panel, BorderLayout.CENTER);

            // Si es edición, llena los campos
            if (empresa != null) {
                txtNombre.setText(empresa.nombre);
                txtDireccion.setText(empresa.direccion);
                txtResponsable.setText(empresa.responsable);
                txtTelefono.setText(empresa.telefono);
                txtCorreo.setText(empresa.correo);
            }

            btnGuardar.addActionListener(ev -> {

                if (txtNombre.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNombre.requestFocus(); return;
                }
                if (txtDireccion.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtDireccion.requestFocus(); return;
                }
                if (txtResponsable.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El responsable no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtResponsable.requestFocus(); return;
                }
                if (txtTelefono.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El teléfono no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtTelefono.requestFocus(); return;
                }
                if (txtCorreo.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El correo no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtCorreo.requestFocus(); return;
                }

                CtrlEmpresa.btnGuardar(txtNombre,txtDireccion,txtResponsable,txtTelefono,txtCorreo);
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

        private int generarId(JFrame parent) {
            if (parent instanceof BancoEmpresasUI) {
                BancoEmpresasUI ui = (BancoEmpresasUI) parent;
                int max = 0;
                for (Empresa e : ui.listaEmpresas) {
                    if (e.id > max) max = e.id;
                }
                return max + 1;
            }
            return 1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BancoEmpresasUI ui = new BancoEmpresasUI();
            ui.setVisible(true);
        });
    }
}