# Changes Made to Original Code
Since a part of the original code has to be altered for the program to function correctly,
changes made to the originally given code will be documented here (as much as possible).

# model.MapData.java
## mapImageFiles Array
```java
private static final String mapImageFiles[] = {
    "assets.png/SPACE.assets.png",
    "assets.png/WALL.assets.png",
    //--- BEGIN EDIT
    //"assets.png/ITEM.assets.png",
    "assets.png/dokuringo.assets.png",
    "assets.png/catplay.assets.png",
    "assets.png/catfood.assets.png"
    //--- END EDIT
};
```

Added 'assets.png/ITEM.assets.png' to allow for item image loading (ITEM.assets.png required)  
items are added as follow:
- dokuringo
- catplay
- catfood

## model.MapData Constructor
```java
model.MapData(int x, int y){
    mapImages = new Image[mapImageFiles.length];
    [...]
    for (int i=0; i<mapImageFiles.length; i++) {
        mapImages[i] = new Image(mapImageFiles[i]);
    }
    [...]
    placeItem(3);
}
```
`mapImage` was initialized with the size of `2` at first, a change to `3` is needed to prevent `ArrayIndexOutOfBounds` Exception  
loop's limit changed to 3 to allow for maximum # of mapImageFiles array (could be changed to mapImageFiles.length later).  
The placeItem implementation is also added here.  
The size of `mapImages` and the limit for the loop was also changed to support bigger item pool

# model.MoveChara.java
# controller.MapGameController.java
## init()
```java
@Override
public void initialize(URL url, ResourceBundle rb) {
    init();
}

public void init() {
    mapData = new model.MapData(21, 15);
    chara = new model.MoveChara(1, 1, mapData);
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

Since we have no use for the 2 arguments in `initialize()`, we can make an independent
function out of the code inside `initialize()` so we can use the function for screen clearing.

## refreshMap()
```java
void refreshMap(model.MapData mapData) {
    for(int y=0; y<mapData.getHeight(); y++){
        for(int x=0; x<mapData.getWidth(); x++){
            int index = y*mapData.getWidth() + x;
            mapImageViews[index] = mapData.getImageView(x,y);
        }
    }
    mapData.setImageViews();
}
```

To make life easier when refreshing the map (after items are collected, etc.)
the logic for refreshing map can be implemented into a function.