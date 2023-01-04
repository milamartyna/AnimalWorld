package agh.ics.oop;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public interface IEngine extends Runnable{

    void run();
    HBox startSimulation();
    boolean isPaused();

}
