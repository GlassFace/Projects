package applicationLayer;

public interface BranchBook	{
	
	public int getID();
	
	public void setID(int id);
	
	public String getTitle();
	
	public void setTitle(String title);
	
	public String getAuthors();
	
	public void setAuthors(String authors);
	
	public String getPublished();
	
	public void setPublished(String published);
	
	public String getPublisher();
	
	public void setPublisher(String publisher);
	
	public String getISBN();
	
	public void setISBN(String isbn);
	
	public States getState();
	
	public void setState(States state);
	
	public Borrower getBorrower();
	
	public void setBorrower(Borrower borrower);
	
	public Keeper getKeeper();
	
	public void setKeeper(Keeper keeper);
}
