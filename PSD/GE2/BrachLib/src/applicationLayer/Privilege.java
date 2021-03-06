package applicationLayer;


public enum Privilege {
	LIBRARIAN (0),
	USER (1),
	BORROWER (2),
	KEEPER (3),
	ADMINISTRATOR (4);
	
	private final int id;
	private Privilege(int i) {
		this.id = i;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static Privilege getPrivilegeById(int i) {
		for (Privilege s : Privilege.values()) {
			if (s.id == i)
				return s;
		}
		return Privilege.USER;
	}
	
	public String toString() {
		switch (this) {
			case LIBRARIAN : return "Librarian";
			case USER : return "User";
			case BORROWER : return "Borrower";
			case KEEPER : return "Keeper";
			case ADMINISTRATOR : return "Administrator";
			default : return "User";
		}
	}
}