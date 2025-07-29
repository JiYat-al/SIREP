package Test;

import Controlador.CtrlEmpresa;
import Modelo.Empresa;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorEmpresaTest {

    @Test
    public void testObtenerEmpresas() {
        ArrayList<Empresa> empresas = CtrlEmpresa.obtenerEmpresas();
        assertNotNull(empresas);
        assertFalse(empresas.isEmpty(), "La lista no debe estar vacía");
    }

    @Test
    public void testCambiarEstatus() {
        boolean resultado = CtrlEmpresa.cambiarEstatus(1);
        assertTrue(resultado, "Debe devolver true para id válido");
    }

    @Test
    public void testObtenerEmpresaPorId() {
        Empresa empresa = CtrlEmpresa.obtenerEmpresaPorId(2);
        assertNotNull(empresa, "La empresa no debe ser null");
        assertEquals("EcoBuild México", empresa.getNombre(), "Nombre debe ser EmpresaX");
    }
}
