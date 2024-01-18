import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Collections;

public class HW6 {

   // Class for the game grid
   public static class GameGrid {

      Node head = null; // head/start of the grid
      int height; // max height of the grid
      int width; // max width of the grid

      // constructor for the game grid
      // that takes two integers and set
      // the height and width values
      // to them
      GameGrid (int one, int two) {
         this.height = one;
         this.width = two;
      }

      // method to add nodes to the grid
      public void add (Node a) { 

         // case where there is no elements in the grid
         // first element should be assigned to the head
         if (head == null) {
            this.head = a;
            return;
         }

         // if there is an object in the grid
         // call the method to find the row to add the object
         // based on the Y value of the node
         Node placeToAdd = findRow(a.y);

         // if the x axis is 0
         // it means the node should be added
         // in the same column as the head.
         if (a.x == 0) {
            placeToAdd.down = a;
            a.up = placeToAdd;
            return;
         }

         // if the column is not the zeroeth one
         // find the column to add, and set the pointers
         placeToAdd = findColumn(placeToAdd, a.x);
         placeToAdd.right = a;
         a.left = placeToAdd;
      }

      // method used to iterate through the 
      // x axis until the last element or the
      // xth element is found
      public Node findColumn (Node a, int x) {
         while (a.right != null && a.x != x) {
            a = a.right;
         }
         return a;
      }

      // method used to iterate through the
      // y axis until the last element or the 
      // yth element is found
      public Node findRow(int y) {
         Node currentNode = head;
         while (currentNode.down != null && currentNode.y != y) {
            currentNode = currentNode.down;
         }
         return currentNode;
      }

      // method to print the grid
      // it iterated from left to right 
      // by each row and prints every node
      // in that row
      public void print () {
         System.out.printf("  ");
         for (int i = 0; i < width; i++) {
            System.out.printf("%d", i);
         }
            System.out.printf("%n");
         Node a = head;
         int count = 0;
         while (a.down != null) {
            System.out.printf("%d ", count);
            printRow(a);
            a = a.down;
            count++;
         }
         System.out.printf("%d ", count);
         printRow(a);
         System.out.printf("%n");
      }

      // method that takes a node and prints
      // every node in the same row
      public void printRow(Node a) {
         int count = 0;
         while (a != null) {
            System.out.print(a.character);
            a = a.right;
         }
         System.out.println();
      }

      // method that iterates through every node and 
      // sets the down and up pointers for every node
      public void setPointers() {
         Node zeroethNode = head; // node in the column 0
         while (zeroethNode.down != null) { // loop that iterates vertically until the last node
            Node newNode = zeroethNode; // new node to be set pointers
            while (newNode.right != null) { // loop that iterates horizontally until the lat node

               // setting the up and down pointers for each node
               newNode = newNode.right;
               Node downNode = newNode.left.down.right; // finding the node below the one being modified
               newNode.down = downNode;
               downNode.up = newNode;
            }
            zeroethNode = zeroethNode.down; // going to the row bellow
         }
      }

      // method to check if the movement is available
      // it checks the input 'direction' and the nodes
      // around the player for the direction
      // and returns false if the player is trying to go to a
      // obstructed node, or if the direction is not a, d, l, r
      // returns true otherwise
      public boolean checkMovement (Node player, char direction) {
         switch (direction) {
            case 'u':
               if (player.up == null || player.up.character == '#') return false;
               break;
            case 'd':
               if (player.down == null || player.down.character == '#') return false;
               break;
            case 'r':
               if (player.right == null || player.right.character == '#') return false;
               break;
            case 'l':
               if (player.left == null || player.left.character == '#') {
                  return false;
               }
               break;
            default: return false;
         }
         return true;
      }

      // method that moves the player
      // first it checks if the player arrives at the tower
      // if so, the player wins, then it checks 
      // if the player is trying to move to a place that is not a space
      // then it should be a bug, so the player loses.
      // (obstacles are not included because they were checked on checkMovement())
      public Node movePlayer (Node player, char c) {
         switch (c) {
            case 'u':
               if (player.up.character == 'I') playerWin(player);
               if (player.up.character != ' ') playerLose(player);
               player.up.character = 'T';
               player.character = ' ';
               return player.up;
            case 'd':
               if (player.down.character == 'I') playerWin(player);
               if (player.down.character != ' ') playerLose(player);
               player.down.character = 'T';
               player.character = ' ';
               return player.down;
            case 'r':
               if (player.right.character == 'I') playerWin(player);
               if (player.right.character != ' ') playerLose(player);
               player.right.character = 'T';
               player.character = ' ';
               return player.right;
            case 'l':
               if (player.left.character == 'I') playerWin(player);
               if (player.left.character != ' ') playerLose(player);
               player.left.character = 'T';
               player.character = ' ';
               return player.left;
         }
         return null;
      }

