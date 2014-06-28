package com.daq.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.idl.daq.L;

import android.util.Log;

public class FormulaContainer {
	
	HashMap<String, Formula> fc = new LinkedHashMap<>();
	
	public void put(String s, Formula f)
	{
		fc.put(s, f);
	}

	public HashMap<String, Formula> getFc() {
		return fc;
	}
	
	public void evaluate(){
		for(Map.Entry<String, Formula> e : fc.entrySet()){
			Log.e("formula",e.getValue().toString());
			e.getValue().evaluate();
		}
	}
	
	public ArrayList<String> getAllFormulaStrings() {
		ArrayList<String> allFormulas = new ArrayList<String>();
		Formula f;
		for (Map.Entry<String, Formula> e : fc.entrySet()) {
			f = e.getValue();
			if (!f.getName().equals(f.getExpression())) {
				allFormulas.add(f.getFormulaString());
			}
		}
		return allFormulas;
	}

	public void clear() {
		// TODO Auto-generated method stub
		Formula f;
		L.d("Displaying old formula strings");
		for(Map.Entry<String, Formula> e : fc.entrySet()){
			L.d(e.getKey()+" "+e.getValue());
		}
		HashMap<String,Formula> tempFc = new HashMap<>();
		for(Map.Entry<String, Formula> e : fc.entrySet()){
			f = e.getValue();
			if(!f.getName().equals(f.getDisplayName())){
				tempFc.put(e.getKey(), e.getValue());
			}
		}
		fc = tempFc;
		L.d("Displaying new formula strings");
		for(Map.Entry<String, Formula> e : fc.entrySet()){
			L.d(e.getKey()+" "+e.getValue());
		}
	}
	
	

}
