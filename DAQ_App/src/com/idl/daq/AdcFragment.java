package com.idl.daq;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.daq.db.AdcDbHelper;
import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.AdcProc;
import com.daq.sensors.Sensor;

import expr.Variable;

public class AdcFragment extends Fragment implements OnClickListener {

	private View rootView;
	private EditText sensorName, Quantity, Unit;
	private TextView pinNo;

	private String sensor, pinNum, formulaString, quantity, unit;

	private Boolean err;

	private AdcProc adcSensor;

	private Callbacks adcCallbacks;

	private GlobalState gS;
	
	private Cursor c = null;
	AdcDbHelper mAdcHelper;

	private FButton selectPin;

	public interface Callbacks {

		public void openFormula(String s);

		public void openPinSelection();

		public void makeToast(String t);

		public void makeSensor(Sensor a);

		public Context getContext();

		public String getPindata();
		
		public Cursor getCursor();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.adc_form, container, false);

		defineAttributes();
		selectPin.setOnClickListener(this);
		setHasOptionsMenu(true);

		if (c != null) {
			autoFillForm();
		}
		
		return rootView;

	}
	
	private void autoFillForm() {
		// TODO Auto-generated method stub
		long row_id;
		if (c.moveToFirst()) {
			sensorName.setText(c.getString(c
					.getColumnIndex(AdcDbHelper.ADC_SENSOR_CODE)));
			Quantity.setText(c.getString(c
					.getColumnIndex(AdcDbHelper.ADC_QUANTITY)));
			Unit.setText(c.getString(c.getColumnIndex(AdcDbHelper.ADC_UNIT)));
			pinNo.setText(c.getString(c
					.getColumnIndex(AdcDbHelper.ADC_PIN_NUMBER)) + "");
			row_id = c.getLong(c.getColumnIndex(AdcDbHelper.ADC_KEY));
			L.d(row_id + "");
			autoFillFormula(row_id);
		}
	}
	
	private void autoFillFormula(long row_id) {
		// TODO Auto-generated method stub
		Cursor c = mAdcHelper.getSqlDB().query(
				AdcDbHelper.ADC_FORMULA_TABLE_NAME, null,
				AdcDbHelper.ADC_FORMULA_SENSOR + "=?",
				new String[] { row_id + "" }, null, null, null);
		if (c.moveToFirst()) {
			gS.initializeFc();
			FormulaContainer tempFc = gS.getfc();
			Formula f;
			do {
				f = getFormula(c, tempFc);
				tempFc.put(f.toString(), f);
			} while (c.moveToNext());
		}
	}
	
	private Formula getFormula(Cursor c, FormulaContainer tempFc) {
		// TODO Auto-generated method stub
		String name = c.getString(c
				.getColumnIndex(AdcDbHelper.ADC_FORMULA_NAME));
		String expression = c.getString(c
				.getColumnIndex(AdcDbHelper.ADC_FORMULA_EXPRESSION));
		String variables = c.getString(c
				.getColumnIndex(AdcDbHelper.ADC_FORMULA_VARIABLES));
		Formula f = new Formula(name, expression);
		if (variables.isEmpty()) {
			Variable x = Variable.make(name);
			f.addVariable(x);
		} else {
			String[] variableList = variables.split(":");
			HashMap<String, Formula> allVars = tempFc.getFc();
			L.d("name of the formula: "+name);
			for(Map.Entry<String, Formula> e : allVars.entrySet()){
				L.d(e.getKey()+" "+e.getValue().toString());
			}
			for (int i=0;i<variableList.length-1;++i) {
				f.addToHashMap(variableList[i], allVars.get(variableList[i]));
			}
		}
		return f;
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
		c = adcCallbacks.getCursor();
		mAdcHelper = gS.getAdcDbHelper();
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
				updateDatabase();
				adcCallbacks.makeSensor(adcSensor);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void updateDatabase() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mAdcHelper.getSqlDB();
		long newRowId;
		Cursor c;

		String query = "SELECT * FROM " + AdcDbHelper.ADC_TABLE_NAME
				+ " WHERE " + AdcDbHelper.ADC_SENSOR_CODE + " = '"
				+ sensorName.getText().toString() + "' AND "
				+ AdcDbHelper.ADC_QUANTITY + " = '"
				+ Quantity.getText().toString() + "' AND "
				+ AdcDbHelper.ADC_UNIT + " = '" + Unit.getText().toString()
				+ "';";
		// c = mAdcHelper.getSqlDB().query(
		// AdcDbHelper.ADC_TABLE_NAME,
		// null,
		// AdcDbHelper.ADC_SENSOR_CODE + "=? AND "
		// + AdcDbHelper.ADC_QUANTITY + "=? AND " + AdcDbHelper.ADC_UNIT +
		// "=? ",
		// new String[] { sensorName.getText().toString(),
		// Quantity.getText().toString(), Unit.getText().toString() }, null,
		// null, null);
		// L.d(query);
		c = db.rawQuery(query, null);
		L.d(" c move to first " + c.moveToFirst());

		ContentValues values = new ContentValues();
		values.put(AdcDbHelper.ADC_SENSOR_CODE, sensorName.getText().toString());
		values.put(AdcDbHelper.ADC_QUANTITY, Quantity.getText().toString());
		values.put(AdcDbHelper.ADC_UNIT, Unit.getText().toString());
		values.put(AdcDbHelper.ADC_PIN_NUMBER, pinNo.getText().toString());

		if (c.moveToFirst()) {
			newRowId = c.getLong(c.getColumnIndex(AdcDbHelper.ADC_KEY));
			db.update(AdcDbHelper.ADC_TABLE_NAME, values, "_id" + "="
					+ newRowId, null);
			L.d("deleting "
					+ db.delete(AdcDbHelper.ADC_FORMULA_TABLE_NAME,
							AdcDbHelper.ADC_FORMULA_SENSOR + "=?",
							new String[] { String.valueOf(newRowId) }) + "");

		} else {
			newRowId = db.insert(AdcDbHelper.ADC_TABLE_NAME, null, values);
		}
		updateFormula(newRowId, db);
	}

	private void updateFormula(long newRowId, SQLiteDatabase db) {
		// TODO Auto-generated method stub

		L.d(gS.getfc()+"");
		HashMap<String, Formula> fc = gS.getfc().getFc();
		String name, expression, variables;
		ContentValues values;
		for (Map.Entry<String, Formula> e : fc.entrySet()) {
			name = e.getValue().toString();
			expression = e.getValue().getExpression();
			variables = "";
			for (Variable var : e.getValue().getAllVariables()) {
				variables += var.toString() + ":";
			}
			values = new ContentValues();
			values.put(AdcDbHelper.ADC_FORMULA_NAME, name);
			values.put(AdcDbHelper.ADC_FORMULA_EXPRESSION, expression);
			values.put(AdcDbHelper.ADC_FORMULA_VARIABLES, variables);
			values.put(AdcDbHelper.ADC_FORMULA_SENSOR, newRowId);
			db.insert(AdcDbHelper.ADC_FORMULA_TABLE_NAME, null, values);
		}
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
