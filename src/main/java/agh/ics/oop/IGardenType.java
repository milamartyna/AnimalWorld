package agh.ics.oop;

public interface IGardenType {

    void seedPlants(WorldMap map, int seedCount);

    void plantIsEaten(WorldMap map, Vector2d position);

}
