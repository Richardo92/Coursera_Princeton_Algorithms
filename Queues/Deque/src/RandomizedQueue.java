import java.util.Iterator;
import java.util.NoSuchElementException;
public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] randomArray;
	private int head;
	private int tail;
	private int N = 0;
	public RandomizedQueue() {
		randomArray = (Item[]) new Object[1];
		head = 0;
		tail = 0;
	}
	
	public boolean isEmpty() {
		return N == 0;
	}
	
	public int size() {
		return N;
	}
	
	public void enqueue(Item item) {
		if (item == null)	throw new NullPointerException();
		if (N < 0) throw new IllegalArgumentException();
		if (N > 0 && N == randomArray.length)		resize(2 * randomArray.length);
		randomArray[tail++] = item;
		N++;
	}
	
	public Item dequeue() {
		if (isEmpty())	throw new NoSuchElementException("Deque underflow");
		if (N < 0) throw new IllegalArgumentException();
		Item temp;
		int random;
		
		random = StdRandom.uniform(head, tail);
		temp = randomArray[random];
		randomArray[random] = randomArray[--tail];
		randomArray[tail] = null;
		N--;
		if (N > 0 && N == randomArray.length / 4)		resize(randomArray.length / 2);
		return temp;
	}
	
	public Item sample() {
		if (isEmpty())	throw new NoSuchElementException("Queue empty");
		Item temp;
		int random;
		random = StdRandom.uniform(head, tail);
		temp = randomArray[random];
		return temp;
	}
	
	private void resize(int length) {
		Item[] copy = (Item[]) new Object[length]; 
		for (int i = 0; i < N; i++)
			copy[i] = randomArray[i];		
		randomArray = copy;
	}
	
	 public Iterator<Item> iterator() {
		 return new ListIterator();  
	 }
	 
	 private class ListIterator implements Iterator<Item> {

		private Item[] reOrganizedArray;
		private int random;
		private int j = 0;
		private Item temp;
		public ListIterator() {
			reOrganizedArray = (Item[]) new Object[N];
			for (int i = 0; i < tail; i++)
				reOrganizedArray[i] = randomArray[i];
			for (int i = 0; i < tail; i++) {
				random = StdRandom.uniform(head, i + 1);
				temp = reOrganizedArray[random];
				reOrganizedArray[random] = reOrganizedArray[i];
				reOrganizedArray[i] = temp;
			}
		}
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return j < tail;
		}

		@Override
		public Item next() {
			if (j >= tail)
				throw new java.util.NoSuchElementException();
			return reOrganizedArray[j++];
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			 throw new UnsupportedOperationException();
		}
		 
	 }
	 
	 public static void main(String[] args) {
	        RandomizedQueue<Integer> z = new RandomizedQueue<Integer>();
	        z.enqueue(1);
	        z.enqueue(4);
	        z.enqueue(3);
	        z.enqueue(6);
	        z.enqueue(7);
	        z.enqueue(8);
	        z.enqueue(41);
	        z.enqueue(53);
	        StdOut.printf("size of queue: %d\n", z.size());
	        
	        
	        for (int i : z) {
	            StdOut.printf("outer i = %d\n", i);
	            for (int j : z) {
	                StdOut.printf(" %d ", j);
	            }
	            StdOut.println();
	        }
	        
	    }
	

}
