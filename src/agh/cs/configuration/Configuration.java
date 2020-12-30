package agh.cs.configuration;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Configuration {
    private static Configuration instance = null;

    private int mapWidth;
    private int mapHeight;

    private int toolbarSize = 28;
    private int toolbarScale = 28;

    private int startingAnimals;
    private int animalEnergy;
    private int grassEnergy;
    private int moveCost;
    private int junglePercentage;
    private int delay;
    private int maps;

    private boolean show = false;
    private boolean pinning = false;

    public static Configuration getInstance() {
        if (instance == null) {
            Gson gson = new Gson();
            String path = "parameters.json";
            try {
                JsonReader reader = new JsonReader(new FileReader(path));
                instance = gson.fromJson(reader, Configuration.class);
            } catch (FileNotFoundException e) {
                instance = new Configuration();
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static void setInstance(Configuration instance) {
        Configuration.instance = instance;
    }

    public int getMapWidth() {
        if (mapWidth < 0) throw new IllegalArgumentException("Provide valid value");
        return this.mapWidth;
    }

    public int getMapHeight() {
        if (mapHeight < 0) throw new IllegalArgumentException("Provide valid value");
        return this.mapHeight;
    }

    public int getStartingAnimals() {
        if (startingAnimals < 0) throw new IllegalArgumentException("Provide valid value");
        return this.startingAnimals;
    }

    public int getGrassEnergy() {
        if (grassEnergy < 0) throw new IllegalArgumentException("Provide valid value");
        return this.grassEnergy;
    }

    public int getMoveCost() {
        if (moveCost < 0) throw new IllegalArgumentException("Provide valid value");
        return this.moveCost;
    }

    public int getJunglePercentage() {
        return this.junglePercentage;
    }

    public int getDelay() {
        if (delay < 0) throw new IllegalArgumentException("Provide valid value");
        return this.delay;
    }

    public int getMaps() {
        return this.maps;
    }

    public int getAnimalEnergy() {
        if (animalEnergy < 0) {
            throw new IllegalArgumentException("Provide valid value");
        }
        return this.animalEnergy;
    }

    public void swapShow() {
        this.setShow(true);
    }

    public int getToolbarSize() {
        return this.toolbarSize;
    }

    public int getToolbarScale() {
        return this.toolbarScale;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setStartingAnimals(int startingAnimals) {
        this.startingAnimals = startingAnimals;
    }

    public void setAnimalEnergy(int animalEnergy) {
        this.animalEnergy = animalEnergy;
    }

    public void setGrassEnergy(int grassEnergy) {
        this.grassEnergy = grassEnergy;
    }

    public void setMoveCost(int moveCost) {
        this.moveCost = moveCost;
    }

    public void setJunglePercentage(int junglePercentage) {
        this.junglePercentage = junglePercentage;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setMaps(int maps) {
        this.maps = maps;
    }

    public void setToolbarSize(int toolbarSize) {
        this.toolbarSize = toolbarSize;
    }

    public void setToolbarScale(int toolbarScale) {
        this.toolbarScale = toolbarScale;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isPinning() {
        return pinning;
    }

    public void setPinning(boolean pinning) {
        this.pinning = pinning;
    }
}
