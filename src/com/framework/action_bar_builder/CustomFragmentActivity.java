package com.framework.action_bar_builder;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/*
 * example using :
 *initialActionBarTabBuiler(R.id.viewPagerTransaksi);
 *actionBarTabBuilder.addNewTab("Pemasukkan", this, new ExampleFragment());
 *actionBarTabBuilder.addNewTab("Pengeluaran", this, new ExampleFragment());
 *actionBarTabBuilder.createAdapter();
 */

public class CustomFragmentActivity extends FragmentActivity implements TabListener, ViewPager.OnPageChangeListener {
	
	protected ActionBarTabBuilder actionBarTabBuilder;
	
	public void initialActionBarTabBuiler(int viewPagerId){
		actionBarTabBuilder = new ActionBarTabBuilder(this, viewPagerId);
		actionBarTabBuilder.setOnPageChangeListener(this);
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		actionBarTabBuilder.getViewPager().setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public void onPageSelected(int arg0) {
		actionBarTabBuilder.getActionBar().setSelectedNavigationItem(arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

}
