package Vista;

import javax.swing.*;

public class InsertDocentes {
    private JPanel panel1;
    private JTextField textFieldtarjeta;
    private JTextField textFieldnombre;
    private JTextField textFieldAP;
    private JTextField textFieldAM;
    private JTextField textFieldCorreo;
    private JButton aceptarButton;

    public JPanel getPanel() {
        return panel1;
    }

    public JButton getBtnAceptar() {
        return aceptarButton;
    }
    public String getTarjeta() {
        return textFieldtarjeta.getText();
    }
    public String getNombre() {
        return textFieldnombre.getText();
    }
    public String getAP() {
        return textFieldAP.getText();
    }
    public String getAM() {
        return textFieldAM.getText();
    }
    public String getCorreo() {
        return textFieldCorreo.getText();
    }
    public void limpiarCampos() {
        textFieldtarjeta.setText("");
        textFieldnombre.setText("");
        textFieldAP.setText("");
        textFieldAM.setText("");
        textFieldCorreo.setText("");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
