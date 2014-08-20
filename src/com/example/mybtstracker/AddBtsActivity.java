package com.example.mybtstracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.beans.AddBtsBeans;
import com.example.beans.DataSingleton;
import com.example.beans.MapTrackingBeans;
import com.example.model.Constants;
import com.example.model.TrackingData;
import com.example.mybtstracker.MapTrackingFragment.ConfirmDialog;
import com.framework.action_bar_builder.ActionBarTabBuilder;
import com.framework.adapter.CustomAdapter;
import com.framework.adapter.TabsPagerAdapter;
import com.framework.commonUtilities.CommonUtilities;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;
import com.google.android.gms.maps.GoogleMap;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class AddBtsActivity extends Activity {

	public EditText editTextName;
	public EditText editTextKeterangan;
	public EditText editTextLongitude;
	public EditText editTextLatitude;
	public ListView listViewCellId;
	public CustomAdapter<Integer> customAdapter;
	public AddBtsBeans addBtsBeans;
	private void initialComponent(){
		addBtsBeans = new AddBtsBeans(this);
		editTextKeterangan = (EditText) findViewById(R.id.editTextKeterangan);
		editTextName = (EditText) findViewById(R.id.editTextBtsName);
		editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
		editTextLongitude = (EditText)findViewById(R.id.editTextLongitude);
		listViewCellId = (ListView) findViewById(R.id.listViewCellId);
		customAdapter = new CustomAdapter<Integer>(this,R.layout.layout_cell_item, addBtsBeans.getBtsData().getListBtsCellId()) {
			
			@Override
			public void setViewItems(View view, int position) {
				Integer data = listData.get(position);
				CommonUtilities.setTextToView(view, R.id.textViewCellId, "Cell id : " + data);
			}
		};
		listViewCellId.setAdapter(customAdapter);
		listViewCellId.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ChooseDialog chooseDialog = new ChooseDialog();
				chooseDialog.setSelectedIndex(arg2);
				chooseDialog.show(getFragmentManager(), "choose_dialog");
				}
		});
		addBtsBeans.setBtsToView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_bts);
		initialComponent();
	}
	
	public void onClickAdd(View view){
		addBtsBeans.getBtsData().setBtsName(editTextName.getText().toString());
		addBtsBeans.getBtsData().setKeterangan(editTextKeterangan.getText().toString());
		addBtsBeans.getBtsData().setLatitude(Double.parseDouble(editTextLatitude.getText().toString()));
		addBtsBeans.getBtsData().setLongitude(Double.parseDouble(editTextLongitude.getText().toString()));
		addBtsBeans.saveData();
		
	}
	
	public void onClickAddCellId(View view){
		CellIdDialog cellIdDialog = new CellIdDialog();
		cellIdDialog.setIndex(-1);
		cellIdDialog.show(getFragmentManager(), "cell_id_dialog");
	}
	
	@SuppressLint("ValidFragment")
	public class ChooseDialog extends DialogFragment {
		private int selectedIndex;

		public int getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Pilih Aksi").setItems(R.array.list_action_cell_id,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								CellIdDialog cellIdDialog = new CellIdDialog();
								cellIdDialog.setIndex(selectedIndex);
								cellIdDialog.show(getFragmentManager(), "cell_id_dialog");
								break;
							case 1:
								addBtsBeans.deleteCellId(selectedIndex);
								customAdapter.notifyDataSetChanged();
								break;
							default:
								break;
							}
						}
					});
			return builder.create();
		}
	}
	
	public class CellIdDialog extends DialogFragment {
		private int index;
		private View rootView;
		private EditText editTextCellId;
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			LayoutInflater layoutInflater = getActivity().getLayoutInflater();
			rootView = layoutInflater.inflate(
					R.layout.layout_edit_cell_item, null);
			// initial
			editTextCellId = (EditText) rootView.findViewById(R.id.editTextCellId);
			
			if(index != -1){
				editTextCellId.setText(addBtsBeans.getBtsData().getListBtsCellId().get(index) + "");
			}
			builder.setView(rootView);
			builder.setTitle("Cell Id");
			builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(index != -1){
						addBtsBeans.editCellId(index, editTextCellId.getText().toString());
						customAdapter.notifyDataSetChanged();
						return;
					}
					addBtsBeans.addCellId(editTextCellId.getText().toString());
					customAdapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "Batal", Toast.LENGTH_LONG).show();
				}
			});
			return builder.create();
		}
	}
	
}
