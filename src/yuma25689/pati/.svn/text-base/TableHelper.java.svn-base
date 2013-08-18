package yuma25689.pati;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TableHelper {

	private SQLiteOpenHelper db;
	private TableOpenInfo tblInf;
	//private final String TMP_PREFIX = "temp_";
	/*
	private String strTblName = "";
	private String strColumnsCreateText = "";
	@SuppressWarnings("unused")
	private String strDBName = "";
	@SuppressWarnings("unused")
	private SQLiteDatabase.CursorFactory factory = null;
	@SuppressWarnings("unused")
	private int iDBVer = 1;
	*/
	/**
	 * コンストラクタ
	 *
	 * @param context
	 * @param strTblName_
	 * @param strColumnsCreateText_
	 * @param factory_
	 * @param strDBName_
	 * @param iDBVer_
	 * @return なし
	 */
	public TableHelper(Context context,
			String strTblName_, 
			String strColumnsCreateText_,
			SQLiteDatabase.CursorFactory factory_,
			String strDBName_, int iDBVer_ ) {
		tblInf.setTblName(strTblName_);
		tblInf.setDBName( strDBName_ );
		tblInf.setColumnsCreateText( strColumnsCreateText_ );
		tblInf.setCursorFactory( factory_ );
		tblInf.setDBVer( iDBVer_ );
		/*
		strTblName = strTblName_;
		*/
	}
	public TableHelper(Context context,
			TableOpenInfo tblInf_) {
		tblInf = tblInf_;
	}
	public void setDb(SQLiteOpenHelper db) {
		this.db = db;
	}
	public SQLiteOpenHelper getDb() {
		return db;
	}
}
