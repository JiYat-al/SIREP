package Controlador;

import Modelo.Docente;
import Modelo.DocenteDAO;
import Vista.DocentesUI;

public class ControladorDocente {
    private DocentesUI vistaDocentes;
    protected DocenteDAO ModDocenteDao;
    public ControladorDocente() {

        this.ModDocenteDao = new DocenteDAO();

    }

    /**CONTROLADOR MOCK PARA PRUEBAS*/
    public ControladorDocente(DocenteDAO dao) {
        this.ModDocenteDao = dao;
    }

    public java.util.List<Docente> obtenerDocentes() {
        return ModDocenteDao.obtenerTodos();
    }

    public boolean agregarDocente(Docente docente) {
        int numeroTarjeta = docente.getNumeroTarjeta();
        String nombre = docente.getNombre();
        String apellido_paterno = docente.getApellidoPaterno();
        String apellido_materno = docente.getApellidoMaterno();
        String correo = docente.getCorreo();
        return ModDocenteDao.nuevoDocente(numeroTarjeta,nombre,apellido_paterno,apellido_materno,correo);
    }
    public boolean eliminarDocente(int numeroTarjeta) {
        return ModDocenteDao.eliminarDocente(numeroTarjeta);
    }
    public boolean actualizarDatos(Docente docente) {

        return ModDocenteDao.actualizarDatos(docente);
    }
}