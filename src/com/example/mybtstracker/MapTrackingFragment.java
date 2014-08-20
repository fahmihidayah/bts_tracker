package com.example.mybtstracker;

import java.util.ArrayList;
import java.util.List;

import com.example.beans.DataSingleton;
import com.example.beans.MapTrackingBeans;
import com.example.model.BtsData;
import com.example.model.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MapTrackingFragment extends Fragment implements Constants,
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	
	private static final int MILLISECONDS_PER_SECOND = 100;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	private View rootView;
	private MapView mapView;
	private GoogleMap map;
	private TelephonyManager telephonyManager;
	private MapTrackingBeans trackingBeans;

	private LocationRequest mLocationRequest;
	private LocationClient locationClient;

	private TextView textViewCurrentLocation;
	private Button buttonCurrentLocation;
//	private Button buttonAddBts;
//	private Button buttonClear;
	public ArrayList<Marker> listMarker;

	private void initialMarker() {
//		clearMarker();
		listMarker = new ArrayList<Marker>();
		ArrayList<BtsData> listBtsData = DataSingleton.getInstance(MapTrackingFragment.this.getActivity())
				.getListBtsDatas();
		Toast.makeText(getActivity(), "data bts ada " + listBtsData.size(),Toast.LENGTH_LONG).show();
		for (BtsData btsData : listBtsData) {
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(new LatLng(btsData.getLatitude(), btsData
					.getLongitude()));
			markerOptions.title(btsData.getBtsName());
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.icon_tower_black));
			Marker marker = map.addMarker(markerOptions);
			listMarker.add(marker);
		}
