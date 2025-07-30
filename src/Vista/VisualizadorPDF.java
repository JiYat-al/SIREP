package Vista;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Clase dedicada para la visualización de documentos PDF
 * Maneja la apertura de archivos y errores de visualización
 */
public class VisualizadorPDF {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private final Color colorExito = new Color(76, 175, 80);
    private final Color colorAdvertencia = new Color(255, 87, 34);

    private JFrame ventanaPadre;
    private String nombreArchivo;
    private String nombreDocumento;
    private String residenteSeleccionado;
    private String rutaCompletaArchivo;

    public VisualizadorPDF(JFrame padre, String archivo, String documento, String residente) {
        this.ventanaPadre = padre;
        this.nombreArchivo = archivo;
        this.nombreDocumento = documento;
        this.residenteSeleccionado = residente;
        this.rutaCompletaArchivo = construirRutaArchivo();
    }

    /**
     * Construir la ruta completa del archivo
     * Utiliza el GestorArchivos para buscar en todas las ubicaciones posibles
     */
    private String construirRutaArchivo() {
        String numeroControl = residenteSeleccionado.split(" - ")[0];

        // Usar GestorArchivos para buscar el archivo
        String rutaEncontrada = Modelo.GestorArchivos.buscarArchivo(numeroControl, nombreArchivo);

        if (rutaEncontrada != null) {
            return rutaEncontrada;
        }

        // Si no se encuentra, retornar ruta esperada del sistema
        return Modelo.GestorArchivos.obtenerDirectorioResidente(numeroControl) + File.separator + nombreArchivo;
    }

