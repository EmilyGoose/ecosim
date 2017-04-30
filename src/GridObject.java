/**
 * GridObject
 * Generic grid object for ECO sim
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

abstract class GridObject implements Comparable<GridObject>{

    private int health;
    private boolean gender;
    private int lastUpdated = -1;

    GridObject() {
        Random random = new Random();
        this.gender =  random.nextBoolean();
        this.health = 0;
        //Health is later set individually in the subclasses
    }

    public void addHealth(int amount) {
        this.health += amount;
    }
    public int getHealth() { return this.health; }

    public int getLastUpdated() { return this.lastUpdated; }
    public void setLastUpdated(int update) { this.lastUpdated = update; }

    public boolean getGender() { return this.gender; }

    public void takeDamage(int amount) {
        this.health -= amount;
    }

    public int compareTo(GridObject object) {
        return (this.health - object.getHealth());
    }

    //This is here because it's the same for wolves and sheep
    public int findNullSpace(ArrayList<GridObject> options) {
        if (options.contains(null)) {
            ArrayList<Integer> nulls = new ArrayList<>(0);
            for (int i = 0; i < options.size(); i ++) {
                if (options.get(i) == null) {
                    nulls.add(i);
                }
            }
            Collections.shuffle(nulls);
            return nulls.get(0);
        } else {
            //Give up and don't move
            return -1;
        }
    }

    abstract int findTarget(ArrayList<GridObject> options);
}
