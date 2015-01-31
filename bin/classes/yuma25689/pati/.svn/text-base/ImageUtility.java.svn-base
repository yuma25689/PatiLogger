package yuma25689.pati;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtility {
	//アイコンサイズの変更
    public static Drawable resizeIcon(Drawable icon,Context ctx) {
        //標準アイコンサイズの取得
        Resources res=ctx.getResources();
        int width =(int)res.getDimension(android.R.dimen.app_icon_size);
        int height=(int)res.getDimension(android.R.dimen.app_icon_size);

        //現在のアイコンサイズの取得
        int iconWidth =icon.getIntrinsicWidth();
        int iconHeight=icon.getIntrinsicHeight();

        //アイコンサイズの変更
        if (width>0 && height>0 && (
        		(iconWidth<width||iconHeight<height) ||
            (width<iconWidth || height<iconHeight))) {
            
        	// なぜかアイコンのサイズをかえると逆におかしくなるのでこのままにする
        	// densityの問題かと思う
        	width=iconWidth;
        	height=iconHeight;
        	
            //変換後のアイコンサイズの計算
            float ratio=(float)iconWidth/(float)iconHeight;
            if (iconWidth>iconHeight) {
                height=(int)(width/ratio);
            } else if (iconHeight>iconWidth) {
                width=(int)(height*ratio);
            }

            //動的キャンバスの生成
            Bitmap.Config c=(icon.getOpacity()!=PixelFormat.OPAQUE)?
                Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
            Bitmap thumb=Bitmap.createBitmap(width,height,c);
            Canvas canvas=new Canvas(thumb);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG,0));

            //動的キャンバスへのアイコン描画
            Rect oldBounds=new Rect();
            oldBounds.set(icon.getBounds());
            icon.setBounds(0,0,width,height);
            icon.draw(canvas);
            icon.setBounds(oldBounds);
            
            //キャンバスをDrawableオブジェクトに変換
            icon=new BitmapDrawable(thumb);
        }
        return icon;
    }
	//アイコンサイズの変更
    public static Drawable resizeIcon(Drawable icon,Context ctx, int width, int height) {
        //標準アイコンサイズの取得
        //Resources res=ctx.getResources();

        //現在のアイコンサイズの取得
        int iconWidth =icon.getIntrinsicWidth();
        int iconHeight=icon.getIntrinsicHeight();

        //アイコンサイズの変更
        if (width>0 && height>0 && (
        		(iconWidth<width||iconHeight<height) ||
            (width<iconWidth || height<iconHeight))) {
                    	
            //変換後のアイコンサイズの計算
            float ratio=(float)iconWidth/(float)iconHeight;
            if (iconWidth>iconHeight) {
                height=(int)(width/ratio);
            } else if (iconHeight>iconWidth) {
                width=(int)(height*ratio);
            }

            //動的キャンバスの生成
            Bitmap.Config c=(icon.getOpacity()!=PixelFormat.OPAQUE)?
                Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
            Bitmap thumb=Bitmap.createBitmap(width,height,c);
            Canvas canvas=new Canvas(thumb);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG,0));

            //動的キャンバスへのアイコン描画
            Rect oldBounds=new Rect();
            oldBounds.set(icon.getBounds());
            icon.setBounds(0,0,width,height);
            icon.draw(canvas);
            icon.setBounds(oldBounds);
            
            //キャンバスをDrawableオブジェクトに変換
            icon=new BitmapDrawable(thumb);
        }
        return icon;
    }
}
