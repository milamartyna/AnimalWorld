package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static java.lang.Math.min;


public class App extends Application implements INextDayChange {
    private double GRID_SIZE = 400.0;
    private double width;
    private double height;
    private SimulationEngine engine;
    private WorldMap map;
    private ParametersParser parametersParser;
    private VariableManager manager;
    private GridPane worldGridPane;
    private double imageSize;

    @Override
    public void start(Stage primaryStage) {
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

            setParameters(sliders, choiceBoxes, variantChoices);

            this.manager = new VariableManager(parametersParser);
            this.map = new WorldMap(manager);
            this.drawScene(manager.energyRequiredToProcreate);
            VBox mapVBox = new VBox(worldGridPane);
            Scene mapScene = new Scene(mapVBox, 600, 600);
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

    private void parseVariantChoices(ChoiceBox[] choicesBox, String[][] variantChoices){
        this.parametersParser.setMapType(choicesBox[0].getValue() == variantChoices[0][0]);
        this.parametersParser.setGardenType(choicesBox[1].getValue() == variantChoices[1][0]);
        this.parametersParser.setBehaviorType(choicesBox[2].getValue() == variantChoices[2][0]);
        this.parametersParser.setMutationType(choicesBox[3].getValue() == variantChoices[3][0]);
    }

    private void setParameters(Slider[] sliders, ChoiceBox[] choiceBoxes, String[][] variantChoices){
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

    }

    public void nextDay() {
        Platform.runLater(this::setScene);
    }

    public void drawScene(int energyRequiredToProcreate){
        int minX = map.startMap.x();
        int minY = map.startMap.y();
        int maxX = map.endMap.x();
        int maxY = map.endMap.y();
        this.width = GRID_SIZE/(double) (map.endMap.x());
        this.height = GRID_SIZE/(double) (map.endMap.y());

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
                    imageSize = min(height, width);
                    Object objectOnMap = map.objectAt(position);
                    GuiElementBox guiElementBox = new GuiElementBox((IMapElement) objectOnMap, imageSize, energyRequiredToProcreate);
                    VBox vBox = guiElementBox.getVBox();
                    worldGridPane.add(vBox, position.x() - minX + 1, maxY - position.y() + 1);
                    GridPane.setHalignment(vBox, HPos.CENTER);
                }
            }
        }
    }

    public void setScene(){
        Platform.runLater(() -> {
            worldGridPane.getColumnConstraints().clear();
            worldGridPane.getRowConstraints().clear();
            worldGridPane.getChildren().clear();
            worldGridPane.setGridLinesVisible(false);
            worldGridPane.setGridLinesVisible(true);
            drawScene(manager.energyRequiredToProcreate);
        });
    }

}