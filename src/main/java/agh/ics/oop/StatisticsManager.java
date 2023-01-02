package agh.ics.oop;

import java.util.HashMap;
import java.util.Map;

public class StatisticsManager implements IMapObserver {
    private int simulationDayCount;
    private int animals;
    private int deadAnimals;
    private int plants;
    private int freeSpots;
    private int sumEnergy;
    private int sumDeadAnimalsLifespan;
    private int sumChildrenOfAliveAnimals;
    private HashMap<GeneDirection[], Integer> animalsDNA;
    private GeneDirection[] mostPopularDNA;

    public StatisticsManager(){
        this.simulationDayCount = -1;
        this.animals = 0;
        this.deadAnimals = 0;
        this.plants = 0;
        this.freeSpots = 0;
        this.sumEnergy = 0;
        this.sumDeadAnimalsLifespan = 0;
        this.animalsDNA = new HashMap<>();
        this.mostPopularDNA = new GeneDirection[]{GeneDirection.ZERO};
    }

    @Override
    public void newDay(){
        this.simulationDayCount += 1;
    }

    @Override
    public void animalBirth(Animal animal) {
        this.animals += 1;
        this.addDNA(animal.getDna());
        this.sumChildrenOfAliveAnimals += 2;
    }

    @Override
    public void animalDeath(Animal animal) {
        this.animals -= 1;
        this.deadAnimals += 1;
        this.removeDNA(animal.getDna());
        this.sumDeadAnimalsLifespan += animal.getAge();
        this.sumChildrenOfAliveAnimals -= animal.getChildrenCount();
    }

    @Override
    public void plantCountUpdate(boolean grow) {
        if(grow){
            this.plants += 1;
        }else {
            this.plants -= 1;
        }
    }

    @Override
    public void freeSpotsUpdate(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    @Override
    public void energyLevelsUpdate(int sumEnergy) {
        this.sumEnergy = sumEnergy;
    }

    @Override
    public void factoryMadeAnimal(int animalCount) {
        this.sumChildrenOfAliveAnimals -= (animalCount) * 2;
    }

    private void addDNA(GeneDirection[] dna) {
        if(animalsDNA.containsKey(dna)){
            animalsDNA.put(dna, animalsDNA.get(dna) + 1);
        }else {
            animalsDNA.put(dna, 1);
        }
    }

    private void removeDNA(GeneDirection[] dna) {
        if(animalsDNA.get(dna) == 1){
            animalsDNA.remove(dna);
        }else {
            animalsDNA.put(dna, animalsDNA.get(dna) - 1);
        }
    }

    @Override
    public int getAnimalCount() {
        return this.animals;
    }

    @Override
    public int getPlantsCount() {
        return this.plants;
    }

    @Override
    public int getFreeSpotsCount() {
        return this.freeSpots;
    }

    @Override
    public double getAverageEnergyLevel() {
        if (animals == 0) return 0;
        else {
            return (double)sumEnergy / animals;
        }
    }

    @Override
    public GeneDirection[] getMostPopularDNA() {
        int max_count = 0;

        for(Map.Entry<GeneDirection[], Integer> val : animalsDNA.entrySet())
        {
            if (max_count < val.getValue()) {
                mostPopularDNA = val.getKey();
                max_count = val.getValue();
            }
        }

        return mostPopularDNA;
    }

    @Override
    public double getAverageLifespan() {
        if(deadAnimals == 0) return 0;
        return (double)this.sumDeadAnimalsLifespan / this.deadAnimals;
    }

    @Override
    public double getAverageChildrenCount() {
        if(animals == 0) return 0;
        else {
            return (double) this.sumChildrenOfAliveAnimals / this.animals;
        }
    }

    @Override
    public int getDay() {
        return this.simulationDayCount;
    }


}
