package com.example.beans;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.location.Location;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

import com.example.model.BtsData;
import com.example.model.Constants;
import com.example.model.TrackingData;
import com.example.mybtstracker.DemoActivity;
import com.example.mybtstracker.MapDemoFragment;
import com.example.mybtstracker.MapTrackingFragment;
import com.example.mybtstracker.R;
import com.framework.file_handler.FileHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DemoBeans implements Constants{
	private MapDemoFragment demoActivity;
	private SignalManager signalManager = new SignalManager();
	private ArrayList<LatLng> listLatLgs = null;
	private Polyline polyline;
	private LatLng currentLatLang;
	private Marker currentNearstMarker; 
	private ArrayList<BtsData> listBtsDatas;
	private Marker selectedMarker;
	
	private ArrayList<File> listFile = new ArrayList<File>();
	
	public DemoBeans(MapDemoFragment demoActivity) {
		super();
		this.demoActivity = demoActivity;
		listLatLgs = new ArrayList<LatLng>();
		listBtsDatas = DataSingleton.getInstance(demoActivity.getActivity()).getListBtsDatas();
	}

	public void onMyLocationChange(TrackingData trackingData, GoogleMap googleMap) {
		DataSingleton.getInstance(demoActivity.getActivity()).notifyDataChange(trackingData);
		signalManager.setSignalLevel(Double.parseDouble(trackingData.getLevel()));
		activeBts(trackingData.getCellId());
		if(currentLatLang == null){
			currentLatLang = new LatLng(Double.parseDouble(trackingData.getLatitude()), Double.parseDouble(trackingData.getLogitude()));
			listLatLgs.add(currentLatLang);
		}
		else {
			LatLng newLatLng = new LatLng(Double.parseDouble(trackingData.getLatitude()), Double.parseDouble(trackingData.getLogitude()));
			listLatLgs.add(newLatLng);
			if(signalManager.isChange()){
				listLatLgs = new ArrayList<LatLng>();
				listLatLgs.add(currentLatLang);
				listLatLgs.add(newLatLng);
				PolylineOptions polylineOptions = new PolylineOptions();
				polylineOptions.addAll(listLatLgs);
				polylineOptions.color(signalManager.getColor());
				polylineOptions.width(5);
				polyline = googleMap.addPolyline(polylineOptions);
			}
			else {
				listLatLgs.add(currentLatLang);
				polyline.setPoints(listLatLgs);
			}
			currentLatLang = newLatLng;
		}
	}
	
	public void loadFileMyBtsTracker(){
		listFile = FileHandler.getListFileInFolder(demoActivity.getActivity(), FOLDER);
		ArrayList<File> deletedFile = new ArrayList<File>();
		for (File file : listFile){
			if(!file.getName().contains(".TRC")){
				deletedFile.add(file);
			}
		}
		for (File file : deletedFile) {
			listFile.remove(file);
		}
	}
	
	public void loadDataBtsTracker(int index){
		DataSingleton.getInstance(demoActivity.getActivity()).getListTrackingData().clear();
		ArrayList<TrackingData> listTracking = (ArrayList<TrackingData>) FileHandler.loadDataFromExternalFile(demoActivity.getActivity(), FOLDER, listFile.get(index).getName());
		DataSingleton.getInstance(demoActivity.getActivity()).getListTrackingData().clear();
		DataSingleton.getInstance(demoActivity.getActivity()).getListTrackingData().addAll(listTracking);
		Toast.makeText(demoActivity.getActivity(), "Data loaded", Toast.LENGTH_LONG).show();
	}
	
	public ArrayList<File> getListFile() {
		return listFile;
	}

	public void setListFile(ArrayList<File> listFile) {
		this.listFile = listFile;
	}

	public void onSignalStrengthsChanged(double level) {
//		double levelSignal = -113 + 2 * signalStrength.getGsmSignalStrength();
		signalManager.setSignalLevel(level);
	}

	private void activeBts(String cellId){
		ArrayList<Marker> listMarker = demoActivity.listMarker;
		BtsData selectedBtsData = null;
		for (BtsData dataBts : listBtsDatas) {
			int indexCellId = dataBts.getListBtsCellId().indexOf(Integer.parseInt(cellId));
			if(indexCellId != -1){
				selectedBtsData = dataBts;
				break;
			}
		}
		Marker currentMarker = null;
		if(selectedBtsData != null){
			for (Marker marker : listMarker) {
				if(marker.getTitle().equalsIgnoreCase(selectedBtsData.getBtsName())){
					currentMarker = marker;
					currentMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_red));
					break;
				}
			}
		}
		if(currentMarker != null && selectedMarker != null && currentMarker != selectedMarker ){
			selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_black));
		}
	} 
	
	private void measureNearestBts(Location location){
		ArrayList<Marker> listMarker = demoActivity.listMarker;
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
	
	private void findBtsHandler(int cellId, TrackingData trackingData, Location location){
		ArrayList<BtsData> listBtsData = DataSingleton.getInstance(demoActivity.getActivity()).getListBtsDatas();
		Marker nearestMarker = null;
		for (BtsData btsData : listBtsData) {
			ArrayList<Integer> listBtsCellId = btsData.getListBtsCellId();
			if(listBtsCellId.contains(cellId)){
				ArrayList<Marker> listMarker = demoActivity.listMarker;
				for (Marker marker : listMarker) {
					if(marker.getTitle().equalsIgnoreCase(btsData.getBtsName())){
						nearestMarker = marker;
						nearestMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tower_red));
						Location btsLocation  = new Location("location_makerker");
						double distance = btsLocation.distanceTo(location);
						distance = gps2m(btsLocation.getLatitude(), btsLocation.getLongitude(), location.getLatitude(), location.getLongitude());
						BigDecimal bigDecimal = new BigDecimal(distance);
//						trackingData.setJarak(bigDecimal);
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

	private static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
	    float pk = (float) (180/3.14169);

	    double a1 = lat_a / pk;
	    double a2 = lng_a / pk;
	    double b1 = lat_b / pk;
	    double b2 = lng_b / pk;

	    double t1 = (double) (Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2));
	    double t2 = (double) (Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2));
	    double t3 = (double) (Math.sin(a1)*Math.sin(b1));
	    double tt = Math.acos(t1 + t2 + t3);

	    return 6366000*tt;
	}
}
