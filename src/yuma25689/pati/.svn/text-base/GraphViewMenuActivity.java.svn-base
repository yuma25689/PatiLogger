package yuma25689.pati;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;

//import mediba.ad.sdk.android.openx.MasAdView;
//import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
//import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
//import android.widget.LinearLayout;
import android.widget.ListView;
//import android.widget.TextView;
import android.widget.Toast;

public class GraphViewMenuActivity extends Activity 
	implements AdapterView.OnItemClickListener, View.OnClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private ListView listView;
	private ArrayList<MainMenuData> list = null;
	String fileExport = null;
	DBHelper dbHelper = null;
	Button btnBack = null;
	Button btnRegist = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(getString(R.string.DlgTitle_GraphView));
        commLogic.init(this);

        listView = (ListView) this.findViewById(R.id.lstMain);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);
        ColorDrawable dw = new ColorDrawable(Color.GRAY);
        listView.setDivider(dw);
        listView.setDividerHeight(1);
        dbHelper = new PatiManDBHelper(this);
        
        updateAdapter();        
    }

    // リスト選択イベントの処理
    public void onItemClick(AdapterView<?> parent,
    		View view, int position, long id ) {
    	try
    	{
    		// Toast.makeText(this,Integer.toString(position),Toast.LENGTH_SHORT).show();
    		Intent intent = null;
    		switch( position )
    		{
    		case 0:
    			// 日別収支表示インテントを呼び出す
    			// Intentの呼び出し
    			intent = new Intent( this, ViewGraphTestActivity .class );
    			startActivity( intent );
    			break;
    		case 1:
    			intent = new Intent( this, ViewMonthPolyGraphActivity .class );
    			startActivity( intent );
    			break;
    		case 2:
    			intent = new Intent( this, ViewMcnWorkTimeGraphActivity .class );
    			startActivity( intent );
    			break;
    		}
    		
    	} catch( Exception e ) {
    		Toast.makeText( this, e.getMessage(), Toast.LENGTH_SHORT );
    	}

    }
        
    // 配列アダプタの更新
    private void updateAdapter() {
    	this.list = new ArrayList<MainMenuData>();
    	String[] item;
    	// リソースからString配列を取得する
    	item = getResources().getStringArray(R.array.graphview_menu);
    	for( int i=0; i < item.length; i++ ) {
    		MainMenuData data = new MainMenuData();
        	data.setMenuString( item[i] );
        	switch( i )
    		{
    		case 0:
    			// グラフテスト用
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.graph );
    			break;
    		case 1:
    			// グラフテスト用
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.graph );
    			break;
    		case 2:
    			// グラフテスト用
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.graph );
    			break;
    		default:
    			data.setResId( R.drawable.icon );
    			break;
    		}        	
        	list.add( data );
    	}
    	// 配列アダプタの生成
    	//ArrayAdapter<String> adapter;
    	MainMenuAdapter adapter =
    		new MainMenuAdapter(this, R.layout.lst_menu_row, list); 
    	//adapter = new ArrayAdapter<String>( this,
    	//	android.R.layout.simple_list_item_1,
    	//	item );
    	listView.setAdapter(adapter);
    }

	@Override
	public void onClick(View v) {
		if( v==btnBack ) {
			finish();
		}
	}
    
}