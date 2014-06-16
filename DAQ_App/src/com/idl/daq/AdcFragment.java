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
	private EditText sensorName,pinNo,icRangeFrom,icRangeTo,inputRangeFrom,inputRangeTo;
	private Button submit,formula_adc;
	private TextView formula;
	
	private String sensor,pinNum,formulaString;
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
		
		
		L.d("adc fragment attached!");
		adcCallbacks = (Callbacks) activity;
	}
	
	private void defineAttributes() {
		// TODO Auto-generated method stub
		sensorName = (EditText) rootView.findViewById(R.id.sensor_name);
		pinNo = (EditText) rootView.findViewById(R.id.pin_no);
		icRangeFrom= (EditText) rootView.findViewById(R.id.ic_range1);
		icRangeTo = (EditText) rootView.findViewById(R.id.ic_range2);
		formula = (TextView) rootView.findViewById(R.id.formula);
		inputRangeFrom= (EditText) rootView.findViewById(R.id.input_range1);
		inputRangeTo = (EditText) rootView.findViewById(R.id.input_range2);
		
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
		try {
			icFrom = Float.parseFloat(icRangeFrom.getText().toString());
			icTo = Float.parseFloat(icRangeTo.getText().toString());
			inputFrom = Float.parseFloat(inputRangeFrom.getText().toString());
			inputTo = Float.parseFloat(inputRangeTo.getText().toString());
			Log.e("ic",icFrom+"");
		} catch (Exception e) {
			err=true;
		}
		
		adcSensor = new AdcProc(sensor,pinNum,icFrom,icTo,gS.getfc(),inputFrom,inputTo);
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
			L.d("opening formula");
			//name of output parameter is sent as argument
			FormulaContainer fc = gS.getfc();
			Formula f = new Formula("temperature","temperature");
			Variable x = Variable.make("temperature");
			f.addVariable(x);
			fc.put("temperature", f);
			adcCallbacks.openFormula("temperature");
		}
	}

	

}
