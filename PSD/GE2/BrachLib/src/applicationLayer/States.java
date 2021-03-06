package applicationLayer;

public enum States {
	REQUESTRETURN (0),
	LENT (1),
	WITHKEEPER (2),
	INTRANSITTOCENTRAL (3),
	INTRANSITTOBRANCH (4),
	INCENTRAL (5);
	
	private final int id;
	States(int i) {
		this.id = i;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static States getStateById(int i) {
		for (States s : States.values()) {
			if (s.id == i)
				return s;
		}
		return States.INCENTRAL;
	}
	
	public String toString() {
		switch (this) {
			case REQUESTRETURN : return "Request Return";
			case LENT : return "Lent";
			case WITHKEEPER : return "With Keeper";
			case INTRANSITTOCENTRAL : return "In Transit to central";
			case INTRANSITTOBRANCH : return "In Transit to branch";
			default : return "In Central library";
		}
	}
}
