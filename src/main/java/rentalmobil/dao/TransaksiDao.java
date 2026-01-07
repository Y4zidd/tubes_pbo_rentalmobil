package rentalmobil.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import rentalmobil.config.DB;
import rentalmobil.model.Transaksi;

public class TransaksiDao {

    public static class MonthlyIncome {

        private final int year;
        private final int month;
        private final int total;

        public MonthlyIncome(int year, int month, int total) {
            this.year = year;
            this.month = month;
            this.total = total;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getTotal() {
            return total;
        }
    }

    private Transaksi map(ResultSet rs) throws SQLException {
        Transaksi t = new Transaksi();
        t.setIdTransaksi(rs.getInt("idTransaksi"));
        t.setNoKTP(rs.getString("noKTP"));
        t.setIdMobil(rs.getInt("idMobil"));
        t.setIdUserPetugas(rs.getInt("idUserPetugas"));
        Date sewa = rs.getDate("tglSewa");
        t.setTglSewa(sewa != null ? sewa.toLocalDate() : null);
        Date rencana = rs.getDate("tglKembaliRencana");
        t.setTglKembaliRencana(rencana != null ? rencana.toLocalDate() : null);
        Date kembali = rs.getDate("tglKembali");
        t.setTglKembali(kembali != null ? kembali.toLocalDate() : null);
        t.setDenda(rs.getInt("denda"));
        t.setStatus(rs.getString("status"));
        t.setTotalBayar(rs.getInt("totalBayar"));
        return t;
    }

    public int insert(Transaksi t) throws SQLException {
        String sql = "INSERT INTO transaksi(noKTP,idMobil,idUserPetugas,tglSewa,tglKembaliRencana,tglKembali,denda,status,totalBayar) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNoKTP());
            ps.setInt(2, t.getIdMobil());
            ps.setInt(3, t.getIdUserPetugas());
            ps.setDate(4, Date.valueOf(t.getTglSewa()));
            ps.setDate(5, t.getTglKembaliRencana() == null ? null : Date.valueOf(t.getTglKembaliRencana()));
            ps.setDate(6, t.getTglKembali() == null ? null : Date.valueOf(t.getTglKembali()));
            ps.setInt(7, t.getDenda());
            ps.setString(8, t.getStatus());
            ps.setInt(9, t.getTotalBayar());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void updateStatusSelesai(int idTransaksi, int denda, LocalDate tglKembali) throws SQLException {
        String sql = "UPDATE transaksi SET status='SELESAI', denda=?, tglKembali=? WHERE idTransaksi=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, denda);
            ps.setDate(2, Date.valueOf(tglKembali));
            ps.setInt(3, idTransaksi);
            ps.executeUpdate();
        }
    }

    public Transaksi findById(int id) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM transaksi WHERE idTransaksi=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public List<Transaksi> findActive() throws SQLException {
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM transaksi WHERE status='DISEWA' ORDER BY idTransaksi DESC")) {
            List<Transaksi> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public List<Transaksi> findCompleted() throws SQLException {
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM transaksi WHERE status='SELESAI' ORDER BY idTransaksi DESC")) {
            List<Transaksi> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public int getTotalIncome() throws SQLException {
        String sql = "SELECT COALESCE(SUM(totalBayar),0) FROM transaksi WHERE status='SELESAI'";
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<MonthlyIncome> getMonthlyIncome() throws SQLException {
        String sql = "SELECT YEAR(tglSewa) AS y, MONTH(tglSewa) AS m, COALESCE(SUM(totalBayar),0) AS total "
                + "FROM transaksi WHERE status='SELESAI' GROUP BY YEAR(tglSewa), MONTH(tglSewa) ORDER BY y, m";
        List<MonthlyIncome> list = new ArrayList<>();
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MonthlyIncome(rs.getInt("y"), rs.getInt("m"), rs.getInt("total")));
            }
        }
        return list;
    }

    public List<Object[]> getMonthlyDetails(int year, int month) throws SQLException {
        String sql = "SELECT t.idTransaksi, t.noKTP, t.idMobil, t.tglSewa, t.tglKembali, t.status, "
                + "m.namaMobil, m.platNomor, COALESCE(t.totalBayar,0) AS totalBayar "
                + "FROM transaksi t JOIN mobil m ON t.idMobil = m.idMobil "
                + "WHERE t.status IN ('DISEWA','SELESAI') "
                + "AND YEAR(t.tglSewa)=? AND MONTH(t.tglSewa)=? "
                + "ORDER BY t.tglSewa, t.idTransaksi";
        List<Object[]> list = new ArrayList<>();
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getInt("idTransaksi"),
                        rs.getString("noKTP"),
                        rs.getInt("idMobil"),
                        rs.getDate("tglSewa") != null ? rs.getDate("tglSewa").toLocalDate() : null,
                        rs.getDate("tglKembali") != null ? rs.getDate("tglKembali").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("namaMobil"),
                        rs.getString("platNomor"),
                        rs.getInt("totalBayar")
                    });
                }
            }
        }
        return list;
    }
}
