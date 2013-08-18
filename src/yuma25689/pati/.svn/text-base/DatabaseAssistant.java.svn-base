package yuma25689.pati;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.util.Log;
//import android.util.Log;

/**
 * テーブルをXMLファイルにexport/XMLファイルからimportする
 * @author yuma25689
 *
 */
public class DatabaseAssistant
{
	private final String[] tblNames
		= { "MakerMan", "MachineMan", "MachineManDtl", "ParlorMan", "MoneyMan" }; 

	private Context _ctx = null;
	private SQLiteDatabase _db = null;
	private Handler _hdr = null;
	private Exporter _exporter = null;
	private Message _msg = null;
	//private Thread _thd = null;

	int iImportMachineRowCnt;
	int iImportParlorRowCnt;
	int iImportMoneyRowCnt;
	int iImportMakerRowCnt;
	public int getImportMachineRowCnt() {
		return iImportMachineRowCnt;
	}
	public int getImportMoneyRowCnt() {
		return iImportMoneyRowCnt;
	}
	public int getImportParlorRowCnt() {
		return iImportParlorRowCnt;
	}
	public int getImportMakerRowCnt() {
		return iImportMakerRowCnt;
	}	
	
	private static final String[][] ESC_CHRS = {
		{"&","&amp;"},
		{"'","&apos;"},
		{"<","&lt;"},
		{">","&gt;"},
		{"\"","&quot;"}
	};
	
	private static final String CLOSING_WITH_TICK = "'>";
	private static final String START_DB = "<export-database name='";
	private static final String END_DB = "</export-database>";
	private static final String START_TABLE = "<table name='";
	private static final String END_TABLE = "</table>";
	private static final String START_ROW = "<row>";
	private static final String END_ROW = "</row>";
	private static final String START_COL = "<col name='";
	private static final String END_COL = "</col>";

	private static final String DB_TAG="export-database";
	private static final String TABLE_TAG="table";
	private static final String ROW_TAG="row";
	private static final String COL_TAG="col";
	private static final String ATTR_NAME="name";
	private static final String NAME_TBLMCN="MachineMan";
	private static final String NAME_TBLPARLOR="ParlorMan";
	private static final String NAME_TBLMONEY="MoneyMan";
	private static final String NAME_TBLMAKER="MakerMan";
	
	private Hashtable<String,String> htImportMachines;
	private Hashtable<String,String> htImportParlors;
	private Hashtable<String,String> htImportMakers;
	/*
	private static final String NAME_CATE="category";
	private static final String NAME_NOTES="notes";
	private static final String NAME_DATETIME="datetime";
	private static final String NAME_TITLE="title";
	private static final String NAME_MEMO="memo";
	private static final String NAME_DETAIL="detail";	
	*/
	public DatabaseAssistant( Context ctx, SQLiteDatabase db, Handler hdr, Thread thd )
	{
		_ctx = ctx;
		_db = db;
		_hdr = hdr;
	}

	private int getAllRecordCount()
	{
		int intCnt = 0;
		
        for( String tableName:tblNames )
        {
			String sql = "select count(1) from " + tableName;
			Cursor cur = _db.rawQuery(sql, null);
			cur.moveToFirst();
			intCnt += cur.getInt(0);		        
        }
		return intCnt;
	}
	//private int iProgressCnt = 0;
	private int iAllRecCnt = 0;
	
