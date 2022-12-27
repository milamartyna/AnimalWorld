package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class SlightCorrection implements IMutationType{

    private final int dnaLength;
    private static final Random random = new Random();

    public SlightCorrection(int dnaLength){
        this.dnaLength = dnaLength;
    }

    // mutation will change genes randomly one up or one down
    @Override
    public void mutation(GeneDirection[] dna) {
        int mutationGeneCount = random.nextInt(this.dnaLength);
        Integer[] mutationGeneIndexes = new Integer[mutationGeneCount];
        for (int i = 0; i < mutationGeneCount; i++) {
            int index = random.nextInt(this.dnaLength);
            while (Arrays.asList(mutationGeneIndexes).contains(index)) {
                index = random.nextInt(this.dnaLength);
            }
            mutationGeneIndexes[i] = index;
        }

        for (Integer index : mutationGeneIndexes) {
            int flag = random.nextInt(2);
            if(flag == 0){
                dna[index] = dna[index].next();
            }else{
                dna[index] = dna[index].previous();
            }
        }
    }

}
