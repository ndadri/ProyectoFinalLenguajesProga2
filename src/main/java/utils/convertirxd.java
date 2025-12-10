package utils;

import org.mindrot.jbcrypt.BCrypt;

public class convertirxd {
    public static void main(String[] args) {
        String password = "admin123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("aña contra");
        System.out.println(hash);
        System.out.println("UPDATE usuario Set password '=" + hash + "' WHERE usuario = 'admin'");
    }
}
