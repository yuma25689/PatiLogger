package yuma25689.pati;

import android.graphics.drawable.Drawable;

public class MainMenuData {
	private String menuString;  
	private int menuId = 0;
	private int resId = 0;
	private Drawable mIcon = null;

    public String getMenuString() {  
    	return menuString;
	}
    public void setMenuString(String _menuString) {  
    	this.menuString = _menuString;  
    }
    public int getMenuId() {
    	return menuId;
    }
    public void setMenuId(int _menuId) {
    	this.menuId = _menuId;
    }
	public void setResId(int _resId) {
		this.resId = _resId;
	}
	public int getResId() {
		return resId;
	}
	/**
	 * @return the Icon
	 */
	public Drawable getIcon() {
		return mIcon;
	}
	/**
	 * @param mIcon the mIcon to set
	 */
	public void setmIcon(Drawable icon) {
		this.mIcon = icon;
	}
}
