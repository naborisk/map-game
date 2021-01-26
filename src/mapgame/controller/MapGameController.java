package mapgame.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import mapgame.model.MapData;
import mapgame.model.MoveChara;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;

    MusicController mc;

    Timeline timer;
    int sec = 0, min = 3;

    boolean soundEnabled;

    @FXML
    Label lblItemApple;
    @FXML
    Label lblItemFood;
    @FXML
    Label lblItemPlay;
    @FXML
    Label time;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        soundEnabled = true;
        //MusicController.BGM();
        mc = new MusicController();
        mc.playBgm();

        // Timer logic
        timer = new Timeline(
                /* 1000 milli sec */
                new KeyFrame(Duration.millis(1000), event -> {
                    sec--;
                    if (sec < 0) {
                        sec = 59;
                        min--;
                    }
                    time.setText(String.format("%02d:%02d", min, sec));

                    if (sec == 0 && min == 0) {
                        try {
                            Node node=(Node) event.getSource();
                            Stage stage=(Stage) node.getScene().getWindow();
                            Parent root = FXMLLoader.load(getClass().getResource("../view/GameOver.fxml")); // Exception
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }));

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
        init();
    }

    ///--- BEGIN EDIT
    public void init() {
        // Timer logic
        min = 3;
        sec = 0;
        time.setText(String.format("%02d:%02d", min, sec));



        // Initialization
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        drawMap(chara, mapData);

        lblItemPlay.setText("0/1");
        lblItemFood.setText("0/1");
        lblItemApple.setText("0/1");
    }

    void refreshMap(MapData mapData) {
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        mapData.setImageViews();
    }
    //--- END EDIT

    // Draw the map
    public void drawMap(MoveChara c, MapData m){
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                if (x==cx && y==cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
        }
    }

    // Get users key actions
    public void keyAction(KeyEvent event){
        KeyCode key = event.getCode(); System.out.println("keycode:"+key);
        if (key == KeyCode.H){
        	leftButtonAction();
        }else if (key == KeyCode.J){
            downButtonAction(); 
        }else if (key == KeyCode.K){
            upButtonAction();
        }else if (key == KeyCode.L){
            rightButtonAction();
        }

        tileCheck(chara.getPosX(), chara.getPosY());
    }

    // Operations for going the cat down
    public void upButtonAction(){
        printAction("UP");
        chara.setCharaDirection(MoveChara.TYPE_UP);
        chara.move(0, -1);
        drawMap(chara, mapData);
    }

    // Operations for going the cat down
    public void downButtonAction(){
        printAction("DOWN");
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        chara.move(0, 1);
        drawMap(chara, mapData);
    }

    // Operations for going the cat right
    public void leftButtonAction(){
        printAction("LEFT");
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        chara.move(-1, 0);
        drawMap(chara, mapData);
    }

    // Operations for going the cat right
    public void rightButtonAction(){
        printAction("RIGHT");
        chara.setCharaDirection(MoveChara.TYPE_RIGHT);
        chara.move(1, 0);
        drawMap(chara, mapData);
    }

    //--- BEGIN EDIT
    // item collection & goal logic
    void tileCheck(int x, int y) {
        //System.out.println(mapData.getNumItems());

        String amount, total;

        switch(mapData.getMap(x, y)) {
            case MapData.TYPE_GOAL:
                // Action when goal is reached
                printAction("GOAL REACHED");
                if(mapData.getNumItems() == 0) {
                    init();
                }
                return;
            //break;
            case MapData.TYPE_ITEM_APPLE:
                // Action when apple is collected
                printAction("APPLE COLLECTED");

                if(soundEnabled) mc.playSfx1();

                amount = lblItemApple.getText().split("/")[0];
                total = lblItemApple.getText().split("/")[1];
                lblItemApple.setText((Integer.parseInt(amount) + 1) + "/" + total);

                mapData.setNumItems(mapData.getNumItems()-1);
            break;
            case MapData.TYPE_ITEM_CATFOOD:
                // Action when catfood is collected
                printAction("CATFOOD COLLECTED");

                if(soundEnabled) mc.playSfx1();

                amount = lblItemFood.getText().split("/")[0];
                total = lblItemFood.getText().split("/")[1];
                lblItemFood.setText((Integer.parseInt(amount) + 1) + "/" + total);

                mapData.setNumItems(mapData.getNumItems()-1);
            break;
            case MapData.TYPE_ITEM_CATPLAY:
                // Action when catplay is collected
                printAction("CATPLAY COLLECTED");

                if(soundEnabled) mc.playSfx1();

                amount = lblItemPlay.getText().split("/")[0];
                total = lblItemPlay.getText().split("/")[1];
                lblItemPlay.setText((Integer.parseInt(amount) + 1) + "/" + total);

                mapData.setNumItems(mapData.getNumItems()-1);
            break;
        }

        // refresh the map after collecting item (will remove goal)
        mapData.setMap(x, y, MapData.TYPE_SPACE);
        refreshMap(mapData);
        drawMap(chara, mapData);
        mapData.setImageViews();
    }
    //--- END EDIT

    public void func1ButtonAction(ActionEvent event) throws IOException {
        printAction("RESET");

        mc.stopBgm();

        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/GameOver.fxml")); // Exception
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //init();
        System.out.println("func1: Nothing to do");
    }

    public void func2ButtonAction(ActionEvent event) {
        Button func2 = (Button) event.getSource();

        soundEnabled = !soundEnabled;
        func2.setText(soundEnabled ? "Sound: ON" : "Sound: OFF");
        if(soundEnabled) {
            mc.playBgm();
        } else  {
            mc.stopBgm();
        }
    }

    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

}
