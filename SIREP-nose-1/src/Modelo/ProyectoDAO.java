package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProyectoDAO {
    public boolean nuevoProyecto( Proyectos p) {
        String sql="INSERT INTO public.proyecto (nombre,descripcion,duracion,n_alumnos," +
                "id_empresa, id_estatus_proyecto,id_origen) VALUES (?,?,?,?,?,?,?);";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getDuracion());
            ps.setInt(4, p.getNumero_alumnos());
            ps.setInt(5, p.getId_empresa());
            ps.setInt(6,1);
            ps.setInt(7,1);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**Para cargar la tabla de proyectos del banco*/
    public List<Proyectos> ObtenerProyectosBanco(){
        List<Proyectos> lista = new ArrayList<>();
        String sql="SELECT p.id_proyecto, p.nombre, p.descripcion, op.nombre_origen FROM public.proyecto AS p JOIN " +
                "public.origen_proyecto AS op ON p.id_origen = op.id_origen WHERE p.estado_actividad = true " +
                "AND p.id_origen = 1 AND id_estatus_proyecto=2;";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proyectos p = new Proyectos();
                p.setId_proyecto(rs.getInt("id_proyecto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setNombreOrigen(rs.getString("nombre_origen"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    /**Cargar la tabla de anteproyectos de */


    public boolean Dardebaja( int id_proyecto){

        System.out.println(id_proyecto);
        String sql="UPDATE PROYECTO SET estado_actividad= false WHERE id_proyecto=?;";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_proyecto);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String[] verInformacionProyectoBanco(int id_proyecto){
        String sql="SELECT p.id_proyecto,p.nombre,p.descripcion,p.duracion,p.n_alumnos, e.nombre AS empresa\n" +
                ",ep.descripcion_estatus AS estatus,\n" +
                "nombre_origen  \n" +
                "FROM Proyecto AS p\n" +
                "JOIN empresa AS e ON p.id_empresa=e.id_empresa\n" +
                "JOIN estatus_proyecto AS ep ON p.id_estatus_proyecto=ep.id_estatus_proyecto\n" +
                "JOIN origen_proyecto AS op ON p.id_origen=op.id_origen WHERE nombre_origen='Banco de Proyectos' " +
                "AND id_proyecto=?;";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_proyecto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[] {
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("duracion"),
                            rs.getString("n_alumnos"),
                            rs.getString("empresa"),
                            rs.getString("estatus"),
                            rs.getString("nombre_origen")
                    };
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no encontró resultados
    }



    /**Ediatr informacion Banco*/
    public boolean EditarInfoBanco(Proyectos p){

        String sql="UPDATE proyecto\n" +
                "SET nombre = ?,\n" +
                "    descripcion = ?,\n" +
                "    duracion = ?,\n" +
                "    n_alumnos = ?,\n" +
                "    id_empresa = ?,\n" +
                "    estado_actividad = ? " +
                "WHERE id_proyecto = ?;";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getDescripcion());
            stmt.setString(3, p.getDuracion());
            stmt.setInt(4, p.getNumero_alumnos());
            stmt.setInt(5, p.getId_empresa());
            stmt.setBoolean(6,p.getEstado());
            stmt.setInt(7, p.getId_proyecto());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
