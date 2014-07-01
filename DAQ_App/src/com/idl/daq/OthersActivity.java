package com.idl.daq;

import java.io.File;
import java.util.ArrayList;

import com.daq.sensors.OthersProc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class OthersActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	Button openFile;
	ArrayList<File> fileList;
	ArrayAdapter<File> fileAdapter;
	ListView fileView;
	OthersProc othersProc;
	EditText sensorName, quantity;

	GlobalState gS;

	boolean isOpen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.others_form);
		sensorName = (EditText) findViewById(R.id.others_name);
		quantity = (EditText) findViewById(R.id.others_quantity);
		openFile = (Button) findViewById(R.id.open_file);
		openFile.setOnClickListener(this);
		fileView = (ListView) findViewById(R.id.others_list);
		fileList = new ArrayList<File>();
		fileAdapter = new ArrayAdapter<File>(this,
				android.R.layout.simple_list_item_1, fileList);
		fileView.setAdapter(fileAdapter);
		fileView.setOnItemClickListener(this);
		isOpen = false;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!isOpen) {
			addFile(Environment.getExternalStorageDirectory());
			isOpen = true;
		}
	}

	public void addFile(File f) {
		if (f.isDirectory()) {
			File[] file = f.listFiles();
			if (file != null && file.length > 0) {
				for (File temp : file) {
					addFile(temp);
				}
			}
		} else {
			if (f.getAbsolutePath().endsWith(".py")) {
				fileList.add(f);
				fileAdapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		File file = fileList.get(position);
		if (sensorName.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter Sensor Name",
					Toast.LENGTH_SHORT).show();
		} else if (quantity.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter Quantity Name",
					Toast.LENGTH_SHORT).show();
		} else {
			othersProc = new OthersProc();
			othersProc.setF(file);
			othersProc.setSensorName(sensorName.getText().toString());
			othersProc.setQuantity(quantity.getText().toString());

			gS = (GlobalState) getApplicationContext();
			gS.addSensor(othersProc);
			Intent i = new Intent(getApplicationContext(),
					SensorListActivity.class);
			startActivity(i);

		}

	}

	// private void showChooser() {
	// // TODO Auto-generated method stub
	// File[] file = Environment.getExternalStorageDirectory().listFiles();
	//
	// for (File f : file) {
	// if (f.isFile() && f.getPath().endsWith("ui.py")) {
	// // Read text from file
	// StringBuilder text = new StringBuilder();
	//
	// try {
	// BufferedReader br = new BufferedReader(new FileReader(f));
	// String line;
	//
	// while ((line = br.readLine()) != null) {
	// text.append(line);
	// text.append('\n');
	// }
	// JSONObject json = new JSONObject();
	// json.put("code", text.toString());
	// json.put("sensor_code", "LM-35");
	//
	// gs.socket.emit("receive", json);
	// Toast.makeText(getApplicationContext(), text.toString(),
	// Toast.LENGTH_LONG).show();
	// } catch (Exception e) {
	// System.out.println(e.toString());
	// }
	// }
	// }
	// }

}
