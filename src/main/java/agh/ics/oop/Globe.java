package agh.ics.oop;

public class Globe implements IMapType{

    // here we get an animal with position already out of bounds
    @Override
    public void walksOutOfBounds(Animal animal, Vector2d startMap, Vector2d endMap) {

        // if animal goes out of map in the corner, we give priority to North/South Pole
        if(animal.getPosition().y() > endMap.y() || animal.getPosition().y() < endMap.y()){
            GeneDirection newDirection = animal.getGeneDirection().opposite();
            Vector2d backToMap = newDirection.toUnitVector();
            animal.setPosition(animal.getPosition().add(backToMap));
            animal.setDirection(newDirection);
        }

        if(animal.getPosition().x() < startMap.x()){
            Vector2d newPosition = new Vector2d(endMap.x(), animal.getPosition().y());
            animal.setPosition(newPosition);
        }

        if(animal.getPosition().x() > endMap.x()){
            Vector2d newPosition = new Vector2d(startMap.x(), animal.getPosition().y());
            animal.setPosition(newPosition);
        }
    }

}