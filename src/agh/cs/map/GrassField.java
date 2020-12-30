package agh.cs.map;

import agh.cs.configuration.Configuration;
import agh.cs.mapElements.Animal;
import agh.cs.objectMapInformations.Vector2d;
import agh.cs.mapElements.Grass;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GrassField extends AbstractWorldMap {
    public Map<Vector2d, Grass> grassList = new HashMap<>();
    private final int plantEnergy = Configuration.getInstance().getGrassEnergy();
    private int currDayAnimalsBorn = 0;
    private int currDayGrassEaten = 0;
    private final int grassSpawned = 5;

    public GrassField() {
    }

    public GrassField(RectangularMap map) {
        super.area = map;
    }


    @Override
    public Object grass_objects(Vector2d position) {
        Object tmp_obj = this.grassList.get(position);
        if (tmp_obj != null) {
            return tmp_obj;
        }
        return null;
    }


    @Override
    public Map<Vector2d, List<Animal>> getAnimalsMap() {
        return this.animals_map;
    }

    public void add_grass(Vector2d position) {
        this.grassList.put(position, new Grass(position));
    }

    @Override
    public IWorldMap copy() {
        GrassField tmp = new GrassField();
        //tmp.grassList = new HashMap<>(this.grassList);
        tmp.animals_map = new HashMap<>(this.animals_map);
        return tmp;
    }

    public Vector2d getUnoccupiedPosition() {
        int a = area.getWidth();
        int b = area.getHeight();
        int x = ThreadLocalRandom.current().nextInt(0, a);
        int y = ThreadLocalRandom.current().nextInt(0, b);
        while (isOccupied(new Vector2d(x, y))) {
            x = ThreadLocalRandom.current().nextInt(0, a);
            y = ThreadLocalRandom.current().nextInt(0, b);
        }
        return new Vector2d(x, y);
    }

    public Map<Vector2d, Grass> getGrassList() {
        return grassList;
    }

    public void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        List<Animal> value = animals_map.get(position);
        if (value.size() == 1) {
            animals_map.remove(position);
        } else {
            value.remove(animal);
        }
    }

    public void grassGeneration(RectangularMap map, RectangularMap jungle) {
        for (int i = 0; i < getGrassSpawned(); i++) {
            Vector2d grass1 = fieldInJungle(map);
            grassList.put(grass1, new Grass(grass1));
        }
        Vector2d grass2 = fieldOutOfJungle(jungle);
        grassList.put(grass2, new Grass(grass2));

    }

    // get free field for grass in the jungle
    private Vector2d fieldInJungle(RectangularMap area) {
        List<Vector2d> freePositions = new ArrayList<>();
        for (Vector2d position : area.getPositions()) {
            if (!this.grassList.containsKey(position) && !this.animals_map.containsKey(position)) {
                freePositions.add(position);
            }
        }
        if (!(freePositions.size() == 0)) {
            int n = ThreadLocalRandom.current().nextInt(freePositions.size());
            return freePositions.get(n);
        }
        return null;
    }

    // get free field for grass out of the jungle
    private Vector2d fieldOutOfJungle(RectangularMap area) {
        List<Vector2d> freePositions = new ArrayList<>();
        for (Vector2d position : area.getPositions()) {
            if (!this.grassList.containsKey(position) && !this.getAnimalsMap().containsKey(position)) {
                freePositions.add(position);
            }
        }
        if (!(freePositions.size() == 0)) {
            int n = ThreadLocalRandom.current().nextInt(freePositions.size());
            return freePositions.get(n);
        }
        return null;
    }

    // make this part of map green: partOfMap = 4 => 1/4 part of jungle and map filled with grass
    public void makeMapGreen(int partOfMap, RectangularMap map, RectangularMap jungle) {

        List<Vector2d> freePositionsInMap = new ArrayList<>();
        for (Vector2d position : map.getPositions()) {
            if (!this.grassList.containsKey(position)) {
                freePositionsInMap.add(position);
            }
        }

        List<Vector2d> freePositionsInJungle = new ArrayList<>();
        for (Vector2d position : map.getPositions()) {
            if (!this.grassList.containsKey(position)) {
                freePositionsInJungle.add(position);
            }
        }

        int mapX = map.getWidth();
        int mapY = map.getHeight();
        int jungleX = jungle.getWidth();
        int jungleY = jungle.getHeight();
        int mapFields = mapX * mapY / partOfMap;
        int jungleFields = jungleX * jungleY / partOfMap;


        for (int i = 0; i < mapFields; i++) {
            int n = ThreadLocalRandom.current().nextInt(freePositionsInMap.size());
            add_grass(freePositionsInMap.get(n));
        }
        for (int i = 0; i < jungleFields; i++) {
            int n = ThreadLocalRandom.current().nextInt(freePositionsInJungle.size());
            add_grass(freePositionsInJungle.get(n));
        }
    }

    public void makeAllAnimalsActions() {
        List<Animal> animalsBorn = new ArrayList<>();
        List<Grass> grassesEaten = new ArrayList<>();
        for (Map.Entry<Vector2d, List<Animal>> entry : getAnimalsMap().entrySet()) {
            List<Animal> currAnimals = entry.getValue();
            Vector2d currKey = entry.getKey();
            animalsBorn.addAll(proliferation(currAnimals));
            grassesEaten.add(eatGrass(currAnimals));
        }
        this.setCurrDayAnimalsBorn(animalsBorn.size());
        this.setCurrDayGrassEaten(grassesEaten.size());
        for (Animal animal : animalsBorn) {
            Vector2d key = animal.getPosition();
            if (getAnimalsMap().get(key) == null) {
                getAnimalsMap().put(key, new ArrayList<Animal>(Arrays.asList(animal)));
            } else {
                getAnimalsMap().get(key).add(animal);
            }
        }
    }

    public Grass eatGrass(List<Animal> animalsToFeed) {
        if (getGrassList().get(animalsToFeed.get(0).getPosition()) == null) {
            return null;
        } else {
            animalsToFeed = get2StrongestAnimals(animalsToFeed);
            Vector2d key = animalsToFeed.get(0).getPosition();
            if (animalsToFeed.size() == 2) {
                animalsToFeed.get(0).setEnergy(animalsToFeed.get(0).getEnergy() + plantEnergy / 2);
                animalsToFeed.get(1).setEnergy(animalsToFeed.get(1).getEnergy() + plantEnergy / 2);
            } else {
                animalsToFeed.get(0).setEnergy(animalsToFeed.get(0).getEnergy() + plantEnergy);
            }
            getGrassList().remove(key);
        }
        return getGrassList().get(animalsToFeed.get(0).getPosition());
    }

    public int getCurrDayAnimalsBorn() {
        return currDayAnimalsBorn;
    }

    public void setCurrDayAnimalsBorn(int currDayAnimalsBorn) {
        this.currDayAnimalsBorn = currDayAnimalsBorn;
    }

    public int getCurrDayGrassEaten() {
        return currDayGrassEaten;
    }

    public void setCurrDayGrassEaten(int currDayGrassEaten) {
        this.currDayGrassEaten = currDayGrassEaten;
    }

    public int getGrassSpawned() {
        return grassSpawned;
    }
}
