package yuma25689.pati;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MstExpandableListAdapter extends BaseExpandableListAdapter {
    	  
		private Context ctx = null;
		//private int[] rowId;  
		private List<String> groupData;  
		private List<List<MainMenuData>> childData;  
             
		public MstExpandableListAdapter(
				Context ctx,
				//int[] rowId,
				List<String> groups, 
				List<List<MainMenuData>> children){
			this.ctx = ctx;
			//this.rowId = rowId;  
			this.groupData = groups;  
			this.childData = children;
		}
             
		//public int getRowId(int groupPosition) {  
        //    return rowId[groupPosition];  
        //}  
             
        public Object getChild(int groupPosition, int childPosition) {
        	return childData.get(groupPosition).get(childPosition);
        }  
     
        public long getChildId(int groupPosition, int childPosition) {  
            return childPosition;  
        }
     
        public int getChildrenCount(int groupPosition) {  
            return childData.get(groupPosition).size();  
        }
     
        public TextView getGenericView() {  
            AbsListView.LayoutParams lp 
            = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
            		ctx.getResources().getDimensionPixelSize(R.dimen.listHeight));  
 
            TextView textView = new TextView(ctx);  
            textView.setLayoutParams(lp);  
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);  
            textView.setPadding(ctx.getResources().getDimensionPixelSize(R.dimen.listGroupPadding), 0, 0, 0);  
            return textView;  
        }  
             
        public View getChildView(int groupPosition, int childPosition,
        		boolean isLastChild, View convertView, ViewGroup parent) {  
     
        	View view = convertView;
        	
        	LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(
        			Context.LAYOUT_INFLATER_SERVICE);  
        	
        	MainMenuData item = (MainMenuData)getChild(groupPosition, childPosition);
  			// 受け取ったビューがnullなら新しくビューを生成 
            if( view == null )
            {
            	view = inflater.inflate(R.layout.lst_menu_row, null);
            }
    		
    		//items.get(position);

    		// 背景画像をセットする
    		//view.setBackgroundResource(item.getResId);
    		
    		if( item != null ) {
    			TextView MainMenuName
    				= (TextView)view.findViewById(R.id.toptext);
    			MainMenuName.setText(item.getMenuString());
    			if( item.getResId() != 0 ) {
    				ImageView MainMenuImg 
    				= (ImageView)view.findViewById(R.id.lsticon);
    				MainMenuImg.setImageResource(item.getResId());
    			}
    		}
            return view;  
        }
             
        public Object getGroup(int groupPosition) {  
            return groupData.get(groupPosition);  
        }  
     
        public int getGroupCount() {  
            return groupData.size();  
        }  
     
        public long getGroupId(int groupPosition) {  
            return groupPosition;  
        }  
     
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,  
               ViewGroup parent) {  
        	TextView textView = getGenericView();  
            textView.setText(getGroup(groupPosition).toString());  
            return textView;  
        }  
     
        public boolean isChildSelectable(int groupPosition, int childPosition) {  
            return true;  
        }  
     
        public boolean hasStableIds() {  
            return true;  
        }  
     
}
