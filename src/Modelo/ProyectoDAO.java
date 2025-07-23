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
                "AND p.id_origen = 1;";
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
    public List<Proyectos> ObtenerAnteproyectos(){
        List<Proyectos> lista = new ArrayList<>();
        String sql="SELECT id_proyecto,nombre,descripcion, estatus FROM PROYECTO AS p JOIN estatus_anteproyecto AS " +
                "ea ON p.id_estatus_anteproyecto=ea.id_estatus_anteproyecto WHERE estado_actividad=true;";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Proyectos p = new Proyectos();
                p.setId_proyecto(rs.getInt("id_proyecto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setNombreOrigen(rs.getString("estatus"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    /**Cargar la tabla de proyecto de residencia
    public List<Proyectos> ObtenerProyectosResidencia(){
        List<Proyectos> lista = new ArrayList<>();
        String sql="SELECT id_proyecto, nombre,descripcion, nombre_origen FROM proyecto" +
                " AS p JOIN origen_proyecto AS op ON p.id_origen=op.id_origen Where p.id_origen=2 AND estado_actividad=true;";

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
*/
/**Dar de baja*/

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

    public String[] verInformacionProyectoResidencia(int id_proyecto){
        String sql="SELECT p.nombre,p.descripcion,p.duracion,p.n_alumnos, e.nombre AS empresa,\n" +
                "p.fecha_inicio,fecha_fin,p.perido_realizacion,ep.descripcion_estatus AS estatus,\n" +
                "nombre_origen,estatus AS estatus_anteproyecto FROM Proyecto AS p\n" +
                "JOIN empresa AS e ON p.id_empresa=e.id_empresa\n" +
                "JOIN estatus_proyecto AS ep ON p.id_estatus_proyecto=ep.id_estatus_proyecto\n" +
                "JOIN origen_proyecto AS op ON p.id_origen=op.id_origen\n" +
                "JOIN estatus_anteproyecto AS ea ON p.id_estatus_anteproyecto=ea.id_estatus_anteproyecto " +
                "WHERE nombre_origen='Externo' AND id_proyecto=?;";

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
                            rs.getString("fecha_inicio"),
                            rs.getString("fecha_fin"),
                            rs.getString("perido_realizacion"),
                            rs.getString("estatus"),
                            rs.getString("nombre_origen"),
                            rs.getString("estatus_anteproyecto"),

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

public String[] verInformacionAnteproyecto(int id_proyecto){
    String sql="SELECT p.nombre AS nombre_proyecto, p.descripcion,p.duracion,p.n_alumnos,e.nombre AS nombre_empresa,\n" +
            "nombre_origen, estatus AS estatus_anteproyecto\n" +
            "FROM PROYECTO AS p\n" +
            "JOIN empresa AS e ON p.id_empresa=e.id_empresa\n" +
            "JOIN origen_proyecto AS op ON p.id_origen=op.id_origen\n" +
            "JOIN estatus_anteproyecto AS ea ON p.id_estatus_anteproyecto=ea.id_estatus_anteproyecto " +
            "WHERE id_proyecto=?;";
    try (Connection con = Conexion_bd.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id_proyecto);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new String[] {
                        rs.getString("nombre_proyecto"),
                        rs.getString("descripcion"),
                        rs.getString("duracion"),
                        rs.getString("n_alumnos"),
                        rs.getString("nombre_empresa"),
                        rs.getString("nombre_origen"),
                        rs.getString("estatus_anteproyecto"),

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







    /**INSERTA UN NUEVO PROYECTO PARA EL BANCO DE PROYECTOS
    public boolean nuevoProyecto(String nombre, String descripcion, String duracion, int n_alumnos,int id_empresa){
        String sql= "INSERT INTO proyecto (nombre,descripcion,duracion,n_alumnos,id_empresa) VALUES (?,?,?,?,?);";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, duracion);
            ps.setInt(4, n_alumnos);
            ps.setInt(5, id_empresa);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/

    /**Editar el estatus del proyecto

    public boolean editarStatus(int id_estatus_proyecto, int id_proyecto){
        String sql="UPDATE proyecto SET id_estatus_proyecto=? WHERE id_proyecto=?";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_estatus_proyecto);
            ps.setInt(2, id_proyecto);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/
}
