package com.daq.db;

import java.util.ArrayList;

import com.idl.daq.L;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdcDbHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "SensorDatabase.db";

	public static final String ADC_TABLE_NAME = "ADC";
	public static final String ADC_SENSOR_CODE = "sensor_code";
	public static final String ADC_QUANTITY = "quantity";
	public static final String ADC_UNIT = "unit";
	public static final String ADC_PIN_NUMBER = "pin_number";
	public static final String ADC_KEY = "_id";

	public static final String ADC_FORMULA_TABLE_NAME = "ADC_FORMULA";
	public static final String ADC_FORMULA_NAME = "name";
	public static final String ADC_FORMULA_EXPRESSION = "expression";
	public static final String ADC_FORMULA_VARIABLES = "variables";
	public static final String ADC_FORMULA_KEY = "_id";
	public static final String ADC_FORMULA_SENSOR = "sensor";
	public static final String ADC_FORMULA_DISPLAY_NAME = "displayName";
	public static final String ADC_FORMULA_DISPLAY_EXPRESSION = "displayExpression";

	public SQLiteDatabase sqlDB = null;
	ArrayList<String> adcCodes = new ArrayList<String>();
	ArrayList<String> adcQuantities = new ArrayList<String>();
	ArrayList<String> adcUnits = new ArrayList<String>();

	public static final String[] projection = { ADC_SENSOR_CODE, ADC_QUANTITY,
			ADC_UNIT };

	public static final String ADC_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS "
			+ ADC_TABLE_NAME
			+ " ("
			+ ADC_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ADC_SENSOR_CODE
			+ " TEXT, "
			+ ADC_QUANTITY
			+ " TEXT, "
			+ ADC_UNIT
			+ " TEXT, "
			+ ADC_PIN_NUMBER
			+ " TEXT, UNIQUE ("
			+ ADC_SENSOR_CODE
			+ ", "
			+ ADC_QUANTITY + ", " + ADC_UNIT + ") ON CONFLICT REPLACE);";

	public static final String ADC_FORMULA_CREATE_SCRIPT = " CREATE TABLE IF NOT EXISTS "
			+ ADC_FORMULA_TABLE_NAME
			+ " ("
			+ ADC_FORMULA_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ADC_FORMULA_NAME
			+ " TEXT,"
			+ ADC_FORMULA_DISPLAY_NAME
			+ " TEXT,"
			+ ADC_FORMULA_EXPRESSION
			+ " TEXT, "
			+ ADC_FORMULA_DISPLAY_EXPRESSION
			+ " TEXT, "
			+ ADC_FORMULA_VARIABLES
			+ " TEXT, "
			+ ADC_FORMULA_SENSOR
			+ " INTEGER, FOREIGN KEY("
			+ ADC_FORMULA_SENSOR
			+ ") REFERENCES "
			+ ADC_TABLE_NAME
			+ "("
			+ ADC_KEY + "));";

	public AdcDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		L.d(ADC_CREATE_SCRIPT + " " + ADC_FORMULA_CREATE_SCRIPT);
		L.d("on create database called");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		L.d("on upgrade database called");
		db.execSQL("DROP TABLE IF EXISTS ADC;DROP TABLE IF EXISTS ADC_FORMULA");
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		L.d("on open database called");
		db.execSQL(ADC_CREATE_SCRIPT);
		db.execSQL(ADC_FORMULA_CREATE_SCRIPT);
	}

	public void openDB() {
		if (sqlDB == null) {
			sqlDB = getWritableDatabase();
		}
	}

	public void loadEntries() {
		Cursor c = sqlDB.query(ADC_TABLE_NAME, projection, null, null, null,
				null, null);
		adcCodes.clear();
		adcQuantities.clear();
		adcUnits.clear();
		c.moveToFirst();
		do {
			adcCodes.add(c.getString(c.getColumnIndex(ADC_SENSOR_CODE)));
			adcQuantities.add(c.getString(c.getColumnIndex(ADC_QUANTITY)));
			adcUnits.add(c.getString(c.getColumnIndex(ADC_UNIT)));
		} while (c.moveToNext());
	}

	public ArrayList<String> getAdcCodes() {
		return adcCodes;
	}

	public ArrayList<String> getAdcQuantities() {
		return adcQuantities;
	}

	public ArrayList<String> getAdcUnits() {
		return adcUnits;
	}

	public SQLiteDatabase getSqlDB() {
		return sqlDB;
	}

	public void test() {
		ContentValues values = new ContentValues();
		values.put(ADC_SENSOR_CODE, "lm-35");
		values.put(ADC_QUANTITY, "Temperature");
		values.put(ADC_UNIT, "Celsius");
		values.put(ADC_PIN_NUMBER, "P9_37");

		long newRowId;
		newRowId = sqlDB.insert(ADC_TABLE_NAME, null, values);

		ContentValues valuesFormula = new ContentValues();
		valuesFormula.put(ADC_FORMULA_NAME, "pin");
		valuesFormula.put(ADC_FORMULA_DISPLAY_NAME, "P9_37");
		valuesFormula.put(ADC_FORMULA_EXPRESSION, "pin");
		valuesFormula.put(ADC_FORMULA_DISPLAY_EXPRESSION, "pin");
		valuesFormula.put(ADC_FORMULA_VARIABLES, "");
		valuesFormula.put(ADC_FORMULA_SENSOR, newRowId);

		sqlDB.insert(ADC_FORMULA_TABLE_NAME, null, valuesFormula);

		valuesFormula = new ContentValues();
		valuesFormula.put(ADC_FORMULA_NAME, "Temperature");
		valuesFormula.put(ADC_FORMULA_EXPRESSION, "pin/10");
		valuesFormula.put(ADC_FORMULA_DISPLAY_NAME, "Temperature");
		valuesFormula.put(ADC_FORMULA_DISPLAY_EXPRESSION, "P9_37/10");
		valuesFormula.put(ADC_FORMULA_VARIABLES, "pin:");
		valuesFormula.put(ADC_FORMULA_SENSOR, newRowId);

		sqlDB.insert(ADC_FORMULA_TABLE_NAME, null, valuesFormula);

		values = new ContentValues();
		values.put(ADC_SENSOR_CODE, "lm-36");
		values.put(ADC_QUANTITY, "Temperature");
		values.put(ADC_UNIT, "Fahrenheit");
		values.put(ADC_PIN_NUMBER, "P9_37");

		newRowId = sqlDB.insert(ADC_TABLE_NAME, null, values);

		valuesFormula = new ContentValues();
		valuesFormula.put(ADC_FORMULA_NAME, "pin");
		valuesFormula.put(ADC_FORMULA_EXPRESSION, "pin");
		valuesFormula.put(ADC_FORMULA_DISPLAY_NAME, "P9_36");
		valuesFormula.put(ADC_FORMULA_DISPLAY_EXPRESSION, "pin");
		valuesFormula.put(ADC_FORMULA_VARIABLES, "");
		valuesFormula.put(ADC_FORMULA_SENSOR, newRowId);

		sqlDB.insert(ADC_FORMULA_TABLE_NAME, null, valuesFormula);

		valuesFormula = new ContentValues();
		valuesFormula.put(ADC_FORMULA_NAME, "Temp");
		valuesFormula.put(ADC_FORMULA_EXPRESSION, "(9*pin/50)+32");
		valuesFormula.put(ADC_FORMULA_DISPLAY_NAME, "Temp");
		valuesFormula.put(ADC_FORMULA_DISPLAY_EXPRESSION, "(9*P9_36/50)+32");
		valuesFormula.put(ADC_FORMULA_VARIABLES, "pin:");
		valuesFormula.put(ADC_FORMULA_SENSOR, newRowId);

		sqlDB.insert(ADC_FORMULA_TABLE_NAME, null, valuesFormula);

	}

	public Cursor getSensorsFor(String s) {
		Cursor c;
		String query = "SELECT * FROM " + ADC_TABLE_NAME + " WHERE "
				+ ADC_SENSOR_CODE + " LIKE '" + s + "%' OR " + ADC_QUANTITY
				+ " LIKE '" + s + "%';";
		L.d(query);
		c = sqlDB.rawQuery(query, null);
		L.d(c.toString());
		L.d(c.getCount());
		return c;
	}

}
