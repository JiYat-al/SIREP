package Controlardor;

import Modelo.Docente;
import Modelo.DocenteDAO;
import Modelo.UsuarioDAO;
import Vista.DocentesUI;

import java.util.List;

public class ControladorDocente {
 private DocentesUI vistaDocentes;
 private DocenteDAO ModDocenteDao;
    public ControladorDocente() {

  this.ModDocenteDao = new DocenteDAO();

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
