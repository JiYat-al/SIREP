
package Vista;

import Controlador.ControladorProyectos;
import Modelo.ProyectoDAO;
import Modelo.Proyectos;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BancoProyectosUI extends JFrame {
    private ControladorProyectos controladorProyectos = new ControladorProyectos(new ProyectoDAO());
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private ArrayList<Proyectos> listaProyectos = new ArrayList<>();

    public BancoProyectosUI() {
        setTitle("Banco de Proyectos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(colorFondo);

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

        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(colorFondo);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 250)));
        JLabel lblTitulo = new JLabel("Banco de Proyectos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 0));
        header.add(lblTitulo, BorderLayout.WEST);

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

        String[] columnas = {
                "ID","Nombre", "Descripción", "Origen"
        };
        modelo = new DefaultTableModel(null, columnas);
        tabla = new JTable(modelo);
        // Ocultar la columna ID (columna 0)
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);


        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        tabla.setRowHeight(36);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(new Color(230, 230, 250));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 17));
        tabla.getTableHeader().setBackground(new Color(220, 219, 245));
        tabla.getTableHeader().setForeground(colorPrincipal);
        ((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 38, 0));
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

        // Panel superior para los botones de filtro con espaciado uniforme
        JPanel panelBotonesFiltro = new JPanel(new GridBagLayout());
        panelBotonesFiltro.setBackground(Color.WHITE);
        panelBotonesFiltro.setBorder(BorderFactory.createEmptyBorder(20, 38, 20, 38));

        // Crear botones con texto descriptivo
        JButton btnBanco = new JButton(" Banco de proyectos");
        JButton btnResidencia = new JButton(" Proyectos de residencia");
        JButton btnAnteproyectos = new JButton(" Anteproyectos");

        JButton[] botones = {btnBanco, btnResidencia, btnAnteproyectos};

        // Preparar el panel para los botones
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.fill = GridBagConstraints.HORIZONTAL;
        gbcBtn.weightx = 1.0;
        gbcBtn.insets = new Insets(0, 10, 0, 10);
        var ref = new Object() {
            AtomicInteger numeroActualBoton = new AtomicInteger();
        };

/**1 banco de proyectos 2 proyectos residencia 3 anteproyectos*/
        int index = 0;
        for (JButton boton : botones) {
            // Configurar estilo del botón
            boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
            boton.setForeground(Color.WHITE);
            boton.setBackground(colorPrincipal);
            boton.setFocusPainted(false);
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            boton.setOpaque(true);

            // Borde con efecto de elevación
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(120, 120, 170, 60)),
                    BorderFactory.createEmptyBorder(12, 24, 12, 24)
            ));

            // Efecto hover
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    boton.setBackground(new Color(82, 83, 159));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    boton.setBackground(colorPrincipal);
                }
            });

            // Agregar al panel con GridBagLayout
            gbcBtn.gridx = index++;
            panelBotonesFiltro.add(boton, gbcBtn);
        }
        btnBanco.addActionListener(e -> {
            cargarTablaBancoProyectos();
            ref.numeroActualBoton.set(1);
        });

        btnResidencia.addActionListener(e -> {
            cargarTablaProyectosResidencia();
            ref.numeroActualBoton.set(2);
        });

        btnAnteproyectos.addActionListener(e -> {
            cargarTablaAnteproyectos();
            ref.numeroActualBoton.set(3);
        });
/**final de los botones de banco proyecto proyecto etc*/

        // Panel de botones de acción
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAcciones.setBackground(Color.WHITE);

        JButton btnEditar = new JButton("Editar");
        JButton btnDarBaja = new JButton("Dar de baja");
        JButton btnVerInfo = new JButton("Ver información");

        // Configurar estilo de botones
        JButton[] botonesAccion = {btnEditar, btnDarBaja, btnVerInfo};
        for (JButton btn : botonesAccion) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(colorPrincipal);
            btn.setForeground(Color.WHITE);
            btn.setEnabled(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(120, 120, 170, 60)),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
            panelAcciones.add(btn);
        }

        // Agregar listener de selección de tabla
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean haySeleccion = tabla.getSelectedRow() != -1;
                for (JButton btn : botonesAccion) {
                    btn.setEnabled(haySeleccion);
                }
            }
        });

