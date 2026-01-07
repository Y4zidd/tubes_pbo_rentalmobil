# Ringkasan OOP CarRentalFX

Dokumen ini merangkum penerapan OOP pada aplikasi CarRentalFX dan menunjukkan bagian kode yang relevan.

## Isi
1. Peta Arsitektur
2. Model (POJO)
3. DAO (Data Access Object)
4. Service (Logika Bisnis)
5. Controller (UI/JavaFX)
6. Util & Config
7. Prinsip OOP
8. Alur Utama

## 1. Peta Arsitektur
- Model → DAO → Service → Controller → FXML (View)
- Pola: DAO Pattern, Service Layer, MVC-ish, Utility class, konfigurasi DB terpusat.

## 2. Model (POJO)
- [User](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/User.java) — data pengguna; konstruktor overload [User.java:L12-L26](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/User.java#L12-L26).
- [Mobil](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Mobil.java) — data mobil; override toString [Mobil.java:L111-L115](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Mobil.java#L111-L115).
- [Transaksi](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Transaksi.java) — data transaksi; util statis hitungLamaSewa [Transaksi.java:L115-L118](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Transaksi.java#L115-L118).
- [Konsumen](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Konsumen.java) — data konsumen.
- [Merek](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Merek.java) — entity merek; override toString [Merek.java:L32-L35](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Merek.java#L32-L35).

## 3. DAO (Data Access Object)
- [UserDao](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/UserDao.java) — cari user, update profil [UserDao.java:L12-L33](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/UserDao.java#L12-L33).
- [MobilDao](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/MobilDao.java) — CRUD mobil; mapping ResultSet→Mobil [MobilDao.java:L15-L34](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/MobilDao.java#L15-L34).
- [TransaksiDao](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/TransaksiDao.java) — insert, update status, laporan bulanan [TransaksiDao.java:L124-L134](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/TransaksiDao.java#L124-L134); inner class MonthlyIncome [TransaksiDao.java:L17-L40](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/TransaksiDao.java#L17-L40).
- [KonsumenDao](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/KonsumenDao.java) — upsert/query konsumen [KonsumenDao.java:L42-L51](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/KonsumenDao.java#L42-L51).
- [MerekDao](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/MerekDao.java) — CRUD merek [MerekDao.java:L20-L28](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/MerekDao.java#L20-L28).

## 4. Service (Logika Bisnis)
- [AuthService](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/AuthService.java) — login via UserDao [AuthService.java:L7-L14](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/AuthService.java#L7-L14).
- [RentalService](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/RentalService.java) — alur sewa/pengembalian, hitung total & denda [RentalService.java:L18-L35](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/RentalService.java#L18-L35), [RentalService.java:L37-L45](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/RentalService.java#L37-L45).

## 5. Controller (UI/JavaFX)
- [LoginController](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/LoginController.java) — event login panggil AuthService [LoginController.java:L35-L58](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/LoginController.java#L35-L58).
- [MobilViewController](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/MobilViewController.java) — CRUD mobil, binding tabel [MobilViewController.java:L133-L145](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/MobilViewController.java#L133-L145), aksi tombol [MobilViewController.java:L147-L153](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/MobilViewController.java#L147-L153).
- [TransaksiViewController](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/TransaksiViewController.java) — sewa & pengembalian via RentalService [TransaksiViewController.java:L248-L269](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/TransaksiViewController.java#L248-L269), [TransaksiViewController.java:L308-L333](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/TransaksiViewController.java#L308-L333).
- [DashboardController](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/DashboardController.java) — routing view, summary kartu, role-based UI [DashboardController.java:L81-L97](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/DashboardController.java#L81-L97).

## 6. Util & Config
- [DB](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/config/DB.java) — pabrik koneksi DB [DB.java:L12-L19](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/config/DB.java#L12-L19).
- [PrintUtil](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/util/PrintUtil.java) — util cetak teks [PrintUtil.java:L11-L22](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/util/PrintUtil.java#L11-L22).
- [App](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/App.java) — bootstrap JavaFX + currentUser [App.java:L40-L46](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/App.java#L40-L46).
- [module-info.java](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/module-info.java) — deklarasi modul untuk FXML.

## 7. Prinsip OOP
- Enkapsulasi: field private + getter/setter (mis. [User.java:L5-L11](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/User.java#L5-L11), [Mobil.java:L5-L15](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Mobil.java#L5-L15)); DAO menyembunyikan SQL (mis. [TransaksiDao.updateStatusSelesai](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/TransaksiDao.java#L82-L90)).
- Abstraksi: API bisnis sederhana di Service (mis. [RentalService.sewaMobil](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/RentalService.java#L18-L35)); util domain [Transaksi.hitungLamaSewa](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Transaksi.java#L115-L118); factory koneksi [DB.getConnection](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/config/DB.java#L12-L19).
- Polymorphism: overloading konstruktor di [User](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/User.java#L12-L26); overriding toString di [Mobil](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Mobil.java#L111-L115) dan [Merek](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/model/Merek.java#L32-L35).
- Komposisi: Service menggabungkan beberapa DAO [RentalService](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/RentalService.java#L14-L17).
- Inheritance: minim; desain lebih menekankan komposisi.

## 8. Alur Utama
- Login: Controller ambil input → Service otentikasi → DAO query → simpan currentUser.
  - [LoginController.doLogin](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/LoginController.java#L35-L58)
  - [AuthService.login](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/AuthService.java#L9-L14)
  - [UserDao.findByUsername](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/UserDao.java#L12-L33)
  - [App.setCurrentUser](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/App.java#L44-L46)
- Sewa mobil: Controller kumpulkan form → Service validasi & hitung → DAO insert transaksi dan ubah status.
  - [TransaksiViewController.handleSewa](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/TransaksiViewController.java#L248-L269)
  - [RentalService.sewaMobil](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/RentalService.java#L18-L35)
  - [TransaksiDao.insert](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/TransaksiDao.java#L60-L80), [MobilDao.setStatus](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/MobilDao.java#L128-L134)
- Pengembalian: Controller pilih transaksi + tanggal kembali → Service hitung denda → DAO update status; mobil tersedia lagi.
  - [TransaksiViewController.handlePengembalian](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/com/mycompany/carrentalfx/TransaksiViewController.java#L308-L333)
  - [RentalService.prosesPengembalian](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/service/RentalService.java#L37-L45)
  - [TransaksiDao.updateStatusSelesai](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/TransaksiDao.java#L82-L90), [MobilDao.setStatus](file:///c:/Users/Yazid/Documents/NetBeansProjects/CarRentalFX/src/main/java/rentalmobil/dao/MobilDao.java#L128-L134)
