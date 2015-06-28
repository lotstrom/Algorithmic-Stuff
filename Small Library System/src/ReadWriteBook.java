import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.text.ParseException;

import java.util.ArrayList;

import java.util.Scanner;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadWriteBook {

	static ArrayList<String> All = new ArrayList<String>();
	static ArrayList<String> AllBorrow = new ArrayList<String>();
	static Scanner book;
	static Scanner borrowbook;
	static File path;
	static JFileChooser fileChooser;
	static Connection connect;
	static Connection connectnew;
	static String DBname;

	/**
	 * Handels the file that is choosen and creates a database.
	 * 
	 * @param OpenBook
	 *            Opens the text file
	 * @param ReadBook
	 *            Reads the text file
	 * @param WriteBook
	 *            Creates database
	 */

	public static void OpenBook() {

		fileChooser = new JFileChooser();
		// A chooser GUI for the user to choose the

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			// (if)Directory of the text file
			java.io.File file = fileChooser.getSelectedFile();
			// Creating a path from the filechooser to use it later when writing
			// the books.
			path = fileChooser.getSelectedFile().getParentFile();

			try {
				book = new Scanner(new FileReader(file));
				System.out.println("The File Was Successfully Loaded");
			} catch (FileNotFoundException e) {
			}

		} else {
			System.out.println("No File Was Selected");
		}
	}

	public static void ReadBook() throws Exception {

		// creating a while loop to scan the text file and add each line to an
		// ArrayList.
		while (book.hasNextLine()) {

			All.add(book.nextLine());
		}
		// Calling the method SortBook from class Book to start sorting the
		// books.
		Book.SortBook();

	}

	public static void WriteBook() throws SQLException, ParseException {

		connectnew = DriverManager.getConnection(
				"jdbc:mysql://localhost/mysql", "root", "");
		try {

			Statement stmt6 = connectnew.createStatement();
			stmt6.executeUpdate("CREATE DATABASE " + DBname);
			// con.close(); // Close the root database after the database has
			// been created in the root
		} catch (Exception e) {
			System.out.println("error");
		}
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
				+ DBname + "", "root", "");
		try {
			Statement stmt = connect.createStatement();
			// Open connection with Con2
			// Creating the tables in the database selected.
			stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS "
					+ "`borrow` (`LibraryCardNumber` INT NOT NULL ,`Name` VARCHAR(70) NULL ,`Street` VARCHAR(70) NULL ,`ZipCode` INT NULL ,`Town` "
					+ "VARCHAR(45) NULL ,PRIMARY KEY (`LibraryCardNumber`) );");

			stmt.executeUpdate("CREATE  TABLE IF NOT EXISTS "
					+ "`dateBorrow` (`iddateBorrow` INT NOT NULL AUTO_INCREMENT ,"
					+ "`ISBN` VARCHAR(45) NULL ,`BorrowDate` DATE NULL ,`ReturnDate` DATE NULL ,"
					+ "`LibraryCardNumber` INT NULL ,PRIMARY KEY (`iddateBorrow`) );");

			stmt.executeUpdate(" CREATE  TABLE IF NOT EXISTS "
					+ "`books` (`ISBN` VARCHAR(45) NOT NULL ,"
					+ "`CopyNumber` INT NULL ,`Title` VARCHAR(70) NULL ,`Author` VARCHAR(45) NULL ,`Publisher` VARCHAR(45) NULL ,"
					+ "`Year` INT NULL ,`Statictics` INT NULL ,PRIMARY KEY (`ISBN`) );");
			// connect.close();

		} catch (SQLException e) {
			System.err.println(e);
		}

		for (int g = 0; g < Book.Right.size(); g++) { // Go through the contents
														// in the arraylist
			Scanner input = new Scanner(Book.Right.get(g));
			input.useDelimiter("#");

			while (input.hasNext()) { // Saving the contents into
										// int/String/Date
				String ISBN = input.next();
				String Copy = input.next();
				int CopyNumber = Integer.parseInt(Copy);
				String Title = input.next();
				String Author = input.next();
				String Publisher = input.next();
				String year = input.next();

				String Stats = input.next();

				String borrow = input.next();

				if (input.hasNext()) {
					String Return = input.next();

					if (input.hasNext()) {

						String CardNumber = input.next();
						String sql = "INSERT INTO `dateBorrow`(`ISBN`, `BorrowDate`, `ReturnDate`, `LibraryCardNumber`) VALUES ( ? , ? , ? , ?)";
						PreparedStatement pst = connect.prepareStatement(sql);
						pst.setString(1, ISBN);
						pst.setString(2, borrow);
						pst.setString(3, Return);
						pst.setString(4, CardNumber);
						pst.execute();
					}
				}
				// Inserting the Books from the arraylist into the database
				String query1 = "INSERT INTO books (`ISBN`, `CopyNumber`, `Title`, `Author`, `Publisher`, `Year`, `Statictics`)"
						+ "VALUES ('"
						+ ISBN
						+ "','"
						+ CopyNumber
						+ "','"
						+ Title
						+ "','"
						+ Author
						+ "','"
						+ Publisher
						+ "','"
						+ year + "','" + Stats + "')";

				Statement stmt = connect.createStatement();
				stmt.executeUpdate(query1);
			}
		}

		java.io.File file2 = new java.io.File(path + "/Borrowers.txt");

		try {
			borrowbook = new Scanner(new FileReader(file2));
			System.out.println("BorrowFile loaded succesfully");
			Scanner line = new Scanner(file2); // Reading the file!

			while (line.hasNextLine()) { // Reading in the file line after line
				AllBorrow.add(line.nextLine()); // Saving into the arraylist
			}
		} catch (Exception e) {
		}
		for (int j = 0; j < AllBorrow.size(); j++) { // Lopping through the
														// contents
			Scanner in = new Scanner(AllBorrow.get(j));
			in.useDelimiter("#");

			while (in.hasNext()) { // Saving the contents from the arralist
									// (Borrowers)
				int LibraryCardNumber = in.nextInt();
				String Name = in.next();
				String Street = in.next();
				if (in.hasNext()) {
					int ZipCode = in.nextInt();

					String Town = in.next();

					// Inserting everything in the borrowers file into the
					// database
					String query3 = "INSERT INTO borrow (`LibraryCardNumber`, `Name`, `Street`, `ZipCode`,`Town`)"
							+ "VALUES ('"
							+ LibraryCardNumber
							+ "','"
							+ Name
							+ "','"
							+ Street
							+ "','"
							+ ZipCode
							+ "','"
							+ Town
							+ "')";

					Statement stmt = connect.createStatement();
					stmt.executeUpdate(query3);
					// Sending the query into the database
				}
			}
		}
	}

	public static boolean LibraryCardNumber(String Card) throws SQLException {

		// Connecting to the databse and getting the LibraryCardNumber
		int LibraryCardNumber = Integer.parseInt(Card);
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
				+ DBname + "", "root", "");
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt
				.executeQuery("select * from borrow where LibraryCardNumber='"
						+ LibraryCardNumber + "'");

		// If the LibraryCardNumber exists then return true
		if (rs.next()) {
			// Getting the LibraryCardNumber from the database and saving it
			LibraryCardNumber = rs.getInt("LibraryCardNumber");

			return true;

		} else {

			return false;
		}
	}

	public static boolean checkCopyNumber(String Copy) throws SQLException {

		Copy = MainFrame.isbn.getText().toString();
		// Connecting to the database and get the ISBN from the user and find it
		// in the table books
		// and then get the CopyNumber for that ISBN(Book)
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
				+ DBname + "", "root", "");
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery("select * from books where ISBN='"
				+ Copy + "'");

		int CopyNumber;
		String ISBN = "";

		if (rs.next()) { // getting the CopyNumber
			ISBN = rs.getString("ISBN");
			CopyNumber = rs.getInt("CopyNumber");
			System.out.println(CopyNumber);

			if (CopyNumber != 0) {// if the copynumber not = to 0 then decrease
									// it with 1(CopyNumber-1)
				CopyNumber -= 1;
				// and update the change into the databse where ISBN and
				// CopyNumber in the same row
				String query4 = ("UPDATE books SET CopyNumber ='" + CopyNumber
						+ "' where ISBN='" + Copy + "'");

				Statement stmt1 = connect.createStatement();
				stmt1.executeUpdate(query4); // Execute the change
				return true; // if all the statments are true then return true
								// else false
			} else {
				return false;
			}

		} else {

			return false;
		}
	}

	public static boolean checkbook(String ISBN) throws Exception {

		// Connecting to the databse
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
				+ DBname, "root", "");
		ISBN = MainFrame.isbn.getText();
		Statement stmt = connect.createStatement();
		ResultSet rs = stmt.executeQuery("select * from books where ISBN='"
				+ ISBN + "'");
		// Query to find the ISBN the user inputs.

		int CopyNumber;
		String Title = "";
		String Author = "", Publisher = "";
		int Year;
		int Statictics;

		if (rs.next()) {
			// Getting the contents needed from the databse
			ISBN = rs.getString("ISBN");
			CopyNumber = rs.getInt("CopyNumber");
			Title = rs.getString("Title");
			Author = rs.getString("Author");
			Year = rs.getInt("Year");
			Statictics = rs.getInt("Statictics");

			MainFrame.searchName.setText(Title + " " + Author + " " + Year);
			MainFrame.pnlSearchN.add(MainFrame.searchName);
			// adding to the searching by ISBN

			return true;

		} else {
			System.out.println("ISB Not Found!");
			// Error messege if the ISBN is not found

			return false;
		}
	}

	public static void main(String args[]) throws Exception {

		ReadWriteBook.OpenBook();
		ReadWriteBook.ReadBook();
	}
}
