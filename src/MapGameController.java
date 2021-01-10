import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
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
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;
    public Timeline timer;
    public int secondCount = 30;
    public int minuteCount = 0;
    public static int direction = 5;
    int numItems;

    private int[] Qua = new int[6];

    @FXML
    public Label time;

    @FXML
    public Label FOOD;

    @FXML
    public Label PLAY;

    @FXML
    public Label DOKURINGO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timer = new Timeline(
                /* 1000 milli sec */
                new KeyFrame(Duration.millis(1000), event -> {
                    secondCount--;
                    if (secondCount < 0) {
                        secondCount = 59;
                        minuteCount--;
                    }
                    time.setText(String.format("%02d:%02d", minuteCount, secondCount));

                    if (secondCount == 0 && minuteCount == 0) {
                        try {
                            MapGame.changeSceneByFXML("gameover.fxml");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
        init();
    }

    void init(){
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        mapImageViews = new ImageView[mapData.getHeight() * mapData.getWidth()];
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                //setMapImageView(x, y);
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }

        numItems = 0;
        Qua = new int[6];

        drawMap(chara, mapData);
        FOOD.setText("0/3"); // ???????????A?C?e??/?S????L????
        PLAY.setText("0/3");// ???????????A?C?e??/?S??????????????
        DOKURINGO.setText("0/3");// ???????????A?C?e??/?S???????????
    }


    //About goal
    public boolean GoalCondition(){
      if(mapData.getMap(chara.getPosX(),chara.getPosY()) == 2){
        return true;
      }else {
        printAction("pp");
        return false;
      }
    }

    // set mapImageViews
    void setMapImageView(int x, int y) {
        int index = y * mapData.getWidth() + x;
        mapImageViews[index] = mapData.getImageView(x, y);
    }

    // Draw the map
    static int count = 1;
    public void drawMap(MoveChara c, MapData m) {
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                int index = y * mapData.getWidth() + x;
                if (x == cx && y == cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
        }
        if (count == 1) {
            MusicController.BGM();
            count++;
        }
    }

    // Get users key actions
    public void keyAction(KeyEvent event) {
        KeyCode key = event.getCode();
        System.out.println("keycode:" + key);
        if (key == KeyCode.H) {
            leftButtonAction();
        } else if (key == KeyCode.J) {
            downButtonAction();
        } else if (key == KeyCode.K) {
            upButtonAction();
        } else if (key == KeyCode.L) {
            rightButtonAction();
        }

        int data = mapData.getMap(chara.getPosX(), chara.getPosY());
        if (data == MapData.TYPE_FOOD || data == MapData.TYPE_PLAY || data == MapData.TYPE_DOKURINGO || data == MapData.TYPE_GOAL) {
            Qua[data]++;
            collection_item(data, Qua[data]);
            if(data != MapData.TYPE_GOAL) mapData.setMap(chara.getPosX(), chara.getPosY(), MapData.TYPE_SPACE);
            mapData.setImageViews();
        }
        drawMap(chara, mapData);

        setMapImageView(chara.getPosX(), chara.getPosY());

        System.out.println(mapData.getMap(chara.getPosX(), chara.getPosY()));

        if(GoalCondition()){
          // go to nextmap
          printAction("ww");
        }
    }

    // Operations for going the cat down
    public void upButtonAction() {
        printAction("UP");
        chara.setCharaDirection(MoveChara.TYPE_UP);
        chara.move(0, -1);
    }

    // Operations for going the cat down
    public void downButtonAction() {
        printAction("DOWN");
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        chara.move(0, 1);
    }

    // Operations for going the cat right
    public void leftButtonAction() {
        printAction("LEFT");
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        chara.move(-1, 0);
    }

    // Operations for going the cat right
    public void rightButtonAction() {
        printAction("RIGHT");
        chara.setCharaDirection(MoveChara.TYPE_RIGHT);
        chara.move(1, 0);

    }

    public void func1ButtonAction(ActionEvent event) {
        System.out.println("func1: Nothing to do");
    }

    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

    private class ImageAnimation extends AnimationTimer {

        private ImageView charaView = null;
        private Image[] charaImages = null;
        private int index = 0;
        private Text time = new Text();

        private long duration = 500 * 1000000L; // 500[ms]
        private long startTime = 0;

        private long count = 0L;
        private long preCount;
        private boolean isPlus = true;

        @Override
        public void handle(long now) {
            if (startTime == 0) {
                startTime = now;
            }
            time.setText(String.valueOf(now));

            preCount = count;
            count = (now - startTime) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if (index < 0 || 2 < index) {
                    index = 1;
                    isPlus = !isPlus; // true == !false, false == !true
                }
            }
        }
    }

    // ??????????????A?N?V?????@?i?\??j
    public void sleep() {
        try {
            Thread.sleep(10000); // 10?b
        } catch (InterruptedException e) {
        }
    }

    // ?A?C?e??????\??
    public void collection_item(int data, int Que) {
        switch(data) {
            case MapData.TYPE_GOAL:
                printAction("GOAL REACHED");
                printAction("numItems: " + numItems);
                if(numItems == 7) {
                    init();
                    secondCount = 60;
                    return;
                }
            break;
            case MapData.TYPE_PLAY:
                MusicController.effectSound2();
                PLAY.setText(String.format("%d/%d", Que, 3));
                System.out.println(Que);
            break;
            case MapData.TYPE_FOOD:
                MusicController.effectSound1();
                FOOD.setText(String.format("%d/%d", Que, 3));
                System.out.println(Que);
            break;
            case MapData.TYPE_DOKURINGO:
                MusicController.effectSound1();
                DOKURINGO.setText(String.format("%d/%d", Que, 1));
                System.out.println(Que);
            break;
        }

        // Increase the total number of items
        if(Que != 0 && data != MapData.TYPE_GOAL) {
            numItems++;
        }
    }

    //Button Settings
    public void whiteCatButtonAction(ActionEvent event) {
        MoveChara.setIndex(0);
        MoveChara.setDirection(direction);
        chara = new MoveChara(MoveChara.getPosX(), MoveChara.getPosY(), mapData);
        drawMap(chara, mapData);
    }
    public void blackCatButtonAction(ActionEvent event) {
        MoveChara.setIndex(1);
        MoveChara.setDirection(direction);
        chara = new MoveChara(MoveChara.getPosX(), MoveChara.getPosY(), mapData);
        drawMap(chara, mapData);
    }
    public void bgmOnButtonAction(ActionEvent event) {
        System.out.println("bgm: ON");
        MusicController.setA(1);
        MusicController.BGM();
    }
    public void bgmOffButtonAction(ActionEvent event) {
        System.out.println("bgm: OFF");
        MusicController.setA(0);
        MusicController.BGM();
    }
    public void explanation(ActionEvent event) {
        Explanation.showExplanationWindow();
    }
    public void resetButtonAction(ActionEvent event) {
        MoveChara.setPosX(1);
        MoveChara.setPosY(1);
        direction = 2;
        MoveChara.setDirection(2);
        MoveChara.setIndex(0);
        MusicController.setA(0);
        MusicController.BGM();
        MusicController.setA(1);
        MusicController.BGM();
        chara = new MoveChara(1, 1, mapData);
        drawMap(chara, mapData);
    }
}
