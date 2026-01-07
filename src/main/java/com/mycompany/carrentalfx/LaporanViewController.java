package com.mycompany.carrentalfx;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import rentalmobil.dao.TransaksiDao;

public class LaporanViewController {

    @FXML
    private TableView<TransaksiDao.MonthlyIncome> table;

    @FXML
    private TableColumn<TransaksiDao.MonthlyIncome, Number> colNo;

    @FXML
    private TableColumn<TransaksiDao.MonthlyIncome, String> colPeriode;

    @FXML
    private TableColumn<TransaksiDao.MonthlyIncome, Number> colTotal;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<Object[]> detailTable;

    @FXML
    private TableColumn<Object[], Number> detailColNo;

    @FXML
    private TableColumn<Object[], String> detailColMobil;

    @FXML
    private TableColumn<Object[], String> detailColPlat;

    @FXML
    private TableColumn<Object[], String> detailColNoKtp;

    @FXML
    private TableColumn<Object[], String> detailColTglSewa;

    @FXML
    private TableColumn<Object[], String> detailColTglKembali;

    @FXML
    private TableColumn<Object[], String> detailColStatus;

    @FXML
    private TableColumn<Object[], Number> detailColTotal;

    private final TransaksiDao transaksiDao = new TransaksiDao();
    private final ObservableList<TransaksiDao.MonthlyIncome> data = FXCollections.observableArrayList();
    private final ObservableList<Object[]> detailData = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    private void initialize() {
        configureTable();
        configureButtons();
        loadData();
    }

    private void configureTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colNo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(c.getValue()) + 1));
        colPeriode.setCellValueFactory(c -> {
            TransaksiDao.MonthlyIncome mi = c.getValue();
            String text = String.format("%02d-%04d", mi.getMonth(), mi.getYear());
            return new SimpleStringProperty(text);
        });
        colTotal.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotal()));
        colTotal.setCellFactory(column -> new TableCell<TransaksiDao.MonthlyIncome, Number>() {
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
        table.setItems(data);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> loadDetail(val));

        if (detailTable != null) {
            detailTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            detailColNo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(detailTable.getItems().indexOf(c.getValue()) + 1));
            detailColMobil.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[6]));
            detailColPlat.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[7]));
            detailColNoKtp.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[1]));
            detailColTglSewa.setCellValueFactory(c -> {
                LocalDate d = (LocalDate) c.getValue()[3];
                return new SimpleStringProperty(d != null ? d.format(dateFormatter) : "");
            });
            detailColTglKembali.setCellValueFactory(c -> {
                LocalDate d = (LocalDate) c.getValue()[4];
                return new SimpleStringProperty(d != null ? d.format(dateFormatter) : "");
            });
            detailColStatus.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[5]));
            detailColTotal.setCellValueFactory(c -> new SimpleIntegerProperty((Integer) c.getValue()[8]));
            detailColTotal.setCellFactory(column -> new TableCell<Object[], Number>() {
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
            detailTable.setItems(detailData);
        }
    }

    private void configureButtons() {
        refreshButton.setOnAction(e -> loadData());
    }

    private void loadData() {
        try {
            List<TransaksiDao.MonthlyIncome> list = transaksiDao.getMonthlyIncome();
            data.setAll(list);
            int total = transaksiDao.getTotalIncome();
            totalIncomeLabel.setText("Total pemasukan selesai: " + currencyFormat.format(total));
            if (!data.isEmpty()) {
                table.getSelectionModel().selectFirst();
            } else {
                detailData.clear();
            }
        } catch (SQLException e) {
            showError("Gagal memuat data laporan: " + e.getMessage());
        }
    }

    private void loadDetail(TransaksiDao.MonthlyIncome mi) {
        if (mi == null || detailTable == null) {
            detailData.clear();
            return;
        }
        try {
            List<Object[]> list = transaksiDao.getMonthlyDetails(mi.getYear(), mi.getMonth());
            detailData.setAll(list);
        } catch (SQLException e) {
            showError("Gagal memuat detail laporan: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
