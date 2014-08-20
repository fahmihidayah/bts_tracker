package com.example.beans;

import java.math.BigDecimal;
import java.security.acl.LastOwnerException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.sax.StartElementListener;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.example.model.BtsData;
import com.example.model.Constants;
import com.example.model.TrackingData;
import com.example.mybtstracker.MapTrackingFragment;
import com.example.mybtstracker.R;
import com.google.android.gms.internal.cu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapTrackingBeans implements Constants {
//	public static double LOW_THRESHOLD = 0;
//	public static double MEDIUM_THRESHOLD = 0.000005;
//	public static double HIGH_THRESHOLD = 0.00001;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			TIME_FORMATER);
	private MapTrackingFragment mapTrackingFragment;
	private SignalManager signalManager = new SignalManager();
	private LatLng currentLatLang = null;
	private ArrayList<LatLng> listLatLgs = null;
	private boolean fistTime = true;
//	private double thresHold = LOW_THRESHOLD;
	private Polyline polyline;

	private Marker currentNearstMarker; 
	
	public MapTrackingBeans(MapTrackingFragment mapTrackingFragment) {
		super();
		this.mapTrackingFragment = mapTrackingFragment;
	}

	public void onMyLocationChange(Location location, GoogleMap googleMap, TelephonyManager telephonyManager) {
		LatLng newLatLg = new LatLng(location.getLatitude(),
				location.getLongitude());
//		measureNearestBts(location);
		if(currentLatLang == null){
			currentLatLang = new LatLng(location.getLatitude(),
					location.getLongitude());
			return;
		}
//		if(isValidThreshold(currentLatLang, newLatLg)){
//			Toast.makeText(mapTrackingFragment.getActivity(), "Location change", Toast.LENGTH_LONG).show();
		String logitude = String.valueOf(location.getLongitude());
		String latitude = String.valueOf(location.getLatitude());
		String level = signalManager.getStringSignal();
		String time = simpleDateFormat.format(new Date());
		
		TelephonyManager tm = (TelephonyManager) mapTrackingFragment.getActivity().getSystemService(mapTrackingFragment.getActivity().TELEPHONY_SERVICE);
		GsmCellLocation locationGsm = (GsmCellLocation) tm.getCellLocation();
		String cellId = String.valueOf(locationGsm.getCid());
		String lac = String.valueOf(locationGsm.getLac());
		String networkType = getNetworkType(tm);
		
		TrackingData data = new TrackingData();
		data.setLogitude(logitude);
		data.setLatitude(latitude);
		data.setLevel(level);
		data.setTime(time);
		data.setLAC(lac);
		data.setCellId(cellId);
		data.setNetworkType(networkType);
		findBtsHandler(locationGsm.getCid(), data, location);
		DataSingleton.getInstance(mapTrackingFragment.getActivity()).getListTrackingData().add(data);
		DataSingleton.getInstance(mapTrackingFragment.getActivity()).notifyDataChange();
			if (signalManager.isChange()) {
				
				if(listLatLgs == null){
					listLatLgs = new ArrayList<LatLng>();
					listLatLgs.add(new LatLng(location.getLatitude(),
						location.getLongitude()));
				}
				LatLng lastLatLng = listLatLgs.get(listLatLgs.size() - 1);
				currentLatLang = new LatLng(location.getLatitude(),
						location.getLongitude());
				listLatLgs = new ArrayList<LatLng>();
				listLatLgs.add(lastLatLng);
				listLatLgs.add(currentLatLang);
				PolylineOptions polylineOptions = new PolylineOptions();
				polylineOptions.addAll(listLatLgs);
				polylineOptions.color(signalManager.getColor());
				polylineOptions.width(5);
				polyline = googleMap.addPolyline(polylineOptions);
				signalManager.setChange(false);
			} else {
				currentLatLang = new LatLng(location.getLatitude(),
						location.getLongitude());
				listLatLgs.add(currentLatLang);
				polyline.setPoints(listLatLgs);
			}
//		}
		

	}

	public void onSignalStrengthsChanged(SignalStrength signalStrength,
			TelephonyManager telephonyManager) {
		double levelSignal = -113 + 2 * signalStrength.getGsmSignalStrength();
		signalManager.setSignalLevel(levelSignal);
//		signalManager.setChange(true);
//		if(fistTime && currentLatLang != null){
////			Toast.makeText(mapTrackingFragment.getActivity(), "signal change", Toast.LENGTH_LONG).show();
//			String logitude = String.valueOf(currentLatLang.longitude);
//			String latitude = String.valueOf(currentLatLang.latitude);
//			String level = signalManager.getStringSignal();
//			String time = simpleDateFormat.format(new Date());
//			TrackingData data = new TrackingData();
//			data.setLogitude(logitude);
//			data.setLatitude(latitude);
//			data.setLevel(level);
//			data.setTime(time);
//			DataSingleton.getInstance().getListTrackingData().add(data);
//			DataSingleton.getInstance().notifyDataChange();
//			fistTime = false;
//		}
	}

	public String getNetworkType(TelephonyManager telephonyManager) {
		int type = telephonyManager.getNetworkType();
		switch (type) {
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "3G";
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "4G";
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "EDGE 2G";
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "1xRTT";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return "EHRPD";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "EVDO 0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "EVDO A";
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return "EVDO B";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "IDEN";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "LTE";
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "UMTS";
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return "UNKNOWN";
		default:
			return "" + type;
		}
	}

	public LatLng getCurrentLatLang() {
		return currentLatLang;
	}

	public void setCurrentLatLang(LatLng currentLatLang) {
		this.currentLatLang = currentLatLang;
	}
	
//	public void setThresHold(double thresHold) {
//		this.thresHold = thresHold;
//	}
	
//	private boolean isValidThreshold(LatLng lastLatLng, LatLng currentLatLng){
////		oast.makeText(mapTrackingFragment.getActivity(), "jarak " + Math.abs(lastLatLng.latitude - currentLatLng.latitude), Toast.LENGTH_LONG).show();
//		if(Math.abs(lastLatLng.latitude - currentLatLng.latitude) > thresHold){
//			return true;
//		}
//		if(Math.abs(lastLatLng.longitude - currentLatLng.longitude) > thresHold){
//			return true;
//		}
//		return false;
//	}
	
	private void measureNearestBts(Location location){
		ArrayList<Marker> listMarker = mapTrackingFragment.listMarker;
		Marker nearestMarker = null;
		if(currentLatLang != null && listMarker != null){
			float nearstValue = -1;
			for (Marker marker : listMarker) {
				//nearestMarker = marker;
				Location markerLocation = new Location("location marker");
				markerLocation.setLatitude(marker.getPosition().latitude);
				markerLocation.setLongitude(marker.getPosition().longitude);
				if(nearstValue == -1){
					nearstValue = location.distanceTo(markerLocation);
					nearestMarker = marker;
				}
				else{
					float tempNearestValue = location.distanceTo(markerLocation);
					if(tempNearestValue < nearstValue){
						nearstValue = tempNearestValue;
						nearestMarker = marker;
					}
				}
			}	
		}
		
		nearestMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_red));
		if(currentNearstMarker != null && nearestMarker != currentNearstMarker){
			currentNearstMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_black));
		}
		currentNearstMarker = nearestMarker;
	}
	
	@SuppressLint("UseValueOf")
	private void findBtsHandler(int cellId, TrackingData trackingData, Location location){
		ArrayList<BtsData> listBtsData = DataSingleton.getInstance(mapTrackingFragment.getActivity()).getListBtsDatas();
		Marker nearestMarker = null;
		for (BtsData btsData : listBtsData) {
			ArrayList<Integer> listBtsCellId = btsData.getListBtsCellId();
			if(listBtsCellId.contains(cellId)){
				ArrayList<Marker> listMarker = mapTrackingFragment.listMarker;
				for (Marker marker : listMarker) {
					if(marker.getTitle().equalsIgnoreCase(btsData.getBtsName())){
						nearestMarker = marker;
						nearestMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_red));
						Location btsLocation  = new Location("location_makerker");
						btsLocation.setLatitude(nearestMarker.getPosition().latitude);
						btsLocation.setLongitude(nearestMarker.getPosition().longitude);
						double distance = distance_between(btsLocation, location);
//						distance = gps2m(btsLocation.getLatitude(), btsLocation.getLongitude(), location.getLatitude(), location.getLongitude());
						Double jarak = new Double(new DecimalFormat("##.##").format(distance));
						trackingData.setJarak(distance);
						break;
					}
				}
			}
		}
		if(currentNearstMarker != null && nearestMarker != null && nearestMarker != currentNearstMarker){
			currentNearstMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_black));
		}
	}
	
	public void clear(GoogleMap map){
		map.clear();
	}

	double distance_between(Location l1, Location l2)
	{
	    //float results[] = new float[1];
	    /* Doesn't work. returns inconsistent results
	    Location.distanceBetween(
	            l1.getLatitude(),
	            l1.getLongitude(),
	            l2.getLatitude(),
	            l2.getLongitude(),
	            results);
	            */
	    double lat1=l1.getLatitude();
	    double lon1=l1.getLongitude();
	    double lat2=l2.getLatitude();
	    double lon2=l2.getLongitude();
	    double R = 6371; // km
	    double dLat = (lat2-lat1)*Math.PI/180;
	    double dLon = (lon2-lon1)*Math.PI/180;
	    lat1 = lat1*Math.PI/180;
	    lat2 = lat2*Math.PI/180;

	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	            Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = R * c * 1000;

	    Log.d("distance","dist betn "+
	            d + " " +
	            l1.getLatitude()+ " " +
	            l1.getLongitude() + " " +
	            l2.getLatitude() + " " +
	            l2.getLongitude()
	            );

	    return d;
	}
}
