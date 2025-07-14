package Modelo;

public class Empresa {

    private String nombre;
    private String direccion;
    private String responsable;
    private String telefono;
    private String correo;
    private int id;

    public Empresa(String nombre, String direccion, String responsable, String telefono, String correo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.responsable = responsable;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Empresa() {
        this.id = 0;
        this.direccion = null;
        this.responsable = null;
        this.telefono = null;
        this.correo = null;
    }

    public Empresa(int id, String nombre, String direccion, String responsable, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.responsable = responsable;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}
}
