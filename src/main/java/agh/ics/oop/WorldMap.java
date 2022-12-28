package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.max;

class WorldMap {

    public final VariableManager manager;
    public Vector2d startMap;
    public Vector2d endMap;
    public HashMap<Vector2d, Integer> placesOfDeath = new HashMap<>();
    private static final Random random = new Random();
    private ArrayList<Animal> animals = new ArrayList<>();
    private HashMap<Vector2d, Plant> plants = new HashMap<>();


    private MapVisualiser mapVisualiser;

    public WorldMap(VariableManager manager){
        this.mapVisualiser = new MapVisualiser(this);
        this.startMap = new Vector2d(0, 0);
        this.endMap = new Vector2d(manager.getWidth(), manager.getHeight());
        this.manager = manager;
        manager.getGardenType().seedPlants(this, 20);
    }

    public void placeAnimal(Animal animal){
        this.animals.add(animal);
    }

    public Vector2d generateMapPosition(){
        int x = random.nextInt(endMap.x() - startMap.x() + 1) + startMap.x();
        int y = random.nextInt(endMap.y() - startMap.y() + 1) + startMap.y();
        return new Vector2d(x, y);
    }

    public void gravedigger(){
        for(Animal animal : animals){
            if(animal.isDead()){
                Vector2d position = animal.getPosition();
                int value = placesOfDeath.get(position);
                placesOfDeath.remove(position);
                placesOfDeath.put(position, value + 1);
                animals.remove(animal);
            }
        }
    }

    public void addPlant(Vector2d position){
        plants.put(position, new Plant(position));
    }

    // I think the eating of the plants will be handled by the map so there's no need for this method
    // to be called in the green equator
    public void plantIsEaten(Vector2d position){
        plants.remove(position);
    }

    public Object objectAt(Vector2d position) {
        for(Animal animal : animals){
            if(animal.getPosition().equals(position)){
                return animal;
            }
        }
        if(plants.containsKey(position)){
            return this.plants.get(position);
        }
        return null;
    }

    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }
    @Override
    public String toString(){
        return mapVisualiser.draw(startMap, endMap);
    }

}