      // method that erases the character,
      // prints the grid, prints a winning message
      // and exit the program
      void playerWin (Node player) {
         player.character = ' ';
         print();
         System.out.println("Tron reaches I/O Tower");
         System.exit(0);
      }

      // method that erases the character,
      // prints the grid, prints a loosing message
      // and exit the program
      void playerLose (Node player) {
         // player.character = ' ';
         System.out.printf("%n");
         print();
         System.out.println("A bug is not hungry anymore");
         System.exit(0);
      }

      // method to find the shortest path to every node in the grid
      public Node bfs (ArrayList<Node> objects, Node player, int index) {
         Deque<Node> queueueue = new LinkedList<Node>(); // creating a queue of nodes to store the next nodes to be visited
         Node bug = objects.get(index); // getting the bug to make the paths
         bug.visitedQuestionMark = true; // setting the bug's node as visited
         queueueue.addFirst(bug); // adding the bug to the queue
         int levelCount = 0; // initializing the distance
         
         while (!queueueue.isEmpty()) {
            Node temp = queueueue.removeFirst(); // removing the first node
            temp.distance = levelCount; // setting the distance from the bug to the node

            // checking the up, down, left, and right nodes are a fit to be a path
            // then setting the node to be visited and setting the parent node
            // and adding the node at the end of he queue
            if (temp.up != null && !temp.up.visitedQuestionMark && (temp.up.character == ' ' || temp.up.character == 'T')) {
               temp.up.visitedQuestionMark = true;
               temp.up.daddyNode = temp;
               queueueue.addLast(temp.up); 
            }
            if (temp.down != null && !temp.down.visitedQuestionMark && (temp.down.character == ' ' || temp.down.character == 'T')) {
               temp.down.visitedQuestionMark = true;
               temp.down.daddyNode = temp;
               queueueue.addLast(temp.down); 
            }
            if (temp.left != null && !temp.left.visitedQuestionMark && (temp.left.character == ' ' || temp.left.character == 'T')) {
               temp.left.visitedQuestionMark = true;
               temp.left.daddyNode = temp;
               queueueue.addLast(temp.left); 
            }
            if (temp.right != null && !temp.right.visitedQuestionMark && (temp.right.character == ' ' || temp.right.character == 'T')) {
               temp.right.visitedQuestionMark = true;
               temp.right.daddyNode = temp;
               queueueue.addLast(temp.right);
            }
            levelCount++; // incrementing the distance
         }
         
         // adding the path to a queue
         Node current = player;
         Node movPos = objects.get(index);
         while (current.daddyNode != null) {
            queueueue.addFirst(current);
            if (current.daddyNode.daddyNode == null) movPos = current; // getting the first move for the move
            current = current.daddyNode;
         }

         // calling the method to find the bug's next movement character
         char movDirection = findMove(movPos, bug);

         // printing the path for the bug
         System.out.printf("Bug %c: %c %d ", objects.get(index).character, movDirection, queueueue.size());
         while (!queueueue.isEmpty()) {
            System.out.printf("(%d, %d) ", current.y, current.x); // printinf the y and x values 
            current = queueueue.removeFirst(); // removing a node from the queue
         }
         System.out.printf("(%d, %d)%n", current.y, current.x);

         char c = bug.character;
         bug.character = ' '; // clearing the bug's previous position
         movPos.character = c; // updating the bug's current position

         clearGrid(); // clearing the grid
         return movPos; // returning the new bug node
      }

      // method that clears the parent nodes
      // and distances for every node in the grid
      public void clearGrid () {
         Node zeroethNode = head; // column 0 node
         while (zeroethNode != null) {
            Node iterativeNode = zeroethNode; // node that iterates horizontally
            while (iterativeNode != null) {

               // redefining the visited, parent, and distance variables
               iterativeNode.visitedQuestionMark = false;
               iterativeNode.daddyNode = null;
               iterativeNode.distance = 0;

               // going to the next node
               iterativeNode = iterativeNode.right;
            }
            zeroethNode = zeroethNode.down; // going down one row
         }
      }

