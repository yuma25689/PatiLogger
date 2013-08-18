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

public class DataViewMenuActivity extends Activity 
	implements AdapterView.OnItemClickListener, View.OnClickListener {
	
	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private ListView listView;
	private ArrayList<MainMenuData> list = null;
	//private static final int PICKFILE_RESULT_CODE = 1;
	//private String filePath = null;
	String fileExport = null;
	DBHelper dbHelper = null;
	Button btnBack = null;
	Button btnRegist = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setTitle(getString(R.string.DlgTitle_DataView));
        
        commLogic.init(this);
        
        listView = (ListView) this.findViewById(R.id.lstMain);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);
        ColorDrawable dw = new ColorDrawable(Color.GRAY);
        listView.setDivider(dw);
        listView.setDividerHeight(1);
        dbHelper = new PatiManDBHelper(this);
    	
        /*
    	// 下ビューの作成
		LinearLayout btm = (LinearLayout)this.findViewById(R.id.main_bottomBar);
		LayoutParams prms_w1 = 
			new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
				, LayoutParams.WRAP_CONTENT, (float)1.0 );
		btnBack = new Button(this);
		btnBack.setText(getString(R.string.btnBack));
		btnBack.setOnClickListener(this);
		btnBack.setLayoutParams(prms_w1);
		btm.addView(btnBack);
        */
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
    			intent = new Intent( this, ViewCashPerDayActivity .class );
    			startActivity( intent );
    			break;
    		case 1:
    			// 月別収支表示インテントを呼び出す
    			intent = new Intent( this, ViewCashPerMonthActivity.class );
    			startActivity( intent );
    			break;
    		case 2:
    			// 台別収支表示インテントを呼び出す
    			intent = new Intent( this, ViewCashPerMachineActivity.class );
    			startActivity( intent );
    			break;
    		case 3:
    			// 店別収支表示インテントを呼び出す
    			intent = new Intent( this, ViewCashPerShopActivity.class );
    			startActivity( intent );
    			break;
    		case 4:
    			// メーカー別収支表示インテントを呼び出す
    			intent = new Intent( this, ViewCashPerMakerActivity.class );
    			startActivity( intent );
    			break;
    		case 5:
    			// 勝率表示インテントを呼び出す
    			intent = new Intent( this, ViewWinRateActivity.class );
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
    	item = getResources().getStringArray(R.array.dataview_menu);
    	for( int i=0; i < item.length; i++ ) {
    		MainMenuData data = new MainMenuData();
        	data.setMenuString( item[i] );
        	switch( i )
    		{
    		case 0:
    			// 日別収支表示インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.day );
    			break;
    		case 1:
    			// 月別収支表示インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.month );
    			break;
    		case 2:
    			// 台別収支表示インテントを呼び出す
            	// TODO: ここは後でアイコンを作って修正する
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.machine_2_toriaezu );
    			break;
    		case 3:
    			// 店別収支表示インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.parlor_toriaezu );
    			break;
    		case 4:
    			// 勝率表示インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.maker2 );
    			break;
    		case 5:
    			// 勝率表示インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.report );
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