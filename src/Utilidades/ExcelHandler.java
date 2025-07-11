package Utilidades;

import Modelo.ModeloResidente;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase para manejar la importación/exportación de archivos Excel
 */
public class ExcelHandler {

    // Nombres esperados de las columnas en el archivo Excel
    private static final String[] COLUMNAS_ESPERADAS = {
            "Número de Control", "Nombre", "Apellido Paterno", "Apellido Materno",
            "Carrera", "Semestre", "Correo", "Teléfono"
    };

    /**
     * Método para importar residentes desde un archivo Excel
     * @param archivo El archivo Excel a importar
     * @return Lista de residentes importados
     */
    public static List<ModeloResidente> importarDesdeExcel(File archivo) {
        List<ModeloResidente> residentes = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = crearWorkbook(archivo, fis)) {

            // Obtener la primera hoja
            Sheet sheet = workbook.getSheetAt(0);

            // Verificar que el archivo tenga datos
            if (sheet.getPhysicalNumberOfRows() < 2) {
                JOptionPane.showMessageDialog(null,
                        "El archivo Excel debe tener al menos una fila de encabezados y una fila de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return residentes;
            }

            // Validar encabezados
            if (!validarEncabezados(sheet)) {
                return residentes; // El método validarEncabezados ya muestra el error
            }

            // Procesar las filas de datos
            int filasConError = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row fila = sheet.getRow(i);
                if (fila == null || esFilaVacia(fila)) {
                    continue; // Saltar filas vacías
                }

                try {
                    ModeloResidente residente = procesarFila(fila, i + 1);
                    if (residente != null) {
                        residentes.add(residente);
                    }
                } catch (Exception e) {
                    filasConError++;
                    System.err.println("Error en fila " + (i + 1) + ": " + e.getMessage());
                }
            }

            // Mostrar resultado
            String mensaje = "Importación completada:\n" +
                    "- Registros importados: " + residentes.size() + "\n" +
                    "- Filas con errores: " + filasConError;

            if (filasConError > 0) {
                mensaje += "\n\nRevisa la consola para detalles de los errores.";
            }

            JOptionPane.showMessageDialog(null, mensaje,
                    "Importación Excel", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al leer el archivo Excel: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado al procesar el archivo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return residentes;
    }

    /**
     * Crear el workbook apropiado según la extensión del archivo
     */
    private static Workbook crearWorkbook(File archivo, FileInputStream fis) throws IOException {
        String nombreArchivo = archivo.getName().toLowerCase();
        if (nombreArchivo.endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (nombreArchivo.endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IOException("Formato de archivo no soportado. Use .xlsx o .xls");
        }
    }

    /**
     * Validar que los encabezados del archivo Excel sean correctos
     */
    private static boolean validarEncabezados(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            JOptionPane.showMessageDialog(null,
                    "El archivo no tiene fila de encabezados.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificar que tenga el número correcto de columnas
        if (headerRow.getPhysicalNumberOfCells() < COLUMNAS_ESPERADAS.length) {
            JOptionPane.showMessageDialog(null,
                    "El archivo debe tener al menos " + COLUMNAS_ESPERADAS.length + " columnas.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificar nombres de columnas
        List<String> errores = new ArrayList<>();
        for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
            Cell cell = headerRow.getCell(i);
            String valorCelda = cell != null ? cell.getStringCellValue().trim() : "";

            if (!COLUMNAS_ESPERADAS[i].equalsIgnoreCase(valorCelda)) {
                errores.add("Columna " + (i + 1) + ": Se esperaba '" + COLUMNAS_ESPERADAS[i] +
                        "' pero se encontró '" + valorCelda + "'");
            }
        }

        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Los encabezados del archivo no son correctos:\n\n");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("\n");
            }
            mensaje.append("\nEncabezados esperados:\n");
            for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
                mensaje.append((i + 1)).append(". ").append(COLUMNAS_ESPERADAS[i]).append("\n");
            }

            JOptionPane.showMessageDialog(null, mensaje.toString(),
                    "Error en encabezados", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Verificar si una fila está vacía
     */
    private static boolean esFilaVacia(Row fila) {
        for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
            Cell cell = fila.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String valor = obtenerValorCelda(cell).trim();
                if (!valor.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Procesar una fila y crear un objeto ModeloResidente
     */
    private static ModeloResidente procesarFila(Row fila, int numeroFila) throws Exception {
        try {
            // Obtener valores de las celdas
            int numeroControl = obtenerValorEntero(fila.getCell(0), "Número de Control", numeroFila);
            String nombre = obtenerValorTexto(fila.getCell(1), "Nombre", numeroFila);
            String apellidoPaterno = obtenerValorTexto(fila.getCell(2), "Apellido Paterno", numeroFila);
            String apellidoMaterno = obtenerValorTexto(fila.getCell(3), "Apellido Materno", numeroFila);
            String carrera = obtenerValorTexto(fila.getCell(4), "Carrera", numeroFila);
            int semestre = obtenerValorEntero(fila.getCell(5), "Semestre", numeroFila);
            String correo = obtenerValorTexto(fila.getCell(6), "Correo", numeroFila);
            String telefono = obtenerValorTexto(fila.getCell(7), "Teléfono", numeroFila);

            // Validaciones básicas
            if (nombre.isEmpty() || apellidoPaterno.isEmpty()) {
                throw new Exception("Nombre y apellido paterno son obligatorios");
            }

            if (semestre < 1 || semestre > 12) {
                throw new Exception("Semestre debe estar entre 1 y 12");
            }

            // Crear y retornar el objeto ModeloResidente
            return new ModeloResidente(numeroControl, nombre, apellidoPaterno,
                    apellidoMaterno, carrera, semestre, correo, telefono, 0);

        } catch (Exception e) {
            throw new Exception("Fila " + numeroFila + ": " + e.getMessage());
        }
    }

    /**
     * Obtener valor de texto de una celda
     */
    private static String obtenerValorTexto(Cell cell, String nombreColumna, int numeroFila) throws Exception {
        if (cell == null) {
            return "";
        }

        try {
            return obtenerValorCelda(cell).trim();
        } catch (Exception e) {
            throw new Exception("Error en columna '" + nombreColumna + "': " + e.getMessage());
        }
    }

    /**
     * Obtener valor entero de una celda
     */
    private static int obtenerValorEntero(Cell cell, String nombreColumna, int numeroFila) throws Exception {
        if (cell == null) {
            throw new Exception("La columna '" + nombreColumna + "' no puede estar vacía");
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String valor = cell.getStringCellValue().trim();
                if (valor.isEmpty()) {
                    throw new Exception("La columna '" + nombreColumna + "' no puede estar vacía");
                }
                return Integer.parseInt(valor);
            } else {
                throw new Exception("La columna '" + nombreColumna + "' debe ser un número");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Error en columna '" + nombreColumna + "': no es un número válido");
        }
    }

    /**
     * Obtener el valor de una celda como String
     */
    private static String obtenerValorCelda(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Para números enteros, no mostrar decimales
                    double valor = cell.getNumericCellValue();
                    if (valor == (int) valor) {
                        return String.valueOf((int) valor);
                    } else {
                        return String.valueOf(valor);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Método para exportar residentes a un archivo Excel
     */
    public static void exportarAExcel(List<ModeloResidente> residentes, File archivo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Residentes");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNAS_ESPERADAS[i]);

                // Estilo para encabezados
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }

            // Crear filas de datos
            int rowNum = 1;
            for (ModeloResidente residente : residentes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(residente.getNumeroControl());
                row.createCell(1).setCellValue(residente.getNombre());
                row.createCell(2).setCellValue(residente.getApellidoPaterno());
                row.createCell(3).setCellValue(residente.getApellidoMaterno());
                row.createCell(4).setCellValue(residente.getCarrera());
                row.createCell(5).setCellValue(residente.getSemestre());
                row.createCell(6).setCellValue(residente.getCorreo());
                row.createCell(7).setCellValue(residente.getTelefono());
            }

            // Autoajustar columnas
            for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(archivo)) {
                workbook.write(fileOut);
            }

            JOptionPane.showMessageDialog(null,
                    "Archivo Excel exportado exitosamente:\n" + archivo.getAbsolutePath(),
                    "Exportación exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al exportar archivo Excel: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para seleccionar un archivo Excel para importar
     */
    public static File seleccionarArchivoExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Seleccionar archivo Excel");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        // Filtro para archivos Excel
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos Excel (*.xlsx, *.xls)", "xlsx", "xls");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Método para seleccionar ubicación para guardar archivo Excel
     */
    public static File seleccionarUbicacionGuardar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Guardar archivo Excel");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedFile(new File("residentes.xlsx"));

        // Filtro para archivos Excel
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos Excel (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            return file;
        }
        return null;
    }

    /**
     * Método completo para importar Excel con selección de archivo
     */
    public static List<ModeloResidente> importarExcelCompleto() {
        File archivo = seleccionarArchivoExcel();
        if (archivo != null) {
            return importarDesdeExcel(archivo);
        }
        return new ArrayList<>();
    }
}