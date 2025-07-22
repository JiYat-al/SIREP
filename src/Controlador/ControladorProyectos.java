package Controlador;

import Modelo.ProyectoDAO;
import Modelo.Proyectos;

import java.util.ArrayList;
import java.util.List;


public class ControladorProyectos {
    private ProyectoDAO proyectoDAO;

    public ControladorProyectos(ProyectoDAO proyectoDAO) {
        this.proyectoDAO = proyectoDAO;
    }

    public List<Proyectos> ObProyectosBanco(){
       return proyectoDAO.ObtenerProyectosBanco();
    }

    public List<Proyectos> ObProyectosResidencia(){
        return proyectoDAO.ObtenerProyectosResidencia();
    }

    public List<Proyectos> ObAnteproyectos(){
        return proyectoDAO.ObtenerAnteproyectos();
    }
 /**Dependiendo del numero cambia el sql*/
    public boolean Baja( int id_proyecto){

        return proyectoDAO.Dardebaja(id_proyecto);
    }
}