	public Boolean exportData(String dir, String file)
	{
		try
		{
			File objDir = new File( dir );
			if( false == objDir.exists() )
			{
				if( false == objDir.mkdirs() )
				{
					if( _hdr != null )
					{
						_msg = new Message();
						String[] strArr = {
							_ctx.getString( R.string.TITLE_ERROR ),
							_ctx.getString( R.string.MSG_FAILED_DIR_CREATE )
						};
						Bundle bdl = new Bundle();
						bdl.putStringArray( PatiLogger.ERROR_MSG_KEY, strArr);
						_msg.setData(bdl);
						_msg.what = PatiLogger.ERROR_MSG_ID;
						_hdr.sendMessage(_msg);
					}
					return false;
				}
			}
			// create a file on the SDcard to export the
			// database contents to
			File myFile = new File( dir + "/" + file );
                        myFile.createNewFile();

                        FileOutputStream fOut =  new FileOutputStream(myFile);
                        BufferedOutputStream bos = new BufferedOutputStream( fOut );

			_exporter = new Exporter( bos );
		
			// プログレスバー用の処理
			if( _hdr != null )
			{
				iAllRecCnt = getAllRecordCount();
				Bundle bdl = new Bundle();
				bdl.putInt(
					PatiLogger.PROGRESS_VAL_KEY,
					iAllRecCnt
				);
				_msg = new Message();
				_msg.setData( bdl );
				_msg.what = PatiLogger.PROGRESS_MAX_MSG_ID;
				_hdr.sendMessage( _msg );
			}
			_exporter.startDbExport( _db.getPath() );
	        for( String tableName:tblNames )
	        {
		        exportTable( tableName );
			}
	        _exporter.endDbExport();
			_exporter.close();
		}
		catch ( Exception e)
		{
			if( _hdr != null )
			{
				_msg = new Message();
				String[] strArr = {
					_ctx.getString( R.string.TITLE_ERROR ),
					e.getMessage()
				};
				Bundle bdl = new Bundle();
				bdl.putStringArray( PatiLogger.ERROR_MSG_KEY, strArr);
				_msg.setData(bdl);
				_msg.what = PatiLogger.ERROR_MSG_ID;
				_hdr.sendMessage(_msg);
			}
			return false;
		}			
		return true;
	}

	/**
	 * ↓形式
	 * <export-database name=''>
	 * <table name=''>
	 * <row>
	 * <col name=''>value</col>
	 * <col name=''>value</col>
	 * ...
	 * </row>
	 * </table>
	 * </export-database>
	 * @param tableName
	 * @throws IOException
	 */
	private void exportTable( String tableName ) throws IOException
	{
        _exporter.startTable(tableName);

		// get everything from the table
		String sql = "select * from " + tableName;
		Cursor cur = _db.rawQuery( sql, new String[0] );
		int numcols = cur.getColumnCount();
		
		cur.moveToFirst();

		// move through the table, creating rows
		// and adding each column with name and value
		// to the row
		while( cur.getPosition() < cur.getCount() )
		{
			_exporter.startRow();
			String name;
			String val;

			for( int idx = 0; idx < numcols; idx++ )
			{
				name = cur.getColumnName(idx);
				val = cur.getString( idx );
				
				_exporter.addColumn( name, val );
			}

			_exporter.endRow();
			cur.moveToNext();
			if( _hdr != null )
			{
				_msg = new Message();
				_msg.what = PatiLogger.PROGRESS_VAL_INCL_MSG_ID;
				_hdr.sendMessage( _msg );
				//iProgressCnt++;
			}
		}

		cur.close();

		_exporter.endTable();
	}

	class Exporter
	{
		private BufferedOutputStream _bos;

		/*
		public Exporter() throws FileNotFoundException
		{
			this( new BufferedOutputStream(
					_ctx.openFileOutput( 
						EXPORT_FILE_NAME,
						Context.MODE_WORLD_READABLE ) ) );
		}*/

		public Exporter( BufferedOutputStream bos )
		{
			_bos = bos;
		}

		public void close() throws IOException
		{
			if ( _bos != null )
			{
				_bos.close();
			}
		}

		public void startDbExport( String dbName ) throws IOException
		{
			String stg = START_DB + dbName + CLOSING_WITH_TICK;
			_bos.write( stg.getBytes() );
		}

		public void endDbExport() throws IOException
		{
			_bos.write( END_DB.getBytes() );
		}

