package yuma25689.pati;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewWinRateActivity extends Activity 
	implements View.OnClickListener {

	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	Button btnBack = null;
	Button btnSrch = null;
	private DBHelper dbHelper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_win_rate);
		setTitle(R.string.DlgTitle_ViewWinRate);
        commLogic.init(this);
		
		dbHelper = new PatiManDBHelper(this);
		
		// スピナーの設定
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
		// ---- 期間
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(getString(R.string.all_period));
        
    	String[] columns_ = {"_id","SUBSTR(WorkDate,1,6) AS WorkMonth"};
    	String selection_ = null;
    	String groupBy = "SUBSTR(WorkDate,1,6)";
    	String orderBy = "SUBSTR(WorkDate,1,6) desc";
    	Cursor c_ = db.query(getString(R.string.MoneyTblName),
    			columns_, selection_, null, groupBy, null, orderBy);
    	c_.moveToFirst();
    	for (int i = 0; i < c_.getCount(); i++) {
    		adapter.add( TableControler.getFmtMonth( c_.getString(1) ) );
    	    c_.moveToNext();
    	}
    	c_.close();
        Spinner spinner = (Spinner) findViewById(R.id.spn_period_winrate);
        spinner.setAdapter(adapter);

		// ----　グループ 
        ArrayAdapter<String> adapterGrp = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item);
        adapterGrp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterGrp.add("台別");
        adapterGrp.add("日別");
        adapterGrp.add("月別");
            	
        spinner = (Spinner) findViewById(R.id.spn_group_winrate);
        spinner.setAdapter(adapterGrp);
        
        // ---- 台
		// queryでSELECTを実行
    	String[] columns = {"_id",this.getString(R.string.McnTblMainClmnName)};
    	String selection = null;
        ArrayAdapter<String> adapterMcn = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item);
        adapterMcn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMcn.add(getString(R.string.all_machine));
    	Cursor c = db.query(this.getString(R.string.McnTblName),
    			columns, selection, null, null, null, this.getString(R.string.McnTblMainClmnName));
    	spinner = (Spinner) findViewById(R.id.spn_machine_winrate);
    	c.moveToFirst();
    	for (int i = 0; i < c.getCount(); i++) {
    		adapterMcn.add( c.getString(1) );
    	    c.moveToNext();
    	}
    	c.close();
		spinner.setAdapter(adapterMcn);
        
        // ---- 店
    	String[] columnsPrl = {"_id",this.getString(R.string.ParlorTblMainClmnName)};
    	String selectionPrl = null;
        ArrayAdapter<String> adapterPrl = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item);
        adapterPrl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPrl.add(getString(R.string.all_parlor));
        
    	Cursor cPrl = db.query(this.getString(R.string.ParlorTblName),
    			columnsPrl, selectionPrl, null, null, null, this.getString(R.string.ParlorTblMainClmnName));
    	spinner = (Spinner) findViewById(R.id.spn_parlor_winrate);
    	try
    	{
    		cPrl.moveToFirst();
	    	for (int i = 0; i < cPrl.getCount(); i++) 
	    	{
	    		adapterPrl.add( cPrl.getString(1) );
	    		cPrl.moveToNext();
	    	}
	    	cPrl.close();
	    	spinner.setAdapter(adapterPrl);
		}
    	catch( Exception ex )
    	{
    		ex.printStackTrace();
    	}
    	
		LinearLayout btm = (LinearLayout)this.findViewById(R.id.winrate_bottomBar);
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
		
	}
	private void finishIntent()
	{
		finish();
	}
	
	private String getSrchCondition()
	{
		String strRet = "";
		
		// ---- 期間
		Spinner spn = (Spinner) findViewById(R.id.spn_period_winrate);
		String strSeletedPeriod = (String)spn.getSelectedItem();
        // ---- 台
		spn = (Spinner) findViewById(R.id.spn_machine_winrate);
		String strSeletedMachine = (String)spn.getSelectedItem();
        // ---- 店
		spn = (Spinner) findViewById(R.id.spn_parlor_winrate);
		String strSeletedParlor = (String)spn.getSelectedItem();
		
		if( false == strSeletedPeriod.equals(getString(R.string.all_period)) )
		{
			strRet = "SUBSTR(WorkDate, 1, 6 ) = '" + strSeletedPeriod.replace("/", "") + "'";			
		}
		if( false == strSeletedMachine.equals(getString(R.string.all_machine)) )
		{
			if( strRet.length() > 0 )
			{
				strRet += " and ";
			}
			strRet += "McnId=" + TableControler.getMcnIdFromMcnName(this, dbHelper, strSeletedMachine);
		}
		if( false == strSeletedParlor.equals(getString(R.string.all_parlor)) )
		{
			if( strRet.length() > 0 )
			{
				strRet += " and ";
			}
			strRet += "ParlorId=" + TableControler.getParlorIdFromParlorName(this, dbHelper, strSeletedParlor);
		}
		
		
		return strRet;
	}
	private String getSrchGroup()
	{
		String strRet = "";
		
		// ----　グループ 
		Spinner spn = (Spinner) findViewById(R.id.spn_group_winrate);
		String strSeletedGrp = (String)spn.getSelectedItem();
		
		if( false == strSeletedGrp.equals("台別") )
		{
			if( strSeletedGrp.equals("日別") )
			{
				strRet="WorkDate";
			}
			else if( strSeletedGrp.equals("月別") )
			{
				strRet="SUBSTR(WorkDate,1,6)";
			}
		}
		
		return strRet;
	}
	
	
	@Override
	public void onClick(View v) {
		if( v==btnBack ) {
			// 戻る
			finishIntent();
		}
		else if( v==btnSrch )
		{
			// 検索
	    	TextView lblWinRate = (TextView) findViewById(R.id.lblWinRate);
	    	TextView txtWinRate = (TextView) findViewById(R.id.txtWinRate);
	    	TextView lblWinCount = (TextView) findViewById(R.id.lblWinCount);
	    	TextView txtWinCount = (TextView) findViewById(R.id.txtWinCount);
	    	TextView txtMaxMin = (TextView) findViewById(R.id.txtMaxMinPerPeriod);
	    	lblWinRate.setText("");
	    	txtWinRate.setText("");
	    	lblWinCount.setText("");
	    	txtWinCount.setText("");
			String strBasicCond = getSrchCondition();
			String strSrchGroup = getSrchGroup();
			try
			{
				// 全件数の取得
				int iAllCnt = 0;
				int iCash = 0;
				SQLiteDatabase db = dbHelper.getReadableDatabase();
		    	String selection = strBasicCond;
		    	String having = "";
		    	String[] columns = {"count(_id), sum(CashFlow)"};
		        Cursor c = db.query(this.getString(R.string.MoneyTblName),
		    			columns, selection, null, strSrchGroup, null, null );
		    	c.moveToFirst();
		    	for (int i = 0; i < c.getCount(); i++)
		    	{
		    		if( strSrchGroup.length() > 0 )
		    		{
		    			iAllCnt ++;
		    		}
		    		else
		    		{
		    			iAllCnt += c.getInt(0);
		    		}
			    	iCash += c.getInt(1);
		    		c.moveToNext();
		    	}
		    	c.close();
				
		    	// 全件数が0なら、no hit
		    	if( iAllCnt == 0 )
		    	{
		    		lblWinRate.setText("該当の検索条件に一致するデータがありませんでした。");
		    	}
		    	
		    	// 勝ち件数の取得
		    	int iWinCnt = 0;
		    	selection = strBasicCond;
		    	having = "";
		    	if( strSrchGroup.length() > 0 )
		    	{
		    		having = "SUM(CashFlow) > 0";
		    	}
		    	else
		    	{
			    	if( strBasicCond != null && strBasicCond.length() > 0 )
			    	{
			    		selection += " and ";
			    	}
		    		selection += "CashFlow > 0";
		    	}
		        c = db.query(this.getString(R.string.MoneyTblName),
		    			columns, selection, null, strSrchGroup, having, null );
		    	c.moveToFirst();
		    	if( strSrchGroup.length() > 0 )
		    	{
		    		iWinCnt = c.getCount();
		    	}
		    	else
		    	{
		    		iWinCnt = c.getInt(0);
		    	}
		    	c.close();
	
		    	// 引き分け件数の取得
		    	int iDrawCnt = 0;
		    	selection = strBasicCond;
		    	having = "";
		    	if( strSrchGroup.length() > 0 )
		    	{
		    		having = "SUM(CashFlow) = 0";
		    	}
		    	else
		    	{
			    	if( strBasicCond != null && strBasicCond.length() > 0 )
			    	{
			    		selection += " and ";
			    	}
		    		selection += "CashFlow = 0";
		    	}
		        c = db.query(this.getString(R.string.MoneyTblName),
		    			columns, selection, null, strSrchGroup, having, null );
		    	c.moveToFirst();
		    	if( strSrchGroup.length() > 0 )
		    	{
		    		iDrawCnt = c.getCount();
		    	}
		    	else
		    	{
		    		iDrawCnt = c.getInt(0);
		    	}		    	
		    	c.close();
		    	
		    	// 負け件数の取得
		    	int iLoseCnt = 0;
		    	selection = strBasicCond;
		    	having = "";
		    	if( strSrchGroup.length() > 0 )
		    	{
		    		having = "SUM(CashFlow) < 0";
		    	}
		    	else
		    	{
			    	if( strBasicCond != null && strBasicCond.length() > 0 )
			    	{
			    		selection += " and ";
			    	}
		    		selection += "CashFlow < 0";
		    	}
		        c = db.query(this.getString(R.string.MoneyTblName),
		    			columns, selection, null, strSrchGroup, having, null );
		    	c.moveToFirst();
		    	if( strSrchGroup.length() > 0 )
		    	{
		    		iLoseCnt = c.getCount();
		    	}
		    	else
		    	{
		    		iLoseCnt = c.getInt(0);
		    	}		    	
		    	c.close();

		    	// 最高勝ち額、最高負け額の取得
		    	int iMaxWin = 0;
		    	int iMaxLose = 0;
		    	selection = strBasicCond;
		    	if( strSrchGroup.length() > 0 )
		    	{
			    	String[] columns2 = {"sum(CashFlow), sum(CashFlow)"};
			        c = db.query(this.getString(R.string.MoneyTblName),
			    			columns2, selection, null, strSrchGroup, null, null );		    		
		    	}
		    	else
		    	{
			    	String[] columns2 = {"max(CashFlow), min(CashFlow)"};
			        c = db.query(this.getString(R.string.MoneyTblName),
			    			columns2, selection, null, strSrchGroup, null, null );		    		
		    	}
		    	c.moveToFirst();
		    	if( strSrchGroup.length() > 0 )
		    	{
			    	for (int i = 0; i < c.getCount(); i++) 
			    	{
			    		if( iMaxWin     < c.getInt(0)  ) iMaxWin = c.getInt(0);
			    		if( c.getInt(1) < iMaxLose ) iMaxLose = c.getInt(1);
			    		c.moveToNext();
			    	}		    		
		    	}
		    	else
		    	{
			    	iMaxWin = c.getInt(0);
			    	iMaxLose = c.getInt(1);
		    	}		    	
		    	c.close();
		    	if( iMaxWin < 0 ) iMaxWin = 0;
		    	if( iMaxLose > 0 ) iMaxLose = 0;
		    	
		    	// 表示
		    	// 勝率の計算
		    	double dblWinRate = (double)iWinCnt / (double)iAllCnt;
		    	txtWinRate.setText( Double.toString( Math.round( dblWinRate * 100) ) + " %");
		    	lblWinRate.setText("勝率：");
		    	lblWinCount.setText("勝敗：");
		    	String strWinLose = Integer.toString( iWinCnt ) + " 勝" + Integer.toString( iLoseCnt ) + " 敗";
		    	if( iDrawCnt != 0 )
		    	{
		    		strWinLose += Integer.toString( iDrawCnt ) + " 分";
		    	}
		    	// ここではないかもしれないけど、とりあえずここに収支も出す
		    	strWinLose += "(総収支:" + Integer.toString( iCash ) + ")"; 
		    	txtWinCount.setText( strWinLose );
		    	txtMaxMin.setText( "最高勝ち額：" + Integer.toString( iMaxWin ) + " 最高負け額：" + Integer.toString( iMaxLose ));
			} catch( Exception ex ) {
				ex.printStackTrace();
				
			}
	    	
		}
	}
	
}
