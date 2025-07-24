package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {
    /** public List<ModeloResidente> obtenerAlumnosPorDocente(int numeroTarjeta) {
     List<ModeloResidente> listaResidentes = new ArrayList<>();
     String sql=  "SELECT " +
     "d.numero_tarjeta AS numero_docente, " +
     "d.nombre AS nombre_docente, " +
     "r.numero_control, " +
     "r.nombre AS nombre_residente," +
     " r.semestre, " +
     "p.nombre AS nombre_proyecto " +
     "FROM docente d JOIN docente_proyecto dp" +
     " ON d.numero_tarjeta = dp.numero_tarjeta " +
     "JOIN proyecto p ON dp.id_proyecto = p.id_proyecto " +
     "JOIN residente r ON p.id_proyecto = r.id_proyecto " +
     "WHERE d.numero_tarjeta = ?;";

     try (Connection con = Conexion_bd.getConnection();
     PreparedStatement ps = con.prepareStatement(sql)) {

     ps.setInt(1, numeroTarjeta);
     ResultSet rs = ps.executeQuery();

     while (rs.next()) {
     ModeloResidente residente = new ModeloResidente();
     residente.setNumero_control(rs.getString("numero_control"));
     residente.setNombre(rs.getString("nombre_residente"));
     residente.setSemestre(rs.getInt("semestre"));
     residente.setNombreProyecto(rs.getString("nombre_proyecto")); // Asegúrate de tener este campo

     listaResidentes.add(residente);
     }

     } catch (Exception e) {
     e.printStackTrace();
     }
     return listaResidentes;
     }*/
}