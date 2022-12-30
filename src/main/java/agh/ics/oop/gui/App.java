package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class App extends Application {

    private WorldMap map;
    private ParametersParser parametersParser;
    private VariableManager manager;

    @Override
    public void start(Stage primaryStage) {
        this.parametersParser = new ParametersParser();
        getParametersFromUser(primaryStage);
    }

    public void getParametersFromUser(Stage primaryStage)
    {
        Scene scene = new Scene(createArgumentGetter(), 700, 850);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Animal World");
        primaryStage.show();
    }

    private VBox createArgumentGetter(){
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.BASELINE_CENTER);

        Slider[] sliders = new Slider[11];

        int[][] numericParameters = {
            // min, max, default Value, Major Tick
            {5, 30, 20, 5}, // width
            {5, 30, 20, 5}, // height
            {0, 100, 5, 20}, // amountOfAnimals
            {0, 100, 5, 20}, // amountOfPlants
            {1, 10, 3, 1}, // dailyEnergyLoss
            {5, 50, 10, 10}, // energyRequiredToProcreate
            {5, 50, 10, 10}, // energyLossForChildren
            {10, 100, 20, 20}, // startEnergy
            {4, 20, 7, 2}, // DNALength
            {10, 100, 20, 20}, // plantEnergy
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

        for(int i = 0; i < numericParameters.length; i++) {
            this.addLabelAndSlider(sliders, i, vBox, numericParameters[i], stringParameters[i]);
        }

        String[][] variantChoices = {
                {"Globe", "Hell Gate"},
                {"Green Equator", "Toxic Fields"},
                {"CompletePredestination", "Craziness"},
                {"Total Randomness", "Slight Correction"}
        };

        // here we should add check boxes for different variants -> Hell Gate / Globe...
        ChoiceBox[] choiceBoxes = new ChoiceBox[4];

        for(int i = 0; i < variantChoices.length; i++){
            ChoiceBox<String> variant = new ChoiceBox<>();
            variant.setItems(FXCollections.observableArrayList(variantChoices[i][0], variantChoices[i][1]));
            choiceBoxes[i] = variant;
            vBox.getChildren().add(variant);
        }

        this.addAcceptButton(vBox, sliders, choiceBoxes, variantChoices);
        return vBox;
    }

    private void addLabelAndSlider(Slider[] sliders, int index, VBox vbox, int[] parameters, String title)
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

        sliders[index] = slider;

        VBox.setMargin(slider, new Insets(0, 10, 0, 10));

        vbox.getChildren().add(label);
        vbox.getChildren().add(slider);
    }

    private void addAcceptButton(VBox vbox, Slider[] sliders, ChoiceBox[] choiceBoxes, String[][] variantChoices)
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
            SimulationEngine engine = new SimulationEngine(manager, map);
            engine.run();

        });

        vbox.getChildren().add(button);
    }

    private void parseVariantChoices(ChoiceBox[] choicesBox, String[][] variantChoices){
        this.parametersParser.setMapType(choicesBox[0].getValue() == variantChoices[0][0]);
        this.parametersParser.setGardenType(choicesBox[1].getValue() == variantChoices[1][0]);
        this.parametersParser.setBehaviorType(choicesBox[2].getValue() == variantChoices[2][0]);
        this.parametersParser.setMutationType(choicesBox[3].getValue() == variantChoices[3][0]);
    }

}
