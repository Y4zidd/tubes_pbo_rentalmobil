package rentalmobil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rentalmobil.config.DB;

public class PetugasDao {

    public List<Object[]> getAll() throws SQLException {
        String sql = "SELECT u.idUser, u.username, u.namaLengkap, u.password "
                + "FROM user u JOIN petugas p ON u.idUser = p.idUser "
                + "WHERE u.level = 'PETUGAS' ORDER BY u.idUser";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<Object[]> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("idUser"),
                    rs.getString("username"),
                    rs.getString("namaLengkap"),
                    rs.getString("password")
                });
            }
            return list;
        }
    }

    public int insert(String username, String password, String namaLengkap) throws SQLException {
        String sqlUser = "INSERT INTO user(username,password,namaLengkap,level) VALUES(?,?,?,'PETUGAS')";
        try (Connection c = DB.getConnection(); PreparedStatement psUser = c.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
            c.setAutoCommit(false);
            try {
                psUser.setString(1, username);
                psUser.setString(2, password);
                psUser.setString(3, namaLengkap);
                psUser.executeUpdate();
                int idUser;
                try (ResultSet rs = psUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        idUser = rs.getInt(1);
                    } else {
                        throw new SQLException("Gagal mendapatkan ID user baru");
                    }
                }
                String sqlPetugas = "INSERT INTO petugas(idUser) VALUES(?)";
                try (PreparedStatement psPetugas = c.prepareStatement(sqlPetugas)) {
                    psPetugas.setInt(1, idUser);
                    psPetugas.executeUpdate();
                }
                c.commit();
                return idUser;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void update(int idUser, String username, String password, String namaLengkap) throws SQLException {
        String sqlUser = "UPDATE user SET username=?, password=?, namaLengkap=? WHERE idUser=?";
        try (Connection c = DB.getConnection(); PreparedStatement psUser = c.prepareStatement(sqlUser)) {
            c.setAutoCommit(false);
            try {
                psUser.setString(1, username);
                psUser.setString(2, password);
                psUser.setString(3, namaLengkap);
                psUser.setInt(4, idUser);
                psUser.executeUpdate();

                c.commit();
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void delete(int idUser) throws SQLException {
        String sql = "DELETE FROM user WHERE idUser=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ps.executeUpdate();
        }
    }
}
