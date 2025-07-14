package Controlador;

import Modelo.ModeloResidente;
import Vista.VistaRegistros;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class ControladorRegistros {
    private VistaRegistros vista;

    public ControladorRegistros(VistaRegistros vista) {
        this.vista = vista;
    }

    // ==================== MÉTODOS PRINCIPALES ====================

    /**
     * Cargar todos los registros desde la base de datos
     */
    public void cargarTodosLosRegistros() {
        try {
            // Cambiar cursor a espera
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Obtener residentes desde el modelo
            List<ModeloResidente> residentes = ModeloResidente.obtenerTodos();

            if (residentes != null) {
                // Cargar en la vista
                vista.cargarResidentes(residentes);

                if (residentes.isEmpty()) {
                    vista.mostrarMensaje(
                            "⚠️ No se encontraron registros en la base de datos",
                            "Sin registros",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } else {
                vista.mostrarMensaje(
                        "❌ Error al cargar los registros desde la base de datos",
                        "Error de carga",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error inesperado al cargar registros:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.err.println("Error al cargar registros: " + e.getMessage());
        } finally {
            // Restaurar cursor normal
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Eliminar el registro seleccionado
     */
    public void eliminarRegistroSeleccionado() {
        ModeloResidente residenteSeleccionado = vista.getResidenteSeleccionado();

        if (residenteSeleccionado == null) {
            vista.mostrarMensaje(
                    "⚠️ Por favor seleccione un registro para eliminar",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Confirmar eliminación
        String mensaje = String.format(
                "¿Está seguro de eliminar el siguiente registro?\n\n" +
                        "📝 Número de Control: %d\n" +
                        "👤 Nombre: %s %s %s\n" +
                        "🎓 Carrera: %s\n\n" +
                        "⚠️ Esta acción no se puede deshacer",
                residenteSeleccionado.getNumeroControl(),
                residenteSeleccionado.getNombre(),
                residenteSeleccionado.getApellidoPaterno(),
                residenteSeleccionado.getApellidoMaterno() != null ? residenteSeleccionado.getApellidoMaterno() : "",
                residenteSeleccionado.getCarrera()
        );

        boolean confirmado = vista.mostrarConfirmacion(mensaje, "Confirmar eliminación");

        if (confirmado) {
            try {
                // Cambiar cursor a espera
                vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Eliminar usando el modelo
                boolean eliminado = residenteSeleccionado.eliminar();

                if (eliminado) {
                    vista.mostrarMensaje(
                            "✅ Registro eliminado exitosamente\n" +
                                    "📝 Número de Control: " + residenteSeleccionado.getNumeroControl(),
                            "Eliminación exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Actualizar la tabla
                    vista.actualizarTabla();
                } else {
                    vista.mostrarMensaje(
                            "❌ No se pudo eliminar el registro\n" +
                                    "Puede que ya haya sido eliminado por otro usuario",
                            "Error de eliminación",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (Exception e) {
                vista.mostrarMensaje(
                        "❌ Error al eliminar el registro:\n" + e.getMessage(),
                        "Error de eliminación",
                        JOptionPane.ERROR_MESSAGE
                );
                System.err.println("Error al eliminar registro: " + e.getMessage());
            } finally {
                // Restaurar cursor normal
                vista.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    /**
     * Editar el registro seleccionado
     */
    public void editarRegistroSeleccionado() {
        ModeloResidente residenteSeleccionado = vista.getResidenteSeleccionado();

        if (residenteSeleccionado == null) {
            vista.mostrarMensaje(
                    "⚠️ Por favor seleccione un registro para editar",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // Crear diálogo de edición
            DialogoEdicion dialogo = new DialogoEdicion(
                    (Frame) SwingUtilities.getWindowAncestor(vista.getPanelPrincipal()),
                    residenteSeleccionado
            );

            dialogo.setVisible(true);

            // Si se confirmó la edición, actualizar tabla
            if (dialogo.isGuardado()) {
                vista.actualizarTabla();
            }

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error al abrir el editor:\n" + e.getMessage(),
                    "Error de edición",
                    JOptionPane.ERROR_MESSAGE
            );
            System.err.println("Error al editar registro: " + e.getMessage());
        }
    }

    /**
     * Buscar registros por criterio
     */
    public void buscarRegistros(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            cargarTodosLosRegistros();
            return;
        }

        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Intentar buscar por número de control primero
            try {
                int numeroControl = Integer.parseInt(criterio.trim());
                ModeloResidente residente = ModeloResidente.buscarPorNumeroControl(numeroControl);

                if (residente != null) {
                    // Mostrar solo este registro
                    java.util.List<ModeloResidente> resultado = java.util.Arrays.asList(residente);
                    vista.cargarResidentes(resultado);
                } else {
                    vista.cargarResidentes(new java.util.ArrayList<>());
                    vista.mostrarMensaje(
                            "⚠️ No se encontró ningún residente con el número de control: " + numeroControl,
                            "Sin resultados",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                // Si no es un número, buscar por nombre
                buscarPorNombre(criterio);
            }

        } catch (Exception e) {
            vista.mostrarMensaje(
                    "❌ Error durante la búsqueda:\n" + e.getMessage(),
                    "Error de búsqueda",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Buscar por nombre (método auxiliar)
     */
    private void buscarPorNombre(String nombre) {
        // Obtener todos los registros y filtrar por nombre
        List<ModeloResidente> todosLosResidentes = ModeloResidente.obtenerTodos();
        java.util.List<ModeloResidente> resultados = new java.util.ArrayList<>();

        String nombreBusqueda = nombre.toLowerCase().trim();

        for (ModeloResidente residente : todosLosResidentes) {
            String nombreCompleto = (residente.getNombre() + " " +
                    residente.getApellidoPaterno() + " " +
                    (residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : ""))
                    .toLowerCase();

            if (nombreCompleto.contains(nombreBusqueda) ||
                    residente.getNombre().toLowerCase().contains(nombreBusqueda) ||
                    residente.getApellidoPaterno().toLowerCase().contains(nombreBusqueda) ||
                    (residente.getApellidoMaterno() != null &&
                            residente.getApellidoMaterno().toLowerCase().contains(nombreBusqueda))) {
                resultados.add(residente);
            }
        }

        vista.cargarResidentes(resultados);

        if (resultados.isEmpty()) {
            vista.mostrarMensaje(
                    "⚠️ No se encontraron registros que coincidan con: " + nombre,
                    "Sin resultados",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Verificar si existe un residente
     */
    public boolean existeResidente(int numeroControl) {
        return ModeloResidente.existe(numeroControl);
    }

    /**
     * Obtener residente por número de control
     */
    public ModeloResidente obtenerResidentePorNumeroControl(int numeroControl) {
        return ModeloResidente.buscarPorNumeroControl(numeroControl);
    }

    /**
     * Verificar integridad de datos antes de operaciones
     */

    }

// ==================== CLASE DIÁLOGO DE EDICIÓN ====================

class DialogoEdicion extends JDialog {
    private JTextField txtNumeroControl;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCarrera;
    private JSpinner spnSemestre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JSpinner spnIdProyecto;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean guardado = false;
    private ModeloResidente residente;

    public DialogoEdicion(Frame parent, ModeloResidente residente) {
        super(parent, "✏️ Editar Residente", true);
        this.residente = residente;

        initComponents();
        cargarDatos();
        setupLayout();
        setupEvents();

        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        txtNumeroControl = new JTextField(15);
        txtNumeroControl.setEditable(false); // No editable
        txtNumeroControl.setBackground(Color.LIGHT_GRAY);

        txtNombre = new JTextField(15);
        txtApellidoPaterno = new JTextField(15);
        txtApellidoMaterno = new JTextField(15);
        txtCarrera = new JTextField(15);
        spnSemestre = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        txtCorreo = new JTextField(15);
        txtTelefono = new JTextField(15);
        spnIdProyecto = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));

        btnGuardar = new JButton("💾 Guardar Cambios");
        btnCancelar = new JButton("❌ Cancelar");

        // Configurar colores
        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(211, 47, 47));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
    }

    private void cargarDatos() {
        txtNumeroControl.setText(String.valueOf(residente.getNumeroControl()));
        txtNombre.setText(residente.getNombre());
        txtApellidoPaterno.setText(residente.getApellidoPaterno());
        txtApellidoMaterno.setText(residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : "");
        txtCarrera.setText(residente.getCarrera());
        spnSemestre.setValue(residente.getSemestre());
        txtCorreo.setText(residente.getCorreo());
        txtTelefono.setText(residente.getTelefono() != null ? residente.getTelefono() : "");
        spnIdProyecto.setValue(residente.getIdProyecto());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel principal con campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Título
        JLabel lblTitulo = new JLabel("✏️ Editar Información del Residente");
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Agregar campos
        agregarCampo(panelCampos, "Número de Control:", txtNumeroControl, gbc, 0);
        agregarCampo(panelCampos, "* Nombre:", txtNombre, gbc, 1);
        agregarCampo(panelCampos, "* Apellido Paterno:", txtApellidoPaterno, gbc, 2);
        agregarCampo(panelCampos, "Apellido Materno:", txtApellidoMaterno, gbc, 3);
        agregarCampo(panelCampos, "* Carrera:", txtCarrera, gbc, 4);
        agregarCampo(panelCampos, "* Semestre:", spnSemestre, gbc, 5);
        agregarCampo(panelCampos, "* Correo:", txtCorreo, gbc, 6);
        agregarCampo(panelCampos, "Teléfono:", txtTelefono, gbc, 7);
        agregarCampo(panelCampos, "ID Proyecto:", spnIdProyecto, gbc, 8);

        // Nota sobre campos obligatorios
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Panel superior con título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        // Panel inferior con nota y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(lblNota, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar todo al diálogo
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Borde general
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo, GridBagConstraints gbc, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JLabel label = new JLabel(etiqueta);
        if (etiqueta.startsWith("*")) {
            label.setForeground(new Color(211, 47, 47)); // Rojo para obligatorios
        }
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void setupEvents() {
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> cancelar());

        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);

        // Escape para cancelar
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    private void guardarCambios() {
        // Validar campos obligatorios
        if (!validarCampos()) {
            return;
        }

        // Confirmar guardado
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de guardar los cambios?\n" +
                        "Nombre: " + txtNombre.getText().trim() + " " + txtApellidoPaterno.getText().trim(),
                "Confirmar cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                // Cambiar cursor a espera
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Actualizar datos del residente
                residente.setNombre(txtNombre.getText().trim());
                residente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
                residente.setApellidoMaterno(txtApellidoMaterno.getText().trim().isEmpty() ? null : txtApellidoMaterno.getText().trim());
                residente.setCarrera(txtCarrera.getText().trim());
                residente.setSemestre((Integer) spnSemestre.getValue());
                residente.setCorreo(txtCorreo.getText().trim());
                residente.setTelefono(txtTelefono.getText().trim().isEmpty() ? null : txtTelefono.getText().trim());
                residente.setIdProyecto((Integer) spnIdProyecto.getValue());

                // Guardar en la base de datos
                boolean actualizado = residente.actualizar();

                if (actualizado) {
                    guardado = true;
                    JOptionPane.showMessageDialog(this,
                            "✅ Cambios guardados exitosamente\n" +
                                    "📝 Número de Control: " + residente.getNumeroControl(),
                            "Actualización exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "❌ No se pudieron guardar los cambios\n" +
                                    "Verifique que el registro aún existe en la base de datos",
                            "Error de actualización",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "❌ Error al guardar los cambios:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("Error al actualizar residente: " + e.getMessage());
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    private boolean validarCampos() {
        // Validar nombre
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio", txtNombre);
            return false;
        }

        if (txtNombre.getText().trim().length() < 2) {
            mostrarError("El nombre debe tener al menos 2 caracteres", txtNombre);
            return false;
        }

        // Validar apellido paterno
        if (txtApellidoPaterno.getText().trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio", txtApellidoPaterno);
            return false;
        }

        if (txtApellidoPaterno.getText().trim().length() < 2) {
            mostrarError("El apellido paterno debe tener al menos 2 caracteres", txtApellidoPaterno);
            return false;
        }

        // Validar carrera
        if (txtCarrera.getText().trim().isEmpty()) {
            mostrarError("La carrera es obligatoria", txtCarrera);
            return false;
        }

        // Validar correo
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            mostrarError("El correo es obligatorio", txtCorreo);
            return false;
        }

        if (!validarFormatoCorreo(correo)) {
            mostrarError("El formato del correo no es válido\nEjemplo: usuario@dominio.com", txtCorreo);
            return false;
        }

        // Validar teléfono (opcional, pero si se proporciona debe ser válido)
        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !validarFormatoTelefono(telefono)) {
            mostrarError("El formato del teléfono no es válido\nDebe contener solo números y tener entre 8 y 15 dígitos", txtTelefono);
            return false;
        }

        return true;
    }

    private boolean validarFormatoCorreo(String correo) {
        return correo.contains("@") &&
                correo.contains(".") &&
                correo.indexOf("@") > 0 &&
                correo.indexOf("@") < correo.lastIndexOf(".") &&
                correo.lastIndexOf(".") < correo.length() - 1;
    }

    private boolean validarFormatoTelefono(String telefono) {
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
        return telefonoLimpio.length() >= 8 && telefonoLimpio.length() <= 15;
    }

    private void mostrarError(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
        campo.requestFocus();
        campo.selectAll();
    }

    private void cancelar() {
        if (hayCambios()) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de cancelar?\nSe perderán todos los cambios realizados.",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                guardado = false;
                dispose();
            }
        } else {
            guardado = false;
            dispose();
        }
    }

    private boolean hayCambios() {
        return !txtNombre.getText().equals(residente.getNombre()) ||
                !txtApellidoPaterno.getText().equals(residente.getApellidoPaterno()) ||
                !txtApellidoMaterno.getText().equals(residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : "") ||
                !txtCarrera.getText().equals(residente.getCarrera()) ||
                !spnSemestre.getValue().equals(residente.getSemestre()) ||
                !txtCorreo.getText().equals(residente.getCorreo()) ||
                !txtTelefono.getText().equals(residente.getTelefono() != null ? residente.getTelefono() : "") ||
                !spnIdProyecto.getValue().equals(residente.getIdProyecto());
    }

    public boolean isGuardado() {
        return guardado;
    }
}