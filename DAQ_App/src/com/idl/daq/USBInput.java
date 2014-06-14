package com.idl.daq;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.idl.daq.USBEngine.USBCallback;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;

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
		mParcelFileDescriptor = mUsbManager.openAccessory(mAccessory);
		if (mParcelFileDescriptor == null) {
			L.e("could not open accessory");
			mCallback.onConnectionClosed();
			return;
		}
		mFileDescriptor = mParcelFileDescriptor.getFileDescriptor();
		mInputStream = new FileInputStream(mFileDescriptor);
		mOutputStream = new FileOutputStream(mFileDescriptor);
		mCallback.onConnectionEstablished();
		usb.setAccessoryConnected(true);

		//DataInputStream dis = new DataInputStream(mInputStream);
		String message;
		
		L.d("Hello");
		while (!usb.mQuit.get() && count<20) {
			try {
				message = "";
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
		usb.mQuit.set(false);
		usb.onDestroy();
		gS.finishUSB();
//	//	sAccessoryThread = null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		L.e("Destroyed");
		
		super.onDestroy();
	}

	
}
