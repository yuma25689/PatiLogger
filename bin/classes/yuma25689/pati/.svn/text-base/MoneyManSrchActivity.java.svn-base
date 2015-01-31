package yuma25689.pati;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
//import android.widget.TextView;

public class MoneyManSrchActivity extends Activity 
	implements View.OnClickListener {
	
	private ActivityCommonLogic commLogic = ActivityCommonLogicFactory.create();//new ActivityCommonLogic();
	
	Button btnBack = null;
	Button btnSrch = null;
	private DBHelper dbHelper = null;
	private int iRequestResult = 0;
	String strTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moneyman_srch_view);
		
		dbHelper = new PatiManDBHelper(this);

        strTitle = getString(R.string.DlgTitle_MoneySrch);
        commLogic.init(this);

        Bundle extras = getIntent().getExtras();
        if( extras != null )
        {
	        // 結果を返却することを求めて起動されたかどうかを表す
	        iRequestResult = extras.getInt(getString(R.string.key_request_result));
        }
        setTitle( strTitle );

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
        Spinner spinner = (Spinner) findViewById(R.id.spn_period_money);
        spinner.setAdapter(adapter);
        
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
    	spinner = (Spinner) findViewById(R.id.spn_machine_money);
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
    	spinner = (Spinner) findViewById(R.id.spn_parlor_money);
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
    	
		LinearLayout btm = (LinearLayout)this.findViewById(R.id.money_bottomBar);
		btnBack = new Button(this);
		btnBack.setText(getString(R.string.btnBack));
		btnBack.setOnClickListener(this);
		btm.addView(btnBack);
		btnSrch = new Button(this);
		btnSrch.setText(getString(R.string.btnSrch));
		btnSrch.setOnClickListener(this);
		btm.addView(btnSrch);
		
	}
	/*
	private String getSrchCondition()
	{
		String strRet = "";
		
		// ---- 期間
		Spinner spn = (Spinner) findViewById(R.id.spn_period_money);
		String strSeletedPeriod = (String)spn.getSelectedItem();
        // ---- 台
		spn = (Spinner) findViewById(R.id.spn_machine_money);
		String strSeletedMachine = (String)spn.getSelectedItem();
        // ---- 店
		spn = (Spinner) findViewById(R.id.spn_parlor_money);
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
	}*/
	@Override
	public void onClick(View v) {
		if( v==btnBack ) {
			// 戻る
			finish();
		}
		else if( v==btnSrch )
		{
			// 検索
			if( iRequestResult == 1 )
			{
				// 返すデータ(Intent&Bundle)の作成
				Intent data = new Intent();
				Bundle bundle = new Bundle();

				// ---- 期間
				Spinner spn = (Spinner) findViewById(R.id.spn_period_money);
				String strSeletedPeriod = (String)spn.getSelectedItem();
		        // ---- 台
				spn = (Spinner) findViewById(R.id.spn_machine_money);
				String strSeletedMachine = (String)spn.getSelectedItem();
		        // ---- 店
				spn = (Spinner) findViewById(R.id.spn_parlor_money);
				String strSeletedParlor = (String)spn.getSelectedItem();

				if( false == strSeletedPeriod.equals(getString(R.string.all_period)) )
				{
					strSeletedPeriod = strSeletedPeriod.replace("/", "");			
					bundle.putString(getString(R.string.key_money_monthfrom), strSeletedPeriod);
					bundle.putString(getString(R.string.key_money_monthto), strSeletedPeriod);
				}
				if( false == strSeletedMachine.equals(getString(R.string.all_machine)) )
				{
					bundle.putString(getString(R.string.key_money_machinename), strSeletedMachine);
				}
				if( false == strSeletedParlor.equals(getString(R.string.all_parlor)) )
				{
					bundle.putString(getString(R.string.key_money_parlorname), strSeletedParlor);
				}
				data.putExtras(bundle);

				setResult(RESULT_OK, data);

				finish();
			}
			
		}
	}	
	
}
