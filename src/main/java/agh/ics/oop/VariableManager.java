package agh.ics.oop;

public class VariableManager {

    // the variable will parse the json(or sth like that) to get such information as what variant to use and
    // what are the constants like width and height of the map
    private int width = 10;
    private int height = 10;

    public final int startAnimalCount = 4;
    public final int startPlantsCount = 20;
    public final int plantsEachDayCount = 5;
    public final int startEnergyForFactoryAnimals = 20;
    public final int dnaLength = 9;
    public final int energyLossForChild = 5;
    public final int plantsEnergy = 2;
    public final int energyRequiredToProcreate = 7;
    // energyRequiredToProcreate >= energyLossForChild

    private IMapType mapType;
    private IMutationType mutationType;
    private IGardenType gardenType;
    private IBehaviorType behaviorType;

    // here I think there should be a passed an array of length 4 of bool values which corresponds
    // to which variable should be chosen
    public VariableManager(){
        setMapType(false);
        setGardenType(true);
        setMutationType(false);
        setBehaviorType(true);
    }

    private void setMapType(boolean flag){
        if(flag) {
            mapType = new Globe();
        }else {
            mapType = new HellGate();
        }
    }

    private void setGardenType(boolean flag){
        if(flag) {
            this.gardenType = new GreenEquator(width, height);
        }else {
            this.gardenType =  new ToxicFields(width, height);
        }
    }

    private void setMutationType(boolean flag){
        if(flag){
            this.mutationType = new TotalRandomness(dnaLength);
        }else {
            this.mutationType = new SlightCorrection(dnaLength);
        }
    }

    private void setBehaviorType(boolean flag){
        if(flag){
            this.behaviorType = new CompletePredestination(dnaLength);
        }else {
            this.behaviorType = new Craziness(dnaLength);
        }
    }

    public IMapType getMapType() {
        return mapType;
    }

    public IGardenType getGardenType() {
        return this.gardenType;
    }

    public IMutationType getMutationType(){
        return this.mutationType;
    }

    public IBehaviorType getBehaviorType() {
        return behaviorType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
