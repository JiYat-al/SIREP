package Modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 * Clase para validar los datos del formulario de anteproyecto
 */
public class ValidadorAnteproyecto {

    private List<String> errores;
    private List<String> advertencias;

    public ValidadorAnteproyecto() {
        this.errores = new ArrayList<>();
        this.advertencias = new ArrayList<>();
    }

    /**
     * Valida todos los campos del formulario de anteproyecto
     * @return true si todas las validaciones pasan, false si hay errores
     */
    public boolean validarFormularioCompleto(
            Proyecto proyecto,
            File archivoSeleccionado,
            DefaultListModel<ModeloResidente> alumnos,
            DefaultListModel<Docente> asesores,
            DefaultListModel<Docente> revisores,
            DefaultListModel<Docente> revisorAnteproyecto,
            Date fechaInicio,
            Date fechaFin,
            String periodo) {

        // Limpiar errores previos
        errores.clear();
        advertencias.clear();

        // Validaciones esenciales del formulario
        validarProyectoSeleccionado(proyecto);
        validarArchivoRequerido(archivoSeleccionado);
        validarAlumnosAsignados(alumnos, proyecto);
        validarAsesorAsignado(asesores);
        validarRevisorAnteproyectoAsignado(revisorAnteproyecto);
        validarFechasCoherentes(fechaInicio, fechaFin);
        validarPeriodoSeleccionado(periodo);

        // Validaciones de lógica de negocio
        validarConflictosDocentes(asesores, revisores, revisorAnteproyecto);
        validarCompatibilidadFechasPeriodo(fechaInicio, fechaFin, periodo);

        return errores.isEmpty();
    }

    /**
     * Valida que se haya seleccionado un proyecto
     */
    private void validarProyectoSeleccionado(Proyecto proyecto) {
        if (proyecto == null) {
            errores.add("Debe seleccionar un proyecto del banco o crear uno nuevo.");
            return;
        }

        if (proyecto.getId_proyecto() <= 0) {
            errores.add("El proyecto seleccionado no tiene un ID válido.");
        }
    }

    /**
     * Valida que se haya seleccionado un archivo
     */
    private void validarArchivoRequerido(File archivo) {
        if (archivo == null) {
            errores.add("Debe seleccionar un archivo del anteproyecto.");
            return;
        }

        if (!archivo.exists()) {
            errores.add("El archivo seleccionado no existe o no es accesible.");
            return;
        }

        // Validar extensión
        String nombre = archivo.getName().toLowerCase();
        if (!nombre.endsWith(".pdf") && !nombre.endsWith(".doc") && !nombre.endsWith(".docx")) {
            errores.add("El archivo debe ser un documento PDF, DOC o DOCX.");
        }

        // Validar tamaño (máximo 10MB)
        long tamaño = archivo.length();
        long maxTamaño = 10 * 1024 * 1024; // 10MB
        if (tamaño > maxTamaño) {
            errores.add("El archivo es demasiado grande. Máximo permitido: 10MB.");
        }

        if (tamaño == 0) {
            errores.add("El archivo está vacío.");
        }
    }

    /**
     * Valida los alumnos asignados al proyecto
     */
    private void validarAlumnosAsignados(DefaultListModel<ModeloResidente> alumnos, Proyecto proyecto) {
        if (alumnos == null || alumnos.getSize() == 0) {
            errores.add("Debe asignar al menos un alumno al anteproyecto.");
            return;
        }

        // Validar límite de alumnos según el proyecto
        if (proyecto != null && proyecto.getNumero_alumnos() > 0) {
            if (alumnos.getSize() > proyecto.getNumero_alumnos()) {
                errores.add("El proyecto permite máximo " + proyecto.getNumero_alumnos() +
                        " alumnos. Actualmente tiene " + alumnos.getSize() + " asignados.");
            }
        }

        // Validar máximo general (por si el proyecto no especifica)
        if (alumnos.getSize() > 3) {
            advertencias.add("Se recomienda no asignar más de 3 alumnos por proyecto.");
        }

        // Verificar que no haya duplicados
        List<Integer> numerosControl = new ArrayList<>();
        for (int i = 0; i < alumnos.getSize(); i++) {
            ModeloResidente residente = alumnos.getElementAt(i);
            if (numerosControl.contains(residente.getNumeroControl())) {
                errores.add("El alumno " + residente.getNumeroControl() +
                        " está duplicado en la lista.");
            } else {
                numerosControl.add(residente.getNumeroControl());
            }
        }
    }

