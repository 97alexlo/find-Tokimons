package com.cmpt213.as3.userInterface;

/**
 * This class makes an empty 10x10 grid with the user's location and displays it.
 */

public class userGrid {
    final int NROWS = 10;
    final int NCOLS = 10;
    public String[][] temp = new String [NROWS][NCOLS];

    public userGrid(int rowPosition ,int colPosition){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                temp[i][j] = "~";
            }
        }
        temp[rowPosition][colPosition]="@";
    }

    public void displayGrid(){
        System.out.println("Game Grid: ");
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        char letter = 'A';
        for (int i = 0; i < NROWS; i++) {
            System.out.print(letter + " ");
            for (int j = 0; j < NCOLS; j++) {
                System.out.print(temp[i][j] + " ");
            }
            System.out.println();
            letter++;
        }
        System.out.println();
    }
}
