package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;


public class App extends Application {
    private double GRID_SIZE = 480.0;
    private double CELL_WIDTH;
    private double CELL_HEIGHT;
    private SimulationEngine engine;
    private WorldMap map;
    private ParametersParser parametersParser;
    private VariableManager manager;
    private HBox world;
    private boolean parametersAccepted;
    private  boolean mapsAndEnginesCreated;
    private GridPane worldGridPane;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        this.worldGridPane = new GridPane();
        this.parametersParser = new ParametersParser();
        getParametersFromUser(primaryStage);
    }

    public void getParametersFromUser(Stage primaryStage)
    {
        Button acceptButton = new Button("Accept Button");

        VBox vBoxCheckBoxes = new VBox();
        Slider[] sliders = new Slider[11];
        ChoiceBox[] choiceBoxes = new ChoiceBox[4];
        String[][] variantChoices = {
                {"Globe", "Hell Gate", "Map Type"},
                {"Green Equator", "Toxic Fields", "Garden Type"},
                {"CompletePredestination", "Craziness", "Behaviour Type"},
                {"Total Randomness", "Slight Correction", "Mutation Type"}
        };

        HBox configPanel = this.createArgumentGetter(sliders, choiceBoxes, variantChoices, vBoxCheckBoxes);
        vBoxCheckBoxes.getChildren().add(acceptButton);
        configPanel.getChildren().add(vBoxCheckBoxes);

        Scene scene = new Scene(configPanel, 1000, 480);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Animal World");
        primaryStage.show();

        acceptButton.setOnAction(e -> {
            this.parametersParser.setWidth((int) sliders[0].getValue());
            this.parametersParser.setHeight((int) sliders[1].getValue());
            this.parametersParser.setStartAnimalCount((int) sliders[2].getValue());
            this.parametersParser.setStartPlantsCount((int) sliders[3].getValue());
            this.parametersParser.setDailyEnergyLoss((int) sliders[4].getValue());
            this.parametersParser.setEnergyRequiredToProcreate((int) sliders[5].getValue());
            this.parametersParser.setEnergyLossForChild((int) sliders[6].getValue());
            this.parametersParser.setStartEnergyForFactoryAnimals((int) sliders[7].getValue());
            this.parametersParser.setDnaLength((int) sliders[8].getValue());
            this.parametersParser.setPlantsEnergy((int) sliders[9].getValue());
            this.parametersParser.setPlantsEachDayCount((int) sliders[10].getValue());

            this.parseVariantChoices(choiceBoxes, variantChoices);

            this.manager = new VariableManager(parametersParser);
            this.map = new WorldMap(manager);

            this.createScene();
            Scene mapScene = new Scene(worldGridPane, 1000, 480);
            primaryStage.setScene(mapScene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Animal Map");
            primaryStage.show();

            this.engine = new SimulationEngine(manager, map, this);
            Thread simulation = new Thread(engine);
            simulation.start();

        });


    }

    private HBox createArgumentGetter(Slider[] sliders, ChoiceBox[] choiceBoxes, String[][] variantChoices, VBox vBoxCheckBoxes){
        HBox hBox = new HBox();
        hBox.setSpacing(50);
        hBox.setAlignment(Pos.BASELINE_CENTER);

        int[][] numericParameters = {
            // min, max, default Value, Major Tick
            {5, 30, 10, 5}, // width
            {5, 30, 10, 5}, // height
            {0, 100, 5, 20}, // amountOfAnimals
            {0, 100, 5, 20}, // amountOfPlants
            {1, 10, 3, 1}, // dailyEnergyLoss
            {5, 50, 30, 10}, // energyRequiredToProcreate
            {5, 50, 30, 10}, // energyLossForChildren
            {10, 100, 20, 20}, // startEnergy
            {4, 20, 7, 2}, // DNALength
            {10, 100, 10, 20}, // plantEnergy
            {5, 50, 5, 10} // plantsEachDay

        };

        String[] stringParameters = {
                "Width",
                "Height",
                "Amount Of Animals",
                "Amount Of Plants",
                "Daily Energy Loss",
                "Energy Required To Procreate",
                "Energy Loss For Children",
                "Start Energy",
                "DNA Length",
                "Plant's Energy",
                "Number Of New Plants Each Day"
        };

        VBox vBoxLeft = new VBox();
        vBoxLeft.setSpacing(3);
        vBoxLeft.setAlignment(Pos.CENTER);

        for(int i = 0; i < numericParameters.length / 2; i++) {
            this.addLabelAndSliderToSideVBox(sliders, i, vBoxLeft, numericParameters[i], stringParameters[i]);
        }


        VBox vBoxRight = new VBox();
        vBoxRight.setSpacing(3);
        vBoxRight.setAlignment(Pos.CENTER);
        for(int i = numericParameters.length / 2; i < numericParameters.length; i++) {
            this.addLabelAndSliderToSideVBox(sliders, i, vBoxRight, numericParameters[i], stringParameters[i]);
        }

        vBoxCheckBoxes.setSpacing(3);
        vBoxCheckBoxes.setAlignment(Pos.CENTER);

        for(int i = 0; i < variantChoices.length; i++){
            this.addLabelAndCheckBox(choiceBoxes, i, vBoxCheckBoxes, variantChoices[i]);
        }
        hBox.getChildren().addAll(vBoxLeft, vBoxRight);

//        this.addAcceptButton(vBoxCheckBoxes, sliders, choiceBoxes, variantChoices);
        return hBox;
    }

    private void addLabelAndSliderToSideVBox(Slider[] sliders, int index, VBox vBox, int[] parameters, String title)
    {
        Label label = new Label(title);
        int min = parameters[0];
        int max = parameters[1];
        int defaultValue = parameters[2];
        int majorTickUnit = parameters[3];

        Slider slider = new Slider();

        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(defaultValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(majorTickUnit);
        slider.setMinorTickCount(majorTickUnit - 1);
        slider.setAccessibleText(title);
        slider.setSnapToTicks(true);

        slider.setPrefWidth(300);
        slider.setPrefHeight(50);

        sliders[index] = slider;

        VBox.setMargin(slider, new Insets(0, 10, 0, 10));

        vBox.getChildren().add(label);
        vBox.getChildren().add(slider);
    }

    private void addLabelAndCheckBox(ChoiceBox[] choiceBoxes, int index, VBox vBox, String[] variantChoices){
        Label label = new Label(variantChoices[2]);
        ChoiceBox<String> variant = new ChoiceBox<>();
        variant.setItems(FXCollections.observableArrayList(variantChoices[0], variantChoices[1]));
        choiceBoxes[index] = variant;
        VBox.setMargin(variant, new Insets(0, 10, 5, 10));
        vBox.getChildren().add(label);
        vBox.getChildren().add(variant);
    }

    private void addAcceptButton(VBox vBox, Slider[] sliders, ChoiceBox[] choiceBoxes, String[][] variantChoices)
    {
        Button button = new Button("Accept parameters");
        button.setOnAction(e -> {
            this.parametersParser.setWidth((int) sliders[0].getValue());
            this.parametersParser.setHeight((int) sliders[1].getValue());
            this.parametersParser.setStartAnimalCount((int) sliders[2].getValue());
            this.parametersParser.setStartPlantsCount((int) sliders[3].getValue());
            this.parametersParser.setDailyEnergyLoss((int) sliders[4].getValue());
            this.parametersParser.setEnergyRequiredToProcreate((int) sliders[5].getValue());
            this.parametersParser.setEnergyLossForChild((int) sliders[6].getValue());
            this.parametersParser.setStartEnergyForFactoryAnimals((int) sliders[7].getValue());
            this.parametersParser.setDnaLength((int) sliders[8].getValue());
            this.parametersParser.setPlantsEnergy((int) sliders[9].getValue());
            this.parametersParser.setPlantsEachDayCount((int) sliders[10].getValue());

            this.parseVariantChoices(choiceBoxes, variantChoices);

            this.manager = new VariableManager(parametersParser);
            this.map = new WorldMap(manager);
            this.engine = new SimulationEngine(manager, map, this);
            Thread simulation = new Thread(engine);
            simulation.start();

        });

        vBox.getChildren().add(button);
    }

    public void nextDay() throws FileNotFoundException {
        Platform.runLater(() -> {
            this.worldGridPane.getChildren().clear();
            this.worldGridPane.getColumnConstraints().clear();
            this.worldGridPane.getRowConstraints().clear();
            this.worldGridPane.setGridLinesVisible(false);

            this.createScene();

        });

    }

    private void createScene() {
        this.createAndAddAxisLabels(worldGridPane);
        this.createAndAddElements(worldGridPane, map, this);
        this.setColRowSizes(worldGridPane);
        this.worldGridPane.setPadding(new Insets(10, 10, 10, 10));
        this.worldGridPane.setGridLinesVisible(true);

    }

    private void parseVariantChoices(ChoiceBox[] choicesBox, String[][] variantChoices){
        this.parametersParser.setMapType(choicesBox[0].getValue() == variantChoices[0][0]);
        this.parametersParser.setGardenType(choicesBox[1].getValue() == variantChoices[1][0]);
        this.parametersParser.setBehaviorType(choicesBox[2].getValue() == variantChoices[2][0]);
        this.parametersParser.setMutationType(choicesBox[3].getValue() == variantChoices[3][0]);
    }

    public void makeMapStage(Stage welcomeStage) throws InterruptedException {

        synchronized (this) {
            while (!parametersAccepted)
                wait();
        }

        createMapsAndEngine();

        // creating necessary gui elements for maps
        Platform.runLater(() -> {
            Stage mapStage = createGuiMaps(welcomeStage);

            // hide previous window, show the new one
            welcomeStage.hide();
            mapStage.show();

            // center window on screen
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            mapStage.setX((primScreenBounds.getWidth() - mapStage.getWidth()) / 2);
            mapStage.setY((primScreenBounds.getHeight() - mapStage.getHeight()) / 2);
        });

        synchronized (this)
        {
            mapsAndEnginesCreated = true;
            notifyAll();
        }
        
    }

    public void createMapsAndEngine()
    {
        this.map = new WorldMap(manager);
        this.engine = new SimulationEngine(manager, map, this);
        this.CELL_WIDTH = GRID_SIZE/(double) (map.endMap.x()+1);
        this.CELL_HEIGHT = GRID_SIZE/(double) (map.endMap.y()+1);
    }

    public Stage createGuiMaps(Stage welcomeStage)
    {
        worldGridPane.setMaxHeight(GRID_SIZE);
        worldGridPane.setMinHeight(GRID_SIZE);
        worldGridPane.setMaxWidth(GRID_SIZE);
        worldGridPane.setMinWidth(GRID_SIZE);

        // adding references to GridPanes to engines (to let them modify them)
        this.engine.setGridPane(worldGridPane);
        this.world = new HBox(worldGridPane);

        VBox.setMargin(world, new Insets(10));
        createAndAddAxisLabels(worldGridPane);
        setColRowSizes(worldGridPane);
        createAndAddElements(worldGridPane,map, this);
        worldGridPane.setGridLinesVisible(true);

        // creating new scene and stage
        Scene scene = new Scene(world, 700, 700);
        Stage mapStage = new Stage();
        mapStage.setScene(scene);

        return mapStage;
    }

    public void createAndAddAxisLabels(GridPane grid) {

        for(int i = 0; i < map.endMap.y(); i++)
        {
            Label label = new Label(Integer.toString(map.endMap.y() - i));
            GridPane.setHalignment(label, HPos.CENTER);
            grid.add(label, 0, i + 1);
        }
        for(int i = 0; i < map.endMap.x(); i++)
        {
            Label label = new Label(Integer.toString(i + map.startMap.x()));
            GridPane.setHalignment(label, HPos.CENTER);
            grid.add(label, i + 1, 0);
        }
    }

    private void setColRowSizes(GridPane grid)
    {
        //setting columns' sizes
        for(int i = 0; i < map.endMap.x() + 1; i++)
        {
            ColumnConstraints column = new ColumnConstraints();
            column.setMinWidth(CELL_WIDTH);
            column.setMaxWidth(CELL_WIDTH);
            grid.getColumnConstraints().add(column);
        }

        // setting rows' sizes
        for(int i = 0; i < map.endMap.y() + 1; i++)
        {
            RowConstraints row = new RowConstraints();
            row.setMaxHeight(CELL_HEIGHT);
            row.setMinHeight(CELL_HEIGHT);
            grid.getRowConstraints().add(row);
        }
    }

    public void createAndAddElements(GridPane grid, WorldMap map, App app) {
        // filling map
        for(int i = 0; i < map.endMap.x(); i++) {
            for (int j = 0; j < map.endMap.y(); j++) {
                Vector2d position = new Vector2d(i, j);
                Object obj = map.objectAt(position);
                if (map.isOccupied(position)) {
                    Object objectOnMap = map.objectAt(position);
                    GuiElementBox guiElementBox = new GuiElementBox((IMapElement) objectOnMap);
                    VBox vBox = guiElementBox.getVBox();
                    grid.add(vBox, position.x() - map.startMap.x() + 1, map.endMap.y() - position.y() + 1);
                    GridPane.setHalignment(vBox, HPos.CENTER);
                }
            }
        }
    }

    private void startEngines() {
        synchronized (this)
        {
            while (!mapsAndEnginesCreated) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Thread boundedThread = new Thread(() -> {
            try {
                while (true) {
                    engine.run();
                }
            } catch(IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        });

        boundedThread.setDaemon(true);
        boundedThread.start();

    }

}