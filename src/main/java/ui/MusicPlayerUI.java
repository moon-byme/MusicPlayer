package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MusicPlayerUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MusicPlayerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("MusicPlayer GUI");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setScene(scene);
        stage.show();
    }
}