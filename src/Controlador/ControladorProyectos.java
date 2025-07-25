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

   /* public List<Proyectos> ObProyectosResidencia(){
        return proyectoDAO.ObtenerProyectosResidencia();
    }*/


    /**Dependiendo del numero cambia el sql*/
    public boolean Baja( int id_proyecto){
        return proyectoDAO.Dardebaja(id_proyecto);
    }

    public String [] InformacionProyectoBanco(int id_proyecto){
        String[] datos = proyectoDAO.verInformacionProyectoBanco(id_proyecto);
        return datos;
    }

    public boolean EditarInformacionProyectoResidencia(Proyectos proyecto){
        return proyectoDAO.EditarInfoBanco(proyecto);
    }

    public boolean NuevoProyectoBanco(Proyectos proyecto){
        return proyectoDAO.nuevoProyecto(proyecto);
    }
}
