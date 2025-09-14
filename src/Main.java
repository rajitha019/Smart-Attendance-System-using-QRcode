import gui.LoginPage;

public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.SwingUtilities.invokeLater(() -> new LoginPage());
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "App failed to start:\n" + e.getMessage());
        }
    }
}
