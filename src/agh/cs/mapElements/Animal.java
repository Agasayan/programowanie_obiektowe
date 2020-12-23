package agh.cs.mapElements;

import agh.cs.configuration.Configuration;
import agh.cs.objectMapInformations.MapDirection;
import agh.cs.objectMapInformations.MoveDirection;
import agh.cs.objectMapInformations.Vector2d;
import agh.cs.map.IWorldMap;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.*;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private IWorldMap map;
    private int[] genes = new int[32];
    private int energy;
    private boolean pinned = false;
    private int birthDay = 0;
    private int deathDay = 0;
    private List<Animal> children = new ArrayList<>();


    public String toString() {
        MapDirection s1 = this.getOrientation();
        return switch (s1) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case NORTH_WEST -> "NW";
            case WEST -> "W";
            case EAST -> "E";
            case SOUTH -> "S";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
        };
    }

    public Animal() {
        this.setOrientation(MapDirection.NORTH);
        this.setPosition(new Vector2d(0, 0));
        this.setEnergy(0);
    }

    public Animal(IWorldMap map, Vector2d position) {
        this.setMap(map);
        this.setPosition(position);
        this.setOrientation(MapDirection.NORTH);
        for (int i = 0; i < getGenes().length; i++) {
            this.getGenes()[i] = ThreadLocalRandom.current().nextInt(0, 8);
        }
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public boolean move(MoveDirection direction) {
        switch (direction) {
            case LEFT:
                this.setOrientation(MapDirection.previous(this.getOrientation()));
                return false;
            case RIGHT:
                this.setOrientation(MapDirection.next(this.getOrientation()));
                return false;
            case FORWARD:
                Vector2d forward_vector = this.getOrientation().toUnitVector(this.getOrientation());
                Vector2d futureVector = this.getPosition();
                futureVector = futureVector.add(forward_vector);
                if (this.getMap().canMoveTo(futureVector)) {
                    this.setPosition(futureVector);
                    return true;
                }
                return false;
            case BACKWARD:
                Vector2d backward_vector = this.getOrientation().toUnitVector(this.getOrientation());
                futureVector = this.getPosition();
                futureVector = futureVector.subtract(backward_vector);
                if (this.getMap().canMoveTo(futureVector)) {
                    this.setPosition(futureVector);
                    return true;
                }
                return false;
        }
        return false;
    }

    public IWorldMap getMap() {
        return this.map;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int e) {
        this.energy = e;
    }

    public int[] getGenes() {
        return this.genes;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;
    }

    //get random turn of this animal
    private int random_turn() {
        int x = ThreadLocalRandom.current().nextInt(0, 32);
        return getGenes()[x];
    }

    // generate random turn and set new orientation
    public void setNewDirection() {
        int n = random_turn();
        switch (n) {
            case 0:
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                for (int i = 1; i <= n; i++) {
                    this.setOrientation(MapDirection.next(this.getOrientation()));
                }
                break;

            case 5:
            case 6:
            case 7:
                for (int i = 1; i <= 8 - n; i++) {
                    this.setOrientation(MapDirection.previous(this.getOrientation()));
                }
                break;
        }
    }

    public Color toColor() {
        int starting = Configuration.getInstance().getAnimalEnergy();
        if (getEnergy() == 0) return new Color(245, 240, 240);
        else if (getEnergy() < starting / 4) return new Color(255, 253, 200);
        else if (getEnergy() < starting / 2) return new Color(255, 253, 170);
        else if (getEnergy() < starting * 3 / 4) return new Color(255, 253, 120);
        else if (getEnergy() < starting) return new Color(255, 253, 80);
        else if (getEnergy() < 2 * starting) return new Color(255, 253, 50);
        else if (getEnergy() < 5 * starting) return new Color(255, 253, 25);
        else if (getEnergy() < 10 * starting) return new Color(255, 253, 0);
        else return new Color(255, 0, 0);
    }

    public int getBestGene() {
        int[] tmp = new int[8];
        for (int k = 0; k < 8; k++) {
            tmp[k] = 0;
        }
        for (int i = 0; i < 32; i++) {
            tmp[getGenes()[i]] += 1;
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

    public void sortGenes() {
        Arrays.sort(this.genes);
    }

    public boolean isDead(int day) {
        if (getEnergy() <= 0) {
            setDeathDay(day);
            return true;
        }
        return false;
    }

    public boolean dead() {
        return getDeathDay() != 0;
    }

    public int howManyAncestors() {
        if (getChildren().size() == 0) {
            return 0;
        } else {
            int sum = 0;
            for (Animal child : getChildren()) {
                sum += 1 + child.howManyAncestors();
            }
            return sum;
        }
    }

    public void setMap(IWorldMap map) {
        this.map = map;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public int getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(int deathDay) {
        this.deathDay = deathDay;
    }

    public List<Animal> getChildren() {
        return children;
    }

    public void setChildren(List<Animal> children) {
        this.children = children;
    }
}
