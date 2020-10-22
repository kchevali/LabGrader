import java.sql.SQLException;
import java.sql.Connection;

class DriverManager {
    public static Connection getConnection(String url) throws SQLException {
        System.setProperty("derby.system.home", "/Users/kevin/Documents/Code/Java/LabGrader/derby");
        return java.sql.DriverManager.getConnection(url);
    }

    public static Connection getConnection(String url, String user, String password) throws SQLException {
        System.setProperty("derby.system.home", "/Users/kevin/Documents/Code/Java/LabGrader/derby");
        return java.sql.DriverManager.getConnection(url, user, password);
    }
}