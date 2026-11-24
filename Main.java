import java.util.Scanner;

/** Menu console Sistem Perpustakaan Mini (ADT Queue). */
public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final Perpustakaan PERPUS = new Perpustakaan("Perpustakaan Mini");

    public static void main(String[] args) {
        int pilih;
        do {
            tampilkanMenu();
            pilih = bacaInt("Pilih menu (1-7): ");
            System.out.println();
            try {  // pelanggaran aturan ditampilkan sebagai pesan, bukan crash
                jalankan(pilih);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        } while (pilih != 7);
    }

    private static void tampilkanMenu() {
        System.out.println("\n==============================");
        System.out.println("      PERPUSTAKAAN MINI");
        System.out.println("==============================");
        System.out.println(" 1. Data Buku");
        System.out.println(" 2. Data Anggota");
        System.out.println(" 3. Transaksi");
        System.out.println(" 4. Cari Data");
        System.out.println(" 5. Operasi Antrean (Queue)");
        System.out.println(" 6. Ringkasan");
        System.out.println(" 7. Keluar");
        System.out.println("------------------------------");
    }

    private static void jalankan(int pilih) {
        switch (pilih) {
            case 1: menuBuku(); break;
            case 2: menuAnggota(); break;
            case 3: menuTransaksi(); break;
            case 4: menuCari(); break;
            case 5: menuQueue(); break;
            case 6: judul("RINGKASAN"); PERPUS.tampilkanRingkasan(); break;
            case 7: System.out.println("Terima kasih. Program selesai."); break;
            default: System.out.println("[ERROR] Menu harus antara 1 dan 7.");
        }
    }

    private static void menuBuku() {
        if (subMenu("DATA BUKU", "Tambah buku", "Tampilkan semua buku") == 1) {
            PERPUS.tambahBuku(new Buku(bacaTeks("Kode      : "), bacaTeks("Judul     : "),
                    bacaTeks("Pengarang : "), bacaInt("Stok      : ")));
            System.out.println("[OK] Buku berhasil ditambahkan.");
        } else {
            PERPUS.tampilkanBuku();
        }
    }

    private static void menuAnggota() {
        if (subMenu("DATA ANGGOTA", "Tambah anggota", "Tampilkan semua anggota") == 1) {
            PERPUS.tambahAnggota(new Anggota(bacaTeks("ID   : "), bacaTeks("Nama : ")));
            System.out.println("[OK] Anggota berhasil ditambahkan.");
        } else {
            PERPUS.tampilkanAnggota();
        }
    }

    private static void menuTransaksi() {
        int p = subMenu("TRANSAKSI", "Ajukan peminjaman (masuk antrean)", "Kembalikan buku", "Riwayat transaksi");
        if (p == 1) System.out.println("[OK] " + ajukan());
        else if (p == 2) System.out.println("[OK] " + PERPUS.kembalikanBuku(bacaTeks("Kode transaksi: ")));
        else PERPUS.tampilkanRiwayat();
    }

    private static String ajukan() {
        return PERPUS.ajukanPeminjaman(bacaTeks("Kode/judul buku  : "),
                bacaTeks("ID/nama anggota : "), bacaInt("Lama pinjam (hari): "));
    }

    private static void menuCari() {
        boolean cariBuku = subMenu("CARI DATA", "Cari buku", "Cari anggota") == 1;
        String kunci = bacaTeks("Kata kunci (kode/nama): ");
        System.out.println();
        if (cariBuku) {
            Buku b = PERPUS.cariBuku(kunci);
            if (b == null) { System.out.println("[ERROR] Buku tidak ditemukan."); return; }
            System.out.println(Buku.header());
            b.tampilkanInfo();
        } else {
            Anggota a = PERPUS.cariAnggota(kunci);
            if (a == null) { System.out.println("[ERROR] Anggota tidak ditemukan."); return; }
            System.out.println(Anggota.header());
            a.tampilkanInfo();
        }
    }

    private static void menuQueue() {
        AntrianPeminjaman q = PERPUS.getAntrian();
        switch (subMenu("OPERASI ANTREAN", "Enqueue (ajukan peminjaman)", "Dequeue (proses terdepan)",
                "Front (lihat terdepan)", "isEmpty / isFull", "Size", "Tampilkan isi")) {
            case 1: System.out.println("[OK] " + ajukan()); break;
            case 2: System.out.println("[OK] " + PERPUS.prosesAntrian()); break;
            case 3:
                if (q.isEmpty()) System.out.println("Antrean kosong.");
                else { System.out.println(Peminjaman.header()); q.front().tampilkanInfo(); }
                return;
            case 4: System.out.println("isEmpty = " + q.isEmpty() + " | isFull = " + q.isFull()); return;
            case 5: System.out.println("Size = " + q.size()); return;
            default: break;
        }
        System.out.println();
        judul("ISI ANTREAN");
        q.tampilkan();
    }

    // ===================== Helper tampilan & input =====================
    private static void judul(String teks) {
        System.out.println("--- " + teks + " ---");
    }

    /** Menampilkan submenu dan mengembalikan pilihan valid (1..jumlah opsi). */
    private static int subMenu(String judul, String... opsi) {
        judul(judul);
        for (int i = 0; i < opsi.length; i++) System.out.println("  " + (i + 1) + ". " + opsi[i]);
        while (true) {
            int p = bacaInt("  Pilih: ");
            if (p >= 1 && p <= opsi.length) { System.out.println(); return p; }
            System.out.println("  Pilihan harus 1-" + opsi.length + ".");
        }
    }

    /** Membaca teks tidak kosong. */
    private static String bacaTeks(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SC.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("  Input tidak boleh kosong.");
        }
    }

    /** Membaca angka bulat; huruf/kosong diulang tanpa crash. */
    private static int bacaInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(SC.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Masukkan angka yang valid.");
            }
        }
    }
}