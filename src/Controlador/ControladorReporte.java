package Controlador;

import Modelo.*;
import Modelo.ReporteDocenteAR;
import Util.ReportesExcel.GeneradorReporteAlumnoExcel;
import Util.ReportesExcel.GeneradorReporteExcelAsesor;


import javax.swing.*;
import java.util.List;

public class ControladorReporte {

    private ReportesDAO reportesDAO;
    private GeneradorReporteExcelAsesor gen;
    private GeneradorReporteAlumnoExcel gen2;

    public ControladorReporte() {
        this.reportesDAO = new ReportesDAO();
        this.gen = new GeneradorReporteExcelAsesor();
    }

    public ControladorReporte(ReportesDAO reportesDAO, GeneradorReporteExcelAsesor generador) {
        this.reportesDAO = reportesDAO;
        this.gen = generador;

    }

   public  List<Docente> obtenerAsesores(){
        List<Docente> docentes = reportesDAO.obtenerAsesores();
        return docentes;
   }
    public  List<Docente> obtenerRevisores(){
        List<Docente> docentes = reportesDAO.obtenerRevisores();
        return docentes;
    }
    public  List<ModeloResidente> obtenerResidentes(){
        List<ModeloResidente> residentes = reportesDAO.obtenerResidentes();
        return residentes;
    }



    public void generarReporteAsesor(int numeroTarjeta, String rutaArchivo) {
        List<ReporteDocenteAR> lista = reportesDAO.obtenerInfoAsesorRevisor(numeroTarjeta,1);
        if (lista.isEmpty()) {
           JOptionPane.showMessageDialog(null," No se encontraron datos para el asesor con tarjeta " + numeroTarjeta);
        } else {
             gen.generarExcel(lista, rutaArchivo,1);
        }
    }
    public void generarReporteRevisor(int numeroTarjeta, String rutaArchivo) {
        List<ReporteDocenteAR> lista = reportesDAO.obtenerInfoAsesorRevisor(numeroTarjeta,2);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null," No se encontraron datos para el asesor con tarjeta " + numeroTarjeta);
        } else {
            gen.generarExcel(lista, rutaArchivo,2);
        }
    }
    public void generarReporteResidente(int numero_control, String rutaArchivo) {
        List<ReporteAlumnoGeneral> lista = reportesDAO.obtenerReporteGeneralAlumno(numero_control);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay datos para el reporte general.");
        } else {
            gen2.generarExcel(lista, rutaArchivo);
        }
    }
}
