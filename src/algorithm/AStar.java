package algorithm;

import cube.CubieCube;
import cube.Face;
import cube.Move;

import java.util.*;

public class AStar
{
    final CubieCube cube;

    public final static int[] PRUNING_TABLE_PERMUTATION = new int[40320];   // 8! possible corner permutations
    public final static int[] PRUNING_TABLE_ORIENTATION = new int[2187];   // 3^7 possible corner orientations

    public AStar(CubieCube cube)
    {
        this.cube = cube;
    }

    // Helper method to reconstruct the path from the goal to the start
    public List<String> findPath()
    {
        List<String> path = new ArrayList<>();
        Node current = this.applyAStar();

        while (current != null)
        {
            if(current.getParent() != null) path.add(current.getCube().toString() +
                    CubieCube.findMove(current.getParent().getCube().getState(), current.getCube().getState()));
            current = current.getParent();
        }
        Collections.reverse(path);  // Reverse the path to get it from start to goal

        return path;    // Return the final path
    }

    public Node applyAStar()
    {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Node::getTotalCost));
        HashSet<String> visited = new HashSet<>();

        Node startNode = new Node(this.cube, null, 0, heuristic(cube.getState()));
        priorityQueue.offer(startNode);

        // run the search
        while (!priorityQueue.isEmpty())
        {
            Node currentNode = priorityQueue.poll();
            Face[] currentState = currentNode.getCube().getState();

            /*
             * Check if the current state is the goal state
             * Reconstruct the path from the goal to the start
             */
            if (CubieCube.isGoal(currentState)) return currentNode;
            if (!visited.contains(currentNode.getCube().toString())) /* Check if the state has been visited */
            {
                visited.add(currentNode.getCube().toString());

                // Generate successor states
                for (Face[] successor : successor(currentState))
                {
                    int cost = currentNode.getCost() + 1; // Assuming all movement costs are equal

                    Node successorNode = new Node(new CubieCube(successor), currentNode, cost, heuristic(successor));
                    priorityQueue.offer(successorNode);
                }
            }
        }

        return null;    // Goal state not found
    }

    /**
     * calculates heuristic valuation of the state sent to it as a parameter
     * based on the following equation:
     * (number of faces with more than one color /3) * ((sum of: (# of different colors on each face -1))/4)
     *
     * @return double valuation of the state sent as an input
     */
    public static double heuristic(Face[] faces)
    {
        int index = CubieCube.cubeIndex(CubieCube.getPermutationOrientation(faces));

        int countFaces = 0;
        int countGlobalColor = 0;
        double result;

        for (int i = 0; i < 24; i += 4)
        {
            int countLocalColor = 0;
            ArrayList<Face> colors = new ArrayList<>();
            for (int j = 0; j < 4; j++)
            {
                if (!colors.contains(faces[i + j]))
                {
                    countLocalColor++;
                    colors.add(faces[i + j]);
                }
            }

            colors.clear();
            countGlobalColor += (countLocalColor - 1);
            if (countLocalColor > 1) countFaces++;
        }
        result = (countFaces * countGlobalColor) / 12.0;

        return result + Math.max(PRUNING_TABLE_PERMUTATION[index/2187], PRUNING_TABLE_ORIENTATION[index%2187]);
    }

    /**
     * the function applies each of the moves in pMoves and applies them separately to the current state
     * @param cubeState: representation of the cube state
     * @return a 12*24 matrix of the 12 possible configurations of children states of the current state
     */
    public static Face[][] successor(Face[] cubeState)
    {
        Face[][] successors = new Face[12][24];

        for (int i = 0; i < CubieCube.ALL_MOVES.length; i++)
        {
            Face[] newState = CubieCube.applyMove(cubeState, CubieCube.ALL_MOVES[i]);
            assert newState != null;
            System.arraycopy(newState, 0, successors[i], 0, newState.length);
        }

        return successors;
    }

    /**
     *
     *  computes pruning tables in advance for 8! possible permutations and 3^7 orientation
     *  by running BFS
     *  last cubie has just one possible orientation depending on other 7 cubie
     *
     */

    public static void computePruningTables()
    {
        Face[] face = new Face[24];

        for(Face j:CubieCube.ALL_FACES)
        {
            for(int k = 0; k < 4; k++) face[4 * j.index() + k] = j;
        }

        for(int i = 0; i < 40320; i++) PRUNING_TABLE_PERMUTATION[i] = -1;
        for(int i = 0; i < 2187; i++) PRUNING_TABLE_ORIENTATION[i] = -1;

        Arrays.fill(PRUNING_TABLE_PERMUTATION, -1);
        Arrays.fill(PRUNING_TABLE_ORIENTATION, -1);

        PRUNING_TABLE_PERMUTATION[0] = 0;
        PRUNING_TABLE_ORIENTATION[0] = 0;

        Queue<String> queue = new LinkedList<>();
        queue.offer(new CubieCube(face).toString());
        int permutations = 40320, orientations = 2187;

        while((permutations > 0 || orientations > 0) && !queue.isEmpty())
        {
            Face[] u = CubieCube.toCube(queue.poll()).getState();

            for(Move move:CubieCube.ALL_MOVES)
            {
                Face[] v = CubieCube.applyMove(u, move);
                int uIndex = CubieCube.cubeIndex(CubieCube.getPermutationOrientation(u));
                int vIndex = CubieCube.cubeIndex(CubieCube.getPermutationOrientation(v));

                if(PRUNING_TABLE_PERMUTATION[vIndex/2187] != -1 && PRUNING_TABLE_ORIENTATION[vIndex%2187] != -1) continue;
                if(PRUNING_TABLE_PERMUTATION[vIndex/2187] == -1)
                {
                    PRUNING_TABLE_PERMUTATION[vIndex/2187] = PRUNING_TABLE_PERMUTATION[uIndex/2187] + 1;
                    permutations--;
                }

                if(PRUNING_TABLE_ORIENTATION[vIndex%2187] == -1)
                {
                    PRUNING_TABLE_ORIENTATION[vIndex%2187] = PRUNING_TABLE_ORIENTATION[uIndex%2187] + 1;
                    orientations--;
                }

                queue.offer(new CubieCube(v).toString());
            }
        }
    }
}
