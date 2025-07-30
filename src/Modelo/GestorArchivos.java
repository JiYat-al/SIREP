package Modelo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Gestor centralizado para el manejo de archivos del sistema
 */
public class GestorArchivos {

    // Configuración de directorios
    private static final String DIRECTORIO_BASE = "DocumentosExpedientes";
    private static final String SUBDIRECTORIO_TEMP = "temp";

    /**
     * Obtener el directorio base donde se almacenan todos los documentos
     */
    public static String obtenerDirectorioBase() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + DIRECTORIO_BASE;
    }

    /**
     * Obtener el directorio específico para un residente
     */
    public static String obtenerDirectorioResidente(String numeroControl) {
        return obtenerDirectorioBase() + File.separator + numeroControl;
    }

    /**
     * Crear directorio para un residente si no existe
     */
    public static boolean crearDirectorioResidente(String numeroControl) {
        try {
            Path directorio = Paths.get(obtenerDirectorioResidente(numeroControl));
            Files.createDirectories(directorio);
            System.out.println("Directorio creado/verificado: " + directorio);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear directorio para residente " + numeroControl + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Guardar archivo subido en el directorio del residente
     */
    public static String guardarArchivo(File archivoOrigen, String numeroControl, String nombreDocumento) {
        try {
            // Crear directorio si no existe
            if (!crearDirectorioResidente(numeroControl)) {
                throw new IOException("No se pudo crear el directorio para el residente");
            }

            // Verificar si es archivo de OneDrive/nube
            String rutaOrigen = archivoOrigen.getAbsolutePath();
            if (esArchivoDeNube(rutaOrigen)) {
                System.out.println("ADVERTENCIA: Archivo detectado en almacenamiento en la nube: " + rutaOrigen);

                // Intentar crear copia local temporal primero
                File archivoTemporal = crearCopiaLocalTemporal(archivoOrigen);
                if (archivoTemporal != null) {
                    System.out.println("Copia temporal creada: " + archivoTemporal.getAbsolutePath());
                    String resultado = guardarDesdeArchivoLocal(archivoTemporal, numeroControl, nombreDocumento);

                    // Limpiar archivo temporal
                    if (archivoTemporal.exists()) {
                        archivoTemporal.delete();
                        System.out.println("Archivo temporal eliminado");
                    }

                    return resultado;
                }
            }

            return guardarDesdeArchivoLocal(archivoOrigen, numeroControl, nombreDocumento);

        } catch (IOException e) {
            System.err.println("Error al guardar archivo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Detectar si un archivo está en almacenamiento en la nube
     */
    private static boolean esArchivoDeNube(String rutaArchivo) {
        String rutaLower = rutaArchivo.toLowerCase();
        return rutaLower.contains("onedrive") ||
                rutaLower.contains("dropbox") ||
                rutaLower.contains("google drive") ||
                rutaLower.contains("icloud");
    }

    /**
     * Crear copia local temporal de archivo en la nube
     */
    private static File crearCopiaLocalTemporal(File archivoOrigen) {
        try {
            // Crear directorio temporal
            String directorioTemp = obtenerDirectorioBase() + File.separator + SUBDIRECTORIO_TEMP;
            Files.createDirectories(Paths.get(directorioTemp));

            // Crear archivo temporal con nombre único
            String nombreTemp = "temp_" + System.currentTimeMillis() + "_" + archivoOrigen.getName();
            File archivoTemporal = new File(directorioTemp, nombreTemp);

            // Copiar archivo usando streams (más confiable para archivos de nube)
            try (java.io.FileInputStream inputStream = new java.io.FileInputStream(archivoOrigen);
                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(archivoTemporal)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                long totalBytes = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }

                System.out.println("Copia temporal completada: " + totalBytes + " bytes");
                return archivoTemporal;

            }

        } catch (Exception e) {
            System.err.println("Error creando copia temporal: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guardar desde archivo local (no en la nube)
     */
    private static String guardarDesdeArchivoLocal(File archivoOrigen, String numeroControl, String nombreDocumento) throws IOException {
        // Generar nombre único para evitar conflictos
        String extension = obtenerExtension(archivoOrigen.getName());
        String nombreSinEspacios = nombreDocumento.replaceAll("\\s+", "_");
        String nombreArchivo = numeroControl + "_" + nombreSinEspacios + "_" + System.currentTimeMillis() + extension;

        // Ruta destino
        String directorioDestino = obtenerDirectorioResidente(numeroControl);
        Path rutaDestino = Paths.get(directorioDestino, nombreArchivo);

        try {
            // Copiar archivo
            Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Archivo guardado exitosamente:");
            System.out.println("- Origen: " + archivoOrigen.getAbsolutePath());
            System.out.println("- Destino: " + rutaDestino.toString());

            return rutaDestino.toString();

        } catch (IOException e) {
            // Log más detallado del error
            System.err.println("Error específico al copiar archivo:");
            System.err.println("- Origen: " + archivoOrigen.getAbsolutePath());
            System.err.println("- Destino: " + rutaDestino.toString());
            System.err.println("- Error: " + e.getMessage());

            throw e;
        }
    }

    /**
     * Buscar archivo en las ubicaciones posibles
     */
    public static String buscarArchivo(String numeroControl, String nombreArchivo) {
        // Lista de posibles ubicaciones donde buscar el archivo
        String[] ubicacionesPosibles = {
                // 1. Directorio del sistema organizado
                obtenerDirectorioResidente(numeroControl) + File.separator + nombreArchivo,

                // 2. Buscar archivos que contengan el nombre (para archivos renombrados)
                buscarArchivoPorPatron(numeroControl, nombreArchivo),

                // 3. Documentos del usuario (OneDrive)
                System.getProperty("user.home") + File.separator + "OneDrive" + File.separator + "Documentos" + File.separator + nombreArchivo,

                // 4. Documentos del usuario (local)
                System.getProperty("user.home") + File.separator + "Documents" + File.separator + nombreArchivo,

                // 5. Directorio de trabajo actual
                System.getProperty("user.dir") + File.separator + "documentos" + File.separator + nombreArchivo,

                // 6. Directorio temporal
                obtenerDirectorioBase() + File.separator + SUBDIRECTORIO_TEMP + File.separator + nombreArchivo
        };

        // Buscar en cada ubicación
        for (String ubicacion : ubicacionesPosibles) {
            if (ubicacion != null && new File(ubicacion).exists()) {
                System.out.println("Archivo encontrado en: " + ubicacion);
                return ubicacion;
            }
        }

        System.out.println("Archivo no encontrado en ninguna ubicación: " + nombreArchivo);
        return null;
    }

    /**
     * Buscar archivo por patrón en el directorio del residente
     */
    private static String buscarArchivoPorPatron(String numeroControl, String nombreArchivo) {
        try {
            File directorio = new File(obtenerDirectorioResidente(numeroControl));
            if (!directorio.exists()) {
                return null;
            }

            File[] archivos = directorio.listFiles();
            if (archivos == null) {
                return null;
            }

            // Buscar archivos que contengan el nombre original
            String nombreBase = obtenerNombreSinExtension(nombreArchivo);
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.getName().contains(nombreBase)) {
                    return archivo.getAbsolutePath();
                }
            }

        } catch (Exception e) {
            System.err.println("Error al buscar archivo por patrón: " + e.getMessage());
        }

        return null;
    }

    /**
     * Eliminar archivo del sistema
     */
    public static boolean eliminarArchivo(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (archivo.exists()) {
                boolean eliminado = archivo.delete();
                if (eliminado) {
                    System.out.println("Archivo eliminado: " + rutaArchivo);
                } else {
                    System.err.println("No se pudo eliminar el archivo: " + rutaArchivo);
                }
                return eliminado;
            } else {
                System.out.println("El archivo no existe: " + rutaArchivo);
                return true; // Considerar como éxito si ya no existe
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar archivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtener información de un archivo
     */
    public static String obtenerInformacionArchivo(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists()) {
                return "Archivo no encontrado";
            }

            long tamaño = archivo.length();
            String tamaañoTexto = formatearTamaño(tamaño);
            String fechaModificacion = new java.util.Date(archivo.lastModified()).toString();

            return String.format("Tamaño: %s | Modificado: %s | Ubicación: %s",
                    tamaañoTexto, fechaModificacion, archivo.getParent());

        } catch (Exception e) {
            return "Error al obtener información: " + e.getMessage();
        }
    }

    /**
     * Formatear tamaño de archivo
     */
    private static String formatearTamaño(long tamaño) {
        if (tamaño < 1024) {
            return tamaño + " bytes";
        } else if (tamaño < 1024 * 1024) {
            return String.format("%.1f KB", tamaño / 1024.0);
        } else {
            return String.format("%.1f MB", tamaño / (1024.0 * 1024.0));
        }
    }

    /**
     * Obtener extensión de archivo
     */
    private static String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0 && ultimoPunto < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(ultimoPunto);
        }
        return "";
    }

    /**
     * Obtener nombre sin extensión
     */
    private static String obtenerNombreSinExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0) {
            return nombreArchivo.substring(0, ultimoPunto);
        }
        return nombreArchivo;
    }

    /**
     * Verificar si un archivo es PDF válido
     */
    public static boolean esPDFValido(File archivo) {
        if (!archivo.exists() || !archivo.isFile()) {
            return false;
        }

        String nombre = archivo.getName().toLowerCase();
        if (!nombre.endsWith(".pdf")) {
            return false;
        }

        // Verificar tamaño mínimo (archivos PDF vacíos suelen ser ~1KB)
        if (archivo.length() < 100) {
            return false;
        }

        // TODO: Agregar validación adicional del contenido PDF si es necesario

        return true;
    }

    /**
     * Listar todos los archivos de un residente
     */
    public static String[] listarArchivosResidente(String numeroControl) {
        try {
            File directorio = new File(obtenerDirectorioResidente(numeroControl));
            if (!directorio.exists()) {
                return new String[0];
            }

            File[] archivos = directorio.listFiles();
            if (archivos == null) {
                return new String[0];
            }

            return java.util.Arrays.stream(archivos)
                    .filter(File::isFile)
                    .map(File::getName)
                    .toArray(String[]::new);

        } catch (Exception e) {
            System.err.println("Error al listar archivos: " + e.getMessage());
            return new String[0];
        }
    }

    /**
     * Inicializar directorios del sistema
     */
    public static void inicializarSistemaArchivos() {
        try {
            // Crear directorio base
            Path directorioBase = Paths.get(obtenerDirectorioBase());
            Files.createDirectories(directorioBase);

            // Crear directorio temporal
            Path directorioTemp = Paths.get(obtenerDirectorioBase(), SUBDIRECTORIO_TEMP);
            Files.createDirectories(directorioTemp);

            System.out.println("Sistema de archivos inicializado:");
            System.out.println("- Directorio base: " + directorioBase);
            System.out.println("- Directorio temporal: " + directorioTemp);

        } catch (IOException e) {
            System.err.println("Error al inicializar sistema de archivos: " + e.getMessage());
        }
    }
}