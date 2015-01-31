package yuma25689.pati;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper implements Serializable {
	private static final long serialVersionUID = -342658128485203126L;

	private TableOpenInfo tblInf[];
	public TableOpenInfo[] getTblInf() {
		return tblInf;
	}

	public void setTblInf(TableOpenInfo[] tblInf) {
		this.tblInf = tblInf;
	}

	private final String TMP_PREFIX = "temp_";
	private final String NEW_PREFIX = "new_";
	/*
	private String strTblName = "";
	private String strColumnsCreateText = "";
	@SuppressWarnings("unused")
	private String strDBName = "";
	@SuppressWarnings("unused")
	private SQLiteDatabase.CursorFactory factory = null;
	@SuppressWarnings("unused")
	private int iDBVer = 1;
	*/
	/**
	 * コンストラクタ
	 *
	 * @param context
	 * @param strTblName_
	 * @param strColumnsCreateText_
	 * @param factory_
	 * @param strDBName_
	 * @param iDBVer_
	 * @return なし
	 */
	public DBHelper(Context context, String dbName, int dbVer) {
		super(context,dbName,null,dbVer);
		// tblInf = tblInf_;
	}

	/**
	 * 最初に実行される
	 *
	 * @param db
	 * @return なし
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		for( int i=0; i < tblInf.length; i++ )
		{
			createTable( db, tblInf[i].getTblName(), tblInf[i].getColumnsCreateText() );
		}
	}

	private void createTable(SQLiteDatabase db,
			String strTblName_,
			String strColumnsCreateText_
			) {
		if ( !strTblName_.equals("") 
				&& !strColumnsCreateText_.equals("")) {
			try {
				db.execSQL(
						"create table if not exists " +
						strTblName_ + strColumnsCreateText_ //"(id text primary key,info text)");
						);
			} catch ( Exception e ) {
				e.printStackTrace();				
			}
		}
	}
	private void dropTable(SQLiteDatabase db, String strTblName_ ) {
		for( int i=0; i < tblInf.length; i++ )
		{
			if ( !tblInf[i].getTblName().equals("") ) {
				db.execSQL(
						"drop table if exists " + strTblName_ 
						);
			}
		}
	}
	
	/**
	 * テーブルのヴァージョンが上がったときに実行される
	 * 旧から新へデータの引き継ぎを行う
	 *
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 * @return なし
	 */	
	@Override
	public void onUpgrade(SQLiteDatabase db,
			int oldVersion, 
			int newVersion) {
		
		db.beginTransaction();
		try {
			for( int i=0; i < tblInf.length; i++ )
			{
				/*
				// 元のテーブルをリネームして退避
				db.execSQL("ALTER TABLE " + tblInf[i].getTblName() + " RENAME TO " + TMP_PREFIX + tblInf[i].getTblName());
				// 元のテーブルのカラムを取得
				List<String> lstOldColumnList = getColumns( db, TMP_PREFIX + tblInf[i].getTblName() );
				// 新しいテーブルを作成
				createTable( db, tblInf[i].getTblName(), tblInf[i].getColumnsCreateText() );
				// 新しいテーブルのカラムを取得
				List<String> lstNewColumnList = getColumns(db, tblInf[i].getTblName());
		*/
				// 元のテーブルのカラムを取得
				List<String> lstOldColumnList = getColumns( db, tblInf[i].getTblName() );
				if( lstOldColumnList == null || lstOldColumnList.size() == 0 )
				{
					// 新規テーブルと思われる
				}
				else
				{
					// 元のテーブルをリネームして退避
					db.execSQL("ALTER TABLE " + tblInf[i].getTblName() + " RENAME TO " + TMP_PREFIX + tblInf[i].getTblName());
				}
								
				// 新しいテーブルを作成
				createTable( db, NEW_PREFIX + tblInf[i].getTblName(), tblInf[i].getColumnsCreateText() );

				// 新しいテーブルのカラムを取得
				List<String> lstNewColumnList = getColumns(db, NEW_PREFIX + tblInf[i].getTblName());
				
				db.execSQL("ALTER TABLE " + NEW_PREFIX + tblInf[i].getTblName() + " RENAME TO " + tblInf[i].getTblName());
				
				if( lstOldColumnList == null || lstOldColumnList.size() == 0 )
					// 新規テーブルと思われる
					continue;
				
				// 新旧で名称の変化しないカラムのみ抽出
				lstOldColumnList.retainAll(lstNewColumnList);
				
				// 共通データを移す。(OLDにしか存在しないものは捨てられ, NEWにしか存在しないものはNULLになる)
				String cols = join(lstOldColumnList, ",");
				String cols2 = cols;
				
				if( "MachineMan".equals( tblInf[i].getTblName() ) )
				{
					if( -1 == cols.indexOf("MakerId"))
					{
						cols += ",MakerId";
						cols2 += ",-1";
					}
				}
				
				cols2 = cols2.replace("WorkEndTime", "coalesce(WorkEndTime,'0000')");
				//cols2 = cols2.replace("WorkStartTime,", "");
				cols2 = cols2.replace("WorkTime", "coalesce(WorkTime,'0000')");
				db.execSQL(
					String.format(
						"INSERT INTO %s (%s) SELECT %s from " + TMP_PREFIX + "%s", 
						tblInf[i].getTblName(),	cols, cols2, tblInf[i].getTblName()
					)
				);
				// 退避した旧テーブルを削除 
				dropTable( db, TMP_PREFIX + tblInf[i].getTblName() );
			}
			// コミット
			db.setTransactionSuccessful();
		} catch(Exception ex ){
			ex.printStackTrace();
		}finally {
			// トランザクション終了
			db.endTransaction();
		}
	}
	
	/**
	 * 指定したテーブルのカラム名リストを取得する。
	 *
	 * @param db
	 * @param tableName
	 * @return カラム名のリスト
	 */
	private List<String> getColumns(
			SQLiteDatabase db,
			String tableName) {
		List<String> ar = null;
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null);
			if (c != null) {
				ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
			}
		} catch(Exception ex) {
			if (c != null)
				c.close();			
		} finally {
			if (c != null)
				c.close();
		}
		return ar;
	}
	
	/**
	 * 文字列を任意の区切り文字で連結する。
	 *
	 * @param list
	 * 文字列のリスト
	 * @param delim
	 * 区切り文字
	 * @return 連結後の文字列
	 */
	private static String join(List<String> list, String delim) {
		final StringBuilder buf = new StringBuilder();
		final int num = list.size();
		for (int i = 0; i < num; i++) {
			if (i != 0)
				buf.append(delim);
			buf.append((String) list.get(i));
		}
		return buf.toString();
	}
}
