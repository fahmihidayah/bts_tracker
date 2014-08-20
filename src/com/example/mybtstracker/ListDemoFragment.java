package com.example.mybtstracker;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.example.beans.DataSingleton;
import com.example.beans.DemoBeans;
import com.example.beans.ListDemoBeans;
import com.example.model.BtsData;
import com.example.model.Constants;
import com.example.model.TrackingData;
import com.example.mybtstracker.BtsTrackingFragment.InfoDialogFragment;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class ListDemoFragment extends Fragment implements Observer{

	private View rootView;
	public ListView listViewReport;
	public CustomAdapter<TrackingData> customAdapter;
	public Button buttonExportCsv;
	private ListDemoBeans listDemoBeans;
	
	private void initialComponent(){
		DataSingleton.getInstance(ListDemoFragment.this.getActivity()).addObserver(this);
		listDemoBeans = new ListDemoBeans(this);
		listViewReport = (ListView) rootView.findViewById(R.id.listViewReport);
		buttonExportCsv = (Button) rootView.findViewById(R.id.buttonExportCsv);
		buttonExportCsv.setVisibility(View.GONE);
		customAdapter = new CustomAdapter<TrackingData>(getActivity(), R.layout.layout_data_item, listDemoBeans.getListTrackingData()) {

			@Override
			public void setViewItems(View view, int position) {
				TrackingData trackingData = listData.get(position);
				CommonUtilities.setTextToView(view, R.id.textViewTime, trackingData.getTime());
				CommonUtilities.setTextToView(view, R.id.textViewCellId, trackingData.getCellId());
				CommonUtilities.setTextToView(view, R.id.textViewType, trackingData.getNetworkType());
				CommonUtilities.setTextToView(view, R.id.textViewLac, trackingData.getLAC());
				CommonUtilities.setTextToView(view, R.id.textViewLevel, trackingData.getLevel());
//				CommonUtilities.setTextToView(view, R.id.textViewLongitude, listData.get(position).getLogitude());
//				CommonUtilities.setTextToView(view, R.id.textViewLatitude, listData.get(position).getLatitude());
			}
		};
		listViewReport.setAdapter(customAdapter);
		listViewReport.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					TrackingData data = DataSingleton.getInstance(ListDemoFragment.this.getActivity()).getListTrackingData().get(arg2);
					InfoDialogFragment infoDialogFragment = new InfoDialogFragment();
					infoDialogFragment.setData(data);
					infoDialogFragment.show(ListDemoFragment.this.getActivity().getFragmentManager(), "info_dialog");	
				}
				catch(NullPointerException ex){
					Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_bts_tracking, null);
		initialComponent();
		return rootView;
	}

	@Override
	public void onDestroy() {
		DataSingleton.getInstance(this.getActivity()).deleteObserver(this);
		super.onDestroy();
	}
	
	@SuppressLint({ "ValidFragment", "NewApi" })
	public class InfoDialogFragment extends DialogFragment {
		private View rootViewDialog;
		private TrackingData data;
		private TextView textViewTime;
		private TextView textViewLac;
		private TextView textViewNode;
		private TextView textViewCellId;
		private TextView textViewLevel;
		private TextView textViewLatitude;
		private TextView textViewLongitude;
		private TextView textViewNetworkType;
		private TextView textViewJarak;
		private DecimalFormat decimalFormat = new  DecimalFormat("##.##");
		
		public void setData(TrackingData data) {
			this.data = data;
		}
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();
			rootViewDialog = layoutInflater.inflate(R.layout.layout_data_info_dialog, null);
			builder.setView(rootViewDialog);
			builder.setTitle("Info");
			textViewTime = (TextView) rootViewDialog.findViewById(R.id.textViewTime);
			textViewLac = (TextView) rootViewDialog.findViewById(R.id.textViewLAC);
			textViewNode = (TextView) rootViewDialog.findViewById(R.id.textViewNode);
			textViewCellId = (TextView) rootViewDialog.findViewById(R.id.textViewCellId);
			textViewLevel = (TextView) rootViewDialog.findViewById(R.id.textViewLevel);
			textViewLatitude = (TextView) rootViewDialog.findViewById(R.id.textViewLatitude);
			textViewLongitude = (TextView) rootViewDialog.findViewById(R.id.textViewLongitude);
			textViewNetworkType = (TextView) rootViewDialog.findViewById(R.id.textViewNetworkType);
			textViewJarak = (TextView) rootViewDialog.findViewById(R.id.textViewJarak);
			if(data != null){
				textViewTime.setText("Time : " + data.getTime());
				textViewLac.setText("Lac :" + data.getLAC());
				
				textViewNode.setText("Node : " + data.getNode());
				textViewCellId.setText("Cell id : " + data.getCellId());
				
				textViewLevel.setText("Level : " + data.getLevel());
				textViewLatitude.setText("Latitude : " + data.getLatitude());
				
				textViewLongitude.setText("Longitude : " + data.getLogitude());
				textViewNetworkType.setText("Network Type : " + data.getNetworkType());
				
				textViewJarak.setText("Jarak : " + decimalFormat.format(data.getFormatedJarak())+ " m");
				
			}
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});

			return builder.create();
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if(data instanceof TrackingData){
			TrackingData trackingData = (TrackingData) data;
			listDemoBeans.getListTrackingData().add(trackingData);
			customAdapter.notifyDataSetChanged();
		}
	}
}
