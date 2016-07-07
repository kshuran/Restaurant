package restaurant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import restaurant.chef.Bartender;
import restaurant.chef.Confectioner;
import restaurant.chef.Cook;
import restaurant.chef.SoupsCook;
import restaurant.dishes.Cake;
import restaurant.dishes.Coffee;
import restaurant.dishes.Soup;

public class Restaurant {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //10 tables in restaurant
        boolean[] tables = new boolean[10];
        
        Queue<Integer> orderSoups = new LinkedList<>();
        Queue<Integer> orderCakes = new LinkedList<>();
        Queue<Integer> orderCoffee = new LinkedList<>();
        
        Queue<Soup> soups = new LinkedList<>();
        Queue<Cake> cakes = new LinkedList<>();
        Queue<Coffee> coffee = new LinkedList<>();
        
        Cook<Integer, Soup> s = new SoupsCook(orderSoups, soups);
        Cook<Integer, Cake> ck = new Confectioner(orderCakes, cakes);
        Cook<Integer, Coffee> b = new Bartender(orderCoffee, coffee);
        
        Thread soupsCook = new Thread(s);
        Thread confectioner = new Thread(ck);
        Thread bartender = new Thread(b);
        soupsCook.start();
        confectioner.start();
        bartender.start();
        //25 clients want to go to restaraunt
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Number random = Math.random() * 1000;
            long time = random.longValue();
            //System.out.println(time);
            String name = "Customer " + (i +1);
            customers.add(i, new Customer(name, tables, orderSoups, orderCakes,
                orderCoffee, soups, cakes, coffee));
            customers.get(i).start();
            Thread.sleep(time);
        }
//        Thread.sleep(1800);
//        customers.add(customers.size(), 
//                new Customer("Last Customer", tables, orderSoups, orderCakes,
//                orderCoffee, soups, cakes, coffee));
//        customers.get(customers.size() - 1).start();/
        
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).join();
        }
        soupsCook.join();
        confectioner.join();
        bartender.join();
        
        System.out.println("Restaurant is empty.");
    }
    
}
