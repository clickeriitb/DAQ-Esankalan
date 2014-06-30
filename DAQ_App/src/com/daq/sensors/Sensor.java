package com.daq.sensors;

import java.util.ArrayList;

import javax.crypto.spec.DESKeySpec;

import org.json.JSONObject;

import android.content.Context;

import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.idl.daq.L;
import com.idl.daq.SensorDetailFragment;
import com.idl.daq.DetailsFrag;

public abstract class Sensor {
	String sensorName,desc;
	int id;
	public DetailsFrag dataFrag;
	FormulaContainer fc;
	//most probably the graph data series will be defined here
	double minThresh,maxThresh;

	static private int sensorCount = 0;
	
	public Sensor(String sensorName,FormulaContainer fc) {
		super();
		this.sensorName = sensorName;
		this.fc = fc;
		sensorCount++;
		id = sensorCount;
		dataFrag = new DetailsFrag();
		//dataFrag.setRetainInstance(true);
		dataFrag.setSensor(this);
		
	}
	
	public Sensor(){
		dataFrag = new DetailsFrag();
		dataFrag.setSensor(this);
	}

	public void initializeFrag(){
		dataFrag = new DetailsFrag();
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

	//FormulaContainer related methods
	public void destroyFc(){
		fc.clear();
	}
	public FormulaContainer getFormulaContainer() {
		return fc;
	}

	public void setFormulaContainer(FormulaContainer fc) {
		this.fc = fc;
	}
	
	public void addToFc(Formula f)
	{
		fc.put(f.toString(), f);
	}

	public int getId() {
		return id;
	}
	
	//returns the jSon of the particular classes
	public abstract JSONObject getJSON();

	public DetailsFrag getDataFrag(Context c) {
		dataFrag.setContext(c);
		return dataFrag;
	}
	
	public void setThresh(double min,double max){
		minThresh = min;
		maxThresh = max;
	}

	public double getMinThresh() {
		return minThresh;
	}

	public double getMaxThresh() {
		return maxThresh;
	}
	
	public abstract String display();
	public abstract String getQuantity();
	
	

	
	//Getters and Setters
	
	public DetailsFrag getDataFrag() {
		return dataFrag;
	}

	public void setDataFrag(DetailsFrag dataFrag) {
		this.dataFrag = dataFrag;
	}

	public void setFc(FormulaContainer fc) {
		this.fc = fc;
	}

	public void setMinThresh(double minThresh) {
		this.minThresh = minThresh;
	}

	public void setMaxThresh(double maxThresh) {
		this.maxThresh = maxThresh;
	}

}
