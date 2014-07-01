package com.idl.daq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.Formula;
import com.daq.sensors.I2CProc;
import com.daq.sensors.Sensor;
import com.idl.daq.SensorBrowseFragment.Callbacks;

import android.app.Activity;
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
	
	

	ArrayList<JSONObject> t;
	/**
	 * The dummy content this fragment is presenting.
	 */
	//private DummyContent.DummyItem mItem;
	private String json;
	private Callbacks detailCallbacks;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SensorDetailFragment() {
	}

	public interface Callbacks {
		
		public Context getContext();
		
		public Sensor getSensor();
		
		public void sendArrayWithAdapter(ArrayAdapter<String> a,ArrayList<String> data);
		
	}
	
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		detailCallbacks = (Callbacks) activity;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(false);
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
		//getLoaderManager().initLoader(0, null, this);
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
		rootView = inflater.inflate(R.layout.raw,
				container, false);
		gS = (GlobalState) detailCallbacks.getContext();
		mySensor = detailCallbacks.getSensor();
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
			a = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data){
				@Override
		        public View getView(int position, View convertView,
		                ViewGroup parent) {
		            View view =super.getView(position, convertView, parent);

		            TextView textView=(TextView) view.findViewById(android.R.id.text1);
		            String temp = textView.getText().toString();
		            if(!gS.getProtocol().equals("Others")){
						String[] val = temp.split(" ");
						String[] val2 = val[0].split(":");
						double d = Double.parseDouble(val2[1]);
						/* YOUR CHOICE OF COLOR */
						if (d < mySensor.getMinThresh()) {
							textView.setTextColor(Color.BLUE);
						} else if (d > mySensor.getMaxThresh()) {
							textView.setTextColor(Color.RED);
						} else {
							textView.setTextColor(Color.BLACK);
						}
		            }
		            return view;
		        }
		    };
			lv.setAdapter(a);
			detailCallbacks.sendArrayWithAdapter(a,data);
			L.d("Sent adapter");
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
	

	
//	public void loadData(){
//		L.d("Loading data");
////		ArrayList<String> data = new ArrayList<String>();
////		ArrayAdapter<String> a = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,data);
////		lv.setAdapter(a);
////		getLoaderManager().initLoader(0, null, this);
//		
//		
//		t = gS.getTemp();
//		for(int i=data.size();i<t.size();++i){
//			try {
//				Formula f;
//				if(t.get(i).get("sensor_code").equals(mySensor.getSensorName())){
//<<<<<<< HEAD
//					
//				
//					String date = t.get(i).getString("date");
//					//String[] r = info.split(":");
//					//L.d(r.length);
//					L.d("date: "+date);
//					
//					L.d("value of formula "+f.getValue());
//					//f.getAllVariables().get(0).setValue(t.get(i).getDouble("data"));
//=======
//					String value = t.get(i).getString("data");
//					String date = t.get(i).getString("date");
//					//String[] r = info.split(":");
//					//L.d(r.length);
//					L.d(value+" "+date);
//					if(mySensor.getFormulaContainer()!=null){
//						if(mySensor instanceof I2CProc){
//							String dataList[] = t.get(i).getString("data").split(":");
//							String register = dataList[0];
//							double val = Double.parseDouble(dataList[1]);
//							f=null;
//							for(Map.Entry<String, Formula> e : mySensor.getFormulaContainer().getFc().entrySet()){
//								if(e.getValue().getDisplayName().equals(register)){
//									f = e.getValue();
//									break;
//								}
//							}
//							f.setValue(val);
//							checkEmptyRegisters.put(register, false);
//						}else{
//							f = mySensor.getFormulaContainer().getFc().get("pin");
//							String value = t.get(i).getString("data");
//							Double d = Double.parseDouble(value);
//							f.setValue(d);
//							checkEmptyRegisters.put("pin", false);
//						}
//						Formula f = mySensor.getFormulaContainer().getFc().get("pin");
//						if(f==null){
//							L.d("I told ya!");
//						}
//						f.setValue(Double.parseDouble(value));
//						f.getAllVariables().get(0).setValue(t.get(i).getDouble("data"));
//						mySensor.getFormulaContainer().evaluate();
//						String s="";
//						for(Map.Entry<String, Formula> e : mySensor.getFormulaContainer().getFc().entrySet()){
//							s=e.getValue().getValue()+"";
//						}
//						value = s;
//					}
//					
//>>>>>>> a851541d888323b9023903dbce9c1673b1f2d22c
////					try {
////						f.setValue(t.get(i).getDouble("data"));
////					} catch (Exception e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
//<<<<<<< HEAD
//					
//					mySensor.getFormulaContainer().evaluate();
//					String s="";
//					
//					for(Map.Entry<String, Formula> e : mySensor.getFormulaContainer().getFc().entrySet()){
//						L.d("formula jkdjflkjsdalkjf "+e.getValue().toString());
//						s=e.getValue().getValue()+"";
//					}
//					L.d(s);
//					
//					data.add(mySensor.getId()+":"+s+" Time:"+date);
//					if(mySensor instanceof I2CProc){
//						for(I2C_ItemClass i2c : ((I2CProc) mySensor).getExecList()){
//							checkEmptyRegisters.put(i2c.getAddr(),true);
//						}
//					}else{
//						checkEmptyRegisters.put("pin",true);
//					}
//					//data.add(t.get(i).getDouble("data")+"");
//					}
//=======
//					data.add(mySensor.getSensorName()+":"+value+" Time:"+date);
//>>>>>>> a851541d888323b9023903dbce9c1673b1f2d22c
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		a.notifyDataSetChanged();		
////		for(int i=0;i<100;++i){
////			try {
////				data.add(i + ": "+(new JSONObject(json).getString("sensorName")));
////			} catch (JSONException e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			}
////			try {
////				Thread.sleep(1000);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
////		}
//	}

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
//		loadData();
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
