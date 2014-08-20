package com.example.mybtstracker;


import com.example.beans.DataSingleton;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}
	
	public void onClickTracking(View view){
		DataSingleton.getInstance(this).getListTrackingData().clear();
		startActivity(new Intent(this, TrackingActivity.class));
	}
	
	public void onClickBts(View view){
		startActivity(new Intent(this, ListBtsActivity.class));
	}
	
	public void onClickDemo(View view){
		startActivity(new Intent(this, DemoActivity.class));
	}
	
}