/*
  Author: Joao Gabriel Andrade Silva  
  Email: jsilva2021@my.fit.edu
  Course: CSE 2010
  Section:  14
  Description of this file: Exchange system of buy/sell orders implementing heap priority queues
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

public class HW4 {
   // creating the object that stores 
   // all the important data
   public static class Order {
      int time;
      String name;
      double price;
      int quantity;

      // constructor for the class
      Order (int t, String n, double p, int q) {
         this.time = t;
         this.name = n;
         this.price = p;
         this.quantity = q;
      }
   }


   // method that is called before adding anything to the buyers list
   // returns true if it is possible to add the seller to the seller's list
   public static boolean checkSell (Order a, MaxHeapPriorityQueue<Double, Order> buyers) {
      if (buyers.size() == 0) return true; // if the buyers heap is empty, free to add
      Order current = buyers.min().getValue();

      // iterating through every matching buyer
      // or until the a order is depleted
      while (a.quantity > 0 && current.price >= a.price) {
         int quant = 0;

         // setting quant to be the min value of both quantities
         // and subtracting from both quantities
         if (a.quantity >= current.quantity) {
            quant = current.quantity;
            a.quantity -= quant;
            current.quantity -= quant;
         }
         else {
            quant = a.quantity;
            current.quantity -= quant;
            a.quantity -= quant;
         }
         
         // outputting the command
         System.out.printf("ExecuteBuySellOrders %.2f %d%n", (a.price + current.price)/2, quant);
         System.out.printf("Buyer: %s %d%nSeller: %s %d%n", current.name, current.quantity, a.name, a.quantity);
         if (current.quantity <= 0) buyers.removeMin();
         
         // if the quantity of the object being added is
         // nonpositive, it is not possible to add it
         if (a.quantity <= 0) return false;
         current = buyers.min().getValue(); // getting the next current
      }

      // if there is still orders in a
      // and no other trade can be made
      // return true and add a to the list of orders
      return true;
   }

   // method that is called before adding anything to the sellers list
   // returns true if it is possible to add the buyer to the buyers's list
   public static boolean checkBuy (Order a, HeapPriorityQueue<Double, Order> sellers) {
      if (sellers.size() == 0) return true; // if the sellers's heap is empty, free to add
      Order current = sellers.min().getValue();

      // iterating through every matching seller
      // or until the a order is depleted
      while (a.quantity > 0 && current.price <= a.price) {
         int quant = 0;

         // setting quant to be the min value of both quantities
         // and subtracting from both quantities
         if (a.quantity >= current.quantity) {
            quant = current.quantity;
            a.quantity -= quant;
            current.quantity -= quant;
         }
         else {
            quant = a.quantity;
            current.quantity -= quant;
            a.quantity -= quant;
         }

         // outputting the command
         System.out.printf("ExecuteBuySellOrders %.2f %d%n", (a.price + current.price)/2, quant);
         System.out.printf("Buyer: %s %d%nSeller: %s %d%n", a.name, a.quantity,current.name, current.quantity);
         if (current.quantity <= 0) sellers.removeMin();
         
         // if the quantity of the object being added is
         // nonpositive, it is not possible to add it
         if (a.quantity <= 0) return false;
         current = sellers.min().getValue(); // getting the next current
      }
      
      // if there is still orders in a
      // and no other trade can be made
      // return true and add a to the list of orders
      return true;
   }

   public static void main (String[] args) throws FileNotFoundException{

      // creating both heap priority queues
      // the min-heap should be used for sellers
      // and the max-heap for buyers.
      // both heaps are ordered by the price.
      HeapPriorityQueue<Double, Order> sellers = new HeapPriorityQueue<>();
      MaxHeapPriorityQueue<Double, Order> buyers = new MaxHeapPriorityQueue<>();

      // scanning the input
      Scanner in = new Scanner(new File("./" + args[0]), "US-ASCII");

      // iterating through the input
      while (in.hasNext()) {

         // getting the command
         // and checking which it is
         String command = in.next(); 
         switch (command) {
            case "EnterSellOrder":

               // getting input
               // and setting each one to
               // their respective variables
               int time = in.nextInt();
               String name = in.next();
               Double price = in.nextDouble();
               int quantity = in.nextInt();

               // outputting the command and variables
               System.out.printf("%s %d %s %.2f %d%n", command, time, name, price, quantity);
               
               // creating an Order object with the input
               Order a = new Order (time, name, price, quantity);

               // calling the check method, if true add to the heap
               if (checkSell(a, buyers)) sellers.insert(price, a);
               break;

            case "EnterBuyOrder":
            
               // getting input
               // and setting each one to
               // their respective variables
               int time2 = in.nextInt();
               String name2 = in.next();
               Double price2 = in.nextDouble();
               int quantity2 = in.nextInt();
               
               // outputting the command and variables
               System.out.printf("%s %d %s %.2f %d%n", command, time2, name2, price2, quantity2);
               
               // creating an Order object with the input
               Order b = new Order(time2, name2, price2, quantity2);
               
               // calling the check method, if true add to the heap
               if (checkBuy(b, sellers)) buyers.insert(price2, b);
               break;

            case "DisplayHighestBuyOrder":
               int time3 = in.nextInt(); // getting the time variable
               
               // defining an Order variable to the highest value on the buyer heap
               Order higher = buyers.min().getValue(); 

               // outputting the information for the buyer
               System.out.printf("%s %d %s %d %.2f %d%n", command, time3, higher.name, higher.time, higher.price, higher.quantity);
               break;
               
            case "DisplayLowestSellOrder":
               int time4 = in.nextInt(); // getting the time variable
               
               // defining an Order variable to the highest value on the buyer heap
               Order lowest = sellers.min().getValue();
               
               // outputting the information for the buyer
               System.out.printf("%s %d %s %d %.2f %d%n", command, time4, lowest.name, lowest.time, lowest.price, lowest.quantity);
            break;
         }
      }
   }
}
