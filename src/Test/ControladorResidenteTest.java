package Test;

import Modelo.ModeloResidente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorResidenteTest {

    @Test
    public void testValidarResidenteParaGuardar_valido() {
        ModeloResidente residente = new ModeloResidente();
        residente.setNumeroControl(123);
        residente.setNombre("Juan");
        residente.setApellidoPaterno("Pérez");
        residente.setCorreo("juan@example.com");

        boolean valido = validarResidenteParaGuardar(residente);

        assertTrue(valido);
        assertTrue(residente.isEsValido());
        assertEquals("", residente.getMotivoInvalido());
    }


    @Test
    public void testValidarResidenteParaGuardar_invalidoNombre() {
        ModeloResidente residente = new ModeloResidente();
        residente.setNumeroControl(123);
        residente.setNombre("");  // nombre inválido
        residente.setApellidoPaterno("Pérez");
        residente.setCorreo("juan@example.com");

        boolean valido = validarResidenteParaGuardar(residente);

        assertFalse(valido);
    }



    @Test
    public void testExisteResidente() {
        int numeroControl = 22161032;
        boolean existe = ModeloResidente.existe(numeroControl);

        assertTrue(existe);
    }

    // Este metodo es auxiliar
    private boolean validarResidenteParaGuardar(ModeloResidente residente) {
        if (residente.getNumeroControl() <= 0) return false;
        if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) return false;
        if (residente.getApellidoPaterno() == null || residente.getApellidoPaterno().trim().isEmpty()) return false;
        if (residente.getCorreo() == null || residente.getCorreo().trim().isEmpty()) return false;

        residente.setEsValido(true);
        residente.setMotivoInvalido("");
        return true;
    }
}
