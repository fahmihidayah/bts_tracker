package com.example.mybtstracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import com.example.beans.DataSingleton;
import com.example.beans.DemoBeans;
import com.example.model.BtsData;
import com.example.model.TrackingData;
import com.example.mybtstracker.MapTrackingFragment.ConfirmDialog;
import com.framework.action_bar_builder.ActionBarTabBuilder;
import com.framework.adapter.CustomAdapter;
import com.framework.commonUtilities.CommonUtilities;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

@SuppressLint("NewApi")
public class DemoActivity extends FragmentActivity {
	ActionBarTabBuilder actionBarTabBuilder;

	private void initialComponent() {
		actionBarTabBuilder = new ActionBarTabBuilder(this, R.id.viewPager);
		actionBarTabBuilder.addNewTabWithDefaultListenner("DEMO TRACKING",
				new MapDemoFragment());
//		actionBarTabBuilder.addNewTabWithDefaultListenner("REPORT",
//				new ListDemoFragment());
		actionBarTabBuilder.createAdapter();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracking_activity);
		initialComponent();
	}
}