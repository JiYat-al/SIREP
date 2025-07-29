package Test;

import Controlador.ControladorLogin;
import Modelo.Usuario;
import Modelo.UsuarioDAO;
import Vista.LoginITO;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorLoginTest {

    // Subclase simulada de LoginITO
    class LoginITOSimulado extends LoginITO {
        private JTextField txtUsuario = new JTextField();
        private JPasswordField txtPassword = new JPasswordField();
        private JButton btnLogin = new JButton("Login");

        public LoginITOSimulado(String usuario, String password) {
            txtUsuario.setText(usuario);
            txtPassword.setText(password);
        }

        @Override
        public JTextField getTxtUsuario() {
            return txtUsuario;
        }

        @Override
        public JPasswordField getTxtPassword() {
            return txtPassword;
        }

        @Override
        public JButton getBtnLogin() {
            return btnLogin;
        }

        @Override
        public void dispose() {
            // No hacer nada
        }
    }

    // Subclase simulada de UsuarioDAO
    class UsuarioDAOSimulado extends UsuarioDAO {
        @Override
        public Usuario validarUsuario(String usuario, String contrasena) {
            if (usuario.equals("admin") && contrasena.equals("1234")) {
                return new Usuario(); // válido
            }
            return null; // inválido
        }
    }

    @Test
    public void testIniciarSesionCorrecto() {
        LoginITOSimulado vista = new LoginITOSimulado("admin", "1234");

        // Crear instancia real pero sustituir el DAO manualmente
        ControladorLogin controlador = new ControladorLogin(vista) {
            {
                this.usuarioDAO = new UsuarioDAOSimulado();
            }
        };

        boolean resultado = controlador.iniciarSesion("admin", "1234");
        assertTrue(resultado);
    }

    @Test
    public void testIniciarSesionIncorrecto() {
        LoginITOSimulado vista = new LoginITOSimulado("admin", "1231");

        ControladorLogin controlador = new ControladorLogin(vista) {
            {
                this.usuarioDAO = new UsuarioDAOSimulado();
            }
        };

        boolean resultado = controlador.iniciarSesion("admin", "mla");
        assertFalse(resultado);
    }
}
