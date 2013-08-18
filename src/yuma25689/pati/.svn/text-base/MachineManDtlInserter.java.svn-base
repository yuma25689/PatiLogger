package yuma25689.pati;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MachineManDtlInserter implements IInserter {

	public final String TBL_NAME = "ParlorMan";
	
	@Override
	public int insert(
			SQLiteDatabase db,
			ContentValues values
	) {		
		// 重複チェック
    	Cursor c = db.query(
    			TBL_NAME,
    			new String[]{"ParlorName"},
    			"ParlorName='"+values.getAsString("ParlorName")+"'",
    			null,
    			null,
    			null,
    			null,
    			null);
    	if( c.getCount() > 0 )
    	{
    		return R.string.MSG_DUP_ERR;
    	}
    	//　重複なし
    	if( -1 == db.insert(TBL_NAME, "", values) )
    	{
    		return -1;
    	}
    	return 0;
	}

}
