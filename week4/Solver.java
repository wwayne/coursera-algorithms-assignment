import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Solver {
  private Node goalNode;
  private boolean isSolved;
  
  public Solver(Board initial) {
    if (initial == null) throw new IllegalArgumentException();
    
    MinPQ<Node> nodePQ = new MinPQ<>(new NodeComparator());
    MinPQ<Node> nodePQTwin = new MinPQ<>(new NodeComparator());
    nodePQ.insert(new Node(initial, null, 0));
    nodePQTwin.insert(new Node(initial.twin(), null, 0));

    Node pNode = nodePQ.delMin();
    Node pNodeTwin = nodePQTwin.delMin();
    while(!pNode.isGoal() && !pNodeTwin.isGoal()) {
      for (Board neighbor: pNode.board.neighbors()) {
        if (isNeighborAlreadyExist(pNode, neighbor)) continue;
        nodePQ.insert(new Node(neighbor, pNode, pNode.moves + 1));
      }

      for (Board neighbor: pNodeTwin.board.neighbors()) {
        if (isNeighborAlreadyExist(pNodeTwin, neighbor)) continue;
        nodePQTwin.insert(new Node(neighbor, pNodeTwin, pNodeTwin.moves + 1));
      }
      
      pNode = nodePQ.delMin();
      pNodeTwin = nodePQTwin.delMin();
    }

    this.isSolved = pNode.isGoal();
    this.goalNode = pNode;
  }
  
  private boolean isNeighborAlreadyExist(Node node, Board neighbor) {
    Node currentNode = node;
    while (currentNode.previousNode != null) {
      currentNode = currentNode.previousNode;
      if (currentNode.board.equals(neighbor)) return true;
    }
    return false;
  }
  
  private static class Node {
    private Board board;
    private Node previousNode;
    private int moves;
    
    public Node(Board board, Node previousNode, int moves) {
      this.board = board;
      this.previousNode = previousNode;
      this.moves = moves;
    }
    
    public int priority() {
      return board.manhattan() + moves;
    }
    
    public boolean isGoal() {
      return board.isGoal();
    }
  }
  
  private static class NodeComparator implements Comparator<Node> {
    public int compare(Node node1, Node node2) {
      return node1.priority() - node2.priority();
    }
  }
  
  public boolean isSolvable() {
    return isSolved;
  }
  
  public int moves() {
    if (!isSolved) return -1;
    return goalNode.moves;
  }
  
  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (!isSolved) return null;
    return new Iterable<Board>() {
      public Iterator<Board> iterator() {
        return new SolutionIterator();
      }
    };
  }
  
  private class SolutionIterator implements Iterator<Board> {
    private int index = 0;
    private Node[] solution;
    
    public SolutionIterator() {
      List<Node> mySolution = new ArrayList<>();
      Node currentNode = goalNode;
      while(currentNode != null) {
        mySolution.add(0, currentNode);
        currentNode = currentNode.previousNode;
      }
      this.solution = mySolution.toArray(new Node[mySolution.size()]);
    }
    
    public boolean hasNext() {
      return index != this.solution.length;
    }
      
    public Board next() {
      if (!hasNext()) throw new NoSuchElementException();
      return solution[index++].board;
    }
      
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
  }
}