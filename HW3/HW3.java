/*

  Author:Joao Gbariel Andrade Silva
  Email:jsilva2021@my.fit.edu
  Course:CSE2010  
  Section:1-4
  Description of this file:homework implementing trees

 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

public class HW3 {

   /*
    * The Tree class has a root node by definition, 
    * which is gonna be the first object to be added.
    * The nodes are made out of the data, an Node parent and 
    * a list of children Nodes.
    */
   public static class Tree {
      public Node root;
      
      // constructor for the tree class
      // which assigns the root node to a new node
      Tree (String newData) {
         root = new Node();
         root.data = newData;
         root.children = new ArrayList<Node>();
         root.parent = null;
      }

      public class Node implements Comparable<Node> {
         String data;
         Node parent;
         ArrayList<Node> children;

         // creating the constructors for the node class
         Node () {}

         Node (String newData) {
            data = newData;
            children = new ArrayList<Node>();
         }

         // append child takes a String,
         // creates a Node with it
         // and adds it do the node children being called
         public void appendChild (String newData) {
            Node a = new Node(newData);
            a.parent = this;
            this.children.add(a);
         }

         // insert child takes a String,
         // creates a node with it
         // adds it to the node children and sort the list
         public void insertChild (String newData) {
            Node a = new Node(newData);
            a.parent = this;
            this.children.add(a);
            Collections.sort(this.children);
         }

         // method to get the parent of a node
         public Node getParent () {
            return this.parent;
         }

         // method to get the list of children of a node
         public ArrayList<Node> getChildren () {
            return this.children;
         }

         // creating a compareTo metod so the collections
         // api can sort the array after inserting data to a
         // node list
         @Override
         public int compareTo(Node a) {
            int order = this.data.compareTo(a.data);
            return order;
         }
      }

      // method that prints all objects in the tree
      // by iterating through it
      public void printTree() {
         printTree(root, 0);
      }

      public void printTree (Node a, int append) {
         for (int i = 0; i < append; i++) {
            System.out.print("   ");
         }
         System.out.printf("%s%n", a.data);
         for (int i = 0; i < a.children.size(); i++) {
            printTree(a.children.get(i), append + 1);
         }
      }

      // find method searches for an specific node in the tree 
      // and return its first appearance recursively
      public Node find (String find) {
         return find(find, root);
      }

      public Node find (String find, Node curr) {
         if (curr.data.equals(find)) return curr;
         else {
            for (int i = 0; i < curr.children.size(); i++) {
               if (find(find, curr.children.get(i)).data.equals(find)) return find(find, curr.children.get(i));
            }
            return curr;
         }
      }

      // findAll method creates a list and returns the list of 
      // all the appearances of a specific name in the tree
      public ArrayList<Node> findAll (String find) {
         return findAll(find, root, new ArrayList<Node>());

      }

      public ArrayList<Node> findAll (String find, Node curr, ArrayList<Node> list) {
         if (curr.data.contains(find)) {
            list.add(curr);
            return list;
         }
         else {
            if (curr.getChildren() != null) {
               for (Node a : curr.getChildren()) {
                  list = (findAll(find, a, list));
               }
            }
            return list;
         }
      }
   }

   // method getMedal goes through  the tree
   // finds all the leaf nodes and add them to a list,
   // then return it.
   // it can also find all the gold medalists
   // (first indexes)  by calling it with the boolean as true
   // and add them to the list then return it.
   public static ArrayList<String> getMedal(Tree.Node a, int command, ArrayList<String> ans) {
      return getMedal(a, command, ans, false);
   }

   public static ArrayList<String> getMedal(Tree.Node a, int command, ArrayList<String> ans, boolean goldMetal) {
      if (a.getChildren().isEmpty()) {
         String toAdd[] = a.data.split(":");
         if (!goldMetal) {
            ans.add(toAdd[command]);
         }
         else {
            if (a.parent.children.get(0).data.equals(a.data)) {
               ans.add(toAdd[command]);
            }
         }
         return ans;
      }
      else {
         for (Tree.Node i : a.getChildren()) {
           getMedal(i, command, ans, goldMetal);
         }
         return ans;
      }
   }

   // the removeRepetition method receives a list,
   // iterates through it and returns a list
   // without any duplicates inside of it.
   public static ArrayList<String> removeRepetition(ArrayList<String> a) {
      ArrayList<String> noRepetitiveList = new ArrayList<>();
      for (String i : a) {
         if (!noRepetitiveList.contains(i)) noRepetitiveList.add(i);
      }
      return noRepetitiveList;
   }
   
   public static void main (String[] args) throws FileNotFoundException {
      File treeCreator = new File("./" + args[0]); // getting the first file
      Scanner in = new Scanner(treeCreator, "US-ASCII"); // assigning the file to be read as a scanner

      Tree tree = new Tree(in.next()); // creating a tree

      // getting the first line separately
      // and adding it to the root of the tree
      String line = in.nextLine();
      String splitLine[] = line.split(" ");
      for (String i : splitLine) {
         if (!i.equals("")) tree.root.insertChild(i);
      }

      // reading the whole file and 
      // finding the node where the input wants to add to
      // and then appending if it's not a medalist
      while (in.hasNextLine()) {
         Tree.Node a = tree.find(in.next());
         line = in.nextLine();
         splitLine = line.split(" ");
         for (String i : splitLine) {
            if (!i.equals("")) {
               if (!i.contains(":")) a.insertChild(i);
               else a.appendChild(i);
            }
         }
      }

      File commands = new File("./" + args[1]);  // getting the second file
      Scanner commandIn = new Scanner(commands, "US-ASCII"); // assigning the second file to be read as a scanner

      // reading the whole file,
      // and executing the commands
      // all the first print statements only output
      // the command, and the variables given in the input.
      while (commandIn.hasNext()) {
         String command = commandIn.next();
         switch (command) {
            case "GetEventsBySport": 
               String sport1 = commandIn.next();
               System.out.printf("%s %s", command, sport1);
               Tree.Node a1 = tree.find(sport1); // finding the node and assigning it to a variable
               for (Tree.Node i : a1.children) {
                  System.out.printf(" %s", i.data); // printing the name of every children for the node found
               }
               System.out.println();
               break;
            
            case "GetWinnersAndCountriesBySportAndEvent":
               String sport2 = commandIn.next();
               String event2 = commandIn.next();
               Tree.Node a2 = tree.find(event2); // finding the event to print the winners of
               System.out.printf("%s %s %s", command, sport2, event2); 
               for (Tree.Node i : a2.children) {
                  System.out.printf(" %s", i.data); // printing every winner
               }
               System.out.println();
               break;
            
            case "GetGoldMedalistAndCountryBySportAndEvent":
               String sport3 = commandIn.next();
               String event3 = commandIn.next();
               System.out.printf("%s %s", command, sport3, event3);
               Tree.Node a3 = tree.find(event3); // finding the event
               System.out.println(a3.children.get(0).data); // printing the gold medal (index 0) for that event
               break;
            
            case "GetAthleteWithMostMedals":
               System.out.printf("%s ", command);
               ArrayList<String> allMedalAth1 = getMedal(tree.root, 0, new ArrayList<String>()); // getting the list of all medalists
               ArrayList<String> totalMedalAthl1 = new ArrayList<>(); // creating a separate list to remove multiples 
               int maxMedalsAth = 0; // initializing the ammount of medals 
               for (int i = 0; i < allMedalAth1.size(); i++) { // going through the list and checking each occurance of a medalist
                  if (Collections.frequency(allMedalAth1, allMedalAth1.get(i)) > maxMedalsAth) { // if they appear more than the current ammount of medals
                     totalMedalAthl1.clear(); // we clear the list organized list
                     totalMedalAthl1.add(allMedalAth1.get(i)); // and add the athlete with more medals to it
                     maxMedalsAth = Collections.frequency(allMedalAth1, allMedalAth1.get(i)); // then increase the ammount of medals the athele(S) got
                  }
                  else if (Collections.frequency(allMedalAth1, allMedalAth1.get(i)) == maxMedalsAth) {
                     totalMedalAthl1.add(allMedalAth1.get(i)); // if they have the same ammount of medal, just add both to the list
                  }
               }
               totalMedalAthl1 = removeRepetition(totalMedalAthl1); // remove duplicates
               System.out.printf("%d ", maxMedalsAth);
               Collections.sort(totalMedalAthl1); // sort the list
               for (String athletes : totalMedalAthl1) {
                  System.out.printf("%s ", athletes); // print the list of medalists
               }
               System.out.println();
               break;
            
            case "GetAthleteWithMostGoldMedals":
               System.out.printf("%s ", command);
               ArrayList<String> allGoldMetalsAth = getMedal(tree.root, 0, new ArrayList<String>(), true); // getting the list of all gold medalists
               ArrayList<String> totalGoldMetalsAthl = new ArrayList<>(); // creating a separate list to remove multiples 
               int maxGoldMedalsAth = 0; // initializing the ammount of medals 
               for (int i = 0; i < allGoldMetalsAth.size(); i++) { // going through the list and checking each occurance of a gold medalist
                  if (Collections.frequency(allGoldMetalsAth, allGoldMetalsAth.get(i)) > maxGoldMedalsAth) { // if they appear more than the current ammount of gold medals
                     totalGoldMetalsAthl.clear(); // we clear the list organized list
                     totalGoldMetalsAthl.add(allGoldMetalsAth.get(i)); // and add the athlete with more medals to it
                     maxGoldMedalsAth = Collections.frequency(allGoldMetalsAth, allGoldMetalsAth.get(i));// then increase the ammount of medals the athele(S) got
                  }
                  else if (Collections.frequency(allGoldMetalsAth, allGoldMetalsAth.get(i)) == maxGoldMedalsAth) { // if they have the same ammount of medal, just add both to the list
                     totalGoldMetalsAthl.add(allGoldMetalsAth.get(i));
                  }
               }
               totalGoldMetalsAthl = removeRepetition(totalGoldMetalsAthl); // remove duplicates
               System.out.printf("%d ", maxGoldMedalsAth);
               Collections.sort(totalGoldMetalsAthl); // sort the list
               for (String athletes : totalGoldMetalsAthl) {
                  System.out.printf("%s ", athletes);  // print the list of medalists
               }
               System.out.println();
               break;
            
            case "GetCountryWithMostMedals":
               System.out.printf("%s ", command);
               ArrayList<String> allmedal = getMedal(tree.root, 1, new ArrayList<String>()); // getting the list of all medalists countries
               ArrayList<String> totalmedals = new ArrayList<>();// creating a separate list to remove multiples 
               int maxMedals = 0; // initializing the ammount of medals 
               for (int i = 0; i < allmedal.size(); i++) { // going through the list and checking each occurance of a medalist
                  if (Collections.frequency(allmedal, allmedal.get(i)) > maxMedals) { // if they appear more than the current ammount of medals
                     totalmedals.clear(); // we clear the list organized list
                     totalmedals.add(allmedal.get(i)); // and add the athlete with more medals to it
                     maxMedals = Collections.frequency(allmedal, allmedal.get(i));// then increase the ammount of medals the athele(S) got
                  }
                  else if (Collections.frequency(allmedal, allmedal.get(i)) == maxMedals) { // if they have the same ammount of medal, just add both to the list
                     totalmedals.add(allmedal.get(i));
                  }
               }
               totalmedals = removeRepetition(totalmedals); // remove duplicates
               System.out.printf("%d ", maxMedals);
               Collections.sort(totalmedals);  // sort the list
               for (String athletes : totalmedals) {
                  System.out.printf("%s ", athletes); // print the list of medalists
               }
               System.out.println();
               break;

            case "GetCountryWithMostGoldMedals":
               System.out.printf("%s ", command);
               ArrayList<String> allGoldMedal = getMedal(tree.root, 1, new ArrayList<String>(), true);  // getting the list of all gold medalists countries
               ArrayList<String> totalGoldMedals = new ArrayList<>(); // creating a separate list to remove multiples 
               int maxGoldMedals = 0; // initializing the ammount of medals 
               for (int i = 0; i < allGoldMedal.size(); i++) { // going through the list and checking each occurance of a gold medalist
                  if (Collections.frequency(allGoldMedal, allGoldMedal.get(i)) > maxGoldMedals) {  // if they appear more than the current ammount of gold medals
                     totalGoldMedals.clear();  // we clear the list organized list
                     totalGoldMedals.add(allGoldMedal.get(i));  // and add the athlete with more medals to it
                     maxGoldMedals = Collections.frequency(allGoldMedal, allGoldMedal.get(i)); // then increase the ammount of medals the athele(S) got
                  }
                  else if (Collections.frequency(allGoldMedal, allGoldMedal.get(i)) == maxGoldMedals) { // if they have the same ammount of medal, just add both to the list
                     totalGoldMedals.add(allGoldMedal.get(i));
                  }
               }
               totalGoldMedals = removeRepetition(totalGoldMedals); // remove duplicates
               System.out.printf("%d ", maxGoldMedals);
               Collections.sort(totalGoldMedals); // sort the list
               for (String athletes : totalGoldMedals) {
                  System.out.printf("%s ", athletes); // print the list of medalists
               }
               System.out.println();
               break;
            
            case "GetSportAndEventByAthlete":
               String athlete = commandIn.next();
               ArrayList<Tree.Node> athleteList = tree.findAll(athlete); // adding all occurrences of an athlede to a list
               System.out.printf("%s %s", command, athlete);
               for (Tree.Node e : athleteList) {
                  System.out.printf(" %s:%s", e.getParent().getParent().data, e.getParent().data); // printing the parents and grandparents for each occurence
               }
               System.out.println();
               break;
         }
      }
   }
}