    /**
     * Valida que se haya asignado exactamente un asesor
     */
    private void validarAsesorAsignado(DefaultListModel<Docente> asesores) {
        if (asesores == null || asesores.getSize() == 0) {
            errores.add("Debe asignar un asesor al anteproyecto.");
            return;
        }

        if (asesores.getSize() > 1) {
            errores.add("Solo puede asignar un asesor al anteproyecto.");
        }
    }

    /**
     * Valida que se haya asignado exactamente un revisor de anteproyecto
     */
    private void validarRevisorAnteproyectoAsignado(DefaultListModel<Docente> revisorAnteproyecto) {
        if (revisorAnteproyecto == null || revisorAnteproyecto.getSize() == 0) {
            errores.add("Debe asignar un revisor del anteproyecto.");
            return;
        }

        if (revisorAnteproyecto.getSize() > 1) {
            errores.add("Solo puede asignar un revisor del anteproyecto.");
        }
    }

    /**
     * Valida que las fechas sean coherentes
     */
    private void validarFechasCoherentes(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null) {
            errores.add("Debe especificar la fecha de inicio del proyecto.");
            return;
        }

        if (fechaFin == null) {
            errores.add("Debe especificar la fecha de finalización del proyecto.");
            return;
        }

        if (fechaInicio.after(fechaFin)) {
            errores.add("La fecha de inicio no puede ser posterior a la fecha de finalización.");
        }

        if (fechaInicio.equals(fechaFin)) {
            advertencias.add("Las fechas de inicio y fin son iguales. Verifique si es correcto.");
        }

        // Validar que no sean fechas muy antiguas
        Date hoy = new Date();
        long diferenciaDias = (hoy.getTime() - fechaInicio.getTime()) / (1000 * 60 * 60 * 24);
        if (diferenciaDias > 365) {
            advertencias.add("La fecha de inicio es de hace más de un año. Verifique si es correcta.");
        }

        // Validar duración mínima
        long duracionDias = (fechaFin.getTime() - fechaInicio.getTime()) / (1000 * 60 * 60 * 24);
        if (duracionDias < 30) {
            advertencias.add("El proyecto tiene una duración muy corta (menos de 30 días).");
        }

