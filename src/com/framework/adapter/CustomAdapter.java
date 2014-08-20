package com.framework.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class CustomAdapter<T> extends ArrayAdapter<T>{

	private Context context;
	private int resourceId;
	protected ArrayList<T> listData;
	
	public CustomAdapter(Context context, int textViewResourceId, ArrayList<T> listData) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourceId = textViewResourceId;
		this.listData = listData;
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}
	
	public LayoutInflater getLayoutInflater(){
		 return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = getLayoutInflater();
		View customView = inflater.inflate(resourceId, null);
		setViewItems(customView, position);
		return customView;
	}
	
	public abstract void setViewItems(View view, int position);
}
