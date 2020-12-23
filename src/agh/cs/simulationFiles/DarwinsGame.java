package agh.cs.simulationFiles;

import agh.cs.map.RectangularMap;
import agh.cs.objectMapInformations.Vector2d;
import agh.cs.mapElements.Animal;
import agh.cs.map.GrassField;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DarwinsGame {
    private int day;
    private GrassField map;
    private RectangularMap jungleArea;
    private RectangularMap areaOfMap;
    private int grassEnergy;
    private int animalEnergy;
    private int width;
    private int height;
    private int moveEnergyCost;
    private int howManyAnimals = 0;
    private int howManyGrasses = 0;
    private int averageEnergy = 0;
    private int howManyDeadAnimals = 0;
    private int howManyDaysForDeadAnimals = 0;
    private int averageChild;
    private int delay;
    private int[] dominatingGenotype = new int[8];
    private int junglePercentage;
    private History history = new History(this);

    public DarwinsGame(int width, int height, int howManyAnimalsAtStart, int animalEnergy, int grassEnergy, int moveEnergyCost, int junglePercentage, int delay) {
        setDay(0);
        this.setJunglePercentage(junglePercentage);
        setAreaOfMap(new RectangularMap(new Vector2d(0, 0), new Vector2d(width - 1, height - 1)));
        double utilValue = Math.sqrt((double) junglePercentage / 100) / 2;
        setJungleArea(new RectangularMap(new Vector2d((int) (width / 2 - width * utilValue), (int) (height / 2 - height * utilValue)), new Vector2d((int) (width / 2 + width * utilValue), (int) (height / 2 + height * utilValue))));
        setMap(new GrassField(getAreaOfMap()));
        this.setWidth(width);
        this.setHeight(height);

        this.setAnimalEnergy(animalEnergy);
        this.setHowManyAnimals(howManyAnimalsAtStart);
        createAnimals(howManyAnimalsAtStart);
        this.setGrassEnergy(grassEnergy);

        this.setMoveEnergyCost(moveEnergyCost);
        this.setDelay(delay);

    }

    public void simulate() {
        removeDeadAnimals();

        getMap().run();
        getMap().makeAllAnimalsActions();
        generateGrass(this.getAreaOfMap(), this.getJungleArea());

        setHowManyAnimals(getHowManyAnimals() + getMap().getCurrDayAnimalsBorn());
        setHowManyGrasses(getHowManyGrasses() - getMap().getCurrDayGrassEaten());
        setAverageEnergy(computeAverageEnergy());
        setAverageChild(computeAverageChildren());

        getHistory().updateHistory();

        setDay(getDay() + 1);
        getMap().setDay(getDay());

    }

    // It's not optimal, because getUnoccupiedPosition goes through all fields in map every time it is called
    // But number of animals wont be very high and createAnimals will be called only once, so I think it's OK
    private void createAnimals(int numberOfAnimals) {
        for (int i = 0; i < numberOfAnimals; i++) {
            Vector2d currPosition = getMap().getUnoccupiedPosition();
            Animal createdAnimal = new Animal(getMap(), currPosition);
            createdAnimal.setEnergy(getAnimalEnergy());
            getMap().place(createdAnimal);
        }
    }

    private int computeAverageEnergy() {
        int avg = 0;
        List<Animal> animals = getMap().getAnimalsToList();

        if (animals.size() > 0) {
            for (Animal animal : animals) {
                avg += animal.getEnergy();
            }
            return avg / animals.size();
        } else {
            return 0;
        }
    }

    private int computeAverageChildren() {
        int avg = 0;
        List<Animal> animals = getMap().getAnimalsToList();

        if (animals.size() > 0) {
            for (Animal animal : animals) {
                avg += animal.getChildren().size();
            }
            return avg / animals.size();
        } else {
            return 0;
        }
    }

    public GrassField getMap() {
        return this.map;
    }

    public int getDominatingGene() {
        int[] tmp = new int[8];
        for (int i = 0; i < 8; i++) {
            tmp[i] = 0;
        }
        for (Animal animal : getMap().getAnimalsToList()) {
            int gene = animal.getBestGene();
            tmp[gene] += 1;
        }
        int max = 0;
        int key = -1;
        for (int i = 0; i < 8; i++) {
            if (tmp[i] > max) {
                max = tmp[i];
                key = i;
            }
        }
        return key;
    }


    public int getAverageEnergy() {
        return this.averageEnergy;
    }

    public int getAverageDaysForDeadAnimals() {
        if (getHowManyDeadAnimals() != 0)
            return getHowManyDaysForDeadAnimals() / getHowManyDeadAnimals();
        else return 0;
    }

    public int getAverageChild() {
        if (getHowManyAnimals() != 0)
            return averageChild / howManyAnimals;
        else
            return 0;
    }

    private void removeDeadAnimals() {
        List<Animal> allAnimals = getMap().getAnimalsToList();
        for (Animal animal : allAnimals) {
            if (animal.isDead(getDay())) {
                this.setHowManyAnimals(this.getHowManyAnimals() - 1);
                this.setHowManyDeadAnimals(this.getHowManyDeadAnimals() + 1);
                this.setHowManyDaysForDeadAnimals(this.getHowManyDaysForDeadAnimals() + (this.getDay() - animal.getBirthDay()));
                getMap().removeAnimal(animal);
            }
        }
    }

    private void generateGrass(RectangularMap areaOfMap, RectangularMap jungleArea) {
        getMap().grassGeneration(areaOfMap, jungleArea);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMap(GrassField map) {
        this.map = map;
    }

    public RectangularMap getJungleArea() {
        return jungleArea;
    }

    public void setJungleArea(RectangularMap jungleArea) {
        this.jungleArea = jungleArea;
    }

    public RectangularMap getAreaOfMap() {
        return areaOfMap;
    }

    public void setAreaOfMap(RectangularMap areaOfMap) {
        this.areaOfMap = areaOfMap;
    }

    public int getGrassEnergy() {
        return grassEnergy;
    }

    public void setGrassEnergy(int grassEnergy) {
        this.grassEnergy = grassEnergy;
    }

    public int getAnimalEnergy() {
        return animalEnergy;
    }

    public void setAnimalEnergy(int animalEnergy) {
        this.animalEnergy = animalEnergy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMoveEnergyCost() {
        return moveEnergyCost;
    }

    public void setMoveEnergyCost(int moveEnergyCost) {
        this.moveEnergyCost = moveEnergyCost;
    }

    public int getHowManyAnimals() {
        return howManyAnimals;
    }

    public void setHowManyAnimals(int howManyAnimals) {
        this.howManyAnimals = howManyAnimals;
    }

    public void setAverageEnergy(int averageEnergy) {
        this.averageEnergy = averageEnergy;
    }

    public int getHowManyDeadAnimals() {
        return howManyDeadAnimals;
    }

    public void setHowManyDeadAnimals(int howManyDeadAnimals) {
        this.howManyDeadAnimals = howManyDeadAnimals;
    }

    public int getHowManyDaysForDeadAnimals() {
        return howManyDaysForDeadAnimals;
    }

    public void setHowManyDaysForDeadAnimals(int howManyDaysForDeadAnimals) {
        this.howManyDaysForDeadAnimals = howManyDaysForDeadAnimals;
    }

    public void setAverageChild(int averageChild) {
        this.averageChild = averageChild;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int[] getDominatingGenotype() {
        return dominatingGenotype;
    }

    public void setDominatingGenotype(int[] dominatingGenotype) {
        this.dominatingGenotype = dominatingGenotype;
    }

    public int getJunglePercentage() {
        return junglePercentage;
    }

    public void setJunglePercentage(int junglePercentage) {
        this.junglePercentage = junglePercentage;
    }

    public int getHowManyGrasses() {
        return howManyGrasses;
    }

    public void setHowManyGrasses(int howManyGrasses) {
        this.howManyGrasses = howManyGrasses;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }
}
