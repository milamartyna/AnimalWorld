package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.controlsfx.control.RangeSlider;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class App extends Application {
    private ParametersParser parametersParser;
    private Properties prop;
    private int simulationCount = 0;
    private Slider[] sliders;
    private RangeSlider mutationCountRange;
    private ChoiceBox[] choiceBoxes;
    private CheckBox saveStatsCheckBox;

    @Override
    public void start(Stage primaryStage) {
        this.parametersParser = new ParametersParser();
        this.prop = new Properties();
        whichConfigOption(primaryStage);
    }

    private void whichConfigOption(Stage primaryStage){
        String[] options = {"Config 1", "Config 2", "Personal Config"};
        Label label = new Label("Configuration Options");
        ChoiceBox configOptions = new ChoiceBox<>(FXCollections.observableArrayList(options));
        Button confirmButton = new Button("Confirm");
        VBox configOptionPanel = new VBox();

        configOptionPanel.setSpacing(10);
        configOptionPanel.setAlignment(Pos.CENTER);
        configOptionPanel.getChildren().addAll(label, configOptions, confirmButton);

        Scene scene = new Scene(configOptionPanel, 1000, 480);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Animal World");
        primaryStage.show();

        confirmButton.setOnAction(e -> {
            if(configOptions.getValue() == options[options.length - 1]){
                startSimulationFromUser(primaryStage);
            }else {
                for(int i = 0; i < options.length - 1; i++){
                    if(configOptions.getValue() == options[i]){
                        this.startSimulationFromConfigFile(i + 1);
                    }
                }
            }
        });

    }

    private void startSimulationFromUser(Stage primaryStage)
    {
        Button acceptButton = new Button("Accept Button");

        VBox vBoxCheckBoxes = new VBox();
        sliders = new Slider[11];
        mutationCountRange = new RangeSlider(0, 20, 3, 7);
        choiceBoxes = new ChoiceBox[4];
        saveStatsCheckBox = new CheckBox("Save Daily Statistics");
        String[][] variantChoices = {
                {"Globe", "Hell Gate", "Map Type"},
                {"Green Equator", "Toxic Fields", "Garden Type"},
                {"CompletePredestination", "Craziness", "Behaviour Type"},
                {"Total Randomness", "Slight Correction", "Mutation Type"}
        };

        HBox configPanel = this.createArgumentGetter(variantChoices, vBoxCheckBoxes);
        vBoxCheckBoxes.getChildren().addAll(saveStatsCheckBox, acceptButton);
        configPanel.getChildren().add(vBoxCheckBoxes);

        Scene scene = new Scene(configPanel, 1000, 480);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Animal World");
        primaryStage.show();

        acceptButton.setOnAction(e -> {

            setUserParameters(variantChoices);
            VariableManager manager = new VariableManager(parametersParser);
            WorldMap map = new WorldMap(manager);
            IEngine engine = new SimulationEngine(manager, map);

            if(saveStatsCheckBox.isSelected()){
                simulationCount++;
                try {
                    engine.saveStats(simulationCount);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            this.startSimulation(engine);

        });
    }

    private void startSimulationFromConfigFile(int i){
        try {
            prop.load(new FileInputStream("src/main/resources/config" + i + ".properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        boolean saveStatsFlag = this.setFileParameters();

        VariableManager manager = new VariableManager(parametersParser);
        WorldMap map = new WorldMap(manager);
        IEngine engine = new SimulationEngine(manager, map);

        if(saveStatsFlag){
            simulationCount++;
            try {
                engine.saveStats(simulationCount);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        this.startSimulation(engine);

    }

    private void startSimulation(IEngine engine){
        Stage worldSimulation = new Stage();
        HBox mapWithStats;
        try {
            mapWithStats = engine.setUpSimulation();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Scene mapScene = new Scene(mapWithStats, 800, 600);
        worldSimulation.setScene(mapScene);
        worldSimulation.setResizable(false);
        worldSimulation.setTitle("Animal Map");
        worldSimulation.show();

        Thread simulation = new Thread(engine);
        simulation.setDaemon(true);
        simulation.start();
    }

    private HBox createArgumentGetter(String[][] variantChoices, VBox vBoxCheckBoxes){
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
            this.addLabelAndSliderToSideVBox(i, vBoxLeft, numericParameters[i], stringParameters[i]);
        }
        this.addMutationRangeSlider(vBoxLeft);

        VBox vBoxRight = new VBox();
        vBoxRight.setSpacing(3);
        vBoxRight.setAlignment(Pos.CENTER);
        for(int i = numericParameters.length / 2; i < numericParameters.length; i++) {
            this.addLabelAndSliderToSideVBox(i, vBoxRight, numericParameters[i], stringParameters[i]);
        }

        vBoxCheckBoxes.setSpacing(3);
        vBoxCheckBoxes.setAlignment(Pos.CENTER);

        for(int i = 0; i < variantChoices.length; i++){
            this.addLabelAndCheckBox(i, vBoxCheckBoxes, variantChoices[i]);
        }
        hBox.getChildren().addAll(vBoxLeft, vBoxRight);

        return hBox;
    }

    private void addLabelAndSliderToSideVBox(int index, VBox vBox, int[] parameters, String title)
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

    private void addLabelAndCheckBox(int index, VBox vBox, String[] variantChoices){
        Label label = new Label(variantChoices[2]);
        ChoiceBox<String> variant = new ChoiceBox<>();
        variant.setItems(FXCollections.observableArrayList(variantChoices[0], variantChoices[1]));
        choiceBoxes[index] = variant;
        VBox.setMargin(variant, new Insets(0, 10, 5, 10));
        vBox.getChildren().add(label);
        vBox.getChildren().add(variant);
    }

    private void setUserParameters(String[][] variantChoices){
        this.parametersParser.setWidth((int) sliders[0].getValue());
        this.parametersParser.setHeight((int) sliders[1].getValue());
        this.parametersParser.setStartAnimalCount((int) sliders[2].getValue());
        this.parametersParser.setStartPlantsCount((int) sliders[3].getValue());
        this.parametersParser.setDailyEnergyLoss((int) sliders[4].getValue());
        this.parametersParser.setMinMutationCount((int) mutationCountRange.getLowValue());
        this.parametersParser.setMaxMutationCount((int) mutationCountRange.getHighValue());
        this.parametersParser.setEnergyRequiredToProcreate((int) sliders[5].getValue());
        this.parametersParser.setEnergyLossForChild((int) sliders[6].getValue());
        this.parametersParser.setStartEnergyForFactoryAnimals((int) sliders[7].getValue());
        this.parametersParser.setDnaLength((int) sliders[8].getValue());
        this.parametersParser.setPlantsEnergy((int) sliders[9].getValue());
        this.parametersParser.setPlantsEachDayCount((int) sliders[10].getValue());

        this.parseVariantChoices(variantChoices);

    }

    private void parseVariantChoices(String[][] variantChoices){
        this.parametersParser.setMapTypeFlag(choiceBoxes[0].getValue() == variantChoices[0][0]);
        this.parametersParser.setGardenTypeFlag(choiceBoxes[1].getValue() == variantChoices[1][0]);
        this.parametersParser.setBehaviorTypeFlag(choiceBoxes[2].getValue() == variantChoices[2][0]);
        this.parametersParser.setMutationTypeFlag(choiceBoxes[3].getValue() == variantChoices[3][0]);
    }

    private boolean setFileParameters(){
        this.parametersParser.setWidth((Integer.parseInt(prop.getProperty("width"))));
        System.out.println((Integer.parseInt(prop.getProperty("width"))));
        this.parametersParser.setHeight((Integer.parseInt(prop.getProperty("height"))));
        System.out.println((Integer.parseInt(prop.getProperty("height"))));
        this.parametersParser.setStartAnimalCount((Integer.parseInt(prop.getProperty("startAnimalCount"))));
        this.parametersParser.setStartPlantsCount((Integer.parseInt(prop.getProperty("startPlantsCount"))));
        this.parametersParser.setDailyEnergyLoss((Integer.parseInt(prop.getProperty("dailyEnergyLoss"))));
        this.parametersParser.setMinMutationCount((Integer.parseInt(prop.getProperty("minMutationCount"))));
        this.parametersParser.setMaxMutationCount((Integer.parseInt(prop.getProperty("maxMutationCount"))));
        this.parametersParser.setEnergyRequiredToProcreate((Integer.parseInt(prop.getProperty("energyRequiredToProcreate"))));
        this.parametersParser.setEnergyLossForChild((Integer.parseInt(prop.getProperty("energyLossForChild"))));
        this.parametersParser.setStartEnergyForFactoryAnimals((Integer.parseInt(prop.getProperty("startEnergyForFactoryAnimals"))));
        this.parametersParser.setDnaLength((Integer.parseInt(prop.getProperty("dnaLength"))));
        this.parametersParser.setPlantsEnergy((Integer.parseInt(prop.getProperty("plantsEnergy"))));
        this.parametersParser.setPlantsEachDayCount((Integer.parseInt(prop.getProperty("plantsEachDayCount"))));

        this.parametersParser.setMapTypeFlag((Boolean.parseBoolean(prop.getProperty("mapTypeFlag"))));
        this.parametersParser.setGardenTypeFlag((Boolean.parseBoolean(prop.getProperty("gardenTypeFlag"))));
        this.parametersParser.setMutationTypeFlag((Boolean.parseBoolean(prop.getProperty("mutationTypeFlag"))));
        this.parametersParser.setBehaviorTypeFlag((Boolean.parseBoolean(prop.getProperty("behaviourTypeFlag"))));

        return Boolean.parseBoolean(prop.getProperty("saveStats"));
    }

    private void addMutationRangeSlider(VBox vBox){
        Label label = new Label("Min-Max Mutation Count");
        VBox.setMargin(mutationCountRange, new Insets(0, 10, 0, 10));

        mutationCountRange.setShowTickLabels(true);
        mutationCountRange.setShowTickMarks(true);
        mutationCountRange.setMajorTickUnit(2);
        mutationCountRange.setMinorTickCount(1);
        mutationCountRange.setAccessibleText("Min/Max Mutation Count");
        mutationCountRange.setSnapToTicks(true);

        mutationCountRange.setPrefWidth(300);
        mutationCountRange.setPrefHeight(50);

        vBox.getChildren().add(label);
        vBox.getChildren().add(mutationCountRange);
    }

}