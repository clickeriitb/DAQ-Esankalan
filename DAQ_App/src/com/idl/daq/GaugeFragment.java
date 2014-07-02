package com.idl.daq;

import java.util.Random;

import com.idl.daq.SensorDetailFragment.Callbacks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class GaugeFragment extends Fragment {

	private GaugeView mGaugeView1;
	private GaugeView mGaugeView2;
	private final Random RAND = new Random();
	//Change these values to get your required minimum and maximum
	float min=25.0f;
	float max=35.0f;
	//Change the position for the minimum and maximum on the dial with respect to 100
	float start=20.0f;
	float end=80.0f;
	
	public GaugeData gData;
	
	View rootView;
	
	private Callbacks gaugeCallbacks;
	
	
	public interface Callbacks {
		public Context getContext();
		
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		gaugeCallbacks = (Callbacks) activity;
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
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_gauge, container,false);
		gData = new GaugeData();
		mGaugeView1 = (GaugeView) rootView.findViewById(R.id.gauge_view1);
		mGaugeView2 = (GaugeView) rootView.findViewById(R.id.gauge_view2);
		mGaugeView1.setMinMaxWithPosition(min, max,start,end);
		mTimer.start();
		return rootView;
	}

	int i=11;
	int g;
	
	private final CountDownTimer mTimer = new CountDownTimer(1000000000, 1000) {

		@Override
		public void onTick(final long millisUntilFinished) {
		
			//gS.setDemo(30.0);
			g=(int) gData.getData();
			//mGaugeView1.setTargetValue(RAND.nextInt(101));
			//mGaugeView2.setTargetValue(RAND.nextInt(101));
			/*if(i<min)
			mGaugeView2.setBackgroundColor(0x5000ff00);
			else if(i>=min && i<=max)
			mGaugeView2.setBackgroundColor(0x00000000);
			else
			mGaugeView2.setBackgroundColor(0x50ff0000);	
			mGaugeView1.setTargetValue(start+(i-min)*((end-start)/(max-min)));
			mGaugeView2.setTargetValue(i);
			i=i+2;*/
			if(g<min)
			mGaugeView2.setBackgroundColor(0x5000ff00);
			else if(g>=min && g<=max)
			mGaugeView2.setBackgroundColor(0x00000000);
			else
			mGaugeView2.setBackgroundColor(0x50ff0000);	
			mGaugeView1.setTargetValue(start+(g-min)*((end-start)/(max-min)));
			mGaugeView2.setTargetValue(g);
		}
			
		

		@Override
		public void onFinish() {}
	};
}
