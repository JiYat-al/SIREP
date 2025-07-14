
package Controlador;

import Modelo.ModeloResidente;
import Vista.VistaResidentes.AgregarManual;
import javax.swing.*;
import java.awt.*;

public class ControladorAgrManual {
    private AgregarManual vista;
    private ModeloResidente modelo;

    public ControladorAgrManual(AgregarManual vista) {
        this.vista = vista;
        this.modelo = new ModeloResidente();
    }

    /**
     * Validar todos los campos del formulario con validaciones mejoradas
     */
    public boolean validarCampos(String numeroControl, String nombre, String apellidoPaterno,
                                 String apellidoMaterno, String carrera, String semestre,
                                 String correo, String telefono) {

        // ==================== VALIDAR NÚMERO DE CONTROL ====================
        if (numeroControl.trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            return false;
        }

        // Eliminar espacios y caracteres no numéricos comunes
        String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

        // Verificar que solo contenga números
        if (!numControlLimpio.matches("\\d+")) {
            mostrarError("El número de control debe contener solo números");
            return false;
        }

        // Verificar longitud exacta de 8 dígitos
        if (numControlLimpio.length() != 8) {
            mostrarError("El número de control debe tener exactamente 8 dígitos");
            return false;
        }

        try {
            long numControl = Long.parseLong(numControlLimpio);

            // Extraer año (primeros 2 dígitos)
            int anio = Integer.parseInt(numControlLimpio.substring(0, 2));

            // Validar rango de años válidos (últimos 20 años hacia atrás y 2 años hacia adelante)
            int anioActual = java.time.Year.now().getValue() % 100; // Obtener últimos 2 dígitos del año actual
            int anioMinimo = (anioActual - 20 + 100) % 100; // Hace 20 años
            int anioMaximo = (anioActual + 2) % 100; // 2 años en el futuro

            // Manejar el cambio de siglo (ej: de 99 a 00)
            boolean anioValido = false;
            if (anioMinimo <= anioMaximo) {
                // Caso normal (ej: 04 a 26)
                anioValido = (anio >= anioMinimo && anio <= anioMaximo);
            } else {
                // Caso de cambio de siglo (ej: 84 a 02)
                anioValido = (anio >= anioMinimo || anio <= anioMaximo);
            }

            if (!anioValido) {
                mostrarError("El año en el número de control no es válido");
                return false;
            }

            // Extraer parte consecutiva (últimos 6 dígitos)
            String consecutivo = numControlLimpio.substring(2);
            int numConsecutivo = Integer.parseInt(consecutivo);

            // Validar que el consecutivo no sea 000000
            if (numConsecutivo == 0) {
                mostrarError("El número consecutivo del control no puede ser 000000");
                return false;
            }

        } catch (NumberFormatException e) {
            mostrarError("Error al procesar el número de control");
            return false;
        }

        // ==================== VALIDAR NOMBRE ====================
        if (nombre.trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            return false;
        }

        if (nombre.trim().length() < 2) {
            mostrarError("El nombre debe tener al menos 2 caracteres");
            return false;
        }

        if (nombre.trim().length() > 50) {
            mostrarError("El nombre no puede exceder 50 caracteres");
            return false;
        }

        // Validar que contenga solo letras, espacios y acentos
        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarError("El nombre solo puede contener letras, espacios y acentos");
            return false;
        }

        // ==================== VALIDAR APELLIDO PATERNO ====================
        if (apellidoPaterno.trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio");
            return false;
        }

        if (apellidoPaterno.trim().length() < 2) {
            mostrarError("El apellido paterno debe tener al menos 2 caracteres");
            return false;
        }

        if (apellidoPaterno.trim().length() > 50) {
            mostrarError("El apellido paterno no puede exceder 50 caracteres");
            return false;
        }

        // Validar que contenga solo letras, espacios y acentos
        if (!apellidoPaterno.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarError("El apellido paterno solo puede contener letras, espacios y acentos");
            return false;
        }

        // ==================== VALIDAR APELLIDO MATERNO (OPCIONAL) ====================
        if (!apellidoMaterno.trim().isEmpty()) {
            if (apellidoMaterno.trim().length() < 2) {
                mostrarError("El apellido materno debe tener al menos 2 caracteres");
                return false;
            }

            if (apellidoMaterno.trim().length() > 50) {
                mostrarError("El apellido materno no puede exceder 50 caracteres");
                return false;
            }

            // Validar que contenga solo letras, espacios y acentos
            if (!apellidoMaterno.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                mostrarError("El apellido materno solo puede contener letras, espacios y acentos");
                return false;
            }
        }

