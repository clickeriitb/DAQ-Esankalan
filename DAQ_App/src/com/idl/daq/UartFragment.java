package com.idl.daq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

import com.daq.db.UartDbHelper;
import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.UartProc;

import expr.Variable;

public class UartFragment extends Fragment implements OnClickListener{
	
	private View rootView;
	EditText SensorName,Quantity,BaudRate,Unit,Command,Byte;
	TextView Formula,PinOne,PinTwo,PinProtocol;
	String sensor,pin1,pin2,command,quantity,unit,formulaString,sub_protocol;
	int baud;
	int byteValue;
	FButton pin_select;
	private Boolean err;
	GlobalState gS;

	private String subprotocol;
	
	private FormulaContainer tempFc;

	private UartProc tempUartSensor;

	private Cursor c=null;
	
	UartDbHelper mUartHelper;

	private UartProc uartSensor;
	private FButton selectPin;


	private Callbacks uartCallbacks;
	
	public interface Callbacks {
		
		public void openFormula(String s);
		
		public void makeToast(String t);
		
		public void makeSensor(UartProc a);
		
		public void openPinSelection();
		
		public Context getContext();
		
		public String getPindata();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_uart,container, false);
		defineAttributes();
		
		tempFc = new FormulaContainer();
		tempUartSensor.setFc(tempFc);
		
		if (c != null) {
			autoFillForm();
		}
		
		//value will come from pin selection
		subprotocol = "UART1";

