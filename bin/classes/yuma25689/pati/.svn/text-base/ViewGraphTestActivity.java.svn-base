package yuma25689.pati;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.graphics.Bitmap;
import android.app.Activity;
import android.content.res.AssetManager;
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
import android.widget.Button;
//import android.widget.LinearLayout;
import android.widget.Toast;

public class ViewGraphTestActivity extends Activity 
	implements View.OnClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	public static final String DATA_FILE_DIR = "/sdcard/patiman/tmp";
	public static final String SAMPLE_FILE = "lib/examples/line_graph_template.html";
	public static final String TEMPLATE_FILE = "lib/examples/graph_line_tmpl3.html";
	public static final String EXPORT_FILE = "lib/examples/line_graph.html";
	public static final String EXPORT_FILE_NAME = "line_graph.html";
	Button btnBack = null;
	Button btnSrch = null;
	private DBHelper dbHelper = null;
	private int iWvWidth = 0;
	private int iWvHeight = 0;
	private final int MAX_GRAPH = 5000000;
	private final int MIN_GRAPH = -5000000;
	private final int FIXED_PX_WIDTH = 20;
	private final int FIXED_PX_HEIGHT = 60;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.view_graph_test);

		setTitle("通算収支遷移グラフ");
        commLogic.init(this);
		
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
			//File objFile2 = new File("file:///android_asset/" + dirName + "/" + file);
			// directoryかどうか判別するのにあけてみる
			try{
				input = as.open(dirName + "/" + file);
			} catch ( Exception ex ) {
				// 開けなかったらdirectoryとみなし、スルー
				continue;
			}
			File objFile = new File( objDir.getPath() + "/" + file);
			if( objFile.exists()
			&& objFile.length() == input.available() )//|| objFile2.isDirectory() ) continue;
				// ファイルがすでに存在する場合、ファイルサイズが同じなら同じファイルとみなして上書きコピーしない
			{
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
		// 検索
		//TextView txtStartDate = (TextView)ViewGraphTestActivity.this.findViewById(R.id.period_start_graph1);
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
		/*
		if( txtStartDate.getText() != null )
		{
			strStartDate = (String) txtStartDate.getText();
		}
		//if( txtEndDate.getText() != null )
		//{
		//	strEndDate = (String) txtEndDate.getText();
		//}
		 * 
		 */
		
		// 表示データをデータベースから持ってくる
		String strTitle = "";
		String strItems = "";
		String strX = "[\"月\"";//\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"10\", \"11\", \"12\",]";
		String strY = "[\"収支\"";//-300000, -200000, -100000, 0, 100000, 200000, 300000]";
		String strGraphMax = "0";//"300000";
		String strGraphMin = "0";//"-300000";
		String strWidth = String.valueOf(iWvWidth-FIXED_PX_WIDTH);
		String strHeight = String.valueOf(iWvHeight-FIXED_PX_HEIGHT);
		String strWorkYear = "";
		String strStartYear = "";
		String strEndYear = "";
		int iMaxMonthIndex = 11;
		int iMinMonthIndex = 0;
		
		int iY_SepCnt = 10;
		int iCashUnit = 10000;
		int iCashMin = 0;
		int iCashMax = 0;
		int iCurrentCash = 0;
    	String[] item = null;
    	
    	// queryでSELECTを実行
    	String[] columns = {"sum(CashFlow)","SUBSTR(WorkDate,1,6) AS WorkMonth", "sum(ExpVal)"};
    	String selection = null;
    	String groupBy = "SUBSTR(WorkDate,1,6)";
    	String orderBy = "SUBSTR(WorkDate,1,6)";
    	SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
		int[] arrYearCashs = new int[12]; 
		boolean[] blnGetDataMonth = new boolean[12];
		for( int x=0; x < 12; x++ )
		{
			blnGetDataMonth[x] = false;
		}
    	try {
	    	Cursor c = dbRead.query("MoneyMan",
	    			columns, selection, null, groupBy, null, orderBy);
	    	c.moveToFirst();
	    	item = new String[c.getCount()];
	    	for (int i = 0; i < item.length; i++) {
	    		if( i==0 )
	    		{
	    			iMinMonthIndex = Integer.parseInt( c.getString(1).substring(4, 6) ) - 1;
	    			iMaxMonthIndex = iMinMonthIndex;
	    			strStartDate = TableControler.getFmtMonth( c.getString(1) );
	    			strWorkYear = strStartDate.substring(0,4);
	    			strStartYear = strWorkYear;
	    			strItems = "[\"" + strWorkYear + "\"";
	    		}
	    		if( false == strWorkYear.equals( c.getString(1).substring(0,4) ) )
	    		{
	    			// 年が変わった。
	    			iMinMonthIndex = 0;
	    			iMaxMonthIndex = 11;
	    			for( int k=iMinMonthIndex; k<=iMaxMonthIndex; k++ )
	    			{
	    				//if( k!=iMinMonthIndex )
	    				//{
	    					strItems += ", ";
	    				//}
	    				if( blnGetDataMonth[k] )
	    				{
	    					strItems += String.valueOf( arrYearCashs[k] );
	    				}
	    				else
	    				{
	    					strItems += "null";
	    				}
	    			}
	    			strItems += "]\n";
	    			
	    			strWorkYear = c.getString(1).substring(0,4);
	    			strEndYear = strWorkYear;
	    			strItems += ",[\"" + strWorkYear + "\"";
	    			arrYearCashs = new int[12];
	    			iCurrentCash = 0;
	    			for( int x=0; x < 12; x++ )
	    			{
	    				blnGetDataMonth[x] = false;
	    			}	    			
	    		}
	    		//strEndDate = TableControler.getFmtMonth( c.getString(1) );
	    		int index = Integer.parseInt( c.getString(1).substring(4, 6) ) - 1 ;
	    		if( iMaxMonthIndex < index )
	    		{
	    			iMaxMonthIndex = index;
	    		}
	    		iCurrentCash += c.getInt(0);
	    		if( iCashMin > iCurrentCash )
	    		{
	    			iCashMin = iCurrentCash;
	    		}
	    		if( iCurrentCash > iCashMax )
	    		{
	    			iCashMax = iCurrentCash;
	    		}
	    		for( int p=index; p < 12; p++ )
	    		{
	    			arrYearCashs[p] = iCurrentCash;
	    		}
	    		blnGetDataMonth[index] = true;
	    	    c.moveToNext();
	    	}
	    	c.close();
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
		for( int k=iMinMonthIndex; k<=iMaxMonthIndex; k++ )
		{
			//if( k!=iMinMonthIndex )
			//{
				strItems += ", ";
			//}
			if( blnGetDataMonth[k] )
			{
				strItems += String.valueOf( arrYearCashs[k] );
			}
			else
			{
				strItems += "null";
			}
		}
		strItems += "]\n";
		
		if( strEndYear.length() > 0 )
		{
			strTitle = "収支グラフ " + strStartYear + "～" + strEndYear;
		}
		else
		{
			strTitle = "収支グラフ " + strStartYear;
		}
		
		// 最大値を計算
		if( iCashMax > 0 )
		{
			for(int q=0; q<MAX_GRAPH;q+=iCashUnit)
			{
				if( iCashMax < q )
				{
					strGraphMax = String.valueOf(q);
					break;
				}
			}
		}
		if( iCashMin < 0 )
		{
			for(int q=0; q>MIN_GRAPH;q-=iCashUnit)
			{
				if( q < iCashMin )
				{
					strGraphMin = String.valueOf(q);
					break;
				}
			}
		}
		
		for( int k=iMinMonthIndex+1; k<=iMaxMonthIndex+1; k++ )
		{
			strX += ", \"" + String.valueOf( k ) + "\"";
		}
		strX += "]\n";
		
		int iGraphYdiff = Integer.parseInt( strGraphMax ) - Integer.parseInt( strGraphMin );
		int iYUnit = iGraphYdiff / iY_SepCnt;
		int iYCurrent = Integer.parseInt( strGraphMin );
		
		strY += ", " + String.valueOf( iYCurrent );
		for( int k=0; k<iY_SepCnt; k++ )
		{
			iYCurrent += iYUnit;
			strY += ", " + String.valueOf( iYCurrent );
		}
		strY += "]\n";
		
		
		// ファイルの内容を置換することによって作成する。

		
		strFileContent 
		= strFileContent.replace( "[TITLE]", strTitle ); 
		strFileContent 
		= strFileContent.replace( "[ITEMS]", strItems ); 
		strFileContent 
		= strFileContent.replace( "[X]", strX ); 
		strFileContent 
		= strFileContent.replace( "[Y]", strY ); 
		strFileContent 
		= strFileContent.replace( "[GRAPH_MAX]", strGraphMax ); 
		strFileContent 
		= strFileContent.replace( "[GRAPH_MIN]", strGraphMin ); 
		strFileContent 
		= strFileContent.replace( "[WIDTH]", strWidth ); 
		strFileContent 
		= strFileContent.replace( "[HEIGHT]", strHeight ); 
		
/*		new AlertDialog.Builder(this)
		.setTitle("Degug")
		.setMessage( strFileContent)
		.setPositiveButton(
			android.R.string.ok,
			new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}
		)
		.show();*/        	
		
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
		WebView wv = (WebView)this.findViewById(R.id.wvViewGraphTest);

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
}
