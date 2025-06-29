package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Set locale default
            Locale.setDefault(new Locale("id", "ID"));
            ResourceBundle bundle = ResourceBundle.getBundle("localization.Bundle", Locale.getDefault());

            // Load Login.fxml sebagai halaman awal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();

            primaryStage.setTitle("Inventaris Bahan Dapur");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
