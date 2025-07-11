package Modelo;
/**MODELO PARA BD USUARIO**/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class UsuarioDAO {
    public Usuario validarUsuario(String usuarioIngresado, String contraseñaIngresada) {
        String sql = "SELECT * FROM public.usuario WHERE usuario = ? AND contrasena = ?";
        System.out.println(usuarioIngresado+contraseñaIngresada);
        try (Connection conn = Conexion_bd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuarioIngresado);
            ps.setString(2, contraseñaIngresada);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Si el usuario existe, crear y llenar el modelo Usuario
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contrasena"));
                u.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
