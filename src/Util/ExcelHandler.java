package Util;

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
 * ExcelHandler SIMPLIFICADO - Solo carga datos, sin validaciones
 * La validación se hace ÚNICAMENTE en VistaResidente
 */
public class ExcelHandler {

    private static final String CARRERA_FIJA = "Ingeniería en Sistemas Computacionales";

    private static final String[] COLUMNAS_ESPERADAS = {
            "Número de Control", "Nombre", "Apellido Paterno", "Apellido Materno",
            "Semestre", "Correo", "Teléfono"
    };

    /**
     * SIMPLIFICADO: Solo carga datos del Excel SIN validar
     */
    public static List<ModeloResidente> importarDesdeExcel(File archivo) {
        List<ModeloResidente> residentes = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = crearWorkbook(archivo, fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() < 2) {
                JOptionPane.showMessageDialog(null,
                        "El archivo debe tener encabezados y al menos una fila de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return residentes;
            }

            if (!validarEncabezados(sheet)) {
                return residentes;
            }

            // SIMPLIFICADO: Solo cargar datos, sin validaciones
            int totalFilas = sheet.getLastRowNum();
            int filasLeidas = 0;

            System.out.println("DEBUG: Procesando desde fila 1 hasta " + totalFilas);

            for (int i = 1; i <= totalFilas; i++) {
                Row fila = sheet.getRow(i);

                if (fila == null) {
                    System.out.println("DEBUG: Fila " + (i + 1) + " es null, saltando...");
                    continue;
                }

                if (esFilaVacia(fila)) {
                    System.out.println("DEBUG: Fila " + (i + 1) + " está vacía, saltando...");
                    continue;
                }

                // SIMPLIFICADO: Solo procesar y agregar, sin validar
                ModeloResidente residente = procesarFila(fila, i + 1);
                if (residente != null) {
                    residentes.add(residente);
                    filasLeidas++;
                    System.out.println("DEBUG: Fila " + (i + 1) + " cargada: " +
                            residente.getNumeroControl() + " - " +
                            residente.getNombre() + " " + residente.getApellidoPaterno());
                }
            }

            // SIMPLIFICADO: Mostrar solo resumen de carga
            mostrarResumenCarga(filasLeidas);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al leer el archivo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("DEBUG: Total de residentes cargados: " + residentes.size());
        return residentes;
    }

    /**
     * SIMPLIFICADO: Solo mostrar resumen de carga
     */
    private static void mostrarResumenCarga(int totalCargados) {
        String mensaje = "📊 Carga de Excel completada\n\n" +
                "📋 Total registros cargados: " + totalCargados + "\n\n" +
                "💡 Los registros se mostrarán en la tabla\n" +
                "🔍 La validación se hará automáticamente\n" +
                "✏️ Doble click para editar cualquier registro\n" +
                "📥 Solo los válidos ✅ se podrán importar a la BD";

        JOptionPane.showMessageDialog(null, mensaje,
                "Carga completada", JOptionPane.INFORMATION_MESSAGE);
    }

    private static Workbook crearWorkbook(File archivo, FileInputStream fis) throws IOException {
        String nombreArchivo = archivo.getName().toLowerCase();
        if (nombreArchivo.endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (nombreArchivo.endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IOException("Formato no soportado. Use .xlsx o .xls");
        }
    }

    /**
     * Formatear texto con capitalización correcta
     */
    private static String formatearTexto(String texto) {
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

    private static boolean validarEncabezados(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            JOptionPane.showMessageDialog(null,
                    "El archivo no tiene encabezados.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (headerRow.getPhysicalNumberOfCells() < COLUMNAS_ESPERADAS.length) {
            JOptionPane.showMessageDialog(null,
                    "Se requieren " + COLUMNAS_ESPERADAS.length + " columnas.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<String> errores = new ArrayList<>();
        for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
            Cell cell = headerRow.getCell(i);
            String valorCelda = cell != null ? cell.getStringCellValue().trim() : "";

            if (!COLUMNAS_ESPERADAS[i].equalsIgnoreCase(valorCelda)) {
                errores.add("Col " + (i + 1) + ": '" + COLUMNAS_ESPERADAS[i] + "' ≠ '" + valorCelda + "'");
            }
        }

        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Encabezados incorrectos:\n\n");
            for (String error : errores) {
                mensaje.append("• ").append(error).append("\n");
            }
            mensaje.append("\n📋 Formato esperado:\n");
            for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
                mensaje.append((i + 1)).append(". ").append(COLUMNAS_ESPERADAS[i]).append("\n");
            }

            JOptionPane.showMessageDialog(null, mensaje.toString(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private static boolean esFilaVacia(Row fila) {
        if (fila == null) return true;

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
     * SIMPLIFICADO: Solo procesa datos SIN validar
     */
    private static ModeloResidente procesarFila(Row fila, int numeroFila) {
        try {
            // Obtener valores de forma segura
            int numeroControl = obtenerValorEnteroSeguro(fila.getCell(0));
            String nombre = formatearTexto(obtenerValorTextoSeguro(fila.getCell(1)));
            String apellidoPaterno = formatearTexto(obtenerValorTextoSeguro(fila.getCell(2)));
            String apellidoMaterno = formatearTexto(obtenerValorTextoSeguro(fila.getCell(3)));
            String carrera = CARRERA_FIJA;
            int semestre = obtenerValorEnteroSeguro(fila.getCell(4));
            String correo = obtenerValorTextoSeguro(fila.getCell(5)).toLowerCase();
            String telefono = obtenerValorTextoSeguro(fila.getCell(6));

            // CREAR RESIDENTE SIN VALIDAR - La validación se hace en Vista
            ModeloResidente residente = new ModeloResidente(numeroControl, nombre, apellidoPaterno,
                    apellidoMaterno, carrera, semestre, correo, telefono, 1);

            // *** CORRECCIÓN CLAVE: NO validar aquí - dejar que Vista valide ***
            // La Vista se encargará de toda la validación

            return residente;

        } catch (Exception e) {
            System.out.println("DEBUG: Error procesando fila " + numeroFila + ": " + e.getMessage());
            // Crear residente con datos mínimos
            return new ModeloResidente(0, "Error", "Fila " + numeroFila, "", CARRERA_FIJA, 9, "error@error.com", "", 1);
        }
    }

    /**
     * Obtener valores de forma segura sin lanzar excepciones
     */
    private static String obtenerValorTextoSeguro(Cell cell) {
        if (cell == null) {
            return "";
        }

        try {
            return obtenerValorCelda(cell).trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Obtener entero de forma segura sin lanzar excepciones
     */
    private static int obtenerValorEnteroSeguro(Cell cell) {
        if (cell == null) {
            return 0;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String valor = cell.getStringCellValue().trim();
                if (valor.isEmpty()) {
                    return 0;
                }
                return Integer.parseInt(valor);
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

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

    public static void exportarAExcel(List<ModeloResidente> residentes, File archivo) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Residentes");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNAS_ESPERADAS[i]);

                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (ModeloResidente residente : residentes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(residente.getNumeroControl());
                row.createCell(1).setCellValue(residente.getNombre());
                row.createCell(2).setCellValue(residente.getApellidoPaterno());
                row.createCell(3).setCellValue(residente.getApellidoMaterno());
                row.createCell(4).setCellValue(residente.getSemestre());
                row.createCell(5).setCellValue(residente.getCorreo());
                row.createCell(6).setCellValue(residente.getTelefono());
            }

            for (int i = 0; i < COLUMNAS_ESPERADAS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(archivo)) {
                workbook.write(fileOut);
            }

            JOptionPane.showMessageDialog(null,
                    "✅ Archivo exportado exitosamente\n📁 " + archivo.getAbsolutePath(),
                    "Exportación completada", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Error al exportar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static File seleccionarArchivoExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Seleccionar archivo Excel");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos Excel (*.xlsx, *.xls)", "xlsx", "xls");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static File seleccionarUbicacionGuardar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Guardar archivo Excel");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedFile(new File("residentes_sistemas.xlsx"));

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

    public static List<ModeloResidente> importarExcelCompleto() {
        String mensaje = "📋 Formato requerido:\n" +
                "1. Número de Control | 2. Nombre | 3. Apellido Paterno\n" +
                "4. Apellido Materno | 5. Semestre (9-15) | 6. Correo | 7. Teléfono\n\n" +
                "🎓 La carrera se asigna automáticamente\n" +
                "✅ La validación se hace en la interfaz\n" +
                "¿Continuar?";

        int opcion = JOptionPane.showConfirmDialog(null, mensaje,
                "Importar Excel", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            File archivo = seleccionarArchivoExcel();
            if (archivo != null) {
                return importarDesdeExcel(archivo);
            }
        }
        return new ArrayList<>();
    }

    public static String getCarreraFija() {
        return CARRERA_FIJA;
    }
}