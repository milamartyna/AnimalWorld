package agh.ics.oop;

import java.util.ArrayList;
import java.util.Random;

class WorldMap {

    protected Vector2d startMap;
    protected Vector2d endMap;
    private static final Random random = new Random();
    protected static ArrayList<Animal> animals = new ArrayList<>();

    public WorldMap(VariableManager manager){
        this.manager = manager;
    }

    public Vector2d generateMapPosition(){
        int x = random.nextInt(endMap.x() - startMap.x() + 1) + startMap.x();
        int y = random.nextInt(endMap.y() - startMap.y() + 1) + startMap.y();
        return new Vector2d(x, y);
    }

    public void gravedigger(){
        for(Animal animal : animals){
            if(animal.isDead()){
                animals.remove(animal);
            }
        }
    }

}
