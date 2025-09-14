package gui;

import model.Student;
import qr.QRGenerator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EnrollmentForm extends JFrame {

    private JTextField idField, nameField, phoneField;
    private JComboBox<String> branchBox;
    private JLabel statusLabel;

    public EnrollmentForm() {
        setTitle("Student Enrollment");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        JLabel titleLabel = new JLabel("New Student Enrollment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(80, 10, 250, 30);
        add(titleLabel);

        // Labels
        JLabel idLabel = new JLabel("Student ID:");
        JLabel nameLabel = new JLabel("Student Name:");
        JLabel branchLabel = new JLabel("Branch:");
        JLabel phoneLabel = new JLabel("Phone Number:");

        idLabel.setBounds(40, 60, 120, 25);
        nameLabel.setBounds(40, 100, 120, 25);
        branchLabel.setBounds(40, 140, 120, 25);
        phoneLabel.setBounds(40, 180, 120, 25);

        add(idLabel); add(nameLabel); add(branchLabel); add(phoneLabel);

        // Fields
        idField = new JTextField();
        nameField = new JTextField();
        phoneField = new JTextField();

        idField.setBounds(170, 60, 170, 25);
        nameField.setBounds(170, 100, 170, 25);
        phoneField.setBounds(170, 180, 170, 25);

        // Branch Dropdown
        branchBox = new JComboBox<>(new String[] {"CSE", "IT", "ECE", "EEE", "ME", "CE"});
        branchBox.setBounds(170, 140, 170, 25);

        add(idField); add(nameField); add(branchBox); add(phoneField);

        // Generate QR Button
        JButton generateBtn = new JButton("Generate QR Code");
        generateBtn.setBounds(100, 230, 200, 35);
        generateBtn.setBackground(new Color(30, 144, 255));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        generateBtn.setFocusPainted(false);
        add(generateBtn);

        // Status Label
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setBounds(30, 280, 340, 40);
        statusLabel.setForeground(new Color(0, 128, 0));
        add(statusLabel);

        // Action Listener
        generateBtn.addActionListener(e -> generateQR());

        setVisible(true);
    }

    private void generateQR() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String branch = (String) branchBox.getSelectedItem();
        String phone = phoneField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || branch.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Save Student
        boolean saved = Student.saveStudent(id, name, branch, phone);
        if (saved) {
            // Generate QR
            String filePath = QRGenerator.generateQR(id, name, branch, phone);
            statusLabel.setText("<html>QR Code saved successfully!<br><font color='blue'>" + filePath + "</font></html>");

            // Reset fields
            idField.setText("");
            nameField.setText("");
            phoneField.setText("");
            branchBox.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save student", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EnrollmentForm::new);
    }
}
