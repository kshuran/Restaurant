package restaurant.chef;

import java.util.Queue;
import restaurant.dishes.Cake;

public class Confectioner extends Cook<Integer, Cake> {

    public Confectioner(Queue<Integer> order, Queue<Cake> cakes) {
        super(order, cakes);
    }
    
    @Override
    protected Cake prepareDish() {
        return new Cake();
    }

}
