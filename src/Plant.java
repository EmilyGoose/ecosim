/*
 * Plant
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Random;

public class Plant extends GridObject{

    Plant() {
        super();
        int min = 5;
        int max = 10;
        int health = new Random().nextInt((max - min) + 1) + min;
        super.addHealth(health); //Random health from 5-10 (inclusive)
    }

    public int findTarget(ArrayList<GridObject> options) {

        return 0;
    }

}
