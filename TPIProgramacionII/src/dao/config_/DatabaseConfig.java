package config_;


import exception.ConexionFallidaException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    private static String persistenceUnitName;

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        try {
            InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("META/persistence.xml");
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo META-INF/persistence.xml en el classpath.");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);

            Element puElement = (Element) doc.getElementsByTagName("persistence-unit").item(0);
            if (puElement != null) {
                persistenceUnitName = puElement.getAttribute("name");
            }

            NodeList properties = doc.getElementsByTagName("property");
            for (int i = 0; i < properties.getLength(); i++) {
                Element prop = (Element) properties.item(i);
                String name = prop.getAttribute("name");
                String value = prop.getAttribute("value");

                switch (name) {
                    case "jakarta.persistence.jdbc.driver":
                        driver = value;
                        break;
                    case "jakarta.persistence.jdbc.url":
                        url = value;
                        break;
                    case "jakarta.persistence.jdbc.user":
                        user = value;
                        break;
                    case "jakarta.persistence.jdbc.password":
                        password = value;
                        break;
                }
            }

            if (driver != null && !driver.isEmpty()) {
                Class.forName(driver);
            }

        } catch (ClassNotFoundException e) {
             throw new ConexionFallidaException("ERROR: No se encontró el driver JDBC en la carpeta lib/.");
        } catch (Exception e) {
             throw new ConexionFallidaException("ERROR al procesar la configuración desde persistence.xml: " + e.getMessage());
        }
    }
    public static Connection getConnection() throws ConexionFallidaException {
        try {
            if (url == null || driver == null) {
                throw new ConexionFallidaException("La configuración JDBC no se pudo cargar correctamente desde el XML.");
            }
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new ConexionFallidaException("Error al establecer la conexión con la base de datos.", e);
        }
    }

    public static void probarConexion() throws ConexionFallidaException {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión exitosa a: " + url);
            }
        } catch (SQLException e) {
            throw new ConexionFallidaException("Fallo en la prueba de diagnóstico de conexión.", e);
        }
    }

    public static String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public static String getUrl() {
        return url;
    }
}