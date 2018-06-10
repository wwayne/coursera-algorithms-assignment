import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;
    
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Should greater than 0");
        }
        
        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }
            thresholds[i] = percolation.numberOfOpenSites() / (double) (n * n);
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        
        double confidence = 1.96 * stddev / Math.sqrt(trials);
        confidenceLo = mean - confidence;
        confidenceHi = mean + confidence;
    }
    
    public double mean() {
        return mean;
    }
    
    public double stddev() {
        return stddev;
    }
    
    public double confidenceLo() {
        return confidenceLo;
    }
    
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0], 10);
        int trials = Integer.parseInt(args[1], 10);

        PercolationStats percolationState = new PercolationStats(n, trials);
        System.out.println("mean = " + percolationState.mean());
        System.out.println("stddev = " + percolationState.stddev());
        double cLo = percolationState.confidenceLo();
        double cHi = percolationState.confidenceHi();
        System.out.println("95% confidence interval = " + cLo + ", " + cHi);
    }
}
