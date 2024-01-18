import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Collections;

public class HW6wrong {
   public static class Tile implements Comparable<Tile> {
      int weight;
      int y;
      int x;
      char container;
      boolean visited = false;
      Tile up;
      Tile right;
      Tile left;
      Tile down;

      Tile (char c) {
         this.container = c;
         this.weight = 3;
      }
      Tile (char c, int y, int x) {
         this.container = c;
         this.weight = 3;
         this.y = y;
         this.x = x;
      }

      @Override
      public int compareTo(Tile b) {
         return this.container - b.container;
      }
   }

   public static void unvisitGrid(Tile[][] grid) {
      for (int i = 0; i < grid.length; i++) {
         for (Tile j : grid[i]) {
            j.visited = false;
         }
      }
   }

   public static void playerMove(Tile[][] grid, Tile player) {
      Scanner move = new Scanner(System.in);
      System.out.printf("Please enter your move [u(p), d(own), l(eft), or r(ight)]: ");
      int directionY = 0;
      int directionX = 0;
      switch (move.next().charAt(0)) {
         case 'u': 
            directionY = -1;
            directionX = 0;
            break;
         case 'd':
            directionY = 1;
            directionX = 0;
            break;
         case 'l':
            directionY = 0;
            directionX = -1;
            break;
         case 'r':
            directionY = 0;
            directionX = 1;
            break;
      }
      if (!checkBounds(grid, player, directionY, directionX)) return;
      grid[player.y][player.x].container = ' ';
      grid[player.y + directionY][player.x + directionX].container = 'T';
      player.y += directionY;
      player.x += directionX;
   }

   public static boolean checkBounds(Tile[][] grid, Tile player, int y, int x) {

      if (player.x + x >= grid[0].length) {
         System.out.printf("x > gridx%n");
         return false;
      }
      else if (player.x + x < 0) {
         System.out.printf("x < 0%n");
         return false;
      }
      if (player.y + y >= grid.length) {
         System.out.printf("y > gridy%n");
         return false;
      }
      else if (player.y + y < 0) {
         System.out.printf("y < 0%n");
         return false;
      }
      if (grid[player.y + y][player.x + x].container == '#') return false;
      return true;
   }

   public static boolean checkWin (Tile player, Tile tower) {
      return (player.x == tower.x && player.y == tower.y);
   }

   public static void printArray(Tile[][] grid) {
      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[0].length; j++) {
            System.out.printf("%c", grid[i][j].container);
         }
         System.out.printf("%n");
      }
   }

   public static boolean checkBugs (ArrayList<Tile> object) {
      for (int i = 2; i < object.size(); i++) {
         if (object.get(1).y == object.get(i).y && object.get(1).x == object.get(i).x) return true;
      }
      return false;
   }

   public static void bfs (Tile[][] grid, Tile initialTile, Tile finalTile) {
      Deque<Tile> deque = new LinkedList<Tile>();
      int x = initialTile.x;
      int y = initialTile.y;
      initialTile.visited = true;
      deque.add(initialTile);
      while (!deque.isEmpty()) {
         Tile removed = deque.remove();
         if (grid[removed.y + 1][removed.x].container == ' ') {
            deque.add(grid[removed.y + 1][removed.x]);
            grid[removed.y + 1][removed.x].visited = true;
         }
         if (grid[removed.y - 1][removed.x].container == ' ') {
            deque.add(grid[removed.y - 1][removed.x]);
            grid[removed.y - 1][removed.x].visited = true;
         }
         if (grid[removed.y][removed.x - 1].container == ' ') {
            deque.add(grid[removed.y][removed.x - 1]);
            grid[removed.y][removed.x - 1].visited = true;
         }
         if (grid[removed.y][removed.x - 1].container == ' ') {
            deque.add(grid[removed.y][removed.x + 1]);
            grid[removed.y][removed.x + 1].visited = true;
         }
      }
   }

   public static void main (String[] args) throws FileNotFoundException {

      Scanner in = new Scanner(new File("./" + args[0]));

      int rows = in.nextInt();
      int columns = in.nextInt();

      Tile[][] grid = new Tile[rows][columns];
      ArrayList<Tile> objects = new ArrayList<>();

      String line = in.nextLine();

      for (int i = 0; i < rows; i++) {
         line = in.nextLine();
         for (int j = 0; j < columns; j++) {
            if (line.charAt(j) != ' ' && line.charAt(j) != '#')
                  objects.add(new Tile(line.charAt(j), i, j));
            grid[i][j] = new Tile(line.charAt(j), i, j);
         }
      }

      Collections.sort(objects);
      for (Tile i : objects) {
         System.out.printf("%c ", i.container);
      }
      System.out.println();

      while (!checkWin(objects.get(1), objects.get(0))) {
         if (checkBugs(objects)) {
            System.out.printf("A bug is no longer hungry%n");
            break;
         }
         printArray(grid);
         playerMove(grid, objects.get(1));
      }
   }
}
