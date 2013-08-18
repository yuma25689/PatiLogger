package yuma25689.pati;

public class MoneyInf {
	public static final long DB_NOT_EXIST_ID = -1; 
	
	private long   lngId;
	private String strDate;
	private String strTime;
	
	MoneyInf( String _strDate, String _strTime ) {
		strDate = _strDate;
		strTime = _strTime;
		setLngId(DB_NOT_EXIST_ID);			
	}
	public String getDate() {
		return strDate;
	}
	public String getTime() {
		return strTime;
	}
	public void setLngId(long lngId) {
		this.lngId = lngId;
	}
	public long getLngId() {
		return lngId;
	}
	
	@Override
	public boolean equals(Object o) {
		try{
			if( ((MoneyInf)o).getTime().equals( this.getTime() )
			&& ((MoneyInf)o).getDate().equals( this.getDate() )
			) return true;
		
		} catch( Exception e ) {
		}
			
		return false;
	}
}
