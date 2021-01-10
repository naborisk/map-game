import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Explanation {
    static void showExplanationWindow() {
        Stage st = new Stage();
        st.setTitle("How to play");
        Label label0 = new Label("~How to move~");
        label0.setFont(new Font("Arial", 25));
        label0.setTextFill(Color.RED);
        Label label1 = new Label("・H: left ← ");
        label1.setFont(new Font("Arial", 20));
        Label label2 = new Label("・ J: down ↓ ");
        label2.setFont(new Font("Arial", 20));
        Label label3 = new Label("・K: up ↑ ");
        label3.setFont(new Font("Arial", 20));
        Label label4 = new Label("・L: right → ");
        label4.setFont(new Font("Arial", 20));

        FlowPane fp = new FlowPane();
        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();

        gp.add(label0, 0,0);
        gp.add(label1, 0,1);
        gp.add(label2, 0,2);
        gp.add(label3, 0,3);
        gp.add(label4, 0,4);

        /*bp.setTop(label0);
        fp.getChildren().add(label1);
        fp.getChildren().add(label2);
        fp.getChildren().add(label3);
        fp.getChildren().add(label4);*/
        Scene s = new Scene(gp, 320, 240);
        st.setScene(s);
        st.show();
    }
}
