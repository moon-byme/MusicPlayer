package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Ponto de entrada da interface gráfica do Reprodutor de Músicas.
 *
 * Estende {@link javafx.application.Application} e carrega o layout FXML
 * {@code MusicPlayerView.fxml}, configurando o tamanho inicial e mínimo
 * da janela principal antes de exibi-la.
 *
 * @author Lethycia
 * @see MusicPlayerController
 */
public class MusicPlayerUI extends Application {

    /** Inicia a aplicação JavaFX. */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Carrega o FXML e exibe a janela principal.
     *
     * @param stage palco principal fornecido pelo runtime JavaFX
     * @throws IOException se o arquivo FXML não for encontrado
     */
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