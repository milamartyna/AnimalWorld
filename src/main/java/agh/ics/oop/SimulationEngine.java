package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;

public class SimulationEngine implements Runnable{
    private int MOVE_DELAY = 50;
    private GridPane gridPane;
    private VariableManager manager;
    private WorldMap map;
    private final App app;

    public SimulationEngine(VariableManager manager, WorldMap map, App app){
        this.map = map;
        this.manager = manager;
        this.app = app;
    }

    //public void run(){
        // here should be whiled True
      //  for (int i = 0; i < 5; i++){
        //    map.nextDay();
        //    System.out.println(map);
      //  }
   // }

    public void run() {
        try {
            Thread.sleep(MOVE_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.map.nextDay();
        System.out.println(map);
        try {
            this.app.nextDay();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

}