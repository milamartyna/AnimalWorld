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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Math.min;

public class GuiElementBox {

    private final VBox box;

    public GuiElementBox(IMapElement element, double size, int totalEnergy, Object animal, IEngine engine){

        Image image = element.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size*0.6);
        imageView.setFitHeight(size*0.6);

        this.box = new VBox();
        this.box.setAlignment(Pos.CENTER);
        if(element.getClass().equals(Animal.class)){
            int minEnergy = min(totalEnergy, element.getEnergy());
            Button button = new Button();
            button.setGraphic(imageView);
            double energyProgress = (double) minEnergy / totalEnergy;
            ProgressBar progressBar = new ProgressBar();
            progressBar.setProgress(energyProgress);
            this.box.getChildren().addAll(button, progressBar);
            setButtonOnAction(button, (Animal) animal, engine);
        }
        else{this.box.getChildren().addAll(imageView);}
    }

    public VBox getVBox(){
        return this.box;
    }

    private void setButtonOnAction(Button button, Animal animal, IEngine engine) {

        button.setOnAction(event -> {
            if (engine.isPaused()) {
                Button infoButton = new Button("Show information");
                Button trackButton = new Button("Track Animal");
                VBox.setMargin(infoButton, new Insets(5));
                VBox.setMargin(trackButton, new Insets(5));
                VBox vBox = new VBox(infoButton, trackButton);
                Scene scene = new Scene(vBox, 230, 100);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

                infoButton.setOnAction(e -> {
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

                trackButton.setOnAction(e -> {
                    engine.startTracking(animal);
                    stage.hide();
                });
            }
        });
    }
}