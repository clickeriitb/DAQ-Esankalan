package com.idl.daq;

import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.AdcProc;
import com.daq.sensors.Sensor;

import expr.Variable;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdcFragment extends Fragment implements OnClickListener {

	private View rootView;
	private EditText sensorName, Quantity, Unit;
	private TextView pinNo;

	private String sensor, pinNum, formulaString, quantity, unit;

	private Boolean err;

	private AdcProc adcSensor;

	private Callbacks adcCallbacks;

	private GlobalState gS;

	private FButton selectPin;

	public interface Callbacks {

		public void openFormula(String s);

		public void openPinSelection();

		public void makeToast(String t);

		public void makeSensor(Sensor a);

		public Context getContext();

		public String getPindata();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.adc_form, container, false);

		defineAttributes();
		selectPin.setOnClickListener(this);
		setHasOptionsMenu(true);

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
		L.d("adc fragment attached!");
		adcCallbacks = (Callbacks) activity;
	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
		sensorName = (EditText) rootView.findViewById(R.id.sensor_name);
		pinNo = (TextView) rootView.findViewById(R.id.pin_no);
		Quantity = (EditText) rootView.findViewById(R.id.quantity_name);
		Unit = (EditText) rootView.findViewById(R.id.unit_adc);
		pinNo.setText(adcCallbacks.getPindata());
		selectPin = (FButton) rootView.findViewById(R.id.pin);
		gS = (GlobalState) adcCallbacks.getContext();
	}

	//
	private void fillForm() {
		// TODO Auto-generated method stub
		err = false;
		sensor = sensorName.getText().toString();
		pinNum = pinNo.getText().toString();

		quantity = Quantity.getText().toString();
		unit = Unit.getText().toString();

		adcSensor = new AdcProc(sensor, quantity, unit, pinNum, gS.getfc());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.adc_form_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.formula_menu:
			quantity = Quantity.getText().toString();
			if (quantity.isEmpty()) {
				adcCallbacks.makeToast("Enter quantity");

			} else {
				L.d("opening formula");
				// name of output parameter is sent as argument
				FormulaContainer fc = gS.getfc();
				Formula f = new Formula(quantity, quantity);
				Variable x = Variable.make(quantity);
				f.addVariable(x);
				fc.put(quantity, f);
				adcCallbacks.openFormula(quantity);
			}

			break;
		case R.id.done_menu:
			fillForm();

			// if any of the fields in form is empty, toast shown
			if (err == true || sensor.isEmpty() || pinNum.isEmpty()
					//|| formulaString.isEmpty()
					) {
				adcCallbacks.makeToast("Empty fields present");
			}

			// else sensor object created
			else {
				adcCallbacks.makeSensor(adcSensor);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// when submit button clicked
		if (v.getId() == R.id.pin) {
			adcCallbacks.openPinSelection();

		}
	}

}
