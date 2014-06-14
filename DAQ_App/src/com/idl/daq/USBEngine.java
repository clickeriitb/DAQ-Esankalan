package com.idl.daq;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.widget.Button;

public class USBEngine {

	private static final int BUFFER_SIZE = 1024;
	private final Context mContext;
	private final UsbManager mUsbManager;
	private final USBCallback mCallback;

	private final static String ACTION_ACCESSORY_DETACHED = "android.hardware.usb.action.USB_ACCESSORY_DETACHED";
	private static final String ACTION_USB_PERMISSION = "ch.nexuscomputing.simpleaccessory.USB_PERMISSION";
	private volatile boolean mAccessoryConnected = false;
	public final AtomicBoolean mQuit = new AtomicBoolean(false);

	private UsbAccessory mAccessory = null;

	private ParcelFileDescriptor mParcelFileDescriptor = null;
	private FileDescriptor mFileDescriptor = null;
	private FileInputStream mInputStream = null;
	private FileOutputStream mOutputStream = null;
	
	private Button sendButton;

	public interface USBCallback {
		void onConnectionEstablished();

		void onDeviceDisconnected();

		void onConnectionClosed();

		void onDataRecieved(String message);
		
		void onNoUSB(Intent i);
		
		void startUSBInput();
	}

	public USBEngine(Context applicationContext, USBCallback callback) {
		mContext = applicationContext;
		mCallback = callback;
		mUsbManager = (UsbManager) mContext
				.getSystemService(Context.USB_SERVICE);
		mContext.registerReceiver(mDetachedReceiver, new IntentFilter(
				ACTION_ACCESSORY_DETACHED));
		//this.sendButton = sendButton;
	}

	public UsbManager getUsbManager(){
		return mUsbManager;
	}
	
	public USBCallback getUsbCallBack(){
		return mCallback;
	}
	
	public UsbAccessory getUsbAccessory(){
		return mAccessory;
	}
	
	public boolean getAccessoryConnected(){
		return mAccessoryConnected;
	}
	
	public void setAccessoryConnected(boolean b){
		mAccessoryConnected = b;
	}
	public void onNewIntent() {
		if (mUsbManager.getAccessoryList() != null) {
			mAccessory = mUsbManager.getAccessoryList()[0];
			connect();
		} 
//		else {
//			Intent socketIntent = new Intent(mContext,SocketLoaderUi.class);
//			mCallback.onNoUSB(socketIntent);
//		}
	}

	public ParcelFileDescriptor getParcel(){
		return mParcelFileDescriptor;
	}
	
	public FileInputStream getInputStream(){
		return mInputStream;
	}
	
	public FileOutputStream getOutputStream(){
		return mOutputStream;
	}
	
	
	private void connect() {
		if (mAccessory != null) {
			L.d("mAccessory not null");
			if(!mUsbManager.hasPermission(mAccessory)){
				L.d("mUsbManager does not have permissions");
				mContext.registerReceiver(mPermissionReceiver, new IntentFilter(ACTION_USB_PERMISSION));
				PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
				L.d("Requesting permission");
				mUsbManager.requestPermission(mAccessory, pi);
				L.d("Requested permission");
				return;
			}else{
				L.d("mUsbManager has permissions");
			}
//			if (sAccessoryThread == null) {
//				sAccessoryThread = new Thread(mAccessoryRunnable,
//						"Reader Thread");
//				sAccessoryThread.start();
//			} else {
//				L.d("reader thread already started");
//			}
//			L.d("open connection");
//			mParcelFileDescriptor = mUsbManager.openAccessory(mAccessory);
//			if (mParcelFileDescriptor == null) {
//				L.e("could not open accessory");
//				mCallback.onConnectionClosed();
//				return;
//			}
//			mFileDescriptor = mParcelFileDescriptor.getFileDescriptor();
//			mInputStream = new FileInputStream(mFileDescriptor);
//			mOutputStream = new FileOutputStream(mFileDescriptor);
//			mCallback.onConnectionEstablished();
//			mAccessoryConnected = true;
//
//			//DataInputStream dis = new DataInputStream(mInputStream);
//			String message = "";
//			
//			byte[] buf = new byte[BUFFER_SIZE];
//			L.d("hi");
//			while (!mQuit.get()) {
//				L.d("Hello");
//				try {
//					//byte[] buf = new byte[BUFFER_SIZE];
//					//message = dis.readUTF();
//					L.d("inputting to buffer");
//					int read = mInputStream.read(buf);
//					L.d("%d bytes read",read);
//					//mCallback.onDataRecieved(message);
//					for(int i=0;i<read;++i){
//						message += buf[i];
//					}
//					sendButton.setText(message);
//					//Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
////					if(message.equals("exit")){
////						mQuit.set(true);
////					}
//					sendButton.setEnabled(true);
//				} catch (Exception e) {
//					L.e("ex " + e.getMessage());
//					//break;
//				}
//			}
//			L.d("exiting reader thread");
//			mCallback.onConnectionClosed();
//
//			if (mParcelFileDescriptor != null) {
//				try {
//					mParcelFileDescriptor.close();
//				} catch (IOException e) {
//					L.e("Unable to close ParcelFD");
//				}
//			}
//
//			if (mInputStream != null) {
//				try {
//					mInputStream.close();
//				} catch (IOException e) {
//					L.e("Unable to close InputStream");
//				}
//			}
//
//			if (mOutputStream != null) {
//				try {
//					mOutputStream.
//			close();
//				} catch (IOException e) {
//					L.e("Unable to close OutputStream");
//				}
//			}
//
//			mAccessoryConnected = false;
//			mQuit.set(false);
//			sAccessoryThread = null;
			
//			if (sAccessoryThread == null) {
//				sAccessoryThread = new Thread(mAccessoryRunnable,
//						"Reader Thread");
//				sAccessoryThread.start();
//			} else {
//				L.d("reader thread already started");
//			}
			mCallback.startUSBInput();
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
			mAccessoryConnected = true;
		} else {
			L.d("accessory is null");
		}
	}

