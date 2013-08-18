package yuma25689.pati;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TableControler {

	String strTblName = null;
	DBHelper dbHelper = null;
	
	final int TABLE_INF_INDEX_CID = 0;
	final int TABLE_INF_INDEX_NAME = 1;
	final int TABLE_INF_INDEX_TYPE = 2;
	final int TABLE_INF_INDEX_NOTNULL = 3;
	final int TABLE_INF_INDEX_DEFVAL = 4;
	

	public TableControler(String strTblName_,DBHelper dbHelper_) {
		strTblName = strTblName_;
		dbHelper = dbHelper_;
	}	

	// TODO: 後で別のクラスに（共通クラスを作成して）移す
	public static String getFmtMonth( String strDate )
	{
		return
		strDate.substring(0,4) +
		"/" +
		strDate.substring(4,6);
	}
	public static String getUnfmtMonth( String strFmtDate )
	{
		return strFmtDate.replace( "/", "" );
	}
	public static String getFmtDate( String strDate )
	{
		return
		strDate.substring(0,4) +
		"/" +
		strDate.substring(4,6) +
		"/" +
		strDate.substring(6,8);
	}
	public static String getFmtTime( String strTime )
	{
		return
		strTime.substring(0,2) +
		":" +
		strTime.substring(2,4);
	}

	/**
	 * getMcnNameFromMcnId
	 */
	public static String getMcnNameFromMcnId( Context ctx, DBHelper dbHelper, String strMcnId )
	{
		String strRet = "";

    	// queryでSELECTを実行
    	String[] columns = {"_id","MachineName"};
    	String selection = "_id = " + strMcnId;
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor c = db.query(ctx.getString(R.string.McnTblName),
    			columns, selection, null, null, null, null);
    	c.moveToFirst();
    	for( int i=0; i < c.getCount(); i++ )
    	{
    		if( c.isNull(1) == false )
    			strRet = c.getString(1);
    	}
    	c.close();
    	return strRet;
	}
	/**
	 * getMcnNameFromMcnId
	 */
	public static String getPatiTypeIdFromMcnId( Context ctx, DBHelper dbHelper, String strMcnId )
	{
		String strRet = "";

    	// queryでSELECTを実行
    	String[] columns = {"_id","PatiTypeId"};
    	String selection = "_id = " + strMcnId;
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor c = db.query(ctx.getString(R.string.McnTblName),
    			columns, selection, null, null, null, null);
    	c.moveToFirst();
    	for( int i=0; i < c.getCount(); i++ )
    	{
    		if( c.isNull(1) == false )
    			strRet = c.getString(1);
    	}
    	c.close();
    	return strRet;
	}
	/**
	 * getMcnIdFromMcnName
	 */
	public static String getMcnIdFromMcnName( Context ctx, DBHelper dbHelper, String strMcnName )
	{
		String strRet = "-1";

    	// queryでSELECTを実行
    	String[] columns = {"_id","MachineName"};
    	String selection = "MachineName='" + strMcnName + "'";
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor c = db.query(ctx.getString(R.string.McnTblName),
    			columns, selection, null, null, null, null);
    	c.moveToFirst();
    	for( int i=0; i < c.getCount(); i++ )
    	{
    		if( c.isNull(0) == false )
    			strRet = c.getString(0);
    	}
    	c.close();
    	return strRet;
	}
	/**
	 * getMcnIdFromMcnName
	 */
	public static ArrayList<String> getMcnIdsFromMakerName( Context ctx, 
			DBHelper dbHelper, String strName )
	{
		ArrayList<String> lstRet = new ArrayList<String>();
		String strRet = "-1";

		String strSrchMakerId = "-1";
		String selection = "";
    	// 暫定対応 本当は名前で見るべきでない
		if( false == strName.equals( ctx.getString(R.string.unknown)) )
		{
			strSrchMakerId = getMakerIdFromMakerName(ctx, dbHelper, strName );
			selection = "MakerId=" + strSrchMakerId;
		}
		else
		{
			selection = "( MakerId=-1 or MakerId is null )";
		}
		
    	// queryでSELECTを実行
    	String[] columns = {"_id","MachineName"};
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor c = db.query(ctx.getString(R.string.McnTblName),
    			columns, selection, null, null, null, null);
    	c.moveToFirst();
    	for( int i=0; i < c.getCount(); i++ )
    	{
    		//if( c.isNull(0) == false )
    			strRet = c.getString(0);
    			lstRet.add(strRet);
    			c.moveToNext();
    	}
    	c.close();
    	return lstRet;
	}
	/**
	 * getParlorNameFromParlorId
	 */
	public static String getParlorNameFromParlorId( Context ctx, DBHelper dbHelper, String strParlorId )
	{
		String strRet = "";

    	// queryでSELECTを実行
    	String[] columns = {"_id","ParlorName"};
    	String selection = "_id = " + strParlorId;
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	try
    	{
	    	Cursor c = db.query(ctx.getString(R.string.ParlorTblName),
	    			columns, selection, null, null, null, null);
	    	c.moveToFirst();
	    	for( int i=0; i < c.getCount(); i++ )
	    	{
	    		if( c.isNull(1) == false )
	    			strRet = c.getString(1);
	    	}
	    	c.close();
    	} catch( Exception ex )
    	{
    		ex.printStackTrace();
    	
    	}
    	return strRet;
	}
	/**
	 * getParlorIdFromParlorName
	 */
	public static String getParlorIdFromParlorName( Context ctx,
			DBHelper dbHelper, String strParlorName )
	{
		String strRet = "-1";

    	// queryでSELECTを実行
    	String[] columns = {"_id","ParlorName"};
    	String selection = "ParlorName='" + strParlorName + "'";
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	try
    	{
	    	Cursor c = db.query(ctx.getString(R.string.ParlorTblName),
	    			columns, selection, null, null, null, null);
	    	c.moveToFirst();
	    	for( int i=0; i < c.getCount(); i++ )
	    	{
	    		if( c.isNull(0) == false )
	    			strRet = c.getString(0);
	    	}
	    	c.close();
    	} catch( Exception e ) {
    		
    		e.printStackTrace();
    	}
    	return strRet;
	}

	/* テーブルの情報を取得する
	 * 
	 */
	public ArrayList<ColumnInfo> getTableInfo(String strWhere) {

		ArrayList<ColumnInfo> arRet = new ArrayList<ColumnInfo>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("pragma table_info(" + strTblName + ")",null);
		
		SQLiteDatabase db2 = null;
		Cursor c2 = null;
		try {
			if( strWhere != null && strWhere.length() > 0 ) {
				db2 = dbHelper.getReadableDatabase();
				// ---- Where句がある場合、データを取得しておく]
				c2 = db2.query(strTblName, null, strWhere, null, null, null, null);
				c2.moveToFirst(); // 必ず1行見つからないといけない少々危険
			}
			
			int rowcnt = c.getCount();
			int colcnt = c.getColumnCount();
			c.moveToFirst();
			for (int i = 0; i < rowcnt; i++) {

				// row1個にカラム１つの情報が入っている
				// カラムの情報を初期化
				ColumnInfo cinf = new ColumnInfo();
				cinf.clear();

				for (int colidx = 0; colidx < colcnt; colidx++) {
					//Log.d(c.getColumnName(colidx), i + " : " + c.getString(colidx) );
	
					switch( colidx ) {
					case TABLE_INF_INDEX_NAME:
						cinf.setStrColumnName(c.getString(colidx));
						if( c2 != null)
						{
							// 既にレコードがある場合、その値をここでPreviewValとして取得するものとする
							int iDx = c2.getColumnIndex(cinf.getStrColumnName());
							cinf.setPreviewVal( c2.getString(iDx) );
						}
						break;
					case TABLE_INF_INDEX_TYPE:
						cinf.setStrColumnType(c.getString(colidx));
						break;
					case TABLE_INF_INDEX_NOTNULL:
						cinf.setIntNotNull(Integer.parseInt(c.getString(colidx)));
						break;
					case TABLE_INF_INDEX_DEFVAL:
						cinf.setStrDefaultType(c.getString(colidx));
						break;
					default:
						// 処理無し
						break;
					}
				}
				// 格納された情報があれば、カラムの情報を追加する
				if( !cinf.isEmpty() )
				{
					arRet.add( cinf );
				}
	            c.moveToNext();
	         }
		} finally {
			if(c != null ) c.close();
			if(c2 != null ) c2.close();
			//if(db != null ) db.close();
			//if(db2 != null ) db2.close();			
		}

         return arRet;
	}
	/**
	 * getMakerIdFromMcnName
	 */
	public static String getMakerIdFromMakerName( Context ctx, DBHelper dbHelper, String strName )
	{
		String strRet = "-1";

    	// queryでSELECTを実行
    	String[] columns = {"_id",ctx.getString(R.string.MakerTblMainClmnName)};
		if( strName != null && false=="".equals( strName ) )
		{
			strName = strName.replace("'", "''");
		}    	
    	String selection = ctx.getString(R.string.MakerTblMainClmnName) + "='" + strName + "'";
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor c = db.query(ctx.getString(R.string.MakerTblName),
    			columns, selection, null, null, null, null);
    	c.moveToFirst();
    	for( int i=0; i < c.getCount(); i++ )
    	{
    		if( c.isNull(0) == false )
    			strRet = c.getString(0);
    	}
    	c.close();
    	return strRet;
	}	
	/**
	 * getMakerNameFromMakerId
	 */
	public static String getMakerNameFromMakerId( Context ctx, DBHelper dbHelper, String strId )
	{
		String strRet = ctx.getString(R.string.noset);//unknown);//"";

    	// queryでSELECTを実行
    	String[] columns = {"_id", ctx.getString(R.string.MakerTblMainClmnName)};
    	String selection = "_id = " + strId;
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	try
    	{
	    	Cursor c = db.query(ctx.getString(R.string.MakerTblName),
	    			columns, selection, null, null, null, null);
	    	c.moveToFirst();
	    	for( int i=0; i < c.getCount(); i++ )
	    	{
	    		if( c.isNull(1) == false 
	    		&& c.getString(1).length() > 0 )
	    	    {
	    	    	strRet = c.getString(1);
	    	    }
	    	    //else
	    	    //{
	    	    //	strRet = ctx.getString(R.string.unknown);
	    	    //}
	    	}
	    	c.close();
    	} catch( Exception ex )
    	{
    		ex.printStackTrace();
    	
    	}
    	return strRet;
	}
	/**
	 * getMakerNameFromMcnId
	 */
	public static String getMakerNameFromMcnId( Context ctx, DBHelper dbHelper, String strMcnId )
	{
		String strRet = ctx.getString(R.string.unknown);//"";

    	// queryでSELECTを実行
    	String[] columns = {"_id",ctx.getString(R.string.McnTblSubClmnName2)};
    	String selection = "_id = " + strMcnId;
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor c = db.query(ctx.getString(R.string.McnTblName),
    			columns, selection, null, null, null, null);
    	c.moveToFirst();
    	for( int i=0; i < c.getCount(); i++ )
    	{
    		if( c.isNull(1) == false 
    		&& c.getString(1).length() > 0 )
    		{
    			strRet = getMakerNameFromMakerId( ctx, dbHelper, c.getString(1));
    		}
    		//else
    		//{
    		//	strRet = ctx.getString(R.string.unknown);
    		//}
    	}
    	c.close();
    	return strRet;
	}

	
}
