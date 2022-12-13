package agh.ics.oop;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

public class World {

    public static void main(String[] args){
        Animal animal1 = new Animal();
        System.out.println(Arrays.toString(animal1.dna));

        Animal animal2 = new Animal();
        System.out.println(Arrays.toString(animal2.dna));
    }
}

