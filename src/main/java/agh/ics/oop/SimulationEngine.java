package agh.ics.oop;

import agh.ics.oop.gui.GuiElementBox;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

public class SimulationEngine implements IEngine{
    private static final double GRID_SIZE = 400.0;
    private static final int MOVE_DELAY = 300;
    private boolean isTracked = false;
    private boolean isDead = false;
    private final VariableManager manager;
    private final WorldMap map;
    private final IMapObserver mapObserver;
    private final GridPane worldGridPane;
    private final VBox statsVBox;
    private final VBox buttonStatsVBox;
    private final VBox infoVBox;
    private final HBox mapWithStats;
    private final VBox mapVBox;
    private boolean saveStats;
    private File csvStatsFile;
    private boolean isPaused;
    private final List<VBox> animalButtonsList = new ArrayList<>();
    private final List<Animal> animalsAssignedToButtons = new ArrayList<>();
    private Animal trackedAnimal;

    public SimulationEngine(VariableManager manager, WorldMap map){
        this.map = map;
        this.mapObserver = map.getMapObserver();
        this.manager = manager;
        this.worldGridPane = new GridPane();
        this.statsVBox = new VBox();
        this.saveStats = false;
        this.buttonStatsVBox = new VBox();
        this.mapWithStats = new HBox();
        this.mapVBox = new VBox(worldGridPane);
        this.isPaused = true;
        this.infoVBox = new VBox();
        this.trackedAnimal = null;
    }

    @Override
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

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void saveStats(int index) throws IOException {
        this.saveStats = true;
        this.csvStatsFile = new File("StatsSimulation" + index + ".csv");
        FileWriter fileWriter = new FileWriter(csvStatsFile);
        fileWriter.append("Day;Animals;Plants;FreeSpots;DNA;AvgEnergy;AvgLifespan;AvgKidsCount");
        fileWriter.close();
    }

    @Override
    public HBox setUpSimulation() throws IOException {
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
            try {
                drawScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void drawScene() throws IOException {
        this.updateStats(statsVBox);
        if(isTracked && trackedAnimal != null && !isDead){this.trackingInfo(infoVBox); }
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

    private void drawBeginScene() throws IOException {
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
//        this.updateStats(statsVBox);
        buttonStatsVBox.getChildren().addAll(startResumeButton, statsVBox, infoVBox);
    }

    private void updateStats(VBox statsVBox) throws IOException {
        statsVBox.getChildren().clear();
        Label[] stats = new Label[8];

        String day = String.valueOf(this.mapObserver.getDay());
        String animals = String.valueOf(this.mapObserver.getAnimalCount());
        String plants = String.valueOf(this.mapObserver.getPlantsCount());
        String freeSpots = String.valueOf(this.mapObserver.getFreeSpotsCount());
        String dna = Arrays.toString(this.mapObserver.getMostPopularDNA());
        String energy = String.format("%.2f", this.mapObserver.getAverageEnergyLevel());
        String lifespan = String.format("%.2f", this.mapObserver.getAverageLifespan());
        String children = String.format("%.2f",this.mapObserver.getAverageChildrenCount());

        stats[0] = new Label("Day: " + day);
        stats[1] = new Label("Animals On The Map: " + animals);
        stats[2] = new Label("Plants On The Map: " + plants);
        stats[3] = new Label("Free Spots On The Map: " + freeSpots);
        stats[4] = new Label("Most Popular DNA: " + dna);
        stats[5] = new Label("Average Energy Level: " + energy);
        stats[6] = new Label("Average Lifespan: " + lifespan);
        stats[7] = new Label("Average Children Per Animal: " + children);

        for(int i = 0; i < 8; i++) {
            statsVBox.getChildren().add(stats[i]);
        }

        if(this.saveStats) {
            String dailyStats = day + ';' + animals + ';' + plants + ';' + freeSpots + ';' + dna + ';' + energy
                    + ';' + lifespan + ';' + children;
            FileWriter fileWriter = new FileWriter(csvStatsFile, true);
            fileWriter.append("\n");
            fileWriter.append(dailyStats);
            fileWriter.close();
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

    private void pauseEngine() {
        this.isPaused = true;
        this.isTracked = false;
        this.trackedAnimal = null;
    }

    public void resumeEngine(){
        this.isPaused = false;
        this.isTracked = true;
        infoVBox.getChildren().clear();
    }

    public void setTrackedAnimal(Animal animal) {
        this.trackedAnimal = animal;
    }

    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public void startTracking(Animal animal){
        setTrackedAnimal(animal);
        isTracked = true;
        isDead = false;
    }

    public void trackingInfo(VBox infoVBox) {
        infoVBox.getChildren().clear();
        infoVBox.getChildren().add(new Label("Tracked Animal informations:"));
        Animal animal = getTrackedAnimal();
        Label[] information = new Label[7];
        information[0] = new Label("DNA: " + Arrays.toString(animal.getDna()));
        information[1] = new Label("Active Gene: " + animal.getActiveGene());
        information[2] = new Label("Energy: " + animal.getEnergy());
        information[3] = new Label("Number of eaten Plants: " + animal.getEatenPlants());
        information[4] = new Label("Number of Children: " + animal.getChildrenCount());
        information[5] = new Label("Age: " + animal.getAge());
        if(animal.isDead()){
            information[6] = new Label("Death day: " + this.mapObserver.getDay());
            isDead = true;
        }else{
            information[6] = new Label("Death day: Still alive");
        }
        for (int i = 0; i < 7; i++) {
            infoVBox.getChildren().add(information[i]);
        }
    }

}