        // ==================== VALIDAR CARRERA ====================
        if (carrera.trim().isEmpty()) {
            mostrarError("La carrera es obligatoria");
            return false;
        }

        if (carrera.trim().length() < 3) {
            mostrarError("El nombre de la carrera debe tener al menos 3 caracteres");
            return false;
        }

        // ==================== VALIDAR SEMESTRE ====================
        if (semestre.trim().isEmpty()) {
            mostrarError("El semestre es obligatorio");
            return false;
        }

        try {
            int sem = Integer.parseInt(semestre.trim());
            if (sem < 1 || sem > 12) {
                mostrarError("El semestre debe estar entre 1 y 12");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El semestre debe ser un número válido");
            return false;
        }

        // ==================== VALIDAR CORREO ====================
        if (correo.trim().isEmpty()) {
            mostrarError("El correo electrónico es obligatorio");
            return false;
        }

        if (!validarFormatoCorreo(correo.trim())) {
            mostrarError("El formato del correo electrónico no es válido");
            return false;
        }

        // ==================== VALIDAR TELÉFONO (OPCIONAL) ====================
        if (!telefono.trim().isEmpty()) {
            // Limpiar teléfono de espacios, guiones y paréntesis
            String telefonoLimpio = telefono.trim().replaceAll("[\\s()-]", "");

            if (!validarFormatoTelefono(telefonoLimpio)) {
                mostrarError("El formato del teléfono no es válido");
                return false;
            }
        }

        return true;
    }

    // ==================== MÉTODOS AUXILIARES DE VALIDACIÓN ====================

    /**
     * Validar formato de correo electrónico con expresión regular robusta
     */
    private boolean validarFormatoCorreo(String correo) {
        // Expresión regular más robusta para emails
        String patronEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (!correo.matches(patronEmail)) {
            return false;
        }

        // Validaciones adicionales
        if (correo.length() > 254) return false; // RFC 5321
        if (correo.startsWith(".") || correo.endsWith(".")) return false;
        if (correo.contains("..")) return false; // Puntos consecutivos

        return true;
    }

    /**
     * Validar formato de teléfono con detección de patrones sospechosos
     */
    private boolean validarFormatoTelefono(String telefono) {
        // Remover todos los caracteres no numéricos
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");

        // Verificar longitud
        if (telefonoLimpio.length() < 8 || telefonoLimpio.length() > 15) {
            return false;
        }

        // Verificar que no sea una secuencia repetitiva obvia
        if (telefonoLimpio.matches("(\\d)\\1{7,}")) { // 8 o más dígitos iguales
            return false;
        }

        // Verificar que no sea una secuencia como 12345678
        if (telefonoLimpio.matches("12345678.*") || telefonoLimpio.matches("87654321.*")) {
            return false;
        }

        return true;
    }

    /**
     * Mostrar mensaje de error simplificado
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Verificar si ya existe un residente con el número de control
     */
    public boolean existeResidente(int numeroControl) {
        return ModeloResidente.existe(numeroControl);
    }

