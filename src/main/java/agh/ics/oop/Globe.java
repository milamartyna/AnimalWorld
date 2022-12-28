package agh.ics.oop;

public class Globe implements IMapType{

    // here we get an animal with position already out of bounds
    @Override
    public void walksOutOfBounds(Animal animal, WorldMap map) {

        // if animal goes out of map in the corner, we give priority to North/South Pole
        if(animal.getPosition().y() > map.endMap.y() || animal.getPosition().y() < map.startMap.y()){
            GeneDirection newDirection = animal.getDirection().opposite();
            Vector2d backToMap = newDirection.toUnitVector();
            animal.setPosition(animal.getPosition().add(backToMap));
            animal.setDirection(newDirection);
        }

        if(animal.getPosition().x() < map.startMap.x()){
            Vector2d newPosition = new Vector2d(map.endMap.x(), animal.getPosition().y());
            animal.setPosition(newPosition);
        }

        if(animal.getPosition().x() > map.endMap.x()){
            Vector2d newPosition = new Vector2d(map.startMap.x(), animal.getPosition().y());
            animal.setPosition(newPosition);
        }
    }

}