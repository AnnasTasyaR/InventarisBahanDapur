package controller;

import dao.InventoryDAO;
import dao.RiwayatDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.BahanDapur;
import model.Riwayat;
import util.Session;
import util.ExpiredCheckerUtil;
import util.DataWrapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.scene.media.AudioClip;


public class InventoryController {

    @FXML private TableView<BahanDapur> bahanTable;
    @FXML private TableColumn<BahanDapur, String> namaColumn;
    @FXML private TableColumn<BahanDapur, Integer> jumlahColumn;
    @FXML private TableColumn<BahanDapur, String> satuanColumn;
    @FXML private TableColumn<BahanDapur, LocalDate> tglColumn;
    @FXML private TableColumn<BahanDapur, String> kategoriColumn;
    @FXML private TableColumn<BahanDapur, Void> actionColumn;

    @FXML private TextField namaField, jumlahField, satuanField, kategoriField;
    @FXML private DatePicker tglKadaluarsaPicker;
    @FXML private Label usernameLabel;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private Label titleLabel;
    @FXML private Button backupBtn;
    @FXML private Button restoreBtn;
    @FXML private Button btnExpired;

    private final InventoryDAO dao = new InventoryDAO();
    private final RiwayatDAO riwayatDAO = new RiwayatDAO();
    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        bundle = ResourceBundle.getBundle("localization.Bundle", java.util.Locale.getDefault());

        usernameLabel.setText("Halo, " + Session.getCurrentUsername());
        titleLabel.setText(bundle.getString("inventory.title"));

        languageComboBox.getItems().addAll("Indonesia", "English");
        languageComboBox.setValue("Indonesia");
        languageComboBox.setOnAction(e -> {
            String selected = languageComboBox.getValue();
            java.util.Locale locale = selected.equals("English") ? new java.util.Locale("en") : new java.util.Locale("id", "ID");
            bundle = ResourceBundle.getBundle("localization.Bundle", locale);
            refreshTexts();
        });

        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        satuanColumn.setCellValueFactory(new PropertyValueFactory<>("satuan"));
        tglColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalKadaluarsa"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("kategori"));

        loadData();
        addButtonToTable();

        backupBtn.setOnAction(e -> {
            Task<Void> backupTask = new Task<Void>() {
                @Override
                protected Void call() {
                    dao.backupKeFile("backup_bahan.ser");
                    return null;
                }

                @Override
                protected void succeeded() {
                    showAlert(Alert.AlertType.INFORMATION, "Backup berhasil disimpan ke file.");
                }

                @Override
                protected void failed() {
                    showAlert(Alert.AlertType.ERROR, "Gagal melakukan backup.");
                }
            };
            new Thread(backupTask).start();
        });

