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
    private ArrayList<Animal> animalsAtTheSameSpot;
    private ArrayList<Animal> animalsWithTheSameEnergy;


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

    public Animal theStrongest(Vector2d position){
        animalsAtTheSameSpot = new ArrayList<Animal>();
        for(Animal animal: animals){
            if(animal.getPosition().equals(position)){
                animalsAtTheSameSpot.add(animal);
            }
        }
        if(theMostEnergy(animalsAtTheSameSpot) != null){
            return theMostEnergy(animalsAtTheSameSpot);
        }
        else{
            if(theOldest(animalsAtTheSameSpot) != null){
                return theOldest(animalsAtTheSameSpot);
            }
            else{
                if(hasTheMostKids(animalsAtTheSameSpot) != null){
                    return hasTheMostKids(animalsAtTheSameSpot);
                }
                else{
                    return chooseRandom(animalsAtTheSameSpot);
                }
            }
        }
    }

    public Animal theMostEnergy(ArrayList<Animal> animalsAtTheSameSpot) {
        int maxEnergy = 0;
        int counter = 1; // how many of the animals have the same maximum energy
        Animal theBest = null;
        for (Animal animal : animalsAtTheSameSpot) {
            if (maxEnergy < animal.getEnergy()) {
                maxEnergy = animal.getEnergy();
                theBest = animal;
                counter = 1;
            }
            else if (maxEnergy == animal.getEnergy()) {
                counter += 1;
            }
        }
        if (counter > 1) {
            return null;
        }
        return theBest;
    }

    public Animal theOldest(ArrayList<Animal> animalsAtTheSameSpot){
        int maxAge = 0;
        int counter = 1; // how many of the animals have the same maximum energy
        Animal theBest = null;
        for (Animal animal : animalsAtTheSameSpot) {
            if (maxAge < animal.getAge()) {
                maxAge = animal.getAge();
                theBest = animal;
                counter = 1;
            }
            else if (maxAge == animal.getAge()) {
                counter += 1;
            }
        }
        if (counter > 1) {
            return null;
        }
        return theBest;
    }

    public Animal hasTheMostKids(ArrayList<Animal> animalsAtTheSameSpot){
        int maxKids = 0;
        int counter = 1; // how many of the animals have the same maximum energy
        Animal theBest = null;
        for (Animal animal : animalsAtTheSameSpot) {
            if (maxKids < animal.getKidsCounter()) {
                maxKids = animal.getKidsCounter();
                theBest = animal;
                counter = 1;
            }
            else if (maxKids == animal.getKidsCounter()) {
                counter += 1;
            }
        }
        if (counter > 1) {
            return null;
        }
        return theBest;
    }

    public Animal chooseRandom(ArrayList<Animal> animalsAtTheSameSpot) {
        int maxEnergy = 0;
        int howMany = 0;
        animalsWithTheSameEnergy = new ArrayList<Animal>();
        for (Animal animal : animalsAtTheSameSpot) {
            maxEnergy = max(maxEnergy, animal.getEnergy());
        }
        for(Animal animal : animalsAtTheSameSpot){
            if(animal.getEnergy() == maxEnergy){
                animalsWithTheSameEnergy.add(animal);
                howMany += 1;
            }
        }
        int choose = random.nextInt(howMany);
        return animalsWithTheSameEnergy.get(choose);
    }

    @Override
    public String toString(){
        return mapVisualiser.draw(startMap, endMap);
    }

}
