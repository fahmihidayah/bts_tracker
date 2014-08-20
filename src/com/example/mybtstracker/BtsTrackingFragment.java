package com.example.mybtstracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.example.beans.BtsTrackingBeans;
import com.example.beans.DataSingleton;
import com.example.beans.MapTrackingBeans;
import com.example.model.TrackingData;
import com.framework.adapter.CustomAdapter;
import com.framework.commonUtilities.CommonUtilities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BtsTrackingFragment extends Fragment {

	public View rootView;
	public ListView listViewReport;
	public CustomAdapter<TrackingData> trackingDataAdapter;
	private BtsTrackingBeans btsTrackingBeans;
	private Button buttonExportToCsv;
	
	private void initialData() {
		listViewReport = (ListView) rootView.findViewById(R.id.listViewReport);
		this.trackingDataAdapter = new CustomAdapter<TrackingData>(
				getActivity(), R.layout.layout_data_item, DataSingleton.getInstance(this.getActivity()).getListTrackingData()) {

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
		listViewReport.setAdapter(trackingDataAdapter);
		listViewReport.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					TrackingData data = DataSingleton.getInstance(BtsTrackingFragment.this.getActivity()).getListTrackingData().get(arg2);
					InfoDialogFragment infoDialogFragment = new InfoDialogFragment();
					infoDialogFragment.setData(data);
					infoDialogFragment.show(BtsTrackingFragment.this.getActivity().getFragmentManager(), "info_dialog");	
				}
				catch(NullPointerException ex){
					Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		buttonExportToCsv = (Button) rootView.findViewById(R.id.buttonExportCsv);
		buttonExportToCsv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SaveDataDialogFragment saveDataDialogFragment = new SaveDataDialogFragment();
				saveDataDialogFragment.show(BtsTrackingFragment.this.getActivity().getSupportFragmentManager(), "save_dialog_fragment");
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_bts_tracking, null);
		initialData();
		btsTrackingBeans = new BtsTrackingBeans(this);
		
		return rootView;
	}
	
	/*
	  private String time;
	private String LAC;
	private String node;
	private String cellId;
	private String level;
	private String latitude;
	private String logitude;
	private String networkType;
	private double jarak;
	 */
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
		private DecimalFormat decimalFormat = new DecimalFormat("##.##");
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
				
				textViewJarak.setText("Jarak : " + decimalFormat.format(data.getJarak())+ " m");
				
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
	
	@SuppressLint({ "ValidFragment", "NewApi" })
	public class SaveDataDialogFragment extends android.support.v4.app.DialogFragment {
		private View rootViewDialog;
		private EditText editTextNamaFile;
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();
			rootViewDialog = layoutInflater.inflate(R.layout.layout_file_csv_dialog, null);
			editTextNamaFile = (EditText) rootViewDialog.findViewById(R.id.editTextName);
			builder.setView(rootViewDialog);
			builder.setTitle("Simpan Data");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							btsTrackingBeans.generateCsvFile(editTextNamaFile.getText().toString());
//							Toast.makeText(getActivity(), "CSV successfuly generated", Toast.LENGTH_LONG).show();
						}
					});

			return builder.create();
		}
	}
}
