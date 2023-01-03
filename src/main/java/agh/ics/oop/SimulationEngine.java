package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationEngine implements Runnable, IEngine{
    private int MOVE_DELAY = 150;
    private VariableManager manager;
    private WorldMap map;
    private final App app;
    private boolean isPaused;
    private final List<VBox> animalButtonsList = new ArrayList<>();
    private final List<Animal> animalsAssignedToButtons = new ArrayList<>();

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

    @Override
    public void addAnimalButton(VBox vBox, Animal animal) {
        this.animalButtonsList.add(vBox);
        this.animalsAssignedToButtons.add(animal);
    }

    @Override
    public void clearAnimalButtons() {
        this.animalButtonsList.clear();
        this.animalsAssignedToButtons.clear();
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

    @Override
    public void highlightDominantGenotypeAnimals(GeneDirection[] genotype) {
        for(int i = 0; i < animalButtonsList.size(); i++)
            if (Arrays.equals(animalsAssignedToButtons.get(i).getDna(), genotype))
                animalButtonsList.get(i).setStyle("-fx-border-color: #ff0000");
    }

}