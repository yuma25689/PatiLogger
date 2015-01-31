package yuma25689.pati;

import java.util.ArrayList;

//import mediba.ad.sdk.android.openx.MasAdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
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
import android.widget.TextView;
import android.widget.Toast;

public class MoneyManActivity extends Activity 
	implements AdapterView.OnItemClickListener, View.OnClickListener {
	//AdapterView.OnItemLongClickListener

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private String strTitleAddString = "";

	private final int MENU_DELETE = 2;
	private final int MENU_EDIT = 3;
	private final int MENU_COPY = 4;
	
	private int iRecordCount = 0;
	private int iCurrentPage = 0;
	private int iViewCntOnPage = 30;
	private ListView listView;
	private ArrayList<MoneyMenuData> list = null;

	private TableOpenInfo tblInf_MoneyMain = new TableOpenInfo();
	private DBHelper dbMoney = null;
	Button btnBack = null;
	Button btnRegist = null;
	Button btnNext = null;
	private final int SRCH_REQUEST_CODE = 100;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_main);
        try
        {
	        listView = (ListView) this.findViewById(R.id.lstMoneyMain);
	        listView.setFocusableInTouchMode(true);
	        listView.setOnItemClickListener(this);
	        //listView.setOnItemLongClickListener(this);
	        TextView tvEmpty = (TextView) this.findViewById(R.id.txtEmpty);
	        listView.setEmptyView( tvEmpty );	        

	    	dbMoney = new PatiManDBHelper(
	    		this
	        );
	        listView
	        .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

	         @Override
	         public void onCreateContextMenu(ContextMenu menu, View v,
	           ContextMenuInfo menuInfo) {

	        	 AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
				  // Only create a context menu for child items
				  if ( 0 < adapterMenuInfo.position ) {
				
					   // Array created earlier when we built the
					   // expandable list
					  MoneyMenuData contact = (MoneyMenuData)list.get(adapterMenuInfo.position);
					  menu.setHeaderTitle(contact.getMenuString());
					  menu.add(0, MENU_EDIT, 0, "編集");
					  menu.add(0, MENU_COPY, 1, "これをコピーして新規作成");
					  menu.add(0, MENU_DELETE, 2, "収支情報を削除");
				  }
	         }

	        });

	    	// 下ビューの作成
			LinearLayout btm = (LinearLayout)this.findViewById(R.id.money_bottomBar);
			LayoutParams prms_w1 = 
				new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
					, LayoutParams.WRAP_CONTENT, (float)1.0 );
			btnBack = new Button(this);
			btnBack.setText(getString(R.string.btnPrev30));
			btnBack.setOnClickListener(this);
			btnBack.setLayoutParams(prms_w1);
			btm.addView(btnBack);
			btnNext = new Button(this);
			btnNext.setText(getString(R.string.btnNext30));
			btnNext.setOnClickListener(this);
			btnNext.setLayoutParams(prms_w1);
			btm.addView(btnNext);

			commLogic.init(this);        
	
	    	updateActivity();
	    	
	    	/*
	        libAdMaker AdMaker = (libAdMaker)findViewById(R.id.admakerview);
	        AdMaker.setActivity(this);
	        AdMaker.siteId = "2318";
	        AdMaker.zoneId = "6229";
	        AdMaker.setUrl("http://images.ad-maker.info/apps/g6j7xncu5e83.html");
	        AdMaker.start();
	        */	    	
        } catch( Exception ex ) {
        	Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void updateActivity()
    {
    	tblInf_MoneyMain.setDBName( getString(R.string.db_name) );
    	tblInf_MoneyMain.setTblName( getString(R.string.MoneyTblName) );
    	tblInf_MoneyMain.setColumnsCreateText( getString(R.string.MoneyTblDef) );
    	tblInf_MoneyMain.setCursorFactory(null);
    	tblInf_MoneyMain.setDBVer( Integer.parseInt( getString(R.string.db_ver) ) );
    	tblInf_MoneyMain.setExcptColumnSpec(getString(R.string.MoneyTblROClmns));
    	strTitleAddString = "";
    	String strWherePhrase = "";
    	String strMonthFrom = null;
    	String strMonthTo = null;
    	String strWorkDateFrom = null;
    	String strWorkDateTo = null;
    	String strMachineName = null;
    	String strParlorName = null;
    	String strMakerName = null;
    	Bundle extras = getIntent().getExtras();
    	if( extras != null )
    	{
    		if( extras.containsKey(getString(R.string.key_money_monthfrom)) )
    		{
    			strMonthFrom = extras.getString(getString(R.string.key_money_monthfrom));
    		}
    		if( extras.containsKey(getString(R.string.key_money_monthto)) )
    		{
    			strMonthTo = extras.getString(getString(R.string.key_money_monthto));
    		}
    		if( extras.containsKey(getString(R.string.key_money_workdatefrom)) )
    		{
    			strWorkDateFrom = extras.getString(getString(R.string.key_money_workdatefrom));
    		}
    		if( extras.containsKey(getString(R.string.key_money_workdateto)) )
    		{
    			strWorkDateTo = extras.getString(getString(R.string.key_money_workdateto));
    		}
    		if( extras.containsKey(getString(R.string.key_money_machinename)) )
    		{
    			strMachineName = extras.getString(getString(R.string.key_money_machinename));
    		}
    		if( extras.containsKey(getString(R.string.key_money_parlorname)) )
    		{
    			strParlorName = extras.getString(getString(R.string.key_money_parlorname));
    		}
    		if( extras.containsKey(getString(R.string.key_money_makername)) )
    		{
    			strMakerName = extras.getString(getString(R.string.key_money_makername));
    		}
    	}
    	
    	// 検索条件の設定+タイトルバーへの表示
    	// 月From
    	if( strMonthFrom != null )
    	{
    		if( strWherePhrase.length() > 0 )
    		{
    			strWherePhrase += " and ";
    		}
    		strWherePhrase += "WorkDate>='" +strMonthFrom.replace("/", "") + "00'";
    		if( strMonthFrom.equals( strMonthTo ) )
    		{
    			// FromとToが同じ
    			strTitleAddString += strMonthFrom;
    		}
    		else if( strMonthTo == null )
    		{
    			strTitleAddString += strMonthFrom + "～";
    		}
    	}
    	// 月To
    	if( strMonthTo != null )
    	{
    		if( strWherePhrase.length() > 0 )
    		{
    			strWherePhrase += " and ";
    		}
    		strWherePhrase += "WorkDate<='" +strMonthTo.replace("/", "") + "99'";
    		if( strMonthFrom == null )
    		{
    			// Fromがない
    			strTitleAddString += "～" + strMonthTo;
    		}
    		else if( false == strMonthFrom.equals( strMonthTo ) )
    		{
    			// FromとToが違う
    			strTitleAddString += strMonthTo;
    		}
    	}
    	if( strMonthFrom == null && strMonthTo == null )
    	{
	    	// 日付From
	    	if( strWorkDateFrom != null )
	    	{
	    		strWherePhrase = "WorkDate>='" + strWorkDateFrom.replace("/", "") + "'";
	    		if( strWorkDateFrom.equals( strWorkDateTo ) )
	    		{
	    			// FromとToが同じ
	    			strTitleAddString = strWorkDateFrom;
	    		}
	    		else if( strWorkDateTo == null )
	    		{
	    			strTitleAddString = strWorkDateFrom + "～";
	    		}
	    	}
	    	// 日付To
	    	if( strWorkDateTo != null )
	    	{
	    		if( strWherePhrase.length() > 0 )
	    		{
	    			strWherePhrase += " and ";
	    		}
	    		strWherePhrase += "WorkDate<='" +strWorkDateTo.replace("/", "") + "'";
	    		if( strWorkDateFrom == null )
	    		{
	    			// Fromがない
	    			strTitleAddString += "～" + strWorkDateTo;
	    		}
	    		else if( false == strWorkDateFrom.equals( strWorkDateTo ) )
	    		{
	    			// FromとToが違う
	    			strTitleAddString += strWorkDateTo;
	    		}
	    	}
    	}
    	// 台
    	if( strMachineName != null )
    	{
    		if( strWherePhrase.length() > 0 )
    		{
    			strWherePhrase += " and ";
    		}
    		if( true == strMachineName.equals( getString(R.string.unknown)))
    		{
	    		strWherePhrase += "(McnId=-1 or McnId is null)";
    		}
    		else
    		{
	    		strWherePhrase += "McnId='" +TableControler.getMcnIdFromMcnName(this,
	    				dbMoney, strMachineName) + "'";
    		}
    		
    		if( strTitleAddString.length() > 0 )
    		{
    			strTitleAddString += ",";
    		}
			strTitleAddString += "台:" + strMachineName;
    	}
    	// 店
    	if( strParlorName != null )
    	{
    		if( true == strParlorName.equals( getString(R.string.unknown)))
    		{
	    		strWherePhrase += "(ParlorId=-1 or ParlorId is null)";
    		}
    		else    			
    		{
	    		if( strWherePhrase.length() > 0 )
	    		{
	    			strWherePhrase += " and ";
	    		}
	    		strWherePhrase += "ParlorId='" +TableControler.getParlorIdFromParlorName(this,
	    				dbMoney, strParlorName) + "'";
    		}
    		if( strTitleAddString.length() > 0 )
    		{
    			strTitleAddString += ",";
    		}
		
			strTitleAddString += "店:" + strParlorName;
    	}
    	// メーカー
    	if( strMakerName != null )
    	{
    		//if( false == strMakerName.equals( getString(R.string.unknown)))
    		//{
	    	//	strWherePhrase += "ParlorId=-1";
    		//}
    		//else
    		//{
	    		if( strWherePhrase.length() > 0 )
	    		{
	    			strWherePhrase += " and ";
	    		}
	    		String strMakerWhere1 = "";
	    		ArrayList<String> lstMcnIds = TableControler.getMcnIdsFromMakerName(
	    				this, dbMoney, strMakerName );
	    		String strInPhrase = "";
	    		if( lstMcnIds.size() > 0 )
	    		{
		    		for( String strMcnId : lstMcnIds )
		    		{
		    			if( strInPhrase.length() == 0 )
		    			{
		    				strInPhrase += "(";
		    			}
		    			else
		    			{
		    				strInPhrase += ",";
		    			}
		    			strInPhrase += strMcnId;
		    		}
	    			if( strInPhrase.length() > 0 )
	    			{
	    				strInPhrase += ")";
	    				strMakerWhere1 = "McnId in " + strInPhrase;
	    				// strWherePhrase += "McnId in " + strInPhrase;
	    			}
	    		}
	    		else
	    		{
	    			strMakerWhere1 = "McnId in (-2)";
	    		}
    			if( true == strMakerName.equals( getString(R.string.unknown)))
    			{
    		    	strWherePhrase += "(" + strMakerWhere1 + " or ( McnId = -1 or McnId is null ))";
    	    	}
    			else
    			{
    				strWherePhrase += strMakerWhere1;
    			}
    		//}
    		if( strTitleAddString.length() > 0 )
    		{
    			strTitleAddString += ",";
    		}
		
			strTitleAddString += "メーカー:" + strMakerName;
    	}
    	
    	// 最後に、タイトルに付加する文字列があれば括弧でくくる
    	if( strTitleAddString.length() > 0 )
    	{
    		strTitleAddString = "(" + strTitleAddString + ")";
    	}
    	setTitle(getString(R.string.DlgTitle_MoneyList) + strTitleAddString);
    	
    	tblInf_MoneyMain.setWherePhrase(strWherePhrase);
        		//, 
        		//tblInf_MoneyMain
        //);
        updateAdapter();    	
    }
	@Override
	public void onClick(View v) {
		if( v==btnBack ) {
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
			//String strCantDpl[] = getResources().getStringArray(R.array.money_cant_dpl);
			//arrCantDpl = new ArrayList<String>( Arrays.asList(strCantDpl) );
			
			//Toast.makeText(this,Integer.toString(position),Toast.LENGTH_SHORT).show();
    		switch( position )
    		{
    		// TODO:新規と更新で同じ設定値がたくさんあるが・・・まとめた方がよくない？
    		case 0:
    			// 新規追加
    			// レコード追加インテントを呼び出す
    			// Intentの呼び出し
    			startCreateNewItem( -1 );
    			
    			break;
    		default:
    			// 更新
    			// レコード更新インテントを呼び出す
    			// Intentの呼び出し
    			startEditItem( list.get(position).getMenuId() );
    			break;
    		}
    		/* 
    		 // Intentの呼び出し
    		 Intent intent = new Intent( this, yuma25689.pati_adv. .class );

    		 // インテントへのパラメータ付加
    		 intent.putExtra("text",strParam1);
    		 
    		 // リクエストコード付きインテントの呼び出し
    		 startActivityForResult( intent, REQUEST_TEXT );
    		 */
    		
    	} catch( Exception e ) {
    		Toast.makeText( this, e.getMessage(), Toast.LENGTH_SHORT );
    	}
    }
    void startCreateNewItem( int iCpyId )
    {
    	if( iCpyId != -1 )
    	{
    		tblInf_MoneyMain.setWherePhrase("_id=" + iCpyId );
    	}
    	else
    	{
    		tblInf_MoneyMain.setWherePhrase(null);
    	}
		
    	ArrayList<String> arrCantDpl = null;
    	Intent intent = new Intent( this, RecordUpdateActivity.class );
    	ArrayList<TableOpenInfo> arrTblInf = new ArrayList<TableOpenInfo>();
		arrTblInf.add(tblInf_MoneyMain);
		intent.putExtra(getString(R.string.key_update), 0);
		intent.putExtra(getString(R.string.key_tblLevel), 0);
		intent.putExtra(getString(R.string.key_id), -1 );
		intent.putExtra(getString(R.string.key_dlgTitle), 
				getString(R.string.MoneyTblDlgTitle) + getString(R.string.StrNewRegister) );
		intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.MoneyTblMainClmnName ));
		intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
		intent.putExtra(getString(R.string.key_addMsgId), getString(R.string.msg_new_cashflow_add));    			
		intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
		intent.putExtra(getString(R.string.key_tblSubClmn1), getString(R.string.MoneyTblSubClmnName1 ));
		intent.putExtra(getString(R.string.key_tblSubClmn2), getString(R.string.MoneyTblSubClmnName2 ));
		intent.putExtra(getString(R.string.key_tblSubClmn3), getString(R.string.MoneyTblSubClmnName3 ));
		//intent.putExtra("DBHelper", dbMoney);
		startActivity( intent );    	
    }
    void startEditItem( int id )
    {
    	ArrayList<String> arrCantDpl = null;
		Intent intent = new Intent( this, RecordUpdateActivity.class );
		ArrayList<TableOpenInfo> arrTblInf = new ArrayList<TableOpenInfo>();

		tblInf_MoneyMain.setWherePhrase("_id=" + id );
		arrTblInf.add(tblInf_MoneyMain);

		intent.putExtra(getString(R.string.key_update), 1);
		intent.putExtra(getString(R.string.key_tblLevel), 0);
		intent.putExtra(getString(R.string.key_id), id );
		intent.putExtra(getString(R.string.key_dlgTitle), 
				getString(R.string.MoneyTblDlgTitle) + getString(R.string.StrUpdate) );
		intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.MoneyTblMainClmnName ));
		intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
		intent.putExtra(getString(R.string.key_updMsgId), getString(R.string.msg_upd_cashflow));    			
		intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
		intent.putExtra(getString(R.string.key_tblSubClmn1), getString(R.string.MoneyTblSubClmnName1 ));
		intent.putExtra(getString(R.string.key_tblSubClmn2), getString(R.string.MoneyTblSubClmnName2 ));
		intent.putExtra(getString(R.string.key_tblSubClmn3), getString(R.string.MoneyTblSubClmnName3 ));
		//intent.putExtra("DBHelper", dbMoney);
		startActivity( intent );
    	
    }
    // リスト選択イベントの処理
    public boolean onContextItemSelected(MenuItem menuItem)
    {
	   	 AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
    	  
    	  MoneyMenuData contact = (MoneyMenuData) list.get(adapterMenuInfo.position);;  
    	  switch (menuItem.getItemId()) {  
	    	  case MENU_EDIT:
	    		  startEditItem(contact.getMenuId());
	    		  return true;
	    	  case MENU_COPY:
	    		  startCreateNewItem( contact.getMenuId() );
	    		  return true;
			  case MENU_DELETE:  
	    			tblInf_MoneyMain.setWherePhrase("_id=" + contact.getMenuId() );
	    			new AlertDialog.Builder(this)
	    			.setTitle(getString(R.string.DLG_TITLE_CONFIRM))
	    			.setMessage("長押しされた行を削除しますが、よろしいですか？")
	    			.setPositiveButton(
	    				android.R.string.ok,
	    				new DialogInterface.OnClickListener() {	
	    					@Override
	    					public void onClick(DialogInterface dialog, int which) {
	    						SQLiteDatabase db = dbMoney.getWritableDatabase();
	    						db.beginTransaction();
	    						db.delete( tblInf_MoneyMain.getTblName(),
	    								tblInf_MoneyMain.getWherePhrase(), null);
	    						db.setTransactionSuccessful();
	    						db.endTransaction();
	    						tblInf_MoneyMain.setWherePhrase("");
	    						updateAdapter();
	    						Toast.makeText( MoneyManActivity.this, "削除しました。", Toast.LENGTH_SHORT );
	    					}
	    				}
	    			)
	    			.setNegativeButton(
	    				android.R.string.cancel,
	    				new DialogInterface.OnClickListener() {	
	    					@Override
	    					public void onClick(DialogInterface dialog, int which) {								
	    					}
	    				}							
	    			)
	    			.show();    			

				  	return true;
			  default:  
				  	return super.onContextItemSelected(menuItem);  
    	  }      	
    }
    /*
    // リスト選択イベントの処理
    public boolean onItemLongClick(AdapterView<?> parent,
    		View view, int position, long id ) {
    	boolean bRet = false;
		
    	try
    	{
    		switch( position )
    		{
    		case 0:
    			// 新規追加
    			// スルー
    			break;
    		default:
    			// TODO:メニュー表示
    			// とりあえず、削除機能がほしいのでそれを実装

    			break;
    		}
    		
    	} catch( Exception e ) {
    		Toast.makeText( this, e.getMessage(), Toast.LENGTH_SHORT );
    	}
    	return bRet;
    } 
    */
	@Override
	protected void onResume() {
		super.onResume();
		updateActivity();
	}
    
    // 配列アダプタの更新
    private void updateAdapter() {

    	if( listView != null ) {
    		listView.removeAllViewsInLayout();
    	}

    	this.list = new ArrayList<MoneyMenuData>();

   		// リストの最初は「新規追加」
    	String strAdd_ex = "";
    	if( PatiLogger.isExistLastFinishNotUpdData( this, tblInf_MoneyMain.getTblName() ) )
    	{
    		strAdd_ex = getString(R.string.add_ex);
    	}
		MoneyMenuData data = new MoneyMenuData();
    	data.setMenuString( getString(R.string.add) + strAdd_ex );
    	data.setBtm1String( "収支のデータを新規に追加する場合、ここをタップ" );
       	data.setResId( android.R.drawable.ic_menu_add );
    	list.add( data );

    	// データベースから台の名称のString配列を取得する
    	int[] ids = null;
    	int[] mcnIds = null;
    	String[] item = null;
    	String[] item2 = null;
    	String[] item3 = null;
    	String[] item4 = null;
    	String[] item5 = null;

    	// queryでSELECTを実行
    	String[] columns = {"_id",getString(R.string.MoneyTblMainClmnName)
    							,getString(R.string.MoneyTblSubClmnName1)
    							,getString(R.string.MoneyTblSubClmnName2)
    							,getString(R.string.MoneyTblSubClmnName3)
    							,getString(R.string.MoneyTblSubClmnName4)
    							,"WorkedInterval"};
    	String selection = tblInf_MoneyMain.getWherePhrase();
    	SQLiteDatabase db = dbMoney.getReadableDatabase();
    	try {
	    	Cursor c = db.query(tblInf_MoneyMain.getTblName(),
	    			columns, selection, null, null, null, "WorkDate desc, WorkTime desc");
	    	c.moveToFirst();
	    	iRecordCount = c.getCount();
	    	ids = new int[iViewCntOnPage];//c.getCount()];
	    	mcnIds = new int[iViewCntOnPage];//c.getCount()];
	    	int iItemIdx = 0;
	    	item = new String[iViewCntOnPage];//c.getCount()];
	    	item2 = new String[iViewCntOnPage];//c.getCount()];
	    	item3 = new String[iViewCntOnPage];//c.getCount()];
	    	item4 = new String[iViewCntOnPage];//c.getCount()];
	    	item5 = new String[iViewCntOnPage];//c.getCount()];
	    	for (int i = 0; i < c.getCount(); i++) {
	    		if( ( iCurrentPage * iViewCntOnPage <= i ) 
	    		&& ( i < ( iCurrentPage * iViewCntOnPage ) + iViewCntOnPage )
	    		)
	    		{
	    			// 収支ID
		    		ids[iItemIdx] = c.getInt(0);
		    		// 台ID
		    		mcnIds[iItemIdx] = c.getInt(3);
		    		// ※ ↓他のとの違いあり
		    		String strTime = "";
		    		String strMcnName = getString(R.string.noset);
		    		String strPrlName = getString(R.string.noset);
		    		String strWorkedInterval = getString(R.string.noset);
		    		if( c.isNull(2) == false && c.getString(2).length() > 0)
		    		{
		    			strTime = " " + TableControler.getFmtTime( c.getString(2) );
		    		}
		    		if( c.isNull(3) == false && c.getInt(3) != -1 )
		    		{
		    			strMcnName = " " + TableControler.getMcnNameFromMcnId( this, dbMoney, c.getString(3));
		    		}
		    		if( c.isNull(4) == false && c.getInt(4) != -1 )
		    		{
		    			strPrlName = " " + TableControler.getParlorNameFromParlorId( this, dbMoney, c.getString(4));
		    		}
		    		if( c.getString(6) != null && c.getString(6).length() == 4 )
		    		{
		    			strWorkedInterval = " " + c.getString(6).substring(0,2) + ":"
		    								+ c.getString(6).substring(2,4);
		    		}
		    		
		    	    item[iItemIdx] = TableControler.getFmtDate( c.getString(1) )
		    	    		+ strTime;
		    	    		//+ strMcnName
		    	    		//+ strPrlName 
		    	    		//+ " " + String.valueOf( c.getInt(5) )
		    	    		//+ strWorkedInterval;//c.getString(6);
		    	    item2[iItemIdx] = strMcnName;
		    	    item3[iItemIdx] = strPrlName;
		    	    item4[iItemIdx] = String.valueOf( c.getInt(5) ) + "円";
		    	    item5[iItemIdx] = strWorkedInterval;
		    	    iItemIdx++;
	    		}
	    	    c.moveToNext();
	    	}
	    	c.close();
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
    	//item = getResources().getStringArray(R.array.main_menu);
    	if( item != null ) {
	    	for( int i=0; i < item.length; i++ ) {
	    		if( item[i] != null )
	    		{
		    		MoneyMenuData data2 = new MoneyMenuData();
		    		data2.setMenuString( item[i] );
		    		data2.setRightString( item4[i] );
		    		data2.setBtmCap1String( "台:" );
		    		data2.setBtm1String( item2[i] );
		    		data2.setBtmCap2String( "店:" );
		    		data2.setBtm2String( item3[i] );
		    		data2.setBtmCap3String( "稼働時間:" );
		    		data2.setBtm3String( item5[i] );
		    		
		    		
		    		
		    		
		    		data2.setMenuId(ids[i]);
		        	// アイコン
		        	String strPatiTypeId = TableControler.getPatiTypeIdFromMcnId(
		        			this,dbMoney,String.valueOf(mcnIds[i]));
		        	if( "0".equals(strPatiTypeId))
		        	{
		        		data2.setResId( R.drawable.slo );
		        	}
		        	else if( "1".equals(strPatiTypeId))
		        	{
		        		data2.setResId( R.drawable.patinko );
		        	}
		        	else
		        	{
		        		data2.setResId( R.drawable.icon );
		        	}
		        	
		        		
		        	list.add( data2 );
	    		}
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
    	
    	// 下ビューが見えるかどうか
    	LinearLayout btm = (LinearLayout)this.findViewById(R.id.money_bottomBar);
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
    	setTitle( getString(R.string.DlgTitle_MoneyList) + strTitleAddString + strCurrentPageMsg );
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
    		btnBack.setEnabled( false );
    	}
    	else
    	{
    		btnBack.setEnabled( true );
    	}
    }
    /*
     * オプションメニューの作成
     */
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean ret = super.onCreateOptionsMenu(menu);
    	menu.add( 0, Menu.FIRST, Menu.NONE, "検索").setIcon(android.R.drawable.ic_menu_search);
    	//menu.add( 0, Menu.FIRST + 1, Menu.NONE, "一括削除").setIcon(android.R.drawable.ic_menu_delete);
    	//menu.add( 0, Menu.FIRST + 1, Menu.NONE, "一括店変更").setIcon(android.R.drawable.ic_menu_edit);
    	return ret;
    }
    
    /**
     * オプションメニューの選択イベント
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case Menu.FIRST:
        	// 検索
        	// 検索アクティビティの起動
        	startSrchIntentForResult();
            return true;
     /*
        case Menu.FIRST + 1:
        	// 一括削除
        	// 削除アクティビティの起動
        	showToast("この機能は実装中です");
            return true;

        case Menu.FIRST + 2:
        	// 一括店変更
        	// 店変更アクティビティの起動
        	showToast("この機能は実装中です");
            return true;
     */
        }
        return false;
    }
	public void startSrchIntentForResult()
	{
		Intent intent = new Intent( this,
				MoneyManSrchActivity .class );
		intent.putExtra(this.getString(R.string.key_request_result),
				1);
		this.startActivityForResult( intent,
				SRCH_REQUEST_CODE );		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case SRCH_REQUEST_CODE:
			if (resultCode == RESULT_OK)
			{
				String[] arrkeys = {
					getString(R.string.key_money_monthfrom),
					getString(R.string.key_money_monthto),
					getString(R.string.key_money_machinename),
					getString(R.string.key_money_parlorname)
				};
				
				Bundle getData = data.getExtras();
				Bundle orgExtras = getIntent().getExtras();
				if( orgExtras != null )
				{
					for( String strKey:arrkeys )
					{
						if( getData.containsKey(strKey))
						{
							if( orgExtras.containsKey(strKey))
							{
								orgExtras.remove( strKey );
							}
							orgExtras.putString( strKey, getData.getString(strKey));
						}
					}
				}
				else
				{
					orgExtras = getData;
				}
				getIntent().putExtras(orgExtras);
				updateActivity();
			} 
			break;
		default:
			break;
		}		
	}
    
}
