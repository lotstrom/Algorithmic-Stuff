package M6;

import java.util.Comparator;
import java.util.ArrayList;

public class HeapPQ<E> implements PrioQueue<E>{

	protected ArrayList<E> heapList, sortList;
	protected SortComparator<E> comparator;

	public HeapPQ(Comparator<E> comp) {

		heapList = new ArrayList<E>();
		sortList = new ArrayList<E>();
		comparator = new SortComparator<E>();
	}

	public void add(E element) {

		heapList.add(element);
		bubbleUp(size() - 1);
	}

	public E removeMin() {

		if (size() > 0) {
			swap(0, size() - 1);
			E outElement = heapList.remove(size() -1);
			bubbleDown(0);
			return outElement;
		}
		else return null;
	}
	
	public int size() {
		return heapList.size();
	}

	public int getParent(int i) {
		return ((i -1) / 2);
	}

	public int leftChild(int i) {
		return 2 * i + 1;
	}

	public int rightChild(int i) {
		return 2 * i + 2;
	}

	public void bubbleUp(int i) {

        while (i > 0 && comparator.compare(heapList.get(getParent(i)), heapList.get(i)) != -1) {
            swap(getParent(i), i);
            i = getParent(i);
        }
    }

	public void swap(int i, int j) {

		E temp = heapList.get(i);
		heapList.set(i, heapList.get(j));
		heapList.set(j, temp);	
	}

	public void bubbleDown(int i) {

		int left = leftChild(i);
		int right = rightChild(i);
		int largest = i;

		if (left < size() && comparator.compare(heapList.get(largest), heapList.get(left)) >= 0) {
			largest = left;
		}

		if (right < size() && comparator.compare(heapList.get(largest), heapList.get(right)) >= 0) {
			largest = right;
		}

		if (largest != i) {
			swap(largest, i);
			bubbleDown(largest);
		}
	}

	public void heapify(int count) {

		int end = 1;

		while (end < count) {

			bubbleUp(end);
			end += 1;
		}
	}
}
