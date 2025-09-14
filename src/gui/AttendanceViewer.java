package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AttendanceViewer {

    public AttendanceViewer() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"S.No", "Student ID", "Name", "Branch", "Email", "Login Time", "Logout Time"}, 0);

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Database connection failed.");
                return;
            }

            String query = """
                SELECT a.student_id, s.name, s.department, s.email, a.timestamp AS login_time
                FROM attendance a
                JOIN students s ON a.student_id = s.student_id
                ORDER BY a.timestamp DESC
            """;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int serial = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    serial++,
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("email"),
                    rs.getTimestamp("login_time"),
                    "" // Logout time is blank
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No attendance records found.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading attendance data.");
            return;
        }

        // Show the frame ONLY if data was loaded
        JFrame frame = new JFrame("Attendance Records");
        frame.setSize(900, 500);
        frame.setLayout(new BorderLayout());

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
