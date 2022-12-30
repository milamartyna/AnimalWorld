package agh.ics.oop;

public class VariableManager {

    // we need to have getters, not public variables
    private int width;
    private int height;
    public int startAnimalCount;
    public int startPlantsCount;
    public int plantsEachDayCount;
    public int startEnergyForFactoryAnimals;
    public int dnaLength;
    public int energyLossForChild;
    public int plantsEnergy;
    public int energyRequiredToProcreate; // energyRequiredToProcreate >= energyLossForChild
    public int dailyEnergyLoss;

    private boolean mapTypeFlag; // True -> Globe
    private boolean gardenTypeFlag; // True -> Green Equator
    private boolean mutationTypeFlag; // True -> Total Randomness
    private boolean behaviorTypeFlag; // True -> Craziness

    private IMapType mapType;
    private IMutationType mutationType;
    private IGardenType gardenType;
    private IBehaviorType behaviorType;

    public ParametersParser parametersParser;

    public VariableManager(ParametersParser parametersParser){
        this.parametersParser = parametersParser;
        this.getParameters();

        setMapType(mapTypeFlag);
        setGardenType(gardenTypeFlag);
        setMutationType(mutationTypeFlag);
        setBehaviorType(behaviorTypeFlag);
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
            this.behaviorType = new Craziness(dnaLength);
        }else {
            this.behaviorType = new CompletePredestination(dnaLength);
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

    public void getParameters(){
        this.width = parametersParser.width;
        this.height = parametersParser.height;
        this.startAnimalCount = parametersParser.startAnimalCount;
        this.startPlantsCount = parametersParser.startPlantsCount;
        this.dailyEnergyLoss = parametersParser.dailyEnergyLoss;
        this.energyRequiredToProcreate = parametersParser.energyRequiredToProcreate;
        this.energyLossForChild = parametersParser.energyLossForChild;
        this.startEnergyForFactoryAnimals = parametersParser.startEnergyForFactoryAnimals;
        this.dnaLength = parametersParser.dnaLength;
        this.plantsEnergy = parametersParser.plantsEnergy;
        this.plantsEachDayCount = parametersParser.plantsEachDayCount;

        this.mapTypeFlag = parametersParser.mapType;
        this.gardenTypeFlag = parametersParser.gardenType;
        this.mutationTypeFlag = parametersParser.mutationType;
        this.behaviorTypeFlag = parametersParser.behaviorType;
    }
}
