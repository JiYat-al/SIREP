package Modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * Clase para validar los datos del formulario de anteproyecto
 * Con validaciones en tiempo real que previenen errores inmediatamente
 */
public class ValidadorAnteproyecto {

    /**
     * Valida si se puede agregar un alumno a la lista
     * @param nuevoAlumno El alumno que se quiere agregar
     * @param modeloAlumnos Lista actual de alumnos
     * @param proyecto Proyecto seleccionado (para verificar límites)
     * @return true si se puede agregar, false si no
     */
    public static boolean validarAgregarAlumno(ModeloResidente nuevoAlumno,
                                               DefaultListModel<ModeloResidente> modeloAlumnos,
                                               Proyecto proyecto) {

        // Verificar si el alumno ya está en la lista
        for (int i = 0; i < modeloAlumnos.getSize(); i++) {
            ModeloResidente existente = modeloAlumnos.getElementAt(i);
            if (existente.getNumeroControl() == nuevoAlumno.getNumeroControl()) {
                JOptionPane.showMessageDialog(null,
                        "El alumno " + nuevoAlumno.getNumeroControl() + " - " +
                                nuevoAlumno.getNombre() + " ya está asignado al proyecto.",
                        "Alumno Duplicado",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Verificar límite máximo de alumnos del proyecto
        if (proyecto != null && proyecto.getNumero_alumnos() > 0) {
            if (modeloAlumnos.getSize() >= proyecto.getNumero_alumnos()) {
                JOptionPane.showMessageDialog(null,
                        "El proyecto permite máximo " + proyecto.getNumero_alumnos() +
                                " alumnos.\nYa tiene " + modeloAlumnos.getSize() + " asignados.",
                        "Límite de Alumnos Excedido",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Verificar límite general (máximo 3 alumnos por proyecto)
        if (modeloAlumnos.getSize() >= 3) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "Ya tiene " + modeloAlumnos.getSize() + " alumnos asignados.\n" +
                            "Se recomienda máximo 3 alumnos por proyecto.\n" +
                            "¿Desea continuar agregando este alumno?",
                    "Advertencia - Muchos Alumnos",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            return respuesta == JOptionPane.YES_OPTION;
        }

        return true; // Todo válido, se puede agregar
    }

    /**
     * Valida si se puede agregar un asesor
     * @param nuevoAsesor El asesor que se quiere agregar
     * @param modeloAsesores Lista actual de asesores
     * @return true si se puede agregar, false si no
     */
    public static boolean validarAgregarAsesor(Docente nuevoAsesor,
                                               DefaultListModel<Docente> modeloAsesores) {

        // Solo se permite un asesor
        if (modeloAsesores.getSize() >= 1) {
            JOptionPane.showMessageDialog(null,
                    "Solo se permite un asesor por proyecto.\n" +
                            "Elimine el asesor actual para asignar uno nuevo.",
                    "Solo Un Asesor Permitido",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Valida si se puede agregar un revisor de anteproyecto
     * @param nuevoRevisor El revisor que se quiere agregar
     * @param modeloRevisorAnteproyecto Lista actual de revisores de anteproyecto
     * @return true si se puede agregar, false si no
     */
    public static boolean validarAgregarRevisorAnteproyecto(Docente nuevoRevisor,
                                                            DefaultListModel<Docente> modeloRevisorAnteproyecto) {

        // Solo se permite un revisor de anteproyecto
        if (modeloRevisorAnteproyecto.getSize() >= 1) {
            JOptionPane.showMessageDialog(null,
                    "Solo se permite un revisor de anteproyecto.\n" +
                            "Elimine el revisor actual para asignar uno nuevo.",
                    "Solo Un Revisor de Anteproyecto Permitido",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Valida si se puede agregar un revisor
     * @param nuevoRevisor El revisor que se quiere agregar
     * @param modeloRevisores Lista actual de revisores
     * @param modeloAsesores Lista de asesores (para evitar duplicados)
     * @param modeloRevisorAnteproyecto Lista de revisor de anteproyecto (para evitar duplicados)
     * @return true si se puede agregar, false si no
     */
    public static boolean validarAgregarRevisor(Docente nuevoRevisor,
                                                DefaultListModel<Docente> modeloRevisores,
                                                DefaultListModel<Docente> modeloAsesores,
                                                DefaultListModel<Docente> modeloRevisorAnteproyecto) {

        // Verificar si ya está como asesor
        for (int i = 0; i < modeloAsesores.getSize(); i++) {
            if (modeloAsesores.getElementAt(i).getNumeroTarjeta() == nuevoRevisor.getNumeroTarjeta()) {
                JOptionPane.showMessageDialog(null,
                        "El docente " + nuevoRevisor.getNombre() +
                                " ya está asignado como asesor.\n" +
                                "Un docente no puede tener múltiples roles en el mismo proyecto.",
                        "Conflicto de Roles",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Verificar si ya está como revisor de anteproyecto
        for (int i = 0; i < modeloRevisorAnteproyecto.getSize(); i++) {
            if (modeloRevisorAnteproyecto.getElementAt(i).getNumeroTarjeta() == nuevoRevisor.getNumeroTarjeta()) {
                JOptionPane.showMessageDialog(null,
                        "El docente " + nuevoRevisor.getNombre() +
                                " ya está asignado como revisor de anteproyecto.\n" +
                                "Un docente no puede tener múltiples roles en el mismo proyecto.",
                        "Conflicto de Roles",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Verificar si ya está como revisor
        for (int i = 0; i < modeloRevisores.getSize(); i++) {
            if (modeloRevisores.getElementAt(i).getNumeroTarjeta() == nuevoRevisor.getNumeroTarjeta()) {
                JOptionPane.showMessageDialog(null,
                        "El docente " + nuevoRevisor.getNombre() +
                                " ya está asignado como revisor.",
                        "Revisor Duplicado",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // Verificar límite máximo de revisores (recomendado máximo 3)
        if (modeloRevisores.getSize() >= 3) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "Ya tiene " + modeloRevisores.getSize() + " revisores asignados.\n" +
                            "Se recomienda máximo 3 revisores por proyecto.\n" +
                            "¿Desea continuar agregando este revisor?",
                    "Advertencia - Muchos Revisores",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            return respuesta == JOptionPane.YES_OPTION;
        }

        return true;
    }

    /**
     * Valida que un archivo tenga el formato correcto (OPCIONAL)
     * @param archivo El archivo seleccionado
     * @return true si es válido o null, false si es inválido
     */
    public static boolean validarArchivo(File archivo) {
        if (archivo == null) {
            return true; // Archivo es opcional, null es válido
        }

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null,
                    "El archivo seleccionado no existe o no es accesible.",
                    "Archivo No Válido",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar extensión
        String nombre = archivo.getName().toLowerCase();
        if (!nombre.endsWith(".pdf") && !nombre.endsWith(".doc") && !nombre.endsWith(".docx")) {
            JOptionPane.showMessageDialog(null,
                    "El archivo debe ser un documento PDF, DOC o DOCX.\n" +
                            "Archivo seleccionado: " + archivo.getName(),
                    "Formato de Archivo No Válido",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar tamaño (máximo 10MB)
        long tamaño = archivo.length();
        long maxTamaño = 10 * 1024 * 1024; // 10MB
        if (tamaño > maxTamaño) {
            double tamañoMB = tamaño / (1024.0 * 1024.0);
            JOptionPane.showMessageDialog(null,
                    String.format("El archivo es demasiado grande: %.1f MB\n" +
                            "Tamaño máximo permitido: 10 MB", tamañoMB),
                    "Archivo Demasiado Grande",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (tamaño == 0) {
            JOptionPane.showMessageDialog(null,
                    "El archivo está vacío.",
                    "Archivo Vacío",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Valida que las fechas sean coherentes al momento de cambiarlas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return true si son válidas, false si no
     */
    public static boolean validarFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return true; // No validar si alguna es null (se valida al guardar)
        }

        if (fechaInicio.after(fechaFin)) {
            JOptionPane.showMessageDialog(null,
                    "La fecha de inicio no puede ser posterior a la fecha de finalización.\n" +
                            "Fecha inicio: " + fechaInicio + "\n" +
                            "Fecha fin: " + fechaFin,
                    "Fechas Incoherentes",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (fechaInicio.equals(fechaFin)) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "Las fechas de inicio y fin son iguales.\n" +
                            "¿Es esto correcto?",
                    "Fechas Iguales",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            return respuesta == JOptionPane.YES_OPTION;
        }

        // Validar duración mínima (menos de 7 días)
        long duracionDias = (fechaFin.getTime() - fechaInicio.getTime()) / (1000 * 60 * 60 * 24);
        if (duracionDias < 7) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "El proyecto tiene una duración muy corta: " + duracionDias + " días.\n" +
                            "¿Desea continuar?",
                    "Duración Muy Corta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            return respuesta == JOptionPane.YES_OPTION;
        }

        return true;
    }

    /**
     * Validación final al momento de guardar - verifica que todo esté completo
     * El archivo es OPCIONAL
     * @return true si se puede guardar, false si faltan datos obligatorios
     */
    public static boolean validarFormularioCompleto(
            Proyecto proyecto,
            File archivoSeleccionado,
            DefaultListModel<ModeloResidente> alumnos,
            DefaultListModel<Docente> asesores,
            DefaultListModel<Docente> revisores,
            DefaultListModel<Docente> revisorAnteproyecto,
            Date fechaInicio,
            Date fechaFin,
            String periodo) {

        List<String> erroresFaltantes = new ArrayList<>();

        // Validar campos obligatorios
        if (proyecto == null) {
            erroresFaltantes.add("Debe seleccionar o crear un proyecto");
        }

        // ARCHIVO ES OPCIONAL - No se valida como obligatorio
        // if (archivoSeleccionado == null) {
        //     erroresFaltantes.add("Debe seleccionar un archivo del anteproyecto");
        // }

        if (alumnos.getSize() == 0) {
            erroresFaltantes.add("Debe asignar al menos un alumno al proyecto");
        }

        if (asesores.getSize() == 0) {
            erroresFaltantes.add("Debe asignar un asesor al proyecto");
        }

        if (revisorAnteproyecto.getSize() == 0) {
            erroresFaltantes.add("Debe asignar un revisor del anteproyecto");
        }

        if (fechaInicio == null) {
            erroresFaltantes.add("Debe especificar la fecha de inicio");
        }

        if (fechaFin == null) {
            erroresFaltantes.add("Debe especificar la fecha de finalización");
        }

        if (periodo == null || periodo.trim().isEmpty()) {
            erroresFaltantes.add("Debe seleccionar un periodo académico");
        }

        // Validar que las fechas sean coherentes si ambas están presentes
        if (fechaInicio != null && fechaFin != null) {
            if (fechaInicio.after(fechaFin)) {
                erroresFaltantes.add("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
        }

        // Si hay errores, mostrarlos y retornar false
        if (!erroresFaltantes.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Complete los siguientes campos obligatorios:\n\n");

            for (int i = 0; i < erroresFaltantes.size(); i++) {
                mensaje.append("• ").append(erroresFaltantes.get(i)).append("\n");
            }

            // Mostrar nota sobre archivo opcional
            if (archivoSeleccionado == null) {
                mensaje.append("\nNota: El archivo del anteproyecto es opcional y se puede agregar más tarde.");
            }

            JOptionPane.showMessageDialog(null,
                    mensaje.toString(),
                    "Campos Obligatorios Faltantes",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;
    }

    /**
     * Método para mostrar información cuando se selecciona un proyecto del banco
     * @param proyecto El proyecto seleccionado
     */
    public static void mostrarInfoProyecto(Proyecto proyecto) {
        if (proyecto != null && proyecto.getNumero_alumnos() > 0) {
            JOptionPane.showMessageDialog(null,
                    "Proyecto seleccionado: " + proyecto.getNombre() + "\n" +
                            "Número máximo de alumnos: " + proyecto.getNumero_alumnos(),
                    "Información del Proyecto",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Valida si se puede eliminar un elemento de una lista
     * @param tipoElemento Tipo de elemento (ej: "alumno", "asesor")
     * @param cantidadSeleccionada Cantidad de elementos seleccionados
     * @param cantidadTotal Cantidad total en la lista
     * @return true si se puede eliminar, false si no
     */
    public static boolean validarEliminar(String tipoElemento, int cantidadSeleccionada, int cantidadTotal) {
        if (cantidadSeleccionada == 0) {
            JOptionPane.showMessageDialog(null,
                    "Por favor seleccione al menos un " + tipoElemento + " para eliminar.",
                    "Selección Requerida",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // Advertir si se van a eliminar todos los elementos
        if (cantidadSeleccionada == cantidadTotal && cantidadTotal > 1) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de eliminar TODOS los " + tipoElemento + "s (" + cantidadTotal + ")?\n" +
                            "Esto dejará el proyecto sin " + tipoElemento + "s asignados.",
                    "Eliminar Todos",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            return respuesta == JOptionPane.YES_OPTION;
        }

        // Confirmación normal
        String mensaje = cantidadSeleccionada == 1 ?
                "¿Está seguro de eliminar el " + tipoElemento + " seleccionado?" :
                "¿Está seguro de eliminar los " + cantidadSeleccionada + " " + tipoElemento + "s seleccionados?";

        int respuesta = JOptionPane.showConfirmDialog(null,
                mensaje,
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return respuesta == JOptionPane.YES_OPTION;
    }

    /**
     * Valida el periodo académico con las fechas
     * @param periodo Periodo seleccionado
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return true si es coherente, false si no
     */
    @SuppressWarnings("deprecation")
    public static boolean validarPeriodoConFechas(String periodo, Date fechaInicio, Date fechaFin) {
        if (periodo == null || fechaInicio == null || fechaFin == null) {
            return true; // No validar si falta información
        }

        int mesInicio = fechaInicio.getMonth() + 1; // getMonth() devuelve 0-11
        int mesFin = fechaFin.getMonth() + 1;

        boolean advertir = false;
        String mensajeAdvertencia = "";

        switch (periodo) {
            case "ENERO_JUNIO":
                if (mesInicio < 1 || mesInicio > 6 || mesFin < 1 || mesFin > 6) {
                    advertir = true;
                    mensajeAdvertencia = "El periodo ENERO-JUNIO generalmente abarca de enero a junio.\n" +
                            "Las fechas seleccionadas están fuera de este rango.";
                }
                break;

            case "JULIO_AGOSTO":
                if (mesInicio < 7 || mesInicio > 8 || mesFin < 7 || mesFin > 8) {
                    advertir = true;
                    mensajeAdvertencia = "El periodo JULIO-AGOSTO generalmente abarca de julio a agosto.\n" +
                            "Las fechas seleccionadas están fuera de este rango.";
                }
                break;

            case "AGOSTO_DICIEMBRE":
                if (mesInicio < 8 || mesInicio > 12 || mesFin < 8 || mesFin > 12) {
                    advertir = true;
                    mensajeAdvertencia = "El periodo AGOSTO-DICIEMBRE generalmente abarca de agosto a diciembre.\n" +
                            "Las fechas seleccionadas están fuera de este rango.";
                }
                break;
        }

        if (advertir) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    mensajeAdvertencia + "\n\n¿Desea continuar?",
                    "Inconsistencia Periodo-Fechas",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            return respuesta == JOptionPane.YES_OPTION;
        }

        return true;
    }
}