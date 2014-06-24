package com.idl.daq;

import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.UartProc;

import expr.Variable;
import android.app.Activity;
import android.content.Context;
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
	
	private UartProc uartSensor;


	private Callbacks uartCallbacks;
	
	public interface Callbacks {
		
		public void openFormula(String s);
		
		public void makeToast(String t);
		
		public void makeSensor(UartProc a);
		
		public Context getContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_uart,container, false);
		defineAttributes();
		submit.setOnClickListener(this);
		formula_uart.setOnClickListener(this);
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
		
		uartSensor = new UartProc(sensor,pin1,pin2,gS.getfc(),baud,command,quantity,byteValue);
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
						uartCallbacks.makeSensor(uartSensor);
					}
				}
				
				//when formula button clicked
				else
				{
					quantity = Quantity.getText().toString();
					if(quantity.isEmpty())
					{
						uartCallbacks.makeToast("Enter name of the quantity");
					}
					else{
						L.d("opening formula");
						//name of output parameter is sent as argument
						FormulaContainer fc = gS.getfc();
						Formula f = new Formula(quantity,quantity);
						Variable x = Variable.make(quantity);
						f.addVariable(x);
						fc.put(quantity, f);
						uartCallbacks.openFormula(quantity);
					}
				}
		
	}
	
	

}

