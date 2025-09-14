package qr;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class QRScanner {

    public static void scanAndMarkAttendance() {
        String key = "kmce";
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            JOptionPane.showMessageDialog(null, "No webcam detected");
            return;
        }

        webcam.setViewSize(new java.awt.Dimension(640, 480));
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setMirrored(true);

        JDialog dialog = new JDialog();
        dialog.setTitle("Scanning QR Code...");
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        final boolean[] running = {true};

        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                running[0] = false;
                if (webcam.isOpen()) webcam.close();
            }
        });

        new Thread(() -> {
            while (running[0]) {
                BufferedImage image = webcam.getImage();
                if (image == null) continue;

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    if (result != null) {
                        String data = result.getText().trim();
                        String[] parts = data.split(",");

                        if (parts.length != 5 || !parts[4].equals(key)) {
                            stopScanner(webcam, dialog, running);
                            JOptionPane.showMessageDialog(
                                null,
                                "Invalid QR Code. Only authorized QR codes are accepted.",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }

                        String studentId = parts[0].trim();
                        String name = parts[1].trim();
                        String branch = parts[2].trim();
                        String email = parts[3].trim();

                        stopScanner(webcam, dialog, running);

                        boolean marked = markAttendance(studentId);
                        if (marked) {
                            JOptionPane.showMessageDialog(null,
                                "✅ Attendance marked successfully!\n\n" +
                                "Student ID: " + studentId + "\n" +
                                "Name      : " + name + "\n" +
                                "Branch    : " + branch + "\n" +
                                "Email     : " + email,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                 "⚠️ You can mark attendance only once every 6 hours.",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        return;
                    }
                } catch (NotFoundException ignored) {
                } catch (Exception e) {
                    e.printStackTrace();
                    stopScanner(webcam, dialog, running);
                    JOptionPane.showMessageDialog(null, "❌ Failed to scan QR Code.");
                    return;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        dialog.setVisible(true);
    }

    private static void stopScanner(Webcam webcam, JDialog dialog, boolean[] running) {
        running[0] = false;
        if (webcam.isOpen()) webcam.close();
        dialog.dispose();
    }

    private static boolean markAttendance(String studentId) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return false;

            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT timestamp FROM attendance WHERE student_id = ? ORDER BY timestamp DESC LIMIT 1"
            );
            checkStmt.setString(1, studentId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                LocalDateTime lastTime = rs.getTimestamp("timestamp").toLocalDateTime();
                if (Duration.between(lastTime, LocalDateTime.now()).toHours() < 6) {
                    return false;
                }
            }

            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO attendance (student_id, timestamp) VALUES (?, NOW())"
            );
            insertStmt.setString(1, studentId);
            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
