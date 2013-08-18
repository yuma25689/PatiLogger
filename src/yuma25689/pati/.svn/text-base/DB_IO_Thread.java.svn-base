package yuma25689.pati;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
//import android.util.Log;

public class DB_IO_Thread extends Thread {

	private Handler handler;
	private Context act;
	private SQLiteDatabase db;
	private final Runnable listener;
	private DBHelper dbHelper;
	public static final int PROC_TYPE_NONE = 0;
	public static final int PROC_TYPE_EXPORT_ALL = 1;
	public static final int PROC_TYPE_IMPORT_ALL = 2;
	public static final int PROC_TYPE_IMPORT_ADD_DUP_ERR = 3;
	public static final int PROC_TYPE_IMPORT_ADD_DUP_IGNORE = 4;
	private int iProcType = PROC_TYPE_NONE;
	private String strDir;
	private String strFile;
	private Boolean bRet = false;
	int iImpMachineCnt,iImpParlorCnt,iImpMoneyCnt, iImpMakerCnt;
	
	public DB_IO_Thread(
			Context _act,
			Handler _handler,
			Runnable _listener,
			SQLiteDatabase _db,
			int _iProcType,
			String _strDir,
			String _strFile,
			DBHelper _dbHelper
	)
    {
        this.db = _db;
        this.act = _act;
        this.handler = _handler;
        this.listener = _listener;
        this.iProcType = _iProcType;
        this.strDir = _strDir;
        this.strFile = _strFile;
        this.dbHelper = _dbHelper;
    }

	public Boolean getResult()
	{
		return bRet;
	}

    @Override
    public void run()
    {
        DatabaseAssistant da = 
        	new DatabaseAssistant(act,db,handler,this);
        switch( iProcType )
        {
        case PROC_TYPE_NONE:
        	break;
        case PROC_TYPE_EXPORT_ALL:
        	bRet = da.exportData(strDir, strFile);
        	break;
        case PROC_TYPE_IMPORT_ALL:
        	bRet = da.importData(
    			strDir + "/" + strFile,
    			DatabaseAssistant.IMPORT_MODE_ALL_CLEAR,
    			dbHelper
        	);
        	iImpMachineCnt = da.getImportMachineRowCnt();
        	iImpParlorCnt = da.getImportParlorRowCnt();
        	iImpMakerCnt = da.getImportMakerRowCnt();
        	iImpMoneyCnt = da.getImportMoneyRowCnt();
        	break;
        case PROC_TYPE_IMPORT_ADD_DUP_ERR:
        	bRet = da.importData(
    			strDir + "/" + strFile,
    			DatabaseAssistant.IMPORT_MODE_EXIST_ERR,
    			dbHelper
        	);
        	iImpMachineCnt = da.getImportMachineRowCnt();
        	iImpParlorCnt = da.getImportParlorRowCnt();
        	iImpMakerCnt = da.getImportMakerRowCnt();
        	iImpMoneyCnt = da.getImportMoneyRowCnt();
        	break;
        case PROC_TYPE_IMPORT_ADD_DUP_IGNORE:
        	bRet = da.importData(
    			strDir + "/" + strFile,
    			DatabaseAssistant.IMPORT_MODE_EXIST_IGNORE,
    			dbHelper
        	);
        	iImpMachineCnt = da.getImportMachineRowCnt();
        	iImpParlorCnt = da.getImportParlorRowCnt();
        	iImpMakerCnt = da.getImportMakerRowCnt();
        	iImpMoneyCnt = da.getImportMoneyRowCnt();
        	break;
        }
        
        // 終了を通知
        handler.post(listener);
    }
    public String getFileNm() {
		return strFile;
	}
    public int getProcType() {
		return iProcType;
	}
    public int getImpCateCnt() {
		return iImpMachineCnt;
	}
    public int getImpTimeCnt() {
		return iImpParlorCnt;
	}
    public int getImpMakerCnt() {
		return iImpMakerCnt;
	}
    public int getImpMemoCnt() {
		return iImpMoneyCnt;
	}
    
}

/*
Message _msg = new Message();
String[] strArr = {
	act.getString( R.string.TITLE_ERROR ),
	act.getString( R.string.MSG_UNIMPLE )
};
Bundle bdl = new Bundle();
bdl.putStringArray( BrainLogger.ERROR_MSG_KEY, strArr);
_msg.setData(bdl);
_msg.what = BrainLogger.ERROR_MSG_ID;
handler.sendMessage(_msg);        	
*/
