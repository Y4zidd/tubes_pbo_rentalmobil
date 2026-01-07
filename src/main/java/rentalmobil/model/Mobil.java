package rentalmobil.model;

public class Mobil {

    private int idMobil;
    private int idMerek;
    private String namaMobil;
    private String tipeMobil;
    private String platNomor;
    private int tahun;
    private int hargaSewaHarian;
    private String gambarPath;
    private String status;
    private String merekName;

    public Mobil() {
    }

    public Mobil(int idMobil, int idMerek, String namaMobil, String tipeMobil, String platNomor, int tahun, int hargaSewaHarian, String gambarPath, String status) {
        this.idMobil = idMobil;
        this.idMerek = idMerek;
        this.namaMobil = namaMobil;
        this.tipeMobil = tipeMobil;
        this.platNomor = platNomor;
        this.tahun = tahun;
        this.hargaSewaHarian = hargaSewaHarian;
        this.gambarPath = gambarPath;
        this.status = status;
    }

    public int getIdMobil() {
        return idMobil;
    }

    public void setIdMobil(int idMobil) {
        this.idMobil = idMobil;
    }

    public int getIdMerek() {
        return idMerek;
    }

    public void setIdMerek(int idMerek) {
        this.idMerek = idMerek;
    }

    public String getNamaMobil() {
        return namaMobil;
    }

    public void setNamaMobil(String namaMobil) {
        this.namaMobil = namaMobil;
    }

    public String getTipeMobil() {
        return tipeMobil;
    }

    public void setTipeMobil(String tipeMobil) {
        this.tipeMobil = tipeMobil;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public void setPlatNomor(String platNomor) {
        this.platNomor = platNomor;
    }

    public int getTahun() {
        return tahun;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    public int getHargaSewaHarian() {
        return hargaSewaHarian;
    }

    public void setHargaSewaHarian(int hargaSewaHarian) {
        this.hargaSewaHarian = hargaSewaHarian;
    }

    public String getGambarPath() {
        return gambarPath;
    }

    public void setGambarPath(String gambarPath) {
        this.gambarPath = gambarPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerekName() {
        return merekName;
    }

    public void setMerekName(String merekName) {
        this.merekName = merekName;
    }

    @Override
    public String toString() {
        String brand = (merekName != null && !merekName.isEmpty()) ? (merekName + " ") : "";
        return brand + namaMobil + " [" + platNomor + "]";
    }
}

