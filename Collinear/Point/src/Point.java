/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new slopeOrder();       // YOUR DEFINITION HERE
    

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
    	if ((this.x == that.x) && (this.y != that.y))
    		return Double.POSITIVE_INFINITY;
    	if ((this.x == that.x) && (this.y == that.y))
    		return Double.NEGATIVE_INFINITY;
    	if ((this.x != that.x) && (this.y == that.y))
    		return +0.0;
    	else
    		return (double) (that.y - this.y) / (that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
    	if (this.y < that.y)
    		return -1;
    	if ((this.y == that.y) && (this.x < that.x))
    		return -1;
    	if ((this.y == that.y) && (this.x == that.x))
    		return 0;
    	else
    		return 1;
    }
    
    private int compareTo(Comparator<Point> SLOPE_ORDER, Point o1, Point o2) {
    	return SLOPE_ORDER.compare(o1, o2);
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }


    private class slopeOrder implements Comparator<Point> {

    	@Override
    	public int compare(Point o1, Point o2) {
    		// TODO Auto-generated method stub
    		if (slopeTo(o1) > slopeTo(o2))
    			return 1;
    		if (slopeTo(o1) < slopeTo(o2))
    			return -1;
    		else
    			return 0;
    	}   	
    }



    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    	StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.1);  // make the points a bit larger
        StdDraw.show(0);
    	Point origin = new Point(0, 0);
    	Point temp = new Point(1, 1);
    	Point temp2 = new Point(2, 3);
    	origin.draw();
    	temp.draw();
    	temp2.draw();

    	int test1;
    	int test2;
    	double angle;
    	test1 = temp.compareTo(temp2.SLOPE_ORDER, origin, temp);
    	StdOut.printf(" %d\n", test1);
    	test2 = temp2.compareTo(temp);
    	StdOut.printf(" %d\n", test2);
    	angle = origin.slopeTo(temp2);
    	StdOut.printf(" %f\n", angle);
    	
    }
}

