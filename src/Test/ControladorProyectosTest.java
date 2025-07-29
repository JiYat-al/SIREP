package Test;

import Controlador.ControladorProyectos;
import Modelo.Proyecto;
import Modelo.ProyectoDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorProyectosTest {
    private ControladorProyectos controlador;
    @BeforeEach
    public void setUp() {
        ProyectoDAO daoReal = new ProyectoDAO(); // DAO real
        controlador = new ControladorProyectos(daoReal);
    }

    @Test
    public void testBajaProyectoRealDAO() {
        ProyectoDAO daoReal = new ProyectoDAO();
        ControladorProyectos controlador = new ControladorProyectos(daoReal);

        boolean resultado = controlador.Baja(1);
        assertTrue(resultado);
    }
    @Test
    public void testNuevoProyectoBancoSinMock() {
        ProyectoDAO daoReal = new ProyectoDAO();
        ControladorProyectos controlador = new ControladorProyectos(daoReal);

        Proyecto nuevo = new Proyecto();
        nuevo.setNombre("Test Proyecto");
        nuevo.setDescripcion("Proyecto de prueba");
        nuevo.setId_empresa(1);

        boolean resultado = controlador.NuevoProyectoBanco(nuevo);
        assertTrue(resultado);
    }
    @Test
    public void testObProyectosBanco() {
        List<Proyecto> proyectos = controlador.ObProyectosBanco();
        assertNotNull(proyectos);
        assertTrue(proyectos.size() >= 0);
    }
    @Test
    /**Este test va a depender que el id se encuntre activo o coincida, delo contrario va a fallar */
    public void testInformacionProyectoBanco() {
        int idProyectoExistente = 24;
        String[] datos = controlador.InformacionProyectoBanco(idProyectoExistente);
        assertNotNull(datos);
        assertTrue(datos.length > 0);
    }
    @Test
    public void testEditarInformacionProyectoResidencia() {
        Proyecto proyecto = new Proyecto();
        proyecto.setId_proyecto(1);
        proyecto.setNombre("Nombre Editado");
        proyecto.setDescripcion("Descripción editada");
        proyecto.setDuracion("10 meses");
        proyecto.setId_empresa(1);

        boolean resultado = controlador.EditarInformacionProyectoResidencia(proyecto);
        assertTrue(resultado);
    }

}
