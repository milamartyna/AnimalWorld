package agh.ics.oop;

public class VariableManager {

    private static IMapType mapType;
    private static IMutationType mutationType;
    private static IGardenType gardenType;

    public void setMapType(boolean flag){
        if(flag){
            mapType = new Globe();
        }
        else {
            mapType = new HellGate();
        }
    }

    public void setGardenType(boolean flag){
        if(flag){
            gardenType = new GreenEquator();
        }
        else{
            gardenType =  new ToxicFields();
        }
    }
}