      // method that find the character for the move made
      public char findMove(Node destination, Node origin) {
         // getting the y and x difference
         int y = destination.y - origin.y;
         int x = destination.x - origin.x;
         
         // checking which direction the bug move
         // and returning the char direction
         if (y == 1) return 'd';
         if (y == -1) return 'u';
         if (x == 1) return 'r';
         if (x == -1) return 'l';

         return '$';
      }

      // Node class
      static class Node implements Comparable<Node> {

         // direction nodes
         Node up;
         Node down;
         Node left;
         Node right;

         // character in the node
         char character;

         // coordinates for the node
         int x;
         int y;

         // parent node
         Node daddyNode;

         // checking if the node was visited
         boolean visitedQuestionMark;

         // distance from this node to the one making the bfs
         int distance;

         // constructor for the node
         // sets every variable but the 
         // character and coordinates
         // to a unmeaningful value
         Node (char c, int y, int x) {
            this.character = c;
            this.y = y;
            this.x = x;
            this.up = null;
            this.left = null;
            this.right = null;
            this.down = null;
            this.daddyNode = null;
            this.visitedQuestionMark = false;
            this.distance = 0;
         }


         // compareTo method to be used by java.util.Collections
         @Override
         public int compareTo(Node b) {
            return this.character - b.character;
         }
      }
   }


   public static void main (String[] args) throws FileNotFoundException {


      // gathering input
      Scanner in = new Scanner(new File("./" + args[0]));
      int rows = in.nextInt();
      int columns = in.nextInt();

      // creating the grid with the max rows and columns
      GameGrid game = new GameGrid(rows, columns);

      // creating a list of Nodes
      ArrayList<GameGrid.Node> objects = new ArrayList<>();

      String line = in.nextLine(); // removing the rest of the line (not having this command breaks the program somehow)
      // String line;
      // System.out.println(line);
      // gathering input and adding the nodes at the j and i coordinates
      for (int i = 0; i < columns; i++) {
         line = in.nextLine(); // getting the full line
         for (int j = 0; j < rows; j++) {
            GameGrid.Node toAdd = new GameGrid.Node(line.charAt(j), i, j); // creating the node to be added
            game.add(toAdd); // adding it to the grid
            if (line.charAt(j) != '#' && line.charAt(j) != ' ') objects.add(toAdd); // adding it to the list of objects (only tron, the tower and bugs)
         }
      }
      game.print();

      // sorting the list
      Collections.sort(objects);
      GameGrid.Node player = objects.get(1); // getting the player at the 1 indes
      game.setPointers(); // calling the method to set pointers
      
      while (true) {
         game.print(); // printing the grid
         // asking for a direction for tron to move
         System.out.printf("Please enter your move [u(p), d(own), l(eft), or r(ight)]: ");
         in = new Scanner (System.in); // updating the scanner to take input from outside the file
         char direction = in.next().charAt(0); // getting the input and the first character of the input
         System.out.printf("%n");

         // continiouslly asking for another input if the direction is not valid
         while (!game.checkMovement(objects.get(1), direction)) {
            System.out.println("Invalid move, try again.");
            System.out.printf("Please enter your move [u(p), d(own), l(eft), or r(ight)]: ");
            direction = in.next().charAt(0);
            System.out.println();
         }

         player = game.movePlayer(objects.get(1), direction); // updating the player position
         objects.remove(1); // removing the previous player node in the list
         objects.add(1, player); // adding the new player node to the list
         game.print(); // printing the list

         // calling bfs for each bug node in the list (index 2+)
         for (int i = 2; i < objects.size(); i++) {
            GameGrid.Node newPosition = game.bfs(objects, player, i);
            objects.remove(i); // removing the previous position for the ith bug
            objects.add(i, newPosition); // adding the new node position for the bug
         }
         if (player.character != 'T') game.playerLose(player);
         System.out.printf("%n");

         // printing the location of each bug 
         for (GameGrid.Node i : objects) {
            System.out.printf("%c : (%d, %d) ", i.character, i.y,i.x);
         }
         System.out.printf("%n");
      }
   }
}
