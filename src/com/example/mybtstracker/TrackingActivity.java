package com.example.mybtstracker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.beans.DataSingleton;
import com.example.beans.MapTrackingBeans;
import com.example.model.BtsData;
import com.example.model.Constants;
import com.example.model.TrackingData;
import com.framework.action_bar_builder.ActionBarTabBuilder;
import com.framework.adapter.TabsPagerAdapter;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;
import com.google.android.gms.internal.bs;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.Toast;

public class TrackingActivity extends FragmentActivity {

	ActionBarTabBuilder actionBarTabBuilder;
	
	private void initialComponent(){
		actionBarTabBuilder = new ActionBarTabBuilder(this, R.id.viewPager);
		actionBarTabBuilder.addNewTabWithDefaultListenner("MAP", new MapTrackingFragment());
		actionBarTabBuilder.addNewTabWithDefaultListenner("REPORT", new BtsTrackingFragment());
		actionBarTabBuilder.createAdapter();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracking_activity);
		initialComponent();
	}
}
