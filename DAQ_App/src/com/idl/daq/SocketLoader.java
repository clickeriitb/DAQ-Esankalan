package com.idl.daq;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SocketLoader extends IntentService{

	GlobalState gS;
	String TAG = "Socket.io";
		
	public SocketLoader() {
		super("com.idl.daq.SocketLoader");
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		gS = (GlobalState) getApplicationContext();
		try {
			gS.socket = new SocketIO("http://"+ gS.ip);
			L.d(gS.ip);
			gS.socket.connect(new IOCallback() {
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
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		L.e("intent service Destroyed");
		
		super.onDestroy();
	}

}
