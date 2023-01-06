package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class TotalRandomness implements IMutationType {

    private final int dnaLength;
    private final int minMutationCount;
    private final int maxMutationCount;
    private static final Random random = new Random();

    public TotalRandomness(int dnaLength, int minMutationCount, int maxMutationCount) {
        this.dnaLength = dnaLength;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = Math.min(maxMutationCount, dnaLength);
    }

    // everytime mutation happens, the number and which genes will change, will be different
    @Override
    public void mutation(GeneDirection[] dna) {
        int mutationGeneCount = random.nextInt(minMutationCount, maxMutationCount);
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
