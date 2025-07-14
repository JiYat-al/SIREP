package app;

import Controlador.ControladorLogin;
import Vista.LoginITO;

public class main {
    public static void main(String[] args) {
        LoginITO vista = new LoginITO();
        new ControladorLogin(vista);
        vista.setVisible(true);
    }
}