import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Board {
    private int[][] blocks;
    private int dimension;
    private Board[] neighbors;
    
    public Board(int[][] blocks) {
      this.blocks = copyBlock(blocks);
      this.dimension = blocks[0].length;
    }
    
    private int[][] copyBlock(int[][] blocks) {
      int[][] copy = new int[blocks.length][];
      for (int i = 0; i < blocks.length; i++) {
        copy[i] = blocks[i].clone();
      }
      return copy;
    }
    
    public int dimension() {
      return dimension;
    }
    
    // number of blocks out of place
    public int hamming() {
      int count = 0;
      for (int row = 0; row < dimension; row++) {
        for (int column = 0; column < dimension; column++) {
          int item = blocks[row][column];
          if (item == 0) continue;
          if (row * dimension + column + 1 != item) count++;
        }
      }
      return count;
    }
    
    public int manhattan() {
      int count = 0;
      for (int row = 0; row < dimension; row++) {
        for (int column = 0; column < dimension; column++) {
          int item = blocks[row][column];
          if (item == 0) continue;
          int diffRow = Math.abs((item - 1) / dimension - row);
          int diffColumn = Math.abs((item - 1) % dimension - column);
          count += diffRow + diffColumn;
        }
      }
      return count;
    }
    
    public boolean isGoal() {
      return hamming() == 0;
    }

    public Board twin() {
      int[][] twinBlock = copyBlock(blocks);
      int row = 0, column = 0;
      while (twinBlock[row][column] == 0 || twinBlock[row][column + 1] == 0) {
        column++;
        if (column == dimension - 1) {
          row++;
          column = 0;
        }
      }
      exch(twinBlock, row, column, row, column + 1);
      return new Board(twinBlock);
    }
    
    private void exch(int[][] block, int oldR, int oldC, int newR, int newC) {
      int oldItem = block[oldR][oldC];
      block[oldR][oldC] = block[newR][newC];
      block[newR][newC] = oldItem;
    }
    
    public boolean equals(Object y) {
      if (y == this) return true;
      if (y == null) return false;
      if (y.getClass() != this.getClass()) return false;
      Board that = (Board) y;
      if (this.dimension != that.dimension) return false;
      for (int r = 0; r < dimension; r++) {
        for (int c = 0; c < dimension; c++) {
          if (this.blocks[r][c] != that.blocks[r][c]) return false;
        }
      }
      return true;
    }
      
    public Iterable<Board> neighbors() {
      return new Iterable<Board>() {
        public Iterator<Board> iterator() {
          if (neighbors == null) {
            findNeighbors();
          }
          return new NeighborIterator();
        }
      };
    }
    
    private void findNeighbors() {
      int row = 0, column = 0;
      while(blocks[row][column] != 0) {
        if (column + 1 == dimension) {
          row++;
          column = 0;
        } else {
          column++;
        }
      }
      
      List<Board> myNeighbors = new ArrayList<>();
      if (row != 0) {
        int[][] copy = copyBlock(blocks);
        exch(copy, row, column, row - 1, column);
        myNeighbors.add(new Board(copy));
      }
      if (row != dimension - 1) {
        int[][] copy = copyBlock(blocks);
        exch(copy, row, column, row + 1, column);
        myNeighbors.add(new Board(copy));
      }
      if (column != 0) {
        int[][] copy = copyBlock(blocks);
        exch(copy, row, column, row, column - 1);
        myNeighbors.add(new Board(copy));
      }
      if (column != dimension - 1) {
        int[][] copy = copyBlock(blocks);
        exch(copy, row, column, row, column + 1);
        myNeighbors.add(new Board(copy));
      }
      
      int neighboarAmount = myNeighbors.size();
      neighbors = myNeighbors.toArray(new Board[neighboarAmount]);
    }
    
    private class NeighborIterator implements Iterator<Board> {
      private int index = 0;
      
      public boolean hasNext() {
        return index != neighbors.length;
      }
      
      public Board next() {
        if (!hasNext()) throw new NoSuchElementException();
        return neighbors[index++];
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    }
      
    public String toString() {
      StringBuilder s = new StringBuilder();
      s.append(dimension + "\n");
      for (int i = 0; i < dimension; i++) {
        for (int j = 0; j < dimension; j++) {
          s.append(String.format("%2d ", blocks[i][j]));
        }
        s.append("\n");
      }
      return s.toString();
    }

    public static void main(String[] args) {
//      int[][] blocks = {{8,1,3}, {4,0,2}, {7,6,5}};
//      int[][] blocks = {{4,1,3}, {0,2,5}, {7,8,6}};
      int[][] blocks = {{1,0,2}, {7,5,4}, {8,6,3}};
      Board myBoard = new Board(blocks);
      System.out.println("dimension: " + myBoard.dimension);
      System.out.println("hamming: " + myBoard.hamming());
      System.out.println("manhattan: " + myBoard.manhattan());
      System.out.println("isGoal: " + myBoard.isGoal());
      System.out.println("twin: " + myBoard.twin());
      for (Board neighbor: myBoard.neighbors()) {
        System.out.println(neighbor.toString());
      }
    }
}