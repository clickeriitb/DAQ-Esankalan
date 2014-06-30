package com.idl.daq;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.idl.daq.SensorBrowseFragment.Callbacks;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class GraphFragment extends Fragment {
	
	GraphViewData[] data;
	public GraphViewSeries series;
	private double TIME = 0;
	Context thiscontext;
	private GlobalState gS;
	Callbacks graphCallbacks;
	View rootView;
	public interface Callbacks {
		
		public void sendSeries(GraphViewSeries series);
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		graphCallbacks = (Callbacks) activity;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.graph, container, false);
		thiscontext = container.getContext();
		
		//btn = (Button)rootView.findViewById(R.id.button);
		//a new series has been generated here 
		series = new GraphViewSeries(new GraphView.GraphViewData[] { new GraphView.GraphViewData(TIME, 0) });
		GraphView graphView = new LineGraphView(
		    thiscontext
		    , "GraphView of sensor data"
		);
	
		graphView.addSeries(series);
		// set view port, start=2, size=40
		graphView.setViewPort(1, 10);
		graphView.setScrollable(true);
		// optional - activate scaling / zooming
		graphView.setScalable(true);
		graphView.getGraphViewStyle().setGridColor(Color.LTGRAY);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.RED);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);

		 
		LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.graph);
		layout.addView(graphView);
		//graph 
		graphCallbacks.sendSeries(series);
		L.d("sent series");	
		return rootView;
	}
	
	public void createGraph(){
		series = new GraphViewSeries(new GraphView.GraphViewData[] { new GraphView.GraphViewData(TIME, 0) });
		GraphView graphView = new LineGraphView(
		    thiscontext
		    , "GraphView of sensor data"
		);
	
		graphView.addSeries(series);
		// set view port, start=2, size=40
		graphView.setViewPort(1, 10);
		graphView.setScrollable(true);
		// optional - activate scaling / zooming
		graphView.setScalable(true);
		graphView.getGraphViewStyle().setGridColor(Color.LTGRAY);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.RED);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);

		 
		LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.graph);
		layout.addView(graphView);
	}
	

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		
		L.d("On detach called");
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
