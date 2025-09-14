package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/smart_attendance";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[INFO] Connected to MySQL database successfully.");
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "❌ MySQL JDBC Driver not found.\n" + e.getMessage(), "Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to connect to the MySQL database.\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return connection;
    }
}
