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

        int firstWolf = -1;
        int firstSheep = -1;
        int firstMale = -1;

        for (int i = 0; i < newOptions.size(); i++) {
            GridObject o = newOptions.get(i);
            if ( //Mate with an available wolf
                    o instanceof Wolf &&
                            o.getGender() != super.getGender() &&
                            o.getHealth() >= EcoSim.MIN_MATE_HEALTH_WOLF && super.getHealth() >= EcoSim.MIN_MATE_HEALTH_WOLF
                    ) {
                firstWolf = i;
                break; //We've found a mate, no need to check further since this is our first priority
            } else if (firstMale == -1 && o instanceof Wolf && o.getGender() && super.getGender()) {
                firstMale = i;
            } else if (firstSheep == -1 && o instanceof Sheep && this.getHealth() < EcoSim.MAX_WOLF_HEALTH - o.getHealth() / 2) {
                firstSheep = i;
            }
        }

        if (firstWolf >= 0) {
            return options.indexOf(newOptions.get(firstWolf));
        } else if (firstMale > -1) {
            //Prioritize fighting over eating (Wolves fight over food)
            return options.indexOf(newOptions.get(firstMale));
        } else if (firstSheep >= 0) {
            return options.indexOf(newOptions.get(firstSheep));
        } else {
            //Pick a random empty square and move to it
            //We need a slightly different method of iteration here
            //This is because options.indexOf(o) where o is null returns the first null value, as null cannot be unique
            return super.findNullSpace(options);
        }
    }
}
