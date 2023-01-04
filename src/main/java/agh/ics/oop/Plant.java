package agh.ics.oop;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Plant implements IMapElement{

    private static final Image image;

    static {
        try {
            image = new Image(new FileInputStream("src/main/resources/grass.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final Vector2d position;

    public Plant(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition(){
        return position;
        }

    @Override
    public String toString(){
        return "*";
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public int getEnergy() {
        return -1;
    }
}