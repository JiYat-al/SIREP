package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConsultasEmpresa{

    public static boolean registrar(Empresa empresas) {
        PreparedStatement ps = null;
        Connection conn = Conexion_bd.getInstancia().getConexion();

        String sql = "INSERT INTO empresa (nombre, direccion, responsable, telefono, correo) VALUES (?, ?, ?, ?, ?)";

        try {

            ps = conn.prepareStatement(sql);
            ps.setString(1, empresas.getNombre());
            ps.setString(2, empresas.getDireccion());
            ps.setString(3, empresas.getResponsable());
            ps.setString(4, empresas.getTelefono());
            ps.setString(5, empresas.getCorreo());
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

    public static ArrayList<Empresa> recuperarDatos() {
        ArrayList<Empresa> empresas = new ArrayList<>();
        Empresa empresa = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = Conexion_bd.getInstancia().getConexion();

        String  sql = "SELECT * FROM empresa";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                empresa = new Empresa();
                empresa.setId(rs.getInt("id_empresa"));
                empresa.setNombre(rs.getString("nombre"));
                empresa.setDireccion(rs.getString("direccion"));
                empresa.setResponsable(rs.getString("responsable"));
                empresa.setTelefono(rs.getString("telefono"));
                empresa.setCorreo(rs.getString("correo"));
                empresas.add(empresa);
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
        return empresas;
    }
}
