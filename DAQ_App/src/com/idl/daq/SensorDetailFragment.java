package com.idl.daq;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.daq.sensors.Sensor;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A fragment representing a single Sensor detail screen. This fragment is
 * either contained in a {@link SensorListActivity} in two-pane mode (on
 * tablets) or a {@link SensorDetailActivity} on handsets.
 */
public class SensorDetailFragment extends Fragment implements LoaderCallbacks<Void>{
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	ListView lv;
	Context c;
	Sensor mySensor;
	public static final String ARG_ITEM_ID = "item_id";
	ArrayList<String> data;// = new ArrayList<String>();
	ArrayAdapter<String> a;// = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,data);
	View rootView;
	private int count;
	GlobalState gS;

	ArrayList<String> t;
	/**
	 * The dummy content this fragment is presenting.
	 */
	//private DummyContent.DummyItem mItem;
	private String json;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SensorDetailFragment() {
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
//		if(a==null)
//		{
//			lv = (ListView) rootView.findViewById(R.id.dataList);
//			data = new ArrayList<String>();
//			a = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,data){
//
//		        @Override
//		        public View getView(int position, View convertView,
//		                ViewGroup parent) {
//		            View view =super.getView(position, convertView, parent);
//
//		            TextView textView=(TextView) view.findViewById(android.R.id.text1);
//
//		            /*YOUR CHOICE OF COLOR*/
//		            textView.setTextColor(Color.BLACK);
//
//		            return view;
//		        }
//		    };
//			lv.setAdapter(a);
//			TextView tw = (TextView) rootView.findViewById(R.id.textView1);
//			tw.setText(mySensor.getSensorName());
//		}
		count = 0;
		getLoaderManager().initLoader(0, null, this);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

//		if (getArguments().containsKey("JSON")) {
//			// Load the dummy content specified by the fragment
//			// arguments. In a real-world scenario, use a Loader
//			// to load content from a content provider.
//			json = getArguments().getString("JSON");
//			//mItem = DummyContent.ITEM_MAP.get(getArguments().getString(					ARG_ITEM_ID));
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		L.d("Oncreate view");
		rootView = inflater.inflate(R.layout.fragment_sensor_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		//if (json != null) {
//			lv = (ListView) rootView.findViewById(R.id.dataList);
//			data = new ArrayList<String>();
//			a = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,data);
//			lv.setAdapter(a);
//			TextView tw = (TextView) rootView.findViewById(R.id.textView1);
//			tw.setText(mySensor.getSensorName());
			
			lv = (ListView) rootView.findViewById(R.id.dataList);
			data = new ArrayList<String>();
			a = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,data){

		        @Override
		        public View getView(int position, View convertView,
		                ViewGroup parent) {
		            View view =super.getView(position, convertView, parent);

		            TextView textView=(TextView) view.findViewById(android.R.id.text1);

		            /*YOUR CHOICE OF COLOR*/
		            textView.setTextColor(Color.BLACK);

		            return view;
		        }
		    };
			lv.setAdapter(a);
//			lv.post(new Runnable(){
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					lv.setSelection(lv.getCount()-1);
//				}
//				
//			});
			TextView tw = (TextView) rootView.findViewById(R.id.textView1);
			tw.setText(mySensor.getSensorName());
		//}

		return rootView;
	}
	

	
	public void loadData(){
		L.d("Loading data");
//		ArrayList<String> data = new ArrayList<String>();
//		ArrayAdapter<String> a = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,data);
//		lv.setAdapter(a);
//		getLoaderManager().initLoader(0, null, this);
		
		
		t = gS.getTemp();
		for(int i=data.size();i<t.size();++i){
			data.add(t.get(i) + " "+mySensor.getSensorName());
		}
		a.notifyDataSetChanged();		
//		for(int i=0;i<100;++i){
//			try {
//				data.add(i + ": "+(new JSONObject(json).getString("sensorName")));
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//		}
	}

	public void setContext(Context c) {
		// TODO Auto-generated method stub
		this.c = c;
		gS = (GlobalState) c;
	}
	
	public void setSensor(Sensor s){
		mySensor = s;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		L.d("Fragment ka onDestroy called");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		L.d("Fragment ka onPause called");
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		L.d("Fragment ka onResume called");
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		L.d("Fragment ka onStart called");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		L.d("Fragment ka onStop called");
		super.onStop();
	}


	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(getActivity()) {

			@Override
			public Void loadInBackground() {
				try {
					// simulate some time consuming operation going on in the
					// background
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		// somehow the AsyncTaskLoader doesn't want to start its job without
		// calling this method
		loader.forceLoad();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
		// TODO Auto-generated method stub
//		data.add(count++ +" hi");
//		a.notifyDataSetChanged();
		loadData();
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
