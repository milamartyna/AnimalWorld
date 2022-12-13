package agh.ics.oop;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

abstract class AbstractWorldMap {

    protected Vector2d startMap;
    protected Vector2d endMap;
    private static final Random random = new Random();

    public Vector2d generateMapPosition(){
        int x = random.nextInt(endMap.x() - startMap.x() + 1) + startMap.x();
        int y = random.nextInt(endMap.y() - startMap.y() + 1) + startMap.y();
        return new Vector2d(x, y);
    }

}
