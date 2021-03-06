package dataStorageLayer;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import applicationLayer.Privilege;
import applicationLayer.States;

public class BranchStorageInterface {
	private static BranchStorageInterface instance = null;
	private final static String database = "jdbc:sqlite:branchlib.db"; 
	private static Connection conn;
	
	/**
	 * Private Constructor
	 * @throws Exception
	 */
	private BranchStorageInterface() throws Exception {
		Class.forName("org.sqlite.JDBC");		
		conn = DriverManager.getConnection(database);
	}
	
	/**
	 * Get or create a branch Storage connection
	 * @return Singleton instance of the branch storage interface
	 * @throws Exception
	 */
	public static BranchStorageInterface getInstance() throws Exception {
		if (instance == null)
			instance = new BranchStorageInterface();
		return instance;
	}
	
	/**
	 * Return all books associated with a keeper
	 * @param userID
	 * @return a list of book id kept by the keeper
	 * @throws Exception
	 */
	public List<Integer> getBooksByKeeper(int userID) throws Exception {
	    
	    List<Integer> alb = new ArrayList<Integer>(); 
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT bookid FROM books WHERE keeperid=" + userID + ";");
	    rs.close();
	    while (rs.next()) {
	    	alb.add(rs.getInt("bookid"));
	    }
	    return alb;
	}
	
	public List<Integer> getBooksByBorrower(int UserID) throws Exception {
		List<Integer> alb = new ArrayList<Integer>();
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT bookid FROM borrowedbooks WHERE userid=" + UserID + ";");
	    rs.close();
	    while(rs.next()) {
	    	alb.add(rs.getInt("userid"));
	    }
	    return alb;
	}
	
	/**
	 * 
	 * @param bookID
	 * @return userid of keeper
	 * @throws Exception
	 */
	public int getBookKeeper(int bookID) throws Exception {
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT keeperid FROM books WHERE bookid=" + bookID + ";");
	    rs.next();
	    int kid = rs.getInt("keeperid");
	    rs.close();
	    return kid;
	}
	
	/**
	 * 
	 * @param bookID
	 * @param Keeper k
	 * @throws Exception
	 */
	public void setBookKeeper(int bookID, int keeperid) throws Exception {
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("UPDATE books SET keeperid=" + keeperid + " WHERE bookid=" + bookID + ";");
		rs.close();
	}
	
	/**
	 * 
	 * @param bookID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int getBookBorrower(int bookID) throws Exception	{
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT userid FROM borrowedbooks WHERE bookid=" + bookID + ";");
	    rs.next();
	    int userid = rs.getInt("userid");
	    rs.close();
	    return userid;
	}
	
	/**
	 * 
	 * @param bookID
	 * @param b
	 * @throws Exception
	 */
	public void setBookBorrower(int bookID, int borrowerId) throws Exception {
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("UPDATE books SET keeperid=" + borrowerId + " WHERE bookid=" + bookID + ";");
		rs.close();
	}
	
	/**
	 * 
	 * @param userID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Privilege getUserPrivilege(int userID) throws Exception {
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("SELECT privilege FROM users WHERE id=" + userID + ";");
		rs.next();
		int p = rs.getInt("privilege");
		rs.close();
		return Privilege.getPrivilegeById(p);
	}
	
	/**
	 * 
	 * @param userId
	 * @param p
	 * @throws Exception
	 */
	public void setUserPrivilege(int userId, Privilege p) throws Exception {
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("UPDATE privilege SET privilege=" + p.getId() + "WHERE id=" + userId + ";");
		rs.close();
	}
	
	/**
	 * 
	 * @param bookID
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public States getBookState(int bookID) throws Exception	{
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("SELECT state FROM books WHERE bookid=" + bookID + ";");
		
		rs.next();
		States s = States.getStateById(rs.getInt("state"));
		rs.close();
		return s;
	}
	
	public void setBookState(int BookID, States s) throws Exception {
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("UPDATE books SET state=" + s.getId() + ";");
		rs.close();
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<Integer> getBooksByState(States s) throws Exception	{
		   List<Integer> lbb = new ArrayList<Integer>();
		   Statement stat = conn.createStatement();
		   ResultSet rs = stat.executeQuery("SELECT bookID FROM books WHERE state=" + s + ";");
		   while(rs.next())
		   {
			   int bid = rs.getInt("bookID");
			   lbb.add(bid);	   
		   }
		   return lbb;
	}
	
	/**
	 * 
	 * @param privilege p
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<Integer> getUsersByPrivilege(Privilege p) throws Exception {
		
		List<Integer> li = new ArrayList<Integer>();
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("SELECT id FROM users WHERE privilege=" + p.getId() + " ORDER BY id ASC;");
	    while (rs.next()) {
	    	li.add(rs.getInt("id"));
	    }
	    rs.close();
	    return li;
	}
}
