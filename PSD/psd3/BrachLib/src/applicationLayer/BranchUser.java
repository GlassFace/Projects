package applicationLayer;

public interface BranchUser {
	
	public String getFirstName();
	
	public void setFirstName(String fn);
	
	public String getLastName();
	
	public void setLastName(String ln);
	
	public boolean getIsStaff();
	
	public void setIsStaff(boolean v);
	
	public int getId();
	
	public void setId(int id);
	
	public Privilege getPrivilege();
	
	public void setPrivilege(Privilege p);	
}