package yuma25689.pati;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ViewCashPerMachineActivity extends Activity 
	implements AdapterView.OnItemClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private ListView listView;
	private ArrayList<MoneyMenuData> list = null;
	
	private TableOpenInfo tblInf = new TableOpenInfo();
	private DBHelper db = null;
	private String strSrchWord = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_per_machine);

        setTitle(getString(R.string.DlgTitle_ViewPerMachine));
        commLogic.init(this);

        listView = (ListView) this.findViewById(R.id.lstViewParMachineMain);
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
					//String[] arrMenuString = strMenuString.split(" ");
					// 収支インテントを呼び出す
					// Intentの呼び出し
					intent = new Intent(
							this, MoneyManActivity.class );
		
					// 台の名前で渡す
					intent.putExtra(getString(R.string.key_money_machinename), strMenuString );//arrMenuString[0]);
		
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
    	String[] itemExpVal = null;
    	String[] itemRight = null;
    	
    	// queryでSELECTを実行
    	String[] columns = {"_id","sum(CashFlow)","McnId","sum(ExpVal)"};
    	String selection = null;
    	
    	String groupBy = "McnId";
    	String orderBy = "sum(CashFlow) desc";
    	SQLiteDatabase dbRead = db.getReadableDatabase();
    	try {
	    	Cursor c = dbRead.query(tblInf.getTblName(),
	    			columns, selection, null, groupBy, null, orderBy);
	    	// 2015/2/2 台の名称での検索
	    	int iCount = 0;
	    	if( strSrchWord != null && 0 < strSrchWord.length())
	    	{
		    	c.moveToFirst();
	    		while ( c.moveToNext() ) {
	    			String strMachineName = TableControler.getMcnNameFromMcnId( this, db, c.getString(2) );
	    			if( strMachineName != null && strMachineName.length() != 0 )
	    			{
	    				if( -1 != strMachineName.indexOf( strSrchWord ))
	    				{
	    					iCount++;
	    				}
	    			}
	    			else
	    			{
	    				if( -1 != getString(R.string.unknown).indexOf( strSrchWord ))
	    				{
	    					iCount++;
	    				}
	    			}
	    		}
	    	}
	    	else
	    	{
	    		iCount = c.getCount();
	    	}
	    	
	    	ids = new int[iCount];//c.getCount()];
	    	item = new String[iCount];//c.getCount()];
	    	itemExpVal = new String[iCount];//c.getCount()];
	    	itemRight = new String[iCount];//c.getCount()];
	    	int iItemIndex = 0;
	    	c.moveToFirst();
	    	while( c.moveToNext() ) {
	    	//for (int i = 0; i < c.getCount(); i++) {
	    		String strMachineName = TableControler.getMcnNameFromMcnId( this, db, c.getString(2) );
	    		if( strMachineName == null || strMachineName.length() == 0 )
	    		{
	    			strMachineName = getString(R.string.unknown);
	    		}
	    		if( -1 == strMachineName.indexOf( strSrchWord ))
	    		{
	    			continue;
	    		}
	    		ids[iItemIndex] = c.getInt(0);
	    	    item[iItemIndex] =  strMachineName;
	    	    itemRight[iItemIndex] = String.valueOf( c.getInt( 1 ) );
	    	    		//+ " " + String.valueOf( c.getInt( 3 ) );
	    	    itemExpVal[iItemIndex] = String.valueOf( c.getInt( 3 ) );
	    	    iItemIndex++;
	    	    // c.moveToNext();
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
	        	data.setMenuId(ids[i]);
	        	data.setBtmCap1String("期待値:");
	        	data.setBtm1String(itemExpVal[i]);
	        	data.setRightString(itemRight[i]);
	        	// アイコン無し
		        //data.setResId( android.R.drawable.ic_menu_add );
	        	list.add( data );
	    	}
    	}
    	// 配列アダプタの生成
    	//ArrayAdapter<String> adapter;
    	MoneyMenuAdapter adapter =
    		new MoneyMenuAdapter(this, R.layout.lst_menu_row, list); 
    	//adapter = new ArrayAdapter<String>( this,
    	// android.R.layout.simple_list_item_1,
    	// item );
    	listView.setAdapter(adapter);
    }
    
    
    
    
    // ------------ 2015/02/02 検索機能の追加    
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
    /*
     * オプションメニューの作成
     */
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean ret = super.onCreateOptionsMenu(menu);
    	menu.add( 0, Menu.FIRST, Menu.NONE, "検索").setIcon(android.R.drawable.ic_menu_search);
    	return ret;
    }
    /**
     * オプションメニューの選択イベント
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case Menu.FIRST:
        	filterListItem();
        	return true;
        }
        return false;
    }
    
    
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
