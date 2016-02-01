
public class PointSET {
	private SET<Point2D> set;
	
	public PointSET() {
		set = new SET<Point2D>();
	}
	
	public boolean isEmpty() {
		return set.isEmpty();
	}
	
	public int size() {
		return set.size();
	}
	
	public void insert(Point2D p) {
		if (!set.contains(p))
			set.add(p);
	}
	
	public boolean contains(Point2D p) {
		return set.contains(p);
	}
	
	public void draw() {
		if (set.size() == 0)
			return;
		for (Point2D a: set)
			a.draw();
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		SET<Point2D> inRect = new SET<Point2D>();
		if (set.size() == 0)
			return inRect;
		for (Point2D a : set) {
			if (rect.contains(a))
				inRect.add(a);
		}
		return inRect;
	}
	
	public Point2D nearest(Point2D p) {
		Point2D disNearest = null;
		double minDis = Double.MAX_VALUE;
		if (set.size() == 0)
			return disNearest;
		for (Point2D a: set) {
			if (minDis > p.distanceSquaredTo(a)) {
				minDis = p.distanceSquaredTo(a);
				disNearest = a;
			}
		}
		return disNearest;	
	}
}
