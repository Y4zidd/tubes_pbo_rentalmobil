package rentalmobil.service;

import java.sql.SQLException;
import java.time.LocalDate;
import rentalmobil.dao.KonsumenDao;
import rentalmobil.dao.MobilDao;
import rentalmobil.dao.TransaksiDao;
import rentalmobil.model.Konsumen;
import rentalmobil.model.Mobil;
import rentalmobil.model.Transaksi;

public class RentalService {

    private final MobilDao mobilDao = new MobilDao();
    private final KonsumenDao konsumenDao = new KonsumenDao();
    private final TransaksiDao transaksiDao = new TransaksiDao();

    public int sewaMobil(String noKTP, String nama, String alamat, String telp, int idMobil, int idUserPetugas, LocalDate tglSewa, LocalDate tglRencana) throws SQLException {

        konsumenDao.upsert(new Konsumen(noKTP, nama, alamat, telp));

        Mobil m = mobilDao.findById(idMobil);
        if (m == null) {
            throw new SQLException("Mobil tidak ditemukan");
        }
        if (!"TERSEDIA".equals(m.getStatus())) {
            throw new SQLException("Mobil tidak tersedia");
        }
        long lama = Transaksi.hitungLamaSewa(tglSewa, tglRencana);
        int total = (int) (lama * m.getHargaSewaHarian());
        Transaksi t = new Transaksi(0, noKTP, idMobil, idUserPetugas, tglSewa, tglRencana, null, 0, "DISEWA", total);
        int id = transaksiDao.insert(t);
        mobilDao.setStatus(idMobil, "DISEWA");
        return id;
    }

    public void prosesPengembalian(int idTransaksi, int idMobil, LocalDate tglKembali, LocalDate tglRencana) throws SQLException {
        int denda = 0;
        if (tglRencana != null && tglKembali.isAfter(tglRencana)) {
            long terlambat = java.time.temporal.ChronoUnit.DAYS.between(tglRencana, tglKembali);
            denda = (int) (terlambat * 50000);
        }
        transaksiDao.updateStatusSelesai(idTransaksi, denda, tglKembali);
        mobilDao.setStatus(idMobil, "TERSEDIA");
    }
}

