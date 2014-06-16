package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;

import com.daq.formula.Formula;
import com.daq.sensors.AdcProc;
import com.daq.sensors.Sensor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SensorFormActivity extends FragmentActivity implements AdcFragment.Callbacks, FormFragment.Callbacks, ExpressionFragment.Callbacks{

	GlobalState gS;
	String protocol,initialSpinnerValue;
	FragmentManager fm;
	FragmentTransaction t;
	Fragment newFrag=null,oldFrag=null,f,p,g,u;
	private ArrayList<String> varList;
	private HashMap<String, Formula> allVar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_with_fragments);
		gS = (GlobalState) getApplicationContext();
		protocol = gS.getProtocol();
		gS.initializeFc();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction t = fm.beginTransaction();
		if(protocol.equals("ADC")){
			if(fm.findFragmentByTag(protocol) == null){
				newFrag = new AdcFragment();
				t.add(R.id.sensor_form_container,newFrag, protocol);
				t.show(newFrag);
			}
		}else if(protocol.equals("UART")){
//			if(fm.findFragmentByTag(protocol) == null){
//				t.add(R.id.sensor_detail_container,new UartFragment(), protocol);
//			}
			/*Under Development*/
		}else if(protocol.equals("I2C")){
			/*Under Development*/
		}
		
		t.commit();
		
	}

	@Override
	public void openFormula(String s) {
		// TODO Auto-generated method stub
		//gS.setGlobalString(s); 
		Log.e("eeeeeeee", "not started");
		oldFrag = newFrag;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction t = fm.beginTransaction();
		//if method called after protocol selection or "add new formula" dialog box new fragment created
		if(fm.findFragmentByTag("formula")==null || !s.equals("back")){
			if(fm.findFragmentByTag("formula")!=null){
				t.remove(fm.findFragmentByTag("formula"));
			}
			newFrag = new FormFragment();
			Log.e("new form fragment", "created");
			t.add(R.id.sensor_form_container,newFrag, "formula");
		}
		else{
			Log.e("old formula screen shown", "old fragment retained");
			newFrag = fm.findFragmentByTag("formula");
			Log.e("string glbal", gS.getGlobalString());
			TextView ex = (TextView) findViewById(R.id.expr);
			ex.setText("");
			ex.setText(gS.getGlobalString());
		}
		Log.e("add","added");
		t.hide(oldFrag);
		t.show(newFrag);
		Log.e("commit","commitment");
		t.commit();
	}

	@Override
	public void makeToast(String t) {
		// TODO Auto-generated method stub
		Toast.makeText(gS, t, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void makeSensor(Sensor a) {
		// TODO Auto-generated method stub
		if(protocol.equals("ADC")){
			gS.addSensor(a);
			Intent i = new Intent(getApplicationContext(),SensorListActivity.class);
			startActivity(i);
		}
		
	}

	@Override
	public void createExpression() {
		// TODO Auto-generated method stub
		gS.setGlobalString("temperature");
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction t = fm.beginTransaction();
		oldFrag = newFrag;
		t.hide(oldFrag);
		newFrag = new ExpressionFragment();
		t.add(R.id.sensor_form_container,newFrag, "expression");
		t.show(newFrag);
		t.commit();
	}


	@Override
	public void getFormula(String name, String expression) {
		// TODO Auto-generated method stub
		Log.e("entered getformula", "jkhjh");
		allVar = gS.getfc().getFc();
		Formula formula = new Formula(name, expression);
		for(String str : varList)
		{
				formula.addToHashMap(str, allVar.get(str));				
		}
		gS.addToFc(formula);
		
	}

	@Override
	public void showProtocolForm() {
		// TODO Auto-generated method stub
		
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction t=fm.beginTransaction();
		oldFrag=newFrag;
		t.hide(oldFrag);
		//newFrag=fm.findFragmentByTag(protocol);
		Log.d("old sensor form retained", "adc");
		
		newFrag = fm.findFragmentByTag(protocol);
		
		TextView tv = (TextView) findViewById(R.id.formula);
		tv.setText(gS.getGlobalString());
		t.show(newFrag);
		if(protocol.equals("UART")){
//			if(fm.findFragmentByTag(protocol) == null){
//				t.add(R.id.sensor_detail_container,u, protocol);
			/*Under Development*/ 
			Log.d("old sensor form retained", "uart");
		}else if(protocol.equals("I2C")){
			/*Under Development*/
			Log.d("old sensor form retained", "");
		}
		
		t.commit();
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return gS;
	}

	@Override
	public void addToVariableList(ArrayList<String> varlist) {
		// TODO Auto-generated method stub
		varList = varlist;
	}

	
}
