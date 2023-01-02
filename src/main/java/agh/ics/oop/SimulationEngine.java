package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.scene.layout.GridPane;

public class SimulationEngine implements Runnable{
    private int MOVE_DELAY = 150;
    private VariableManager manager;
    private WorldMap map;
    private final App app;
    private boolean isPaused;

    public SimulationEngine(VariableManager manager, WorldMap map, App app){
        this.map = map;
        this.manager = manager;
        this.app = app;
        this.isPaused = true;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(MOVE_DELAY);
                if(!this.isPaused){
                    this.map.nextDay();
                    app.setScene();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void pauseEngine(){
        this.isPaused = true;
    }

    public void resumeEngine(){
        this.isPaused = false;
    }
}