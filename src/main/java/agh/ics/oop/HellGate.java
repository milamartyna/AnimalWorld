package agh.ics.oop;

public class HellGate implements IMapType {
    @Override
    public void walksOutOfBounds(Animal animal, WorldMap map) {
        int loss = animal.getMap().manager.energyLossForChild;
        animal.updateEnergy(loss);
        Vector2d newPosition = map.generateMapPosition();
        animal.setPosition(newPosition);
    }
}