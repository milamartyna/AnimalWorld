package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {

    final static int size = 20;
    private Image image;
    private ImageView imageView;
    private Label position;
    private VBox box;

    public GuiElementBox(IMapElement element){
        this.position = new Label(element.getPosition().toString());
        this.box = new VBox();
        this.box.getChildren().addAll(position);
        this.box.setAlignment(Pos.CENTER);
    }

    public VBox getVBox() { return this.box; }
}
