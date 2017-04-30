/**
 * Wolf
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;

public class Wolf extends GridObject{

    Wolf() {
        super();
        super.addHealth(50 + (int)(Math.random() * 51)); //Random health from 50-100 (inclusive)
    }

    Wolf(int health) {
        super();
        super.addHealth(health);
    }

    public int findTarget(ArrayList<GridObject> options) {
        ArrayList<GridObject> newOptions = (ArrayList)options.clone();

        //Shuffle the array to make sure we don't create a tendency to move in a particular direction
        Collections.shuffle(newOptions);

        //Try to mate first, if the wolf is healthy enough for mating it's good on food for now
        for (GridObject o : newOptions) {
            if ( //Mate with an available wolf
                    o instanceof Wolf &&
                            o.getGender() != super.getGender() &&
                            o.getHealth() >= EcoSim.MIN_MATE_HEALTH && super.getHealth() >= EcoSim.MIN_MATE_HEALTH
                    ) {
                return options.indexOf(o);
            }
        }

        //Next, try to eat a sheep
        for (GridObject o : newOptions) {
            if (o instanceof Sheep) {
                return options.indexOf(o);
            }
        }

        //Next, try to fight another male wolf
        for (GridObject o : newOptions) {
            if (o instanceof Wolf && o.getGender() && super.getGender()) {
                return options.indexOf(o);
            }
        }

        //Pick a random empty square and move to it
        //We need a slightly different method of iteration here
        //This is because options.indexOf(o) where o is null returns the first null value, as null cannot be unique
        return super.findNullSpace(options);
    }
}
