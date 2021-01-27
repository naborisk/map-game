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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import mapgame.model.MapData;
import mapgame.model.MoveChara;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;

    public static final int LIMIT_SECONDS = 180; // Set time here in seconds, it will be auto-converted in the code

    MusicController mc;

    Timeline timer;
    int sec, min, score;

    boolean soundEnabled;

    @FXML
    Label lblItemApple, lblItemFood, lblItemPlay;
    @FXML
    Label lblTime, lblScore, lblLevel, lblTotalScore;
    @FXML
    HBox hbStats;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hbStats.setAlignment(Pos.BASELINE_CENTER);

        soundEnabled = true;
        //MusicController.BGM();
        mc = new MusicController();
        mc.playBgm();

        min = LIMIT_SECONDS/60; // get time limit in minutes (discarding seconds)
        sec = LIMIT_SECONDS%60; // get the seconds left from the minutes above

        // Timer logic
        timer = new Timeline(
                /* 1000 milli sec */
                new KeyFrame(Duration.millis(1000), event -> {
                    sec--;
                    if (sec < 0) {
                        sec = 59;
                        min--;
                    }
                    lblTime.setText(String.format("%02d:%02d", min, sec));

                    if (sec == 0 && min == 0) {
                        try {
                            GameOver();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }));

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
        init();
    }

    private void GameOver() {
        mc.stopBgm();
        try {
            Stage stage=(Stage) lblScore.getScene().getWindow(); // getScene is called from lblScore
            Parent root = FXMLLoader.load(getClass().getResource("../view/GameOver.fxml")); // Exception
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    ///--- BEGIN EDIT
    public void init() {
        // Score logic
        score = 3000;

        // Timer logic
        min = LIMIT_SECONDS/60;
        sec = LIMIT_SECONDS%60;

        lblTime.setText(String.format("%02d:%02d", min, sec));

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
        lblScore.setText(String.valueOf((score-=10))); // Set the text to tha value of score-10 while assigning the value
        if(score < 0){
            try {
                GameOver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                    var currentScore = Integer.valueOf(lblScore.getText());
                    var totalScore = Integer.valueOf(lblTotalScore.getText());

                    var currentLevel = Integer.valueOf(lblLevel.getText());

                    lblTotalScore.setText(String.valueOf(totalScore + currentScore));
                    lblLevel.setText(String.valueOf(currentLevel + 1));

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

    public void charaButtonAction(ActionEvent event) {
        var x = chara.getPosX();
        var y = chara.getPosY();
        var color = chara.getCurrentChar().equals(MoveChara.CHAR_BLACK) ? MoveChara.CHAR_WHITE : MoveChara.CHAR_BLACK;

        chara = new MoveChara(x, y, mapData, color);

        printAction("CHARACTER CHANGED");

        //mc.stopBgm();

        //GameOver();
        //init();


        System.out.println("func1: Nothing to do");
    }

    public void soundButtonAction(ActionEvent event) {
        Button func2 = (Button) event.getSource();

        soundEnabled = !soundEnabled;
        func2.setText(soundEnabled ? "Sound: ON" : "Sound: OFF");
        if(soundEnabled) {
            mc.playBgm();
        } else  {
            mc.stopBgm();
        }
    }

    public void restartButtonAction(ActionEvent event) {
        var color = chara.getCurrentChar();
        chara = new MoveChara(1, 1, mapData, color);
        drawMap(chara, mapData);
    }

    public void resetButtonAction(ActionEvent event) {
        init();
    }


    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

}
