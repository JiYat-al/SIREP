package Vista;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistroAnteproyectoUI extends JFrame {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);

    public RegistroAnteproyectoUI() {
        setTitle("Registro de Anteproyecto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // ¡Pantalla completa!
        setMinimumSize(new Dimension(1100, 700));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(colorFondo);

        // Banner tipo encabezado grande (ancho completo)
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(colorPrincipal);
        banner.setPreferredSize(new Dimension(0, 115));
        JLabel lblTitulo = new JLabel("Registro de Anteproyecto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(32, 48, 22, 0));
        banner.add(lblTitulo, BorderLayout.WEST);
        mainPanel.add(banner, BorderLayout.NORTH);

        // Panel central, blanco y AMPLIO
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(38, 120, 38, 120));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(16, 4, 4, 18);

        // Campo Título (TextArea con borde morado)
        gbc.gridy = 0;
        panelCentral.add(labelField("Título del anteproyecto", colorPrincipal), gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextArea txtTitulo = new JTextArea(2, 34);
        txtTitulo.setLineWrap(true); txtTitulo.setWrapStyleWord(true);
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane scrollTitulo = new JScrollPane(txtTitulo);
        scrollTitulo.setPreferredSize(new Dimension(530, 38));
        scrollTitulo.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(scrollTitulo, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        // Campo Descripción
        gbc.gridy++;
        panelCentral.add(labelField("Descripción", colorPrincipal), gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextArea txtDescripcion = new JTextArea(4, 34);
        txtDescripcion.setLineWrap(true); txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(530, 66));
        scrollDesc.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        panelCentral.add(scrollDesc, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        // Campo Alumno responsable
        gbc.gridy++;
        panelCentral.add(labelField("Alumno responsable", colorPrincipal), gbc);
        gbc.gridy++;
        JComboBox<String> cbAlumno = new JComboBox<>(new String[]{"Alumno A", "Alumno B", "Alumno C"});
        personalizaCombo(cbAlumno, colorPrincipal);
        cbAlumno.setPreferredSize(new Dimension(350, 32));
        panelCentral.add(cbAlumno, gbc);

        // Campo Asesor
        gbc.gridy++;
        panelCentral.add(labelField("Asesor", colorPrincipal), gbc);
        gbc.gridy++;
        JComboBox<String> cbAsesor = new JComboBox<>(new String[]{"Docente X", "Docente Y", "Docente Z"});
        personalizaCombo(cbAsesor, colorPrincipal);
        cbAsesor.setPreferredSize(new Dimension(350, 32));
        panelCentral.add(cbAsesor, gbc);

        // Campo Estado
        gbc.gridy++;
        panelCentral.add(labelField("Estado", colorPrincipal), gbc);
        gbc.gridy++;
        JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Enviado", "En revisión", "Aprobado", "Rechazado"});
        personalizaCombo(cbEstado, colorPrincipal);
        cbEstado.setPreferredSize(new Dimension(350, 32));
        panelCentral.add(cbEstado, gbc);

        // Fecha de registro (solo muestra)
        gbc.gridy++;
        panelCentral.add(labelField("Fecha de registro", colorPrincipal), gbc);
        gbc.gridy++;
        JLabel lblFecha = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblFecha.setForeground(colorPrincipal.darker());
        lblFecha.setPreferredSize(new Dimension(180, 32));
        panelCentral.add(lblFecha, gbc);

        // Campo archivo PDF
        gbc.gridy++;
        panelCentral.add(labelField("Archivo PDF", colorPrincipal), gbc);
        gbc.gridy++;
        JButton btnArchivo = new JButton("Seleccionar archivo PDF...");
        btnArchivo.setBackground(colorPrincipal);
        btnArchivo.setForeground(Color.WHITE);
        btnArchivo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnArchivo.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));
        btnArchivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnArchivo.setFocusPainted(false);
        btnArchivo.setOpaque(true);
        JLabel lblArchivo = new JLabel("Ningún archivo seleccionado");
        lblArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblArchivo.setForeground(new Color(120, 120, 130));
        JPanel archivoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        archivoPanel.setBackground(Color.WHITE);
        archivoPanel.add(btnArchivo); archivoPanel.add(Box.createRigidArea(new Dimension(10,0))); archivoPanel.add(lblArchivo);
        archivoPanel.setPreferredSize(new Dimension(500, 36));
        panelCentral.add(archivoPanel, gbc);

        btnArchivo.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF (*.pdf)", "pdf"));
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                lblArchivo.setText(chooser.getSelectedFile().getName());
            }
        });

        // Botón guardar
        gbc.gridy++;
        gbc.insets = new Insets(32, 4, 4, 4); gbc.anchor = GridBagConstraints.CENTER; gbc.gridwidth = 2;
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(colorPrincipal);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(14, 44, 14, 44));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(true);
        panelCentral.add(btnGuardar, gbc);

        mainPanel.add(panelCentral, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Validación al guardar (puedes agregar más)
        btnGuardar.addActionListener(e -> {
            if (txtTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el título del anteproyecto.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtTitulo.requestFocus(); return;
            }
            if (txtDescripcion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Agrega una descripción del anteproyecto.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtDescripcion.requestFocus(); return;
            }
            JOptionPane.showMessageDialog(this, "Anteproyecto guardado (demo).");
            dispose();
        });
    }

    private static JLabel labelField(String texto, Color colorPrincipal) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(colorPrincipal);
        return l;
    }

    private static void personalizaCombo(JComboBox<String> combo, Color colorPrincipal) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        combo.setBackground(new Color(248,248,255));
        combo.setForeground(colorPrincipal.darker());
        combo.setUI(new BasicComboBoxUI());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroAnteproyectoUI().setVisible(true);
        });
    }
}

