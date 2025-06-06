import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;

public class Laporan {

    public static void tampilkanSemuaPresensiDanSimpanKeFile() {
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT p.nama, p.nip, p.divisi, pr.shift, pr.status, pr.waktu_absen " +
                             "FROM presensi pr JOIN pegawai p ON pr.pegawai_id = p.id")) {

            FileWriter writer = new FileWriter("laporan_presensi.txt");
            writer.write("=== LAPORAN SEMUA PRESENSI ===\n");

            while (rs.next()) {
                String data = "Nama: " + rs.getString("nama") + "\n" +
                        "NIP: " + rs.getString("nip") + "\n" +
                        "Divisi: " + rs.getString("divisi") + "\n" +
                        "Shift: " + rs.getString("shift") + "\n" +
                        "Status: " + rs.getString("status") + "\n" +
                        "Waktu: " + rs.getString("waktu_absen") + "\n" +
                        "---------------------------\n";
                System.out.print(data);
                writer.write(data);
            }
            writer.close();
            System.out.println("✅ Laporan disimpan ke laporan_presensi.txt!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void tampilkanLaporanIndividuDanSimpan(String nip) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT p.nama, p.divisi, pr.shift, pr.status, pr.waktu_absen " +
                             "FROM presensi pr JOIN pegawai p ON pr.pegawai_id = p.id " +
                             "WHERE p.nip = ?")) {

            ps.setString(1, nip);
            ResultSet rs = ps.executeQuery();

            FileWriter writer = new FileWriter("laporan_individu_" + nip + ".txt");
            writer.write("=== LAPORAN PRESENSI UNTUK NIP: " + nip + " ===\n");

            while (rs.next()) {
                String data = "Nama: " + rs.getString("nama") + "\n" +
                        "Divisi: " + rs.getString("divisi") + "\n" +
                        "Shift: " + rs.getString("shift") + "\n" +
                        "Status: " + rs.getString("status") + "\n" +
                        "Waktu: " + rs.getString("waktu_absen") + "\n" +
                        "---------------------------\n";
                System.out.print(data);
                writer.write(data);
            }
            writer.close();
            System.out.println("✅ Laporan individu disimpan ke laporan_individu_" + nip + ".txt!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void tampilkanLaporanPerBulanDanSimpan(int bulan, int tahun) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT p.nama, p.nip, p.divisi, pr.shift, pr.status, pr.waktu_absen " +
                             "FROM presensi pr JOIN pegawai p ON pr.pegawai_id = p.id " +
                             "WHERE MONTH(pr.waktu_absen) = ? AND YEAR(pr.waktu_absen) = ?")) {

            ps.setInt(1, bulan);
            ps.setInt(2, tahun);
            ResultSet rs = ps.executeQuery();

            String filename = String.format("laporan_presensi_%02d_%d.txt", bulan, tahun);
            FileWriter writer = new FileWriter(filename);
            writer.write("=== LAPORAN PRESENSI BULAN " + bulan + " TAHUN " + tahun + " ===\n");

            boolean dataAda = false;
            while (rs.next()) {
                dataAda = true;
                String data = "Nama: " + rs.getString("nama") + "\n" +
                        "NIP: " + rs.getString("nip") + "\n" +
                        "Divisi: " + rs.getString("divisi") + "\n" +
                        "Shift: " + rs.getString("shift") + "\n" +
                        "Status: " + rs.getString("status") + "\n" +
                        "Waktu: " + rs.getString("waktu_absen") + "\n" +
                        "---------------------------\n";
                System.out.print(data);
                writer.write(data);
            }

            writer.close();

            if (dataAda) {
                System.out.println("✅ Laporan bulan " + bulan + " disimpan ke " + filename + "!");
            } else {
                System.out.println("⚠️ Tidak ada data presensi untuk bulan dan tahun tersebut.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
