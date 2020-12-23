package agh.cs.objectMapInformations;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString(int x, int y) {
        String s_x = Integer.toString(x);
        String s_y = Integer.toString(y);
        System.out.println("(" + s_x + "," + s_y + ")");
        return ("(" + s_x + "," + s_y + ")");
    }

    public boolean precedes(Vector2d other) {
        if (this.x <= other.x && this.y <= other.y) {
            return true;
        } else return false;
    }

    public boolean follows(Vector2d other) {
        if (this.x >= other.x && this.y <= other.y) {
            return true;
        } else return false;
    }

    public Vector2d upperRight(Vector2d other) {
        int max_x = Integer.max(this.x, other.x);
        int max_y = Integer.max(this.y, other.y);
        return new Vector2d(max_x, max_y);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int min_x = Integer.min(this.x, other.x);
        int min_y = Integer.min(this.y, other.y);
        return new Vector2d(min_x, min_y);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }



    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if (this.x == that.x && this.y == that.y) {
            return true;
        } else return false;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += this.x * 37;
        hash += this.y * 39;
        return hash;
    }
}
