package student_classes;
import java.util.LinkedList;

public class MyQueue<T> {

	LinkedList<T> backingSet = new LinkedList<T>();

	// returns the number of items in the queue
	public synchronized int size() {
		return backingSet.size();
	}

	// removes all items from the queue
	public synchronized void clear() {
		backingSet.clear();
	}

	// adds the object to one end of the queue, and continues to try and add if the space is empty
	public synchronized void enqueue(T o) {
		this.notifyAll();
		backingSet.add(o);
	}

	// removes an object from the opposite end of the end adding
	public synchronized T dequeue() {
		// if the queue is empty, the thread will wait until an item becomes available
		while (backingSet.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {

			}
		}
		// use poll() to remove - retrieves and removes the head (first element) of this list.
		return backingSet.poll();
	}
}

