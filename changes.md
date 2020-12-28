# Changes Made to Original Code
Since a part of the original code has to be altered for the program to function correctly,
changes made to the originally given code will be documented here (as much as possible).

# MapData.java
## mapImageFiles Array
```java
private static final String mapImageFiles[] = {
    "png/SPACE.png",
    "png/WALL.png",
    //--- BEGIN EDIT
    "png/ITEM.png"
    //--- END EDIT
};
```

Added 'png/ITEM.png' to allow for item image loading (ITEM.png required)

## MapData Constructor
```java
MapData(int x, int y){
    mapImages = new Image[3];
    [...]
    for (int i=0; i<3; i++) {
        mapImages[i] = new Image(mapImageFiles[i]);
    }
    [...]
    placeItem(3);
}
```
`mapImage` was initialized with the size of `2` at first, a change to `3` is needed to prevent `ArrayIndexOutOfBounds` Exception  
loop's limit changed to 3 to allow for maximum # of mapImageFiles array (could be changed to mapImageFiles.length later).  
The placeItem implementation is also added here.

# MoveChara.java
# MapGameController.java
```java
@Override
public void initialize(URL url, ResourceBundle rb) {
    init();
}

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
```

Since we have to use for the 2 arguments in `initialize()`, we can make an independent
function out of the code inside `initialize()` so we can use the function for screen clearing.
