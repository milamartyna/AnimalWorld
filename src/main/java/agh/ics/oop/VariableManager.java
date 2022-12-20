package agh.ics.oop;

public class VariableManager {

    private static IMapType mapType;
    private static IMutationType mutationType;

    public void setMapType(boolean flag){
        if(flag){
            mapType = new Globe();
        }
        else {
            mapType = new HellGate();
        }
    }



}
