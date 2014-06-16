
package com.idl.daq;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.AdcProc;
import com.daq.sensors.Sensor;
import com.daq.sensors.UartProc;
import com.idl.daq.USBEngine.USBCallback;

import android.app.Application;
import android.content.Intent;

public class GlobalState extends Application{
	
	boolean isUsb,isExiting;
	USBEngine usb=null;
	String protocol="",globalString;

	ArrayList<Sensor> sensors;
	
	ArrayList<JSONObject> temp;
	
	FormulaContainer tempFc;
	
	public void initializeFc(){
		tempFc = new  FormulaContainer();
	}
	
	public void destroyFc(){
		tempFc = null;
	}
	
	public FormulaContainer getfc()
	{
		return tempFc;
	}
	
	public void addToFc(Formula f)
	{
		tempFc.put(f.toString(), f);
	}
	
	//returns the number of formulas for a sensor
	public int getFormNum()
	{
		return tempFc.getFc().size();
	}
	
	public GlobalState() {
		super();
		sensors = new ArrayList<Sensor>();
		temp = new ArrayList<JSONObject>();
		isExiting = true;
	}

//	public void addToTemp(String s){
//		L.d("added " + s);
//		temp.add(s);
//	}
	
	public ArrayList<JSONObject> getTemp(){
		return temp;
	}
	
	public boolean isExiting() {
		return isExiting;
	}

	public void setExiting(boolean isExiting) {
		this.isExiting = isExiting;
	}

	public boolean isUsb() {
		return isUsb;
	}

	public void setUsb(boolean isUsb) {
		this.isUsb = isUsb;
	}


	public ArrayList<Sensor> getSensors() {
		return sensors;
	}
	
	public void addAdcProc(AdcProc a){
		sensors.add(a);
	}
	
	public void addUartProc(UartProc u){
		sensors.add(u);
	}
	
	public void addSensor(Sensor s){
		sensors.add(s);
	}
	
	public void clear(){
		if(isExiting){
			sensors.clear();
		}
	}
	

	
	public ArrayList<String> getAllSensorNames(){
		ArrayList<String> allSensorNames = new ArrayList<String>();
		for(Sensor s : sensors){
			allSensorNames.add(s.getSensorName());
		}
		return allSensorNames;
	}

	public Sensor getSensorById(int id){
		return sensors.get(id);
	}

	public void initiateUSB() {
		// TODO Auto-generated method stub
		L.d("Initiating USB");
		if(usb==null){
			usb = new USBEngine(this, usbCallB);
		}
		usb.onNewIntent();
	}

	
	public void finishUSB(){
		usb = null;
	}

	public USBEngine getUsb() {
		return usb;
	}

	public String getProtocol()
	{
		return protocol;
	}
	
	public void setProtocol(String p)
	{
		protocol = p;
	}
	
	
	public void setGlobalString(String s)
	{
		globalString = s;
	}
	
	public String getGlobalString()
	{
		return globalString;
	}
	


	private USBCallback usbCallB = new USBCallback(){

		@Override
		public void onDeviceDisconnected() {
			L.d("device physically disconnected");
		}

		@Override
		public void onConnectionEstablished() {
			L.d("device connected! ready to go!");
		}

		@Override
		public void onConnectionClosed() {
			L.d("connection closed");
		}

		@Override
		public void onDataRecieved(String message) {
			L.d("received message : %s", message);
			try {
				temp.add(new JSONObject(message));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	values.add("BBB : " + message);
		//	arrayAdapter.notifyDataSetChanged();
			//showToast(message);
//			for(int i=0;i<data.length;++i)
//			L.d("byte %d : %d ",i,data[i]);
		}

		@Override
		public void onNoUSB(Intent i) {
			// TODO Auto-generated method stub
			startActivity(i);
		}

		@Override
		public void startUSBInput() {
			// TODO Auto-generated method stub
			Intent i = new Intent(getApplicationContext(),USBInput.class);
			startService(i);
		}
		
		
		
	};
	
	
	
}
