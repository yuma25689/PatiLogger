package yuma25689.pati;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MakerManInserter implements IInserter {

	public final String TBL_NAME = "MakerMan";
	
	@Override
	public int insert(
			SQLiteDatabase db,
			ContentValues values
	) {
		String strVal = values.getAsString("MakerName");
		if( strVal != null && false=="".equals( strVal ) )
		{
			strVal = strVal.replace("'", "''");
		}		
		// 重複チェック
    	Cursor c = db.query(
    			TBL_NAME,
    			new String[]{"MakerName"},
    			"MakerName='"+strVal+"'",
    			null,
    			null,
    			null,
    			null,
    			null);
    	if( c.getCount() > 0 )
    	{
    		c.close();
    		return R.string.MSG_DUP_ERR;
    	}
    	c.close();
    	//　重複なし
    	if( -1 == db.insert(TBL_NAME, "", values) )
    	{
    		return -1;
    	}
    	return 0;
	}

}
