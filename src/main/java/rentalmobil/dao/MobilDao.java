package rentalmobil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rentalmobil.config.DB;
import rentalmobil.model.Mobil;

public class MobilDao {

    private Mobil map(ResultSet rs) throws SQLException {
        Mobil m = new Mobil();
        m.setIdMobil(rs.getInt("idMobil"));
        m.setIdMerek(rs.getInt("idMerek"));
        m.setNamaMobil(rs.getString("namaMobil"));
        m.setTipeMobil(rs.getString("tipeMobil"));
        m.setPlatNomor(rs.getString("platNomor"));
        m.setTahun(rs.getInt("tahun"));
        m.setHargaSewaHarian(rs.getInt("hargaSewaHarian"));
        try {
            m.setGambarPath(rs.getString("gambarPath"));
        } catch (SQLException ignore) {
        }
        m.setStatus(rs.getString("status"));
        try {
            m.setMerekName(rs.getString("namaMerek"));
        } catch (SQLException ignore) {
        }
        return m;
    }

    public List<Mobil> getAll() throws SQLException {
        String sql = "SELECT m.*, mr.namaMerek FROM mobil m LEFT JOIN merek mr ON m.idMerek=mr.idMerek ORDER BY m.idMobil DESC";
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            List<Mobil> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public List<Mobil> getAvailable() throws SQLException {
        String sql = "SELECT m.*, mr.namaMerek FROM mobil m LEFT JOIN merek mr ON m.idMerek=mr.idMerek WHERE m.status='TERSEDIA' ORDER BY mr.namaMerek, m.namaMobil";
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            List<Mobil> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public Mobil findById(int id) throws SQLException {
        String sql = "SELECT m.*, mr.namaMerek FROM mobil m LEFT JOIN merek mr ON m.idMerek=mr.idMerek WHERE m.idMobil=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public Mobil findByPlat(String plat) throws SQLException {
        String sql = "SELECT m.*, mr.namaMerek FROM mobil m LEFT JOIN merek mr ON m.idMerek=mr.idMerek WHERE m.platNomor=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, plat);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public int insert(Mobil m) throws SQLException {
        String sql = "INSERT INTO mobil(idMerek,namaMobil,tipeMobil,platNomor,tahun,hargaSewaHarian,gambarPath,status) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getIdMerek());
            ps.setString(2, m.getNamaMobil());
            ps.setString(3, m.getTipeMobil());
            ps.setString(4, m.getPlatNomor());
            ps.setInt(5, m.getTahun());
            ps.setInt(6, m.getHargaSewaHarian());
            ps.setString(7, m.getGambarPath());
            ps.setString(8, m.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void update(Mobil m) throws SQLException {
        String sql = "UPDATE mobil SET idMerek=?, namaMobil=?, tipeMobil=?, platNomor=?, tahun=?, hargaSewaHarian=?, gambarPath=?, status=? WHERE idMobil=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, m.getIdMerek());
            ps.setString(2, m.getNamaMobil());
            ps.setString(3, m.getTipeMobil());
            ps.setString(4, m.getPlatNomor());
            ps.setInt(5, m.getTahun());
            ps.setInt(6, m.getHargaSewaHarian());
            ps.setString(7, m.getGambarPath());
            ps.setString(8, m.getStatus());
            ps.setInt(9, m.getIdMobil());
            ps.executeUpdate();
        }
    }

    public void delete(int idMobil) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM mobil WHERE idMobil=?")) {
            ps.setInt(1, idMobil);
            ps.executeUpdate();
        }
    }

    public void setStatus(int idMobil, String status) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE mobil SET status=? WHERE idMobil=?")) {
            ps.setString(1, status);
            ps.setInt(2, idMobil);
            ps.executeUpdate();
        }
    }

    public int countRented() throws SQLException {
        String sql = "SELECT COUNT(*) FROM mobil WHERE status='DISEWA'";
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
