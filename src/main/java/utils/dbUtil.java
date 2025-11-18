package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class dbUtil {

    private static String url;
    private static String user;
    private static String password;

    static {
        try {            // Cargar archivo db.properties desde resources
            InputStream in = dbUtil.class.getClassLoader().getResourceAsStream("db.properties");

            if (in == null) {
                throw new RuntimeException("No se encontró el archivo db.properties en resources");
            }

            Properties p = new Properties();
            p.load(in);

            url = p.getProperty("jdbc.url");
            user = p.getProperty("jdbc.user");
            password = p.getProperty("jdbc.password");

            // Registrar driver (en MySQL nuevo puede no ser necesario, pero no estorba)
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (Exception e) {
            throw new RuntimeException("Error cargando db.properties o inicializando la conexión", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}
