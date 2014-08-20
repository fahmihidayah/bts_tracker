package com.example.beans;

import android.widget.EditText;
import android.widget.Toast;

import com.example.model.BtsData;
import com.example.mybtstracker.AddBtsActivity;

public class AddBtsBeans {
	private AddBtsActivity addBtsActivity;
	private BtsData btsData = new BtsData("", 0, 0);
	private boolean isEdit;
	public AddBtsBeans(AddBtsActivity addBtsActivity) {
		super();
		this.addBtsActivity = addBtsActivity;
		isEdit = addBtsActivity.getIntent().getBooleanExtra("isEdit", false);
		int indexEdit = addBtsActivity.getIntent().getIntExtra("index", 0);
		if(isEdit){
			btsData = DataSingleton.getInstance(addBtsActivity).getListBtsDatas().get(indexEdit);
			
		}
	}
	
	public BtsData getBtsData() {
		return btsData;
	}
	
	public void addCellId(String cellIdStr){
		Integer cellId = new Integer(cellIdStr);
		btsData.getListBtsCellId().add(cellId);
	}
	
	public void editCellId(int index,String cellIdStr){
		try{
			Integer cellId = new Integer(cellIdStr);
			btsData.getListBtsCellId().set(index, cellId);
		}
		catch(IndexOutOfBoundsException ex){
			
		}
		catch(NumberFormatException ex){
			Toast.makeText(addBtsActivity, "Format angka salah", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void deleteCellId(int index){
		btsData.getListBtsCellId().remove(index);
	}
	
	public void saveData(){
		if(!isEdit){
			Toast.makeText(addBtsActivity, "Success Add Data", Toast.LENGTH_LONG).show();
			DataSingleton.getInstance(addBtsActivity).getListBtsDatas().add(btsData);
		}
		saveDataBts();
	}
	
	public void saveDataBts(){
		DataSingleton.getInstance(addBtsActivity).saveAllData(addBtsActivity);
	}
	
	public void setBtsToView(){
		addBtsActivity.editTextKeterangan.setText(btsData.getKeterangan());
		addBtsActivity.editTextLatitude.setText(btsData.getLatitude()+"");
		addBtsActivity.editTextLongitude.setText(btsData.getLongitude() + "");
		addBtsActivity.editTextName.setText(btsData.getBtsName());
		addBtsActivity.customAdapter.notifyDataSetChanged();
	}
	 
}
