package agh.cs.map;

import agh.cs.configuration.Configuration;
import agh.cs.objectMapInformations.MapDirection;
import agh.cs.mapElements.Animal;
import agh.cs.objectMapInformations.MoveDirection;
import agh.cs.objectMapInformations.Vector2d;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractWorldMap implements IWorldMap {
    public Map<Vector2d, List<Animal>> animals_map = new HashMap<>();
    public RectangularMap area;
    private final int moveEnergy = Configuration.getInstance().getMoveCost();
    private int day;


    @Override
    public boolean canMoveTo(Vector2d position) {
        if (area.getPositions().contains(position))
            return true;
        return false;
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (this.canMoveTo(position)) {
            if (this.animals_map.get(position) == null) {
                List<Animal> tmp_animal_list = new ArrayList<>();
                tmp_animal_list.add(animal);
                this.animals_map.put(position, tmp_animal_list);
            } else {
                List<Animal> tmp_animal_list = animals_map.get(position);
                tmp_animal_list.add(animal);
            }
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        // I'm creating animals list first, because otherwise I would have problems with the fact, that I'm
        // removing and placing again new keys and values in hashmap, so order could be flawed
        List<Animal> animals_list = getAnimalsToList();
        int n = animals_list.size();
        for (int i = 0; i < n; i++) {
            Animal tmp_animal = animals_list.get(i);
            tmp_animal.setNewDirection();
            this.animalMoveFunction(tmp_animal);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (this.animals_map.get(position) != null) {
            return true;
        }
        return false;
    }


    @Override
    public Object objectAt(Vector2d position) {
        Object tmp_obj = this.animals_map.get(position);
        if (tmp_obj != null) {
            return tmp_obj;
        }
        return grass_objects(position);
    }


    @Override
    public abstract IWorldMap copy();

    public abstract Object grass_objects(Vector2d position);


    public abstract Map<Vector2d, List<Animal>> getAnimalsMap();

    // get all animals from hashmap into list
    public List<Animal> getAnimalsToList() {
        List<Animal> animals_list = new ArrayList<>();
        for (Map.Entry<Vector2d, List<Animal>> entry : this.animals_map.entrySet()) {
            animals_list.addAll(entry.getValue());
        }
        return animals_list;
    }

    // I decided to slice long code into 3 functions, because it is easier to read that way, in my opinion :)
    private void animalMoveFunction(Animal animal) {
        Vector2d old_position = animal.getPosition();
        if (animal.move(MoveDirection.FORWARD)) {
            Vector2d new_position = animal.getPosition();
            animal.setEnergy(animal.getEnergy() - moveEnergy);
            List<Animal> old_position_animals = this.animals_map.get(old_position);
            if (old_position_animals.size() == 1) {
                onlyOneAnimalMove(old_position, new_position, animal, old_position_animals);
            } else {
                multipleAnimalsCaseMove(old_position, new_position, animal, old_position_animals);
            }
        }
    }

    // function in case of only one animal being under current key
    private void onlyOneAnimalMove(Vector2d old_position, Vector2d new_position, Animal animal, List<Animal> old_position_animals) {
        this.animals_map.remove(old_position);
        if (animals_map.get(new_position) == null) {
            List<Animal> new_position_animals = new ArrayList<>();
            new_position_animals.add(animal);
            this.animals_map.put(new_position, new_position_animals);
        } else {
            this.animals_map.get(new_position).add(old_position_animals.get(0));
        }
    }

    // function in case of multiple animals being under current key
    private void multipleAnimalsCaseMove(Vector2d old_position, Vector2d new_position, Animal animal, List<Animal> old_position_animals) {
        int counter = 0;
        for (int k = 0; k < old_position_animals.size(); k++) {
            if (old_position_animals.get(k).getPosition().equals(new_position)) {
                List<Animal> new_position_animals = this.animals_map.get(new_position);
                if (new_position_animals != null) {
                    new_position_animals.add(old_position_animals.get(counter));
                } else {
                    new_position_animals = new ArrayList<>();
                    new_position_animals.add(old_position_animals.get(counter));
                    this.animals_map.put(new_position, new_position_animals);
                }
                old_position_animals.remove(counter);
                this.animals_map.replace(old_position, old_position_animals);
                this.animals_map.replace(new_position, new_position_animals);
                break;
            } else {
                counter++;
            }
        }
    }

    // function for creating children where it is possible
    @Override
    public List<Animal> proliferation(List<Animal> animals) {
        List<Animal> animalsToAdd = new ArrayList<>();
        if (animals.size() > 1) {
            Vector2d curr_position = animals.get(0).getPosition();
            List<Animal> parents = this.get2StrongestAnimals(animals);
            int halfStartEnergy = (1 / 2) * Configuration.getInstance().getAnimalEnergy();
            if (parents.get(0).getEnergy() >= halfStartEnergy && parents.get(0).getEnergy() >= halfStartEnergy) {
                Vector2d birthplace = getDeliveryPosition(curr_position);
                Animal child = new Animal(this, birthplace);
                child.setGenes(passGenes(parents.get(0), parents.get(1)));
                createFamily(parents.get(0), parents.get(1), child);
                child.setEnergy(parents.get(0).getEnergy() / 4 + parents.get(1).getEnergy() / 4);
                parents.get(0).setEnergy(parents.get(0).getEnergy() - parents.get(0).getEnergy() / 4);
                parents.get(1).setEnergy(parents.get(1).getEnergy() - parents.get(1).getEnergy() / 4);
                child.setBirthDay(getDay());
                animalsToAdd.add(child);
            }
        }

        return animalsToAdd;
    }

    // function for creating mother and father on current position
    protected List<Animal> get2StrongestAnimals(List<Animal> list) {
        Animal father = new Animal();
        Animal mother = new Animal();
        for (Animal animal_in_list : list) {
            if (animal_in_list.getEnergy() > father.getEnergy()) {
                mother = father;
                father = animal_in_list;
            } else if (animal_in_list.getEnergy() > mother.getEnergy()) {
                mother = animal_in_list;
            }
        }
        Animal finalFather = father;
        Animal finalMother = mother;
        return new ArrayList<Animal>() {
            {
                add(finalFather);
                add(finalMother);
            }
        };
    }

    // checking possible new animal birth positions
    private Vector2d getDeliveryPosition(Vector2d curr_position) {
        List<Vector2d> free_positions = new ArrayList<>();
        List<Vector2d> all_positions = new ArrayList<>();
        MapDirection util_instance = MapDirection.NORTH;
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH)));
        }
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH_EAST)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH_EAST))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH_EAST)));
        }
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.EAST)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.EAST))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.EAST)));
        }
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH_EAST)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH_EAST))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH_EAST)));
        }
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH)));
        }
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH_WEST)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH_WEST))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.SOUTH_WEST)));
        }
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.WEST)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.WEST))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.WEST)));
        }
        all_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH_WEST)));
        if (!(this.objectAt(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH_WEST))) instanceof Animal)) {
            free_positions.add(curr_position.add(util_instance.toUnitVector(MapDirection.NORTH_WEST)));
        }
        if (free_positions.size() > 1) {
            int n = ThreadLocalRandom.current().nextInt(0, free_positions.size());
            return free_positions.get(n);
        } else {
            int n = ThreadLocalRandom.current().nextInt(0, 7);
            return all_positions.get(n);
        }
    }

    // function for passing genes to the child
    private int[] passGenes(Animal father, Animal mother) {
        int size = 32;
        int x1 = ThreadLocalRandom.current().nextInt(1, size - 2);
        int x2 = ThreadLocalRandom.current().nextInt(x1, size - 1);
        int[] genes1 = father.getGenes();
        int[] genes2 = mother.getGenes();
        int[] childGenes = new int[32];
        System.arraycopy(genes1, 0, childGenes, 0, x1);
        System.arraycopy(genes2, x1, childGenes, x1, x2 - x1);
        System.arraycopy(genes1, x2, childGenes, x2, 32 - x2);
        return childGenes;
    }

    // create happy animal family
    private void createFamily(Animal father, Animal mother, Animal child) {
        father.getChildren().add(child);
        mother.getChildren().add(child);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
