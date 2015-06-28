import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This class listens for the users inputs and contains all the action commands
 * for buttons etc.
 */

public class Listner implements ActionListener {

	public void actionPerformed(ActionEvent e) {

		String libCard = MainFrame.libraryCard.getText();
		if (e.getActionCommand().equals("submit")) {
			// if the isbn exists

			try {
				if (ReadWriteBook.LibraryCardNumber(MainFrame.libraryCard
						.getText()) == true) {
					MainFrame.submit.setEnabled(true);
					MainFrame.search.setEnabled(true);
					MainFrame.add.setEnabled(true);

				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"OPS!!!!!! Library Card Does Not Exist!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		int counter = 0;

		if (e.getActionCommand().equals("search")) {

			try {
				if (ReadWriteBook.checkbook(MainFrame.isbn.getText()) == true) {
					MainFrame.frame2.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"OPS!!!Book not found!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if (e.getActionCommand().equals("add")) {

			try {
				if (ReadWriteBook.checkCopyNumber(MainFrame.isbn.getText()) == true) {

					java.sql.Date date = new java.sql.Date(
							System.currentTimeMillis());
					java.sql.Date newDate = new java.sql.Date(
							System.currentTimeMillis() + 1209600000);

					System.out.println(MainFrame.libraryCard.getText());

					String sql = "INSERT INTO `dateBorrow`(`ISBN`, `BorrowDate`, `ReturnDate`, `LibraryCardNumber`) VALUES ( ? , ? , ? , ?)";
					PreparedStatement pst = ReadWriteBook.connect
							.prepareStatement(sql);
					pst.setString(1, MainFrame.isbn.getText());
					pst.setDate(2, date);
					pst.setDate(3, newDate);
					pst.setInt(4,
							Integer.parseInt(MainFrame.libraryCard.getText()));

					pst.execute();

					MainFrame.frame2.setVisible(true);
					MainFrame.book.setEnabled(true);

				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Book is currently full booked", "Sorry",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			counter++;
			MainFrame.frame2.setVisible(false);
		}

		if (e.getActionCommand().equals("book")) {

			int counter1 = 0;
			String street = "", zipcode = "", town = "", name = "", libraryCardNumber = "";

			try {
				Statement stmt = ReadWriteBook.connect.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT borrow.LibraryCardNumber, borrow.Name, borrow.Street, borrow.ZipCode, borrow.Town, books.Author, books.Title, books.Year, "
								+ "dateBorrow.ReturnDate FROM `dateBorrow` JOIN `borrow` ON dateBorrow.LibraryCardNumber = borrow.LibraryCardNumber JOIN "
								+ "`books` ON books.ISBN = dateBorrow.ISBN WHERE borrow.LibraryCardNumber = "
								+ libCard + ";");

				MainFrame.pnlR.removeAll();
				JLabel te = new JLabel("**Books Rented To Your Account**");
				MainFrame.pnlR.add(te);

				while (rs.next()) {

					counter1++;

					libraryCardNumber = rs.getString("LibraryCardNumber");
					name = rs.getString("Name");
					street = rs.getString("Street");
					zipcode = rs.getString("ZipCode");
					town = rs.getString("Town");
					String author = rs.getString("Author");
					String title = rs.getString("Title");
					int year = rs.getInt("Year");
					Date returnDate = rs.getDate("ReturnDate");
					MainFrame.frame3.setTitle("Card Number: "
							+ libraryCardNumber + " | Name " + name);
					JLabel tmep = new JLabel(title + ", " + author + " " + year
							+ " | Return Date: " + returnDate);
					MainFrame.pnlR.add(tmep);
					MainFrame.frame3.setVisible(true);
				}
				JLabel tmep3 = new JLabel("User Details:");
				JLabel tmep2 = new JLabel("Card Number: " + libraryCardNumber
						+ " | Name: " + name + " | Street: " + street
						+ " | ZipCode: " + zipcode + " | Town: " + town);
				MainFrame.pnlR.add(tmep3);
				MainFrame.pnlR.add(tmep2);

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
