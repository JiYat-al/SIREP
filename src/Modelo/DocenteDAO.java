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
    //Cargar tabla docentes
    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql="SELECT numero_tarjeta,nombre,apellido_paterno,apellido_materno,correo FROM " +
                "docente WHERE estatus = true AND" +
                " numero_tarjeta NOT IN (SELECT numero_tarjeta FROM usuario);";


        //String sql = "SELECT * FROM docente";

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
        String sql = "UPDATE public.docente SET estatus=false WHERE numero_tarjeta = ?";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numeroTarjeta);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Docente docentePorID(int numero_tarjeta){
        Docente docente = new Docente();

        String sql = "SELECT * FROM docente WHERE numero_tarjeta = ?";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero_tarjeta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    docente.setNumeroTarjeta(numero_tarjeta);
                    docente.setNombre(rs.getString("nombre"));
                    docente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    docente.setApellidoMaterno(rs.getString("apellido_materno"));
                    docente.setCorreo(rs.getString("correo"));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return docente; // Si no encontró resultados
    }

    public static ArrayList<Docente> revisoresPorIDProyecto(int id_proyecto){
        Docente docente;
        ArrayList<Docente> revisores = new ArrayList<>();

        String sql = "SELECT d.numero_tarjeta, d.nombre, d.apellido_paterno, d.apellido_materno, correo\n" +
                "FROM docente d\n" +
                "JOIN docente_proyecto dp\n" +
                "\tON dp.numero_tarjeta = d.numero_tarjeta\n" +
                "WHERE dp.id_proyecto = ?\n" +
                "\tAND dp.rol = 'Revisor';";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_proyecto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    docente = new Docente();
                    docente.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                    docente.setNombre(rs.getString("nombre"));
                    docente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    docente.setApellidoMaterno(rs.getString("apellido_materno"));
                    docente.setCorreo(rs.getString("correo"));
                    revisores.add(docente);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return revisores; // Si no encontró resultados
    }

    public static Docente revisorAnteproyectoPorIDProyecto(int id_proyecto){
        Docente docente = new Docente();

        String sql = "SELECT d.numero_tarjeta, d.nombre, d.apellido_paterno, d.apellido_materno, correo\n" +
                "FROM docente d\n" +
                "JOIN docente_proyecto dp\n" +
                "\tON dp.numero_tarjeta = d.numero_tarjeta\n" +
                "WHERE dp.id_proyecto = ?\n" +
                "\tAND dp.rol = 'Revisor'" +
                "\tAND dp.etapa = 'anteproyecto';";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_proyecto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    docente = new Docente();
                    docente.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                    docente.setNombre(rs.getString("nombre"));
                    docente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    docente.setApellidoMaterno(rs.getString("apellido_materno"));
                    docente.setCorreo(rs.getString("correo"));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return docente; // Si no encontró resultados
    }

    public static boolean asginarRevisoresProyecto(Anteproyecto anteproyecto) {
        PreparedStatement ps = null;
        Connection conn = Conexion_bd.getInstancia().getConexion();

        String sql = "INSERT INTO docente_proyecto(numero_tarjeta, id_proyecto, rol, etapa)\n" +
                "\tVALUES (?, ?, 'Revisor', 'proyecto');";

        try {

            ps = conn.prepareStatement(sql);
            int id_proyecto = anteproyecto.getProyecto().getId_proyecto();

            for (Docente revisor : anteproyecto.getRevisores()) {
                ps.setInt(1, revisor.getNumeroTarjeta());
                ps.setInt(2, id_proyecto); // Primer parámetro: id_proyecto
// Segundo parámetro: id_residente
                ps.executeUpdate();
            }
            return true;

        } catch (SQLException e){
            System.err.println(e);
            return false;
        } finally {
            try{
                conn.close();
            } catch(SQLException e){
                System.err.println(e);
            }
        }
    }

    public static boolean asignarAsesorProyecto(Anteproyecto anteproyecto) {
        PreparedStatement ps = null;
        Connection conn = Conexion_bd.getInstancia().getConexion();

        String sql = "INSERT INTO docente_proyecto(numero_tarjeta, id_proyecto, rol, etapa)\n" +
                "\tVALUES (?, ?, 'Asesor', 'proyecto');";

        try {

            ps = conn.prepareStatement(sql);
            ps.setInt(1,anteproyecto.getAsesor().getNumeroTarjeta());
            ps.setInt(2,anteproyecto.getProyecto().getId_proyecto());
            ps.execute();
            return true;

        } catch (SQLException e){
            System.err.println(e);
            return false;
        } finally {
            try{
                conn.close();
            } catch(SQLException e){
                System.err.println(e);
            }
        }
    }

    public static boolean asignarRevisorAnteproyecto(Anteproyecto anteproyecto) {
        PreparedStatement ps = null;
        Connection conn = Conexion_bd.getInstancia().getConexion();

        String sql = "INSERT INTO docente_proyecto(numero_tarjeta, id_proyecto, rol, etapa)\n" +
                "\tVALUES (?, ?, 'Revisor', 'anteproyecto');";

        try {

            ps = conn.prepareStatement(sql);
            ps.setInt(1,anteproyecto.getRevisorAnteproyecto().getNumeroTarjeta());
            ps.setInt(2,anteproyecto.getProyecto().getId_proyecto());
            ps.execute();
            return true;

        } catch (SQLException e){
            System.err.println(e);
            return false;
        } finally {
            try{
                conn.close();
            } catch(SQLException e){
                System.err.println(e);
            }
        }
    }
}