public class Percolation {
	private int N;
	private int[] state;
	private WeightedQuickUnionUF puppy;
	private WeightedQuickUnionUF puppyAgain;

    public  Percolation(int N) {
    	if (N <= 0)
    		throw new IllegalArgumentException();
    	this.N = N;
    	puppy = new WeightedQuickUnionUF(N*N + 2);
    	puppyAgain = new WeightedQuickUnionUF(N*N + 1);
    	state = new int[N*N + 2];
    	for (int i = 0; i < N*N; i++)
    		state[i] = 0;
    	state[N*N] = 1;
    	state[N*N + 1] = 1;
    }
    
    private int index(int i, int j) {
    	return N * (i-1) + j - 1;
    }
    
    public void open(int i, int j) {
    	if (i <= 0 || j <= 0 || i > N || j > N)
    		throw new IndexOutOfBoundsException();  
    	if (isOpen(i, j))
    		return;
    	int c = index(i, j);
    	state[c] = 1;
    	if ((i != 1) && (isOpen(i-1, j))) { //top
    		puppy.union(index(i-1, j), c);
    		puppyAgain.union(index(i-1, j), c);
    	}
    	else if (i == 1) {
    		puppy.union(c, N*N);
    		puppyAgain.union(c, N*N);
    	}
    	if ((i != N) && (isOpen(i+1, j))) { //bottom
    		puppy.union(index(i+1, j), c);
    		puppyAgain.union(index(i+1, j), c);
    	}
    	else if (i == N)
    		puppy.union(c, N*N + 1);
    	if ((j != 1) && (isOpen(i, j-1))) { //left
    		puppy.union(index(i, j-1), c);
    		puppyAgain.union(index(i, j-1), c);
    	}
    	if ((j != N) && (isOpen(i, j+1))) { //right
    		puppy.union(index(i, j+1), c);
    		puppyAgain.union(index(i, j+1), c);
    	}
    	  
    }
    
    public boolean isOpen(int i, int j) { // is site (row i, column j) open?
    	if (i <= 0 || j <= 0 || i > N || j > N) 
    		throw new IndexOutOfBoundsException();   		
    	return state[index(i, j)] == 1;  	
    }
    
    public boolean isFull(int i, int j) {     // is site (row i, column j) full?
    	if (i <= 0 || j <= 0 || i > N || j > N)
    		throw new IndexOutOfBoundsException(); 
    	return (puppy.connected(index(i, j), N*N)) && (puppyAgain.connected(index(i, j), N*N));
    }
    
    public boolean percolates() { // does the system percolate?
    	return puppy.connected(N*N, N*N + 1);    	
    }    
}

