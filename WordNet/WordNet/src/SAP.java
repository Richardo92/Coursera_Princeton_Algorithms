
public class SAP {
	private final Digraph sapG;
	private revisedBFS sapBfsV;
	private revisedBFS sapBfsW;
	private int V = 0;
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null)
			throw new NullPointerException();
		sapG = new Digraph(G);
		V = sapG.V();
	}

	 // length of shortest ancestral path between v and w; -1 if no such path
	 public int length(int v, int w) {
		   if (v < 0 || v >= V)
			   throw new IndexOutOfBoundsException();
		   if (w < 0 || w >= V)
			   throw new IndexOutOfBoundsException();
		   int[] result = getShortestPath(v, w);
		   return result[0];
	 }
	
	 
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	 public int ancestor(int v, int w) {
		 if (v < 0 || v >= V)
			   throw new IndexOutOfBoundsException();
		 if (w < 0 || w >= V)
			   throw new IndexOutOfBoundsException();
		 int[] result = getShortestPath(v, w);
		 return result[1];
	 }
	 
	 private int[] getShortestPath(int v, int w) {
		   sapBfsV = new revisedBFS(sapG, v);
		   sapBfsW = new revisedBFS(sapG, w);
		   int[] result = new int[2];
		   boolean[] markedV = sapBfsV.gerMarked();
		   boolean[] markedW = sapBfsW.gerMarked();
		   int minDistance = Integer.MAX_VALUE;
		   int minAncestor = Integer.MAX_VALUE;
		   int distance = 0;
		   for (int i = 0; i < V; i++) {
			   if (markedV[i] && markedW[i]) {
				   distance = sapBfsV.distTo(i) + sapBfsW.distTo(i);
				   if (distance < minDistance) {
					   minDistance = distance;
					   minAncestor = i;
				   }
			   }
			   
		   }
		   if (minDistance == Integer.MAX_VALUE) {
			   result[0] = -1;
			   result[1] = -1;
			   return result;
		   }
		   result[0] = minDistance;
		   result[1] = minAncestor;
		   return result;
	 }
	 
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	 public int length(Iterable<Integer> v, Iterable<Integer> w) {
		 if (v == null || w == null)
			 throw new NullPointerException();
		 int[] result = getShortestPath(v, w);
		 return result[0];
	 }

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	 public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		 if (v == null || w == null)
			 throw new NullPointerException();
		 int[] result = getShortestPath(v, w);
		 return result[1];
	 }
	 
	 private int[] getShortestPath(Iterable<Integer> v, Iterable<Integer> w) {
		   int minDistance = Integer.MAX_VALUE;
		   int minAncestor = Integer.MAX_VALUE;
		   int[] result = new int[2];
		   for (int a: v) {
			   for (int b : w) {
				   int[] temp = getShortestPath(a, b);
				   if (temp[0] != -1 && temp[0] < minDistance) {
					   minDistance = temp[0];
					   minAncestor = temp[1];
				   }
			   }
		   }
		   if (minDistance == Integer.MAX_VALUE) {
			   result[0] = -1;
			   result[1] = -1;
			   return result;
		   }
		   result[0] = minDistance;
		   result[1] = minAncestor;
		   return result;
	 }
	 
	 /*  recursion causes stack overflow
	 private int find(int neighbour, int v, int w) {
		 for (int a : sapG.adj(neighbour)) {
			 if (sapBfsW.hasPathTo(a)) {
				 ancestor = a;
				 return sapBfsV.distTo(a) + sapBfsW.distTo(a);
			 }
			 return find(a, v, w);
		 }
		 ancestor = -1;
		 return -1;
	 }
	*/ 
	 public static void main(String args[]) {
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
