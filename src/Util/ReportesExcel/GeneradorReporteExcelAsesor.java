package Util.ReportesExcel;
import Modelo.ReporteDocenteAR;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneradorReporteExcelAsesor {

    public void generarExcel(List<ReporteDocenteAR> lista, String rutaArchivo, int n) {
        /**1 Asesor 2 Revisor*/
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte Asesor");

        // Estilo de encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Crear encabezado
        String[] columnas = {
                "Tarjeta", "Nombre Docente", "Apellido Paterno", "Apellido Materno",
                "Rol", "Etapa", "Nombre Proyecto", "Descripción Proyecto",
                "Nombre Alumno", "Apellido P. Alumno", "Apellido M. Alumno"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar filas
        int fila = 1;
        for (ReporteDocenteAR a : lista) {
            Row row = sheet.createRow(fila++);
            row.createCell(0).setCellValue(a.getNumeroTarjeta());
            row.createCell(1).setCellValue(a.getNombreDocente());
            row.createCell(2).setCellValue(a.getApellidoPaterno());
            row.createCell(3).setCellValue(a.getApellidoMaterno());
            row.createCell(4).setCellValue(a.getRol());
            row.createCell(5).setCellValue(a.getEtapa());
            row.createCell(6).setCellValue(a.getNombreProyecto());
            row.createCell(7).setCellValue(a.getDescripcionProyecto());
            row.createCell(8).setCellValue(a.getNombreAlumno());
            row.createCell(9).setCellValue(a.getApellidoPaternoAlumno());
            row.createCell(10).setCellValue(a.getApellidoMaternoAlumno());
        }

        // Autoajustar columnas
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Guardar archivo
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            workbook.write(fos);
            workbook.close();
            System.out.println(" Reporte generado en: " + rutaArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
