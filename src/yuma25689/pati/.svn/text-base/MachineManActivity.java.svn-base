package yuma25689.pati;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
//import java.util.HashMap;
//import java.util.TreeMap;
//import java.util.HashMap;
import java.util.List;

//import mediba.ad.sdk.android.openx.MasAdView;

//import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ToggleButton;
//import android.widget.ListView;
//import android.widget.SimpleExpandableListAdapter;
//import android.widget.Toast;
import android.widget.Toast;

public class MachineManActivity extends Activity 
	implements ExpandableListView.OnChildClickListener{//AdapterView.OnItemClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private ExpandableListView listView;
	//private ArrayList<DataMenuData> list = null;
	private final int MODE_ALL = 1;
	private final int MODE_SORT = 2;
	private int currentMode = MODE_ALL;
	
	private final int MENU_MONEY_VIEW = 1;
	private final int MENU_DELETE = 2;
	private TableOpenInfo tblInf_MachineMain = new TableOpenInfo();
	private DBHelper dbMachine = null;
    List<String> groupData; 
	private final int REQUEST_CODE = 101;
	
    private String strMessage = null;
    private int iDeleteId = 0;
    private String strDelete = null;
    private int[] iMcnIds = null;
	private int iRequestResult = 0;
	private ExpandableListAdapter mAdapter;
	private final int MENU_EDIT = 3;
	private final int MENU_COPY = 4;
	private String strTitle = "";
	ArrayList<String> arrCantDpl = null;
	private String strSrchWord = "";
	//private static final String GRP_NAME_KEY = "GRP_NAME_KEY";  
	//private static final String CHILD_NAME_KEY = "CHILD_NAME_KEY";
	//private static final int MAX_DIVIDE_CNT = 20;
	
	ToggleButton btnAll;
	ToggleButton btnSort;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_main);

        strTitle = getString(R.string.DlgTitle_McnList); 

        /*
        libAdMaker AdMaker = (libAdMaker)findViewById(R.id.admakerview);
        AdMaker.setActivity(this);
        AdMaker.siteId = "2318";
        AdMaker.zoneId = "6229";
        AdMaker.setUrl("http://images.ad-maker.info/apps/g6j7xncu5e83.html");
        AdMaker.start();
        */
        Bundle extras = getIntent().getExtras();
        if( extras != null )
        {
	        // 結果を返却することを求めて起動されたかどうかを表す
	        iRequestResult = extras.getInt(getString(R.string.key_request_result));
	        if( iRequestResult == 1 )
	        {
	        	strTitle = getString(R.string.DlgTitle_McnSelect);
	        }
        }
        setTitle(strTitle);
        
    	String strAdd_ex = "";
    	if( PatiLogger.isExistLastFinishNotUpdData( this, getString(R.string.McnTblName) ) )
    	{
    		strAdd_ex = getString(R.string.add_ex);
    	}
        Button btnAdd = (Button)this.findViewById(R.id.btnMachineAdd);
        btnAdd.setText( getString(R.string.add) + strAdd_ex );
        btnAdd.setOnClickListener(
        		new OnClickListener() {
					
					@Override
					public void onClick(View v) {
		    			// 新規追加
		    			// レコード追加インテントを呼び出す
		    			// Intentの呼び出し
						startCreateNewItem(-1);
					}
				}
        );
        listView = (ExpandableListView) this.findViewById(R.id.lstMachineMain);
        TextView tvEmpty = (TextView) this.findViewById(R.id.txtEmpty);
        listView.setEmptyView( tvEmpty );	        
        
        //listView.setOnItemLongClickListener(this);
        
    	tblInf_MachineMain.setDBName( getString(R.string.db_name) );
    	tblInf_MachineMain.setTblName( getString(R.string.McnTblName) );
    	tblInf_MachineMain.setColumnsCreateText( getString(R.string.McnTblDef) );
    	tblInf_MachineMain.setCursorFactory(null);
    	tblInf_MachineMain.setDBVer( Integer.parseInt( getString(R.string.db_ver) ) );
    	tblInf_MachineMain.setExcptColumnSpec(getString(R.string.McnTblROClmns));
    	tblInf_MachineMain.setWherePhrase(null);
    	dbMachine = new PatiManDBHelper(
        	this
        );
        		//, 
        		//tblInf_MachineMain
        //);
    	
      	LinearLayout btm = (LinearLayout) this.findViewById(R.id.mcnview_bottomBar);
    	LayoutParams prms_w1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
    				, LayoutParams.WRAP_CONTENT, (float)1.0 );
