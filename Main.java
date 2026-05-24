import java.util.ArrayList;
import java.util.Scanner;

// Kelas Menu untuk merepresentasikan item menu
class Menu {
    private String nama;
    private double harga;
    private String kategori; // "Makanan" atau "Minuman"

    public Menu(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    @Override
    public String toString() {
        return String.format("%s - Rp%.0f", nama, harga);
    }
}

// Kelas untuk item pesanan (menu + jumlah)
class OrderItem {
    private Menu menu;
    private int quantity;

    public OrderItem(Menu menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int qty) {
        this.quantity += qty;
    }

    public double getSubtotal() {
        return menu.getHarga() * quantity;
    }
}

// Kelas utama
public class Main {
    private static ArrayList<Menu> daftarMenu = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Inisialisasi data awal (minimal 4 makanan, 4 minuman)
        inisialisasiMenu();

        int pilihan;
        do {
            tampilkanMenuUtama();
            pilihan = bacaInt("Pilihan Anda: ");
            switch (pilihan) {
                case 1:
                    prosesPemesanan();
                    break;
                case 2:
                    kelolaMenu();
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan aplikasi restoran.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (pilihan != 3);
    }

    private static void inisialisasiMenu() {
        // Makanan
        daftarMenu.add(new Menu("Nasi Goreng", 25000, "Makanan"));
        daftarMenu.add(new Menu("Mie Ayam", 20000, "Makanan"));
        daftarMenu.add(new Menu("Ayam Bakar", 35000, "Makanan"));
        daftarMenu.add(new Menu("Sate Ayam", 30000, "Makanan"));
        // Minuman
        daftarMenu.add(new Menu("Es Teh Manis", 5000, "Minuman"));
        daftarMenu.add(new Menu("Jus Jeruk", 12000, "Minuman"));
        daftarMenu.add(new Menu("Kopi Hitam", 10000, "Minuman"));
        daftarMenu.add(new Menu("Air Mineral", 4000, "Minuman"));
    }

    private static void tampilkanMenuUtama() {
        System.out.println("\n===== RESTORAN KITA =====");
        System.out.println("1. Pemesanan (Pelanggan)");
        System.out.println("2. Manajemen Menu (Pemilik)");
        System.out.println("3. Keluar");
    }

    // ==================== FUNGSI PEMESANAN ====================
    private static void prosesPemesanan() {
        ArrayList<OrderItem> pesanan = new ArrayList<>();
        String input;

        System.out.println("\n--- DAFTAR MENU ---");
        tampilkanDaftarMenu(); // menampilkan menu kelompok makanan & minuman

        System.out.println("\nSilakan pesan (masukkan nomor menu, atau ketik 'selesai' untuk mengakhiri):");
        while (true) {
            System.out.print("Nomor menu / 'selesai': ");
            input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("selesai")) {
                break;
            }
            int nomor;
            try {
                nomor = Integer.parseInt(input);
                if (nomor < 1 || nomor > daftarMenu.size()) {
                    System.out.println("Nomor menu tidak valid. Silakan pilih lagi.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak dikenal. Masukkan nomor menu atau 'selesai'.");
                continue;
            }

            Menu selected = daftarMenu.get(nomor - 1);
            System.out.print("Jumlah: ");
            int jumlah;
            try {
                jumlah = Integer.parseInt(scanner.nextLine());
                if (jumlah <= 0) {
                    System.out.println("Jumlah harus positif.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Jumlah tidak valid.");
                continue;
            }

            // Cek apakah menu sudah ada di pesanan
            boolean found = false;
            for (OrderItem item : pesanan) {
                if (item.getMenu().getNama().equals(selected.getNama())) {
                    item.addQuantity(jumlah);
                    found = true;
                    break;
                }
            }
            if (!found) {
                pesanan.add(new OrderItem(selected, jumlah));
            }
            System.out.println(selected.getNama() + " x" + jumlah + " ditambahkan ke pesanan.");
        }

        if (pesanan.isEmpty()) {
            System.out.println("Tidak ada pesanan. Kembali ke menu utama.");
            return;
        }

        cetakStruk(pesanan);
    }

    private static void tampilkanDaftarMenu() {
        System.out.println("\n[MAKANAN]");
        int nomor = 1;
        for (Menu m : daftarMenu) {
            if (m.getKategori().equals("Makanan")) {
                System.out.printf("%d. %s - Rp%.0f\n", nomor, m.getNama(), m.getHarga());
            }
            nomor++;
        }
        System.out.println("\n[MINUMAN]");
        nomor = 1;
        for (Menu m : daftarMenu) {
            if (m.getKategori().equals("Minuman")) {
                System.out.printf("%d. %s - Rp%.0f\n", nomor, m.getNama(), m.getHarga());
            }
            nomor++;
        }
        // Tampilkan nomor urut global agar pelanggan mudah memilih
        System.out.println("\n(Daftar lengkap dengan nomor urut)");
        for (int i = 0; i < daftarMenu.size(); i++) {
            Menu m = daftarMenu.get(i);
            System.out.printf("%d. %s - %s - Rp%.0f\n", i + 1, m.getNama(), m.getKategori(), m.getHarga());
        }
    }

    // ==================== PERHITUNGAN DAN STRUK ====================
    private static void cetakStruk(ArrayList<OrderItem> pesanan) {
        // Hitung subtotal awal
        double subtotal = 0;
        for (OrderItem item : pesanan) {
            subtotal += item.getSubtotal();
        }

        // Penawaran: beli 1 gratis 1 untuk minuman jika subtotal > 50000
        double potonganPromo = 0;
        if (subtotal > 50000) {
            double minHargaMinuman = Double.MAX_VALUE;
            for (OrderItem item : pesanan) {
                Menu m = item.getMenu();
                if (m.getKategori().equals("Minuman") && m.getHarga() < minHargaMinuman) {
                    minHargaMinuman = m.getHarga();
                }
            }
            if (minHargaMinuman != Double.MAX_VALUE) {
                potonganPromo = minHargaMinuman;
            }
        }

        double setelahPromo = subtotal - potonganPromo;

        // Diskon 10% jika setelah promo > 100000
        double diskon = 0;
        if (setelahPromo > 100000) {
            diskon = setelahPromo * 0.10;
        }

        double setelahDiskon = setelahPromo - diskon;

        // Pajak 10% dari total setelah diskon
        double pajak = setelahDiskon * 0.10;
        double biayaPelayanan = 20000;
        double totalBayar = setelahDiskon + pajak + biayaPelayanan;

        // CETAK STRUK
        System.out.println("\n========== STRUK PEMBELIAN ==========");
        System.out.printf("%-20s %-5s %-10s %-12s\n", "Nama Item", "Qty", "Harga", "Subtotal");
        for (OrderItem item : pesanan) {
            Menu m = item.getMenu();
            System.out.printf("%-20s %-5d Rp%-8.0f Rp%-10.0f\n", m.getNama(), item.getQuantity(),
                    m.getHarga(), item.getSubtotal());
        }
        System.out.println("--------------------------------------");
        System.out.printf("%-30s Rp%-10.0f\n", "Subtotal:", subtotal);
        if (potonganPromo > 0) {
            System.out.printf("%-30s Rp%-10.0f\n", "Penawaran (gratis 1 minuman):", -potonganPromo);
        }
        System.out.printf("%-30s Rp%-10.0f\n", "Setelah Promo:", setelahPromo);
        if (diskon > 0) {
            System.out.printf("%-30s Rp%-10.0f\n", "Diskon 10% (>100rb):", -diskon);
        }
        System.out.printf("%-30s Rp%-10.0f\n", "Setelah Diskon:", setelahDiskon);
        System.out.printf("%-30s Rp%-10.0f\n", "Pajak 10%:", pajak);
        System.out.printf("%-30s Rp%-10.0f\n", "Biaya Pelayanan:", biayaPelayanan);
        System.out.println("--------------------------------------");
        System.out.printf("%-30s Rp%-10.0f\n", "TOTAL BAYAR:", totalBayar);
        System.out.println("======================================\n");
    }

    // ==================== MANAJEMEN MENU ====================
    private static void kelolaMenu() {
        int pilihan;
        do {
            System.out.println("\n--- MANAJEMEN MENU RESTORAN ---");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama");
            pilihan = bacaInt("Pilihan: ");
            switch (pilihan) {
                case 1:
                    tambahMenu();
                    break;
                case 2:
                    ubahHargaMenu();
                    break;
                case 3:
                    hapusMenu();
                    break;
                case 4:
                    System.out.println("Kembali ke menu utama.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 4);
    }

    private static void tambahMenu() {
        System.out.println("\n--- TAMBAH MENU BARU ---");
        System.out.print("Nama menu: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("Nama tidak boleh kosong.");
            return;
        }
        double harga = bacaDouble("Harga: ");
        if (harga <= 0) {
            System.out.println("Harga harus positif.");
            return;
        }
        String kategori;
        while (true) {
            System.out.print("Kategori (Makanan/Minuman): ");
            kategori = scanner.nextLine().trim();
            if (kategori.equalsIgnoreCase("Makanan") || kategori.equalsIgnoreCase("Minuman")) {
                break;
            }
            System.out.println("Kategori harus 'Makanan' atau 'Minuman'.");
        }
        daftarMenu.add(new Menu(nama, harga, kategori));
        System.out.println("Menu '" + nama + "' berhasil ditambahkan.");
    }

    private static void ubahHargaMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("Belum ada menu.");
            return;
        }
        System.out.println("\n--- UBAH HARGA MENU ---");
        tampilkanSemuaMenu();
        int nomor = bacaInt("Pilih nomor menu yang akan diubah: ", 1, daftarMenu.size());
        Menu m = daftarMenu.get(nomor - 1);
        System.out.printf("Menu: %s, Harga lama: Rp%.0f\n", m.getNama(), m.getHarga());
        double hargaBaru = bacaDouble("Harga baru: ");
        if (hargaBaru <= 0) {
            System.out.println("Harga harus positif.");
            return;
        }
        System.out.print("Yakin ingin mengubah? (Ya/Tidak): ");
        String konfirmasi = scanner.nextLine().trim();
        if (konfirmasi.equalsIgnoreCase("Ya")) {
            m.setHarga(hargaBaru);
            System.out.println("Harga berhasil diubah.");
        } else {
            System.out.println("Perubahan dibatalkan.");
        }
    }

    private static void hapusMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("Belum ada menu.");
            return;
        }
        System.out.println("\n--- HAPUS MENU ---");
        tampilkanSemuaMenu();
        int nomor = bacaInt("Pilih nomor menu yang akan dihapus: ", 1, daftarMenu.size());
        Menu m = daftarMenu.get(nomor - 1);
        System.out.printf("Menu: %s - Rp%.0f\n", m.getNama(), m.getHarga());
        System.out.print("Yakin ingin menghapus? (Ya/Tidak): ");
        String konfirmasi = scanner.nextLine().trim();
        if (konfirmasi.equalsIgnoreCase("Ya")) {
            daftarMenu.remove(nomor - 1);
            System.out.println("Menu berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    private static void tampilkanSemuaMenu() {
        System.out.println("Daftar Menu:");
        for (int i = 0; i < daftarMenu.size(); i++) {
            Menu m = daftarMenu.get(i);
            System.out.printf("%d. %s (%s) - Rp%.0f\n", i + 1, m.getNama(), m.getKategori(), m.getHarga());
        }
    }

    // ==================== UTILITY INPUT ====================
    private static int bacaInt(String pesan) {
        System.out.print(pesan);
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka. " + pesan);
            scanner.next();
        }
        int hasil = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return hasil;
    }

    private static int bacaInt(String pesan, int min, int max) {
        int nilai;
        while (true) {
            nilai = bacaInt(pesan);
            if (nilai >= min && nilai <= max)
                break;
            System.out.printf("Masukkan angka antara %d - %d.\n", min, max);
        }
        return nilai;
    }

    private static double bacaDouble(String pesan) {
        System.out.print(pesan);
        while (!scanner.hasNextDouble()) {
            System.out.print("Input harus angka. " + pesan);
            scanner.next();
        }
        double hasil = scanner.nextDouble();
        scanner.nextLine();
        return hasil;
    }
}