package algorithm;

import cube.CubieCube;

// Node class to represent states along with their parent and cost
public class Node
{
    private final CubieCube state;
    private final Node parent;
    private final int cost;
    private final double heuristic;

    public Node(CubieCube state, Node parent, int cost, double heuristic)
    {
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public double getTotalCost() {
        return this.cost + this.heuristic;
    }

    public CubieCube getCube() {
        return this.state;
    }

    public Node getParent() { return parent; }

    public int getCost() {
        return cost;
    }
}
