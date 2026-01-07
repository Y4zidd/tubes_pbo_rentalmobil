package com.mycompany.carrentalfx;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rentalmobil.dao.MobilDao;
import rentalmobil.model.Mobil;

public class MobilPickerController {

    @FXML
    private TableView<Mobil> table;

    @FXML
    private TableColumn<Mobil, String> colMerek;

    @FXML
    private TableColumn<Mobil, String> colNama;

    @FXML
    private TableColumn<Mobil, String> colTipe;

    @FXML
    private TableColumn<Mobil, String> colPlat;

    @FXML
    private TableColumn<Mobil, Number> colTahun;

    @FXML
    private TableColumn<Mobil, Number> colHarga;

    @FXML
    private TableColumn<Mobil, String> colStatus;

    @FXML
    private ImageView imagePreview;

    @FXML
    private Label imageLabel;

    @FXML
    private Button pilihButton;

    @FXML
    private Button batalButton;

    private final MobilDao mobilDao = new MobilDao();
    private final ObservableList<Mobil> data = FXCollections.observableArrayList();
    private Stage dialogStage;
    private Mobil selectedMobil;

    @FXML
    private void initialize() {
        configureTable();
        configureSelection();
        configureButtons();
        loadData();
    }

    private void configureTable() {
        colMerek.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMerekName()));
        colNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNamaMobil()));
        colTipe.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipeMobil()));
        colPlat.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlatNomor()));
        colTahun.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTahun()));
        colHarga.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getHargaSewaHarian()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        table.setItems(data);
    }

    private void configureSelection() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> updatePreview(val));
    }

    private void configureButtons() {
        pilihButton.setOnAction(e -> handlePilih());
        batalButton.setOnAction(e -> handleBatal());
    }

    private void loadData() {
        try {
            List<Mobil> list = mobilDao.getAvailable();
            data.setAll(list);
        } catch (SQLException e) {
            showError("Gagal memuat data mobil: " + e.getMessage());
        }
    }

    private File resolveImageFile(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        File userDir = new File(System.getProperty("user.dir"));
        File fromUserDir = new File(userDir, path);
        if (fromUserDir.exists()) {
            return fromUserDir;
        }
        File parent = userDir.getParentFile();
        if (parent != null) {
            File fromRentalMobil = new File(new File(parent, "rentalMobil"), path);
            if (fromRentalMobil.exists()) {
                return fromRentalMobil;
            }
        }
        return null;
    }

    private void updatePreview(Mobil m) {
        if (m == null) {
            imagePreview.setImage(null);
            imageLabel.setText("Tidak ada gambar");
            return;
        }
        String path = m.getGambarPath();
        if (path != null && !path.isBlank()) {
            File file = resolveImageFile(path);
            if (file != null && file.exists()) {
                imagePreview.setImage(new Image(file.toURI().toString()));
                imageLabel.setText(file.getName());
            } else {
                imagePreview.setImage(null);
                imageLabel.setText("File gambar tidak ditemukan");
            }
        } else {
            imagePreview.setImage(null);
            imageLabel.setText("Tidak ada gambar");
        }
    }

    private void handlePilih() {
        Mobil m = table.getSelectionModel().getSelectedItem();
        if (m == null) {
            showError("Pilih mobil yang akan digunakan.");
            return;
        }
        selectedMobil = m;
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private void handleBatal() {
        selectedMobil = null;
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Mobil getSelectedMobil() {
        return selectedMobil;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
