package com.example.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BtsData implements Serializable {
	private String btsName;
	private double longitude;
	private double latitude;
	private String keterangan;
	private ArrayList<Integer> listBtsCellId = new ArrayList<Integer>();

	public BtsData(String btsName, double longitude, double latitude) {
		super();
		this.btsName = btsName;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getBtsName() {
		return btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public ArrayList<Integer> getListBtsCellId() {
		return listBtsCellId;
	}

	public void setListBtsCellId(ArrayList<Integer> listBtsCellId) {
		this.listBtsCellId = listBtsCellId;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((btsName == null) ? 0 : btsName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BtsData other = (BtsData) obj;
		if (btsName == null) {
			if (other.btsName != null)
				return false;
		} else if (!btsName.equals(other.btsName))
			return false;
		return true;
	}

}