		public void startTable( String tableName ) throws IOException
		{
			String stg = START_TABLE + tableName + CLOSING_WITH_TICK;
			_bos.write( stg.getBytes() );
		}

		public void endTable() throws IOException
		{
			_bos.write( END_TABLE.getBytes() );
		}

		public void startRow() throws IOException
		{
			_bos.write( START_ROW.getBytes() );
		}

		public void endRow() throws IOException
		{
			_bos.write( END_ROW.getBytes() );
		}

		public void addColumn( String name, String val ) throws IOException
		{
			if( val != null )
			{
				//エスケープ文字のチェックを行う。あれば置換する。
				for( int i=0; i < ESC_CHRS.length; i++ )
				{
					if( val.indexOf( ESC_CHRS[i][0] ) != -1 )
					{
						val = val.replace( ESC_CHRS[i][0], ESC_CHRS[i][1] );
					}
				}
			}
			String stg = START_COL + name + CLOSING_WITH_TICK + val + END_COL;
			_bos.write( stg.getBytes() );
		}
	}
	// インポートのモード
	public static final int IMPORT_MODE_NONE = 1;
	public static final int IMPORT_MODE_ALL_CLEAR = 1;
	public static final int IMPORT_MODE_ALL_REWRITE = 2;
	public static final int IMPORT_MODE_EXIST_ERR = 3;
	public static final int IMPORT_MODE_EXIST_IGNORE = 4;

	public Boolean importData(
		String strFilePath,
		int importMode,
		DBHelper dbHlpr
	)
	{
		Importer imp = new Importer( strFilePath, importMode, dbHlpr );
		return imp.importData();
	}	
	
	class Importer
	{
		private String _path;
		private DBHelper _dbHlpr;
		int iImpMode;
		ArrayList<DupInf> arrDupInf;
		class DupInf
		{
			String strTbl;
			MoneyInf dupEntryMoney;
			MachineInf dupEntryMachine;
			ParlorInf dupEntryParlor;
			
			DupInf( String tbl, MachineInf dupEntC )
			{
				strTbl = tbl;
				dupEntryMachine = dupEntC; 
			}
			DupInf( String tbl, MoneyInf dupEntM )
			{
				strTbl = tbl;
				dupEntryMoney = dupEntM; 
			}
			DupInf( String tbl, ParlorInf dupEntT )
			{
				strTbl = tbl;
				dupEntryParlor = dupEntT;				
			}
			public MachineInf getDupEntryCate() {
				return dupEntryMachine;
			}
			public MoneyInf getDupEntryMemo() {
				return dupEntryMoney;
			}
			public ParlorInf getDupEntryTime() {
				return dupEntryParlor;
			}
			public String getStrTbl() {
				return strTbl;
			}
		}
		
		// SAX APIによる実装
		public Importer( String strFilePath, int importMode, DBHelper dbHlpr )
		{
			_path = strFilePath;
			iImpMode = importMode;
			_dbHlpr = dbHlpr;
		}
		
