
public class AntrianPeminjaman {
    private Peminjaman[] data;
    private int depan;   // indeks elemen terdepan
    private int jumlah;  // banyak elemen

    public AntrianPeminjaman() { this(5); }

    public AntrianPeminjaman(int kapasitas) {
        data = new Peminjaman[kapasitas > 0 ? kapasitas : 5];
    }

    public boolean enqueue(Peminjaman p) {
        if (isFull()) return false;
        data[(depan + jumlah) % data.length] = p;
        jumlah++;
        return true;
    }
    public Peminjaman dequeue() {
        if (isEmpty()) return null;
        Peminjaman p = data[depan];
        data[depan] = null;
        depan = (depan + 1) % data.length;
        jumlah--;
        return p;
    }

    public Peminjaman front() { return isEmpty() ? null : data[depan]; }

    public boolean isEmpty() { return jumlah == 0; }
    public boolean isFull() { return jumlah == data.length; }
    public int size() { return jumlah; }

    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("(Antrean kosong)");
            return;
        }
        System.out.println(Peminjaman.header());
        for (int i = 0; i < jumlah; i++) data[(depan + i) % data.length].tampilkanInfo();
    }
}