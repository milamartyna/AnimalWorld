package agh.ics.oop;

public class CompletePredestination implements IBehaviorType{

    private final int dnaLength;

    public CompletePredestination(int dnaLength){
        this.dnaLength = dnaLength;
    }

    @Override
    public int geneActivation(int currGeneIndex) {
        if (currGeneIndex == this.dnaLength - 1){
            return 0;
        }else {
            return currGeneIndex + 1;
        }
    }

}
