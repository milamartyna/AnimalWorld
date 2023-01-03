package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.GeneDirection;
import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import static java.lang.Math.min;

public class GuiElementBox {

    private Image image;
    private ImageView imageView;
    private Button button;
    private VBox box;
    private ProgressBar progressBar;
    private double energyProgress;
    public GuiElementBox(IMapElement element, double size, int totalEnergy){
        try{
            image = new Image(new FileInputStream(element.getImage()));
            this.imageView = new ImageView(image);
            this.imageView.setFitWidth(size*0.6);
            this.imageView.setFitHeight(size*0.6);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.box = new VBox();
        this.box.setAlignment(Pos.CENTER);
        if(element.getClass().equals(Animal.class)){
            int minEnergy = min(totalEnergy, element.getEnergy());
            this.button = new Button();
            this.button.setGraphic(imageView);
            this.energyProgress = minEnergy / totalEnergy;
            this.progressBar = new ProgressBar();
            this.progressBar.setProgress(energyProgress);
            this.box.getChildren().addAll(button, progressBar);
        }
        else{this.box.getChildren().addAll(imageView);}
    }

    public VBox getVBox() { return this.box; }

}