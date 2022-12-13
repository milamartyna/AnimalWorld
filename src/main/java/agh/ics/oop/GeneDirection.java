package agh.ics.oop;

import java.util.Random;

public enum GeneDirection {

    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN;

    private static final Random random = new Random();

    public static GeneDirection generateGeneDirection()  {
        GeneDirection[] directions = values();
        return directions[random.nextInt(directions.length)];
    }

    public String toString() {
        return switch (this) {
            case ZERO -> "0";
            case ONE -> "1";
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
        };
    }

    private int toNumber(){
        return switch (this) {
            case ZERO -> 0;
            case ONE -> 1;
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
            case SIX -> 6;
            case SEVEN -> 7;
        };
    }

    public GeneDirection fromNumber(int num) {
        return switch (num) {
            case 0 -> ZERO;
            case 1 -> ONE;
            case 2 -> TWO;
            case 3 -> THREE;
            case 4 -> FOUR;
            case 5 -> FIVE;
            case 6 -> SIX;
            case 7 -> SEVEN;
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }

    public GeneDirection next(){
        return switch(this){
            case ZERO -> ONE;
            case ONE -> TWO;
            case TWO -> THREE;
            case THREE -> FOUR;
            case FOUR -> FIVE;
            case FIVE -> SIX;
            case SIX -> SEVEN;
            case SEVEN -> ZERO;
        };
    }

    public GeneDirection previous(){
        return switch(this){
            case ZERO -> SEVEN;
            case ONE -> ZERO;
            case TWO -> ONE;
            case THREE -> TWO;
            case FOUR -> THREE;
            case FIVE -> FOUR;
            case SIX -> FIVE;
            case SEVEN -> SIX;
        };
    }
    public Vector2d toUnitVector(){
        return switch(this){
            case ZERO -> new Vector2d(0, 1);
            case ONE -> new Vector2d(1, 1);
            case TWO -> new Vector2d(1, 0);
            case THREE -> new Vector2d(1, -1);
            case FOUR -> new Vector2d(0,-1);
            case FIVE -> new Vector2d(-1,-1);
            case SIX -> new Vector2d(-1,0);
            case SEVEN -> new Vector2d(-1,1);
        };
    }

    public GeneDirection turn(GeneDirection gene){
        if (gene.toNumber() + this.toNumber() > 7) {
            return fromNumber(gene.toNumber() + this.toNumber() - 8);
        }
        return fromNumber(gene.toNumber() + this.toNumber());
    }

}