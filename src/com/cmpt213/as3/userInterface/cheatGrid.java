package com.cmpt213.as3.userInterface;

import java.util.*;

/**
 * This class creates a 10x10 cheat grid and places tokimons and fokimons
 * randomly on the grid. It also displays the grid.
 */

public class cheatGrid {
    final int NROWS = 10;
    final int NCOLS = 10;
    public String[][] grid = new String [NROWS][NCOLS];
    public List<Integer> tokiRow = new ArrayList<Integer>();
    public List<Integer> tokiCol = new ArrayList<Integer>();
    public List<Integer> fokiRow = new ArrayList<Integer>();
    public List<Integer> fokiCol = new ArrayList<Integer>();

    // Place tokimons and Fokimons randomly on the grid
    public cheatGrid(int numToki,int numFoki, int x, int y) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = " ";
            }
        }
        grid[x][y] = "@"; // initial position cant have Fokimon or Tokimon
        for (int i = 0; i < numToki; i++) {
            int randRow = random.nextInt(10);
            int randCol = random.nextInt(10);
            while (!grid[randRow][randCol].isBlank()) {
                randRow = random.nextInt(10);
                randCol = random.nextInt(10);
            }
            grid[randRow][randCol] = "$";
            tokiRow.add(randRow);
            tokiCol.add(randCol);
        }
        for (int i = 0; i < numFoki; i++) {
            int randRow = random.nextInt(10);
            int randCol = random.nextInt(10);
            while (!(grid[randRow][randCol].isBlank())) {
                randRow = random.nextInt(10);
                randCol = random.nextInt(10);
            }
            grid[randRow][randCol] = "X";
            fokiRow.add(randRow);
            fokiCol.add(randCol);
        }
        grid[x][y] = " "; // set initial position to blank
    }

    public void displayCheatGrid() {
        System.out.println("Cheat Grid: ");
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        char letter = 'A';
        for (int i = 0; i < NROWS; i++) {
            System.out.print(letter + " ");
            for (int j = 0; j < NCOLS; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
            letter++;
        }
        System.out.println();
    }
}
