import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.util.Arrays;

public class SAP {
  private Digraph G;
  
  public SAP(Digraph g) {
    if (g == null) throw new IllegalArgumentException();
    G = new Digraph(g);
  }

  public int length(int v, int w) {
    if (v == w) return 0;
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    int shortestLength = -1;
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
        int distance = bfsV.distTo(vertex) + bfsW.distTo(vertex);
        if (shortestLength == -1 || distance < shortestLength) {
          shortestLength = distance;
        }
      }
    }
    return shortestLength;
  }

  public int ancestor(int v, int w) {
    if (v == w) return v;
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    int shortestLength = -1;
    int ancestor = -1;
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
        int distance = bfsV.distTo(vertex) + bfsW.distTo(vertex);
        if (shortestLength == -1 || distance < shortestLength) {
          shortestLength = distance;
          ancestor = vertex;
        }
      }
    }
    return ancestor;
  }

  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) throw new IllegalArgumentException();
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    int shortestDistance = -1;
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
        int distance = bfsV.distTo(vertex) + bfsW.distTo(vertex);
        if (shortestDistance == -1 || distance < shortestDistance) {
          shortestDistance = distance;
        }
      }
    }
    return shortestDistance;
  }

  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) throw new IllegalArgumentException();
    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
    
    int shortestDistance = -1;
    int shortestAncestor = -1;
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
        int distance = bfsV.distTo(vertex) + bfsW.distTo(vertex);
        if (shortestDistance == -1 || distance < shortestDistance) {
          shortestDistance = distance;
          shortestAncestor = vertex;
        }
      }
    }
    return shortestAncestor;
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}