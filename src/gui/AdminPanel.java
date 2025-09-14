package gui;

import qr.QRScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminPanel extends JFrame {

    public AdminPanel() {
        setTitle("Smart Attendance Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Heading label
        JLabel heading = new JLabel("Smart Attendance System", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150)); // padding

        // Buttons
        JButton markBtn = new JButton("Mark Attendance");
        JButton enrollBtn = new JButton("New Enrollment");
        JButton viewBtn = new JButton("View Attendance");
        JButton logoutBtn = new JButton("Logout");

        styleButton(markBtn, new Color(0, 123, 255));
        styleButton(enrollBtn, new Color(0, 150, 255));
        styleButton(viewBtn, new Color(0, 180, 255));
        styleButton(logoutBtn, new Color(220, 53, 69));

        centerPanel.add(markBtn);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(enrollBtn);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(viewBtn);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(logoutBtn);

        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        markBtn.addActionListener((ActionEvent e) -> QRScanner.scanAndMarkAttendance());
        enrollBtn.addActionListener((ActionEvent e) -> new EnrollmentForm());
        viewBtn.addActionListener((ActionEvent e) -> new AttendanceViewer());
        logoutBtn.addActionListener((ActionEvent e) -> {
            dispose();
            new LoginPage();
        });

        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(250, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPanel::new);
    }
}
