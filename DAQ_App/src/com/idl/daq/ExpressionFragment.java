package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.daq.formula.Formula;
import com.idl.daq.FormFragment.Callbacks;

import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExpressionFragment extends Fragment implements OnClickListener {
	
	private Spinner expSpinner;
	private GlobalState gS;
	Intent i;
	private ArrayList<String> list;
	private TextView mathExpression;
	
	private Button one,two,three,four,five,six,seven,eight,nine,times,divide,plus,minus,power,exp,submit;
   	private Button log,ln,del,zero,dot,clr,openbracket,closebracket,sqrt;
    
   	private ArrayList<String> userInput ;
	private int index;
	private Boolean err;
	private String express;
	
	private View rootView;
	
	private Callbacks expressionCallbacks;
	
	public interface Callbacks 
	{
		public void makeToast(String t);
		
		public void openFormula(String s);
		
		public Context getContext();
		
		public void addToVariableList(ArrayList<String> varList);
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

		expressionCallbacks = (Callbacks) activity;
		Log.e("callbacks", "callbacks defined");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.expression,
				container, false);
		Log.e("inflater", "inflated");
		gS = (GlobalState) expressionCallbacks.getContext();
		Log.e("gs", "global state defined");
		defineAttributes();
		Log.e("defineattributes", "called");
		
		return rootView;
	}

	private void defineAttributes() {
		// TODO Auto-generated method stub
		
		expSpinner = (Spinner) rootView.findViewById(R.id.spinner1);
		
		setSpinner();
		
		submit = (Button) rootView.findViewById(R.id.submit);
		submit.setOnClickListener(this);
		list = new ArrayList<>();
		Log.e("dekhoooooooooo", "Activity created properly");
   		userInput= new ArrayList<String>();
   		index=0;
   		err=true;

   		mathExpression = (TextView)rootView.findViewById(R.id.express);
   		
   		
   		one = (Button)rootView.findViewById(R.id.button1);
   		two = (Button)rootView.findViewById(R.id.button2);
   		three = (Button)rootView.findViewById(R.id.button3);
   		plus = (Button)rootView.findViewById(R.id.button4);
   		minus = (Button)rootView.findViewById(R.id.button5);
   		power = (Button)rootView.findViewById(R.id.button29);
   		exp = (Button)rootView.findViewById(R.id.button7);
   		four = (Button)rootView.findViewById(R.id.button8);
   		five = (Button)rootView.findViewById(R.id.button9);
   		six = (Button)rootView.findViewById(R.id.button10);
   		times = (Button)rootView.findViewById(R.id.button11);
   		divide = (Button)rootView.findViewById(R.id.button12);
   		seven  = (Button)rootView.findViewById(R.id.button15);
   		eight = (Button)rootView.findViewById(R.id.button16);
   		nine = (Button)rootView.findViewById(R.id.button17);
   		log = (Button)rootView.findViewById(R.id.button19);
   		ln = (Button)rootView.findViewById(R.id.button20);
   		del = (Button)rootView.findViewById(R.id.button21);
   		zero = (Button)rootView.findViewById(R.id.button23);
   		dot = (Button)rootView.findViewById(R.id.button24);
   		clr = (Button)rootView.findViewById(R.id.button30);
   		openbracket = (Button)rootView.findViewById(R.id.button26);
   	    closebracket = (Button)rootView.findViewById(R.id.button27);
   		sqrt= (Button)rootView.findViewById(R.id.button25);
   		submit=(Button)rootView.findViewById(R.id.button6);
   		
   		one.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				userInput.add(index,"1");
   				index++;	
   				mathExpression.append(userInput.get(index-1));
   			}	
   		});
   		
   		two.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   					userInput.add(index,"2");
   			        index++; 			
   				  mathExpression.append(userInput.get(index-1));
   				}			
   		});
   		
   		three.setOnClickListener(new OnClickListener(){	
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   					userInput.add(index,"3"); 
   				index++;
   				  mathExpression.append(userInput.get(index-1));			
   			}
   			
   		});
   		four.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				
   					userInput.add(index,"4"); 
   					index++;
   				  mathExpression.append(userInput.get(index-1));				
   			}		
   		});
   		five.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   					userInput.add(index,"5");
   				index++;
   		  mathExpression.append(userInput.get(index-1));			
   			}
   		});
   		
   		six.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				
   					userInput.add(index,"6"); 
   				index++;
   				  mathExpression.append(userInput.get(index-1));		
   			}
   		});
   		seven.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				
   			userInput.add(index,"7"); 
   				index++;
   				mathExpression.append(userInput.get(index-1));		
   			}	
   		});
   		eight.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				 userInput.add(index,"8") ;
   				 index++;
   				  mathExpression.append(userInput.get(index-1));				
   			}
   		});
   		nine.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   					userInput.add(index,"9"); 
   				index++;
   				mathExpression.append(userInput.get(index-1));
   			}
   			
   		});
   		zero.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				 userInput.add(index,"0") ; 
   				 index++;
   		         mathExpression.append(userInput.get(index-1));
   		  			}
   		});
   		plus.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   					userInput.add(index,"+");
   			      index++;
   				  mathExpression.append(userInput.get(index-1));
   				}			
   		});
   		
   		minus.setOnClickListener(new OnClickListener(){
    	@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				 userInput.add(index,"-"); 
   				 index++;
				  mathExpression.append(userInput.get(index-1));
				}			
   		});
   		times.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				userInput.add(index,"*");
   				index++;
			 mathExpression.append(userInput.get(index-1));
	    	}
   		});
   		divide.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   					userInput.add(index,"/");
   					index++;
   	   				  mathExpression.append(userInput.get(index-1));
   			}
   		});
   		
   		power.setOnClickListener(new OnClickListener(){
        @Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				 userInput.add(index,"^");
   				 index++;
 				  mathExpression.append(userInput.get(index-1));
				}				
   		});
   		exp.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				userInput.add( index,"exp");
   				index++;
   				  mathExpression.append(userInput.get(index-1));
   			}
   		});
   		   log.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				userInput.add(index,"log");
   				index++;
   			    mathExpression.append(userInput.get(index-1));		
   			}
   			
   		});
   		ln.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				 userInput.add(index,"ln");
   				 index++;	
   				 mathExpression.append(userInput.get(index-1));		
   			}
   		});
   		dot.setOnClickListener(new OnClickListener(){
    	@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				userInput.add(index, ".");
   				index++;
   				  mathExpression.append(userInput.get(index-1));		
   			}
   		});
   		openbracket.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				userInput.add(index,"(");
   				index++;
				  mathExpression.append(userInput.get(index-1));
				}				
   		});
   		closebracket.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				 userInput.add(index,")"); 
   				 index++;
				  mathExpression.append(userInput.get(index-1));		
   			}
   		});
   		sqrt.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				userInput.add(index,"sqrt");  
   				index++;
   				  mathExpression.append(userInput.get(index-1));
   			}
   		});
   		clr.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				index--;
   				userInput.add(index,"");
   				index++;
   				mathExpression.setText(userInput.get(index-1));
   			}
   			
   		});
   		del.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				index--;
   				userInput.remove(index);
   				mathExpression.setText("");
   				for(int i=0;i<index;i++)
   				{
   				  mathExpression.append(userInput.get(i));
   			
   				}
   			}
   		});  		
		
	}

	private void setSpinner() {
		// TODO Auto-generated method stub
		//gS = (GlobalState) expressionCallbacks.getContext();
		HashMap<String,Formula>  f = gS.getfc().getFc();
		ArrayList<Formula> fa = new ArrayList<Formula>();
		fa.add(new Formula("Select a Variable",""));
		String s = gS.getGlobalString();
		//adding the initial parameter to the list, it's value=incoming data
		//fa.add(new Formula(s,""));
		Log.e("dekhoooooooooo", "Activity created properly");
		
		for(Map.Entry<String, Formula> e : f.entrySet())
		{
			fa.add(e.getValue());
		}	
		
		ArrayAdapter<Formula> af = new ArrayAdapter<Formula>(getActivity(),android.R.layout.simple_list_item_1,fa);
		
		expSpinner.setAdapter(af);
		expSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int i, long id) {
				// TODO Auto-generated method stub
				String proc = String.valueOf(expSpinner.getSelectedItem());
				
				if(!proc.equals("Select a Variable") && !list.contains(proc)){
					list.add(proc);
					userInput.add(index,proc);  
					mathExpression.append(userInput.get(index));
				}
				expressionCallbacks.makeToast("Selected"+proc);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		}); 		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.submit){
			
			express = mathExpression.getText().toString();
			err=true;
			Expr expr = null;
			try { 
				expr = Parser.parse(express);
				Log.e("evaluate",expr.toString());
				}
			
			catch (SyntaxException e) {
			    System.err.println(e.explain());
			    Toast.makeText(gS,"Enter valid expression",Toast.LENGTH_SHORT ).show();
			    err=false;
			}
			
			
			if(err)
			{
				//i = getIntent();
				//String name = i.getStringExtra("temp_name");
				gS.setGlobalString(mathExpression.getText().toString());
				Log.e("string", gS.getGlobalString());
				expressionCallbacks.addToVariableList(list);
				//Log.e("globalstring",mathExpression.getText().toString());
				expressionCallbacks.openFormula("back");
				//Log.e("formula opened","");
				//i = new Intent(getApplicationContext(),Form.class);
//				i.putExtra("expr", express);
//				i.putStringArrayListExtra("var_list", list);
//				i.putExtra("temp_name", name);
//				startActivity(i);
				
			}
		}
		
	}
		
	}

