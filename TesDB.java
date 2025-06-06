import java.sql.*;

public class TesDB {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/presensidancuti_pegawai";
            String user = "root";
            String password = "";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Koneksi database berhasil!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Koneksi database gagal!");
            e.printStackTrace(); // <<< baris ini penting supaya error detailnya muncul
        }
    }
}
