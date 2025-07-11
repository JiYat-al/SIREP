package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**INSERTAR */
public class DocenteDAO {
    public boolean nuevoDocente(int numeroTarjeta, String nombre,String apellido_paterno,String apellido_materno, String correo){
        String sql="INSERT INTO public.docente (numero_tarjeta, nombre, apellido_paterno, apellido_materno, correo) VALUES (?,?,?,?,?)";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numeroTarjeta);
            ps.setString(2, nombre);
            ps.setString(3, apellido_paterno);
            ps.setString(4, apellido_materno);
            ps.setString(5, correo);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

/**MODIFICAR NOMBRE*/
    public boolean actualizarNombre(int numeroTarjeta, String nombre,String apellido_paterno,String apellido_materno){
        String sql="UPDATE public.docente SET nombre=?, apellido_paterno=?, apellido_materno=? WHERE numero_tarjeta = ?";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellido_paterno);
            ps.setString(3, apellido_materno);
            ps.setInt(4, numeroTarjeta);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return false;
    }


    /**MODIFICAR correo*/

    public boolean nuevoCorreo(int numeroTarjeta, String correo){
        String sql="UPDATE public.docente SET correo=? WHERE numero_tarjeta = ?";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setInt(2, numeroTarjeta);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return false;
    }

}
