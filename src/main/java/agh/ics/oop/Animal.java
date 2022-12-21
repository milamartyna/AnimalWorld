package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class Animal {

    // don't know where but maybe not here
    public final static int startEnergyForAll = 9;
    private final static int dnaLength = 9;
    private final static int energyLossForChild = 5;
    //

    private WorldMap map;
    private int energy;
    public GeneDirection[] dna;
    private Vector2d position;
    private GeneDirection direction;
    private int activeGene; // this is the index of the dna the animal will be using the next day
    private static final Random random = new Random();

    // constructor for newborn Animals
    public Animal(GeneDirection[] dna, Vector2d position){
//        this.map = map;
        this.energy = energyLossForChild; // there is some loss of energy in the child making process
        this.dna = dna;
        this.position = position;
        this.activeGene = random.nextInt(dnaLength);
        this.direction = GeneDirection.generateGeneDirection();
    }

    // constructor for factory made animals
    public Animal(){
//        this.map = map;
        this.energy = startEnergyForAll; // coś nie halo
        this.dna = generateDna();
        this.position = new Vector2d(0, 0); // need to fix
        this.activeGene = 0; // chyba tak, trza dopytać
        this.direction = GeneDirection.generateGeneDirection();
    }

    private GeneDirection[] generateDna(){
        GeneDirection[] geneDirection = new GeneDirection[dnaLength];
        for(int i = 0; i < dnaLength; i++){
            geneDirection[i] = GeneDirection.generateGeneDirection();
        }
        return geneDirection;
    }

    public void move(){
        this.direction = this.direction.turn(this.dna[this.activeGene]);
        // here the active gene change should occur
        this.position = this.position.add(this.direction.toUnitVector());
    }

    private GeneDirection[] crossDna(Animal father){
        GeneDirection[] childDna = new GeneDirection[dnaLength];
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

        int strongerGene = (int) Math.ceil(stronger.energy / totalEnergy * dnaLength);
        int weakerGene = (int) Math.floor(weaker.energy / totalEnergy * dnaLength);

        int side = random.nextInt(2);

        if(side == 0){
            leftArr = Arrays.copyOfRange(stronger.dna, 0, strongerGene);
            rightArr = Arrays.copyOfRange(weaker.dna ,strongerGene, dnaLength);
        }else {
            rightArr = Arrays.copyOfRange(stronger.dna, weakerGene, dnaLength);
            leftArr = Arrays.copyOfRange(weaker.dna ,0, weakerGene);
        }

        System.arraycopy(leftArr, 0, childDna, 0, leftArr.length);
        System.arraycopy(rightArr, 0, childDna, leftArr.length, rightArr.length);

        return childDna;
    }

    // this method should not be called unless animals are on the same position
    public void makeChild(Animal father){
        this.updateEnergy(energyLossForChild);
        father.updateEnergy(energyLossForChild);
        GeneDirection[] childDna = this.crossDna(father);
        new Animal(childDna, this.position); // here should also be a map so the animal is placed
    }

    public void updateEnergy(int loss){
        this.energy = this.energy - loss;
    }

    public boolean isDead(){
        return this.energy == 0;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDirection(GeneDirection geneDirection){
        this.direction = geneDirection;
    }

    public GeneDirection getGeneDirection(){
        return direction;
    }

}
