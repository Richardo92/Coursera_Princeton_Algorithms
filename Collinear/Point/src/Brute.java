import java.util.Arrays;


public class Brute {
	
	
	public static void main(String[] args) {
		// rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.001);  // make the points a bit larger


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

        
        for (int i = 0; i < N - 3; i++) {
        	for (int j = i + 1; j < N - 2; j++) {
        		for (int m = j + 1; m < N - 1; m++) {
        			for (int n = m + 1; n < N; n++) {
        				if ((a[i].slopeTo(a[j]) == a[i].slopeTo(a[m])) 
        					&& (a[i].slopeTo(a[m]) == a[i].slopeTo(a[n]))) {
        					StdOut.println(a[i].toString() + " -> "  
                                    + a[j].toString() + " -> " + a[m].toString()   
                                    + " -> " + a[n].toString());  
        					a[i].drawTo(a[n]);     					
        				}
        			}
        				
        		}
        	}
        }
        StdDraw.show(0);
        
        
	}

}
