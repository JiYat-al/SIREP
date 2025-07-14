package Controlardor;

import Modelo.ConsultasEmpresa;
import Modelo.Empresa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class CtrlEmpresa  {

    public static void btnGuardar(JTextField txtNombre, JTextField txtDireccion, JTextField txtResponsable, JTextField txtTelefono, JTextField txtCorreo) {
        Empresa empresa;
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String responsable = txtResponsable.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();

        empresa = new Empresa(nombre, direccion, responsable, telefono, correo);

        boolean estaRegistrado = ConsultasEmpresa.registrar(empresa);

        if (estaRegistrado) {
            JOptionPane.showMessageDialog(null, "Registro Guardado");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo registrar");
        }
    }

    public static ArrayList<Empresa> obtenerEmpresas() {
        return ConsultasEmpresa.recuperarDatos();
    }


}
