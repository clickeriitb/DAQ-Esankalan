package com.idl.daq;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;







import com.jjoe64.graphview.GraphView;

import android.content.Context;
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

public class DataFragment extends Fragment implements LoaderCallbacks<Void>{
	
	ListView lv;
	Context c;
	ArrayList<String> data;// = new ArrayList<String>();
	ArrayAdapter<String> a;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.data, container, false);
		
		lv = (ListView) rootView.findViewById(R.id.dataList);
		data = new ArrayList<String>();
		a = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data);
		lv.setAdapter(a);
		TextView tw = (TextView) rootView.findViewById(R.id.sensor_name);
		tw.setText("LM_35");
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	        	if(isAdded()){
	            getActivity().runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                	data.add(Double.toString(Math.random()));
	                    //Here I try few different things
	                	a.notifyDataSetChanged();
	                    
	                }
	            });
	        }
	        	else {
	        		Log.d("data", "cant display data");
	        	}
	        }
	    }, 0, 100);
		
		a.notifyDataSetChanged();
		
		return rootView;
	}
	
	public void loadData(){
		Log.d("msg","Loading data");
		int t = 150;
		for(int i=data.size();i<t;++i){
			try {
				
				/*if(t.get(i).get("sensor_code").equals(mySensor.getSensorName())){
					Formula f = mySensor.getFormula().getFc().get("temperature");
					String info = t.get(i).getString("data");
					String[] r = info.split(":");
					f.setValue(Double.parseDouble(r[0]));
					//f.getAllVariables().get(0).setValue(t.get(i).getDouble("data"));
					gS.getfc().evaluate();
					String s="";
					for(Map.Entry<String, Formula> e : gS.getfc().getFc().entrySet()){
						s=e.getKey()+": "+e.getValue().getValue()+" ";
					}
					L.d(s); */
					//data.add(mySensor.getSensorName() + ": " +info+" "+s);
					data.add(Double.toString(Math.random()));
				}
			/*catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			catch(Exception e){
				e.printStackTrace();
			}
		}
		a.notifyDataSetChanged();
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
		loadData();
		getLoaderManager().restartLoader(0, null, this);
		
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		
		L.d("on detach ffsdlkdkljgkljdlskfjklajslkdjlkf");
	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
}

