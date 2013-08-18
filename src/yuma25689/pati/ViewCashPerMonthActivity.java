package yuma25689.pati;

import java.util.ArrayList;
//import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
//import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ViewCashPerMonthActivity extends Activity 
	implements AdapterView.OnItemClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();

	private ListView listView;
	private ArrayList<MoneyMenuData> list = null;

	private TableOpenInfo tblInf = new TableOpenInfo();
	private DBHelper db = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_per_month);

        setTitle(getString(R.string.DlgTitle_ViewPerMonth));
        commLogic.init(this);

        listView = (ListView) this.findViewById(R.id.lstViewParMonthMain);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);

        tblInf.setDBName( getString(R.string.db_name) );
        tblInf.setTblName( getString(R.string.MoneyTblName) );
        tblInf.setColumnsCreateText( getString(R.string.MoneyTblDef) );
        tblInf.setCursorFactory(null);
        tblInf.setDBVer( Integer.parseInt( getString(R.string.db_ver) ) );
        tblInf.setExcptColumnSpec(getString(R.string.MoneyTblROClmns));
        tblInf.setWherePhrase(null);
    	db = new PatiManDBHelper(
        	this
        );
        		//, 
        		//tblInf
        //);
        updateAdapter();
    }
    // リスト選択イベントの処理
    public void onItemClick(AdapterView<?> parent,
    		View view, int position, long id ) {
    	try
    	{
    		Intent intent = null;
    		switch( position )
    		{
			default:
				String strMenuString = list.get(position).getMenuString();
				String[] arrMenuString = strMenuString.split(" ");
				// 収支インテントを呼び出す
				// Intentの呼び出し
				intent = new Intent(
						this, MoneyManActivity.class );
	
				// YYYY/MMで渡す
				intent.putExtra(getString(R.string.key_money_monthfrom), arrMenuString[0]);
				intent.putExtra(getString(R.string.key_money_monthto), arrMenuString[0]);
	
				startActivity( intent );
	
				break;
    		}
    		
    	} catch( Exception e ) {
    		Toast.makeText( this, e.getMessage(), Toast.LENGTH_SHORT );
    	}

    }
	@Override
	protected void onResume() {
		super.onResume();
		updateAdapter();
	}
    
    // 配列アダプタの更新
    private void updateAdapter() {
    	
    	if( listView != null ) {
    		listView.removeAllViewsInLayout();
    	}

    	this.list = new ArrayList<MoneyMenuData>();

    	// データベースからString配列を取得する
    	MoneyMenuData data = null;
    	int[] ids = null;
    	String[] item = null;
    	String[] item2 = null;
    	String[] item3 = null;
    	String[] item4 = null;
    	
    	// queryでSELECTを実行
    	String[] columns = {"sum(CashFlow)","SUBSTR(WorkDate,1,6) AS WorkMonth", "sum(ExpVal)"};
    	String selection = null;
    	String groupBy = "SUBSTR(WorkDate,1,6)";
    	String orderBy = "SUBSTR(WorkDate,1,6) desc";
    	SQLiteDatabase dbRead = db.getReadableDatabase();
    	try {
	    	Cursor c = dbRead.query(tblInf.getTblName(),
	    			columns, selection, null, groupBy, null, orderBy);
	    	c.moveToFirst();
	    	ids = new int[c.getCount()];
	    	item = new String[c.getCount()];
	    	item2 = new String[c.getCount()];//c.getCount()];
	    	item3 = new String[c.getCount()];//c.getCount()];
	    	item4 = new String[c.getCount()];
	    	
	    	for (int i = 0; i < item.length; i++) {
	    		//ids[i] = c.getInt(0);
	    	    item[i] = TableControler.getFmtMonth( c.getString(1) ); 
	    	    //+ " " + String.valueOf( c.getInt( 0 ) );
	    	    item2[i] = String.valueOf( c.getInt( 0 ) ) + "円";
	    	    //+ " " + String.valueOf( c.getInt( 2 ) );
	    		int iWrkIntervalUnknownCnt = 0;
	    		int iWrkIntervalSummary = 0;
	    		Cursor c3 = dbRead.rawQuery("select mo.WorkedInterval from MoneyMan mo where SUBSTR(WorkDate,1,6)='" 
	    				+ String.valueOf( c.getString( 1 ) ) + "'", null);
	    		if( c3.getCount() > 0 )
	    		{
	    			c3.moveToFirst();
	    			for( int j = 0; j < c3.getCount(); j++ )
	    			{
	    				if( c3.getString(0) != null 
	    				&& c3.getString(0).trim().length() > 0 ) 
	    				//&& c3.getString(0).indexOf(":") != -1 )
	    				{
	    					iWrkIntervalSummary += getTimeValFromYYMM( c3.getString(0) );
	    				}
	    				else
	    				{
	    					iWrkIntervalUnknownCnt++;
	    				}
	    				c3.moveToNext();
	    			}
	    		}
	    		int iCurrentDateCnt = c3.getCount();
	    		c3.close();

	    		double dblSalaryPerMin = 0;
	    		double dblSalaryPerHour = 0;
	    		String strSalaryPerHour = "";
	    		if( iWrkIntervalSummary > 0 )
	    		{
	    			dblSalaryPerMin = c.getInt( 0 ) / iWrkIntervalSummary;
	    			dblSalaryPerHour = dblSalaryPerMin * 60;
	    			int intSalaryPerHour = (int) dblSalaryPerHour;
	    			strSalaryPerHour = String.valueOf( intSalaryPerHour ) + "円";
	    		}
	    		else
	    		{
	    			strSalaryPerHour = getString(R.string.unknown);
	    		}
	    		
	    		String strWrkIntervalString = getYYMMFromTimeVal( iWrkIntervalSummary );
	    		if( iWrkIntervalUnknownCnt > 0 )
	    		{
	    			if( iWrkIntervalUnknownCnt == c3.getCount() )
	    			{
	    				strWrkIntervalString = "未入力";
	    			}
	    			else
	    			{
	    				strWrkIntervalString += "(未入力" + iWrkIntervalUnknownCnt + "/" + iCurrentDateCnt + "件)";
	    			}
	    		}
	        	item3[i] = strWrkIntervalString;
	        	item4[i] = strSalaryPerHour;
	    	    
	    	    c.moveToNext();
	    	}
	    	c.close();
	    	
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
    	//item = getResources().getStringArray(R.array.main_menu);
    	if( item != null ) {
	    	for( int i=0; i < item.length; i++ ) {
	    		data = new MoneyMenuData();
	        	data.setMenuString( item[i] );
	        	data.setRightString( item2[i] );
	        	data.setBtmCap2String( "稼働時間:" );
	        	data.setBtm2String( item3[i] );
	        	data.setBtmCap4String( "収支/h:" );
	        	data.setBtm4String( item4[i] );
	        	data.setMenuId(ids[i]);
	        	// アイコン無し
		        //data.setResId( android.R.drawable.ic_menu_add );
	        	list.add( data );
	    	}
    	}
    	// 配列アダプタの生成
    	//ArrayAdapter<String> adapter;
    	MoneyMenuAdapter adapter =
    		new MoneyMenuAdapter(this, R.layout.lst_money_row, list); 
    	//adapter = new ArrayAdapter<String>( this,
    	// android.R.layout.simple_list_item_1,
    	// item );
    	listView.setAdapter(adapter);
    }
    private int getTimeValFromYYMM(String YYMM)
    {
    	int iRet = 0;
    	
    	String strHour = "";
    	String strMinute = "";
    	
    	//String[] split = YYMM.split(":");
    	strHour = YYMM.substring(0,2);//split[0];
    	strMinute = YYMM.substring(2,4);
    	
    	int iHour = Integer.parseInt( strHour );
    	int iMinute = Integer.parseInt( strMinute );
    	
    	iRet = iHour * 60 + iMinute;

    	return iRet;
    }
    private String getYYMMFromTimeVal(int timeVal)
    {
    	String sRet = "";
    	
    	int iHour = (int)Math.ceil( timeVal / 60 );
    	int iMinute = timeVal - iHour*60;
    	
    	sRet = String.format("%02d:%02d", iHour, iMinute );

    	return sRet;
    }
    
}
