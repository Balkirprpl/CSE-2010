/*
   Author: Joao Gabriel Andrade Silva  
   Email: jsilva2021@my.fit.edu
   Course: CSE 2010
   Section:  14
   Description of this file: Implementation of skip lists to create a schedule of activities.
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HW5 {
      
   public static class SkipList {
      
      int levels = 0; // variable to keep track of the height
      FakeRandHeight randomHeight = new FakeRandHeight(); // object that gives the random heigh
      
      // positive and negative max values to set to the head and tail
      final int POSLIMIT = Integer.MAX_VALUE;
      final int NEGLIMIT = Integer.MIN_VALUE;
      
      // declaring the head and tail
      Node head;
      Node tail;

      // contructor for the skiplist that sets the tail value to positive max
      // and head to the negative max. Furthermore it links both together.
      SkipList() {
         tail = new Node (POSLIMIT);
         head = new Node (NEGLIMIT);
         head.next = tail;
         tail.prev = head;
      }

      // class node that contains pointers to each direction
      // a String value and a integer key.
      public class Node {
         Node up;
         Node down;
         Node next;
         Node prev;
         String name;
         int key;

         // constructor for nodes using only integers (head and tail)
         Node (int x) {
            up = null;
            down = null;
            next = null;
            prev = null;
            name = "___head/tail___";
            key = x;
         }

         // constructor for nodes that use integers and strings
         Node(int k, String n) {
            up = null;
            down = null;
            next = null;
            prev = null;
            name = n;
            key = k;
         }
      }

      
      // remove method
      public Node remove(int k) {

         // find the node to be removed
         Node toRemove = search(k);

         // if the key is not found, there is nothing to do
         // so just return null
         if (toRemove.key != k) return null;

         // call the methods to remove all the pointers of the node found
         removePointers(toRemove);

         while (toRemove != null) { // iterating until there is no node to remove
            removePointers(toRemove); // remove all pointers of the node

            // goes up one level
            if (toRemove.up != null) toRemove = toRemove.up; 
            else break;
         }

         // calling method to clear empty levels
         clearEmptyLevel();

         // returning the node removed;
         return toRemove;
      }

      // method to clear empty levels
      public void clearEmptyLevel() {

         // initializing the a node as head
         Node a = head;

         // loop that checks if the level below contains only two objects 
         // if so, go one step down and reduce the amount of levels by one
         while (a.down != null && a.down.next.key == a.next.key) {
            a = a.down;
            levels--;
         }

         // after reaching the last duplicated node
         // makes it the new head.
         head = a;
      }

      // method to remove pointers from a specific node
      public void removePointers(Node toRemove) {
         // gets the next and the previous node to the node to be removed
         Node nextNode = toRemove.next;
         Node prevNode = toRemove.prev;

         // changes their .next and .prev to one another
         // not pointing at the node removed
         prevNode.next = nextNode;
         nextNode.prev = prevNode;
      }

      // method used to put something in the list
      public void put(int k, String v) {

         // find the previous position to the node 
         Node positionNode = search(k);
         Node temp; // temporary node used to call other methods later on
         
         // getting the amount of levels to add from the randomHeight
         int levelsToAdd = 0;
         levelsToAdd = randomHeight.get();

         // checking if the node found already is on the list
         // and returning an error
         if (positionNode.key == k) {
            System.out.printf(" ExistingActivityError:%s", positionNode.name);
            return;
         }

         // adding the new node "levelsToAdd" times
         for (int i = 0; i <= levelsToAdd; i++) {

            // checking if the list contains the amount of levels necessary to add a new node
            if (levelsToAdd >= levels) {
               levels++; // increasing the amount of levels
               addLevel(); // creating the new level
            }
            // duplicating the node found
            temp = positionNode;

            // finding the next node that points up and setting positionNode to the level above
            while (positionNode.up == null) {
               positionNode = positionNode.prev;
            }
            positionNode = positionNode.up;

            // setting temp to be new level added in the next vertical level
            temp = insertAfterAbove(positionNode, temp, k, v);

         }

         return;
      }

      // inserting the node vertically and horizontally on the list
      public Node insertAfterAbove(Node positionNode, Node temp, int k, String v) {
         Node toAdd = new Node(k, v); // creating a new node to be added 
         // getting the node on the level below
         Node prevToAdd = positionNode.down.down;

         // calling the methods to set the pointers in sequence and in height
         setSequencePointers(temp, toAdd);
         setHeightPointers(positionNode, k, toAdd, prevToAdd);

         return toAdd;
      }

      // method to set the previous and next pointers
      public void setSequencePointers(Node temp, Node toAdd) {
         // setting the newNode.next to be the node on the next position
         toAdd.next = temp.next;
         // setting the newNode.prev to be the node found
         toAdd.prev = temp;
         // updating the pointers for the node.next and next.prev to be the newNode
         temp.next.prev = toAdd;
         temp.next = toAdd;
      }

      // method to set the height pointer
      public void setHeightPointers(Node positionNode, int k, Node toAdd, Node prevToAdd) {
         if (prevToAdd == null) return; // if there is no node before just return

         while (true) { // iterating until finding the node that contains K
            if (prevToAdd.next.key != k) {
               prevToAdd = prevToAdd.next;
            }
            else {
               break;
            }
         }

         // setting the down pointer to be the new node but on the inferior level
         // and the up pointer to be the new node
         toAdd.down = prevToAdd.next;
         prevToAdd.next.up = toAdd;

         // setting the newNode up pointer
         if (positionNode != null && positionNode.next.key == k) {
            toAdd.up = positionNode.next;
         }
      }

      // method that adds an empty level to the list
      public void addLevel() {
         // creating the new head and tail nodes
         Node newHead = new Node(NEGLIMIT);
         Node newTail = new Node(POSLIMIT);

         // setting the pointers to the new head and tail
         newHead.next = newTail;
         newTail.prev = newHead;

         // setting the pointers to the new head and tail
         newHead.down = head;
         newTail.down = tail;

         // setting the inferior's level head to the new head
         // same with the tail
         head.up = newHead;
         tail.up = newTail;

         // setting the new head and tail for the list
         head = newHead;
         tail = newTail;
      }

      // method that prints a range in the list
      public void printSubMap(int initialK, int finalK) {
         // finding the first node in the sequence

         Node a = search(initialK);
         boolean found = false;

         // printing the node. if not the head/tail
         if (!a.name.equals("___head/tail___")) {
            System.out.printf("%d:%s", a.key, a.name);
            found = true;
         }

         // iterating linearly until reaching the endValue
         // and printing all nodes if not head/tail
         while (a.next.key < finalK) {
            a = a.next;
            if (!a.name.equals("___head/tail___")) System.out.printf("%d:%s ", a.key, a.name);
            found = true;
         }
         // checking if it is the one being searched for
         if (!found) {
            System.out.println("None");
            return; // if not return
         }
         System.out.println();
      }

      // method that prints all nodes in the same day
      public void printSubMapDay(int date) {
         Node a = this.head; // defining head

         // similar to the search method, but finding the node previous to the 1/10000
         // k value of the node. This way makes finding the first 3-4 digits possible
         while (a.down != null) {
            a = a.down;

            while (a.next.key/10000 <= date - 1) {
               a = a.next;
            }
         }

         // if the node does not have the value searched for
         // print none
         if (a.next.key/10000 != date) System.out.print(" None"); 

         // iterating through every node value in the same 1/10000 (day) 
         // and printing them all
         while (a.next.key/10000 == date) {
            a = a.next;
            System.out.printf(" %08d:%s", a.key, a.name);
         }
      }
 
      // method to print every node with value less than the one asked but on the same day
      public void printSubMapBefore(int time) {
         Node a = search(time); // finding the node 
          // iterating through every activity on the same day
          // and printing them
         while (a != null && a.key/10000 == time/10000) {
            if (!a.name.equals("___head/tail___")) System.out.printf(" %08d:%s", a.key, a.name);
            a = a.prev; // going backwards on the list
         }
         System.out.println();
      }

      // search method
      public Node search(int k) {
         Node a = this.head; // defining the start point at the head
         
         // going down one level, then going to the left when possible
         while (a.down != null) {
            a = a.down;

            while (a.next.key <= k) a = a.next;
         }
         return a;
      }

      // printing the list with head as a starting point
      public void printList() {
         printList(head, levels);
      }

      // printing the list from a node
      public void printList(Node a, int level) {
         System.out.printf("(S%d)", level); // printing the level
         Node temp = a; // getting the first node on the list


         // loop that goes from left to right
         // and prints every node that is not
         // head/tail and checks if the level is empty
         while (temp != null) { 
            if (temp.key == NEGLIMIT && temp.next.key == POSLIMIT) System.out.printf(" Empty");
            if (!temp.name.equals("___head/tail___")) System.out.printf(" %08d:%s", temp.key, temp.name);
            temp = temp.next;
         }
         System.out.println();

         // calling the method to print the level below
         if (a.down != null) printList(a.down, level - 1);
         return;
      }
   }

   public static void main (String[] args) throws FileNotFoundException {

      // setting up the input with files and scanning it
      Scanner in = new Scanner(new File("./" + args[0]));
      String command;
      // creating the skiplist
      SkipList list = new SkipList();

      while (in.hasNext()) {
         command = in.next(); // getting the command and printing it
         System.out.printf("%s", command);
         switch (command) {
            case "AddActivity":
               // getting the values provided
               int time = in.nextInt();
               String activity = in.next();
               // printing the node to add
               System.out.printf(" %08d %s", time, activity);
               // adding it to the list
               list.put(time, activity);
               System.out.println();
               break;
            case "RemoveActivity":
               // getting the values provided
               int time2 = in.nextInt();
               System.out.printf(" %08d", time2);
               SkipList.Node removedNode = list.remove(time2); // removing the node
               if (removedNode == null) System.out.print(" NoActivityError\n"); // checking if the node was found
               else System.out.printf(" %s%n", removedNode.name);
               break;
            case "GetActivity":
               // getting the values provided
               int time3 = in.nextInt();
               System.out.printf(" %08d ", time3);
               SkipList.Node getActivityNode = list.search(time3); // finding the node
               if (getActivityNode.key != time3) System.out.print("None\n"); // checking if the node was found
               else System.out.printf("%s%n", getActivityNode.name);
               break;
            case "GetActivitiesBetweenTimes":
               // getting the values provided
               int startTime = in.nextInt(), endTime = in.nextInt();
               System.out.printf(" %08d %08d ", startTime, endTime);
               // printing all the values in the range
               list.printSubMap(startTime, endTime);
               break;
            case "GetActivitiesForOneDay":
               // getting the values provided
               int date = in.nextInt();
               System.out.printf(" %04d", date);
               // printing all the values in the day
               list.printSubMapDay(date);
               System.out.println();
               break;
            case "GetActivitiesFromEarlierInTheDay":
               // getting the values provided
               int time4 = in.nextInt();
               System.out.printf(" %08d", time4);
               // printing all the previous values in the day
               list.printSubMapBefore(time4);
               break;
            case "PrintSkipList":
               System.out.println();
               // printing the list
               list.printList();
               break;
            default:
               // default if command not found == missInput
               System.out.println(" MissInput");
               break;
         }
      }
   }
}
