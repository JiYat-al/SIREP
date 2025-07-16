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
     * MEJORADO: Validar todos los campos con validación de teléfono mejorada
     */
    public boolean validarCampos(String numeroControl, String nombre, String apellidoPaterno,
                                 String apellidoMaterno, String carrera, String semestre,
                                 String correo, String telefono) {

        // ==================== VALIDAR NÚMERO DE CONTROL ====================
        if (numeroControl.trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            return false;
        }

        String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

        if (!numControlLimpio.matches("\\d+")) {
            mostrarError("El número de control debe contener solo números");
            return false;
        }

        if (numControlLimpio.length() != 8) {
            mostrarError("El número de control debe tener exactamente 8 dígitos");
            return false;
        }

        try {
            long numControl = Long.parseLong(numControlLimpio);
            int anio = Integer.parseInt(numControlLimpio.substring(0, 2));
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

            String consecutivo = numControlLimpio.substring(2);
            int numConsecutivo = Integer.parseInt(consecutivo);

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

            if (!apellidoMaterno.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                mostrarError("El apellido materno solo puede contener letras, espacios y acentos");
                return false;
            }
        }

        // ==================== VALIDAR SEMESTRE ====================
        if (semestre.trim().isEmpty()) {
            mostrarError("El semestre es obligatorio");
            return false;
        }

        try {
            int sem = Integer.parseInt(semestre.trim());
            if (sem < 9 || sem > 15) {
                mostrarError("El semestre debe estar entre 9 y 15");
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

        // ==================== VALIDAR TELÉFONO MEJORADO ====================
        if (!telefono.trim().isEmpty()) {
            if (!validarFormatoTelefonoMejorado(telefono.trim())) {
                return false; // El error ya se muestra en el método
            }
        }

        return true;
    }

    // ==================== MÉTODOS AUXILIARES DE VALIDACIÓN ====================

    private boolean validarFormatoCorreo(String correo) {
        String patronEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (!correo.matches(patronEmail)) {
            return false;
        }

        if (correo.length() > 254) return false;
        if (correo.startsWith(".") || correo.endsWith(".")) return false;
        if (correo.contains("..")) return false;

        return true;
    }

    /**
     * NUEVO: Validación mejorada de teléfono con detección de patrones sospechosos
     */
    private boolean validarFormatoTelefonoMejorado(String telefono) {
        // Remover caracteres no numéricos para análisis
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");

        // Verificar longitud
        if (telefonoLimpio.length() < 8) {
            mostrarError("El teléfono debe tener al menos 8 dígitos");
            return false;
        }

        if (telefonoLimpio.length() > 15) {
            mostrarError("El teléfono no puede tener más de 15 dígitos");
            return false;
        }

        // Verificar patrones sospechosos
        if (telefonoLimpio.matches("(\\d)\\1{7,}")) {
            mostrarError("El teléfono no puede tener 8 o más dígitos iguales consecutivos");
            return false;
        }

        // Verificar secuencias obvias
        if (telefonoLimpio.matches("12345678.*")) {
            mostrarError("El teléfono no puede ser una secuencia ascendente");
            return false;
        }

        if (telefonoLimpio.matches("87654321.*")) {
            mostrarError("El teléfono no puede ser una secuencia descendente");
            return false;
        }

        // Verificar números comunes sospechosos
        if (telefonoLimpio.startsWith("00000000") ||
                telefonoLimpio.startsWith("11111111") ||
                telefonoLimpio.startsWith("99999999")) {
            mostrarError("El teléfono parece ser un número de prueba");
            return false;
        }

        // Verificar que tenga variación en los dígitos
        String[] digitos = telefonoLimpio.split("");
        java.util.Set<String> digitosUnicos = new java.util.HashSet<>(java.util.Arrays.asList(digitos));

        if (digitosUnicos.size() < 3) {
            mostrarError("El teléfono debe tener al menos 3 dígitos diferentes");
            return false;
        }

        // Validar formato común mexicano si parece ser de México
        if (telefonoLimpio.length() == 10) {
            // Validar códigos de área mexicanos comunes
            String codigoArea = telefonoLimpio.substring(0, 3);
            if (codigoArea.equals("000") || codigoArea.equals("111") || codigoArea.equals("999")) {
                mostrarError("El código de área del teléfono no es válido");
                return false;
            }
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista,
                mensaje,
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
    }

    public boolean existeResidente(int numeroControl) {
        return ModeloResidente.existe(numeroControl);
    }

    /**
     * MEJORADO: Guardar residente directamente en BD con mensajes concisos
     */
    public boolean guardarResidenteDirectoEnBD(String numeroControl, String nombre, String apellidoPaterno,
                                               String apellidoMaterno, String carrera, String semestre,
                                               String correo, String telefono) {
        try {
            if (!validarCampos(numeroControl, nombre, apellidoPaterno, apellidoMaterno,
                    carrera, semestre, correo, telefono)) {
                return false;
            }

            String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");
            int numControl = Integer.parseInt(numControlLimpio);

            if (existeResidente(numControl)) {
                mostrarError("Ya existe un residente con este número de control");
                return false;
            }

            ModeloResidente residente = new ModeloResidente();
            residente.setNumeroControl(numControl);
            residente.setNombre(formatearTexto(nombre.trim()));
            residente.setApellidoPaterno(formatearTexto(apellidoPaterno.trim()));
            residente.setApellidoMaterno(apellidoMaterno.trim().isEmpty() ? null : formatearTexto(apellidoMaterno.trim()));
            residente.setCarrera(carrera);
            residente.setSemestre(Integer.parseInt(semestre.trim()));
            residente.setCorreo(correo.trim().toLowerCase());
            residente.setTelefono(telefono.trim().isEmpty() ? null : telefono.trim());
            residente.setIdProyecto(1);

            boolean guardado = residente.guardarEnBaseDatos();

            if (guardado) {
                JOptionPane.showMessageDialog(vista,
                        "✅ Residente guardado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                mostrarError("❌ Error al guardar en la base de datos");
                return false;
            }

        } catch (NumberFormatException e) {
            mostrarError("Error en el formato de los números");
            return false;
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
            return false;
        }
    }

    private String formatearTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        String textoLimpio = texto.trim();

        if (!textoLimpio.contains(" ")) {
            return textoLimpio.substring(0, 1).toUpperCase() + textoLimpio.substring(1).toLowerCase();
        }

        String[] palabras = textoLimpio.split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                String palabra = palabras[i].substring(0, 1).toUpperCase() + palabras[i].substring(1).toLowerCase();
                resultado.append(palabra);
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }

        return resultado.toString();
    }

    /**
     * MEJORADO: Confirmación más concisa
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

    public ModeloResidente buscarResidentePorNumeroControl(int numeroControl) {
        return ModeloResidente.buscarPorNumeroControl(numeroControl);
    }

    public boolean validarNumeroControlUnico(String numeroControl) {
        try {
            String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

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

    public boolean validarSoloNumeroControl(String numeroControl) {
        if (numeroControl.trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            return false;
        }

        String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

        if (!numControlLimpio.matches("\\d+")) {
            mostrarError("El número de control debe contener solo números");
            return false;
        }

        if (numControlLimpio.length() != 8) {
            mostrarError("El número de control debe tener exactamente 8 dígitos");
            return false;
        }

        try {
            long numControl = Long.parseLong(numControlLimpio);
            int anio = Integer.parseInt(numControlLimpio.substring(0, 2));
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
     * NUEVO: Método estático para facilitar el uso
     */
    public static boolean procesarAgregarResidente(Frame parent) {
        AgregarManual vista = new AgregarManual(parent);
        ControladorAgrManual controlador = new ControladorAgrManual(vista);

        vista.setVisible(true);
        return vista.isGuardado();
    }

    /**
     * NUEVO: Validar solo el teléfono (método público para uso externo)
     */
    public boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return true; // Teléfono es opcional
        }
        return validarFormatoTelefonoMejorado(telefono.trim());
    }

    /**
     * NUEVO: Obtener sugerencias para corregir teléfono
     */
    public String[] getSugerenciasTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return new String[0];
        }

        java.util.List<String> sugerencias = new java.util.ArrayList<>();
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");

        // Sugerencia de formato
        if (telefonoLimpio.length() == 10) {
            sugerencias.add("Formato sugerido: " +
                    telefonoLimpio.substring(0, 3) + "-" +
                    telefonoLimpio.substring(3, 6) + "-" +
                    telefonoLimpio.substring(6));
        }

        // Sugerencia para números muy cortos
        if (telefonoLimpio.length() < 8) {
            sugerencias.add("Agregue más dígitos (mínimo 8)");
        }

        // Sugerencia para números muy largos
        if (telefonoLimpio.length() > 15) {
            sugerencias.add("Retire dígitos extras (máximo 15)");
        }

        return sugerencias.toArray(new String[0]);
    }
}