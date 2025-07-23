
package Vista;

import Controlador.ControladorProyectos;
import Modelo.ConsultasEmpresa;
import Modelo.Empresa;
import Modelo.ProyectoDAO;
import Modelo.Proyectos;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class BancoProyectosUI extends JFrame {
    private ControladorProyectos controladorProyectos = new ControladorProyectos(new ProyectoDAO());
    private DefaultTableModel modelo;
    private JTable tabla;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private ArrayList<Proyectos> listaProyectos = new ArrayList<>();
    // Campos accesibles globalmente en la clase

    private JTextField txtNombre, txtDuracion;
    private JTextArea txtDescripcion;
    private JScrollPane scrollDescripcion;
    private JSpinner spinnerAlumnos;
    private JComboBox<Empresa> comboEmpresa;
    private JComboBox<String >comboEstatus;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtPeriodo;
    private JTextField txtOrigen;
    private JTextField txtEstatusAnteproyecto;





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
        gbcL.gridx = 0;
        gbcL.gridy = 0;
        gbcL.insets = new Insets(0, 0, 14, 0);
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
                "ID", "Nombre", "Descripción", "Origen"
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

            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        cargarTablaBancoProyectos();
        // Panel superior para los botones de filtro con espaciado uniforme
        JPanel panelBotonesFiltro = new JPanel(new GridBagLayout());
        panelBotonesFiltro.setBackground(Color.WHITE);
        panelBotonesFiltro.setBorder(BorderFactory.createEmptyBorder(20, 38, 20, 38));

        // Crear botones con texto descriptivo
        JButton btnBanco = new JButton(" Banco de proyectos");
       /* JButton btnResidencia = new JButton(" Proyectos de residencia");
        JButton btnAnteproyectos = new JButton(" Anteproyectos"); */
        JButton[] botones = {btnBanco, /**btnResidencia, btnAnteproyectos*/};

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
/**
        btnResidencia.addActionListener(e -> {
            cargarTablaProyectosResidencia();
            ref.numeroActualBoton.set(2);
        });

        btnAnteproyectos.addActionListener(e -> {
            cargarTablaAnteproyectos();
            ref.numeroActualBoton.set(3);
        });*/
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
                    int tipoFormulario = ref.numeroActualBoton.get(); // 1, 2 o 3
                    mostrarCamposPorOpcion(tipoFormulario);
                }
            }
            cargarTablaBancoProyectos();
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
                        cargarTablaBancoProyectos();

                        if (ref.numeroActualBoton.get() == 1) {
                            cargarTablaBancoProyectos();
                        }
                        if (ref.numeroActualBoton.get() == 2) {
                            cargarTablaAnteproyectos();
                        }
                        if (ref.numeroActualBoton.get() == 3) {
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
                int idProyecto = Integer.parseInt(tabla.getValueAt(filaSeleccionada, 0).toString());
                if (proyecto != null) {
                    mostrarInformacionProyectoBanco(idProyecto);
/**1 banco de proyectos 2 proyectos residencia 3 anteproyectos*/
                  /**  if (ref.numeroActualBoton.get() == 1) {
                        mostrarInformacionProyectoBanco(idProyecto);
                    }
                    if (ref.numeroActualBoton.get() == 2) {
                        /*mostrarInformacionProyectoResidencia(idProyecto);*/
                    /**}
                    if (ref.numeroActualBoton.get() == 3) {
                        /*mostrarInformacionAnteproyecto(idProyecto);
                    }*/

                }
            }
        });
        JPanel centroTabla = new JPanel(new BorderLayout());
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
            dialogoNuevoProyecto.setSize(500, 500);
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
            JTextArea txtDescripcion = new JTextArea(5, 20);
            JTextField txtDuracion = new JTextField(20);
            JSpinner spinnerAlumnos = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
            JComboBox<Empresa> comboEmpresa = new JComboBox<>();

            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);
            JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
            ArrayList<Empresa> empresaArrayList = ConsultasEmpresa.recuperarDatos();
            for (Empresa empresa : empresaArrayList) {
                comboEmpresa.addItem(empresa);
            }
            // Agregar campos al panel
            panelForm.add(new JLabel("Nombre:"), gbc);
            gbc.gridy++;
            panelForm.add(txtNombre, gbc);
            gbc.gridy++;
            panelForm.add(new JLabel("Descripción:"), gbc);
            gbc.gridy++;
            gbc.fill = GridBagConstraints.BOTH;
            panelForm.add(scrollDescripcion, gbc);
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridy++;
            panelForm.add(new JLabel("Duración (meses):"), gbc);
            gbc.gridy++;
            panelForm.add(txtDuracion, gbc);
            gbc.gridy++;
            panelForm.add(new JLabel("Número de alumnos:"), gbc);
            gbc.gridy++;
            panelForm.add(spinnerAlumnos, gbc);
            gbc.gridy++;
            panelForm.add(new JLabel("Empresa:"), gbc);
            gbc.gridy++;
            panelForm.add(comboEmpresa, gbc);

            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnGuardar = new JButton("Guardar");
            JButton btnCancelar = new JButton("Cancelar");

            btnGuardar.addActionListener(ev -> {
                String nombre = txtNombre.getText().trim();
                String descripcion = txtDescripcion.getText().trim();
                String duracionStr = txtDuracion.getText().trim();
                int numAlumnos = (int) spinnerAlumnos.getValue();
                Empresa empresaSeleccionada = (Empresa) comboEmpresa.getSelectedItem();
                int id=empresaSeleccionada.getId();
// Validaciones combinadas
                if (nombre.isEmpty() || descripcion.isEmpty() || duracionStr.isEmpty() || empresaSeleccionada == null ||
                        nombre.length() < 2 || nombre.matches("\\d+") ||
                        descripcion.length() < 2 || descripcion.matches("\\d+")) {

                    JOptionPane.showMessageDialog(dialogoNuevoProyecto,
                            "Nombre y descripción deben tener al menos dos caracteres y no ser solo números.",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

// Validación de duración
                int duracion;
                try {
                    duracion = Integer.parseInt(duracionStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialogoNuevoProyecto,
                            "La duración debe ser un número válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Proyectos nuevoProyecto = new Proyectos(nombre, descripcion, duracionStr, numAlumnos, id);
                if(controladorProyectos.NuevoProyectoBanco(nuevoProyecto)){
                 JOptionPane.showMessageDialog(null,"Registrado con exito");
                    dialogoNuevoProyecto.dispose();
                    cargarTablaBancoProyectos();
                }else{
                    JOptionPane.showMessageDialog(null,"Error al registar el  proyecto");
                    dialogoNuevoProyecto.dispose();
                    cargarTablaBancoProyectos();
                }


            });

            btnCancelar.addActionListener(ev -> {
                cargarTablaBancoProyectos();
                dialogoNuevoProyecto.dispose();
            });


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
            modelo.addRow(new Object[]{p.getId_proyecto(), p.getNombre(), p.getDescripcion(), p.getNombreOrigen()});
        }
    }

    /*private void cargarTablaProyectosResidencia() {
        modelo.setRowCount(0);
        listaProyectos = new ArrayList<>(controladorProyectos.ObProyectosResidencia());
        for (Proyectos p : listaProyectos) {
            modelo.addRow(new Object[]{p.getId_proyecto(), p.getNombre(), p.getDescripcion(), p.getNombreOrigen()});
        }
    }*/

    private void cargarTablaAnteproyectos() {
        modelo.setRowCount(0);
        listaProyectos = new ArrayList<>(controladorProyectos.ObAnteproyectos());
        for (Proyectos p : listaProyectos) {
            modelo.addRow(new Object[]{p.getId_proyecto(), p.getNombre(), p.getDescripcion(), p.getNombreOrigen()});
        }
    }

    private Proyectos obtenerProyectoSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            return listaProyectos.get(filaSeleccionada);
        }
        return null;
    }



    private void mostrarInformacionProyectoBanco(int id_proyecto) {
        String[] datosProyecto = controladorProyectos.InformacionProyectoBanco(id_proyecto);

        if (datosProyecto != null) {
            StringBuilder info = new StringBuilder();
            info.append("Información del Proyecto\n\n");
            info.append("🔹 Nombre: ").append(datosProyecto[0]).append("\n");
            info.append("🔹 Descripción: ").append(datosProyecto[1]).append("\n");
            info.append("🔹 Duración: ").append(datosProyecto[2]).append("\n");
            info.append("🔹 Número de Alumnos: ").append(datosProyecto[3]).append("\n");
            info.append("🔹 Empresa: ").append(datosProyecto[4]).append("\n");
            info.append("🔹 Estatus: ").append(datosProyecto[5]).append("\n");
            info.append("🔹 Origen: ").append(datosProyecto[6]).append("\n");

            JOptionPane.showMessageDialog(null, info.toString(), "Proyecto", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró información del proyecto.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
   /* private void mostrarInformacionProyectoResidencia(int id_proyecto) {
        String[] datosProyecto = controladorProyectos.InformacionProyectoResidencia(id_proyecto);

        if (datosProyecto != null) {
            StringBuilder info = new StringBuilder();
            info.append("Información del Proyecto de Residencia\n\n");
            info.append("🔹 Nombre: ").append(datosProyecto[0]).append("\n");
            info.append("🔹 Descripción: ").append(datosProyecto[1]).append("\n");
            info.append("🔹 Duración: ").append(datosProyecto[2]).append("\n");
            info.append("🔹 Número de Alumnos: ").append(datosProyecto[3]).append("\n");
            info.append("🔹 Empresa: ").append(datosProyecto[4]).append("\n");
            info.append("🔹 Fecha Inicio: ").append(datosProyecto[5]).append("\n");
            info.append("🔹 Fecha Fin: ").append(datosProyecto[6]).append("\n");
            info.append("🔹 Período de Realización: ").append(datosProyecto[7]).append("\n");
            info.append("🔹 Estatus del Proyecto: ").append(datosProyecto[8]).append("\n");
            info.append("🔹 Origen: ").append(datosProyecto[9]).append("\n");
            info.append("🔹 Estatus del Anteproyecto: ").append(datosProyecto[10]).append("\n");

            JOptionPane.showMessageDialog(null, info.toString(), "Proyecto de Residencia", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró información del proyecto de residencia.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }*/

    /*private void mostrarInformacionAnteproyecto(int id_proyecto) {
        String[] datosProyecto = controladorProyectos.InformacionProyectoAnteproyecto(id_proyecto);

        if (datosProyecto != null) {
            StringBuilder info = new StringBuilder();
            info.append("Información del Anteproyecto\n\n");
            info.append("🔹 Nombre: ").append(datosProyecto[0]).append("\n");
            info.append("🔹 Descripción: ").append(datosProyecto[1]).append("\n");
            info.append("🔹 Duración: ").append(datosProyecto[2]).append("\n");
            info.append("🔹 Número de Alumnos: ").append(datosProyecto[3]).append("\n");
            info.append("🔹 Empresa: ").append(datosProyecto[4]).append("\n");
            info.append("🔹 Origen: ").append(datosProyecto[5]).append("\n");
            info.append("🔹 Estatus del Anteproyecto: ").append(datosProyecto[6]).append("\n");

            JOptionPane.showMessageDialog(null, info.toString(), "Anteproyecto", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró información del anteproyecto.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }*/







    public void mostrarCamposPorOpcion(int tipoFormulario) {
        Proyectos proyecto = obtenerProyectoSeleccionado();
        if (proyecto == null) return;

        JDialog dialogo = new JDialog(this, "Editar Proyecto", true);
        dialogo.setSize(600, 600);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());



        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        int fila = 0;
        gbc.gridy = fila++;


        // Llama al método según tipoFormulario
        System.out.println("op"+ tipoFormulario);
        construirFormularioBanco(panel, gbc, proyecto);
        /**if (tipoFormulario == 1) {


        }*/ /*else if (tipoFormulario==2) {
            dialogo.setSize(600, 700);
            dialogo.setLocationRelativeTo(this);
            construirFormularioProyectoResidencia(panel, gbc, proyecto);
        }*/

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");


        btnCancelar.addActionListener(ev -> dialogo.dispose());

        btnGuardar.addActionListener(ev -> {

            Proyectos proyectoValidado = obtenerDatosFormulario(tipoFormulario);
            String nombre = proyectoValidado.getNombre();
            String descripcion = proyectoValidado.getDescripcion();
            String duracionStr = proyectoValidado.getDuracion();
            if (proyectoValidado.getNombre().length() < 2 ||
                    proyectoValidado.getNombre().matches("\\d+") ||
                    proyectoValidado.getDescripcion().length() < 2 ||
                    proyectoValidado.getDescripcion().matches("\\d+")) {

                JOptionPane.showMessageDialog(dialogo,
                        "Nombre y descripción deben tener al menos dos caracteres y no ser solo números.",
                        "Error de Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Integer.parseInt(duracionStr);  // Solo se usa para validar
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo,
                        "La duración debe ser un número válido.",
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (controladorProyectos.EditarInformacionProyectoResidencia(proyectoValidado)) {
                JOptionPane.showMessageDialog(dialogo, "Proyecto actualizado.");
                dialogo.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogo, "Ocurrió un error al actualizar.");
            }


                /*if (controladorProyectos.EditarInformacionProyectoResidencia(obtenerDatosFormulario(tipoFormulario))){
                    JOptionPane.showMessageDialog(dialogo, "Proyecto actualizado.");
                    dialogo.dispose();
                }else{JOptionPane.showMessageDialog(dialogo, "Ocurrio un error al actualizar.");
                    dialogo.dispose();
                }*/



        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        dialogo.setLayout(new BorderLayout());
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }


    private void construirFormularioBanco(JPanel panel, GridBagConstraints gbc, Proyectos proyecto) {
        String[] datosProyecto = controladorProyectos.InformacionProyectoBanco(proyecto.getId_proyecto());
        if (datosProyecto == null || datosProyecto.length < 5) return;

        //  Inicialización de componentes con valores
        txtNombre = new JTextField(datosProyecto[0], 20);
        txtDescripcion = new JTextArea(datosProyecto[1], 3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        scrollDescripcion = new JScrollPane(txtDescripcion);

        txtDuracion = new JTextField(datosProyecto[2], 20);

        spinnerAlumnos = new JSpinner(new SpinnerNumberModel(
                Integer.parseInt(datosProyecto[3]), 1, 100, 1
        ));

        comboEmpresa = new JComboBox<>();
        ArrayList<Empresa> empresaArrayList = ConsultasEmpresa.recuperarDatos();
        for (Empresa empresa : empresaArrayList) {
            comboEmpresa.addItem(empresa);
        }


        comboEstatus = new JComboBox<>();
        comboEstatus.addItem("Disponible");
        comboEstatus.addItem("Cancelado");

        String estatusTexto = datosProyecto[4];
        comboEstatus.setSelectedItem(estatusTexto.trim());

        // Diseño del formulario
        gbc.gridy++;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridy++;
        panel.add(txtNombre, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollDescripcion, gbc);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy++;
        panel.add(new JLabel("Duración:"), gbc);
        gbc.gridy++;
        panel.add(txtDuracion, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Número de Alumnos:"), gbc);
        gbc.gridy++;
        panel.add(spinnerAlumnos, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Empresa:"), gbc);
        gbc.gridy++;
        panel.add(comboEmpresa, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Estatus del Proyecto:"), gbc);
        gbc.gridy++;
        panel.add(comboEstatus, gbc);
    }

    /*private void construirFormularioProyectoResidencia(JPanel panel, GridBagConstraints gbc, Proyectos proyecto) {
        String[] datosProyecto = controladorProyectos.InformacionProyectoResidencia(proyecto.getId_proyecto());
        if (datosProyecto == null || datosProyecto.length < 11) return;

        // ⚙️ Inicialización de componentes
        txtNombre = new JTextField(datosProyecto[0], 20);

        txtDescripcion = new JTextArea(datosProyecto[1], 3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        scrollDescripcion = new JScrollPane(txtDescripcion);

        txtDuracion = new JTextField(datosProyecto[2], 20);

        spinnerAlumnos = new JSpinner(new SpinnerNumberModel(
                Integer.parseInt(datosProyecto[3]), 1, 100, 1
        ));

        comboEmpresa = new JComboBox<>();
        ArrayList<Empresa> empresaArrayList = ConsultasEmpresa.recuperarDatos();
        for (Empresa empresa : empresaArrayList) {
            comboEmpresa.addItem(empresa);
        }

        comboEstatus = new JComboBox<>();
        comboEstatus.addItem("Disponible");
        comboEstatus.addItem("Cancelado");
        String estatusTexto = datosProyecto[4];
        comboEstatus.setSelectedItem(estatusTexto.trim());

        txtFechaInicio = new JTextField(datosProyecto[5], 20);
        txtFechaFin = new JTextField(datosProyecto[6], 20);
        txtPeriodo = new JTextField(datosProyecto[7], 20);
        txtOrigen = new JTextField(datosProyecto[8], 20);
        txtEstatusAnteproyecto = new JTextField(datosProyecto[9], 20);
        int fila = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtNombre, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        panel.add(scrollDescripcion, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Duración:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtDuracion, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Número de Alumnos:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(spinnerAlumnos, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Empresa:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(comboEmpresa, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Fecha Inicio:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtFechaInicio, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Fecha Fin:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtFechaFin, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Período de Realización:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtPeriodo, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Estatus del Proyecto:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(comboEstatus, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Origen:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtOrigen, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridy = fila++;
        panel.add(new JLabel("Estatus del Anteproyecto:"), gbc);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtEstatusAnteproyecto, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weightx = 1.0;
        panel.add(txtEstatusAnteproyecto, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
    }
*/







    private Proyectos obtenerDatosFormulario(int tipoFormulario) {
        Proyectos proyectoActualizado = new Proyectos();

        // Extraer datos base
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada != -1) {
            // Asegúrate que la primera columna (0) tenga el ID
            int idProyecto = Integer.parseInt(tabla.getValueAt(filaSeleccionada, 0).toString());
            System.out.println("ID del proyecto seleccionado: " + idProyecto);
            proyectoActualizado.setId_proyecto(idProyecto);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un proyecto de la tabla.");
        }


        proyectoActualizado.setNombre(txtNombre.getText().trim());
        proyectoActualizado.setDescripcion(txtDescripcion.getText().trim());
        proyectoActualizado.setDuracion(txtDuracion.getText().trim());
        proyectoActualizado.setNumero_alumnos((Integer) spinnerAlumnos.getValue());

        // Empresa seleccionada
        Empresa seleccionada = (Empresa) comboEmpresa.getSelectedItem();
        int idEmpresa = seleccionada.getId();
        System.out.println("ID del empresa seleccionado: " + idEmpresa);
        proyectoActualizado.setId_empresa(idEmpresa);
        // Estatus del proyecto
        String estatus = comboEstatus.getSelectedItem().toString();
      if(estatus.equals("Cancelado")){
          proyectoActualizado.setEstado(false);
      }else {
          proyectoActualizado.setEstado(true);
      }
        /*if (estatus.equals("Activo")) {
            proyectoActualizado.setId_estatus_proyecto(1);
        } else if (estatus.equals("Prorroga")) {
            proyectoActualizado.setId_estatus_proyecto(2);
        } else if (estatus.equals("Cancelado")) {
            proyectoActualizado.setId_estatus_proyecto(3);
        }*/



        // Puedes agregar más campos según el tipoFormulario
       /** if (tipoFormulario == 2) {
            proyectoActualizado.setFechaInicio(txtFechaInicio.getText().trim());
            proyectoActualizado.setFechaFin(txtFechaFin.getText().trim());
            proyectoActualizado.setPeriodo(txtPeriodo.getText().trim());
            proyectoActualizado.setOrigen(txtOrigen.getText().trim());
            proyectoActualizado.setEstatusAnteproyecto(txtEstatusAnteproyecto.getText().trim());
        } else if (tipoFormulario == 3) {
            proyectoActualizado.setOrigen(txtOrigen.getText().trim());
            proyectoActualizado.setEstatusAnteproyecto(txtEstatusAnteproyecto.getText().trim());
        }*/

        return proyectoActualizado;
    }











    /**private void mostrarInformacionProyecto(Proyectos proyecto) {
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
    }*/

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BancoProyectosUI().setVisible(true);
        });
    }
}
