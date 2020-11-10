package com.cmpt213.as3.gameLogic;
import com.cmpt213.as3.userInterface.*;
import java.util.*;

/**
 * This class takes in user's input and displays
 * either a cheat grid or a normal game grid
 */

public class TokimonFinder {
    int numToki;
    int numFoki;
    boolean cheat;
    String initPosition;
    int x;
    int y;
    String choice;
    int tokisCollected;
    int numSpells = 3;
    int tokiRevealCount;
    int fokiRevealCount;

    public static void main(String[] args) {
        TokimonFinder myGame = new TokimonFinder();

        if (args.length > 3) {
            System.out.println("Only 0 to 3 arguments will be accepted\n" +
                    "1. Number of Tokimons \n" +
                    "2. Number of Fokimons \n" +
                    "3. Game mode");
            System.exit(-1);
        } else if (args.length == 0) {
            myGame.numToki = 10;
            myGame.numFoki = 5;
            myGame.cheat = false;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].matches("(.*)numToki(.*)")) {
                args[i] = args[i].replace("--numToki=", "");
                if (args[i].isBlank()) {
                    myGame.numToki = 10;
                } else if (Integer.parseInt(args[i]) < 5) {
                    System.out.println("Number of Tokimons cannot be less than 5.");
                    System.exit(-1);
                } else if (Integer.parseInt(args[i]) >= 95) {
                    System.out.println("Number of Tokimons cannot exceed 95");
                    System.exit(-1);
                } else {
                    myGame.numToki = Integer.parseInt(args[i]);
                }

            } else if (args[i].matches("(.*)numFoki(.*)")) {
                args[i] = args[i].replace("--numFoki=", "");
                if (args[i].isBlank()) {
                    myGame.numFoki = 5;
                } else if (Integer.parseInt(args[i]) >= 95) {
                    System.out.println("Number of Fokimons cannot exceed 95");
                    System.exit(-1);
                } else {
                    myGame.numFoki = Integer.parseInt(args[i]);
                }
            } else if (args[i].matches("(.*)cheat(.*)")) {
                myGame.cheat = true;
            }
        }

        HashMap<Character, Integer> rowNumber = new HashMap<Character, Integer>();
        rowNumber.put('A', 0);
        rowNumber.put('B', 1);
        rowNumber.put('C', 2);
        rowNumber.put('D', 3);
        rowNumber.put('E', 4);
        rowNumber.put('F', 5);
        rowNumber.put('G', 6);
        rowNumber.put('H', 7);
        rowNumber.put('I', 8);
        rowNumber.put('J', 9);

        // Get and validate initial position from user
        System.out.println("\nPlease enter an initial position on the game board:\n" +
                "-Rows are lettered from A to J\n" +
                "-Columns are numbered from 1 to 10");
        Scanner input = new Scanner(System.in);
        myGame.initPosition = input.nextLine().toUpperCase().trim();
        while (myGame.initPosition.length() > 3 ||
                myGame.initPosition.length() < 2) {
            System.out.println("The position entered is not valid\n" +
                    "Example: B5\n" +
                    "Please try again");
            myGame.initPosition = input.nextLine().toUpperCase();
        }
        while(Character.isDigit(myGame.initPosition.charAt(0)) || (myGame.initPosition.charAt(0) - 'A' > 9) ||
                Character.isLetter(myGame.initPosition.charAt(1))) {
            System.out.println("The position entered is not valid\n" +
                    "Example: B5\n" +
                    "Please try again");
            myGame.initPosition = input.nextLine().toUpperCase().trim();
        }
        if (myGame.initPosition.length() == 3) {
            String columnString = new StringBuilder().
                    append(myGame.initPosition.charAt(1)).
                    append(myGame.initPosition.charAt(2)).toString();
            int rowPosition = rowNumber.get(myGame.initPosition.charAt(0));
            int colPosition = Integer.parseInt(columnString) - 1;
            while (rowPosition < 0 || rowPosition > 9 ||
                    colPosition < 0 || colPosition > 9 || Character.isLetter(myGame.initPosition.charAt(2))) {
                System.out.println("The position entered is invalid\n" +
                        "Please try again");
                myGame.initPosition = input.nextLine().toUpperCase().trim();
                if (myGame.initPosition.length() != 3) { // could be length 2
                    break;
                }
                columnString = new StringBuilder().
                        append(myGame.initPosition.charAt(1)).
                        append(myGame.initPosition.charAt(2)).toString();
                rowPosition = rowNumber.get(myGame.initPosition.charAt(0));
                colPosition = Integer.parseInt(columnString) - 1;
            }
            myGame.x = rowPosition;
            myGame.y = colPosition;
        }

        if (myGame.initPosition.length() == 2) {
            System.out.println("two");
            String columnString = new StringBuilder().
                    append(myGame.initPosition.charAt(1)).toString();
            int rowPosition = rowNumber.get(myGame.initPosition.charAt(0));
            int colPosition = Integer.parseInt(columnString) - 1;
            while (rowPosition < 0 || rowPosition > 9 ||
                    colPosition < 0 || colPosition > 9) {
                System.out.println("The position entered is invalid\n" +
                        "Please try again");
                myGame.initPosition = input.nextLine().toUpperCase().trim();
                columnString = new StringBuilder().
                        append(myGame.initPosition.charAt(1)).toString();
                rowPosition = rowNumber.get(myGame.initPosition.charAt(0));
                colPosition = Integer.parseInt(columnString) - 1;
            }
            myGame.x = rowPosition;
            myGame.y = colPosition;
        }

        cheatGrid cheatGrid = new cheatGrid(myGame.numToki, myGame.numFoki, myGame.x, myGame.y);
        // display cheat grid if desired
        if(myGame.cheat) {
            cheatGrid.displayCheatGrid();
        }

        // display normal grid otherwise
        userGrid userGrid = new userGrid(myGame.x, myGame.y);
        userGrid.temp[0][0] = "~";
        userGrid.temp[myGame.x][myGame.y] = "@";
        userGrid.displayGrid();
        myGame.userControls(cheatGrid, userGrid);
    }

    // checks if the user found Tokimon or landed on a Fokimon (lost)
    public void foundOrLost(cheatGrid cheatGrid, userGrid userGrid) {
        if(!cheatGrid.grid[x][y].isBlank()) {
            if(cheatGrid.grid[x][y].equals("$")) { // if land on Tokimon
                userGrid.temp[x][y] = userGrid.temp[x][y] + cheatGrid.grid[x][y]; // set to "@$"
                tokisCollected++;
                numToki--;
                System.out.println("You found a Tokimon!");
            }
            else { // if land on Tokimon
                userGrid.temp[x][y] = userGrid.temp[x][y] + cheatGrid.grid[x][y]; // set to "@X"
                cheatGrid.grid[x][y] = "@" + cheatGrid.grid[x][y];
                cheatGrid.displayCheatGrid();
                System.out.println("Landed on a Fokimon. You lost!");
                System.exit(1);
            }
        }
    }

    public void userControls(cheatGrid cheatGrid, userGrid userGrid) {
        if(numToki == 0) {
            System.out.println("Congratulations! You've collected all the Tokimons. You win!");
            System.exit(1);
        }
        System.out.println("Controls:\nW - move up\n" +
                        "A - move left\n" +
                        "S - move down\n" +
                        "D - move right\n" +
                        "SPELL - use a spell");
        Scanner input = new Scanner(System.in);
        choice = input.nextLine().toUpperCase().trim();
        while (!(choice.equals("SPELL") || choice.equals("W") || choice.equals("A")
                || choice.equals("S") || choice.equals("D"))) {
            System.out.println("\nInvalid option chosen. Please try again\nControls:\nW - move up\n" +
                            "A - move left\n" +
                            "S - move down\n" +
                            "D - move right\n" +
                            "SPELL - use a spell" +
                            "E - exit game");
            choice = input.nextLine().toUpperCase().trim();
        }
        if(choice.equals("SPELL")) {
            if(numSpells == 0) {
                System.out.println("You've used up all your spells. Try another option");
                userControls(cheatGrid, userGrid);
            }
            System.out.println("Spells:\n" +
                    "1 - Jump to another grid location\n" +
                    "2 - Randomly reveal location of one Tokimons\n" +
                    "3 - Randomly remove one Fokimon");
            String spellChoice = input.nextLine().trim();
            while (!(spellChoice.equals("1") || spellChoice.equals("2")|| spellChoice.equals("3"))) {
                System.out.println("\nInvalid option chosen. Please try again\nSpells:\n" +
                        "1 - Jump to another grid location\n" +
                        "2 - Randomly reveal location of one Tokimons\n" +
                        "3 - Randomly remove one Fokimon");
                spellChoice = input.nextLine().trim();
            }
            if(spellChoice.equals("1")) { // jump to another location
                HashMap<Character, Integer> rowNumber = new HashMap<Character, Integer>();
                rowNumber.put('A', 0);
                rowNumber.put('B', 1);
                rowNumber.put('C', 2);
                rowNumber.put('D', 3);
                rowNumber.put('E', 4);
                rowNumber.put('F', 5);
                rowNumber.put('G', 6);
                rowNumber.put('H', 7);
                rowNumber.put('I', 8);
                rowNumber.put('J', 9);
                if(userGrid.temp[x][y].equals("@")) { // remove @ since it will be moved to a different location
                    userGrid.temp[x][y] = " ";
                }
                else if(userGrid.temp[x][y].equals("@$")) {
                    userGrid.temp[x][y] = "$";
                }
                System.out.println("Where do you want to land on the grid?");
                String spellInput = input.nextLine().toUpperCase().trim();
                while (spellInput.length() > 3 || spellInput.length() < 2) {
                    System.out.println("The position entered is not valid\n" +
                            "Example: B5\n" +
                            "Please try again");
                    spellInput = input.nextLine().toUpperCase().trim();
                }
                while(Character.isDigit(spellInput.charAt(0)) || (spellInput.charAt(0) - 'A' > 9) ||
                        Character.isLetter(spellInput.charAt(1))) {
                    System.out.println("The position entered is not valid\n" +
                            "Example: B5\n" +
                            "Please try again");
                    spellInput = input.nextLine().toUpperCase().trim();
                }
                if (spellInput.length() == 3) {
                    String columnString = new StringBuilder().
                            append(spellInput.charAt(1)).
                            append(spellInput.charAt(2)).toString();
                    int rowPosition = rowNumber.get(spellInput.charAt(0));
                    int colPosition = Integer.parseInt(columnString) - 1;
                    while (rowPosition < 0 || rowPosition > 9 ||
                            colPosition < 0 || colPosition > 9 || Character.isLetter(spellInput.charAt(2))) {
                        System.out.println(rowPosition + " " + colPosition);
                        System.out.println("The position entered is invalid\n" +
                                "Please try again");
                        spellInput = input.nextLine().toUpperCase().trim();
                        if (spellInput.length() != 3) { // could be length 2
                            break;
                        }
                        columnString = new StringBuilder().
                                append(spellInput.charAt(1)).
                                append(spellInput.charAt(2)).toString();
                        rowPosition = rowNumber.get(spellInput.charAt(0));
                        colPosition = Integer.parseInt(columnString) - 1;
                    }
                    x = rowPosition;
                    y = colPosition;
                }

                if (spellInput.length() == 2) {
                    String columnString = new StringBuilder().
                            append(spellInput.charAt(1)).toString();
                    int rowPosition = rowNumber.get(spellInput.charAt(0));
                    int colPosition = Integer.parseInt(columnString) - 1;
                    while (rowPosition < 0 || rowPosition > 9 ||
                            colPosition < 0 || colPosition > 9) {
                        System.out.println(rowPosition + " " + colPosition);
                        System.out.println("The position entered is invalid\n" +
                                "Please try again");
                        spellInput = input.nextLine().toUpperCase().trim();
                        columnString = new StringBuilder().
                                append(spellInput.charAt(1)).toString();
                        rowPosition = rowNumber.get(spellInput.charAt(0));
                        colPosition = Integer.parseInt(columnString) - 1;
                    }
                    x = rowPosition;
                    y = colPosition;
                }
                userGrid.temp[x][y] = "@"; // set new location to "@"
                userGrid.displayGrid();
                foundOrLost(cheatGrid, userGrid);
                numSpells--;
                System.out.println("Number of Tokimons collected: " + tokisCollected);
                System.out.println("Number of Tokimons remaining: " + numToki);
                System.out.println("Number of spells remaining: " + numSpells);
                userControls(cheatGrid, userGrid); // ask user for next choice
            }
            else if(spellChoice.equals("2")) {
                userGrid.temp[cheatGrid.tokiRow.get(tokiRevealCount)][cheatGrid.tokiCol.get(tokiRevealCount)] = "$";
                userGrid.displayGrid();
                tokiRevealCount++;
                numSpells--;
                System.out.println("A Tokimon has been revealed");
                System.out.println("Number of Tokimons collected: " + tokisCollected);
                System.out.println("Number of Tokimons remaining: " + numToki);
                System.out.println("Number of spells remaining: " + numSpells);
                userControls(cheatGrid, userGrid);
            }
            else if(spellChoice.equals("3")) {
                cheatGrid.grid[cheatGrid.fokiRow.get(fokiRevealCount)][cheatGrid.fokiCol.get(fokiRevealCount)] = " ";
                userGrid.displayGrid();
                fokiRevealCount++;
                numSpells--;
                System.out.println("A Fokimon has been removed");
                System.out.println("Number of Tokimons collected: " + tokisCollected);
                System.out.println("Number of Tokimons remaining: " + numToki);
                System.out.println("Number of spells remaining: " + numSpells);
                userControls(cheatGrid, userGrid);
            }
        }
        else if(choice.equals("W")) {
            if(x == 0) {
                System.out.println("Cannot go further. Try another option");
                userControls(cheatGrid, userGrid);
            }
            userGrid.temp[x][y] = " ";
            x--;
            userGrid.temp[x][y] = "@";
            foundOrLost(cheatGrid, userGrid);
            userGrid.displayGrid();
            System.out.println("Number of Tokimons collected: " + tokisCollected);
            System.out.println("Number of Tokimons remaining: " + numToki);
            System.out.println("Number of spells remaining: " + numSpells);
            userControls(cheatGrid, userGrid);
        }
        else if(choice.equals("S")) {
            if(x == 9) {
                System.out.println("Cannot go further. Try another option");
                userControls(cheatGrid, userGrid);
            }
            userGrid.temp[x][y] = " ";
            x++;
            userGrid.temp[x][y] = "@";
            foundOrLost(cheatGrid, userGrid);
            userGrid.displayGrid();
            System.out.println("Number of Tokimons collected: " + tokisCollected);
            System.out.println("Number of Tokimons remaining: " + numToki);
            System.out.println("Number of spells remaining: " + numSpells);
            userControls(cheatGrid, userGrid);
        }
        else if(choice.equals("A")) {
            if(y == 0) {
                System.out.println("Cannot go further. Try another option");
                userControls(cheatGrid, userGrid);
            }
            userGrid.temp[x][y] = " ";
            y--;
            userGrid.temp[x][y] = "@";
            foundOrLost(cheatGrid, userGrid);
            userGrid.displayGrid();
            System.out.println("Number of Tokimons collected: " + tokisCollected);
            System.out.println("Number of Tokimons remaining: " + numToki);
            System.out.println("Number of spells remaining: " + numSpells);
            userControls(cheatGrid, userGrid);
        }
        else if(choice.equals("D")) {
            if(y == 9) {
                System.out.println("Cannot go further. Try another option");
                userControls(cheatGrid, userGrid);
            }
            userGrid.temp[x][y] = " ";
            y++;
            userGrid.temp[x][y] = "@";
            foundOrLost(cheatGrid, userGrid);
            userGrid.displayGrid();
            System.out.println("Number of Tokimons collected: " + tokisCollected);
            System.out.println("Number of Tokimons remaining: " + numToki);
            System.out.println("Number of spells remaining: " + numSpells);
            userControls(cheatGrid, userGrid);
        }
        else {
            System.out.println("Invalid option. Try again");
            userControls(cheatGrid, userGrid);
        }
    }
}
