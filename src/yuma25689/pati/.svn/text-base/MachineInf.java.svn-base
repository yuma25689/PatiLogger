package yuma25689.pati;

public class MachineInf {
	private String strName;
	private String strNotes;
	
	MachineInf( String _strName, String _strNotes ) {
		strName = _strName;
		strNotes = _strNotes;
	}
	public String getName() {
		return strName;
	}
	public String getNotes() {
		return strNotes;
	}

	@Override
	public boolean equals(Object o) {
		if( 
			((MachineInf)o).getName()
			.equals( this.getName() )
		)
		{
			return true;			
		}
		return false;
	}
	@Override
	public String toString() {
		return getName();
	}
}
