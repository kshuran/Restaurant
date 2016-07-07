package restaurant.chef;

import java.util.Queue;
import restaurant.dishes.Soup;

public class SoupsCook extends Cook<Integer, Soup> {
    
    public SoupsCook(Queue<Integer> order, Queue<Soup> soups) {
        super(order, soups);
    }

    @Override
    protected Soup prepareDish() {
        return new Soup();
    }
    
}
