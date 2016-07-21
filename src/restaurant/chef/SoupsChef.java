package restaurant.chef;

import java.util.Queue;
import restaurant.dishes.Soup;

public class SoupsChef extends Shef<Integer, Soup> {
    
    public SoupsChef(Queue<Integer> order, Queue<Soup> soups) {
        super(order, soups);
    }

    @Override
    protected Soup prepareDish() {
        return new Soup();
    }
    
}
