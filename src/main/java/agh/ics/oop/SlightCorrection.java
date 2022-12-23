package agh.ics.oop;

import java.util.Random;

public class SlightCorrection implements IMutationType{

    private final int dnaLength;
    private static final Random random = new Random();

    public SlightCorrection(int dnaLength){
        this.dnaLength = dnaLength;
    }

    @Override
    public void mutation(GeneDirection[] dna) {

    }
}
