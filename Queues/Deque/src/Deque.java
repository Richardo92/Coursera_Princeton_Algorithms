import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
	private int N;               // number of elements on queue
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    /**
     * Initializes an empty queue.
     */
    public Deque() {
        first = null;
        last  = null;
        N = 0;
    }

    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return (first == null || last == null);
    }

    /**
     * Returns the number of items in this queue.
     * @return the number of items in this queue
     */
    public int size() {
        return N;     
    }
    
    public void addFirst(Item item) {
    	if (item == null)	throw new NullPointerException();
    	Node<Item> oldfirst = first;
    	first = new Node<Item>();
    	first.item = item;
    	if (isEmpty())	last = first;
    	else {
    		first.next = oldfirst;
    		oldfirst.prev = first;
    	}
    	N++;
    }

    public void addLast(Item item) {
    	if (item == null)	throw new NullPointerException();
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else  {
        	oldlast.next = last;
        	last.prev = oldlast;
        }
        N++;
    }

    public Item removeFirst() {
    	if (isEmpty()) throw new NoSuchElementException("Deque underflow");
    	Item item = first.item;
    	if (first == last) {
    		last = null;
    		first = null;
    		N--;
    		return item;
    	}
    	first = first.next;
    	first.prev = null;
    	N--;
    	if (isEmpty())	last = null;
    	return item;
    }
    
    public Item removeLast() {
    	if (isEmpty())	 throw new NoSuchElementException("Deque underflow");
    	Item item = last.item;
    	if (first == last) {
    		last = null;
    		first = null;
    		N--;
    		return item;
    	}
    	last = last.prev;
    	last.next = null;
    	N--;
    	return item;
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(first);  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    public static void main(String[] args) {
        Deque<String> q = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.addLast(item);
            else if (!q.isEmpty()) StdOut.print(q.removeFirst() + " ");
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
	
}
