
public class KdTree {
	private static class KdNode {  
        private KdNode leftNode;  
        private KdNode rightNode;  
        private final boolean isVertical;  
        private final double x;  
        private final double y;  
  
        public KdNode(final double x, final double y, final KdNode leftNode,  
                final KdNode rightNode, final boolean isVertical) {  
            this.x = x;  
            this.y = y;  
            this.leftNode = leftNode;  
            this.rightNode = rightNode;  
            this.isVertical = isVertical;  
        }  
    }  
	
	private static final RectHV CONTAINER = new RectHV(0, 0, 1, 1);  
    private KdNode rootNode;  
    private int size;  
    
    public KdTree() {
    	this.size = 0;
    	this.rootNode = null;
    }
	
    public boolean isEmpty() {
    	return this.size == 0;
    }
	
    public int size() {
    	return this.size;
    }
    
    public void insert(Point2D p) {
    	this.rootNode = insert(this.rootNode, p, true); 
    }
    
    private KdNode insert(final KdNode node, final Point2D p,  
            final boolean isVertical) {
    	if (node == null) {
    		this.size++;
    		return new KdNode(p.x(), p.y(), null, null, isVertical);
    	}
    	if (node.x == p.x() && node.y == p.y()) {  
            return node;  
        }  
    	if ((node.isVertical && (p.x() < node.x)) || (!node.isVertical && (p.y() < node.y)))
    		node.leftNode = insert(node.leftNode, p, !node.isVertical);
    	else
    		node.rightNode = insert(node.rightNode, p, !node.isVertical);
    	return node;   	
    }
    
    public boolean contains(Point2D p) {
    	KdNode temp = this.rootNode;
        while (temp != null) {
        	if ((temp.x == p.x()) && (temp.y == p.y()))
        		return true;
        	if (temp.isVertical && (p.x() < temp.x) || (!temp.isVertical && (p.y() < temp.y)))
        		temp = temp.leftNode;
        	else
        		temp = temp.rightNode;
        }
        
        return false;
    }
    
    public void draw() {
    	StdDraw.setScale(0, 1);  
    	  
        StdDraw.setPenColor(StdDraw.BLACK);  
        StdDraw.setPenRadius();  
        CONTAINER.draw();  
  
        draw(rootNode, CONTAINER);  
    }
    
    private void draw(KdNode node, RectHV container) {
    	if (node == null) {  
            return;  
        }  
  
        // draw the point  
        StdDraw.setPenColor(StdDraw.BLACK);  
        StdDraw.setPenRadius(0.01);  
        new Point2D(node.x, node.y).draw();  
        RectHV newContainerLeft = null;
        RectHV newContainerRight = null;
        Point2D min, max;
        if (node.isVertical) {
        	min = new Point2D(node.x, container.ymin());
        	max = new Point2D(node.x, container.ymax());
        	StdDraw.setPenColor(StdDraw.RED);
        	StdDraw.setPenRadius();
        	min.drawTo(max);
        	newContainerLeft = new RectHV(container.xmin(), container.ymin(),
        			node.x, container.ymax());
        	newContainerRight = new RectHV(node.x, container.ymin(),
        			container.xmax(), container.ymax());
        }
        else {
        	min = new Point2D(container.xmin(), node.y);
        	max = new Point2D(container.xmax(), node.y);
        	StdDraw.setPenColor(StdDraw.BLUE);
        	StdDraw.setPenRadius();
        	min.drawTo(max);
        	newContainerLeft = new RectHV(container.xmin(), container.ymin(),
        			container.xmax(), node.y);
        	newContainerRight = new RectHV(container.xmin(), node.y,
        			container.xmax(), container.ymax());
        }
        draw(node.leftNode, newContainerLeft);
        draw(node.rightNode, newContainerRight);
        
    	
    }
    
    public Iterable<Point2D> range(RectHV rect) {
    	SET<Point2D> inRect = new SET<Point2D>();
    	range(rect, this.rootNode, CONTAINER, inRect);
    	   	
		return inRect;
    	
    }
    
