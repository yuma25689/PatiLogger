package yuma25689.pati;

import java.io.Serializable;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class TableOpenInfo 
	implements Parcelable, Serializable {
	
	private static final long serialVersionUID = -3591713552368419805L;

	public TableOpenInfo() {
	}

	private String TblName = "";
	private String TmpTblName = "";
	private String ColumnsCreateText = "";
	private String DBName = "";
	private SQLiteDatabase.CursorFactory CursorFactory = null;
	private int DBVer = 1;
	private String strExcptColumnSpec = "";
	private String strWherePhrase = "";

	public String getTblName() {
		return TblName;
	}
	public String getTmpTblName() {
		return TmpTblName;
	}
	public void setTblName(String tblName) {
		TblName = tblName;
	}
	public void setTmpTblName(String tmpTblName) {
		TmpTblName = tmpTblName;
	}
	public String getColumnsCreateText() {
		return ColumnsCreateText;
	}
	public void setColumnsCreateText(String columnsCreateText) {
		ColumnsCreateText = columnsCreateText;
	}
	public String getDBName() {
		return DBName;
	}
	public void setDBName(String dBName) {
		DBName = dBName;
	}
	public SQLiteDatabase.CursorFactory getCursorFactory() {
		return CursorFactory;
	}
	public void setCursorFactory(SQLiteDatabase.CursorFactory cursorFactory) {
		CursorFactory = cursorFactory;
	}
	public int getDBVer() {
		return DBVer;
	}
	public void setDBVer(int dBVer) {
		DBVer = dBVer;
	}
	public void setWherePhrase(String strWherePhrase) {
		this.strWherePhrase = strWherePhrase;
	}
	public String getWherePhrase() {
		return strWherePhrase;
	}	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(TblName);
		out.writeString(ColumnsCreateText);
		out.writeString(DBName);
		out.writeValue(CursorFactory);
		out.writeInt(DBVer);
		out.writeString(strExcptColumnSpec);
		out.writeString(strWherePhrase);
	}

    public static final Parcelable.Creator<TableOpenInfo> CREATOR
    = new Parcelable.Creator<TableOpenInfo>() {
    	public TableOpenInfo createFromParcel(Parcel in) {
    		return new TableOpenInfo(in);
    	}

    	public TableOpenInfo[] newArray(int size) {
    		return new TableOpenInfo[size];
    	}
    };

    private TableOpenInfo(Parcel in) {
    	TblName = in.readString();
    	ColumnsCreateText = in.readString();
    	DBName = in.readString();
    	CursorFactory = (SQLiteDatabase.CursorFactory)in.readValue(SQLiteDatabase.CursorFactory.class.getClassLoader());
    	DBVer = in.readInt();
    	strExcptColumnSpec = in.readString();
    	strWherePhrase = in.readString();
    }
	public void setExcptColumnSpec(String strExcptColumnSpec) {
		this.strExcptColumnSpec = strExcptColumnSpec;
	}
	public String getExcptColumnSpec() {
		return strExcptColumnSpec;
	}
}
