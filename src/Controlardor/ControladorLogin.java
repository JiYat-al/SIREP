package Controlardor;
/**CONTROLADOR PARA EL LOGIN*/
import Modelo.Usuario;
import Modelo.UsuarioDAO;
import Vista.InicioSesion;
import Vista.MenuPrincipal;

import javax.swing.*;

public class ControladorLogin {
    private InicioSesion vista;
    private UsuarioDAO usuarioDAO;


    public ControladorLogin(InicioSesion vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
        initListeners();

    }

    public boolean iniciarSesion(String usuario, String contraseña) {
        Usuario u = usuarioDAO.validarUsuario(usuario, contraseña);
        return u != null;

    }
    private void initListeners() {
        vista.getEntrarButton().addActionListener(e -> {
            String usuario = vista.getUsuarioTexto().getText();
            String contraseña = new String(vista.getContraseñaTexto().getPassword()).trim();

            if (iniciarSesion(usuario, contraseña)) {
                MenuPrincipal menu = new MenuPrincipal();
                JFrame frame = new JFrame("MenuPrincipal");
                frame.setContentPane(menu.getPanel());
                frame.setSize(900, 700);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                new ControladorVistaPrincipal(menu);

                // Cerrar login
                SwingUtilities.getWindowAncestor(vista.getPanel()).dispose();
            } else {
                JOptionPane.showMessageDialog(vista.getPanel(), "Usuario o contraseña incorrectos.");
            }
        });
    }


}
