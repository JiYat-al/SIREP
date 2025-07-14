package Util;

import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {

    // Encripta una contraseña con bcrypt (genera hash con salt)
    public static String encriptar(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt());
    }

    // Verifica si la contraseña ingresada coincide con el hash guardado
    public static boolean verificar(String contrasenaIngresada, String hashGuardado) {
        if (hashGuardado == null || hashGuardado.isEmpty()) {
            return false;  // evita errores si no hay hash
        }
        return BCrypt.checkpw(contrasenaIngresada, hashGuardado);
    }
}
