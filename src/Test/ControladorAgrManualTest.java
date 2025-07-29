package Test;

import Controlador.ControladorAgrManual;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ControladorAgrManualTest {

    private ControladorAgrManual controlador;

    @BeforeEach
    void setUp() {
        controlador = new ControladorAgrManual() {

            protected void mostrarError(String mensaje) {
                // No hace nada en test para evitar mostrar diálogos
            }
        };
    }

    @Test
    void validarTelefono_null_retornaTrue() {
        assertTrue(controlador.validarTelefono(null));
    }

    @Test
    void validarTelefono_vacio_retornaTrue() {
        assertTrue(controlador.validarTelefono("  "));
    }

    @Test
    void validarTelefono_formatoValido_retornaTrue() {
        assertTrue(controlador.validarTelefono("5512345678"));
    }

    @Test
    void validarTelefono_formatoInvalido_retornaFalse() {
        assertFalse(controlador.validarTelefono("123"));
    }

    @Test
    void validarFormatoCorreo_correosValidos_retornaTrue() {
        assertTrue(controlador.validarFormatoCorreo("prueba@gmail.com"));
        assertTrue(controlador.validarFormatoCorreo("usuario.123@dominio.co"));
        assertTrue(controlador.validarFormatoCorreo("nombre_apellido@empresa.mx"));
    }

    @Test
    void validarCampos_todosCorrectos_retornaTrue() {
        boolean resultado = controlador.validarCampos(
                "23012345",       // número de control válido (8 dígitos)
                "Juan",           // nombre válido
                "Pérez",          // apellido paterno válido
                "López",          // apellido materno válido
                "ISC",            // carrera (no se valida en este metodo)
                "10",             // semestre válido
                "juan@mail.com",  // correo válido
                "5512345678"      // teléfono válido
        );
        assertTrue(resultado);
    }

    @Test
    void validarCampos_nombreVacio_retornaFalse() {
        boolean resultado = controlador.validarCampos(
                "23012345", "", "Pérez", "López", "ISC", "10", "juan@mail.com", "5512345678");
        assertFalse(resultado);
    }
    @Test
    void validarCampos_numControlConLetras_retornaFalse() {
        boolean resultado = controlador.validarCampos(
                "ABC12345", "Juan", "Pérez", "López", "ISC", "10", "juan@mail.com", "5512345678");
        assertFalse(resultado);
    }
    @Test
    void validarCampos_semestreInvalido_retornaFalse() {
        boolean resultado = controlador.validarCampos(
                "23012345", "Juan", "Pérez", "López", "ISC", "8", "juan@mail.com", "5512345678");
        assertFalse(resultado);
    }
@Test
    void validarApellidoPaterno_retornaFalse(){
        boolean resultado= controlador.validarCampos(
                "23012345", "Juan", "112", "López", "ISC", "8", "juan@mail.com", "5512345678");
    assertFalse(resultado);

}
    @Test
    void validarApellidoPaterno_vacio_retornaFalse(){
        boolean resultado= controlador.validarCampos(
                "23012345", "Juan", "   ", "López", "ISC", "8", "juan@mail.com", "5512345678");
        assertFalse(resultado);

    }


}
