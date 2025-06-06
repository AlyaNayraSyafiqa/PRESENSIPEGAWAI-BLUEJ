public class Pegawai {
    private int id;
    private String nama;
    private String nip;
    private String divisi;

    public Pegawai(int id, String nama, String nip, String divisi) {
        this.id = id;
        this.nama = nama;
        this.nip = nip;
        this.divisi = divisi;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNip() {
        return nip;
    }

    public String getDivisi() {
        return divisi;
    }

    @Override
    public String toString() {
        return nip + " - " + nama;
    }
}
