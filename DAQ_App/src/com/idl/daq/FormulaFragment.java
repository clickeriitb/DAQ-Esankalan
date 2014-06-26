package com.idl.daq;

import java.util.ArrayList;

import com.daq.sensors.Sensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class FormulaFragment extends Fragment implements OnClickListener {

	private View rootView;
	private String name, expression;
	private Button add, clearAll;
	private TextView ex;
	private EditText fname;
	private GlobalState gS;

	private ListView listFormula;
	private ArrayList<String> formulaStrings;
	private ArrayAdapter<String> formulaAdapter;
	private Sensor tempSensor;

	private ScrollView background;

	private Callbacks formCallbacks;

	public interface Callbacks {
		public void makeToast(String t);

		public void createExpression();

		public void openFormula(String s);

		public void getFormula(String name, String expression,String displayName,String displayExpression);

		public void showProtocolForm();

		public Context getContext();
	}

	public FormulaFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e("onattach done", "activity attached");
		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		setRetainInstance(true);
		formCallbacks = (Callbacks) activity;
		Log.e("form callbacks", "callbacks defined");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		gS = (GlobalState) formCallbacks.getContext();
		tempSensor = gS.getSensor();
		L.d(gS.getProtocol());
		String protocol = gS.getProtocol();
	    rootView = inflater.inflate(R.layout.adc_expression, container, false);
	    if(protocol.equals("UART"))
	    { background = (ScrollView)rootView.findViewById(R.id.background);
	    background.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
	    }
		defineAttributes();
		Log.e("defineattributes", "called");
		add.setOnClickListener(this);
		clearAll.setOnClickListener(this);
		setHasOptionsMenu(true);
		return rootView;

	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
		fname = (EditText) rootView.findViewById(R.id.fname);
		add = (Button) rootView.findViewById(R.id.add_formula);
		clearAll = (Button) rootView.findViewById(R.id.clear_all);
		ex = (TextView) rootView.findViewById(R.id.expr);
		listFormula = (ListView) rootView.findViewById(R.id.lv_formula);
		setFormulaStrings();
		
		
	}
	
	public void setFormulaStrings(){
		formulaStrings = tempSensor.getFormulaContainer().getAllFormulaStrings();
		L.d("displaying formula Strings after setFormulaStrings");
		for(String s : formulaStrings){
			L.d(s);
		}
		formulaAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,formulaStrings);
		listFormula.setAdapter(formulaAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.form_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.done:
//			expression = gS.getGlobalString();
			// if no expression was entered
//			if (expression == null) {
//				formCallbacks.makeToast("Enter expression");
//			}
//
//			else {
//				callDialog();
//			}
			formCallbacks.showProtocolForm();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// if edit expression clicked
		if (v.getId() == R.id.add_formula) {
			// if formula name not entered
			formCallbacks.createExpression();
		}

		// if done button clicked
		else if(v.getId() == R.id.clear_all){
			tempSensor.destroyFc();
			setFormulaStrings();
			L.d("hi aunty");
			L.d("hi aunty2");
			
		}

	}

	public void callDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		Log.e("alert dialog built", "");
		alertDialogBuilder
				.setMessage("Add another formula?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// getFormula();
								// finish();
								// i = new Intent(Form.this,Form.class);
								// startActivity(i);
								name = fname.getText().toString();
								expression = gS.getGlobalString();
								//formCallbacks.getFormula(name, expression);
								formCallbacks.openFormula("new");
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// getFormula();
						// formCallbacks.getFormula(name,expression);
						name = fname.getText().toString();
						expression = gS.getGlobalString();
						//formCallbacks.getFormula(name, expression);
						formCallbacks.showProtocolForm();
						Log.e("show protocol called", gS.getProtocol());
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
