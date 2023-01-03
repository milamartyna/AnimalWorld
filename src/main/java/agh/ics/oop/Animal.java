package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class Animal implements IMapElement{

    private int age;
    private final WorldMap map;
    private int energy;
    private int childrenCount;
    public GeneDirection[] dna;
    private Vector2d position;
    private GeneDirection direction;
    private int activeGene; // this is the index of the dna the animal will be using the next day
    private int eatenPlants;
    private static final Random random = new Random();

    // constructor for newborn Animals
    public Animal(GeneDirection[] dna, Vector2d position, WorldMap map){
        this.map = map;
        // because after procreation the map animal energy sum should remain the same
        this.energy = map.manager.energyLossForChild * 2;
        this.age = 0;
        this.childrenCount = 0;
        this.dna = dna;
        this.position = position;
        this.activeGene = random.nextInt(map.manager.dnaLength);
        this.direction = GeneDirection.generateGeneDirection();
        this.map.placeAnimal(this);
        this.eatenPlants = 0; // how much plant did animal eat
    }

    // constructor for factory made Animals
    public Animal(WorldMap map){
        this.map = map;
        this.energy = map.manager.startEnergyForFactoryAnimals;
        this.age = 0;
        this.childrenCount = 0;
        this.dna = generateDna();
        this.position = this.map.generateMapPosition(); // don't know if that's correct
        this.activeGene = 0; // not sure
        this.direction = GeneDirection.generateGeneDirection();
        this.map.placeAnimal(this); // important to place the animal after setting all the parameters
        this.eatenPlants = 0;
    }

    private GeneDirection[] generateDna(){
        GeneDirection[] geneDirection = new GeneDirection[map.manager.dnaLength];
        for(int i = 0; i < map.manager.dnaLength; i++){
            geneDirection[i] = GeneDirection.generateGeneDirection();
        }
        return geneDirection;
    }

    public void move(){
        this.direction = this.direction.turn(this.dna[this.activeGene]);
        this.activeGene = this.map.manager.getBehaviorType().geneActivation(this.activeGene);
        this.position = this.position.add(this.direction.toUnitVector());
    }

    private GeneDirection[] crossDna(Animal father){
        GeneDirection[] childDna = new GeneDirection[map.manager.dnaLength];
        GeneDirection[] leftArr;
        GeneDirection[] rightArr;

        Animal stronger;
        Animal weaker;
        double totalEnergy = (this.energy + father.energy);

        if(this.energy > father.energy){
            stronger = this;
            weaker = father;
        }else {
            stronger = father;
            weaker = this;
        }

        int strongerGene = (int) Math.ceil(stronger.energy / totalEnergy * map.manager.dnaLength);
        int weakerGene = (int) Math.floor(weaker.energy / totalEnergy * map.manager.dnaLength);

        int side = random.nextInt(2);

        if(side == 0){
            leftArr = Arrays.copyOfRange(stronger.dna, 0, strongerGene);
            rightArr = Arrays.copyOfRange(weaker.dna ,strongerGene, map.manager.dnaLength);
        }else {
            rightArr = Arrays.copyOfRange(stronger.dna, weakerGene, map.manager.dnaLength);
            leftArr = Arrays.copyOfRange(weaker.dna ,0, weakerGene);
        }

        System.arraycopy(leftArr, 0, childDna, 0, leftArr.length);
        System.arraycopy(rightArr, 0, childDna, leftArr.length, rightArr.length);

        return childDna;
    }

    // this method should not be called unless animals are on the same position
    public void makeChild(Animal father){
        this.updateEnergy(map.manager.energyLossForChild);
        this.updateChildrenCount();
        father.updateChildrenCount();
        father.updateEnergy(map.manager.energyLossForChild);
        GeneDirection[] childDna = this.crossDna(father);
        this.map.manager.getMutationType().mutation(childDna);
        new Animal(childDna, this.position, this.map);
    }

    // we can't have an animal with negative value, so if an animal walks out of bounds in hell gate
    // it will zero the animals energy
    public void updateEnergy(int loss){
        this.energy = Math.max(this.energy - loss, 0);
    }

    public void eatsPlant(int plantsEnergy){
        this.energy = this.energy + plantsEnergy;
        this.eatenPlants = this.eatenPlants + 1;
    }

    public boolean isDead(){
        return this.energy <= 0;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void getsDayOlder(){
        this.age = age + 1;
        this.energy = this.energy - map.manager.dailyEnergyLoss;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void updateChildrenCount(){
        this.childrenCount = this.childrenCount + 1;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDirection(GeneDirection geneDirection){
        this.direction = geneDirection;
    }

    public WorldMap getMap() {
        return map;
    }

    public GeneDirection[] getDna(){
        return this.dna;
    }

    public GeneDirection getDirection() {
        return direction;
    }

    public int getEnergy(){
        return this.energy;
    }

    public int getAge(){
        return this.age;
    }

    public int getActiveGene() {
        return this.activeGene;
    }

    public int getEatenPlants() {
        return this.eatenPlants;
    }

    @Override
    public String toString(){
        return "A";
    }

    @Override
    public String getImage() {
        return switch (this.direction) {
            case ZERO -> "src/main/resources/zero.png";
            case ONE -> "src/main/resources/one.png";
            case TWO -> "src/main/resources/two.png";
            case THREE -> "src/main/resources/three.png";
            case FOUR -> "src/main/resources/four.png";
            case FIVE -> "src/main/resources/five.png";
            case SIX -> "src/main/resources/six.png";
            case SEVEN -> "src/main/resources/seven.png";
        };
    }
}
