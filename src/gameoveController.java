import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import java.io.IOException;
import javafx.scene.control.Label;

public class gameoveController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void FuncStartButtonAction(ActionEvent event) {
        try {
            MusicController.setA(0);
            MusicController.BGM();
            MusicController.setA(1);
            MusicController.BGM();
            MapGame.changeSceneByFXML("MapGame.fxml");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
