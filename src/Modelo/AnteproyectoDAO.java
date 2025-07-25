package Modelo;

import org.postgresql.replication.ReplicationSlotInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnteproyectoDAO {

    public static ArrayList<Anteproyecto>  listaAnteproyectos () {
        ArrayList<Anteproyecto> anteproyectos = new ArrayList<>();
        Anteproyecto anteproyecto;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = Conexion_bd.getInstancia().getConexion();

        String  sql = "SELECT p.id_proyecto, p.ruta_archivo, dp.numero_tarjeta, p.perido_realizacion, p.estatus_anteproyecto,\n" +
                "fecha_inicio, p.fecha_fin\n" +
                "FROM proyecto p\n" +
                "JOIN docente_proyecto dp\n" +
                "  ON p.id_proyecto = dp.id_proyecto\n" +
                "WHERE p.id_estatus_proyecto = 1\n" +
                "  AND dp.rol = 'Asesor';";


        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                anteproyecto = new Anteproyecto();
                anteproyecto.setProyecto(ProyectoDAO.proyectoPorID(rs.getInt("id_proyecto")));
                anteproyecto.setArchivoAnteproyecto(rs.getString("ruta_archivo"));
                anteproyecto.setResidentes(ModeloResidente.buscarPorIDProyecto(rs.getInt("id_proyecto")));
                anteproyecto.setAsesor(DocenteDAO.docentePorID(rs.getInt("numero_tarjeta")));
                anteproyecto.setRevisores(DocenteDAO.revisoresPorIDProyecto(rs.getInt("id_proyecto")));
                anteproyecto.setPeriodo(Periodo.valueOf(rs.getString("perido_realizacion")));
                anteproyecto.setEstatusRevision(rs.getBoolean("estatus_anteproyecto"));
                anteproyecto.setFechaInicio(rs.getDate("fecha_inicio"));
                anteproyecto.setFechaFin(rs.getDate("fecha_fin"));
                anteproyectos.add(anteproyecto);
            }


        } catch (SQLException e){
            System.err.println(e);
        } finally {
            try{
                conn.close();
            } catch(SQLException e){
                System.err.println(e);
            }
        }
        return anteproyectos;
    }
}
