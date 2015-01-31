package yuma25689.pati;

import java.util.Date;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

//import mediba.ad.sdk.android.openx.MasAdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;

public class RecordUpdateActivity extends Activity
	implements View.OnClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	private static final String DEFAULT_QR_CODE_APP_PACKAGE = "com.google.zxing.client.android";
		
	private final String COL_NM_ID = "_id";
	private final String COL_INS_DT = "InsDatetime";
	private final String COL_UPD_DT = "LastUpdDatetime";

	// ユーザによって終了中かどうか
	private boolean blnUserFinishing = false;
	// 入力中のレコードのテーブル情報等を保持
	private SharedPreferences mUpdInf;

	// リクエストコード
	private final int MACHINE_REQUEST_CODE = 101;
	private final int PARLOR_REQUEST_CODE = 102;
	private final int MAKER_REQUEST_CODE = 103;

	//String strTblName = null;
	//TableOpenInfo tblOpnInf;
	String strDlgTitle = null;
	String strTblMainClmnNm = null;
	String strTblSubClmn1Nm = null;
	String strTblSubClmn2Nm = null;
	String strTblSubClmn3Nm = null;
	int iRecId = -1;
	ArrayList<TableOpenInfo> arrTblInf = null;
	ArrayList<String> arrCantDplClmns = null;
	ArrayList<ColumnInfo> arrGuiValue = null;
	ArrayList<ColumnInfo> arrGuiValueLastUpdated = null;
	int iUpdateFlg = 0;
	int iTblLevel = 0;
	DBHelper dbHelper = null;
	String strTitle = null;
	TableOpenInfo tblOpnInf = null;
	boolean blnNextTblExists = false;
	boolean blnUpdate = false;
	String addMsg = null;
	String updMsg = null;
	// ボタンキメうちはあまりよくない
	// 他のツールでも使いたいので・・・
	Button btnBack = null;
	Button btnClear = null;
	Button btnRegist = null;
	Button btnNext = null;
	ImageButton btnQR = null;

	RecordUpdater vMain = null;

	int iRequestResult = 0;
	
	private final String strNullInsert = "null";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUpdInf = this.getSharedPreferences("updview_tmpvalues",Activity.MODE_PRIVATE);
		try {
			Bundle extras = getIntent().getExtras();
			if( extras != null ) {
				iRequestResult = extras.getInt(getString(R.string.key_request_result));				
				iUpdateFlg = extras.getInt(getString(R.string.key_update));
				iTblLevel = extras.getInt(getString(R.string.key_tblLevel));
				iRecId = extras.getInt( getString(R.string.key_id) );
				strDlgTitle = extras.getString( getString(R.string.key_dlgTitle) );
				strTblMainClmnNm = extras.getString(getString(R.string.key_tblMainClmn));
				strTblSubClmn1Nm = extras.getString(getString(R.string.key_tblSubClmn1));
				strTblSubClmn2Nm = extras.getString(getString(R.string.key_tblSubClmn2));
				strTblSubClmn3Nm = extras.getString(getString(R.string.key_tblSubClmn3));
				addMsg = extras.getString(getString(R.string.key_addMsgId));
				updMsg = extras.getString(getString(R.string.key_updMsgId));
				if( getString(R.string.key_cantDpl ) != null )
				{
					arrCantDplClmns = (ArrayList<String>) extras.getSerializable( getString(R.string.key_cantDpl ));
				}
				else
				{
					arrCantDplClmns = null;
				}
				arrTblInf = (ArrayList<TableOpenInfo>)
					extras.getSerializable(getString(R.string.key_tblOpenInfArray));
				//dbHelper = (DBHelper)extras.getSerializable("DBHelper");
				tblOpnInf = arrTblInf.get(iTblLevel);
			}
		} catch ( Exception e ) {
			Log.e("Error", e.getMessage());
		}

		blnUpdate = ( iUpdateFlg == 1 );//!( tblOpnInf.getWherePhrase() == null 
		//|| tblOpnInf.getWherePhrase().length() < 1 );
		
        if( blnUpdate == false && 1 == mUpdInf.getInt( tblOpnInf.getTblName() + "last_finish_not_upd_close", 0 ) )
        {
        	// 新規追加時で、
        	// 前回は、入力中に終了されてしまった場合
        	// 入力中の状態から復帰する
			try
			{
				// onPauseの時、ファイルに保存されているはず。
				FileInputStream fis = this.openFileInput(tblOpnInf.getTblName() + "upd_tmp_value.dat");
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				Parcel p2 = Parcel.obtain();
				p2.unmarshall(buffer, 0, buffer.length);
				p2.setDataPosition(0);
				RecordStatParcel prcl = (RecordStatParcel) p2.readValue(RecordStatParcel.class.getClassLoader());
				arrGuiValue = prcl.getValue();
				fis.close();
			} catch( IOException ex ) {
				Toast.makeText(this,"入力中の値の復元に失敗しました。" + ex.getMessage(), Toast.LENGTH_LONG).show();
			}        	
        }
		try {
			setContentView(R.layout.record_update);
		} catch( Exception e ) {
			Log.e("Error", e.getMessage());
		}
        commLogic.init(this);
		
		// 次のテーブルが存在するかどうか
		blnNextTblExists = iTblLevel + 1 < arrTblInf.size(); 
		
		String strNum = Integer.toString( iTblLevel + 1 );
		
		// このActivityのタイトルを設定する
		if( 1 < arrTblInf.size() )
		{ 
			setTitle( strDlgTitle + "(" + strNum + ")" );
		}
		else
		{
			setTitle( strDlgTitle );
		}
		
		dbHelper = new PatiManDBHelper(this);
		
		LinearLayout btm = (LinearLayout)this.findViewById(R.id.rec_upd_bottomBar);
		LayoutParams prms_w1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
				, LayoutParams.WRAP_CONTENT, (float)1.0 );
		LayoutParams prms_w2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT
				, LayoutParams.WRAP_CONTENT );
		btnRegist = new Button(this);
		btnRegist.setText(getString(R.string.btnRegister));
		btnRegist.setLayoutParams(prms_w1);
		btnRegist.setOnClickListener(this);
		btm.addView(btnRegist);
		//btnClear= new Button(this);
		//btnClear.setText(getString(R.string.btnClear));//btnBack));
		//btnClear.setOnClickListener(this);
		//btnClear.setLayoutParams(prms_w1);
		//btm.addView(btnClear);
		btnBack = new Button(this);
		btnBack.setText(getString(R.string.btnBack));
		btnBack.setOnClickListener(this);
		btnBack.setLayoutParams(prms_w1);
		btm.addView(btnBack);

		// QRコードアプリを呼び出す
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String strPkgName = sp.getString( "lst_qr_launch_key", "" );
		if( strPkgName.equals("") )
		{
			strPkgName = DEFAULT_QR_CODE_APP_PACKAGE;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN,null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		if( tblOpnInf != null 
		&& tblOpnInf.getTblName().equals( getString(R.string.MoneyTblName ) ) )
		{
			boolean blnGetLaunchApp = false;
	        // インストールされているアプリの一覧を取得する
	        PackageManager pm = getPackageManager();
	        List<ResolveInfo> appInfo = pm.queryIntentActivities(intent, 0);
	        Drawable icon = null;
		   	Drawable iconResized = null;
			        
	        if( appInfo != null )
	        {
	        	for( ResolveInfo info : appInfo )
	        	{
	        		if( info.activityInfo.packageName.equals(strPkgName) )
	        		{
	        		   	icon = pm.getApplicationIcon(info.activityInfo.applicationInfo);
	        			iconResized = ImageUtility.resizeIcon(icon,this,64,64);
	           		   	blnGetLaunchApp = true;
	        			break;
	        		}
	        	}
	        }
	        if( blnGetLaunchApp )
	        {
	        	btnQR = new ImageButton(this);
	        	btnQR.setImageDrawable(iconResized);
	        	btnQR.setOnClickListener(this);
	        	btnQR.setLayoutParams(prms_w2);
	    		btm.addView(btnQR);      	
	        }
		}
		if( blnNextTblExists )
		{
			// 次のテーブルがある場合、次のテーブルへ移動するボタンを表示する
			btnNext = new Button(this);
			btnNext.setText(getString(R.string.btnNext));
			btnNext.setOnClickListener(this);
		}
		SharedPreferences.Editor editor;
		if( false == blnUpdate )
		{
			editor = mUpdInf.edit();
			// 更新中にする
			//editor.putInt("last_finish_not_upd_close", 1 );
			editor.putInt(tblOpnInf.getTblName() + "last_finish_not_upd_close", 1 );
			editor.commit();
		}
	}

	private void finishIntent()
	{
		finishIntent(true);
	}
	private void finishIntent(boolean blnClear)
	{
		try {
			blnUserFinishing = blnClear;
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(vMain.getWindowToken(), 0);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		finish();
	}

	// ユーザが入力済みかどうか調べてその結果を返す
	private boolean isUserInput() {

		boolean blnUserInput = false;

		RecordUpdater updV =
			(RecordUpdater)this.findViewById(R.id.record_updator);

		ArrayList<ColumnInfo> arrVals = updV.getValues();
		for( int i=0; i<arrVals.size(); i++ )
		{
			for( int j=0; j<arrGuiValueLastUpdated.size(); j++ )
			{
				if( arrVals.get(i).equals( arrGuiValueLastUpdated.get(i)) )
				{
					if( false == arrVals.get(i).colAndValEquals(arrGuiValueLastUpdated.get(i)) )
					{
						blnUserInput = true;
						break;
					}
				}
			}
		}
		return blnUserInput;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
			// 更新されているかチェックする。
			if( isUserInput() )
			{
				/*
				// 更新されていたら破棄していいかどうか聞く
		        AlertDialog.Builder alert = new AlertDialog.Builder(this);
		        alert.setTitle("確認");
		        alert.setMessage("入力した値は破棄されますが、画面を終了してもよろしいですか？");
		        alert.setPositiveButton("はい", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Yesボタンが押された時の処理
		            	finishIntent();
		            }});
		        alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Noボタンが押された時の処理
		            }});
		        alert.show();
		        */
				finishIntent(false);
			}
			else
			{
				finishIntent();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		// 終了ボタン
		if( v==btnBack ) {
			// 更新されているかチェックする。
			if( isUserInput() )
			{
				/*
				// 更新されていたら破棄していいかどうか聞く
		        AlertDialog.Builder alert = new AlertDialog.Builder(this);
		        alert.setTitle("確認");
		        alert.setMessage("入力した値は破棄されますが、画面を終了してもよろしいですか？");
		        alert.setPositiveButton("はい", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Yesボタンが押された時の処理
		            	finishIntent();
		            }});
		        alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Noボタンが押された時の処理
		            }});
		        alert.show();
		        */
				finishIntent(false);
			}
			else
			{
				finishIntent();
			}
			//finish();
		}
		// クリアボタン
		else if( v==btnClear )
		{
			if( isUserInput() )
			{
				// 更新されていたら破棄していいかどうか聞く
		        AlertDialog.Builder alert = new AlertDialog.Builder(this);
		        alert.setTitle("確認");
		        alert.setMessage("入力した値を破棄して画面をクリアします。よろしいですか？");
		        alert.setPositiveButton("はい", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Yesボタンが押された時の処理
		        		vMain = (RecordUpdater)RecordUpdateActivity.this.findViewById(R.id.record_updator);
		        		vMain.setControlsForRecord(null);//tblOpnInf.getWherePhrase());
		            }});
		        alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Noボタンが押された時の処理
		            }});
		        alert.show();
		        return;
			}
			else
			{
				vMain = (RecordUpdater)RecordUpdateActivity.this.findViewById(R.id.record_updator);
        		vMain.setControlsForRecord(null);//tblOpnInf.getWherePhrase());
			}			
		}
		// 一時保存ボタン
		//else if( v==btnTmpSave)
		//{
		//	finishIntent(false);
		//}
		// 登録ボタン
		else if( v==btnRegist )
		{
			btnBack.setEnabled(false);
			btnRegist.setEnabled(false);
			
			boolean blnUpdateClmnExists = false;
			//ContentValues 
			String strQuery = null;
			if( blnUpdate == false )
			{
				// 新規追加
				strQuery = "insert into " + tblOpnInf.getTblName()
						 + " values(";
			} else if ( blnUpdate == true ) {
				// 更新
				strQuery = "update " + tblOpnInf.getTblName() + " set ";
			}

			// 現在日時を取得しておく
			Calendar now = Calendar.getInstance();
			Date d = now.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

			// ※ ひどい作り・・・
			// テーブルカラム情報の取得
			RecordUpdater updV =
				(RecordUpdater)this.findViewById(R.id.record_updator);
			ArrayList<ColumnInfo> arrClmns = updV.getColumnInfs();
			// 画面設定値の取得
			ArrayList<ColumnInfo> arrGUIVals = updV.getValues();

			// 画面にないカラムの取得
			String[] strExceptColumns = null;
			if(!( tblOpnInf.getExcptColumnSpec()== null 
					|| tblOpnInf.getExcptColumnSpec().length() < 1 ) )
			{
				strExceptColumns = tblOpnInf.getExcptColumnSpec().split(",");
			}

			String strMessageClmnVal = null;
			String strComma = "";
			Iterator<ColumnInfo> iterator = arrClmns.iterator(); 
			while( iterator.hasNext() )
			{
				// 全てのカラムをループする
				ColumnInfo clmnTmp = iterator.next();
				String strClmnVal = new String();
				boolean blnColumnOnGUI = true;
				boolean blnContinue = false;

				// 画面にあるカラムかどうかを判定する
				if( strExceptColumns != null )
				{
					for( int i=0; i<strExceptColumns.length; i++)
					{
						if( clmnTmp.getStrColumnName().equals( strExceptColumns[i] ) )
						{
							// 画面から値を取得できないカラム
							blnColumnOnGUI = false;
							if( clmnTmp.getStrColumnName().equals( COL_NM_ID ) )
							{
								// idのカラムだった場合
								if( blnUpdate == false )
								{
									// 新規の場合
									// nullにする
									strClmnVal = strNullInsert;
								}
								else
								{
									// 更新の場合、スルー
									blnContinue = true;
									break;
								}
							}
							else if( clmnTmp.getStrColumnName().equals( COL_INS_DT ) )
							{
								if( blnUpdate == false )
								{
									// 作成日時
									strClmnVal = sdf.format(d);
								}
								else
								{
									// strClmnVal = strNullInsert;
									// 更新の場合、スルー
									blnContinue = true;
									break;
								}
							}
							else if( clmnTmp.getStrColumnName().equals( COL_UPD_DT ) )
							{
								// 更新日時
								if( blnUpdate == true )
								{
									strClmnVal = sdf.format(d);
								}
								else
								{
									strClmnVal = strNullInsert;									
								}

							}
							else
							{
								// それ以外のカラム
								if( blnUpdate == false )
								{
									strClmnVal = strNullInsert;
								}
								else
								{
									// 更新の場合、スルー
									blnContinue = true;
									break;
								}
							}
							break;
						}
					}
				}
				if( blnContinue == true )
				{
					continue;
				}
				// 画面から値を取得できるカラムならば、ここで値を取得
				if( blnColumnOnGUI == true )
				{
					// 最低の作り
					// 画面上の値とマッチングする
					Iterator<ColumnInfo> itGUI = arrGUIVals.iterator(); 
					while( itGUI.hasNext() )
					{
						ColumnInfo clmnGUITmp = itGUI.next();
						if( clmnGUITmp.equals(clmnTmp))
						{
							strClmnVal = clmnGUITmp.getExtraVal1();
							if( strTblMainClmnNm.equals(clmnTmp.getStrColumnName()) )
							{
								strMessageClmnVal = strClmnVal;
							}
							else if( strTblSubClmn1Nm.equals(clmnTmp.getStrColumnName()) )
							{
								if( strClmnVal != null && strClmnVal.length() > 0 )
								{
									strMessageClmnVal += " " + strClmnVal;
								}
							}
							else if( strTblSubClmn2Nm.equals(clmnTmp.getStrColumnName()) )
							{
								if( strClmnVal != null && strClmnVal.length() > 0 )
								{
									// TODO: ビタ打ち直す
									if( strTblSubClmn2Nm.equals( "McnId"))
									{
										strMessageClmnVal += " " + TableControler.getMcnNameFromMcnId(this, dbHelper, strClmnVal );
									}
									else
									{
										strMessageClmnVal += " " + strClmnVal;
									}
								}
							}
							else if( strTblSubClmn3Nm.equals(clmnTmp.getStrColumnName()) )
							{
								if( strClmnVal != null && strClmnVal.length() > 0 )
								{
									// TODO: ビタ打ち直す
									if( strTblSubClmn3Nm.equals( "ParlorId"))
									{
										strMessageClmnVal += " " + TableControler.getParlorNameFromParlorId(this, dbHelper, strClmnVal );
									}
									else
									{
										strMessageClmnVal += " " + strClmnVal;
									}
								}
							}
							break;
						}
					}
					// 更新の場合、得られた値が前の値と変更されているかどうかチェックする
					if( blnUpdate == true )
					{
						if( clmnTmp.getPreviewVal() == null  )
						{
							if( strClmnVal == null || strClmnVal.length() < 1 )
							{
								// 変更なし
								// この値はスルーする
								continue;
							}
							else
							{
								// strClmnValがnullでなければ、変更はあり
								// このカラムの値は、前の値と変更されている
								blnUpdateClmnExists = true;
							}
						}
						else
						{
							if( clmnTmp.getPreviewVal().equals( strClmnVal ) == true )
							{
								// 変更なし
								// この値はスルーする
								continue;
							}
							else
							{
								// このカラムの値は、前の値と変更されている
								blnUpdateClmnExists = true;
							}
						}
					}					
				}

				// 値をクエリ発行用の値に変換する(文字列の場合、''で括る)
				if( clmnTmp.getiColumnType() == ColumnInfo.COLUMN_TYPE_TEXT )
				{
					if( strClmnVal != null && strClmnVal.length() > 0 
						&& false == strClmnVal.equals( strNullInsert ) )
					{
						// シングルクォーテーションは、エスケープする
						strClmnVal = strClmnVal.replace("'", "''");
						strClmnVal = "'" + strClmnVal + "'";
					}
				}


				if( strClmnVal == null || strClmnVal.length() < 1 )
				{
					// 画面から得られた値がない
					// ※ どうやら、0以外。sqliteのpragma tableinfoで返却されるnotnullの値は、android1.6だと99だが、2.1だと1を返している
					//if( clmnTmp.getIntNotNull() == 99 )
					if( clmnTmp.getIntNotNull() != 0 )
					{
						// NotNullなのに得られた値がNull
						View vVal = updV.getValView(clmnTmp.getStrColumnName());
						String strErr = getString(R.string.StrItem)
							+ getString(R.string.StrMsgPrlnStart)
							+ updV.getColumnNameForShow( clmnTmp.getStrColumnName() )
							+ getString(R.string.StrMsgPrlnEnd)
							+ getString(R.string.ErrNotNull);
						if( vVal instanceof EditText ) {
							// エディットコントロールの場合、そこにエラー情報を設定
							((EditText)vVal).setError( strErr );
						}
						else
						{
							// そうでない場合、Toast出力
							Toast.makeText(this, strErr, Toast.LENGTH_LONG ).show();
						}
						vVal.requestFocus();
						// 登録せずに処理終了
						btnBack.setEnabled(true);
						btnRegist.setEnabled(true);						
						return;
					}
					else
					{
						// NotNullでない
						// strNullInsertに書き換える
						strClmnVal = strNullInsert;
					}
				}
				
				// 重複チェック。
				// ただし、NULLの場合は重複チェックしないので、重複チェックする項目は必ずNOT NULLにする。
				if( arrCantDplClmns != null && arrCantDplClmns.contains( clmnTmp.getStrColumnName() ) )
				{
					if( strClmnVal != strNullInsert )
					{
						String[] clmns = { clmnTmp.getStrColumnName() };
						Cursor cTmp = null;
						SQLiteDatabase dbRead = null;
						try {
							// データベースの値で同じ値のものを拾ってくる
							dbRead = dbHelper.getReadableDatabase();
							cTmp = dbRead.query(tblOpnInf.getTblName(),
								clmns,
								clmnTmp.getStrColumnName() + "=" + strClmnVal,
								null, null, null, null);
							if( 0 < cTmp.getCount() )
							{
								// 同じ値のものがデータベースに存在するので、重複エラーで終了する
								View vVal = updV.getValView(clmnTmp.getStrColumnName());
								String strErr = updV.getColumnNameForShow( clmnTmp.getStrColumnName() )
									+ getString(R.string.StrMsgPrlnStart)
									+ strClmnVal
									+ getString(R.string.StrMsgPrlnEnd)
									+ getString(R.string.ErrDpl);
								if( vVal instanceof EditText ) {
									// エディットコントロールの場合、そこにエラー情報を設定
									((EditText)vVal).setError( strErr );
								}
								else
								{
									// そうでない場合、Toast出力
									Toast.makeText(this, strErr, Toast.LENGTH_LONG ).show();
								}
								vVal.requestFocus();
								// 登録せずに処理終了
								btnBack.setEnabled(true);
								btnRegist.setEnabled(true);								
								return;
							}
						} catch( Exception e ) {
								
						} finally {
							cTmp.close();
							dbRead.close();
						}
					}
				}
				
				if( blnUpdate == false ) {
					// 新規追加
					strQuery = strQuery + strComma + strClmnVal;
				}
				else
				{
					// 更新
					strQuery = strQuery + strComma + clmnTmp.getStrColumnName() + "=" + strClmnVal;					
				}
				strComma = ",";
			}
			if( blnUpdate == false )
			{
				strQuery += ")";
			}
			else
			{
				strQuery += " Where " + tblOpnInf.getWherePhrase(); 
				if( blnUpdateClmnExists == false )
				{
					Toast.makeText(this, getString(R.string.not_updated), Toast.LENGTH_SHORT).show();
					btnBack.setEnabled(true);
					btnRegist.setEnabled(true);					
					return;
				}
			}
			// TODO: 暫定版
			String strMsg = null;
			if( blnUpdate == false )
			{
				//strMsg = getString(R.string.msg_new_machine_add);
				strMsg = addMsg;
			}
			else
			{
				//strMsg = getString(R.string.msg_upd_machine);
				strMsg = updMsg;				
			}
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.beginTransaction();

			MessageFormat msgAddOK = new MessageFormat(strMsg);
			String[] strAddOK_prms = {strMessageClmnVal};
			int RetId = -1;
			
			try {
				if( blnUpdate == false )
				{
					// 新規の場合、次にautoincrementで登録される_idを取得
					Cursor c_ = db.rawQuery(
							"SELECT * FROM sqlite_sequence where name = '"
							+ tblOpnInf.getTblName() + "'", null );
					if( c_.getCount() > 0 )
					{
						c_.moveToFirst();
						RetId = c_.getInt(c_.getColumnIndex("seq"));
					}
					else
					{
						RetId = 0;
					}
					c_.close();
					RetId++;
				}				
				db.execSQL(strQuery);
				db.setTransactionSuccessful();
				Toast.makeText(this,  msgAddOK.format(strAddOK_prms), Toast.LENGTH_LONG ).show();
			} catch( Exception e ) {
				Log.e("insert_err", e.getMessage());
			} finally {
				db.endTransaction();
				db.close();
			}
			if( blnUpdate == true )
			{
				updV.setControlsForRecord(tblOpnInf.getWherePhrase());
				// arrGuiValue = updV.getValues();
				arrGuiValueLastUpdated = updV.getValues();
				finishIntent();
			}
			else
			{
				if( iRequestResult == 1 )
				{
					// 値を返却するように要求されていた場合
					// 返すデータ(Intent&Bundle)の作成
					Intent data = new Intent();
					Bundle bundle = new Bundle();
					//SQLiteDatabase db_ = dbHelper.getReadableDatabase();

					bundle.putInt(getString(R.string.key_request_id), RetId);
					
					//c_.close();
					//db_.close();
					
					data.putExtras(bundle);

					setResult(RESULT_OK, data);		
				}
								
				finishIntent();
				//finish();
			}
			btnBack.setEnabled(true);
			btnRegist.setEnabled(true);
		}
		else if( v==btnNext )
		{
			// 次のテーブルの
			
			// Intentの呼び出し
			Intent intent = new Intent( this, RecordUpdateActivity.class );

			intent.putExtra(getString(R.string.key_tblOpenInfArray), arrTblInf);
			intent.putExtra(getString(R.string.key_tblLevel), iTblLevel + 1);
			//intent.putExtra("DBHelper", dbMachine);
			startActivity( intent );
		}
		else if( v==btnQR )
		{
			// QRコードアプリを呼び出す
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			String strPkgName = sp.getString( "lst_qr_launch_key", "" );
			//Uri uri=Uri.fromParts("package",,null);
			Intent intent = new Intent( Intent.ACTION_MAIN, null );
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
		}		
	}
	// onPauseと同じくらいのタイミング?必ず呼ばれるわけではない
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		RecordUpdater updV =
			(RecordUpdater)this.findViewById(R.id.record_updator);
		arrGuiValue = updV.getValues();
		outState.putParcelable("record_restore",new RecordStatParcel(arrGuiValue));
	}
	// onResumeと同じくらいのタイミング?必ずではないかも
	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		iTblLevel = extras.getInt(getString(R.string.key_tblLevel));
		arrTblInf = (ArrayList<TableOpenInfo>)
			extras.getSerializable(getString(R.string.key_tblOpenInfArray));
		vMain = (RecordUpdater)this.findViewById(R.id.record_updator);
		vMain.setStrTblName(tblOpnInf.getTblName());
		vMain.setStrExcptColumnSpec(tblOpnInf.getExcptColumnSpec());
		vMain.setDbHelper(dbHelper);
		vMain.setControlsForRecord(tblOpnInf.getWherePhrase());		
		RecordStatParcel prcl = savedInstanceState.getParcelable("record_restore");
		arrGuiValue = prcl.getValue();
		if( arrGuiValue != null )
		{
			RecordUpdater updV =
				(RecordUpdater)this.findViewById(R.id.record_updator);
			updV.setValues(arrGuiValue);
		}
	}
	// 開始時に必ず呼ばれる(OnCreateの後)
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		Bundle extras = getIntent().getExtras();
		iTblLevel = extras.getInt(getString(R.string.key_tblLevel));
		arrTblInf = (ArrayList<TableOpenInfo>)extras.getSerializable(getString(R.string.key_tblOpenInfArray));
		vMain = (RecordUpdater)this.findViewById(R.id.record_updator);
		vMain.setStrTblName(tblOpnInf.getTblName());
		vMain.setStrExcptColumnSpec(tblOpnInf.getExcptColumnSpec());
		vMain.setDbHelper(dbHelper);
		vMain.setControlsForRecord(tblOpnInf.getWherePhrase());
		RecordUpdater updV =
			(RecordUpdater)this.findViewById(R.id.record_updator);
		if( arrGuiValueLastUpdated == null )
		{
			// レコード更新直後の値を保持
			arrGuiValueLastUpdated = updV.getValues();
		}
		if( arrGuiValue != null )
		{
			updV.setValues(arrGuiValue);
		}
		//else
		//{
			// 新規追加と思われるが、初期の値を取得しておく
			//arrGuiValue = updV.getValues();
		//}
		super.onResume();
	}
	// onPauseは終了時必ず呼ばれる
	@Override
	protected void onPause() {
		RecordUpdater updV =
			(RecordUpdater)this.findViewById(R.id.record_updator);
		arrGuiValue = updV.getValues();

		if( blnUpdate == false )
		{
			mUpdInf = this.getSharedPreferences("updview_tmpvalues",Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor;
			editor = mUpdInf.edit();
	
			if( blnUserFinishing == true )
			{
				editor.putInt("last_finish_not_upd_close", 0 );
				editor.putInt(tblOpnInf.getTblName() + "last_finish_not_upd_close", 0 );
			}
			else
			{
				editor.putInt(tblOpnInf.getTblName() + "last_finish_not_upd_close", 1 );
				editor.putString("upd_tablename", tblOpnInf.getTblName() );
				editor.putString("upd_excptClmn", arrTblInf.get(0).getExcptColumnSpec() );
				editor.putString("upd_where", arrTblInf.get(0).getWherePhrase() );
				editor.putString("upd_ClmnCreate", arrTblInf.get(0).getColumnsCreateText() );
				editor.putInt("upd_DBver", arrTblInf.get(0).getDBVer() );
				String strCantDplString = "";
				if( arrCantDplClmns != null )
				{
					for( String strDplClm : arrCantDplClmns )
					{
						if( strCantDplString.length() > 0 )
						{
							strCantDplString += ",";
						}
						strCantDplString += strDplClm;
					}
				}
				editor.putString("upd_CantDpl", strCantDplString );
				editor.putString(getString(R.string.db_name), arrTblInf.get(0).getDBName() );
				editor.putInt(getString(R.string.key_update), iUpdateFlg );
				editor.putInt(getString(R.string.key_tblLevel), iTblLevel );
				editor.putInt(getString(R.string.key_id), iRecId );
				editor.putString(getString(R.string.key_dlgTitle), strDlgTitle );
				editor.putString(getString(R.string.key_tblMainClmn), strTblMainClmnNm );
				editor.putString(getString(R.string.key_addMsgId), addMsg );
				editor.putString(getString(R.string.key_updMsgId), updMsg );
				//editor.putString(getString(R.string.key_tblOpenInfArray),
				editor.putString(getString(R.string.key_tblSubClmn1), strTblSubClmn1Nm );
				editor.putString(getString(R.string.key_tblSubClmn2), strTblSubClmn2Nm );
				editor.putString(getString(R.string.key_tblSubClmn3), strTblSubClmn3Nm );
				
				try
				{
					FileOutputStream fos = this.openFileOutput(tblOpnInf.getTblName() + "upd_tmp_value.dat", Context.MODE_PRIVATE);
					RecordStatParcel p = new RecordStatParcel(arrGuiValue);
					Parcel p1 = Parcel.obtain();
				    p1.writeValue(p);
					fos.write(p1.marshall());
					fos.flush();
					fos.close();
				} catch( IOException ex ) {
					Toast.makeText(this,"入力中の値の保持に失敗しました。" + ex.getMessage(), Toast.LENGTH_LONG).show();
					editor.putInt("last_finish_not_upd_close", 0 );
					editor.putInt(tblOpnInf.getTblName() + "last_finish_not_upd_close", 0 );
				}
				 
			}
			editor.commit();
		}
		super.onPause();
	}
	
	public void startMachineIntentForResult()
	{
		Intent intent = new Intent( this,
				MachineManActivity .class );
		intent.putExtra(this.getString(R.string.key_request_result),
				1);
		this.startActivityForResult( intent,
				MACHINE_REQUEST_CODE );		
	}
	public void startParlorIntentForResult()
	{
		Intent intent = new Intent( this,
				ParlorsManActivity .class );
		intent.putExtra(this.getString(R.string.key_request_result), 1);
		this.startActivityForResult( intent, PARLOR_REQUEST_CODE );		    			
	}
	public void startMakerIntentForResult()
	{
		Intent intent = new Intent( this,
				MakerManActivity .class );
		intent.putExtra(this.getString(R.string.key_request_result), 1);
		this.startActivityForResult( intent, MAKER_REQUEST_CODE );		    			
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case MACHINE_REQUEST_CODE:
			if (resultCode == RESULT_OK)
			{
				RecordUpdater updV =
					(RecordUpdater)this.findViewById(R.id.record_updator);
				arrGuiValue = updV.getValues();

				Bundle bundle = data.getExtras();
				ColumnInfo ci = updV.getValue("McnId");
				int iMcnId = bundle.getInt(getString(R.string.key_machine_id));
				ci.setExtraVal1(Integer.toString(iMcnId));
				for( int i=0; i < arrGuiValue.size(); i++ )
				{
					ColumnInfo ci2 = arrGuiValue.get(i);
					if( ci2.getStrColumnName().equals( ci.getStrColumnName() ))
					{
						arrGuiValue.remove(i);
						arrGuiValue.add(i,ci);
						break;
					}
				}
			}
			break;
		case PARLOR_REQUEST_CODE:
			if (resultCode == RESULT_OK)
			{
				RecordUpdater updV =
					(RecordUpdater)this.findViewById(R.id.record_updator);
				arrGuiValue = updV.getValues();

				Bundle bundle = data.getExtras();
				ColumnInfo ci = updV.getValue("ParlorId");
				int iParlorId = bundle.getInt(getString(R.string.key_parlor_id));
				ci.setExtraVal1(Integer.toString(iParlorId));
				for( int i=0; i < arrGuiValue.size(); i++ )
				{
					ColumnInfo ci2 = arrGuiValue.get(i);
					if( ci2.getStrColumnName().equals( ci.getStrColumnName() ))
					{
						arrGuiValue.remove(i);
						arrGuiValue.add(i,ci);
						break;
					}
				}
			} 
			break;
		case MAKER_REQUEST_CODE:
			if (resultCode == RESULT_OK)
			{
				RecordUpdater updV =
					(RecordUpdater)this.findViewById(R.id.record_updator);
				arrGuiValue = updV.getValues();

				Bundle bundle = data.getExtras();
				ColumnInfo ci = updV.getValue("MakerId");
				int iParlorId = bundle.getInt(getString(R.string.key_maker_id));
				ci.setExtraVal1(Integer.toString(iParlorId));
				for( int i=0; i < arrGuiValue.size(); i++ )
				{
					ColumnInfo ci2 = arrGuiValue.get(i);
					if( ci2.getStrColumnName().equals( ci.getStrColumnName() ))
					{
						arrGuiValue.remove(i);
						arrGuiValue.add(i,ci);
						break;
					}
				}
			} 
			break;
		default:
			break;
		}		
	}
    /*
     * オプションメニューの作成
     */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean ret = super.onCreateOptionsMenu(menu);
    	String strMenu = null;
    	int iMenuId = 0;
    	if( blnUpdate )
    	{
    		strMenu = "リロード";
    		iMenuId = android.R.drawable.stat_sys_download;
    	}
    	else
    	{
    		strMenu = "クリア";
    		iMenuId = android.R.drawable.ic_input_delete;    	
    	}
    	menu.add( 0, Menu.FIRST, Menu.NONE, strMenu).setIcon(iMenuId);
    	return ret;
    }
    
    /**
     * オプションメニューの選択イベント
     */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case Menu.FIRST:
        	// リロードORクリア
        	if( blnUpdate )
        	{
        		// リロード
				// 画面をクリアしていいかどうか聞く
		        AlertDialog.Builder alert = new AlertDialog.Builder(this);
		        alert.setTitle("確認");
		        alert.setMessage("入力中の値があれば破棄されますが、画面をリロードしてもよろしいですか？");
		        alert.setPositiveButton("はい", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Yesボタンが押された時の処理
		        		vMain = (RecordUpdater)RecordUpdateActivity.this.findViewById(R.id.record_updator);
		        		vMain.setControlsForRecord(tblOpnInf.getWherePhrase());
		    			RecordUpdater updV =
		    				(RecordUpdater)RecordUpdateActivity.this.findViewById(R.id.record_updator);
		    			if( arrGuiValue != null )
		    			{
		    				updV.setValues(arrGuiValue);
		    			}
		            }});
		        alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Noボタンが押された時の処理
		            }});
		        alert.show();				
        	}
        	else
        	{
        		// クリア
				// 画面をクリアしていいかどうか聞く
		        AlertDialog.Builder alert = new AlertDialog.Builder(this);
		        alert.setTitle("確認");
		        alert.setMessage("入力中の値があれば破棄されますが、画面をクリアしてもよろしいですか？");
		        alert.setPositiveButton("はい", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Yesボタンが押された時の処理
		        		vMain = (RecordUpdater)RecordUpdateActivity.this.findViewById(R.id.record_updator);
		        		vMain.setControlsForRecord(null);//tblOpnInf.getWherePhrase());
		            }});
		        alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		                //Noボタンが押された時の処理
		            }});
		        alert.show();				
        		
        		//vMain = (RecordUpdater)this.findViewById(R.id.record_updator);
        		//vMain.setControlsForRecord(tblOpnInf.getWherePhrase());
        	}
            return true;
          
        }
        return false;
    }	
	
}
