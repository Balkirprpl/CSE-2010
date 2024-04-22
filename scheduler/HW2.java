import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HW2 {


   // Creating an object Info that stores the name of the class
   // all the possible time slots for that class
   // and if it has any conflicts with other classes
   public static class Info {
      String name;
      ArrayList<String> list = new ArrayList<>();
      boolean available = true;

      Info () {} // default constructor
      
      Info (String newName, String className) { // quick constructor
         name = newName;
         list.add(className);
      }
   }
   // defining the info list as a global variable so it can be
   // accesses throughtout all the methods
   static ArrayList<Info> infoList = new ArrayList<>();
   public static void main (String[] args) throws FileNotFoundException {

      // setting up the input with files and scanning it
      // then adding everything to the global list
      File input = new File("./" + args[0]);
      Scanner in = new Scanner(input, "US-ASCII");
      while (in.hasNextLine()) {
         String line = in.nextLine();
         String lineArr[] = line.split(" ");
         Info a = new Info();
         a.name = lineArr[0];
         for (int i = 1; i < lineArr.length; i++) {
            a.list.add(lineArr[i]);
         }
         infoList.add(a);
      }
   
      // initializing the result list
      // and setting it to be
      // the output of the recursive
      // method.
      ArrayList<Info> result = solveSchedule(0, new ArrayList<Info>());
      checkConflict(result); // checking for conflicts


      // outputting
      System.out.println("---Course schedule---");
      printSchedule(result, 2);
      System.out.println("---Courses with a time conflict---");
      printSchedule(result, 3);
   }

   // creating the method that compares two lists
   // and outputs the "best" one
   public static ArrayList<Info> compareList (ArrayList<Info> a, ArrayList<Info> b) {
      int size = a.size(); // getting the size i have to iterate
      int sumA = 0, sumB = 0; // initializing the importance values for each list as 0
      if (a.size() > b.size()) return a; // comparing sizes
      else if (a.size() < b.size()) return b;
      else { // if they have the same size: get which one has more importance
         for (int i = 0; i < size; i++) {
            if (a.get(i).name.equals(infoList.get(i).name) && a.get(i).available) sumA += i;
            if (b.get(i).name.equals(infoList.get(i).name) && b.get(i).available) sumB += i;
         }

         // returning the one with the best importance
         if (sumB > sumA) {
            return b;
         }
         else {
            return a;
         }
      }
   }

   // method that goes throug the whole list and checks
   // if any time conflicts occur.
   public static void checkConflict (ArrayList<Info> a) {
      for (int i = 0; i < a.size(); i++) {
         for (int j = 0; j < a.size(); j++) {
            String time = a.get(i).list.get(0);
            if (i != j && j > i) {
               if (time.equals(a.get(j).list.get(0))) a.get(j).available = false;
            }
         }
      }
   }

   // overriding the method that prints the list.
   // command 1: prints the list as it is
   // command 2: prints only the objects w/o time conflict
   // command 3: prints only the objects with time conflict
   public static void printSchedule (ArrayList<Info> a) {
      printSchedule(a, 1);
   }

   public static void printSchedule (ArrayList<Info> a, int command) {
      if (command == 1) {
         for (int i = 0; i < a.size(); i++) {
            System.out.printf("%d %s %s %b%n", i, a.get(i).name, a.get(i).list.toString(), a.get(i).available);
         }
      }
      else if (command == 2) {
         for (int i = 0; i < a.size(); i++) {
            if (a.get(i).available) System.out.printf("%s %s%n", a.get(i).name, a.get(i).list.toString());
         }
      }
      else if (command == 3) {
         for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).available) System.out.printf("%s %s%n", a.get(i).name, a.get(i).list.toString());
         }
      }
   }
   
   // recursive method that returns the best schedule
   public static ArrayList<Info> solveSchedule (int k, ArrayList<Info> masterList) {
      // checking if the method reached
      // the end of the list
      // checking conflicts and returning
      if (k >= infoList.size()) {
         checkConflict(masterList);
         return masterList; // basecase
      }
      // getting the list of class times for the class "k"
      ArrayList<String> currentSchedule = infoList.get(k).list;
      
      // checking the size of the list
      // if size == 1, just add the time to the list
      // and continue to solve it.
      // else creates a branching
      if (currentSchedule.size() == 1) {
         masterList.add(infoList.get(k));
         masterList = solveSchedule(k + 1, masterList);
      }
      else {
         // creating a copy of the list before insertion
         ArrayList<Info> mLCopy = (ArrayList) masterList.clone();

         // adding the first class time to the list and calling
         // the solve method again to get the whole list
         masterList.add(new Info(infoList.get(k).name, currentSchedule.get(0)));
         masterList = (ArrayList) solveSchedule(k + 1, masterList).clone();

         // iterating from 1 to the n number of class times
         // (because the 0th was already defined)
         for (int i = 1; i < currentSchedule.size(); i++) {
            
            // making a new Info object and adding
            // it to the copy list
            Info newInfo = new Info(infoList.get(k).name, currentSchedule.get(i));
            mLCopy.add(newInfo);
            
            // creating a secure copy of the new list
            // so it does not interfere with the original copy
            ArrayList<Info> secureCopy = (ArrayList) mLCopy.clone();

            // solving the copy list
            mLCopy = (ArrayList) solveSchedule(k + 1, mLCopy).clone();

            // defining the main list as the best list
            // between the main list and the copy (branching)
            masterList = (ArrayList) compareList(masterList, mLCopy).clone();

            // removing the last class added
            // and defining the copy to be that list
            for (int j = 0; j < secureCopy.size(); j++)
               if (secureCopy.get(j).name.compareTo(infoList.get(k).name) == 0)
                  secureCopy.remove(j);
            mLCopy = secureCopy;
         }
      }
      return masterList; // returning the best list
   }
}