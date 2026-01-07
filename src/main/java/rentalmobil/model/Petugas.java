package rentalmobil.model;

public class Petugas extends User {

    public Petugas() {
        super();
    }

    public Petugas(int idUser, String username, String password, String namaLengkap) {
        super(idUser, username, password, namaLengkap, "PETUGAS");
    }

    public void inputTransaksi() {
        System.out.println("Petugas " + getNamaLengkap() + " sedang menginput transaksi sewa.");
    }

    public void prosesPengembalian() {
        System.out.println("Petugas " + getNamaLengkap() + " sedang memproses pengembalian mobil.");
    }

    public void cetakNota() {
        System.out.println("Petugas " + getNamaLengkap() + " mencetak nota transaksi.");
    }

    @Override
    public String getRoleDescription() {
        return "Petugas";
    }
}
