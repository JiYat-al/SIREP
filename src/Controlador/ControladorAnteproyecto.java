package Controlador;

import Modelo.AnteproyectoDAO;
import Modelo.Empresa;
import Modelo.Proyecto;
import Modelo.ProyectoDAO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorAnteproyecto {

    ProyectoDAO proyectoDAO = new ProyectoDAO();

    public DefaultComboBoxModel<Proyecto> cargarComboProyectosBanco() {
        List<Proyecto> listaProyectos = new ArrayList<>();
        listaProyectos = proyectoDAO.ObtenerProyectosBanco();
        DefaultComboBoxModel<Proyecto> modeloCombo = new DefaultComboBoxModel<>();
        for (Proyecto p : listaProyectos) {
            modeloCombo.addElement(p);
        }
        return modeloCombo;
    }
    public static DefaultComboBoxModel<Empresa> cargarComboEmpresas() {
        ArrayList<Empresa> listaEmpresas = new ArrayList<>();
        listaEmpresas = CtrlEmpresa.obtenerEmpresas();
        DefaultComboBoxModel<Empresa> modeloCombo = new DefaultComboBoxModel<>();
        for (Empresa p : listaEmpresas) {
            modeloCombo.addElement(p);
        }
        return modeloCombo;
    }
}
