/**
 * Sheep
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;

public class Sheep extends GridObject {

    Sheep() {
        super();
        super.addHealth(50 + (int)(Math.random() * 51)); //Random health from 50-100 (inclusive)
    }

    Sheep(int health) {
        super();
        super.addHealth(health);
    }

    public int findTarget(ArrayList<GridObject> options) {

        ArrayList<GridObject> newOptions = (ArrayList)options.clone();

        //Shuffle the array to make sure we don't create a tendency to move in a particular direction
        Collections.shuffle(newOptions);

        //Try to mate first, if the sheep is healthy enough for mating it's good on food for now
        for (GridObject o : newOptions) {
            if ( //Mate with an available sheep
                    o instanceof Sheep &&
                    o.getGender() != super.getGender() &&
                    o.getHealth() >= EcoSim.MIN_MATE_HEALTH && super.getHealth() >= EcoSim.MIN_MATE_HEALTH
                    ) {
                return options.indexOf(o);
            }
        }

        //Next, try to eat
        for (GridObject o : newOptions) {
            if (o instanceof Plant) {
                return options.indexOf(o);
            }
        }

        //Pick a random empty square and move to it
        //We need a slightly different method of iteration here
        //This is because options.indexOf(o) where o is null returns the first null value, as null cannot be unique
        return super.findNullSpace(options);
    }
}
