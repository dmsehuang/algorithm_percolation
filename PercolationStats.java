import java.util.Random;
import java.util.ArrayList;

public class PercolationStats {
    private double[] fractions;
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }
        fractions = new double[T];
        for (int k = 0; k < T; k++){
            Percolation percolation = new Percolation(N);
            ArrayList<Integer> blocked = new ArrayList<Integer>();
            for (int l = 1; l <= N*N; l++) {
                blocked.add(l);
            }
            // Monte Carlo Simulation
            Random ran = new Random();
            int count = 0;
            while (!percolation.percolates()){
                int index = ran.nextInt(blocked.size());
                int num = blocked.get(index);
                blocked.remove(index);
                int j = (num % N) == 0 ? N:(num % N); // if j==0, j=8
                int i = (num-j)/N+1;
                percolation.open(i, j);
                count++;
            }
            fractions[k] = (double) count/(double) (N*N);
        }
    }
    // sample mean of percolation threshold
    public double mean() {
        double sum = 0.0;
        for (int i = 0; i < fractions.length; i++) {
            sum+=fractions[i];
        }
        return sum/(double) fractions.length;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        double sum = 0.0;
        double meanVal = mean();
        for (int i = 0; i < fractions.length; i++){
            double xSquare = (fractions[i]-meanVal)*(fractions[i]-meanVal);
            sum += xSquare;
        }
        double deltaSquare = sum/(double) (fractions.length-1);
        return Math.sqrt(deltaSquare);
    }
    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        double meanVal = mean();
        double dev = stddev();
        double T = (double) fractions.length;
        return meanVal-1.96*dev/(Math.sqrt(T));
    }
    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        double meanVal = mean();
        double dev = stddev();
        double T = (double) fractions.length;
        return meanVal+1.96*dev/(Math.sqrt(T));
    }
    //test client,described below
    public static void main(String[]args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        System.out.println("N :" + N + " T:" + T);
        PercolationStats stats = new PercolationStats(N,T);
        System.out.println("mean\t\t\t= " + stats.mean());
        System.out.println("stddev\t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval\t= " + 
        stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
