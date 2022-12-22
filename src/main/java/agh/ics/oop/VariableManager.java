package agh.ics.oop;

public class VariableManager {

    // the variable will parse the json(or sth like that) to get such information as what variant to use and
    // what are the constants like width and height of the map
    private int width = 10;
    private int height = 10;

    private static IMapType mapType;
    private static IMutationType mutationType;
    private static IGardenType gardenType;

    // here I think there should be a passed an array of length 4 of bool values which corresponds
    // to which variable should be chosen
    public VariableManager(){
        setMapType(true);
        setGardenType(true);
    }

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
            gardenType = new GreenEquator(width, height);
        }
        else{
            gardenType =  new ToxicFields();
        }
    }

    public static IGardenType getGardenType() {
        return gardenType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