    private void range(RectHV rect, KdNode node, RectHV container, SET<Point2D> inRect) {
    	RectHV newContainerLeft = null;
    	RectHV newContainerRight = null;
    	if (node == null) {  
            return;  
        } 
    	Point2D p = new Point2D(node.x, node.y);
    	if (rect.contains(p))
    		inRect.add(p);
    	if (node.isVertical) {
    		newContainerLeft = new RectHV(container.xmin(), container.ymin(),
        			node.x, container.ymax());
    		newContainerRight = new RectHV(node.x, container.ymin(),
        			container.xmax(), container.ymax());
    		if (rect.intersects(newContainerLeft))
    			range(rect, node.leftNode, newContainerLeft, inRect);
    		if (rect.intersects(newContainerRight))
    			range(rect, node.rightNode, newContainerRight, inRect);
    		
    	}
    	else {
    		newContainerLeft = new RectHV(container.xmin(), container.ymin(),
        			container.xmax(), node.y);
    		newContainerRight = new RectHV(container.xmin(), node.y,
        			container.xmax(), container.ymax());
    		if (rect.intersects(newContainerLeft))
    			range(rect, node.leftNode, newContainerLeft, inRect);
    		if (rect.intersects(newContainerRight))
    			range(rect, node.rightNode, newContainerRight, inRect);
    	}
    }
    private double distance = Double.MAX_VALUE;
    public Point2D nearest(Point2D p) {
    	//double distance = Double.MAX_VALUE;
    	if (this.isEmpty())
    		return null;
    	distance = Double.MAX_VALUE;
    	return nearest(p, this.rootNode, CONTAINER, null);    	
    }
    
    private Point2D nearest(Point2D p, KdNode node, RectHV container, Point2D candidate) {
    	double temp;
    	RectHV newContainerLeft = null;
    	RectHV newContainerRight = null;
    	if (node == null)
    		return candidate;
    	Point2D now = new Point2D(node.x, node.y);;
    	Point2D nearest = candidate;
    	
    	temp = p.distanceSquaredTo(now);
    	if (temp < distance) {
    		//candidate = now;
    		distance = temp;
    		nearest = now;
    	}
    	if (node.isVertical) {
    		newContainerLeft = new RectHV(container.xmin(), container.ymin(),
        			node.x, container.ymax());
    		newContainerRight = new RectHV(node.x, container.ymin(),
        			container.xmax(), container.ymax());
    		if (newContainerLeft.contains(p)) {
    			if (newContainerLeft.distanceSquaredTo(p) < distance) {
    				nearest = nearest(p, node.leftNode,  newContainerLeft, nearest);
    				//candidate = nearest;
    			}
    			if (newContainerRight.distanceSquaredTo(p) < distance)
    				nearest = nearest(p, node.rightNode,  newContainerRight, nearest);
    		}
    		else {
    			if (newContainerRight.distanceSquaredTo(p) < distance) {
    				nearest = nearest(p, node.rightNode, newContainerRight, nearest);
    				//candidate = nearest;
    			}
    			if (newContainerLeft.distanceSquaredTo(p) < distance)
    				nearest = nearest(p, node.leftNode,  newContainerLeft, nearest);
    		}
    	}
    	else {
    		newContainerLeft = new RectHV(container.xmin(), container.ymin(),
        			container.xmax(), node.y);
    		newContainerRight = new RectHV(container.xmin(), node.y,
        			container.xmax(), container.ymax());
    		if (newContainerLeft.contains(p)) {
    			if (newContainerLeft.distanceSquaredTo(p) < distance) {
    				nearest = nearest(p, node.leftNode, newContainerLeft, nearest);
    				//candidate = nearest;
    			}
    			if (newContainerRight.distanceSquaredTo(p) < distance)
    				nearest = nearest(p, node.rightNode,  newContainerRight, nearest);
    		}
    		else {
    			if (newContainerRight.distanceSquaredTo(p) < distance) {
    				nearest = nearest(p, node.rightNode,  newContainerRight, nearest);
    				//candidate = nearest;
    			}
    			if (newContainerLeft.distanceSquaredTo(p) < distance)
    				nearest = nearest(p, node.leftNode,  newContainerLeft, nearest);
    		}    		
    	}    	
    	return nearest;
    }
    
    
    public static void main(String[] args) {
    	String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            kdtree.draw();
            StdDraw.show(0);
            StdDraw.show(40);
        }
    }
}
