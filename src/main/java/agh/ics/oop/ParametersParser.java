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
    public int energyRequiredToProcreate; // energyRequiredToProcreate >= energyLossForChild
    public int dailyEnergyLoss;

    public boolean mapType; // True -> Globe
    public boolean gardenType; // True -> Green Equator
    public boolean mutationType; // True -> Total Randomness
    public boolean behaviorType; // True -> Craziness

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

    public void setMapType(boolean mapType) {
        this.mapType = mapType;
    }

    public void setGardenType(boolean gardenType) {
        this.gardenType = gardenType;
    }

    public void setMutationType(boolean mutationType) {
        this.mutationType = mutationType;
    }

    public void setBehaviorType(boolean behaviorType) {
        this.behaviorType = behaviorType;
    }
}
