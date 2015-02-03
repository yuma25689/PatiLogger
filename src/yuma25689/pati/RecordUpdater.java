package yuma25689.pati;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
//import android.widget.Toast;
import android.widget.TimePicker;

/*
 * データベースのレコードを追加、更新するためのGUIを提供する
 * 
 * 
 */
public class RecordUpdater extends ScrollView {

	private final String STR_TARGET_MARK = "target";
	private final String STR_PARENT_MARK = "parent";
	private final String STR_TITLE_MARK = "title";
	
	private final int RATE_MAX = 5;
	private boolean blnInitEnd = false;

	// タグづけ用
	private final String TYPE_EDIT = "1";
	private final String TYPE_MULEDIT = "2";
	private final String TYPE_DATE = "3";
	private final String TYPE_STAR = "4";
	private final String TYPE_NUM = "5";
	private final String TYPE_MCN_ID = "6";
	private final String TYPE_PRL_ID = "7";
	private final String TYPE_TIME = "8";
	private final String TYPE_PATITYPE_ID = "9";
	private final String TYPE_MAKER_ID = "10";

	private int defaultYear;
	private int defaultMonth;
	private int defaultDay;
	private int defaultHour;
	private int defaultMinute;
	
	private int viewid = 10000; // 多分大丈夫・・・？
	
	private Context ctx;
	private String strTblName;
	private String strExcptColumnSpec; // 対象外カラム特定用文字列
	private String strWherePhrase;
	private DBHelper dbHelper = null;
	private ArrayList<ColumnInfo> arrClmnInf;

	public RecordUpdater(Context context ) {
		super(context);
		initView(context);
	}
	public RecordUpdater(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		ctx = context;
		//strTblName = strTblName_;
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View v = inflater.inflate( R.layout.record_update_view, null );
		addView(v);
		Calendar calendar = Calendar.getInstance();
		defaultYear = calendar.get(Calendar.YEAR);
		defaultMonth = calendar.get(Calendar.MONTH);
		defaultDay = calendar.get(Calendar.DATE);
		defaultHour = calendar.get(Calendar.HOUR_OF_DAY);
		defaultMinute = calendar.get(Calendar.MINUTE);
		blnInitEnd = true;
	}

	// TODO: クラス化して、動作を変更可能にすべき
	public String getTargetVal( View target )
	{
		String getVal = null;
		if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_EDIT 
		|| target.getTag(R.string.DATA_TYPE_KEY) == TYPE_MULEDIT
		)
		{
			if( ((TextView)target).getText().toString() != null 
				&& ((TextView)target).getText().toString().length() > 0 )
			{
				getVal = ((TextView)target).getText().toString();
			}
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_DATE)
		{
			if( target.getTag(R.string.DATE_DATAVALUE) != null )
			{
				getVal = target.getTag(R.string.DATE_DATAVALUE).toString();
			}
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_TIME)
		{
			if( target.getTag(R.string.TIME_DATAVALUE) != null )
			{
				getVal = target.getTag(R.string.TIME_DATAVALUE).toString();
			}
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_STAR)
		{
			getVal = ((TextView)target).getText().toString();
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_NUM)
		{
			getVal = ((TextView)target).getText().toString();
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_MCN_ID
				|| target.getTag(R.string.DATA_TYPE_KEY) == TYPE_PRL_ID 
				|| target.getTag(R.string.DATA_TYPE_KEY) == TYPE_MAKER_ID )
		{
			Spinner spinner = (Spinner)target;
			Cursor cursor = (Cursor)spinner.getSelectedItem();
			int getId = -1;
			if( cursor == null )
			{
				getId = -1;
			}
			else
			{
				getId = cursor.getInt(cursor.getColumnIndex("_id"));
			}
			//Toast.makeText(ctx,  
			//		Integer.valueOf(getId).toString(),  
			//		Toast.LENGTH_LONG).show();
			getVal = String.valueOf( getId );
		}
		else if( target.getTag( R.string.DATA_TYPE_KEY ) == TYPE_PATITYPE_ID )
		{
			Spinner spinner = (Spinner)target;
			getVal = String.valueOf( spinner.getSelectedItemPosition() );
		}
		