		selectPin.setOnClickListener(this);

		
		setHasOptionsMenu(true);
		return rootView;
	}
	
	
	private void autoFillForm() {
		// TODO Auto-generated method stub
		long row_id;
		if (c.moveToFirst()) {
			SensorName.setText(c.getString(c
					.getColumnIndex(UartDbHelper.UART_SENSOR_CODE)));
			Quantity.setText(c.getString(c
					.getColumnIndex(UartDbHelper.UART_QUANTITY)));	
			Unit.setText(c.getString(c.getColumnIndex(UartDbHelper.UART_UNIT)));
			PinOne.setText(c.getString(c
					.getColumnIndex(UartDbHelper.UART_PIN_RX)));
			PinTwo.setText(c.getString(c
					.getColumnIndex(UartDbHelper.UART_PIN_TX)));
			BaudRate.setText(c.getInt(c
					.getColumnIndex(UartDbHelper.UART_BAUD_RATE)) + "");
			Command.setText(c.getInt(c
					.getColumnIndex(UartDbHelper.UART_COMMAND)) + "");
			Byte.setText(c.getString(c.getColumnIndex(UartDbHelper.UART_BYTES))
					+ "");
			row_id = c.getLong(c.getColumnIndex(UartDbHelper.UART_KEY));
			L.d(row_id + "");
			autoFillFormula(row_id);
		}

	}
	
	private void autoFillFormula(long row_id) {
		// TODO Auto-generated method stub
		Cursor c = mUartHelper.getSqlDB().query(
				UartDbHelper.UART_FORMULA_TABLE_NAME, null,
				UartDbHelper.UART_FORMULA_SENSOR + "=?",
				new String[] { row_id + "" }, null, null, null);
		if (c.moveToFirst()) {
			Formula f;
			do {
				f = getFormula(c, tempFc);
				L.d(f.getFormulaString());
				tempFc.put(f.toString(), f);
			} while (c.moveToNext());
			Formula.setText(f.getFormulaString());
		}
	}
	
	private Formula getFormula(Cursor c, FormulaContainer tempFc) {
		// TODO Auto-generated method stub
		String name = c.getString(c
				.getColumnIndex(UartDbHelper.UART_FORMULA_NAME));
		String expression = c.getString(c
				.getColumnIndex(UartDbHelper.UART_FORMULA_EXPRESSION));
		String variables = c.getString(c
				.getColumnIndex(UartDbHelper.UART_FORMULA_VARIABLES));
		Formula f = new Formula(name, expression);
		if (variables.isEmpty()) {
			Variable x = Variable.make(name);
			f.addVariable(x);
		} else {
			String[] variableList = variables.split(":");
			HashMap<String, Formula> allVars = tempFc.getFc();
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
		L.d("uart fragment attached!");
		uartCallbacks = (Callbacks) activity;
	}


	private void defineAttributes() {
		// TODO Auto-generated method stub
				
				SensorName = (EditText) rootView.findViewById(R.id.sensor_name);
				Quantity = (EditText) rootView.findViewById(R.id.quantity_name1);
				Unit = (EditText) rootView.findViewById(R.id.s_unit1);
				PinOne = (TextView) rootView.findViewById(R.id.pin1);
				PinTwo = (TextView) rootView.findViewById(R.id.pin2);
				Command = (EditText) rootView.findViewById(R.id.command);
				PinProtocol = (TextView)rootView.findViewById(R.id.sub_protocol);
				BaudRate = (EditText) rootView.findViewById(R.id.baud_rate);
				//Formula = (TextView) rootView.findViewById(R.id.formula);
				Byte = (EditText) rootView.findViewById(R.id.byte_string);
				//pin_select.setOnClickListener(this);
				gS = (GlobalState) uartCallbacks.getContext();
				selectPin = (FButton) rootView.findViewById(R.id.pin_uart);
//				c = uartCallbacks.getCursor();
				gS.initializeSensor();
				tempUartSensor = (UartProc) gS.getSensor();
				
				//PinOne.setText(array[1]);
				//PinTwo.setText(array[2]);
		
	}

	
	private void fillForm() {
		// TODO Auto-generated method stub
		err=false;
		sensor = SensorName.getText().toString();
		quantity = Quantity.getText().toString();
		unit = Unit.getText().toString();
		pin1 = PinOne.getText().toString();
		pin2 = PinTwo.getText().toString();
		sub_protocol = PinProtocol.getText().toString();
		command = Command.getText().toString();
		//formulaString = Formula.getText().toString();
		
		try {
			baud = Integer.parseInt(BaudRate.getText().toString());
			byteValue = Integer.parseInt(Byte.getText().toString());
		} catch (Exception e) {
			err=true;
		}
		
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.adc_form_menu, menu);
	}
	
	
	private void updateSensor() {
		// TODO Auto-generated method stub
		fillForm();
		tempUartSensor.setSensorName(sensor);
		tempUartSensor.setQuantity(quantity);
		tempUartSensor.setUnit(unit);
		tempUartSensor.setPin1(pin1);
		tempUartSensor.setPin2(pin2);
		tempUartSensor.setCommand(command);
		tempUartSensor.setBaudRate(baud);
		tempUartSensor.setByteValue(byteValue);
		tempUartSensor.setPin(sub_protocol);
	}

	private void updateDatabase() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mUartHelper.getSqlDB();
		long newRowId;
		Cursor c;

		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.formula_menu:
			quantity = Quantity.getText().toString();
			if (quantity.isEmpty()) {
				uartCallbacks.makeToast("Enter quantity");

			} else {
				L.d("opening formula");
				// name of output parameter is sent as argument
				Formula f = new Formula(quantity, quantity);
				Variable x = Variable.make(quantity);
				f.addVariable(x);
				tempFc.put(quantity, f);
				uartCallbacks.openFormula(quantity);
			}

			break;
		case R.id.done_menu:
			fillForm();

			// if any of the fields in form is empty, toast shown
			if (err == true || sensor.isEmpty() || pin1.isEmpty()||pin2.isEmpty()
					) {
				uartCallbacks.makeToast("Empty fields present");
			}

			// else sensor object created
			else {
				uartCallbacks.makeSensor(uartSensor);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// when submit button clicked
				if (v.getId() == R.id.pin_uart) {
					uartCallbacks.openPinSelection();

				}
			}

//		HashMap<String, Formula> fc = tempFc.getFc();
//		String name, expression, variables;
//		ContentValues values;
//		for (Map.Entry<String, Formula> e : fc.entrySet()) {
//			name = e.getValue().toString();
//			expression = e.getValue().getExpression();
//			variables = "";
//			for (Variable var : e.getValue().getAllVariables()) {
//				variables += var.toString() + ":";
//			}
//			values = new ContentValues();
//			values.put(UartDbHelper.UART_FORMULA_NAME, name);
//			values.put(UartDbHelper.UART_FORMULA_EXPRESSION, expression);
//			values.put(UartDbHelper.UART_FORMULA_VARIABLES, variables);
//			values.put(UartDbHelper.UART_FORMULA_SENSOR, newRowId);
//			db.insert(UartDbHelper.UART_FORMULA_TABLE_NAME, null, values);

		

	

}

