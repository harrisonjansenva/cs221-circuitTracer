import java.io.FileNotFoundException;

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
        //TODO: print out clear usage instructions when there are problems with
        // any command line args
        System.out.println("To USE: this program takes three arguments: the storage method, console or GUI output, and the file name.");
        System.out.println("first arg: -s for stack or -q for queue.");
        System.out.println("second arg: -c for console or -g for GUI output.");
        System.out.println("third arg: the full filename.");
        System.out.println("EXAMPLE: java CircuitTracer -q -c valid2.dat");
    }

    /**
     * Set up the CircuitBoard and all other components based on command
     * line arguments.
     *
     * @param args command line arguments passed through from main()
     */
    public CircuitTracer(String[] args) {
        //TODO: parse and validate command line args - first validation provided
        if (args.length != 3) {
            printUsage();
            return; //exit the constructor immediately
        }
        Storage<TraceState> stateStore;
        switch (args[0]) {
            case "-q":
                stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
                break;
            case "-s":
                stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
                break;
            default:
                printUsage();

        }
       try {
           CircuitBoard board = new CircuitBoard(args[2]);
       }
       catch (FileNotFoundException e) {
           System.err.println( "Invalid filename Provided! try again." + e.getMessage());
       }
        switch (args[1]) {
            case "-c":



                break;
            case "-g":
                throw new UnsupportedOperationException("GUI mode not yet supported.");
            default:
                printUsage();
        }
        //TODO: initialize the Storage to use either a stack or queue
        //TODO: read in the CircuitBoard from the given file
        //TODO: run the search for best paths
        //TODO: output results to console or GUI, according to specified choice
    }
    public void generateNextMoves() {

    }

} // class CircuitTracer
