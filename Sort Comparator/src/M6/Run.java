package M6;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;

public class Run {
	
	static Scanner scan = new Scanner(System.in);

	static SortComparator<Integer> comparator;
	static HeapPQ<Integer> heap = new HeapPQ<Integer>(comparator);
	static QuickSort quick = new QuickSort();
	
	static <T> void heapSort(List<T> list, Comparator<? super T> comp, HeapPQ<Integer> heap) {

		int counter = list.size();

		heap.heapify(counter);

		while (counter > 0) {

			int removed = heap.removeMin();
			heap.sortList.add(removed);
			counter--;		
		}
	}
	
	public static void buildHeap(Integer[] array, HeapPQ<Integer> heap) {

		for (int i = 0; i < array.length; i++) {
			heap.add(array[i]);
		}
	}

	public static List<Integer> arrayToList(Integer[] array) {

		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < array.length; i++) {

			list.add(array[i]);
			}
		return list;
	}

	public static void main (String[] args) {

		System.out.println("Specify an amount of indexes.");
		int number = scan.nextInt();

		Integer[] array = InsertionSort.randomArray(number);

		buildHeap(array, heap);
		
		long start = 0;
		long end = 0;
		long start2 = 0;
		long end2 = 0;
		long start3 = 0;
		long end3 = 0;
		long start4 = 0;
		long end4 = 0;
		
		//counting the average time for 20 iterations of each sort.

		for(int i = 1; i < 20; i++) {
			
			Integer[] copy = array.clone();
			long startHeapSort = System.currentTimeMillis();
	
			heapSort(heap.heapList, comparator, heap);
	
			long endHeapSort = System.currentTimeMillis();
			end += endHeapSort;  
			start += startHeapSort;
		}
		System.out.println("\nTime with Heap sort: " + (end - start) / 20 + " milliseconds");
		
		for(int i = 1; i < 20; i++) {
			
			Integer[] copy = array.clone();
			long startQuickSort = System.currentTimeMillis();
	
			quick.quickSort(copy, comparator);
	
			long endQuickSort = System.currentTimeMillis();
			end2 += endQuickSort;  
			start2 += startQuickSort;
		}
		System.out.println("\nTime with Quick sort: " + (end2 - start2) / 20  + " milliseconds");
		
		for(int i = 1; i < 20; i++) {
			
			Integer[] copy = array.clone();

			long startMergeSort = System.currentTimeMillis();
	
			List<Integer> listOne = MergeSort.mergeList(arrayToList(copy), comparator);
	
			long endMergeSort = System.currentTimeMillis();
			start3 += startMergeSort;  
			end3 += endMergeSort;
		}
			System.out.println("\nTime with Merge sort: " + (end3 - start3) / 20  + " milliseconds");
		
		for(int i = 1; i < 20; i++) {
			
			Integer[] copy = array.clone();
		
			long startInsert = System.currentTimeMillis();
	
			InsertionSort.insertionSort(copy);
	
			long endInsert = System.currentTimeMillis();
			end4 += endInsert;  
			start4 += startInsert;
		}	
		System.out.println("\nTime with Insertion sort: " + (end4 - start4) / 20  + " milliseconds");
	}
}

