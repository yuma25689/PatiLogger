package yuma25689.pati;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//import java.util.Enumeration;
import java.util.HashMap;
//import java.util.Hashtable;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ViewCashPerMakerActivity extends Activity 
	implements AdapterView.OnItemClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private ListView listView;
	private ArrayList<DataMenuData> list = null;
	
	private final int MENU_SHARE = 0;
	private final int MENU_MACHINE = 1;
	
	private TableOpenInfo tblInf = new TableOpenInfo();
	private DBHelper db = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_per_machine);

        setTitle(getString(R.string.DlgTitle_ViewPerMaker));
        commLogic.init(this);

        listView = (ListView) this.findViewById(R.id.lstViewParMachineMain);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);
        listView.setFastScrollEnabled(true);

        listView
        .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

         @Override
         public void onCreateContextMenu(ContextMenu menu, View v,
           ContextMenuInfo menuInfo) {

        	 AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
        	 DataMenuData contact = (DataMenuData)listView.getItemAtPosition(adapterMenuInfo.position);
        	 	
			  //MoneyMenuData contact = (MoneyMenuData) list.get(index);
			  menu.setHeaderTitle(contact.getMenuString());
			  menu.add(0, MENU_SHARE, 0, "共有");
			  menu.add(1, MENU_MACHINE, 1, "このメーカーの台一覧");
         }

        });
        
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
					// 台インテントを呼び出す
					// Intentの呼び出し
					intent = new Intent(
					//		this, MachineManActivity.class );
							this, MoneyManActivity.class );
					// 台の名前で渡す
					//intent.putExtra(getString(R.string.key_machine_makername), arrMenuString[0]);
					intent.putExtra(getString(R.string.key_money_makername), arrMenuString[0]);
		
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
    	String[] columns = {"sum(CashFlow)","McnId","sum(ExpVal)"};
    	String selection = null;
    	String groupBy = "McnId";
    	String orderBy = "sum(CashFlow) desc";
    	SQLiteDatabase dbRead = db.getReadableDatabase();
    	try {
	    	Cursor c = /*dbRead.rawQuery(
	    			"select sum(CashFlow),ma.MakerId,sum(ExpVal)"
	    			+ " from MoneyMan mo, "
	    			+ " (select _id as McnId, MakerId from MachineMan ) ma "
	    			+ " where ma.McnId = mo.McnId or mo.McnId = -1 "
	    			+ " group by ma.MakerId "
	    			,null
	    			
	    	);*/
	    	dbRead.query(tblInf.getTblName(),
	    		columns, selection, null, groupBy, null, orderBy);
	    	c.moveToFirst();
	    	HashMap<String,Integer> htMaker = new HashMap<String,Integer>();
	    	while (true) { //int i = 0; i < c.getCount(); i++) {
	    		String strMakerName = TableControler.getMakerNameFromMcnId( this, db, c.getString(1) );
	    		if( strMakerName == null || strMakerName.length() == 0 )
	    		{
	    			strMakerName = getString(R.string.unknown);
	    		}	    		
	    		if( htMaker.containsKey(strMakerName) )
	    		{
	    			int iTmp = htMaker.get(strMakerName);
	    			htMaker.put( strMakerName, iTmp + c.getInt(0) );
	    		}
	    		else
	    		{
	    			htMaker.put( strMakerName, c.getInt(0) );
	    		}
	    		if( c.isLast() ) break;
	    		c.moveToNext();		
	    	}
	    	ArrayList<Map.Entry<String, Integer>> al = new ArrayList<Map.Entry<String, Integer>>(htMaker.entrySet());
	    	Collections.sort(al, new SIMapComparator() );
	    	//Enumeration<String> eKey = htMaker.();
	    	//Enumeration<Integer> eVal = htMaker.elements();
	    	ids = new int[htMaker.size()];
	    	item = new String[htMaker.size()];
	    	itemRight = new String[htMaker.size()];
	    	//int i=0;
	    	for( int i=0; i < al.size(); i++ ) //while( eKey.hasMoreElements() )
	    	{
	    		String makerNm = al.get(i).getKey();//eKey.nextElement();
	    		String makerId = TableControler.getMakerIdFromMakerName( this, db, makerNm );
	    		if( makerId == null || "".equals( makerId ) )
	    		{
	    			ids[i] = -1;
	    		}
	    		else
	    		{
	    			ids[i] = Integer.parseInt(makerId);
	    		}
	    	    item[i] =  makerNm;
	    	    itemRight[i] = String.valueOf( al.get(i).getValue() );
	    	    		//+ " " + al.get(i).getValue();//eVal.nextElement()
	    	    		//;
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
    class SIMapComparator implements Comparator<Map.Entry<String, Integer>> {
    	  public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
    	    int s1 = o1.getValue();
    	    int s2 = o2.getValue();
    	    return Double.valueOf(s2).compareTo(Double.valueOf(s1));
    	  }
    	}
    // リスト選択イベントの処理
    public boolean onContextItemSelected(MenuItem menuItem)
    {
		 AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
		 DataMenuData contact = (DataMenuData)listView.getItemAtPosition(adapterMenuInfo.position);
		 //MoneyMenuData contact = (MoneyMenuData)listView.getItemAtPosition(adapterMenuInfo.position);
    	  switch (menuItem.getItemId()) {  
			  case MENU_SHARE:
				  	// 情報を共有する
				  	share(contact.getMenuString());
				  			//+ " " + contact.getRightString() 
				  			//+ " " + contact.getBtmCap1String() 
				  			//+ " " + contact.getBtm1String() );
	    			return true;
			  case MENU_MACHINE:
					String[] arrMenuString = contact.getMenuString().split(" ");
					// 台インテントを呼び出す
					// Intentの呼び出し
					Intent intent = new Intent(
							this, MachineManActivity.class );
					// 台の名前で渡す
					intent.putExtra(getString(R.string.key_machine_makername), arrMenuString[0]);
					startActivity( intent );
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
