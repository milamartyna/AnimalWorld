package agh.ics.oop;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class World {

    public static void main(String[] args){

        VariableManager manager = new VariableManager();
        WorldMap map = new WorldMap(manager);

        System.out.println(map);

        Animal animal1 = new Animal(map);
        System.out.println(Arrays.toString(animal1.dna));

        Animal animal2 = new Animal(map);
        System.out.println(Arrays.toString(animal2.dna));


    }
}

