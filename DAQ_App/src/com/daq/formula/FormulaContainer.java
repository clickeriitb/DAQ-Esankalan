package com.daq.formula;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
	
	
	
	

}
