package student_classes;

import java.util.HashSet;

public class MySet<T> {

	HashSet<T> backingSet = new HashSet<T>();

	// returns the number of items in the set
	public int size(){
		return backingSet.size();
	}

	// removes all items from the set
	public synchronized void clear(){
		backingSet.clear();
	}

	// removes the object from the set
	public synchronized boolean remove(T o){
		return backingSet.remove(o);

	}

	// adds the object to the set
	public synchronized boolean add(T o){
		return backingSet.add(o);
	}

	// returns true if the set contains the object
	public synchronized boolean contains(T o){
		return backingSet.contains(o);
	}

}
