package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.User;
import dao.UserDAO;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SignupController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button signupButton;
    @FXML
    private Button backToLoginButton;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label titleLabel, usernameLabel, passwordLabel, confirmPasswordLabel, languageLabel;

    private ResourceBundle bundle;

    @FXML
    private void initialize() {
        languageComboBox.getItems().addAll("English", "Indonesia");
        languageComboBox.setValue("Indonesia");
        setBundle(new Locale("id", "ID"));  // ✅ Gunakan "id" bukan "in"

        languageComboBox.setOnAction(e -> {
            String selected = languageComboBox.getValue();
            Locale locale = selected.equals("English") ? new Locale("en") : new Locale("id", "ID");
            setBundle(locale);
        });
    }

    private void setBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("localization.Bundle", locale);
        updateTexts();
    }

    private void updateTexts() {
        titleLabel.setText(bundle.getString("signup.title"));
        usernameLabel.setText(bundle.getString("username"));
        passwordLabel.setText(bundle.getString("password"));
        confirmPasswordLabel.setText(bundle.getString("signup.confirmPassword"));
        signupButton.setText(bundle.getString("signup.button"));
        backToLoginButton.setText(bundle.getString("back.to.login")); // ✅ Tambahkan label tombol kembali
        languageLabel.setText(bundle.getString("language"));
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, bundle.getString("signup.emptyFields"));
            return;
        }

        if (!password.equals(confirm)) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("signup.mismatch"));
            return;
        }

        User user = new User(username, password);
        boolean success = UserDAO.signupUser(user);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, bundle.getString("signup.success"));
            goToLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, bundle.getString("signup.failed"));
        }
    }

    @FXML
    private void backToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();
            Stage stage = (Stage) backToLoginButton.getScene().getWindow();  // ✅ Diperbaiki
            stage.setScene(new Scene(root));
            stage.setTitle(bundle.getString("login.title"));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal kembali ke halaman login.\n" + e.getMessage());
        }
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(bundle.getString("login.title"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal membuka halaman login.");
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
