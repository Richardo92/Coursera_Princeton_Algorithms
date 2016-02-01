public class PercolationStats {
	private Percolation puppy;
	private double[] probability;
	public PercolationStats(int N, int T) {
	probability = new double[T];
	int row;
	int column;
	int count = 0;
	for (int i = 0; i < T; i++){
		puppy = new Percolation(N);
		do {
			row = StdRandom.uniform(1, N+1);
			column = StdRandom.uniform(1, N+1);
			if (puppy.isOpen(row, column))
				continue;
			puppy.open(row, column);
			count++;
		} while(!puppy.percolates());
		probability[i] = (double) count / (N*N);
		count = 0;
	}		
	}	
	public double mean() {
		return StdStats.mean(probability);
	}
	public double stddev() {
		return StdStats.stddev(probability);
	}
	private double halfInterval() {
	    return 1.96 * stddev() / Math.sqrt(probability.length);
	}
	public double confidenceLo() {
		 return mean() - halfInterval();
	}
	public double confidenceHi() {
        return mean() + halfInterval();
    }
	public static void main(String[] args) {
		PercolationStats pls = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		StdOut.printf("mean                     = %f\n", pls.mean());
		StdOut.printf("stddev                   = %f\n", pls.stddev());
		StdOut.printf("95%% confidence Interval  = %f, %f\n", pls.confidenceLo(), pls.confidenceHi());
	}

}
