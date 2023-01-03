package agh.ics.oop;

import javafx.scene.layout.VBox;

public interface IEngine {

    void run();

    void addAnimalButton(VBox vBox, Animal animal);

    void clearAnimalButtons();

    void highlightDominantGenotypeAnimals(GeneDirection[] genotype);

    boolean isPaused();
    void pauseEngine();

    void resumeEngine();

}
