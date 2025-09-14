import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smart_attendance", "root", "nani1919");
            System.out.println("✅ Connection successful.");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
