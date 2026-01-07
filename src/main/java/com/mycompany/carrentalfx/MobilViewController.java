package com.mycompany.carrentalfx;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import rentalmobil.dao.MerekDao;
import rentalmobil.dao.MobilDao;
import rentalmobil.model.Merek;
import rentalmobil.model.Mobil;

public class MobilViewController {

    @FXML
    private TableView<Mobil> table;

    @FXML
    private TableColumn<Mobil, Number> colNo;

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
    private ComboBox<Merek> merekCombo;

    @FXML
    private TextField namaField;

    @FXML
    private TextField tipeField;

    @FXML
    private TextField platField;

    @FXML
    private Spinner<Integer> tahunSpinner;

    @FXML
    private Spinner<Integer> hargaSpinner;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private ImageView imagePreview;

    @FXML
    private Label imageLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button chooseImageButton;

    private final MobilDao mobilDao = new MobilDao();
    private final MerekDao merekDao = new MerekDao();
    private final ObservableList<Mobil> data = FXCollections.observableArrayList();
    private Path currentImagePath;
    private boolean hasNewImage;

    @FXML
    private void initialize() {
        configureSpinners();
        configureStatusCombo();
        configureTable();
        configureButtons();
        hasNewImage = false;
        if (chooseImageButton != null) {
            chooseImageButton.setText("Pilih Gambar");
        }
        loadMerek();
        loadData();
    }

