package agh.ics.oop;

import javafx.scene.layout.HBox;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IEngine extends Runnable{

    void run();
    HBox startSimulation() throws IOException;
    boolean isPaused();
    void saveStats(int index) throws IOException;

}
