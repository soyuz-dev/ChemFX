package chem.chemfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChemFXMain extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChemFXMain.class.getResource("chemfx-compound-view.fxml"));
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("ChemFXLogo.png")));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("ChemFX");
        stage.setScene(scene);
        stage.getIcons().add(icon);
        stage.show();
    }
}