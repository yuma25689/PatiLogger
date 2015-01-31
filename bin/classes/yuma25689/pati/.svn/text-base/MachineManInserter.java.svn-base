package yuma25689.pati;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MachineManInserter implements IInserter {

	public final String TBL_MACHINEMAN = "MachineMan";
	
	@Override
	public int insert(
			SQLiteDatabase db,
			ContentValues values
		) 
	{
		// 'のエスケープ
		String strVal = values.getAsString("MachineName");
		if( strVal != null && false=="".equals( strVal ) )
		{
			strVal = strVal.replace("'", "''");
		}	
		// 重複チェック
		Cursor c = db.query(
    			TBL_MACHINEMAN,
    			new String[]{"MachineName"},
    			"MachineName='"+strVal+"'",
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

    	if( -1 == db.insert(TBL_MACHINEMAN, "", values) )
    	{
    		return -1;
    	}
    	return 0;
	}

}
