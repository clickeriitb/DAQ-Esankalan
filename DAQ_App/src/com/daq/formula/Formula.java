package com.daq.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.idl.daq.GlobalState;
import com.idl.daq.L;

import android.util.Log;
import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import expr.Variable;

public class Formula {

	private String name, displayName;
	private String expression, displayExpression;
	private double value;
	private HashMap<String,Formula> variables;
	private ArrayList<Variable> var;
	
	public Formula(String name, String expression) {
		super();
		this.name = name;
		this.expression = expression;
		
		variables = new LinkedHashMap<>();
		var = new ArrayList<>();
		this.displayName = name;
	}
	
	public Formula(String name, String expression,String displayName,String displayExpression) {
		super();
		this.name = name;
		this.expression = expression;
		
		variables = new LinkedHashMap<>();
		var = new ArrayList<>();
		this.displayName = displayName;
		this.displayExpression = displayExpression;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return displayName;
	}
	
	
	public void addToHashMap(String s, Formula f)
	{
		variables.put(s, f);
		var.add(Variable.make(s));
	}
	
	public void addVariable(Variable v){
		var.add(v);
	}
	
	public String getName(){
		return name;
	}
	
	public double getValue(){
		
		return value;
	}
	
	public void evaluate()
	{
		Expr expr = null;
		
		try { 
			expr = Parser.parse(expression);
			Log.e("evaluate ",expr.toString());
			L.d(expression);
			Variable v;
//			if(variables == null){
//				Log.e("var","What the hell!");
//			}
//			Double d;
			Formula f;
			if(variables.size()!=0){
				Log.e("varsize","variable size not 0");
				for(int i=0;i<var.size();++i){
					v = var.get(i);
					f = variables.get(v.toString());
					if(f!=null){
						L.d("under evaluation "+f.getName()+" "+f.getDisplayName()+" not null");
					}
					v.setValue(f.getValue());
				}
				value = expr.value();
			}
		}
		catch (SyntaxException e) {
		    System.err.println(e.explain());
		    
		}
		L.d("formula "+name+" value:"+value);
		
	}
	
	public void setValue(double value){
		this.value = value;
	}
	
	public ArrayList<Variable> getAllVariables(){
		return var;
	}
	
	public String getFormulaString(){
		return displayName+"="+displayExpression;
	}
	
	public String getExpression(){
		return expression;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public String getDisplayExpression(){
		return displayExpression;
	}

	public void logAllVariables() {
		// TODO Auto-generated method stub
		L.d("logging variables for formula "+name);
		for(Map.Entry<String, Formula> e : variables.entrySet()){
			L.d(e.getKey()+" "+e.getValue().getName());
		}
	}
}
