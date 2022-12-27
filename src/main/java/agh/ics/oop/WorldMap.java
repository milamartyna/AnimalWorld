package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class WorldMap {

    public final VariableManager manager;
    public Vector2d startMap;
    public Vector2d endMap;
    private static final Random random = new Random();
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final HashMap<Vector2d, Plant> plants = new HashMap<>();

    private final MapVisualiser mapVisualiser;

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

    private void animalsMove(){
        for (Animal animal : animals){
            animal.move();
            this.manager.getMapType().walksOutOfBounds(animal, this);
        }
    }

    private void animalsEat(){

    }

    public Vector2d generateMapPosition(){
        int x = random.nextInt(endMap.x() - startMap.x() + 1) + startMap.x();
        int y = random.nextInt(endMap.y() - startMap.y() + 1) + startMap.y();
        return new Vector2d(x, y);
    }

    private void gravedigger(){
        for(Animal animal : animals){
            if(animal.isDead()){
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