	public void onDestroy() {
		//closeConnection();
		write("","exit");
		mQuit.set(true);
		mContext.unregisterReceiver(mDetachedReceiver);
		sAccessoryThread = null;
		L.d("mEngine destroying done");
	}

	private final BroadcastReceiver mDetachedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null
					&& ACTION_ACCESSORY_DETACHED.equals(intent.getAction())) {
				mCallback.onDeviceDisconnected();
				mQuit.set(true);
			}
		}
	};
	
	private final BroadcastReceiver mPermissionReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			mContext.unregisterReceiver(mPermissionReceiver);
			 if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
				 connect();
			 }
		}
	};

	public void write(byte[] data) {
		if (mAccessoryConnected && mOutputStream != null) {
//			try {
//				mOutputStream.write(data);
//			} catch (IOException e) {
//				L.e("could not send data");
//			}
		} else {
			L.d("accessory not connected");
		}
	}
	
	public void write(String message,String id){
		if (mAccessoryConnected){ 
			
			if(mOutputStream != null) {
		
			
			try {
					JSONObject json = new JSONObject();
					try {
						json.put("objId", id);
						json.put("message", message);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte[] msgBuff;
					msgBuff = json.toString().getBytes("UTF-8");
					//msgBuff = message.getBytes("UTF-8");
					mOutputStream.write(msgBuff);
					//sendButton.setEnabled(false);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					L.e("Cannot send message : %s",message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
				}
			} else {
				L.d("OutputStream is null");
			}
		} else {
			L.d("accessory not connected");
		}
	}
	
	public void write(JSONObject json){
		if (mAccessoryConnected){ 
			
			if(mOutputStream != null) {
		
			
			try {
//					JSONObject json = new JSONObject();
//					try {
//						json.put("objId", id);
//						json.put("message", message);
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
						
						
						json.put("rate", 1000);
						 
						byte[] msgBuff;
						msgBuff = json.toString().getBytes("UTF-8");
						//msgBuff = message.getBytes("UTF-8");
						mOutputStream.write(msgBuff);
						//sendButton.setEnabled(false);
			}
				catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
					
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					L.e("Cannot send message : %s",json);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					
				}
			} else {
				L.d("OutputStream is null");
			}
		} else {
			L.d("accessory not connected");
		}
	}

		
	private static Thread sAccessoryThread;
	private final Runnable mAccessoryRunnable = new Runnable() {
		@Override
		public void run() {
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
			mAccessoryConnected = true;

			//DataInputStream dis = new DataInputStream(mInputStream);
			String message;
			
			L.d("Hello");
			while (!mQuit.get()) {
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
						mQuit.set(true);
					}
					//Thread.sleep(8000);
				} catch (Exception e) {
					L.e("ex " + e.getMessage());
					//break;
				}
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

			mAccessoryConnected = false;
			mQuit.set(false);
			sAccessoryThread = null;
		}
	};
}