//    	LayoutParams prms_w2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT
//    				, LayoutParams.WRAP_CONTENT );
    	btnAll = new ToggleButton(this);
    	btnAll.setTextOn(getString(R.string.btnAll));
    	btnAll.setTextOff(getString(R.string.btnAll));
    	btnAll.setLayoutParams(prms_w1);
    	btnAll.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				// 全部表示に切り替える
 				currentMode = MODE_ALL;
 		    	btnAll.setChecked(true);
 		    	btnSort.setChecked(false);
 				updateAdapter();
 			}
 		});
    	btm.addView(btnAll);
    	btnSort = new ToggleButton(this);
    	btnSort.setTextOn(getString(R.string.btnSpecial));
    	btnSort.setTextOff(getString(R.string.btnSpecial));
    	btnSort.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				// 最近の表示に切り替える				
 				currentMode = MODE_SORT;
 		    	btnAll.setChecked(false);
 		    	btnSort.setChecked(true);
 				updateAdapter();
 			}
    	});
    	btnSort.setLayoutParams(prms_w1);
    	btm.addView(btnSort);
    	btnAll.setChecked(true);
    	btnSort.setChecked(false);

        commLogic.init(this);
        updateAdapter();

    
        listView
        .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                
         @Override
         public void onCreateContextMenu(ContextMenu menu, View v,
           ContextMenuInfo menuInfo) {

			  ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
			  int type = ExpandableListView
			    .getPackedPositionType(info.packedPosition);
			  int groupPosition = ExpandableListView
			    .getPackedPositionGroup(info.packedPosition);
			  int childPosition = ExpandableListView
			    .getPackedPositionChild(info.packedPosition);
			  // Only create a context menu for child items
			  if (type == 1) {
			
				   // Array created earlier when we built the
				   // expandable list
				  DataMenuData contact = (DataMenuData) mAdapter.getChild(
						  groupPosition, childPosition);
				  menu.setHeaderTitle(contact.getHideString());
				  menu.add(0, MENU_EDIT, 0, "編集");
				  menu.add(0, MENU_COPY, 1, "これをコピーして新規作成");
				  menu.add(0, MENU_MONEY_VIEW, 2, "この台の収支一覧");
				  menu.add(0, MENU_DELETE, 3, "台情報を削除");
			  }
         }

        });
        
    }
    // リスト選択イベントの処理
    public boolean onContextItemSelected(MenuItem menuItem)
    {
    	ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuItem.getMenuInfo();
    	int groupPos = 0, childPos = 0;  
    	  int type = ExpandableListView  
    	    .getPackedPositionType(info.packedPosition); 

    	  if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {  
    	   groupPos = ExpandableListView  
    	     .getPackedPositionGroup(info.packedPosition);  
    	   childPos = ExpandableListView  
    	     .getPackedPositionChild(info.packedPosition);  
    	  }  
    	  
    	  DataMenuData contact = (DataMenuData) mAdapter.getChild(groupPos, childPos);  
    	  switch (menuItem.getItemId()) {  
	    	  case MENU_EDIT:
	    		  startEditItem(contact.getMenuId());
	    		  return true;
	    	  case MENU_COPY:
	    		  startCreateNewItem( contact.getMenuId() );
	    		  return true;
			  case MENU_MONEY_VIEW:  
	    			// 収支インテントを呼び出す
	    			// Intentの呼び出し
	    			Intent intent = new Intent(
	    					this, MoneyManActivity.class );

	    			// 台の名称で渡す。
	    			intent.putExtra(getString(R.string.key_money_machinename), contact.getHideString());

	    			startActivity( intent );				  
	    			return true;  
			  case MENU_DELETE:  
				  	deleteMachineInf(contact.getMenuId(), contact.getHideString());
				  	return true;
			  default:  
				  	return super.onContextItemSelected(menuItem);  
    	  }      	
    }
    private void startCreateNewItem(int iCpyId)
    {
    	if( iCpyId != -1 )
    	{
    		tblInf_MachineMain.setWherePhrase("_id=" + iCpyId );
    	}
    	else
    	{
    		tblInf_MachineMain.setWherePhrase(null);
    	}
		Intent intent = null;
		ArrayList<String> arrCantDpl = null;
		ArrayList<TableOpenInfo> arrTblInf = null;
		String strCantDpl[] = getResources().getStringArray(R.array.machine_cant_dpl);			    		
		arrCantDpl = new ArrayList<String>( Arrays.asList(strCantDpl) );
		
		intent = new Intent( MachineManActivity.this,
				RecordUpdateActivity.class );
		arrTblInf = new ArrayList<TableOpenInfo>();
		arrTblInf.add(tblInf_MachineMain);
		intent.putExtra(getString(R.string.key_update), 0);
		intent.putExtra(getString(R.string.key_tblLevel), 0);
		intent.putExtra(getString(R.string.key_id), -1 );
		intent.putExtra(getString(R.string.key_dlgTitle), 
				getString(R.string.McnTblDlgTitle) + getString(R.string.StrNewRegister) );
		intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.McnTblMainClmnName ));
		intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
		intent.putExtra(getString(R.string.key_addMsgId), getString(R.string.msg_new_machine_add));    			
		intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
		intent.putExtra(getString(R.string.key_tblSubClmn1), "");
		intent.putExtra(getString(R.string.key_tblSubClmn2), "");
		intent.putExtra(getString(R.string.key_tblSubClmn3), "");
		//intent.putExtra("DBHelper", dbMachine);
		//startActivity( intent );
		if( iRequestResult == 1 )
		{
			intent.putExtra(getString(R.string.key_request_result),
					1);
			startActivityForResult( intent,
					REQUEST_CODE );
		}
		else
		{		    			
			startActivity( intent );
		}		
    }
    private void startEditItem( int id )
    {
		// レコード更新インテントを呼び出す
    	tblInf_MachineMain.setWherePhrase("_id=" + id );
		// Intentの呼び出し
		Intent intent = new Intent( this, RecordUpdateActivity.class );
		ArrayList<TableOpenInfo> arrTblInf = new ArrayList<TableOpenInfo>();
		arrTblInf.add(tblInf_MachineMain);

		intent.putExtra(getString(R.string.key_update), 1);
		intent.putExtra(getString(R.string.key_tblLevel), 0);
		intent.putExtra(getString(R.string.key_id), id );
		intent.putExtra(getString(R.string.key_dlgTitle), 
				getString(R.string.McnTblDlgTitle) + getString(R.string.StrUpdate) );
		intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.McnTblMainClmnName ));
		intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
		intent.putExtra(getString(R.string.key_updMsgId), getString(R.string.msg_upd_machine));
		intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
		intent.putExtra(getString(R.string.key_tblSubClmn1), "");
		intent.putExtra(getString(R.string.key_tblSubClmn2), "");
		intent.putExtra(getString(R.string.key_tblSubClmn3), "");
		//intent.putExtra("DBHelper", dbMachine);
		startActivity( intent );    	
    }	
    
    private void deleteMachineInf(int deleteId, String deleteItem )
    {
		// 子供
		iDeleteId = deleteId;
		strDelete = deleteItem;
		
		// 収支管理に使われていれば、代わりの台をどれにするか選択させる
		SQLiteDatabase db = dbMachine.getWritableDatabase();
		Cursor cr = db.query( "MoneyMan",
				new String[]{ "Count(*)" }
				,"McnId=" + iDeleteId
				,null, null, null, null);
		
		cr.moveToFirst();
		if( cr.getInt(0) <= 0 )
		{
			new AlertDialog.Builder(this)
			.setTitle(strDelete)//getString(R.string.DLG_TITLE_CONFIRM))
			.setMessage("台データを削除しますが、よろしいですか？")
			.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SQLiteDatabase db = dbMachine.getWritableDatabase();
						// どこにも使われていなければ、削除する
						db.beginTransaction();
						db.delete( tblInf_MachineMain.getTblName(),
								"_id=" + iDeleteId, null);
						db.setTransactionSuccessful();
						db.endTransaction();
						strMessage = "台"+ strDelete+"削除しました。";
						updateAdapter();
						// Toast.makeText( MachineManActivity.this, "削除しました。", Toast.LENGTH_SHORT );						
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
		}
		else
		{
			// どこかで使われている場合、代わりの台を選択させ、削除と更新が行われてよいか聞く
			new AlertDialog.Builder(MachineManActivity.this)
			.setTitle(strDelete)//getString(R.string.DLG_TITLE_CONFIRM))
			.setMessage("削除しようとしている台は既に使用されているので、台削除後、その台が使用されているデータは他の台に置き換えが必要ですが、よろしいですか？")
			.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 代わりの台をどれにするか選択させる
						SQLiteDatabase db = dbMachine.getWritableDatabase();
						Cursor cr = db.rawQuery("select ma._id, ma.MachineName from MachineMan ma where ma._id <> " 
								+ iDeleteId + " order by ma.MachineName", null );
						
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								MachineManActivity.this);
						
						String[] strNms = new String[cr.getCount()];
						iMcnIds = new int[cr.getCount()];
						
						cr.moveToFirst();
						for( int i=0; i < cr.getCount(); i++ )
						{
							// 表示項目の配列
							iMcnIds[i] = cr.getInt(0);
							strNms[i] = cr.getString(1);
							cr.moveToNext();
						}
						
						// タイトルを設定
						alertDialogBuilder.setTitle("削除される台と入れ替える台を選択");
						// 表示項目とリスナの設定
						alertDialogBuilder.setItems(strNms,
						        new DialogInterface.OnClickListener() {
						            @Override
						            public void onClick(DialogInterface dialog, int which) {
						                // 項目が選択されたときの処理
						            	SQLiteDatabase db = dbMachine.getWritableDatabase();
			    						db.beginTransaction();
			    						
			    						ContentValues cv = new ContentValues();
			    						
			    						cv.put("McnId", iMcnIds[which]);
			    						
			    						// 収支情報更新
			    						db.update( "MoneyMan",
			    								cv, "McnId=" + iDeleteId, null);
			    						
			    						// 台情報削除
			    						db.delete( tblInf_MachineMain.getTblName(),
			    								"_id=" + iDeleteId, null);
			    						
			    						db.setTransactionSuccessful();
			    						db.endTransaction();
			    						//Toast.makeText( MachineManActivity.this, "削除しました。", Toast.LENGTH_LONG );

			    						strMessage = "台"+ strDelete+"を削除し、台" 
			    						+TableControler.getMcnNameFromMcnId( MachineManActivity.this, dbMachine, String.valueOf( iMcnIds[which]))
			    						+"と入れ替えました。";
			    						updateAdapter();
						    	
						            }
						});
																	
						// ダイアログを表示
						alertDialogBuilder.create().show();    			    						
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
		}    						
		
		return;    	
    }

    // リスト選択イベントの処理
    public boolean onChildClick(ExpandableListView parent,
    		View view, int groupPosition, int childPosition, long id) {
		String strCantDpl[] = getResources().getStringArray(R.array.machine_cant_dpl);
		arrCantDpl = new ArrayList<String>( Arrays.asList(strCantDpl) );
		
		// アダプタを取得
		ExpandableListAdapter adapter = parent.getExpandableListAdapter();
		//Toast.makeText(this,Integer.toString(position),Toast.LENGTH_SHORT).show();
		switch( childPosition )
		{
		/*
		// TODO:新規と更新で同じ設定値がたくさんあるが・・・まとめた方がよくない？
		case 0:
			// 新規追加
			// レコード追加インテントを呼び出す
			// Intentの呼び出し
			intent = new Intent( this, RecordUpdateActivity.class );
			arrTblInf = new ArrayList<TableOpenInfo>();
			tblInf_MachineMain.setWherePhrase(null);
			arrTblInf.add(tblInf_MachineMain);
			intent.putExtra(getString(R.string.key_tblLevel), 0);
			intent.putExtra(getString(R.string.key_id), -1 );
			intent.putExtra(getString(R.string.key_dlgTitle), 
					getString(R.string.McnTblDlgTitle) + getString(R.string.StrNewRegister) );
			intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.McnTblMainClmnName ));
			intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
			intent.putExtra(getString(R.string.key_addMsgId), getString(R.string.msg_new_machine_add));    			
			intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
			intent.putExtra(getString(R.string.key_tblSubClmn1), "");
			intent.putExtra(getString(R.string.key_tblSubClmn2), "");
			intent.putExtra(getString(R.string.key_tblSubClmn3), "");
			//intent.putExtra("DBHelper", dbMachine);
			startActivity( intent );    			
			break;*/
		default:
			DataMenuData item = (DataMenuData)adapter.getChild(groupPosition, childPosition);

			// 結果取得用の起動の場合、結果を返して終了
			if( iRequestResult == 1 )
			{
				// 返すデータ(Intent&Bundle)の作成
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt(getString(R.string.key_machine_id), item.getMenuId());
				data.putExtras(bundle);

				setResult(RESULT_OK, data);

				finish();
				break;
			}
			
			// 普通の起動の場合、更新
			startEditItem(item.getMenuId());
			// レコード更新インテントを呼び出す
			/*
			// Intentの呼び出し
			tblInf_MachineMain.setWherePhrase("_id=" + item.getMenuId() );
			intent = new Intent( this, RecordUpdateActivity.class );
			arrTblInf = new ArrayList<TableOpenInfo>();
			arrTblInf.add(tblInf_MachineMain);

			intent.putExtra(getString(R.string.key_update), 1);
			intent.putExtra(getString(R.string.key_tblLevel), 0);
			intent.putExtra(getString(R.string.key_id), item.getMenuId() );
			intent.putExtra(getString(R.string.key_dlgTitle), 
					getString(R.string.McnTblDlgTitle) + getString(R.string.StrUpdate) );
			intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.McnTblMainClmnName ));
			intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
			intent.putExtra(getString(R.string.key_updMsgId), getString(R.string.msg_upd_machine));
			intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
			intent.putExtra(getString(R.string.key_tblSubClmn1), "");
			intent.putExtra(getString(R.string.key_tblSubClmn2), "");
			intent.putExtra(getString(R.string.key_tblSubClmn3), "");
			//intent.putExtra("DBHelper", dbMachine);
			startActivity( intent );
			    */		
			break;
		}
    		
    	return false;
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
    	if( currentMode == MODE_SORT )
    	{
    		updateAdapterSort();
    		return;
    	}
    	String strWherePhrase = "";
    	String strMakerName = null;
    	Bundle extras = getIntent().getExtras();
    	String strTitleAddString = "";
    	if( extras != null )
    	{
    		if( extras.containsKey(getString(R.string.key_machine_makername)) )
    		{
    			strMakerName = extras.getString(getString(R.string.key_machine_makername));
    		}
    	}
    	String strAdd_ex = "";
    	if( PatiLogger.isExistLastFinishNotUpdData( this, getString(R.string.McnTblName) ) )
    	{
    		strAdd_ex = getString(R.string.add_ex);
    	}    	
        Button btnAdd = (Button)this.findViewById(R.id.btnMachineAdd);
        btnAdd.setText( getString(R.string.add) + strAdd_ex );
    	
    	// 検索条件の設定+タイトルバーへの表示
    	// メーカー
    	if( strMakerName != null )
    	{
    		if( strWherePhrase.length() > 0 )
    		{
    			strWherePhrase += " and ";
    		}
    		if( false == strMakerName.equals( getString(R.string.noset)))
    		{
	    		strWherePhrase += "MakerId=" +TableControler.getMakerIdFromMakerName(this,
	    				dbMachine, strMakerName) + "";
    		}
    		else
    		{
	    		strWherePhrase += "( MakerId < 0 or MakerId is null )";
    		}
    		if( strTitleAddString.length() > 0 )
    		{
    			strTitleAddString += ",";
    		}
			strTitleAddString += getString(R.string.caption_maker) + strMakerName;
    	}
    	
    	// 2012/5/15 台の名称での検索
    	if( false == strSrchWord.equals(""))
    	{
    		if( strWherePhrase.length() > 0 )
    		{
    			strWherePhrase += " and ";
    		}
    		strWherePhrase += "MachineName like '%" + strSrchWord + "%'";
    		strTitleAddString += getString(R.string.caption_machine) + "名に'" + strSrchWord + "'を含む";
    	}
    	
    	// 最後に、タイトルに付加する文字列があれば括弧でくくる
    	if( strTitleAddString.length() > 0 )
    	{
    		strTitleAddString = "(" + strTitleAddString + ")";
    	}
    	tblInf_MachineMain.setWherePhrase(strWherePhrase);

    	// 親要素  
        groupData = new ArrayList<String>();
        // 子要素  
        List<List<DataMenuData>> childData 
        	= new ArrayList<List<DataMenuData>>();
        
   		// リストの最初は「新規追加」
        /*
        this.list = new ArrayList<DataMenuData>();
    	DataMenuData data = new DataMenuData();
    	data.setMenuString( getString(R.string.add) );
       	data.setResId( android.R.drawable.ic_menu_add );
    	list.add( data );
		*/
        
    	// データベースから台の名称のString配列を取得する
    	
    	// queryでSELECTを実行
    	// 台の名称の最初の1文字を取得
    	String[] columns = {
    			"COUNT(*)",
    			"SUBSTR(" + getString(R.string.McnTblMainClmnName) + ", 1, 1)"
    			};
    	String selection = tblInf_MachineMain.getWherePhrase();
    	String groupBy = "SUBSTR(" + getString(R.string.McnTblMainClmnName) + ", 1, 1)";
    	SQLiteDatabase db = dbMachine.getReadableDatabase();
    	try {
	    	Cursor cPrt1 = db.query(tblInf_MachineMain.getTblName(),
	    			columns, selection, null, groupBy, null, "SUBSTR(" + getString(R.string.McnTblMainClmnName) + ", 1, 1)");
	    	cPrt1.moveToFirst();
	    	for (int i = 0; i < cPrt1.getCount(); i++) {
	    		//Map<String, String> curMap = new HashMap<String, String>();
	    		String strCurParentName = "";
	    		/*
	    		if( cPrt1.getInt(0) > MAX_DIVIDE_CNT )
	    		{
	    			// 名前の1文字目でも分割し切れていないので、さらに2文字目までで分割する
	    	    	Cursor cPrt2 = db.query(tblInf_MachineMain.getTblName(),
	    	    			columns, selection, null, groupBy, null, "SUBSTR(" 
	    	    			+ getString(R.string.McnTblMainClmnName) + ", 1, 2)");
	    	    	cPrt2.moveToFirst();
	    	    	for (int i = 0; i < cPrt1.getCount(); i++) {
	    	    		Map<String, String> curMap = new HashMap<String, String>();
	    	    		String strCurParentName = "";
	    	    	}
	    		}
	    		else
	    		{*/
	    			strCurParentName = cPrt1.getString(1);
	    			//curMap.put( GRP_NAME_KEY, strCurParentName );
	    			groupData.add( strCurParentName );//curMap );
	    		//}
	    		List<DataMenuData> children = new ArrayList<DataMenuData>();
	    		String[] columnsChild = {"_id",getString(R.string.McnTblMainClmnName),
	        			getString(R.string.McnTblSubClmnName1)};
	        	String selectionChild = "SUBSTR(" 
	        		+ getString(R.string.McnTblMainClmnName) + ", 1, " 
	        		+ strCurParentName.length() + " ) = '" + strCurParentName + "'"
	        		;
	        	if( null != tblInf_MachineMain.getWherePhrase() && tblInf_MachineMain.getWherePhrase().length() > 0 )
	        	{
	        		selectionChild += " and " + tblInf_MachineMain.getWherePhrase();
	        	}
	        	//SQLiteDatabase db = dbMachine.getReadableDatabase();
	        	
	        	try {
	    	    	Cursor cChild = db.query(tblInf_MachineMain.getTblName(),
	    	    			columnsChild, selectionChild, null, null, null, "MachineName");
	    	    	cChild.moveToFirst();
	    	    	//ids = new int[cChild.getCount()];
	    	    	//item = new String[cChild.getCount()];
	    	    	//subclmn1 = new int[cChild.getCount()];
	    	    	for (int j = 0; j < cChild.getCount(); j++) {
	    	    		//ids[j] = cChild.getInt(0);
	    	    	    //item[j] = cChild.getString(1);
	    	    	    //subclmn1[j] = cChild.getInt(2);
	    	    	    // Map<String, String> curChildMap = new HashMap<String, String>();
	    	    	    //curChildMap.put( CHILD_NAME_KEY, cChild.getString(1) );
	    	    	    DataMenuData data = new DataMenuData();
	    	        	data.setMenuString( cChild.getString(1) );
	    	        	data.setHideString( cChild.getString(1) );
	    	        	data.setMenuId(cChild.getInt(0));
	    	        	if( cChild.getInt(2) == 0 )
	    	        	{
	    		        	// アイコンの設定
	    			        data.setResId( R.drawable.machine_2_toriaezu );
	    	        	}
	    	        	else if( cChild.getInt(2) == 1 )
	    	        	{
	    	        		data.setResId( R.drawable.patinko_machine );
	    	        	}
	    	        	else
	    	        	{
	    	        		data.setResId( R.drawable.icon );
	    	        	}
	    	    	    
	    	    	    children.add( data );
	    	    	    cChild.moveToNext();
	    	    	}
    	    	    childData.add( children );
	    	    	cChild.close();
	        	} catch( Exception e ) {
	        		e.printStackTrace();
	        	}
	    	    cPrt1.moveToNext();
	    	}
	    	cPrt1.close();
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
        // adapter 設定
    	/*
        mAdapter = new SimpleExpandableListAdapter(  
            this,  
            // 親要素  
            groupData,  
            // 親要素のレイアウト  
            android.R.layout.simple_expandable_list_item_1,  
            // 親要素のListで、表示するMapのKey  
            new String[] { GRP_NAME_KEY },  
            // 親要素レイアウト内での文字を表示する TextView の ID  
            new int[] { android.R.id.text1 },  
            // 子要素  
            childData,  
            // 子要素のレイアウト  
            android.R.layout.simple_expandable_list_item_1,  
            // 子要素のListで、表示するMapのKey  
            new String[] { CHILD_NAME_KEY },  
            // 子要素レイアウト内での文字を表示する TextView の ID  
            new int[] { android.R.id.text1 }  
        );  */
    	mAdapter = new DataExpandableListAdapter(
    				this,
    				groupData,
    				childData
    			);
        listView.setAdapter(mAdapter);      	
        listView.setFocusableInTouchMode(true);
        listView.setOnChildClickListener(this);//setOnItemClickListener(this);
        
        if( strMessage != null )
        {
        	Toast.makeText( MachineManActivity.this, strMessage, Toast.LENGTH_LONG ).show();
        	strMessage = null;
        }
        
        setTitle(strTitle + strTitleAddString);
    	//item = getResources().getStringArray(R.array.main_menu);
    	/*
    	if( item != null ) {
	    	for( int i=0; i < item.length; i++ ) {
	    		data = new DataMenuData();
	        	data.setMenuString( item[i] );
	        	data.setMenuId(ids[i]);
	        	if( subclmn1[i] == 0 )
	        	{
		        	// アイコンの設定
			        data.setResId( R.drawable.machine_2_toriaezu );
	        	}
	        	else if( subclmn1[i] == 1 )
	        	{
	        		data.setResId( R.drawable.patinko_machine );
	        	}
	        	else
	        	{
	        		data.setResId( R.drawable.icon );
	        	}
	        	list.add( data );
	    	}
    	}
    	// 配列アダプタの生成
    	//ArrayAdapter<String> adapter;
    	DataMenuAdapter adapter =
    		new DataMenuAdapter(this, R.layout.lst_menu_row, list); 
    	//adapter = new ArrayAdapter<String>( this,
    	// android.R.layout.simple_list_item_1,
    	// item );
    	listView.setAdapter(adapter);*/
    }
    /**
     * ソートモードの場合
     */
    private void updateAdapterSort() {

    	// 1グループ30件
    	final int SORT_MODE_GROUP_CNT = 30;
     	
    	String where = "";
    	tblInf_MachineMain.setWherePhrase(where);

    	// 親要素  
        groupData = new ArrayList<String>();
        // 子要素
        List<List<DataMenuData>> childData 
        	= new ArrayList<List<DataMenuData>>();
        
    	// グループとなる文字列の配列
        String[] groups = {
        	"最近打った台順(最近の30件)","勝ち額順(最近3ヶ月の上位30件)","負け額順(最近3ヶ月の上位30件)"
        };
    	
        // 台情報格納用クラス
        class ItemInfo
        {
        	String LastRegistDateTime;
        	int CashFlow;
        	int McnId;
        	/**
        	 * @return the lastRegistDateTime
        	 */
        	public String getLastRegistDateTime() {
        		return LastRegistDateTime;
        	}
        	/**
        	 * @param lastRegistDateTime the lastRegistDateTime to set
        	 */
        	public void setLastRegistDateTime(String lastRegistDateTime) {
        		LastRegistDateTime = lastRegistDateTime;
        	}
        	/**
        	 * @return the cashFlow
        	 */
        	public int getCashFlow() {
        		return CashFlow;
        	}
        	/**
        	 * @param cashFlow the cashFlow to set
        	 */
        	public void setCashFlow(int cashFlow) {
        		CashFlow = cashFlow;
        	}
        	/**
        	 * @return the mcnId
        	 */
        	public int getMcnId() {
        		return McnId;
        	}
        	/**
        	 * @param mcnId the mcnId to set
        	 */
        	public void setMcnId(int mcnId) {
        		McnId = mcnId;
        	}        	
        };
		// 現在日時を取得しておく
		Calendar now = Calendar.getInstance();
		Date d = now.getTime();
		d.setMonth( d.getMonth() - 3 );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String strDateBefore3Month = sdf.format(d);
        
        // 台ごとの勝ち負けの額をマップに取得する
        //TreeMap<Integer,Integer> mapCash = new TreeMap<Integer,Integer>();
        ArrayList<ItemInfo> arrCash = new ArrayList<ItemInfo>();
        
       	String[] columns = {"sum(CashFlow)","McnId","sum(ExpVal)"};
       	// 3ヶ月前以降の稼働のみ
    	String selection = "McnId is not null and WorkDate >= '" + strDateBefore3Month + "'";
    	String groupBy = "McnId";
    	String orderBy = "sum(CashFlow) desc";
    	SQLiteDatabase dbRead = dbMachine.getReadableDatabase();
    	try {
	    	Cursor c = dbRead.query("MoneyMan",
	    			columns, selection, null, groupBy, null, orderBy);
	    	if( 0 < c.getCount() )
	    	{
		    	c.moveToFirst();
		    	//int iCnt = 0;
		    	do
		    	{
		    		//iCnt++;
		    		// mapCash.put(c.getInt(2), c.getInt(1));
		    		ItemInfo ii = new ItemInfo();
		    		ii.setCashFlow(c.getInt(0));
		    		ii.setMcnId(c.getInt(1));
		    		arrCash.add( ii );
		    	} while( c.moveToNext() );
	    	}
		    c.close();
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
        // 最近登録された順に、台をマップに取得する
    	ArrayList<ItemInfo> arrRecent = new ArrayList<ItemInfo>();
       	String[] columnsRecent = {"McnId","max(WorkDate||WorkTime)"};
    	String groupByRecent = "McnId";
    	String orderByRecent = "WorkDate desc,WorkTime desc";
    	try {
	    	Cursor c = dbRead.query("MoneyMan",
	    			columnsRecent, selection, null, groupByRecent, null, orderByRecent);
	    	if( 0 < c.getCount() )
	    	{
		    	c.moveToFirst();
		    	int iCnt = 0;
		    	do {
		    		iCnt++;
		    		if( SORT_MODE_GROUP_CNT < iCnt )
		    		{
		    			break;
		    		}
		    		ItemInfo ii = new ItemInfo();
		    		ii.setLastRegistDateTime(c.getString(1));
		    		ii.setMcnId(c.getInt(0));
		    		arrRecent.add( ii );

		    	} while( c.moveToNext() );
	    	}
		    c.close();
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
    	dbRead.close();
    	
    	for (int i = 0; i < groups.length; i++) {
    		String strCurParentName = "";
    		strCurParentName = groups[i];
    		
    		// グループが2つ目の場合
    		if( i==1 )
    		{
    			if( 0 < arrCash.size() )
    			{
    				groupData.add( strCurParentName );
    			}
        		List<DataMenuData> children = new ArrayList<DataMenuData>();

        		for (int j = 0; j < arrCash.size() && j < SORT_MODE_GROUP_CNT; j++) {
	        	        	        		
		    	    DataMenuData data = new DataMenuData();
		    	    String mcnName = TableControler.getMcnNameFromMcnId(
		        			this, dbMachine, String.valueOf( arrCash.get(j).getMcnId() ) );
		        	data.setMenuString( ( j + 1 )+ "." + mcnName
		        			);
		        	data.setRightString(arrCash.get(j).getCashFlow() + "円" );
    	        	data.setHideString( mcnName );
		        	
		        	String strPatiTypeId = TableControler.getPatiTypeIdFromMcnId(
		        			this, dbMachine, String.valueOf( arrCash.get(j).getMcnId() ) 
		        			);
		        	Integer patiTypeId = -1;
		        	if( strPatiTypeId.isEmpty() == false )
		        	{
		        		patiTypeId = Integer.parseInt( strPatiTypeId );
		        	}
		        	data.setMenuId(arrCash.get(j).getMcnId() );
		        	if( patiTypeId == 0 )
		        	{
			        	// アイコンの設定
				        data.setResId( R.drawable.machine_2_toriaezu );
		        	}
		        	else if( patiTypeId == 1 )
		        	{
		        		data.setResId( R.drawable.patinko_machine );
		        	}
		        	else
		        	{
		        		data.setResId( R.drawable.icon );
		        	}
		    	    
		    	    children.add( data );
	    		}
        	    childData.add( children );
    		}
    		// グループが3つ目の場合
    		if( i==2 )
    		{
    			if( 0 < arrCash.size() )
    			{
    				groupData.add( strCurParentName );
    			}
        		List<DataMenuData> children = new ArrayList<DataMenuData>();
    			
        		int k=0;
	    		for (int j = arrCash.size()-1; 0 <= j && arrCash.size()-30 <= j; j--, k++) {
	        	        	        		
		    	    DataMenuData data = new DataMenuData();
		    	    String mcnName = TableControler.getMcnNameFromMcnId(
		        			this, dbMachine, String.valueOf( arrCash.get(j).getMcnId() ) );
		        	data.setMenuString( ( k + 1 )+ "." + mcnName
		        			);
		        	data.setRightString(arrCash.get(j).getCashFlow() + "円" );
    	        	data.setHideString( mcnName );
		        	String strPatiTypeId = TableControler.getPatiTypeIdFromMcnId(
		        			this, dbMachine, String.valueOf( arrCash.get(j).getMcnId() ) 
		        			);
		        	Integer patiTypeId = -1;
		        	if( strPatiTypeId.isEmpty() == false )
		        	{
		        		patiTypeId = Integer.parseInt( strPatiTypeId );
		        	}
		        	data.setMenuId(arrCash.get(j).getMcnId() );

		        	if( patiTypeId == 0 )
		        	{
			        	// アイコンの設定
				        data.setResId( R.drawable.machine_2_toriaezu );
		        	}
		        	else if( patiTypeId == 1 )
		        	{
		        		data.setResId( R.drawable.patinko_machine );
		        	}
		        	else
		        	{
		        		data.setResId( R.drawable.icon );
		        	}
		    	    
		    	    children.add( data );
	    		}
	    	    childData.add( children );
    		}
    		// グループが1つ目の場合
    		if( i==0 )
    		{
    			if( 0 < arrRecent.size() )
    			{
    				groupData.add( strCurParentName );
    			}
        		List<DataMenuData> children = new ArrayList<DataMenuData>();
    			
	    		for (int j = 0; j < arrRecent.size() && j < SORT_MODE_GROUP_CNT; j++) {
	        	        	        		
		    	    DataMenuData data = new DataMenuData();
		    	    String strAdd = arrRecent.get(j).getLastRegistDateTime();
		    	    if( strAdd != null && 12 == strAdd.length() )
		    	    {
	        			strAdd = " " + TableControler.getFmtDate( strAdd.substring(0,8) ) 
	        			+ " " + TableControler.getFmtTime( strAdd.substring(8) );   	    	
		    	    }
		        	data.setMenuString( ( j + 1 ) + "." + TableControler.getMcnNameFromMcnId(
		        			this, dbMachine, String.valueOf( arrRecent.get(j).getMcnId() ) 
		        			) + strAdd
		        			);
		        	String strPatiTypeId = TableControler.getPatiTypeIdFromMcnId(
		        			this, dbMachine, String.valueOf( arrRecent.get(j).getMcnId() ) 
		        			);
		        	Integer patiTypeId = -1;
		        	if( strPatiTypeId.isEmpty() == false )
		        	{
		        		patiTypeId = Integer.parseInt( strPatiTypeId );
		        	}
		        	data.setMenuId(arrRecent.get(j).getMcnId() );

		        	if( patiTypeId == 0 )
		        	{
			        	// アイコンの設定
				        data.setResId( R.drawable.machine_2_toriaezu );
		        	}
		        	else if( patiTypeId == 1 )
		        	{
		        		data.setResId( R.drawable.patinko_machine );
		        	}
		        	else
		        	{
		        		data.setResId( R.drawable.icon );
		        	}
		    	    
		    	    children.add( data );
	    		}
	    	    childData.add( children );
    		}    		
    	}
    	
    	mAdapter = new DataExpandableListAdapter(
    				this,
    				groupData,
    				childData
    			);
        listView.setAdapter(mAdapter);      	
        listView.setFocusableInTouchMode(true);
        listView.setOnChildClickListener(this);//setOnItemClickListener(this);
        
        if( strMessage != null )
        {
        	Toast.makeText( MachineManActivity.this, strMessage, Toast.LENGTH_LONG ).show();
        	strMessage = null;
        }
        
        setTitle(strTitle);
    }

    /*
     * オプションメニューの作成
     */
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean ret = super.onCreateOptionsMenu(menu);
    	menu.add( 0, Menu.FIRST, Menu.NONE, "全て開く").setIcon(android.R.drawable.ic_menu_gallery);
    	menu.add( 0, Menu.FIRST + 1, Menu.NONE, "全て閉じる").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	menu.add( 0, Menu.FIRST + 2, Menu.NONE, "検索").setIcon(android.R.drawable.ic_menu_search);
    	return ret;
    }
    
    /**
     * オプションメニューの選択イベント
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case Menu.FIRST:
        	// 全て開く
            // すべて開いた状態に
            for( int k=0; k < groupData.size(); k++ )
            {
            	listView.expandGroup(k);
            }
            return true;
     
        case Menu.FIRST + 1:
        	// 全て閉じる
            // すべて閉じた状態に
            for( int k=0; k < groupData.size(); k++ )
            {
            	listView.collapseGroup(k);
            }
            return true;
        case Menu.FIRST + 2:
        	filterListItem();
        /*
            final EditText edit = new EditText(this);
            edit.setText(strSrchWord);
            edit.setSingleLine(true);
            AlertDialog alertDialog = new AlertDialog.Builder(this).
            	setTitle("台の名前の検索条件設定")
            	.setView(edit)
            	.setMessage("以下の入力値を含む名前の台を検索します。")
            	.setPositiveButton("検索する", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String editStr = edit.getText().toString();
                        strSrchWord = editStr;
                        updateAdapter();
                    }
                })
            	.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
            	.create();
            alertDialog.show();
            */
        	return true;
        }
        return false;
    }
    
    void filterListItem()
    {
        final EditText edit = new EditText(this);
        edit.setText(strSrchWord);
        edit.setSingleLine(true);
        AlertDialog alertDialog = new AlertDialog.Builder(this).
        	setTitle("台の名前の検索条件設定")
        	.setView(edit)
        	.setMessage("以下の入力値を含む名前の台を検索します。")
        	.setPositiveButton("検索する", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String editStr = edit.getText().toString();
                    strSrchWord = editStr;
                    updateAdapter();
                }
            })
        	.setNegativeButton("やめる", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
        	.create();
        alertDialog.show();
    	
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case REQUEST_CODE:
			// RecordUpdatorから新規作成の結果が戻ってきた。
			if (resultCode == RESULT_OK)
			{
				// 返すデータ(Intent&Bundle)の作成
				Intent dataRet = new Intent();
				Bundle bundle = new Bundle();
				Bundle bundleRet = data.getExtras();
				int RetId = bundleRet.getInt(getString(R.string.key_request_id));
				bundle.putInt(getString(R.string.key_machine_id), RetId );
				dataRet.putExtras(bundle);

				setResult(RESULT_OK, dataRet);

				finish();				
			} 
			break;
		default:
			break;
		}		
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if( event.getKeyCode() == KeyEvent.KEYCODE_SEARCH )
		{
			// 検索ボタンが押下された
			// 検索機能を機能する
			filterListItem();
		}
		
		return super.dispatchKeyEvent(event);
	}

}
