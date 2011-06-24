package applicationLayer;
import java.util.List;

import dataStorageLayer.BookStorageFacade;

class Borrower extends BranchUserImpl {

	
	public List<Book> getBooksBorrowed() throws Exception{
		
		List<Book> b;
		b = BookStorageFacade.getBooksByBorrower(this);		
		return b; 
	}
	
	
}
