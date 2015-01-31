package yuma25689.pati;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.content.ContextWrapper;
//import android.util.Log;
import android.util.Log;

@SuppressLint("NewApi")
public class DBFileBackupHelper extends FileBackupHelper {

	protected static final String TAG = "DBFileBackupHelper";

	@SuppressLint("NewApi")
	public DBFileBackupHelper(Context context, final String file) {
		// super(context, files);
	    super(
	    	// コンテキストをカスタマイズする	
	    	new ContextWrapper(context) {
	        @Override
	        public File getFilesDir() {
	    		Log.v(TAG, "DBFileBackupHelper_context_getFilesDir called");	        	
	            return super.getDatabasePath(file)
	                        .getParentFile();
	        }
	    }, file);		
	}
}
