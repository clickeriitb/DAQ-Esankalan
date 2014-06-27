package com.idl.daq;

import java.util.ArrayList;

import com.daq.db.AdcDbHelper;
import com.daq.db.I2CDbHelper;
import com.daq.db.UartDbHelper;
import com.daq.sensors.Sensor;
import com.idl.daq.AdcFragment.Callbacks;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SensorBrowseFragment extends Fragment implements
		OnQueryTextListener, OnItemClickListener {

	ArrayList<String> sensorCodes, sensorQuantities, sensorUnits;
	ListView sensorList;
	SearchView searchV;
	ArrayList<String> sensorDetails;
	ArrayAdapter<String> listAdapter;

	View rootView;
	Callbacks browseSensorCallbacks;

	GlobalState gS;
	String protocol;

	public interface Callbacks {

		public void makeToast(String t);

		public Context getContext();

		public void openProtocol(Cursor c);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView = inflater.inflate(R.layout.fragment_browse_sensor, container,
				false);
		sensorList = (ListView) rootView.findViewById(R.id.list_sensors);

		searchV = (SearchView) rootView.findViewById(R.id.search_sensors);
		SearchManager searchM = (SearchManager) getActivity().getSystemService(
				Context.SEARCH_SERVICE);
		searchV.setSearchableInfo(searchM.getSearchableInfo(getActivity()
				.getComponentName()));

		// searchV.setIconifiedByDefault(false);
		// searchV.setQuery("", false);
		// searchV.clearFocus();

		searchV.setOnQueryTextListener(this);

		gS = (GlobalState) browseSensorCallbacks.getContext();
		protocol = gS.getProtocol();
		if (protocol.equals("ADC")) {
			gS.getAdcDbHelper().loadEntries();
			sensorCodes = gS.getAdcDbHelper().getAdcCodes();
			sensorQuantities = gS.getAdcDbHelper().getAdcQuantities();
			sensorUnits = gS.getAdcDbHelper().getAdcUnits();
		} else if (protocol.equals("UART")) {
			gS.getUartDbHelper().loadEntries();
			sensorCodes = gS.getUartDbHelper().getUartCodes();
			sensorQuantities = gS.getUartDbHelper().getUartQuantities();
			sensorUnits = gS.getUartDbHelper().getUartUnits();
		} else if (protocol.equals("I2C")) {
			gS.getI2CDbHelper().loadEntries();
			sensorCodes = gS.getI2CDbHelper().getI2CCodes();
			sensorQuantities = gS.getI2CDbHelper().getI2CQuantities();
			sensorUnits = gS.getI2CDbHelper().getI2CUnits();
		}
		sensorDetails = new ArrayList<String>();
		for (int i = 0; i < sensorCodes.size(); ++i) {
			sensorDetails.add(sensorCodes.get(i) + " "
					+ sensorQuantities.get(i) + " " + sensorUnits.get(i));
		}

		listAdapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1, sensorDetails);

		sensorList.setAdapter(listAdapter);
		sensorList.setOnItemClickListener(this);
		return rootView;
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
		browseSensorCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		inflater.inflate(R.menu.menu_browse, menu);
		// SearchManager searchManager = (SearchManager)
		// getSystemService(Context.SEARCH_SERVICE);
		// MenuItem m = menu.findItem(R.id.search);
		// SearchView searchView = (SearchView) MenuItemCompat.getActionView(m);
		// //
		// // try {
		// //
		// // SearchView searchView = (SearchView)
		// MenuItemCompat.getActionView(m);
		// // searchView.setSearchableInfo(searchManager
		// // .getSearchableInfo(getComponentName()));
		// // } catch (Exception e) {
		// // // TODO Auto-generated catch block
		// // L.d(e.toString());
		// // e.printStackTrace();
		// // }
		// SearchManager searchManager = (SearchManager)
		// getActivity().getSystemService(Context.SEARCH_SERVICE);
		// // SearchView searchView = (SearchView) menu.findItem(R.id.search)
		// // .getActionView();
		// searchView.setSearchableInfo(searchManager.getSearchableInfo(new
		// ComponentName(getActivity().getApplicationContext(),SensorFormActivity
		// .class)));
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == R.id.action_add) {
			browseSensorCallbacks.openProtocol(null);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		Cursor cr = null;
		if (protocol.equals("ADC")) {
			cr = gS.getAdcDbHelper().getSensorsFor(arg0);
		} else if (protocol.equals("UART")) {
			cr = gS.getUartDbHelper().getSensorsFor(arg0);
		} else if (protocol.equals("I2C")) {
			cr = gS.getI2CDbHelper().getSensorsFor(arg0);
		}
		if (cr == null) {
			L.d("what the hell");
		}
		sensorDetails.clear();
		if (cr.moveToFirst()) {
			L.d("Hello cr");
			if (protocol.equals("ADC")) {
				do {

					String code = cr.getString(cr
							.getColumnIndex(AdcDbHelper.ADC_SENSOR_CODE));
					String quantity = cr.getString(cr
							.getColumnIndex(AdcDbHelper.ADC_QUANTITY));
					String unit = cr.getString(cr
							.getColumnIndex(AdcDbHelper.ADC_UNIT));
					sensorDetails.add(code + " " + quantity + " " + unit);
				} while (cr.moveToNext());
			} else if (protocol.equals("UART")) {
				do {

					String code = cr.getString(cr
							.getColumnIndex(UartDbHelper.UART_SENSOR_CODE));
					String quantity = cr.getString(cr
							.getColumnIndex(UartDbHelper.UART_QUANTITY));
					String unit = cr.getString(cr
							.getColumnIndex(UartDbHelper.UART_UNIT));
					sensorDetails.add(code + " " + quantity + " " + unit);
				} while (cr.moveToNext());
			} else if (protocol.equals("I2C")) {
				do {

					String code = cr.getString(cr
							.getColumnIndex(I2CDbHelper.I2C_SENSOR_CODE));
					String quantity = cr.getString(cr
							.getColumnIndex(I2CDbHelper.I2C_QUANTITY));
					String unit = cr.getString(cr
							.getColumnIndex(I2CDbHelper.I2C_UNIT));
					sensorDetails.add(code + " " + quantity + " " + unit);
				} while (cr.moveToNext());
			}
		}
		if (sensorDetails.size() == 0) {
			listAdapter.add("No such sensor found");
		}
		listAdapter.notifyDataSetChanged();
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String sensorInfo = sensorDetails.get(position);
		String[] args = sensorInfo.split(" ");
		Cursor c;
		SQLiteDatabase db = null;
		String query = null;

		if (protocol.equals("ADC")) {
			db = gS.getAdcDbHelper().getSqlDB();
			query = "SELECT * FROM " + AdcDbHelper.ADC_TABLE_NAME + " WHERE "
					+ AdcDbHelper.ADC_SENSOR_CODE + " = '" + args[0] + "' AND "
					+ AdcDbHelper.ADC_QUANTITY + " = '" + args[1] + "' AND "
					+ AdcDbHelper.ADC_UNIT + " = '" + args[2] + "';";
		} else if (protocol.equals("UART")) {
			db = gS.getUartDbHelper().getSqlDB();
			query = "SELECT * FROM " + UartDbHelper.UART_TABLE_NAME + " WHERE "
					+ UartDbHelper.UART_SENSOR_CODE + " = '" + args[0]
					+ "' AND " + UartDbHelper.UART_QUANTITY + " = '" + args[1]
					+ "' AND " + UartDbHelper.UART_UNIT + " = '" + args[2]
					+ "';";
		} else if (protocol.equals("I2C")) {
			db = gS.getI2CDbHelper().getSqlDB();
			query = "SELECT * FROM " + I2CDbHelper.I2C_TABLE_NAME + " WHERE "
					+ I2CDbHelper.I2C_SENSOR_CODE + " = '" + args[0] + "' AND "
					+ I2CDbHelper.I2C_QUANTITY + " = '" + args[1] + "' AND "
					+ I2CDbHelper.I2C_UNIT + " = '" + args[2] + "';";
		}
		L.d(query);
		c = db.rawQuery(query, null);
		L.d(c.toString());
		L.d(c.getCount());
		browseSensorCallbacks.openProtocol(c);
	}

}
