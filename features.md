# Features added to the project
Additional features that was implemented by me will be documented here.

# MapData.java
## placeItem
```java
void placeItem(int number) {
    int i = 0;
    while(i < number) {
        int x = (int) (Math.random() * maps.length);
        int y = (int) (Math.random() * maps.length);
        if(maps[y][x] == TYPE_SPACE){
            maps[y][x] = TYPE_OTHERS;
            i++;
            System.out.println("Item placed on: " + x + ", " + y);
        }
    }
}
```
This is a simple implementation of `placeItem`. 
This function will randomly find a free slot of `TYPE_SPACE` and change it to `TYPE_ITEM`

# MapGameController.java
## tileCheck
```java
void tileCheck() {
    int x = chara.getPosX();
    int y = chara.getPosY();

    switch(mapData.getMap(x, y)) {
        case MapData.TYPE_GOAL:
            // Action when goal is reached
            printAction("GOAL REACHED");
            init();
        break;
        case MapData.TYPE_ITEM_APPLE:
            // Action when apple is collected
        case MapData.TYPE_ITEM_CATFOOD:
            // Action when catfood is collected
        case MapData.TYPE_ITEM_CATPLAY:
            // Action when catplay is collected
        default:
            mapData.setMap(x, y, MapData.TYPE_SPACE);
            refreshMap(mapData);
        break;
    }

    refreshMap(mapData);
    drawMap(chara, mapData);
}
```
This function checks for the type of tile the player is standing on and perform
actions according to the type of tile. The action
of each item will be performed and the map will refresh.
