package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import dao.UserDAO;
import util.Session;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private Label usernameLabel, passwordLabel, languageLabel, titleLabel;

    private ResourceBundle bundle;

    @FXML
    private void initialize() {
        // Tambahkan pilihan bahasa
        languageComboBox.getItems().addAll("Indonesia", "English");
        languageComboBox.setValue("Indonesia");
        setBundle(new Locale("id", "ID"));

        // Event saat bahasa diganti
        languageComboBox.setOnAction(e -> {
            String selected = languageComboBox.getValue();
            Locale locale = selected.equals("English") ? new Locale("en") : new Locale("id", "ID");
            setBundle(locale);
        });
    }

    private void setBundle(Locale locale) {
        try {
            bundle = ResourceBundle.getBundle("localization.Bundle", locale);
            updateTexts();
        } catch (Exception e) {
            System.err.println("Gagal load resource bundle: " + e.getMessage());
        }
    }

    private void updateTexts() {
        if (bundle != null) {
            usernameLabel.setText(bundle.getString("username"));
            passwordLabel.setText(bundle.getString("password"));
            loginButton.setText(bundle.getString("login"));
            signupButton.setText(bundle.getString("signup"));
            languageLabel.setText(bundle.getString("language"));
            titleLabel.setText(bundle.getString("login.title"));
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, bundle.getString("login.empty"));
            return;
        }

        User user = UserDAO.login(username, password);

        if (user == null) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("login.failed"));
        } else {
            // ✅ Simpan session username untuk digunakan di halaman lain
            Session.setCurrentUsername(user.getUsername());
            openInventoryPage();
        }
    }

    @FXML
    private void goToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Signup.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(bundle.getString("signup.title"));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal membuka halaman signup.\n" + e.getMessage());
        }
    }

    @FXML
    private void openInventoryPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Inventory.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(bundle.getString("inventory.title"));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal membuka halaman inventaris.\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
