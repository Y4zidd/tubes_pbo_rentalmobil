package rentalmobil.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaksi {

    private int idTransaksi;
    private String noKTP;
    private int idMobil;
    private int idUserPetugas;
    private LocalDate tglSewa;
    private LocalDate tglKembaliRencana;
    private LocalDate tglKembali;
    private int denda;
    private String status;
    private int totalBayar;

    public Transaksi() {
    }

    public Transaksi(int idTransaksi, String noKTP, int idMobil, int idUserPetugas, LocalDate tglSewa, LocalDate tglKembaliRencana, LocalDate tglKembali, int denda, String status, int totalBayar) {
        this.idTransaksi = idTransaksi;
        this.noKTP = noKTP;
        this.idMobil = idMobil;
        this.idUserPetugas = idUserPetugas;
        this.tglSewa = tglSewa;
        this.tglKembaliRencana = tglKembaliRencana;
        this.tglKembali = tglKembali;
        this.denda = denda;
        this.status = status;
        this.totalBayar = totalBayar;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getNoKTP() {
        return noKTP;
    }

    public void setNoKTP(String noKTP) {
        this.noKTP = noKTP;
    }

    public int getIdMobil() {
        return idMobil;
    }

    public void setIdMobil(int idMobil) {
        this.idMobil = idMobil;
    }

    public int getIdUserPetugas() {
        return idUserPetugas;
    }

    public void setIdUserPetugas(int idUserPetugas) {
        this.idUserPetugas = idUserPetugas;
    }

    public LocalDate getTglSewa() {
        return tglSewa;
    }

    public void setTglSewa(LocalDate tglSewa) {
        this.tglSewa = tglSewa;
    }

    public LocalDate getTglKembaliRencana() {
        return tglKembaliRencana;
    }

    public void setTglKembaliRencana(LocalDate tglKembaliRencana) {
        this.tglKembaliRencana = tglKembaliRencana;
    }

    public LocalDate getTglKembali() {
        return tglKembali;
    }

    public void setTglKembali(LocalDate tglKembali) {
        this.tglKembali = tglKembali;
    }

    public int getDenda() {
        return denda;
    }

    public void setDenda(int denda) {
        this.denda = denda;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(int totalBayar) {
        this.totalBayar = totalBayar;
    }

    public static long hitungLamaSewa(LocalDate mulai, LocalDate sampai) {
        long d = ChronoUnit.DAYS.between(mulai, sampai);
        return d <= 0 ? 1 : d;
    }

    public int hitungtotal() {
        return totalBayar;
    }

    public int HitungDenda() {
        if (tglKembaliRencana == null || tglKembali == null) {
            denda = 0;
            return denda;
        }
        if (!tglKembali.isAfter(tglKembaliRencana)) {
            denda = 0;
            return denda;
        }
        long terlambat = ChronoUnit.DAYS.between(tglKembaliRencana, tglKembali);
        denda = (int) (terlambat * 50000);
        return denda;
    }

    public void SimpanTransaksi() {
    }
}
