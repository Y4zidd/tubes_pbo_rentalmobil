package com.mycompany.carrentalfx;

import java.sql.SQLException;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import rentalmobil.dao.KonsumenDao;
import rentalmobil.model.Konsumen;

public class KonsumenViewController {

    @FXML
    private TableView<Konsumen> table;

    @FXML
    private TableColumn<Konsumen, Number> colNo;

    @FXML
    private TableColumn<Konsumen, String> colNoKtp;

    @FXML
    private TableColumn<Konsumen, String> colNama;

    @FXML
    private TableColumn<Konsumen, String> colAlamat;

    @FXML
    private TableColumn<Konsumen, String> colTelepon;

    @FXML
    private TextField noKtpField;

    @FXML
    private TextField namaField;

    @FXML
    private TextField alamatField;

    @FXML
    private TextField telpField;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    private final KonsumenDao konsumenDao = new KonsumenDao();
    private final ObservableList<Konsumen> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureTable();
        configureButtons();
        loadData();
    }

    private void configureTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colNo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(c.getValue()) + 1));
        colNoKtp.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNoKTP()));
        colNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNamaKonsumen()));
        colAlamat.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTempatTinggal()));
        colTelepon.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNomorTelepon()));
        table.setItems(data);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> populateForm(val));
    }

    private void configureButtons() {
        addButton.setOnAction(e -> handleUpsert(false));
        editButton.setOnAction(e -> handleUpsert(true));
        deleteButton.setOnAction(e -> handleDelete());
        refreshButton.setOnAction(e -> loadData());
    }

    private void loadData() {
        try {
            List<Konsumen> list = konsumenDao.getAll();
            data.setAll(list);
        } catch (SQLException e) {
            showError("Gagal memuat data konsumen: " + e.getMessage());
        }
    }

    private void populateForm(Konsumen k) {
        if (k == null) {
            clearForm();
            return;
        }
        noKtpField.setText(k.getNoKTP());
        namaField.setText(k.getNamaKonsumen());
        alamatField.setText(k.getTempatTinggal());
        telpField.setText(k.getNomorTelepon());
    }

    private void clearForm() {
        noKtpField.clear();
        namaField.clear();
        alamatField.clear();
        telpField.clear();
    }

    private Konsumen buildFromForm() {
        String ktp = noKtpField.getText();
        String nama = namaField.getText();
        String alamat = alamatField.getText();
        String telp = telpField.getText();
        if (ktp == null || ktp.isBlank() || nama == null || nama.isBlank()) {
            showError("No KTP dan nama konsumen wajib diisi.");
            return null;
        }
        return new Konsumen(ktp, nama, alamat, telp);
    }

    private void handleUpsert(boolean editing) {
        if (editing && table.getSelectionModel().getSelectedItem() == null) {
            showError("Pilih data konsumen yang akan diubah.");
            return;
        }
        Konsumen k = buildFromForm();
        if (k == null) {
            return;
        }
        try {
            konsumenDao.upsert(k);
            loadData();
            clearForm();
        } catch (SQLException e) {
            showError("Gagal menyimpan data konsumen: " + e.getMessage());
        }
    }

    private void handleDelete() {
        Konsumen selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Pilih data konsumen yang akan dihapus.");
            return;
        }
        try {
            konsumenDao.delete(selected.getNoKTP());
            loadData();
            clearForm();
        } catch (SQLException e) {
            showError("Gagal menghapus data konsumen: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
