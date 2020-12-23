package agh.cs.mapElements;

import agh.cs.objectMapInformations.Vector2d;

import java.awt.*;

public class Grass {
    private Vector2d position;

    public Grass(Vector2d vector) {
        this.setPosition(vector);
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public String toString() {
        return "*";
    }

    public Color toColor() {
        return new Color(0, 240, 50);
    }

}
