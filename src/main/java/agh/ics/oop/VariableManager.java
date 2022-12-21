package agh.ics.oop;

public class VariableManager {

    private static IMapType mapType;

    public VariableManager(){
        setMapType(true);
    }

    public void setMapType(boolean flag){
        if(flag){
            mapType = new Globe();
        }
        else {
            mapType = new HellGate();
        }
    }



}
