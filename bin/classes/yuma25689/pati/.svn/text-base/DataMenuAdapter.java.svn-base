
package yuma25689.pati;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DataMenuAdapter extends ArrayAdapter<DataMenuData> {

	private ArrayList<DataMenuData> items;
	private LayoutInflater inflater;

	public DataMenuAdapter(Context ctx,
			int resId, ArrayList<DataMenuData> items ) {
		super(ctx, resId, items);
		this.items = items;
		this.inflater = (LayoutInflater) ctx
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// ビューを受け取る
		View view = convertView;

		if( view == null ) {
			// 受け取ったビューがnullなら新しくビューを生成  
			view = inflater.inflate(R.layout.lst_data_row, null);			
		}
		
		// Activity側でpositionをあわせる必要があるので注意すること
		DataMenuData item = items.get(position);

		// 背景画像をセットする
		//view.setBackgroundResource(item.getResId);
		
		if( item != null ) {
			TextView MainMenuName
				= (TextView)view.findViewById(R.id.toptext);
			MainMenuName.setText(item.getMenuString());
			TextView tmp
				= (TextView)view.findViewById(R.id.toptextright);
			tmp.setText(item.getRightString());
			
			if( item.getResId() != 0 ) {
				ImageView MainMenuImg 
				= (ImageView)view.findViewById(R.id.lsticon);
				MainMenuImg.setImageResource(item.getResId());
			}
		}

		return view;
	}
	
	
	
}
