package rentalmobil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import rentalmobil.config.DB;
import rentalmobil.model.User;

public class UserDao {

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setIdUser(rs.getInt("idUser"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setNamaLengkap(rs.getString("namaLengkap"));
                    u.setLevel(rs.getString("level"));
                    try {
                        u.setPhotoPath(rs.getString("photoPath"));
                    } catch (SQLException ignore) {
                    }
                    return u;
                }
            }
        }
        return null;
    }

    public void updateNama(int idUser, String namaLengkap) throws SQLException {
        String sql = "UPDATE user SET namaLengkap=? WHERE idUser=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, namaLengkap);
            ps.setInt(2, idUser);
            ps.executeUpdate();
        }
    }

    public void updatePhoto(int idUser, String photoPath) throws SQLException {
        String sql = "UPDATE user SET photoPath=? WHERE idUser=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, photoPath);
            ps.setInt(2, idUser);
            ps.executeUpdate();
        }
    }
}
