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
    private boolean tieneProrroga;
    private Date fechaFinNueva;
    private String archivoAutorizacionProrroga;

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
        this.tieneProrroga = false;
        this.fechaFinNueva = null;
        this.archivoAutorizacionProrroga = null;
    }

    public Anteproyecto(Proyecto proyecto, String archivoAnteproyecto, ArrayList<ModeloResidente> residentes,
                        Docente asesor, ArrayList<Docente> revisores, Docente revisorAnteproyecto, Periodo periodo, boolean estatusRevision, Date fechaInicio,
                        Date fechaFin, boolean tieneProrroga, Date fechaFinNueva, String archivoAutorizacionProrroga) {
        this.proyecto = proyecto;
        this.archivoAnteproyecto = archivoAnteproyecto;
        this.residentes = residentes;
        this.asesor = asesor;
        this.revisores = revisores;
        this.revisorAnteproyecto = revisorAnteproyecto;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tieneProrroga = tieneProrroga;
        this.fechaFinNueva = fechaFinNueva;
        this.archivoAutorizacionProrroga = archivoAutorizacionProrroga;
    }

    public Anteproyecto() {
        proyecto = null;
        archivoAnteproyecto = null;
        residentes = null;
        asesor = null;
        revisores = null;
        revisorAnteproyecto = null;
        periodo = null;
        fechaInicio = null;
        fechaFin = null;
        fechaFinNueva = null;
        tieneProrroga = false;
        archivoAutorizacionProrroga = null;
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

    public boolean isTieneProrroga() {
        return tieneProrroga;
    }

    public void setTieneProrroga(boolean tieneProrroga) {
        this.tieneProrroga = tieneProrroga;
    }

    public Date getFechaFinNueva() {
        return fechaFinNueva;
    }

    public void setFechaFinNueva(Date fechaFinNueva) {
        this.fechaFinNueva = fechaFinNueva;
    }

    public String getArchivoAutorizacionProrroga() {
        return archivoAutorizacionProrroga;
    }

    public void setArchivoAutorizacionProrroga(String archivoAutorizacionProrroga) {
        this.archivoAutorizacionProrroga = archivoAutorizacionProrroga;
    }

}
