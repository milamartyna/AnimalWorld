package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class Animal {

    private int age;
    private WorldMap map;
    private int energy;
    private int childrenCount;
    public GeneDirection[] dna;
    private Vector2d position;
    private GeneDirection direction;
    private int activeGene; // this is the index of the dna the animal will be using the next day
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
    }

    // constructor for factory made animals
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
        // the change of the active gene
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
    // probably can be void but for testing
    public Animal makeChild(Animal father){
        this.updateEnergy(map.manager.energyLossForChild);
        this.updateChildrenCount();
        father.updateChildrenCount();
        father.updateEnergy(map.manager.energyLossForChild);
        GeneDirection[] childDna = this.crossDna(father);
        this.map.manager.getMutationType().mutation(childDna);
        return new Animal(childDna, this.position, this.map);
    }

    public void updateEnergy(int loss){
        this.energy = this.energy - loss;
    }

    public void eatsPlant(int plantsEnergy){
        this.energy = this.energy + plantsEnergy;
    }

    public boolean isDead(){
        return this.energy <= 0;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void getsDayOlder(){
        this.age = age + 1;
        this.energy = energy - map.manager.energyLossForEachDay;
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

    @Override
    public String toString(){
        return "A";
    }
}
