
public class Buku {
    private String kode;
    private String judul;
    private String pengarang;
    private int stok;

    public Buku() {
        this("B000", "Tanpa Judul", "Anonim", 0);
    }

    public Buku(String kode, String judul, String pengarang, int stok) {
        setKode(kode);
        setJudul(judul);
        setPengarang(pengarang);
        setStok(stok);
    }

    public String getKode() { return kode; }
    public String getJudul() { return judul; }
    public String getPengarang() { return pengarang; }
    public int getStok() { return stok; }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) throw new IllegalArgumentException("Kode buku tidak boleh kosong.");
        this.kode = kode.trim().toUpperCase();
    }

    public void setJudul(String judul) {
        if (judul == null || judul.trim().isEmpty()) throw new IllegalArgumentException("Judul tidak boleh kosong.");
        this.judul = judul.trim();
    }

    public void setPengarang(String pengarang) {
        if (pengarang == null || pengarang.trim().isEmpty()) throw new IllegalArgumentException("Pengarang tidak boleh kosong.");
        this.pengarang = pengarang.trim();
    }

    public void setStok(int stok) {
        if (stok < 0) throw new IllegalArgumentException("Stok buku tidak boleh negatif.");
        this.stok = stok;
    }

    public boolean isTersedia() { return stok > 0; }

    public void kurangiStok() {
        if (stok <= 0) throw new IllegalStateException("Stok buku '" + judul + "' habis.");
        stok--;
    }

    public void tambahStok() { stok++; }

    public static String header() {
        String h = String.format("%-6s | %-25s | %-15s | %4s", "Kode", "Judul", "Pengarang", "Stok");
        return h + "\n" + "-".repeat(h.length());
    }

    public void tampilkanInfo() {
        System.out.printf("%-6s | %-25s | %-15s | %4d%n", kode, judul, pengarang, stok);
    }
}