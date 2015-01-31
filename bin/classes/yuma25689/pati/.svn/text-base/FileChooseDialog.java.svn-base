package yuma25689.pati;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

// AlertDialogラッバー？なぜ今更そんなものを作る・・・！
public class FileChooseDialog {

	DialogInterface.OnClickListener OKlsn = null;
	String strDir = null;
	String FileList[] = null;
	int iFileCnt = 0;
	Context ctx = null;
	AlertDialog dlg = null;
	
	protected FileChooseDialog(
			Context context,
			DialogInterface.OnClickListener listener,
			String _strDir
	)
	{
		ctx = context;
		OKlsn = listener;
		strDir = _strDir;
	}
	public class DataComparator implements java.util.Comparator<String>
	{
		public int compare(String o1, String o2){
			return (o2).compareTo(o1);
		}
	}	
	public String[] getFileList()
	{
		FileList = null;

		ArrayList<String> arrFileLst
			= new ArrayList<String>();
		File f = new File(strDir);
	    File files[] = f.listFiles();
	    if( files == null )
	    {
	    	return FileList;
	    }
	    for (int i = 0; i < files.length; i++) 
	    {
	    	if( files[i].getName().endsWith( ".xml" ))
	    	{
	    		arrFileLst.add( files[i].getName() );
	    	}
	    }
	    if( arrFileLst.size() > 0 )
	    {
	    	FileList = new String[arrFileLst.size()];
	    	arrFileLst.toArray(FileList);
	    	Arrays.sort(FileList, new DataComparator());
	    }
	    return FileList;
	}
	
	public String show(
		String title,
		String msg
	)
	{
		new AlertDialog.Builder(ctx)
			.setTitle( title )
			//.setMessage( msg )
			.setItems(FileList, OKlsn )
			.show();

		return null;
	}
	
	
}
