package dataStorageLayer;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import applicationLayer.Borrower;
import applicationLayer.BranchBook;
import applicationLayer.Keeper;
import applicationLayer.Privilege;
import applicationLayer.States;
import uk.ac.gla.dcs.psd3.ay2009.project.CentralLibClient;
import uk.ac.gla.dcs.psd3.ay2009.project.CentralLibClientImpl;
import uk.ac.gla.dcs.psd3.ay2009.project.model.User;
import uk.ac.gla.dcs.psd3.ay2009.project.model.UserImpl;


public class BranchStorageInterface {

	public static List<Integer> getBooksByKeeper(int userID) throws ClassNotFoundException, SQLException {
	    Class.forName("org.sqlite.JDBC");
	    List<BranchBook> alb = new ArrayList<BranchBook>();
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT * FROM books WHERE keeperid=" + userID + ";");
	    while (rs.next()) {
	    	//alb.add(new Book());
	    }
	    return alb;
	}
	
	public static int getBookKeeper(int bookID) throws ClassNotFoundException, SQLException {
	    Class.forName("org.sqlite.JDBC");
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT keeperid FROM books WHERE bookid=" + bookID + ";");
	    while (rs.next()) {
	    	return rs.getInt("keeperid");
	    }
	    rs.close();
	    conn.close();
	    return -1;
	}
	
	public static void setBookKeeper(int bookID, Keeper k) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("UPDATE books SET keeperid=" + k.getId() + " WHERE bookid=" + bookID + ";");
		rs.close();
		conn.close();
	}
	
	public static Borrower getBookBorrower(int bookID) throws ClassNotFoundException, SQLException
	{
	    Class.forName("org.sqlite.JDBC");
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT userid FROM borrowedbooks WHERE bookid=" + bookID + ";");
	    while (rs.next()) {
	    	int userid = rs.getInt("userid");
	    	
	    }
	    rs.close();
	    conn.close();
	    return -1;
	}
	
	public static void setBookBorrower(int bookID, Borrower b) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("UPDATE books SET keeperid=" + b.getId() + " WHERE bookid=" + bookID + ";");
		rs.close();
		conn.close();
	}
	
	public static Privilege getUserPrivilege(int userID) throws ClassNotFoundException, SQLException
	{
	    Class.forName("org.sqlite.JDBC");
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT privilege FROM users WHERE id=" + userID + ";");
	    while (rs.next()) {
	    	   switch (rs.getInt("privilege"))
			   {
				   case 0:
					   return Privilege.LIBRARIAN;
				   case 1:
					   return Privilege.USER;
				   case 2:
					   return Privilege.BORROWER;
				   case 3:
					   return Privilege.KEEPER;
				   case 4:
					   return Privilege.ADMINISTRATOR;
				   default:
					   return null;
			   }
	    }
	    rs.close();
	    conn.close();
	    return null;
	}
	
	public static States getBookState(int bookID) throws SQLException, ClassNotFoundException
	{
		   Class.forName("org.sqlite.JDBC");
		   Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
		   Statement stat = conn.createStatement();
		   ResultSet rs = stat.executeQuery("SELECT state FROM books WHERE bookid=" + bookID + ";");
		   while (rs.next()) {
		
			   switch (rs.getInt("state"))
			   {
				   case 0:
					   return States.REQUESTRETURN;
				   case 1:
					   return States.LENT;
				   case 2:
					   return States.WITHKEEPER;
				   case 3:
					   return States.INTRANSITTOCENTRAL;
				   case 4:
					   return States.INTRANSITTOBRANCH;
				   case 5:
					   return States.INCENTRAL;
				   default:
					   return null;
			   }
			   
		   }
		   rs.close();
		   conn.close();
		   return null;
	}
	
	public static List<Integer> getBookByState(States s) throws SQLException, ClassNotFoundException
	{
		   List<Integer> lbb = new ArrayList<Integer>();
		   Class.forName("org.sqlite.JDBC");
		   Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
		   Statement stat = conn.createStatement();
		   ResultSet rs = stat.executeQuery("SELECT bookID FROM books WHERE state=" + s + ";");
		   while(rs.next())
		   {
			   int bid = rs.getInt("bookID");
			   lbb.add(bid);	   
		   }
		   return lbb;
	}
	
	public static List<Integer> getUsersByPrivilege(Privilege privilege) throws SQLException, ClassNotFoundException
	{
		List<Integer> li = new ArrayList<Integer>();
	    Class.forName("org.sqlite.JDBC");
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:branchlib.db");
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT id FROM users WHERE privilege=" + privilege + " ORDER BY id ASC;");
	    while (rs.next()) {
	    	li.add(rs.getInt("id"));
	    }
	    rs.close();
	    conn.close();
	    return li;
	}
}
