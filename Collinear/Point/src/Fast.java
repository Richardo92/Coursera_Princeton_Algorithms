import java.util.Arrays;


public class Fast {

	public static void main(String[] args) {
		// rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        
        Point[] a = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            StdOut.printf(" %d\n", x);
            StdOut.printf(" %d\n", y);
            Point p = new Point(x, y);
            a[i] = p;
            p.draw();
        }
        Arrays.sort(a);
        Point[] aux = new Point[N];
        for (int i = 0; i < N - 3; i++) {
        	for (int j = i; j < N; j++)
        		aux[j] = a[j];
        	Arrays.sort(aux, i + 1, N, aux[i].SLOPE_ORDER);
        	Arrays.sort(aux, 0, i, aux[i].SLOPE_ORDER);
        	int head = i + 1;
        	int tail = i + 2;
        	int pHead = 0;
        	while(tail < N){
        		while((tail < N) && (aux[i].slopeTo(aux[head]) == aux[i].slopeTo(aux[tail])))
        			tail++;
        		if (tail - head >= 3) {
        			double pAngle = Double.NEGATIVE_INFINITY;
        			while (pHead < i) {
        				pAngle = aux[i].slopeTo(aux[pHead]);
        				if (pAngle < aux[i].slopeTo(aux[head]))
        					pHead++;
        				else
        					break;
        			}
        			if (pAngle != aux[i].slopeTo(aux[head])) {
        				aux[i].drawTo(aux[tail - 1]);
        				String output = aux[i].toString() + " -> "; 
        				for (int m = head; m < tail - 1; m++)
        					output += aux[m].toString() + " -> ";
        				output += aux[tail - 1];
        				StdOut.println(output);
        			}
        		}
        			head = tail;
        			tail++;    	
        	}
        }
        StdDraw.show(0);
	}
}
