import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *
 * @author mvail
 * @author harrisonjansenvanbeek
 */
public class CircuitBoard {
    /**
     * current contents of the board
     */
    private char[][] board;
    /**
     * location of row,col for '1'
     */
    private Point startingPoint;
    /**
     * location of row,col for '2'
     */
    private Point endingPoint;

    //constants you may find useful
    private final int ROWS; //initialized in constructor
    private final int COLS; //initialized in constructor
    private final char OPEN = 'O';    //capital 'o', an open position
    private final char CLOSED = 'X';//a blocked position
    private final char TRACE = 'T';    //part of the trace connecting 1 to 2
    private final char START = '1';    //the starting component
    private final char END = '2';    //the ending component
    private final String ALLOWED_CHARS = "OXT12"; //useful for validating with indexOf

    /**
     * Construct a CircuitBoard from a given board input file, where the first
     * line contains the number of rows and columns as ints and each subsequent
     * line is one row of characters representing the contents of that position.
     * Valid characters are as follows:
     * 'O' an open position
     * 'X' an occupied, unavailable position
     * '1' first of two components needing to be connected
     * '2' second of two components needing to be connected
     * 'T' is not expected in input files - represents part of the trace
     * connecting components 1 and 2 in the solution
     *
     * @param filename file containing a grid of characters
     * @throws FileNotFoundException      if Scanner cannot open or read the file
     * @throws InvalidFileFormatException for any file formatting or content issue
     */
    public CircuitBoard(String filename) throws FileNotFoundException {
        Scanner fileScan = new Scanner(new File(filename));
        String rowsLine = fileScan.nextLine().trim();
        String[] dimensions = rowsLine.split("\\s+");

        if (dimensions.length != 2) {
            throw new InvalidFileFormatException(" Invalid number of dimensions in file " + filename);  //ensure no extra characters in the row with dimensions.
        }
        try {
            ROWS = Integer.parseInt(dimensions[0]);
            COLS = Integer.parseInt(dimensions[1]);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(" Invalid number of dimensions in file " + filename);
        }


        board = new char[ROWS][COLS];
        int row = 0;
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine().trim();
            if (row >= ROWS) {      //check to make sure there aren't extra rows in the file
                throw new InvalidFileFormatException(filename + " has too many rows.");
            }
            String[] columns = line.split("\\s+");
            if (columns.length != COLS) {   //check to make sure each row has the appropriate number of columns
                throw new InvalidFileFormatException(" Invalid number of columns in file " + filename);
            }
            for (int col = 0; col < COLS; col++) {
                char currChar = columns[col].charAt(0);
                if (ALLOWED_CHARS.indexOf(currChar) == -1) {    //check to ensure no invalid characters in the file.
                    throw new InvalidFileFormatException(filename + " has invalid characters.");
                }
                if (currChar == '1') {  //check for a starting point while making sure there aren't multiple.
                    if (startingPoint != null) {
                        throw new InvalidFileFormatException(filename + " has multiple starting points.");
                    }
                    startingPoint = new Point(row, col);
                }
                if (currChar == '2') {  //check for ending point and making sure there aren't multiple.
                    if (endingPoint != null) {
                        throw new InvalidFileFormatException(filename + " has multiple ending points.");
                    }
                    endingPoint = new Point(row, col);
                }
                board[row][col] = currChar;

            }
            row++;
        }
        if (row < ROWS) {       //if we have empty rows, there are too few rows in the file.
            throw new InvalidFileFormatException(filename + " has too few rows.");
        }
        if (startingPoint == null || endingPoint == null) { //have to make sure we have a starting and ending point.
            throw new InvalidFileFormatException(filename + " has invalid starting or ending points.");
        }
        fileScan.close();


    }

    /**
     * Copy constructor - duplicates original board
     *
     * @param original board to copy
     */
    public CircuitBoard(CircuitBoard original) {
        board = original.getBoard();
        startingPoint = new Point(original.startingPoint);
        endingPoint = new Point(original.endingPoint);
        ROWS = original.numRows();
        COLS = original.numCols();
    }

    /**
     * Utility method for copy constructor
     *
     * @return copy of board array
     */
    private char[][] getBoard() {
        char[][] copy = new char[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                copy[row][col] = board[row][col];
            }
        }
        return copy;
    }

    /**
     * Return the char at board position x,y
     *
     * @param row row coordinate
     * @param col col coordinate
     * @return char at row, col
     */
    public char charAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Return whether given board position is open
     *
     * @param row
     * @param col
     * @return true if position at (row, col) is open
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
            return false;
        }
        return board[row][col] == OPEN;
    }

    /**
     * Set given position to be a 'T'
     *
     * @param row
     * @param col
     * @throws OccupiedPositionException if given position is not open
     */
    public void makeTrace(int row, int col) {
        if (isOpen(row, col)) {
            board[row][col] = TRACE;
        } else {
            throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
        }
    }

    /**
     * @return starting Point(row,col)
     */
    public Point getStartingPoint() {
        return new Point(startingPoint);
    }

    /**
     * @return ending Point(row,col)
     */
    public Point getEndingPoint() {
        return new Point(endingPoint);
    }

    /**
     * @return number of rows in this CircuitBoard
     */
    public int numRows() {
        return ROWS;
    }

    /**
     * @return number of columns in this CircuitBoard
     */
    public int numCols() {
        return COLS;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                str.append(board[row][col] + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }

}// class CircuitBoard
