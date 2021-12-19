import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int _trials;
    private double[] _results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <=0)
            throw new IllegalArgumentException("invalid n and trials value");
        _trials = trials;
        _results = new double[trials];
        //
        // run monte carlo T times
        for(int iter = 0; iter < trials; iter++) {
            Percolation curPercolation = new Percolation(n);
            // repeat until percolates
            while (!curPercolation.percolates()) {
                // random is inclusive of start but exclusive of end
                curPercolation.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
            }
            // add to results
            _results[iter] = (double)curPercolation.numberOfOpenSites()/ (double) (n*n);
            // curPercolation.printGrid();
            // StdOut.println("frac: " + _results[iter]);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(_results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(_results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96*stddev())/(Math.sqrt((double) _trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96*stddev())/(Math.sqrt((double) _trials));
    }

   // test client (see below)
   public static void main(String[] args) {
        if (args.length == 2) {
            PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            StdOut.printf("mean\t\t\t= %f\n", percStats.mean());
            StdOut.printf("stddev\t\t\t= %f\n", percStats.stddev());
            StdOut.printf("95%% confidence interval\t= [%f, %f]\n", percStats.confidenceLo(), percStats.confidenceHi());
        }
   }
}
