package Vista;

import Controlador.ControladorProyectos;
import Modelo.Empresa;
import Modelo.EmpresaDAO;
import Modelo.Proyecto;
import Modelo.ProyectoDAO;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FormularioNuevoProyecto extends JDialog {
    private final Color colorPrincipal = new Color(92, 93, 169);
    ControladorProyectos controladorProyectos;

    public FormularioNuevoProyecto(JFrame parent, Consumer<Proyecto> onProyectoGuardado) {
        super(parent, "Nuevo Proyecto", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        controladorProyectos = new ControladorProyectos(new ProyectoDAO());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Registrar Nuevo Proyecto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorPrincipal);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titulo);

        JPanel camposPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        camposPanel.setBackground(Color.WHITE);

        JTextField txtNombre = new JTextField();
        JTextArea txtDescripcion = new JTextArea(2, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        JTextField txtDuracion = new JTextField();
        JSpinner spinnerAlumnos = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        JComboBox<Empresa> comboEmpresa = new JComboBox<>();
        JComboBox<String> comboOrigen = new JComboBox<>();
        comboOrigen.addItem("Banco de Proyectos");
        comboOrigen.addItem("Externo");
        for (Empresa empresa : EmpresaDAO.recuperarDatos()) {
            comboEmpresa.addItem(empresa);
        }

        camposPanel.add(new JLabel("Nombre:"));
        camposPanel.add(txtNombre);
        camposPanel.add(new JLabel("Descripción:"));
        camposPanel.add(scrollDesc);
        camposPanel.add(new JLabel("Duración (meses):"));
        camposPanel.add(txtDuracion);
        camposPanel.add(new JLabel("Número de alumnos:"));
        camposPanel.add(spinnerAlumnos);
        camposPanel.add(new JLabel("Empresa:"));
        camposPanel.add(comboEmpresa);
        camposPanel.add(new JLabel("Origen"));
        camposPanel.add(comboOrigen);

        mainPanel.add(camposPanel);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(colorPrincipal);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnGuardar.setFocusPainted(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancelar.setFocusPainted(false);

        btnGuardar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String duracion = txtDuracion.getText().trim();
            int numAlumnos = (int) spinnerAlumnos.getValue();
            Empresa empresa = (Empresa) comboEmpresa.getSelectedItem();
            int origen = comboOrigen.getSelectedIndex() + 1;
            System.out.println(origen);

            if (nombre.isEmpty() || descripcion.isEmpty() || duracion.isEmpty() || empresa == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Integer.parseInt(duracion);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La duración debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Proyecto proyecto = new Proyecto(nombre, descripcion, duracion, numAlumnos, empresa.getId(), origen);

            controladorProyectos.NuevoProyectoBanco(proyecto);

            proyecto = controladorProyectos.proyectoPorNombre(nombre);

            if (onProyectoGuardado != null) {
                onProyectoGuardado.accept(proyecto);
            }

            dispose();
        });

        btnCancelar.addActionListener(ev -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        mainPanel.add(panelBotones);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

}
