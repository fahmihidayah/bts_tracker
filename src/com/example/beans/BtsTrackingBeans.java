package com.example.beans;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.example.model.Constants;
import com.example.model.TrackingData;
import com.example.mybtstracker.BtsTrackingFragment;
import com.framework.file_handler.FileHandler;

import android.content.Context;

public class BtsTrackingBeans implements Observer, Constants {
	private BtsTrackingFragment btsTrackingFragment;

	public BtsTrackingBeans(BtsTrackingFragment btsTrackingFragment) {
		super();
		this.btsTrackingFragment = btsTrackingFragment;
		DataSingleton.getInstance(btsTrackingFragment.getActivity()).addObserver(this);
	}

	@Override
	public void update(Observable observable, Object data) {
		btsTrackingFragment.trackingDataAdapter.notifyDataSetChanged();
	}
	
	public void generateCsvFile(String fileName){
		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		String data = "Cell id,LAC,Latitude,Longitude,Level,Network Type,Time,Jarak\n";
		ArrayList<TrackingData> trackingData = DataSingleton.getInstance(btsTrackingFragment.getActivity()).getListTrackingData();
		for (TrackingData trackingData2 : trackingData) {
			data = data + trackingData2.getCellId() + ","+
					trackingData2.getLAC() + "," +
					trackingData2.getLatitude() + "," +
					trackingData2.getLogitude() + "," +
					trackingData2.getLevel() + "," +
					trackingData2.getNetworkType() + "," +
					trackingData2.getTime() + ","+
					decimalFormat.format(trackingData2.getJarak())+ "\n";
		}
		FileHandler.writeStringToExternalStorage(btsTrackingFragment.getActivity(),FOLDER, fileName, "csv", data);
		FileHandler.saveDataToExternalFile(btsTrackingFragment.getActivity(), FOLDER, fileName + ".TRC", Context.MODE_PRIVATE, trackingData);
	}
}
