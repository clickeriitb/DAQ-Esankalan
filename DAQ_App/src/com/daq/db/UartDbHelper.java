package com.daq.db;

import java.util.ArrayList;

import com.idl.daq.L;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UartDbHelper extends SQLiteOpenHelper {
	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "SensorDatabase.db";

	public static final String UART_TABLE_NAME = "UART";
	public static final String UART_SENSOR_CODE = "sensor_code";
	public static final String UART_QUANTITY = "quantity";
	public static final String UART_UNIT = "unit";
	public static final String UART_PIN_RX = "pin_rx";
	public static final String UART_PIN_TX = "pin_tx";
//	public static final String UART_PIN_PROTOCOL = "sub_protocol";
	public static final String UART_BAUD_RATE = "baud_rate";
	public static final String UART_COMMAND = "command";
	public static final String UART_BYTES = "byte";
	public static final String UART_KEY = "_id";

	public SQLiteDatabase sqlDB = null;
	ArrayList<String> uartCodes = new ArrayList<String>();
	ArrayList<String> uartQuantities = new ArrayList<String>();
	ArrayList<String> uartUnits = new ArrayList<String>();
	
	public static final String[] projection = {UART_SENSOR_CODE,UART_QUANTITY,UART_UNIT }; 


	public static final String UART_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS "
			+ UART_TABLE_NAME + " (" + UART_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + UART_SENSOR_CODE
			+ " TEXT, " + UART_QUANTITY + " TEXT, " + UART_UNIT + " TEXT, "
			+ UART_PIN_RX + " TEXT, " + UART_PIN_TX + " TEXT, " + UART_BAUD_RATE + " NUMERIC, " + UART_COMMAND
			+ " NUMERIC, " + UART_BYTES + " NUNERIC, UNIQUE ("
			+ UART_SENSOR_CODE + ", " + UART_QUANTITY + ", " + UART_UNIT
			+ ") ON CONFLICT REPLACE);";
	
	
	
	public UartDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		L.d("on create database called");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		L.d("on upgrade database called");
		db.execSQL("DROP TABLE IF EXISTS UART;DROP TABLE IF EXISTS UART_FORMULA");
		onCreate(db);
		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		L.d("on open database called");
		db.execSQL(UART_CREATE_SCRIPT);
	}
	
	public void openDB() {
		// TODO Auto-generated method stub
		if(sqlDB==null){
			sqlDB = getWritableDatabase();
		}	
	}
	
	public void loadEntries() {
		// TODO Auto-generated method stub
		
		Cursor c = sqlDB.query(UART_TABLE_NAME, projection, null, null, null, null, null);
		
		uartCodes.clear();
		uartQuantities.clear();
		uartUnits.clear();
		
		L.d("before move to first");
		if(c.moveToFirst())
			
		{	L.d("after move to first");
		do{
			L.d("enters do");
			uartCodes.add(c.getString(c.getColumnIndex(UART_SENSOR_CODE)));
			L.d("uart sensor code");
			uartQuantities.add(c.getString(c.getColumnIndex(UART_QUANTITY)));
			uartUnits.add(c.getString(c.getColumnIndex(UART_UNIT)));
		}while(c.moveToNext());
		L.d("method ends");
	}
		}

	
	public ArrayList<String> getUartCodes(){
		return uartCodes;
	}
	
	public ArrayList<String> getUartQuantities(){
		return uartQuantities;
	}
	
	public ArrayList<String> getUartUnits(){
		return uartUnits;
	}
	
	public SQLiteDatabase getSqlDB() {
		return sqlDB;
	}
	
	public void test() {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(UART_SENSOR_CODE, "GY-26");
		values.put(UART_QUANTITY, "Angle");
		values.put(UART_UNIT, "Degree");
		values.put(UART_PIN_RX, "P9_26");
		values.put(UART_PIN_TX, "P9_24");
		values.put(UART_BAUD_RATE, 9600);
		values.put(UART_COMMAND, 31);
		values.put(UART_BYTES, 8);
		
		sqlDB.insert(UART_TABLE_NAME, null, values);
//			
	}


	public Cursor getSensorsFor(String s){
		Cursor c;
		String query = "SELECT * FROM "+UART_TABLE_NAME+" WHERE "+UART_SENSOR_CODE+" LIKE '"+s+"%' OR "+UART_QUANTITY+" LIKE '"+s+"%';";
		L.d(query);
		c = sqlDB.rawQuery(query, null);
		L.d(c.toString());
		L.d(c.getCount());
		return c;
	}

}