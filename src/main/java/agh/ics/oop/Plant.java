package agh.ics.oop;

public class Plant implements IMapElement{

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
    public String getImage() {
        return "src/main/resources/grass.png";
    }

    @Override
    public int getEnergy() {
        return -1;
    }
}