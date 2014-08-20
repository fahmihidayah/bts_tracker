package com.example.beans;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Observable;

import android.content.Context;
import android.widget.Toast;

import com.example.model.BtsData;
import com.example.model.Constants;
import com.example.model.TrackingData;
import com.framework.file_handler.FileHandler;

public class DataSingleton extends Observable implements Constants {
	private static DataSingleton instance;
	private ArrayList<TrackingData> listTrackingData = new ArrayList<TrackingData>();
	private ArrayList<BtsData> listBtsDatas = new ArrayList<BtsData>();
	
	protected DataSingleton(Context context) {
		
	}

	public static DataSingleton getInstance(Context context) {
		if (instance == null) {
			instance = new DataSingleton(context);
			
		}
		return instance;
	}

	public ArrayList<TrackingData> getListTrackingData() {
		return listTrackingData;
	}

	public void setListTrackingData(ArrayList<TrackingData> listTrackingData) {
		this.listTrackingData = listTrackingData;
	}

	public ArrayList<BtsData> getListBtsDatas() {
		return listBtsDatas;
	}

	public void setListBtsDatas(ArrayList<BtsData> listBtsDatas) {
		this.listBtsDatas = listBtsDatas;
	}

	public void initialTitleTrackingData() {
		TrackingData trackingTitle = new TrackingData();
		trackingTitle.setTime("Time        ");
		trackingTitle.setLatitude("Latitude");
		trackingTitle.setLogitude("Longitude      ");
		trackingTitle.setLevel("Level");
		getListTrackingData().add(trackingTitle);
	}

	public void notifyDataChange() {
		setChanged();
		notifyObservers();
	}
	
	public void notifyDataChange(Object object){
		setChanged();
		notifyObservers(object);
	}

	public void saveAllData(Context context) {
		try {
			FileHandler.saveDataToFile(context, BTS_FILE, Context.MODE_PRIVATE,
					listBtsDatas);
			FileHandler.saveDataToFile(context, TRACKING_FILE, Context.MODE_PRIVATE, listTrackingData);
		} catch (FileNotFoundException e) {
			Toast.makeText(context, "File Not found", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(context, "Error input output", Toast.LENGTH_LONG)
					.show();
		}

	}
	
	public void loadAllData(Context context)  {
		try {
			listBtsDatas = (ArrayList<BtsData>) FileHandler.loadDataFromFile(
					context, BTS_FILE);
			listTrackingData = (ArrayList<TrackingData>) FileHandler.loadDataFromFile(context, TRACKING_FILE);
		} catch (StreamCorruptedException e) {
			Toast.makeText(context, "StreamCorruptedException", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		}
						
	}
	
	
}
