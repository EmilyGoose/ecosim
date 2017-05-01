/**
 * Plant
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;

public class Plant extends GridObject{

    Plant() {
        super();
        //This is a simplified version of (Math.random * ((max - min) + 1) + min)
        super.addHealth(2 + (int)(Math.random() * 4)); //Random health from 2-5 (inclusive)
    }

    public int findTarget(ArrayList<GridObject> options) {

        return 0;
    }

}
