package agh.ics.oop;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

public class World {

    public static void main(String[] args){
        int startAnimalNumber = 10;
        int height = 10;
        int width = 10;

        VariableManager manager = new VariableManager();
        WorldMap map = new WorldMap(manager);

        Animal animal1 = new Animal(map);
        System.out.println(Arrays.toString(animal1.dna));

        Animal animal2 = new Animal(map);
        System.out.println(Arrays.toString(animal2.dna));
    }
}

