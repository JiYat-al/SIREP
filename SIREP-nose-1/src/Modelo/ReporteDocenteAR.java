package Modelo;

public class ReporteDocenteAR {
    private int numeroTarjeta;
    private String nombreDocente;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rol;
    private String etapa;
    private String nombreProyecto;
    private String descripcionProyecto;
    private String nombreAlumno;
    private String apellidoPaternoAlumno;
    private String apellidoMaternoAlumno;

    public ReporteDocenteAR() {
    }

    public ReporteDocenteAR(int numeroTarjeta, String nombreDocente,
                         String apellidoPaterno, String apellidoMaterno, String rol, String etapa,
                         String nombreProyecto, String descripcionProyecto, String nombreAlumno,
                         String apellidoPaternoAlumno, String apellidoMaternoAlumno) {
        this.numeroTarjeta = numeroTarjeta;
        this.nombreDocente = nombreDocente;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.rol = rol;
        this.etapa = etapa;
        this.nombreProyecto = nombreProyecto;
        this.descripcionProyecto = descripcionProyecto;
        this.nombreAlumno = nombreAlumno;
        this.apellidoPaternoAlumno = apellidoPaternoAlumno;
        this.apellidoMaternoAlumno = apellidoMaternoAlumno;
    }

    public int getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(int numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getDescripcionProyecto() {
        return descripcionProyecto;
    }

    public void setDescripcionProyecto(String descripcionProyecto) {
        this.descripcionProyecto = descripcionProyecto;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidoPaternoAlumno() {
        return apellidoPaternoAlumno;
    }

    public void setApellidoPaternoAlumno(String apellidoPaternoAlumno) {
        this.apellidoPaternoAlumno = apellidoPaternoAlumno;
    }

    public String getApellidoMaternoAlumno() {
        return apellidoMaternoAlumno;
    }

    public void setApellidoMaternoAlumno(String apellidoMaternoAlumno) {
        this.apellidoMaternoAlumno = apellidoMaternoAlumno;
    }
}
