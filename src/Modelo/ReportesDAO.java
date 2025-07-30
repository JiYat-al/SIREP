package Modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAO {

    public List<ReporteDocenteAR> obtenerInfoAsesorRevisor(int numero_tarjeta, int n) {
        /**1 ASESOR 2 REVISOR*/
        if(n==1){
            List<ReporteDocenteAR> lista = new ArrayList<>();
            String sql = "SELECT D.numero_tarjeta, D.nombre, D.apellido_paterno, D.apellido_materno, \n" +
                    "DP.rol, DP.etapa, P.nombre AS nombre_del_proyecto, P.descripcion, \n" +
                    "R.nombre AS nombre_alumno, R.apellido_paterno AS ap_pat_alumno, R.apellido_materno AS ap_mat_alumno \n" +
                    "FROM DOCENTE D \n" +
                    "JOIN DOCENTE_PROYECTO DP ON D.numero_tarjeta = DP.numero_tarjeta \n" +
                    "JOIN PROYECTO P ON P.id_proyecto = DP.id_proyecto \n" +
                    "JOIN RESIDENTE R ON R.id_proyecto = P.id_proyecto \n" +
                    " WHERE D.numero_tarjeta = ? AND rol='Asesor';";

            try (Connection con = Conexion_bd.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setInt(1, numero_tarjeta);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ReporteDocenteAR info = new ReporteDocenteAR();
                    info.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                    info.setNombreDocente(rs.getString("nombre"));
                    info.setApellidoPaterno(rs.getString("apellido_paterno"));
                    info.setApellidoMaterno(rs.getString("apellido_materno"));
                    info.setRol(rs.getString("rol"));
                    info.setEtapa(rs.getString("etapa"));
                    info.setNombreProyecto(rs.getString("nombre_del_proyecto"));
                    info.setDescripcionProyecto(rs.getString("descripcion"));
                    info.setNombreAlumno(rs.getString("nombre_alumno"));
                    info.setApellidoPaternoAlumno(rs.getString("ap_pat_alumno"));
                    info.setApellidoMaternoAlumno(rs.getString("ap_mat_alumno"));
                    lista.add(info);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lista;
        }
        List<ReporteDocenteAR> lista = new ArrayList<>();
        String sql = "SELECT D.numero_tarjeta, D.nombre, D.apellido_paterno, D.apellido_materno, \n" +
                "DP.rol, DP.etapa, P.nombre AS nombre_del_proyecto, P.descripcion, \n" +
                "R.nombre AS nombre_alumno, R.apellido_paterno AS ap_pat_alumno, R.apellido_materno AS ap_mat_alumno \n" +
                "FROM DOCENTE D \n" +
                "JOIN DOCENTE_PROYECTO DP ON D.numero_tarjeta = DP.numero_tarjeta \n" +
                "JOIN PROYECTO P ON P.id_proyecto = DP.id_proyecto \n" +
                "JOIN RESIDENTE R ON R.id_proyecto = P.id_proyecto \n" +
                " WHERE D.numero_tarjeta = ? AND rol='Revisor';";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, numero_tarjeta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())  {
                ReporteDocenteAR info = new ReporteDocenteAR();
                info.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                info.setNombreDocente(rs.getString("nombre"));
                info.setApellidoPaterno(rs.getString("apellido_paterno"));
                info.setApellidoMaterno(rs.getString("apellido_materno"));
                info.setRol(rs.getString("rol"));
                info.setEtapa(rs.getString("etapa"));
                info.setNombreProyecto(rs.getString("nombre_del_proyecto"));
                info.setDescripcionProyecto(rs.getString("descripcion"));
                info.setNombreAlumno(rs.getString("nombre_alumno"));
                info.setApellidoPaternoAlumno(rs.getString("ap_pat_alumno"));
                info.setApellidoMaternoAlumno(rs.getString("ap_mat_alumno"));
                lista.add(info);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;

}
    public List<Docente> obtenerAsesores() {
        List<Docente> lista = new ArrayList<>();

        String sql = "SELECT D.numero_tarjeta, D.nombre, D.apellido_paterno, D.apellido_materno " +
                "FROM DOCENTE AS D " +
                "JOIN DOCENTE_PROYECTO AS DP ON D.numero_tarjeta = DP.numero_tarjeta " +
                "WHERE DP.rol = 'Asesor';";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Docente d = new Docente();
                d.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                d.setNombre(rs.getString("nombre"));
                d.setApellidoPaterno(rs.getString("apellido_paterno"));
                d.setApellidoMaterno(rs.getString("apellido_materno"));
                lista.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Docente> obtenerRevisores() {
        List<Docente> lista = new ArrayList<>();

        String sql = "SELECT D.numero_tarjeta, D.nombre, D.apellido_paterno, D.apellido_materno " +
                "FROM DOCENTE AS D " +
                "JOIN DOCENTE_PROYECTO AS DP ON D.numero_tarjeta = DP.numero_tarjeta " +
                "WHERE DP.rol = 'Revisor';";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Docente d = new Docente();
                d.setNumeroTarjeta(rs.getInt("numero_tarjeta"));
                d.setNombre(rs.getString("nombre"));
                d.setApellidoPaterno(rs.getString("apellido_paterno"));
                d.setApellidoMaterno(rs.getString("apellido_materno"));
                lista.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<ModeloResidente> obtenerResidentes() {
        List<ModeloResidente> lista = new ArrayList<>();

        String sql = "SELECT id_residente,numero_control, nombre,apellido_paterno,apellido_materno " +
                "FROM RESIDENTE WHERE estatus=true AND id_estatus_residente=2;";

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModeloResidente r = new ModeloResidente();
                r.setIdResidente(rs.getInt("id_residente"));
                r.setNumeroControl(rs.getInt("numero_control"));
                r.setNombre(rs.getString("nombre"));
                r.setApellidoPaterno(rs.getString("apellido_paterno"));
                r.setApellidoMaterno(rs.getString("apellido_materno"));
                lista.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;


    }


    public List<ReporteAlumnoGeneral> obtenerReporteGeneralAlumno(int id) {
        List<ReporteAlumnoGeneral> lista = new ArrayList<>();

        String sql = """
        SELECT 
          R.numero_control,
          R.nombre,
          R.apellido_paterno,
          R.apellido_materno,
          R.semestre,
          R.telefono,
          P.nombre AS nombre_proyecto,
          P.descripcion,
          P.duracion,
          P.fecha_inicio,
          P.fecha_fin,
          E.nombre AS nombre_empresa,

          (
            SELECT STRING_AGG(D.nombre || ' ' || D.apellido_paterno || ' ' || D.apellido_materno || '' || D.correo || '' || DP2.Etapa || ' (' || DP2.rol || ')', ', ')
            FROM DOCENTE_PROYECTO AS DP2
            JOIN DOCENTE AS D ON DP2.numero_tarjeta = D.numero_tarjeta
            WHERE DP2.id_proyecto = P.id_proyecto AND DP2.rol = 'Asesor'
          ) AS asesores,

          (
            SELECT STRING_AGG(D.nombre || ' ' || D.apellido_paterno || ' ' || D.apellido_materno || '' || D.correo || '' || DP2.Etapa || ' (' || DP2.rol || ')', ', ')
            FROM DOCENTE_PROYECTO AS DP2
            JOIN DOCENTE AS D ON DP2.numero_tarjeta = D.numero_tarjeta
            WHERE DP2.id_proyecto = P.id_proyecto AND DP2.rol = 'Revisor'
          ) AS revisores

        FROM 
          RESIDENTE AS R 
        JOIN PROYECTO AS P ON R.id_proyecto = P.id_proyecto
        JOIN ESTATUS_PROYECTO AS EP ON EP.id_estatus_proyecto = P.id_estatus_proyecto
        JOIN EMPRESA AS E ON E.id_empresa = P.id_empresa
        WHERE EP.id_estatus_proyecto = 1 AND R.id_residente = ?;
    """;

        try (Connection con = Conexion_bd.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);  // ahora sí es válido, después de declarar stmt

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReporteAlumnoGeneral rep = new ReporteAlumnoGeneral();
                    rep.setNumeroControl(rs.getString("numero_control"));
                    rep.setNombre(rs.getString("nombre"));
                    rep.setApellidoPaterno(rs.getString("apellido_paterno"));
                    rep.setApellidoMaterno(rs.getString("apellido_materno"));
                    rep.setSemestre(rs.getInt("semestre"));
                    rep.setTelefono(rs.getString("telefono"));
                    rep.setNombreProyecto(rs.getString("nombre_proyecto"));
                    rep.setDescripcion(rs.getString("descripcion"));
                    rep.setDuracion(rs.getString("duracion"));
                    rep.setFechaInicio(rs.getString("fecha_inicio"));
                    rep.setFechaFin(rs.getString("fecha_fin"));
                    rep.setNombreEmpresa(rs.getString("nombre_empresa"));
                    rep.setAsesores(rs.getString("asesores"));
                    rep.setRevisores(rs.getString("revisores"));
                    lista.add(rep);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
