package rentalmobil.model;

public class Konsumen {

    private String noKTP;
    private String namaKonsumen;
    private String tempatTinggal;
    private String nomorTelepon;

    public Konsumen() {
    }

    public Konsumen(String noKTP, String namaKonsumen, String tempatTinggal, String nomorTelepon) {
        this.noKTP = noKTP;
        this.namaKonsumen = namaKonsumen;
        this.tempatTinggal = tempatTinggal;
        this.nomorTelepon = nomorTelepon;
    }

    public String getNoKTP() {
        return noKTP;
    }

    public void setNoKTP(String noKTP) {
        this.noKTP = noKTP;
    }

    public String getNamaKonsumen() {
        return namaKonsumen;
    }

    public void setNamaKonsumen(String namaKonsumen) {
        this.namaKonsumen = namaKonsumen;
    }

    public String getTempatTinggal() {
        return tempatTinggal;
    }

    public void setTempatTinggal(String tempatTinggal) {
        this.tempatTinggal = tempatTinggal;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String GetInfoPelanggan() {
        return noKTP + " - " + namaKonsumen;
    }
}
