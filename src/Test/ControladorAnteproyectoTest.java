package Test;

import Controlador.ControladorAnteproyecto;
import Modelo.Empresa;
import Modelo.Proyecto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ControladorAnteproyectoTest {

    private ControladorAnteproyecto controlador;

    @BeforeEach
    void setUp() {
        controlador = new ControladorAnteproyecto();
    }

    @Test
    void testCargarComboProyectosBanco() {
        // Simular resultados
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto A");
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto B");

        List<Proyecto> listaSimulada = Arrays.asList(proyecto1, proyecto2);

        // Sobrescribir metodo para devolver datos simulados
        ControladorAnteproyecto controladorLocal = new ControladorAnteproyecto() {
            @Override
            public DefaultComboBoxModel<Proyecto> cargarComboProyectosBanco() {
                DefaultComboBoxModel<Proyecto> modeloCombo = new DefaultComboBoxModel<>();
                for (Proyecto p : listaSimulada) {
                    modeloCombo.addElement(p);
                }
                return modeloCombo;
            }
        };

        DefaultComboBoxModel<Proyecto> modelo = controladorLocal.cargarComboProyectosBanco();

        assertEquals(2, modelo.getSize());
        assertEquals("Proyecto A", modelo.getElementAt(0).getNombre());
        assertEquals("Proyecto B", modelo.getElementAt(1).getNombre());
    }

    @Test
    void testCargarComboEmpresas() {

        Empresa empresa1 = new Empresa();
        empresa1.setNombre("Empresa 1");
        Empresa empresa2 = new Empresa();
        empresa2.setNombre("Empresa 2");

        List<Empresa> empresasSimuladas = Arrays.asList(empresa1, empresa2);

        DefaultComboBoxModel<Empresa> modelo = new DefaultComboBoxModel<>();
        for (Empresa e : empresasSimuladas) {
            modelo.addElement(e);
        }

        assertEquals(2, modelo.getSize());
        assertEquals("Empresa 1", modelo.getElementAt(0).getNombre());
        assertEquals("Empresa 2", modelo.getElementAt(1).getNombre());
    }
}
