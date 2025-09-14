package gui;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    public LoginPage() {
        setTitle("Admin Login - Smart Attendance System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load background image
        JPanel backgroundPanel = new JPanel() {
            ImageIcon bg = new ImageIcon("images/login_bg.jpeg"); // Change if needed

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // Create login panel (white box in the center)
        JPanel loginPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        loginPanel.setBackground(new Color(255, 255, 255, 230));
        loginPanel.setPreferredSize(new Dimension(300, 200));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField userField = new JTextField();
        userField.setToolTipText("Enter Username");

        JPasswordField passField = new JPasswordField();
        passField.setToolTipText("Enter Password");

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);

        // Add components
        loginPanel.add(titleLabel);
        loginPanel.add(userField);
        loginPanel.add(passField);
        loginPanel.add(loginButton);

        // Center the login panel
        backgroundPanel.add(loginPanel);

        // Add background to frame
        add(backgroundPanel);

        // Login action
        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                dispose();
                new AdminPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
