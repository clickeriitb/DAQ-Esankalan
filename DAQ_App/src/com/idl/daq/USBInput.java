package com.idl.daq;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.idl.daq.USBEngine.USBCallback;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

public class USBInput extends IntentService{
	
	private static final int BUFFER_SIZE = 1024;

	GlobalState gS;
	USBEngine usb;
	private UsbManager mUsbManager;
	private USBCallback mCallback;
	private UsbAccessory mAccessory;
	private ParcelFileDescriptor mParcelFileDescriptor = null;
	private FileDescriptor mFileDescriptor = null;
	private FileInputStream mInputStream = null;
	private FileOutputStream mOutputStream = null;

	public USBInput() {
		super("com.idl.daq.USBInput");
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		//Testing code
//		JSONObject json;
//		gS = (GlobalState) getApplicationContext();
//		ArrayList<JSONObject> temp = gS.getTemp();
//		while(true){
//			json = new JSONObject();
//			
//			try {
//				json.put("sensor_code", "abc");
//				json.put("data", "1.618");
//				json.put("date", "2313");
//				temp.add(json);
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			L.d("added "+json.toString());
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		//Actual code
		gS = (GlobalState) getApplicationContext();
		usb = gS.getUsb();
		mUsbManager = usb.getUsbManager();
		mCallback = usb.getUsbCallBack();
		mAccessory = usb.getUsbAccessory();
		mParcelFileDescriptor = usb.getParcel();
		mInputStream = usb.getInputStream();
		mOutputStream = usb.getOutputStream();
		int count = 0;
		L.d("adding....");
//		while(true){
//			gS.addToTemp(count + " hi");
//			count++;
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		L.d("open connection");
//		mParcelFileDescriptor = mUsbManager.openAccessory(mAccessory);
//		if (mParcelFileDescriptor == null) {
//			L.e("could not open accessory");
//			mCallback.onConnectionClosed();
//			return;
//		}
//		mFileDescriptor = mParcelFileDescriptor.getFileDescriptor();
//		mInputStream = new FileInputStream(mFileDescriptor);
//		mOutputStream = new FileOutputStream(mFileDescriptor);
		mCallback.onConnectionEstablished();
		usb.setAccessoryConnected(true);

		//DataInputStream dis = new DataInputStream(mInputStream);
		String message;
		
		L.d("Hello");
		while (!usb.mQuit.get()) {
			try {
				message = "";
				//Toast.makeText(gS, "input", Toast.LENGTH_SHORT).show();
				byte[] buf = new byte[BUFFER_SIZE];
				//message = dis.readUTF();
				L.d("hi");
				int read = mInputStream.read(buf);
				for(int i=0;i<read;++i){
					message += (char)buf[i];
				}
				mCallback.onDataRecieved(message);
				//Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				if(message.equals("exit")){
					usb.mQuit.set(true);
				}
				//Thread.sleep(8000);
			} catch (Exception e) {
				L.e("ex " + e.getMessage());
				break;
			}
			count++;
		}
		L.d("exiting reader thread");
		mCallback.onConnectionClosed();

		if (mParcelFileDescriptor != null) {
			try {
				mParcelFileDescriptor.close();
			} catch (IOException e) {
				L.e("Unable to close ParcelFD");
			}
		}

		if (mInputStream != null) {
			try {
				mInputStream.close();
			} catch (IOException e) {
				L.e("Unable to close InputStream");
			}
		}

		if (mOutputStream != null) {
			try {
				mOutputStream.close();
			} catch (IOException e) {
				L.e("Unable to close OutputStream");
			}
		}

		usb.setAccessoryConnected(false);
		L.d("usb set Accessory Connected false");
		usb.mQuit.set(false);
		L.d("mQuit false");
		usb.onDestroy();
		gS.finishUSB();
		L.d("finish USB");
//	//	sAccessoryThread = null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		L.e("Destroyed");
		
		super.onDestroy();
	}

	
}
