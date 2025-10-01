package chem.chemfx;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class ChemFXMain extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        // Load logo image
        Image logoImg = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("ChemFXLogo.png")
        ));

        // --- Splash Screen ---
        ImageView logoView = new ImageView(logoImg);
        logoView.setPreserveRatio(true);
        logoView.setFitWidth(500);

        StackPane splashRoot = new StackPane(logoView);
        Scene splashScene = new Scene(splashRoot, 1080, 720);

        stage.setScene(splashScene);
        stage.setTitle("ChemFX");
        stage.getIcons().add(logoImg);
        stage.show();

        // --- Animations ---
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), logoView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition hold = new PauseTransition(Duration.seconds(1.5));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), logoView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition splashAnimation = new SequentialTransition(fadeIn, hold, fadeOut);

        splashAnimation.setOnFinished(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        ChemFXMain.class.getResource("chemfx-compound-view.fxml")
                );
                Scene mainScene = new Scene(fxmlLoader.load(), 1080, 720);
                mainScene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm()
                );
                stage.setScene(mainScene);
                stage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        splashAnimation.play();
    }
}
