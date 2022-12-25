package agh.ics.oop;

public interface IMapType {

    /**
    the method controls what happens with the animal if it goes out of bounds of the map,
     only if the animal is out of bounds should the method be called
     **/
    void walksOutOfBounds(Animal animal, WorldMap map);
    // here I don't think we can pass as an argument a map because this will be used
}
