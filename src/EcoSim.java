/*
 * EcoSim
 * Main project file for ECOsim
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

//IntelliJ-specific line to stop annoying "access can be package-private" warnings
@SuppressWarnings("WeakerAccess")

public class EcoSim {

    //See README.md for full list of constants and their uses

    //Program constants
    private static final int GRID_SIZE = 25;
    private static final int ITERATIONS = 1000;
    private static final int TICK_LENGTH = 100; //Milliseconds per grid refresh

    //World constants. Not guaranteed but they'll be close.
    private static final double WORLD_FILL_DENSITY = 0.3;
    //WOLF_DENSITY + SHEEP_DENSITY + PLANT_DENSITY = 1
    private static final double WOLF_DENSITY = 0.1;
    private static final double SHEEP_DENSITY = 0.6;
    //We don't need to declare PLANT_DENSITY because we get it algebraically
    private static final double GROWTH_RATE = 0.1; //Chance a new plant will spawn in a null spot

    //Animal health
    public static final int MAX_WOLF_HEALTH = 100;
    public static final int MAX_SHEEP_HEALTH = 150;

    //Mating-related constants
    public static final int MIN_MATE_HEALTH_SHEEP = 40;
    public static final int MIN_MATE_HEALTH_WOLF = 60;
    public static final int BABY_HEALTH_SHEEP = 20;
    public static final int BABY_HEALTH_WOLF = 40;

    //Attack-related constants
    private static final double STRUGGLE_CHANCE = 0.6; //Chance a wolf will be unable to attack a stronger sheep
    private static final double EATING_HEALTH_RATIO = 0.2; //Multiplied by an eaten sheep's health before adding to wolf
    private static final double FIGHT_DAMAGE_LOSER = 0.8;
    private static final double FIGHT_DAMAGE_WINNER = 0.2;

    private static GridObject[][] map;

    public static void main(String[] args) throws Exception {
        map = new GridObject[GRID_SIZE][GRID_SIZE];

        //Set up the grid to display everything
        DisplayGrid grid = new DisplayGrid(map);

        //Populate the grid
        for (int col = 0; col < GRID_SIZE; col++) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //Check if the tile even needs to be filled in the first place
                if (Math.random() <= WORLD_FILL_DENSITY) {
                    //Choose the GridObject we're going to spawn
                    double randomType = Math.random();
                    //Spawn a GridObject
                    if (randomType <= WOLF_DENSITY) {
                        map[col][row] = new Wolf();
                    } else if (randomType <= WOLF_DENSITY + SHEEP_DENSITY) {
                        map[col][row] = new Sheep();
                    } else {
                        //Spawn plants that have already been here for a bit so sheep can eat
                        int min = 2;
                        int max = 5;
                        int newAge = new Random().nextInt((max - min) + 1) + min;
                        map[col][row] = new Plant(newAge);
                    }
                }
            }
        }

        //Initial grid draw
        grid.refresh();


        //Run the specified number of iterations
        for(int i = 0; i < ITERATIONS; i++) {

            //Spawn plants based on rates
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (map[row][col] == null && Math.random() <= GROWTH_RATE) {
                        map[row][col] = new Plant();
                    }
                }
            }

            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    EcoSim.updateObject(col, row, map, i);
                }
            }

            grid.refresh();

            //Make the thread sleep to allow the user to see the result of the tick
            try{ Thread.sleep(TICK_LENGTH); }catch(Exception e) { System.out.println("Couldn't sleep for some reason."); }
        }
    }

    private static void updateObject(int x, int y, GridObject[][] map, int currentIter) {
        GridObject object = map[y][x];
        //Make sure the object is an animal we haven't moved yet
        if (object != null && !(object instanceof Plant) && object.getLastUpdated() < currentIter) {

            //Create the ArrayList instances to be passed to the object
            //Max capacity is 8 so we initialize as such
            //This saves us from constantly reallocating memory (Thanks Adam!)
            ArrayList<GridObject> options = new ArrayList<>(8);
            ArrayList<int[]> optionCoords = new ArrayList<>(8);

            //Note: Animals can move diagonally
            for (int col = -1; col <= 1; col += 1) {
                if (y + col >= 0 && y + col < GRID_SIZE) { //If the outer loop is out of bounds we don't need an inner
                    for (int row = -1; row <= 1; row += 1) {
                        if (x + row >= 0 && x + row < GRID_SIZE) { //Make sure it's not out of bounds
                            if (!(row == 0 && col == 0)) { //Make sure non-movement isn't on the list
                                options.add(map[y+col][x+row]);
                                optionCoords.add(new int[]{y + col, x + row});
                            }
                        }
                    }
                }
            }

            //Get the animal to find a target it likes (food, mate, etc...)
            int arrayIndex = object.findTarget(options);

            int[] coords;

            if (arrayIndex > -1) { //-1 is returned if the object doesn't move
                coords = optionCoords.get(arrayIndex);

                int newY = coords[0];
                int newX = coords[1];

                //Make sure the animal is actually moving
                if ((newY != y || newX != x) && (newY < GRID_SIZE && newX < GRID_SIZE && newY >= 0 && newX >= 0)) {
                    GridObject targetSpot = map[newY][newX];

                    if (targetSpot != null) {
                        if (object instanceof Sheep) {
                            if (targetSpot instanceof Plant && targetSpot.getHealth() > 0) {
                                //Yum!
                                object.addHealth(targetSpot.getHealth());
                                //Move, overwriting the plant
                                map[newY][newX] = map[y][x];
                                map[y][x] = null;
                            } else if (
                                    targetSpot instanceof Sheep &&
                                    targetSpot.getGender() != object.getGender() && //Opposite genders
                                    targetSpot.getHealth() >= MIN_MATE_HEALTH_SHEEP &&
                                    object.getHealth() >= MIN_MATE_HEALTH_SHEEP //Enough health
                                    ) {
                                //Spawn a sheep

                                //Max capacity is 16 so we initialize as such
                                //This saves us from constantly reallocating memory (Thanks Adam!)
                                ArrayList<int[]> emptySpotsNearby = getEmptySpotsNearby(x, y, newX, newY, map);

                                //If there are no empty spots, the area is overcrowded and the sheep cannot give birth
                                if (emptySpotsNearby.size() > 0) {
                                    int[] newSheepSpot = emptySpotsNearby.get(0);
                                    //Spawn the sheep
                                    map[newSheepSpot[0]][newSheepSpot[1]] = new Sheep(BABY_HEALTH_SHEEP);
                                    //Damage the parents
                                    targetSpot.takeDamage(BABY_HEALTH_SHEEP/2);
                                    object.takeDamage(BABY_HEALTH_SHEEP/2);
                                }
                            }
                        } else if (object instanceof Wolf) {
                            if (targetSpot instanceof Sheep) {
                                if (object.compareTo(targetSpot) > 0 || Math.random() <= STRUGGLE_CHANCE) {
                                    //Yum!
                                    object.addHealth((int)(targetSpot.getHealth() * EATING_HEALTH_RATIO));
                                    //Move, overwriting the sheep
                                    map[newY][newX] = map[y][x];
                                    map[y][x] = null;
                                }
                                //If previous if statement is false, the wolf was too weak! No sheep for him!
                                //The sheep used to kill the wolf but apparently that "never happens in the world"
                            } else if (targetSpot instanceof Wolf && object.getGender() && targetSpot.getGender()) {
                                //Male wolves fight
                                if (object.compareTo(targetSpot) > 0) {
                                    //Wolves take damage scaled based on the other wolf's health
                                    targetSpot.takeDamage((int)(FIGHT_DAMAGE_LOSER * object.getHealth()));
                                    object.takeDamage((int)(FIGHT_DAMAGE_WINNER * targetSpot.getHealth()));
                                } else if (object.compareTo(targetSpot) < 0) {
                                    object.takeDamage((int)(FIGHT_DAMAGE_LOSER * targetSpot.getHealth()));
                                    targetSpot.takeDamage((int)(FIGHT_DAMAGE_WINNER * object.getHealth()));
                                } else {
                                    //Both wolves take reduced damage
                                    object.takeDamage((int)(FIGHT_DAMAGE_WINNER * targetSpot.getHealth()));
                                    targetSpot.takeDamage((int)(FIGHT_DAMAGE_WINNER * object.getHealth()));
                                }
                            } else if (
                                    targetSpot instanceof Wolf &&
                                    targetSpot.getGender() != object.getGender() && //Opposite genders
                                    targetSpot.getHealth() >= MIN_MATE_HEALTH_WOLF &&
                                    object.getHealth() >= MIN_MATE_HEALTH_WOLF //Enough health
                                    ) {
                                //Spawn a wolf

                                //Max capacity is 16 so we initialize as such
                                //This saves us from constantly reallocating memory (Thanks Adam!)
                                ArrayList<int[]> emptySpotsNearby = getEmptySpotsNearby(x, y, newX, newY, map);

                                //If there are no empty spots, the area is overcrowded and the sheep cannot give birth
                                if (emptySpotsNearby.size() > 0) {
                                    int[] newWolfSpot = emptySpotsNearby.get(0);
                                    //Spawn the wolf
                                    map[newWolfSpot[0]][newWolfSpot[1]] = new Wolf(BABY_HEALTH_WOLF);
                                    //Damage the parents
                                    targetSpot.takeDamage(BABY_HEALTH_WOLF/2);
                                    object.takeDamage(BABY_HEALTH_WOLF/2);
                                }
                            }
                        }
                    } else {
                        //just move
                        map[newY][newX] = map[y][x];
                        map[y][x] = null;
                    }


                }
            }

            //Time-based damage (end of tick)
            object.takeDamage(1);
            if (object.getHealth() <= 0) {
                map[y][x] = null;
            }

        }

        //Flag the object as updated
        if (object != null) {
            object.setLastUpdated(currentIter);
            if (object instanceof Plant) {
                if (object.getHealth() < 0) {
                    map[y][x] = null;
                } else {
                    ((Plant) object).addAge();
                }
            }
        }
    }

    private static boolean hasSpaceLeft() {
        for (GridObject[] row : map) {
            for (GridObject rowItem : row) {
                if (rowItem == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int[] countObjects() {
        //Structure: [plants, sheep, wolves]
        int[] returnedValues = new int[]{0,0,0};

        //Iterate through and count values
        for (GridObject[] row : map) {
            for (GridObject o : row) {
                if (o instanceof Plant) {
                    returnedValues[0] += 1;
                } else if (o instanceof  Sheep) {
                    returnedValues[1] += 1;
                } else if (o instanceof  Wolf) {
                    returnedValues[2] += 1;
                }
            }
        }

        return returnedValues;
    }

    private static ArrayList<int[]> getEmptySpotsNearby(int x, int y, int newX, int newY, GridObject[][] map) {
        ArrayList<int[]> emptySpotsNearby = new ArrayList<>(16);

        for (int col = -1; col <= 1; col += 1) {
            for (int row = -1; row <= 1; row += 1) {
                if (y + col >= 0 &&
                        y + col < GRID_SIZE &&
                        x + row >= 0 &&
                        x + row < GRID_SIZE &&
                        map[y + col][x + row] == null
                        ) {
                    emptySpotsNearby.add(new int[]{y + col, x + row});
                }
                if (newY + col >= 0 &&
                        newY + col < GRID_SIZE &&
                        newX + row >= 0 &&
                        newX + row < GRID_SIZE &&
                        map[newY + col][newX + row] == null
                        ) {
                    emptySpotsNearby.add(new int[]{newY + col, newX + row});
                }
            }
        }
        Collections.shuffle(emptySpotsNearby);

        return emptySpotsNearby;
    }
}