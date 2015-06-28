import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Book {

	static ArrayList<String> Authors = new ArrayList<String>();
	static ArrayList<String> BorrowDate = new ArrayList<String>();
	static ArrayList<String> ReturnDate = new ArrayList<String>();
	static ArrayList<String> LibraryCardNumber = new ArrayList<String>();
	static ArrayList<String> Wrong1 = new ArrayList<String>();
	static ArrayList<String> Right = new ArrayList<String>();
	static String[] part;
	static String[] finalpart;

	/**
	 * Creating a method to sort the books.
	 * 
	 * @param ISBN
	 *            Checking if ISBN is correct.
	 * @param Copy
	 *            number Check if the copy number is an int.
	 * @param Titel
	 *            Checking for a value.
	 * @param Author
	 *            Checking for a value.
	 * @param Publisher
	 *            Checking for a value.
	 * @param Year
	 *            Check if Year is an int.
	 * @param Statistic
	 *            Check if Statistic is an int.
	 * @param Borrow
	 *            date Checks for date format.
	 * @param Return
	 *            date Checks for date format.
	 */

	public static void SortBook() throws Exception {

		// Adding the lines from the main arraylist into a String Array (part)
		part = ReadWriteBook.All.toArray(new String[ReadWriteBook.All.size()]);

		for (int i = 0; i < part.length; i++) {
			// Splitting each line in the array into a different array
			finalpart = part[i].split("#");
			// Creating a variable to check the date
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

			// Creating a String to contain all error messages
			String Error = "";

			// Checking if the Isbn is correct or not form the method IsbnCheck.
			try {

				if (IsbnCheck() == true) {

				} else {
					// Adding a Wrong message if the Isbn is incorrect
					Error += "Wrong Isbn ";
				}
			} catch (Exception e) {
				// Adding a Wrong message if the Isbn contain non numeric
				// thingys.
				Error += "Wrong Isbn ";
			}

			try {
				int number = Integer.parseInt(finalpart[1]);

			} catch (Exception y) {

				// Adding a Wrong message if copy number is not an int.
				Error += "Wrong Copy Number ";
			}

			if (finalpart[2].equals("")) {
				// Right.remove(part[i]);
				Error += "No Title ";

			}

			if (finalpart[3].equals("")) {
				// Right.remove(part[i]);
				Error += "No Author ";
			}

			if (finalpart[4].equals("")) {
				// Right.remove(part[i]);
				Error += "No Publisher ";
			}

			try {
				int year = Integer.parseInt(finalpart[5]);

			} catch (Exception y) {

				Error += "Wrong Year ";
			}

			try {
				int stat = Integer.parseInt(finalpart[6]);

			} catch (Exception y) {

				Error += "Wrong Stat ";
			}

			/*
			 * Try to add a borrow date, if there isn't any then add no borrow
			 * date also checking if the Borrowdate is a valid date
			 */
			try {

				BorrowDate.add(finalpart[7]);
				try {
					sdf.setLenient(false);
					sdf.parse(finalpart[7]);

				} catch (Exception e) {
					// Right.remove(part[i]);
					Error += "Wrong Borrow Date ";

				}
			} catch (Exception e) {
				ReturnDate.add("No BorrowDate Available");
			}

			/*
			 * Try to add a return date, if there isn't any then add no return
			 * date also checking if the Return date is a valid date if there is
			 * no return date then nothing is wrong with the line.
			 */
			try {

				ReturnDate.add(finalpart[8]);
				try {
					sdf.setLenient(false);
					sdf.parse(finalpart[8]);

				} catch (Exception e) {
					// Right.remove(part[i]);
					Error += "Wrong Return Date ";

				}
			} catch (Exception e) {
				ReturnDate.add("No Return Date Available");
			}

			/*
			 * Try to add a Card Number , if there isn't any then add no Card
			 * Number also checking if the Card Number is am int if there is no
			 * Card Number then nothing is wrong with the line.
			 */
			try {
				LibraryCardNumber.add(finalpart[9]);
				try {
					int numbercard = Integer.parseInt(finalpart[9]);

				} catch (Exception w) {

					Error += "Wrong Card Number ";
				}

			} catch (Exception e) {
				LibraryCardNumber.add("No Card Number");
			}
			// if the String Error is empty then add the lines to the Right
			// ArrayList.
			if (Error.isEmpty()) {
				Right.add(part[i]);
			}
			// if Error contain a variable then add it to the Wrongbook
			// ArrayList.
			else {
				Wrong1.add(Error + part[i]);
			}
		}

		// Adding author to its own arraylist to sort the correct books by
		// author.
		part = Right.toArray(new String[Right.size()]);

		for (int i = 0; i < part.length; i++) {
			finalpart = part[i].split("#");

			Authors.add(finalpart[3]);
		}

		// Creating 2 for loops to compare root.
		for (int a = 0; a < Authors.size(); a++) {
			for (int i = 0; i < Authors.size() - 1; i++) {
				// Comparing if teh Authors is bigger than 0 to go to next step.
				if (Authors.get(i).compareTo(Authors.get(i + 1)) > 0) {

					String temp = Right.get(i);
					Right.set(i, Right.get(i + 1));
					Right.set((i + 1), temp);
					// Creating a temp String and starting to swap and connect
					// both arrayLists.

					String temps = Authors.get(i);
					Authors.set(i, Authors.get(i + 1));
					Authors.set((i + 1), temps);
				}
			}
		}

		// calling the method to write the books from the class ReadWriteBook
		// after being done with sorting.
		ReadWriteBook.WriteBook();
	}

	// a method to check the Isbn.
	public static boolean IsbnCheck() {

		int check = 0;
		for (int i = 0; i < 12; i += 2) {
			check += Integer.valueOf(finalpart[0].substring(i, i + 1));
		}
		for (int i = 1; i < 12; i += 2) {
			check += Integer.valueOf(finalpart[0].substring(i, i + 1)) * 3;
		}
		check += Integer.valueOf(finalpart[0].substring(12));
		return check % 10 == 0;
	}
}
