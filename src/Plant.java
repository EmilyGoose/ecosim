/*
 * Plant
 * Subclass of GridObject
 * Misha Larionov
 * 2017-04-24
 */

import java.util.ArrayList;
import java.util.Random;

//IntelliJ-specific line to stop annoying "access can be package-private" warnings
@SuppressWarnings("WeakerAccess")

public class Plant extends GridObject{

    private int age;

    Plant() {
        super();
        this.age = 0;
    }

    Plant(int newAge) {
        super();
        this.age = newAge;
    }

    @Override
    public int getHealth() {
        int x = this.age;
        return (int)(((-0.36) * (x * x)) + (3.6 * x) + 1);
    }

    public int findTarget(ArrayList<GridObject> options) {
        //The plant actually never needs to do this
        return 0;
    }

    public void addAge() {
        this.age += 1;
    }

}
