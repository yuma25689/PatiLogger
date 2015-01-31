
package yuma25689.pati;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuAdapter extends ArrayAdapter<MainMenuData> {

	//private ArrayList<MainMenuData> items;
	private LayoutInflater inflater;
	//private Context mContext = null;
	private boolean needRecreate = false;
	private ArrayList<Integer> arrDisableIndex = new ArrayList<Integer>();
	//private int displayID = 0;

	public static final int RES_ID_SEPARATOR = 100;
	public static final int RES_ID_LAUNCHER = 101;
	
	public MainMenuAdapter(Context ctx,
			int resId, ArrayList<MainMenuData> items ) {
		super(ctx, resId, items);
		//this.mContext = ctx;
		//this.items = items;
		this.needRecreate = false;
		this.inflater = (LayoutInflater) ctx
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	}
	
	public void addDisableIndex(int index)
	{
		arrDisableIndex.add(index);
	}
	public void clrDisableIndex()
	{
		arrDisableIndex.clear();
	}
	
	public void setNeedRecreate( boolean b )
	{
		needRecreate = b;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// ビューを受け取る
		// 再利用がうまくいかないので、必ず再作成するように修正する
		View view = null;//convertView;
		if( needRecreate == false )
		{
			view = convertView;
		}

		// このポジションにリストの項目が作成されている場合、再利用されるが、それが問題になる場合がある。
		// 再利用させないために、再利用できるビュー（=getItem(position)で見つかるビューと思われる）が見つかったら、また作成し直す
		MainMenuData item = getItem(position);
		//Object obj = getItem(position);
		// Activity側でpositionをあわせる必要があるので注意すること
		//MainMenuData item = items.get(position);

		if( view == null ) {//|| obj != null) {
			// 受け取ったビューがnullなら新しくビューを生成
			if( item != null && item.getResId() == RES_ID_SEPARATOR )
			{
				view = inflater.inflate(R.layout.lst_separate_row, null);
				TextView txtSeparator
				= (TextView)view.findViewById(R.id.separator);
				txtSeparator.setText( item.getMenuString() );
				view.setBackgroundColor(Color.DKGRAY);
				txtSeparator.setEnabled(false);
				return view;
			}
			else
			{
				view = inflater.inflate(R.layout.lst_menu_row, null);
			}
		}
		
		// 背景画像をセットする
		//view.setBackgroundResource(item.getResId);
		
		if( item != null ) {
			TextView MainMenuName
				= (TextView)view.findViewById(R.id.toptext);
			MainMenuName.setText(item.getMenuString());
			if( item.getResId() == RES_ID_LAUNCHER && item.getIcon() != null)
			{
				ImageView MainMenuImg 
				= (ImageView)view.findViewById(R.id.lsticon);
				MainMenuImg.setImageDrawable(item.getIcon());
			}
			else if( item.getResId() != 0 ) 
			{
				ImageView MainMenuImg 
				= (ImageView)view.findViewById(R.id.lsticon);
				MainMenuImg.setImageResource(item.getResId());
			}
		}

		return view;
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		if(arrDisableIndex.indexOf(position) != -1)
		{
			return false;
		}
		return super.isEnabled(position);
	}
	
	
	
}
