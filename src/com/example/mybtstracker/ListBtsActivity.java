package com.example.mybtstracker;


import com.example.beans.DataSingleton;
import com.example.beans.ListBtsBeans;
import com.example.model.BtsData;
import com.example.mybtstracker.MapTrackingFragment.ConfirmDialog;
import com.framework.adapter.CustomAdapter;
import com.framework.commonUtilities.CommonUtilities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;

@SuppressLint("NewApi")
public class ListBtsActivity extends Activity {
	
	public ListView listViewBts;
	public CustomAdapter<BtsData> customAdapter;
	private ListBtsBeans listBtsBeans;
	
	private void initialComponent(){
		listBtsBeans = new ListBtsBeans(this);
		listViewBts = (ListView) findViewById(R.id.listViewBts);
		customAdapter = new CustomAdapter<BtsData>(this,R.layout.layout_bts_item , listBtsBeans.getListDataBts()) {
			
			@Override
			public void setViewItems(View view, int position) {
				BtsData btsData = listData.get(position);
				CommonUtilities.setTextToView(view, R.id.textViewNamaBts, btsData.getBtsName());
				CommonUtilities.setTextToView(view, R.id.textViewLatitudeLongitudeBts, 
						"Longitude : " + btsData.getLongitude() + ", Latitude : " + btsData.getLatitude()
						);
			}
		};
		listViewBts.setAdapter(customAdapter);
		listViewBts.setOnItemClickListener(new  AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ChooseDialog chooseDialog = new ChooseDialog();
				chooseDialog.setSelectedIndex(arg2);
				chooseDialog.show(getFragmentManager(), "choose_dialog");
			}
		});
	}
	
	public void onClickTambahBts(View view){
		startActivity(new Intent(this, AddBtsActivity.class));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_bts_activity);
		initialComponent();
	}
	

	@SuppressLint({ "ValidFragment", "NewApi" })
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
								Intent intent = new Intent(getActivity(), AddBtsActivity.class);
								intent.putExtra("isEdit", true);
								intent.putExtra("index", selectedIndex);
								startActivity(intent);
								break;
							case 1:
								ConfirmDialog confirmDialog = new ConfirmDialog();
								confirmDialog.setSelectedIndex(selectedIndex);
								confirmDialog.show(getFragmentManager(), "confirm_dialog");
								break;
							default:
								break;
							}
						}
					});
			return builder.create();
		}
	}
	
	@SuppressLint({ "ValidFragment", "NewApi" })
	public class ConfirmDialog extends DialogFragment {
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
			builder.setTitle("Pilih Aksi");
			builder.setMessage("Apakah anda ingin menghapus bts ini?");
			builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DataSingleton.getInstance(getActivity()).getListBtsDatas().remove(selectedIndex);
					DataSingleton.getInstance(getActivity()).saveAllData(getActivity());
					Toast.makeText(getActivity(), "Data dihapus", Toast.LENGTH_LONG).show();
					customAdapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			return builder.create();
		}
	}
	
	@Override
	protected void onResume() {
		customAdapter.notifyDataSetChanged();
		super.onResume();
	}
}