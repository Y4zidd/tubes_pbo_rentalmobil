package rentalmobil.model;

public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(int idUser, String username, String password, String namaLengkap) {
        super(idUser, username, password, namaLengkap, "ADMIN");
    }

    public void kelolaDataMaster() {
        System.out.println("Admin " + getNamaLengkap() + " sedang mengelola data master.");
    }

    public void kelolaLaporan() {
        System.out.println("Admin " + getNamaLengkap() + " sedang mengelola laporan pendapatan.");
    }

    public void kelolaPengguna() {
        System.out.println("Admin " + getNamaLengkap() + " sedang mengelola data pengguna/petugas.");
    }

    public void validasiInput() {
        System.out.println("Admin " + getNamaLengkap() + " melakukan validasi input sebelum disimpan.");
    }

    @Override
    public String getRoleDescription() {
        return "Admin";
    }
}
