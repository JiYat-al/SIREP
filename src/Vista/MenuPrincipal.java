package Vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal {

    private JPanel menuprincipal;
    private JLabel labelfechahora;
    private JButton cerrarSesionButton;
    private JMenuItem registardoc;


    public JPanel getPanel() {
        return menuprincipal;
    }

    private void createUIComponents() {
        labelfechahora = new JLabel();
        labelfechahora.setFont(new Font("Arial", Font.PLAIN, 14));
        labelfechahora.setHorizontalAlignment(SwingConstants.RIGHT);

        // Timer para actualizar la hora cada segundo
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
            String texto = ahora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            labelfechahora.setText("Fecha y hora: " + texto);
        });
        timer.start();
    }

    public JButton getBtnCerrarSesion() {
        return cerrarSesionButton;
    }
    public JMenuItem getItemNuevodoc(){
        return registardoc;
    }

}
