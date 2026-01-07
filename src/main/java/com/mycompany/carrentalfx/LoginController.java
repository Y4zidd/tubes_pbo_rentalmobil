package com.mycompany.carrentalfx;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rentalmobil.model.User;
import rentalmobil.service.AuthService;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> doLogin());
        passwordField.setOnAction(e -> doLogin());
    }

    private void doLogin() {
        errorLabel.setText("");
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            errorLabel.setText("Username dan password wajib diisi.");
            return;
        }
        try {
            User u = authService.login(username, password);
            if (u == null) {
                errorLabel.setText("Username atau password salah.");
                return;
            }
            App.setCurrentUser(u);
            App.setRoot("dashboard");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setWidth(1024);
            stage.setHeight(640);
            stage.centerOnScreen();
        } catch (Exception e) {
            errorLabel.setText("Gagal login: " + e.getMessage());
        }
    }
}

