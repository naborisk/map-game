package mapgame.controller;

import java.net.URL;
import java.security.Key;
import java.util.Arrays;
import java.util.Objects;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import mapgame.model.MapData;
import mapgame.model.MoveChara;

import javax.swing.plaf.ActionMapUIResource;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;

    public KeyCode[] keybind;

    public KeyCode[] KEYBIND_HJKL = {KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L};
    public KeyCode[] KEYBIND_WASD = {KeyCode.A, KeyCode.S, KeyCode.W, KeyCode.D};

    public static final int LIMIT_SECONDS = 180; // Set time here in seconds, it will be auto-converted in the code

    MusicController mc;

    Timeline timer;
    int sec, min, score;

    boolean soundEnabled;

    @FXML
    Label lblItemWatch, lblItemFood, lblItemPlay;
    @FXML
    Label lblTime, lblScore, lblLevel, lblTotalScore;
    @FXML
    HBox hbStats;
    @FXML
    Image imgCharaButton;

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

        keybind = KEYBIND_HJKL;
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

        // Make the game harder when level is high
        int totalSeconds = LIMIT_SECONDS, currentLevel =Integer.parseInt(lblLevel.getText());
        if(currentLevel > 1) totalSeconds = LIMIT_SECONDS - (Integer.parseInt(lblLevel.getText()) * 20 );
        if(totalSeconds < 60) totalSeconds = 60; // Let's not make it impossible

        // Timer logic
        min = totalSeconds/60;
        sec = totalSeconds%60;

        lblTime.setText(String.format("%02d:%02d", min, sec));

        timer.play();

        // Initialization
        mapData = new MapData(21, 15);
        String color = Objects.isNull(chara) ? MoveChara.CHAR_WHITE : chara.getCurrentChar();

        chara = new MoveChara(1, 1, mapData, color);
        refreshImages();

        lblItemWatch.setText("0/1");
        lblItemPlay.setText("0/3");
        lblItemFood.setText("0/3");
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
        if (key == keybind[0]){
        	leftButtonAction();
        }else if (key == keybind[1]){
            downButtonAction(); 
        }else if (key == keybind[2]){
            upButtonAction();
        }else if (key == keybind[3]){
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
                    int currentScore = Integer.valueOf(lblScore.getText());
                    int totalScore = Integer.valueOf(lblTotalScore.getText());

                    int currentLevel = Integer.valueOf(lblLevel.getText());

                    lblTotalScore.setText(String.valueOf(totalScore + currentScore));
                    lblLevel.setText(String.valueOf(currentLevel + 1));

                    init();
                }
                return;
            //break;
            case MapData.TYPE_ITEM_WATCH:
                // Action when apple is collected
                printAction("WATCH COLLECTED");
                printAction("ZA WARUDO! TOKI WO TOMARE!!!");

                if(soundEnabled) mc.playSfx1();

                amount = lblItemWatch.getText().split("/")[0];
                total = lblItemWatch.getText().split("/")[1];
                lblItemWatch.setText((Integer.parseInt(amount) + 1) + "/" + total);

                timer.stop();

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
        int posX = chara.getPosX();
        int posY = chara.getPosY();
        String color = chara.getCurrentChar().equals(MoveChara.CHAR_BLACK) ? MoveChara.CHAR_WHITE : MoveChara.CHAR_BLACK;

        chara = new MoveChara(posX, posY, mapData, color);

        refreshImages();

        //mapData.setImageViews();

        printAction("CHARACTER CHANGED");

        System.out.println("func1: Nothing to do");
    }

    private void refreshImages() {
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        drawMap(chara, mapData);
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
        String color = chara.getCurrentChar();
        chara = new MoveChara(1, 1, mapData, color);
        drawMap(chara, mapData);
    }

    public void resetButtonAction(ActionEvent event) {
        init();
    }

    public void modeButtonAction(ActionEvent event) {
        Button btnMode = (Button) event.getSource();

        keybind = Arrays.equals(keybind, KEYBIND_HJKL) ? KEYBIND_WASD : KEYBIND_HJKL;
        btnMode.setText(Arrays.equals(keybind, KEYBIND_HJKL) ? "Mode: HJKL" : "Mode: WASD");
    }


    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

}
