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
     * Validar todos los campos del formulario
     */
    public boolean validarCampos(String numeroControl, String nombre, String apellidoPaterno,
                                 String apellidoMaterno, String carrera, String semestre,
                                 String correo, String telefono) {

        // Validar número de control
        if (numeroControl.trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            return false;
        }

        try {
            int numControl = Integer.parseInt(numeroControl.trim());
            if (numControl <= 0) {
                mostrarError("El número de control debe ser un número positivo");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El número de control debe ser un número válido");
            return false;
        }

        // Validar nombre
        if (nombre.trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            return false;
        }

        if (nombre.trim().length() < 2) {
            mostrarError("El nombre debe tener al menos 2 caracteres");
            return false;
        }

        // Validar apellido paterno
        if (apellidoPaterno.trim().isEmpty()) {
            mostrarError("El apellido paterno es obligatorio");
            return false;
        }

        if (apellidoPaterno.trim().length() < 2) {
            mostrarError("El apellido paterno debe tener al menos 2 caracteres");
            return false;
        }

        // Validar carrera
        if (carrera.trim().isEmpty()) {
            mostrarError("La carrera es obligatoria");
            return false;
        }

        // Validar semestre
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

        // Validar correo
        if (correo.trim().isEmpty()) {
            mostrarError("El correo es obligatorio");
            return false;
        }

        if (!validarFormatoCorreo(correo.trim())) {
            mostrarError("El formato del correo no es válido\nEjemplo: usuario@dominio.com");
            return false;
        }

        // Validar teléfono (opcional, pero si se proporciona debe ser válido)
        if (!telefono.trim().isEmpty() && !validarFormatoTelefono(telefono.trim())) {
            mostrarError("El formato del teléfono no es válido\nDebe contener solo números y tener entre 8 y 15 dígitos");
            return false;
        }

        return true;
    }

    /**
     * Validar formato de correo electrónico
     */
    private boolean validarFormatoCorreo(String correo) {
        return correo.contains("@") &&
                correo.contains(".") &&
                correo.indexOf("@") > 0 &&
                correo.indexOf("@") < correo.lastIndexOf(".") &&
                correo.lastIndexOf(".") < correo.length() - 1;
    }

    /**
     * Validar formato de teléfono
     */
    private boolean validarFormatoTelefono(String telefono) {
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
        return telefonoLimpio.length() >= 8 && telefonoLimpio.length() <= 15;
    }

    /**
     * Mostrar mensaje de error
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista,
                mensaje,
                "Error de validación",
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

            // Verificar si ya existe el residente
            int numControl = Integer.parseInt(numeroControl.trim());
            if (existeResidente(numControl)) {
                mostrarError("❌ Ya existe un residente con el número de control: " + numControl);
                return false;
            }

            // Crear el modelo del residente
            ModeloResidente residente = new ModeloResidente();
            residente.setNumeroControl(numControl);
            residente.setNombre(nombre.trim());
            residente.setApellidoPaterno(apellidoPaterno.trim());
            residente.setApellidoMaterno(apellidoMaterno.trim().isEmpty() ? null : apellidoMaterno.trim());
            residente.setCarrera(carrera.trim());
            residente.setSemestre(Integer.parseInt(semestre.trim()));
            residente.setCorreo(correo.trim());
            residente.setTelefono(telefono.trim().isEmpty() ? null : telefono.trim());
            residente.setIdProyecto(1); // Proyecto por defecto

            // Confirmar guardado
            int opcion = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro de guardar este residente?\n" +
                            "Nombre: " + nombre.trim() + " " + apellidoPaterno.trim() + "\n" +
                            "Número de Control: " + numeroControl.trim(),
                    "Confirmar guardado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                // Cambiar cursor a espera
                vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Intentar guardar usando el modelo
                boolean guardado = residente.insertar();

                if (guardado) {
                    JOptionPane.showMessageDialog(vista,
                            "✅ Residente guardado exitosamente en la base de datos\n" +
                                    "📝 Número de Control: " + numeroControl.trim(),
                            "Guardado exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    mostrarErrorBD("No se pudo guardar el residente en la base de datos");
                    return false;
                }
            }

        } catch (NumberFormatException e) {
            mostrarError("Error en el formato de los números: " + e.getMessage());
            return false;
        } catch (Exception e) {
            mostrarErrorBD("Error inesperado al guardar: " + e.getMessage());
            return false;
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }

        return false;
    }

    /**
     * Mostrar mensaje de error de base de datos
     */
    private void mostrarErrorBD(String mensaje) {
        JOptionPane.showMessageDialog(vista,
                mensaje,
                "Error de Base de Datos",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Confirmar cancelación si hay cambios
     */
    public boolean confirmarCancelacion(boolean hayCambios) {
        if (hayCambios) {
            int opcion = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro de cancelar?\nSe perderán todos los datos ingresados.",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

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
     * Validar que el número de control sea único
     */
    public boolean validarNumeroControlUnico(String numeroControl) {
        try {
            int numControl = Integer.parseInt(numeroControl.trim());

            if (existeResidente(numControl)) {
                mostrarError("❌ Ya existe un residente con el número de control: " + numControl);
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            mostrarError("El número de control debe ser un número válido");
            return false;
        }
    }

    /**
     * Limpiar formulario
     */
    public void limpiarFormulario() {
        // Este método puede ser llamado desde la vista para limpiar los campos
        // La implementación específica dependería de cómo esté estructurada la vista
    }

    /**
     * Validar solo el número de control
     */
    public boolean validarNumeroControl(String numeroControl) {
        if (numeroControl.trim().isEmpty()) {
            mostrarError("El número de control es obligatorio");
            return false;
        }

        try {
            int numControl = Integer.parseInt(numeroControl.trim());
            if (numControl <= 0) {
                mostrarError("El número de control debe ser un número positivo");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            mostrarError("El número de control debe ser un número válido");
            return false;
        }
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