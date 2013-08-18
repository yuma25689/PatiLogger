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

public class ViewCashPerShopActivity extends Activity 
	implements AdapterView.OnItemClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private ListView listView;
	private ArrayList<DataMenuData> list = null;
	
	private TableOpenInfo tblInf = new TableOpenInfo();
	private DBHelper db = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_per_shop);

        setTitle(getString(R.string.DlgTitle_ViewPerShop));
        commLogic.init(this);

        listView = (ListView) this.findViewById(R.id.lstViewParShopMain);
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
    		case 0:
			default:
				String strMenuString = list.get(position).getMenuString();
				//String[] arrMenuString = strMenuString.split(" ");
				// 収支インテントを呼び出す
				// Intentの呼び出し
				intent = new Intent(
						this, MoneyManActivity.class );
	
				// 店の名前で渡す
				intent.putExtra(getString(R.string.key_money_parlorname), strMenuString );//arrMenuString[0]);
	
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

    	this.list = new ArrayList<DataMenuData>();

    	// データベースからString配列を取得する
    	DataMenuData data = null;
    	int[] ids = null;
    	String[] item = null;
    	String[] itemRight = null;
    	
    	// queryでSELECTを実行
    	String[] columns = {"_id","sum(CashFlow)","ParlorId","sum(ExpVal)"};
    	String selection = null;
    	String groupBy = "ParlorId";
    	String orderBy = "sum(CashFlow) desc";
    	SQLiteDatabase dbRead = db.getReadableDatabase();
    	try {
	    	Cursor c = dbRead.query(tblInf.getTblName(),
	    			columns, selection, null, groupBy, null, orderBy);
	    	c.moveToFirst();
	    	ids = new int[c.getCount()];
	    	item = new String[c.getCount()];
	    	itemRight = new String[c.getCount()];
	    	for (int i = 0; i < item.length; i++) {
	    		ids[i] = c.getInt(0);
	    		String strParlorName = TableControler.getParlorNameFromParlorId(
	    				this, db, c.getString(2) );
	    		if( strParlorName == null || strParlorName.length() == 0 )
	    		{
	    			strParlorName = getString(R.string.unknown);
	    		}
	    		
	    	    item[i] = strParlorName;
	    	    //+ " " + String.valueOf( c.getInt( 1 ) );
	    	    itemRight[i] = String.valueOf( c.getInt( 1 ) );
	    	    //+ " " + String.valueOf( c.getInt( 3 ) );
	    	    c.moveToNext();
	    	}
	    	c.close();
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
    	//item = getResources().getStringArray(R.array.main_menu);
    	if( item != null ) {
	    	for( int i=0; i < item.length; i++ ) {
	    		data = new DataMenuData();
	        	data.setMenuString( item[i] );
	        	data.setMenuId(ids[i]);
	        	data.setRightString(itemRight[i]);
	        	// アイコン無し
		        //data.setResId( android.R.drawable.ic_menu_add );
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
    	listView.setAdapter(adapter);
    }
}
