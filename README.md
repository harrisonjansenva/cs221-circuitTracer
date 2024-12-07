********************

* CircuitTracer
* CS221-2
* 6 December 2024
* Harrison Jansen van Beek

* ********************

## OVERVIEW:

This program takes in a circuit board (with limitations given by X) and finds the shortest point between the start and
the end by checking every possible path.

## INCLUDED FILES:

* /boards directory - contains sample boards to test functionality
* CircuitBoard.java - file that reads in the circuit boards and gives functionality to create paths and understand where
  the start and end of the path are at.
* CircuitTracer.java - driver class that runs the command line (or to be implemented) GUI functionality.
* CircuitTracerTester.java - built out test suite to ensure proper functionality of other files.
* Storage.java - A special made storage class that allows us to store Trace states in a stack or a queue.
* TraceState.java - a partially completed path between the start and the end. Used to build out paths and also generate
  the next step in the path.
* InvalidFileFormatException.java - custom exception for invalid files that are provided.
* OccupiedPositionException.java - custom exception if the next available path is already occupied, unavailable, or does
  not exist.

## Compiling and Running:

* From the directory with all source files, compile everything needed with:
* $ javac CircuitTracer.java
* If running the tester, you would use <javac CircuitTracerTester.java>.
* The program takes in three command line arguments:

1. <-s> or <-q> : these determine if you will use a stack or a queue based implementation to store the TraceStates.
2. <-c> or <-g> : these determine if you want the output in the command line or as a GUI.
   #### NOTE: GUI functionality not yet implemented.
3. The filename. If using one of the example boards, you must include /boards/ before the name of the example board you
   are using.

### Example:

java CircuitTracer -q -c /boards/valid2.dat

## Program Design and Important Concepts:

CircuitTracer is designed to find and trace the shortest path between a starting point (1) and an ending point (2) on a
circuit board while avoiding blocked positions, marked as X. The implementation uses a choice of either a stack or a
queue to
store search states, providing flexibility for different search strategies. The functionality of the classes is detailed
below:

1. The CircuitBoard class represents the circuit board as a 2D grid of characters. It is responsible for loading the
   board’s layout from a file and validating its format, storing the positions of the start (1) and end (2) points,
   and providing utility methods to check if positions are open (O) or occupied (X), and marking a position as part of
   the trace (T). This class ensures that the board state is consistent and that no invalid moves or traces are made.


2. The TraceState class represents a single path on the circuit board. It maintains the current state of the board and
   the sequence of points that form the path. It creates new states by adding a valid position to the path and checks if
   the path forms a valid solution by being adjacent to the endpoint (2). The use of a point ArrayList enables
   efficient tracking and extension of paths during the search.


3. The Storage class abstracts the underlying data structure (stack or queue) for storing TraceState objects during the
   search. It uses an enumeration to allow the program to dynamically choose between stack (LIFO) or queue (FIFO)
   behavior. This abstraction facilitates different search strategies, such as stack for depth-first search (DFS) or
   queue for breadth-first search (BFS).


4. The CircuitTracer class is the main driver responsible for orchestrating the search process. It parses command-line
   arguments to determine the search strategy, output mode, and input file. It initializes the CircuitBoard and
   populates the Storage with initial TraceState objects. The program iteratively retrieves and expands TraceState
   objects until the shortest path(s) are found, outputting the results in either console or GUI mode. The modular
   design allows easy addition of new output modes or search strategies. We use a brute force method to ensure all paths
   are searched and guarantee the shortest possible path is found.


5. Exception classes such as InvalidFileFormatException and OccupiedPositionException provide robust error handling for
   invalid board configurations or improper moves.

The algorithm begins by identifying all valid moves from the starting point (1) and storing them in the
chosen data structure (Storage). Each TraceState is expanded by attempting moves in four cardinal directions, checking
if the resulting position is open. If a state forms a valid solution, it is added to a list of best paths. Only the
shortest paths are retained by comparing path lengths. The CircuitBoard class ensures that every move is valid by
checking the bounds and status of each position. The makeTrace method prevents overwriting blocked or already-traced
positions, maintaining the integrity of the board.

The program is designed with modularity and extensibility in mind. Separating concerns across multiple classes improves
readability and maintainability. The Storage abstraction allows easy switching between search strategies without
altering the main logic. Reusing CircuitBoard and TraceState objects ensures encapsulation and avoids unintended
modifications to shared state.

## Testing

The majority of testing was completed using the built in test suite. This provided a robust way of ensuring the classes
all function as expected.

To complete some mild debugging, I also inserted breakpoints when loading in best paths and new circuit boards in
CircuitTracer to ensure they were functioning properly.

## Analysis

The choice between a stack and a queue, in our functionality, offers little practical difference to the end user: they
both will result in searching for every valid path from 1 to 2. However, if this program were tweaked slightly, it would
make a great difference in how things were implemented.

For example, if our goal was to find the shortest route first and not exhaustively explore every route, we would want to
use a queue-based implementation every time. While it can occupy more spots in memory due to the sheer breadth of the
search, it also ensures we make the fewest possible steps before finding the shortest route.

However, if our goal was to find any solution the fastest (and not worry about it being the shortest route), a
stack-based implementation will usually find a result the quickest. A stack will go as far down one path as it can to
find a solution.

I like to think of the two algorithms as different types of root systems on plants: a stack as a carrot and a queue as a
sequoia tree.

A carrot grows straight down in one direction as far as it can, much like the “Last In, First Out” model of a stack
ensures that you go as far down one valid path as possible until you either find a solution or hit a dead end.

The sequoias, however, spread their roots shallow and wide, ensuring they cover as much ground laterally as possible
before going deeper in search of nutrients. Similarly, a queue ensures that we check all paths layer by layer,
exhaustively exploring each before moving to the next depth.

A queue will usually find the shortest solution the quickest because it doesn’t “get stuck” exploring one long path.
That said, there are exceptions. For example, if a stack happens to process the shortest route first, it will find it
faster and use less memory than a queue. But that depends entirely on luck or the structure of the board.

A queue generally holds more states in memory because it processes all paths at a given depth before moving on. But this
trade-off guarantees the shortest solution will always be found first.

Our O(n) seems a little drastic in this problem: it's O(4^n) as each move can exponentially increase in 4 directions
based off of n number of open positions on the board. In our case, with the number of blocked-off sections and size
constraints, it usually evaluates closer to n^2.

## Discussion

The hardest part of this project, for me, was ensuring the file was read in correctly. The pseudo-code made the search
algorithm easy to implement. When we wrote our input file code for warmup, I was really rusty on coding, so I didn't
like how I wrote that project at all. I ended up starting from scratch, and while not perfect, is significantly better
than what I had written before. Overall, this was a great capstone to the year and I enjoyed learning more about the
implementations of stacks vs queues for searching!

