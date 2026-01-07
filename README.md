# Ringkasan Lengkap Konsep OOP pada Project CarRentalFX

Dokumen ini menjelaskan penerapan konsep Object-Oriented Programming (OOP) dalam proyek ini secara komprehensif.

> **Catatan:** Hampir seluruh kode dalam proyek ini berbasis OOP karena Java adalah bahasa OOP. Namun, proyek ini lebih mengutamakan **Encapsulation** dan **Composition** dibandingkan Inheritance yang dalam.

## 1. Encapsulation (Enkapsulasi) - *Sangat Dominan*
Enkapsulasi adalah pembungkusan data dan metode dalam satu unit (class) serta menyembunyikan detail implementasi. Ini adalah konsep yang paling banyak digunakan di proyek ini.

*   **Penerapan:** Semua class Model (`User`, `Mobil`, `Konsumen`, `Transaksi`, `Petugas`) menggunakan variabel `private` dan method `public` (getter/setter).
*   **Tujuan:** Melindungi data agar tidak diubah sembarangan dan mempermudah pemeliharaan.
*   **Contoh 1 (User):** `src/main/java/rentalmobil/model/User.java`

```java
public class User {
    private String username; // Data private

    public String getUsername() { // Akses public
        return username;
    }
}
```

*   **Contoh 2 (Konsumen):** `src/main/java/rentalmobil/model/Konsumen.java`

```java
public class Konsumen {
    private String noKTP;
    private String namaKonsumen;

    public String getNoKTP() {
        return noKTP;
    }

    public void setNoKTP(String noKTP) {
        this.noKTP = noKTP;
    }
}
```

## 2. Inheritance (Pewarisan) - *Digunakan Sesuai Kebutuhan*
Pewarisan memungkinkan sebuah class mewarisi sifat dari class lain. Dalam proyek ini, pewarisan digunakan terutama untuk integrasi dengan framework JavaFX.

*   **Penerapan Utama:** Class `App` mewarisi `Application` dari JavaFX.
*   **File:** `src/main/java/com/mycompany/carrentalfx/App.java`
*   **Catatan:** Proyek ini menghindari hierarki pewarisan yang dalam (deep inheritance) untuk menjaga kode tetap sederhana.

```java
public class App extends Application { ... }
```

## 3. Polymorphism (Polimorfisme)
Kemampuan objek atau method untuk memiliki banyak bentuk.

### a. Method Overriding (Menimpa Method)
Mengubah perilaku method yang diwarisi dari parent class.
*   **Contoh:** `App.java` menimpa method `start()` dari class `Application`.
*   **Contoh:** `Merek.java` menimpa method `toString()` dari class `Object` untuk menampilkan nama merek saat objek ini dimasukkan ke ComboBox.
    *   **File:** `src/main/java/rentalmobil/model/Merek.java`

```java
@Override
public String toString() {
    return namaMerek;
}
```

### b. Method Overloading (Overloading Method)
Membuat beberapa method dengan nama sama tapi parameter berbeda.
*   **Penerapan:** Constructor pada class Model.
*   **Contoh (User, beberapa constructor):** `src/main/java/rentalmobil/model/User.java`

```java
public class User {
    public User() {
    }

    public User(int idUser, String username, String password,
                String namaLengkap, String level) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.level = level;
    }
}
```

## 4. Abstraction (Abstraksi)
Menyembunyikan kerumitan implementasi dan menampilkan fungsionalitas sederhana.

*   **Layer DAO (Data Access Object):** `MobilDao`, `UserDao`, `MerekDao` dll. menyembunyikan kompleksitas query SQL. Controller tidak perlu tahu sintaks SQL.
*   **Contoh DAO (MobilDao.getAll):** `src/main/java/rentalmobil/dao/MobilDao.java`

```java
public List<Mobil> getAll() throws SQLException {
    String sql = "SELECT m.*, mr.namaMerek FROM mobil m "
               + "LEFT JOIN merek mr ON m.idMerek=mr.idMerek "
               + "ORDER BY m.idMobil DESC";
    try (Connection c = DB.getConnection();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        List<Mobil> list = new ArrayList<>();
        while (rs.next()) {
            list.add(map(rs));
        }
        return list;
    }
}
```

*   **Layer Service:** `RentalService` menyembunyikan logika bisnis yang rumit (validasi, hitung denda) dari Controller.
*   **Contoh Service (sewaMobil):** `src/main/java/rentalmobil/service/RentalService.java`

