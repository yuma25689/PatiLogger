
package yuma25689.pati;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoneyMenuAdapter extends ArrayAdapter<MoneyMenuData> {

	private ArrayList<MoneyMenuData> items;
	private LayoutInflater inflater;

	Context ctx_ = null;
	
	public MoneyMenuAdapter(Context ctx,
			int resId, ArrayList<MoneyMenuData> items ) {
		super(ctx, resId, items);
		ctx_ = ctx;
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
			view = inflater.inflate(R.layout.lst_money_row, null);			
		}
		
		// Activity側でpositionをあわせる必要があるので注意すること
		MoneyMenuData item = items.get(position);

		// 背景画像をセットする
		//view.setBackgroundResource(item.getResId);
		
		if( item != null ) {
			TextView MainMenuName
				= (TextView)view.findViewById(R.id.toptext);
			MainMenuName.setText(item.getMenuString());
			TextView tmp
				= (TextView)view.findViewById(R.id.toptextright);
			if( item.getRightString() != null && item.getRightString().length() > 0
			&& item.getRightString().toCharArray()[0] == '-' )
			{
				tmp.setTextColor( ctx_.getResources().getColor(R.color.red ));
			}
			else
			{
				tmp.setTextColor( ctx_.getResources().getColor(android.R.color.secondary_text_dark ));
			}
			tmp.setText(item.getRightString());
			tmp = (TextView)view.findViewById(R.id.bottomtext1cap);
			tmp.setText(item.getBtmCap1String());
			tmp = (TextView)view.findViewById(R.id.bottomtext1);
			tmp.setText(item.getBtm1String());
			tmp = (TextView)view.findViewById(R.id.bottomtext2cap);
			tmp.setText(item.getBtmCap2String());
			tmp = (TextView)view.findViewById(R.id.bottomtext2);
			tmp.setText(item.getBtm2String());
			
			tmp = (TextView)view.findViewById(R.id.bottomtext3cap);
			tmp.setText(item.getBtmCap3String());
			tmp = (TextView)view.findViewById(R.id.bottomtext3);
			tmp.setText(item.getBtm3String());			
			tmp = (TextView)view.findViewById(R.id.bottomtext4cap);
			tmp.setText(item.getBtmCap4String());
			tmp = (TextView)view.findViewById(R.id.bottomtext4);
			if( item.getBtm4String() != null && item.getBtm4String().length() > 0
			&& item.getBtm4String().toCharArray()[0] == '-' )
			{
				tmp.setTextColor( ctx_.getResources().getColor(R.color.red ));
			}
			else
			{
				tmp.setTextColor( ctx_.getResources().getColor(android.R.color.secondary_text_dark ));
			}
			tmp.setText(item.getBtm4String());			
			
			if( item.getResId() != 0 ) {
				ImageView MainMenuImg 
				= (ImageView)view.findViewById(R.id.lsticon);
				MainMenuImg.setImageResource(item.getResId());
			}
		}

		return view;
	}
	
	
	
}
