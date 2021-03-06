package mapgame.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
    public static final int TYPE_SPACE = 0;
    public static final int TYPE_WALL = 1;

    //--- BEGIN EDIT
    public static final int TYPE_ITEM_WATCH = 2;
    public static final int TYPE_ITEM_CATPLAY = 3;
    public static final int TYPE_ITEM_CATFOOD = 4;

    public static final int TYPE_GOAL = 5;
    int numItems;
    //---END EDIT

    private static final String mapImageFiles[] = {
            "mapgame/assets/png/SPACE.png",
            "mapgame/assets/png/WALL.png",
            //--- BEGIN EDIT
            //"mapgame.assets.png/ITEM.mapgame.assets.png",
            "mapgame/assets/png/tokei.png",
            "mapgame/assets/png/catplay.png",
            "mapgame/assets/png/catfood.png",
            "mapgame/assets/png/GOAL.png"
            //--- END EDIT
    };

    private Image[] mapImages;
    private ImageView[][] mapImageViews;
    private int[][] maps;
    private int width;
    private int height;

    public MapData(int x, int y){
        mapImages = new Image[mapImageFiles.length]; // This array's length should be the same with mapImageFiles's
        mapImageViews = new ImageView[y][x];
        for (int i=0; i< mapImageFiles.length; i++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }

        width = x;
        height = y;
        maps = new int[y][x];

        fillMap(MapData.TYPE_WALL);
        digMap(1, 3);
        //placeItem(3);
        setItem(1, 3, 3);
        setGoal();
        setImageViews();
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
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

    public void setMap(int x, int y, int type){
        if (x < 1 || width <= x-1 || y < 1 || height <= y-1) {
            return;
        }
        maps[y][x] = type;
    }

    //--- BEGIN EDIT

    public int getNumItems() {
        return numItems;
    }

    //--- END EDIT

	// set images based on two-dimentional arrays (maps[y][x])
    public void setImageViews() {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
            }
        }
    }

	// fill two-dimentional arrays with a given number (maps[y][x])
    public void fillMap(int type){
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                maps[y][x] = type;
            }
        }
    }

	// dig walls for creating trails
    public void digMap(int x, int y){
        setMap(x, y, MapData.TYPE_SPACE);
        int[][] dl = {{0,1},{0,-1},{-1,0},{1,0}};
        int[] tmp;

        for (int i=0; i<dl.length; i++) {
            int r = (int)(Math.random()*dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        //System.out.println(dl[0][0] + " " + dl[0][1]);

        for (int i=0; i<dl.length; i++){
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(x+dx*2, y+dy*2) == MapData.TYPE_WALL){
                setMap(x+dx, y+dy, MapData.TYPE_SPACE);
                digMap(x+dx*2, y+dy*2);

            }

        }
    }

    //--- BEGIN EDIT
//    void placeItem(int number) {
//        this.numItems = number;
//
//        int i = 0;
//        while(i < number) {
//            int x = (int) (Math.random() * maps.length);
//            int y = (int) (Math.random() * maps.length);
//            if(maps[y][x] == TYPE_SPACE){
//                maps[y][x] = TYPE_OTHERS;
//                i++;
//                System.out.println("Item placed on: " + x + ", " + y);
//            }
//        }
//    }

    public void setItem(int num) {
        setItem(num, num, num);
    }

    public void setItem(int numWatch, int numPlay, int numFood) {
        int count = 0;
        //for (int i = TYPE_ITEM_APPLE; i <= TYPE_ITEM_CATFOOD; i++) {
        while (count < numWatch) {
            int j = (int) (Math.random() * getHeight());
            int z = (int) (Math.random() * getWidth());
            if (getMap(j, z) == TYPE_SPACE && j != 1 && z != 1) {
                setMap(j, z, TYPE_ITEM_WATCH);
                count++;
            }
        }
        count=0;

        while (count < numPlay) {
            int j = (int) (Math.random() * getHeight());
            int z = (int) (Math.random() * getWidth());
            if (getMap(j, z) == TYPE_SPACE && j != 1 && z != 1) {
                setMap(j, z, TYPE_ITEM_CATPLAY);
                count++;
            }
        }
        count = 0;

        while (count < numFood) {
            int j = (int) (Math.random() * getHeight());
            int z = (int) (Math.random() * getWidth());
            if (getMap(j, z) == TYPE_SPACE && j != 1 && z != 1) {
                setMap(j, z, TYPE_ITEM_CATFOOD);
                count++;
            }
        }
        //}
        numItems = numWatch + numPlay + numFood;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public void setGoal() {
        setMap(19, 13, TYPE_GOAL);
    }
    //--- END EDIT
}
