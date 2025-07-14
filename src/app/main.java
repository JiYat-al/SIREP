package app;

import Controlardor.ControladorLogin;
import Vista.LoginITO;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        LoginITO vista = new LoginITO();
        new ControladorLogin(vista);
        vista.setVisible(true);
    }
}