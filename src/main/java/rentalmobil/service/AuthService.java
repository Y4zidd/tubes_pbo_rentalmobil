package rentalmobil.service;

import rentalmobil.dao.UserDao;
import rentalmobil.model.Admin;
import rentalmobil.model.Petugas;
import rentalmobil.model.User;

public class AuthService {
    private final UserDao userDao = new UserDao();

    public User login(String username, String password) throws Exception {
        User u = userDao.findByUsername(username);
        if (u == null) return null;
        if (!u.getPassword().equals(password)) return null;
        if ("ADMIN".equalsIgnoreCase(u.getLevel())) {
            return new Admin(u.getIdUser(), u.getUsername(), u.getPassword(), u.getNamaLengkap());
        }
        if ("PETUGAS".equalsIgnoreCase(u.getLevel())) {
            return new Petugas(u.getIdUser(), u.getUsername(), u.getPassword(), u.getNamaLengkap());
        }
        return u;
    }
}
