package com.daq.tabsswipe.adapter;

import com.idl.daq.DataFragment;
import com.idl.daq.GraphFragment;
import com.idl.daq.L;
import com.idl.daq.RawFragment;
import com.idl.daq.SensorDetailFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	public GraphFragment graph;
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Games fragment activity
			L.d("graph fragment");
			graph = new GraphFragment(); 
			return graph;
		case 1:
			// Movies fragment activity
			L.d("data fragment");
			return new SensorDetailFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return "Graph";
		case 1:
			// Movies fragment activity
			L.d("data fragment");
			return "Data";
		}
		return super.getPageTitle(position);
	}
	
	

}
