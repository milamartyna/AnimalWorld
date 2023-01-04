package agh.ics.oop;

import agh.ics.oop.gui.App;
import agh.ics.oop.gui.GuiElementBox;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

public class SimulationEngine implements IEngine{
    private static final double GRID_SIZE = 400.0;
    private static final int MOVE_DELAY = 300;
    private final VariableManager manager;
    private final WorldMap map;
    private final IMapObserver mapObserver;
    private final App app;
    private final GridPane worldGridPane;
    private final VBox statsVBox;
    private final VBox buttonStatsVBox;
    private final HBox mapWithStats;
    private final VBox mapVBox;
    private boolean isPaused;
    private final List<VBox> animalButtonsList = new ArrayList<>();
    private final List<Animal> animalsAssignedToButtons = new ArrayList<>();


    public SimulationEngine(VariableManager manager, WorldMap map, App app){
        this.map = map;
        this.mapObserver = map.getMapObserver();
        this.manager = manager;
        this.app = app;
        this.worldGridPane = new GridPane();
        this.statsVBox = new VBox();
        this.buttonStatsVBox = new VBox();
        this.mapWithStats = new HBox();
        this.mapVBox = new VBox(worldGridPane);
        this.isPaused = true;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(MOVE_DELAY);
                if(!this.isPaused){
                    this.map.nextDay();
                    this.setScene();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public HBox startSimulation(){
        this.drawScene();
        this.drawBeginScene();
        this.addAnimalInfoButton();
        mapWithStats.getChildren().addAll(mapVBox, buttonStatsVBox);
        mapWithStats.setAlignment(Pos.CENTER);
        mapWithStats.setSpacing(20);
        return mapWithStats;
    }

    private void setScene(){
        Platform.runLater(() -> {
            worldGridPane.getColumnConstraints().clear();
            worldGridPane.getRowConstraints().clear();
            worldGridPane.getChildren().clear();
            worldGridPane.setGridLinesVisible(false);
            worldGridPane.setGridLinesVisible(true);
            drawScene();
        });
    }

    private void drawScene() {
        this.updateStats(statsVBox);
        this.clearAnimalButtons();
        int minX = map.startMap.x();
        int minY = map.startMap.y();
        int maxX = map.endMap.x();
        int maxY = map.endMap.y();
        double width = GRID_SIZE / (double) (map.endMap.x());
        double height = GRID_SIZE / (double) (map.endMap.y());

        Label labelyx = new Label("y \\ x");
        worldGridPane.getColumnConstraints().add(new ColumnConstraints(width));
        worldGridPane.getRowConstraints().add(new RowConstraints(height));
        GridPane.setHalignment(labelyx, HPos.CENTER);
        worldGridPane.add(labelyx, 0, 0);

        for (int i = 1; i <= maxX - minX + 1; i++) {
            Label label = new Label(Integer.toString(minX + i - 1));
            worldGridPane.getColumnConstraints().add(new ColumnConstraints(width));
            GridPane.setHalignment(label, HPos.CENTER);
            worldGridPane.add(label, i, 0);
        }

        for (int i = 1; i <= maxY - minY + 1; i++) {
            Label label = new Label(Integer.toString(maxY - i + 1));
            worldGridPane.getRowConstraints().add(new RowConstraints(height));
            GridPane.setHalignment(label, HPos.CENTER);
            worldGridPane.add(label, 0, i);
        }
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Vector2d position = new Vector2d(x, y);
                if (map.isOccupied(position)) {
                    double imageSize = min(height, width);
                    Object objectOnMap = map.objectAt(position);
                    GuiElementBox guiElementBox = new GuiElementBox((IMapElement) objectOnMap, imageSize, manager.energyRequiredToProcreate, objectOnMap, this);
                    VBox vBox = guiElementBox.getVBox();
                    if (objectOnMap.getClass().equals(Animal.class)) {
                        this.addAnimalButton(vBox, (Animal) objectOnMap);
                    }
                    worldGridPane.add(vBox, position.x() - minX + 1, maxY - position.y() + 1);
                    GridPane.setHalignment(vBox, HPos.CENTER);
                }
            }
        }
    }

    private void drawBeginScene(){
        worldGridPane.setGridLinesVisible(true);
        statsVBox.setAlignment(Pos.CENTER);
        statsVBox.setSpacing(5);
        statsVBox.setAlignment(Pos.CENTER);
        Button startResumeButton = new Button("START");

        startResumeButton.setOnAction(event -> {
            if (this.isPaused()){
                this.resumeEngine();
                startResumeButton.setText("PAUSE");
            }else {
                this.pauseEngine();
                startResumeButton.setText("RESUME");
            }
        });
        this.updateStats(statsVBox);
        buttonStatsVBox.getChildren().addAll(startResumeButton, statsVBox);
    }

    private void updateStats(VBox statsVBox){
        statsVBox.getChildren().clear();
        Label[] stats = new Label[8];

        stats[0] = new Label("Day: " + this.mapObserver.getDay());
        stats[1] = new Label("Animals On The Map: " + this.mapObserver.getAnimalCount());
        stats[2] = new Label("Plants On The Map: " + this.mapObserver.getPlantsCount());
        stats[3] = new Label("Free Spots On The Map: " + this.mapObserver.getFreeSpotsCount());
        stats[4] = new Label("Most Popular DNA: " + Arrays.toString(this.mapObserver.getMostPopularDNA()));
        stats[5] = new Label("Average Energy Level: " + String.format("%.2f", this.mapObserver.getAverageEnergyLevel()));
        stats[6] = new Label("Average Lifespan: " + String.format("%.2f", this.mapObserver.getAverageLifespan()));
        stats[7] = new Label("Average Children Per Animal: " + String.format("%.2f",this.mapObserver.getAverageChildrenCount()));

        for(int i = 0; i < 8; i++) {
            statsVBox.getChildren().add(stats[i]);
        }
    }

    private void addAnimalInfoButton() {
        Button button = new Button("Dominant");
        button.setOnAction(event -> {
            if (this.isPaused()){
                this.highlightDominantGenotypeAnimals(this.mapObserver.getMostPopularDNA());
            }
        });
        buttonStatsVBox.setSpacing(5);
        buttonStatsVBox.getChildren().add(button);
    }

    private void addAnimalButton(VBox vBox, Animal animal) {
        this.animalButtonsList.add(vBox);
        this.animalsAssignedToButtons.add(animal);
    }

    private void clearAnimalButtons() {
        this.animalButtonsList.clear();
        this.animalsAssignedToButtons.clear();
    }

    private void highlightDominantGenotypeAnimals(GeneDirection[] genotype) {
        for(int i = 0; i < animalButtonsList.size(); i++)
            if (Arrays.equals(animalsAssignedToButtons.get(i).getDna(), genotype))
                animalButtonsList.get(i).setStyle("-fx-border-color: #ff0000");
    }

    private void pauseEngine(){
        this.isPaused = true;
    }

    public void resumeEngine(){
        this.isPaused = false;
    }

}