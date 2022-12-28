package agh.ics.oop;

import java.io.PrintStream;
import java.util.*;

public class World {

    public static void main(String[] args){

        VariableManager manager = new VariableManager();
        WorldMap map = new WorldMap(manager);

//        Animal animal1 = new Animal(map);
//        System.out.println(Arrays.toString(animal1.dna));
//        System.out.println(animal1.getPosition());
//
//        Animal animal2 = new Animal(map);
//        System.out.println(Arrays.toString(animal2.dna));
//        System.out.println(animal2.getPosition());
//
//        Animal childAnimal = animal1.makeChild(animal2);
//        System.out.println(Arrays.toString(childAnimal.dna));
//
//        animal1.move();
//        System.out.println(animal1.getPosition());
//
//        System.out.println(map);

        SimulationEngine engine = new SimulationEngine(manager, map);
        engine.run();


    }
}