    /**
     * Método principal para mostrar el visualizador
     */
    public void mostrarVisualizador() {
        // Verificar si el archivo existe
        File archivo = new File(rutaCompletaArchivo);

        if (!archivo.exists()) {
            mostrarDialogoArchivoNoEncontrado();
            return;
        }

        // Intentar abrir con el visor predeterminado del sistema
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();

                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(archivo);
                    mostrarNotificacionExito();
                } else {
                    mostrarDialogoSinSoporte();
                }

            } catch (IOException e) {
                mostrarDialogoErrorApertura(e);
            } catch (Exception e) {
                mostrarDialogoErrorGeneral(e);
            }
        } else {
            mostrarDialogoSinDesktop();
        }
    }

    /**
     * Mostrar diálogo cuando el archivo no se encuentra
     */
    private void mostrarDialogoArchivoNoEncontrado() {
        JDialog dialogo = new JDialog(ventanaPadre, "Archivo no encontrado", true);
        dialogo.setSize(500, 350);
        dialogo.setLocationRelativeTo(ventanaPadre);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Icono y título
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setOpaque(false);

        JLabel iconoError = new JLabel("📄❌");
        iconoError.setFont(new Font("Segoe UI", Font.BOLD, 48));

        JLabel titulo = new JLabel("Archivo no encontrado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorAdvertencia);

        panelSuperior.add(iconoError);
        panelSuperior.add(Box.createHorizontalStrut(15));
        panelSuperior.add(titulo);

        // Información
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setOpaque(false);
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información del documento"));

        JTextArea infoTexto = new JTextArea(
                "Residente: " + residenteSeleccionado + "\n" +
                        "Documento: " + nombreDocumento + "\n" +
                        "Archivo: " + nombreArchivo + "\n" +
                        "Ruta esperada: " + rutaCompletaArchivo + "\n\n" +
                        "POSIBLES CAUSAS:\n" +
                        "• El archivo fue movido o eliminado\n" +
                        "• Problemas de permisos de acceso\n" +
                        "• Error en la ruta de almacenamiento\n" +
                        "• El archivo no se subió correctamente\n\n" +
                        "SOLUCIONES:\n" +
                        "• Vuelva a subir el documento\n" +
                        "• Verifique la carpeta de documentos\n" +
                        "• Contacte al administrador del sistema"
        );
        infoTexto.setEditable(false);
        infoTexto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoTexto.setBackground(Color.WHITE);
        infoTexto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollInfo = new JScrollPane(infoTexto);
        panelInfo.add(scrollInfo, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false);

        JButton btnAbrir = crearBoton("📂 Abrir Carpeta", colorPrincipal);
        btnAbrir.addActionListener(e -> {
            try {
                File directorio = new File(rutaCompletaArchivo).getParentFile();
                if (directorio.exists()) {
                    Desktop.getDesktop().open(directorio);
                } else {
                    JOptionPane.showMessageDialog(dialogo,
                            "La carpeta tampoco existe:\n" + directorio.getAbsolutePath(),
                            "Carpeta no encontrada",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo,
                        "Error al abrir la carpeta:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCerrar = crearBoton("Cerrar", new Color(120, 120, 120));
        btnCerrar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnAbrir);
        panelBotones.add(btnCerrar);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    /**
     * Mostrar notificación de éxito
     */
    /**
     * Mostrar notificación de éxito
     */
    private void mostrarNotificacionExito() {
        JOptionPane.showMessageDialog(ventanaPadre,
                "Documento abierto exitosamente:\n\n" +
                        "📄 " + nombreDocumento + "\n" +
                        "📁 " + nombreArchivo + "\n" +
                        "👤 " + residenteSeleccionado + "\n\n" +
                        "El archivo se ha abierto con el visor PDF predeterminado.",
                "Documento abierto",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostrar diálogo cuando Desktop no está soportado
     */
    private void mostrarDialogoSinDesktop() {
        JOptionPane.showMessageDialog(ventanaPadre,
                "Sistema no compatible con apertura automática de archivos.\n\n" +
                        "INFORMACIÓN DEL DOCUMENTO:\n" +
                        "Residente: " + residenteSeleccionado + "\n" +
                        "Documento: " + nombreDocumento + "\n" +
                        "Archivo: " + nombreArchivo + "\n" +
                        "Ubicación: " + rutaCompletaArchivo + "\n\n" +
                        "SOLUCIÓN:\n" +
                        "Abra manualmente el archivo desde la ubicación mostrada.",
                "Apertura manual requerida",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Mostrar diálogo cuando no hay soporte para abrir archivos
     */
    private void mostrarDialogoSinSoporte() {
        JOptionPane.showMessageDialog(ventanaPadre,
                "El sistema no permite abrir archivos automáticamente.\n\n" +
                        "INFORMACIÓN DEL DOCUMENTO:\n" +
                        "Residente: " + residenteSeleccionado + "\n" +
                        "Documento: " + nombreDocumento + "\n" +
                        "Archivo: " + nombreArchivo + "\n" +
                        "Ubicación: " + rutaCompletaArchivo + "\n\n" +
                        "SOLUCIÓN:\n" +
                        "1. Copie la ruta mostrada arriba\n" +
                        "2. Abra el explorador de archivos\n" +
                        "3. Navegue a la ubicación\n" +
                        "4. Haga doble clic en el archivo PDF",
                "Apertura no soportada",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Mostrar diálogo de error de apertura
     */
    private void mostrarDialogoErrorApertura(IOException e) {
        JDialog dialogo = new JDialog(ventanaPadre, "Error al abrir documento", true);
        dialogo.setSize(550, 400);
        dialogo.setLocationRelativeTo(ventanaPadre);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título con icono
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setOpaque(false);

        JLabel iconoError = new JLabel("⚠️");
        iconoError.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel titulo = new JLabel("Error al abrir documento PDF");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(colorAdvertencia);

        panelTitulo.add(iconoError);
        panelTitulo.add(Box.createHorizontalStrut(10));
        panelTitulo.add(titulo);

        // Información del error
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setOpaque(false);

        JTextArea textoError = new JTextArea(
                "INFORMACIÓN DEL DOCUMENTO:\n" +
                        "Residente: " + residenteSeleccionado + "\n" +
                        "Documento: " + nombreDocumento + "\n" +
                        "Archivo: " + nombreArchivo + "\n" +
                        "Ruta: " + rutaCompletaArchivo + "\n\n" +
                        "ERROR TÉCNICO:\n" +
                        e.getClass().getSimpleName() + ": " + e.getMessage() + "\n\n" +
                        "POSIBLES CAUSAS:\n" +
                        "• El archivo PDF está corrupto o dañado\n" +
                        "• No hay un visor PDF instalado\n" +
                        "• Permisos insuficientes para abrir el archivo\n" +
                        "• El archivo está siendo usado por otra aplicación\n" +
                        "• Problema con la asociación de archivos PDF\n\n" +
                        "SOLUCIONES SUGERIDAS:\n" +
                        "1. Instalar un visor PDF (Adobe Reader, etc.)\n" +
                        "2. Verificar permisos del archivo\n" +
                        "3. Cerrar otras aplicaciones que usen el archivo\n" +
                        "4. Volver a subir el documento\n" +
                        "5. Contactar al administrador del sistema"
        );
        textoError.setEditable(false);
        textoError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textoError.setBackground(Color.WHITE);
        textoError.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollError = new JScrollPane(textoError);
        scrollError.setBorder(BorderFactory.createTitledBorder("Detalles del error"));
        panelInfo.add(scrollError, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false);

        JButton btnReintentar = crearBoton("🔄 Reintentar", colorExito);
        btnReintentar.addActionListener(e2 -> {
            dialogo.dispose();
            mostrarVisualizador(); // Reintentar
        });

        JButton btnCopiarRuta = crearBoton("📋 Copiar Ruta", colorPrincipal);
        btnCopiarRuta.addActionListener(e2 -> {
            java.awt.datatransfer.StringSelection stringSelection =
                    new java.awt.datatransfer.StringSelection(rutaCompletaArchivo);
            java.awt.datatransfer.Clipboard clipboard =
                    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            JOptionPane.showMessageDialog(dialogo,
                    "Ruta copiada al portapapeles:\n" + rutaCompletaArchivo,
                    "Ruta copiada",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnCerrar = crearBoton("Cerrar", new Color(120, 120, 120));
        btnCerrar.addActionListener(e2 -> dialogo.dispose());

        panelBotones.add(btnReintentar);
        panelBotones.add(btnCopiarRuta);
        panelBotones.add(btnCerrar);

        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    /**
     * Mostrar diálogo de error general
     */
    private void mostrarDialogoErrorGeneral(Exception e) {
        JOptionPane.showMessageDialog(ventanaPadre,
                "Error inesperado al intentar abrir el documento:\n\n" +
                        "INFORMACIÓN:\n" +
                        "Residente: " + residenteSeleccionado + "\n" +
                        "Documento: " + nombreDocumento + "\n" +
                        "Archivo: " + nombreArchivo + "\n\n" +
                        "ERROR:\n" +
                        e.getClass().getSimpleName() + ": " + e.getMessage() + "\n\n" +
                        "Contacte al administrador del sistema si el problema persiste.",
                "Error inesperado",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Crear botón estilizado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorder(new CompoundBorder(
                new LineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    /**
     * Verificar si un archivo existe
     */
    public boolean archivoExiste() {
        return new File(rutaCompletaArchivo).exists();
    }

    /**
     * Obtener información del archivo
     */
    public String obtenerInformacionArchivo() {
        File archivo = new File(rutaCompletaArchivo);

        if (!archivo.exists()) {
            return "Archivo no encontrado";
        }

        long tamaño = archivo.length();
        String tamaañoTexto;

        if (tamaño < 1024) {
            tamaañoTexto = tamaño + " bytes";
        } else if (tamaño < 1024 * 1024) {
            tamaañoTexto = String.format("%.1f KB", tamaño / 1024.0);
        } else {
            tamaañoTexto = String.format("%.1f MB", tamaño / (1024.0 * 1024.0));
        }

        return String.format("Tamaño: %s | Modificado: %s",
                tamaañoTexto,
                new java.util.Date(archivo.lastModified()));
    }

    /**
     * Obtener la ruta completa del archivo
     */
    public String getRutaCompleta() {
        return rutaCompletaArchivo;
    }

    /**
     * Establecer una ruta personalizada para el archivo
     */
    public void setRutaPersonalizada(String rutaPersonalizada) {
        this.rutaCompletaArchivo = rutaPersonalizada;
    }
}