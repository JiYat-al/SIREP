package Modelo;

import java.util.Date;

public class Proyectos {
    private int id_proyecto;
    private String nombre;
    private String descripcion;
    private String duracion;
    private int numero_alumnos;
    private int id_empresa;
    private Date fecha_inicio ;
    private Date fecha_fin ;
    private String periodo_realizacion;
    private int id_estatus_proyecto;
    private int id_origen;
    private String NombreOrigen;
    private int id_estatus_anteproyecto;

    public Proyectos() {

    }

    public Proyectos(String nombre, String descripcion, int id_origen) {  /**Constructor para obtener datos para vista*/
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.id_origen = id_origen;
    }

    public String getNombreOrigen() {
        return NombreOrigen;
    }

    public void setNombreOrigen(String nombreOrigen) {
        NombreOrigen = nombreOrigen;
    }

    public int getId_proyecto() {
        return id_proyecto;
    }

    public void setId_proyecto(int id_proyecto) {
        this.id_proyecto = id_proyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getNumero_alumnos() {
        return numero_alumnos;
    }

    public void setNumero_alumnos(int numero_alumnos) {
        this.numero_alumnos = numero_alumnos;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getPeriodo_realizacion() {
        return periodo_realizacion;
    }

    public void setPeriodo_realizacion(String periodo_realizacion) {
        this.periodo_realizacion = periodo_realizacion;
    }

    public int getId_estatus_proyecto() {
        return id_estatus_proyecto;
    }

    public void setId_estatus_proyecto(int id_estatus_proyecto) {
        this.id_estatus_proyecto = id_estatus_proyecto;
    }

    public int getId_origen() {
        return id_origen;
    }

    public void setId_origen(int id_origen) {
        this.id_origen = id_origen;
    }

    public int getId_estatus_anteproyecto() {
        return id_estatus_anteproyecto;
    }

    public void setId_estatus_anteproyecto(int id_estatus_anteproyecto) {
        this.id_estatus_anteproyecto = id_estatus_anteproyecto;
    }
}
