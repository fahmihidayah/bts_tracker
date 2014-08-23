package com.example.mybtstracker;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;

import com.example.beans.DataSingleton;
import com.example.model.BtsData;

import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class SplashScreenActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_activity);
		DataSingleton.getInstance(this).loadAllData(this);
		if(DataSingleton.getInstance(this).getListBtsDatas().size() == 0){
			initialData(this);
		}
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
	public void initialData(Context context){
		BtsData btsData = new BtsData("Belimbing 1", 112.638, -7.9418);
		btsData.getListBtsCellId().add(77148406);
		btsData.getListBtsCellId().add(77151593);
		btsData.getListBtsCellId().add(77147015);
		btsData.getListBtsCellId().add(77147018);
		btsData.getListBtsCellId().add(77147016);
		btsData.getListBtsCellId().add(77147013);
		btsData.getListBtsCellId().add(51543);
		btsData.getListBtsCellId().add(51546);
		btsData.getListBtsCellId().add(77144305);
		btsData.getListBtsCellId().add(77144303);
		btsData.getListBtsCellId().add(77151595);
		btsData.getListBtsCellId().add(77148404);
		
		
		
		DataSingleton.getInstance(context).getListBtsDatas().add(btsData);

		btsData = new BtsData("Lowokwaru", 112.608, -7.93577);
		btsData.getListBtsCellId().add(77146693);
		btsData.getListBtsCellId().add(60943);
		btsData.getListBtsCellId().add(53125);
		btsData.getListBtsCellId().add(51273);
		btsData.getListBtsCellId().add(77146607);
		btsData.getListBtsCellId().add(54745);
		btsData.getListBtsCellId().add(54744);
		btsData.getListBtsCellId().add(54746);
		btsData.getListBtsCellId().add(59941);
		btsData.getListBtsCellId().add(62693);
		btsData.getListBtsCellId().add(62694);
		btsData.getListBtsCellId().add(62696);
		btsData.getListBtsCellId().add(61586);
		btsData.getListBtsCellId().add(77148205);
		btsData.getListBtsCellId().add(77149403);
		btsData.getListBtsCellId().add(77148204);
		btsData.getListBtsCellId().add(77144293);
		btsData.getListBtsCellId().add(77149395);
		
		
		
		
		
		
		DataSingleton.getInstance(context).getListBtsDatas().add(btsData);
		
		btsData = new BtsData("Dau", 112.594, -7.9202);
		btsData.getListBtsCellId().add(61586);
		btsData.getListBtsCellId().add(62345);
		btsData.getListBtsCellId().add(62346);
		btsData.getListBtsCellId().add(29661);
		btsData.getListBtsCellId().add(77148173);
		btsData.getListBtsCellId().add(77148175);
		btsData.getListBtsCellId().add(23441);
		btsData.getListBtsCellId().add(59944);
	
		
	
		
		
		
		DataSingleton.getInstance(context).getListBtsDatas().add(btsData);
		
		btsData = new BtsData("Belimbing 2", 112.64278909, -7.96659348);
		btsData.getListBtsCellId().add(77148287);
		btsData.getListBtsCellId().add(77146635);
		btsData.getListBtsCellId().add(77146638);
	
		btsData.getListBtsCellId().add(77148287);
		btsData.getListBtsCellId().add(77151323);
		btsData.getListBtsCellId().add(77146995);
		btsData.getListBtsCellId().add(77146634);
		btsData.getListBtsCellId().add(77146637);
		btsData.getListBtsCellId().add(77146695);
		btsData.getListBtsCellId().add(77151294);
		btsData.getListBtsCellId().add(77148185);
		btsData.getListBtsCellId().add(77148188);
		btsData.getListBtsCellId().add(77149416);
		btsData.getListBtsCellId().add(52453);
		btsData.getListBtsCellId().add(22899);
		btsData.getListBtsCellId().add(22890);
		
		
		
		
		
		DataSingleton.getInstance(context).getListBtsDatas().add(btsData);
		btsData = new BtsData("Junrejo", 112.57601211, -7.90128214);
		btsData.getListBtsCellId().add(77148465);
		btsData.getListBtsCellId().add(59392);
		btsData.getListBtsCellId().add(77148464);
		
		
		
		
		DataSingleton.getInstance(context).getListBtsDatas().add(btsData);
		
		btsData = new BtsData("Klojen", 112.63282, -7.97152);
		btsData.getListBtsCellId().add(52453);
		btsData.getListBtsCellId().add(56163);
		btsData.getListBtsCellId().add(22904);
		btsData.getListBtsCellId().add(77149413);
		btsData.getListBtsCellId().add(77146817);
		btsData.getListBtsCellId().add(77146814);
		
		
		
		
		
		DataSingleton.getInstance(context).getListBtsDatas().add(btsData);
		DataSingleton.getInstance(context).saveAllData(context);
	}
}