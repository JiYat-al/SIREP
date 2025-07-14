
package Vista;

import Controlador.ControladorDocente;
import Modelo.Docente;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

public class DocentesUI extends JFrame {
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private ArrayList<Docente> listaDocentes = new ArrayList<>();
    private ControladorDocente controlador = new ControladorDocente();


    public DocentesUI() {
        /* cargarDesdeBD();*/
        setTitle("Docentes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));

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
        gbcL.insets = new Insets(24, 0, 18, 0); // icono separado arriba
        gbcL.anchor = GridBagConstraints.NORTH;
        JLabel icono = new JLabel("\uD83D\uDC68\u200D\uD83C\uDF93", SwingConstants.CENTER); // Emoji maestro
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icono.setForeground(Color.WHITE);
        barraLateral.add(icono, gbcL);

        gbcL.gridy++;
        gbcL.insets = new Insets(0, 0, 0, 0); // sin margen extra
        JLabel verticalTitle = new JLabel("<html>D<br>O<br>C<br>E<br>N<br>T<br>E<br>S</html>");
        verticalTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        verticalTitle.setForeground(new Color(245, 243, 255, 200));
        verticalTitle.setHorizontalAlignment(SwingConstants.CENTER);
        barraLateral.add(verticalTitle, gbcL);

        // Botón de ayuda abajo
        gbcL.gridy++;
        gbcL.weighty = 1.0;
        gbcL.anchor = GridBagConstraints.SOUTH;
        gbcL.insets = new Insets(0, 0, 24, 0); // margen abajo
        JButton btnAyuda = new JButton("?");
        btnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnAyuda.setForeground(colorPrincipal);
        btnAyuda.setBackground(Color.WHITE);
        btnAyuda.setFocusPainted(false);
        btnAyuda.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAyuda.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí puedes registrar, editar y eliminar docentes.\n" +
                            "Usa el botón 'Registrar nuevo docente' para agregar uno nuevo.\n" +
                            "Haz clic en 'Editar' para modificar un docente existente.",
                    "Ayuda", JOptionPane.INFORMATION_MESSAGE);
        });
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
        JLabel lblTitulo = new JLabel("Banco de Docentes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 0));
        header.add(lblTitulo, BorderLayout.WEST);

        // Botón Registrar
        JButton btnNuevo = new JButton("Registrar nuevo docente") {
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

        // Tabla de docentes
        String[] columnas = {
                "No. Tarjeta", "Nombre", "Correo", ""
        };
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        tabla = new JTable(modelo) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
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
        tabla.getColumnModel().getColumn(3).setCellRenderer(new BotonAccionesRenderer());
        tabla.getColumnModel().getColumn(3).setCellEditor(new BotonAccionesEditor(new JCheckBox()));

        JScrollPane scrollTabla = new JScrollPane(tabla);
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

        mainPanel.add(panelContenido, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Docentes de ejemplo:

        cargarTabla();



        // ---- Evento del botón Registrar (Abre formulario) ----
        btnNuevo.addActionListener(e -> {
            RegistrarDocenteDialog dialog = new RegistrarDocenteDialog(this, colorPrincipal, null);
            dialog.setVisible(true);
            /**if (dialog.seRegistro) {
             listaDocentes.add(dialog.docenteEditado);
             cargarTabla();
             }*/
            if (dialog.seRegistro) {
                boolean exito = controlador.agregarDocente(dialog.docenteEditado);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Docente registrado exitosamente.");
                    cargarDesdeBD(); // Nuevo método que vuelve a consultar los docentes
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar el docente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        // Botón Regresar
        JButton btnRegresar = new JButton("\u2190"); // Flecha izquierda Unicode
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnRegresar.setForeground(colorPrincipal);
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> {
            dispose();
            /**  new Menu().setVisible(true);*/
        });

        // Panel superior para el botón
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTop.setOpaque(false);
        panelTop.add(btnRegresar);

        // Agrega el panelTop al principio del BorderLayout.NORTH de tu ventana principal
        mainPanel.add(panelTop, BorderLayout.NORTH);
        cargarDesdeBD();
    }
    private void cargarDesdeBD() {
        listaDocentes = new ArrayList<>(controlador.obtenerDocentes());
        cargarTabla();
    }



    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Docente d : listaDocentes) {
            String nombreCompleto =
                    (d.getNombre() != null ? d.getNombre() : "") + " " +
                            (d.getApellidoPaterno() != null ? d.getApellidoPaterno() : "") + " " +
                            (d.getApellidoMaterno() != null ? d.getApellidoMaterno() : "");
            modelo.addRow(new Object[]{
                    d.getNumeroTarjeta(),
                    nombreCompleto.trim(),
                    d.getCorreo() != null ? d.getCorreo() : "",
                    ""
            });
        }
    }

    /**momoooooooooooooooooo2*/

    // Botón Editar y Eliminar bonito en la tabla
    class BotonAccionesRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEditar;
        private final JButton btnEliminar;

        public BotonAccionesRenderer() {
            // Cambia el FlowLayout a CENTER y ajusta el margen izquierdo
            setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0));
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
            // Cambia el FlowLayout a CENTER y ajusta el margen izquierdo
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
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
                DocentesUI.this.editarDocente(selectedRow);
            });

            btnEliminar.addActionListener(e -> {

                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    int numeroTarjeta = (int) tabla.getValueAt(filaSeleccionada, 0);
                    int opcion = JOptionPane.showConfirmDialog(
                            null,
                            "¿Seguro que quieres eliminar al docente?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (opcion == JOptionPane.YES_OPTION) {
                        boolean eliminado = controlador.eliminarDocente(numeroTarjeta);
                        if (eliminado) {
                            JOptionPane.showMessageDialog(null, "Docente eliminado correctamente.");
                            cargarDesdeBD(); // Actualizar tabla después de eliminar
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al eliminar docente.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                /**listaDocentes.remove(selectedRow);*/
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

    // Método para eliminar docente de la base de datos (implementa según tu sistema)
    // private void eliminarDocenteDeBaseDeDatos(Docente docente) {
    //     // TODO: Conectar y eliminar el registro en la base de datos
    // }

    // Método para editar un docente
    private void editarDocente(int row) {
        if (row >= 0 && row < listaDocentes.size()) {
            Docente docente = listaDocentes.get(row);
            RegistrarDocenteDialog dialog = new RegistrarDocenteDialog(this, colorPrincipal, docente);
            dialog.setVisible(true);
            if (dialog.seRegistro) {
                boolean actualizado = controlador.actualizarDatos(dialog.docenteEditado);
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Docente actualizado exitosamente.");
                    cargarDesdeBD();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el docente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                /**listaDocentes.set(row, dialog.docenteEditado);
                 cargarTabla();*/
            }
        }
    }

    public void mostrarDocentesEnTabla(java.util.List<Modelo.Docente> lista) {
        modelo.setRowCount(0);
        for (Modelo.Docente d : lista) {
            modelo.addRow(new Object[]{
                    d.getNumeroTarjeta(),
                    d.getNombre() != null ? d.getNombre() : "",
                    d.getApellidoPaterno() != null ? d.getApellidoPaterno() : "",
                    d.getApellidoMaterno() != null ? d.getApellidoMaterno() : "",
                    d.getCorreo() != null ? d.getCorreo() : ""
            });
        }
    }   /**MOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO*/


    public static class RegistrarDocenteDialog extends JDialog {
        public boolean seRegistro = false;
        public Docente docenteEditado;
        public RegistrarDocenteDialog(JFrame parent, Color colorPrincipal, Docente docente) {
            super(parent, (docente == null ? "Registrar" : "Editar") + " docente", true);
            setSize(500, 380);
            setResizable(false);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(18, 10, 0, 10); // MÁS espacio arriba de cada campo
            gbc.anchor = GridBagConstraints.WEST;

            // Etiquetas alineadas a la derecha
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            panel.add(labelField("No. Tarjeta", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Nombre(s)", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Apellido paterno", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Apellido materno", colorPrincipal, SwingConstants.RIGHT), gbc);
            gbc.gridy++;
            panel.add(labelField("Correo", colorPrincipal, SwingConstants.RIGHT), gbc);

            // Campos alineados a la izquierda y más anchos
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField txtNumTarjeta = new JTextField(18);
            estilizaCampo(txtNumTarjeta, colorPrincipal);
            panel.add(txtNumTarjeta, gbc);

            gbc.gridy++;
            JTextField txtNombre = new JTextField(18);
            estilizaCampo(txtNombre, colorPrincipal);
            panel.add(txtNombre, gbc);

            gbc.gridy++;
            JTextField txtApellidoPaterno = new JTextField(18);
            estilizaCampo(txtApellidoPaterno, colorPrincipal);
            panel.add(txtApellidoPaterno, gbc);

            gbc.gridy++;
            JTextField txtApellidoMaterno = new JTextField(18);
            estilizaCampo(txtApellidoMaterno, colorPrincipal);
            panel.add(txtApellidoMaterno, gbc);

            gbc.gridy++;
            JTextField txtCorreo = new JTextField(18);
            estilizaCampo(txtCorreo, colorPrincipal);
            panel.add(txtCorreo, gbc);

            // Botón guardar centrado
            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(28, 4, 4, 4); gbc.fill = GridBagConstraints.NONE;
            JButton btnGuardar = new JButton(docente == null ? "Guardar" : "Actualizar");
            btnGuardar.setBackground(colorPrincipal);
            btnGuardar.setForeground(Color.WHITE);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 36, 10, 36));
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setOpaque(true);
            panel.add(btnGuardar, gbc);

            add(panel, BorderLayout.CENTER);

            // Si es edición, llena los campos
            /** VIEJOO
             *  if (docente != null) {
             txtNumTarjeta.setText(String.valueOf(docente.getNumeroTarjeta()));
             String[] partes = docente.getNombre().split(" ");
             txtNombre.setText(partes.length > 0 ? partes[0] : "");
             txtApellidoPaterno.setText(partes.length > 1 ? partes[1] : "");
             txtApellidoMaterno.setText(partes.length > 2 ? partes[2] : "");
             txtCorreo.setText(docente.getCorreo());
             }*/
            if (docente != null) {
                txtNumTarjeta.setText(String.valueOf(docente.getNumeroTarjeta()));
                txtNombre.setText(docente.getNombre() != null ? docente.getNombre() : "");
                txtApellidoPaterno.setText(docente.getApellidoPaterno() != null ? docente.getApellidoPaterno() : "");
                txtApellidoMaterno.setText(docente.getApellidoMaterno() != null ? docente.getApellidoMaterno() : "");
                txtCorreo.setText(docente.getCorreo() != null ? docente.getCorreo() : "");
            }


            btnGuardar.addActionListener(ev -> {
                String numTarjetaString = txtNumTarjeta.getText().trim();
                String nombre = txtNombre.getText().trim();
                String apellidoPaterno = txtApellidoPaterno.getText().trim();
                String apellidoMaterno = txtApellidoMaterno.getText().trim();
                String correo = txtCorreo.getText().trim();

                if (numTarjetaString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El número de tarjeta no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNumTarjeta.requestFocus(); return;
                }
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNombre.requestFocus(); return;
                }
                if (apellidoPaterno.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El apellido paterno no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtApellidoPaterno.requestFocus(); return;
                }
                if (apellidoMaterno.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El apellido materno no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtApellidoMaterno.requestFocus(); return;
                }
                if (correo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El correo no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtCorreo.requestFocus(); return;
                }



                try{
                    int numTarjetaInt = Integer.parseInt(numTarjetaString);
                    docenteEditado = new Docente(numTarjetaInt, nombre, apellidoPaterno, apellidoMaterno,correo);
                    seRegistro = true;
                    dispose();
                }catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(this, "El numero de tarjeta debe ser numerico.", "Validación", JOptionPane.WARNING_MESSAGE);
                    txtNumTarjeta.requestFocus(); return;
                }

            });
        }

        private static JLabel labelField(String texto, Color colorPrincipal, int align) {
            JLabel l = new JLabel(texto);
            l.setFont(new Font("Segoe UI", Font.BOLD, 15));
            l.setForeground(colorPrincipal);
            l.setHorizontalAlignment(align);
            return l;
        }

        private static void estilizaCampo(JTextField campo, Color colorPrincipal) {
            campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorPrincipal, 1, true),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DocentesUI().setVisible(true);
        });
    }
}
