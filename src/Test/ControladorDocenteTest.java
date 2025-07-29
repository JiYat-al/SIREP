package Test;
import Controlador.ControladorDocente;
import Modelo.Docente;
import Modelo.DocenteDAO;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorDocenteTest {

    // Mock manual del DAO
    static class FakeDocenteDAO extends DocenteDAO {
        public List<Docente> lista = new ArrayList<>();

        @Override
        public List<Docente> obtenerTodos() {
            return lista;
        }

        @Override
        public boolean nuevoDocente(int numeroTarjeta, String nombre, String apPat, String apMat, String correo) {
            Docente d = new Docente();
            d.setNumeroTarjeta(numeroTarjeta);
            d.setNombre(nombre);
            d.setApellidoPaterno(apPat);
            d.setApellidoMaterno(apMat);
            d.setCorreo(correo);
            lista.add(d);
            return true;
        }

        @Override
        public boolean eliminarDocente(int numeroTarjeta) {
            return lista.removeIf(d -> d.getNumeroTarjeta() == numeroTarjeta);
        }

        @Override
        public boolean actualizarDatos(Docente docente) {
            for (Docente d : lista) {
                if (d.getNumeroTarjeta() == docente.getNumeroTarjeta()) {
                    d.setNombre(docente.getNombre());
                    return true;
                }
            }
            return false;
        }
    }

    @Test
    void testAgregarYObtenerDocente() {
        FakeDocenteDAO fakeDao = new FakeDocenteDAO();
        ControladorDocente controlador = new ControladorDocente(fakeDao);

        Docente docente = new Docente();
        docente.setNumeroTarjeta(1);
        docente.setNombre("Juan");
        docente.setApellidoPaterno("Perez");
        docente.setApellidoMaterno("Gomez");
        docente.setCorreo("juan@correo.com");

        assertTrue(controlador.agregarDocente(docente));

        List<Docente> lista = controlador.obtenerDocentes();
        assertEquals(1, lista.size());
        assertEquals("Juan", lista.get(0).getNombre());
    }



    @Test
    void testEliminarDocente() {
        FakeDocenteDAO fakeDao = new FakeDocenteDAO();
        ControladorDocente controlador = new ControladorDocente(fakeDao);

        Docente docente = new Docente();
        docente.setNumeroTarjeta(2);
        docente.setNombre("Ana");

        fakeDao.lista.add(docente);

        assertTrue(controlador.eliminarDocente(2));
        assertTrue(fakeDao.lista.isEmpty());
    }

    @Test
    void testActualizarDocente() {
        FakeDocenteDAO fakeDao = new FakeDocenteDAO();
        ControladorDocente controlador = new ControladorDocente(fakeDao);

        Docente docente = new Docente();
        docente.setNumeroTarjeta(3);
        docente.setNombre("Luis");

        fakeDao.lista.add(docente);

        docente.setNombre("Luis Actualizado");

        assertTrue(controlador.actualizarDatos(docente));
        assertEquals("Luis Actualizado", fakeDao.lista.get(0).getNombre());
    }
}
