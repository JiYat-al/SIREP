package Vista;

import Modelo.Anteproyecto;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FormularioProrroga extends JDialog {
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Anteproyecto anteproyectoSeleccionado;
    private final AnteproyectoInterfaz ventanaPadre;
    private JSpinner spinnerFecha;
    private JLabel lblArchivoSeleccionado;

    public FormularioProrroga(AnteproyectoInterfaz padre, Anteproyecto anteproyecto) {
        super(padre, "Gestión de Prórroga - " + anteproyecto.getProyecto().getNombre(), true);
        this.ventanaPadre = padre;
        this.anteproyectoSeleccionado = anteproyecto;

        initializeComponents();
        setupLayout();
        setupEvents();

        setSize(900, 550);
        setLocationRelativeTo(padre);
        setResizable(false);
    }

    private void initializeComponents() {
        // Inicializar componentes principales
        spinnerFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy");
        spinnerFecha.setEditor(dateEditor);
        spinnerFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerFecha.setPreferredSize(new Dimension(150, 35));

        // Configurar fecha inicial
        if (anteproyectoSeleccionado.isTieneProrroga() && anteproyectoSeleccionado.getFechaFinNueva() != null) {
            spinnerFecha.setValue(anteproyectoSeleccionado.getFechaFinNueva());
        } else {
            long tiempoActual = anteproyectoSeleccionado.getFechaFin().getTime();
            Date fechaPorDefecto = new Date(tiempoActual + (30L * 24 * 60 * 60 * 1000));
            spinnerFecha.setValue(fechaPorDefecto);
        }

        // Label para archivo seleccionado
        lblArchivoSeleccionado = new JLabel("Ningún archivo seleccionado");
        lblArchivoSeleccionado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblArchivoSeleccionado.setForeground(new Color(120, 120, 120));

        // Si ya tiene documento de prórroga, mostrarlo
        if (anteproyectoSeleccionado.isTieneProrroga() &&
                anteproyectoSeleccionado.getArchivoAutorizacionProrroga() != null &&
                !anteproyectoSeleccionado.getArchivoAutorizacionProrroga().trim().isEmpty()) {

            String nombreArchivo = anteproyectoSeleccionado.getArchivoAutorizacionProrroga();
            if (nombreArchivo.contains("\\") || nombreArchivo.contains("/")) {
                nombreArchivo = nombreArchivo.substring(Math.max(
                        nombreArchivo.lastIndexOf("\\"),
                        nombreArchivo.lastIndexOf("/")
                ) + 1);
            }
            lblArchivoSeleccionado.setText(nombreArchivo);
            lblArchivoSeleccionado.setForeground(new Color(76, 175, 80));
            lblArchivoSeleccionado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
    }

    private void setupLayout() {
        // Panel principal con fondo degradado MORADO (tema SIREP)
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(230, 225, 255),
                        0, getHeight(), colorPrincipal
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(180, 170, 255, 40));
                g2.fillOval(-50, -50, 150, 150);
                g2.fillOval(getWidth() - 100, getHeight() - 100, 120, 120);
            }
        };

        // Header con tema MORADO SIREP
        JPanel headerPanel = createHeader();

        // Contenido principal
        JPanel contenidoPanel = createContentPanel();

        // Panel de botones
        JPanel panelBotones = createButtonPanel();

        // Ensamblar diálogo
        JScrollPane scrollPane = new JScrollPane(contenidoPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        panelPrincipal.add(headerPanel, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint grad = new GradientPaint(
                        0, 0, colorPrincipal,
                        0, getHeight(), colorPrincipal.darker()
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
            }
        };
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel tituloHeader = new JLabel("Gestión de Prórroga para: " + anteproyectoSeleccionado.getProyecto().getNombre());
        tituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloHeader.setForeground(Color.WHITE);
        headerPanel.add(tituloHeader, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contenidoPanel = new JPanel();
        contenidoPanel.setLayout(new BoxLayout(contenidoPanel, BoxLayout.Y_AXIS));
        contenidoPanel.setOpaque(false);
        contenidoPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Información del anteproyecto
        JPanel infoPanel = createInfoSection();
        contenidoPanel.add(infoPanel);
        contenidoPanel.add(Box.createVerticalStrut(25));

        // Sección de nueva fecha
        JPanel seccionFecha = createDateSection();
        contenidoPanel.add(seccionFecha);
        contenidoPanel.add(Box.createVerticalStrut(30));

        // Sección de archivo
        JPanel seccionArchivo = createFileSection();
        contenidoPanel.add(seccionArchivo);
        contenidoPanel.add(Box.createVerticalStrut(30));

        return contenidoPanel;
    }

    private JPanel createInfoSection() {
        JPanel infoPanel = crearSeccionProrrogaSIREP("INFORMACIÓN DEL ANTEPROYECTO");
        String alumnos = anteproyectoSeleccionado.getResidentes().stream()
                .map(r -> r.getNombre())
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("Sin alumnos");

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String fechaOriginal = formato.format(anteproyectoSeleccionado.getFechaFin());
        String fechaProrroga = anteproyectoSeleccionado.isTieneProrroga() && anteproyectoSeleccionado.getFechaFinNueva() != null
                ? formato.format(anteproyectoSeleccionado.getFechaFinNueva())
                : "No definida";

        JLabel lblInfo = new JLabel("<html><b>Proyecto:</b> " + anteproyectoSeleccionado.getProyecto().getNombre() +
                "<br><b>Alumnos:</b> " + alumnos +
                "<br><b>Fecha fin original:</b> " + fechaOriginal +
                (anteproyectoSeleccionado.isTieneProrroga() ?
                        "<br><b>Fecha fin con prórroga:</b> " + fechaProrroga : "") + "</html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo.setForeground(new Color(60, 60, 80));
        infoPanel.add(lblInfo, BorderLayout.CENTER);

        return infoPanel;
    }

    private JPanel createDateSection() {
        JPanel seccionFecha = crearSeccionProrrogaSIREP("NUEVA FECHA DE FINALIZACIÓN *");
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelFecha.setOpaque(false);

        JLabel lblFecha = new JLabel("Nueva fecha fin de residencia:");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFecha.setForeground(new Color(60, 60, 80));

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String fechaOriginal = formato.format(anteproyectoSeleccionado.getFechaFin());
        JLabel lblFechaAyuda = new JLabel("(Debe ser posterior a la fecha fin original: " + fechaOriginal + ")");
        lblFechaAyuda.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFechaAyuda.setForeground(new Color(120, 120, 120));

        panelFecha.add(lblFecha);
        panelFecha.add(Box.createHorizontalStrut(15));
        panelFecha.add(spinnerFecha);
        seccionFecha.add(panelFecha, BorderLayout.CENTER);
        seccionFecha.add(lblFechaAyuda, BorderLayout.SOUTH);

        return seccionFecha;
    }

    private JPanel createFileSection() {
        JPanel seccionArchivo = crearSeccionProrrogaSIREP("DOCUMENTO DE AUTORIZACIÓN DE PRÓRROGA *");
        JPanel panelArchivo = new JPanel(new BorderLayout(10, 5));
        panelArchivo.setOpaque(false);

        JPanel panelArchivoSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelArchivoSuperior.setOpaque(false);

        JButton btnSeleccionarArchivo = createFileButton();

        JLabel lblArchivoDescripcion = new JLabel("Documento oficial que autoriza la prórroga del anteproyecto:");
        lblArchivoDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblArchivoDescripcion.setForeground(new Color(60, 60, 80));

        JLabel lblArchivoRequisitos = new JLabel("(Solo archivos PDF, máximo 10 MB)");
        lblArchivoRequisitos.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblArchivoRequisitos.setForeground(new Color(120, 120, 120));

        panelArchivoSuperior.add(lblArchivoDescripcion);
        panelArchivoSuperior.add(Box.createHorizontalStrut(15));
        panelArchivoSuperior.add(btnSeleccionarArchivo);

        JPanel panelArchivoInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        panelArchivoInferior.setOpaque(false);
        panelArchivoInferior.add(lblArchivoSeleccionado);
        panelArchivoInferior.add(Box.createHorizontalStrut(10));
        panelArchivoInferior.add(lblArchivoRequisitos);

        panelArchivo.add(panelArchivoSuperior, BorderLayout.NORTH);
        panelArchivo.add(panelArchivoInferior, BorderLayout.SOUTH);

        seccionArchivo.add(panelArchivo, BorderLayout.CENTER);

        return seccionArchivo;
    }

    private JButton createFileButton() {
        JButton btnSeleccionarArchivo = new JButton("Seleccionar PDF") {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                setPreferredSize(new Dimension(120, 30));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true; repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false; repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color colorBase = hover ? colorPrincipal.darker() : colorPrincipal;
                GradientPaint grad = new GradientPaint(0, 0, colorBase.brighter(), 0, getHeight(), colorBase);
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };

        // Configurar evento directamente
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());

        return btnSeleccionarArchivo;
    }

    private JPanel createButtonPanel() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setOpaque(false);

        JButton btnGuardar = crearBotonProrrogaSIREP("Guardar Prórroga", new Color(76, 175, 80));
        JButton btnCancelar = crearBotonProrrogaSIREP("Cancelar", new Color(244, 67, 54));

        // Configurar eventos directamente
        btnGuardar.addActionListener(e -> guardarProrroga());
        btnCancelar.addActionListener(e -> cancelar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        return panelBotones;
    }

    private void setupEvents() {
        // Los eventos se configuran directamente en los métodos createButtonPanel y createFileButton
        // No es necesario configurar eventos adicionales aquí
    }

    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Documento de Autorización de Prórroga");

        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf");
        fileChooser.setFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Documents"));

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            if (validarArchivo(archivoSeleccionado)) {
                String rutaCompleta = archivoSeleccionado.getAbsolutePath();
                anteproyectoSeleccionado.setArchivoAutorizacionProrroga(rutaCompleta);

                lblArchivoSeleccionado.setText(archivoSeleccionado.getName());
                lblArchivoSeleccionado.setForeground(new Color(76, 175, 80));
                lblArchivoSeleccionado.setFont(new Font("Segoe UI", Font.PLAIN, 12));

                JOptionPane.showMessageDialog(this,
                        "Archivo cargado exitosamente:\n" + archivoSeleccionado.getName() +
                                "\n\nTamaño: " + String.format("%.2f", archivoSeleccionado.length() / 1024.0) + " KB",
                        "Archivo Válido",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean validarArchivo(File archivo) {
        StringBuilder errores = new StringBuilder();

        if (!archivo.exists()) {
            errores.append("• El archivo seleccionado no existe.\n");
        }

        String nombreArchivo = archivo.getName().toLowerCase();
        if (!nombreArchivo.endsWith(".pdf")) {
            errores.append("• Solo se permiten archivos PDF.\n");
        }

        long tamanoMB = archivo.length() / (1024 * 1024);
        if (tamanoMB > 10) {
            errores.append("• El archivo no puede exceder los 10 MB. Tamaño actual: ")
                    .append(tamanoMB)
                    .append(" MB.\n");
        }

        if (archivo.length() == 0) {
            errores.append("• El archivo está vacío.\n");
        }

        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Errores en el archivo seleccionado:\n\n" + errores.toString(),
                    "Archivo No Válido",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void guardarProrroga() {
        StringBuilder errores = new StringBuilder();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        // Validar fecha
        Date nuevaFecha = (Date) spinnerFecha.getValue();
        Date fechaActualSistema = new Date();
        Date fechaFinOriginal = anteproyectoSeleccionado.getFechaFin();

        if (nuevaFecha.before(fechaFinOriginal)) {
            errores.append("• La nueva fecha debe ser posterior a la fecha fin original (")
                    .append(formato.format(fechaFinOriginal))
                    .append(").\n");
        }

        if (nuevaFecha.before(fechaActualSistema)) {
            errores.append("• La nueva fecha no puede ser anterior a la fecha actual.\n");
        }

        // Validar documento de autorización
        String archivoAutorizacion = anteproyectoSeleccionado.getArchivoAutorizacionProrroga();
        if (archivoAutorizacion == null || archivoAutorizacion.trim().isEmpty()) {
            errores.append("• Debe seleccionar el documento de autorización de prórroga.\n");
        } else {
            File archivoValidacion = new File(archivoAutorizacion);
            if (!archivoValidacion.exists()) {
                errores.append("• El archivo de autorización seleccionado no existe o fue movido.\n");
            }
        }

        // Validar que la prórroga no sea excesivamente larga (máximo 6 meses)
        long diferencia = nuevaFecha.getTime() - fechaFinOriginal.getTime();
        long seismeses = 6L * 30 * 24 * 60 * 60 * 1000;
        if (diferencia > seismeses) {
            errores.append("• La prórroga no puede exceder los 6 meses desde la fecha fin original.\n");
        }

        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor corrija los siguientes errores:\n\n" + errores.toString(),
                    "Errores de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmación final
        String nombreArchivoConfirmacion = "Sin archivo";
        if (anteproyectoSeleccionado.getArchivoAutorizacionProrroga() != null) {
            File archivoTemp = new File(anteproyectoSeleccionado.getArchivoAutorizacionProrroga());
            nombreArchivoConfirmacion = archivoTemp.getName();
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Confirma guardar la prórroga con los siguientes datos?\n\n" +
                        "Anteproyecto: " + anteproyectoSeleccionado.getProyecto().getNombre() + "\n" +
                        "Nueva fecha fin: " + formato.format(nuevaFecha) + "\n" +
                        "Documento autorización: " + nombreArchivoConfirmacion,
                "Confirmar Prórroga",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Guardar la prórroga
            anteproyectoSeleccionado.setTieneProrroga(true);
            anteproyectoSeleccionado.setFechaFinNueva(nuevaFecha);

            JOptionPane.showMessageDialog(this,
                    "Prórroga guardada exitosamente.\n\n" +
                            "El anteproyecto ahora tiene una nueva fecha fin: " + formato.format(nuevaFecha),
                    "Prórroga Guardada",
                    JOptionPane.INFORMATION_MESSAGE);

            // Actualizar la tabla en la ventana padre
            ventanaPadre.cargarTablaAnteproyectos();

            dispose();
        }
    }

    private void cancelar() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar?\nSe perderán todos los cambios no guardados.",
                "Cancelar Prórroga",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private JPanel crearSeccionProrrogaSIREP(String titulo) {
        JPanel seccion = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 4, 15, 15);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2.setColor(new Color(255, 152, 0, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        seccion.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        seccion.setPreferredSize(new Dimension(0, 120));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(255, 152, 0));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        seccion.add(lblTitulo, BorderLayout.NORTH);
        return seccion;
    }

    private JButton crearBotonProrrogaSIREP(String texto, Color color) {
        return new JButton(texto) {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true; repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false; repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 4, 25, 25);

                Color colorBase = hover ? color.darker() : color;
                GradientPaint grad = new GradientPaint(0, 0, colorBase.brighter(), 0, getHeight(), colorBase);
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
    }
}
