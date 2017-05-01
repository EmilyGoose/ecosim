/*
 * Wolf
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Wolf extends GridObject{

    Wolf() {
        super();
        int min = 50;
        int max = 100;
        int health = new Random().nextInt((max - min) + 1) + min;
        super.addHealth(health); //Random health from 50-100 (inclusive)
    }

    Wolf(int health) {
        super();
        super.addHealth(health);
    }

    @Override
    public void addHealth(int health) {
        if (this.getHealth() + health > EcoSim.MAX_WOLF_HEALTH) {
            //Make sure health doesn't exceed the maximum
            health = EcoSim.MAX_WOLF_HEALTH - this.getHealth();
        }
        super.addHealth(health);
    }

    public int findTarget(ArrayList<GridObject> options) {

        //Clone the array
        ArrayList<GridObject> newOptions = new ArrayList<>(options);

        //Shuffle the array to make sure we don't create a tendency to move in a particular direction
        Collections.shuffle(newOptions);

        //Try to mate first, if the wolf is healthy enough for mating it's good on food for now
        for (GridObject o : newOptions) {
            if ( //Mate with an available wolf
                    o instanceof Wolf &&
                            o.getGender() != super.getGender() &&
                            o.getHealth() >= EcoSim.MIN_MATE_HEALTH_WOLF && super.getHealth() >= EcoSim.MIN_MATE_HEALTH_WOLF
                    ) {
                return options.indexOf(o);
            }
        }

        //Next, try to eat a sheep
        for (GridObject o : newOptions) {
            //Wolves don't eat if they're full or close to full
            if (o instanceof Sheep && this.getHealth() < EcoSim.MAX_WOLF_HEALTH - o.getHealth()) {
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
