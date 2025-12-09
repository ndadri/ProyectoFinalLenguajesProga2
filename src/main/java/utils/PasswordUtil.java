package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Genera un hash seguro de la contraseña usando BCrypt
     * @param plainPassword Contraseña en texto plano
     * @return Hash de la contraseña
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Verifica si una contraseña coincide con su hash
     * @param plainPassword Contraseña en texto plano
     * @param hashedPassword Hash almacenado en la base de datos
     * @return true si coinciden, false si no
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un string es un hash BCrypt válido
     * @param password String a verificar
     * @return true si es un hash BCrypt
     */
    public static boolean isBCryptHash(String password) {
        return password != null && password.startsWith("$2a$") && password.length() == 60;
    }
}