package restaurant.chef;

import java.util.Queue;
import restaurant.dishes.Coffee;

public class Bartender extends Shef<Integer, Coffee> {
    
    public Bartender(Queue<Integer> order, Queue<Coffee> coffee) {
        super(order, coffee);
    }

    @Override
    protected Coffee prepareDish() {
        return new Coffee();
    }

}
