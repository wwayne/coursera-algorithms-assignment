import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int gridSize;
    private WeightedQuickUnionUF siteList;
    private WeightedQuickUnionUF siteListWithouBottom;
    private int virtualTopSite;
    private int virtualBottomSite;
    private int dimension;
    private int openedSitesAmount;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should greater than 0");
        }
        grid = new boolean[n][n];
        gridSize = n * n;
        siteList = new WeightedQuickUnionUF(n * n + 2);
        siteListWithouBottom = new WeightedQuickUnionUF(n * n + 1);
        virtualTopSite = 0;
        virtualBottomSite = n * n + 1;    
        dimension = n;
        openedSitesAmount = 0;
    }     
    
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int index = convert2DToIndex(row, col);
            if (index >= 1 && index <= dimension) {
                siteList.union(index, virtualTopSite);
                siteListWithouBottom.union(index, virtualTopSite);
            }
            if (index > dimension * (dimension - 1) &&
                     index <= dimension * dimension) {
                siteList.union(index, virtualBottomSite);
            }
            
            connectAroundIfOpen(index, row + 1, col);
            connectAroundIfOpen(index, row - 1, col);
            connectAroundIfOpen(index, row, col + 1);
            connectAroundIfOpen(index, row, col - 1);
            
            grid[row - 1][col - 1] = true;
            openedSitesAmount++;
        }
    }
    
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (isOutOfIndex(row, col)) {
            throw new IllegalArgumentException("Out of indices");
        }
        return grid[row - 1][col - 1];
    }
    
    // is an open site that can be connected to an open site in the top row
    public boolean isFull(int row, int col)  {
        if (!isOpen(row, col)) {
            return false;
        }
        int index = convert2DToIndex(row, col);
        return siteListWithouBottom.connected(index, virtualTopSite);
    }
    // number of open sites
    public int numberOfOpenSites() {
        return openedSitesAmount;
    }
    
    // does the system percolate?
    public boolean percolates() {
        return siteList.connected(virtualTopSite, virtualBottomSite);
    }
    
    private boolean isOutOfIndex(int row, int col) {
        return row < 1 || row > dimension || col < 1 || col > dimension;
    }
    
    private int convert2DToIndex(int row, int col) {
        if (isOutOfIndex(row, col)) {
            throw new IllegalArgumentException("Out of indices");
        } 
        
        return (row - 1) * dimension + col;
    }
    
    private void connectAroundIfOpen(int originIndex, int row, int col) {
        if (!isOutOfIndex(row, col) && isOpen(row, col)) {
            int aroundIndex = convert2DToIndex(row, col);
            siteList.union(originIndex, aroundIndex);
            siteListWithouBottom.union(originIndex, aroundIndex);
        }
    }
    
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0], 10);
        Percolation percolation = new Percolation(n);
        percolation.open(-1, 5);
        System.out.println(percolation.percolates());
    }
}
