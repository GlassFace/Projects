package applicationLayer;

import dataStorageLayer.UserStorageFacade;

/**
 * An administrator have all the rights of a Keeper but can also
 * grant a user to be a keeper.
 */
public class Administrator extends Keeper {
	
	/**
	 * Change the state of a user to keeper, throw exceptions if user is already keeper or administrator
	 * and if the user is not a staff member. 
	 * @param user
	 * @throws Exception
	 */
	public void addKeeper(BranchUser user) throws Exception {
		if(user.getClass() == Keeper.class || user.getClass() == Administrator.class) {
			throw new Exception(user.getFirstName()+" "+user.getLastName()+" is already a Keeper or an Administrator");
		}
		
		if(!user.getIsStaff()) {
			throw new Exception(user.getFirstName()+" "+user.getLastName()+" is not a Staff Member");
		}
		
		UserStorageFacade usf = UserStorageFacade.getInstance();
		usf.setUserPrivilege(user.getId(), Privilege.KEEPER);
	}
	
	/**
	 * Remove the keeper status of a user.
	 * @param user
	 * @throws Exception
	 */
	public void removeKeeper(BranchUser user) throws Exception{
		
		if(user.getClass() != Keeper.class ){
			throw new Exception(user.getFirstName()+" "+user.getLastName()+" is not a keeper");	
		}
		
		UserStorageFacade usf = UserStorageFacade.getInstance();
		usf.setUserPrivilege(user.getId(), Privilege.BORROWER);
	}
}