		public Boolean importData()
		{
			Boolean bRet = true;
			String strErr = null;
			iImportMachineRowCnt = 0;
			iImportParlorRowCnt = 0;
			iImportMoneyRowCnt = 0;
			iImportMakerRowCnt = 0;
			htImportMachines = new Hashtable<String,String>();
			htImportParlors = new Hashtable<String,String>();
			htImportMakers = new Hashtable<String,String>();
			
			arrDupInf = new ArrayList<DupInf>();
			arrDupInf.clear();
			try {
				_db.beginTransaction();
				
				if( iImpMode == IMPORT_MODE_ALL_CLEAR )
				{
					// テーブルをすべて削除する
					for(  String tableName:tblNames )
					{
						_db.delete( tableName, null, null );
					}
				}

				// パスからURIを取得
				String uri = new File( _path ).toURL().toExternalForm();
				
				// SAXでそのURIのXMLを解析する
				SAXParserFactory spf = SAXParserFactory.newInstance();
	            SAXParser sp = spf.newSAXParser(); 				
	            XMLReader xmlReader = sp.getXMLReader();
	            xmlReader.setContentHandler( new ImportSaxHandler() );
				xmlReader.parse( uri );

			} catch (SAXException e) {
				//Log.w("sax_error", e.getMessage());
				strErr = e.getMessage();
				bRet = false;
			} catch (IOException e) {
				//Log.w("sax_parse_error", e.getMessage());
				strErr = e.getMessage();
				bRet = false;
			} catch (ParserConfigurationException e) {
				//Log.w("sax_parser_creation_error", e.getMessage());
				strErr = e.getMessage();
				bRet = false;
			} catch (Exception e) {
				strErr = e.toString() + e.getCause() + e.getMessage();
				bRet = false;				
			}
			
		
			if( bRet == false )
			{
				if( _hdr != null )
				{
					_msg = new Message();
					String[] strArr = {
						_ctx.getString( R.string.TITLE_ERROR ),
						strErr
					};
					Bundle bdl = new Bundle();
					bdl.putStringArray( PatiLogger.ERROR_MSG_KEY, strArr);
					_msg.setData(bdl);
					_msg.what = PatiLogger.ERROR_MSG_ID;
					_hdr.sendMessage(_msg);
				}
			}

			_db.endTransaction();
			
			return bRet;
		}	

		private class ImportSaxHandler extends DefaultHandler {
			
			Stack<String> stackName;
			Hashtable<String,String> dicCurRowValues = new Hashtable<String,String>();
			StringBuffer sbCurrentVal;
			String strErr;
			int lastElmTagId = ID_NONE;
			int lastElmNameId = ID_NAME_UNKNOWN;
			int lastTblId = ID_NAME_UNKNOWN;
			
			private static final int ID_NONE = 0;
			private static final int ID_DB = 1;
			private static final int ID_ROW = 2;
			private static final int ID_TABLE = 3;
			private static final int ID_COL = 4;
			private static final int ID_NAME_UNKNOWN = 10;
			private static final int ID_TABLE_MCNINF = 11;
			private static final int ID_TABLE_PARLORINF = 12;
			private static final int ID_TABLE_MONEYINF = 13;
			private static final int ID_TABLE_MAKERINF = 14;
			//private static final int ID_TABLE_MCNINFDTL = 14;

			ImportSaxHandler()
			{}
			
			private int chkName( String strName )
			{
				/*
				if( strName.equals( NAME_CATE ) )
				{
					return ID_COL_CATEGORY;
				}
				else if( strName.equals( NAME_DATETIME ) )
				{
					return ID_COL_DATETIME;
				}
				else if( strName.equals( NAME_NOTES ) )
				{
					return ID_COL_NOTES;
				}
				else if( strName.equals( NAME_MEMO ) )
				{
					return ID_COL_MEMO;
				}
				else if( strName.equals( NAME_TITLE ) )
				{
					return ID_COL_TITLE;
				}
				else if( strName.equals( NAME_DETAIL ) )
				{
					return ID_COL_DETAIL;
				}
				else */ 
				if( strName.equals( NAME_TBLMCN ) )
				{
					return ID_TABLE_MCNINF;
				}
				else if( strName.equals( NAME_TBLPARLOR ) )
				{
					return ID_TABLE_PARLORINF;
				}
				else if( strName.equals( NAME_TBLMONEY ) )
				{
					return ID_TABLE_MONEYINF;
				}
				else if( strName.equals( NAME_TBLMAKER ) )
				{
					return ID_TABLE_MAKERINF;
				}				

				return ID_NAME_UNKNOWN;
			}
			
