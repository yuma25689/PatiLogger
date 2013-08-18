package yuma25689.pati;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
// import java.util.Arrays;
import java.util.Date;

//import mediba.ad.sdk.android.openx.MasAdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.content.pm.ActivityInfo;
//import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
//import android.content.res.Resources;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PaintFlagsDrawFilter;
//import android.graphics.PixelFormat;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
//import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
//import android.widget.TextView;
import android.widget.Toast;

public class PatiLogger extends Activity 
	implements AdapterView.OnItemClickListener, Runnable {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();

	private static final String DEFAULT_QR_CODE_APP_PACKAGE = "com.google.zxing.client.android";
	private static final int MAINMENU_FIRST_IDX = 1;
	private static final int LAUNCH_FIRST_IDX = 8;
	private ListView listView;
	private ArrayList<MainMenuData> list = null;
	//private static final int PICKFILE_RESULT_CODE = 1;
	//private String filePath = null;
	// 入力中のレコードのテーブル情報等を保持
	//private SharedPreferences mUpdInf;
	
	private ProgressDialog progressDialog = null;
	private DB_IO_Thread thread = null;
	String fileExport = null;
	DBHelper dbHelper = null;
	/////////////// 定数 /////////////////
	public static final String EXPORT_FILE_DIR = "/sdcard/patiman/export";
	public static final String EXPORT_FILE_NAME = "_export.xml";
	public static final String ERROR_MSG_KEY = "patiman_err_msg";
	public static final String PROGRESS_VAL_KEY = "patiman_progress_val";
	static final String DATETIME_FORMAT = "yyyy年MM月dd日HH時mm分ss秒";
	public static final int END_MSG_ID = 0;
	public static final int ERROR_MSG_ID = 1;
	public static final int PROGRESS_MAX_MSG_ID = 2;
	public static final int PROGRESS_VAL_MSG_ID = 3;
	public static final int PROGRESS_VAL_INCL_MSG_ID = 4;
	
	// 別スレッド実行中のイベントを処理するハンドラ
	private final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {

			switch( msg.what )
			{
			case ERROR_MSG_ID:
				OKDialog(
					PatiLogger.this,
					msg.getData().getStringArray(ERROR_MSG_KEY)[0],
					msg.getData().getStringArray(ERROR_MSG_KEY)[1]
				);	
				return;
			case PROGRESS_MAX_MSG_ID:
				progressDialog.setMax(
					msg.getData()
						.getInt(PROGRESS_VAL_KEY) );
				progressDialog.setProgress( 0 );
				return;
			case PROGRESS_VAL_INCL_MSG_ID:
				progressDialog.incrementProgressBy( 1 );
				return;
			default:
				break;
			}			
			super.handleMessage(msg);
		}
	};
	int iImpMethod = DB_IO_Thread.PROC_TYPE_NONE;
	String[] strFileList = null;
	DialogInterface.OnClickListener importProc =
		new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dlgIF, int which )
			{
				String impFileNm = strFileList[which];

				progressDialog = new ProgressDialog(PatiLogger.this);
				progressDialog.setTitle(
					PatiLogger.this.getString(R.string.DLG_TITLE_IMPORT_PROGRESS));
		        progressDialog.setMessage(
		        		PatiLogger.this.getString(R.string.MSG_IMPORT_PROGRESS));
		        progressDialog.setIndeterminate(false);
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//.STYLE_HORIZONTAL);
		        progressDialog.show();

		        thread = new DB_IO_Thread(
		        		PatiLogger.this,
		        	handler,
		        	PatiLogger.this,
		        	dbHelper.getWritableDatabase(),
		        	iImpMethod,
		        	EXPORT_FILE_DIR,
		        	impFileNm,
		        	dbHelper
		        );
	            thread.start();
			};
		};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
        mUpdInf = this.getSharedPreferences("updview_tmpvalues",Activity.MODE_PRIVATE);
        
        if( 1 == mUpdInf.getInt( "last_finish_not_upd_close", 0 ) )
        {
        	// 前回は、入力中に終了されてしまった場合
        	// 入力中の状態から復帰する
        	TableOpenInfo tblInf_ = new TableOpenInfo();
        	tblInf_.setDBName( mUpdInf.getString(getString(R.string.db_name),null) );
        	tblInf_.setTblName( mUpdInf.getString("upd_tablename",null) );
        	tblInf_.setCursorFactory(null);
        	tblInf_.setExcptColumnSpec(mUpdInf.getString("upd_excptClmn",null));
        	tblInf_.setWherePhrase(mUpdInf.getString("upd_where",null));
        	tblInf_.setColumnsCreateText(mUpdInf.getString("upd_ClmnCreate",null));
        	tblInf_.setDBVer( mUpdInf.getInt("upd_DBver",-1) );
        	
        	if( tblInf_.getTblName() == null )
        	{
        		// 状態がおかしい
        		// できたら、「復元に失敗しました」等のメッセージを表示
        		
        	}
        	else
        	{
	    		ArrayList<String> arrCantDpl = null;
	    		String strCantDpl_ = mUpdInf.getString("upd_CantDpl",null);
	    		String strCantDpl[] = strCantDpl_.split(",");			    		
	    		arrCantDpl = new ArrayList<String>( Arrays.asList(strCantDpl) );
	
	    		Intent intent = new Intent( this,
	    				RecordUpdateActivity.class );
	    		ArrayList<TableOpenInfo> arrTblInf = new ArrayList<TableOpenInfo>();
	    		arrTblInf.add(tblInf_);
	    		intent.putExtra(getString(R.string.key_update),
	    				mUpdInf.getInt(getString(R.string.key_update), -1 ));
	    		intent.putExtra(getString(R.string.key_tblLevel), 
	    				mUpdInf.getInt(getString(R.string.key_tblLevel), -1 ));
	    		intent.putExtra(getString(R.string.key_id), 
	    				mUpdInf.getInt(getString(R.string.key_id), -1 ));
	    		intent.putExtra(getString(R.string.key_dlgTitle), 
	    				mUpdInf.getString(getString(R.string.key_dlgTitle), null ) );
	    		intent.putExtra(getString(R.string.key_tblMainClmn),
	    				mUpdInf.getString(getString(R.string.key_tblMainClmn), null ));
	    		intent.putExtra(getString(R.string.key_cantDpl), 
	    				arrCantDpl);
	    		intent.putExtra(getString(R.string.key_addMsgId), 
	    				mUpdInf.getString(getString(R.string.key_addMsgId),null));    			
	    		intent.putExtra(getString(R.string.key_tblOpenInfArray),
	    				arrTblInf);
	    		intent.putExtra(getString(R.string.key_tblSubClmn1), 
	    				mUpdInf.getString(getString(R.string.key_tblSubClmn1),null));
	    		intent.putExtra(getString(R.string.key_tblSubClmn2), 
	    				mUpdInf.getString(getString(R.string.key_tblSubClmn2),null));
	    		intent.putExtra(getString(R.string.key_tblSubClmn3), 
	    				mUpdInf.getString(getString(R.string.key_tblSubClmn3),null));
	    		//intent.putExtra("DBHelper", dbMachine);
	    		startActivity( intent );
        	}
        }*/
        setContentView(R.layout.main);
        
        setTitle(getString(R.string.DlgTitle_Main));
        commLogic.init(this);

        /*
        libAdMaker AdMaker = (libAdMaker)findViewById(R.id.admakerview);
        AdMaker.setActivity(this);
        AdMaker.siteId = "2318";
        AdMaker.zoneId = "6229";
        AdMaker.setUrl("http://images.ad-maker.info/apps/g6j7xncu5e83.html");
        AdMaker.start();		
        */
        listView = (ListView) this.findViewById(R.id.lstMain);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(this);
        ColorDrawable dw = new ColorDrawable(Color.GRAY);
        listView.setDivider(dw);
        listView.setDividerHeight(1);
        dbHelper = new PatiManDBHelper(this);
    	
        //updateAdapter();
    }

    @Override
    protected void onResume() {
    	/*
        mUpdInf = this.getSharedPreferences("updview_tmpvalues",Activity.MODE_PRIVATE);

        if( 1 == mUpdInf.getInt( "last_finish_not_upd_close", 0 ) )
        {
        	// 前回は、入力中に終了されてしまった場合
        	// 入力中の状態から復帰する
        	TableOpenInfo tblInf_ = new TableOpenInfo();
        	tblInf_.setDBName( mUpdInf.getString(getString(R.string.db_name),null) );
        	tblInf_.setTblName( mUpdInf.getString("upd_tablename",null) );
        	tblInf_.setCursorFactory(null);
        	tblInf_.setExcptColumnSpec(mUpdInf.getString("upd_excptClmn",null));
        	tblInf_.setWherePhrase(mUpdInf.getString("upd_where",null));
        	tblInf_.setColumnsCreateText(mUpdInf.getString("upd_ClmnCreate",null));
        	tblInf_.setDBVer( mUpdInf.getInt("upd_DBver",-1) );
        	
        	if( tblInf_.getTblName() == null )
        	{
        		// 状態がおかしい
        		// できたら、「復元に失敗しました」等のメッセージを表示
        		
        	}
        	else
        	{
	    		ArrayList<String> arrCantDpl = null;
	    		String strCantDpl_ = mUpdInf.getString("upd_CantDpl",null);
	    		String strCantDpl[] = strCantDpl_.split(",");			    		
	    		arrCantDpl = new ArrayList<String>( Arrays.asList(strCantDpl) );
	
	    		Intent intent = new Intent( this,
	    				RecordUpdateActivity.class );
	    		ArrayList<TableOpenInfo> arrTblInf = new ArrayList<TableOpenInfo>();
	    		arrTblInf.add(tblInf_);
	    		intent.putExtra(getString(R.string.key_update),
	    				mUpdInf.getInt(getString(R.string.key_update), -1 ));
	    		intent.putExtra(getString(R.string.key_tblLevel), 
	    				mUpdInf.getInt(getString(R.string.key_tblLevel), -1 ));
	    		intent.putExtra(getString(R.string.key_id), 
	    				mUpdInf.getInt(getString(R.string.key_id), -1 ));
	    		intent.putExtra(getString(R.string.key_dlgTitle), 
	    				mUpdInf.getString(getString(R.string.key_dlgTitle), null ) );
	    		intent.putExtra(getString(R.string.key_tblMainClmn),
	    				mUpdInf.getString(getString(R.string.key_tblMainClmn), null ));
	    		intent.putExtra(getString(R.string.key_cantDpl), 
	    				arrCantDpl);
	    		intent.putExtra(getString(R.string.key_addMsgId), 
	    				mUpdInf.getString(getString(R.string.key_addMsgId),null));    			
	    		intent.putExtra(getString(R.string.key_tblOpenInfArray),
	    				arrTblInf);
	    		intent.putExtra(getString(R.string.key_tblSubClmn1), 
	    				mUpdInf.getString(getString(R.string.key_tblSubClmn1),null));
	    		intent.putExtra(getString(R.string.key_tblSubClmn2), 
	    				mUpdInf.getString(getString(R.string.key_tblSubClmn2),null));
	    		intent.putExtra(getString(R.string.key_tblSubClmn3), 
	    				mUpdInf.getString(getString(R.string.key_tblSubClmn3),null));
	    		//intent.putExtra("DBHelper", dbMachine);
	    		startActivity( intent );
        	}
        }*/
    	updateAdapter();
    	super.onResume();
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
    		case MAINMENU_FIRST_IDX:
    			// 収支管理インテントを呼び出す
    			// Intentの呼び出し
    			intent = new Intent( this, MoneyManActivity .class );
    			startActivity( intent );
    			break;
    		case MAINMENU_FIRST_IDX + 1:
    			// 台管理インテントを呼び出す
    			// Intentの呼び出し
    			intent = new Intent( this, MachineManActivity .class );
    			startActivity( intent );
    			break;
    		case MAINMENU_FIRST_IDX + 2:
    			// 店管理インテントを呼び出す
    			// Intentの呼び出し
    			intent = new Intent( this, ParlorsManActivity .class );
    			startActivity( intent );
    			break;
    		case MAINMENU_FIRST_IDX + 3:
    			// メーカー管理インテントを呼び出す
    			// Intentの呼び出し
    			intent = new Intent( this, MakerManActivity .class );
    			startActivity( intent );
    			break;    			
    			/*
    		case 3:
    			// 判別インテントを呼び出す
    			String clazz = "com.mytools.SloTool";//.class"; 
    			intent = new Intent( Intent.ACTION_MAIN );
    			int idx = clazz.lastIndexOf('.'); 
                String pkg = clazz.substring(0, idx); 
                intent.setClassName(pkg, clazz); 
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                startActivity(intent);     			
    			break;*/
    		case MAINMENU_FIRST_IDX + 4:
    			// データ表示インテントを呼び出す
    			// Intentの呼び出し
    			intent = new Intent( this, DataViewMenuActivity .class );
    			startActivity( intent );
    			break;
    		case MAINMENU_FIRST_IDX + 5:
    			// グラフ表示インテントを呼び出す
    			intent = new Intent( this, GraphViewMenuActivity.class );
    			startActivity( intent );
    			break;
    			
    		case LAUNCH_FIRST_IDX://MAINMENU_FIRST_IDX + 7:
    			// QRコードアプリを呼び出す
    			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    			String strPkgName = sp.getString( "lst_qr_launch_key", "" );
    			//Uri uri=Uri.fromParts("package",,null);
    			intent = new Intent( Intent.ACTION_MAIN, null );
    			intent.addCategory(Intent.CATEGORY_LAUNCHER);
    			//startActivity( intent );
    	        // インストールされているアプリの一覧を取得する
    	        PackageManager pm = getPackageManager();
    	        List<ResolveInfo> appInfo = pm.queryIntentActivities(intent, 0);
    	        if( appInfo != null )
    	        {
    	        	for( ResolveInfo info : appInfo )
    	        	{
    	        		if( info.activityInfo.packageName.equals(strPkgName) )
    	        		{
    	        			//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    	        			intent.setClassName(strPkgName, info.activityInfo.name );
    	        			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	        			startActivity( intent );
    	        			break;
    	        		}
    	        	}
    	        }
    			break;
    		}
    		
    	} catch( Exception e ) {
    		Toast.makeText( this, e.getMessage(), Toast.LENGTH_SHORT );
    	}
    }
    
    /*
     * オプションメニューの作成
     */
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean ret = super.onCreateOptionsMenu(menu);
    	menu.add( 0, Menu.FIRST, Menu.NONE, "エクスポート").setIcon(android.R.drawable.stat_sys_download);
    	menu.add( 0, Menu.FIRST + 1, Menu.NONE, "インポート").setIcon(android.R.drawable.stat_sys_upload);
    	menu.add( 0, Menu.FIRST + 2, Menu.NONE, "設定").setIcon(android.R.drawable.ic_menu_preferences);
    	return ret;
    }
    
    /**
     * オプションメニューの選択イベント
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case Menu.FIRST:
        	//ChooseFile();
			// エクスポート
			// モード選択・・・したいんだけど・・・
			Date timeOfExport = new Date();
	        SimpleDateFormat dtFmt = 
	        	new SimpleDateFormat( DATETIME_FORMAT );
			final String strTimeOfExport = dtFmt.format(timeOfExport);			

			fileExport = strTimeOfExport + EXPORT_FILE_NAME;

			new AlertDialog.Builder(this)
			.setTitle(getString(R.string.DLG_TITLE_CONFIRM))
			.setMessage(getString(R.string.MSG_EXPORT_CONFIRM) 
					+ "[DIRECTORY]\n" + EXPORT_FILE_DIR + "\n" + "[FILE]\n" + fileExport )
			.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						progressDialog = new ProgressDialog(PatiLogger.this);
						progressDialog.setTitle(PatiLogger.this.getString(R.string.DLG_TITLE_EXPORT_PROGRESS));
				        progressDialog.setMessage(PatiLogger.this.getString(R.string.MSG_EXPORT_PROGRESS));
				        progressDialog.setIndeterminate(false);
				        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//.STYLE_HORIZONTAL);
				        progressDialog.show();

				        thread = new DB_IO_Thread(
				        	PatiLogger.this,
				        	handler,
				        	PatiLogger.this,
				        	dbHelper.getReadableDatabase(),
				        	DB_IO_Thread.PROC_TYPE_EXPORT_ALL,
				        	EXPORT_FILE_DIR,
				        	fileExport,
				        	dbHelper
				        );
			            thread.start();
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
     
        case Menu.FIRST + 1:
        	// インポート
        	//ChooseFile();
			// インポート
			// モード選択
			new AlertDialog.Builder(PatiLogger.this)
			.setTitle(getString(R.string.TITLE_IMPORT_MODE))
			//.setMessage(getString(R.string.MSG_SELECT_IMPORT_MODE))
			.setItems( getResources().getStringArray(R.array.import_modes),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(
							DialogInterface dialog,
							int which) 
					{
						String strConfirmMsg = "";
						switch( which )
						{
							// 確認ダイアログ用のテキストをモードによって変えて作成
						case 0:
							// 追加:重複許さない
							strConfirmMsg = getString(R.string.MSG_ADD_DONT_ALLOW_DUP);
							iImpMethod = DB_IO_Thread.PROC_TYPE_IMPORT_ADD_DUP_ERR;
							break;
						case 1:
							// 追加:重複無視
							strConfirmMsg = getString(R.string.MSG_ADD_IGNORE_DUP);
							iImpMethod = DB_IO_Thread.PROC_TYPE_IMPORT_ADD_DUP_IGNORE;
							break;
						case 2:
							// 全消し全入れ
							strConfirmMsg = getString(R.string.MSG_ALL_CLR_INSERT);
							iImpMethod = DB_IO_Thread.PROC_TYPE_IMPORT_ALL;
							break;
							
						}
						// 確認ダイアログ出力
						new AlertDialog.Builder(PatiLogger.this)
						.setTitle(getString(R.string.DLG_TITLE_CONFIRM))
						.setMessage(strConfirmMsg)
						.setPositiveButton(
							android.R.string.ok,
							new DialogInterface.OnClickListener() {	
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// ファイル選択ダイアログ
									FileChooseDialog dlg =
										new FileChooseDialog(
											PatiLogger.this,
											importProc,
											EXPORT_FILE_DIR
										);
									
									try
									{
										strFileList = dlg.getFileList();
									}
									catch( Exception e )
									{
										OKDialog( PatiLogger.this,
												getString(R.string.TITLE_ERROR),
												e.getMessage() );
										return;
									}
									if( strFileList == null )
									{
										OKDialog( PatiLogger.this,
											getString(R.string.TITLE_ERROR),
											getString(R.string.MSG_IMP_FILE_NONE)
												+ EXPORT_FILE_DIR
										);
										return;
									}

									dlg.show(
										getString(R.string.TITLE_FILE_SELECT),
										getString(R.string.MSG_FILE_SELECT)
									);
								}
							}
						)
						.setNegativeButton(
							android.R.string.cancel,
							new DialogInterface.OnClickListener() {	
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// 処理なし
								}
							}
						)
						.show();

						
					}
				}
			)
			.show();       	
            return true;
        case Menu.FIRST + 2:
    		Intent intent = new Intent( this,
    				MainPreferenceActivity.class );
        	startActivity( intent );
        	
        	return true;
        }
        return false;
    }    
/*
    public void ChooseFile() {

    	//filePath = null;
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
              intent.setType("file/*");
        startActivityForResult(intent,PICKFILE_RESULT_CODE);     
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    		case PICKFILE_RESULT_CODE:
    			if(resultCode==RESULT_OK){
    				//String FilePath = data.getData().getPath();
    				//filePath = FilePath;
    			}
    			break;
    	}
    }
*/
    // 配列アダプタの更新
    private void updateAdapter() {
    	this.list = new ArrayList<MainMenuData>();
    	String[] item;
    	// 配列アダプタの生成
    	//ArrayAdapter<String> adapter;

    	// セパレータの挿入
    	MainMenuData separatorMenu = new MainMenuData();
    	separatorMenu.setMenuString( "メインメニュー" );
    	separatorMenu.setResId( MainMenuAdapter.RES_ID_SEPARATOR);
    	list.add(separatorMenu);
    	
    	// リソースからString配列を取得する
    	item = getResources().getStringArray(R.array.main_menu);
    	for( int i=0; i < item.length; i++ ) {
    		MainMenuData data = new MainMenuData();
        	data.setMenuString( item[i] );
        	switch( i )
    		{
    		case 0:
    			// 収支管理インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.slo );
    			break;    			
    		case 1:
    			// 台管理インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.machine_2_toriaezu );
    			break;
    		case 2:
    			// 店管理インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.parlor_toriaezu );
    			break;
    		case 3:
    			// 店管理インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.maker2 );
    			break;
    			/*
    		case 3:
    			// 判別インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.hanbetu );
    			break;
    			*/
    		case 4:
    			// データ表示インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.report );
    			break;
    		case 5:
    			// グラフ表示インテントを呼び出す
            	//data.setResId( R.drawable.blank );
            	data.setResId( R.drawable.graph );
    			break;
    		default:
    			data.setResId( R.drawable.icon );
    			break;
    		}        	
        	list.add( data );
    	}
    	
    	// セパレータの挿入
    	MainMenuData separator = new MainMenuData();
    	separator.setMenuString( "他のアプリを起動する" );
    	separator.setResId( MainMenuAdapter.RES_ID_SEPARATOR);

    	// アプリランチャメニュー
    	MainMenuData launch = new MainMenuData();
    	
    	// TODO: 設定からQRコードアプリ名を取得する
    	
		//SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	launch.setResId(MainMenuAdapter.RES_ID_LAUNCHER);
    	//separator.setResId( MainMenuAdapter.RES_ID_SEPARATOR);
    	boolean blnGetLaunchApp = false;
    	// アイコンを取得する
    	Drawable icon = null;
    	String strLabel = "";
    	// TODO: もっとスマートなやり方に直せるかどうか検討の余地あり
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String strPkgName = sp.getString( "lst_qr_launch_key", "" );
		boolean blnPkgNull = false;
		if( strPkgName.equals("") )
		{
			blnPkgNull = true;
			strPkgName = DEFAULT_QR_CODE_APP_PACKAGE;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN,null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		//Uri uri=Uri.fromParts("package",,null);
		//startActivity( intent );
        // インストールされているアプリの一覧を取得する
        PackageManager pm = getPackageManager();
        List<ResolveInfo> appInfo = pm.queryIntentActivities(intent, 0);
        if( appInfo != null )
        {
        	for( ResolveInfo info : appInfo )
        	{
        		if( info.activityInfo.packageName.equals(strPkgName) )
        		{
        			icon = pm.getApplicationIcon(info.activityInfo.applicationInfo);
        			strLabel = (String) pm.getApplicationLabel(info.activityInfo.applicationInfo);
        			Drawable iconResized = ImageUtility.resizeIcon(icon,this);
        			launch.setmIcon(iconResized);
        			blnGetLaunchApp = true;
        			//startActivity( intent );
        			break;
        		}
        	}
        }
    	launch.setMenuString( strLabel );
    	if( blnGetLaunchApp )
    	{
	    	list.add(separator);
	    	list.add(launch);
	    	if( blnPkgNull )
	    	{
				Editor ed = sp.edit();
				ed.putString( "lst_qr_launch_key", strPkgName );
				ed.commit();
	    	}
    	}

    	MainMenuAdapter adapter =
        		new MainMenuAdapter(this, R.layout.lst_menu_row, list);
    	adapter.addDisableIndex(0);
    	adapter.addDisableIndex(7);

    	// このリストビューでは、ランチャー機能で横の画面が起動したときにエラーになるのを防止する目的で、
    	// 必ずリスト項目の再作成を行うように設定する
    	adapter.setNeedRecreate( true );
    	//adapter = new ArrayAdapter<String>( this,
    	//	android.R.layout.simple_list_item_1,
    	//	item );
    	listView.setAdapter(adapter);
    }

	@Override
	public void run() {
		// エクスポート／インポートダイアログ終了時のイベント
    	String strMsgOpt = getString(R.string.MSG_CLEAR_ALL);
		progressDialog.dismiss();
		if( true == thread.getResult() )
		{
			switch( thread.getProcType() )
			{
				case DB_IO_Thread.PROC_TYPE_EXPORT_ALL:				
			    	OKDialog(
							PatiLogger.this,
							getString(R.string.DLG_TITLE_EXPORT_OK),
							getString(R.string.MSG_EXPORT_OK)
							+ "[DIRECTORY]\n" + EXPORT_FILE_DIR
							+ "\n" + "[FILE]\n" + thread.getFileNm() );
			    	break;
				case DB_IO_Thread.PROC_TYPE_IMPORT_ADD_DUP_IGNORE:
				case DB_IO_Thread.PROC_TYPE_IMPORT_ADD_DUP_ERR:
					strMsgOpt = getString(R.string.MSG_ADD);
				case DB_IO_Thread.PROC_TYPE_IMPORT_ALL:		
			    	OKDialog(
			    		PatiLogger.this,
						getString(R.string.DLG_TITLE_IMPORT_OK),
						getString(R.string.MSG_IMPORT_OK)
						+ "[DIRECTORY]\n" + EXPORT_FILE_DIR
						+ "\n" + "[FILE]\n" + thread.getFileNm() + "\n\n"
						+ strMsgOpt + "\n"
						+ getString(R.string.MACHINE_INF) + thread.getImpCateCnt() + getString(R.string.COUNT) + "\n"
						+ getString(R.string.PARLOR_INF) + thread.getImpTimeCnt() + getString(R.string.COUNT) + "\n"
						+ getString(R.string.MAKER_INF) + thread.getImpMakerCnt() + getString(R.string.COUNT) + "\n"
						+ getString(R.string.MONEY_INF) + thread.getImpMemoCnt() + getString(R.string.COUNT) + "\n"
					);
			        break;
			}
		}		
	}
	// ダイアログの表示
	private void OKDialog(
			final Activity activity,
			String title, String text )
	{
		new AlertDialog.Builder(activity)
		.setTitle(title)
		.setMessage(text)
		.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
		.create()
		.show();
	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		dbHelper.close();
	}
	public static boolean isExistLastFinishNotUpdData( Context ctx, String strTblName )
	{
		SharedPreferences UpdInf = ctx.getSharedPreferences("updview_tmpvalues",Activity.MODE_PRIVATE);
		int iExist = UpdInf.getInt( strTblName + "last_finish_not_upd_close", 0 );
		if( iExist == 1 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}