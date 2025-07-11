package app;

import Controlardor.ControladorLogin;
import Vista.InicioSesion;

import javax.swing.*;

public class main {
    public static void main(String[] args){
        InicioSesion vista = new InicioSesion();

        JFrame frame = new JFrame("Inicio de Sesión");
        frame.setContentPane(vista.getPanel());
        frame.setSize(550, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        new ControladorLogin(vista);

    }
    }

