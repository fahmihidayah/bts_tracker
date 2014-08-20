package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class TrackingData implements Serializable {
	private String time;
	private String LAC;
	private String node;
	private String cellId;
	private String level;
	private String latitude;
	private String logitude;
	private String networkType;
	private double jarak ;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLAC() {
		if(LAC == null){
			return "";
		}
		return LAC;
	}

	public void setLAC(String lAC) {
		if(lAC == null){
			lAC = "";
		}
		LAC = lAC;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		if(node == null){
			node = "";
		}
		this.node = node;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLogitude() {
		return logitude;
	}

	public void setLogitude(String logitude) {
		this.logitude = logitude;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}


	public double getJarak() {
		return jarak;
	}

	public void setJarak(double jarak) {
		this.jarak = jarak;
	}

	public String getFormatedJarak(){
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(jarak);
	}
}
