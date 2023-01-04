package agh.ics.oop;

import javafx.scene.image.Image;

public interface IMapElement {
    Vector2d getPosition();
    String toString();
    Image getImage();
    int getEnergy();
}
