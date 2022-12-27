package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class TotalRandomness implements IMutationType {

    private final int dnaLength;
    private static final Random random = new Random();

    public TotalRandomness(int dnaLength) {
        this.dnaLength = dnaLength;
    }

    // everytime mutation happens, the number and which genes will change, will be different
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
            dna[index] = GeneDirection.generateGeneDirection();
        }
    }

}
