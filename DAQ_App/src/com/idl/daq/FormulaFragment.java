package com.idl.daq;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FormulaFragment extends Fragment implements OnClickListener {

	private View rootView;
	private String name, expression;
	private Button expr;
	private TextView ex;
	private EditText fname;
	private GlobalState gS;

	private Callbacks formCallbacks;

	public interface Callbacks {
		public void makeToast(String t);

		public void createExpression();

		public void openFormula(String s);

		public void getFormula(String name, String expression);

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
		rootView = inflater.inflate(R.layout.adc_expression, container, false);
		Log.e("inflater", "inflated");
		gS = (GlobalState) formCallbacks.getContext();
		defineAttributes();
		Log.e("defineattributes", "called");
		expr.setOnClickListener(this);
		setHasOptionsMenu(true);
		return rootView;

	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
		fname = (EditText) rootView.findViewById(R.id.fname);
		expr = (Button) rootView.findViewById(R.id.edit_exp);
		ex = (TextView) rootView.findViewById(R.id.expr);
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
			expression = gS.getGlobalString();
			// if no expression was entered
			if (expression == null) {
				formCallbacks.makeToast("Enter expression");
			}

			else {
				callDialog();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// if edit expression clicked
		if (v.getId() == R.id.edit_exp) {
			// if formula name not entered
			if (fname.getText().toString().isEmpty()) {
				formCallbacks.makeToast("Enter formula name");
			} else {
				// go to expression fragment
				formCallbacks.createExpression();
			}
		}

		// if done button clicked
		else {

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
								formCallbacks.getFormula(name, expression);
								formCallbacks.openFormula("new");
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// getFormula();
						// formCallbacks.getFormula(name,expression);
						name = fname.getText().toString();
						expression = gS.getGlobalString();
						formCallbacks.getFormula(name, expression);
						formCallbacks.showProtocolForm();
						Log.e("show protocol called", gS.getProtocol());
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