    private final NumberFormat rupiahFormat = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    private void configureSpinners() {
        int currentYear = Year.now().getValue();
        tahunSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1990, currentYear + 1, currentYear));
        SpinnerValueFactory.IntegerSpinnerValueFactory hargaFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000000, 0, 50000);
        hargaFactory.setConverter(new javafx.util.StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                if (value == null) {
                    return "";
                }
                return rupiahFormat.format(value);
            }

            @Override
            public Integer fromString(String text) {
                if (text == null || text.isBlank()) {
                    return 0;
                }
                String cleaned = text.replace(".", "").replace(",", "");
                try {
                    return Integer.parseInt(cleaned);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });
        hargaSpinner.setValueFactory(hargaFactory);
    }

    private void configureStatusCombo() {
        statusCombo.setItems(FXCollections.observableArrayList("TERSEDIA", "DISEWA"));
        statusCombo.getSelectionModel().selectFirst();
    }

    private void configureTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colNo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(c.getValue()) + 1));
        colMerek.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMerekName()));
        colNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNamaMobil()));
        colTipe.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipeMobil()));
        colPlat.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlatNomor()));
        colTahun.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTahun()));
        colHarga.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getHargaSewaHarian()));
        colHarga.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(rupiahFormat.format(item.intValue()));
                }
            }
        });
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        table.setItems(data);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> populateForm(val));
    }

    private void configureButtons() {
        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        refreshButton.setOnAction(e -> loadData());
        chooseImageButton.setOnAction(e -> handleImageButton());
    }

    private void loadMerek() {
        try {
            List<Merek> list = merekDao.getAll();
            merekCombo.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError("Gagal memuat data merek: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            data.setAll(mobilDao.getAll());
        } catch (SQLException e) {
            showError("Gagal memuat data mobil: " + e.getMessage());
        }
    }

    private java.io.File resolveImageFile(String path) {
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

    private void populateForm(Mobil m) {
        if (m == null) {
            clearForm();
            return;
        }
        for (Merek mk : merekCombo.getItems()) {
            if (mk.getIdMerek() == m.getIdMerek()) {
                merekCombo.getSelectionModel().select(mk);
                break;
            }
        }
        namaField.setText(m.getNamaMobil());
        tipeField.setText(m.getTipeMobil());
        platField.setText(m.getPlatNomor());
        tahunSpinner.getValueFactory().setValue(m.getTahun());
        hargaSpinner.getValueFactory().setValue(m.getHargaSewaHarian());
        statusCombo.getSelectionModel().select(m.getStatus());
        hasNewImage = false;
        if (chooseImageButton != null) {
            chooseImageButton.setText("Pilih Gambar");
        }
        String path = m.getGambarPath();
        if (path != null && !path.isBlank()) {
            File file = resolveImageFile(path);
            if (file != null && file.exists()) {
                currentImagePath = file.toPath();
                imagePreview.setImage(new Image(file.toURI().toString()));
                imageLabel.setText(file.getName());
            } else {
                currentImagePath = null;
                imagePreview.setImage(null);
                imageLabel.setText("File gambar tidak ditemukan");
            }
        } else {
            clearImage();
        }
    }

    private void clearForm() {
        table.getSelectionModel().clearSelection();
        merekCombo.getSelectionModel().clearSelection();
        namaField.clear();
        tipeField.clear();
        platField.clear();
        tahunSpinner.getValueFactory().setValue(Year.now().getValue());
        hargaSpinner.getValueFactory().setValue(0);
        statusCombo.getSelectionModel().selectFirst();
        clearImage();
    }

    private Mobil buildFromForm(Mobil existing) {
        Merek merek = merekCombo.getSelectionModel().getSelectedItem();
        String nama = namaField.getText();
        String tipe = tipeField.getText();
        String plat = platField.getText();
        Integer tahun = tahunSpinner.getValue();
        Integer harga = hargaSpinner.getValue();
        String status = statusCombo.getSelectionModel().getSelectedItem();
        if (merek == null || nama == null || nama.isBlank() || plat == null || plat.isBlank()) {
            showError("Merek, nama mobil, dan plat nomor wajib diisi.");
            return null;
        }
        Mobil m = existing != null ? existing : new Mobil();
        m.setIdMerek(merek.getIdMerek());
        m.setNamaMobil(nama);
        m.setTipeMobil(tipe);
        m.setPlatNomor(plat);
        m.setTahun(tahun != null ? tahun : 0);
        m.setHargaSewaHarian(harga != null ? harga : 0);
        m.setStatus(status != null ? status : "TERSEDIA");
        if (currentImagePath != null) {
            m.setGambarPath(currentImagePath.toString());
        }
        return m;
    }

    private void handleAdd() {
        Mobil m = buildFromForm(null);
        if (m == null) {
            return;
        }
        try {
            int id = mobilDao.insert(m);
            m.setIdMobil(id);
            loadData();
            clearForm();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || (e.getMessage() != null && e.getMessage().contains("Duplicate entry"))) {
                showError("Plat nomor sudah terdaftar, gunakan plat nomor lain.");
            } else {
                showError("Gagal menambah mobil: " + e.getMessage());
            }
        }
    }

    private void handleEdit() {
        Mobil selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pilih data mobil yang akan diubah.");
            return;
        }
        Mobil updated = buildFromForm(selected);
        if (updated == null) {
            return;
        }
        try {
            mobilDao.update(updated);
            loadData();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || (e.getMessage() != null && e.getMessage().contains("Duplicate entry"))) {
                showError("Plat nomor sudah terdaftar, gunakan plat nomor lain.");
            } else {
                showError("Gagal mengubah mobil: " + e.getMessage());
            }
        }
    }

    private void handleDelete() {
        Mobil selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pilih data mobil yang akan dihapus.");
            return;
        }
        try {
            mobilDao.delete(selected.getIdMobil());
            loadData();
            clearForm();
        } catch (SQLException e) {
            showError("Gagal menghapus mobil: " + e.getMessage());
        }
    }

    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar Mobil");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(table.getScene().getWindow());
        if (file != null) {
            currentImagePath = file.toPath();
            imagePreview.setImage(new Image(file.toURI().toString()));
            imageLabel.setText(file.getName());
            hasNewImage = true;
            if (chooseImageButton != null) {
                chooseImageButton.setText("Simpan Gambar");
            }
        }
    }

    private void handleImageButton() {
        if (!hasNewImage) {
            chooseImage();
        } else {
            handleSaveImage();
        }
    }

    private void handleSaveImage() {
        Mobil selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pilih data mobil yang akan diubah gambarnya.");
            return;
        }
        if (!hasNewImage || currentImagePath == null) {
            showError("Pilih gambar terlebih dahulu.");
            return;
        }
        selected.setGambarPath(currentImagePath.toString());
        try {
            mobilDao.update(selected);
            loadData();
            hasNewImage = false;
            if (chooseImageButton != null) {
                chooseImageButton.setText("Pilih Gambar");
            }
        } catch (SQLException e) {
            showError("Gagal menyimpan gambar mobil: " + e.getMessage());
        }
    }

    private void clearImage() {
        currentImagePath = null;
        imagePreview.setImage(null);
        imageLabel.setText("Tidak ada gambar");
        hasNewImage = false;
        if (chooseImageButton != null) {
            chooseImageButton.setText("Pilih Gambar");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
