package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.IEngine;
import agh.ics.oop.IMapElement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Math.min;

public class GuiElementBox {

    private Image image;
    private ImageView imageView;
    private Button button;
    private VBox box;
    private ProgressBar progressBar;
    private double energyProgress;

    public GuiElementBox(IMapElement element, double size, int totalEnergy, Object animal, IEngine engine){

        image = element.getImage();
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(size*0.6);
        this.imageView.setFitHeight(size*0.6);

        this.box = new VBox();
        this.box.setAlignment(Pos.CENTER);
        if(element.getClass().equals(Animal.class)){
            int minEnergy = min(totalEnergy, element.getEnergy());
            this.button = new Button();
            this.button.setGraphic(imageView);
            this.energyProgress = (double) minEnergy / totalEnergy;
            this.progressBar = new ProgressBar();
            this.progressBar.setProgress(energyProgress);
            this.box.getChildren().addAll(button, progressBar);
            setButtonOnAction(this.button, (Animal) animal, engine);
        }
        else{this.box.getChildren().addAll(imageView);}
    }

    public VBox getVBox(){
        return this.box;
    }

    private static void setButtonOnAction(Button button, Animal animal, IEngine engine) {

        button.setOnAction(event -> {
            if (engine.isPaused()) {
                Button genotypeButton = new Button("Show information");
                HBox.setMargin(genotypeButton, new Insets(20));
                HBox hBox = new HBox(genotypeButton);
                Scene scene = new Scene(hBox, 230, 100);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                genotypeButton.setOnAction(e -> {
                    stage.hide();
                    Stage genotypeStage = new Stage();
                    VBox infoVBox = new VBox();
                    Label[] information = new Label[6];
                    information[0] = new Label("DNA: " + Arrays.toString(animal.getDna()));
                    information[1] = new Label("Active Gene: " + animal.getActiveGene());
                    information[2] = new Label("Energy: " + animal.getEnergy());
                    information[3] = new Label("Number of eaten Plants: " + animal.getEatenPlants());
                    information[4] = new Label("Number of Children: " + animal.getChildrenCount());
                    information[5] = new Label("Age: " + animal.getAge());
                    for(int i = 0; i < 6; i++) {
                        infoVBox.getChildren().add(information[i]);
                    }
                    genotypeStage.setScene(new Scene(infoVBox, 400, 150));
                    genotypeStage.show();
                });
            }
        });
    }
}