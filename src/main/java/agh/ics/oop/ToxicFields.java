package agh.ics.oop;

import java.util.*;

public class ToxicFields implements IGardenType{

    private final int mapWidth;
    private final int mapHeight;
    LinkedList<Vector2d> preferredSpots;
    LinkedList<Vector2d> nonPreferredSpots;
    Random random = new Random();

    public ToxicFields(int width, int height){
        this.mapWidth = width;
        this.mapHeight = height;
    }

    @Override
    public void seedPlants(WorldMap map, int seedCount) {
        this.setSpotsLists(sortHashmap(map.getPlacesOfDeath()));
        Collections.shuffle(this.preferredSpots);
        Collections.shuffle(this.nonPreferredSpots);
        while (seedCount > 0){
            boolean plantInJungle = this.isJungle();
            Vector2d preferedPosition = preferredSpots.remove();
            Vector2d nonPreferedPosition = nonPreferredSpots.remove();
            if(plantInJungle && !map.isOccupied(preferedPosition)){
                map.addPlant(preferedPosition);
                this.nonPreferredSpots.add(nonPreferedPosition);
            }
            if(!plantInJungle && !map.isOccupied(nonPreferedPosition)){
                map.addPlant(nonPreferedPosition);
                this.preferredSpots.add(preferedPosition);
            }
            seedCount--;
        }
    }

    @Override
    public void plantIsEaten(WorldMap map, Vector2d position) {
        // here we should probably note that this position is no longer unavailable
        // we do not add this position to the (non)prefered list because it is working
        // a bit different than in a GreenEquator
    }

    private boolean isJungle(){
        int x = random.nextInt(10);
        return x < 8;
    }

    private static HashMap<Vector2d, Integer> sortHashmap(HashMap<Vector2d, Integer> hashMap){

        HashMap<Vector2d, Integer> sortedMap = new HashMap<Vector2d, Integer>();  // this is going to be our result
        ArrayList<Integer> list = new ArrayList<>();

        // Create a list from elements of HashMap
        for (Map.Entry<Vector2d, Integer> entry : hashMap.entrySet()) {
            list.add(entry.getValue());
        }
        // Sort the list
        Collections.sort(list);
        // put data from sorted list to hashmap
        for (Integer i : list) {
            for (Map.Entry<Vector2d, Integer> entry : hashMap.entrySet()) {
                if (entry.getValue().equals(i)) {
                    sortedMap.put(entry.getKey(), i);
                }
            }
        }
        return sortedMap;
    }

    // I want to make (non)prefered spots constantly updated
    private void setSpotsLists(HashMap<Vector2d, Integer> hashMap){
        preferredSpots = new LinkedList<>();
        nonPreferredSpots = new LinkedList<>();
        int area = mapHeight*mapWidth;
        int numOfPreferedSpots = (int) (area * 0.2);
        int counter = 0;
        for (Map.Entry<Vector2d, Integer> entry : hashMap.entrySet()) {
            if(counter <= numOfPreferedSpots){
                preferredSpots.add(entry.getKey());
            }
            else{
                nonPreferredSpots.add(entry.getKey());
            }
            counter += 1;
        }
    }

}