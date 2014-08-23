package com.example.mybtstracker;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.example.beans.DataSingleton;
import com.example.beans.DemoBeans;
import com.example.model.BtsData;
import com.example.model.Constants;
import com.example.model.TrackingData;
import com.framework.adapter.CustomAdapter;
import com.framework.commonUtilities.CommonUtilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MapDemoFragment extends Fragment implements Constants,
LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	private View rootView;
	private MapView mapView;
	private GoogleMap map;
	private TextView textViewKeterangan;
	private LocationClient locationClient;

	private Button buttonStartDemo;
	public ArrayList<Marker> listMarker;
	public DemoBeans demoBeans;
	private DecimalFormat decimalFormat = new DecimalFormat("##.##");

	private void initialComponent(){
		listMarker = new ArrayList<Marker>();
		ArrayList<BtsData> listBtsDatas = DataSingleton.getInstance(MapDemoFragment.this.getActivity()).getListBtsDatas();
		for (BtsData btsData : listBtsDatas) {
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.title(btsData.getBtsName());
			markerOptions.snippet(btsData.getLatitude() + ", " + btsData.getLongitude());
			markerOptions.position(new LatLng(btsData.getLatitude(),btsData.getLongitude()));
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_black));
			listMarker.add(map.addMarker(markerOptions));
		}
		demoBeans = new DemoBeans(this);
		demoBeans.loadFileMyBtsTracker();
		
		buttonStartDemo = (Button) rootView
				.findViewById(R.id.buttonStartDemo);
		buttonStartDemo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MapDemoFragment.this.getActivity(),"data ada : " + DataSingleton.getInstance(MapDemoFragment.this.getActivity()).getListTrackingData().size() , Toast.LENGTH_LONG).show();
				
				final ArrayList<TrackingData> trackingDatas = DataSingleton.getInstance(MapDemoFragment.this.getActivity()).getListTrackingData();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try{
							for (final TrackingData trackingData : trackingDatas) {
								MapDemoFragment.this.getActivity().runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										demoBeans.onMyLocationChange(trackingData, map);
										demoBeans.onSignalStrengthsChanged(Double.parseDouble(trackingData.getLevel()));
										//Toast.makeText(DemoActivity.this, "data "+trackingData.getCellId(), Toast.LENGTH_LONG).show();
										/*
										 * textViewTime.setText("Time : " + data.getTime());
					textViewLac.setText("Lac :" + data.getLAC());
					
					textViewNode.setText("Node : " + data.getNode());
					textViewCellId.setText("Cell id : " + data.getCellId());
					
					textViewLevel.setText("Level : " + data.getLevel());
					textViewLatitude.setText("Latitude : " + data.getLatitude());
					
					textViewLongitude.setText("Longitude : " + data.getLogitude());
					textViewNetworkType.setText("Network Type : " + data.getNetworkType());
					
					textViewJarak.setText("Jarak : " + data.getJarak().intValue()+ "km");
										 */
										textViewKeterangan.setText("Longitude : "+trackingData.getLogitude()+" Latitude :" +trackingData.getLatitude() + 
												"\nCellid : " + trackingData.getCellId() + "\nJarak : " + decimalFormat.format(trackingData.getJarak()) + " m\n"
												+"Level Signal : "+trackingData.getLevel() + " dBm " + "Network Type :" + trackingData.getNetworkType() +"\n"
												+ "LAC : " + trackingData.getLAC() ) ;
									}
								});
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.d("data tracking", trackingData.getLogitude());
							}
						}
						catch (NullPointerException ed){
							Log.d("exception", ed.toString());
						}
					}
				}).start();
				
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		locationClient = new LocationClient(getActivity(), this, this);
		super.onCreate(savedInstanceState);
	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.demo_map_fragment, container,
				false);
		
		mapView = (MapView) rootView.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		textViewKeterangan = (TextView) rootView.findViewById(R.id.textViewKeterangan);
		demoBeans = new DemoBeans(this);
		
		MapsInitializer.initialize(getActivity());
		this.initialComponent();
		ChooseDialog chooseDialog = new ChooseDialog();
		chooseDialog.show(getActivity().getFragmentManager(), "choose_dialog");
		return rootView;
	}

	@Override
	public void onStart() {
		locationClient.connect();
		super.onStart();
	}

	@Override
	public void onResume() {
		mapView.onResume();
//		initialMarker();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onStop() {

		if (locationClient.isConnected()) {
			locationClient.removeLocationUpdates(this);
		}
		locationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (locationClient != null) {
			Location location = locationClient.getLastLocation();
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, 17);
			map.animateCamera(cameraUpdate);
		}
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLocationChanged(Location location) {
		
	}
	
	@SuppressLint("ValidFragment")
	public class ChooseDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Pilih File");
			CustomAdapter<File>  customAdapter = new CustomAdapter<File>(getActivity(), R.layout.layout_file_item, demoBeans.getListFile()) {

				@Override
				public void setViewItems(View view, int position) {
					File file = listData.get(position);
					CommonUtilities.setTextToView(view, R.id.textViewFileName, file.getName());
				}
			};
			builder.setAdapter(customAdapter, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					demoBeans.loadDataBtsTracker(which);
					
//					Toast.makeText(getActivity(), "you selected" + demoBeans.getListFile().get(which).getName(), Toast.LENGTH_LONG).show();
				}
			});
			return builder.create();
		}
	}

}
