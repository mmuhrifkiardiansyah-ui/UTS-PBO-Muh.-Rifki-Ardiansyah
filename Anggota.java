
public class Anggota {
    private static final int MAKS_PINJAM = 3;

    private String id;
    private String nama;
    private int jumlahPinjam;
    private boolean aktif;

    
    public Anggota() {
        this("A000", "Tanpa Nama");
    }

    public Anggota(String id, String nama) {
        setId(id);
        setNama(nama);
        this.jumlahPinjam = 0;
        this.aktif = true;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public int getJumlahPinjam() { return jumlahPinjam; }
    public boolean isAktif() { return aktif; }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("ID anggota tidak boleh kosong.");
        this.id = id.trim().toUpperCase();
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) throw new IllegalArgumentException("Nama tidak boleh kosong.");
        this.nama = nama.trim();
    }

    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public boolean bisaMeminjam() { return aktif && jumlahPinjam < MAKS_PINJAM; }

    public void tambahPinjaman() {
        if (jumlahPinjam >= MAKS_PINJAM)
            throw new IllegalStateException(nama + " sudah meminjam maksimal " + MAKS_PINJAM + " buku.");
        jumlahPinjam++;
    }

    public void kurangiPinjaman() { if (jumlahPinjam > 0) jumlahPinjam--; }

    public static String header() {
        String h = String.format("%-6s | %-20s | %-6s | %s", "ID", "Nama", "Pinjam", "Status");
        return h + "\n" + "-".repeat(h.length());
    }

    public void tampilkanInfo() {
        System.out.printf("%-6s | %-20s | %-6s | %s%n",
                id, nama, jumlahPinjam + "/" + MAKS_PINJAM, aktif ? "Aktif" : "Nonaktif");
    }
}