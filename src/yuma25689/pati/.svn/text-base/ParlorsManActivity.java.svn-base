package yuma25689.pati;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import mediba.ad.sdk.android.openx.MasAdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
//import android.widget.ListView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class ParlorsManActivity extends Activity 
	implements AdapterView.OnItemClickListener, ExpandableListView.OnChildClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	private ExpandableListView listView;
	//private ListView listView;
	private ArrayList<MainMenuData> list = null;
	
	private final int MENU_MONEY_VIEW = 1;
	private final int MENU_DELETE = 2;
	private final int MENU_EDIT = 3;
	private final int MENU_COPY = 4;
	private String strTitle = "";
	ArrayList<String> arrCantDpl = null;
	
	private TableOpenInfo tblInf_ParlorMain = new TableOpenInfo();
	private DBHelper dbParlor = null;
	private int iRequestResult = 0;

    private final int REQUEST_CODE = 102;
	
	private int iDeleteId = 0;
	private String strDelete = null;
    private int[] iParlorIds = null;
    List<String> groupData;
    private String strMessage = null;
	private String strSrchWord = "";

	private ExpandableListAdapter mAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parlor_main);
        strTitle = getString(R.string.DlgTitle_ParlorList);
        commLogic.init(this);

        Bundle extras = getIntent().getExtras();
        if( extras != null )
        {
	        // 結果を返却することを求めて起動されたかどうかを表す
	        iRequestResult = extras.getInt(getString(R.string.key_request_result));
	        strTitle = getString(R.string.DlgTitle_ParlorSelect);
        }
        setTitle(strTitle);

    	String strAdd_ex = "";
    	if( PatiLogger.isExistLastFinishNotUpdData( this, getString(R.string.ParlorTblName) ) )
    	{
    		strAdd_ex = getString(R.string.add_ex);
    	}
        
        Button btnAdd = (Button)this.findViewById(R.id.btnParlorAdd);
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
        
        listView = (ExpandableListView) this.findViewById(R.id.lstParlorMain);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);
        TextView tvEmpty = (TextView) this.findViewById(R.id.txtEmpty);
        listView.setEmptyView( tvEmpty );	        

    	tblInf_ParlorMain.setDBName( getString(R.string.db_name) );
    	tblInf_ParlorMain.setTblName( getString(R.string.ParlorTblName) );
    	tblInf_ParlorMain.setColumnsCreateText( getString(R.string.ParlorTblDef) );
    	tblInf_ParlorMain.setCursorFactory(null);
    	tblInf_ParlorMain.setDBVer( Integer.parseInt( getString(R.string.db_ver) ) );
    	tblInf_ParlorMain.setExcptColumnSpec(getString(R.string.ParlorTblROClmns));
    	tblInf_ParlorMain.setWherePhrase(null);
    	dbParlor = new PatiManDBHelper(
        	this
        );
        		//, 
        		//tblInf_ParlorMain
        //);
    	/*
        libAdMaker AdMaker = (libAdMaker)findViewById(R.id.admakerview);
        AdMaker.setActivity(this);
        AdMaker.siteId = "2318";
        AdMaker.zoneId = "6229";
        AdMaker.setUrl("http://images.ad-maker.info/apps/g6j7xncu5e83.html");
        AdMaker.start();		
    	*/
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
				  MainMenuData contact = (MainMenuData) mAdapter.getChild(
						  groupPosition, childPosition);
				  menu.setHeaderTitle(contact.getMenuString());
				  menu.add(0, MENU_EDIT, 0, "編集");
				  menu.add(0, MENU_COPY, 1, "これをコピーして新規作成");
				  menu.add(0, MENU_MONEY_VIEW, 2, "この店の収支一覧");
				  menu.add(0, MENU_DELETE, 3, "店情報を削除");
			  }
         }

        });
    }
    private void startCreateNewItem(int iCpyId)
    {
    	if( iCpyId != -1 )
    	{
    		tblInf_ParlorMain.setWherePhrase("_id=" + iCpyId );
    	}
    	else
    	{
    		tblInf_ParlorMain.setWherePhrase(null);
    	}
		Intent intent = null;
		ArrayList<String> arrCantDpl = null;
		ArrayList<TableOpenInfo> arrTblInf = null;
		String strCantDpl[] = getResources().getStringArray(R.array.parlor_cant_dpl);			    		
		arrCantDpl = new ArrayList<String>( Arrays.asList(strCantDpl) );
		
		intent = new Intent( ParlorsManActivity.this,
				RecordUpdateActivity.class );
		arrTblInf = new ArrayList<TableOpenInfo>();
		arrTblInf.add(tblInf_ParlorMain);
		intent.putExtra(getString(R.string.key_update), 0);
		intent.putExtra(getString(R.string.key_tblLevel), 0);
		intent.putExtra(getString(R.string.key_id), -1 );
		intent.putExtra(getString(R.string.key_dlgTitle), 
				getString(R.string.ParlorTblDlgTitle) + getString(R.string.StrNewRegister) );
		intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.ParlorTblMainClmnName ));
		intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
		intent.putExtra(getString(R.string.key_addMsgId), getString(R.string.msg_new_parlor_add));    			
		intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
		intent.putExtra(getString(R.string.key_tblSubClmn1), "");
		intent.putExtra(getString(R.string.key_tblSubClmn2), "");
		intent.putExtra(getString(R.string.key_tblSubClmn3), "");
		//intent.putExtra("DBHelper", dbParlor);
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
    	tblInf_ParlorMain.setWherePhrase("_id=" + id );
		// Intentの呼び出し
		Intent intent = new Intent( this, RecordUpdateActivity.class );
		ArrayList<TableOpenInfo> arrTblInf = new ArrayList<TableOpenInfo>();
		arrTblInf.add(tblInf_ParlorMain);

		intent.putExtra(getString(R.string.key_update), 1);
		intent.putExtra(getString(R.string.key_tblLevel), 0);
		intent.putExtra(getString(R.string.key_id), id );
		intent.putExtra(getString(R.string.key_dlgTitle), 
				getString(R.string.ParlorTblDlgTitle) + getString(R.string.StrUpdate) );
		intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.ParlorTblMainClmnName ));
		intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
		intent.putExtra(getString(R.string.key_updMsgId), getString(R.string.msg_upd_parlor));
		intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
		intent.putExtra(getString(R.string.key_tblSubClmn1), "");
		intent.putExtra(getString(R.string.key_tblSubClmn2), "");
		intent.putExtra(getString(R.string.key_tblSubClmn3), "");
		//intent.putExtra("DBHelper", dbParlor);
		startActivity( intent );    }	
    
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
    	  
    	  MainMenuData contact = (MainMenuData) mAdapter.getChild(groupPos, childPos);  
    	  switch (menuItem.getItemId()) { 
	    	  case MENU_EDIT:
	    		  startEditItem(contact.getMenuId());
	    		  return true;
	    	  case MENU_COPY:
	    		  startCreateNewItem(contact.getMenuId());
	    		  return true;
			  case MENU_MONEY_VIEW:  
	    			// 収支インテントを呼び出す
	    			// Intentの呼び出し
	    			Intent intent = new Intent(
	    					this, MoneyManActivity.class );

	    			// 店の名称で渡す。
	    			intent.putExtra(getString(R.string.key_money_parlorname), contact.getMenuString());

	    			startActivity( intent );				  
	    			return true;  
			  case MENU_DELETE:  
				  	deleteParlorInf(contact.getMenuId(),contact.getMenuString());
				  	return true;
			  default:  
				  	return super.onContextItemSelected(menuItem);  
    	  }      	
    }
    
    // リスト選択イベントの処理
    public void onItemClick(AdapterView<?> parent,
    		View view, int position, long id ) {
    	try
    	{
    		Intent intent = null;
			ArrayList<String> arrCantDpl = null;
			ArrayList<TableOpenInfo> arrTblInf = null;
			String strCantDpl[] = getResources().getStringArray(R.array.parlor_cant_dpl);
			arrCantDpl = new ArrayList<String>( Arrays.asList(strCantDpl) );
			
			//Toast.makeText(this,Integer.toString(position),Toast.LENGTH_SHORT).show();
    		switch( position )
    		{
    		// TODO:新規と更新で同じ設定値がたくさんあるが・・・まとめた方がよくない？
    		case 0:
    			// 新規追加
    			// レコード追加インテントを呼び出す
    			// Intentの呼び出し
    			intent = new Intent( this, RecordUpdateActivity.class );
    			arrTblInf = new ArrayList<TableOpenInfo>();
    			tblInf_ParlorMain.setWherePhrase(null);
    			arrTblInf.add(tblInf_ParlorMain);
    			intent.putExtra(getString(R.string.key_update), 0);
    			intent.putExtra(getString(R.string.key_tblLevel), 0);
    			intent.putExtra(getString(R.string.key_id), -1 );
    			intent.putExtra(getString(R.string.key_dlgTitle), 
    					getString(R.string.ParlorTblDlgTitle) + getString(R.string.StrNewRegister) );
    			intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.ParlorTblMainClmnName ));
    			intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
    			intent.putExtra(getString(R.string.key_addMsgId), getString(R.string.msg_new_parlor_add));
    			intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
    			intent.putExtra(getString(R.string.key_tblSubClmn1), "");
    			intent.putExtra(getString(R.string.key_tblSubClmn2), "");
    			intent.putExtra(getString(R.string.key_tblSubClmn3), "");
    			//intent.putExtra("DBHelper", dbParlor);
    			startActivity( intent );    			
    			break;
    		default:
    			MainMenuData item = list.get(position);
    			
    			// 結果取得用の起動の場合、結果を返して終了
    			if( iRequestResult == 1 )
    			{
					// 返すデータ(Intent&Bundle)の作成
					Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt(getString(R.string.key_parlor_id), item.getMenuId());
					data.putExtras(bundle);

					setResult(RESULT_OK, data);

					finish();
					break;
    			}
    			
    			// 普通の起動の場合、更新
    			
    			// レコード更新インテントを呼び出す
    			// Intentの呼び出し
    			startEditItem( item.getMenuId() );
    			/*
    			intent = new Intent( this, RecordUpdateActivity.class );
    			arrTblInf = new ArrayList<TableOpenInfo>();

    			tblInf_ParlorMain.setWherePhrase("_id=" + item.getMenuId() );
    			arrTblInf.add(tblInf_ParlorMain);

    			intent.putExtra(getString(R.string.key_tblLevel), 0);
    			intent.putExtra(getString(R.string.key_id), item.getMenuId() );
    			intent.putExtra(getString(R.string.key_dlgTitle), 
    					getString(R.string.ParlorTblDlgTitle) + getString(R.string.StrUpdate) );
    			intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.ParlorTblMainClmnName ));
    			intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
    			intent.putExtra(getString(R.string.key_updMsgId), getString(R.string.msg_upd_parlor));
    			intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
    			intent.putExtra(getString(R.string.key_tblSubClmn1), "");
    			intent.putExtra(getString(R.string.key_tblSubClmn2), "");
    			intent.putExtra(getString(R.string.key_tblSubClmn3), "");
    			//intent.putExtra("DBHelper", dbParlor);
    			startActivity( intent );    			
    			*/
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

    // リスト選択イベントの処理
    public boolean onChildClick(ExpandableListView parent,
    		View view, int groupPosition, int childPosition, long id) {
		Intent intent = null;
		ArrayList<String> arrCantDpl = null;
		ArrayList<TableOpenInfo> arrTblInf = null;
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
			intent = new Intent( this, yuma25689.pati_adv.RecordUpdateActivity.class );
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
			MainMenuData item = (MainMenuData)adapter.getChild(groupPosition, childPosition);

			// 結果取得用の起動の場合、結果を返して終了
			if( iRequestResult == 1 )
			{
				// 返すデータ(Intent&Bundle)の作成
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt(getString(R.string.key_parlor_id), item.getMenuId());
				data.putExtras(bundle);

				setResult(RESULT_OK, data);

				finish();
				break;
			}
			
			// 普通の起動の場合、更新

			// レコード更新インテントを呼び出す
			// Intentの呼び出し
			tblInf_ParlorMain.setWherePhrase("_id=" + item.getMenuId() );
			intent = new Intent( this, RecordUpdateActivity.class );
			arrTblInf = new ArrayList<TableOpenInfo>();
			arrTblInf.add(tblInf_ParlorMain);

			intent.putExtra(getString(R.string.key_update), 1);
			intent.putExtra(getString(R.string.key_tblLevel), 0);
			intent.putExtra(getString(R.string.key_id), item.getMenuId() );
			intent.putExtra(getString(R.string.key_dlgTitle), 
					getString(R.string.McnTblDlgTitle) + getString(R.string.StrUpdate) );
			intent.putExtra(getString(R.string.key_tblMainClmn), getString(R.string.ParlorTblMainClmnName ));
			intent.putExtra(getString(R.string.key_cantDpl), arrCantDpl);
			intent.putExtra(getString(R.string.key_updMsgId), getString(R.string.msg_upd_parlor));
			intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
			intent.putExtra(getString(R.string.key_tblSubClmn1), "");
			intent.putExtra(getString(R.string.key_tblSubClmn2), "");
			intent.putExtra(getString(R.string.key_tblSubClmn3), "");
			//intent.putExtra("DBHelper", dbMachine);
			startActivity( intent );    			
			break;
		}
    		
    	return false;
    }
    private void deleteParlorInf(int deleteId, String deleteItem )
    {
		// 子供
		iDeleteId = deleteId;
		strDelete = deleteItem;
		
		// 収支管理に使われていれば、代わりの台をどれにするか選択させる
		SQLiteDatabase db = dbParlor.getWritableDatabase();
		Cursor cr = db.query( "MoneyMan",
				new String[]{ "Count(*)" }
				,"ParlorId=" + iDeleteId
				,null, null, null, null);
		
		cr.moveToFirst();
		if( cr.getInt(0) <= 0 )
		{
			new AlertDialog.Builder(this)
			.setTitle(strDelete)
			.setMessage("店のデータを削除しますが、よろしいですか？")
			.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// どこにも使われていなければ、削除する
						SQLiteDatabase db = dbParlor.getWritableDatabase();
						db.beginTransaction();
						db.delete( tblInf_ParlorMain.getTblName(),
								"_id=" + iDeleteId, null);
						db.setTransactionSuccessful();
						db.endTransaction();
						
						strMessage = "店"+ strDelete+"を削除しました。";
						updateAdapter();
						
						//Toast.makeText( ParlorsManActivity.this, "削除しました。", Toast.LENGTH_SHORT );
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
			new AlertDialog.Builder(ParlorsManActivity.this)
			.setTitle(strDelete)//getString(R.string.DLG_TITLE_CONFIRM))
			.setMessage("削除しようとしている店は既に使用されているので、店削除後、その店が使用されているデータは他の店に置き換えが必要ですが、よろしいですか？")
			.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 代わりの台をどれにするか選択させる
						SQLiteDatabase db = dbParlor.getWritableDatabase();
						Cursor cr = db.rawQuery("select ma._id, ma.ParlorName from ParlorMan ma where ma._id <> " 
								+ iDeleteId + " order by ma.ParlorName", null );
						
						final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								ParlorsManActivity.this);
						
						String[] strNms = new String[cr.getCount()];
						iParlorIds = new int[cr.getCount()];
						
						cr.moveToFirst();
						for( int i=0; i < cr.getCount(); i++ )
						{
							// 表示項目の配列
							iParlorIds[i] = cr.getInt(0);
							strNms[i] = cr.getString(1);
							cr.moveToNext();
						}
						
						// タイトルを設定
						alertDialogBuilder.setTitle("削除される店と入れ替える店を選択");
						// 表示項目とリスナの設定
						alertDialogBuilder.setItems(strNms,
						        new DialogInterface.OnClickListener() {
						            @Override
						            public void onClick(DialogInterface dialog, int which) {
						                // 項目が選択されたときの処理
						            	SQLiteDatabase db = dbParlor.getWritableDatabase();
			    						db.beginTransaction();
			    						
			    						ContentValues cv = new ContentValues();
			    						
			    						cv.put("ParlorId", iParlorIds[which]);
			    						
			    						// 収支情報更新
			    						db.update( "MoneyMan",
			    								cv, "ParlorId=" + iDeleteId, null);
			    						
			    						// 台情報削除
			    						db.delete( tblInf_ParlorMain.getTblName(),
			    								"_id=" + iDeleteId, null);
			    						
			    						db.setTransactionSuccessful();
			    						db.endTransaction();
			    						strMessage = "店"+ strDelete+"を削除し、店" 
			    						+TableControler.getParlorNameFromParlorId( ParlorsManActivity.this, dbParlor, String.valueOf( iParlorIds[which]))
			    						+"と入れ替えました。";
						            	
			    						//strMessage = "削除しました。";
			    						//Toast.makeText( ParlorsManActivity.this, "削除しました。", Toast.LENGTH_LONG );

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
    	String strWherePhrase = "";
    	String strTitleAddString = "";
    	// 親要素  
        groupData = new ArrayList<String>();  
        // 子要素  
        List<List<MainMenuData>> childData 
        	= new ArrayList<List<MainMenuData>>();    	

    	String strAdd_ex = "";
    	if( PatiLogger.isExistLastFinishNotUpdData( this, getString(R.string.ParlorTblName) ) )
    	{
    		strAdd_ex = getString(R.string.add_ex);
    	}    	
        Button btnAdd = (Button)this.findViewById(R.id.btnParlorAdd);
        btnAdd.setText( getString(R.string.add) + strAdd_ex );
        
       	// 2012/5/15 店の名称での検索
    	if( false == strSrchWord.equals(""))
    	{
    		if( strWherePhrase.length() > 0 )
    		{
    			strWherePhrase += " and ";
    		}
    		strWherePhrase += "ParlorName like '%" + strSrchWord + "%'"; 
    		strTitleAddString += "店名に'" + strSrchWord + "'を含む";
    	}
       	if( strTitleAddString.length() > 0 )
    	{
    		strTitleAddString = "(" + strTitleAddString + ")";
    	}
       	tblInf_ParlorMain.setWherePhrase(strWherePhrase);    	
   		// リストの最初は「新規追加」
        /*
        this.list = new ArrayList<MainMenuData>();
    	MainMenuData data = new MainMenuData();
    	data.setMenuString( getString(R.string.add) );
       	data.setResId( android.R.drawable.ic_menu_add );
    	list.add( data );
		*/
        
    	// データベースから台の名称のString配列を取得する
    	
    	// queryでSELECTを実行
    	// 台の名称の最初の1文字を取得
    	String[] columns = {
    			"COUNT(*)",
    			"SUBSTR(" + getString(R.string.ParlorTblMainClmnName) + ", 1, 1)"
    			};
    	String selection = tblInf_ParlorMain.getWherePhrase();//null;
    	String groupBy = "SUBSTR(" + getString(R.string.ParlorTblMainClmnName) + ", 1, 1)";
    	SQLiteDatabase db = dbParlor.getReadableDatabase();
    	try {
	    	Cursor cPrt1 = db.query(tblInf_ParlorMain.getTblName(),
	    			columns, selection, null, groupBy, null, "SUBSTR(" + getString(R.string.ParlorTblMainClmnName) + ", 1, 1)");
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
	    		List<MainMenuData> children = new ArrayList<MainMenuData>();
	    		String[] columnsChild = {"_id",getString(R.string.ParlorTblMainClmnName)};//,
	        			//getString(R.string.ParlorTblSubClmnName1)};
	        	String selectionChild = "SUBSTR(" 
	        		+ getString(R.string.ParlorTblMainClmnName) + ", 1, " 
	        		+ strCurParentName.length() + " ) = '" + strCurParentName + "'";
	        	//SQLiteDatabase db = dbMachine.getReadableDatabase();
	        	if( null != tblInf_ParlorMain.getWherePhrase() && tblInf_ParlorMain.getWherePhrase().length() > 0 )
	        	{
	        		selectionChild += " and " + tblInf_ParlorMain.getWherePhrase();
	        	}
	        	
	        	try {
	    	    	Cursor cChild = db.query(tblInf_ParlorMain.getTblName(),
	    	    			columnsChild, selectionChild, null, null, null, "ParlorName");
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
	    	    	    MainMenuData data = new MainMenuData();
	    	        	data.setMenuString( cChild.getString(1) );
	    	        	data.setMenuId(cChild.getInt(0));
	    	        	//if( cChild.getInt(2) == 0 )
	    	        	//{
	    		        	// アイコンの設定
	    			    //    data.setResId( R.drawable.parlor_toriaezu );
	    	        	//}
	    	        	//else if( cChild.getInt(2) == 1 )
	    	        	//{
	    	        	//	data.setResId( R.drawable.patinko_machine );
	    	        	//}
	    	        	//else
	    	        	//{
	    	        		data.setResId( R.drawable.parlor_toriaezu );
	    	        	//}
	    	    	    
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
    	mAdapter = new MstExpandableListAdapter(
    				this,
    				groupData,
    				childData
    			);
        listView.setAdapter(mAdapter);      	
        listView.setFocusableInTouchMode(true);
        listView.setOnChildClickListener(this);
        //setOnItemClickListener(this);
    	//item = getResources().getStringArray(R.array.main_menu);
    	/*
    	if( item != null ) {
	    	for( int i=0; i < item.length; i++ ) {
	    		data = new MainMenuData();
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
    	MainMenuAdapter adapter =
    		new MainMenuAdapter(this, R.layout.lst_menu_row, list); 
    	//adapter = new ArrayAdapter<String>( this,
    	// android.R.layout.simple_list_item_1,
    	// item );
    	listView.setAdapter(adapter);*/    	

        if( strMessage != null )
        {
        	Toast.makeText( ParlorsManActivity.this, strMessage, Toast.LENGTH_LONG ).show();
        	strMessage = null;
        }
        setTitle(strTitle + strTitleAddString);
        
    	////////////////
    	/*
    	if( listView != null ) {
    		listView.removeAllViewsInLayout();
    	}

    	this.list = new ArrayList<MainMenuData>();

   		// リストの最初は「新規追加」
		MainMenuData data = new MainMenuData();
    	data.setMenuString( getString(R.string.add) );
       	data.setResId( android.R.drawable.ic_menu_add );
    	list.add( data );

    	// データベースから店の名称のString配列を取得する
    	int[] ids = null;
    	String[] item = null;
    	
    	// queryでSELECTを実行
    	String[] columns = {"_id",getString(R.string.ParlorTblMainClmnName)};
    	String selection = null;
    	SQLiteDatabase db = dbParlor.getReadableDatabase();
    	try {
	    	Cursor c = db.query(tblInf_ParlorMain.getTblName(),
	    			columns, selection, null, null, null, "ParlorName");
	    	c.moveToFirst();
	    	ids = new int[c.getCount()];
	    	item = new String[c.getCount()];
	    	for (int i = 0; i < item.length; i++) {
	    		ids[i] = c.getInt(0);
	    	    item[i] = c.getString(1);
	    	    c.moveToNext();
	    	}
	    	c.close();
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
    	//item = getResources().getStringArray(R.array.main_menu);
    	if( item != null ) {
	    	for( int i=0; i < item.length; i++ ) {
	    		data = new MainMenuData();
	        	data.setMenuString( item[i] );
	        	data.setMenuId(ids[i]);
	        	// アイコン無し
		        data.setResId( R.drawable.parlor_toriaezu );
	        	list.add( data );
	    	}
    	}
    	// 配列アダプタの生成
    	//ArrayAdapter<String> adapter;
    	MainMenuAdapter adapter =
    		new MainMenuAdapter(this, R.layout.lst_menu_row, list); 
    	//adapter = new ArrayAdapter<String>( this,
    	// android.R.layout.simple_list_item_1,
    	// item );
    	
    	listView.setAdapter(adapter);
    	*/
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
            	setTitle("店の名前の検索条件設定")
            	.setView(edit)
            	.setMessage("以下の入力値を含む名前の店を検索します。")
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
				bundle.putInt(getString(R.string.key_parlor_id), RetId );
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
			// リストの項目をフィルタリングするための条件をユーザに入力してもらう
			filterListItem();
		}
		
		return super.dispatchKeyEvent(event);
	}
	private void filterListItem() {
		// TODO Auto-generated method stub
        final EditText edit = new EditText(this);
        edit.setText(strSrchWord);
        edit.setSingleLine(true);
        AlertDialog alertDialog = new AlertDialog.Builder(this).
        	setTitle("店の名前の検索条件設定")
        	.setView(edit)
        	.setMessage("以下の入力値を含む名前の店を検索します。")
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
}
