package agh.ics.oop;

public interface IMapObserver {
    void newDay();
    void animalBirth(Animal animal);
    void animalDeath(Animal animal);
    void plantCountUpdate(boolean grow);
    void freeSpotsUpdate(int freeSpots);
    void energyLevelsUpdate(int sumEnergy);
    void factoryMadeAnimal(int animalCount);

    int getDay();
    int getAnimalCount();
    int getPlantsCount();
    int getFreeSpotsCount();
    double getAverageEnergyLevel();
    GeneDirection[] getMostPopularDNA();
    double getAverageLifespan();
    double getAverageChildrenCount();
}
