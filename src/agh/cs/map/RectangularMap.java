package agh.cs.map;

import agh.cs.mapElements.Animal;
import agh.cs.objectMapInformations.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectangularMap extends AbstractWorldMap {
    private Vector2d bottom_left;
    private Vector2d upper_right;
    private List<Vector2d> fields = new ArrayList<>();

    public RectangularMap(Vector2d v1, Vector2d v2) {
        this.setBottom_left(v1);
        this.setUpper_right(v2);
        for (int i = getBottom_left().x; i < getWidth(); i++) {
            for (int j = getBottom_left().y; j < getHeight(); j++) {
                this.getFields().add(new Vector2d(i, j));
            }
        }
    }

    @Override
    public Object grass_objects(Vector2d position) {
        return null;
    }


    @Override
    public Map<Vector2d, List<Animal>> getAnimalsMap() {
        return this.animals_map;
    }

    @Override
    public IWorldMap copy() {
        RectangularMap tmp = new RectangularMap(this.getBottom_left(), this.getUpper_right());
        tmp.animals_map = new HashMap<>(this.animals_map);
        return tmp;
    }


    public int getWidth() {
        return this.getUpper_right().x - this.getBottom_left().x + 1;
    }

    public int getHeight() {
        return this.getUpper_right().y - this.getBottom_left().y + 1;
    }

    public List<Vector2d> getPositions() {
        return this.getFields();
    }


    public Vector2d getBottom_left() {
        return bottom_left;
    }

    public void setBottom_left(Vector2d bottom_left) {
        this.bottom_left = bottom_left;
    }

    public Vector2d getUpper_right() {
        return upper_right;
    }

    public void setUpper_right(Vector2d upper_right) {
        this.upper_right = upper_right;
    }

    public List<Vector2d> getFields() {
        return fields;
    }

    public void setFields(List<Vector2d> fields) {
        this.fields = fields;
    }
}
