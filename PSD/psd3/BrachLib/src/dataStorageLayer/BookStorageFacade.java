package dataStorageLayer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import applicationLayer.BranchBook;
import applicationLayer.BranchBookImpl;
import applicationLayer.Keeper;


import uk.ac.gla.dcs.psd3.ay2009.project.CentralLibClient;
import uk.ac.gla.dcs.psd3.ay2009.project.CentralLibClientImpl;
import uk.ac.gla.dcs.psd3.ay2009.project.model.Book;
import uk.ac.gla.dcs.psd3.ay2009.project.model.BookDescription;

public class BookStorageFacade {
	private static BookStorageFacade instance = null;
	private CentralLibClient libClient = null;
	// *ToDo* Change this to the config file
	private static String dbPropertiesFN = "config/test/db.properties";
	private BookStorageFacade()
	{
		libClient = new CentralLibClientImpl(dbPropertiesFN);
	}
	public static BookStorageFacade getInstance() {
		if (instance == null)
			instance = new BookStorageFacade();
		
		return instance;
	}
	public BranchBook getBookBy(String key, Object value)
	{
		if (key.equalsIgnoreCase("id")) {
			//	REturn book with ID blah
		}
	}
	
	public List<BranchBook> getBooksBy(String key, Object value) throws Exception
	{
		if (key.equalsIgnoreCase("isbn"))
		{
			//	Return book by ISBN
		} else if (key.equalsIgnoreCase("publisher")) {
			//	Return book by Publisher
		} else if (key.equalsIgnoreCase("published")) {
		} else if (key.equalsIgnoreCase("title")) {
			
		} else if (key.equalsIgnoreCase("author")) {
			
		} else if (key.equalsIgnoreCase("state")) {
			
		} else if (key.equalsIgnoreCase("borrower")) {
			
		} else if (key.equalsIgnoreCase("state")) {
			
		} else if (key.equalsIgnoreCase("keeper")) {
			List<Integer> li = BranchStorageInterface.getBooksByKeeper(((Keeper) value).getId());
			List<BranchBook> bb = new ArrayList<BranchBook>();
			for (Integer l : li)
			{
				Book b = libClient.getBookByID(l);
				BranchBook bbsd = new BranchBookImpl();
				if (!b.equals(null)){
					BookDescription bd = b.getBookDescription();
					bbsd.setID(bd.getID());
					bbsd.setAuthors(bd.getAuthors());
					bbsd.setISBN(bd.getISBN());
					bbsd.setKeeper((Keeper) value);
					bbsd.setPublished(bd.getPublished());
					bbsd.setPublisher(bd.getPublisher());
					bbsd.setBorrower(BranchStorageInterface.getBookBorrower(bd.getID()));
					
				}
			}
		} else {
			throw new Exception("Unknown Key!");
		}
	}
}
