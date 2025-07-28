package Modelo;

import java.util.ArrayList;
import java.util.Date;

public class Anteproyecto {

    private Proyecto proyecto;
    private String archivoAnteproyecto;
    private ArrayList<ModeloResidente> residentes;
    private Docente asesor;
    private ArrayList<Docente> revisores;
    private Docente revisorAnteproyecto;
    private Periodo periodo;
    private Date fechaInicio;
    private Date fechaFin;

    public Anteproyecto(Proyecto proyecto, String archivoAnteproyecto, ArrayList<ModeloResidente> residentes,
                        Docente asesor, ArrayList<Docente> revisores, Docente revisorAnteproyecto, Periodo periodo, boolean estatusRevision, Date fechaInicio,
                        Date fechaFin) {
        this.proyecto = proyecto;
        this.archivoAnteproyecto = archivoAnteproyecto;
        this.residentes = residentes;
        this.asesor = asesor;
        this.revisores = revisores;
        this.revisorAnteproyecto = revisorAnteproyecto;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public  Anteproyecto() {
        proyecto = null;
        archivoAnteproyecto = null;
        residentes = null;
        asesor = null;
        revisores = null;
        revisorAnteproyecto = null;
        periodo = null;
        fechaInicio = null;
        fechaFin = null;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getArchivoAnteproyecto() {
        return archivoAnteproyecto;
    }

    public void setArchivoAnteproyecto(String archivoAnteproyecto) {
        this.archivoAnteproyecto = archivoAnteproyecto;
    }

    public ArrayList<ModeloResidente> getResidentes() {
        return residentes;
    }

    public void setResidentes(ArrayList<ModeloResidente> residentes) {
        this.residentes = residentes;
    }

    public Docente getAsesor() {
        return asesor;
    }

    public void setAsesor(Docente asesor) {
        this.asesor = asesor;
    }

    public ArrayList<Docente> getRevisores() {
        return revisores;
    }

    public void setRevisores(ArrayList<Docente> revisores) {
        this.revisores = revisores;
    }

    public Docente getRevisorAnteproyecto() {return revisorAnteproyecto;}

    public void setRevisorAnteproyecto(Docente revisorAnteproyecto) {this.revisorAnteproyecto = revisorAnteproyecto;}

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

}
