package rentalmobil.dao;

import rentalmobil.config.DB;
import rentalmobil.model.Konsumen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KonsumenDao {
    private Konsumen map(ResultSet rs) throws SQLException {
        return new Konsumen(
            rs.getString("noKTP"),
            rs.getString("namaKonsumen"),
            rs.getString("tempatTinggal"),
            rs.getString("nomorTelepon")
        );
    }

    public Konsumen find(String noKTP) throws SQLException {
        String sql = "SELECT * FROM konsumen WHERE noKTP=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, noKTP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Konsumen> getAll() throws SQLException {
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM konsumen ORDER BY namaKonsumen")) {
            List<Konsumen> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    public void upsert(Konsumen k) throws SQLException {
        String sql = "INSERT INTO konsumen(noKTP,namaKonsumen,tempatTinggal,nomorTelepon) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE namaKonsumen=VALUES(namaKonsumen), tempatTinggal=VALUES(tempatTinggal), nomorTelepon=VALUES(nomorTelepon)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNoKTP());
            ps.setString(2, k.getNamaKonsumen());
            ps.setString(3, k.getTempatTinggal());
            ps.setString(4, k.getNomorTelepon());
            ps.executeUpdate();
        }
    }

    public void delete(String noKTP) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM konsumen WHERE noKTP=?")) {
            ps.setString(1, noKTP);
            ps.executeUpdate();
        }
    }
}

