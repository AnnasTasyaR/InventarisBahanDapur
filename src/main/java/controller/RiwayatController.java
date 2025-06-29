package controller;

import dao.RiwayatDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.Riwayat;
import util.Session;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class RiwayatController {

    @FXML private ListView<String> riwayatListView;
    @FXML private Label usernameLabel;
    @FXML private Button kembaliButton;

    private final RiwayatDAO dao = new RiwayatDAO();
    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        Locale locale = Locale.getDefault();
        bundle = ResourceBundle.getBundle("localization.Bundle", locale);

        // Ambil username dari session
        String username = Session.getCurrentUsername();
        System.out.println("DEBUG: Username dari Session = " + username); // ✅ DEBUG log

        if (username == null || username.isEmpty()) {
            usernameLabel.setText(bundle.getString("history.for") + " [tidak ada user]");
            showError("Kesalahan", "Username tidak tersedia. Silakan login ulang.");
            return;
        }

        usernameLabel.setText(bundle.getString("history.for") + " " + username);

        // Ambil data riwayat dari database
        List<Riwayat> riwayats = dao.getByUser(username);

        if (riwayats.isEmpty()) {
            riwayatListView.getItems().add("Belum ada riwayat aktivitas.");
        } else {
            for (Riwayat r : riwayats) {
                riwayatListView.getItems().add(r.toString());
            }
        }

        kembaliButton.setOnAction(e -> kembaliKeInventory());
    }

    private void kembaliKeInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Inventory.fxml"));
            loader.setResources(bundle);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) kembaliButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showError(bundle.getString("error.title"), "Gagal kembali ke halaman inventaris.\n" + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
