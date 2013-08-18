package yuma25689.pati;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
//import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class MainPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        //String[] entries = {"test","test2"};
	        //String[] entVals = {"1","2"};
	        ArrayList<String> appList = new ArrayList<String>();
	        ArrayList<String> appPkgName = new ArrayList<String>();
	        
	        addPreferencesFromResource(R.xml.pref);
	        
	        // qrコードアプリの設定
	        ListPreference lp = (ListPreference) findPreference( "lst_qr_launch_key" );
	        
	        // インストールされているアプリの一覧を取得する
	        PackageManager pm = getPackageManager();
	        Intent intent = new Intent( Intent.ACTION_MAIN, null );
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        //List<ApplicationInfo> appInfo = pm.getInstalledApplications(PackageManager.GET_META_DATA);
	        List<ResolveInfo> appInfo = pm.queryIntentActivities(intent, 0);//getInstalledApplications(PackageManager.GET_META_DATA);
	        if( appInfo != null )
	        {
	        	for( ResolveInfo info : appInfo )
	        	{
	        		// システムアプリは除く
	        		//if( (info.flags & ApplicationInfo.FLAG_SYSTEM ) == ApplicationInfo.FLAG_SYSTEM ) continue;
	        		// 自分は除く
	        		if( info.activityInfo.applicationInfo.packageName.equals(this.getPackageName()) ) continue;
	        		// アイコンがないものは除く
	        		//if( info.activityInfo.icon == 0 ) continue;
	        		appList.add( (String) pm.getApplicationLabel(info.activityInfo.applicationInfo) );
	        		appPkgName.add( info.activityInfo.applicationInfo.packageName );
	        	}
	        }
	        
	        lp.setEntries( (String[]) appList.toArray(new String[appList.size()]) );
	        lp.setEntryValues( (String[]) appPkgName.toArray(new String[appPkgName.size()]));
	        lp.setSummary(lp.getEntry());
	     
	        
	    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
        // qrコードアプリの設定
        ListPreference lp = (ListPreference) findPreference( "lst_qr_launch_key" );
        lp.setSummary( lp.getEntry() );
	}
}
