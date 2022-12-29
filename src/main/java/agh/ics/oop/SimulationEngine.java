package agh.ics.oop;

public class SimulationEngine {

    private VariableManager manager;
    private WorldMap map;

    public SimulationEngine(VariableManager manager, WorldMap map){
        this.map = map;
        this.manager = manager;
    }

    public void run(){
        // here should be whiled True
        for (int i = 0; i < 20; i++){
            map.nextDay();
            System.out.println(map);
        }

    }

}