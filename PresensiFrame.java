import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PresensiFrame extends JFrame {
    public PresensiFrame() {
        setTitle("Form Presensi");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ArrayList<Pegawai> daftarPegawai = DatabaseManager.getDaftarPegawai();
        JComboBox<Pegawai> nipCombo = new JComboBox<>(daftarPegawai.toArray(new Pegawai[0]));
        JButton simpanButton = new JButton("Simpan");

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Pilih Pegawai (NIP):"));
        panel.add(nipCombo);
        panel.add(new JLabel());
        panel.add(simpanButton);

        simpanButton.addActionListener(e -> {
            Pegawai selected = (Pegawai) nipCombo.getSelectedItem();
            DatabaseManager.simpanPresensi(selected);
            JOptionPane.showMessageDialog(this, "✅ Presensi tersimpan!");
        });

        add(panel);
        setVisible(true);
    }
}
