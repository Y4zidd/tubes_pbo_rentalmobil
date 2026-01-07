package com.mycompany.carrentalfx;

import java.io.File;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import rentalmobil.dao.UserDao;
import rentalmobil.model.User;

public class ProfileController {

    @FXML
    private ImageView photoView;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField namaField;

    @FXML
    private Label levelLabel;

    @FXML
    private Button choosePhotoButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button closeButton;

    private final UserDao userDao = new UserDao();
    private User currentUser;
    private File selectedPhotoFile;

    @FXML
    private void initialize() {
        currentUser = App.getCurrentUser();
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            namaField.setText(currentUser.getNamaLengkap());
            levelLabel.setText(currentUser.getLevel());
            loadPhoto(currentUser.getPhotoPath());
        }
        choosePhotoButton.setOnAction(e -> handleChoosePhoto());
        saveButton.setOnAction(e -> handleSave());
        closeButton.setOnAction(e -> handleClose());
    }

    private void loadPhoto(String path) {
        if (path == null || path.isBlank()) {
            photoView.setImage(null);
            return;
        }
        File file = resolveImageFile(path);
        if (file != null && file.exists()) {
            photoView.setImage(new Image(file.toURI().toString()));
        } else {
            photoView.setImage(null);
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

    private void handleChoosePhoto() {
        Window window = choosePhotoButton.getScene() != null ? choosePhotoButton.getScene().getWindow() : null;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Foto Profil");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(window);
        if (file != null) {
            selectedPhotoFile = file;
            photoView.setImage(new Image(file.toURI().toString()));
        }
    }

    private void handleSave() {
        if (currentUser == null) {
            handleClose();
            return;
        }
        String newNama = namaField.getText();
        if (newNama == null || newNama.isBlank()) {
            showError("Nama lengkap tidak boleh kosong.");
            return;
        }
        try {
            boolean changed = false;
            if (!newNama.equals(currentUser.getNamaLengkap())) {
                userDao.updateNama(currentUser.getIdUser(), newNama);
                currentUser.setNamaLengkap(newNama);
                changed = true;
            }
            if (selectedPhotoFile != null) {
                String path = selectedPhotoFile.getAbsolutePath();
                userDao.updatePhoto(currentUser.getIdUser(), path);
                currentUser.setPhotoPath(path);
                changed = true;
            }
            if (changed) {
                App.setCurrentUser(currentUser);
            }
            handleClose();
        } catch (SQLException e) {
            showError("Gagal menyimpan foto profil: " + e.getMessage());
        }
    }

    private void handleClose() {
        Window window = closeButton.getScene() != null ? closeButton.getScene().getWindow() : null;
        if (window != null) {
            window.hide();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
