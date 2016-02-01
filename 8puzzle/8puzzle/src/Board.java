public class Board {
	private int[][] board;
	private int N;
	private int zRow;
	private int zCol;

	public Board(int[][] blocks) {
		N = blocks.length;
		board = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				board[i][j] = blocks[i][j];
				if (board[i][j] == 0) {
					zRow = i;
					zCol = j;
				}
			}
		}
	}

	public int dimension() {
		return board.length;
	}

	public int hamming() {
		int count = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if ((i == N - 1) && (j == N - 1))
					break;
				if (board[i][j] != i * N + j + 1)
					count++;
			}
		}
		return count;
	}
	
	public int manhattan() {
		int row;
		int column;
		int mDistance = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == 0)
					continue;
				if (board[i][j] != i * N + j + 1) {
					if (board[i][j] % N == 0) {
						row = board[i][j] / N - 1;
						column = N - 1;
					}
					else {
						row = board[i][j] / N;
						column = board[i][j] % N - 1;
					}
					mDistance += Math.abs(i - row) + Math.abs(j - column);
				}
			}
		}
		return mDistance;
	}
	
	public boolean isGoal() {
		if (this.manhattan() == 0)
			return true;
		return false;
	}
	
	public Board twin() {
		int temp;
		Board twinBoard = new Board(board);		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N - 1; j++) {
					if ((twinBoard.board[i][j] != 0) && (twinBoard.board[i][j + 1] != 0)) {
						temp = twinBoard.board[i][j];
						twinBoard.board[i][j] = twinBoard.board[i][j + 1];
						twinBoard.board[i][j + 1] = temp;
						return twinBoard;
					}
			}
		}
		return null;
	}
	
	public boolean equals(Object y) {
		if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        int N1 = this.board.length;
        int N2 = that.board.length;
        if (N1 != N2)
        	return false;
        for (int i = 0; i < N1; i++) {
			for (int j = 0; j < N1; j++) {
				if (this.board[i][j] != that.board[i][j])
					return false;
			}
        }
        return true;
	}
	
	  
	
	public Iterable<Board> neighbors() {
		Queue<Board> neighbors = new Queue<Board>();
		
		Board left = toLeft(this);
		Board right = toRight(this);
		Board up = toUp(this);
		Board down = toDown(this);
		if (left != null)
				neighbors.enqueue(left);
		if (right != null)			
				neighbors.enqueue(right);
		if (up != null)
				neighbors.enqueue(up);
		if (down != null)
				neighbors.enqueue(down);
		return neighbors;
	}
	
	private static Board toLeft(Board y) {
		int temp;
		if (y.zCol - 1 < 0)
			return null;
		Board left = new Board(y.board);
		temp = left.board[y.zRow][y.zCol];
		left.board[y.zRow][y.zCol] = left.board[y.zRow][y.zCol - 1];
		left.board[y.zRow][y.zCol - 1] = temp;
		left.zCol = y.zCol - 1;
		return left;
	}
	
	private static Board toRight(Board y) {
		int temp;
		if (y.zCol + 1 >= y.N)
			return null;
		Board right = new Board(y.board);
		temp = right.board[y.zRow][y.zCol];
		right.board[y.zRow][y.zCol] = right.board[y.zRow][y.zCol + 1];
		right.board[y.zRow][y.zCol + 1] = temp;
		right.zCol = y.zCol + 1;
		return right;
	}
	
	private static Board toUp(Board y) {
		int temp;
		if (y.zRow - 1 < 0)
			return null;
		Board up = new Board(y.board);
		temp = up.board[y.zRow][y.zCol];
		up.board[y.zRow][y.zCol] = up.board[y.zRow - 1][y.zCol];
		up.board[y.zRow - 1][y.zCol] = temp;
		up.zRow = y.zRow - 1;
		return up;
	}
	
	private static Board toDown(Board y) {
		int temp;
		if (y.zRow + 1 >= y.N)
			return null;
		Board down = new Board(y.board);
		temp = down.board[y.zRow][y.zCol];
		down.board[y.zRow][y.zCol] = down.board[y.zRow + 1][y.zCol];
		down.board[y.zRow + 1][y.zCol] = temp;
		down.zRow = y.zRow + 1;
		return down;
	}
	

	
	public String toString() {
		int N = board.length;
	    StringBuilder s = new StringBuilder();
	    s.append(N + "\n");
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            s.append(String.format("%2d ", board[i][j]));
	        }
	        s.append("\n");
	    }
	    return s.toString();
	}
	
	public static void main(String[] args) {

	    // create initial board from file
	    In in = new In(args[0]);
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);
	    Board temp = initial.twin();
	    StdOut.println(initial.dimension());
	    StdOut.println(initial.hamming());
	    StdOut.println(initial.manhattan());
	    StdOut.println(initial.isGoal());
	    StdOut.println(initial.toString());
	    //StdOut.println(initial.equals(initial));
	    StdOut.println(initial.equals(temp));
	    
	    for (Board a : initial.neighbors()) {
	    	StdOut.println(a.toString());	    
	    	for (Board b : a.neighbors())
	    		StdOut.println(b.toString());
	    }
	    /* test
	    StdOut.println(temp.dimension());
	    StdOut.println(temp.hamming());
	    StdOut.println(temp.manhattan());
	    StdOut.println(temp.isGoal());
	    StdOut.println(temp.toString());
	    */
			
	}
}
