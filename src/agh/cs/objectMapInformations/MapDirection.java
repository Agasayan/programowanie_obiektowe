package agh.cs.objectMapInformations;

public enum MapDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST;

    public String toString() {
        switch (this) {
            case EAST:
                return "Wschod";
            case WEST:
                return "Zachod";
            case NORTH:
                return "Polnoc";
            case SOUTH:
                return "Poludnie";
            default:
                return "Zly kierunek";
        }
    }

    public static MapDirection next(MapDirection direction) {
        switch (direction) {
            case NORTH:
                return NORTH_EAST;
            case NORTH_EAST:
                return EAST;
            case EAST:
                return SOUTH_EAST;
            case SOUTH_EAST:
                return SOUTH;
            case SOUTH:
                return SOUTH_WEST;
            case SOUTH_WEST:
                return WEST;
            case WEST:
                return NORTH_WEST;
            default:
                return NORTH;
        }
    }

    public static MapDirection previous(MapDirection direction) {
        switch (direction) {
            case NORTH:
                return NORTH_WEST;
            case NORTH_EAST:
                return NORTH;
            case EAST:
                return NORTH_EAST;
            case SOUTH_EAST:
                return EAST;
            case SOUTH:
                return SOUTH_EAST;
            case SOUTH_WEST:
                return SOUTH;
            case WEST:
                return SOUTH_WEST;
            default:
                return WEST;
        }
    }

    public Vector2d toUnitVector(MapDirection direction) {
        switch (direction) {
            case NORTH:
                return new Vector2d(0, 1);
            case EAST:
                return new Vector2d(1, 0);
            case SOUTH:
                return new Vector2d(0, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTH_WEST:
                return new Vector2d(-1,1);
            case NORTH_EAST:
                return new Vector2d(1,1);
            case SOUTH_WEST:
                return new Vector2d(-1,-1);
            case SOUTH_EAST:
                return new Vector2d(1,-1);
            default:
                return new Vector2d(0, 0);
        }
    }
}
