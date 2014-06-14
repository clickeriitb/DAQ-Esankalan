package com.idl.daq;

import org.json.JSONException;
import org.json.JSONObject;

import com.idl.daq.USBEngine.USBCallback;
import com.daq.sensors.Sensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 * An activity representing a list of Sensors. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link SensorDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SensorListFragment} and the item details (if present) is a
 * {@link SensorDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link SensorListFragment.Callbacks} interface to listen for item selections.
 */
public class SensorListActivity extends ActionBarActivity implements
		SensorListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	private Sensor mySensor;
	private SensorDetailFragment mySFrag;
	private GlobalState gS;
	private USBEngine mEngine = null;
	private String currentFragment;
	
	//Action bar containing ADD, START, STOP 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.sensors_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}

//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//
//		if (findViewById(R.id.sensor_detail_container) != null) {
//			// The detail container view will be present only in the
//			// large-screen layouts (res/values-large and
//			// res/values-sw600dp). If this view is present, then the
//			// activity should be in two-pane mode.
//			mTwoPane = true;
//			L.d("Two Pane");
//			// In two-pane mode, list itemstouched should be given the
//			// 'activated' state when touched.
//			((SensorListFragment) getSupportFragmentManager().findFragmentById(
//					R.id.sensor_list)).setActivateOnItemClick(true);
//		}
//		else
//			L.d("Not two pane");
//
//		super.onStart();
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//layout in which details are shown at the side of item selected
		setContentView(R.layout.activity_sensor_twopane);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

//		if (findViewById(R.id.sensor_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
			currentFragment="";
			L.d("Two Pane");
			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((SensorListFragment) getSupportFragmentManager().findFragmentById(
					R.id.sensor_list)).setActivateOnItemClick(true);
			//L.d("handling intent action: " + intent.getAction());
//			
//			if (mEngine == null) {
//				mEngine = new USBEngine(getApplicationContext(), mCallback);
//			}
//			mEngine.onNewIntent();
			gS =(GlobalState) getApplicationContext();
			mEngine = gS.getUsb();
			
//		}
//		else
//			L.d("Not two pane");

		// TODO: If exposing deep links into your app, handle intents here.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		//if ADD button clicked(to add new sensor)
		else if(id==R.id.action_add){
			Intent i = new Intent(getApplicationContext(),SelectProtocol.class);
			startActivity(i);
			//gS.setExiting(false);
			//finish();
		} 
		
		else if(id==R.id.action_start){
//			if (mEngine != null) {
//				mEngine.write(mySensor.getJSON());
//				
//			}
//			if(mySFrag!=null)
//			mySFrag.loadData();
			L.d("Start");
			JSONObject json = new JSONObject();
			try {
				json.put("objId", "start");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mEngine.write(json);
			
		} else if(id==R.id.action_stop){
			L.d("Stop");
			JSONObject json = new JSONObject();
			try {
				json.put("objId", "stop");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mEngine.write(json);
		}
			return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link SensorListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(Sensor s,int position) {
		mySensor = s;
		mySFrag = mySensor.getDataFrag(getApplicationContext());
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
		//	if(!mySFrag.isVisible())
		//	{
				//Bundle arguments = new Bundle();
				//arguments.putString("JSON",s.getJSON().toString());
				//SensorDetailFragment fragment = s.getDataFrag(this);
				L.d(mySFrag.isAdded());
				L.d(mySFrag.isVisible());
				String tabId = s.getSensorName();
				//mySFrag.setArguments(arguments);
				L.d("Replacing fragment");
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction t = fm.beginTransaction();
				
//				if (fm.findFragmentByTag(tabId) == null) {
//					Log.e("update :", "replace"+tabId);
//					L.d("position : " + position);
//					if(position%2==0){
//					fm.beginTransaction()
//							.replace(R.id.sensor_detail_container, mySFrag, tabId)
//							.commit();
//					}else{
//						fm.beginTransaction()
//						.replace(R.id.sensor_detail_container2, mySFrag, tabId)
//						.commit();
//					}
//				}
				
				if(fm.findFragmentByTag(tabId) == null){
					t.add(R.id.sensor_detail_container,mySFrag, tabId);
					
				}
				if(!currentFragment.isEmpty()){
					L.d("Hiding " + currentFragment);
					t.hide(fm.findFragmentByTag(currentFragment));
				}
				t.show(mySFrag);
				currentFragment=tabId;
				t.commit();
//				FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//				t.replace(R.id.sensor_detail_container, mySFrag);
//				t.addToBackStack(null);
//				t.commit();
				L.d("Fragment Replaced");
		//	}
		}
//		} else {
//			// In single-pane mode, simply start the detail activity
//			// for the selected item ID.
//			Intent detailIntent = new Intent(this, SensorDetailActivity.class);
//			detailIntent.putExtra("JSON",s.getJSON().toString());
//			startActivity(detailIntent);
//		}
	}

	@Override
	public GlobalState getGlobalState() {
		// TODO Auto-generated method stub
		gS =(GlobalState) getApplicationContext();
		return gS;
	}
	
//	@Override
//	protected void onNewIntent(Intent intent) {
//		L.d("handling intent action: " + intent.getAction());
//		if (mEngine == null) {
//			mEngine = new AccessoryEngine(getApplicationContext(), mCallback);
//		}
//		mEngine.onNewIntent(intent);
//		super.onNewIntent(intent);
//	}
	
	private final USBCallback mCallback = new USBCallback() {
		@Override
		public void onDeviceDisconnected() {
			L.d("device physically disconnected");
		}

		@Override
		public void onConnectionEstablished() {
			L.d("device connected! ready to go!");
		}

		@Override
		public void onConnectionClosed() {
			L.d("connection closed");
		}

		@Override
		public void onDataRecieved(String message) {
			L.d("received message : %s", message);
		//	values.add("BBB : " + message);
		//	arrayAdapter.notifyDataSetChanged();
			//showToast(message);
//			for(int i=0;i<data.length;++i)
//			L.d("byte %d : %d ",i,data[i]);
		}

		@Override
		public void onNoUSB(Intent i) {
			// TODO Auto-generated method stub
			startActivity(i);
		}

		@Override
		public void startUSBInput() {
			// TODO Auto-generated method stub
			
		}
	};
	
//	@Override
//	protected void onDestroy() {
//		L.d("On destroy called");
//		//mEngine.onDestroy();
//		mEngine = null;
//		gS.clear();
//		L.d("Cleared");
//		super.onDestroy();
//	}
}
