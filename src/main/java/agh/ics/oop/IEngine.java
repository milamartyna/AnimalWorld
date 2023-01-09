package agh.ics.oop;

import javafx.scene.layout.HBox;
import java.io.IOException;

public interface IEngine extends Runnable{

    void run();
    HBox setUpSimulation() throws IOException;
    boolean isPaused();
    void saveStats(int index) throws IOException;
    void startTracking(Animal animal);

}
