package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.daq.formula.Formula;
import com.daq.sensors.Sensor;
import com.daq.sensors.UartProc;

public class SensorFormActivity extends FragmentActivity implements
		AdcFragment.Callbacks, FormulaFragment.Callbacks,

		ExpressionFragment.Callbacks, UartFragment.Callbacks,
		PinSelectFragmentAdc.Callbacks, SensorBrowseFragment.Callbacks,
		PinSelectFragmentUart.Callbacks, I2CFragment.Callbacks,
		I2CPinSelect.Callbacks, I2C_ConfigFragment.Callbacks,
		I2C_ExecFragment.Callbacks {

	GlobalState gS;
	String protocol, initialSpinnerValue;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	Fragment newFrag = null, oldFrag = null;
	private ArrayList<String> varList;
	private HashMap<String, Formula> allVar;
	String pinData = "";
	Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_with_fragments);
		gS = (GlobalState) getApplicationContext();
		protocol = gS.getProtocol();
		// gS.initializeFc();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction t = fm.beginTransaction();
		// if (protocol.equals("ADC")) {
		// if (fm.findFragmentByTag(protocol) == null) {
		// newFrag = new AdcFragment();
		// }
		// } else if (protocol.equals("UART")) {
		// if (fm.findFragmentByTag(protocol) == null) {
		// newFrag = new UartFragment();
		// }
		// } else if (protocol.equals("I2C")) {
		// /* Under Development */
		// }
		newFrag = new SensorBrowseFragment();
		t.add(R.id.sensor_form_container, newFrag, "browse");
		t.show(newFrag);
		t.commit();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		L.d("handling intent action: " + intent.getAction());
		super.onNewIntent(intent);
		if (!Intent.ACTION_SEARCH.equals(intent.getAction())) {
			onCreate(null);
		}
	}

	// TODO clean it saaf safayi
	@Override
	public void openFormula(String s) {
		// TODO Auto-generated method stub
		// gS.setGlobalString(s);
		Log.e("eeeeeeee", "not started");
		oldFrag = newFrag;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction t = fm.beginTransaction();
		// if method called after protocol selection or "add new formula" dialog
		// box new fragment created
		if (fm.findFragmentByTag("formula") == null || !s.equals("back")) {
			if (fm.findFragmentByTag("formula") != null) {
				t.remove(fm.findFragmentByTag("formula"));
			}
			newFrag = new FormulaFragment();
			Log.e("new form fragment", "created");
			t.add(R.id.sensor_form_container, newFrag, "formula");
		} else {
			Log.e("old formula screen shown", "old fragment retained");
			newFrag = fm.findFragmentByTag("formula");
			Log.e("string glbal", gS.getGlobalString());
			// TextView ex = (TextView) findViewById(R.id.expr);
			// ex.setText("");
			// ex.setText(gS.getGlobalString());
		}
		Log.e("add", "added");
		t.hide(oldFrag);
		t.show(newFrag);
		Log.e("commit", "commitment");
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
		gS.addSensor(a);
		Intent i = new Intent(getApplicationContext(), SensorListActivity.class);
		startActivity(i);

	}

	@Override
	public void createExpression() {
		// TODO Auto-generated method stub
		gS.setGlobalString("temperature");
		addNewFragment(new ExpressionFragment(), "expression", -1, -1);
	}

	@Override
	public void getFormula(String name, String expression, String displayName,
			String displayExpression) {
		// TODO Auto-generated method stub
		Log.e("entered getformula", "jkhjh");
		allVar = gS.getSensor().getFormulaContainer().getFc();
		Formula formula = new Formula(name, expression, displayName,
				displayExpression);
		for (String str : varList)
			formula.addToHashMap(str, allVar.get(str));
		gS.getSensor().addToFc(formula);

	}

	@Override
	public void showProtocolForm() {
		// TODO Auto-generated method stub
		showOldFragment(protocol, R.anim.right_in, R.anim.right_out);
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// Intent intent=new Intent(this,SelectProtocol.class);
		// startActivity(intent);
		// finish();
		// super.onBackPressed();
		
		L.d("on back pressed called ");

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction t = fm.beginTransaction();

		Fragment temp;
		
		L.d("browse frag added"+fm.findFragmentByTag("browse").isAdded());
		
		L.d("browse frag visible"+fm.findFragmentByTag("browse").isVisible());

		if ((temp = fm.findFragmentByTag("browse"))!=null) {
			if (temp.isVisible()) {
				Intent intent = new Intent(this, SelectProtocol.class);
				L.d("Browse frag visible, intent added");
				startActivity(intent);
				L.d("Browse frag visible, intent starts");
				finish();
				L.d("Browse frag finishes");
				return;
				
			}
		}
		
		

		if ((temp=fm.findFragmentByTag(protocol))!=null) {
			L.d("protocol is added");
			if(temp.isVisible())
			{
				L.d("protocol is visible");
				oldFrag = temp;
				newFrag = fm.findFragmentByTag("browse");
				L.d("back to browse");
			}
		}
		
		if ((temp = fm.findFragmentByTag(protocol+"_id"))!=null) {
			if(temp.isVisible())
			{
				oldFrag = temp;
				newFrag = fm.findFragmentByTag(protocol);
			}
		} 
		
		if ((temp = fm.findFragmentByTag("expression"))!=null) {
			if(temp.isVisible())
			{
				oldFrag = temp;
				newFrag = fm.findFragmentByTag("formula");
			}
		} 
		if ((temp = fm.findFragmentByTag("i2c_config"))!=null) {
			if(temp.isVisible())
			{
				oldFrag = temp;
				newFrag = fm.findFragmentByTag(protocol);
			}
		} 
		if ((temp = fm.findFragmentByTag("i2c_exec"))!=null) {
			if(temp.isVisible())
			{
				oldFrag = temp;
				newFrag = fm.findFragmentByTag("i2c_config");
			}
		} 
		if ((temp = fm.findFragmentByTag("formula"))!=null) {
			if(temp.isVisible()){
				oldFrag = temp;
				if (protocol.equals("ADC")) {
					newFrag = fm.findFragmentByTag(protocol);
				} else {
					newFrag = fm.findFragmentByTag("i2c_exec");
				}
			}
		}
		if (R.anim.right_in != -1 && R.anim.right_out != -1) {
			t.setCustomAnimations(R.anim.right_in,
					R.anim.right_out);
		}
		
		if(oldFrag==null && newFrag==null){
			L.d("old Frag and new Frag null");
		}
		t.hide(oldFrag);
		L.d("hide old frag");
		t.show(newFrag);
		L.d("show new frag");
		t.commit();
		L.d("commit frag");
	}

	@Override
	public void openProtocol(Cursor c) {
		// TODO Auto-generated method stub
		protocol = gS.getProtocol();
		Fragment fragment = null;
		if (protocol.equals("ADC"))
			fragment = new AdcFragment();
		else if (protocol.equals("UART"))
			fragment = new UartFragment();
		else if (protocol.equals("I2C"))
			fragment = new I2CFragment();

		if (c != null) {
			this.c = c;
			L.d("in openprotocol cursor is not null");
		}
		if (fragment != null) {
			addNewFragment(fragment, protocol, R.anim.left_in, R.anim.left_out);
			L.d("in openprotocol fragment is not null");
		}
	}

	@Override
	public Cursor getCursor() {
		// TODO Auto-generated method stub
		return c;
	}

	@Override
	public void openPinSelection() {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		if (protocol == "ADC")
			fragment = new PinSelectFragmentAdc();
		else if (protocol == "UART")
			fragment = new PinSelectFragmentUart();
		else if (protocol.equals("I2C"))
			fragment = new I2CPinSelect();
		addNewFragment(fragment, protocol + "_pin", R.anim.vertical_up_in,
				R.anim.vertical_up_out);
	}

	@Override
	public void sendSelectedPins(String s) {
		// TODO Auto-generated method stub
		pinData = s;
	}

	@Override
	public String getPindata() {
		// TODO Auto-generated method stub
		return pinData;
	}

	@Override
	public void openForm() {
		// TODO Auto-generated method stub

		if (showOldFragment(protocol, R.anim.vertical_down_in,
				R.anim.vertical_down_out)) {
			if (protocol.equals("ADC")) {
				TextView pin_view = (TextView) findViewById(R.id.pin_no);
				pin_view.setText(pinData);
			} else if (protocol.equals("UART")) {
				String[] data = pinData.split(":");
				TextView pin_view = (TextView) findViewById(R.id.sub_protocol);
				pin_view.setText(data[0]);
				TextView pin_view1 = (TextView) findViewById(R.id.pin1);
				pin_view1.setText(data[1]);
				TextView pin_view2 = (TextView) findViewById(R.id.pin2);
				pin_view2.setText(data[2]);

			} else if (protocol.equals("I2C")) {
				String arr[] = pinData.split(":");
				TextView sda = (TextView) findViewById(R.id.sda);
				sda.setText(arr[1]);
				TextView scl = (TextView) findViewById(R.id.scl);
				scl.setText(arr[2]);
			}
		}
	}

	public boolean addNewFragment(Fragment fragment, String tag,
			int incoming_animation, int outgoing_animation) {
		try {
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
			if (incoming_animation != -1 && outgoing_animation != -1) {
				fragmentTransaction.setCustomAnimations(incoming_animation,
						outgoing_animation);
			}
			oldFrag = newFrag;
			fragmentTransaction.hide(oldFrag);
			newFrag = fragment;
			fragmentTransaction.add(R.id.sensor_form_container, newFrag, tag);
			fragmentTransaction.show(newFrag);
			fragmentTransaction.commit();
			return true;
		} catch (Exception e) {
			Log.e("fragment error", e.toString());
			return false;
		}
	}

	public boolean showOldFragment(String tag, int incoming_animation,
			int outgoing_animation) {
		try {
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
			if (incoming_animation != -1 && outgoing_animation != -1) {
				fragmentTransaction.setCustomAnimations(incoming_animation,
						outgoing_animation);
			}
			oldFrag = newFrag;
			fragmentTransaction.hide(oldFrag);
			Fragment tempFrag = fragmentManager.findFragmentByTag(tag);
			if (tempFrag != null) {
				newFrag = tempFrag;
				fragmentTransaction.show(newFrag);
				fragmentTransaction.commit();
				return true;
			} else
				return false;
		} catch (Exception e) {
			Log.e("fragment error", e.toString());
			return false;
		}
	}

	@Override
	public void openConfig() {
		// TODO Auto-generated method stub
		if (protocol.equals("I2C")) {
			Fragment fragment = new I2C_ConfigFragment();
			if (!showOldFragment("i2c_config", R.anim.left_in, R.anim.left_out)) {
				addNewFragment(fragment, "i2c_config", R.anim.left_in,
						R.anim.left_out);
			}
		}
	}

	@Override
	public void openExec() {
		// TODO Auto-generated method stub
		if (protocol.equals("I2C")) {
			Fragment fragment = new I2C_ExecFragment();
			if (!showOldFragment("i2c_exec", R.anim.left_in, R.anim.left_out)) {
				addNewFragment(fragment, "i2c_exec", R.anim.left_in,
						R.anim.left_out);
			}
		}
	}

}
