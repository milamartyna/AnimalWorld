package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class WorldMap {

    public final VariableManager manager;
    public Vector2d startMap;
    public Vector2d endMap;
    public HashMap<Vector2d, Integer> placesOfDeath = new HashMap<>();
    private static final Random random = new Random();
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final HashMap<Vector2d, ArrayList<Animal>> animalsOnPositions;
    private final HashMap<Vector2d, Plant> plants = new HashMap<>();
    private final ArrayList<Vector2d> mapAsPositions;
    private final MapVisualiser mapVisualiser;

    public WorldMap(VariableManager manager){
        this.mapVisualiser = new MapVisualiser(this);
        this.startMap = new Vector2d(0, 0);
        this.endMap = new Vector2d(manager.getWidth() - 1, manager.getHeight() - 1);
        this.manager = manager;
        this.mapAsPositions = this.setMapAsPositions();
        this.animalsOnPositions = this.setAnimalsOnPositions();
        manager.getGardenType().seedPlants(this, manager.startPlantsCount);
        this.setUpMap();
    }

    public void placeAnimal(Animal animal){
        this.animals.add(animal);
        ArrayList<Animal> animalsOnTheSamePosition = animalsOnPositions.get(animal.getPosition());
        animalsOnTheSamePosition.add(animal);
    }

    public void nextDay(){
        this.gravedigger();
        this.animalsMove();
        this.animalsEat();
        this.animalsProcreate();
        manager.getGardenType().seedPlants(this, manager.plantsEachDayCount);

        // for testing
        for (Animal animal : animals){
            System.out.println("Animal");
            System.out.println("Position " + animal.getPosition());
            System.out.println("Facing " + animal.getDirection());
            System.out.println("Energy " + animal.getEnergy());
            System.out.println("Age " + animal.getAge());
            System.out.println("Kids Count " + animal.getChildrenCount());
            System.out.println("___________");
        }
        System.out.println();
        System.out.println();
    }

    private void animalsMove(){
        for (Animal animal : animals){
            Vector2d oldPosition = animal.getPosition();
            animal.move();
            if(this.isOutOfBounds(animal.getPosition())) {
                this.manager.getMapType().walksOutOfBounds(animal, this);
            }
            Vector2d newPosition = animal.getPosition();
            this.updateAnimalsOnPositions(animal, oldPosition, newPosition);
        }
    }

    private void animalsEat(){
        for(Vector2d mapPosition : this.mapAsPositions){
            ArrayList<Animal> animalsOnTheSamePosition = this.animalsOnPositions.get(mapPosition);
            if(animalsOnTheSamePosition.size() >= 1){
                Animal bestAnimal = this.bestAnimalWins(animalsOnTheSamePosition);
                bestAnimal.eatsPlant(this.manager.plantsEnergy);
                this.plantIsEaten(mapPosition);
                // Don't know what to with this regarding Toxic Fields
                this.manager.getGardenType().plantIsEaten(this, mapPosition);
            }
        }
    }

    private void animalsProcreate(){
        for(Vector2d mapPosition : this.mapAsPositions){
            ArrayList<Animal> animalsOnTheSamePosition = this.animalsOnPositions.get(mapPosition);

            if(animalsOnTheSamePosition.size() == 2){
                Animal mother = animalsOnTheSamePosition.get(0);
                Animal father = animalsOnTheSamePosition.get(1);
                if(canBeParents(mother, father)){
                    mother.makeChild(father);
                }
            }else if (animalsOnTheSamePosition.size() > 2) {
                Animal mother = this.bestAnimalWins(animalsOnTheSamePosition);
                ArrayList<Animal> possibleFathers = new ArrayList<>(animalsOnTheSamePosition);
                possibleFathers.remove(mother);
                Animal father = this.bestAnimalWins(possibleFathers);
                if(canBeParents(mother, father)){
                    mother.makeChild(father);
                }
            }
        }
    }

    public Vector2d generateMapPosition(){
        int x = random.nextInt(endMap.x() - startMap.x() + 1) + startMap.x();
        int y = random.nextInt(endMap.y() - startMap.y() + 1) + startMap.y();
        return new Vector2d(x, y);
    }

    private void gravedigger(){
        for(Animal animal : animals){
            if(animal.isDead()){
                Vector2d position = animal.getPosition();
                int value = placesOfDeath.get(position);
                placesOfDeath.remove(position);
                placesOfDeath.put(position, value + 1);
                animals.remove(animal);
            }else {
                animal.getsDayOlder();
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

    private Animal bestAnimalWins(ArrayList<Animal> competingAnimals){
        ArrayList<Animal> mostEnergetic = this.mostSomething(competingAnimals, 0);
        if(mostEnergetic.size() == 1){
            return mostEnergetic.get(0);
        }else {
            ArrayList<Animal> oldest = this.mostSomething(mostEnergetic, 1);
            if(oldest.size() == 1){
                return oldest.get(0);
            }else {
                ArrayList<Animal> mostChildren = this.mostSomething(oldest, 2);
                return mostChildren.get(0);
            }
            // either we pick the one lest animal with most kids or just one random
            // animal among the ones with most kids
        }
    }

    private ArrayList<Animal> mostSomething(ArrayList<Animal> competingAnimals, int index){
        int mostSth = 0;
        int thisAnimalSth;
        for(Animal animal : competingAnimals){
            thisAnimalSth = this.attributeValueThatMatters(animal, index);
            if(thisAnimalSth > mostSth){
                mostSth = thisAnimalSth;
            }
        }

        ArrayList<Animal> mostSthAnimals = new ArrayList<>();
        for(Animal animal : competingAnimals){
            thisAnimalSth = this.attributeValueThatMatters(animal, index);
            if(thisAnimalSth >= mostSth){
                mostSthAnimals.add(animal);
            }
        }
        return mostSthAnimals;
    }

    private int attributeValueThatMatters(Animal animal, int index){
        if(index == 0){
            return animal.getEnergy();
        } else if (index == 1) {
            return animal.getAge();
        }else{
            return animal.getChildrenCount();
        }
    }

    // have to check if this works
    private void updateAnimalsOnPositions(Animal animal, Vector2d oldPosition, Vector2d newPosition){
        ArrayList<Animal> animalsOnOldPosition = animalsOnPositions.get(oldPosition);
        animalsOnOldPosition.remove(animal);
        ArrayList<Animal> animalsOnNewPosition = animalsOnPositions.get(newPosition);
        animalsOnNewPosition.add(animal);
    }

    private boolean canBeParents(Animal mother, Animal father){
        int minEnergy = this.manager.energyRequiredToProcreate;
        return mother.getEnergy() >= minEnergy && father.getEnergy() >= minEnergy;
    }

    // make factory made animals
    private void setUpMap(){
        for (int i = 0; i < manager.startAnimalCount; i++){
            new Animal(this);
        }
    }

    private ArrayList<Vector2d> setMapAsPositions(){
        ArrayList<Vector2d> mapAsPositions = new ArrayList<>();
        for(int i = startMap.x(); i <= endMap.x(); i++){
            for(int j = startMap.y(); j <= endMap.y(); j++){
                mapAsPositions.add(new Vector2d(i, j));
            }
        }
        return mapAsPositions;
    }

    private HashMap<Vector2d, ArrayList<Animal>> setAnimalsOnPositions(){
        HashMap<Vector2d, ArrayList<Animal>> animalsOnPositions = new HashMap<>();
        for(Vector2d position : mapAsPositions){
            animalsOnPositions.put(position, new ArrayList<>());
        }
        return animalsOnPositions;
    }

    private boolean isOutOfBounds(Vector2d position){
        return position.x() < startMap.x() || position.x() > endMap.x() ||
                position.y() < startMap.y() || position.y() > endMap.y();
    }

}
