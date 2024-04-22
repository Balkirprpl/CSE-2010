/*
  Author: Joao Gabriel Andrade Silva  
  Email: jsilva2021@my.fit.edu  
  Course: CSE 2010
  Section: E1
  Description of this file: The implementation of singly linked list for homework 1.
 */

import java.io.*;
import java.util.Scanner;

public class HW1 {


   /*
   Creating all the lists as global variables
   so they can be accessed throughout the whole code
   */
   static LinkedList list = new LinkedList();
   static LinkedList usbDrive = new LinkedList();
   static LinkedList hdmi2VgaAdapter = new LinkedList();
   static LinkedList appleIPhone = new LinkedList();

   public static class Product {
      String store; // This variable stores the seller of the object (walmart, bestbuy etc...)
      double price; // This variable stores the price of the object 
      double shipping; // This variable stores the shipping cost of the object
      int storage; // This variable stores the number of products available to buy
      double total; // This variable stores the total price of the object (price + shipping)
      String product; // This variable stores the typ of product being sold

      Product(){} // default constructor for the object

      // personalized constructor to create an object and set the 
      // product, store and price in only one statement
      Product (String productName, String seller, double newPrice) {
         store = seller;
         price = newPrice;
         product = productName;
      }
   }

   // Initializing the LinkedList class
   public static class LinkedList {
   
      Node head; // head of list

      // Node class implemented by the linked list
      static class Node {

         Product data; // this variable is the data that each node carries
         Node next; // this variable is the pointer that makes the ordering of the list

         // The constructor for the node
         // takes a type product and sets it to the data
         Node(Product d) {
            data = d; // setting data to be the product
            next = null; // setting next to be null by default
         }
      }

      // Method to insert a new node in the correct spot in the list
      public static LinkedList insert(LinkedList list, Product data)
      {
         // Create a new node with the product
         Node new_node = new Node(data);

         // If the Linked List is empty,
         // the head points to the new node
         // created when calling the method
         if (list.head == null) {
            list.head = new_node;
         }
         // if the list is not empty
         // find where to put the new node
         else { 
            // variable created to make comparisons as
            // the first node in the list (head)
            // and use it todefine where to put the new object
            Node current = list.head; 

            // compare if both total prices are equal
            // so the method can sort alphabetically
            // using the compareTo() method to check
            // the two strings and see which one has the
            // greatest lexicographical value
            if (current.data.total == data.total) { 
               // checking for the first node
               if (current.data.store.compareTo(new_node.data.store) > 0) {
                  new_node.next = current;
                  list.head = new_node;
                  return list;
               }
               // iterating through the list and finding
               // the correct node to insert the new node
               while (current.next != null && current.next.data.total == new_node.data.total && current.data.store.compareTo(new_node.data.store) < 0) {
                  current = current.next;
               }
            }
            
            // comparing both total prices and putting the one
            // that has the least value to be put first
            else if (current.data.total > new_node.data.total) { // checking for the first node
               new_node.next = current;
               list.head = new_node;
               return list;
            }
            // iterating through the list and finding
            // the correct node to insert the new node
            else {
               while (current.next != null && current.next.data.total < new_node.data.total) {
                  current = current.next;
               }
            }   
            new_node.next = current.next;
            current.next = new_node;
         }
         // Return the list by head
         return list;
      }

      // Method to print the LinkedList
      public static void DisplaySellerList(LinkedList list) {
         // creating a node as the first element
         // of the given list to be iterated through
         Node currNode = list.head;

         // printing the header for the printed list
         System.out.printf("%10s%14s%14s%12s%n","seller", "productPrice", "shippingCost", "totalCost");

         // iterate through the list
         while (currNode != null) {
            // print the data at current node
            System.out.printf("%10s%14.2f%14.2f%12.2f%n", currNode.data.store, currNode.data.price, currNode.data.shipping, currNode.data.total);
            // get the next node
            currNode = currNode.next;
         }
      }

