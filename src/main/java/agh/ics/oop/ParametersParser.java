package agh.ics.oop;

public class ParametersParser {

    public int width;
    public int height;
    public int startAnimalCount;
    public int startPlantsCount;
    public int plantsEachDayCount;
    public int startEnergyForFactoryAnimals;
    public int dnaLength;
    public int energyLossForChild;
    public int plantsEnergy;
    public int energyRequiredToProcreate;
    public int dailyEnergyLoss;
    public int minMutationCount;
    public int maxMutationCount;

    public boolean mapTypeFlag; // True -> Globe
    public boolean gardenTypeFlag; // True -> Green Equator
    public boolean mutationTypeFlag; // True -> Total Randomness
    public boolean behaviorTypeFlag; // True -> Craziness

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setStartAnimalCount(int startAnimalCount) {
        this.startAnimalCount = startAnimalCount;
    }

    public void setStartPlantsCount(int startPlantsCount) {
        this.startPlantsCount = startPlantsCount;
    }

    public void setDailyEnergyLoss(int energyLossForEachDay) {
        this.dailyEnergyLoss = energyLossForEachDay;
    }

    public void setEnergyRequiredToProcreate(int energyRequiredToProcreate) {
        this.energyRequiredToProcreate = energyRequiredToProcreate;
    }

    public void setEnergyLossForChild(int energyLossForChild) {
        this.energyLossForChild = energyLossForChild;
    }

    public void setDnaLength(int dnaLength) {
        this.dnaLength = dnaLength;
    }

    public void setStartEnergyForFactoryAnimals(int startEnergyForFactoryAnimals) {
        this.startEnergyForFactoryAnimals = startEnergyForFactoryAnimals;
    }

    public void setPlantsEnergy(int plantsEnergy) {
        this.plantsEnergy = plantsEnergy;
    }

    public void setPlantsEachDayCount(int plantsEachDayCount) {
        this.plantsEachDayCount = plantsEachDayCount;
    }

    public void setMinMutationCount(int minMutations){
        this.minMutationCount = minMutations;
    }

    public void setMaxMutationCount(int maxMutations){
        this.maxMutationCount = maxMutations;
    }

    public void setMapTypeFlag(boolean mapType) {
        this.mapTypeFlag = mapType;
    }

    public void setGardenTypeFlag(boolean gardenType) {
        this.gardenTypeFlag = gardenType;
    }

    public void setMutationTypeFlag(boolean mutationType) {
        this.mutationTypeFlag = mutationType;
    }

    public void setBehaviorTypeFlag(boolean behaviorType) {
        this.behaviorTypeFlag = behaviorType;
    }


}
