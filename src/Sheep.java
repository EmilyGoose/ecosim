/*
 * Sheep
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Sheep extends GridObject {

    Sheep() {
        super();
        int min = 50;
        int max = 100;
        int health = new Random().nextInt((max - min) + 1) + min;
        super.addHealth(health); //Random health from 50-100 (inclusive)
    }

    Sheep(int health) {
        super();
        this.addHealth(health);
    }

    @Override
    public void addHealth(int health) {
        if (this.getHealth() + health > EcoSim.MAX_SHEEP_HEALTH) {
            //Make sure health doesn't exceed the maximum
            health = EcoSim.MAX_SHEEP_HEALTH - this.getHealth();
        }
        super.addHealth(health);
    }

    public int findTarget(ArrayList<GridObject> options) {

        //Clone the array
        ArrayList<GridObject> newOptions = new ArrayList<>(options);

        //Shuffle the array to make sure we don't create a tendency to move in a particular direction
        Collections.shuffle(newOptions);

        int firstSheep = -1;
        int firstPlant = -1;

        for (int i = 0; i < newOptions.size(); i++) {
            GridObject o = newOptions.get(i);
            if ( //Mate with an available sheep
                    o instanceof Sheep &&
                            o.getGender() != super.getGender() &&
                            o.getHealth() >= EcoSim.MIN_MATE_HEALTH_SHEEP && super.getHealth() >= EcoSim.MIN_MATE_HEALTH_SHEEP
                    ) {
                firstSheep = i;
                break;
            } else if (firstPlant == -1 && o instanceof Plant) {
                firstPlant = i;
            }
        }

        if (firstSheep >= 0) {
            return options.indexOf(newOptions.get(firstSheep));
        } else if (firstPlant >= 0) {
            return options.indexOf(newOptions.get(firstPlant));
        } else {
            //Pick a random empty square and move to it
            //We need a slightly different method of iteration here
            //This is because options.indexOf(o) where o is null returns the first null value, as null cannot be unique
            return super.findNullSpace(options);
        }
    }
}
