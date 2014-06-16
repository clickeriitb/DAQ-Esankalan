package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;
import com.daq.formula.Formula;
import com.idl.daq.SensorListFragment.Callbacks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

public class FormFragment extends Fragment implements OnClickListener {
	
	private View rootView;
	private String name,expression;
	private Button expr,done;
	private TextView ex;
	private EditText fname;
	private GlobalState gS;
	
	private Callbacks formCallbacks;
	
	public interface Callbacks 
	{
		public void makeToast(String t);
		
		public void createExpression();

		public void openFormula(String s);
		
		public void getFormula(String name, String expression);
		
		public void showProtocolForm();
		
		public Context getContext();
	}
	
	
	
	
	public FormFragment() {
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

		formCallbacks = (Callbacks) activity;
		Log.e("form callbacks", "callbacks defined");
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.form,
				container, false);
		Log.e("inflater", "inflated");
		gS = (GlobalState) formCallbacks.getContext();
		defineAttributes();
		Log.e("defineattributes", "called");
		expr.setOnClickListener(this);
		done.setOnClickListener(this);
		
		return rootView;
		
		
	}


	private void defineAttributes() {
		// TODO Auto-generated method stub
		fname = (EditText) rootView.findViewById(R.id.fname);
		expr = (Button) rootView.findViewById(R.id.edit_exp);
		done = (Button)rootView.findViewById(R.id.form_done);
		ex = (TextView) rootView.findViewById(R.id.expr);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		//if edit expression clicked
		if(v.getId()==R.id.edit_exp)
		{
			//if formula name not entered
			if(fname.getText().toString().isEmpty())
			{
				formCallbacks.makeToast("Enter formula name");
			}
			else
			{
				//go to expression fragment
				formCallbacks.createExpression();
			}
		}
		
		//if submit button clicked
		else
		{
			expression = gS.getGlobalString();
			//if no expression was entered
			if(expression==null)
			{
				formCallbacks.makeToast("Enter expression");
			}
			
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
				Log.e("alert dialog built", "");
					alertDialogBuilder
						.setMessage("Add another formula?")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
//								getFormula();
//								finish();
//								i = new Intent(Form.this,Form.class);
//								startActivity(i);
								name = fname.getText().toString();
								expression = gS.getGlobalString();
								Log.e("formmmmmmmmmmmmmmmmmmm",gS.getProtocol() + " "  + name + " " + expression);
								formCallbacks.getFormula(name,expression);
								Log.e("formmmmmmmmmmmmmmmmmmm",gS.getProtocol());
								formCallbacks.openFormula("new");
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								//getFormula();
								//formCallbacks.getFormula(name,expression);
								Log.e("formmmmmmmmmmmmmmmmmmm",gS.getProtocol());
								name = fname.getText().toString();
								expression = gS.getGlobalString();
								formCallbacks.getFormula(name,expression);
								Log.e("formmmmm",gS.getProtocol());
								formCallbacks.showProtocolForm();
								Log.e("show protocol called",gS.getProtocol());
							}
						});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
			}		
		}
		
	}
	
	

}
