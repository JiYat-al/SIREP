package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    /**Cargar tabla docentes*/
    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql="SELECT * FROM docente WHERE numero_tarjeta NOT IN (SELECT numero_tarjeta FROM usuario)";

        /**String sql = "SELECT * FROM docente";*/

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Docente d = new Docente();
                d.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                d.setNombre(rs.getString("nombre"));
                d.setApellidoPaterno(rs.getString("apellido_paterno"));
                d.setApellidoMaterno(rs.getString("apellido_materno"));
                d.setCorreo(rs.getString("correo"));
                lista.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }


    /**
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
     */
    public boolean actualizarDatos(Docente docente) {
        String sql = "UPDATE public.docente SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo = ? WHERE numero_tarjeta = ?";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, docente.getNombre());
            ps.setString(2, docente.getApellidoPaterno());
            ps.setString(3, docente.getApellidoMaterno());
            ps.setString(4, docente.getCorreo());
            ps.setInt(5, docente.getNumeroTarjeta());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean eliminarDocente(int numeroTarjeta) {
        String sql = "DELETE FROM public.docente WHERE numero_tarjeta = ?";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numeroTarjeta);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
