package com.daq.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.idl.daq.GlobalState;

import android.util.Log;
import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import expr.Variable;

public class Formula {

	private String name;
	private String expression;
	private double value;
	private HashMap<String,Formula> variables;
	private ArrayList<Variable> var;	
	
	public Formula(String name, String expression) {
		super();
		this.name = name;
		this.expression = expression;
		
		variables = new LinkedHashMap<>();
		var = new ArrayList<>();
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public void addToHashMap(String s, Formula f)
	{
		variables.put(s, f);
		var.add(Variable.make(s));
	}
	
	public void addVariable(Variable v){
		var.add(v);
	}
	
	public double getValue(){
		
		return value;
	}
	
	public void evaluate()
	{
		Expr expr = null;
		
		try { 
			expr = Parser.parse(expression);
			Log.e("evaluate",expr.toString());
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
					v.setValue(f.getValue());
				}
				value = expr.value();
			}
		}
		catch (SyntaxException e) {
		    System.err.println(e.explain());
		    
		}
		
	}
	
	public void setValue(double value){
		this.value = value;
	}
	
	public ArrayList<Variable> getAllVariables(){
		return var;
	}
}