        if (duracionDias > 365) {
            advertencias.add("El proyecto tiene una duración muy larga (más de un año).");
        }
    }

    /**
     * Valida que se haya seleccionado un periodo
     */
    private void validarPeriodoSeleccionado(String periodo) {
        if (periodo == null || periodo.trim().isEmpty()) {
            errores.add("Debe seleccionar un periodo académico.");
            return;
        }

        List<String> periodosValidos = List.of("ENERO_JUNIO", "JULIO_AGOSTO", "AGOSTO_DICIEMBRE");
        if (!periodosValidos.contains(periodo)) {
            errores.add("El periodo seleccionado no es válido.");
        }
    }

    /**
     * Valida que no haya conflictos entre docentes
     */
    private void validarConflictosDocentes(DefaultListModel<Docente> asesores,
                                           DefaultListModel<Docente> revisores,
                                           DefaultListModel<Docente> revisorAnteproyecto) {

        List<Integer> tarjetasUsadas = new ArrayList<>();

        // Agregar asesor
        if (asesores != null && asesores.getSize() > 0) {
            tarjetasUsadas.add(asesores.getElementAt(0).getNumeroTarjeta());
        }

        // Verificar revisor de anteproyecto
        if (revisorAnteproyecto != null && revisorAnteproyecto.getSize() > 0) {
            int tarjetaRevisor = revisorAnteproyecto.getElementAt(0).getNumeroTarjeta();
            if (tarjetasUsadas.contains(tarjetaRevisor)) {
                errores.add("El revisor del anteproyecto no puede ser el mismo que el asesor.");
            } else {
                tarjetasUsadas.add(tarjetaRevisor);
            }
        }

        // Verificar revisores
        if (revisores != null) {
            for (int i = 0; i < revisores.getSize(); i++) {
                int tarjetaRevisor = revisores.getElementAt(i).getNumeroTarjeta();
                if (tarjetasUsadas.contains(tarjetaRevisor)) {
                    errores.add("Un docente no puede tener múltiples roles en el mismo proyecto.");
                    break;
                } else {
                    tarjetasUsadas.add(tarjetaRevisor);
                }
            }
        }
    }

    /**
     * Valida la compatibilidad entre fechas y periodo académico
     */
    private void validarCompatibilidadFechasPeriodo(Date fechaInicio, Date fechaFin, String periodo) {
        if (fechaInicio == null || fechaFin == null || periodo == null) {
            return; // Ya validado en otros métodos
        }

        // Obtener mes de inicio y fin
        @SuppressWarnings("deprecation")
        int mesInicio = fechaInicio.getMonth() + 1; // getMonth() devuelve 0-11
        @SuppressWarnings("deprecation")
        int mesFin = fechaFin.getMonth() + 1;

        switch (periodo) {
            case "ENERO_JUNIO":
                if (mesInicio < 1 || mesInicio > 6) {
                    advertencias.add("El periodo ENERO-JUNIO generalmente inicia entre enero y marzo.");
                }
                if (mesFin < 1 || mesFin > 6) {
                    advertencias.add("El periodo ENERO-JUNIO generalmente termina entre abril y junio.");
                }
                break;

            case "JULIO_AGOSTO":
                if (mesInicio < 7 || mesInicio > 8) {
                    advertencias.add("El periodo JULIO-AGOSTO generalmente inicia en julio.");
                }
                if (mesFin < 7 || mesFin > 8) {
                    advertencias.add("El periodo JULIO-AGOSTO generalmente termina en agosto.");
                }
                break;

            case "AGOSTO_DICIEMBRE":
                if (mesInicio < 8 || mesInicio > 12) {
                    advertencias.add("El periodo AGOSTO-DICIEMBRE generalmente inicia entre agosto y septiembre.");
                }
                if (mesFin < 8 || mesFin > 12) {
                    advertencias.add("El periodo AGOSTO-DICIEMBRE generalmente termina entre noviembre y diciembre.");
                }
                break;
        }
    }

    /**
     * Obtiene la lista de errores encontrados
     */
    public List<String> getErrores() {
        return new ArrayList<>(errores);
    }

    /**
     * Obtiene la lista de advertencias encontradas
     */
    public List<String> getAdvertencias() {
        return new ArrayList<>(advertencias);
    }

    /**
     * Verifica si hay errores
     */
    public boolean tieneErrores() {
        return !errores.isEmpty();
    }

    /**
     * Verifica si hay advertencias
     */
    public boolean tieneAdvertencias() {
        return !advertencias.isEmpty();
    }

    /**
     * Obtiene un resumen de todos los problemas encontrados
     */
    public String getResumenValidacion() {
        StringBuilder resumen = new StringBuilder();

        if (!errores.isEmpty()) {
            resumen.append("ERRORES:\n");
            for (String error : errores) {
                resumen.append("• ").append(error).append("\n");
            }
        }

        if (!advertencias.isEmpty()) {
            if (resumen.length() > 0) resumen.append("\n");
            resumen.append("ADVERTENCIAS:\n");
            for (String advertencia : advertencias) {
                resumen.append("• ").append(advertencia).append("\n");
            }
        }

        return resumen.toString();
    }
}