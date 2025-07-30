package Vista;

import Modelo.ModeloResidente;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DialogoConfirmacionSimple extends JDialog {
    private JButton btnConfirmar, btnCancelar;
    private boolean confirmado = false;
    private ModeloResidente residente;

    public DialogoConfirmacionSimple(Frame parent, ModeloResidente residente) {
        super(parent, "Confirmar Transición a Residente", true);
        this.residente = residente;
        initUI();
        setSize(500, 250);
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

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnCancelar = new JButton("Cancelar");
        btnConfirmar = new JButton("Confirmar Transición");

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnConfirmar);

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(infoPanel, BorderLayout.NORTH);

        mainPanel.add(titulo, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);


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

    public boolean isConfirmado() {
        return confirmado;
    }
}