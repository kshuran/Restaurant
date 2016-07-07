package restaurant;

import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import restaurant.dishes.Cake;
import restaurant.dishes.Coffee;
import restaurant.dishes.Dish;
import restaurant.dishes.Soup;

public class Customer extends Thread {
    
    boolean[] tables;
    int tableNumber;
    Queue<Integer> ordersSoups;
    Queue<Integer> ordersCakes;
    Queue<Integer> ordersCoffee;
    Queue<Soup> soups;
    Queue<Cake> cakes;
    Queue<Coffee> coffee;
    
    public Customer() {}
    
    public Customer(String name, boolean[] tables, Queue<Integer> ordersSoups,
            Queue<Integer> orderCakes, Queue<Integer> ordersCoffee, 
            Queue<Soup> soups, Queue<Cake> cakes, Queue<Coffee> coffee) {
        super(name);
        this.tables = tables;
        this.ordersSoups = ordersSoups;
        this.ordersCakes = orderCakes;
        this.ordersCoffee = ordersCoffee;
        this.soups = soups;
        this.cakes = cakes;
        this.coffee = coffee;
        
        System.out.println(name + " in queue.");
    }
    
    private void getTable() throws InterruptedException {
        synchronized(tables) {
            while (true) {
                for (int i = 0; i < tables.length; i++) {
                    if (tables[i] == false) {
                        tableNumber = i + 1;
                        tables[i] = true;
                        System.out.println(Thread.currentThread().getName() 
                                + " get table " + tableNumber);
                        return;
                    }
                }
                tables.wait();
            }
        }
    }
    
    private void returnTable() {
        synchronized(tables) {
            tables[tableNumber - 1] = false;
            tables.notify();
            System.out.println(Thread.currentThread().getName() 
                    + " return table " + tableNumber);
            tableNumber = 0;
        }
    }
    
    private void makeOrder() {
        synchronized(ordersSoups) {
            ordersSoups.add(tableNumber);
            ordersSoups.notify();
        }
        synchronized(ordersCakes) {
            ordersCakes.add(tableNumber);
            ordersCakes.notify();
        }
        synchronized(ordersCoffee) {
            ordersCoffee.add(tableNumber);
            ordersCoffee.notify();
        }
        System.out.println(Thread.currentThread().getName() + " make order.");
    }
    
    private void takeSoup() throws InterruptedException {
        Dish dish = null;
        synchronized(soups) {
            while (soups.isEmpty()) {
                soups.wait();
            }
            dish = soups.poll();
        }
        System.out.println(Thread.currentThread().getName() + " take " 
                + dish.getClass().getSimpleName());
    }
    
    private void takeCake() throws InterruptedException {
        Dish dish = null;
        synchronized(cakes) {
            while (cakes.isEmpty()) {
                cakes.wait();
            }
            dish = cakes.poll();
        }
        System.out.println(Thread.currentThread().getName() + " take " 
                + dish.getClass().getSimpleName());
    }
    
    private void takeCoffee() throws InterruptedException {
        Dish dish = null;
        synchronized(coffee) {
            while (coffee.isEmpty()) {
                coffee.wait();
            }
            dish = coffee.poll();
        }
        System.out.println(Thread.currentThread().getName() + " take " 
                + dish.getClass().getSimpleName());
    }
    
    
    public void run() {
        try {
            getTable();
            makeOrder();
            takeSoup();
            takeCake();
            takeCoffee();
            returnTable();
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(Thread.currentThread().getName() + " go away.");
    }
}
