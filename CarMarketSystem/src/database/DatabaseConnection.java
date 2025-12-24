package database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String PROP_FILE = "/db.properties";
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROP_FILE)) {
                Properties prop = new Properties();
                if (input == null) {
                    throw new SQLException("Sorry, unable to find " + PROP_FILE);
                }
                prop.load(input);

                conn = DriverManager.getConnection(
                        prop.getProperty("db.url"),
                        prop.getProperty("db.user"),
                        prop.getProperty("db.password"));
                System.out.println("Database connected!");
            } catch (IOException ex) {
                throw new SQLException("Error loading database properties: " + ex.getMessage());
            }
        }
        return conn;
    }
}
