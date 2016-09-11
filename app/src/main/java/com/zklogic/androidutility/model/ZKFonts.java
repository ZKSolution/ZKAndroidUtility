package com.zklogic.androidutility.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zklogic.androidutility.Exceptions.ZKFontException;
import com.zklogic.androidutility.ZKUtil;

import java.util.HashMap;

public class ZKFonts {

	private static ZKFonts instance;
	private boolean isDebugg = false;

	private HashMap<Integer,String> fontList = new HashMap<Integer,String>();
	private HashMap<Integer,Typeface> fontLoad = new HashMap<Integer,Typeface>();

	private ZKFonts(Context context) {
		// TODO Auto-generated constructor stub
		isDebugg = false;
	}

	public static ZKFonts getIntance(Context context) {
		if (instance == null) {
			instance = new ZKFonts(context);
		}
		return instance;
	}

	public void addFont(Context context, Integer key, String fontPath) throws ZKFontException{
		try{

			Typeface font = Typeface.createFromAsset(context.getAssets(), fontPath);
			this.fontLoad.put(key,font);
			this.fontList.put(key,fontPath);
		}
		catch (Exception e){
			throw new ZKFontException();
		}
	}



	public void applyFont0Font(TextView text) {
        Typeface typeface = this.fontLoad.get(0);
        if(typeface != null){
            text.setTypeface(typeface);
        }
        else{
            showLog("font list is empty");
        }

	}

	public void applyFont(Button button) {
        applyFont0Font((TextView) button);
	}

	public void applyFont(EditText edit) {
        applyFont0Font((TextView) edit);
	}

	public void applyFontViewGroup(ViewGroup group) {
		for (int i = 0; i < group.getChildCount(); i++)
		{
			View child = group.getChildAt(i);
			if (child instanceof ViewGroup)
			{
				applyFontViewGroup((ViewGroup) child);
			}
			else if (child != null)
			{
				if (child instanceof TextView)
				{
                    applyFont0Font((TextView) child);
				}
			}
		}
	}

    public Typeface getFont(int fontKey){
        Typeface typeface = this.fontLoad.get(fontKey);
        if(typeface != null){
            return typeface;
        }
        else{
            showLog("fontKey is not available");
        }
        return null;
    }

	public void applyFont(TextView text, int fontKey) {
        Typeface typeface = this.fontLoad.get(fontKey);
        if(typeface != null){
            text.setTypeface(typeface);
        }
        else{
            showLog("fontKey is not available");
        }

	}

	public void applyFont(Button button, int fontKey) {
		applyFont((TextView) button, fontKey);
	}

	public void applyFont(EditText edit, int fontKey) {
		applyFont((TextView) edit, fontKey);
	}

	public void applyFontViewGroup(ViewGroup group, int fontKey) {
		for (int i = 0; i < group.getChildCount(); i++)
		{
			View child = group.getChildAt(i);
			if (child instanceof ViewGroup)
			{
				applyFontViewGroup((ViewGroup) child);
			}
			else if (child != null)
			{
				showLog(child.toString());
				if (child instanceof TextView)
				{
					applyFont((TextView) child, fontKey);
				}
			}
		}
	}

	public void setDeugg(boolean isDeugg) {
		this.isDebugg = isDeugg;
	}

	public boolean isDeugg() {
		return isDebugg;
	}

	private void showLog(String msg) {
		if (isDebugg) {
			ZKUtil.showLog(msg);
		}
	}

}
