package com.idl.daq;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.daq.formula.FormulaContainer;
import com.daq.sensors.Sensor;
import com.daq.sensors.UartProc;


public class UartFragment extends Fragment implements OnClickListener{
	
	private View rootView;
	EditText SensorName,Quantity,BaudRate,Unit,Command,Byte;
	TextView Formula,PinOne,PinTwo,PinProtocol;
	String sensor,pin1,pin2,command,quantity,unit,formulaString,sub_protocol;
	int baud;
	int byteValue;
	FButton pin_select;
	private Boolean err;
	private GlobalState gS;
	
	private FormulaContainer tempFc;

	private UartProc tempUartSensor;

	private Cursor c=null;
	UartDbHelper mUartHelper;

	private FButton selectPin;


	private Callbacks uartCallbacks;
	
	
	public interface Callbacks {
		
		public void openFormula(String s);
		
		public void makeToast(String t);
		
		public void makeSensor(Sensor a);
		
		public void openPinSelection();
		
		public Context getContext();
		
		public String getPindata();
		
		public Cursor getCursor();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.uart_form,container, false);
		defineAttributes();
		selectPin.setOnClickListener(this);
		setHasOptionsMenu(true);
		
		
		if (c != null) {
			autoFillForm();
		}
		
		//value will come from pin selection
		sub_protocol = "UART1";
		
		return rootView;
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
				Byte = (EditText) rootView.findViewById(R.id.byte_string);
				
				gS = (GlobalState) uartCallbacks.getContext();
				selectPin = (FButton) rootView.findViewById(R.id.pin_uart);
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
			getSubProtocol();
			PinProtocol.setText(sub_protocol);
			BaudRate.setText(c.getInt(c
					.getColumnIndex(UartDbHelper.UART_BAUD_RATE)) + "");
			Command.setText(c.getInt(c
					.getColumnIndex(UartDbHelper.UART_COMMAND)) + "");
			Byte.setText(c.getString(c.getColumnIndex(UartDbHelper.UART_BYTES))
					+ "");
			row_id = c.getLong(c.getColumnIndex(UartDbHelper.UART_KEY));
			L.d(row_id + "");
		}
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
		err = false;
		sensor = SensorName.getText().toString();
		quantity = Quantity.getText().toString();
		unit = Unit.getText().toString();
		pin1 = PinOne.getText().toString();
		pin2 = PinTwo.getText().toString();
		sub_protocol = PinProtocol.getText().toString();
		command = Command.getText().toString();		
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()==R.id.done_menu)
		{
			fillForm();

			// if any of the fields in form is empty, toast shown
			if (err == true || sensor.isEmpty() || pin1.isEmpty()||pin2.isEmpty()||quantity.isEmpty()||unit.isEmpty()||PinProtocol.getText().toString().isEmpty())
			{
				uartCallbacks.makeToast("Empty fields present");
			}

			// else sensor object created
			else {
				updateSensor();
				updateDatabase();
				uartCallbacks.makeSensor(tempUartSensor);
			}
		}
		return super.onOptionsItemSelected(item);
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
		
		c = db.rawQuery(query, null);
		L.d(" c move to first " + c.moveToFirst());
		
		ContentValues values = new ContentValues();
		values.put(UartDbHelper.UART_SENSOR_CODE, SensorName.getText().toString());
		values.put(UartDbHelper.UART_QUANTITY, Quantity.getText().toString());
		values.put(UartDbHelper.UART_UNIT, Unit.getText().toString());
		values.put(UartDbHelper.UART_PIN_RX, PinOne.getText().toString());
		values.put(UartDbHelper.UART_PIN_TX, PinTwo.getText().toString());
		values.put(UartDbHelper.UART_BAUD_RATE,  BaudRate.getText().toString());
		values.put(UartDbHelper.UART_BYTES, Byte.getText().toString());
		values.put(UartDbHelper.UART_COMMAND, Command.getText().toString());

		if (c.moveToFirst()) {
			newRowId = c.getLong(c.getColumnIndex(UartDbHelper.UART_KEY));
			db.update(UartDbHelper.UART_TABLE_NAME, values, "_id" + "="
					+ newRowId, null);
			L.d("updatinnnnnnnnnnnnnnnnnnnnnnnnnnng");
		} else {
			newRowId = db.insert(UartDbHelper.UART_TABLE_NAME, null, values);
			L.d("making neeeeeeeeeeew");
		}
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// when submit button clicked
				if (v.getId() == R.id.pin_uart) {
					uartCallbacks.openPinSelection();
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
	
	private void getSubProtocol()
	{
		pin1 = PinOne.getText().toString();
		pin2 = PinTwo.getText().toString();
		
		if(pin1.equals("P9_26"))
		{
			sub_protocol="UART1";
		}
		if(pin1.equals("P9_22"))
		{
			sub_protocol="UART2";
		}
		if(pin1.equals("P9_11"))
		{
			sub_protocol="UART4";
		}
		if(pin1.equals("P9_38"))
		{
			sub_protocol="UART5";
		}
	}

}

