package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daq.db.AdcDbHelper;
import com.daq.db.I2CDbHelper;
import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.I2CProc;
import com.daq.sensors.Sensor;
import com.idl.daq.AdcFragment.Callbacks;

import expr.Variable;
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

public class I2CFragment extends Fragment implements OnClickListener {

	private View rootView;
	private GlobalState gS;
	private EditText sensorName, Quantity, Unit, i2cAddress;
	private FButton pinSelect;
	private TextView sda, scl;
	private Boolean err;
	
	private FormulaContainer tempFc;
	private List<I2C_ItemClass> tempConfig, tempExec;
	
	private Cursor c = null;
	I2CDbHelper mI2cHelper;
	private String sensor, quantity, unit, i2cAddr, sdaVal, sclVal;
	
	private I2CProc tempI2cSensor;

	// I2CDbHelper i2cHelper;
	private Callbacks I2C_Callbacks;


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
		tempConfig = new ArrayList<I2C_ItemClass>();
		tempExec = new ArrayList<I2C_ItemClass>();
		tempI2cSensor.setFc(tempFc);
		tempI2cSensor.setConfigList(tempConfig);
		tempI2cSensor.setExecList(tempExec);
		if (c != null) {
			L.d("in I2C fragment cursor is not null");
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
		L.d("in onAttach i2c fragment attached!");
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
			row_id = c.getLong(c.getColumnIndex(I2CDbHelper.I2C_KEY));
			L.d("auto fill form i2c table row id "+row_id);
			test();
			autoFillConfig(row_id);
			autoFillFormula(row_id);
			autoFillExec(row_id);
			tempFc.logAllFormulas();
		}

	}
	
	public void test(){
		Cursor c = mI2cHelper.getSqlDB().query(I2CDbHelper.I2C_EXEC_TABLE, null,null, null, null, null, null);
		if(c.moveToFirst()){
			do{
			L.d(c.getString(c.getColumnIndex(I2CDbHelper.I2C_EXEC_CMD))+" "+c.getString(c.getColumnIndex(I2CDbHelper.I2C_EXEC_FOREIGN)));
			}while(c.moveToNext());
		}
		Cursor c2 = mI2cHelper.getSqlDB().query(I2CDbHelper.I2C_FORMULA_TABLE_NAME, null,null, null, null, null, null);
		if(c2.moveToFirst()){
			do{
			L.d(c2.getString(c2.getColumnIndex(I2CDbHelper.I2C_FORMULA_NAME))+" "+c2.getString(c2.getColumnIndex(I2CDbHelper.I2C_FORMULA_SENSOR)));
			}while(c2.moveToNext());
		}
		
		
	};

	private void autoFillExec(long row_id) {
		// TODO Auto-generated method stub
//		Cursor c = mI2cHelper.getSqlDB().query(
//				I2CDbHelper.I2C_EXEC_TABLE, null,
//				I2CDbHelper.I2C_EXEC_KEY + "=?",
//				new String[] { row_id + "" }, null, null, null);
		String query = "SELECT * FROM I2C_EXEC WHERE exec_foreign="+row_id;
		Cursor c = mI2cHelper.getSqlDB().rawQuery(query,null);
		L.d("auto fill exec row_id received "+row_id);
		L.d("number of records found "+c.getCount());
		if (c.moveToFirst()) {
			
			I2C_ItemClass obj;
			do {
				String cmd = c.getString(c.getColumnIndex(I2CDbHelper.I2C_EXEC_CMD));
				L.d("in auto fill exec, Exec cmd "+cmd);
				obj = new I2C_ItemClass();
				String[] cmdParts = cmd.split(":");
				if(cmdParts[0].equals("ru") || cmdParts[0].equals("rs")){
					obj.setType("read");
					obj.setAddr(cmdParts[1]);
					obj.setSigned(cmdParts[0].equals("ru") ? false : true);
				}else if(cmdParts[0].equals("w")){
					obj.setType("write");
					obj.setAddr(cmdParts[1]);
					obj.setVal(cmdParts[2]);
				}else{
					obj.setType("delay");
					obj.setDelay(cmdParts[1]);
				}
				tempExec.add(obj);
				L.d("after if exec "+cmd);
//				f = getFormula(c, tempFc);
//				tempFc.put(f.toString(), f);
			} while (c.moveToNext());
		}else{
			L.d("auto fill exec no cursor entry present");
		}
	}

	private void autoFillConfig(long row_id) {
		// TODO Auto-generated method stub
		L.d("in auto fill config row_id received "+row_id);
//		Cursor c = mI2cHelper.getSqlDB().query(
//				I2CDbHelper.I2C_CONFIG_TABLE, null,
//				I2CDbHelper.I2C_CONFIG_KEY + "=?",
//				new String[] { row_id+"" }, null, null, null);
		String query = "SELECT * FROM I2C_CONFIG WHERE config_foreign="+row_id;
		Cursor c = mI2cHelper.getSqlDB().rawQuery(query,null);
		L.d("number of records found "+c.getCount());
		if (c.moveToFirst()) {
			
			I2C_ItemClass obj;
			do {
				String cmd = c.getString(c.getColumnIndex(I2CDbHelper.I2C_CONFIG_CMD));
				obj = new I2C_ItemClass();
				L.d("in auto fill config, config cmd "+cmd);
				String[] cmdParts = cmd.split(":");
				if(cmdParts[0].equals("ru")){
					obj.setType("read");
					obj.setAddr(cmdParts[1]);
				}else if(cmdParts[0].equals("w")){
					obj.setType("write");
					obj.setAddr(cmdParts[1]);
					obj.setVal(cmdParts[2]);
				}else{
					obj.setType("delay");
					obj.setDelay(cmdParts[1]);
				}
				L.d("after if config cmd "+cmd);
//				f = getFormula(c, tempFc);
//				tempFc.put(f.toString(), f);
				tempConfig.add(obj);
			} while (c.moveToNext());
		}
	}

	private void autoFillFormula(long row_id) {
		// TODO Auto-generated method stub
		L.d("in auto fill config row_id received "+row_id);
		
		Cursor c = mI2cHelper.getSqlDB().query(
				I2CDbHelper.I2C_FORMULA_TABLE_NAME, null,
				I2CDbHelper.I2C_FORMULA_SENSOR + "=?",
				new String[] { row_id + "" }, null, null, null);
		L.d("number of records found "+c.getCount());
		if (c.moveToFirst()) {
			
			Formula f;
			do {
				 
				f = getFormula(c, tempFc);
				f.logAllVariables();
				tempFc.put(f.getName(), f);
				L.d("in auto fill formula, formula display name "+f.toString()+" formula actual name "+f.getName());
			} while (c.moveToNext());
		}
	}

	private Formula getFormula(Cursor c, FormulaContainer tempFc) {
		// TODO Auto-generated method stub
		String name = c.getString(c
				.getColumnIndex(I2CDbHelper.I2C_FORMULA_NAME));
		String expression = c.getString(c
				.getColumnIndex(I2CDbHelper.I2C_FORMULA_EXPRESSION));
		String variables = c.getString(c
				.getColumnIndex(I2CDbHelper.I2C_FORMULA_VARIABLES));
		String displayExpression = c.getString(c
				.getColumnIndex(I2CDbHelper.I2C_FORMULA_DISPLAY_EXPRESSION));
		String displayName = c.getString(c
				.getColumnIndex(I2CDbHelper.I2C_FORMULA_DISPLAY_NAME));
		L.d("formula details "+name+" "+expression+" "+variables+" "+displayExpression+" "+displayName);
		Formula f = new Formula(name, expression,displayName, displayExpression);
		if (variables.isEmpty()) {
			Variable x = Variable.make(name);
			L.d("empty formula variables "+name);
		//	f.addVariable(x);
		} else {
			String[] variableList = variables.split(":");
			HashMap<String, Formula> allVars = tempFc.getFc();
			L.d("name of the formula: "+name);
			for(Map.Entry<String, Formula> e : allVars.entrySet()){
				L.d(e.getKey()+" "+e.getValue().toString());
			}
			tempFc.logAllFormulas();
			L.d("length of variableList "+variableList.length);
			for (int i=0;i<variableList.length;++i) {
				Formula temf = allVars.get(variableList[i]);
				L.d("temf "+temf.getName()+" "+temf.getDisplayName());
				f.addToHashMap(variableList[i], allVars.get(variableList[i]));
			}
			
		}
		return f;
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
		mI2cHelper = gS.getI2CDbHelper();
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
			L.d("opening config menu from i2c fragment");
			updateSensor();
			I2C_Callbacks.openConfig();
			break;
		case R.id.done_menu:
			fillForm();
			//if empty
			L.d("updating i2c sensor from i2c fragment");
			updateSensor();
			updateDatabase();
			I2C_Callbacks.makeSensor(tempI2cSensor);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void updateDatabase() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mI2cHelper.getSqlDB();
		long newRowId;
		Cursor c;
		
		String query = "SELECT * FROM " + I2CDbHelper.I2C_TABLE_NAME
				+ " WHERE " + I2CDbHelper.I2C_SENSOR_CODE + " = '"
				+ sensorName.getText().toString() + "' AND "
				+ I2CDbHelper.I2C_QUANTITY + " = '"
				+ Quantity.getText().toString() + "' AND "
				+ I2CDbHelper.I2C_UNIT + " = '" + Unit.getText().toString()
				+ "';";
		
		c = db.rawQuery(query, null);
		
		ContentValues values = new ContentValues();
		values.put(I2CDbHelper.I2C_SENSOR_CODE, sensorName.getText().toString());
		values.put(I2CDbHelper.I2C_QUANTITY, Quantity.getText().toString());
		values.put(I2CDbHelper.I2C_UNIT, Unit.getText().toString());
		values.put(I2CDbHelper.I2C_ADDRESS, i2cAddress.getText().toString());
		values.put(I2CDbHelper.I2C_PIN_SDA, sda.getText().toString());
		values.put(I2CDbHelper.I2C_PIN_SCL, scl.getText().toString());
		
		if(c.moveToFirst()) {
			newRowId = c.getLong(c.getColumnIndex(I2CDbHelper.I2C_KEY));
			L.d("updata sensor new row id "+newRowId);
			db.update(I2CDbHelper.I2C_TABLE_NAME, values, "_id" + "="
					+ newRowId, null);
			L.d("deleting number of formula rows"
					+ db.delete(I2CDbHelper.I2C_FORMULA_TABLE_NAME,
							I2CDbHelper.I2C_FORMULA_SENSOR + "=?",
							new String[] { String.valueOf(newRowId) }) + "");
			L.d("deleting number of config rows"
					+ db.delete(I2CDbHelper.I2C_CONFIG_TABLE,
							I2CDbHelper.I2C_CONFIG_FOREIGN + "=?",
							new String[] { String.valueOf(newRowId) }) + "");
			L.d("deleting number of exec rows"
					+ db.delete(I2CDbHelper.I2C_EXEC_TABLE,
							I2CDbHelper.I2C_EXEC_FOREIGN + "=?",
							new String[] { String.valueOf(newRowId) }) + "");
		}else {
			L.d("new row added");
			newRowId = db.insert(I2CDbHelper.I2C_TABLE_NAME, null, values);
			L.d(newRowId+"");
		}
		updateFormula(newRowId, db);
		updateExec(newRowId,db);
		updateConfig(newRowId,db);
		
	}

	private void updateConfig(long newRowId, SQLiteDatabase db) {
		// TODO Auto-generated method stub
		List<I2C_ItemClass> configList = tempI2cSensor.getConfigList();
		ContentValues values;
		for(I2C_ItemClass item : configList){
			values = new ContentValues();
			L.d("update config info "+item.getInfo());
			values.put(I2CDbHelper.I2C_CONFIG_CMD, item.getInfo());
			L.d("update config rowid "+newRowId);
			values.put(I2CDbHelper.I2C_CONFIG_FOREIGN, newRowId);
			L.d("update config foreign key "+db.insert(I2CDbHelper.I2C_CONFIG_TABLE, null, values));
		}
	}

	private void updateExec(long newRowId, SQLiteDatabase db) {
		// TODO Auto-generated method stub
		List<I2C_ItemClass> execList = tempI2cSensor.getExecList();
		ContentValues values;
		for(I2C_ItemClass item : execList){
			values = new ContentValues();
			L.d("update exec info "+item.getInfo());
			values.put(I2CDbHelper.I2C_EXEC_CMD, item.getInfo());
			L.d("update exec rowId "+newRowId);
			values.put(I2CDbHelper.I2C_EXEC_FOREIGN, newRowId);
			L.d("update exec foreign key "+db.insert(I2CDbHelper.I2C_EXEC_TABLE, null, values));
		}
	}

	private void updateFormula(long newRowId, SQLiteDatabase db) {
		// TODO Auto-generated method stub
		HashMap<String, Formula> fc = tempFc.getFc();
		String name, expression, variables,displayName,displayExpression;
		ContentValues values;
		tempFc.logAllFormulas();
		for (Map.Entry<String, Formula> e : fc.entrySet()) {
			name = e.getValue().getName();
			expression = e.getValue().getExpression();
			displayName = e.getValue().getDisplayName();
			displayExpression = e.getValue().getDisplayExpression();
			variables = "";
			for (Variable var : e.getValue().getAllVariables()) {
				variables += var.toString() + ":";
			}
			L.d("updating formula from i2c fragment name:"+name+" variables:"+variables);
			values = new ContentValues();
			values.put(I2CDbHelper.I2C_FORMULA_NAME, name);
			values.put(I2CDbHelper.I2C_FORMULA_EXPRESSION, expression);
			values.put(I2CDbHelper.I2C_FORMULA_VARIABLES, variables);
			values.put(I2CDbHelper.I2C_FORMULA_SENSOR, newRowId);
			values.put(I2CDbHelper.I2C_FORMULA_DISPLAY_NAME,displayName);
			values.put(I2CDbHelper.I2C_FORMULA_DISPLAY_EXPRESSION,displayExpression);
			L.d("update formula rowId "+db.insert(I2CDbHelper.I2C_FORMULA_TABLE_NAME, null, values));
			
		}
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
