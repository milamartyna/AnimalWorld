package agh.ics.oop;

public interface IGardenType {

    public void seedPlants(WorldMap map, int seedCount);

    public void plantIsEaten(WorldMap map, Vector2d position);

}
