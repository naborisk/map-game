import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.control.Label;


public class MapGame extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("MAP GAME");
        Pane myPane_top = (Pane) FXMLLoader.load(getClass().getResource("MapGame.fxml"));
        Scene myScene = new Scene(myPane_top);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    public static void changeSceneByFXML(String name) throws IOException {
        Pane myPane_top = (Pane) FXMLLoader.load(MapGame.class.getResource(name));
        Scene myScene = new Scene(myPane_top);
        stage.setScene(myScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
