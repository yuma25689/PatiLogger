package yuma25689.pati;

import java.util.ArrayList;

//import mediba.ad.sdk.android.openx.MasAdView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ViewCashPerDayActivity extends Activity 
	implements AdapterView.OnItemClickListener, View.OnClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private int iRecordCount = 0;
	private int iCurrentPage = 0;
	private int iViewCntOnPage = 30;
	
	private ListView listView;
	private ArrayList<MoneyMenuData> list = null;
	
	private final int MENU_SHARE = 0;
	private TableOpenInfo tblInf = new TableOpenInfo();
	private DBHelper db = null;

	Button btnPrev = null;
	Button btnNext = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_per_day);

        setTitle(getString(R.string.DlgTitle_ViewPerDay));
        commLogic.init(this);

        listView = (ListView) this.findViewById(R.id.lstViewParDayMain);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);
        listView.setFastScrollEnabled(true);

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
    	/*
        libAdMaker AdMaker = (libAdMaker)findViewById(R.id.admakerview);
        AdMaker.setActivity(this);
        AdMaker.siteId = "2318";
        AdMaker.zoneId = "6229";
        AdMaker.setUrl("http://images.ad-maker.info/apps/g6j7xncu5e83.html");
        AdMaker.start();		
    	*/

        listView
        .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

         @Override
         public void onCreateContextMenu(ContextMenu menu, View v,
           ContextMenuInfo menuInfo) {

        	 AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
        	 MoneyMenuData contact = (MoneyMenuData)listView.getItemAtPosition(adapterMenuInfo.position);
			 //MoneyMenuData contact = (MoneyMenuData) list.get(index);
			 menu.setHeaderTitle(contact.getMenuString());
			 menu.add(0, MENU_SHARE, 0, "共有");
         }

        });
    	// 下ビューの作成
		LinearLayout btm = (LinearLayout)this.findViewById(R.id.bottomBar);
		LayoutParams prms_w1 = 
			new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
				, LayoutParams.WRAP_CONTENT, (float)1.0 );
		btnPrev = new Button(this);
		btnPrev.setText(getString(R.string.btnPrev30));
		btnPrev.setOnClickListener(this);
		btnPrev.setLayoutParams(prms_w1);
		btm.addView(btnPrev);
		btnNext = new Button(this);
		btnNext.setText(getString(R.string.btnNext30));
		btnNext.setOnClickListener(this);
		btnNext.setLayoutParams(prms_w1);
		btm.addView(btnNext);
        
        updateAdapter();
    }
	@Override
	public void onClick(View v) {
		if( v==btnPrev ) {
			// 前30件
			if( iCurrentPage != 0)
			{
				iCurrentPage--;
			}
			updateAdapter();
		}
		else if( v==btnNext )
		{
			// 次30件
			iCurrentPage++;
			updateAdapter();
		}
	}
    
    // リスト選択イベントの処理
    public void onItemClick(AdapterView<?> parent,
    		View view, int position, long id ) {
    	try
    	{
    		switch( position )
    		{
    		//case 0:
    		default:
    			String strMenuString = list.get(position).getMenuString();
    			String[] arrMenuString = strMenuString.split(" ");
    			// String strWorkDate = arrMenuString[0].replace("/", "");
    			// 収支インテントを呼び出す
    			// Intentの呼び出し
    			Intent intent = new Intent(
    					this, MoneyManActivity.class );

    			// YYYY/MM/DD形式で渡す。
    			intent.putExtra(getString(R.string.key_money_workdatefrom), arrMenuString[0]);
    			intent.putExtra(getString(R.string.key_money_workdateto), arrMenuString[0]);

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
    	//MoneyMenuData data = null;
    	int[] ids = null;
    	String[] item = null;
    	String[] item2 = null;
    	String[] item3 = null;
    	String[] item4 = null;
    	String[] item5 = null;
    	String[] item6 = null;
    	String[] item7 = null;
    	
    	// queryでSELECTを実行
    	String[] columns = {"sum(CashFlow)","WorkDate","sum(ExpVal)"};
    	String selection = null;
    	
    	String groupBy = "WorkDate";
    	String orderBy = "WorkDate desc";
    	SQLiteDatabase dbRead = db.getReadableDatabase();
    	try {
	    	Cursor c = dbRead.query(tblInf.getTblName(),
	    			columns, selection, null, groupBy, null, orderBy);
	    	c.moveToFirst();
	    	iRecordCount = c.getCount();
	    	
	    	int iItemIdx = 0;
	    	ids = new int[iViewCntOnPage];//c.getCount()];
	    	item = new String[iViewCntOnPage];//c.getCount()];
	    	item2 = new String[iViewCntOnPage];//c.getCount()];
	    	item3 = new String[iViewCntOnPage];//c.getCount()];
	    	item4 = new String[iViewCntOnPage];//c.getCount()];
	    	item5 = new String[iViewCntOnPage];//c.getCount()];
	    	item6 = new String[iViewCntOnPage];//c.getCount()];
	    	item7 = new String[iViewCntOnPage];//c.getCount()];
	    	for (int i = 0; i < c.getCount(); i++) {
	    		if( ( iCurrentPage * iViewCntOnPage <= i ) 
	    	    		&& ( i < ( iCurrentPage * iViewCntOnPage ) + iViewCntOnPage )
	    	    		)
	    		{
		    		//ids[i] = c.getInt(0);
		    		int iMachineNameNotInputCnt = 0;
		    		String strMachineNames = "";
		    		Cursor c2 = dbRead.rawQuery("select distinct ma.MachineName from MachineMan ma, MoneyMan mo where ma._id = mo.McnId and mo.WorkDate='" 
		    				+ String.valueOf( c.getString( 1 ) ) + "'", null);
		    		if( c2.getCount() > 0 )
		    		{
		    			c2.moveToFirst();
		    			for( int j = 0; j < c2.getCount(); j++ )
		    			{
		    				if( strMachineNames.length() > 0 )
		    				{
		    					strMachineNames += ",";
		    				}
		    				if( c2.getString(0) != null && c2.getString(0).length() > 0)
		    				{
		    					strMachineNames += c2.getString(0);
		    				}
		    				else
		    				{
		    					iMachineNameNotInputCnt++;
		    				}
		    				c2.moveToNext();
		    			}
		    		}
		    		c2.close();
		    		if( strMachineNames == null || strMachineNames.length() <= 0 )
		    		{
		    			strMachineNames = getString(R.string.unknown);
		    		}
		    		else if( iMachineNameNotInputCnt > 0 )
		    		{
		    			strMachineNames += "(未入力あり)";
		    		}
		    		
		    		int iWrkIntervalUnknownCnt = 0;
		    		int iWrkIntervalSummary = 0;
		    		Cursor c3 = dbRead.rawQuery("select mo.WorkedInterval from MoneyMan mo where mo.WorkDate='" 
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
		    		
		        	item[iItemIdx] = TableControler.getFmtDate( c.getString(1) );
		        	item2[iItemIdx] = String.valueOf( c.getInt( 0 ) ) + "円";
		        	item3[iItemIdx] = strMachineNames;
		        	item4[iItemIdx] = strWrkIntervalString;
		        	item5[iItemIdx] = String.valueOf( c.getInt( 2 ) ) + "円";
		        	item6[iItemIdx] = strSalaryPerHour;
		        	item7[iItemIdx] = String.valueOf( iCurrentDateCnt ) + "件";
		    	    //+ " " + String.valueOf( c.getInt( 0 ) ) 
		    	    // + " " + strMachineNames;//String.valueOf( c.getInt( 2 ) );
		    	    iItemIdx++;
	    		}
	    	    c.moveToNext();
	    	}
	    	c.close();
	    	dbRead.close();
	    	//item = getResources().getStringArray(R.array.main_menu);
	    	if( item != null ) {
		    	for( int i=0; i < item.length; i++ ) {
		    		if( item[i] != null )
		    		{
		    			MoneyMenuData data = new MoneyMenuData();
			        	data.setMenuString( item[i] );
			        	data.setMenuId(ids[i]);
			        	data.setRightString( item2[i] );
			        	data.setBtmCap1String( "打った台:" );
			        	data.setBtm1String( item3[i] );
			        	data.setBtmCap2String( "稼働時間:" );
			        	data.setBtm2String( item4[i] );
			        	//data.setBtmCap3String( "件数:" );
			        	//data.setBtm3String( item7[i] );
			        	data.setBtmCap4String( "収支/h:" );
			        	data.setBtm4String( item6[i] );
			        	// アイコン無し
				        //data.setResId( android.R.drawable.ic_menu_add );
			        	list.add( data );
		    		}
		    	}
	    	}
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
    	
    	// 配列アダプタの生成
    	//ArrayAdapter<String> adapter;
    	MoneyMenuAdapter adapter =
    		new MoneyMenuAdapter(this, R.layout.lst_money_row, list); 
    	//adapter = new ArrayAdapter<String>( this,
    	// android.R.layout.simple_list_item_1,
    	// item );
    	listView.setAdapter(adapter);
    	
    	// 下ビューが見えるかどうか
    	LinearLayout btm = (LinearLayout)this.findViewById(R.id.bottomBar);
    	if( iRecordCount < iViewCntOnPage )
    	{
    		btm.setVisibility(View.GONE);
    	}
    	else
    	{
    		btm.setVisibility(View.VISIBLE);
    	}
    	// タイトルバーにpageを設定
    	String strCurrentPageMsg = "";
    	int iTotalPage = (int)Math.ceil( (double)iRecordCount / (double)iViewCntOnPage );
    	if( iTotalPage < 2 )
    	{
    		strCurrentPageMsg = "";
    	}
    	else
    	{
    		strCurrentPageMsg = "(" + ( iCurrentPage + 1 ) + " / " +  iTotalPage + ")ページ" ;
    	}
    	setTitle( getString(R.string.DlgTitle_ViewPerDay) + strCurrentPageMsg );
    	
    	// ボタンの使用可否
    	if( ( ( iCurrentPage * iViewCntOnPage ) + iViewCntOnPage ) <= iRecordCount )
    	{
    		btnNext.setEnabled( true );
    	}
    	else
    	{
    		btnNext.setEnabled( false );
    	}
    	if( iCurrentPage == 0 )
    	{
    		btnPrev.setEnabled( false );
    	}
    	else
    	{
    		btnPrev.setEnabled( true );
    	}
    	
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
    
    // リスト選択イベントの処理
    public boolean onContextItemSelected(MenuItem menuItem)
    {
		 AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
		 MoneyMenuData contact = (MoneyMenuData)listView.getItemAtPosition(adapterMenuInfo.position);
    	  switch (menuItem.getItemId()) {  
			  case MENU_SHARE:
				  	// 情報を共有する
				  	share(contact.getMenuString() + " " + contact.getRightString() 
				  			+ " " + contact.getBtmCap1String() + " " + contact.getBtm1String() );
	    			return true;  
			  default:  
				  	return super.onContextItemSelected(menuItem);  
    	  }      	
    }

    /**
     * 暗黙的 Intent でテキストを共有する
     */
    public void share(String text) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(
                intent, "他のアプリにテキスト送信"));
    }    
}
