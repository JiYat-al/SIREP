package Controlador;

import Modelo.ModeloResidente;
import Vista.VistaResidentes.VistaResidente;
import Util.ExcelHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

public class ControladorResidente {
    private VistaResidente vista;
    private ModeloResidente modelo;
    private List<String> estadosValidacion; // Para guardar el estado de cada fila
    private JPanel panelIndicador; // Panel lateral con indicador

    public ControladorResidente(VistaResidente vista) {
        this.vista = vista;
        this.modelo = new ModeloResidente();
        this.estadosValidacion = new ArrayList<>();
        configurarEventos();
    }

    /**
     * Configurar eventos de la vista
     */
    private void configurarEventos() {
        // Los eventos se configuran directamente en la vista, pero pueden ser manejados aquí
        // Este controlador actúa como intermediario entre la vista y el modelo
    }

    /**
     * Cargar Excel en la tabla para visualización con validación visual
     */
    public void cargarExcelEnTabla() {
        try {
            // Cambiar cursor a espera
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Seleccionar archivo Excel
            java.io.File archivo = ExcelHandler.seleccionarArchivoExcel();
            if (archivo == null) {
                return; // Usuario canceló
            }

            // Cargar datos desde Excel
            List<ModeloResidente> residentes = ExcelHandler.importarDesdeExcel(archivo);

            if (residentes != null && !residentes.isEmpty()) {
                // Validar y preparar datos con estados
                procesarResidentesConValidacion(residentes);

            } else {
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "⚠️ No se encontraron datos válidos en el archivo Excel",
                        "Sin datos", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "❌ Error al cargar el archivo Excel:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Restaurar cursor normal
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Procesar residentes con validación y mostrar en tabla con tooltips e indicador
     * MANTIENE los residentes que ya estaban en la tabla
     */
    private void procesarResidentesConValidacion(List<ModeloResidente> nuevosResidentes) {
        // Obtener residentes que ya estaban en la tabla
        List<ModeloResidente> residentesExistentes = vista.getListaResidentes();

        // Combinar residentes existentes + nuevos del Excel
        List<ModeloResidente> todosLosResidentes = new ArrayList<>();
        todosLosResidentes.addAll(residentesExistentes);
        todosLosResidentes.addAll(nuevosResidentes);

        // Limpiar estados anteriores
        estadosValidacion.clear();

        int validos = 0;
        int invalidos = 0;

        // Limpiar tabla y cargar TODOS los residentes con validación
        vista.limpiarTabla();

        for (int i = 0; i < todosLosResidentes.size(); i++) {
            ModeloResidente residente = todosLosResidentes.get(i);
            String estadoValidacion = validarResidenteCompleto(residente, i + 1);

            // Agregar residente a la tabla
            vista.agregarResidente(residente);
            estadosValidacion.add(estadoValidacion);

            if (estadoValidacion.startsWith("✅")) {
                validos++;
            } else {
                invalidos++;
            }
        }

        // Configurar renderer y tooltips para mostrar filas en rojo si son inválidas
        configurarVisualizacionValidacion();

        // Agregar/actualizar indicador lateral
        agregarIndicadorLateral(validos, invalidos);

        // Mostrar resumen
        mostrarResumenValidacionVisual(todosLosResidentes.size(), validos, invalidos);
    }

    /**
     * Método para agregar un residente manual y actualizar validación
     */
    public void agregarResidenteManual(ModeloResidente residente) {
        // Agregar a la vista
        vista.agregarResidente(residente);

        // Validar el nuevo residente
        String estadoValidacion = validarResidenteCompleto(residente, estadosValidacion.size() + 1);
        estadosValidacion.add(estadoValidacion);

        // Reconfigurar visualización si hay validaciones activas
        if (!estadosValidacion.isEmpty()) {
            configurarVisualizacionValidacion();

            // Contar válidos e inválidos
            int validos = 0, invalidos = 0;
            for (String estado : estadosValidacion) {
                if (estado.startsWith("✅")) {
                    validos++;
                } else {
                    invalidos++;
                }
            }

            // Actualizar indicador lateral
            agregarIndicadorLateral(validos, invalidos);
        }
    }

    /**
     * Revalidar todos los residentes en la tabla (útil después de cambios en BD)
     */
    public void revalidarTodosLosResidentes() {
        List<ModeloResidente> residentes = vista.getListaResidentes();

        if (residentes.isEmpty()) {
            estadosValidacion.clear();
            removerIndicadorLateral();
            return;
        }

        // Limpiar estados anteriores
        estadosValidacion.clear();

        int validos = 0;
        int invalidos = 0;

        // Validar todos los residentes actuales
        for (int i = 0; i < residentes.size(); i++) {
            ModeloResidente residente = residentes.get(i);
            String estadoValidacion = validarResidenteCompleto(residente, i + 1);
            estadosValidacion.add(estadoValidacion);

            if (estadoValidacion.startsWith("✅")) {
                validos++;
            } else {
                invalidos++;
            }
        }

        // Configurar visualización
        configurarVisualizacionValidacion();

        // Actualizar indicador lateral
        agregarIndicadorLateral(validos, invalidos);
    }

    /**
     * Configurar visualización con tooltips y colores
     */
    private void configurarVisualizacionValidacion() {
        JTable tabla = vista.getTablaResidentes();

        // Configurar renderer personalizado para colorear filas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Colorear fila según estado de validación
                if (row < estadosValidacion.size()) {
                    String estado = estadosValidacion.get(row);

                    if (isSelected) {
                        if (estado.startsWith("✅")) {
                            c.setBackground(new Color(200, 255, 200)); // Verde claro seleccionado
                        } else {
                            c.setBackground(new Color(255, 200, 200)); // Rojo claro seleccionado
                        }
                    } else {
                        if (estado.startsWith("✅")) {
                            c.setBackground(Color.WHITE); // Blanco normal
                        } else {
                            c.setBackground(new Color(255, 235, 235)); // Rojo muy claro
                        }
                    }

                    // Color de texto
                    if (estado.startsWith("✅")) {
                        c.setForeground(new Color(0, 120, 0)); // Verde oscuro
                    } else {
                        c.setForeground(new Color(180, 0, 0)); // Rojo oscuro
                    }
                }

                return c;
            }
        });

        // Configurar tooltips personalizados
        tabla.setToolTipText(""); // Habilitar tooltips básicos

        // Interceptar tooltips para mostrar información específica
        configurarTooltipsPersonalizados(tabla);
    }

