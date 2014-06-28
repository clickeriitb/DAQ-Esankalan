
package com.idl.daq;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.daq.db.AdcDbHelper;
import com.daq.db.I2CDbHelper;
import com.daq.db.UartDbHelper;
import com.daq.formula.Formula;
import com.daq.formula.FormulaContainer;
import com.daq.sensors.AdcProc;
import com.daq.sensors.I2CProc;
import com.daq.sensors.Sensor;
import com.daq.sensors.UartProc;
import com.idl.daq.USBEngine.USBCallback;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GlobalState extends Application{
	
	boolean isUsb,isExiting;
	USBEngine usb=null;
	String protocol="",globalString;

	ArrayList<Sensor> sensors;
	
	ArrayList<JSONObject> temp;
	
	//FormulaContainer tempFc;
	Sensor tempSensor;
	
	SocketIO socket;
	String ip="192.168.1.145:3000/wifi";
	String TAG = "Socket.io";
	
	//database
	AdcDbHelper mADCHelper;
	UartDbHelper mUartHelper;
	I2CDbHelper mI2CHelper;
	
	//Constructor
	public GlobalState() {
		super();
		sensors = new ArrayList<Sensor>();
		temp = new ArrayList<JSONObject>();
		isExiting = true;
	}
	
	public AdcDbHelper getAdcDbHelper(){
		return mADCHelper;
	}
	
	public UartDbHelper getUartDbHelper() {
		// TODO Auto-generated method stub
		return mUartHelper;
	}
	
	public I2CDbHelper getI2CDbHelper(){
		return mI2CHelper;
	}
	
	public void initializeDB(){
		mADCHelper = new AdcDbHelper(this);
		mADCHelper.openDB();
		mADCHelper.test();
		mADCHelper.loadEntries();
		L.d("Adc created");
		mUartHelper = new UartDbHelper(this);
		L.d("Uart open db");
		mUartHelper.openDB();
		L.d("Uart opened db");
		mUartHelper.test();
		L.d("Uart test called");
		mUartHelper.loadEntries();
		L.d("Loaded uart entries");
		
		mI2CHelper = new I2CDbHelper(this);
		mI2CHelper.openDB();
		mI2CHelper.test();
		mI2CHelper.loadEntries();
	}
	
	public void initializeSensor(){
		if(protocol.equals("ADC")){
			tempSensor = new AdcProc();
		}else if(protocol.equals("UART")){
			tempSensor = new UartProc();
		}else if(protocol.equals("I2C")){
			tempSensor =  new I2CProc();
		}
		
	}
	
	
	public Sensor getSensor(){
		return tempSensor;
	}
	
	
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
	
	
	public void startSocket(){
		//gS = (GlobalState) getApplicationContext();
		try {
			socket = new SocketIO("http://"+ ip);
			L.d(ip);
			socket.connect(new IOCallback() {
				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
					try {
						System.out.println("Obj Received");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onMessage(String data, IOAcknowledge ack) {
					System.out.println(data);
					Log.v("reading : ", data);
					//writeReading(data);
					//int x = Integer.parseInt(data);
					//mActivity.get().temp.add(x);
					//socket.emit("receive", "Got It!!");
				}

				
				@Override
				public void onError(SocketIOException socketIOException) {
					Log.d(TAG, "an Error occured\n");
					L.d("an Error occured\n");
					//gS.socket.reconnect();
					//writeToConsole("an Error occured\n");
					socketIOException.printStackTrace();
				}

				@Override
				public void onDisconnect() {
					Log.d(TAG, "Connection terminated.\n");
					//writeToConsole("Connection terminated.\n");
				}

				@Override
				public void onConnect() {
					L.d("Connection established\n");
					//writeToConsole("Connection established\n");
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					Log.d(TAG, "Reading : " + args[0]);
					Log.d(TAG,args[0].toString());
					try {
						temp.add(new JSONObject(args[0].toString()));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//writeReading((String)args[0]);
					//writeToConsole("Server triggered event '" + event + "'\n");
				}
			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.e("socket err", e1.toString());
			e1.printStackTrace();
		}

		IOAcknowledge ioAck = new IOAcknowledge() {
			@Override
			public void ack(Object... args) {
				Log.d(TAG, "Server acknowledges this package.\n");
				//writeToConsole("Server acknowledges this package.\n");
			}
		};
		L.d("Started socket");
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
			//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			L.d("received message : %s", message);
			try {
//				JSONArray j = new JSONArray(message);
				JSONObject j = new JSONObject(message);
				temp.add(j);
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
