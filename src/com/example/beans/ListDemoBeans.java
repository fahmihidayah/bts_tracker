package com.example.beans;

import java.util.ArrayList;

import com.example.model.TrackingData;
import com.example.mybtstracker.ListDemoFragment;

public class ListDemoBeans {
	private ListDemoFragment listDemoFragment;
	private ArrayList<TrackingData> listTrackingData = new ArrayList<TrackingData>();
	
	public ListDemoBeans(ListDemoFragment listDemoFragment) {
		super();
		this.listDemoFragment = listDemoFragment;
	}

	public ListDemoFragment getListDemoFragment() {
		return listDemoFragment;
	}

	public void setListDemoFragment(ListDemoFragment listDemoFragment) {
		this.listDemoFragment = listDemoFragment;
	}

	public ArrayList<TrackingData> getListTrackingData() {
		return listTrackingData;
	}

	public void setListTrackingData(ArrayList<TrackingData> listTrackingData) {
		this.listTrackingData = listTrackingData;
	};
	
	

}
