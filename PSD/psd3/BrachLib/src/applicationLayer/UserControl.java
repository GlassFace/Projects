package applicationLayer;

import dataStorageLayer.UserStorageFacade;

public class UserControl {

	private BranchUser user;
	private static UserControl instance = null;
	
	private UserControl() {
		user = null;
	}
	
	public static UserControl getInstance(){
		if (instance == null)
			instance = new UserControl();
		return instance;
	}
	
	public void login(String name, String password) throws Exception{
		user = UserStorageFacade.login(name, password);
	}
	
	public void logout() throws Exception{
		if (user != null)
			user = null;
		else
			throw new Exception("No User defined in the Active Context");
		
	}

	public BranchUser getUser() {
		return user;
	}
	
	
}
