/***
* CaveExplore.java
* Somika Ganesh
* CS352
* David Law
* CaveExplorer class serach the cave layot to find path to mirror and mark 'V' for the path visited and mark S before the M if mirror is found.
**/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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
        Queue<int[]> queue = new LinkedList<>();
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

        // Initialize the queue with 'S' position
        queue.add(new int[]{startRow, startCol});

        // Define possible directions (West, East, North, South)
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            // Check if 'M' is found
            if (cave[row][col] == 'M') {
            	mark[previousRow][previousCol] = 'S';
                return true; // Path to mirror pool is found
            }

            // Explore neighboring cells
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                // Check if the new position is valid and not visited
                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols
                    && cave[newRow][newCol] != 'R' && !visited[newRow][newCol]) {
                	 // Mark the cell as visited
                	previousRow = row;
                	previousCol = col;
                	mark[row][col]='V';
                    visited[row][col] = true;
                    // Add the new position to the queue
                    queue.add(new int[]{newRow, newCol});
                }
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
            boolean foundNextMove = false;

            // Explore neighboring cells
            for (int i = 0; i < directions.length; i++) {
                int newRow = currentRow + directions[i][0];
                int newCol = currentCol + directions[i][1];

                // Check if the new position is valid and not visited
                if (isValidCell(newRow, newCol) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    currentRow = newRow;
                    currentCol = newCol;
                    pathBuilder.append(directionSymbols[i]);
                    foundNextMove = true;
                    break;
                }
            }

            if (!foundNextMove) {
                return ""; // No path found
            }
        }

        return pathBuilder.toString();
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
	    
	    System.out.println("\nIs there a path to the mirror pool? " + hasPath);
	    if (hasPath) {
            System.out.println("Path taken: " + ce1.getPath());
        }else {
	        System.out.println("No path found.");
	    }
       
		
		// Step 5/6: Repeat for a different CaveExplorer object read from a file
		// Uncomment code below to start testing your 1-parameter constructor

		
	    String fileName = "testdat.txt"; 
		try { 
			CaveExplorer ce2 = new CaveExplorer(fileName); 
			System.out.println("\nStarting CaveExplorer 2");
			System.out.println("Initial state:");
	        System.out.println(ce2.toString());
	        
            hasPath = ce2.solve();
            
            System.out.println("\nFinal state:");
	        System.out.println(ce2.toString());
            System.out.println("\nIs there a path to the mirror pool? " + hasPath);
            if (hasPath) {
                System.out.println("Path taken: " + ce2.getPath());
            }else {
    	        System.out.println("No path found.");
            }
		} 
		catch (FileNotFoundException e ) {
			System.out.println("Can't find file " + fileName); 
		}
		catch (Exception e) {
			System.out.println("Other error: " + e.getMessage());
		}
		
		System.out.println("Finished CaveExplorer");
	}

}