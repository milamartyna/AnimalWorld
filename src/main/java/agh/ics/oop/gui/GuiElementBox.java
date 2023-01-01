package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static java.lang.Math.min;

public class GuiElementBox {

    private Image image;
    private ImageView imageView;
    private Label position;
    private VBox box;
    private ProgressBar progressBar;
    private double energyProgress;
    public GuiElementBox(IMapElement element, double size, int totalEnergy){
        try{
            image = new Image(new FileInputStream(element.getImage()));
            this.imageView = new ImageView(image);
            this.imageView.setFitWidth(size*0.7);
            this.imageView.setFitHeight(size*0.7);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.position = new Label(element.getPosition().toString());
        this.box = new VBox();
        this.box.getChildren().addAll(imageView);
        this.box.setAlignment(Pos.CENTER);
        if(element.getClass().equals(Animal.class)){
            int minEnergy = min(totalEnergy, element.getEnergy());
            this.energyProgress = minEnergy / totalEnergy;
            this.progressBar = new ProgressBar();
            this.progressBar.setProgress(energyProgress);
            this.box.getChildren().addAll(progressBar);
        }
    }

    public VBox getVBox() { return this.box; }
}
