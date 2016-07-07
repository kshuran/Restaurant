package restaurant.chef;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import restaurant.dishes.Dish;

public abstract class Cook<Integer, T extends Dish> implements Runnable {
    private Queue<Integer> orders;
    private Queue<T> dishes;
    private Integer tableNumber;
    
    protected Cook() {}
    
    protected Cook(Queue<Integer> orders, Queue<T> dishes) {
        this.orders = orders;
        this.dishes = dishes;
    }
    
    protected void takeOrder() throws InterruptedException {
        //System.out.println("Cook try take order.");
        synchronized(orders) {
            while (orders.isEmpty()) {
                Instant start = Instant.now();
                //10_000 - time to wait orders
                //if orders are absent - shef leave kitchen
                orders.wait(10000);
                Instant end = Instant.now();
                long dur = Duration.between(start, end).toMillis();
                if (dur >= 10000) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            tableNumber = orders.poll();
            //System.out.println("Cooker take order.");
        }
    }
    
    protected void serv() {
        synchronized(dishes) {
            dishes.add(prepareDish());
            dishes.notify();
        }
    }
    
    public void run() {
        while (true) {
            try {
                takeOrder();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(this
                            .getClass().getSimpleName() + " has end to cook.");
                    return;
                }
                //1000 - time to prepare dish
                Thread.currentThread().sleep(1000);
                serv();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
    }
    
    abstract protected T prepareDish();
}
