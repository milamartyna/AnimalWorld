package agh.ics.oop;

public class HellGate implements IMapType {

    public void walksOutOfBounds(Animal animal, WorldMap map){
        if(animal.getPosition().x() < map.startMap.x() || animal.getPosition().x() > map.endMap.x() || animal.getPosition().y() < map.startMap.y() || animal.getPosition().y() > map.endMap.y()){
            int newEnergy = animal.getEnergy() - animal.startEnergyForAll;
            animal.setEnergy(newEnergy);
            Vector2d newPosition = map.generateMapPosition();
            animal.setPosition(newPosition);
        }
    }
}
