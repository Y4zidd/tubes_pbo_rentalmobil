package com.mycompany.carrentalfx;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import atlantafx.base.controls.Tile;
import rentalmobil.dao.KonsumenDao;
import rentalmobil.dao.MobilDao;
import rentalmobil.dao.TransaksiDao;
import rentalmobil.model.User;

public class DashboardController {

    @FXML
    private Label userLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Button profileButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button mobilButton;

    @FXML
    private Button petugasButton;

    @FXML
    private Button konsumenButton;

    @FXML
    private Button transaksiButton;

    @FXML
    private Button laporanButton;

    @FXML
    private StackPane contentPane;

    @FXML
    private VBox homePane;

    @FXML
    private Tile mobilTile;

    @FXML
    private Tile konsumenTile;

    @FXML
    private Tile transaksiTile;

    @FXML
    private Tile pemasukanTile;

    private final MobilDao mobilDao = new MobilDao();
    private final KonsumenDao konsumenDao = new KonsumenDao();
    private final TransaksiDao transaksiDao = new TransaksiDao();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    private Node mobilView;
    private Node petugasView;
    private Node konsumenView;
    private Node transaksiView;
    private Node laporanView;

    @FXML
    private void initialize() {
        User current = App.getCurrentUser();
        if (current != null) {
            userLabel.setText(current.getNamaLengkap() + " (" + current.getLevel() + ")");
        }
        configureRole(current);
        updateSummaryCards();
        profileButton.setOnAction(e -> showProfileDialog());
        dashboardButton.setOnAction(e -> showHome());
        mobilButton.setOnAction(e -> showMobil());
        petugasButton.setOnAction(e -> showPetugas());
        konsumenButton.setOnAction(e -> showKonsumen());
        transaksiButton.setOnAction(e -> showTransaksi());
        laporanButton.setOnAction(e -> showLaporan());
        logoutButton.setOnAction(e -> doLogout());
        showHome();
    }

    private void updateSummaryCards() {
        try {
            int totalMobil = mobilDao.getAll().size();
            int totalMobilDisewa = mobilDao.countRented();
            int totalKonsumen = konsumenDao.getAll().size();
            int totalTransaksiSelesai = transaksiDao.getTotalIncome() >= 0
                    ? countCompletedTransactions()
                    : 0;
            int totalIncome = transaksiDao.getTotalIncome();
            if (mobilTile != null) {
                String desc = totalMobil + " mobil terdaftar";
                if (totalMobilDisewa > 0) {
                    desc += ", " + totalMobilDisewa + " sedang disewa";
                }
                mobilTile.setDescription(desc);
            }
            if (konsumenTile != null) {
                konsumenTile.setDescription(totalKonsumen + " konsumen terdaftar");
            }
            if (transaksiTile != null) {
                transaksiTile.setDescription(totalTransaksiSelesai + " transaksi selesai");
            }
            if (pemasukanTile != null) {
                pemasukanTile.setDescription("Total pemasukan: " + currencyFormat.format(totalIncome));
            }
        } catch (Exception e) {
            if (mobilTile != null) {
                mobilTile.setDescription("Gagal memuat data");
            }
        }
    }

    private int countCompletedTransactions() {
        try (java.sql.Connection c = rentalmobil.config.DB.getConnection();
             java.sql.Statement st = c.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM transaksi WHERE status='SELESAI'")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (java.sql.SQLException e) {
            return 0;
        }
        return 0;
    }

    private void configureRole(User current) {
        String level = current.getLevel();
        if (level == null) {
            return;
        }
        String upper = level.toUpperCase();
        if ("ADMIN".equals(upper)) {
            if (titleLabel != null) {
                titleLabel.setText("Dashboard Admin");
            }
            transaksiButton.setVisible(false);
            transaksiButton.setManaged(false);
        } else if ("PETUGAS".equals(upper)) {
            if (titleLabel != null) {
                titleLabel.setText("Dashboard Petugas");
            }
            mobilButton.setVisible(false);
            mobilButton.setManaged(false);
            petugasButton.setVisible(false);
            petugasButton.setManaged(false);
            laporanButton.setVisible(false);
            laporanButton.setManaged(false);
        } else {
            if (titleLabel != null) {
                titleLabel.setText("Dashboard");
            }
        }
    }

    private void showHome() {
        contentPane.getChildren().setAll(homePane);
        updateSummaryCards();
    }

    private void showMobil() {
        if (mobilView == null) {
            mobilView = loadView("mobil-view");
        }
        contentPane.getChildren().setAll(mobilView);
    }

    private void showPetugas() {
        if (petugasView == null) {
            petugasView = loadView("petugas-view");
        }
        contentPane.getChildren().setAll(petugasView);
    }

    private void showKonsumen() {
        if (konsumenView == null) {
            konsumenView = loadView("konsumen-view");
        }
        contentPane.getChildren().setAll(konsumenView);
    }

    private void showTransaksi() {
        if (transaksiView == null) {
            transaksiView = loadView("transaksi-view");
        }
        contentPane.getChildren().setAll(transaksiView);
    }

    private void showLaporan() {
        if (laporanView == null) {
            laporanView = loadView("laporan-view");
        }
        contentPane.getChildren().setAll(laporanView);
    }

    private void showPlaceholder(String title) {
        Label label = new Label(title + " belum diimplementasikan.");
        contentPane.getChildren().setAll(label);
    }

    private void showProfileDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("profile-dialog.fxml"));
            Node root = loader.load();
            Stage stage = new Stage();
            stage.initOwner(profileButton.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Profil Pengguna");
            stage.setScene(new Scene((javafx.scene.Parent) root));
            stage.setResizable(false);
            stage.showAndWait();
            User current = App.getCurrentUser();
            if (current != null) {
                userLabel.setText(current.getNamaLengkap() + " (" + current.getLevel() + ")");
            }
        } catch (Exception e) {
            Label label = new Label("Gagal membuka profil: " + e.getMessage());
            contentPane.getChildren().setAll(label);
        }
    }

    private Node loadView(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(name + ".fxml"));
            return loader.load();
        } catch (Exception e) {
            Label error = new Label("Gagal memuat " + name + ": " + e.getMessage());
            return error;
        }
    }

    private void doLogout() {
        try {
            App.setCurrentUser(null);
            App.setRoot("login");
        } catch (IOException e) {
            Label label = new Label("Gagal logout: " + e.getMessage());
            contentPane.getChildren().setAll(label);
        }
    }
}
