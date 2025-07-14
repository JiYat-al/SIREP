package Controlardor;

import Modelo.Usuario;
import Modelo.UsuarioDAO;
import Vista.LoginITO;
import Vista.Menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorLogin {

    private LoginITO vista;
    private UsuarioDAO usuarioDAO;

    private int intentosFallidos = 0;
    private final int MAX_INTENTOS = 3;

    // Tiempo de bloqueo en milisegundos (ejemplo: 30 segundos)
    private final int TIEMPO_BLOQUEO = 30_000;

    private Timer timerDesbloqueo;

    public ControladorLogin(LoginITO vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();

        // Asigna el listener al botón de login
        this.vista.getBtnLogin().addActionListener(e -> intentarLogin());

        // Inicializa el timer de desbloqueo (pero no arranca)
        timerDesbloqueo = new Timer(TIEMPO_BLOQUEO, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desbloquearLogin();
            }
        });
        timerDesbloqueo.setRepeats(false); // solo se ejecuta una vez
    }

    public boolean iniciarSesion(String usuario, String contrasena) {
        Usuario u = usuarioDAO.validarUsuario(usuario, contrasena);
        return u != null;
    }

    private void intentarLogin() {
        String usuario = vista.getTxtUsuario().getText().trim();
        String contrasena = new String(vista.getTxtPassword().getPassword()).trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa ambos campos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (intentosFallidos >= MAX_INTENTOS) {
            JOptionPane.showMessageDialog(vista,
                    "Has alcanzado el máximo de intentos. El acceso está bloqueado.\n" +
                            "Intenta de nuevo en 30 segundos.",
                    "Acceso bloqueado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (iniciarSesion(usuario, contrasena)) {
            JOptionPane.showMessageDialog(vista, "¡Bienvenido, " + usuario + "!", "Acceso", JOptionPane.INFORMATION_MESSAGE);
            intentosFallidos = 0; // resetear contador si éxito
            new Menu().setVisible(true);
            // Aquí iría la lógica para abrir la siguiente ventana
        } else {
            intentosFallidos++;
            JOptionPane.showMessageDialog(vista,
                    "Usuario o contraseña incorrectos. Intentos: " + intentosFallidos,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            if (intentosFallidos >= MAX_INTENTOS) {
                JOptionPane.showMessageDialog(vista,
                        "Has alcanzado el máximo de intentos permitidos. El sistema bloqueará el acceso durante 30 segundos.",
                        "Acceso bloqueado",
                        JOptionPane.ERROR_MESSAGE);

                vista.getBtnLogin().setEnabled(false);

                // Iniciar temporizador para desbloquear
                timerDesbloqueo.start();
            }
        }
    }

    private void desbloquearLogin() {
        intentosFallidos = 0;
        vista.getBtnLogin().setEnabled(true);
        JOptionPane.showMessageDialog(vista,
                "Puedes intentar iniciar sesión nuevamente.",
                "Acceso desbloqueado",
                JOptionPane.INFORMATION_MESSAGE);
    }
}