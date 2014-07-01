package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

import com.daq.formula.Formula;
import com.daq.sensors.I2CProc;
import com.daq.sensors.OthersProc;
import com.daq.sensors.Sensor;
import com.daq.tabsswipe.adapter.TabsPagerAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

public class DetailsFrag extends Fragment implements ActionBar.TabListener,
		LoaderCallbacks<Void> {

	private View rootView;
	private TabHost tabHost;
	private ViewPager viewPager;
	// to take the tabs out of the action bar
	private PagerTabStrip strip;
	private ActionBar actionBar;
	private TabsPagerAdapter mAdapter;
	private String[] tabs = { "Graphical", "Tabular" };
	// recieves sensor object from SensorListActivity in the form of the tabId
	private Sensor mySensor;

	private Context c;
	private GlobalState gS;

	// HashMap<String, Boolean> checkEmptyRegisters;

	// needed for display
	private ArrayList<String> data, time, info;
	private ArrayAdapter<String> infoAdapter;

	int count;
	// why
	static int countFrag = 0;

	ArrayList<JSONObject> t;

	// a new series is being built for each DetailsFrag
	public GraphViewSeries series;
	private double TIME = 0;

	Callbacks sensorInfoCallbacks;

	public interface Callbacks {

		public ArrayAdapter<String> getArrayAdapter();

		public ArrayList<String> getArrayList();

		public GraphViewSeries getSeries();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		setRetainInstance(true);
		sensorInfoCallbacks = (Callbacks) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TIME = 0;
		L.d("on create view called");
		countFrag++;
		rootView = inflater.inflate(R.layout.fragment_sensor_detail, container,
				false);
		viewPager = (ViewPager) rootView.findViewById(R.id.pager);
		strip = (PagerTabStrip) rootView.findViewById(R.id.pager_tabs);

		actionBar = getActivity().getActionBar();
		L.d("action bar selected");
		mAdapter = new TabsPagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(mAdapter);
		L.d("view pager set");
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// setupTabs();
		L.d("setup tabs");

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				// actionBar.setSelectedNavigationItem(position);
				L.d("action bar position: " + position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		data = new ArrayList<String>();
		time = new ArrayList<String>();
		count = 0;
		// load the asyc task
		getLoaderManager().initLoader(0, null, this);

		// checkEmptyRegisters = new HashMap<String, Boolean>();
		// initializeInputs();

		return rootView;
	}

	// private void initializeInputs() {
	// // TODO Auto-generated method stub
	// if (mySensor instanceof I2CProc) {
	// for (I2C_ItemClass i : ((I2CProc) mySensor).getExecList()) {
	// checkEmptyRegisters.put(i.getAddr(), true);
	// }
	// } else {
	// checkEmptyRegisters.put("pin", true);
	// }
	// }

	private void setupTabs() {
		// TODO Auto-generated method stub
		for (String tab : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab)
					.setTabListener(this));
		}

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		L.d("tab position: " + tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// viewPager.setCurrentItem(tab.getPosition());
		// L.d("tab position: "+tab.getPosition());
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void setSensor(Sensor sensor) {
		// TODO Auto-generated method stub
		mySensor = sensor;
	}

	public void setContext(Context c) {
		// TODO Auto-generated method stub
		this.c = c;
		gS = (GlobalState) c;
	}

	// @Override
	// public Context getContext() {
	// // TODO Auto-generated method stub
	// return c;
	// }
	//
	// @Override
	// public Sensor getSensor() {
	// // TODO Auto-generated method stub
	// return mySensor;
	// }

	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(getActivity()) {

			@Override
			public Void loadInBackground() {
				try {
					// simulate some time consuming operation going on in the
					// background
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		// somehow the AsyncTaskLoader doesn't want to start its job without
		// calling this method
		loader.forceLoad();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
		// TODO Auto-generated method stub
		loadData();
		getLoaderManager().restartLoader(0, null, this);
	}

	private void loadData() {
		// TODO Auto-generated method stub
		// it receives the new data from SensorListActivity
		series = mAdapter.graph.series;
		info = mAdapter.detail.data;
		infoAdapter = mAdapter.detail.a;
		L.d("received adapters");
		L.d("Loading data");
		L.d("Received series");
		t = gS.getTemp();
		for (int i = data.size(); i < t.size(); ++i) {
			L.d("value of i " + i);
			try {
				if (t.get(i).get("sensor_code")
						.equals(mySensor.getSensorName())) {
					// for(Map.Entry<String, Formula> e :
					// mySensor.getFormulaContainer().getFc().entrySet()){
					// L.d(e.getKey()+" "+e.getValue());
					// }
					String date = t.get(i).getString("date");
					time.add(date);
					data.add(date);
					String sensorData = "";
					if (mySensor.getFormulaContainer() != null) {
						Log.e("formula","other formula not null");
						Formula f;
						if (mySensor instanceof I2CProc) {
							String dataList[] = t.get(i).getString("data")
									.split(";");
							for (String dataElement : dataList) {
								String dataVals[] = dataElement.split(":");
								String register = dataVals[0];
								double val = Double.parseDouble(dataVals[1]);
								f = null;
								for (Map.Entry<String, Formula> e : mySensor
										.getFormulaContainer().getFc()
										.entrySet()) {
									if (e.getValue().getDisplayName()
											.equals(register)) {
										f = e.getValue();
										break;
									}
								}
								f.setValue(val);
							}

							// checkEmptyRegisters.put(register, false);
						} else {
							String dataVals[] = t.get(i).getString("data")
									.split(";");
							String dataList[] = dataVals[0].split(":");
							double val = Double.parseDouble(dataList[1]);
							f = mySensor.getFormulaContainer().getFc()
									.get("pin");
							f.setValue(val);
						}
						// When formula evaluation takes place
						mySensor.getFormulaContainer().evaluate();
						L.d("leaving formula container");
						mySensor.getFormulaContainer().logAllFormulas();
						String s = "";
						for (Map.Entry<String, Formula> e : mySensor
								.getFormulaContainer().getFc().entrySet()) {
							s = e.getValue().getValue() + "";
						}
						sensorData = s;
						L.d("value after formula evaluation " + sensorData);
						displayRawData(sensorData, date);
						displayGraphData(sensorData);
					} else {
						if (mySensor instanceof OthersProc) {
							sensorData = t.get(i).getString("data");
						} else {
							String dataVals[] = t.get(i).getString("data")
									.split(";");
							String dataList[] = dataVals[0].split(":");
							String value = dataList[1];
							sensorData = value;
							L.d("rawdata " + sensorData);
						}
						displayRawData(sensorData, date);
					}
					L.d(sensorData + " " + date);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void displayGraphData(String sensorData) {
		// TODO Auto-generated method stub
		final double graphVal = Double.parseDouble(sensorData);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// keep appending the particular list
				JSONObject obj = mySensor.getJSON();
				try {
					// TODO change hardcoded value
					series.appendData(new GraphView.GraphViewData(TIME
							+ (float) (1000), graphVal), true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					series.appendData(new GraphView.GraphViewData(TIME++,
							graphVal), true);
					e.printStackTrace();
				}
				// Here I try few different things

			}
		});
	}

	private void displayRawData(String sensorData, String date) {
		// TODO Auto-generated method stub
		if (info == null && infoAdapter == null) {
			L.d("info or infoAdapter null hain");
		}
		if (series == null) {
			L.d("Series null");
		}
		// notify the SensorList activity that a value has been recieved
		info.add(mySensor.getId() + ":" + sensorData + " Time:" + date);
		infoAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		L.d("on Destroy of Deatilsfarag caleedf");
		mAdapter = null;
		super.onDestroy();
	}

	// @Override
	// public void sendArrayWithAdapter(ArrayAdapter<String> a,ArrayList<String>
	// data) {
	// // TODO Auto-generated method stub
	// detailAdapter = a;
	// info = data;
	// }

}
