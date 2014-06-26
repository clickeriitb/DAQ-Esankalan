package com.idl.daq;

import java.util.HashMap;
import java.util.Map;

import com.daq.db.UartDbHelper;
import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.UartProc;

import expr.Variable;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UartFragment extends Fragment implements OnClickListener{
	
	private View rootView;
	EditText SensorName,PinOne,PinTwo,Quantity,BaudRate,Unit,Command,Byte;
	TextView Formula;
	String sensor,pin1,pin2,command,quantity,unit,formula;
	float baud;
	int byteValue;
	Button submit,formula_uart;
	private Boolean err;
	GlobalState gS;
	
	private String subprotocol;
	
	private FormulaContainer tempFc;

	private UartProc tempUartSensor;

	private Cursor c=null;
	
	UartDbHelper mUartHelper;

	private Callbacks uartCallbacks;
	
	public interface Callbacks {
		
		public void openFormula(String s);
		
		public void makeToast(String t);
		
		public void makeSensor(UartProc a);
		
		public Context getContext();
		
		public Cursor getCursor();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_uart,container, false);
		
		defineAttributes();
		submit.setOnClickListener(this);
		formula_uart.setOnClickListener(this);
		
		tempFc = new FormulaContainer();
		tempUartSensor.setFc(tempFc);
		
		if (c != null) {
			autoFillForm();
		}
		
		//value will come from pin selection
		subprotocol = "UART1";
		
		return rootView;
	}
	
	private void defineAttributes() {
		// TODO Auto-generated method stub
				SensorName = (EditText) rootView.findViewById(R.id.sensor_name);
				Quantity = (EditText) rootView.findViewById(R.id.quantity_name1);
				Unit = (EditText) rootView.findViewById(R.id.s_unit1);
				PinOne = (EditText) rootView.findViewById(R.id.pin1);
				PinTwo = (EditText) rootView.findViewById(R.id.pin2);
				Command = (EditText) rootView.findViewById(R.id.command);
				BaudRate = (EditText) rootView.findViewById(R.id.baud_rate);
				Formula = (TextView) rootView.findViewById(R.id.formula);
				Byte = (EditText) rootView.findViewById(R.id.byte_string);
				
				submit = (Button) rootView.findViewById(R.id.submit_uart);
				formula_uart = (Button) rootView.findViewById(R.id.formula_uart);
				
				gS = (GlobalState) uartCallbacks.getContext();
				c = uartCallbacks.getCursor();
				mUartHelper = gS.getUartDbHelper();
				
				gS.initializeSensor();
				tempUartSensor = (UartProc) gS.getSensor();
		
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
			BaudRate.setText(c.getFloat(c
					.getColumnIndex(UartDbHelper.UART_BAUD_RATE)) + "");
			Command.setText(c.getFloat(c
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


	
	private void fillForm() {
		// TODO Auto-generated method stub
		err=false;
		sensor = SensorName.getText().toString();
		quantity = Quantity.getText().toString();
		unit = Unit.getText().toString();
		pin1 = PinOne.getText().toString();
		pin2 = PinTwo.getText().toString();
		command = Command.getText().toString();
		formula = Formula.getText().toString();
		
		try {
			baud = Float.parseFloat(BaudRate.getText().toString());
			byteValue = Integer.parseInt(Byte.getText().toString());
		} catch (Exception e) {
			err=true;
		}
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		//when submit button clicked
				if(v.getId()==R.id.submit_uart)
				{
					fillForm();
					
					//if any of the fields in form is empty, toast shown
					if(err==true||sensor.isEmpty()||quantity.isEmpty()||unit.isEmpty()||pin1.isEmpty()||pin2.isEmpty()||command.isEmpty()||formula.isEmpty())
					{
						uartCallbacks.makeToast("Empty fields present");
					}
					
					//else sensor object created
					else
					{
						updateDatabase();
						uartCallbacks.makeSensor(tempUartSensor);
					}
				}
				
				//when formula button clicked
				else
				{
					quantity = Quantity.getText().toString();
					sensor = SensorName.getText().toString();
					
					if(quantity.isEmpty() || sensor.isEmpty())
					{
						if (sensor.isEmpty())
							uartCallbacks.makeToast("Enter sensor name");
						else
							uartCallbacks.makeToast("Enter quantity");
					}
					else{
						L.d("opening formula");
						//name of output parameter is sent as argument
						Formula f = new Formula("pin1","pin1",PinOne.getText().toString(),"pin");
						Variable x = Variable.make("pin1");
						f.addVariable(x);
						tempFc.put("pin1", f);
						updateSensor();
						uartCallbacks.openFormula("");
					}
				}
		
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
	}

	private void updateDatabase() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mUartHelper.getSqlDB();
		long newRowId;
		Cursor c;

		String query = "SELECT * FROM " + UartDbHelper.UART_TABLE_NAME
				+ " WHERE " + UartDbHelper.UART_SENSOR_CODE + " = '"
				+ SensorName.getText().toString() + "' AND "
				+ UartDbHelper.UART_QUANTITY + " = '"
				+ Quantity.getText().toString() + "' AND "
				+ UartDbHelper.UART_UNIT + " = '" + Unit.getText().toString()
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
		values.put(UartDbHelper.UART_SENSOR_CODE, SensorName.getText().toString());
		values.put(UartDbHelper.UART_QUANTITY, Quantity.getText().toString());
		values.put(UartDbHelper.UART_UNIT, Unit.getText().toString());
		values.put(UartDbHelper.UART_PIN_RX, PinOne.getText().toString());
		values.put(UartDbHelper.UART_PIN_TX, PinTwo.getText().toString());
		values.put(UartDbHelper.UART_BAUD_RATE, BaudRate.getText().toString());
		values.put(UartDbHelper.UART_BYTES, Byte.getText().toString());
		values.put(UartDbHelper.UART_COMMAND, Command.getText().toString());

		if (c.moveToFirst()) {
			newRowId = c.getLong(c.getColumnIndex(UartDbHelper.UART_KEY));
			db.update(UartDbHelper.UART_TABLE_NAME, values, "_id" + "="
					+ newRowId, null);
			L.d("deleting "
					+ db.delete(UartDbHelper.UART_FORMULA_TABLE_NAME,
							UartDbHelper.UART_FORMULA_SENSOR + "=?",
							new String[] { String.valueOf(newRowId) }) + "");

		} else {
			newRowId = db.insert(UartDbHelper.UART_TABLE_NAME, null, values);
			L.d("new row added "+newRowId);
		}
		updateFormula(newRowId, db);
	}
	
	private void updateFormula(long newRowId, SQLiteDatabase db) {
		// TODO Auto-generated method stub

		HashMap<String, Formula> fc = tempFc.getFc();
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
			values.put(UartDbHelper.UART_FORMULA_NAME, name);
			values.put(UartDbHelper.UART_FORMULA_EXPRESSION, expression);
			values.put(UartDbHelper.UART_FORMULA_VARIABLES, variables);
			values.put(UartDbHelper.UART_FORMULA_SENSOR, newRowId);
			db.insert(UartDbHelper.UART_FORMULA_TABLE_NAME, null, values);
		}
	}
	
	

}