// Evento del botón Editar
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                Proyectos proyecto = obtenerProyectoSeleccionado();
                if (proyecto != null) {
                    mostrarDialogoEdicion(proyecto);
                }
            }
        });

// Evento del botón Dar de Baja
        btnDarBaja.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                Proyectos proyecto = obtenerProyectoSeleccionado();
                int idProyecto = Integer.parseInt(tabla.getValueAt(filaSeleccionada, 0).toString());
                if (proyecto != null) {
                    int confirmacion = JOptionPane.showConfirmDialog(this,
                            "¿Está seguro que desea dar de baja el proyecto '" + proyecto.getNombre() + "'?",
                            "Confirmar Baja",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        controladorProyectos.Baja(idProyecto);
                        JOptionPane.showMessageDialog(this,
                                "El proyecto ha sido dado de baja correctamente.",
                                "Operación Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);
                       if(ref.numeroActualBoton.get()==1){
                           cargarTablaBancoProyectos();
                       }
                        if(ref.numeroActualBoton.get()==2){
                            cargarTablaAnteproyectos();
                        }
                        if(ref.numeroActualBoton.get()==3){
                            cargarTablaAnteproyectos();
                        }


                    }
                }
            }
        });

// Evento del botón Ver Información
        btnVerInfo.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                Proyectos proyecto = obtenerProyectoSeleccionado();
                if (proyecto != null) {
                    mostrarInformacionProyecto(proyecto);
                }
            }
        });        JPanel centroTabla = new JPanel(new BorderLayout());
        centroTabla.setBackground(colorFondo);
        centroTabla.setBorder(BorderFactory.createEmptyBorder(32, 38, 0, 38));
        centroTabla.add(panelBotonesFiltro, BorderLayout.NORTH);
        centroTabla.add(scrollTabla, BorderLayout.CENTER);
        centroTabla.add(panelAcciones, BorderLayout.SOUTH);
        panelContenido.add(centroTabla, BorderLayout.CENTER);

        mainPanel.add(panelContenido, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Acciones de los botones
        btnNuevo.addActionListener(e -> {
            JDialog dialogoNuevoProyecto = new JDialog(this, "Nuevo Proyecto", true);
            dialogoNuevoProyecto.setSize(600, 500);
            dialogoNuevoProyecto.setLocationRelativeTo(this);

            JPanel panelForm = new JPanel(new GridBagLayout());
            panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);

            // Campos del formulario
            JTextField txtNombre = new JTextField(20);
            JTextField txtDuracion = new JTextField(20);
            JTextArea txtDescripcion = new JTextArea(5, 20);
            JComboBox<String> comboEmpresa = new JComboBox<>();

            // Configurar área de descripción
            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);
            JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

            // Agregar campos al panel
            panelForm.add(new JLabel("Nombre:"), gbc);
            gbc.gridy++;
            panelForm.add(txtNombre, gbc);
            gbc.gridy++;
            panelForm.add(new JLabel("Duración (meses):"), gbc);
            gbc.gridy++;
            panelForm.add(txtDuracion, gbc);
            gbc.gridy++;
            panelForm.add(new JLabel("Empresa:"), gbc);
            gbc.gridy++;
            panelForm.add(comboEmpresa, gbc);
            gbc.gridy++;
            panelForm.add(new JLabel("Descripción:"), gbc);
            gbc.gridy++;
            gbc.fill = GridBagConstraints.BOTH;
            panelForm.add(scrollDescripcion, gbc);

            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnGuardar = new JButton("Guardar");
            JButton btnCancelar = new JButton("Cancelar");

            btnGuardar.addActionListener(ev -> {
                // Aquí iría la lógica para guardar el proyecto
                dialogoNuevoProyecto.dispose();
            });

            btnCancelar.addActionListener(ev -> dialogoNuevoProyecto.dispose());

            panelBotones.add(btnGuardar);
            panelBotones.add(btnCancelar);

            dialogoNuevoProyecto.setLayout(new BorderLayout());
            dialogoNuevoProyecto.add(panelForm, BorderLayout.CENTER);
            dialogoNuevoProyecto.add(panelBotones, BorderLayout.SOUTH);

            dialogoNuevoProyecto.setVisible(true);
        });
    }

    private void cargarTablaBancoProyectos() {
        modelo.setRowCount(0);
        listaProyectos = new ArrayList<>(controladorProyectos.ObProyectosBanco());
        for (Proyectos p : listaProyectos) {
            modelo.addRow(new Object[]{p.getId_proyecto(),p.getNombre(), p.getDescripcion(),  p.getNombreOrigen()});
        }
    }

    private void cargarTablaProyectosResidencia() {
        modelo.setRowCount(0);
        listaProyectos = new ArrayList<>(controladorProyectos.ObProyectosResidencia());
        for (Proyectos p : listaProyectos) {
            modelo.addRow(new Object[]{p.getId_proyecto(),p.getNombre(), p.getDescripcion(), p.getNombreOrigen()});
        }
    }

    private void cargarTablaAnteproyectos() {
        modelo.setRowCount(0);
        listaProyectos = new ArrayList<>(controladorProyectos.ObAnteproyectos());
        for (Proyectos p : listaProyectos) {
            modelo.addRow(new Object[]{p.getId_proyecto(),p.getNombre(), p.getDescripcion(), p.getNombreOrigen()});
        }
    }

    private Proyectos obtenerProyectoSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            return listaProyectos.get(filaSeleccionada);
        }
        return null;
    }

    private void mostrarDialogoEdicion(Proyectos proyecto) {
        JDialog dialogo = new JDialog(this, "Editar Proyecto", true);
        dialogo.setSize(600, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos del formulario
        JTextField txtNombre = new JTextField(proyecto.getNombre(), 20);
        JTextField txtDuracion = new JTextField(proyecto.getDuracion(), 20);
        JTextArea txtDescripcion = new JTextArea(proyecto.getDescripcion(), 5, 20);

        // Configurar área de descripción
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        // Agregar campos al panel
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridy++;
        panelForm.add(txtNombre, gbc);
        gbc.gridy++;
        panelForm.add(new JLabel("Duración (meses):"), gbc);
        gbc.gridy++;
        panelForm.add(txtDuracion, gbc);
        gbc.gridy++;
        panelForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        panelForm.add(scrollDescripcion, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(ev -> {
            proyecto.setNombre(txtNombre.getText().trim());
            proyecto.setDuracion(txtDuracion.getText().trim());
            proyecto.setDescripcion(txtDescripcion.getText().trim());

            // Aquí iría la lógica para actualizar el proyecto en la base de datos

            JOptionPane.showMessageDialog(dialogo,
                    "Los cambios han sido guardados correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            cargarTablaBancoProyectos();
            dialogo.dispose();
        });

        btnCancelar.addActionListener(ev -> dialogo.dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.setLayout(new BorderLayout());
        dialogo.add(panelForm, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);

        dialogo.setVisible(true);
    }

    private void mostrarInformacionProyecto(Proyectos proyecto) {
        StringBuilder info = new StringBuilder();
        info.append("Información del Proyecto\n\n");
        info.append("Nombre: ").append(proyecto.getNombre()).append("\n");
        info.append("Descripción: ").append(proyecto.getDescripcion()).append("\n");
        info.append("Duración: ").append(proyecto.getDuracion()).append(" meses\n");
        info.append("ID Empresa: ").append(proyecto.getId_empresa()).append("\n");
        if (proyecto.getFecha_inicio() != null) {
            info.append("Fecha Inicio: ").append(proyecto.getFecha_inicio()).append("\n");
        }
        if (proyecto.getFecha_fin() != null) {
            info.append("Fecha Fin: ").append(proyecto.getFecha_fin()).append("\n");
        }
        info.append("Número de Alumnos: ").append(proyecto.getNumero_alumnos()).append("\n");
        info.append("Periodo de Realización: ").append(proyecto.getPeriodo_realizacion()).append("\n");

        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Información del Proyecto",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BancoProyectosUI().setVisible(true);
        });
    }
}
