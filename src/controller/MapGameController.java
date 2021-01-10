package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import model.MapData;
import model.MoveChara;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;

    int totalItems;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }

    ///--- BEGIN EDIT
    public void init() {
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
                mapData.setNumItems(mapData.getNumItems()-1);
            break;
            case MapData.TYPE_ITEM_CATFOOD:
                // Action when catfood is collected
                printAction("CATFOOD COLLECTED");
                mapData.setNumItems(mapData.getNumItems()-1);
            break;
            case MapData.TYPE_ITEM_CATPLAY:
                // Action when catplay is collected
                printAction("CATPLAY COLLECTED");
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

    public void func1ButtonAction(ActionEvent event) {
        printAction("RESET");
        init();
        //System.out.println("func1: Nothing to do");
    }

    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

}
