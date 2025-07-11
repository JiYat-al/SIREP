package Controlardor;

import Modelo.DocenteDAO;
import Vista.InsertDocentes;
import Vista.MenuPrincipal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorVistaPrincipal {
    private MenuPrincipal vista;

    public ControladorVistaPrincipal(MenuPrincipal vista) {
        this.vista = vista;

        this.vista.getBtnCerrarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    System.exit(0);
            }

        });

        vista.getItemNuevodoc().addActionListener(e -> {
            InsertDocentes agregarDocente = new InsertDocentes();

            JFrame frame = new JFrame("Agregar Docente");
            frame.setContentPane(agregarDocente.getPanel());
            frame.setSize(600, 500);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            agregarDocente.getBtnAceptar().addActionListener(ev -> {


                int tarjeta = Integer.parseInt(agregarDocente.getTarjeta());
                String nombre = agregarDocente.getNombre();
                String apPaterno = agregarDocente.getAP();
                String apMaterno = agregarDocente.getAM();
                String correo = agregarDocente.getCorreo();




                DocenteDAO dao = new DocenteDAO();
                boolean exito = dao.nuevoDocente(tarjeta, nombre, apPaterno, apMaterno, correo);

                if (exito) {
                    agregarDocente.limpiarCampos();

                    JOptionPane.showMessageDialog(null, "Docente agregado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar docente.");
                }
            });
        });
    }


}
