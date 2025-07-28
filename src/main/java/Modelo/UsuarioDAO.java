package Modelo;

import Util.Seguridad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class UsuarioDAO {

    // Variables para manejar códigos de verificación
    private static Map<String, String> codigosVerificacion = new HashMap<>();
    private static Map<String, Long> tiempoExpiracion = new HashMap<>();
    private static final long TIEMPO_EXPIRACION = 5 * 60 * 1000; // 5 minutos

    public Usuario validarUsuario(String usuario, String contrasenaIngresada) {
        String sql = "SELECT u.id_usuario, u.usuario, u.contrasena, u.numero_tarjeta, " +
                "d.nombre, d.apellido_paterno, d.apellido_materno, d.correo " +
                "FROM usuario u " +
                "INNER JOIN docente d ON u.numero_tarjeta = d.numero_tarjeta " +
                "WHERE u.usuario = ?";

        System.out.println("Intentando validar usuario: " + usuario);

        try (Connection conn = Conexion_bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashGuardado = rs.getString("contrasena");

                // Validar la contraseña ingresada contra el hash almacenado
                if (Seguridad.verificar(contrasenaIngresada, hashGuardado)) {
                    // Crear objeto Usuario con información completa
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setNumeroTarjeta(rs.getInt("numero_tarjeta"));

                    System.out.println("Usuario validado: " + rs.getString("nombre") + " " +
                            rs.getString("apellido_paterno"));
                    return u;
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al validar usuario: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null; // Usuario no encontrado o contraseña incorrecta
    }

    // ========== MÉTODOS PARA RECUPERACIÓN DE CONTRASEÑA ==========

    /**
     * Verifica si un usuario existe y el email coincide con el registrado en la tabla docente
     */
    public boolean verificarUsuarioYEmail(String nombreUsuario, String emailIngresado) {
        String sql = "SELECT d.correo FROM usuario u " +
                "INNER JOIN docente d ON u.numero_tarjeta = d.numero_tarjeta " +
                "WHERE u.usuario = ?";

        try (Connection conn = Conexion_bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String emailRegistrado = rs.getString("correo");

                // Verificar que el email no sea nulo y coincida (sin distinción de mayúsculas)
                if (emailRegistrado != null &&
                        emailRegistrado.trim().toLowerCase().equals(emailIngresado.trim().toLowerCase())) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.err.println("Error al verificar usuario y email: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene información completa del docente asociado al usuario
     */
    public DocenteInfo obtenerInfoDocente(String nombreUsuario) {
        String sql = "SELECT d.numero_tarjeta, d.nombre, d.apellido_paterno, d.apellido_materno, d.correo " +
                "FROM usuario u " +
                "INNER JOIN docente d ON u.numero_tarjeta = d.numero_tarjeta " +
                "WHERE u.usuario = ?";

        try (Connection conn = Conexion_bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                DocenteInfo docente = new DocenteInfo();
                docente.numeroTarjeta = rs.getInt("numero_tarjeta");
                docente.nombre = rs.getString("nombre");
                docente.apellidoPaterno = rs.getString("apellido_paterno");
                docente.apellidoMaterno = rs.getString("apellido_materno");
                docente.correo = rs.getString("correo");
                return docente;
            }

        } catch (Exception e) {
            System.err.println("Error al obtener información del docente: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Clase interna para representar información del docente
     */
    public static class DocenteInfo {
        public int numeroTarjeta;
        public String nombre;
        public String apellidoPaterno;
        public String apellidoMaterno;
        public String correo;

        public String getNombreCompleto() {
            return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
        }
    }

    /**
     * Genera y guarda un código de verificación para el usuario después de verificar email
     */
    public String generarCodigoVerificacion(String nombreUsuario, String emailIngresado) {
        // Verificar que el usuario existe y el email coincide
        if (!verificarUsuarioYEmail(nombreUsuario, emailIngresado)) {
            return null;
        }

        // Obtener información completa del docente
        DocenteInfo docente = obtenerInfoDocente(nombreUsuario);
        if (docente == null) {
            return null;
        }

        // Generar código de verificación
        String codigo = String.format("%06d", (int)(Math.random() * 1000000));

        // Guardar código con tiempo de expiración
        codigosVerificacion.put(nombreUsuario, codigo);
        tiempoExpiracion.put(nombreUsuario, System.currentTimeMillis() + TIEMPO_EXPIRACION);

        // Enviar código por email usando el correo del docente y su nombre completo
        if (enviarEmailVerificacion(docente.correo, docente.getNombreCompleto(), codigo)) {
            System.out.println("Código de verificación enviado a: " + docente.correo);
            return codigo;
        } else {
            // Si falla el envío, limpiar el código generado
            codigosVerificacion.remove(nombreUsuario);
            tiempoExpiracion.remove(nombreUsuario);
            System.err.println("Error al enviar el código de verificación.");
            return null;
        }
    }

    /**
     * Envía el código de verificación por email usando Gmail
     */
    private boolean enviarEmailVerificacion(String email, String nombreUsuario, String codigo) {
        try {
            // Configuración del servidor SMTP de Gmail
            java.util.Properties props = new java.util.Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Credenciales de Gmail
            final String emailRemitente = "jefaturasirep@gmail.com"; // Tu email de Gmail
            final String passwordRemitente = "dgza ofcz cbfx owgp"; // App Password de Gmail

            // Crear sesión
            javax.mail.Session session = javax.mail.Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new javax.mail.PasswordAuthentication(emailRemitente, passwordRemitente);
                        }
                    });

            // Crear mensaje
            javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(session);
            message.setFrom(new javax.mail.internet.InternetAddress(emailRemitente, "SIREP - ITO"));
            message.setRecipients(javax.mail.Message.RecipientType.TO,
                    javax.mail.internet.InternetAddress.parse(email));
            message.setSubject("🔐 Código de Verificación - SIREP");

            // Contenido del email con diseño profesional
            String contenidoHTML =
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<meta charset=\"UTF-8\">" +
                            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                            "<title>Código de Verificación</title>" +
                            "</head>" +
                            "<body style=\"margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8f9fa;\">" +
                            "<div style=\"max-width: 600px; margin: 0 auto; background-color: white; box-shadow: 0 4px 6px rgba(0,0,0,0.1);\">" +

                            "<!-- Header -->" +
                            "<div style=\"background: linear-gradient(135deg, #5c5da9 0%, #7b68ee 100%); padding: 40px 30px; text-align: center;\">" +
                            "<h1 style=\"color: white; margin: 0 0 10px 0; font-size: 28px; font-weight: 300;\">🔐 SIREP</h1>" +
                            "<h2 style=\"color: #e8e6ff; margin: 0; font-size: 16px; font-weight: 300;\">Recuperación de Contraseña</h2>" +
                            "</div>" +

                            "<!-- Body -->" +
                            "<div style=\"padding: 40px 30px;\">" +
                            "<p style=\"color: #333; font-size: 18px; line-height: 1.6; margin: 0 0 20px 0;\">" +
                            "Hola <strong style=\"color: #5c5da9;\">" + nombreUsuario + "</strong>," +
                            "</p>" +

                            "<p style=\"color: #666; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;\">" +
                            "Has solicitado recuperar tu contraseña. Utiliza el siguiente código de verificación para continuar:" +
                            "</p>" +

                            "<!-- Código de verificación -->" +
                            "<div style=\"text-align: center; margin: 40px 0;\">" +
                            "<div style=\"background: linear-gradient(135deg, #5c5da9 0%, #7b68ee 100%); " +
                            "color: white; " +
                            "font-size: 36px; " +
                            "font-weight: bold; " +
                            "padding: 25px 40px; " +
                            "border-radius: 12px; " +
                            "letter-spacing: 8px; " +
                            "display: inline-block;" +
                            "box-shadow: 0 4px 15px rgba(92, 93, 169, 0.3);\">" +
                            codigo +
                            "</div>" +
                            "</div>" +

                            "<!-- Información adicional -->" +
                            "<div style=\"background-color: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 4px solid #5c5da9; margin: 30px 0;\">" +
                            "<p style=\"color: #495057; font-size: 14px; line-height: 1.6; margin: 0;\">" +
                            "<strong>⏰ Este código es válido por 5 minutos.</strong><br>" +
                            "<strong>🔒 Por tu seguridad, no compartas este código con nadie.</strong><br>" +
                            "<strong>❓ Si no solicitaste este cambio, puedes ignorar este correo.</strong>" +
                            "</p>" +
                            "</div>" +

                            "<p style=\"color: #666; font-size: 14px; line-height: 1.6; margin: 30px 0 0 0;\">" +
                            "Saludos,<br>" +
                            "<strong style=\"color: #5c5da9;\">Equipo SIREP</strong><br>" +
                            "<span style=\"color: #999; font-size: 12px;\">Instituto Tecnológico de Oaxaca - ITO</span>" +
                            "</p>" +
                            "</div>" +

                            "<!-- Footer -->" +
                            "<div style=\"background-color: #f8f9fa; padding: 20px 30px; text-align: center; border-top: 1px solid #e9ecef;\">" +
                            "<p style=\"color: #6c757d; font-size: 12px; margin: 0; line-height: 1.4;\">" +
                            "Este es un mensaje automático, por favor no responder.<br>" +
                            "<strong>SIREP</strong> - Tecnológico Nacional de México (TECNM)<br>" +
                            "Instituto Tecnológico de Oaxaca - ITO" +
                            "</p>" +
                            "</div>" +

                            "</div>" +
                            "</body>" +
                            "</html>";

            message.setContent(contenidoHTML, "text/html; charset=utf-8");

            // Enviar email
            javax.mail.Transport.send(message);

            return true;

        } catch (Exception e) {
            System.err.println("Error al enviar email con Gmail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si el código de verificación es válido
     */
    public boolean verificarCodigo(String nombreUsuario, String codigo) {
        // Verificar si el código existe
        if (!codigosVerificacion.containsKey(nombreUsuario)) {
            return false;
        }

        // Verificar si el código no ha expirado
        Long expiracion = tiempoExpiracion.get(nombreUsuario);
        if (expiracion == null || System.currentTimeMillis() > expiracion) {
            // Limpiar códigos expirados
            codigosVerificacion.remove(nombreUsuario);
            tiempoExpiracion.remove(nombreUsuario);
            return false;
        }

        // Verificar si el código es correcto
        String codigoGuardado = codigosVerificacion.get(nombreUsuario);
        return codigo.equals(codigoGuardado);
    }

    /**
     * Actualiza la contraseña del usuario después de verificar el código
     */
    public boolean actualizarPassword(String nombreUsuario, String nuevaPassword) {
        // Verificar que el código fue verificado previamente
        if (!codigosVerificacion.containsKey(nombreUsuario)) {
            return false;
        }

        String sql = "UPDATE usuario SET contrasena = ? WHERE usuario = ?";

        try (Connection conn = Conexion_bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Encriptar la nueva contraseña
            String passwordEncriptada = Seguridad.encriptar(nuevaPassword);

            stmt.setString(1, passwordEncriptada);
            stmt.setString(2, nombreUsuario);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Limpiar códigos de verificación después del cambio exitoso
                codigosVerificacion.remove(nombreUsuario);
                tiempoExpiracion.remove(nombreUsuario);

                System.out.println("Contraseña actualizada exitosamente para: " + nombreUsuario);
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error al actualizar password: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Limpia códigos de verificación expirados (método de mantenimiento)
     */
    public void limpiarCodigosExpirados() {
        long tiempoActual = System.currentTimeMillis();

        codigosVerificacion.entrySet().removeIf(entry -> {
            String usuario = entry.getKey();
            Long expiracion = tiempoExpiracion.get(usuario);
            if (expiracion != null && tiempoActual > expiracion) {
                tiempoExpiracion.remove(usuario);
                return true;
            }
            return false;
        });
    }
}