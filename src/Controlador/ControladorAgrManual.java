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


    public boolean validarCampos(String numeroControl, String nombre, String apellidoPaterno,
                                 String apellidoMaterno, String carrera, String semestre,
                                 String correo, String telefono) {


        if (numeroControl.trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            return false;
        }

        String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

        if (!numControlLimpio.matches("\\d+")) {
            mostrarError("El número de control debe contener solo números");
            return false;
        }

        // Validar longitud máxima de 9 caracteres ***
        if (numControlLimpio.length() > 9) {
            mostrarError("El número de control no puede tener más de 9 dígitos");
            return false;
        }

        //  Validar longitud mínima más flexible ***
        if (numControlLimpio.length() < 6) {
            mostrarError("El número de control debe tener al menos 6 dígitos");
            return false;
        }

        try {
            // Validar como String (no convertir a int para evitar overflow) ***
            // Si tiene 8 dígitos, aplicar validación tradicional del año
            if (numControlLimpio.length() == 8) {
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
                    mostrarError("El año en el número de control no es válido para formato de 8 dígitos");
                    return false;
                }

                String consecutivo = numControlLimpio.substring(2);
                if (consecutivo.equals("000000")) {
                    mostrarError("El número consecutivo del control no puede ser 000000");
                    return false;
                }
            }
            // Para otros formatos (6, 7, 9 dígitos), solo verificar que no sean todos ceros
            else {
                if (numControlLimpio.matches("0+")) {
                    mostrarError("El número de control no puede ser solo ceros");
                    return false;
                }
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

        if (nombre.trim().length() > 100) { // *** FIX: Aumentado límite a 100 ***
            mostrarError("El nombre no puede exceder 100 caracteres");
            return false;
        }

        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarError("El nombre solo puede contener letras, espacios y acentos");
            return false;
        }

        if (apellidoPaterno.trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio");
            return false;
        }

        if (apellidoPaterno.trim().length() < 2) {
            mostrarError("El apellido paterno debe tener al menos 2 caracteres");
            return false;
        }

        if (apellidoPaterno.trim().length() > 100) { // *** FIX: Aumentado límite a 100 ***
            mostrarError("El apellido paterno no puede exceder 100 caracteres");
            return false;
        }

        if (!apellidoPaterno.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarError("El apellido paterno solo puede contener letras, espacios y acentos");
            return false;
        }

        if (!apellidoMaterno.trim().isEmpty()) {
            if (apellidoMaterno.trim().length() < 2) {
                mostrarError("El apellido materno debe tener al menos 2 caracteres");
                return false;
            }

            if (apellidoMaterno.trim().length() > 100) { // *** FIX: Aumentado límite a 100 ***
                mostrarError("El apellido materno no puede exceder 100 caracteres");
                return false;
            }

            if (!apellidoMaterno.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                mostrarError("El apellido materno solo puede contener letras, espacios y acentos");
                return false;
            }
        }

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

        if (correo.trim().isEmpty()) {
            mostrarError("El correo electrónico es obligatorio");
            return false;
        }

        if (!validarFormatoCorreo(correo.trim())) {
            mostrarError("El formato del correo electrónico no es válido");
            return false;
        }

        if (!telefono.trim().isEmpty()) {
            if (!validarFormatoTelefonoMejorado(telefono.trim())) {
                return false; // El error ya se muestra en el método
            }
        }

        return true;
    }


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

    public boolean existeResidente(String numeroControl) {
        try {
            int numControl = Integer.parseInt(numeroControl.trim());
            return ModeloResidente.existe(numControl);
        } catch (NumberFormatException e) {
            return false; // Si no se puede convertir, asumir que no existe
        }
    }

    public boolean guardarResidenteDirectoEnBD(String numeroControl, String nombre, String apellidoPaterno,
                                               String apellidoMaterno, String carrera, String semestre,
                                               String correo, String telefono) {
        try {
            System.out.println("DEBUG: === INICIANDO GUARDADO MANUAL ===");
            System.out.println("DEBUG: Datos recibidos:");
            System.out.println("  - numeroControl: '" + numeroControl + "'");
            System.out.println("  - nombre: '" + nombre + "'");
            System.out.println("  - apellidoPaterno: '" + apellidoPaterno + "'");
            System.out.println("  - apellidoMaterno: '" + apellidoMaterno + "'");
            System.out.println("  - carrera: '" + carrera + "'");
            System.out.println("  - semestre: '" + semestre + "'");
            System.out.println("  - correo: '" + correo + "'");
            System.out.println("  - telefono: '" + telefono + "'");

            if (!validarCampos(numeroControl, nombre, apellidoPaterno, apellidoMaterno,
                    carrera, semestre, correo, telefono)) {
                System.out.println("DEBUG: Validación de campos falló");
                return false;
            }

            String numControlLimpio = numeroControl.trim().replaceAll("[\\s-]", "");

            // *** FIX: Verificar existencia usando String ***
            if (existeResidente(numControlLimpio)) {
                mostrarError("Ya existe un residente con este número de control");
                return false;
            }

            // *** FIX: Verificar duplicado por correo ***
            if (existeCorreo(correo.trim())) {
                mostrarError("Ya existe un residente con este correo electrónico");
                return false;
            }

            ModeloResidente residente = new ModeloResidente();

            // Asignar datos con validación estricta ***
            try {
                int numControl = Integer.parseInt(numControlLimpio);
                residente.setNumeroControl(numControl);
                System.out.println("DEBUG: Número de control convertido: " + numControl);
            } catch (NumberFormatException e) {
                mostrarError("Error al procesar el número de control: " + e.getMessage());
                return false;
            }

            try {
                int sem = Integer.parseInt(semestre.trim());
                residente.setSemestre(sem);
                System.out.println("DEBUG: Semestre convertido: " + sem);
            } catch (NumberFormatException e) {
                mostrarError("Error al procesar el semestre: " + e.getMessage());
                return false;
            }

            // Asignar campos de texto
            residente.setNombre(formatearTexto(nombre.trim()));
            residente.setApellidoPaterno(formatearTexto(apellidoPaterno.trim()));
            residente.setApellidoMaterno(apellidoMaterno.trim().isEmpty() ? null : formatearTexto(apellidoMaterno.trim()));
            residente.setCarrera(carrera.trim());
            residente.setCorreo(correo.trim().toLowerCase());
            residente.setTelefono(telefono.trim().isEmpty() ? null : telefono.trim());
            residente.setIdProyecto(1); // Proyecto por defecto
            residente.setEstatus(true); // Activo

            System.out.println("DEBUG: Objeto residente creado:");
            System.out.println("  - numeroControl: " + residente.getNumeroControl());
            System.out.println("  - nombre: " + residente.getNombre());
            System.out.println("  - apellidoPaterno: " + residente.getApellidoPaterno());
            System.out.println("  - apellidoMaterno: " + residente.getApellidoMaterno());
            System.out.println("  - carrera: " + residente.getCarrera());
            System.out.println("  - semestre: " + residente.getSemestre());
            System.out.println("  - correo: " + residente.getCorreo());
            System.out.println("  - telefono: " + residente.getTelefono());
            System.out.println("  - idProyecto: " + residente.getIdProyecto());
            System.out.println("  - estatus: " + residente.isEstatus());

            System.out.println("DEBUG: Llamando a guardarEnBaseDatos()...");
            boolean guardado = residente.guardarEnBaseDatos();
            System.out.println("DEBUG: Resultado de guardarEnBaseDatos(): " + guardado);

            if (guardado) {
                System.out.println("DEBUG: ✅ Guardado exitoso - mostrando mensaje de éxito");
                JOptionPane.showMessageDialog(vista,
                        "✅ Residente guardado exitosamente\n" +
                                "📝 Número de Control: " + numControlLimpio + "\n" +
                                "👤 Nombre: " + residente.getNombre() + " " + residente.getApellidoPaterno(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                System.out.println("DEBUG: ❌ guardarEnBaseDatos() retornó false");

                // *** FIX: Verificar si realmente se guardó en la BD con delay ***
                try {
                    Thread.sleep(100); // Pequeño delay para asegurar consistencia
                } catch (InterruptedException ignored) {}

                ModeloResidente verificacion = ModeloResidente.buscarPorNumeroControl(residente.getNumeroControl());
                if (verificacion != null) {
                    System.out.println("DEBUG: ✅ PARADOJA CONFIRMADA: El registro SÍ existe en BD");
                    System.out.println("DEBUG: Registro encontrado - ID: " + verificacion.getIdResidente() +
                            ", Nombre: " + verificacion.getNombre());

                    JOptionPane.showMessageDialog(vista,
                            "✅ Residente guardado exitosamente\n" +
                                    "📝 Número de Control: " + numControlLimpio + "\n" +
                                    "👤 Nombre: " + residente.getNombre() + " " + residente.getApellidoPaterno() + "\n\n" +
                                    "ℹ️ Nota técnica: Problema menor en la confirmación del guardado,\n" +
                                    "pero el registro se guardó correctamente en la base de datos.",
                            "Guardado Exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    System.out.println("DEBUG: ❌ CONFIRMADO: El registro NO existe en BD");
                    mostrarError("❌ Error al guardar en la base de datos\n" +
                            "El registro no se encontró después del intento de guardado.");
                    return false;
                }
            }

        } catch (NumberFormatException e) {
            System.err.println("DEBUG: Error de formato numérico: " + e.getMessage());
            mostrarError("Error en el formato de los números: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("DEBUG: Error inesperado: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error inesperado: " + e.getMessage());
            return false;
        }
    }

    private boolean existeCorreo(String correo) {
        try {
            // Buscar en todos los residentes activos
            java.util.List<ModeloResidente> residentes = ModeloResidente.obtenerTodos();
            for (ModeloResidente residente : residentes) {
                if (residente.getCorreo() != null &&
                        residente.getCorreo().equalsIgnoreCase(correo.trim())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
            return false; // En caso de error, permitir continuar
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

            if (existeResidente(numControlLimpio)) {
                mostrarError("Ya existe un residente con este número de control");
                return false;
            }

            return true;
        } catch (Exception e) {
            mostrarError("Error al validar número de control: " + e.getMessage());
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

        if (numControlLimpio.length() > 9) {
            mostrarError("El número de control no puede tener más de 9 dígitos");
            return false;
        }

        if (numControlLimpio.length() < 6) {
            mostrarError("El número de control debe tener al menos 6 dígitos");
            return false;
        }

        try {
            if (numControlLimpio.length() == 8) {
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
            }

            return true;
        } catch (NumberFormatException e) {
            mostrarError("Error al procesar el número de control");
            return false;
        }
    }

    public static boolean procesarAgregarResidente(Frame parent) {
        AgregarManual vista = new AgregarManual(parent);
        vista.setVisible(true);
        return vista.isGuardado();
    }

    public boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return true; // Teléfono es opcional
        }
        return validarFormatoTelefonoMejorado(telefono.trim());
    }

    public String[] getSugerenciasTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return new String[0];
        }

        java.util.List<String> sugerencias = new java.util.ArrayList<>();
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");

        if (telefonoLimpio.length() == 10) {
            sugerencias.add("Formato sugerido: " +
                    telefonoLimpio.substring(0, 3) + "-" +
                    telefonoLimpio.substring(3, 6) + "-" +
                    telefonoLimpio.substring(6));
        }

        if (telefonoLimpio.length() < 8) {
            sugerencias.add("Agregue más dígitos (mínimo 8)");
        }

        if (telefonoLimpio.length() > 15) {
            sugerencias.add("Retire dígitos extras (máximo 15)");
        }

        return sugerencias.toArray(new String[0]);
    }
}