package Modelo;

import com.sun.source.tree.TryTree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConsultasEmpresas {

    public boolean registrar(Empresas empresas) {
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
