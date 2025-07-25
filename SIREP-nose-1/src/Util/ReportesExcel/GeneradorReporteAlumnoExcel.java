package Util.ReportesExcel;
import Modelo.ReporteAlumnoGeneral;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.util.List;
public class GeneradorReporteAlumnoExcel {


    public static void generarExcel(List<ReporteAlumnoGeneral> lista, String rutaArchivo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte General");

            // Encabezados
            Row header = sheet.createRow(0);
            String[] columnas = {"No. Control", "Nombre", "Apellido Paterno", "Apellido Materno", "Semestre", "Teléfono",
                    "Proyecto", "Descripción", "Duración", "Inicio", "Fin", "Empresa", "Asesores", "Revisores"};

            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            // Datos
            int rowNum = 1;
            for (ReporteAlumnoGeneral r : lista) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getNumeroControl());
                row.createCell(1).setCellValue(r.getNombre());
                row.createCell(2).setCellValue(r.getApellidoPaterno());
                row.createCell(3).setCellValue(r.getApellidoMaterno());
                row.createCell(4).setCellValue(r.getSemestre());
                row.createCell(5).setCellValue(r.getTelefono());
                row.createCell(6).setCellValue(r.getNombreProyecto());
                row.createCell(7).setCellValue(r.getDescripcion());
                row.createCell(8).setCellValue(r.getDuracion());
                row.createCell(9).setCellValue(r.getFechaInicio());
                row.createCell(10).setCellValue(r.getFechaFin());
                row.createCell(11).setCellValue(r.getNombreEmpresa());
                row.createCell(12).setCellValue(r.getAsesores());
                row.createCell(13).setCellValue(r.getRevisores());
            }

            // Autoajustar
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream fileOut = new FileOutputStream(rutaArchivo);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
