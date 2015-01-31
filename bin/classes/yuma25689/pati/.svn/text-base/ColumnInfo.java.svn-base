package yuma25689.pati;

import android.os.Parcel;
import android.os.Parcelable;

public class ColumnInfo implements Parcelable {

	final String SQLITE_COLUMN_TYPE_NAMES[] = {
		"TEXT",
		"NUMERIC",
		"INTEGER",
		"REAL",
		"NONE",
	};
	
	public static final int COLUMN_TYPE_TEXT = 0;
	public static final int COLUMN_TYPE_NUMERIC = 1;
	public static final int COLUMN_TYPE_INTEGER = 2;
	public static final int COLUMN_TYPE_REAL = 3;
	public static final int COLUMN_TYPE_NONE = 4;
	
	private String strColumnName;
	private String strColumnType;
	private int iColumnType;
	private int intNotNull;
	private String strDefaultType;
	private String strPreviewVal;
	private String strExtraVal1;
	private String strExtraVal2;
	
	
	public ColumnInfo() {
	}

	public ColumnInfo(ColumnInfo c) 
	{
		strColumnName = c.strColumnName;
		strColumnType = c.strColumnType;
		iColumnType = c.iColumnType;
		intNotNull = c.intNotNull;
		strDefaultType = c.strDefaultType;
		strExtraVal1 = c.strExtraVal1;
		strExtraVal2 = c.strExtraVal2;		
	}

	/*
	 * カラム情報が合っているかどうかを調べる。ただし、ExtraValについては調査しない
	 */
	public boolean equals(ColumnInfo c)
	{
		return ( 
			strColumnName.equals( c.strColumnName )
		//&& strColumnType.equals( c.strColumnType )
		&& iColumnType == c.iColumnType
		&& intNotNull == c.intNotNull
		//&& strDefaultType.equals( c.strDefaultType )
		);
		
	}
	public boolean colAndValEquals(ColumnInfo c)
	{		
		if( strColumnName.equals( c.strColumnName )
		&& iColumnType == c.iColumnType
		&& intNotNull == c.intNotNull )
		{
			if( strExtraVal1 == null )
			{
				if( c.strExtraVal1 != null )
				{
					return false;
				}
			}
			else if( false == strExtraVal1.equals( c.strExtraVal1 ) )
			{
				return false;
			}
			if( strExtraVal2 == null )
			{
				if( c.strExtraVal2 != null )
				{
					return false;
				}
			}
			else if( false == strExtraVal2.equals( c.strExtraVal2 ) )
			{
				return false;
			}
			
		}
		else
		{
			return false;
		}
		return true;
	}
	
	public void clear()
	{
		strColumnName = null;
		strColumnType = null;
		iColumnType = 0;
		intNotNull = 0;
		strDefaultType = null;
		strExtraVal1 = null;
		strExtraVal2 = null;
	}
	public boolean isEmpty()
	{
		if( strColumnName.equals( null ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// setter/getter
	public String getStrColumnName() {
		return strColumnName;
	}
	public void setStrColumnName(String strColumnName) {
		this.strColumnName = strColumnName;
	}
	public String getStrColumnType() {
		return strColumnType;
	}
	public void setStrColumnType(String strColumnType) {
		this.strColumnType = strColumnType;
		for( int i=0; i<SQLITE_COLUMN_TYPE_NAMES.length; i++)
		{
			if( SQLITE_COLUMN_TYPE_NAMES[i].equals(strColumnType.toUpperCase()) )
			{
				this.iColumnType = i;
			}
		}
	}
	public int getiColumnType() {
		return iColumnType;
	}
	public void setiColumnType(int iColumnType) {
		this.iColumnType = iColumnType;
	}
	public int getIntNotNull() {
		return intNotNull;
	}
	public void setIntNotNull(int intNotNull) {
		this.intNotNull = intNotNull;
	}
	public String getStrDefaultType() {
		return strDefaultType;
	}
	public void setStrDefaultType(String strDefaultType) {
		this.strDefaultType = strDefaultType;
	}
	public void setExtraVal1(String strExtraVal1) {
		this.strExtraVal1 = strExtraVal1;
	}
	public String getExtraVal1() {
		return strExtraVal1;
	}
	public void setExtraVal2(String strExtraVal2) {
		this.strExtraVal2 = strExtraVal2;
	}
	public String getExtraVal2() {
		return strExtraVal2;
	}

	public void setPreviewVal(String strPreviewVal) {
		this.strPreviewVal = strPreviewVal;
	}

	public String getPreviewVal() {
		return strPreviewVal;
	}

	// Parcelの実装・・・めんどくせえ・・・この仕組み考えた奴超頭悪い・・・。
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(strColumnName);
		dest.writeString(strColumnType);
		dest.writeInt(iColumnType);
		dest.writeInt(intNotNull);
		dest.writeString(strDefaultType);
		dest.writeString(strExtraVal1);
		dest.writeString(strExtraVal2);
	}
    private ColumnInfo(Parcel in) {
    	strColumnName = in.readString();
    	strColumnType = in.readString();
    	iColumnType = in.readInt();
    	intNotNull = in.readInt();
    	strDefaultType = in.readString();
    	strExtraVal1 = in.readString();
    	strExtraVal2 = in.readString();
    }
    public static final Parcelable.Creator<ColumnInfo> CREATOR
		    = new Parcelable.Creator<ColumnInfo>() {
		public ColumnInfo createFromParcel(Parcel in) {
		    return new ColumnInfo(in);
		}
		
		public ColumnInfo[] newArray(int size) {
		    return new ColumnInfo[size];
		}
	};
}
