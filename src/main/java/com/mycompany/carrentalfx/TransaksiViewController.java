package com.mycompany.carrentalfx;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rentalmobil.dao.KonsumenDao;
import rentalmobil.dao.MobilDao;
import rentalmobil.dao.TransaksiDao;
import rentalmobil.model.Konsumen;
import rentalmobil.model.Mobil;
import rentalmobil.model.Transaksi;
import rentalmobil.service.RentalService;

public class TransaksiViewController {

    @FXML
    private TableView<Transaksi> table;

    @FXML
    private TableColumn<Transaksi, Number> colNo;

    @FXML
    private TableColumn<Transaksi, String> colNoKtp;

    @FXML
    private TableColumn<Transaksi, String> colMobil;

    @FXML
    private TableColumn<Transaksi, String> colTglSewa;

    @FXML
    private TableColumn<Transaksi, String> colTglRencana;

    @FXML
    private TableColumn<Transaksi, String> colTglKembali;

    @FXML
    private TableColumn<Transaksi, Number> colTotal;

    @FXML
    private TableColumn<Transaksi, Number> colDenda;

    @FXML
    private TableColumn<Transaksi, String> colStatus;

    @FXML
    private TextField noKtpField;

    @FXML
    private TextField namaField;

    @FXML
    private TextField alamatField;

    @FXML
    private TextField telpField;

    @FXML
    private ComboBox<String> noKtpCombo;

    @FXML
    private ComboBox<String> statusFilterCombo;

    @FXML
    private ComboBox<Mobil> mobilCombo;

    @FXML
    private TextField mobilSewaField;

    @FXML
    private Button pilihMobilButton;

    @FXML
    private DatePicker tglSewaPicker;

    @FXML
    private DatePicker tglRencanaPicker;

    @FXML
    private TextField idTransaksiField;

    @FXML
    private TextField mobilField;

    @FXML
    private TextField tglRencanaField;

    @FXML
    private DatePicker tglKembaliPicker;

    @FXML
    private TextField dendaField;

    @FXML
    private Button sewaButton;

    @FXML
    private Button pengembalianButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button cetakSewaButton;

    @FXML
    private Button cetakPengembalianButton;

    private final MobilDao mobilDao = new MobilDao();
    private final TransaksiDao transaksiDao = new TransaksiDao();
    private final KonsumenDao konsumenDao = new KonsumenDao();
    private final RentalService rentalService = new RentalService();
    private final ObservableList<Transaksi> data = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @FXML
    private void initialize() {
        configureTable();
        configureButtons();
        configureKtpField();
        configureDendaListener();
        configureStatusFilter();
        loadKonsumen();
        loadMobil();
        loadData();
    }