```java
public int sewaMobil(String noKTP, String nama, String alamat, String telp,
                     int idMobil, int idUserPetugas,
                     LocalDate tglSewa, LocalDate tglRencana)
        throws SQLException {
    konsumenDao.upsert(new Konsumen(noKTP, nama, alamat, telp));
    Mobil m = mobilDao.findById(idMobil);
    // validasi dan perhitungan total bayar
    int id = transaksiDao.insert(t);
    mobilDao.setStatus(idMobil, "DISEWA");
    return id;
}
```

## 5. Composition (Komposisi) - *Pola Struktur Utama*
Alih-alih menggunakan pewarisan (Inheritance), proyek ini menggunakan Komposisi ("Has-A Relationship"). Ini adalah praktik OOP modern yang disarankan.

*   **Penerapan:** Controller *memiliki* (has-a) instance dari DAO atau Service.
*   **Contoh 1 (DashboardController):** `src/main/java/com/mycompany/carrentalfx/DashboardController.java`

```java
public class DashboardController {
    private final MobilDao mobilDao = new MobilDao();
    private final KonsumenDao konsumenDao = new KonsumenDao();
    private final TransaksiDao transaksiDao = new TransaksiDao();
}
```

*   **Contoh 2 (RentalService):** `src/main/java/rentalmobil/service/RentalService.java`

```java
public class RentalService {
    private final MobilDao mobilDao = new MobilDao();
    private final KonsumenDao konsumenDao = new KonsumenDao();
    private final TransaksiDao transaksiDao = new TransaksiDao();
}
```

## 6. Static & Nested Classes (Kelas Statis & Bersarang)
Penggunaan fitur OOP lanjutan untuk pengorganisasian kode.

*   **Static Method (Utility Pattern):**
    *   `DB.java`: Menggunakan method `static` agar bisa dipanggil tanpa objek `new DB()`.
    *   `PrintUtil.java`: Class ini berisi method `static void printText(...)` untuk mencetak struk, tanpa perlu menyimpan state/data.
*   **Nested Class (Inner Class):** `TransaksiDao` memiliki class `MonthlyIncome` di dalamnya (`public static class`).

## 7. Peta Seluruh File & Peran OOP-nya
Berikut adalah daftar lengkap file dalam proyek dan peran OOP masing-masing, sehingga Anda bisa melihat bahwa **semua kode** memiliki tempatnya dalam konsep OOP.

### A. Model (Representasi Objek Nyata)
Semua file ini menggunakan **Encapsulation**.
*   `User.java`: Objek Pengguna sistem.
*   `Konsumen.java`: Objek Penyewa mobil.
*   `Mobil.java`: Objek Mobil yang disewakan.
*   `Merek.java`: Objek Merek mobil (menggunakan **Polymorphism** pada `toString`).
*   `Transaksi.java`: Objek Transaksi penyewaan.
*   `Petugas.java`: Objek khusus Petugas (sederhana).

### B. DAO (Data Access Object - Abstraksi Data)
Semua file ini menggunakan **Abstraction** untuk menyembunyikan SQL.
*   `UserDao.java`, `KonsumenDao.java`, `MobilDao.java`, `MerekDao.java`, `TransaksiDao.java`, `PetugasDao.java`.

### C. Service (Logika Bisnis - Abstraksi Proses)
Menggabungkan beberapa DAO untuk melakukan satu tugas bisnis yang kompleks.
*   `AuthService.java`: Menangani logika login.
*   `RentalService.java`: Menangani logika penyewaan dan pengembalian (menghitung denda, cek stok).

### D. Controller (Penghubung UI & Logika)
Menggunakan **Composition** untuk memanggil Service/DAO.
*   `App.java`: Entry point (**Inheritance** dari JavaFX).
*   `LoginController.java`: Mengontrol tampilan login.
*   `DashboardController.java`: Mengontrol menu utama.
*   `PrimaryController.java`, `SecondaryController.java`: Navigasi dasar.
*   `MobilViewController.java`, `KonsumenViewController.java`, `TransaksiViewController.java`, `LaporanViewController.java`, `PetugasViewController.java`: Mengelola tampilan CRUD masing-masing fitur.
*   `MobilPickerController.java`: Dialog pemilihan mobil.
*   `ProfileController.java`: Dialog edit profil.

### E. Utility & Config
*   `DB.java`: Konfigurasi Database (**Static** pattern).
*   `PrintUtil.java`: Alat bantu cetak (**Static** method pattern).

## Kesimpulan
Tidak ada kode yang "terbuang" atau "bukan OOP".
1.  **Class Model** menyimpan data (State).
2.  **Class Controller** menangani interaksi (Behavior).
3.  **Class DAO/Service** menangani logika dan data persistance.
Semuanya bekerja sama sebagai objek-objek yang saling berinteraksi.