    /**
     * Crear y guardar un residente en la base de datos
     * MODIFICADO: Solo valida, NO guarda en BD, menos mensajes de aviso
     */
    public boolean guardarResidente(String numeroControl, String nombre, String apellidoPaterno,
                                    String apellidoMaterno, String carrera, String semestre,
                                    String correo, String telefono) {

        try {
            // Validar campos primero
            if (!validarCampos(numeroControl, nombre, apellidoPaterno, apellidoMaterno,
                    carrera, semestre, correo, telefono)) {
                return false;
            }

            // Limpiar número de control
            String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");
            int numControl = Integer.parseInt(numControlLimpio);

            // Verificar si ya existe el residente EN LA BD
            if (existeResidente(numControl)) {
                mostrarError("Ya existe un residente con este número de control");
                return false;
            }

            // CONFIRMACIÓN SIMPLIFICADA - menos texto, más directo
            int opcion = JOptionPane.showConfirmDialog(vista,
                    "¿Agregar este residente a la tabla?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            return opcion == JOptionPane.YES_OPTION;

        } catch (NumberFormatException e) {
            mostrarError("Error en el formato de los números");
            return false;
        } catch (Exception e) {
            mostrarError("Error inesperado. Contacte al administrador");
            return false;
        }
    }

    /**
     * Confirmar cancelación - SIMPLIFICADO
     */
    public boolean confirmarCancelacion(boolean hayCambios) {
        if (hayCambios) {
            int opcion = JOptionPane.showConfirmDialog(vista,
                    "¿Desea cancelar? Se perderán los cambios",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION);

            return opcion == JOptionPane.YES_OPTION;
        }
        return true;
    }

    /**
     * Buscar un residente por número de control
     */
    public ModeloResidente buscarResidentePorNumeroControl(int numeroControl) {
        return ModeloResidente.buscarPorNumeroControl(numeroControl);
    }

    /**
     * Validar que el número de control sea único con validación mejorada
     */
    public boolean validarNumeroControlUnico(String numeroControl) {
        try {
            // Limpiar número de control
            String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

            // Validar formato primero
            if (!validarSoloNumeroControl(numeroControl)) {
                return false;
            }

            int numControl = Integer.parseInt(numControlLimpio);

            if (existeResidente(numControl)) {
                mostrarError("Ya existe un residente con este número de control");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            mostrarError("El número de control debe ser un número válido");
            return false;
        }
    }

    /**
     * Limpiar formulario - SIMPLIFICADO
     */
    public void limpiarFormulario() {
        // Mensaje simplificado
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(vista,
                    "Formulario limpiado",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Validar solo el número de control con validación académica completa
     */
    public boolean validarSoloNumeroControl(String numeroControl) {
        if (numeroControl.trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            return false;
        }

        // Limpiar espacios y guiones
        String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

        // Verificar que solo contenga números
        if (!numControlLimpio.matches("\\d+")) {
            mostrarError("El número de control debe contener solo números");
            return false;
        }

        // Verificar longitud exacta de 8 dígitos
        if (numControlLimpio.length() != 8) {
            mostrarError("El número de control debe tener exactamente 8 dígitos");
            return false;
        }

        try {
            // Validar que sea un número válido
            long numControl = Long.parseLong(numControlLimpio);

            // Extraer año (primeros 2 dígitos)
            int anio = Integer.parseInt(numControlLimpio.substring(0, 2));

            // Validar rango de años
            int anioActual = java.time.Year.now().getValue() % 100;
            int anioMinimo = (anioActual - 20 + 100) % 100;
            int anioMaximo = (anioActual + 2) % 100;

            boolean anioValido = false;
            if (anioMinimo <= anioMaximo) {
                anioValido = (anio >= anioMinimo && anio <= anioMaximo);
            } else {
                anioValido = (anio >= anioMinimo || anio <= anioMaximo);
            }

            if (!anioValido) {
                mostrarError("El año en el número de control no es válido");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            mostrarError("Error al procesar el número de control");
            return false;
        }
    }

    /**
     * Validar solo el número de control (método legacy)
     */
    public boolean validarNumeroControl(String numeroControl) {
        return validarSoloNumeroControl(numeroControl);
    }

    /**
     * Obtener información de un residente para pre-cargar formulario
     */
    public String[] obtenerDatosResidente(int numeroControl) {
        ModeloResidente residente = buscarResidentePorNumeroControl(numeroControl);

        if (residente != null) {
            return new String[] {
                    String.valueOf(residente.getNumeroControl()),
                    residente.getNombre(),
                    residente.getApellidoPaterno(),
                    residente.getApellidoMaterno() != null ? residente.getApellidoMaterno() : "",
                    residente.getCarrera(),
                    String.valueOf(residente.getSemestre()),
                    residente.getCorreo(),
                    residente.getTelefono() != null ? residente.getTelefono() : ""
            };
        }

        return null;
    }

    /**
     * Método estático para facilitar el uso del controlador
     */
    public static boolean procesarAgregarResidente(Frame parent) {
        AgregarManual vista = new AgregarManual(parent);
        ControladorAgrManual controlador = new ControladorAgrManual(vista);

        // Aquí se configurarían los eventos entre vista y controlador
        // Por ahora, solo mostramos la vista
        vista.setVisible(true);

        return vista.isGuardado();
    }
}
