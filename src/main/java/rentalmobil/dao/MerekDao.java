package rentalmobil.dao;

import rentalmobil.config.DB;
import rentalmobil.model.Merek;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MerekDao {

    private Merek map(ResultSet rs) throws SQLException {
        return new Merek(rs.getInt("idMerek"), rs.getString("namaMerek"));
    }

    public List<Merek> getAll() throws SQLException {
        String sql = "SELECT * FROM merek ORDER BY idMerek ASC";
        try (Connection c = DB.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            List<Merek> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public Merek findById(int id) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM merek WHERE idMerek=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public int insert(String nama) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO merek(namaMerek) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nama);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void update(int id, String nama) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE merek SET namaMerek=? WHERE idMerek=?")) {
            ps.setString(1, nama);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM merek WHERE idMerek=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

