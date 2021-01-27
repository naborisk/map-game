package mapgame.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    MusicController mc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mc = new MusicController();
        mc.play("src/mapgame/assets/bgm/gameover.mp3");
    }

    public void restartButtonAction(ActionEvent event) throws IOException {
        mc.stopBgm();
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/mapgame/view/MapGame.fxml"));/* Exception */
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
