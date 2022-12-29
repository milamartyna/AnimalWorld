package agh.ics.oop;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class GreenEquator implements IGardenType {
    private int lastPreferredRow;
    private int firstPreferredRow;

    private final int mapWidth;
    private final int mapHeight;
    private final LinkedList<Vector2d> preferredSpots = new LinkedList<>();
    private final LinkedList<Vector2d> nonPreferredSpots = new LinkedList<>();
    private static final Random random = new Random();

    public GreenEquator(int width, int height){
        this.mapWidth = width;
        this.mapHeight = height;
        this.setPreferredRowsIndexes();
        this.setSpotsLists();
    }

    // here we decided that if with the probability of 80/20 the plant should grow in the
    // preferred spots but all the preferred spots are already taken then the grass will
    // not grow **We Can Change It Using Two More If statements**
    @Override
    public void seedPlants(WorldMap map, int seedCount) {
        Collections.shuffle(this.preferredSpots);
        Collections.shuffle(this.nonPreferredSpots);
        while (seedCount > 0){
            boolean plantInJungle = this.isJungle();
            if(plantInJungle && this.preferredSpots.size() > 0){
                map.addPlant(this.preferredSpots.remove());
            }
            if(!plantInJungle && this.nonPreferredSpots.size() > 0){
                map.addPlant(this.nonPreferredSpots.remove());
            }
            seedCount--;
        }
    }

    @Override
    public void plantIsEaten(WorldMap map, Vector2d position) {
        if(isRowPreferred(position.y())){
            this.preferredSpots.add(position);
        }else {
            this.nonPreferredSpots.add(position);
        }
    }

    private boolean isJungle(){
        int x = random.nextInt(10);
        return x < 8;
    }

    private void setPreferredRowsIndexes(){
        int equatorRowsCount = (int) Math.ceil(mapHeight * 0.2);

        if(mapHeight % 2 == 0){
            if(equatorRowsCount % 2 != 0){
                equatorRowsCount += 1;
            }
            this.firstPreferredRow = (mapHeight / 2) - (equatorRowsCount / 2);
            this.lastPreferredRow = (mapHeight / 2) + (equatorRowsCount / 2) - 1;
        }

        if(mapHeight % 2 != 0){
            if(equatorRowsCount % 2 == 0) {
                equatorRowsCount += 1;
            }
            this.firstPreferredRow = (mapHeight / 2) - (equatorRowsCount / 2);
            this.lastPreferredRow = (mapHeight / 2) + (equatorRowsCount / 2);
        }
    }

    private void setSpotsLists(){
        for(int i = 0; i < this.mapHeight; i++){
            for(int j = 0; j < this.mapWidth; j++){
                if(isRowPreferred(j)){
                    preferredSpots.add(new Vector2d(i, j));
                }else{
                    nonPreferredSpots.add(new Vector2d(i, j));
                }
            }
        }
    }

    private boolean isRowPreferred(int x){
        return x >= this.firstPreferredRow && x <= this.lastPreferredRow;
    }
}