public class Solver {
	private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
	private MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
	private Stack<Board> tempOutput = new Stack<Board>();
	private Queue<Board> output = new Queue<Board>();
	private boolean result;
	private int resultMoves;
	private SearchNode temp = null;
	private SearchNode twinTemp = null;
	
	private class SearchNode implements Comparable<SearchNode>{  
        private Board board;   
        private final int priority;   
        private SearchNode previous = null;
        private int moves;
        public SearchNode(Board board, int moves, SearchNode previous){  
            this.board = board;
            this.priority = board.manhattan() + moves;
            this.previous = previous;
            this.moves = moves;
        }  
  
        @Override  
        public int compareTo(SearchNode that) {  
            if (this.board.equals(that.board)) return 0;  
            if (this.priority < that.priority) return -1;  
            else return 1;  
        }  
    }  

	public Solver(Board initial) {
		temp = new SearchNode(initial, 0, null);
		twinTemp = new SearchNode(initial.twin(), 0, null);
		
		while ((!temp.board.isGoal()) && (!twinTemp.board.isGoal())) {		
			for (Board a : temp.board.neighbors()) {
				if (temp.previous == null)
					pq.insert(new SearchNode(a, temp.moves + 1, temp));
				else if (a.equals(temp.previous.board))
					continue;
				else
					pq.insert(new SearchNode(a, temp.moves + 1, temp));
			}
			temp = pq.delMin();						
			for (Board a : twinTemp.board.neighbors()) {
				if (twinTemp.previous == null)
					twinPQ.insert(new SearchNode(a, twinTemp.moves + 1, twinTemp));	
				else if (a.equals(twinTemp.previous.board))
					continue;
				else
					twinPQ.insert(new SearchNode(a, twinTemp.moves + 1, twinTemp));	
			}
			twinTemp = twinPQ.delMin();	
		}
		if (temp.board.isGoal()) {
			result = true;
			resultMoves = temp.moves;
		}
		else if (twinTemp.board.isGoal()) {
			resultMoves = -1;
			result = false;
		}
	}
	    
	public boolean isSolvable() {
		if (result == true)
			return true;
		else 
			return false;
	}
	
	public int moves() {		
		return resultMoves;
	}
	
	public Iterable<Board> solution() {
		if (!this.isSolvable())
			return null;
		while ( temp != null) {
			tempOutput.push(temp.board);
			temp = temp.previous;
		}
		while (!tempOutput.isEmpty())
			output.enqueue(tempOutput.pop());
		return output;
	}
	
	public static void main(String[] args) {
		In in = new In(args[0]);
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);
	    
	    Solver solver = new Solver(initial);
	    
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
}
