package Vista.VistaResidentes;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import Modelo.ModeloResidente;
import Controlador.ControladorResidente;

public class VistaResidente {
    private JPanel panelPrincipal;
    private DefaultTableModel modeloTabla;
    private JTable tablaResidentes;
    private final Color colorPrincipal = new Color(92, 93, 169);
    private final Color colorFondo = new Color(245, 243, 255);
    private final Color colorExito = new Color(76, 175, 80);
    private final Color colorError = new Color(244, 67, 54);
    private final Color colorAdvertencia = new Color(255, 152, 0);
    private final Color colorEliminar = new Color(229, 115, 115);

    private List<ModeloResidente> listaResidentes = new ArrayList<>();
    private List<Boolean> validacionEstados = new ArrayList<>();
    private ControladorResidente controlador;

    public VistaResidente() {
        controlador = new ControladorResidente(this);
        crearPanelPrincipal();
    }

    private void crearPanelPrincipal() {
        panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(230, 225, 255),
                        0, getHeight(), colorPrincipal
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(new Color(180, 170, 255, 80));
                g2.drawArc(-120, -120, 300, 300, 0, 360);
                g2.setColor(new Color(140, 130, 220, 60));
                g2.drawArc(-80, -80, 200, 200, 0, 360);

                g2.setColor(new Color(180, 170, 255, 60));
                g2.drawArc(getWidth() - 180, getHeight() - 180, 160, 160, 0, 360);
                g2.setColor(new Color(140, 130, 220, 40));
                g2.drawArc(getWidth() - 120, getHeight() - 120, 100, 100, 0, 360);
            }
        };
        panelPrincipal.setBackground(colorFondo);

        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                g2.setColor(new Color(230, 230, 250));
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(32, 38, 24, 38));

        JLabel lblTitulo = new JLabel("Gestión de Residentes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(colorPrincipal);
        header.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelBotones.setOpaque(false);

        JButton btnCargarExcel = crearBotonAccion("📄 Cargar Excel", new Color(103, 58, 183));
        btnCargarExcel.addActionListener(e -> cargarExcelCompleto());
        panelBotones.add(btnCargarExcel);

        JButton btnImportar = crearBotonAccion("📥 Importar", colorExito);
        btnImportar.addActionListener(e -> importarSoloValidos());
        panelBotones.add(btnImportar);

        JButton btnAgregarManual = crearBotonAccion("➕ Agregar Manual", new Color(33, 150, 243));
        btnAgregarManual.addActionListener(e -> abrirAgregarManual());
        panelBotones.add(btnAgregarManual);

        JButton btnEliminarSeleccionado = crearBotonAccion("🗑️ Eliminar", colorEliminar);
        btnEliminarSeleccionado.addActionListener(e -> eliminarResidenteSeleccionado());
        panelBotones.add(btnEliminarSeleccionado);

        JButton btnLimpiarTabla = crearBotonAccion("🧹 Limpiar", new Color(158, 158, 158));
        btnLimpiarTabla.addActionListener(e -> limpiarTablaConConfirmacion());
        panelBotones.add(btnLimpiarTabla);

        header.add(panelBotones, BorderLayout.EAST);
        panelContenido.add(header, BorderLayout.NORTH);

        configurarTabla();

        JScrollPane scrollTabla = new JScrollPane(tablaResidentes);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 38, 38, 38));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = colorPrincipal;
                this.trackColor = new Color(235, 235, 250);
            }
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        panelContenido.add(scrollTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
    }

    private JButton crearBotonAccion(String texto, Color color) {
        return new JButton(texto) {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 5, getWidth()-6, getHeight()-3, 25, 25);
                GradientPaint grad = new GradientPaint(0, 0, hover ? color.darker() : color,
                        0, getHeight(), color.brighter());
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
    }

    /**
     * MEJORADO: Configuración de tabla con tooltips de errores
     */
    private void configurarTabla() {
        String[] columnas = {
                "Estado", "No. Control", "Nombre", "Apellido P.", "Apellido M.",
                "Semestre", "Correo", "Telefono"
        };

        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return String.class;
                if (columnIndex == 1 || columnIndex == 5) return Integer.class;
                return String.class;
            }
        };

        tablaResidentes = new JTable(modeloTabla) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            private int hoveredRow = -1;

            {
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setRowSelectionAllowed(true);
                setColumnSelectionAllowed(false);
                getTableHeader().setReorderingAllowed(false);
                setFocusable(true);

                // NUEVO: Configurar tooltips con errores de validación
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent e) {
                        int row = rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row;
                            repaint();
                        }

                        // MEJORADO: TOOLTIP CON TODOS LOS ERRORES DE VALIDACIÓN
                        if (row >= 0 && row < listaResidentes.size()) {
                            ModeloResidente residente = listaResidentes.get(row);

                            // Crear una copia temporal para validar sin modificar el original
                            ModeloResidente residenteTemporal = new ModeloResidente(residente);

                            // Validar completamente y recopilar TODOS los errores
                            List<String> todosLosErrores = validarCompletoConErrores(residenteTemporal, row);

                            if (!todosLosErrores.isEmpty()) {
                                // Crear tooltip con TODOS los errores
                                StringBuilder tooltip = new StringBuilder("<html>");
                                tooltip.append("<div style='padding: 8px; max-width: 400px; font-family: Arial, sans-serif;'>");
                                tooltip.append("<b style='color: #d32f2f; font-size: 14px;'>⚠️ Errores encontrados:</b><br><br>");

                                for (String error : todosLosErrores) {
                                    tooltip.append("<span style='color: #333; font-size: 12px;'>• ").append(error).append("</span><br>");
                                }

                                tooltip.append("<br><i style='color: #666; font-size: 11px;'>💡 Doble click para editar y corregir</i>");
                                tooltip.append("</div></html>");

                                setToolTipText(tooltip.toString());
                            } else {
                                setToolTipText("<html><div style='padding: 5px; font-family: Arial, sans-serif;'>" +
                                        "<b style='color: #2e7d32; font-size: 14px;'>✅ Registro válido</b><br>" +
                                        "<i style='color: #666; font-size: 11px;'>💡 Doble click para editar</i>" +
                                        "</div></html>");
                            }
                        } else {
                            setToolTipText(null);
                        }
                    }
                });

                addMouseListener(new MouseAdapter() {
                    public void mouseExited(MouseEvent e) {
                        hoveredRow = -1;
                        setToolTipText(null);
                        repaint();
                    }

                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int row = rowAtPoint(e.getPoint());
                            if (row >= 0) {
                                editarResidente(row);
                            }
                        }
                    }
                });

                // NUEVO: Personalizar tiempo de aparición del tooltip
                ToolTipManager.sharedInstance().setInitialDelay(300);
                ToolTipManager.sharedInstance().setDismissDelay(10000);
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                if (isRowSelected(row)) {
                    c.setBackground(new Color(220, 219, 245));
                } else if (row == hoveredRow) {
                    c.setBackground(new Color(240, 240, 255));
                } else {
                    c.setBackground(Color.WHITE);
                }

                // Color especial para columna de estado
                if (column == 0) {
                    String estado = (String) getValueAt(row, 0);
                    if ("✅".equals(estado)) {
                        c.setForeground(colorExito);
                    } else if ("❌".equals(estado)) {
                        c.setForeground(colorError);
                    } else {
                        c.setForeground(colorAdvertencia);
                    }
                } else {
                    c.setForeground(new Color(60, 60, 100));
                }

                return c;
            }

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                // Posicionar tooltip cerca del cursor pero sin obstruir
                Point p = e.getPoint();
                return new Point(p.x + 15, p.y - 10);
            }
        };

        tablaResidentes.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tablaResidentes.setRowHeight(40);
        tablaResidentes.setShowVerticalLines(false);
        tablaResidentes.setShowHorizontalLines(true);
        tablaResidentes.setGridColor(new Color(230, 230, 250));
        tablaResidentes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tablaResidentes.getTableHeader().setBackground(new Color(220, 219, 245));
        tablaResidentes.getTableHeader().setForeground(colorPrincipal);
        tablaResidentes.getTableHeader().setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) tablaResidentes.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Configurar anchos de columna
        tablaResidentes.getColumnModel().getColumn(0).setPreferredWidth(60);  // Estado
        tablaResidentes.getColumnModel().getColumn(1).setPreferredWidth(100); // No. Control
        tablaResidentes.getColumnModel().getColumn(2).setPreferredWidth(120); // Nombre
        tablaResidentes.getColumnModel().getColumn(3).setPreferredWidth(120); // Apellido P.
        tablaResidentes.getColumnModel().getColumn(4).setPreferredWidth(120); // Apellido M.
        tablaResidentes.getColumnModel().getColumn(5).setPreferredWidth(80);  // Semestre
        tablaResidentes.getColumnModel().getColumn(6).setPreferredWidth(200); // Correo
        tablaResidentes.getColumnModel().getColumn(7).setPreferredWidth(120); // Telefono
    }

    private String formatearTexto(String texto) {
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

    /**
     * MEJORADO: Validación que incluye duplicados en previsualización y BD
     */
    private boolean validarResidente(ModeloResidente residente) {
        return validarResidenteEnFila(residente, -1); // -1 significa que no hay fila específica
    }
    /**
     * NUEVO: Validar completamente y recopilar TODOS los errores
     */
    private List<String> validarCompletoConErrores(ModeloResidente residente, int filaEnTabla) {
        List<String> errores = new ArrayList<>();

        // 1. VALIDAR NÚMERO DE CONTROL
        if (residente.getNumeroControl() <= 0) {
            errores.add("❌ Número de control inválido o vacío");
        } else {
            String numStr = String.valueOf(residente.getNumeroControl());
            if (numStr.length() != 8) {
                errores.add("❌ Número de control debe tener 8 dígitos (tiene " + numStr.length() + ")");
            } else {
                // Validar años futuros
                try {
                    int anio = Integer.parseInt(numStr.substring(0, 2));
                    int anioActual = java.time.Year.now().getValue() % 100;
                    if (anio > anioActual) {
                        errores.add("❌ Año futuro no permitido (" + (2000 + anio) + ")");
                    }
                } catch (Exception e) {
                    errores.add("❌ Error en formato del número de control");
                }
            }

            // Verificar duplicados en previsualización (excluyendo esta fila)
            boolean duplicadoEnPrevia = false;
            for (int i = 0; i < listaResidentes.size(); i++) {
                if (i != filaEnTabla && listaResidentes.get(i).getNumeroControl() == residente.getNumeroControl()) {
                    duplicadoEnPrevia = true;
                    break;
                }
            }
            if (duplicadoEnPrevia) {
                errores.add("❌ Número de control duplicado en esta carga");
            }

            // Verificar si existe en BD
            try {
                if (ModeloResidente.existe(residente.getNumeroControl())) {
                    errores.add("❌ Ya existe en la base de datos");
                }
            } catch (Exception e) {
                // Ignorar errores de BD para tooltips
            }
        }

        // 2. VALIDAR NOMBRE
        if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) {
            errores.add("❌ Nombre es obligatorio");
        } else if (residente.getNombre().trim().length() < 2) {
            errores.add("❌ Nombre debe tener al menos 2 caracteres");
        } else if (!residente.getNombre().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            errores.add("❌ Nombre contiene caracteres inválidos");
        }

        // 3. VALIDAR APELLIDO PATERNO
        if (residente.getApellidoPaterno() == null || residente.getApellidoPaterno().trim().isEmpty()) {
            errores.add("❌ Apellido paterno es obligatorio");
        } else if (residente.getApellidoPaterno().trim().length() < 2) {
            errores.add("❌ Apellido paterno debe tener al menos 2 caracteres");
        } else if (!residente.getApellidoPaterno().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            errores.add("❌ Apellido paterno contiene caracteres inválidos");
        }

        // 4. VALIDAR APELLIDO MATERNO (opcional)
        if (residente.getApellidoMaterno() != null && !residente.getApellidoMaterno().trim().isEmpty()) {
            if (residente.getApellidoMaterno().trim().length() < 2) {
                errores.add("⚠️ Apellido materno debe tener al menos 2 caracteres");
            } else if (!residente.getApellidoMaterno().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                errores.add("❌ Apellido materno contiene caracteres inválidos");
            }
        }

        // 5. VALIDAR SEMESTRE
        if (residente.getSemestre() < 9 || residente.getSemestre() > 15) {
            errores.add("❌ Semestre debe estar entre 9 y 15 (actual: " + residente.getSemestre() + ")");
        }

        // 6. VALIDAR CORREO
        if (residente.getCorreo() == null || residente.getCorreo().trim().isEmpty()) {
            errores.add("❌ Correo electrónico es obligatorio");
        } else if (!residente.getCorreo().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            errores.add("❌ Formato de correo inválido");
        } else if (residente.getCorreo().length() > 254) {
            errores.add("❌ Correo demasiado largo (máximo 254 caracteres)");
        }

        // 7. VALIDAR TELÉFONO (opcional)
        if (residente.getTelefono() != null && !residente.getTelefono().trim().isEmpty()) {
            String telefonoLimpio = residente.getTelefono().replaceAll("[^0-9]", "");

            if (telefonoLimpio.length() < 8) {
                errores.add("❌ Teléfono debe tener al menos 8 dígitos");
            } else if (telefonoLimpio.length() > 15) {
                errores.add("❌ Teléfono no puede tener más de 15 dígitos");
            } else if (telefonoLimpio.matches("(\\d)\\1{7,}")) {
                errores.add("⚠️ Teléfono tiene demasiados dígitos repetidos");
            } else if (telefonoLimpio.matches("12345678.*") || telefonoLimpio.matches("87654321.*")) {
                errores.add("⚠️ Teléfono parece ser secuencial");
            }
        }

        return errores;
    }

    /**
     * NUEVO: Verificar si el número de control ya existe en la lista de previsualización
     */
    private boolean verificarDuplicadoEnPrevia(ModeloResidente residenteNuevo) {
        if (residenteNuevo.getNumeroControl() <= 0) {
            return false; // No verificar duplicados si el número es inválido
        }

        long contador = listaResidentes.stream()
                .filter(r -> r.getNumeroControl() == residenteNuevo.getNumeroControl())
                .count();

        return contador > 0;
    }

    /**
     * NUEVO: Validar solo el formato, sin verificar BD
     */
    private boolean validarFormatoResidente(ModeloResidente residente) {
        boolean esValido = true;

        // Validar campos básicos sin consultar BD
        if (residente.getNumeroControl() <= 0) esValido = false;
        if (residente.getNombre() == null || residente.getNombre().trim().isEmpty()) esValido = false;
        if (residente.getApellidoPaterno() == null || residente.getApellidoPaterno().trim().isEmpty()) esValido = false;
        if (residente.getCorreo() == null || residente.getCorreo().trim().isEmpty()) esValido = false;
        if (residente.getSemestre() < 9 || residente.getSemestre() > 15) esValido = false;

        // Validar formato de correo
        if (residente.getCorreo() != null && !residente.getCorreo().isEmpty()) {
            if (!residente.getCorreo().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                esValido = false;
            }
        }

        return esValido;
    }

    public void agregarResidente(ModeloResidente residente) {
        listaResidentes.add(residente);

        // MEJORADO: Validar incluyendo duplicados
        boolean esValido = validarResidente(residente);
        validacionEstados.add(esValido);

        Object[] fila = {
                esValido ? "✅" : "❌",
                residente.getNumeroControl(),
                formatearTexto(residente.getNombre()),
                formatearTexto(residente.getApellidoPaterno()),
                formatearTexto(residente.getApellidoMaterno()),
                residente.getSemestre(),
                residente.getCorreo(),
                residente.getTelefono()
        };
        modeloTabla.addRow(fila);
    }

    public void cargarResidentes(List<ModeloResidente> residentes) {
        limpiarTabla();
        for (ModeloResidente residente : residentes) {
            agregarResidente(residente);
        }

        mostrarResumenCarga();
    }

    /**
     * MEJORADO: Resumen actualizado para mostrar TODOS los registros
     */
    private void mostrarResumenCarga() {
        int validos = 0;
        int invalidos = 0;

        for (Boolean estado : validacionEstados) {
            if (estado) {
                validos++;
            } else {
                invalidos++;
            }
        }

        String mensaje = "📊 Registros cargados desde Excel:\n\n" +
                "📋 Total cargados: " + listaResidentes.size() + "\n" +
                "✅ Válidos (listos para BD): " + validos + "\n" +
                "❌ Con errores (solo previsualización): " + invalidos;

        if (invalidos > 0) {
            mensaje += "\n\n💡 TODOS los registros se muestran en la tabla\n" +
                    "🔍 Pase el mouse sobre ❌ para ver errores específicos\n" +
                    "✏️ Doble click para editar cualquier registro\n" +
                    "📥 Solo los ✅ se importarán a la base de datos";
        } else {
            mensaje += "\n\n🎉 Todos los registros son válidos y listos para importar";
        }

        JOptionPane.showMessageDialog(panelPrincipal, mensaje,
                "Carga completada", JOptionPane.INFORMATION_MESSAGE);
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
        listaResidentes.clear();
        validacionEstados.clear();
    }

    private void cargarExcelCompleto() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            List<ModeloResidente> residentes = Util.ExcelHandler.importarExcelCompleto();

            if (residentes != null && !residentes.isEmpty()) {
                cargarResidentes(residentes);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No se encontraron datos válidos en el archivo Excel",
                        "Sin datos",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar Excel: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void importarSoloValidos() {
        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay registros para importar.\nPrimero cargue un archivo Excel.",
                    "Sin datos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<ModeloResidente> residentesValidos = new ArrayList<>();
        for (int i = 0; i < listaResidentes.size(); i++) {
            if (i < validacionEstados.size() && validacionEstados.get(i)) {
                residentesValidos.add(listaResidentes.get(i));
            }
        }

        if (residentesValidos.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay registros válidos para importar.\nRevise los errores usando el mouse sobre los registros ❌",
                    "Sin registros válidos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int totalInvalidos = listaResidentes.size() - residentesValidos.size();
        String mensaje = "¿Confirma importar solo los registros válidos?\n\n" +
                "✅ Válidos a importar: " + residentesValidos.size() + "\n" +
                "❌ Con errores (se omitirán): " + totalInvalidos;

        int opcion = JOptionPane.showConfirmDialog(panelPrincipal, mensaje,
                "Confirmar importación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                ModeloResidente.ResultadoImportacion resultado =
                        ModeloResidente.importarResidentes(residentesValidos);

                mostrarResultadoImportacion(resultado, totalInvalidos);

                if (resultado.getExitosos() > 0) {
                    limpiarTabla();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Error durante la importación: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * MEJORADO: Resultado más conciso
     */
    private void mostrarResultadoImportacion(ModeloResidente.ResultadoImportacion resultado, int invalidosOmitidos) {
        String mensaje = "🚀 Importación completada:\n\n" +
                "✅ Importados: " + resultado.getExitosos() + "\n" +
                "❌ Fallidos: " + resultado.getFallidos() + "\n" +
                "🔄 Duplicados: " + resultado.getDuplicados() + "\n" +
                "⚠️ Omitidos: " + invalidosOmitidos;

        int tipoMensaje = resultado.getExitosos() > 0 ?
                JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

        JOptionPane.showMessageDialog(panelPrincipal, mensaje,
                "Resultado de importación", tipoMensaje);
    }

    private void eliminarResidenteSeleccionado() {
        int filaSeleccionada = tablaResidentes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String nombreCompleto = listaResidentes.get(filaSeleccionada).getNombre() + " " +
                    listaResidentes.get(filaSeleccionada).getApellidoPaterno();

            int opcion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Eliminar el registro de " + nombreCompleto + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                modeloTabla.removeRow(filaSeleccionada);
                listaResidentes.remove(filaSeleccionada);
                validacionEstados.remove(filaSeleccionada);

                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Registro eliminado",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un registro para eliminar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarResidente(int row) {
        if (row >= 0 && row < listaResidentes.size()) {
            ModeloResidente residente = listaResidentes.get(row);
            Window parentWindow = SwingUtilities.getWindowAncestor(panelPrincipal);
            Frame parentFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;

            DialogoEditarResidente dialogo = new DialogoEditarResidente(parentFrame, residente);
            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {
                ModeloResidente residenteEditado = dialogo.getResidente();

                // CORREGIDO: Limpiar errores anteriores antes de validar
                residenteEditado.getErroresValidacion().clear();

                // Reemplazar en la lista
                listaResidentes.set(row, residenteEditado);

                // *** SOLUCIÓN: Revalidar TODOS los registros después de editar ***
                // Esto es necesario porque cambiar un número de control puede hacer que
                // otros registros que antes eran duplicados ahora sean válidos
                revalidarTodosLosRegistros();

                // CORREGIDO: Actualizar TODA la fila en la tabla con los nuevos datos
                boolean esValido = validacionEstados.get(row); // Obtener el estado ya calculado
                modeloTabla.setValueAt(esValido ? "✅" : "❌", row, 0);
                modeloTabla.setValueAt(residenteEditado.getNumeroControl(), row, 1);
                modeloTabla.setValueAt(formatearTexto(residenteEditado.getNombre()), row, 2);
                modeloTabla.setValueAt(formatearTexto(residenteEditado.getApellidoPaterno()), row, 3);
                modeloTabla.setValueAt(formatearTexto(residenteEditado.getApellidoMaterno()), row, 4);
                modeloTabla.setValueAt(residenteEditado.getSemestre(), row, 5);
                modeloTabla.setValueAt(residenteEditado.getCorreo(), row, 6);
                modeloTabla.setValueAt(residenteEditado.getTelefono(), row, 7);

                // Forzar repintado de la tabla
                tablaResidentes.repaint();

                // Mostrar mensaje de éxito con estado actualizado
                String estadoMensaje = esValido ? "✅ válido" : "❌ con errores";
                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Registro actualizado correctamente (" + estadoMensaje + ")",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * NUEVO: Método para revalidar TODOS los registros
     * Necesario después de editar porque cambiar un número de control puede afectar
     * la validación de otros registros (duplicados)
     */
    private void revalidarTodosLosRegistros() {
        // Limpiar estados anteriores
        validacionEstados.clear();

        // Revalidar cada registro
        for (int i = 0; i < listaResidentes.size(); i++) {
            ModeloResidente residente = listaResidentes.get(i);
            boolean esValido = validarResidenteEnFila(residente, i);
            validacionEstados.add(esValido);

            // Actualizar el estado en la tabla
            if (i < modeloTabla.getRowCount()) {
                modeloTabla.setValueAt(esValido ? "✅" : "❌", i, 0);
            }
        }
    }

    /**
     * NUEVO: Validación específica para una fila (útil durante edición)
     */
    private boolean validarResidenteEnFila(ModeloResidente residente, int filaActual) {
        try {
            // Primero validar formato básico
            boolean formatoValido = validarFormatoResidente(residente);

            // Verificar duplicados en la lista de previsualización (excluyendo la fila actual)
            boolean duplicadoEnLista = verificarDuplicadoEnPreviaExcluyendoFila(residente, filaActual);

            // Verificar si existe en BD
            boolean existeEnBD = false;
            try {
                existeEnBD = ModeloResidente.existe(residente.getNumeroControl());
            } catch (Exception e) {
                existeEnBD = false;
            }

            return formatoValido && !duplicadoEnLista && !existeEnBD;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * MEJORADO: Verificar duplicados excluyendo una fila específica
     */
    private boolean verificarDuplicadoEnPreviaExcluyendoFila(ModeloResidente residenteNuevo, int filaExcluir) {
        if (residenteNuevo.getNumeroControl() <= 0) {
            return false;
        }

        for (int i = 0; i < listaResidentes.size(); i++) {
            if (i != filaExcluir && listaResidentes.get(i).getNumeroControl() == residenteNuevo.getNumeroControl()) {
                return true;
            }
        }
        return false;
    }


    private void abrirAgregarManual() {
        Window parentWindow = SwingUtilities.getWindowAncestor(panelPrincipal);
        Frame parentFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;

        AgregarManual dialogo = new AgregarManual(parentFrame);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "✅ Residente guardado en la base de datos",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarTablaConConfirmacion() {
        if (listaResidentes.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "La tabla ya está vacía",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Limpiar toda la tabla?\nSe eliminarán " + listaResidentes.size() + " registro(s).",
                "Confirmar limpieza",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            limpiarTabla();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "✅ Tabla limpiada",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ==================== GETTERS Y MÉTODOS DE UTILIDAD ====================

    public List<ModeloResidente> getListaResidentes() {
        return new ArrayList<>(listaResidentes);
    }

    public ModeloResidente getResidenteSeleccionado() {
        int filaSeleccionada = tablaResidentes.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < listaResidentes.size()) {
            return listaResidentes.get(filaSeleccionada);
        }
        return null;
    }

    public JPanel getPanelResidente() {
        return panelPrincipal;
    }

    public JTable getTablaResidentes() {
        return tablaResidentes;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public ControladorResidente getControlador() {
        return controlador;
    }

    public void setCursor(Cursor cursor) {
        if (panelPrincipal != null) {
            panelPrincipal.setCursor(cursor);
        }
    }


    public void revalidarResidentes() {
        for (ModeloResidente residente : listaResidentes) {
            // Revalidar cada residente
            residente.validarCamposBasicos();
        }

        actualizarVisualizacion();
    }
    public void actualizarResidente(ModeloResidente residenteEditado) {
        DefaultTableModel modelo = (DefaultTableModel) tablaResidentes.getModel();

        // Buscar el residente en la tabla y actualizarlo
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int numeroControl = (Integer) modelo.getValueAt(i, 0);
            if (numeroControl == residenteEditado.getNumeroControl()) {
                // Actualizar fila
                modelo.setValueAt(residenteEditado.getNombre(), i, 1);
                modelo.setValueAt(residenteEditado.getApellidoPaterno(), i, 2);
                modelo.setValueAt(residenteEditado.getApellidoMaterno(), i, 3);
                modelo.setValueAt(residenteEditado.getSemestre(), i, 4);
                modelo.setValueAt(residenteEditado.getCorreo(), i, 5);
                modelo.setValueAt(residenteEditado.getTelefono(), i, 6);

                // Actualizar también en la lista
                for (int j = 0; j < listaResidentes.size(); j++) {
                    if (listaResidentes.get(j).getNumeroControl() == residenteEditado.getNumeroControl()) {
                        listaResidentes.set(j, residenteEditado);
                        break;
                    }
                }
                break;
            }
        }

        // Actualizar visualización
        actualizarVisualizacion();
    }
    public void actualizarVisualizacion() {
        // Repintar la tabla para mostrar cambios
        tablaResidentes.repaint();
        tablaResidentes.revalidate();

        // Actualizar contadores si los tienes
        // actualizarContadores();
    }
    public void setListaResidentes(List<ModeloResidente> nuevaLista) {
        this.listaResidentes = new ArrayList<>(nuevaLista);

        // Actualizar tabla
        DefaultTableModel modelo = (DefaultTableModel) tablaResidentes.getModel();
        modelo.setRowCount(0);

        for (ModeloResidente residente : nuevaLista) {
            Object[] fila = {
                    residente.getNumeroControl(),
                    residente.getNombre(),
                    residente.getApellidoPaterno(),
                    residente.getApellidoMaterno(),
                    residente.getSemestre(),
                    residente.getCorreo(),
                    residente.getTelefono()
            };
            modelo.addRow(fila);
        }

        actualizarVisualizacion();
    }

    public List<ModeloResidente> getResidentesValidos() {
        List<ModeloResidente> residentesValidos = new ArrayList<>();

        for (ModeloResidente residente : listaResidentes) {
            if (residente.isEsValido()) {
                residentesValidos.add(residente);
            }
        }

        return residentesValidos;
    }
    public List<ModeloResidente> getResidentesInvalidos() {
        List<ModeloResidente> residentesInvalidos = new ArrayList<>();

        for (ModeloResidente residente : listaResidentes) {
            if (!residente.isEsValido()) {
                residentesInvalidos.add(residente);
            }
        }

        return residentesInvalidos;
    }
}