package Controlardor;

import Modelo.ConsultasEmpresa;
import Modelo.Empresa;

import javax.swing.*;
import java.util.ArrayList;

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

    public static void editarEmpresa (int id, JTextField txtNombre, JTextField txtDireccion, JTextField txtResponsable, JTextField txtTelefono, JTextField txtCorreo){
        Empresa empresa = new Empresa();
        empresa.setId(id);
        empresa.setNombre(txtNombre.getText());
        empresa.setDireccion(txtDireccion.getText());
        empresa.setResponsable(txtResponsable.getText());
        empresa.setTelefono(txtTelefono.getText());
        empresa.setCorreo(txtCorreo.getText());
        boolean ban = ConsultasEmpresa.actualizarEmpresa(empresa);
        System.out.println(empresa.getNombre() + empresa.getDireccion());
    }

}
