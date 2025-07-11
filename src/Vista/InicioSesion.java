package Vista;

import javax.swing.*;

public class InicioSesion {
    private JPanel principal;
    private JButton Entrarbutton;
    private JLabel ContraLabel;
    private JLabel ContraseñaLabel;
    private JPasswordField fieldcontraseña;
    private JTextField fieldUsuario;
    private JLabel Bienvenido;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    public JPanel getPanel() {
        return principal;
    }
    public JButton getEntrarButton() {
        return Entrarbutton;
    }

    public JTextField getUsuarioTexto() {
        return fieldUsuario;
    }
    public JPasswordField getContraseñaTexto() {
        return fieldcontraseña;
    }
}
