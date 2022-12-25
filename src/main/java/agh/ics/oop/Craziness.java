package agh.ics.oop;

import java.util.Random;

public class Craziness implements IBehaviorType{

    private final int dnaLength;
    private static final Random random = new Random();

    public Craziness(int dnaLength){
        this.dnaLength = dnaLength;
    }

    @Override
    public int geneActivation(int currGeneIndex) {
        if(isNormalBehaviour()) {
            if (currGeneIndex == this.dnaLength - 1) {
                return 0;
            } else {
                return currGeneIndex + 1;
            }
        }
        else {
            int nextGene = random.nextInt(0, this.dnaLength);
            while (nextGene == currGeneIndex){
                nextGene = random.nextInt(0, this.dnaLength);
            }
            return nextGene;
        }
    }

    private boolean isNormalBehaviour(){
        int x = random.nextInt(10);
        return x < 8;
    }

}
