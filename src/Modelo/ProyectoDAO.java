package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProyectoDAO {
  /**  public boolean nuevoProyecto( String nombre,String descripcion, String duracion, int numero_alumnos,
                                  int id_empresa, Date fecha_inicio, Date fecha_fin, String periodo_realizacion,
                                  int id_estatus_proyecto, int id_origen, int id_estatus_anteproyecto) {

        String sql="INSERT INTO public.proyecto (nombre,descripcion,duracion,n_alumnos," +
                "id_empresa,fecha_inicio,fecha_fin,periodo_realizacion," +
                "id_estatus_proyecto,id_origen,id_estatus_anteproyecto) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, duracion);
            ps.setInt(4, numero_alumnos);
            ps.setInt(5, id_empresa);
            ps.setDate(6, (java.sql.Date) fecha_inicio);
            ps.setDate(7, (java.sql.Date) fecha_fin);
            ps.setString(8, periodo_realizacion);
            ps.setInt(9, id_estatus_proyecto);
            ps.setInt(10, id_origen);
            ps.setInt(11, id_estatus_anteproyecto);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/
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
    /**Cargar la tabla de proyecto de residencia*/
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

    public verInormacion(){
    String sql="";

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
