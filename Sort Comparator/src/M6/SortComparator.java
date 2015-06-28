package M6;

import java.util.Comparator;

public class SortComparator<E> implements Comparator<E> {

	public int compare(E original, E other) {

		return((Comparable<E>)original).compareTo(other);
	}	
}
