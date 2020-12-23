package agh.cs.simulationFiles;

import java.util.ArrayList;

public class History {
    private ArrayList<Integer> days;
    private ArrayList<Integer> numberOfAnimals;
    private ArrayList<Integer> numberOfGrass;
    private ArrayList<Integer> dominatingGene;
    private ArrayList<Integer> averageEnergy;
    private ArrayList<Integer> averageLifeLength;
    private ArrayList<Integer> averageChildren;
    private DarwinsGame simulation;

    public History(DarwinsGame simulation) {
        this.setDays(new ArrayList<>());
        this.setNumberOfAnimals(new ArrayList<>());
        this.setNumberOfGrass(new ArrayList<>());
        this.setDominatingGene(new ArrayList<>());
        this.setAverageEnergy(new ArrayList<>());
        this.setAverageLifeLength(new ArrayList<>());
        this.setAverageChildren(new ArrayList<>());
        this.setSimulation(simulation);
    }

    public void updateHistory() {
        getDays().add(getSimulation().getDay());
        getNumberOfAnimals().add(getSimulation().getHowManyAnimals());
        getNumberOfGrass().add(getSimulation().getHowManyGrasses());
        getDominatingGene().add(getSimulation().getDominatingGene());
        getAverageEnergy().add(getSimulation().getAverageEnergy());
        getAverageLifeLength().add(getSimulation().getAverageDaysForDeadAnimals());
        getAverageChildren().add(getSimulation().getAverageChild());
    }

    public ArrayList<Integer> getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(ArrayList<Integer> numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public ArrayList<Integer> getNumberOfGrass() {
        return numberOfGrass;
    }

    public void setNumberOfGrass(ArrayList<Integer> numberOfGrass) {
        this.numberOfGrass = numberOfGrass;
    }

    public ArrayList<Integer> getDominatingGene() {
        return dominatingGene;
    }

    public void setDominatingGene(ArrayList<Integer> dominatingGene) {
        this.dominatingGene = dominatingGene;
    }

    public ArrayList<Integer> getAverageEnergy() {
        return averageEnergy;
    }

    public void setAverageEnergy(ArrayList<Integer> averageEnergy) {
        this.averageEnergy = averageEnergy;
    }

    public ArrayList<Integer> getAverageLifeLength() {
        return averageLifeLength;
    }

    public void setAverageLifeLength(ArrayList<Integer> averageLifeLength) {
        this.averageLifeLength = averageLifeLength;
    }

    public ArrayList<Integer> getAverageChildren() {
        return averageChildren;
    }

    public void setAverageChildren(ArrayList<Integer> averageChildren) {
        this.averageChildren = averageChildren;
    }

    public DarwinsGame getSimulation() {
        return simulation;
    }

    public void setSimulation(DarwinsGame simulation) {
        this.simulation = simulation;
    }

    public ArrayList<Integer> getDays() {
        return days;
    }

    public void setDays(ArrayList<Integer> days) {
        this.days = days;
    }
}