    private void configureTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colNo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(c.getValue()) + 1));
        colNoKtp.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNoKTP()));
        colMobil.setCellValueFactory(c -> {
            int idMobil = c.getValue().getIdMobil();
            try {
                Mobil m = mobilDao.findById(idMobil);
                if (m != null) {
                    return new SimpleStringProperty(m.getMerekName() + " " + m.getNamaMobil());
                }
            } catch (SQLException ignore) {
            }
            return new SimpleStringProperty(String.valueOf(idMobil));
        });
        colTglSewa.setCellValueFactory(c -> {
            LocalDate d = c.getValue().getTglSewa();
            return new SimpleStringProperty(d != null ? d.format(dateFormatter) : "");
        });
        colTglRencana.setCellValueFactory(c -> {
            LocalDate d = c.getValue().getTglKembaliRencana();
            return new SimpleStringProperty(d != null ? d.format(dateFormatter) : "");
        });
        if (colTglKembali != null) {
            colTglKembali.setCellValueFactory(c -> {
                Transaksi t = c.getValue();
                LocalDate d = t.getTglKembali();
                if (!"SELESAI".equalsIgnoreCase(t.getStatus())) {
                    return new SimpleStringProperty("");
                }
                return new SimpleStringProperty(d != null ? d.format(dateFormatter) : "");
            });
        }
        colTotal.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalBayar()));
        colDenda.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getDenda()));
        colTotal.setCellFactory(column -> new TableCell<Transaksi, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value.intValue()));
                }
            }
        });
        colDenda.setCellFactory(column -> new TableCell<Transaksi, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value.intValue()));
                }
            }
        });
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        table.setItems(data);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> populatePengembalian(val));
    }

    private void configureButtons() {
        sewaButton.setOnAction(e -> handleSewa());
        pengembalianButton.setOnAction(e -> handlePengembalian());
        refreshButton.setOnAction(e -> {
            loadData();
            loadMobil();
        });
        pilihMobilButton.setOnAction(e -> openMobilPicker());
        if (cetakSewaButton != null) {
            cetakSewaButton.setOnAction(e -> handleCetakSewa());
        }
        if (cetakPengembalianButton != null) {
            cetakPengembalianButton.setOnAction(e -> handleCetakPengembalian());
        }
    }

    private void loadMobil() {
        try {
            List<Mobil> list = mobilDao.getAvailable();
            mobilCombo.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError("Gagal memuat data mobil: " + e.getMessage());
        }
    }

    private void loadKonsumen() {
        try {
            List<Konsumen> list = konsumenDao.getAll();
            if (noKtpCombo != null) {
                List<String> ktpList = new java.util.ArrayList<>();
                for (Konsumen k : list) {
                    ktpList.add(k.getNoKTP());
                }
                noKtpCombo.setItems(FXCollections.observableArrayList(ktpList));
            }
        } catch (SQLException e) {
            showError("Gagal memuat data konsumen: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            List<Transaksi> list;
            String status = statusFilterCombo != null
                    ? statusFilterCombo.getSelectionModel().getSelectedItem()
                    : "DISEWA";
            if ("SELESAI".equals(status)) {
                list = transaksiDao.findCompleted();
            } else {
                list = transaksiDao.findActive();
            }
            data.setAll(list);
            if (colTglKembali != null) {
                boolean selesai = "SELESAI".equals(status);
                colTglKembali.setVisible(selesai);
            }
            boolean selesaiView = "SELESAI".equals(status);
            if (pengembalianButton != null) {
                pengembalianButton.setDisable(selesaiView);
            }
            if (tglKembaliPicker != null) {
                tglKembaliPicker.setDisable(selesaiView);
            }
        } catch (SQLException e) {
            showError("Gagal memuat data transaksi: " + e.getMessage());
        }
    }

    private void configureKtpField() {
        if (noKtpCombo != null) {
            noKtpCombo.setOnAction(e -> lookupKonsumen());
            noKtpCombo.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    lookupKonsumen();
                }
            });
        }
    }

    private void configureDendaListener() {
        tglKembaliPicker.valueProperty().addListener((obs, oldDate, newDate) -> updateDendaPreview(newDate));
    }

    private void configureStatusFilter() {
        if (statusFilterCombo != null) {
            statusFilterCombo.setItems(FXCollections.observableArrayList("DISEWA", "SELESAI"));
            statusFilterCombo.getSelectionModel().select("DISEWA");
            statusFilterCombo.setOnAction(e -> loadData());
        }
    }

    private void lookupKonsumen() {
        String ktp;
        if (noKtpCombo != null) {
            ktp = noKtpCombo.getEditor().getText();
        } else {
            ktp = noKtpField.getText();
        }
        if (ktp == null || ktp.isBlank()) {
            namaField.clear();
            alamatField.clear();
            telpField.clear();
            return;
        }
        try {
            Konsumen k = konsumenDao.find(ktp);
            if (k != null) {
                namaField.setText(k.getNamaKonsumen());
                alamatField.setText(k.getTempatTinggal());
                telpField.setText(k.getNomorTelepon());
            } else {
                namaField.clear();
                alamatField.clear();
                telpField.clear();
                showError("Data konsumen dengan No KTP tersebut tidak ditemukan. Silakan input di menu Konsumen.");
            }
        } catch (SQLException e) {
            showError("Gagal mengambil data konsumen: " + e.getMessage());
        }
    }

    private void populatePengembalian(Transaksi t) {
        if (t == null) {
            idTransaksiField.clear();
            mobilField.clear();
            tglRencanaField.clear();
            tglKembaliPicker.setValue(null);
            dendaField.clear();
            return;
        }
        idTransaksiField.setText(String.valueOf(t.getIdTransaksi()));
        try {
            Mobil m = mobilDao.findById(t.getIdMobil());
            if (m != null) {
                mobilField.setText(m.getMerekName() + " " + m.getNamaMobil());
            } else {
                mobilField.setText(String.valueOf(t.getIdMobil()));
            }
        } catch (SQLException e) {
            mobilField.setText(String.valueOf(t.getIdMobil()));
        }
        LocalDate rencana = t.getTglKembaliRencana();
        tglRencanaField.setText(rencana != null ? rencana.format(dateFormatter) : "");
        tglKembaliPicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.format(dateFormatter) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isBlank()) {
                    return null;
                }
                return LocalDate.parse(string, dateFormatter);
            }
        });
        tglKembaliPicker.setValue(LocalDate.now());
        updateDendaPreview(tglKembaliPicker.getValue());
    }

    private void updateDendaPreview(LocalDate tglKembali) {
        Transaksi t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            dendaField.clear();
            return;
        }
        LocalDate tglRencana = t.getTglKembaliRencana();
        int denda = 0;
        if (tglRencana != null && tglKembali != null && tglKembali.isAfter(tglRencana)) {
            long terlambat = java.time.temporal.ChronoUnit.DAYS.between(tglRencana, tglKembali);
            denda = (int) (terlambat * 50000);
        }
        if (denda > 0) {
            dendaField.setText(String.valueOf(denda));
        } else {
            dendaField.clear();
        }
    }

    private void handleSewa() {
        String ktp;
        if (noKtpCombo != null) {
            ktp = noKtpCombo.getEditor().getText();
        } else {
            ktp = noKtpField.getText();
        }
        String nama = namaField.getText();
        String alamat = alamatField.getText();
        String telp = telpField.getText();
        Mobil m = mobilCombo.getSelectionModel().getSelectedItem();
        LocalDate tglSewa = tglSewaPicker.getValue();
        LocalDate tglRencana = tglRencanaPicker.getValue();
        if (ktp == null || ktp.isBlank() || nama == null || nama.isBlank() || m == null || tglSewa == null || tglRencana == null) {
            showError("No KTP, nama, mobil, dan tanggal sewa/kembali wajib diisi.");
            return;
        }
        int idUser = App.getCurrentUser() != null ? App.getCurrentUser().getIdUser() : 0;
        try {
            int idTransaksiBaru = rentalService.sewaMobil(ktp, nama, alamat, telp, m.getIdMobil(), idUser, tglSewa, tglRencana);
            table.getSelectionModel().clearSelection();
            loadData();
            loadMobil();
            clearFormSewa();
            pilihTransaksiById(idTransaksiBaru);
        } catch (SQLException e) {
            showError("Gagal memproses sewa: " + e.getMessage());
        }
    }

    private void clearFormSewa() {
        if (noKtpCombo != null) {
            noKtpCombo.getSelectionModel().clearSelection();
            noKtpCombo.getEditor().clear();
        } else {
            noKtpField.clear();
        }
        namaField.clear();
        alamatField.clear();
        telpField.clear();
        mobilCombo.getSelectionModel().clearSelection();
        mobilSewaField.clear();
        tglSewaPicker.setValue(null);
        tglRencanaPicker.setValue(null);
    }

    private void openMobilPicker() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("mobil-picker-dialog.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initOwner(table.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Pilih Mobil");
            stage.setScene(new Scene(root));
            MobilPickerController controller = loader.getController();
            controller.setDialogStage(stage);
            stage.showAndWait();
            Mobil selected = controller.getSelectedMobil();
            if (selected != null) {
                mobilCombo.getItems().setAll(selected);
                mobilCombo.getSelectionModel().selectFirst();
                String nama = selected.getMerekName() != null
                        ? selected.getMerekName() + " " + selected.getNamaMobil()
                        : selected.getNamaMobil();
                mobilSewaField.setText(nama);
            }
        } catch (IOException e) {
            showError("Gagal membuka dialog pilih mobil: " + e.getMessage());
        }
    }

    private void handlePengembalian() {
        Transaksi t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            showError("Pilih transaksi yang akan diproses pengembaliannya.");
            return;
        }
        if ("SELESAI".equalsIgnoreCase(t.getStatus())) {
            showError("Transaksi ini sudah SELESAI dan tidak bisa diubah lagi.");
            return;
        }
        LocalDate tglKembali = tglKembaliPicker.getValue();
        if (tglKembali == null) {
            showError("Tanggal kembali wajib diisi.");
            return;
        }
        LocalDate tglRencana = t.getTglKembaliRencana();
        try {
            rentalService.prosesPengembalian(t.getIdTransaksi(), t.getIdMobil(), tglKembali, tglRencana);
            int denda = 0;
            if (tglRencana != null && tglKembali.isAfter(tglRencana)) {
                long terlambat = java.time.temporal.ChronoUnit.DAYS.between(tglRencana, tglKembali);
                denda = (int) (terlambat * 50000);
            }
            dendaField.setText(String.valueOf(denda));
            int id = t.getIdTransaksi();
            loadData();
            loadMobil();
            pilihTransaksiById(id);
        } catch (SQLException e) {
            showError("Gagal memproses pengembalian: " + e.getMessage());
        }
    }

    private void pilihTransaksiById(int id) {
        if (id <= 0) {
            return;
        }
        for (Transaksi t : data) {
            if (t.getIdTransaksi() == id) {
                table.getSelectionModel().select(t);
                table.scrollTo(t);
                break;
            }
        }
    }

    private void handleCetakSewa() {
        Transaksi t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            showError("Pilih transaksi DISEWA yang akan dicetak struk sewanya.");
            return;
        }
        if (!"DISEWA".equalsIgnoreCase(t.getStatus())) {
            showError("Struk sewa hanya untuk transaksi dengan status DISEWA.");
            return;
        }
        String content = buildStrukSewa(t);
        javafx.application.Platform.runLater(() -> {
            rentalmobil.util.PrintUtil.printText(null, "Struk Sewa", content);
        });
    }

    private void handleCetakPengembalian() {
        Transaksi t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            showError("Pilih transaksi SELESAI yang akan dicetak struk pengembaliannya.");
            return;
        }
        if (!"SELESAI".equalsIgnoreCase(t.getStatus())) {
            showError("Struk pengembalian hanya untuk transaksi dengan status SELESAI.");
            return;
        }
        String content = buildStrukPengembalian(t);
        javafx.application.Platform.runLater(() -> {
            rentalmobil.util.PrintUtil.printText(null, "Struk Pengembalian", content);
        });
    }

    private String buildStrukSewa(Transaksi t) {
        StringBuilder sb = new StringBuilder();
        sb.append("STRUK SEWA MOBIL\n");
        sb.append("========================\n");
        sb.append("ID Transaksi : ").append(t.getIdTransaksi()).append("\n");
        sb.append("No KTP       : ").append(t.getNoKTP()).append("\n");
        try {
            Konsumen k = konsumenDao.find(t.getNoKTP());
            if (k != null) {
                sb.append("Nama         : ").append(k.getNamaKonsumen()).append("\n");
                sb.append("Alamat       : ").append(k.getTempatTinggal()).append("\n");
                sb.append("Telepon      : ").append(k.getNomorTelepon()).append("\n");
            }
        } catch (SQLException ignore) {
        }
        try {
            Mobil m = mobilDao.findById(t.getIdMobil());
            if (m != null) {
                sb.append("Mobil        : ");
                if (m.getMerekName() != null && !m.getMerekName().isEmpty()) {
                    sb.append(m.getMerekName()).append(" ");
                }
                sb.append(m.getNamaMobil()).append(" [").append(m.getPlatNomor()).append("]\n");
                sb.append("Harga/Hari   : ").append(currencyFormat.format(m.getHargaSewaHarian())).append("\n");
            }
        } catch (SQLException ignore) {
        }
        LocalDate sewa = t.getTglSewa();
        LocalDate rencana = t.getTglKembaliRencana();
        sb.append("Tgl Sewa     : ").append(sewa != null ? sewa.format(dateFormatter) : "-").append("\n");
        sb.append("Tgl Kembali  : ").append(rencana != null ? rencana.format(dateFormatter) : "-").append("\n");
        sb.append("Total Bayar  : ").append(currencyFormat.format(t.getTotalBayar())).append("\n");
        sb.append("========================\n");
        sb.append("Terima kasih telah menggunakan layanan kami.\n");
        return sb.toString();
    }

    private String buildStrukPengembalian(Transaksi t) {
        StringBuilder sb = new StringBuilder();
        sb.append("STRUK PENGEMBALIAN MOBIL\n");
        sb.append("========================\n");
        sb.append("ID Transaksi : ").append(t.getIdTransaksi()).append("\n");
        sb.append("No KTP       : ").append(t.getNoKTP()).append("\n");
        try {
            Konsumen k = konsumenDao.find(t.getNoKTP());
            if (k != null) {
                sb.append("Nama         : ").append(k.getNamaKonsumen()).append("\n");
            }
        } catch (SQLException ignore) {
        }
        try {
            Mobil m = mobilDao.findById(t.getIdMobil());
            if (m != null) {
                sb.append("Mobil        : ");
                if (m.getMerekName() != null && !m.getMerekName().isEmpty()) {
                    sb.append(m.getMerekName()).append(" ");
                }
                sb.append(m.getNamaMobil()).append(" [").append(m.getPlatNomor()).append("]\n");
            }
        } catch (SQLException ignore) {
        }
        LocalDate sewa = t.getTglSewa();
        LocalDate rencana = t.getTglKembaliRencana();
        LocalDate kembali = t.getTglKembali();
        sb.append("Tgl Sewa     : ").append(sewa != null ? sewa.format(dateFormatter) : "-").append("\n");
        sb.append("Tgl Rencana  : ").append(rencana != null ? rencana.format(dateFormatter) : "-").append("\n");
        sb.append("Tgl Kembali  : ").append(kembali != null ? kembali.format(dateFormatter) : "-").append("\n");
        sb.append("Total Bayar  : ").append(currencyFormat.format(t.getTotalBayar())).append("\n");
        sb.append("Denda        : ").append(currencyFormat.format(t.getDenda())).append("\n");
        sb.append("========================\n");
        sb.append("Terima kasih, transaksi telah SELESAI.\n");
        return sb.toString();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