      // method to find a specific product object and delete it from the list
      public static void deleteByKey(Product a, LinkedList list) {
         // creating a node as the first element
         // of the given list to be iterated through
         Node currNode = list.head, prev = null;

         // checking if the first node has the same object
         if (currNode != null && currNode.data == a) {
             list.head = currNode.next; // Changed head
             return;
         }

         // search for the key to be deleted
         while (currNode != null && currNode.data != a) {
             // if currNode does not hold the object
             // continue to next node
             prev = currNode;
             currNode = currNode.next;
         }
         // if the object was found the links are changed
         // so nothing is pointing to the object
         // therefore deleting it
         if (currNode != null) {
             prev.next = currNode.next;
         }
      }
   }

   // method to set the shipping cost of all the object of the same seller
   public static void SetShippingCost(LinkedList list, String seller, double shippingCost, double freeShipping) {
      LinkedList.Node current = list.head; // defining the current as head to iterate
      while (current != null) { // iterates through the list
         // finds all the objects with the same seller
         if (current.data.store.equals(seller)) { 
            // if the product price is greater than
            // the minimum for the shipping cost
            // I set the shipping cost as the input
            // else it is defined to be 0 as default
            if (current.data.price < freeShipping) {
               current.data.shipping = shippingCost;
            }
            // adding the price and shipping cost to the total
            current.data.total = current.data.price + current.data.shipping;
         }
         // going to the next node
         current = current.next;
      }
      // calling the method to add all the objects of
      // the same seller to the proper lists
      AddToLists(seller);
   }
   
   // method that checks in which list the object should
   // be put in and put them in them there
   public static void AddToLists(String seller) {
      LinkedList.Node current = list.head; // defining the current to iterate
      while (current != null) {
         if (current.data.store.equals(seller)) {
            // checking which product each object is
            // and calling the insert method to put
            // them in the list of that object
            if (current.data.product.equals("appleIPhone")) LinkedList.insert(appleIPhone, current.data);
            if (current.data.product.equals("hdmi2VgaAdapter")) LinkedList.insert(hdmi2VgaAdapter, current.data);
            if (current.data.product.equals("USBdrive")) LinkedList.insert(usbDrive, current.data);
         }
         current = current.next; // going to the next node
      }
   }

   // method to increase the inventory of an object
   // and print the updated inventory
   public static void IncreaseInventory(Product a, int quantity) {
      a.storage += quantity;
      System.out.println(" " + a.storage);
   }

   // method to decrease the inventory of an object,
   // check if it is possible to do so, and delete
   // the object if theres no more of them in stock
   public static void CustomerPurchase(Product a, int quantity) {
      if (a.storage - quantity >= 0) { // checking if its possible to subtrack
         a.storage -= quantity; // subtracting 
         System.out.println(" " + a.storage); // outputting the new quantity
      }
      // else does nothing other than printing NotEnoughInventoryError
      else System.out.println(" NotEnoughInventoryError");
      // if the storage hits zero, remove the object from the list
      if (a.storage == 0) DepleteInventoryRemoveSeller(a); 
   }

   // method to find the product inside the list
   public static Product FindProduct (String seller, LinkedList list) {
      LinkedList.Node current = list.head; // setting the current to iterate the list
      String out = current.data.store; // setting a string to find for
      do {
         // searching for the nodes with the seller
         // ultil finding the node with the object
         // and returning the product
         if (current.data.store.equals(seller)) return current.data;
         current = current.next; // moving through the list
      }
      while (!out.equals(seller));
      return null; // returning null if not found
   }

   // method to remove the product 
   // from the designated list when called
   public static void DepleteInventoryRemoveSeller(Product a) {
      // printing the command
      System.out.printf("%s %s %s%n","DepletedInventoryRemoveSeller", a.product, a.store);

      // calling the methods to delete the object in the list
      if (a.product.equals("appleIPhone")) LinkedList.deleteByKey(a, appleIPhone);
      if (a.product.equals("USBdrive")) LinkedList.deleteByKey(a, usbDrive);
      if (a.product.equals("hdmi2VgaAdapter")) LinkedList.deleteByKey(a, hdmi2VgaAdapter);
   }

