package com.idl.daq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.UartProc;

import expr.Variable;

public class UartFragment extends Fragment implements OnClickListener{
	
	private View rootView;
	EditText SensorName,Quantity,BaudRate,Unit,Command,Byte;
	TextView Formula,PinOne,PinTwo,PinProtocol;
	String sensor,pin1,pin2,command,quantity,unit,formulaString,sub_protocol;
	float baud;
	int byteValue;
	FButton pin_select;
	private Boolean err;
	GlobalState gS;
	
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
		rootView = inflater.inflate(R.layout.uart_form,container, false);
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
		L.d("uart fragment attached!");
		uartCallbacks = (Callbacks) activity;
	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
				//String array[] = uartCallbacks.getPindata().split(":");
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
				PinProtocol.setText(uartCallbacks.getPindata().toString());
//				PinOne.setText(array[1]);
//				PinTwo.setText(array[2]);
		
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
			baud = Float.parseFloat(BaudRate.getText().toString());
			byteValue = Integer.parseInt(Byte.getText().toString());
		} catch (Exception e) {
			err=true;
		}
		
		uartSensor = new UartProc(sensor,pin1,pin2,sub_protocol,gS.getfc(),baud,command,quantity,byteValue);
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
				uartCallbacks.makeToast("Enter quantity");

			} else {
				L.d("opening formula");
				// name of output parameter is sent as argument
				FormulaContainer fc = gS.getfc();
				Formula f = new Formula(quantity, quantity);
				Variable x = Variable.make(quantity);
				f.addVariable(x);
				fc.put(quantity, f);
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

	

}

