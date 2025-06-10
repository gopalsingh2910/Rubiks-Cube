
     
                   | U0     U1 |
                   | U2     U3 |
          ---------------------------------------
           L16 L17 | F8     F9 | R4 R5 | B20 B21
           L18 L19 | F10   F11 | R6 R7 | B22 B23
          ---------------------------------------
                   | D12   D13 |
                   | D14   D15 |
                   
           U(0), R(1), F(2), D(3), L(4), B(5)

# Rubik's Cube 2x2x2 Solver
This is 2X2 java based Rubik Cube implementation integreted with graphical user interface.

## *Abstract*
The 2x2x2 Rubik's Cube (also known as the Pocket Cube or Mini Cube) is composed of just 8 corner pieces. Even though it is a simplified version of the regular Rubik's Cube, the Pocket Cube has 3674160 possible configurations. Being the total number of states finite, any search algorithm that is both complete and optimal can give us the shortest sequence of moves to solve any scrambled cube (e.g. BFS, Dijkstra, A*). It has been proven that the maximum number of moves required to solve any Pocket Cube is 11, but currently there is no known procedure to know exactly what these 11 moves are without searching in the states space or listing every possible state in a table with their distance from the solve state in terms of moves.

## *Algorithms*
The algorithm implemented is A* search algorithm which is an informed search strategy that utilizes prior information to compute an heuristic function able to estimate the number of moves to reach the goal state. This strategy is implemented in a graph-search fashion, meaning that reapeated states are eliminated.s in the search for a solution using for example A*.

## *Heuristics*
As previously mentioned, the heuristic value is the estimatation of the minimum number of steps required to get from the current state to the goal state, the closer to the real value the better the performace. But, in case of graph-search, two main properties has to be respected:

 - Admissability: the estimation must never exceed the real value h(n) <= h*(n);
 - Consistency: the estimation of a parent state must never exceed the estimation of a child state plus the cost of the arc between them h(n) <= h(n') + c(n, n') - (Note that consistency implies admissability);
How do we choose a consistent heuristic function? A choice could be h(n) = 0 which is trivially consistent, but it wouldn't be of any help as the A* search would be reduced to the Dijkstra algorithm and no prior knowledge could be exploited to speed up the search.

Three heuristics were taken into account:

 - Sum of number of misplaced colors in all 6 faces divided by 4 to guarantee consistency (as a single move can only move 4 pieces);
heuristic: c(n) = (number of faces with more than one color /3) * ((sum of: (# of different colors on each face -1))/4)
 - 3D Manhattan distance, also known as taxicab matrix, is the sum of moves to correctly position all 8 pieces indipendently, and then divided by 4 (for the same reason as above);
 - Pattern database, which is a lookup table that stores every cube state with the real distance from solved state (precomputed using BFS);

## *Visualization*
The Cube is viewed from front on 2D frame. In this cube representation only one side can be viewed at one time. To view other sides the cube can be rotated via arrow keys.
After cube is solved, list of moves is generated which is displayed on left side panel and clicking on particular move brings cube to corresponding state.was efficient enough to be used to solve most scrambled cubes, it was computationally demanding for longer sequences of scrambling as time complexity increases exponentially with the depth of the solution. Pattern database was the best heuristic as, of course, it gives the true estimate value, but for this python implementation required several hours for the completion of the all the database's entries.

## *Usage information*
One must have knowledge of few standard libraries of java which helps in graphics visualization - 
 - java.swing
 - java.awt
 - java.awt.event

In addition to this, one must know OOPS working principles to understand the code written.

## *Running*
The main method is present in src/gui/JCube.java
The excution begin from there. Just compile and run the code from this main method.

## *Snippets*

![scrambled](https://github.com/gopalsingh2910/Rubiks-Cube/blob/main/snippets/Screenshot1.png)           scrambled cube state
![moves generated](https://github.com/gopalsingh2910/Rubiks-Cube/blob/main/snippets/Screenshot2.png)          moves are generated
![solved cube](https://github.com/gopalsingh2910/Rubiks-Cube/blob/main/snippets/Screenshot3.png)          solved cube
