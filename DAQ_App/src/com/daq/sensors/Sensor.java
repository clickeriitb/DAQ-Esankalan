package com.daq.sensors;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;

import com.daq.formula.FormulaContainer;
import com.idl.daq.SensorDetailFragment;

public abstract class Sensor {
	String sensorName,desc;
	int id;
	SensorDetailFragment dataFrag;
	FormulaContainer fc;

	static private int sensorCount = 0;
	
	public Sensor(String sensorName,FormulaContainer fc) {
		super();
		this.sensorName = sensorName;
		this.fc = fc;
		sensorCount++;
		id = sensorCount;
		dataFrag = new SensorDetailFragment();
		//dataFrag.setRetainInstance(true);
		dataFrag.setSensor(this);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sensorName;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public FormulaContainer getFormula() {
		return fc;
	}

	public void setFormulaContainer(FormulaContainer fc) {
		this.fc = fc;
	}

	public int getId() {
		return id;
	}
	
	public abstract JSONObject getJSON();

	public SensorDetailFragment getDataFrag(Context c) {
		dataFrag.setContext(c);
		return dataFrag;
	}
	
	
	
	
	
}