    /**
     * Configurar tooltips personalizados con información detallada
     */
    private void configurarTooltipsPersonalizados(JTable tabla) {
        // Crear MouseMotionListener personalizado para tooltips
        tabla.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = tabla.rowAtPoint(e.getPoint());

                if (row >= 0 && row < estadosValidacion.size()) {
                    String estado = estadosValidacion.get(row);
                    String tooltip = crearTooltipDetallado(estado, row);
                    tabla.setToolTipText(tooltip);
                } else {
                    tabla.setToolTipText(null);
                }
            }
        });
    }

    /**
     * Crear tooltip detallado con error específico y ejemplos
     */
    private String crearTooltipDetallado(String estado, int fila) {
        StringBuilder tooltip = new StringBuilder();
        tooltip.append("<html><body style='padding: 10px; max-width: 450px; font-family: Segoe UI;'>");

        if (estado.startsWith("✅")) {
            tooltip.append("<div style='color: #2e7d32; font-weight: bold;'>✅ Registro válido</div><br>");
            tooltip.append("<div style='color: #4caf50;'>Este registro se importará correctamente a la base de datos</div><br>");
            tooltip.append("<div style='color: #666; font-size: 11px;'>💡 Listo para guardar</div>");
        } else {
            tooltip.append("<div style='color: #d32f2f; font-weight: bold;'>❌ Error encontrado:</div><br>");
            tooltip.append("<div style='color: #f44336; background: #ffebee; padding: 5px; border-radius: 3px;'>");
            tooltip.append(estado.substring(2)); // Quitar el "❌ "
            tooltip.append("</div><br>");

            // Agregar ejemplos según el tipo de error
            String error = estado.toLowerCase();

            if (error.contains("número de control")) {
                tooltip.append("<div style='background: #e8f5e8; padding: 8px; border-radius: 5px;'>");
                tooltip.append("<b style='color: #2e7d32;'>📋 Formato correcto:</b><br>");
                tooltip.append("• Exactamente 8 dígitos: <code style='background: #fff; padding: 2px;'>22161063</code><br>");
                tooltip.append("• AA = Año de ingreso (22 = 2022)<br>");
                tooltip.append("• CCCCCC = Número consecutivo<br>");
                tooltip.append("• Año válido: últimos 20 años<br>");
                tooltip.append("</div>");
            } else if (error.contains("ya existe en bd")) {
                tooltip.append("<div style='background: #fff3e0; padding: 8px; border-radius: 5px;'>");
                tooltip.append("<b style='color: #f57c00;'>🔄 Número duplicado:</b><br>");
                tooltip.append("• Este número ya está en la base de datos<br>");
                tooltip.append("• Verifique que sea correcto<br>");
                tooltip.append("• Cada estudiante debe tener un número único<br>");
                tooltip.append("</div>");
            } else if (error.contains("nombre") || error.contains("apellido")) {
                tooltip.append("<div style='background: #e8f5e8; padding: 8px; border-radius: 5px;'>");
                tooltip.append("<b style='color: #2e7d32;'>👤 Formato correcto:</b><br>");
                tooltip.append("• Solo letras: <code style='background: #fff; padding: 2px;'>Juan Carlos</code><br>");
                tooltip.append("• Acentos permitidos: <code style='background: #fff; padding: 2px;'>José María</code><br>");
                tooltip.append("• Espacios permitidos: <code style='background: #fff; padding: 2px;'>Ana Sofía</code><br>");
                tooltip.append("• Mínimo 2 caracteres, máximo 50<br>");
                tooltip.append("</div>");
            } else if (error.contains("carrera")) {
                tooltip.append("<div style='background: #e8f5e8; padding: 8px; border-radius: 5px;'>");
                tooltip.append("<b style='color: #2e7d32;'>🎓 Formato correcto:</b><br>");
                tooltip.append("• Ejemplo: <code style='background: #fff; padding: 2px;'>Ingeniería en Sistemas</code><br>");
                tooltip.append("• Mínimo 3 caracteres<br>");
                tooltip.append("• Nombre completo de la carrera<br>");
                tooltip.append("</div>");
            } else if (error.contains("semestre")) {
                tooltip.append("<div style='background: #e8f5e8; padding: 8px; border-radius: 5px;'>");
                tooltip.append("<b style='color: #2e7d32;'>📚 Valores válidos:</b><br>");
                tooltip.append("• Rango: <code style='background: #fff; padding: 2px;'>1 a 12</code><br>");
                tooltip.append("• Licenciatura: generalmente 1-9<br>");
                tooltip.append("• Maestría: generalmente 1-4<br>");
                tooltip.append("</div>");
            } else if (error.contains("correo")) {
                tooltip.append("<div style='background: #e8f5e8; padding: 8px; border-radius: 5px;'>");
                tooltip.append("<b style='color: #2e7d32;'>📧 Formato correcto:</b><br>");
                tooltip.append("• Ejemplo: <code style='background: #fff; padding: 2px;'>juan.perez@tecnm.mx</code><br>");
                tooltip.append("• Debe contener @ y dominio válido<br>");
                tooltip.append("• Sin puntos consecutivos (..)<br>");
                tooltip.append("• Máximo 254 caracteres<br>");
                tooltip.append("</div>");
            } else if (error.contains("teléfono")) {
                tooltip.append("<div style='background: #e8f5e8; padding: 8px; border-radius: 5px;'>");
                tooltip.append("<b style='color: #2e7d32;'>📱 Formato correcto:</b><br>");
                tooltip.append("• Ejemplo: <code style='background: #fff; padding: 2px;'>4421234567</code><br>");
                tooltip.append("• Entre 8 y 15 dígitos<br>");
                tooltip.append("• Se acepta: <code style='background: #fff; padding: 2px;'>(442) 123-4567</code><br>");
                tooltip.append("• Sin secuencias repetitivas<br>");
                tooltip.append("</div>");
            }

            tooltip.append("<br><div style='color: #666; font-size: 11px; font-style: italic;'>");
            tooltip.append("💡 Corrija este error para que el registro se pueda importar");
            tooltip.append("</div>");
        }

        tooltip.append("</body></html>");
        return tooltip.toString();
    }

    /**
     * Agregar indicador lateral con estadísticas de validación
     */
    private void agregarIndicadorLateral(int validos, int invalidos) {
        // Buscar el panel padre de la tabla (debería ser el JScrollPane)
        JTable tabla = vista.getTablaResidentes();
        Container scrollPane = tabla.getParent().getParent(); // tabla -> viewport -> scrollpane
        Container panelContenido = scrollPane.getParent(); // scrollpane -> panel contenido

        if (panelContenido instanceof JPanel && panelContenido.getLayout() instanceof BorderLayout) {
            // Remover indicador anterior si existe
            Component[] components = panelContenido.getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel && "indicadorValidacion".equals(comp.getName())) {
                    panelContenido.remove(comp);
                    break;
                }
            }

            // Crear nuevo panel indicador
            panelIndicador = crearPanelIndicador(validos, invalidos);
            panelIndicador.setName("indicadorValidacion");

            // Agregar al lado derecho
            panelContenido.add(panelIndicador, BorderLayout.EAST);
            panelContenido.revalidate();
            panelContenido.repaint();
        }
    }

    /**
     * Crear panel indicador con estadísticas visuales
     */
    private JPanel crearPanelIndicador(int validos, int invalidos) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con gradiente suave
                GradientPaint grad = new GradientPaint(
                        0, 0, new Color(248, 250, 252),
                        getWidth(), getHeight(), new Color(241, 245, 249)
                );
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde sutil
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setOpaque(false);

        // Título del indicador
        JLabel lblTitulo = new JLabel("📊 Estado de Validación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(51, 65, 85));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);

        panel.add(Box.createVerticalStrut(15));

        // Estadísticas de válidos
        JLabel lblValidos = new JLabel("✅ Válidos: " + validos);
        lblValidos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblValidos.setForeground(new Color(34, 139, 34));
        panel.add(lblValidos);

        panel.add(Box.createVerticalStrut(8));

        // Estadísticas de inválidos
        JLabel lblInvalidos = new JLabel("❌ Inválidos: " + invalidos);
        lblInvalidos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInvalidos.setForeground(new Color(220, 53, 69));
        panel.add(lblInvalidos);

        panel.add(Box.createVerticalStrut(15));

        // Barra de progreso visual
        JPanel barraProporcional = crearBarraProporcional(validos, invalidos);
        panel.add(barraProporcional);

        panel.add(Box.createVerticalStrut(15));

        // Instrucciones
        JLabel lblInstrucciones = new JLabel("<html><center style='color: #64748b; font-size: 11px;'>" +
                "🔍 Pase el cursor sobre<br>" +
                "las filas para<br>" +
                "ver detalles del error<br><br>" +
                "</center></html>");
        lblInstrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInstrucciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblInstrucciones);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Crear barra proporcional visual
     */
    private JPanel crearBarraProporcional(int validos, int invalidos) {
        int total = validos + invalidos;

        JPanel barra = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (total > 0) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int altura = getHeight();
                    int ancho = getWidth();

                    // Fondo gris
                    g2.setColor(new Color(226, 232, 240));
                    g2.fillRoundRect(0, 0, ancho, altura, altura/2, altura/2);

                    // Parte válida (verde)
                    if (validos > 0) {
                        int anchoValidos = (int) ((double) validos / total * ancho);
                        g2.setColor(new Color(34, 197, 94));
                        g2.fillRoundRect(0, 0, anchoValidos, altura, altura/2, altura/2);
                    }

                    // Parte inválida (roja) - al final
                    if (invalidos > 0) {
                        int anchoInvalidos = (int) ((double) invalidos / total * ancho);
                        int inicioInvalidos = ancho - anchoInvalidos;
                        g2.setColor(new Color(239, 68, 68));
                        g2.fillRoundRect(inicioInvalidos, 0, anchoInvalidos, altura, altura/2, altura/2);
                    }
                }
            }
        };

        barra.setPreferredSize(new Dimension(160, 12));
        barra.setMaximumSize(new Dimension(160, 12));
        barra.setOpaque(false);

        // Tooltip con porcentajes
        if (total > 0) {
            double porcentajeValidos = (double) validos / total * 100;
            double porcentajeInvalidos = (double) invalidos / total * 100;
            barra.setToolTipText(String.format("Válidos: %.1f%% | Inválidos: %.1f%%",
                    porcentajeValidos, porcentajeInvalidos));
        }

        return barra;
    }

    /**
     * Validar un residente completo incluyendo verificación en BD
     */
    private String validarResidenteCompleto(ModeloResidente residente, int fila) {
        try {
            // Validar número de control formato
            String errorNumControl = validarNumeroControl(residente.getNumeroControl(), fila);
            if (errorNumControl != null) return "❌ " + errorNumControl;

            // VERIFICAR SI YA EXISTE EN LA BASE DE DATOS
            if (ModeloResidente.existe(residente.getNumeroControl())) {
                return "❌ Ya existe en BD - Número de control " + residente.getNumeroControl() + " duplicado";
            }

            // Validar nombre
            String errorNombre = validarNombre(residente.getNombre(), fila, "nombre");
            if (errorNombre != null) return "❌ " + errorNombre.substring(errorNombre.indexOf(":") + 2);

            // Validar apellido paterno
            String errorApellidoP = validarNombre(residente.getApellidoPaterno(), fila, "apellido paterno");
            if (errorApellidoP != null) return "❌ " + errorApellidoP.substring(errorApellidoP.indexOf(":") + 2);

            // Validar apellido materno (opcional)
            if (residente.getApellidoMaterno() != null && !residente.getApellidoMaterno().trim().isEmpty()) {
                String errorApellidoM = validarNombre(residente.getApellidoMaterno(), fila, "apellido materno");
                if (errorApellidoM != null) return "❌ " + errorApellidoM.substring(errorApellidoM.indexOf(":") + 2);
            }

            // Validar carrera
            String errorCarrera = validarCarrera(residente.getCarrera(), fila);
            if (errorCarrera != null) return "❌ " + errorCarrera.substring(errorCarrera.indexOf(":") + 2);

            // Validar semestre
            String errorSemestre = validarSemestre(residente.getSemestre(), fila);
            if (errorSemestre != null) return "❌ " + errorSemestre.substring(errorSemestre.indexOf(":") + 2);

            // Validar correo
            String errorCorreo = validarCorreo(residente.getCorreo(), fila);
            if (errorCorreo != null) return "❌ " + errorCorreo.substring(errorCorreo.indexOf(":") + 2);

            // Validar teléfono (opcional)
            if (residente.getTelefono() != null && !residente.getTelefono().trim().isEmpty()) {
                String errorTelefono = validarTelefono(residente.getTelefono(), fila);
                if (errorTelefono != null) return "❌ " + errorTelefono.substring(errorTelefono.indexOf(":") + 2);
            }

            return "✅ VÁLIDO"; // Todo correcto

        } catch (Exception e) {
            return "❌ Error inesperado: " + e.getMessage();
        }
    }

    /**
     * Validar número de control académico
     */
    private String validarNumeroControl(int numeroControl, int fila) {
        String numControlStr = String.valueOf(numeroControl);

        // Verificar longitud exacta de 8 dígitos
        if (numControlStr.length() != 8) {
            return "Fila " + fila + ": Número de control debe tener exactamente 8 dígitos";
        }

        try {
            // Extraer año (primeros 2 dígitos)
            int anio = Integer.parseInt(numControlStr.substring(0, 2));

            // Validar rango de años válidos
            int anioActual = java.time.Year.now().getValue() % 100;
            int anioMinimo = (anioActual - 20 + 100) % 100;
            int anioMaximo = (anioActual + 2) % 100;

            boolean anioValido = false;
            if (anioMinimo <= anioMaximo) {
                anioValido = (anio >= anioMinimo && anio <= anioMaximo);
            } else {
                anioValido = (anio >= anioMinimo || anio <= anioMaximo);
            }

            if (!anioValido) {
                return "Fila " + fila + ": Número de control con año inválido (20" + String.format("%02d", anio) + ")";
            }

            // Validar consecutivo
            String consecutivo = numControlStr.substring(2);
            if (consecutivo.equals("000000")) {
                return "Fila " + fila + ": Número de control con consecutivo inválido (000000)";
            }

            if (consecutivo.startsWith("00")) {
                return "Fila " + fila + ": Número de control con consecutivo sospechoso (empieza con 00)";
            }

        } catch (Exception e) {
            return "Fila " + fila + ": Error al procesar número de control";
        }

        return null;
    }

    /**
     * Validar nombres y apellidos
     */
    private String validarNombre(String nombre, int fila, String campo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Fila " + fila + ": " + campo + " es obligatorio";
        }

        String nombreLimpio = nombre.trim();

        if (nombreLimpio.length() < 2) {
            return "Fila " + fila + ": " + campo + " muy corto (mínimo 2 caracteres)";
        }

        if (nombreLimpio.length() > 50) {
            return "Fila " + fila + ": " + campo + " muy largo (máximo 50 caracteres)";
        }

        if (!nombreLimpio.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            return "Fila " + fila + ": " + campo + " contiene caracteres inválidos (solo letras y espacios)";
        }

        return null;
    }

    /**
     * Validar carrera
     */
    private String validarCarrera(String carrera, int fila) {
        if (carrera == null || carrera.trim().isEmpty()) {
            return "Fila " + fila + ": Carrera es obligatoria";
        }

        if (carrera.trim().length() < 3) {
            return "Fila " + fila + ": Carrera muy corta (mínimo 3 caracteres)";
        }

        return null;
    }

    /**
     * Validar semestre
     */
    private String validarSemestre(int semestre, int fila) {
        if (semestre < 1 || semestre > 12) {
            return "Fila " + fila + ": Semestre inválido (debe estar entre 1 y 12)";
        }

        return null;
    }

    /**
     * Validar correo electrónico
     */
    private String validarCorreo(String correo, int fila) {
        if (correo == null || correo.trim().isEmpty()) {
            return "Fila " + fila + ": Correo electrónico es obligatorio";
        }

        String correoLimpio = correo.trim();

        // Expresión regular para correo
        String patronEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (!correoLimpio.matches(patronEmail)) {
            return "Fila " + fila + ": Correo electrónico con formato inválido";
        }

        if (correoLimpio.length() > 254) {
            return "Fila " + fila + ": Correo electrónico muy largo (máximo 254 caracteres)";
        }

        if (correoLimpio.startsWith(".") || correoLimpio.endsWith(".")) {
            return "Fila " + fila + ": Correo electrónico no puede empezar o terminar con punto";
        }

        if (correoLimpio.contains("..")) {
            return "Fila " + fila + ": Correo electrónico no puede tener puntos consecutivos";
        }

        return null;
    }

    /**
     * Validar teléfono
     */
    private String validarTelefono(String telefono, int fila) {
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");

        if (telefonoLimpio.length() < 8 || telefonoLimpio.length() > 15) {
            return "Fila " + fila + ": Teléfono con longitud inválida (debe tener 8-15 dígitos)";
        }

        // Verificar patrones sospechosos
        if (telefonoLimpio.matches("(\\d)\\1{7,}")) {
            return "Fila " + fila + ": Teléfono con patrón repetitivo sospechoso";
        }

        if (telefonoLimpio.matches("12345678.*") || telefonoLimpio.matches("87654321.*")) {
            return "Fila " + fila + ": Teléfono con secuencia sospechosa";
        }

        return null;
    }

    /**
     * Mostrar resumen de validación visual
     */
    private void mostrarResumenValidacionVisual(int total, int validos, int invalidos) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("📊 Validación completada:\n\n");
        mensaje.append("✅ Registros válidos: ").append(validos).append("\n");
        mensaje.append("❌ Registros inválidos: ").append(invalidos).append("\n");
        mensaje.append("📋 Total procesados: ").append(total).append("\n\n");

        mensaje.append("🎨 Visualización:\n");
        mensaje.append("• Panel lateral muestra estadísticas\n");

        if (validos > 0) {
            mensaje.append("💡 Use 'Importar' para guardar solo los válidos");
        } else {
            mensaje.append("⚠️ No hay registros válidos para importar");
        }

        int tipoMensaje = (validos > 0) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
        String titulo = (invalidos == 0) ? "✅ Validación exitosa" : "⚠️ Validación con errores";

        JOptionPane.showMessageDialog(vista.getPanelResidente(), mensaje.toString(), titulo, tipoMensaje);
    }

    /**
     * Importar datos actuales de la tabla a la base de datos (solo los válidos)
     */
    public void importarABaseDatos() {
        List<ModeloResidente> listaResidentes = vista.getListaResidentes();

        // Verificar que hay datos en la tabla
        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "⚠️ No hay datos para importar\n" +
                            "💡 Primero use 'Cargar Excel' para cargar datos en la tabla",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Filtrar solo los registros válidos
        List<ModeloResidente> residentesValidos = new ArrayList<>();

        for (int i = 0; i < listaResidentes.size() && i < estadosValidacion.size(); i++) {
            if (estadosValidacion.get(i).startsWith("✅")) {
                residentesValidos.add(listaResidentes.get(i));
            }
        }

        if (residentesValidos.isEmpty()) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "⚠️ No hay registros válidos para importar\n" +
                            "💡 Todos los registros tienen errores de validación\n" +
                            "🔍 Pase el cursor sobre las filas para ver los errores\n" +
                            "📊 Revise el panel lateral para más información",
                    "Sin registros válidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar la importación solo de registros válidos
        int totalInvalidos = listaResidentes.size() - residentesValidos.size();
        String mensajeConfirmacion = "¿Confirma importar solo los registros válidos?\n\n" +
                "✅ Registros válidos a importar: " + residentesValidos.size() + "\n" +
                "❌ Registros inválidos (se omitirán): " + totalInvalidos + "\n\n" +
                "⚡ Solo se guardarán los registros válidos en la BD\n" ;

        int opcion = JOptionPane.showConfirmDialog(vista.getPanelResidente(),
                mensajeConfirmacion,
                "Confirmar importación selectiva",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                // Cambiar cursor a espera
                vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Crear diálogo de procesamiento
                JDialog processingDialog = crearDialogoProcesamiento(residentesValidos.size());

                // Realizar importación en hilo separado
                SwingWorker<ModeloResidente.ResultadoImportacion, Void> worker =
                        new SwingWorker<ModeloResidente.ResultadoImportacion, Void>() {

                            @Override
                            protected ModeloResidente.ResultadoImportacion doInBackground() throws Exception {
                                // Importar solo los residentes válidos
                                return ModeloResidente.importarResidentes(residentesValidos);
                            }

                            @Override
                            protected void done() {
                                try {
                                    // Cerrar diálogo de procesamiento
                                    processingDialog.dispose();

                                    // Obtener resultado
                                    ModeloResidente.ResultadoImportacion resultado = get();

                                    // Mostrar resultado
                                    mostrarResultadoImportacionSelectiva(resultado, totalInvalidos);

                                    // Si fue exitoso, limpiar tabla y remover indicador
                                    if (resultado.getExitosos() > 0) {
                                        vista.limpiarTabla();
                                        estadosValidacion.clear();
                                        removerIndicadorLateral();
                                    }

                                } catch (Exception e) {
                                    processingDialog.dispose();
                                    JOptionPane.showMessageDialog(vista.getPanelResidente(),
                                            "❌ Error durante la importación:\n" + e.getMessage(),
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    vista.setCursor(Cursor.getDefaultCursor());
                                }
                            }
                        };

                // Mostrar diálogo y ejecutar worker
                SwingUtilities.invokeLater(() -> processingDialog.setVisible(true));
                worker.execute();

            } catch (Exception e) {
                vista.setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "❌ Error al iniciar la importación:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Remover indicador lateral
     */
    private void removerIndicadorLateral() {
        if (panelIndicador != null) {
            Container parent = panelIndicador.getParent();
            if (parent != null) {
                parent.remove(panelIndicador);
                parent.revalidate();
                parent.repaint();
            }
            panelIndicador = null;
        }
    }

    /**
     * Mostrar resultado de importación selectiva
     */
    private void mostrarResultadoImportacionSelectiva(ModeloResidente.ResultadoImportacion resultado, int invalidosOmitidos) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("🚀 Importación selectiva completada:\n\n");
        mensaje.append("✅ Registros importados: ").append(resultado.getExitosos()).append("\n");
        mensaje.append("❌ Registros fallidos: ").append(resultado.getFallidos()).append("\n");
        mensaje.append("🔄 Registros duplicados: ").append(resultado.getDuplicados()).append("\n");
        mensaje.append("⚠️ Registros omitidos (inválidos): ").append(invalidosOmitidos).append("\n");
        mensaje.append("📊 Total procesados para BD: ").append(resultado.getTotal()).append("\n");

        // Calcular tasa de éxito
        double tasaExito = resultado.getTotal() > 0 ?
                (double) resultado.getExitosos() / resultado.getTotal() * 100 : 0;
        mensaje.append("📈 Tasa de éxito en BD: ").append(String.format("%.1f%%", tasaExito));

        int tipoMensaje = (resultado.getExitosos() > 0) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        String titulo = (resultado.getExitosos() > 0) ? "✅ Importación exitosa" : "❌ Importación fallida";

        JOptionPane.showMessageDialog(vista.getPanelResidente(), mensaje.toString(), titulo, tipoMensaje);
    }

    /**
     * Crear diálogo de procesamiento
     */
    private JDialog crearDialogoProcesamiento(int totalRegistros) {
        JOptionPane processingPane = new JOptionPane(
                "🔄 Procesando importación...\n" +
                        "📊 Registros válidos: " + totalRegistros + "\n" +
                        "⏳ Por favor espere...",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{},
                null
        );

        return processingPane.createDialog(vista.getPanelResidente(), "Importando");
    }

    /**
     * Exportar datos actuales a Excel
     */
    public void exportarAExcel() {
        List<ModeloResidente> listaResidentes = vista.getListaResidentes();

        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "⚠️ No hay datos para exportar",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            java.io.File archivo = ExcelHandler.seleccionarUbicacionGuardar();
            if (archivo != null) {
                ExcelHandler.exportarAExcel(listaResidentes, archivo);

                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "✅ Archivo exportado exitosamente\n" +
                                "📁 Ubicación: " + archivo.getAbsolutePath(),
                        "Exportación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "❌ Error al exportar:\n" + e.getMessage(),
                    "Error de exportación",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Buscar un residente por número de control
     */
    public ModeloResidente buscarResidentePorNumeroControl(int numeroControl) {
        return ModeloResidente.buscarPorNumeroControl(numeroControl);
    }

    /**
     * Obtener todos los residentes de la base de datos
     */
    public List<ModeloResidente> obtenerTodosLosResidentes() {
        return ModeloResidente.obtenerTodos();
    }

    /**
     * Verificar si un residente existe en la base de datos
     */
    public boolean existeResidente(int numeroControl) {
        return ModeloResidente.existe(numeroControl);
    }

    /**
     * Cargar residentes desde la base de datos a la tabla
     */
    public void cargarResidentesDesdeBaseDatos() {
        try {
            vista.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            List<ModeloResidente> residentes = ModeloResidente.obtenerTodos();

            if (residentes != null && !residentes.isEmpty()) {
                vista.cargarResidentes(residentes);

                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "✅ Residentes cargados desde la base de datos\n" +
                                "📊 Total de registros: " + residentes.size(),
                        "Carga exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista.getPanelResidente(),
                        "⚠️ No se encontraron residentes en la base de datos",
                        "Sin datos",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista.getPanelResidente(),
                    "❌ Error al cargar residentes desde la base de datos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            vista.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Limpiar la tabla de residentes
     */
    public void limpiarTabla() {
        vista.limpiarTabla();
        estadosValidacion.clear();
        removerIndicadorLateral();
    }

    /**
     * Obtener el residente seleccionado en la tabla
     */
    public ModeloResidente obtenerResidenteSeleccionado() {
        return vista.getResidenteSeleccionado();
    }
}