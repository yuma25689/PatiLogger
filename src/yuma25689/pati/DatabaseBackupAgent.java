package yuma25689.pati;

import android.annotation.SuppressLint;
import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.util.Log;

/**
 * @author 25689
 *
 */
@SuppressLint("NewApi")
public class DatabaseBackupAgent extends BackupAgentHelper {
	static final String DATABASE_FILENAME = PatiManDBHelper.dbName;
	static final String DATABASE_BACKUP_KEY = "key_database_backup";
	static final Object LOCK = new Object();
	static final String TAG = "DatabaseBackupAgent";
	@Override
	public void onCreate() {
		Log.v(TAG, "DatabaseBackupAgent onCreate");
		FileBackupHelper helper = new DBFileBackupHelper(this, DATABASE_FILENAME);
		addHelper(DATABASE_BACKUP_KEY, helper);
	}
}
