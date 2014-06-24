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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdcFragment extends Fragment implements OnClickListener{
	
	private View rootView;
	private EditText sensorName,pinNo,icRangeFrom,icRangeTo,inputRangeFrom,inputRangeTo,Quantity,Unit;
	private Button submit,formula_adc;
	private TextView formula;
	
	private String sensor,pinNum,formulaString,quantity,unit;
	private float icFrom,icTo,inputFrom,inputTo;
	private Boolean err;
	
	private AdcProc adcSensor;
	
	private Callbacks adcCallbacks;
	
	private GlobalState gS;
	
	
	public interface Callbacks {
		
		public void openFormula(String s);
		
		public void makeToast(String t);
		
		public void makeSensor(Sensor a);
		
		public Context getContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity1_adc,
				container, false);
		
		defineAttributes();
		submit.setOnClickListener(this);
		formula_adc.setOnClickListener(this);
		
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
		pinNo = (EditText) rootView.findViewById(R.id.pin_no);
		icRangeFrom= (EditText) rootView.findViewById(R.id.range1);
		icRangeTo = (EditText) rootView.findViewById(R.id.range2);
		formula = (TextView) rootView.findViewById(R.id.formula);
		inputRangeFrom= (EditText) rootView.findViewById(R.id.input_range1);
		inputRangeTo = (EditText) rootView.findViewById(R.id.input_range2);
		Quantity = (EditText) rootView.findViewById(R.id.quantity_name);
		Unit = (EditText) rootView.findViewById(R.id.unit_adc);
		
		submit = (Button) rootView.findViewById(R.id.submit);
		formula_adc = (Button) rootView.findViewById(R.id.formula_adc);
		
		gS = (GlobalState) adcCallbacks.getContext();
	}
	
	//
	private void fillForm() {
		// TODO Auto-generated method stub
		err=false;
		sensor = sensorName.getText().toString();
		pinNum = pinNo.getText().toString();
		formulaString = formula.getText().toString();
		quantity = Quantity.getText().toString();
		unit = Unit.getText().toString();
		try {
			icFrom = Float.parseFloat(icRangeFrom.getText().toString());
			icTo = Float.parseFloat(icRangeTo.getText().toString());
			inputFrom = Float.parseFloat(inputRangeFrom.getText().toString());
			inputTo = Float.parseFloat(inputRangeTo.getText().toString());
			Log.e("ic",icFrom+"");
		} catch (Exception e) {
			err=true;
		}
		
		adcSensor = new AdcProc(sensor,quantity,unit,pinNum,icFrom,icTo,gS.getfc(),inputFrom,inputTo);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		//when submit button clicked
		if(v.getId()==R.id.submit)
		{
			fillForm();
			
			//if any of the fields in form is empty, toast shown
			if(err==true||sensor.isEmpty()||pinNum.isEmpty()||formulaString.isEmpty())
			{
				adcCallbacks.makeToast("Empty fields present");
			}
			
			//else sensor object created
			else
			{
				adcCallbacks.makeSensor(adcSensor);
			}
		}
		//when formula button clicked
		else
		{
			quantity = Quantity.getText().toString();
			if(quantity.isEmpty()){
				adcCallbacks.makeToast("Enter quantity");
				
			}else{
				L.d("opening formula");
				//name of output parameter is sent as argument
				FormulaContainer fc = gS.getfc();
				Formula f = new Formula(quantity,quantity);
				Variable x = Variable.make(quantity);
				f.addVariable(x);
				fc.put(quantity, f);
				adcCallbacks.openFormula(quantity);
			}
			
			
		}
	}

	

}
