package yuma25689.pati;


import android.content.Context;

public class PatiManDBHelper extends DBHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int TBL_CNT = 5;
	public static final String dbName = "PatiLogger.db";
	private static final int dbVer = 10;
	
	public PatiManDBHelper(Context context) {
		// このデータベースにある全てのテーブルの情報を格納	
		super(context,dbName,dbVer);
		TableOpenInfo arrTblInf[] = new TableOpenInfo[TBL_CNT];

		arrTblInf[0] = new TableOpenInfo();
		arrTblInf[0].setTblName(context.getString(R.string.MakerTblName));
		arrTblInf[0].setColumnsCreateText( context.getString(R.string.MakerTblDef) );

		arrTblInf[1] = new TableOpenInfo();
		arrTblInf[1].setTblName(context.getString(R.string.McnTblName));
		arrTblInf[1].setColumnsCreateText( context.getString(R.string.McnTblDef) );
		
		arrTblInf[2] = new TableOpenInfo();
		arrTblInf[2].setTblName(context.getString(R.string.McnDtlTblName));
		arrTblInf[2].setColumnsCreateText( context.getString(R.string.McnDtlTblDef) );

		arrTblInf[3] = new TableOpenInfo();
		arrTblInf[3].setTblName(context.getString(R.string.ParlorTblName));
		arrTblInf[3].setColumnsCreateText( context.getString(R.string.ParlorTblDef) );

		arrTblInf[4] = new TableOpenInfo();
		arrTblInf[4].setTblName(context.getString(R.string.MoneyTblName));
		arrTblInf[4].setColumnsCreateText( context.getString(R.string.MoneyTblDef) );

		setTblInf( arrTblInf );
	}
}
