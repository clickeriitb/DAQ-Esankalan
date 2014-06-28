package com.idl.daq;

import com.daq.db.AdcDbHelper;
import com.daq.db.I2CDbHelper;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.I2CProc;
import com.daq.sensors.Sensor;
import com.idl.daq.AdcFragment.Callbacks;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class I2CFragment extends Fragment implements OnClickListener {

	private View rootView;
	private GlobalState gS;
	private EditText sensorName, Quantity, Unit, i2cAddress;
	private FButton pinSelect;
	private TextView sda, scl;
	private Boolean err;
	
	private FormulaContainer tempFc;
	
	private Cursor c = null;
	private String sensor, quantity, unit, i2cAddr, sdaVal, sclVal;
	
	private I2CProc tempI2cSensor;

	// I2CDbHelper i2cHelper;
	private Callbacks I2C_Callbacks;

	private I2CDbHelper i2cHelper;

	public interface Callbacks {

		public void openFormula(String s);

		public void openPinSelection();

		public void makeToast(String t);

		public void makeSensor(Sensor a);

		public Context getContext();

		public String getPindata();

		public Cursor getCursor();
		
		public void openConfig();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.i2c_form, container, false);
		defineAttributes();
		setHasOptionsMenu(true);
		
		tempFc = new FormulaContainer();
		if (c != null) {
			autoFillForm();
		}
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		setRetainInstance(true);
		L.d("i2c fragment attached!");
		I2C_Callbacks = (Callbacks) activity;
	}

	private void autoFillForm() {
		// TODO Auto-generated method stub
		long row_id;
		if (c.moveToFirst()) {
			sensorName.setText(c.getString(c
					.getColumnIndex(I2CDbHelper.I2C_SENSOR_CODE)));
			Quantity.setText(c.getString(c
					.getColumnIndex(I2CDbHelper.I2C_QUANTITY)));
			Unit.setText(c.getString(c.getColumnIndex(I2CDbHelper.I2C_UNIT)));
			i2cAddress.setText(c.getString(c
					.getColumnIndex(I2CDbHelper.I2C_UNIT)));
			sda.setText(c.getString(c.getColumnIndex(I2CDbHelper.I2C_PIN_SDA)));
			scl.setText(c.getString(c.getColumnIndex(I2CDbHelper.I2C_PIN_SCL)));

		}

	}

	private void defineAttributes() {
		// TODO Auto-generated method stub

		sensorName = (EditText) rootView.findViewById(R.id.sensor_code_i2c);
		Quantity = (EditText) rootView.findViewById(R.id.quantity_i2c);
		Unit = (EditText) rootView.findViewById(R.id.unit_i2c);
		i2cAddress = (EditText) rootView.findViewById(R.id.i2c_addr);
		pinSelect = (FButton) rootView.findViewById(R.id.pin_i2c);
		sda = (TextView) rootView.findViewById(R.id.sda);
		scl = (TextView) rootView.findViewById(R.id.scl);
		pinSelect.setOnClickListener(this);
		gS = (GlobalState) I2C_Callbacks.getContext();
		c = I2C_Callbacks.getCursor();
		i2cHelper = gS.getI2CDbHelper();
		gS.initializeSensor();
		tempI2cSensor = (I2CProc) gS.getSensor();

	}

	private void fillForm() {
		// TODO Auto-generated method stub
		sensor = sensorName.getText().toString();
		i2cAddr = i2cAddress.getText().toString();
		quantity = Quantity.getText().toString();
		unit = Unit.getText().toString();
		sdaVal = sda.getText().toString();
		sclVal = scl.getText().toString();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.i2c_form_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.cmd_menu:
			updateSensor();
			I2C_Callbacks.openConfig();
			break;
		case R.id.done_menu:
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void updateSensor() {
		// TODO Auto-generated method stub
		fillForm();
		tempI2cSensor.setSensorName(sensor);
		tempI2cSensor.setQuantity(quantity);
		tempI2cSensor.setUnit(unit);
		tempI2cSensor.setI2cAddress(i2cAddr);
		tempI2cSensor.setScl(sclVal);
		tempI2cSensor.setSda(sdaVal);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.pin_i2c:
				I2C_Callbacks.openPinSelection();
			break;
		}
	}

}
