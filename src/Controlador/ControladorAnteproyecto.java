package Controlador;

import Modelo.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorAnteproyecto {

    ProyectoDAO proyectoDAO = new ProyectoDAO();

    public DefaultComboBoxModel<Proyecto> cargarComboProyectosBanco() {
        List<Proyecto> listaProyectos = new ArrayList<>();
        listaProyectos = proyectoDAO.ObtenerProyectosBanco();
        DefaultComboBoxModel<Proyecto> modeloCombo = new DefaultComboBoxModel<>();
        for (Proyecto p : listaProyectos) {
            modeloCombo.addElement(p);
        }
        return modeloCombo;
    }
    public static DefaultComboBoxModel<Empresa> cargarComboEmpresas() {
        ArrayList<Empresa> listaEmpresas = new ArrayList<>();
        listaEmpresas = CtrlEmpresa.obtenerEmpresas();
        DefaultComboBoxModel<Empresa> modeloCombo = new DefaultComboBoxModel<>();
        for (Empresa p : listaEmpresas) {
            modeloCombo.addElement(p);
        }
        return modeloCombo;
    }

    public static boolean registrarAnteproyecto(Proyecto proyecto, File archivoSeleccionado, DefaultListModel<ModeloResidente> alumnos, DefaultListModel<Docente> asesores, DefaultListModel<Docente> revisores, DefaultListModel<Docente> revisorAnteproyecto, JSpinner fecha_inicio, JSpinner fecha_fin, String periodo) {
        Anteproyecto anteproyecto = new Anteproyecto();

        anteproyecto.setProyecto(proyecto);

        if (archivoSeleccionado != null) {
            try {
                String carpetaDestino = "archivos_anteproyectos";
                File carpeta = new File(carpetaDestino);
                if (!carpeta.exists()) carpeta.mkdirs(); // crear si no existe

                // Evitar sobrescritura (opcional)
                String nombreArchivo = archivoSeleccionado.getName();
                File destino = new File(carpeta, nombreArchivo);

                // Copiar
                Files.copy(archivoSeleccionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Guardar ruta relativa
                anteproyecto.setArchivoAnteproyecto(carpetaDestino + "/" + nombreArchivo);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + ex.getMessage());
            }
        }

        ArrayList<ModeloResidente> residentes = new ArrayList<>();

        for (int i = 0; i < alumnos.getSize(); i++)
        {
            residentes.add(alumnos.getElementAt(i));
        }

        anteproyecto.setResidentes(residentes);

        Docente asesor = new Docente();

        if (asesores.getSize() > 0)
        {
            asesor = asesores.getElementAt(0);
        }

        anteproyecto.setAsesor(asesor);

        ArrayList<Docente> docentesRevisores = new ArrayList<>();

        for (int i = 0; i < revisores.getSize(); i++)
        {
            docentesRevisores.add(revisores.getElementAt(i));
        }

        anteproyecto.setRevisores(docentesRevisores);

        Docente revisorAnt = new Docente();

        if (revisorAnteproyecto.getSize() > 0) {
            revisorAnt = revisorAnteproyecto.getElementAt(0);
        }

        anteproyecto.setRevisorAnteproyecto(revisorAnt);

        Date fechaInicioValue = (Date) fecha_inicio.getValue();
        Date fechaFinalValue = (Date) fecha_fin.getValue();

        anteproyecto.setFechaInicio(fechaInicioValue);
        anteproyecto.setFechaFin(fechaFinalValue);

        anteproyecto.setPeriodo(Periodo.valueOf(periodo));

        //Llamada a clasesDAO
        AnteproyectoDAO.registrar(anteproyecto);
        ModeloResidente.asigancionAlumnosProyecto(anteproyecto);
        DocenteDAO.asignarAsesorProyecto(anteproyecto);
        DocenteDAO.asginarRevisoresProyecto(anteproyecto);
        DocenteDAO.asignarRevisorAnteproyecto(anteproyecto);

        return true;

    }

    public static boolean editarAnteproyecto(Proyecto proyecto, File archivoSeleccionado, DefaultListModel<ModeloResidente> alumnos, DefaultListModel<Docente> asesores, DefaultListModel<Docente> revisores, DefaultListModel<Docente> revisorAnteproyecto, JSpinner fecha_inicio, JSpinner fecha_fin, String periodo) {
        Anteproyecto anteproyecto = new Anteproyecto();

        anteproyecto.setProyecto(proyecto);

        if (archivoSeleccionado != null) {
            try {
                String carpetaDestino = "archivos_anteproyectos";
                File carpeta = new File(carpetaDestino);
                if (!carpeta.exists()) carpeta.mkdirs(); // crear si no existe

                // Evitar sobrescritura (opcional)
                String nombreArchivo = archivoSeleccionado.getName();
                File destino = new File(carpeta, nombreArchivo);

                // Copiar
                Files.copy(archivoSeleccionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Guardar ruta relativa
                anteproyecto.setArchivoAnteproyecto(carpetaDestino + "/" + nombreArchivo);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + ex.getMessage());
            }
        }

        ArrayList<ModeloResidente> residentes = new ArrayList<>();

        for (int i = 0; i < alumnos.getSize(); i++)
        {
            residentes.add(alumnos.getElementAt(i));
        }

        anteproyecto.setResidentes(residentes);

        Docente asesor = new Docente();

        if (asesores.getSize() > 0)
        {
            asesor = asesores.getElementAt(0);
        }

        anteproyecto.setAsesor(asesor);

        ArrayList<Docente> docentesRevisores = new ArrayList<>();

        for (int i = 0; i < revisores.getSize(); i++)
        {
            docentesRevisores.add(revisores.getElementAt(i));
        }

        anteproyecto.setRevisores(docentesRevisores);

        Docente revisorAnt = new Docente();

        if (revisorAnteproyecto.getSize() > 0) {
            revisorAnt = revisorAnteproyecto.getElementAt(0);
        }

        anteproyecto.setRevisorAnteproyecto(revisorAnt);

        Date fechaInicioValue = (Date) fecha_inicio.getValue();
        Date fechaFinalValue = (Date) fecha_fin.getValue();

        anteproyecto.setFechaInicio(fechaInicioValue);
        anteproyecto.setFechaFin(fechaFinalValue);

        anteproyecto.setPeriodo(Periodo.valueOf(periodo));

        //Llamada a clasesDAO
        AnteproyectoDAO.registrar(anteproyecto);
        ModeloResidente.asigancionAlumnosProyecto(anteproyecto);
        DocenteDAO.asignarAsesorProyecto(anteproyecto);
        DocenteDAO.asginarRevisoresProyecto(anteproyecto);
        DocenteDAO.asignarRevisorAnteproyecto(anteproyecto);

        return true;

    }
}
