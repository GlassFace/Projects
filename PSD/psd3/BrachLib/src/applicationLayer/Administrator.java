package applicationLayer;

import dataStorageLayer.UserStorageFacade;

class Administrator extends Keeper{
	
	public void addKeeper(BranchUser user)throws Exception{
	
		if(user.getPrivilege() == Privilege.KEEPER || user.getPrivilege() == Privilege.ADMINISTRATOR){
			throw new Exception(user.getFirstName()+" "+user.getLastName()+" is already a Keeper or an Administrator");
		}
		
		if(!user.getIsStaff()){
			throw new Exception(user.getFirstName()+" "+user.getLastName()+" is not a Staff Member");
		}
		
		UserStorageFacade.setUserPrivilege(user.getId(), Privilege.KEEPER);
	}
	
	public void removeKeeper(BranchUser user) throws Exception{
		
		if(user.getPrivilege() != Privilege.KEEPER ){
			throw new Exception(user.getFirstName()+" "+user.getLastName()+" is not a keeper");	
		}
		
		UserStorageFacade.setUserPrivilege(user.getId(), Privilege.BORROWER);
	}
}
