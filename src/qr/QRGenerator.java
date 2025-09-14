package qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class QRGenerator {

    private static final String SECRET_KEY = "kmce";

    public static String generateQR(String id, String name, String branch, String phone) {
        try {
            String qrData = String.join(",", id, name, branch, phone, SECRET_KEY);

            String fileName = "qrcodes/" + id + ".png";
            Path path = Paths.get(fileName);

            // Ensure folder exists
            Files.createDirectories(path.getParent());

            BitMatrix matrix = new MultiFormatWriter().encode(
                    qrData,
                    BarcodeFormat.QR_CODE,
                    300, 300
            );

            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            System.out.println("[INFO] QR generated at: " + path.toAbsolutePath());
            return path.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
