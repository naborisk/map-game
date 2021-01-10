import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MapData {
    public static final int TYPE_SPACE = 0;
    public static final int TYPE_WALL = 1;
    public static final int TYPE_PLAY = 2;
    public static final int TYPE_FOOD = 3;
    public static final int TYPE_DOKURINGO = 4;
    public static final int TYPE_GOAL = 5;

    private static final String mapImageFiles[] = {
        "png/SPACE.png",
        "png/WALL.png",
        "png/catplay.png",
        "png/catfood.png",
        "png/dokuringo.png",
        "png/GOAL.png"
    };

    private static Image[] mapImages;
    private ImageView[][] mapImageViews;
    private int[][] maps;
    private int width;
    private int height, r, d;

    MapData(int x, int y) {
        mapImages = new Image[mapImageFiles.length];
        mapImageViews = new ImageView[y][x];
        for (int i = 0; i < mapImageFiles.length; i++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }

        width = x;
        height = y;
        maps = new int[y][x];

        fillMap(MapData.TYPE_WALL);
        digMap(1, 3);
        GoalPosition();
        setitem();
        setImageViews();

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMap(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return maps[y][x];
    }

    public ImageView getImageView(int x, int y) {
        return mapImageViews[y][x];
    }

    public void setMap(int x, int y, int type) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        maps[y][x] = type;
    }

    // set images based on two-dimentional arrays (maps[y][x])
    public void setImageViews() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
            }
        }
    }

    public void setImageViews_one(int x, int y) {
        mapImageViews[y][x] = new ImageView(mapImages[0]);
    }

    // fill two-dimentional arrays with a given number (maps[y][x])
    public void fillMap(int type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maps[y][x] = type;
            }
        }
    }

    // dig walls for creating trails
    public void digMap(int x, int y) {
        setMap(x, y, MapData.TYPE_SPACE);
        int[][] dl = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };
        int[] tmp;

        for (int i = 0; i < dl.length; i++) {
            int r = (int) (Math.random() * dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        for (int i = 0; i < dl.length; i++) {
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(x + dx * 2, y + dy * 2) == MapData.TYPE_WALL) {
                setMap(x + dx, y + dy, MapData.TYPE_SPACE);
                digMap(x + dx * 2, y + dy * 2);

            }
        }
    }

    //About item
    public void setitem() {
        int count = 0;
        for (int i = 2; i < 4; i++) {
            while (count < 3) {
                int j = (int) (Math.random() * getHeight());
                int z = (int) (Math.random() * getWidth());
                if (getMap(j, z) == 0 && j != 1 && z != 1) {
                    setMap(j, z, i);
                    count++;
                }
            }
            count = 0;
        }
        while (true) {
            int j = (int) (Math.random() * getHeight());
            int z = (int) (Math.random() * getWidth());
            if (getMap(j, z) == TYPE_SPACE) {
                setMap(j, z, 4);
                break;
            }
        }
    }

    //About goal
    public void GoalPosition(){
      while(true){
        int r = (int)(Math.random()*getHeight());
        int d = (int)(Math.random()*getWidth());
        if(getMap(r,d) == TYPE_SPACE){
          setMap(r,d,MapData.TYPE_GOAL);
          break;
        }
      }
    }
}
