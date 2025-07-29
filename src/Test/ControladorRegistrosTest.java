package Test;

import Controlador.ControladorRegistros;
import Modelo.ModeloResidente;
import Vista.VistaRegistros;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorRegistrosTest {
    @Test
    public void testCargarTodosLosRegistros() {
        VistaRegistrosDummy vistaDummy = new VistaRegistrosDummy();
        ControladorRegistros controlador = new ControladorRegistros(vistaDummy);

        controlador.cargarTodosLosRegistros();

        // Verificamos que se hayan cargado registros
        assertFalse(vistaDummy.listaResidentesCargados.isEmpty()); // Esto también es correcto

    }

    // Clase dummy para simular la vista
    class VistaRegistrosDummy extends VistaRegistros {
        public List<ModeloResidente> listaResidentesCargados = new ArrayList<>();

        @Override
        public void cargarResidentes(List<ModeloResidente> residentes) {
            this.listaResidentesCargados = residentes;
        }
    }

    @Test
    public void testBuscarRegistrosPorNombre() {
        VistaRegistrosDummy vistaDummy = new VistaRegistrosDummy();
        ControladorRegistros controlador = new ControladorRegistros(vistaDummy);

        controlador.buscarRegistros("María");

        assertFalse(vistaDummy.listaResidentesCargados.isEmpty());
    }
    @Test
    public void testExisteResidente() {
        ControladorRegistros controlador = new ControladorRegistros(null);

        boolean existe = controlador.existeResidente(22161007);

        assertTrue(existe);
    }

}
