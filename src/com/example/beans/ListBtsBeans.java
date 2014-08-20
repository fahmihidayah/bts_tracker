package com.example.beans;

import java.util.ArrayList;

import com.example.model.BtsData;
import com.example.mybtstracker.ListBtsActivity;

public class ListBtsBeans {
	private ListBtsActivity btsActivity;
	private ArrayList<BtsData> listDataBts;

	public ListBtsBeans(ListBtsActivity btsActivity) {
		super();
		this.btsActivity = btsActivity;
		listDataBts = DataSingleton.getInstance(btsActivity).getListBtsDatas();
	}

	public ArrayList<BtsData> getListDataBts() {
		return listDataBts;
	}

	public void setListDataBts(ArrayList<BtsData> listDataBts) {
		this.listDataBts = listDataBts;
	}

	
}
