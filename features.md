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