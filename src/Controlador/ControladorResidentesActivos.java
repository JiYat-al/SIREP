package Controlador;

import Modelo.ModeloResidente;
import Vista.VistaResidentesActivos;
import Vista.DialogoConfirmacionRegresoCandidato;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ControladorResidentesActivos {
    private VistaResidentesActivos vista;

    public ControladorResidentesActivos(VistaResidentesActivos vista) {
        this.vista = vista;
    }

    public void cargarTodosLosResidentes() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<ModeloResidente> residentes = ModeloResidente.obtenerResidentesActivos();
                vista.cargarResidentes(residentes);

                System.out.println("Residentes activos cargados: " + residentes.size());

            } catch (Exception e) {
                System.err.println("Error al cargar residentes: " + e.getMessage());
                vista.mostrarMensaje(
                        "Error al cargar los residentes:\n" + e.getMessage(),
                        "Error de carga",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    public void editarResidenteSeleccionado() {
        ModeloResidente residente = vista.getResidenteSeleccionado();

        if (residente == null) {
            vista.mostrarMensaje(
                    "Por favor, seleccione un residente para editar.",
                    "Ningún residente seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            vista.abrirDialogoEdicion(residente);
        } catch (Exception e) {
            System.err.println("Error al abrir dialogo de edicion: " + e.getMessage());
            vista.mostrarMensaje(
                    "Error al abrir el editor del residente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void darDeBajaResidenteSeleccionado() {
        ModeloResidente residente = vista.getResidenteSeleccionado();

        if (residente == null) {
            vista.mostrarMensaje(
                    "Por favor, seleccione un residente para dar de baja.",
                    "Ningún residente seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String mensaje = "¿Está seguro de dar de baja al residente?\n\n" +
                "Nombre: " + residente.getNombre() + " " + residente.getApellidoPaterno() + "\n" +
                "No. Control: " + residente.getNumeroControl() + "\n\n" +
                "Esta acción cambiará su estatus a inactivo.";

        boolean confirmar = vista.mostrarConfirmacion(mensaje, "Confirmar baja de residente");

        if (confirmar) {
            try {
                boolean resultado = residente.darDeBaja();

                if (resultado) {
                    vista.mostrarMensaje(
                            "Residente dado de baja exitosamente.\n" +
                                    "Nombre: " + residente.getNombre() + " " + residente.getApellidoPaterno(),
                            "Operación exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    cargarTodosLosResidentes();
                } else {
                    vista.mostrarMensaje(
                            "No se pudo dar de baja al residente.\n" +
                                    "Verifique que el registro aún existe en la base de datos.",
                            "Error en la operación",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (Exception e) {
                System.err.println("Error al dar de baja residente: " + e.getMessage());
                vista.mostrarMensaje(
                        "Error al dar de baja al residente:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public void regresarACandidato() {
        ModeloResidente residente = vista.getResidenteSeleccionado();

        if (residente == null) {
            vista.mostrarMensaje(
                    "Por favor, seleccione un residente para regresar a candidato.",
                    "Ningún residente seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            DialogoConfirmacionRegresoCandidato dialogo = new DialogoConfirmacionRegresoCandidato(
                    (java.awt.Frame) SwingUtilities.getWindowAncestor(vista),
                    residente
            );

            dialogo.setVisible(true);

            if (dialogo.isConfirmado()) {
                boolean resultado = ModeloResidente.regresarACandidato(residente.getIdResidente());

                if (resultado) {
                    vista.mostrarMensaje(
                            "Residente regresado a candidato exitosamente.\n" +
                                    "Nombre: " + residente.getNombre() + " " + residente.getApellidoPaterno() + "\n" +
                                    "Observaciones: " + dialogo.getObservaciones(),
                            "Operación exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    cargarTodosLosResidentes();
                } else {
                    vista.mostrarMensaje(
                            "No se pudo regresar el residente a candidato.\n" +
                                    "Verifique que el registro aún existe en la base de datos.",
                            "Error en la operación",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } catch (Exception e) {
            System.err.println("Error al regresar a candidato: " + e.getMessage());
            vista.mostrarMensaje(
                    "Error al procesar la operación:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}