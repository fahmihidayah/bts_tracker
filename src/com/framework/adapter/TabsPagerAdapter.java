package com.framework.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> listPageFragment = new ArrayList<Fragment>();
	
	public ArrayList<Fragment> getListPageFragment() {
		return listPageFragment;
	}

	public void setListPageFragment(ArrayList<Fragment> listPageFragment) {
		this.listPageFragment = listPageFragment;
	}

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		return listPageFragment.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPageFragment.size();
	}

}
