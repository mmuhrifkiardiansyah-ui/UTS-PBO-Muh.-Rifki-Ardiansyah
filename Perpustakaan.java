import java.util.ArrayList;

/**
 * Entitas Perpustakaan (pengelola).
 * Relasi: agregasi ke Buku & Anggota, komposisi ke AntrianPeminjaman.
 */
public class Perpustakaan {
    private String nama;
    private double totalPendapatan;
    private boolean buka;
    private int counter;

    private ArrayList<Buku> daftarBuku = new ArrayList<>();            // agregasi
    private ArrayList<Anggota> daftarAnggota = new ArrayList<>();      // agregasi
    private ArrayList<Peminjaman> riwayat = new ArrayList<>();
    private AntrianPeminjaman antrian = new AntrianPeminjaman();       // komposisi

    /** Konstruktor default. */
    public Perpustakaan() { this("Perpustakaan Mini"); }

    /** Konstruktor berparameter. */
    public Perpustakaan(String nama) {
        setNama(nama);
        this.buka = true;
    }

    public String getNama() { return nama; }
    public double getTotalPendapatan() { return totalPendapatan; }
    public boolean isBuka() { return buka; }
    public AntrianPeminjaman getAntrian() { return antrian; }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) throw new IllegalArgumentException("Nama perpustakaan tidak boleh kosong.");
        this.nama = nama.trim();
    }

    public void tambahBuku(Buku b) {
        if (cariBuku(b.getKode()) != null) throw new IllegalArgumentException("Kode buku " + b.getKode() + " sudah ada.");
        daftarBuku.add(b);
    }

    public void tambahAnggota(Anggota a) {
        if (cariAnggota(a.getId()) != null) throw new IllegalArgumentException("ID anggota " + a.getId() + " sudah ada.");
        daftarAnggota.add(a);
    }

    /** Mencari buku berdasarkan kode atau nama (judul). */
    public Buku cariBuku(String kunci) {
        for (Buku b : daftarBuku)
            if (b.getKode().equalsIgnoreCase(kunci.trim()) || b.getJudul().equalsIgnoreCase(kunci.trim())) return b;
        return null;
    }

    /** Mencari anggota berdasarkan ID atau nama. */
    public Anggota cariAnggota(String kunci) {
        for (Anggota a : daftarAnggota)
            if (a.getId().equalsIgnoreCase(kunci.trim()) || a.getNama().equalsIgnoreCase(kunci.trim())) return a;
        return null;
    }

    /** Enqueue: mengajukan peminjaman ke antrean. */
    public String ajukanPeminjaman(String kunciBuku, String kunciAnggota, int lama) {
        Buku b = cariBuku(kunciBuku);
        Anggota a = cariAnggota(kunciAnggota);
        if (b == null) throw new IllegalArgumentException("Buku tidak ditemukan.");
        if (a == null) throw new IllegalArgumentException("Anggota tidak ditemukan.");
        Peminjaman p = new Peminjaman("PJ" + (counter + 1), b, a, lama);  // validasi lama pinjam
        if (!antrian.enqueue(p)) throw new IllegalStateException("Antrean penuh, proses antrean dulu.");
        counter++;
        return "Permintaan " + p.getKode() + " masuk antrean (posisi " + antrian.size() + ").";
    }

    /** Dequeue: memproses permintaan terdepan sambil menerapkan aturan bisnis. */
    public String prosesAntrian() {
        Peminjaman p = antrian.dequeue();
        if (p == null) throw new IllegalStateException("Antrean kosong.");
        if (!p.getAnggota().bisaMeminjam())
            throw new IllegalStateException("DITOLAK " + p.getKode() + ": " + p.getAnggota().getNama()
                    + " nonaktif/sudah meminjam maksimal 3 buku.");
        if (!p.getBuku().isTersedia())
            throw new IllegalStateException("DITOLAK " + p.getKode() + ": stok '" + p.getBuku().getJudul() + "' habis.");
        p.getBuku().kurangiStok();
        p.getAnggota().tambahPinjaman();
        riwayat.add(p);
        return "DISETUJUI " + p.getKode() + ": " + p.getAnggota().getNama() + " meminjam '"
                + p.getBuku().getJudul() + "' (Rp" + (long) p.hitungBiaya() + ").";
    }

    /** Mengembalikan buku berdasarkan kode transaksi. */
    public String kembalikanBuku(String kode) {
        for (Peminjaman p : riwayat) {
            if (p.getKode().equalsIgnoreCase(kode.trim())) {
                p.kembalikan();
                totalPendapatan += p.hitungBiaya();
                return "Buku dikembalikan. Biaya Rp" + (long) p.hitungBiaya() + ".";
            }
        }
        throw new IllegalArgumentException("Transaksi " + kode + " tidak ditemukan.");
    }

    public void tampilkanBuku() {
        if (daftarBuku.isEmpty()) { System.out.println("(Belum ada buku)"); return; }
        System.out.println(Buku.header());
        for (Buku b : daftarBuku) b.tampilkanInfo();
    }

    public void tampilkanAnggota() {
        if (daftarAnggota.isEmpty()) { System.out.println("(Belum ada anggota)"); return; }
        System.out.println(Anggota.header());
        for (Anggota a : daftarAnggota) a.tampilkanInfo();
    }

    public void tampilkanRiwayat() {
        if (riwayat.isEmpty()) { System.out.println("(Belum ada transaksi)"); return; }
        System.out.println(Peminjaman.header());
        for (Peminjaman p : riwayat) p.tampilkanInfo();
    }

    public void tampilkanRingkasan() {
        System.out.printf("%-18s: %s%n", "Perpustakaan", nama + (buka ? " (buka)" : " (tutup)"));
        System.out.printf("%-18s: %d%n", "Jumlah buku", daftarBuku.size());
        System.out.printf("%-18s: %d%n", "Jumlah anggota", daftarAnggota.size());
        System.out.printf("%-18s: %d%n", "Transaksi diproses", riwayat.size());
        System.out.printf("%-18s: %d%n", "Antrean menunggu", antrian.size());
        System.out.printf("%-18s: Rp%d%n", "Total pendapatan", (long) totalPendapatan);
    }
}