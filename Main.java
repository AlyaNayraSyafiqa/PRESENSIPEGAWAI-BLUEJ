import javax.swing.*;
import java.awt.GridLayout;

public class Main {
    public Main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu Utama");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JButton presensiButton = new JButton("Presensi");
            JButton cutiIzinButton = new JButton("Cuti/Izin");

            presensiButton.addActionListener(e -> new PresensiFrame());
            cutiIzinButton.addActionListener(e -> new CutiIzinFrame());

            JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
            panel.add(presensiButton);
            panel.add(cutiIzinButton);

            frame.add(panel);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        DatabaseManager.inisialisasiDatabase();
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
