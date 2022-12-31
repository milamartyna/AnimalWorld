package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.scene.layout.GridPane;

public class SimulationEngine implements Runnable{
    private int MOVE_DELAY = 50;
    private VariableManager manager;
    private WorldMap map;
    private final App app;

    public SimulationEngine(VariableManager manager, WorldMap map, App app){
        this.map = map;
        this.manager = manager;
        this.app = app;
    }

    public void run() {
        try {
            Thread.sleep(MOVE_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.map.nextDay();
        app.setScene();
        System.out.println(map);
    }

}