package yuma25689.pati;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.graphics.Bitmap;
import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
//import android.widget.LinearLayout;
import android.widget.Toast;

public class ViewMcnWorkTimeGraphActivity extends Activity 
	implements View.OnClickListener {
	
	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	public static final String DATA_FILE_DIR = "/sdcard/patiman/tmp";
	//public static final String SAMPLE_FILE = "lib/examples/line_graph_template.html";
	public static final String TEMPLATE_FILE = "lib/examples/graph_circle_tmpl1.html";
	public static final String EXPORT_FILE = "lib/examples/worktime_pie_graph.html";
	public static final String EXPORT_FILE_NAME = "worktime_pie_graph.html";
	Button btnBack = null;
	Button btnSrch = null;
	private DBHelper dbHelper = null;
	private int iWvWidth = 0;
	private int iWvHeight = 0;
	//private final int MAX_GRAPH = 5000000;
	//private final int MIN_GRAPH = -5000000;
	private final int FIXED_PX_WIDTH = 20;
	//private final int FIXED_PX_HEIGHT = 120;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.view_graph_3);
		commLogic.init(this);
		
		setTitle("台の稼働時間割合グラフ");//getString(R.string.DlgTitle_DrinkPieGraph));
		
		dbHelper = new PatiManDBHelper(this);

		// HTMLのライブラリがコピーされていなければ、assetsからローカルにコピーする
		try
		{
			copy2Local("lib");
			copy2Local("lib/html5jp");
			copy2Local("lib/examples");
			copy2Local("lib/html5jp/excanvas");
			copy2Local("lib/html5jp/graph");
		} catch( Exception ex ) {
			Toast.makeText(this, "ファイルコピーでエラー発生", Toast.LENGTH_LONG ).show();
			return;
		}
		
		// ---- 期間
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(getString(R.string.all_period));
            	
    	String[] columns_ = {"_id","SUBSTR(WorkDate,1,6) AS WorkMonth"};
    	String selection_ = null;
    	String groupBy = "SUBSTR(WorkDate,1,6)";
    	String orderBy = "SUBSTR(WorkDate,1,6) desc";
    	SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
    	Cursor c_ = dbRead.query(getString(R.string.MoneyTblName),
    			columns_, selection_, null, groupBy, null, orderBy);
    	c_.moveToFirst();
    	for (int i = 0; i < c_.getCount(); i++) {
    		adapter.add( TableControler.getFmtMonth( c_.getString(1) ) );
    	    c_.moveToNext();
    	}
    	c_.close();
        Spinner spinner = (Spinner)findViewById(R.id.month_graph_3);
        spinner.setAdapter(adapter);		

		/*
		LinearLayout btm = (LinearLayout)this.findViewById(R.id.graph_bottomBar);
		LayoutParams prms_w1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
				, LayoutParams.WRAP_CONTENT, (float)1.0 );
		btnSrch = new Button(this);
		btnSrch.setText(getString(R.string.btnSrch));
		btnSrch.setOnClickListener(this);
		btnSrch.setLayoutParams(prms_w1);
		btm.addView(btnSrch);
		
		btnBack = new Button(this);
		btnBack.setText(getString(R.string.btnBack));
		btnBack.setOnClickListener(this);
		btnBack.setLayoutParams(prms_w1);
		btm.addView(btnBack);
		*/
	}

	private void copy2Local(String dirName) throws IOException{

		File objDir = new File( DATA_FILE_DIR + "/" + dirName );
		if( false == objDir.exists() )
		{
			if( false == objDir.mkdirs() )
			{
				/*
				String[] strArr = {
					getString( R.string.TITLE_ERROR ),
					getString( R.string.MSG_FAILED_DIR_CREATE )
				};
				*/
				
			}
		}
		
		// assetsから読み込み、出力する
		String[] fileList = getResources().getAssets().list(dirName);
		if(fileList == null || fileList.length == 0){
			return;
		}
		AssetManager as = getResources().getAssets(); 
		InputStream input = null;
		FileOutputStream output = null;
		
		for(String file : fileList) {
			File objFile = new File( objDir.getPath() + "/" + file);
			//File objFile2 = new File("file:///android_asset/" + dirName + "/" + file);
			if( objFile.exists() )
			{//|| objFile2.isDirectory() ) continue;
				objFile.delete();
				//continue;
			}
			// directoryかどうか判別するのにあけてみる
			try{
				input = as.open(dirName + "/" + file);
			} catch ( Exception ex ) {
				// 開けなかったらdirectoryとみなし、スルー
				continue;
			}
			objFile.createNewFile();
			output = new FileOutputStream(objFile);//openFileOutput(objFile.getPath(), Context.MODE_WORLD_READABLE); 			
			
		    int DEFAULT_BUFFER_SIZE = 1024 * 4; 
		    
		    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE]; 
		    int n = 0; 
			while (-1 != (n = input.read(buffer))) { 
				output.write(buffer, 0, n); 
			}
			output.close();
			input.close();
		}
	}
	private void finishIntent()
	{
		finish();
	}	
	@Override
	public void onClick(View v) {
		if( v==btnBack ) {
			// 戻る
			finishIntent();
		}
		else if( v==btnSrch )
		{
			updateGraph();
		}
	}
	private void updateGraph()
	{
		WebView wv = (WebView)this.findViewById(R.id.wvViewGraphTest);
		
		// 検索
		Spinner spnStartDate = (Spinner)ViewMcnWorkTimeGraphActivity.this.findViewById(R.id.month_graph_3);
		//TextView txtEndDate = (TextView)ViewGraphTestActivity.this.findViewById(R.id.period_end_graph1);
		
		String strFileContent = null;
	    StringBuffer buf = new StringBuffer();
		
		try{
			// テンプレートファイルオープン
			AssetManager as = getResources().getAssets();
			//File file = new File("file:///android_asset/" + TEMPLATE_FILE);
			//FileReader filereader = new FileReader(file);
			InputStream input = null;
		    //int DEFAULT_BUFFER_SIZE = 1024 * 4; 
			input = as.open(TEMPLATE_FILE);
			//byte[] buffer = new byte[DEFAULT_BUFFER_SIZE]; 
		    //int n = 0; 
		    BufferedReader reader = 
		        new BufferedReader(new InputStreamReader(input, "UTF-8"/* 文字コード指定 */));
		    String str;
		    while ((str = reader.readLine()) != null) {
		            buf.append(str);
		            buf.append("\n");
		    }
			input.close();
		}catch(FileNotFoundException e){
			Toast.makeText(this, "ファイル読み込みでエラー発生:" + e.getMessage(), Toast.LENGTH_LONG ).show();
			return;
		}catch(IOException e){
			Toast.makeText(this, "ファイル読み込みでエラー発生:" + e.getMessage(), Toast.LENGTH_LONG ).show();
			return;
		}
		
		strFileContent = buf.toString();
		
		String strStartDate = "";
		//String strEndDate = "";
		
		if( spnStartDate.getSelectedItem() != null )
		{
			strStartDate = (String)spnStartDate.getSelectedItem();
		}
		else
		{
			strStartDate = getString(R.string.all_period);
		}

		// 表示データをデータベースから持ってくる
		String strTitle = "";
		String strItems = "";
		//String strX = "[\"月\"";//\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"10\", \"11\", \"12\",]";
		//String strY = "[\"金額\"";//-300000, -200000, -100000, 0, 100000, 200000, 300000]";
		//String strGraphMax = "0";//"300000";
		//String strGraphMin = "0";//"-300000";
		String strWidth = String.valueOf(iWvWidth-FIXED_PX_WIDTH);
		String strHeight = String.valueOf(iWvHeight/2);//-FIXED_PX_HEIGHT);

		Configuration config = getResources().getConfiguration();
		int iOrientation = config.orientation;

		switch(iOrientation){
			case Configuration.ORIENTATION_PORTRAIT:
				strWidth = String.valueOf(iWvHeight);
				wv.setInitialScale(100);
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				strHeight = String.valueOf(iWvHeight);
				wv.setInitialScale(100);
				break;
			case Configuration.ORIENTATION_SQUARE:
				strWidth = String.valueOf(iWvHeight);
				wv.setInitialScale(100);
				break;
		}		
		
		//このグラフではそうしないと凡例がきれいにでないようなので、HeightはWidthの1/2とする。
		//strHeight = String.valueOf(((iWvWidth-FIXED_PX_WIDTH)*1)/2);
		
		
		//String strWorkYear = "";
		//String strStartYear = "";
		//String strEndYear = "";
		//int iMaxMonthIndex = 11;
		//int iMinMonthIndex = 0;
		
		//int iY_SepCnt = 10;
		//int iCashUnit = 100;
		//int iCashMin = 0;
		//int iCashMax = 0;
		//int iCurrentCash = 0;
    	String[] item = null;

    	// queryでSELECTを実行
    	// WorkTimeはテキストで入っているので、サマリは使用不可
    	String[] columns = {"WorkedInterval","McnId"};//{"sum(WorkTime)","McnId"};
    	String selection = "";
    	if( getString(R.string.all_period).equals( strStartDate ) )
    	{
    		selection = "";
    	}
    	else
    	{
    		selection = "SUBSTR(WorkDate,1,6) = '" + TableControler.getUnfmtMonth( strStartDate ) + "'";
    	}
    	String groupBy = null;//"McnId";
    	String orderBy = "McnId desc, WorkDate";
    	SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
    	TreeMap<String,Integer> mapWorkIntervals = new TreeMap<String,Integer>(); 
    	//ArrayList<String> arrNames = new ArrayList<String>(); 
    	//ArrayList<Integer> arrMinutes = new ArrayList<Integer>();
    	int iCurrentMcnMinute = 0;
    	int iPrevMcnId = -2;
    	boolean blnDataExists = false;
		//int[] arrYearCashs = new int[12]; 
    	try {
	    	Cursor c = dbRead.query("MoneyMan",
	    			columns, selection, null, groupBy, null, orderBy);
	    	c.moveToFirst();
	    	item = new String[c.getCount()];
	    	for (int i = 0; i < item.length; i++) {
	    		int iMcnId = c.getInt(1);
	    		if( i == 0 )
	    		{
	    			iPrevMcnId = iMcnId;
	    		}
	    		if( iPrevMcnId == iMcnId || iPrevMcnId <= 0 )
	    		{
	    		}
	    		else
	    		{
		    		String strName = "";
		    		if( iPrevMcnId <= 0 )
		    		{
		    			strName = "不明";
		    		}
		    		else
		    		{
		    			strName = TableControler.getMcnNameFromMcnId(this, dbHelper, String.valueOf(iPrevMcnId));
		    		}
		    		mapWorkIntervals.put(strName, iCurrentMcnMinute);
	    			iCurrentMcnMinute = 0;
	    			blnDataExists = false;
	    		}
	    		if( c.getString(0) != null && c.getString(0).length() == 4 )
	    		{
		    		String strHour = c.getString(0).substring(0,2);
		    		String strMinute = c.getString(0).substring(2,4);
		    		iCurrentMcnMinute += ( Integer.parseInt(strHour)*60 + Integer.parseInt(strMinute) );
	    		}
	    		else
	    		{
	    			iCurrentMcnMinute += 0;
	    		}
	    		iPrevMcnId = iMcnId;
    			blnDataExists = true;
	    	    c.moveToNext();	    		
	    	}
	    	if( blnDataExists )//iPrevMcnId != -2 )
	    	{
	    		String strName = "";
	    		if( iPrevMcnId <= 0 )
	    		{
	    			strName = "不明";
	    		}
	    		else
	    		{
	    			strName = TableControler.getMcnNameFromMcnId(this, dbHelper, String.valueOf( iPrevMcnId ) );
	    		}
	    		mapWorkIntervals.put(strName, iCurrentMcnMinute);
	    		//arrNames.add( strName );
    			//arrMinutes.add( iCurrentMcnMinute );
    			iCurrentMcnMinute = 0;
    			blnDataExists = false;
	    	}
	    	c.close();
	    	
	    	// 入れた値を稼働時間の大きな順にソートしなおす
	    	ArrayList<Map.Entry<String,Integer>> mapSorted 
	    		= new ArrayList<Map.Entry<String,Integer>>(mapWorkIntervals.entrySet()); 

	    	Collections.sort(
	    			mapSorted, 
	    			new Comparator<Entry<String,Integer>>(){ 
	    				public int compare(Entry<String,Integer> o1, Entry<String,Integer> o2){
	    					Map.Entry<String,Integer> e1 = (Entry<String, Integer>) o1;
	    					Map.Entry<String,Integer> e2 = (Entry<String, Integer>) o2;
	    					Integer e1Value = (Integer) e1.getValue(); 
	    					Integer e2Value = (Integer) e2.getValue();	
	    					return (e2Value.compareTo(e1Value)); 
	    				} 
	    				public boolean equals(Object obj){ 
	    					return super.equals(obj); 
	    				} 
	    			} 
	    	);
	    		    	
	    	for( int j=0; j < mapSorted.size(); j++ )
	    	{
	    		if( false == "".equals( strItems ))
	    		{
	    			strItems += ", \n";
	    		}
	    		strItems += "[\"" + mapSorted.get(j).getKey() + "\", " + mapSorted.get(j).getValue() + "]";
	    	}
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
		// ファイルの内容を置換することによって作成する。
		strFileContent 
		= strFileContent.replace( "[TITLE]", strTitle ); 
		strFileContent 
		= strFileContent.replace( "[ITEMS]", strItems ); 
		//strFileContent 
		//= strFileContent.replace( "[X]", strX ); 
		//strFileContent 
		//= strFileContent.replace( "[Y]", strY ); 
		//strFileContent 
		//= strFileContent.replace( "[GRAPH_MAX]", strGraphMax ); 
		//strFileContent 
		//= strFileContent.replace( "[GRAPH_MIN]", strGraphMin ); 
		strFileContent 
		= strFileContent.replace( "[WIDTH]", strWidth ); 
		strFileContent 
		= strFileContent.replace( "[HEIGHT]", strHeight ); 
		
		// 置換後のファイルの内容で表示ファイルを作成する
		File objDir = new File( DATA_FILE_DIR + "/" + "lib/examples" );
		if( false == objDir.exists() )
		{
			if( false == objDir.mkdirs() )
			{
				/*
				String[] strArr = {
					getString( R.string.TITLE_ERROR ),
					getString( R.string.MSG_FAILED_DIR_CREATE )
				};
				*/
				
			}
		}
    	try {
			File objFile = new File( objDir.getPath() + "/" + EXPORT_FILE_NAME);
			//File objFile2 = new File("file:///android_asset/" + dirName + "/" + file);
			if( objFile.exists() )//|| objFile2.isDirectory() ) continue;
				objFile.delete();
			FileOutputStream output = null;
			output = new FileOutputStream(objFile);//openFileOutput(objFile.getPath(), Context.MODE_WORLD_READABLE); 			
			output.write(strFileContent.getBytes());
			output.close();
		}catch(FileNotFoundException e){
			Toast.makeText(this, "ファイル書き込みでエラー発生:" + e.getMessage(), Toast.LENGTH_LONG ).show();
			return;
		}catch(IOException e){
			Toast.makeText(this, "ファイル書き込みでエラー発生:" + e.getMessage(), Toast.LENGTH_LONG ).show();
			return;
		}
		// lstViewGraphTest
		
		wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                setProgressBarIndeterminateVisibility(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setProgressBarIndeterminateVisibility(false);
            }
        });
		WebSettings setting = wv.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(true);		
		setting.setBuiltInZoomControls(true);
		setting.setJavaScriptEnabled(true);
		//setting.setGeolocationEnabled(true);
		setting.setJavaScriptCanOpenWindowsAutomatically(true);
		setting.setLoadsImagesAutomatically(true);
		setting.setPluginsEnabled(true);
		setting.setLightTouchEnabled(true);
		setting.setSupportZoom(true);		
		/*
		setting.setDatabaseEnabled(true);
		setting.setDatabasePath("/data/data/" + getPackageName() + "/database");
		setting.setDomStorageEnabled(true);
		setting.setAppCacheMaxSize(1024 * 1024 * 8);
		setting.setAppCachePath("/data/data/" + getPackageName() + "/cache");
		setting.setAppCacheEnabled(true);*/
		setting.setAllowFileAccess(true);
		setting.setCacheMode(WebSettings.LOAD_NORMAL);
		wv.loadUrl( "file://" + DATA_FILE_DIR + "/" + EXPORT_FILE);		
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	
		// Viewのサイズを取得
		WebView wv = (WebView)this.findViewById(R.id.wvViewGraphTest);
		iWvWidth = wv.getWidth();
		iWvHeight = wv.getHeight();
		Log.v("onWindowFocusChanged", "TextView width=" + wv.getWidth() + ", height=" + wv.getHeight());
		updateGraph();
	}
    /*
     * オプションメニューの作成
     *
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean ret = super.onCreateOptionsMenu(menu);
    	menu.add( 0, Menu.FIRST, Menu.NONE, "データビューへ").setIcon(R.drawable.report);
    	return ret;
    }
    
    /**
     * オプションメニューの選択イベント
     *
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case Menu.FIRST:
 			// 飲料別収支ビューインテントを呼び出す
			// Intentの呼び出し
			Intent intent = new Intent( this, ViewCashPerDrinkActivity .class );
			startActivity( intent );
        	break;
        }
        return false;        
    }*/
}
