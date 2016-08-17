package restaurant.chef;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import restaurant.dishes.Dish;

public abstract class Shef<Integer, T extends Dish> implements Runnable {
    private Queue<Integer> orders;
    private Queue<T> dishes;
    private Integer tableNumber;
    private final ExecutorService sousChefs;
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    Phaser phaser = new Phaser(3);
    
    class Task implements Runnable {
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Task from chef.");
            phaser.arrive();
        }
    }
    
    //protected Shef() {}
    
    protected Shef(Queue<Integer> orders, Queue<T> dishes) {
        this.orders = orders;
        this.dishes = dishes;
        sousChefs = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, 
                tasks, 
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        System.out.println("Souschef is created.");
                        return t; 
                    }
                });
    }
    
    protected void takeOrder() throws InterruptedException {
        //System.out.println("Chef try take order.");
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
            System.out.println(this.getClass().getSimpleName() + " take order.");
        }
    }
    
    protected void giveTask() throws InterruptedException {
        System.out.println(this.getClass().getSimpleName() + " give tasks.");
        sousChefs.execute(new Task());
        sousChefs.execute(new Task());
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
                while (Thread.currentThread().isInterrupted()) {
                    System.out.println(this
                            .getClass().getSimpleName() + " has end to cook.");
                    return;
                }
                //1000 - time to prepare dish
                //Thread.currentThread().sleep(1000);
                giveTask();
                System.out.println(this.getClass().getSimpleName() + " is waiting.");
                phaser.arriveAndAwaitAdvance();
                System.out.println(this.getClass().getSimpleName() + " give dish.");
                serv();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
    }
    
    abstract protected T prepareDish();
}
