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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import rentalmobil.dao.PetugasDao;

public class PetugasViewController {

    @FXML
    private TableView<Object[]> table;

    @FXML
    private TableColumn<Object[], Number> colNo;

    @FXML
    private TableColumn<Object[], String> colUsername;

    @FXML
    private TableColumn<Object[], String> colNama;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField namaField;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    private final PetugasDao petugasDao = new PetugasDao();
    private final ObservableList<Object[]> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureTable();
        configureButtons();
        loadData();
    }

    private void configureTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colNo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(c.getValue()) + 1));
        colUsername.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[1]));
        colNama.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[2]));
        table.setItems(data);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> populateForm(val));
    }

    private void configureButtons() {
        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        refreshButton.setOnAction(e -> loadData());
    }

    private void loadData() {
        try {
            List<Object[]> list = petugasDao.getAll();
            data.setAll(list);
        } catch (SQLException e) {
            showError("Gagal memuat data petugas: " + e.getMessage());
        }
    }

    private void populateForm(Object[] row) {
        if (row == null) {
            clearForm();
            return;
        }
        usernameField.setText((String) row[1]);
        namaField.setText((String) row[2]);
        passwordField.clear();
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        namaField.clear();
    }

    private void handleAdd() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String nama = namaField.getText();
        if (username == null || username.isBlank() || password == null || password.isBlank() || nama == null || nama.isBlank()) {
            showError("Username, password, dan nama lengkap wajib diisi.");
            return;
        }
        try {
            petugasDao.insert(username, password, nama);
            loadData();
            clearForm();
        } catch (SQLException e) {
            showError("Gagal menambah petugas: " + e.getMessage());
        }
    }

    private void handleEdit() {
        Object[] row = table.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Pilih data petugas yang akan diubah.");
            return;
        }
        String username = usernameField.getText();
        String nama = namaField.getText();
        if (username == null || username.isBlank() || nama == null || nama.isBlank()) {
            showError("Username dan nama lengkap wajib diisi.");
            return;
        }
        String newPassword = passwordField.getText();
        String currentPassword = (String) row[3];
        String password = (newPassword == null || newPassword.isBlank()) ? currentPassword : newPassword;
        int idUser = (Integer) row[0];
        try {
            petugasDao.update(idUser, username, password, nama);
            loadData();
            clearForm();
        } catch (SQLException e) {
            showError("Gagal mengubah petugas: " + e.getMessage());
        }
    }

    private void handleDelete() {
        Object[] row = table.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Pilih data petugas yang akan dihapus.");
            return;
        }
        int idUser = (Integer) row[0];
        try {
            petugasDao.delete(idUser);
            loadData();
            clearForm();
        } catch (SQLException e) {
            showError("Gagal menghapus petugas: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
