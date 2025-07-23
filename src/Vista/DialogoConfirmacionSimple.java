package Vista;

import Modelo.ModeloResidente;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DialogoConfirmacionSimple extends JDialog {
    private JCheckBox chk1, chk2, chk3, chk4;
    private JButton btnConfirmar, btnCancelar;
    private boolean confirmado = false;
    private ModeloResidente residente;

    public DialogoConfirmacionSimple(Frame parent, ModeloResidente residente) {
        super(parent, "Confirmar Transición a Residente", true);
        this.residente = residente;
        initUI();
        setSize(500, 350);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Confirmar Transición de Candidato a Residente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Info del candidato
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Candidato"));
        infoPanel.add(new JLabel("Nombre:"));
        infoPanel.add(new JLabel(residente.getNombre() + " " + residente.getApellidoPaterno()));
        infoPanel.add(new JLabel("No. Control:"));
        infoPanel.add(new JLabel(String.valueOf(residente.getNumeroControl())));
        infoPanel.add(new JLabel("Correo:"));
        infoPanel.add(new JLabel(residente.getCorreo()));

        // Checkboxes
        JPanel checkPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        checkPanel.setBorder(BorderFactory.createTitledBorder("Verificar Requisitos"));

        chk2 = new JCheckBox("Solicitud firmada por el alumno");
        chk3 = new JCheckBox("Solicitud firmada y sellada por la institución");
        chk4 = new JCheckBox("Todos los procesos administrativos completados");

        checkPanel.add(chk1);
        checkPanel.add(chk2);
        checkPanel.add(chk3);
        checkPanel.add(chk4);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnCancelar = new JButton("Cancelar");
        btnConfirmar = new JButton("Confirmar Transición");
        btnConfirmar.setEnabled(false);

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnConfirmar);

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(checkPanel, BorderLayout.CENTER);

        mainPanel.add(titulo, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Eventos
        ActionListener checkListener = e -> updateButton();
        chk1.addActionListener(checkListener);
        chk2.addActionListener(checkListener);
        chk3.addActionListener(checkListener);
        chk4.addActionListener(checkListener);

        btnConfirmar.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "¿Confirma que desea convertir este candidato en residente?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                confirmado = true;
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void updateButton() {
        boolean allSelected = chk1.isSelected() && chk2.isSelected() &&
                chk3.isSelected() && chk4.isSelected();
        btnConfirmar.setEnabled(allSelected);

        if (allSelected) {
            btnConfirmar.setBackground(Color.GREEN);
            btnConfirmar.setText("✓ Confirmar Transición");
        } else {
            btnConfirmar.setBackground(null);
            btnConfirmar.setText("Confirmar Transición");
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}