import java.awt.*;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 *
 * @author mvail
 */
public class CircuitTracer {

    /**
     * Launch the program.
     *
     * @param args three required arguments:
     *             first arg: -s for stack or -q for queue
     *             second arg: -c for console output or -g for GUI output
     *             third arg: input file name
     */
    public static void main(String[] args) {
        new CircuitTracer(args); //create this with args
    }

    /**
     * Print instructions for running CircuitTracer from the command line.
     */
    private void printUsage() {
        System.out.println("To USE: this program takes three arguments: the storage method, console or GUI output, and the file name.");
        System.out.println("first arg: -s for stack or -q for queue.");
        System.out.println("second arg: -c for console or -g for GUI output.");
        System.out.println("third arg: the full filename. \n ***** IF USING THE FILES IN THE BOARDS FOLDER, INCLUDE THE PATH IN THE FILENAME." );
        System.out.println("EXAMPLE: java CircuitTracer -q -c ./boards/valid2.dat");
    }

    /**
     * Set up the CircuitBoard and all other components based on command
     * line arguments.
     *
     * @param args command line arguments passed through from main()
     */
    public CircuitTracer(String[] args) {
        if (args.length != 3) {
            printUsage();
            return; //exit the constructor immediately
        }
        Storage<TraceState> stateStore;
        ArrayList<TraceState> bestPaths = new ArrayList<TraceState>();

        switch (args[0]) {
            case "-q":
                stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
                break;
            case "-s":
                stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
                break;
            default:
                printUsage();
                return;

        }
        try {
            CircuitBoard board = new CircuitBoard(args[2]);
            Point startingPoint = board.getStartingPoint();
            for (int i = -1; i <= 1; i++) {
                if (board.isOpen(startingPoint.x + i, startingPoint.y)) {
                    TraceState newState = new TraceState(board, startingPoint.x + i, startingPoint.y);
                    stateStore.store(newState);
                }
                if (board.isOpen(startingPoint.x, startingPoint.y + i)) {
                    TraceState newState2 = new TraceState(board, startingPoint.x, startingPoint.y + i);
                    stateStore.store(newState2);
                }
            }
            while (!stateStore.isEmpty()) {
                TraceState currentEvaluated = stateStore.retrieve();

                if (currentEvaluated.isSolution()) {
                    if (bestPaths.isEmpty() || currentEvaluated.pathLength() == bestPaths.getFirst().pathLength()) {
                        bestPaths.add(currentEvaluated);
                    } else if (currentEvaluated.pathLength() < bestPaths.getFirst().pathLength()) {
                        bestPaths.clear();
                        bestPaths.add(currentEvaluated);
                    }
                } else {

                    for (int i = -1; i < 2; i++) {
                        if (i == 0) continue;
                        int newX = currentEvaluated.getRow() + i;
                        int newY = currentEvaluated.getCol() + i;
                        if (currentEvaluated.isOpen(newX, currentEvaluated.getCol())) {
                                TraceState newState = new TraceState(currentEvaluated, newX, currentEvaluated.getCol());
                                stateStore.store(newState);
                        }
                        if (currentEvaluated.isOpen(currentEvaluated.getRow(), newY)) {
                            TraceState newState2 = new TraceState(currentEvaluated, currentEvaluated.getRow(), newY);
                            stateStore.store(newState2);
                        }
                    }

                }
            }
        } catch (Exception e) {
            System.err.println(" An error Occurred! Try again. " +  e + e.getMessage());

        }
        switch (args[1]) {
            case "-c":
//                System.out.println("There are " + bestPaths.size() + " solutions.");
//                System.out.println("Shortest Path: " + bestPaths.getFirst().pathLength());
                for (TraceState states : bestPaths) {
                    System.out.println(states.toString());
                }


                break;
            case "-g":
                throw new UnsupportedOperationException("GUI mode not yet supported.");
            default:
                printUsage();
                return;
        }

    }

} // class CircuitTracer