		return getVal;
	}
	public void setTargetVal( View target, ColumnInfo clmnTmp )
	{
		if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_EDIT 
		|| target.getTag(R.string.DATA_TYPE_KEY) == TYPE_MULEDIT
		)
		{
			((TextView)target).setText(clmnTmp.getExtraVal1());
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_DATE)
		{
			if( clmnTmp.getExtraVal1() != null )
			{
				((TextView)target).setText(
					TableControler.getFmtDate( clmnTmp.getExtraVal1() )
				);
			}
			else
			{
				((TextView)target).setText(null);
			}
			((TextView)target).setTag( R.string.DATE_DATAVALUE, clmnTmp.getExtraVal1());
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_TIME)
		{
			if( clmnTmp.getExtraVal1() != null )
			{
				((TextView)target).setText(
					TableControler.getFmtTime( clmnTmp.getExtraVal1() )
				);
			}
			else
			{
				((TextView)target).setText(null);
			}
			((TextView)target).setTag( R.string.TIME_DATAVALUE, clmnTmp.getExtraVal1() );
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_STAR)
		{
			((TextView)target).setText( clmnTmp.getExtraVal1() );
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_NUM)
		{
			((TextView)target).setText( clmnTmp.getExtraVal1() );
		}
		else if( target.getTag(R.string.DATA_TYPE_KEY) == TYPE_MCN_ID
				|| target.getTag(R.string.DATA_TYPE_KEY) == TYPE_PRL_ID
				|| target.getTag(R.string.DATA_TYPE_KEY) == TYPE_MAKER_ID )
		{
			Spinner spinner = (Spinner)target;
			for(int m=0;m<spinner.getCount();m++)
			{
				if( Integer.parseInt( clmnTmp.getExtraVal1() ) 
						== spinner.getItemIdAtPosition(m) )
				{
					spinner.setSelection(m);
				}
			}			
		}
		else if( target.getTag( R.string.DATA_TYPE_KEY ) == TYPE_PATITYPE_ID )
		{
			Spinner spinner = (Spinner)target;
			if(spinner.getCount() < Integer.parseInt( clmnTmp.getExtraVal1() ) )
			{
				spinner.setSelection(Integer.parseInt( clmnTmp.getExtraVal1() ) );
			}
		}
		
		return;
	}	
	/*
	 * 現在の設定値を取得する
	 * 新しい値は、ExtraVal1に設定する
	 */
	public ArrayList<ColumnInfo> getValues()
	{
		boolean blnGot = false;
		ArrayList<ColumnInfo> arrRet = new ArrayList<ColumnInfo>();

		// 全てのTableRowをループする
		TableLayout vParent = 
			(TableLayout)findViewById(R.id.rec_upd_tbl_layout );
		for( int i=0; i<vParent.getChildCount(); i++ )
		{
			blnGot = false;
			ColumnInfo clmnInfTmp = null;
			TableRow vTblRowTmp = 
				(TableRow)vParent.getChildAt(i);

			View targetTmp = null;
			String getVal = null;
			
			// TagがColumnInfo場合のみ、対象
			if( vTblRowTmp.getTag()
					instanceof ColumnInfo )
			{
				clmnInfTmp = 
					new ColumnInfo((ColumnInfo)vTblRowTmp.getTag());

				for( int j=0; j<vTblRowTmp.getChildCount();j++)
				{
					targetTmp = vTblRowTmp.getChildAt(j); 
					// タグに対象マークのついているコントロールのgetTextで値が取得できるものとする
					if( targetTmp.getTag() instanceof String 
						&& targetTmp.getTag() == STR_TARGET_MARK) 
					{
						getVal = getTargetVal(targetTmp);
						blnGot = true;
					}
					else if( targetTmp.getTag() instanceof String 
							&& targetTmp.getTag() == STR_PARENT_MARK)
					{
						// 親項目だった場合
						// (親項目は、1階層しかなく、親はLinearLayout固定とする)
						// 更に子も検索する
						for( int k=0; k<((LinearLayout)vTblRowTmp.getChildAt(j))
														.getChildCount();k++)
						{
							targetTmp = ((LinearLayout)vTblRowTmp.getChildAt(j))
														.getChildAt(k);
							if( targetTmp.getTag() instanceof String 
									&& targetTmp.getTag() == STR_TARGET_MARK) 
							{
								getVal = getTargetVal(targetTmp);
								blnGot = true;
								break;
							}
						}
					}
					if( blnGot == true )
					{
						// 新しい値は、ExtraVal1に設定する
						clmnInfTmp.setExtraVal1( getVal );
						break;
					}
				}
			}
			
			if( clmnInfTmp != null )
			{
				arrRet.add( clmnInfTmp );
			}
		}
		return arrRet;
	}
	/*
	 * 現在の設定値を取得する
	 * 新しい値は、ExtraVal1に設定する
	 */
	public ColumnInfo getValue(String strClmnName)
	{
		boolean blnGot = false;
		ColumnInfo ciRet = new ColumnInfo();

		// 全てのTableRowをループする
		TableLayout vParent = 
			(TableLayout)findViewById(R.id.rec_upd_tbl_layout );
		for( int i=0; i<vParent.getChildCount(); i++ )
		{
			blnGot = false;
			ColumnInfo clmnInfTmp = null;
			TableRow vTblRowTmp = 
				(TableRow)vParent.getChildAt(i);

			View targetTmp = null;
			String getVal = null;
			
			// TagがColumnInfoだった場合のみ、対象
			if( vTblRowTmp.getTag()
					instanceof ColumnInfo ) 
			{
				clmnInfTmp = 
					new ColumnInfo((ColumnInfo)vTblRowTmp.getTag());

				if( clmnInfTmp.getStrColumnName().equals(strClmnName) == false )
					continue;

				for( int j=0; j<vTblRowTmp.getChildCount();j++)
				{
					targetTmp = vTblRowTmp.getChildAt(j); 
					// タグに対象マークのついているコントロールのgetTextで値が取得できるものとする
					if( targetTmp.getTag() instanceof String 
						&& targetTmp.getTag() == STR_TARGET_MARK) 
					{
						getVal = getTargetVal(targetTmp);
						blnGot = true;
					}
					else if( targetTmp.getTag() instanceof String 
							&& targetTmp.getTag() == STR_PARENT_MARK)
					{
						// 親項目だった場合
						// (親項目は、1階層しかなく、親はLinearLayout固定とする)
						// 更に子も検索する
						for( int k=0; k<((LinearLayout)vTblRowTmp.getChildAt(j)).getChildCount();k++)
						{
							targetTmp = ((LinearLayout)vTblRowTmp.getChildAt(j)).getChildAt(k);
							if( targetTmp.getTag() instanceof String 
									&& targetTmp.getTag() == STR_TARGET_MARK) 
							{
								getVal = getTargetVal(targetTmp);
								blnGot = true;
								break;
							}
						}
					}
					if( blnGot == true )
					{
						// 新しい値は、ExtraVal1に設定する
						clmnInfTmp.setExtraVal1( getVal );
						break;
					}
				}
			}
			
			if( clmnInfTmp != null && clmnInfTmp.getStrColumnName().equals(strClmnName) )
			{
				ciRet = clmnInfTmp;
				break;
			}
		}
		return ciRet;
	}
	/*
	 * 上からもらった設定値をフォームに設定する
	 */
	public void setValues(ArrayList<ColumnInfo> arrSet)
	{
		// 全てのTableRowをループする
		TableLayout vParent = 
			(TableLayout)findViewById(R.id.rec_upd_tbl_layout );
		for( int i=0; i<vParent.getChildCount(); i++ )
		{
			ColumnInfo clmnInfTmp = null;
			TableRow vTblRowTmp = 
				(TableRow)vParent.getChildAt(i);

			View targetTmp = null;
			//String getVal = null;
			
			// TagがColumnInfoだった場合のみ、対象
			if( vTblRowTmp.getTag()
					instanceof ColumnInfo ) 
			{
				clmnInfTmp = 
					new ColumnInfo((ColumnInfo)vTblRowTmp.getTag());
				
				// マッチング
				for( ColumnInfo ci:arrSet )
				{
					if( clmnInfTmp.getStrColumnName().equals( ci.getStrColumnName() ) )
					{
						clmnInfTmp = ci;
						break;
					}
				}
				
				for( int j=0; j<vTblRowTmp.getChildCount();j++)
				{
					targetTmp = vTblRowTmp.getChildAt(j); 
					// タグに対象マークのついているコントロールのgetTextで値が取得できるものとする
					if( targetTmp.getTag() instanceof String 
						&& targetTmp.getTag() == STR_TARGET_MARK) 
					{
						setTargetVal(targetTmp, clmnInfTmp );
					}
					else if( targetTmp.getTag() instanceof String 
							&& targetTmp.getTag() == STR_PARENT_MARK)
					{
						// 親項目だった場合
						// (親項目は、1階層しかなく、親はLinearLayout固定とする)
						// 更に子も検索する
						for( int k=0; k<((LinearLayout)vTblRowTmp.getChildAt(j)).getChildCount();k++)
						{
							targetTmp = ((LinearLayout)vTblRowTmp.getChildAt(j)).getChildAt(k);
							if( targetTmp.getTag() instanceof String 
									&& targetTmp.getTag() == STR_TARGET_MARK) 
							{
								setTargetVal(targetTmp, clmnInfTmp );
								break;
							}
						}
					}
				}
			}
			
		}
		return;
	}
	/*
	 * 上からもらった設定値をフォームに設定する
	 */
	public void setValue(ColumnInfo ci)
	{
		// 全てのTableRowをループする
		TableLayout vParent = 
			(TableLayout)findViewById(R.id.rec_upd_tbl_layout );
		for( int i=0; i<vParent.getChildCount(); i++ )
		{
			ColumnInfo clmnInfTmp = null;
			TableRow vTblRowTmp = 
				(TableRow)vParent.getChildAt(i);

			View targetTmp = null;
			//String getVal = null;
			
			// TagがColumnInfoだった場合のみ、対象
			if( vTblRowTmp.getTag()
					instanceof ColumnInfo ) 
			{
				clmnInfTmp = 
					new ColumnInfo((ColumnInfo)vTblRowTmp.getTag());
				
				// マッチング
				if( clmnInfTmp.getStrColumnName().equals( ci.getStrColumnName() ) )
				{
					clmnInfTmp = ci;
				}
				else
				{
					continue;
				}
				
				for( int j=0; j<vTblRowTmp.getChildCount();j++)
				{
					targetTmp = vTblRowTmp.getChildAt(j); 
					// タグに対象マークのついているコントロールのgetTextで値が取得できるものとする
					if( targetTmp.getTag() instanceof String 
						&& targetTmp.getTag() == STR_TARGET_MARK) 
					{
						setTargetVal(targetTmp, clmnInfTmp );
					}
					else if( targetTmp.getTag() instanceof String 
							&& targetTmp.getTag() == STR_PARENT_MARK)
					{
						// 親項目だった場合
						// (親項目は、1階層しかなく、親はLinearLayout固定とする)
						// 更に子も検索する
						for( int k=0; k<((LinearLayout)vTblRowTmp.getChildAt(j)).getChildCount();k++)
						{
							targetTmp = ((LinearLayout)vTblRowTmp.getChildAt(j)).getChildAt(k);
							if( targetTmp.getTag() instanceof String 
									&& targetTmp.getTag() == STR_TARGET_MARK) 
							{
								setTargetVal(targetTmp, clmnInfTmp );
								break;
							}
						}
					}
				}
			}
			
		}
		return;
	}	
	/*
	 * カラム名からその値が設定されているコントロールを取得する
	 */
	public View getValView(String strColumnName)
	{
		View vRet = null;

		boolean blnGot = false;
		View targetTmp = null;
		
		// 全てのTableRowをループする
		TableLayout vParent = (TableLayout) findViewById(R.id.rec_upd_tbl_layout );
		for( int i=0; i<vParent.getChildCount(); i++ )
		{
			blnGot = false;
			ColumnInfo clmnInfTmp = null;
			TableRow vTblRowTmp = (TableRow) vParent.getChildAt(i);
			if( vTblRowTmp.getTag() instanceof ColumnInfo ) {
				clmnInfTmp = new ColumnInfo((ColumnInfo) vTblRowTmp.getTag());
				// 最初の子供のTagがColumnInfoだった場合のみ、対象
				
				if( clmnInfTmp.getStrColumnName().equals(strColumnName) )
				{
					// カラムの名前が指定されたものである
					for( int j=0; j<vTblRowTmp.getChildCount();j++) {
						targetTmp = vTblRowTmp.getChildAt(j); 
						// タグに対象マークのついているコントロールのgetTextで値が取得できるものとする
						if( targetTmp.getTag() instanceof String 
							&& targetTmp.getTag() == STR_TARGET_MARK) 
						{
							vRet = targetTmp;
							blnGot = true;
							break;
						}
						else if( targetTmp.getTag() instanceof String 
								&& targetTmp.getTag() == STR_PARENT_MARK)
						{
							// 親項目だった場合
							// (親項目は、1階層しかなく、親はLinearLayout固定とする)
							// 更に子も検索する
							for( int k=0; k<((LinearLayout)vTblRowTmp.getChildAt(j)).getChildCount();k++)
							{
								targetTmp = ((LinearLayout)vTblRowTmp.getChildAt(j)).getChildAt(k);
								if( targetTmp.getTag() instanceof String 
										&& targetTmp.getTag() == STR_TARGET_MARK) 
								{
									vRet = targetTmp;
									blnGot = true;
									break;									
								}
							}
							if( blnGot == true )
							{
								break;
							}
						}
					}
					break;
				}
			}
		}
		return vRet;
	}
	
	/*
	 * 現在ロード中のテーブルの全カラム情報を取得する
	 */
	public ArrayList<ColumnInfo> getColumnInfs()
	{
		return arrClmnInf;
	}

	/*
	 * 指定されたテーブルに合わせたコントロールをView上に配置する
	 * @param strRecWherePhrase コントロール上にロードするレコードを特定するWhere句
	 */
	public boolean 
		setControlsForRecord(String strRecWherePhrase) {

		// ---- 親となるテーブルレイアウトの取得
		// (もしかしたら、TableLayout決め打ちじゃなくてLayoutでもいいかもしれない)
		TableLayout tblLayout
			= (TableLayout)findViewById(R.id.rec_upd_tbl_layout );
		tblLayout.removeAllViewsInLayout();

		// ---- テーブルのカラム情報を取得する
		TableControler tbl = new TableControler(strTblName, dbHelper);
		arrClmnInf = tbl.getTableInfo(strRecWherePhrase);

		String[] strExceptColumns = null;
		if(strExcptColumnSpec != null)
		{
			strExceptColumns = strExcptColumnSpec.split(",");
		}
		boolean bThrough = false;

		// ----- 指定された列を行としてLayoutに追加することを試みる
		int iRowCnt=0;
		Iterator<ColumnInfo> iterator = arrClmnInf.iterator(); 
		while( iterator.hasNext() )
		{
			// 全てのカラムをループする
			ColumnInfo clmnTmp = iterator.next();

			// 除くカラムかどうかを判定する
			if( strExceptColumns != null )
			{
				bThrough = false;
				for( int i=0; i<strExceptColumns.length; i++)
				{
					if( clmnTmp.getStrColumnName().equals( strExceptColumns[i] ) )
					{
						// 除くリストに一致するものがあったフラグを立てる
						bThrough = true;
						break;
					}
				}
			}
			// 除くリストに一致するものがあったら、このカラムは飛ばす
			if( bThrough == true ) {
				continue;
			}

			// カラムの情報を設定する
			//String strClmnNmShow = clmnTmp.getStrColumnName();
			TableRow tblRow_clmnTitle = new TableRow(ctx);
			String strClmnNmShow = getColumnNameForShow( clmnTmp.getStrColumnName() );
			TextView txtClmnNm = new TextView(ctx);
			txtClmnNm.setText(strClmnNmShow);
			//params.span = 2;
			//txtClmnNm.setLayoutParams( new ViewGroup.LayoutParams(
			//		50, 50 ) );//params );
			txtClmnNm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
			txtClmnNm.setTag(STR_TITLE_MARK);
			//txtClmnNm.setPadding( 0, 0, 3, 0 );
			txtClmnNm.setBackgroundColor(Color.LTGRAY);
			tblRow_clmnTitle.setBackgroundColor(Color.BLUE);
			tblRow_clmnTitle.setLayoutParams(new TableRow.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT ));
			tblRow_clmnTitle.addView(txtClmnNm);
			tblRow_clmnTitle.setTag(STR_TITLE_MARK);
			//tblLayout.addView( tblRow_clmnTitle );

			TableRow tblRow = createTableRow(iRowCnt,strClmnNmShow,clmnTmp);
			if( tblRow != null ) {
				tblLayout.addView( tblRow );
				iRowCnt++;
			}
		}
		return true;
	}

	/**
	 * 行を作成する
	 * @param strClmnNm
	 * @see 型、引数等によってROWに置くコントロールを変更する
	 * @return
	 */
	public TableRow createTableRow(int iRowCnt, String strClmnNmShow, ColumnInfo clmnTmp) {
		LinearLayout.LayoutParams lp_fill_w = new LinearLayout.LayoutParams( 
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
		LinearLayout.LayoutParams lp_fill_1 = new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
		LinearLayout.LayoutParams lp_wrap = new LinearLayout.LayoutParams( 
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		LinearLayout.LayoutParams lp_wrap_1 = new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		lp_fill_1.weight = 1f;
		lp_wrap_1.weight = 1f;
		
		int iRowHdrColor = Color.LTGRAY;
		//int iRowColor = Color.WHITE;
		

		TableRow tblRow = new TableRow(ctx);
		tblRow.setTag(clmnTmp);
		
		//if( false == strClmnNmShow.equals("台"))
		//{
			TextView txtClmnNm = new TextView(ctx);
			txtClmnNm.setText(strClmnNmShow);
			if( iRowCnt % 2 != 0 )
			{
				iRowHdrColor = Color.rgb(0xbb, 0xbb, 0xbb);
				//iRowColor = Color.LTGRAY;
			}
			txtClmnNm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
			txtClmnNm.setTag(clmnTmp);
			txtClmnNm.setPadding( 5, 5, 5, 5 );
			txtClmnNm.setBackgroundColor(iRowHdrColor);
			txtClmnNm.setTextColor(Color.BLACK);		
			txtClmnNm.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
			tblRow.addView( txtClmnNm, new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT) );
		/*
		}
		else
		{
			Button btnClmnNm = new Button(ctx);
			btnClmnNm.setText(strClmnNmShow);
			btnClmnNm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
			btnClmnNm.setTag(clmnTmp);
			btnClmnNm.setTextColor(Color.BLACK);		
			btnClmnNm.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
			tblRow.addView( btnClmnNm, new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT) );
		}*/
		//LayoutParams lp_fill = new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT );

		tblRow.setLayoutParams(lp_fill_w);
		//tblRow.setBackgroundColor(Color.BLUE);
		// 型によって固有の処理
		// リストの中にあれば、その型にする
		// 日付型の項目名リスト
		String[] arrDateList = ctx.getResources().getStringArray(R.array.ctl_date);
		for( int i=0; i<arrDateList.length; i++)
		{
			if( clmnTmp.getStrColumnName().equals( arrDateList[i] ) )
			{
				// 一致したら日付型
				LinearLayout layoutTmp = new LinearLayout(ctx);
				//layoutTmp.setLayoutParams(lp_fill_w);
				//layoutTmp.setBackgroundColor(Color.RED);
				//layoutTmp.setGravity(Gravity.RIGHT);
				layoutTmp.setTag(STR_PARENT_MARK);
				//layoutTmp.setBackgroundColor(iRowColor);

				TextView txtDate = new TextView(ctx);
				//txtDate.setBackgroundColor(Color.MAGENTA);
				txtDate.setLayoutParams(lp_wrap_1);
				//txtDate.setGravity(Gravity.LEFT);
				txtDate.setTag(STR_TARGET_MARK);
				//txtDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
				txtDate.setId(viewid);
				txtDate.setTag(R.string.DATA_TYPE_KEY, TYPE_DATE);
				if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
				)
				{
					// 値が既にある場合それを設定する
					// 日付型の場合、変換をかます
					// このツールでは必ずdatabaseにはYYYYMMDDで入っている
					
					txtDate.setText(
							TableControler.getFmtDate( clmnTmp.getPreviewVal() )
					);
					txtDate.setTag( R.string.DATE_DATAVALUE, clmnTmp.getPreviewVal() );
				}
				else
				{
					//if(clmnTmp.getIntNotNull() == 99 )
					if(clmnTmp.getIntNotNull() != 0 )
					{
						// NOT NULL、かつ前に設定された値がない場合
						String strVal = 
	                		String.format("%04d",defaultYear) + "/" +
	                		String.format("%02d",defaultMonth+1) + "/" +
	                		String.format("%02d",defaultDay);
						String strDataVal = 
	                		String.format("%04d",defaultYear) +
	                		String.format("%02d",defaultMonth+1) +
	                		String.format("%02d",defaultDay);
						txtDate.setText( strVal );
						txtDate.setTag( R.string.DATE_DATAVALUE, strDataVal );
					}
				}

				Button btnDateChoice = new Button(ctx);
				btnDateChoice.setLayoutParams(lp_wrap);
				//btnDateChoice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
				btnDateChoice.setText(ctx.getString(R.string.BtnCap_Date));
				btnDateChoice.setTag(R.string.ASS_VIEW_ID,viewid);
				btnDateChoice.setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Calendar calendar = Calendar.getInstance();
							defaultYear = calendar.get(Calendar.YEAR);
							defaultMonth = calendar.get(Calendar.MONTH);
							defaultDay = calendar.get(Calendar.DATE);
							final int TXT_VIEW_ID = (int)(Integer)v.getTag(R.string.ASS_VIEW_ID);
							DatePickerDialog datePickerDialog = new DatePickerDialog(
						            ctx,
						            new DatePickerDialog.OnDateSetListener() {
						                @Override
						                public void onDateSet(
						                		DatePicker view, int year, int monthOfYear, int dayOfMonth) {
											TextView txtDate = (TextView)findViewById(TXT_VIEW_ID);
						                	String strShowDate = 
						                		String.format("%04d",year) + "/" +
						                		String.format("%02d",monthOfYear + 1) + "/" +
						                		String.format("%02d",dayOfMonth);
						                	String strDataDate =
						                		String.format("%04d",year) +
						                		String.format("%02d",monthOfYear + 1) +
						                		String.format("%02d",dayOfMonth);
						                	
						                	txtDate.setText( strShowDate );
						                	txtDate.setTag( R.string.DATE_DATAVALUE, strDataDate );
						                }
						            },
						            defaultYear, defaultMonth, defaultDay);

							// 日付入力ダイアログを表示する
							datePickerDialog.show();
						}
					}
				);
	
				viewid++;
				layoutTmp.addView(txtDate);
				layoutTmp.addView(btnDateChoice);
				
				tblRow.addView(layoutTmp);
				return tblRow;
			}
			
		}
		// 時間型の項目名リスト
		String[] arrTimeList = ctx.getResources().getStringArray(R.array.ctl_time);
		for( int i=0; i<arrTimeList.length; i++)
		{
			if( clmnTmp.getStrColumnName().equals( arrTimeList[i] ) )
			{
				// 一致したら時間型
				LinearLayout layoutTmp = new LinearLayout(ctx);
				layoutTmp.setTag(STR_PARENT_MARK);
				//layoutTmp.setBackgroundColor(iRowColor);
				
				TextView txtTime = new TextView(ctx);
				txtTime.setLayoutParams(lp_wrap_1);
				//txtTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
				txtTime.setTag(STR_TARGET_MARK);
				txtTime.setId(viewid);
				txtTime.setTag(R.string.DATA_TYPE_KEY, TYPE_TIME);
				if( clmnTmp.getStrColumnName().equals("WorkEndTime")
				|| clmnTmp.getStrColumnName().equals("WorkTime"))
				{
					
					// 稼動開始時間か稼動終了時間の場合
					// 収支入力とみなす
					txtTime.addTextChangedListener(
							new TextWatcher()
							{

								@Override
								public void afterTextChanged(Editable s) {									
									if( blnInitEnd == false ) return;
									ColumnInfo ciTarget = getValue("WorkedInterval");
								
									/*
									if( ciTarget.getExtraVal1() != null
									&&  ciTarget.getExtraVal1().length() > 0
									)
									{
										// すでに入力されている場合は、反映しない
										// TODO:反映ボタンを作るべき？
										return;
									}*/
									ColumnInfo ciStart = getValue("WorkTime");
									ColumnInfo ciEnd = getValue("WorkEndTime");
									
									if( ciStart.getExtraVal1() == null 
									||  ciStart.getExtraVal1().length() != 4 )
									{
										return;
									}
									if( ciEnd.getExtraVal1() == null 
									||  ciEnd.getExtraVal1().length() != 4)
									{
										return;
									}
									String strStartHour = ciStart.getExtraVal1().substring(0,2);
									String strStartTime = ciStart.getExtraVal1().substring(2,4);
									int iStartTime = Integer.parseInt(strStartHour) * 60 + Integer.parseInt(strStartTime);
									
									String strEndHour = ciEnd.getExtraVal1().substring(0,2);
									String strEndTime = ciEnd.getExtraVal1().substring(2,4);
									int iEndTime = Integer.parseInt(strEndHour) * 60 + Integer.parseInt(strEndTime);
									
									int iInterval = iEndTime - iStartTime;
									if( iInterval < 0 )
									{
										return;
									}
									String strIntervalHour = String.format("%02d", iInterval / 60);
									String strIntervalMinute = String.format("%02d", iInterval 
											- Integer.parseInt(strIntervalHour) * 60);
									
									ciTarget.setExtraVal1( strIntervalHour + strIntervalMinute );
									
									setValue( ciTarget );
								}

								@Override
								public void beforeTextChanged(CharSequence s,
										int start, int count, int after) {
									// TODO 自動生成されたメソッド・スタブ
									
								}

								@Override
								public void onTextChanged(CharSequence s,
										int start, int before, int count) {
									// TODO 自動生成されたメソッド・スタブ
									
								}
								
							}
					);
				}
				if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
				)
				{
					// 値が既にある場合それを設定する
					// 日付型の場合、変換をかます
					// このツールでは必ずdatabaseにはHHMMで入っている
					txtTime.setText(
						TableControler.getFmtTime( clmnTmp.getPreviewVal() )
					);
					txtTime.setTag( R.string.TIME_DATAVALUE, clmnTmp.getPreviewVal() );
				}
				else
				{
					//if(clmnTmp.getIntNotNull() == 99 )
					if(clmnTmp.getIntNotNull() != 0 
					//|| clmnTmp.getStrColumnName().equals("WorkEndTime")
					)
					{
						// NOT NULL、かつ前に設定された値がない場合
						String strVal = String.format("%02d",defaultHour) + ":" +
                			String.format("%02d",defaultMinute );
						String strDataVal = String.format("%02d",defaultHour) +
            								String.format("%02d",defaultMinute );
						txtTime.setText( strVal );
						txtTime.setTag( R.string.TIME_DATAVALUE, strDataVal );
					}
				}

				Button btnTimeChoice = new Button(ctx);
				btnTimeChoice.setLayoutParams(lp_wrap);
				if( clmnTmp.getStrColumnName().equals("WorkedInterval") )
				{
					btnTimeChoice.setTag(R.string.BtnCap_Time,1);	
				}
				//btnTimeChoice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
				btnTimeChoice.setText(ctx.getString(R.string.BtnCap_Time));
				btnTimeChoice.setTag(R.string.ASS_VIEW_ID,viewid);
				btnTimeChoice.setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Calendar calendar = Calendar.getInstance();
							defaultHour = calendar.get(Calendar.HOUR_OF_DAY);
							defaultMinute = calendar.get(Calendar.MINUTE);

							boolean is24 = true;
							final int TXT_VIEW_ID = (int)(Integer)v.getTag(R.string.ASS_VIEW_ID);
							if( v.getTag(R.string.BtnCap_Time) != null && (int)(Integer)v.getTag(R.string.BtnCap_Time) == 1 )
							{
								defaultHour = 0;
								defaultMinute = 0;
								TextView txtTime = (TextView)findViewById(TXT_VIEW_ID);
								String txt = (String) txtTime.getText();
								if( txt != null && txt.indexOf(":") != -1 )
								{
									String[] str = txt.split(":");
									defaultHour = Integer.valueOf( str[0] );
									defaultMinute = Integer.valueOf( str[1] );
								}
								//is24 = false;
							}
							TimePickerDialog timePickerDialog = new TimePickerDialog(
						            ctx,
						            new TimePickerDialog.OnTimeSetListener() {

										@Override
										public void onTimeSet(TimePicker view,
												int hourOfDay, int minute) {
											TextView txtTime = (TextView)findViewById(TXT_VIEW_ID);
						                	String strShowTime = 
						                		String.format("%02d",hourOfDay) + ":" +
						                		String.format("%02d",minute );
						                	String strDataTime =
						                		String.format("%02d",hourOfDay) +
						                		String.format("%02d",minute );
						                	
						                	txtTime.setTag( R.string.TIME_DATAVALUE, strDataTime );
						                	txtTime.setText( strShowTime );
											
										}
						            },
						            defaultHour, defaultMinute, is24 );

							// 時間入力ダイアログを表示する
							timePickerDialog.show();
						}
					}
				);
	
				viewid++;
				layoutTmp.addView(txtTime);
				layoutTmp.addView(btnTimeChoice);
				
				tblRow.addView(layoutTmp);
				return tblRow;
			}
			
		}
		
		// 複数行テキストの項目名リスト
		String[] arrMulEdit = ctx.getResources().getStringArray(R.array.ctl_mul_edit);
		for( int i=0; i<arrMulEdit.length; i++)
		{
			if( clmnTmp.getStrColumnName().equals( arrMulEdit[i] ) )
			{
				// 一致したら複数行テキスト
				EditText edtClmnVal = new EditText(ctx);
				//edtClmnVal.setBackgroundColor(iRowColor);
				// edtClmnVal.setSingleLine();
				// ターゲットマークをつける
				edtClmnVal.setTag(STR_TARGET_MARK);
				edtClmnVal.setTag(R.string.DATA_TYPE_KEY, TYPE_MULEDIT);
				if( clmnTmp.getPreviewVal() != null 
						//&& clmnTmp.getPreviewVal().length() > 0
				)
				{
					// 値が既にある場合それを設定する
					edtClmnVal.setText( clmnTmp.getPreviewVal() );
				}
					
				tblRow.addView( edtClmnVal );
				return tblRow;
			}
		}
		// スターコントロールの項目名リスト
		String[] arrStarList = ctx.getResources().getStringArray(R.array.ctl_star );
		for( int i=0; i<arrStarList.length; i++)
		{
			if( clmnTmp.getStrColumnName().equals( arrStarList[i] ) )
			{
				// 一致したらスターコントロール
				LinearLayout layoutTmp = new LinearLayout(ctx);
				//layoutTmp.setLayoutParams(lp_fill_w);
				//layoutTmp.setGravity(Gravity.RIGHT);
				layoutTmp.setTag(STR_PARENT_MARK);
				//layoutTmp.setBackgroundColor(iRowColor);

				TextView txtRate = new TextView(ctx);
				txtRate.setLayoutParams(lp_wrap_1);
				//txtRate.setGravity(Gravity.LEFT);
				txtRate.setTag(STR_TARGET_MARK);
				txtRate.setId(viewid);
				txtRate.setTag(R.string.DATA_TYPE_KEY, TYPE_STAR);
				if( clmnTmp.getPreviewVal() != null 
						//&& clmnTmp.getPreviewVal().length() > 0
				)
				{
					// 値が既にある場合それを設定する
					txtRate.setText( clmnTmp.getPreviewVal() );
				}

				Button btnRateInput = new Button(ctx);
				btnRateInput.setLayoutParams(lp_wrap);
				btnRateInput.setText(ctx.getString(R.string.BtnCap_Rate));
				btnRateInput.setTag(R.string.ASS_VIEW_ID,viewid);
				btnRateInput.setOnClickListener(
					new View.OnClickListener() {						
						@Override
						public void onClick(View v) {
							final int TXT_VIEW_ID = (int)(Integer)v.getTag(R.string.ASS_VIEW_ID);
							final RatingBar ctlRate = new RatingBar(ctx);
							final TextView txtRate = (TextView)findViewById(TXT_VIEW_ID);
							float fRate = 0f;
							if( txtRate.getText().length() > 0 )
							{
								fRate = Float.parseFloat(txtRate.getText().toString());
							}
							LinearLayout l = new LinearLayout(ctx);
							LayoutParams params = new LayoutParams(
									LayoutParams.WRAP_CONTENT, 
									LayoutParams.WRAP_CONTENT);
							ctlRate.setLayoutParams(params);
							ctlRate.setNumStars(RATE_MAX);
							ctlRate.setRating(fRate);
							ctlRate.setStepSize(1f);
							ctlRate.setOnRatingBarChangeListener(
									new RatingBar.OnRatingBarChangeListener() {
										
										@Override
										public void onRatingChanged(RatingBar ratingBar, float rating,
												boolean fromUser) {
											
											if( ctlRate.getRating() == 0f )
											{
												// Rateは最小で1にする
												ctlRate.setRating( 1 );
											}
										}
									}
							);
							l.addView(ctlRate);
							AlertDialog.Builder ad=new AlertDialog.Builder(ctx);
							ad.setView( l );//ctlRate );
							ad.setTitle(ctx.getString(R.string.RateDlg_Title));
							ad.setPositiveButton(ctx.getString(R.string.btnOK),
						            new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
												//TextView txtRate = (TextView)findViewById(TXT_VIEW_ID);
												if( ctlRate.getRating() != 0f ) {
													String strShowRate = Float.toString(ctlRate.getRating());
													txtRate.setText( strShowRate );
												}
										}
							});
							ad.setNegativeButton(ctx.getString(R.string.btnCancel),
						            new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
							});
							ad.setCancelable(true);
							ad.setOnCancelListener(
								new DialogInterface.OnCancelListener() {
									@Override
									public void onCancel(DialogInterface dialog) {
									}
								}
						    );
							ad.show();
						}
					}
				);
				
				viewid++;
				layoutTmp.addView(txtRate);
				layoutTmp.addView(btnRateInput);

				tblRow.addView(layoutTmp);
				return tblRow;
			}
		}
		
		// 更に、マスタに登録されている値をSpinnerで選ぶ項目
		// 現在日時を取得、数ヶ月前以前から登録のない台は、前に行かないようにする。
		Calendar now = Calendar.getInstance();
		Date d = now.getTime();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		String strPeriodConditionWherePhrase = "";
		Boolean blnIgnoreMonthBefore = sp.getBoolean("chk_ignore_month_set_key", false );
		if( blnIgnoreMonthBefore )
		{
			Integer ignoreMonthBefore = 3;
			try
			{
				ignoreMonthBefore = Integer.parseInt(sp.getString( "ignore_month_for_sort", "3" ));
			} catch( ClassCastException e ) {
				ignoreMonthBefore = 3;
			}
			d.setMonth( d.getMonth() - ignoreMonthBefore );
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String strDateBefore3Month = sdf.format(d);
			strPeriodConditionWherePhrase = " where WorkDate >= '" + strDateBefore3Month + "' ";
		}
		// ★台
		// TODO: ビタ打ちできたら直す
		if( clmnTmp.getStrColumnName().equals( "McnId" ) )
		{
			// 一致したら台登録 見た目は台の名称、値は台id
	    	// データベースから台の名称のString配列を取得する
			LinearLayout layoutTmp = new LinearLayout(ctx);
			layoutTmp.setTag(STR_PARENT_MARK);
			//layoutTmp.setBackgroundColor(iRowColor);

			
			// queryでSELECTを実行
	    	//String[] columns = {"_id",ctx.getString(R.string.McnTblMainClmnName)};
	    	//String selection = null;
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	Cursor c = db.rawQuery( 
	    			"select -1 as _id, '未設定' as " + ctx.getString(R.string.McnTblMainClmnName) 
	    			+ ", 0 as sort, 0 as refcnt "
	    			+ " union select t1._id, t1." + ctx.getString(R.string.McnTblMainClmnName)
	    			+ ", 1 as sort, COALESCE(t2.refcnt,0) as refcnt from " + ctx.getString(R.string.McnTblName)
	    			+ " t1 left outer join ( select McnId, -1 * count(1) as refcnt from " 
	    				+ ctx.getString(R.string.MoneyTblName)
	    				// 2015/01/31 追加
	    				+ strPeriodConditionWherePhrase			
	    				+ " group by McnId ) t2 on t1._id = t2.McnId "
	    			+ " order by sort, refcnt, " + ctx.getString(R.string.McnTblMainClmnName)
	    			,null
	    	);
	    	
	    	/*
	    	Cursor c = db.rawQuery( 
	    			"select -1 as _id, '未設定' as " + ctx.getString(R.string.McnTblMainClmnName) 
	    			+ ", 0 as sort union select _id, " + ctx.getString(R.string.McnTblMainClmnName)
	    			+ ", 1 as sort from " + ctx.getString(R.string.McnTblName)
	    			+ " order by sort, " + ctx.getString(R.string.McnTblMainClmnName)
	    			,null
	    	);
	    	*/
	    	
	    	//Cursor c = db.query(ctx.getString(R.string.McnTblName),
	    	//		columns, selection, null, null, null,
	    	//		ctx.getString(R.string.McnTblMainClmnName));
	    	//c.moveToFirst();
	    	//String str = c.getString(1);
			Spinner spnMcn = new Spinner(ctx);
			spnMcn.setLayoutParams(lp_fill_1);//lp_wrap_1);
			//spnMcn.setTag(STR_TARGET_MARK);
			//spnMcn.setId(viewid);
			//spnMcn.setTag(R.string.DATA_TYPE_KEY, TYPE_EDIT);
			if( c.getCount() > 0 )
			{
		    	String[] from = {ctx.getString(R.string.McnTblMainClmnName)};
		    	int[] to = {android.R.id.text1};
		    	SimpleCursorAdapter adapter = 
		    		new SimpleCursorAdapter(ctx,android.R.layout.simple_spinner_item,c,from,to);
		    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spnMcn.setAdapter(adapter);
				//spnMcn.setSelection( 0 );
				if( c.getCount() > 1 )
				{
					spnMcn.setSelection( 1 );
				}
				else
				{
					spnMcn.setSelection( 0 );	
				}
			}
			//else
			//{
			//	String[] arrEmp = {""};
			//	ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, arrEmp );
		    //	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			//	spnMcn.setAdapter(adapter);				
			//}
			//Cursor c2 = (Cursor)spnMcn.getItemAtPosition(0);
			//String strTest = c2.getString(c2.getColumnIndex(ctx.getString(R.string.McnTblMainClmnName)));
			// spnMcn.setSelection(0);
			//spnMcn.setLayoutParams(
			//		new LayoutParams(
			//			LinearLayout.LayoutParams.FILL_PARENT, 
			//			LinearLayout.LayoutParams.FILL_PARENT) );
			spnMcn.setTag(STR_TARGET_MARK);
			spnMcn.setTag( R.string.DATA_TYPE_KEY, TYPE_MCN_ID );

			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				c.moveToFirst();
				for( int m=0; m<c.getCount(); m++ )
				{
					if( c.getInt(0) == Integer.parseInt( clmnTmp.getPreviewVal() ) )
					{
						spnMcn.setSelection( m );
					}
					c.moveToNext();
				}
			}

			//c.close();
			Button btnChoice = new Button(ctx);
			btnChoice.setLayoutParams(lp_wrap);
			btnChoice.setText(ctx.getString(R.string.BtnCap_Mcn));
			btnChoice.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
		    			// 台管理インテントを呼び出す
		    			// Intentの呼び出し
		    			((RecordUpdateActivity)ctx).startMachineIntentForResult();
		    			//ctx.startActivity( intent );
					}
				}
			);
			layoutTmp.addView(spnMcn);
			layoutTmp.addView(btnChoice);
			
			tblRow.addView(layoutTmp);

			//tblRow.addView(spnMcn);
			return tblRow;
		}
		// ★店
		// TODO: ビタ打ちできたら直す
		if( clmnTmp.getStrColumnName().equals( "ParlorId" ) )
		{
			// 一致したら台登録 見た目は台の名称、値は台id
	    	// データベースから台の名称のString配列を取得する

			LinearLayout layoutTmp = new LinearLayout(ctx);
			layoutTmp.setTag(STR_PARENT_MARK);
			//layoutTmp.setBackgroundColor(iRowColor);
			
	    	// queryでSELECTを実行
	    	//String[] columns = {"_id",ctx.getString(R.string.ParlorTblMainClmnName)};
	    	//String selection = null;
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	
	    	Cursor c = db.rawQuery(
	    			"select -1 as _id, '未設定' as " + ctx.getString(R.string.ParlorTblMainClmnName) 
	    			+ ", 0 as sort, 0 as refcnt union select _id, " + ctx.getString(R.string.ParlorTblMainClmnName)
	    			+ ", 1 as sort, COALESCE(t2.refcnt,0) as refcnt from " + ctx.getString(R.string.ParlorTblName)
	    			+ " t1 left outer join ( select ParlorId, -1 * count(1) as refcnt from " 
	    				+ ctx.getString(R.string.MoneyTblName)
	    				// 2015/01/31 追加
	    				+ strPeriodConditionWherePhrase
	    				+ " group by ParlorId ) t2 on t1._id = t2.ParlorId "
	    			+ " order by sort, refcnt, " + ctx.getString(R.string.ParlorTblMainClmnName)
	    			,null	    			
	    			/*
	    			"select -1 as _id, '未設定' as " + ctx.getString(R.string.ParlorTblMainClmnName) 
	    			+ ", 0 as sort union select _id, " + ctx.getString(R.string.ParlorTblMainClmnName)
	    			+ ", 1 as sort from " + ctx.getString(R.string.ParlorTblName)
	    			+ " order by sort, " + ctx.getString(R.string.ParlorTblMainClmnName)
	    			,null*/
	    			);
	    	
	    	//Cursor c = db.query(ctx.getString(R.string.ParlorTblName),
	    	//		columns, selection, null, null, null, ctx.getString(R.string.ParlorTblMainClmnName));
	    	//c.moveToFirst();
	    	//String str = c.getString(1);
			Spinner spnMcn = new Spinner(ctx);
			spnMcn.setLayoutParams(lp_wrap_1);
			spnMcn.setHorizontalScrollBarEnabled(true);

			if( c.getCount() > 0 )
			{
		    	String[] from = {ctx.getString(R.string.ParlorTblMainClmnName)};
		    	int[] to = {android.R.id.text1};
		    	SimpleCursorAdapter adapter = 
		    		new SimpleCursorAdapter(ctx,android.R.layout.simple_spinner_item,c,from,to);
		    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    	
		    	//adapter.
				spnMcn.setAdapter(adapter);
				
				if( c.getCount() > 1 )
				{
					spnMcn.setSelection( 1 );
				}
				else
				{
					spnMcn.setSelection( 0 );	
				}				
			}
			//else
			//{
				//String[] arrEmp = {""};
				//ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, arrEmp );
		    	//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//spnMcn.setAdapter(adapter);				
			//}
			//Cursor c2 = (Cursor)spnMcn.getItemAtPosition(0);
			//String strTest = c2.getString(c2.getColumnIndex(ctx.getString(R.string.McnTblMainClmnName)));
			//spnMcn.setLayoutParams(
			//		new LayoutParams(
			//			LinearLayout.LayoutParams.FILL_PARENT, 
			//			LinearLayout.LayoutParams.FILL_PARENT) );
			spnMcn.setTag(STR_TARGET_MARK);
			spnMcn.setTag( R.string.DATA_TYPE_KEY, TYPE_PRL_ID );

			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				c.moveToFirst();
				for( int m=0; m<c.getCount(); m++ )
				{
					if( c.getInt(0) == Integer.parseInt( clmnTmp.getPreviewVal() ) )
					{
						spnMcn.setSelection( m );
					}
					c.moveToNext();
				}
			}
			//c.close();
			Button btnChoice = new Button(ctx);
			btnChoice.setLayoutParams(lp_wrap);
			btnChoice.setText(ctx.getString(R.string.BtnCap_Parlor));
			btnChoice.setOnClickListener(
				new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
		    			// 店管理インテントを呼び出す
		    			// Intentの呼び出し
						((RecordUpdateActivity)ctx).startParlorIntentForResult();
		    			//ctx.startActivity( intent );						
					}
				}
			);
			layoutTmp.addView(spnMcn);
			layoutTmp.addView(btnChoice);
			
			tblRow.addView(layoutTmp);

			//tblRow.addView(spnMcn);
			return tblRow;
		}
		// ★メーカー名
		// TODO: ビタ打ちできたら直す
		if( clmnTmp.getStrColumnName().equals( "MakerId" ) )
		{
			// 一致したら台登録 見た目は台の名称、値は台id
	    	// データベースから台の名称のString配列を取得する
			LinearLayout layoutTmp = new LinearLayout(ctx);
			layoutTmp.setTag(STR_PARENT_MARK);

			SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	
	    	Cursor c = db.rawQuery( 
	    			"select -1 as _id, '" + ctx.getString(R.string.not_want_set) + "' as " + ctx.getString(R.string.MakerTblMainClmnName) 
	    			+ ", 0 as sort union select _id, " + ctx.getString(R.string.MakerTblMainClmnName)
	    			+ ", 1 as sort from " + ctx.getString(R.string.MakerTblName)
	    			+ " order by sort, " + ctx.getString(R.string.MakerTblMainClmnName)
	    			,null
	    			);
			Spinner spnMcn = new Spinner(ctx);
			spnMcn.setLayoutParams(lp_fill_1);
			if( c.getCount() > 0 )
			{
		    	String[] from = {ctx.getString(R.string.MakerTblMainClmnName)};
		    	int[] to = {android.R.id.text1};
		    	SimpleCursorAdapter adapter = 
		    		new SimpleCursorAdapter(ctx,android.R.layout.simple_spinner_item,c,from,to);
		    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    	spnMcn.setPrompt("台の" + ctx.getString(R.string.caption_maker) + "を選択");
				spnMcn.setAdapter(adapter);
				//spnMcn.setSelection( 0 );
			}

			spnMcn.setTag(STR_TARGET_MARK);
			spnMcn.setTag( R.string.DATA_TYPE_KEY, TYPE_MAKER_ID );

			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				c.moveToFirst();
				for( int m=0; m<c.getCount(); m++ )
				{
					if( c.getInt(0) == Integer.parseInt( clmnTmp.getPreviewVal() ) )
					{
						spnMcn.setSelection( m );
					}
					c.moveToNext();
				}
			}		
		
			//c.close();
			Button btnChoice = new Button(ctx);
			btnChoice.setLayoutParams(lp_wrap);
			btnChoice.setText(ctx.getString(R.string.BtnCap_Maker));
			btnChoice.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
		    			// メーカー管理インテントを呼び出す
		    			// Intentの呼び出し
		    			((RecordUpdateActivity)ctx).startMakerIntentForResult();
					}
				}
			);
			layoutTmp.addView(spnMcn);
			layoutTmp.addView(btnChoice);
			
			tblRow.addView(layoutTmp);

			//tblRow.addView(spnMcn);
			return tblRow;
		}		
		// ★種別
		// TODO: ビタ打ちできたら直す
		if( clmnTmp.getStrColumnName().equals( "PatiTypeId" ) )
		{
			// 一致したら台登録 見た目は台の名称、値は台id
	    	// データベースから台の名称のString配列を取得する

	    	// queryでSELECTを実行
	    	//String[] columns = {"_id",ctx.getString(R.string.ParlorTblMainClmnName)};
	    	//String selection = null;
	    	//SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	//Cursor c = db.query(ctx.getString(R.string.ParlorTblName),
	    	//		columns, selection, null, null, null, ctx.getString(R.string.ParlorTblMainClmnName));
	    	//c.moveToFirst();
	    	//String str = c.getString(1);
			Spinner spn = new Spinner(ctx);
			//if( c.getCount() > 0 )
			//{
		    //	String[] from = {ctx.getString(R.string.ParlorTblMainClmnName)};
		    //	int[] to = {android.R.id.text1};
		    //	SimpleCursorAdapter adapter = 
		    //		new SimpleCursorAdapter(ctx,android.R.layout.simple_spinner_item,c,from,to);
		    //	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			//	spn.setAdapter(adapter);
			//}
			//else
			//{
				String[] arrEmp = {"スロット","パチンコ"};
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, arrEmp );
		    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spn.setAdapter(adapter);
			//}
			//Cursor c2 = (Cursor)spnMcn.getItemAtPosition(0);
			//String strTest = c2.getString(c2.getColumnIndex(ctx.getString(R.string.McnTblMainClmnName)));
			//spnMcn.setLayoutParams(
			//		new LayoutParams(
			//			LinearLayout.LayoutParams.FILL_PARENT, 
			//			LinearLayout.LayoutParams.FILL_PARENT) );
			spn.setTag(STR_TARGET_MARK);
			spn.setTag( R.string.DATA_TYPE_KEY, TYPE_PATITYPE_ID );
			//spn.setBackgroundColor(iRowColor);
			
			spn.setSelection( 0 );
			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				spn.setSelection( Integer.parseInt( clmnTmp.getPreviewVal() ) );
			}
			//c.close();

			tblRow.addView(spn);
			return tblRow;
		}
		// 2012/02/16試し
		// 台名にオートコンプリート機能をつける
		if( clmnTmp.getStrColumnName().equals( "MachineName" ) )
		{		
			AutoCompleteTextView edtautoClmnVal = new AutoCompleteTextView(ctx);
			edtautoClmnVal.setSingleLine();
			// ターゲットマークをつける
			edtautoClmnVal.setTag(STR_TARGET_MARK);
			edtautoClmnVal.setTag(R.string.DATA_TYPE_KEY, TYPE_EDIT);
			//edtClmnVal.setBackgroundColor(iRowColor);
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	
	    	Cursor cursor = db.rawQuery(
	    			"select " + ctx.getString(R.string.McnTblMainClmnName)
	    			+ " from " + ctx.getString(R.string.McnTblName)
	    			+ " order by " + ctx.getString(R.string.McnTblMainClmnName)
	    			,null
	    			);
	    	if( cursor.getCount() > 0 )
	    	{
		    	ArrayList<String> arrayList = new ArrayList<String>();
		    	cursor.moveToFirst();
		    	while(!cursor.isAfterLast()) {
		    	     arrayList.add(cursor.getString(cursor.getColumnIndex(ctx.getString(R.string.McnTblMainClmnName))));
		    	     cursor.moveToNext();
		    	}
		    	String[] arrNames = new String[cursor.getCount()];
		    	arrayList.toArray(arrNames);
		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, arrNames);//simple_list_item_1, arrNames);
		    	edtautoClmnVal.setAdapter( adapter );
		    	//edtautoClmnVal.setDropDownBackgroundResource(android.R.color.primary_text_dark);
	    	}
			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				edtautoClmnVal.setText( clmnTmp.getPreviewVal() );
			}
			
			tblRow.addView( edtautoClmnVal );
			return tblRow;
		}		
		// 店名にオートコンプリート機能をつける
		if( clmnTmp.getStrColumnName().equals( "ParlorName" ) )
		{		
			AutoCompleteTextView edtautoClmnVal = new AutoCompleteTextView(ctx);
			edtautoClmnVal.setSingleLine();
			// ターゲットマークをつける
			edtautoClmnVal.setTag(STR_TARGET_MARK);
			edtautoClmnVal.setTag(R.string.DATA_TYPE_KEY, TYPE_EDIT);
			//edtClmnVal.setBackgroundColor(iRowColor);
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	
	    	Cursor cursor = db.rawQuery(
	    			"select " + ctx.getString(R.string.ParlorTblMainClmnName)
	    			+ " from " + ctx.getString(R.string.ParlorTblName)
	    			+ " order by " + ctx.getString(R.string.ParlorTblMainClmnName)
	    			,null
	    			);
	    	if( cursor.getCount() > 0 )
	    	{
		    	ArrayList<String> arrayList = new ArrayList<String>();
		    	cursor.moveToFirst();
		    	while(!cursor.isAfterLast()) {
		    	     arrayList.add(cursor.getString(cursor.getColumnIndex(ctx.getString(R.string.ParlorTblMainClmnName))));
		    	     cursor.moveToNext();
		    	}
		    	String[] arrNames = new String[cursor.getCount()];
		    	arrayList.toArray(arrNames);
		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, arrNames);//simple_list_item_1, arrNames);
		    	edtautoClmnVal.setAdapter( adapter );
		    	//edtautoClmnVal.setDropDownBackgroundResource(android.R.color.primary_text_dark);
	    	}
			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				edtautoClmnVal.setText( clmnTmp.getPreviewVal() );
			}
			
			tblRow.addView( edtautoClmnVal );
			return tblRow;
		}
		// メーカー名にオートコンプリート機能をつける
		if( clmnTmp.getStrColumnName().equals( "MakerName" ) )
		{		
			AutoCompleteTextView edtautoClmnVal = new AutoCompleteTextView(ctx);
			edtautoClmnVal.setSingleLine();
			// ターゲットマークをつける
			edtautoClmnVal.setTag(STR_TARGET_MARK);
			edtautoClmnVal.setTag(R.string.DATA_TYPE_KEY, TYPE_EDIT);
			//edtClmnVal.setBackgroundColor(iRowColor);
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	
	    	Cursor cursor = db.rawQuery(
	    			"select " + ctx.getString(R.string.MakerTblMainClmnName)
	    			+ " from " + ctx.getString(R.string.MakerTblName)
	    			+ " order by " + ctx.getString(R.string.MakerTblMainClmnName)
	    			,null
	    			);
	    	if( cursor.getCount() > 0 )
	    	{
		    	ArrayList<String> arrayList = new ArrayList<String>();
		    	cursor.moveToFirst();
		    	while(!cursor.isAfterLast()) {
		    	     arrayList.add(cursor.getString(cursor.getColumnIndex(ctx.getString(R.string.MakerTblMainClmnName))));
		    	     cursor.moveToNext();
		    	}
		    	String[] arrNames = new String[cursor.getCount()];
		    	arrayList.toArray(arrNames);
		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, arrNames);//simple_list_item_1, arrNames);
		    	edtautoClmnVal.setAdapter( adapter );
		    	//edtautoClmnVal.setDropDownBackgroundResource(android.R.color.primary_text_dark);
	    	}
			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				edtautoClmnVal.setText( clmnTmp.getPreviewVal() );
			}
			
			tblRow.addView( edtautoClmnVal );
			return tblRow;
		}
		// ここまで来たら、デフォルトの設定
		if( clmnTmp.getiColumnType() == ColumnInfo.COLUMN_TYPE_TEXT 
			|| clmnTmp.getiColumnType() == ColumnInfo.COLUMN_TYPE_NONE )
		{		
			EditText edtClmnVal = new EditText(ctx);
			edtClmnVal.setSingleLine();
			// ターゲットマークをつける
			edtClmnVal.setTag(STR_TARGET_MARK);
			edtClmnVal.setTag(R.string.DATA_TYPE_KEY, TYPE_EDIT);
			//edtClmnVal.setBackgroundColor(iRowColor);
			
			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				edtClmnVal.setText( clmnTmp.getPreviewVal() );
			}
			
			tblRow.addView( edtClmnVal );
			return tblRow;
		}
		else if( clmnTmp.getiColumnType() == ColumnInfo.COLUMN_TYPE_INTEGER
				|| clmnTmp.getiColumnType() == ColumnInfo.COLUMN_TYPE_REAL)
		{
			// TODO:数値項目は未完
			EditText edtClmnVal = new EditText(ctx);
			edtClmnVal.setSingleLine();
			// ターゲットマークをつける
			edtClmnVal.setTag(STR_TARGET_MARK);
			edtClmnVal.setTag(R.string.DATA_TYPE_KEY, TYPE_NUM);
			//edtClmnVal.setBackgroundColor(iRowColor);

			// 数値しか入力できないようにする
			edtClmnVal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

			if( clmnTmp.getStrColumnName().equals("Invest")
			|| clmnTmp.getStrColumnName().equals("Recovery"))
			{
				
				// 投資か回収の場合
				// 収支入力とみなす
				edtClmnVal.addTextChangedListener(
						new TextWatcher()
						{
							@Override
							public void afterTextChanged(Editable s) {									
								if( blnInitEnd == false ) return;
								ColumnInfo ciTarget = getValue("CashFlow");
							
								/*
								if( ciTarget.getExtraVal1() != null
								&&  ciTarget.getExtraVal1().length() > 0
								)
								{
									// すでに入力されている場合は、反映しない
									// TODO:反映ボタンを作るべき？
									return;
								}*/
								
								ColumnInfo ciStart = getValue("Invest");
								ColumnInfo ciEnd = getValue("Recovery");
								
								if( ciStart.getExtraVal1() == null 
								||  ciStart.getExtraVal1().length() < 1 )
								{
									return;
								}
								if( ciEnd.getExtraVal1() == null 
								||  ciEnd.getExtraVal1().length() < 1)
								{
									return;
								}
								int iInv, iRecv;
								try
								{
									iInv = Integer.parseInt(ciStart.getExtraVal1());
								} catch( NumberFormatException e ) {
									iInv = 0;
								}
								try
								{
									iRecv = Integer.parseInt(ciEnd.getExtraVal1());
								} catch( NumberFormatException e ) {
									iRecv = 0;
								}
								
								int iInterval = iRecv - iInv;

								ciTarget.setExtraVal1( String.format("%d",iInterval ));
								
								setValue( ciTarget );
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
								// TODO 自動生成されたメソッド・スタブ
								
							}

							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								// TODO 自動生成されたメソッド・スタブ
								
							}
							
						}
				);
			}
			if( clmnTmp.getPreviewVal() != null 
					//&& clmnTmp.getPreviewVal().length() > 0
			)
			{
				// 値が既にある場合それを設定する
				edtClmnVal.setText( clmnTmp.getPreviewVal() );
			}

			
			tblRow.addView( edtClmnVal );
			return tblRow;			
		}
		
		return null;
	}
	
	/*
	 * 
	 * 
	 */
	public String getColumnNameForShow( String strClmnNm )
	{
		String strClmnNmShow = strClmnNm;
		// カラム名は、リソースを元に日本語等に変換して表示する
		String[] item_bef;
		String[] item_aft;
    	item_bef = getResources().getStringArray(R.array.clmn_title);
    	item_aft = getResources().getStringArray(R.array.clmn_title_show);
    	for( int i=0; i < item_bef.length; i++ ) {
    		if( item_bef[i].equals( strClmnNm ) ) {
    			strClmnNmShow = item_aft[i];
    			break;
    		}
    	}
    	return strClmnNmShow;
	}
	public Context getCtx() {
		return ctx;
	}
	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	public String getStrTblName() {
		return strTblName;
	}
	public void setStrTblName(String strTblName) {
		this.strTblName = strTblName;
	}
	public String getStrExcptColumnSpec() {
		return strExcptColumnSpec;
	}
	public void setStrExcptColumnSpec(String strColumnSpec) {
		this.strExcptColumnSpec = strColumnSpec;
	}
	public DBHelper getDbHelper() {
		return dbHelper;
	}
	public void setDbHelper(DBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	public void setWherePhrase(String strWherePhrase) {
		this.strWherePhrase = strWherePhrase;
	}
	public String getWherePhrase() {
		return strWherePhrase;
	}
}