   public static void main (final String[] args) {
      Scanner in = new Scanner(System.in); // initializing the scanner

      while (in.hasNext()){ // checking for input
         String command = in.next(); // getting each command
         switch (command) {

            /*
            * the SetProductPrice command gets the product name
            * store name and price, then it prints out these variables
            * and insert a product object inside the general list
            * with all these three nuggets of information
            */
            case "SetProductPrice":
               String prod = in.next(); 
               String sellerName = in.next();
               double price = in.nextDouble();
               System.out.printf("%s %s %s %.2f%n", command, prod, sellerName, price);
               LinkedList.insert(list, new Product(prod, sellerName, price));
               break;

            /*
            * the SetShippingCost command gets the name of the store,
            * the shipping cost and the minimum price for shipping cost 
            * to be free, prints them out and calls the setShippingCost method
            */
            case "SetShippingCost":
               String seller = in.next();
               double shippingCost = in.nextDouble();
               double minimumShippingCost = in.nextDouble();
               System.out.printf("%s %s %.0f %.0f%n", command, seller, shippingCost, minimumShippingCost);
               SetShippingCost(list, seller, shippingCost, minimumShippingCost);
               break;

            /*
            * the IncreaseInventory command gets the name of the product,
            * the name of the store and the amount to be increased
            * prints them out, checks which list of product it should send to the method
            * by checking the product name and then calls the method
            */
            case "IncreaseInventory":
               String product = in.next();
               String sellerToIncrease = in.next();
               int amount = in.nextInt();
               System.out.printf("%s %s %s %d", command, product, sellerToIncrease, amount);
               if (product.equals("appleIPhone")) IncreaseInventory(FindProduct(sellerToIncrease, appleIPhone), amount);
               if (product.equals("hdmi2VgaAdapter")) IncreaseInventory(FindProduct(sellerToIncrease, hdmi2VgaAdapter), amount);
               if (product.equals("USBdrive")) IncreaseInventory(FindProduct(sellerToIncrease, usbDrive), amount);
               break;

            /*
            * the CustomerPurchase command gets the name of the product,
            * the name of the store, and the amount to try buying,
            * prints them out and then checks the product to call
            * the CustomerPurchase method to decrease the storage from the object
            */
            case "CustomerPurchase":
               String productToBuy = in.next();
               String sellerToBuy = in.next();
               int amountToBuy = in.nextInt();
               System.out.printf("%s %s %s %d", command, productToBuy, sellerToBuy, amountToBuy);
               if (productToBuy.equals("appleIPhone")) CustomerPurchase(FindProduct(sellerToBuy, appleIPhone), amountToBuy);
               if (productToBuy.equals("hdmi2VgaAdapter")) CustomerPurchase(FindProduct(sellerToBuy, hdmi2VgaAdapter), amountToBuy);
               if (productToBuy.equals("USBdrive")) CustomerPurchase(FindProduct(sellerToBuy, usbDrive), amountToBuy);
               break;

            /*
            * the DisplaySellerList command gets the product to print
            * the list of, print it out and then checks which list
            * it should send to the DisplaySellerList method to print out
            */
            case "DisplaySellerList":
               String productToPrint = in.next();
               System.out.printf("%s %s%n", command, productToPrint);
               if (productToPrint.equals("appleIPhone")) LinkedList.DisplaySellerList(appleIPhone);
               if (productToPrint.equals("hdmi2VgaAdapter")) LinkedList.DisplaySellerList(hdmi2VgaAdapter);
               if (productToPrint.equals("USBdrive")) LinkedList.DisplaySellerList(usbDrive);
               break;
         }   
      }
   }
}
