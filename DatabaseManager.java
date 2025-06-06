import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/presensidancuti_pegawai";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void inisialisasiDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Buat tabel admin
            stmt.execute("CREATE TABLE IF NOT EXISTS admin (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE, " +
                    "password VARCHAR(50))");

            // Buat tabel pegawai
            stmt.execute("CREATE TABLE IF NOT EXISTS pegawai (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nama VARCHAR(100), " +
                    "nip VARCHAR(50) UNIQUE, " +
                    "divisi VARCHAR(50), " +
                    "gaji DECIMAL(10,2) DEFAULT 5000000, " +
                    "sisa_cuti INT DEFAULT 12)");

            // Buat tabel presensi
            stmt.execute("CREATE TABLE IF NOT EXISTS presensi (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "pegawai_id INT, " +
                    "shift VARCHAR(20), " +
                    "status VARCHAR(20), " +
                    "waktu_absen DATETIME, " +
                    "FOREIGN KEY (pegawai_id) REFERENCES pegawai(id))");

            // Buat tabel cuti/izin
            stmt.execute("CREATE TABLE IF NOT EXISTS cuti_izin (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "pegawai_id INT, " +
                    "jenis VARCHAR(20), " +
                    "tanggal_mulai DATE, " +
                    "tanggal_selesai DATE, " +
                    "jumlah_hari INT, " +
                    "keterangan VARCHAR(100), " +
                    "FOREIGN KEY (pegawai_id) REFERENCES pegawai(id))");

            // Pastikan hanya 1 admin (username UNIQUE)
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM admin WHERE username = 'admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO admin (username, password) VALUES ('admin', 'admin123')");
            }

            // Tambahkan data dummy pegawai (100 pegawai)
            String[] divisiList = {"Keuangan", "IT", "HRD", "Marketing", "Produksi"};
            String[] namaDepan = {"Adi", "Budi", "Citra", "Dewi", "Eka", "Farhan"};
            String[] namaBelakang = {"Susanto", "Putri", "Wijaya", "Rahma", "Saputra", "Pratama"};
            Random rand = new Random();

            for (int div = 0; div < 5; div++) {
                String kodeDivisi = String.format("%02d", div + 1);
                String divisi = divisiList[div];
                for (int i = 1; i <= 20; i++) {
                    String noPegawai = String.format("%04d", i + (div * 20));
                    String nip = kodeDivisi + noPegawai;
                    String nama = namaDepan[rand.nextInt(namaDepan.length)] + " " + namaBelakang[rand.nextInt(namaBelakang.length)];
                    stmt.execute("INSERT IGNORE INTO pegawai (nama, nip, divisi) VALUES ('" + nama + "', '" + nip + "', '" + divisi + "')");
                }
            }

            System.out.println("✅ Database & data dummy sudah siap!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Pegawai> getDaftarPegawai() {
        ArrayList<Pegawai> daftar = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pegawai")) {

            while (rs.next()) {
                daftar.add(new Pegawai(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("nip"),
                        rs.getString("divisi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftar;
    }

    public static void simpanPresensi(Pegawai pegawai) {
    try (Connection conn = connect();
         PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO presensi (pegawai_id, shift, status, waktu_absen) VALUES (?, ?, ?, ?)")) {

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();

        String shift;
        String status;

        if (hour >= 6 && hour < 14) {
            shift = "Pagi";
            status = (hour > 7) ? "Terlambat" : "Tidak";
        } else if (hour >= 14 && hour < 22) {
            shift = "Siang";
            status = (hour > 15) ? "Terlambat" : "Tidak";
        } else {
            shift = "Malam";
            // Jam malam 22:00 - 05:59
            // Batas tidak terlambat jam 23:00
            status = (hour > 23 || hour < 0) ? "Terlambat" : "Tidak";
        }

        ps.setInt(1, pegawai.getId());
        ps.setString(2, shift);
        ps.setString(3, status);
        ps.setTimestamp(4, Timestamp.valueOf(now));
        ps.executeUpdate();

        if (status.equals("Terlambat")) {
            try (PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE pegawai SET gaji = gaji * 0.99 WHERE id = ?")) {
                ps2.setInt(1, pegawai.getId());
                ps2.executeUpdate();
            }
        }

        System.out.println("✅ Presensi tersimpan & gaji update!");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public static void simpanCutiIzin(int pegawaiId, String jenis, String tanggalMulai, String tanggalSelesai, String keterangan) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO cuti_izin (pegawai_id, jenis, tanggal_mulai, tanggal_selesai, jumlah_hari, keterangan) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {

            LocalDate tglMulai = LocalDate.parse(tanggalMulai);
            LocalDate tglSelesai = LocalDate.parse(tanggalSelesai);
            long jumlahHari = ChronoUnit.DAYS.between(tglMulai, tglSelesai) + 1;
            if (jumlahHari < 1) jumlahHari = 1;

            ps.setInt(1, pegawaiId);
            ps.setString(2, jenis);
            ps.setDate(3, Date.valueOf(tanggalMulai));
            ps.setDate(4, Date.valueOf(tanggalSelesai));
            ps.setInt(5, (int) jumlahHari);
            ps.setString(6, keterangan);
            ps.executeUpdate();

            // Update sisa cuti (hanya jika Izin)
            if (jenis.equals("Izin")) {
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "UPDATE pegawai SET sisa_cuti = GREATEST(sisa_cuti - ?, 0) WHERE id = ?")) {
                    ps2.setInt(1, (int) jumlahHari);
                    ps2.setInt(2, pegawaiId);
                    ps2.executeUpdate();
                }
            }

            System.out.println("✅ Data cuti/izin tersimpan & sisa cuti diperbarui!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
