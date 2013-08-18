package yuma25689.pati;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MoneyManInserter implements IInserter {

	public final String TBL_NAME = "MoneyMan";
	
	@Override
	public int insert(
			SQLiteDatabase db,
			ContentValues values
	) {
		String strWorkTime = values.getAsString("WorkTime");
		if(strWorkTime==null || strWorkTime.length() < 1 )
		{
			strWorkTime = "0000";
			values.remove("WorkTime");
			values.put("WorkTime", strWorkTime);
		}
		// 重複チェック
    	Cursor c = db.query(
    			TBL_NAME,
    			new String[]{"WorkDate"},
    			"WorkDate='"+values.getAsString("WorkDate")+
    			"' and WorkTime='" + strWorkTime+"'"+
    			" and CashFlow="+values.getAsString("CashFlow")+
    			// ↓これは変わる可能性が・・・
    			//" and McnId=" + values.getAsString("McnId")+
    			//" and ParlorId=" + values.getAsString("ParlorId")+
    			" and InsDatetime='" + values.getAsString("InsDatetime")+ "'"
    			,
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
		//try
		//{
    	if( -1 == db.insert(TBL_NAME, null, values) )
    	{	
    		return -1;
    	}
		//}catch(Exception ex )
		//{
		//	return -1;
		//}
		
    	return 0;
	}

}
