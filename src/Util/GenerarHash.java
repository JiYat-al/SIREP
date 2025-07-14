package Util;

public class GenerarHash {
    public static void main(String[] args) {
        String contrasena = "JefaturaSIREp23*";
        String hash = Util.Seguridad.encriptar(contrasena);
        System.out.println("Hash generado: " + hash);
    }
}
