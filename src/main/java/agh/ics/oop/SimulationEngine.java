package agh.ics.oop;

import javafx.scene.layout.GridPane;

public class SimulationEngine {
    private GridPane gridPane;
    private VariableManager manager;
    private WorldMap map;

    public SimulationEngine(VariableManager manager, WorldMap map){
        this.map = map;
        this.manager = manager;
    }

    public void run(){
        // here should be whiled True
        for (int i = 0; i < 5; i++){
            map.nextDay();
            System.out.println(map);
        }

    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }
}