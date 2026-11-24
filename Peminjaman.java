/** Entitas Peminjaman. Relasi asosiasi ke Buku dan Anggota. */
public class Peminjaman {
    private static final double TARIF_PER_HARI = 1000;

    private String kode;
    private Buku buku;         // asosiasi
    private Anggota anggota;   // asosiasi
    private int lamaHari;
    private boolean dikembalikan;

    /** Konstruktor default. */
    public Peminjaman() {
        this("PJ000", null, null, 1);
    }

    /** Konstruktor berparameter. */
    public Peminjaman(String kode, Buku buku, Anggota anggota, int lamaHari) {
        this.kode = kode;
        this.buku = buku;
        this.anggota = anggota;
        setLamaHari(lamaHari);
        this.dikembalikan = false;
    }

    public String getKode() { return kode; }
    public Buku getBuku() { return buku; }
    public Anggota getAnggota() { return anggota; }
    public int getLamaHari() { return lamaHari; }
    public boolean isDikembalikan() { return dikembalikan; }

    public void setLamaHari(int lamaHari) {
        if (lamaHari < 1 || lamaHari > 30) throw new IllegalArgumentException("Lama pinjam harus 1-30 hari.");
        this.lamaHari = lamaHari;
    }

    /** Biaya = tarif per hari x lama pinjam. */
    public double hitungBiaya() { return TARIF_PER_HARI * lamaHari; }

    /** Mengembalikan buku: stok dan jatah pinjam anggota dipulihkan. */
    public void kembalikan() {
        if (dikembalikan) throw new IllegalStateException("Transaksi " + kode + " sudah dikembalikan.");
        dikembalikan = true;
        buku.tambahStok();
        anggota.kurangiPinjaman();
    }

    /** Header tabel untuk tampilan daftar transaksi. */
    public static String header() {
        String h = String.format("%-6s | %-22s | %-14s | %4s | %9s | %s",
                "Kode", "Buku", "Anggota", "Hari", "Biaya", "Status");
        return h + "\n" + "-".repeat(h.length());
    }

    public void tampilkanInfo() {
        System.out.printf("%-6s | %-22s | %-14s | %4d | %9s | %s%n", kode,
                buku.getJudul(), anggota.getNama(), lamaHari, "Rp" + (long) hitungBiaya(),
                dikembalikan ? "Dikembalikan" : "Dipinjam");
    }
}