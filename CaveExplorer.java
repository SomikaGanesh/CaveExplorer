/***
 * CaveExplore.java
 * Somika Ganesh
 * CS352
 * David Law
 * CaveExplorer class serach the cave layot to find path to mirror and mark 'V' for the path visited and mark S before the M if mirror is found.
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class CaveExplorer {
    private char[][] cave;
    private int numRows;
    private int numCols;
    private char[][] mark;

    // Step 1: Zero parameter constructor to create the specified cave
    public CaveExplorer() {
        String layout =
                "RRRRRR\n" +
                        "R..SRR\n" +
                        "R.RRRR\n" +
                        "R.MRRR\n" +
                        "RRRRRR\n";

        String[] rows = layout.split("\n");
        numRows = rows.length;
        numCols = rows[0].length();
        cave = new char[numRows][numCols];
        mark = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                cave[i][j] = rows[i].charAt(j);

            }
        }
    }



    // Step 2: toString method
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (mark[i][j] == 'V' || mark[i][j] == 'S')
                    result.append(mark[i][j]);
                else
                    result.append(cave[i][j]);
            }
            result.append('\n');
        }
        return result.toString();
    }


    // Step 3: solve method
    public boolean solve() {
        Stack<int[]> stack = new Stack<>();
        boolean[][] visited = new boolean[numRows][numCols];
        int previousRow = 0;
        int previousCol = 0;

        // Start from the 'S' position
        int startRow = -1;
        int startCol = -1;

        // Find the 'S' position
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (cave[i][j] == 'S') {
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
        }

        // Check if 'S' position is found
        if (startRow == -1 || startCol == -1) {
            return false; // 'S' not found
        }

        // Initialize the stack with 'S' position
        stack.push(new int[]{startRow, startCol});

        // Define possible directions (West, East, North, South)
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int row = current[0];
            int col = current[1];

            visited[row][col] = true;

            // Check if 'M' is found
            if (cave[row][col] == 'M') {
                mark[previousRow][previousCol] = 'S';
                return true; // Path to mirror pool is found
            }

            boolean foundNextMove = false;

            // Explore neighboring cells
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                // Check if the new position is valid and not visited
                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols
                        && cave[newRow][newCol] != 'R' && !visited[newRow][newCol]) {
                    stack.push(new int[]{newRow, newCol});
                    // Mark the cell as visited
                    previousRow = row;
                    previousCol = col;
                    mark[row][col]='V';
                    foundNextMove = true;
                    break; // Explore the new direction
                }
            }
            if (!foundNextMove) {
                // Dead end, backtrack
                stack.pop();
            }
        }

        return false; // No path to mirror pool is found
    }

    // Step 4: getPath method
    public String getPath() {
        int startRow = -1;
        int startCol = -1;

        // Find the starting position
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (cave[i][j] == 'S') {
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
        }

        if (startRow == -1 || startCol == -1) {
            return ""; // No starting point found
        }

        int currentRow = startRow;
        int currentCol = startCol;
        boolean[][] visited = new boolean[numRows][numCols];
        StringBuilder pathBuilder = new StringBuilder();

        // Define possible directions (West, East, North, South)
        int[][] directions = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
        char[] directionSymbols = { 'W', 'E', 'N', 'S' };

        while (cave[currentRow][currentCol] != 'M') {
            visited[currentRow][currentCol] = true;
            boolean foundNextMove = false;

            // Explore neighboring cells
            for (int i = 0; i < directions.length; i++) {
                int newRow = currentRow + directions[i][0];
                int newCol = currentCol + directions[i][1];

                // Check if the new position is valid and not visited
                if (isValidCell(newRow, newCol) && !visited[newRow][newCol]) {
                    currentRow = newRow;
                    currentCol = newCol;
                    pathBuilder.append(directionSymbols[i]);
                    foundNextMove = true;
                    break;
                }
            }

            if (!foundNextMove) {
                // Dead end, backtrack
                char lastMove = pathBuilder.charAt(pathBuilder.length() - 1);
                int backtrackDirectionIndex = getDirectionIndex(lastMove);
                currentRow -= directions[backtrackDirectionIndex][0];
                currentCol -= directions[backtrackDirectionIndex][1];
                pathBuilder.deleteCharAt(pathBuilder.length() - 1); // Remove last move
            }
        }

        return pathBuilder.toString();
    }
    private int getDirectionIndex(char move) {
        char[] directionSymbols = {'W', 'E', 'N', 'S'};
        for (int i = 0; i < directionSymbols.length; i++) {
            if (move == directionSymbols[i]) {
                return i;
            }
        }
        return -1; // Invalid move
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols && cave[row][col] != 'R';
    }


    // Step 5: One parameter constructor to read from a file

    public CaveExplorer(String fname) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fname));
        numRows = in.nextInt();
        numCols = in.nextInt();
        String s = in.nextLine(); // Skip newline character

        // Construct cave and populate with rest of data in the file
        cave = new char[numRows][numCols];
        mark = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            String row = in.nextLine();
            for (int j = 0; j < numCols; j++) {
                cave[i][j] = row.charAt(j);
            }
        }

        in.close();


    }


    public static void main(String[] args) {
        System.out.println("Starting CaveExplorer");

        // Create a CaveExplorer object and print the starting layout
        CaveExplorer ce1 = new CaveExplorer();
        System.out.println("Starting CaveExplorer 1");
        System.out.println("Initial state:");
        System.out.println(ce1.toString());

        // Call solve
        boolean hasPath = ce1.solve();

        // Print the final layout, whether there is a path, and if so, what it is.
        System.out.println("\nFinal state:");
        System.out.println(ce1.toString());

        System.out.println("Is there a path to the mirror pool? " + hasPath);
        if (hasPath) {
            System.out.println("Path taken: " + ce1.getPath());
        }else {
            System.out.println("No path found.");
        }


        // Step 5/6: Repeat for a different CaveExplorer object read from a file
        // Uncomment code below to start testing your 1-parameter constructor



        try {
            String fileName = "testdat.txt";
            CaveExplorer ce2 = new CaveExplorer(fileName);
            System.out.println("\nStarting CaveExplorer 2");
            System.out.println("Initial state:");
            System.out.println(ce2.toString());

            hasPath = ce2.solve();

            System.out.println("\nFinal state:");
            System.out.println(ce2.toString());
            System.out.println("Is there a path to the mirror pool? " + hasPath);
            if (hasPath) {
                System.out.println("Path taken: " + ce2.getPath());
            }else {
                System.out.println("No path found.");
            }

            fileName = "testdat1.txt";
            CaveExplorer ce3 = new CaveExplorer(fileName);
            System.out.println("\nStarting CaveExplorer 3");
            System.out.println("Initial state:");
            System.out.println(ce3.toString());

            boolean hasPath2 = ce3.solve();

            System.out.println("\nFinal state:");
            System.out.println(ce3.toString());
            System.out.println("Is there a path to the mirror pool? " + hasPath2);
            if (hasPath2) {
                System.out.println("Path taken: " + ce3.getPath());
            } else {
                System.out.println("No path found.");
            }


            fileName = "testdat2.txt";
            CaveExplorer ce4 = new CaveExplorer(fileName);
            System.out.println("\nStarting CaveExplorer 4");
            System.out.println("Initial state:");
            System.out.println(ce4.toString());

            boolean hasPath3 = ce4.solve();

            System.out.println("\nFinal state:");
            System.out.println(ce4.toString());
            System.out.println("Is there a path to the mirror pool? " + hasPath3);
            if (hasPath3) {
                System.out.println("Path taken: " + ce4.getPath());
            } else {
                System.out.println("No path found.");
            }


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Finished CaveExplorer");
    }

}