import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CutiIzinFrame extends JFrame {
    public CutiIzinFrame() {
        setTitle("Form Izin/Sakit");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ArrayList<Pegawai> daftarPegawai = DatabaseManager.getDaftarPegawai();

        JComboBox<Pegawai> nipCombo = new JComboBox<>(daftarPegawai.toArray(new Pegawai[0]));
        String[] jenisOptions = {"Izin", "Sakit"};
        JComboBox<String> jenisCombo = new JComboBox<>(jenisOptions);

        JTextField tanggalMulaiField = new JTextField();
        JTextField tanggalSelesaiField = new JTextField();
        JTextField keteranganField = new JTextField();

        JButton simpanButton = new JButton("Simpan");

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.add(new JLabel("Pilih Pegawai (NIP):"));
        panel.add(nipCombo);
        panel.add(new JLabel("Jenis (Izin/Sakit):"));
        panel.add(jenisCombo);
        panel.add(new JLabel("Tanggal Mulai (yyyy-mm-dd):"));
        panel.add(tanggalMulaiField);
        panel.add(new JLabel("Tanggal Selesai (yyyy-mm-dd):"));
        panel.add(tanggalSelesaiField);
        panel.add(new JLabel("Keterangan:"));
        panel.add(keteranganField);
        panel.add(new JLabel());
        panel.add(simpanButton);

        simpanButton.addActionListener(e -> {
            Pegawai selected = (Pegawai) nipCombo.getSelectedItem();
            String jenis = (String) jenisCombo.getSelectedItem();
            String tanggalMulai = tanggalMulaiField.getText();
            String tanggalSelesai = tanggalSelesaiField.getText();
            String keterangan = keteranganField.getText();

            DatabaseManager.simpanCutiIzin(selected.getId(), jenis, tanggalMulai, tanggalSelesai, keterangan);
            JOptionPane.showMessageDialog(this, "✅ Data izin/sakit tersimpan!");
        });

        add(panel);
        setVisible(true);
    }
}
