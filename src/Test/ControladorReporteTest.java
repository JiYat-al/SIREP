package Test;

import Controlador.ControladorReporte;
import Modelo.Docente;
import Modelo.ModeloResidente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorReporteTest {

    private ControladorReporte controlador;

    @BeforeEach
    public void setUp() {
        controlador = new ControladorReporte(); // Usa el constructor por defecto
    }

    @Test
    public void testObtenerAsesores() {
        List<Docente> asesores = controlador.obtenerAsesores();
        assertNotNull(asesores, "La lista de asesores no debe ser null");
        // Prueba que no este vaciaaa
        assertFalse(asesores.isEmpty(), "La lista de asesores no debe estar vacía");
    }

    @Test
    public void testObtenerRevisores() {
        List<Docente> revisores = controlador.obtenerRevisores();
        assertNotNull(revisores, "La lista de revisores no debe ser null");
        assertFalse(revisores.isEmpty(), "La lista de revisores no debe estar vacía");
    }

    @Test
    public void testObtenerResidentes() {
        List<ModeloResidente> residentes = controlador.obtenerResidentes();
        assertNotNull(residentes, "La lista de residentes no debe ser null");
        assertFalse(residentes.isEmpty(), "La lista de residentes no debe estar vacía");
    }
}