        restoreBtn.setOnAction(e -> {
            Task<List<BahanDapur>> restoreTask = new Task<List<BahanDapur>>() {
                @Override
                protected List<BahanDapur> call() {
                    return dao.restoreDariFile("backup_bahan.ser");
                }

                @Override
                protected void succeeded() {
                    bahanTable.setItems(FXCollections.observableArrayList(getValue()));
                    showAlert(Alert.AlertType.INFORMATION, "Data berhasil dipulihkan dari file.");
                }

                @Override
                protected void failed() {
                    showAlert(Alert.AlertType.ERROR, "Gagal memulihkan data dari file.");
                }
            };
            new Thread(restoreTask).start();
        });
    }

    private void refreshTexts() {
        titleLabel.setText(bundle.getString("inventory.title"));
        usernameLabel.setText(bundle.getString("greeting") + ", " + Session.getCurrentUsername());
    }

    private void loadData() {
        Task<List<BahanDapur>> loadTask = new Task<List<BahanDapur>>() {
            @Override
            protected List<BahanDapur> call() {
                return dao.getSemuaBahan();
            }

            @Override
            protected void succeeded() {
                List<BahanDapur> list = getValue();
                bahanTable.setItems(FXCollections.observableArrayList(list));

                StringBuilder expiredMessages = new StringBuilder();
                for (BahanDapur bahan : list) {
                    LocalDate expiryDate = bahan.getTanggalKadaluarsa();
                    if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
                        expiredMessages.append("⚠️ Bahan \"")
                                .append(bahan.getNama())
                                .append("\" sudah kadaluarsa pada ")
                                .append(expiryDate)
                                .append(".\n");
                    }
                }

                if (expiredMessages.length() > 0) {
                    Platform.runLater(() -> {
                        try {
                            String soundPath = getClass().getResource("assetsound/alert.wav").toString();
                            AudioClip clip = new AudioClip(soundPath);
                            clip.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Peringatan Kadaluarsa");
                        alert.setHeaderText("Beberapa bahan sudah kadaluarsa:");
                        alert.setContentText(expiredMessages.toString());
                        alert.showAndWait();
                    });
                }
            }
        };

        new Thread(loadTask).start();
    }

    @FXML
    private void tambahBahan() {
        String nama = namaField.getText();
        String jumlahStr = jumlahField.getText();
        String satuan = satuanField.getText();
        String kategori = kategoriField.getText();
        LocalDate tgl = tglKadaluarsaPicker.getValue();

        if (nama.isEmpty() || jumlahStr.isEmpty() || satuan.isEmpty() || kategori.isEmpty() || tgl == null) {
            showAlert(Alert.AlertType.WARNING, "Semua kolom harus diisi.");
            return;
        }

        try {
            int jumlah = Integer.parseInt(jumlahStr);
            String username = Session.getCurrentUsername();

            Object existingId = namaField.getUserData();
            if (existingId != null) {
                BahanDapur updated = new BahanDapur(existingId.toString(), nama, jumlah, satuan, tgl, kategori);
                dao.updateBahan(updated);
                riwayatDAO.simpan(new Riwayat(username, "Edit bahan: " + nama, LocalDateTime.now()));
                showAlert(Alert.AlertType.INFORMATION, "Data berhasil diperbarui.");
                namaField.setUserData(null);
            } else {
                String id = UUID.randomUUID().toString();
                BahanDapur bahan = new BahanDapur(id, nama, jumlah, satuan, tgl, kategori);
                dao.tambahBahan(bahan);
                riwayatDAO.simpan(new Riwayat(username, "Tambah bahan: " + nama, LocalDateTime.now()));
                showAlert(Alert.AlertType.INFORMATION, "Data berhasil ditambahkan.");
            }

            loadData();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Jumlah harus berupa angka.");
        }
    }

    private void clearForm() {
        namaField.clear();
        jumlahField.clear();
        satuanField.clear();
        kategoriField.clear();
        tglKadaluarsaPicker.setValue(null);
        namaField.setUserData(null);
    }

    private void addButtonToTable() {
        actionColumn.setCellFactory(param -> new TableCell<BahanDapur, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Hapus");

            {
                editBtn.setOnAction(event -> {
                    BahanDapur selected = getTableView().getItems().get(getIndex());
                    namaField.setText(selected.getNama());
                    jumlahField.setText(String.valueOf(selected.getJumlah()));
                    satuanField.setText(selected.getSatuan());
                    kategoriField.setText(selected.getKategori());
                    tglKadaluarsaPicker.setValue(selected.getTanggalKadaluarsa());
                    namaField.setUserData(selected.getId());
                });

                deleteBtn.setOnAction(event -> {
                    BahanDapur selected = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Hapus bahan " + selected.getNama() + "?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            dao.hapusBahan(selected.getId());
                            riwayatDAO.simpan(new Riwayat(
                                    Session.getCurrentUsername(),
                                    "Hapus bahan: " + selected.getNama(),
                                    LocalDateTime.now()));
                            loadData();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, editBtn, deleteBtn);
                    setGraphic(box);
                }
            }
        });
    }

    @FXML
    private void openRiwayat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Riwayat.fxml"));
            loader.setResources(bundle);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) bahanTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal membuka halaman riwayat.");
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            loader.setResources(bundle);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) bahanTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal logout.");
            e.printStackTrace();
        }
    }

    // ✅ Handler tombol "Lihat Bahan Kadaluarsa"
    @FXML
    private void handleExpiredClick() {
        List<BahanDapur> semuaBahan = dao.getSemuaBahan();
        DataWrapper<List<BahanDapur>> expiredWrapper = ExpiredCheckerUtil.getExpiredWrapper(semuaBahan);
        List<BahanDapur> expiredList = expiredWrapper.getData();

        if (expiredList.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Tidak ada bahan yang kadaluarsa.");
        } else {
            StringBuilder sb = new StringBuilder("Bahan kadaluarsa:\n");
            for (BahanDapur b : expiredList) {
                sb.append("- ").append(b.getNama())
                  .append(" (").append(b.getTanggalKadaluarsa()).append(")\n");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText("Daftar bahan yang sudah kadaluarsa:");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
