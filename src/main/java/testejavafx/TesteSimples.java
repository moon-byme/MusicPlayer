package testejavafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TesteSimples extends Application {
    
    @Override
    public void start(Stage stage) {
        Label label = new Label("🎵 JavaFX está funcionando! 🎵");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 300, 200);
        
        stage.setTitle("Teste JavaFX");
        stage.setScene(scene);
        stage.show();
        
        System.out.println("✅ JavaFX inicializado com sucesso!");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}