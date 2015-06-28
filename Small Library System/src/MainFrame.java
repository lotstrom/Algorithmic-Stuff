import java.awt.*;

import javax.swing.*;

/**
 * Class creates complete interface for the program
 * 
 * @author Mustafa Hussein & Ale Lotstrom
 * @param Library
 * @param SearchOrAdd
 * @param Receipt
 * @param LogIn
 */

public class MainFrame {
	private JFrame frame = new JFrame("Musse's Library");
	static JFrame frame2 = new JFrame("Search or Add books to the cart");
	static JFrame frame3 = new JFrame("Receipt");
	static JFrame frame4 = new JFrame("Log In");

	static JLabel searchName = new JLabel("");
	static JButton submit = new JButton("Submit");
	static JButton add = new JButton("Add");
	static JButton ok = new JButton("OK");
	static JTextField libraryCard = new JTextField(
			"Enter Your Library Card Number");
	static JButton search = new JButton("Search");
	public static JTextField isbn = new JTextField(
			"Enter the ISBN for the Book");
	static JButton book = new JButton("Book");
	static JTextField dbName = new JTextField("Enter the name of the database");
	static JButton submitDb = new JButton("Submit");

	static JTextField rootPw = new JTextField("Enter the root password");
	static JButton submitPw = new JButton("Submit");
	static String password = "";

	private JPanel pnlSearch = new JPanel();
	static JPanel pnlSearchN = new JPanel();
	private JPanel pnlSearchS = new JPanel();
	private JPanel pnW = new JPanel(new GridLayout(2, 0, 0, 0));
	private JPanel pnlS = new JPanel();
	private JPanel pnlMain = new JPanel();

	static JPanel pnlR = new JPanel();
	static JPanel pnlRS = new JPanel();

	MainFrame() {
		submit.addActionListener(new Listner()); // JButton Submit
		submit.setActionCommand("submit");

		search.addActionListener(new Listner()); // JButton Search
		search.setActionCommand("search");
		search.setEnabled(false);
		book.addActionListener(new Listner()); // JButton Book
		book.setActionCommand("book");
		book.setEnabled(false);
		add.addActionListener(new Listner()); // JButton Add
		add.setActionCommand("add");
		add.setEnabled(false);
		searchName.setVisible(true);
		//
		submitPw.addActionListener(new Listner()); // JButton Search
		submitPw.setActionCommand("submitPw");
		//
		submitDb.addActionListener(new Listner()); // JButton Search
		submitDb.setActionCommand("submitDb");
		pnW.add(libraryCard);
		pnW.add(submit);
		pnW.add(isbn);
		pnW.add(search);
		pnlS.add(book);
		pnlR.add(pnlRS, BorderLayout.SOUTH);

		pnlSearchS.add(add);
		pnlSearch.add(pnlSearchN, BorderLayout.NORTH);
		pnlSearch.add(pnlSearchS, BorderLayout.SOUTH);
		pnlMain.add(pnW, BorderLayout.NORTH);
		pnlMain.add(pnlS, BorderLayout.SOUTH);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(pnlMain, BorderLayout.CENTER);
		frame2.getContentPane().setLayout(new BorderLayout());
		frame2.getContentPane().add(pnlSearch, BorderLayout.CENTER);
		frame3.getContentPane().setLayout(new BorderLayout());
		frame3.getContentPane().add(pnlR, BorderLayout.CENTER);
	}

	public void launchFrame() {
		// Display Frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 150);
		frame.setVisible(true);
		frame.setLocation(300, 200);
		frame2.setSize(600, 150);
		frame2.setLocation(300, 200);
		frame3.setSize(400, 150);
		frame3.setLocation(700, 200);

	}

	public static void main(String[] args) throws Exception {
		ReadWriteBook.OpenBook();
		ReadWriteBook.DBname = JOptionPane
				.showInputDialog("Please Enter Database Name: ");

		ReadWriteBook.ReadBook();
		MainFrame db = new MainFrame();
		db.launchFrame();

		// ReadWriteBook.DBname=
		// JOptionPane.showInputDialog("Enter the name of the database") ;
	}
}
