package com.framework.commonUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CommonUtilities {
	public static void setTextToView(View rootView,int resourceTextView, String text){
		TextView textView = (TextView) rootView.findViewById(resourceTextView);
		Log.d("Result", "this is " + text);
		textView.setText(text);
	}
	
	public static Button getButton(View rootView, int resource){
		return(Button) rootView.findViewById(resource);
	}
	
	public static String getStringDate(Date date, String format){
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(date);
	}
}
