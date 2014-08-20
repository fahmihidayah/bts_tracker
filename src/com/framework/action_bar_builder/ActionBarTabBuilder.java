package com.framework.action_bar_builder;

import java.util.ArrayList;

import com.framework.adapter.TabsPagerAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LauncherActivity;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;


/*
 * example using:
 * actionBarTabBuilder = new ActionBarTabBuilder(this, R.id.viewPagerTransaksi);
 * actionBarTabBuilder.addNewTabWithDefaultListenner("Pemasukkan", new FragmentPemasukkan());
 * actionBarTabBuilder.addNewTabWithDefaultListenner("Pemasukkan", new FragmentPengeluaran()); 
 * actionBarTabBuilder.createAdapter();
 * 
 * 
 */
public class ActionBarTabBuilder {
	private ActionBar actionBar;
	private FragmentActivity activity;
	private LayoutInflater inflater;
	private ViewPager viewPager;
	private TabsPagerAdapter tabsPagerAdapter;
	private ArrayList<Fragment> listFragment;
	private TabListener defaulTabListener = new TabListener() {
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			viewPager.setCurrentItem(tab.getPosition());
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}
	};
	private ViewPager.OnPageChangeListener defaultOnPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			actionBar.setSelectedNavigationItem(arg0);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	
	public ActionBarTabBuilder(FragmentActivity activity, int viewPagerId) {
		super();
		this.activity = activity;
		actionBar = activity.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		inflater = activity.getLayoutInflater();
		viewPager = (ViewPager) activity.findViewById(viewPagerId);
		listFragment = new ArrayList<Fragment>();
		setDefaultOnPageChangeListener();
	}

	public void setTitile(String title){
		actionBar.setTitle(title);
	}
	
	public void addNewTab(String name, TabListener listener, Fragment fragment){
		actionBar.addTab(actionBar.newTab().setText(name)
				.setTabListener(listener));
		listFragment.add(fragment);
	}
	
	public void addNewTabWithDefaultListenner(String name, Fragment fragment){
		actionBar.addTab(actionBar.newTab().setText(name)
				.setTabListener(defaulTabListener));
		listFragment.add(fragment);
	}
	
	public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener){
		viewPager.setOnPageChangeListener(onPageChangeListener);
	}
	
	public void setDefaultOnPageChangeListener(){
		viewPager.setOnPageChangeListener(defaultOnPageChangeListener);
	}
	
	public void createAdapter(){
		tabsPagerAdapter = new TabsPagerAdapter(activity.getSupportFragmentManager());
		tabsPagerAdapter.setListPageFragment(listFragment);
		viewPager.setAdapter(tabsPagerAdapter);
	}

	public ActionBar getActionBar() {
		return actionBar;
	}

	public void setActionBar(ActionBar actionBar) {
		this.actionBar = actionBar;
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}

	public void setDefaulTabListener(TabListener defaulTabListener) {
		this.defaulTabListener = defaulTabListener;
	}

	public void setDefaultOnPageChangeListener(
			ViewPager.OnPageChangeListener defaultOnPageChangeListener) {
		this.defaultOnPageChangeListener = defaultOnPageChangeListener;
	}
	
//
//	private void initialFragmentPage() {
//		listPageFragment = new ArrayList<Fragment>();
//		BtsTrackingFragment btsTrackingFragment = new BtsTrackingFragment();
//		
//		MapTrackingFragment mapTrackingFragment = new MapTrackingFragment();
//		listPageFragment.add(mapTrackingFragment);
//		listPageFragment.add(btsTrackingFragment);
//	}

//	private void initalActionBar() {
//		
//		TabsPagerAdapter tabPageAdapter = new TabsPagerAdapter(
//				getSupportFragmentManager());
//		tabPageAdapter.setListPageFragment(listPageFragment);
//		viewPager.setAdapter(tabPageAdapter);
//	}
}
