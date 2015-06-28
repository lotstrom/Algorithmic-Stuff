package M6;

import java.util.Random;
import java.util.Scanner;

public class InsertionSort {
	static Random random = new Random();
	static Scanner input = new Scanner(System.in);
	
	public static void insertionSort(Integer[] a) {
		int valueToInsert = 0;
		int holePos = 0;
		for (int i = 1; i < a.length; i++) {
			holePos = i - 1;
			valueToInsert = a[i];
			while ((holePos >= 0) && (valueToInsert < a[holePos])) {
				a[holePos + 1] = a[holePos--];
				a[holePos + 1] = valueToInsert;
			}
		}
	}

	public static Integer[] bestCase(Integer a) {
		Integer[] array = new Integer[a];
		int number = 0;
		for (int i = 0; i < array.length; i++) {
			array[i] = number;
			number++;
		}
		return array;
	}
	
	public static Integer[] worstCase(Integer b) {
		Integer[] array = new Integer[b];
		int number = b;
		for (int i = 0; i < array.length; i++) {
			array[i] = number;
			number--;
		}
		return array;
	}

	public static Integer[] randomArray(Integer c) {
		Integer[] array = new Integer[c];
		for (int i = 0; i < array.length; i++) {
			array[i] = random.nextInt(100001);
		}
		return array;
	}
}