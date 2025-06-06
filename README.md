# Aplikasi Presensi Pegawai

Proyek ini merupakan aplikasi desktop sederhana berbasis Java Swing untuk mengelola data presensi, cuti, dan izin pegawai. Proyek dikembangkan menggunakan BlueJ dan terstruktur dalam beberapa class utama.

## Struktur File

| Nama File             | Deskripsi                                           |
|-----------------------|-----------------------------------------------------|
| `Main.java`           | File utama yang menjalankan aplikasi                |
| `LoginFrame.java`     | Halaman login untuk pengguna                        |
| `PresensiFrame.java`  | Form dan tampilan data presensi                     |
| `CutiIzinFrame.java`  | Form input cuti dan izin pegawai                    |
| `Laporan.java`        | Menampilkan laporan presensi dan cuti              |
| `DatabaseManager.java`| Mengelola koneksi dan operasi ke database          |
| `Pegawai.java`        | Class model untuk data pegawai                     |
| `TesDB.java`          | Class untuk pengujian koneksi database             |

## Teknologi

- Java
- Swing (Java GUI)
- JDBC untuk koneksi database
- IDE: BlueJ

## Cara Menjalankan

1. Buka folder proyek ini di BlueJ
2. Pastikan konfigurasi database di `DatabaseManager.java` sudah sesuai
3. Jalankan file `Main.java`
4. Masuk menggunakan akun yang tersedia (jika ada pengecekan login)

## Catatan

- File ini hanya mencakup source code Java
- File `.class` dan file database eksternal tidak disertakan
- Proyek ini dibuat untuk keperluan pembelajaran dan pengujian konsep Java GUI