			private int chkLocalName( String strLocal )
			{
				if( strLocal.equals( COL_TAG ) )
				{
					return ID_COL;
				} 
				else if( strLocal.equals( ROW_TAG ) )
				{
					return ID_ROW;
				}
				else if( strLocal.equals( TABLE_TAG ) )
				{
					return ID_TABLE;
				}
				else if( strLocal.equals( DB_TAG ) )
				{
					return ID_DB;
				}
				
				return ID_NONE;
			}	
			
			/**
			 * XMLの開始
			 */
			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
				sbCurrentVal = new StringBuffer();
				stackName = new Stack<String>();
			}
			/**
			 * Elementの開始
			 */
			@Override
			public void startElement(
					String uri,
					String localName,
					String qName,
					Attributes attributes) throws SAXException
			{
				//strCurrentElm = localName;
				// 現在値のクリア
				String strCurrentName = "";
				// Elementが入れ子になっていることを考えると、↓おそらく、ここでクリアしてはいけない
				//dicCurRowValues = new Hashtable<String,String>();
				for(int i=0; i < attributes.getLength(); i++){
					
			        if( ATTR_NAME == attributes.getLocalName(i) )
			        {
			        	// 属性名がnameだった場合
			        	stackName.push(
		        			attributes.getValue(i)
			        	);
			        	// 現在の名前として関数内で保持する
			        	strCurrentName
			        		= attributes.getValue(i);
			        }
			    }
				// タグ名のチェック
				lastElmTagId = chkLocalName( localName );
				switch( lastElmTagId )
				{
				case ID_COL:
				case ID_TABLE:
					// 名前から、どういう属性のタグか判別する
					lastElmNameId = chkName( strCurrentName );
					switch( lastElmNameId )
					{
						case ID_TABLE_MCNINF:							
						case ID_TABLE_PARLORINF:
						case ID_TABLE_MONEYINF:
						case ID_TABLE_MAKERINF:
							// テーブル名だった場合、現在のテーブルIDとして保持する
							lastTblId = lastElmNameId;
							// strCurrentTbl = strCurrentName;
							break;
							/*
						case ID_COL_CATEGORY:
						case ID_COL_TITLE:
						case ID_COL_NOTES:
						case ID_COL_MEMO:
						case ID_COL_DETAIL:
						case ID_COL_DATETIME:*/
						default:
							break;
					}					
					break;
				case ID_ROW:
					// 行の開始で、現在の行の値をクリアする
					dicCurRowValues.clear();
					break;
				case ID_NONE:
				case ID_DB:
					break;
				}				
				super.startElement(uri, localName, qName, attributes);
			}
			@Override
			public void characters(char[] ch, int start, int length)
					throws SAXException
			{
				// XMLインポート時に、いらないタグの項目が改行のみ等で入ってきてしまうのに対応した苦肉の策
				// これで、ユーザはスペースのみの項目を作成できない
				// TODO:入力の方もスペースのみが入力できないように制御すること
				String str = new String( ch, start, length );
				if( str.trim().length() > 0 )
				{
					sbCurrentVal.append( ch, start, length );
				}
				super.characters(ch, start, length);
			}
			/**
			 * Elementの終了イベント
			 */
			@Override
			public void endElement(String uri,
					String localName,
					String qName
			) throws SAXException
			{
				int iRet = 0;
				String strCurrentName = "";
				// タグ名のチェック
				switch( chkLocalName( localName ) )
				{
				case ID_COL:
				case ID_TABLE:
					// カラムかテーブルだった場合(カラムとテーブルはname属性もち)
					// Element開始時に保持してあるはずの名称を取得
					strCurrentName = stackName.pop();
					switch( chkName( strCurrentName ) )
					{
						// 名前からElementの種別を判断
						case ID_TABLE_MCNINF:							
						case ID_TABLE_PARLORINF:
						case ID_TABLE_MONEYINF:
						case ID_TABLE_MAKERINF:
							// テーブル名
							// 特に保持するものなし
							break;
						default:
							// カラム
							String strVal = sbCurrentVal.toString();
							if( "null".equals(strVal) )
							{
								strVal = "";
							}
							dicCurRowValues.put( strCurrentName, strVal );
							break;
					}
					break;
				case ID_ROW:
					// 行だった場合
					// ここでDBに吐く
					IInserter objInsert = null;
					TableControler objTblCtrl = null;
					String strJpTblName = null;
					String strTblName = null;
					switch( lastTblId )
					{
					case ID_TABLE_MCNINF:
						strTblName = "MachineMan";
						objInsert = new MachineManInserter();
						strJpTblName = "台情報";
						break;
					case ID_TABLE_PARLORINF:
						strTblName = "ParlorMan";
						objInsert = new ParlorManInserter();
						strJpTblName = "店情報";
						break;
					case ID_TABLE_MAKERINF:
						strTblName = "MakerMan";
						objInsert = new MakerManInserter();
						strJpTblName = "メーカー情報";
						break;
					case ID_TABLE_MONEYINF:
						strTblName = "MoneyMan";
						objInsert = new MoneyManInserter();
						strJpTblName = "収支情報";
						break;
						/*
						 * 今のところ使用しないし、使用できない
						 * やるならば、特殊なインポート方法が必要
					case ID_TABLE_MCNINFDTL:
						strTblName = "MachineManDetail";
						objInsert = new MachineManDtlInserter();
						strJpTblName = "台情報詳細";
						break;*/
					}
					// いずれでもなかった場合、insertは行わないので、ここでbreak
					if( strTblName == null ) break;
					objTblCtrl = new TableControler(strTblName, _dbHlpr ); //new PatiManDBHelper(_ctx) );
					ContentValues values = new ContentValues();
					ArrayList<ColumnInfo> arrClmnInf = objTblCtrl.getTableInfo(null);
					for( ColumnInfo clmn:arrClmnInf)
					{
						// id列は、採番させるのでここには含めないこと
						// →方式の変更:一時的に必要になるので、一度保持してあとで消す
						//if( false == clmn.getStrColumnName().equals("_id"))
						//{							
							String strVal = null;
							if( dicCurRowValues.get(clmn.getStrColumnName()) != null )
							{
								// 現在のテーブルのカラムがインポートするデータに存在した場合
								if( dicCurRowValues.get(clmn.getStrColumnName()).length() > 0 )
								{
									// 値がある場合のみ、nullでない値を設定
									strVal = dicCurRowValues.get(clmn.getStrColumnName());
								}
								
								// 登録値として設定する
								values.put( clmn.getStrColumnName(), strVal );
							}
						//}
					}					
					
					// 外部キーのIDで登録されている項目の場合
					// インポート時に、IDと中身がずれてしまう可能性があるので、無理やり合わせる
					// 以下は、そのための処理
					if( lastTblId == ID_TABLE_MAKERINF )
					{
						// メーカー管理へレコードを挿入する場合
						// インポートされるIDと名前の組み合わせを保持
						htImportMakers.put( values.getAsString("_id"),
								values.getAsString("MakerName"));						
					}					
					else if( lastTblId == ID_TABLE_MCNINF )
					{
						// 台管理へレコードを挿入する場合
						// インポートされる台のIDと名前の組み合わせを保持
						htImportMachines.put( values.getAsString("_id"), values.getAsString("MachineName"));

						// インポートされるメーカーのIDからメーカーの名前を取得
						String strOldId = null;
						String strNewId = null;
						String strName = null;						
						strOldId = values.getAsString("MakerId");
						if( false == "-1".equals( strOldId ) && null != strOldId )
						{
							strName = htImportMakers.get(strOldId);
							// 名前から、IDを取得
							strNewId = TableControler.getMakerIdFromMakerName(
									_ctx, _dbHlpr, strName );
							// 新しいIDで古いIDを置換する
							values.remove("MakerId");
							values.put("MakerId", strNewId);
						}						
					}
					else if( lastTblId == ID_TABLE_PARLORINF )
					{
						// 店管理へレコードを挿入する場合
						// インポートされる店のIDと名前の組み合わせを保持						
						htImportParlors.put( values.getAsString("_id"), values.getAsString("ParlorName"));						
					}
					else if( lastTblId == ID_TABLE_MONEYINF )
					{
						// 収支管理へレコードを挿入する場合
						String strOldId = null;
						String strNewId = null;
						String strName = null;
						
						// インポートされる台のIDから台の名前を取得
						strOldId = values.getAsString("McnId");
						if( false == "-1".equals( strOldId ) )
						{
							strName = htImportMachines.get(strOldId);
							// 台の名前から、新しい台のIDを取得
							strNewId = TableControler.getMcnIdFromMcnName(
									_ctx, _dbHlpr, strName );
							// 新しいIDで古いIDを置換する
							values.remove("McnId");
							values.put("McnId", strNewId);
						}
						
						// インポートされる店のIDから台の名前を取得
						strOldId = values.getAsString("ParlorId");
						if( false == "-1".equals( strOldId ) )
						{
							strName = htImportParlors.get(strOldId);
							// 台の名前から、新しい店のIDを取得
							strNewId = TableControler.getParlorIdFromParlorName(
									_ctx, _dbHlpr, strName );
							// 新しいIDで古いIDを置換する
							values.remove("ParlorId");
							values.put("ParlorId", strNewId);
						}
					}
					
					// id列はもう必要ないので削除する
					values.remove("_id");
					
					switch( iImpMode )
					{
					case IMPORT_MODE_EXIST_ERR:
					case IMPORT_MODE_EXIST_IGNORE:
					case IMPORT_MODE_ALL_CLEAR:
						iRet = objInsert.insert(
							_db,
							values
						);
						if( iRet != 0 )
						{
							if( iRet == R.string.MSG_DUP_ERR )
							{
								if( iImpMode == IMPORT_MODE_EXIST_ERR )
								{
									strErr = _ctx.getString(R.string.ERR_INS_DUP);
									strErr = String.format( strErr, strJpTblName );
								}
								else
								{
									/*
									arrDupInf.add( new DupInf(
											strTblName,
											new MachineInf( 
												strCurrentCtgry, strCurrentNotes)) 
									);*/
									break;
								}
							}
							else
							{
								strErr = _ctx.getString(R.string.ERR_INS);
								strErr = String.format( strErr, strJpTblName );
							}
							throw new SAXParseException(strErr, null); 
						}
						switch( lastTblId )
						{
						case ID_TABLE_MCNINF:
							iImportMachineRowCnt++;
							break;
						case ID_TABLE_PARLORINF:
							iImportParlorRowCnt++;
							break;
						case ID_TABLE_MAKERINF:
							iImportMakerRowCnt++;
							break;
						case ID_TABLE_MONEYINF:
							iImportMoneyRowCnt++;
							break;
						}
						break;
					case IMPORT_MODE_ALL_REWRITE:
						// とりあえずこれは来ないことに・・・！
						break;
					}
					break;
				case ID_DB:
					strCurrentName = stackName.pop();
				case ID_NONE:
					break;
				}				
				sbCurrentVal.setLength(0); // これで消えるらしい java超基本しか分からん
				super.endElement(uri, localName, qName);
			}	
			@Override
			public void endDocument() throws SAXException
			{
				if( false == stackName.empty() )
				{
					// 開始に対する終了タグが処理されていない
					// XMLの構造が合っていないはず
					strErr = _ctx.getString(R.string.ERR_XML_INVALIDATE);
					throw new SAXParseException(strErr, null);
				}
				// ここでのみcommitを行う
				_db.setTransactionSuccessful();
				super.endDocument();
			}
		}		
	}
}

