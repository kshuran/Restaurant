package restaurant.chef;

import java.util.Queue;
import restaurant.dishes.Cake;

public class Confectioner extends Shef<Integer, Cake> {

    public Confectioner(Queue<Integer> order, Queue<Cake> cakes) {
        super(order, cakes);
    }
    
    @Override
    protected Cake prepareDish() {
        return new Cake();
    }

}
