package yuma25689.pati;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

// インスタンスの状態を保持するためのParcel
public class RecordStatParcel implements Parcelable {
	public ArrayList<ColumnInfo> getValue() {
		return arrGuiValue;
	}
	ArrayList<ColumnInfo> arrGuiValue = null;

    public static final Parcelable.Creator<RecordStatParcel> CREATOR
    	= new Parcelable.Creator<RecordStatParcel>() {
		public RecordStatParcel createFromParcel(Parcel in) 
		{
		    return new RecordStatParcel(in);
		}
		public RecordStatParcel[] newArray(int size) 
		{
		    return new RecordStatParcel[size];
		}
    };
    
    private RecordStatParcel(Parcel in) 
    {
        // writeToParcelで保存した順番で読み出す必要がある
    	arrGuiValue = in.createTypedArrayList( ColumnInfo.CREATOR );
    }
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(arrGuiValue);
	}
	public RecordStatParcel(ArrayList<ColumnInfo> lst) {  
		this.arrGuiValue = lst;  
	} 
}
