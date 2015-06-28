package M6;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {
	static <T> List<T> mergeList(List<T> list, Comparator<? super T> c) {

		c = new SortComparator<T>();

		if (list.size() <= 1)
			return list;

		List<T> left = new ArrayList<T>(), right = new ArrayList<T>();
		int middle = list.size() / 2, highest = list.size();
		for (int n = 0; n < middle; n++) {
			left.add(list.get(n));
		}
		for (int n = middle; n < highest; n++) {
			right.add(list.get(n));
		}
		left = mergeList(left, c);
		right = mergeList(right, c);
		return merge(left, right, c);
	}

	static <T> List<T> merge(List<T> left, List<T> right,
			Comparator<? super T> c) {
		List<T> result = new ArrayList<T>();
		while (left.size() > 0 || right.size() > 0) {
			if (left.size() > 0 && right.size() > 0) {
				if (c.compare(left.get(0), right.get(0)) <= 0) {
					result.add(left.get(0));
					left.remove(0);
				} 
				else {
					result.add(right.get(0));
					right.remove(0);
				}
			} 
			else if (left.size() > 0) {
				result.add(left.get(0));
				left.remove(0);
			} 
			else if (right.size() > 0) {
				result.add(right.get(0));
				right.remove(0);
			}
		}
		return result;
	}
}

