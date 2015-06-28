package M6;

import java.util.ArrayList;
import java.util.Comparator;

public class QuickSort {

	static Integer[] array = new Integer[1000000];
	static ArrayList sorted = new ArrayList();
	static long time;

	static <T> void quickSort(T[] a, Comparator<? super T> comp) {

		comp = new SortComparator<T>();
		ArrayList<T> temp = new ArrayList<T>();
		for (int i = 0; i < a.length; i++) {
			temp.add(a[i]);
		}		
		sorted = algorithm(temp, comp);
	}

	static <T> ArrayList<T> algorithm(ArrayList<T> a, Comparator<? super T> comp) {
		
		ArrayList<T> B = new ArrayList<T>();
	    ArrayList<T> C = new ArrayList<T>();
		ArrayList<T> D = new ArrayList<T>();
		
		if (a.size() <= 1)
			return a;

		int pivot = (int)Math.random() * a.size();
		T pivotValue = a.get(pivot);
		for (int i = 0; i < a.size(); i++) {
			int v = comp.compare(a.get(i), pivotValue);
			if (v == -1) {
				B.add(a.get(i));
			} else if (v == 0) {
				C.add(a.get(i));
			} else {
				D.add(a.get(i));
			}
		}		
		return concatenate(algorithm(B, comp), C, algorithm(D, comp));
		
	}	
	static <T> ArrayList<T> concatenate(ArrayList<T> a, ArrayList<T> b, ArrayList<T> c) {

		a.addAll(c);
		a.addAll(b);		
		return a;		
	}	
}