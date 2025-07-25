package Vista;

import Modelo.ModeloResidente;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DialogoConfirmacionRegresoCandidato extends JDialog {
    private JCheckBox chkDocumentacionIncompleta;
    private JCheckBox chkProcesosIncompletos;
    private JCheckBox chkSolicitudRechazada;
    private JCheckBox chkOtrasRazones;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JTextArea txtObservaciones;

    private boolean confirmado = false;
    private ModeloResidente residente;
    private final Color colorPrincipal = new Color(92, 93, 169);

    public DialogoConfirmacionRegresoCandidato(Frame parent, ModeloResidente residente) {
        super(parent, "Regresar Residente a Candidato", true);
        this.residente = residente;

        initComponents();
        setupLayout();
        setupEvents();

        setSize(520, 480);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        chkDocumentacionIncompleta = new JCheckBox("Documentación incompleta o incorrecta");
        chkProcesosIncompletos = new JCheckBox("Procesos administrativos incompletos");
        chkSolicitudRechazada = new JCheckBox("Solicitud rechazada por la institución");
        chkOtrasRazones = new JCheckBox("Otras razones administrativas");

        chkDocumentacionIncompleta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkProcesosIncompletos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkSolicitudRechazada.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkOtrasRazones.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtObservaciones = new JTextArea(4, 30);
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservaciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        btnConfirmar = crearBoton("Confirmar Regreso", new Color(255, 152, 0));
        btnCancelar = crearBoton("Cancelar", new Color(211, 47, 47));

        btnConfirmar.setEnabled(false);
    }

    private JButton crearBoton(String texto, Color color) {
        return new JButton(texto) {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (isEnabled()) { hover = true; repaint(); }
                    }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color baseColor = isEnabled() ? color : new Color(180, 180, 180);

                if (hover && isEnabled()) {
                    g2.setColor(baseColor.darker());
                } else {
                    g2.setColor(baseColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Regresar Residente a Candidato");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(colorPrincipal);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBackground(new Color(245, 245, 250));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);

        gbc.gridx = 0; gbc.gridy = 0;
        panelInfo.add(new JLabel("Residente:"), gbc);
        gbc.gridx = 1;
        JLabel lblNombre = new JLabel(residente.getNombre() + " " + residente.getApellidoPaterno() +
                " " + (residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : ""));
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelInfo.add(lblNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelInfo.add(new JLabel("No. Control:"), gbc);
        gbc.gridx = 1;
        JLabel lblControl = new JLabel(String.valueOf(residente.getNumeroControl()));
        lblControl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelInfo.add(lblControl, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelInfo.add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1;
        panelInfo.add(new JLabel(residente.getCarrera()), gbc);

        JPanel panelChecks = new JPanel();
        panelChecks.setLayout(new BoxLayout(panelChecks, BoxLayout.Y_AXIS));
        panelChecks.setBackground(Color.WHITE);
        panelChecks.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 152, 0), 1),
                "Motivo del Regreso (Seleccione al menos uno)",
                0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(255, 152, 0)
        ));

        panelChecks.add(Box.createVerticalStrut(10));
        panelChecks.add(chkDocumentacionIncompleta);
        panelChecks.add(Box.createVerticalStrut(8));
        panelChecks.add(chkProcesosIncompletos);
        panelChecks.add(Box.createVerticalStrut(8));
        panelChecks.add(chkSolicitudRechazada);
        panelChecks.add(Box.createVerticalStrut(8));
        panelChecks.add(chkOtrasRazones);
        panelChecks.add(Box.createVerticalStrut(10));

        JPanel panelObservaciones = new JPanel(new BorderLayout());
        panelObservaciones.setBackground(Color.WHITE);
        panelObservaciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorPrincipal, 1),
                "Observaciones (Obligatorio)",
                0, 0, new Font("Segoe UI", Font.BOLD, 14), colorPrincipal
        ));

        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        scrollObservaciones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollObservaciones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelObservaciones.add(scrollObservaciones, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.add(panelInfo, BorderLayout.NORTH);
        panelCentral.add(panelChecks, BorderLayout.CENTER);
        panelCentral.add(panelObservaciones, BorderLayout.SOUTH);

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void setupEvents() {
        ItemListener checkListener = e -> verificarRequisitos();

        chkDocumentacionIncompleta.addItemListener(checkListener);
        chkProcesosIncompletos.addItemListener(checkListener);
        chkSolicitudRechazada.addItemListener(checkListener);
        chkOtrasRazones.addItemListener(checkListener);

        txtObservaciones.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { verificarRequisitos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { verificarRequisitos(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { verificarRequisitos(); }
        });

        btnConfirmar.addActionListener(e -> confirmarRegreso());
        btnCancelar.addActionListener(e -> dispose());

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void verificarRequisitos() {
        boolean alMenosUnCheck = chkDocumentacionIncompleta.isSelected() ||
                chkProcesosIncompletos.isSelected() ||
                chkSolicitudRechazada.isSelected() ||
                chkOtrasRazones.isSelected();

        boolean observacionesCompletas = !txtObservaciones.getText().trim().isEmpty();

        btnConfirmar.setEnabled(alMenosUnCheck && observacionesCompletas);
    }

    private void confirmarRegreso() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Confirma que desea regresar este residente a candidato?\n\n" +
                        "Residente: " + residente.getNombre() + " " + residente.getApellidoPaterno() + "\n" +
                        "No. Control: " + residente.getNumeroControl() + "\n\n" +
                        "Esta acción cambiará su estatus en el sistema.",
                "Confirmar Regreso a Candidato",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            confirmado = true;
            dispose();
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getObservaciones() {
        return txtObservaciones.getText().trim();
    }

    public String getMotivosSeleccionados() {
        StringBuilder motivos = new StringBuilder();

        if (chkDocumentacionIncompleta.isSelected()) {
            motivos.append("Documentación incompleta; ");
        }
        if (chkProcesosIncompletos.isSelected()) {
            motivos.append("Procesos incompletos; ");
        }
        if (chkSolicitudRechazada.isSelected()) {
            motivos.append("Solicitud rechazada; ");
        }
        if (chkOtrasRazones.isSelected()) {
            motivos.append("Otras razones; ");
        }

        return motivos.toString();
    }
}