//		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//
//			@Override
//			public boolean onMarkerClick(Marker marker) {
//				ChooseDialog chooseDialog = new ChooseDialog();
//				chooseDialog.show(getActivity().getFragmentManager(),
//						"choose_dialog");
//				return true;
//			}
//		});
	}
	
	private void clearMarker(){
		if(listMarker != null){
			for (Marker marker : listMarker) {
				marker.remove();
			}
			listMarker.clear();
		}
		else {
			listMarker = new ArrayList<Marker>();
		}
	}
	
	
	public void initialListenner() {

		telephonyManager = (TelephonyManager) getActivity().getSystemService(
				Activity.TELEPHONY_SERVICE);

		telephonyManager.listen(new PhoneStateListener() {

			@Override
			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				// change color
				trackingBeans.onSignalStrengthsChanged(signalStrength,
						telephonyManager);
			}
		}, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

	}

	private void initialComponent() {
		textViewCurrentLocation = (TextView) rootView
				.findViewById(R.id.textViewCurrentLocation);
		buttonCurrentLocation = (Button) rootView
				.findViewById(R.id.buttonCurrentLocation);
		buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Location location = locationClient.getLastLocation();
				String locationText = "Latitude :" + location.getLatitude()
						+ " Longitude :" + location.getLongitude();
				textViewCurrentLocation.setText(locationText);
				TelephonyManager tm = (TelephonyManager) getActivity()
						.getSystemService(getActivity().TELEPHONY_SERVICE);
				GsmCellLocation locationFsm = (GsmCellLocation) tm
						.getCellLocation();
				if (locationFsm != null) {
					Toast.makeText(getActivity(),
							"cell id " + locationFsm.getCid(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
//		buttonAddBts = (Button) rootView.findViewById(R.id.buttonAddBts);
//		buttonAddBts.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// AddBtsDialogFragment addBtsDialogFragment = new
//				// AddBtsDialogFragment();
//				// addBtsDialogFragment.show(getActivity().getFragmentManager(),
//				// "add_bts_dialog");
//				Intent intent = new Intent(getActivity(), AddBtsActivity.class);
//				intent.putExtra("isEdit", false);
//				startActivity(intent);
//			}
//		});

//		buttonClear = (Button) rootView.findViewById(R.id.buttonClear);
//		buttonClear.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ConfirmDeleteAllBtsDialog confirmDeleteAllBtsDialog = new ConfirmDeleteAllBtsDialog();
//				confirmDeleteAllBtsDialog.show(getActivity()
//						.getFragmentManager(), "conifirm_delete_all_dialog");
//			}
//		});
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		locationClient = new LocationClient(getActivity(), this, this);
		super.onCreate(savedInstanceState);
	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_map_tracking, container,
				false);
		initialComponent();
		mapView = (MapView) rootView.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);

		trackingBeans = new MapTrackingBeans(this);
		this.initialListenner();
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		MapsInitializer.initialize(getActivity());
		this.initialMarker();
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
			locationClient.requestLocationUpdates(mLocationRequest, this);
			Location location = locationClient.getLastLocation();
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, 17);
			map.animateCamera(cameraUpdate);
			LatLng latlang = new LatLng(location.getLatitude(),
					location.getLongitude());
			trackingBeans.setCurrentLatLang(latLng);
		}
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLocationChanged(Location location) {
		// add data
		if (locationClient != null) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, 17);
			map.animateCamera(cameraUpdate);
			trackingBeans.onMyLocationChange(location, map, telephonyManager);
		}
	}

	//
	// public class AddBtsDialogFragment extends DialogFragment {
	// private View rootViewDialog;
	// private EditText editTextName;
	// private EditText editTextKeterangan;
	// private EditText editTextLongitude;
	// private EditText editTextLatitude;
	//
	// @Override
	// public Dialog onCreateDialog(Bundle savedInstanceState) {
	// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	//
	// LayoutInflater layoutInflater = getActivity().getLayoutInflater();
	// rootViewDialog = layoutInflater.inflate(
	// R.layout.layout_add_bts_dialog, null);
	// // initial
	// editTextKeterangan = (EditText)
	// rootViewDialog.findViewById(R.id.editTextKeterangan);
	// editTextLatitude = (EditText)
	// rootViewDialog.findViewById(R.id.editTextLatitude);
	// editTextLongitude = (EditText)
	// rootViewDialog.findViewById(R.id.editTextLongitude);
	// editTextName = (EditText)
	// rootViewDialog.findViewById(R.id.editTextBtsName);
	//
	//
	// builder.setView(rootViewDialog);
	// builder.setTitle("Tambah BTS");
	// builder.setPositiveButton("Save", new OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// try{
	// LatLng position = new
	// LatLng(Double.parseDouble(editTextLatitude.getText().toString()),
	// Double.parseDouble(editTextLongitude.getText().toString()));
	// listMarker.add(map.addMarker(new
	// MarkerOptions().snippet(editTextKeterangan.getText().toString())
	// .title(editTextName.getText().toString())
	// .position(position)
	// .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_black))
	// ));
	// }
	// catch(NumberFormatException ex){
	// Toast.makeText(getActivity(), "Data salah", Toast.LENGTH_LONG).show();
	// }
	//
	// }
	// });
	// builder.setNegativeButton("Batal", new OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// Toast.makeText(getActivity(), "Batal", Toast.LENGTH_LONG).show();
	// }
	// });
	// return builder.create();
	// }
	// }

	@SuppressLint("ValidFragment")
	public class ChooseDialog extends DialogFragment {
		private int selectedIndex;

		public int getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Pilih Aksi").setItems(R.array.list_action_bts,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:

								break;
							case 1:

								break;
							case 2:
								ConfirmDialog confirmDialog = new ConfirmDialog();
								confirmDialog.show(getFragmentManager(),
										"confirm_dialog");
							default:
								break;
							}
						}
					});
			return builder.create();
		}
	}

	@SuppressLint("ValidFragment")
	public class ConfirmDialog extends DialogFragment {

		private int indexMarker;

		public int getIndexMarker() {
			return indexMarker;
		}

		public void setIndexMarker(int indexMarker) {
			this.indexMarker = indexMarker;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Konfirmasi");
			builder.setMessage("Apakah anda ingin menghapus BTS ini?");
			builder.setPositiveButton("IYA",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							listMarker.get(indexMarker).remove();
							listMarker.remove(indexMarker);
						}
					});
			builder.setNegativeButton("TIDAK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getActivity(), "Batal",
									Toast.LENGTH_LONG).show();
						}
					});

			return builder.create();
		}
	}

	@SuppressLint("ValidFragment")
	public class ConfirmDeleteAllBtsDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Konfirmasi");
			builder.setMessage("Apakah anda ingin menghapus semua BTS?");
			builder.setPositiveButton("IYA",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							trackingBeans.clear(map);
						}
					});
			builder.setNegativeButton("TIDAK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getActivity(), "Batal",
									Toast.LENGTH_LONG).show();
						}
					});

			return builder.create();
		}
	}
}
