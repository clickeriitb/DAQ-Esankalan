package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daq.formula.Formula;

import expr.Expr;
import expr.Parser;
import expr.SyntaxException;

public class ExpressionFragment extends Fragment implements OnClickListener {

	private ListView variableListHolder;
	private GlobalState gS;
	Intent i;
	private ArrayList<String> list;
	private TextView mathExpression;

	
	private EditText formulaName;


	private LinearLayout background;

	private FButton one, two, three, four, five, six, seven, eight, nine,
			times, divide, plus, minus, power, exp;
	private FButton log, ln, zero, dot, clr, openbracket, closebracket, sqrt,
			pi;
	private ImageButton del;
	private ArrayList<String> userInput, expression;
	private int index;
	private Boolean err;
	private String express;

	private View rootView;

	private Callbacks expressionCallbacks;

	public interface Callbacks {
		public void makeToast(String t);

		public void openFormula(String s);

		public Context getContext();

		public void addToVariableList(ArrayList<String> varList);
		
		public void getFormula(String name, String expression,String displayName,String displayExpression);

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
		expressionCallbacks = (Callbacks) activity;
		Log.e("callbacks", "callbacks defined");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		gS = (GlobalState) expressionCallbacks.getContext();
		String protocol	= gS.getProtocol();
		rootView = inflater.inflate(R.layout.adc_calc, container, false);
		if(protocol.equals("UART"))
	    { background = (LinearLayout)rootView.findViewById(R.id.background);
	      background.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue));
	    }
		Log.e("gs", "global state defined");
		defineAttributes();
		Log.e("defineattributes", ""
				+ "called");
		setHasOptionsMenu(true);
		return rootView;
		
		
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
			
			err = false;

			//building expression
			express = "";
			for(String s : expression){
				express+=s;
			}
			L.d("expr "+express);
			
			if(express.isEmpty()){
				err = true;
				expressionCallbacks.makeToast("Enter expression");
				
			}else if(formulaName.getText().toString().isEmpty()){
				err = true;
				expressionCallbacks.makeToast("Enter formula");
			}
			Expr expr = null;
			try {
				expr = Parser.parse(express);
				Log.e("evaluate", expr.toString());
			}

			catch (SyntaxException e) {
				System.err.println(e.explain());
				Toast.makeText(gS, "Enter valid expression", Toast.LENGTH_SHORT)
						.show();
				err = true;
			}

			if (!err) {
				expressionCallbacks.addToVariableList(list);
				expressionCallbacks.getFormula(formulaName.getText().toString(), express, formulaName.getText().toString(), mathExpression.getText().toString());
				gS.setGlobalString(express);
				Log.e("string", gS.getGlobalString());
				expressionCallbacks.openFormula("");

			}

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void defineAttributes() {
		// TODO Auto-generated method stub

		variableListHolder = (ListView) rootView.findViewById(R.id.lv);
		formulaName = (EditText) rootView.findViewById(R.id.fname);

		setListView();

		// submit = (Button) rootView.findViewById(R.id.submit);
		// submit.setOnClickListener(this);
		list = new ArrayList<>();
		Log.e("dekhoooooooooo", "Activity created properly");
		userInput = new ArrayList<String>();
		expression = new ArrayList<String>();
		index = 0;
		err = false;

		mathExpression = (TextView) rootView.findViewById(R.id.express);

		one = (FButton) rootView.findViewById(R.id.button1);
		two = (FButton) rootView.findViewById(R.id.button2);
		three = (FButton) rootView.findViewById(R.id.button3);
		plus = (FButton) rootView.findViewById(R.id.button4);
		minus = (FButton) rootView.findViewById(R.id.button5);
		power = (FButton) rootView.findViewById(R.id.button29);
		exp = (FButton) rootView.findViewById(R.id.button7);
		four = (FButton) rootView.findViewById(R.id.button8);
		five = (FButton) rootView.findViewById(R.id.button9);
		six = (FButton) rootView.findViewById(R.id.button10);
		times = (FButton) rootView.findViewById(R.id.button11);
		divide = (FButton) rootView.findViewById(R.id.button12);
		seven = (FButton) rootView.findViewById(R.id.button15);
		eight = (FButton) rootView.findViewById(R.id.button16);
		nine = (FButton) rootView.findViewById(R.id.button17);
		log = (FButton) rootView.findViewById(R.id.button19);
		ln = (FButton) rootView.findViewById(R.id.button20);
		del = (ImageButton) rootView.findViewById(R.id.button21);
		zero = (FButton) rootView.findViewById(R.id.button23);
		dot = (FButton) rootView.findViewById(R.id.button24);
		clr = (FButton) rootView.findViewById(R.id.button30);
		openbracket = (FButton) rootView.findViewById(R.id.button26);
		closebracket = (FButton) rootView.findViewById(R.id.button27);
		sqrt = (FButton) rootView.findViewById(R.id.button25);
		// submit = (FButton) rootView.findViewById(R.id.button6);
		pi = (FButton) rootView.findViewById(R.id.button31);

		pi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userInput.add(index, "3.14");
				expression.add(index, "3.14");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		one.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "1");
				expression.add(index, "1");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});

		two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "2");
				expression.add(index, "2");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});

		three.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "3");
				expression.add(index, "3");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}

		});
		four.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				userInput.add(index, "4");
				expression.add(index, "4");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		five.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "5");
				expression.add(index, "5");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});

		six.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				userInput.add(index, "6");
				expression.add(index, "6");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		seven.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				userInput.add(index, "7");
				expression.add(index, "7");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		eight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "8");
				expression.add(index, "8");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		nine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "9");
				expression.add(index, "9");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}

		});
		zero.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "0");
				expression.add(index, "0");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "+");
				expression.add(index, "+");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});

		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "-");
				expression.add(index, "-");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		times.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "*");
				expression.add(index, "*");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		divide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "/");
				expression.add(index, "/");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});

		power.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "^");
				expression.add(index, "^");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		exp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "exp");
				expression.add(index, "exp");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		log.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "log");
				expression.add(index, "log");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}

		});
		ln.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "ln");
				expression.add(index, "ln");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		dot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, ".");
				expression.add(index, ".");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		openbracket.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "(");
				expression.add(index, "(");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		closebracket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, ")");
				expression.add(index, ")");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		sqrt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userInput.add(index, "sqrt");
				expression.add(index, "sqrt");
				index++;
				mathExpression.append(userInput.get(index - 1));
			}
		});
		clr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index--;
				userInput.add(index, "");
				expression.add(index, "");
				index++;
				mathExpression.setText(userInput.get(index - 1));
			}

		});
		del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (index != 0) {
					index--;
					userInput.remove(index);
					expression.remove(index);
					mathExpression.setText("");
					for (int i = 0; i < index; i++) {
						mathExpression.append(userInput.get(i));

					} 
				}
			}
		});

	}

	private void setListView() {
		// TODO Auto-generated method stub
		// gS = (GlobalState) expressionCallbacks.getContext();
		HashMap<String, Formula> f = gS.getSensor().getFormulaContainer().getFc();
		ArrayList<Formula> variableList = new ArrayList<Formula>();
		// variableList.add(new Formula("Select a Variable", ""));
		// adding the initial parameter to the list, it's value=incoming data
		// variableList.add(new Formula(s,""));
		Log.e("dekhoooooooooo", "Activity created properly");

		
		for (Map.Entry<String, Formula> e : f.entrySet()) {
			variableList.add(e.getValue());
		}

		ArrayAdapter<Formula> af = new ArrayAdapter<Formula>(getActivity(),
				R.layout.variable_list, R.id.list_item, variableList);

		variableListHolder.setAdapter(af);
		variableListHolder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Formula proc = (Formula) variableListHolder.getItemAtPosition(position);

				if (!list.contains(proc.getName())) {
					list.add(proc.getName());
				}

				userInput.add(index, proc.toString());
				expression.add(index, proc.getName());
				mathExpression.append(userInput.get(index));
				index++;
				variableListHolder.setSelection(0);

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
